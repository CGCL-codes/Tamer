package org.stanwood.media.store.mp4.atomicparsley;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.stanwood.media.logging.LoggerOutputStream;
import org.stanwood.media.store.mp4.IAtom;
import org.stanwood.media.store.mp4.IMP4Manager;
import org.stanwood.media.store.mp4.MP4ArtworkType;
import org.stanwood.media.store.mp4.MP4AtomKey;
import org.stanwood.media.store.mp4.MP4Exception;
import org.stanwood.media.store.mp4.MP4ITunesStore;
import org.stanwood.media.util.FileHelper;
import org.stanwood.media.util.NativeHelper;
import org.stanwood.media.util.Platform;

public class MP4AtomicParsleyManager implements IMP4Manager {

    private static final Log log = LogFactory.getLog(MP4AtomicParsleyManager.class);

    private static final Pattern TAG_LIST_PATTERN = Pattern.compile("^Atom \"(.+?)\" contains\\: (.+)$");

    private static final Pattern RANGE_PATTERN = Pattern.compile("(\\d+) of (\\d+)", Pattern.CASE_INSENSITIVE);

    private static final Pattern ARTWORK_PATTERN = Pattern.compile("(\\d+) .*of artwork");

    private static final Pattern DNS_PATTERN = Pattern.compile("^Atom \"----\" \\[(.+?);(.+?)\\] contains\\: (.+)$");

    private String apCmdPath;

    /**
	 * This checks that the stores system commands can be found before the store is used.
	 * @throws MP4Exception Thrown if their is a problem locating the commands
	 */
    @Override
    public void init(File nativeDir) throws MP4Exception {
        if (apCmdPath == null) {
            apCmdPath = NativeHelper.getNativeApplication(nativeDir, "AtomicParsley");
        }
        boolean errors = false;
        if (!checkCommand(apCmdPath)) {
            log.error(MessageFormat.format("Unable to execute command {0}", apCmdPath));
            errors = true;
        }
        if (!checkCommandVersion()) {
            log.error(MessageFormat.format("Unable to find or execute command ''{0}''.", apCmdPath));
            errors = true;
        }
        if (errors) {
            throw new MP4Exception("Required system command not found");
        }
    }

    private static class AtomResult {

        MP4AtomKey key;

        String value;
    }

    @Override
    public List<IAtom> listAtoms(File mp4File) throws MP4Exception {
        if (!mp4File.exists()) {
            throw new MP4Exception(MessageFormat.format("Unable to find mp4 file {0}", mp4File));
        }
        String output = getCommandOutput(true, false, true, apCmdPath, mp4File, "--outputXML");
        AtomicParsleyOutputParser parser = new AtomicParsleyOutputParser(output);
        return parser.listAtoms();
    }

    private boolean hasArtwrokAtom(List<IAtom> atoms) {
        for (IAtom atom : atoms) {
            if (atom.getKey() == MP4AtomKey.ARTWORK) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(File mp4File, List<IAtom> atoms) throws MP4Exception {
        if (log.isDebugEnabled()) {
            log.debug("Upadting MP4 file '" + mp4File + "' with " + atoms.size());
        }
        List<IAtom> exsitingAtoms = listAtoms(mp4File);
        Iterator<IAtom> it = atoms.iterator();
        while (it.hasNext()) {
            IAtom atom = it.next();
            if (hasAtom(exsitingAtoms, atom)) {
                it.remove();
            }
        }
        List<Object> args = new ArrayList<Object>();
        args.add(mp4File);
        args.add("--output");
        File tempFile;
        try {
            tempFile = FileHelper.createTempFile("tempmedia", ".mp4");
            args.add(tempFile);
        } catch (IOException e) {
            throw new MP4Exception("Unable to create temp file", e);
        }
        if (hasArtwrokAtom(atoms)) {
            args.add("--artwork");
            args.add("REMOVE_ALL");
        }
        for (IAtom atom : atoms) {
            if (atom.getKey().getDnsName() != null) {
                args.add("--manualAtomRemove");
                args.add("moov.udta.meta.ilst.----.name:[" + atom.getKey().getDnsName() + "]");
            }
        }
        for (IAtom atom : atoms) {
            ((AbstractAPAtom) atom).writeAtom(mp4File, true, args);
        }
        getCommandOutput(true, false, true, apCmdPath, args.toArray(new Object[args.size()]));
        if (tempFile.exists() && tempFile.length() > 0) {
            try {
                FileHelper.delete(mp4File);
                FileHelper.move(tempFile, mp4File);
            } catch (IOException e) {
                throw new MP4Exception(MessageFormat.format("Unable to move file ''{0}'' to ''{1}''", tempFile, mp4File), e);
            }
        } else {
            throw new MP4Exception(MessageFormat.format("Unable to update MP4 metadata of file ''{0}''", mp4File));
        }
        for (IAtom atom : atoms) {
            ((AbstractAPAtom) atom).cleanup();
        }
        if (log.isDebugEnabled()) {
            log.debug("MP4 modified '" + mp4File + "'");
        }
    }

    private boolean hasAtom(List<IAtom> atoms, IAtom atom1) {
        for (IAtom atom2 : atoms) {
            if (atom1.getKey() == MP4AtomKey.PURCHASED_DATE) {
                if (atom2.getKey().equals(atom1.getKey())) {
                    return true;
                }
            } else {
                if (atom2.getKey().equals(atom1.getKey()) && atom2.toString().equals(atom1.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public IAtom createAtom(MP4AtomKey name, String value) {
        return new APAtomString(name, value);
    }

    /** {@inheritDoc} */
    @Override
    public IAtom createAtom(MP4AtomKey name, boolean value) {
        return new APAtomBoolean(name, value);
    }

    /** {@inheritDoc} */
    @Override
    public IAtom createAtom(MP4AtomKey name, int value) {
        return new APAtomNumber(name, value);
    }

    /** {@inheritDoc} */
    @Override
    public IAtom createAtom(MP4AtomKey name, short number, short total) {
        return new APAtomRange(name, number, total);
    }

    /** {@inheritDoc} */
    @Override
    public IAtom createAtom(MP4AtomKey name, MP4ArtworkType type, int size, byte data[]) {
        return new APAtomArtwork(name, type, size, data);
    }

    /**
	 * <p>Used to set the managers parameters.</p>
	 * <p>This manager has following optional parameters:
	 * 	<ul>
	 * 		<li>atomicparsley - The path to the AtomicParsley command</li>
	 *  </ul>
	 * </p>
	 * @param key The name of the parameter
	 * @param value The value of the parameter
	 */
    @Override
    public void setParameter(String key, String value) {
        if (key.equalsIgnoreCase("atomicparsley")) {
            apCmdPath = value;
        }
    }

    String getCommandOutput(boolean captureStdout, boolean captureStderr, boolean failOnExitCode, String command, Object... args) throws MP4Exception {
        CommandLine cmdLine = new CommandLine(command);
        for (Object arg : args) {
            if (arg instanceof File) {
                cmdLine.addArgument(((File) arg).getAbsolutePath(), false);
            } else if (arg instanceof String) {
                if (Platform.isWindows()) {
                    String a = ((String) arg).replaceAll("\"", Matcher.quoteReplacement("\\\""));
                    cmdLine.addArgument(a, false);
                } else {
                    String a = (String) arg;
                    cmdLine.addArgument(a, false);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("About to execute: " + cmdLine.toString());
        }
        Executor exec = new DefaultExecutor();
        exec.setExitValues(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        try {
            ByteArrayOutputStream capture = new ByteArrayOutputStream();
            OutputStream out = new LoggerOutputStream(Level.INFO);
            if (captureStdout) {
                out = capture;
            }
            OutputStream err = new LoggerOutputStream(Level.ERROR);
            if (captureStderr) {
                err = capture;
            }
            exec.setStreamHandler(new PumpStreamHandler(out, err));
            try {
                int exitCode = exec.execute(cmdLine);
                if (failOnExitCode && exitCode != 0) {
                    log.error(capture.toString());
                    throw new MP4Exception(MessageFormat.format("System command returned a non zero exit code ''{0}'': {1}", exitCode, cmdLine.toString()));
                }
            } catch (ExecuteException e) {
                log.error(capture.toString());
                throw new MP4Exception(MessageFormat.format("Unable to execute system command: {0}", cmdLine.toString()), e);
            }
            return capture.toString();
        } catch (IOException e) {
            throw new MP4Exception(MessageFormat.format("Unable to execute system command: {0}", cmdLine.toString()), e);
        }
    }

    private boolean checkCommandVersion() {
        boolean extended = false;
        try {
            String output = getCommandOutput(true, true, false, apCmdPath);
            if (output.contains("--longdesc") && output.contains("--flavour")) {
                extended = true;
            }
        } catch (MP4Exception e) {
            return false;
        }
        if (!extended) {
            log.error(MessageFormat.format("The found version of ''AtomicParsley'' application does not support setting some mp4 box types that are needed. This means only a limited set of meta data can be written to mp4/m4v files. The documentation for the ''{0}'' gives details on downloading a newer version and using that instead.", MP4ITunesStore.class.getName()));
            return false;
        }
        return true;
    }

    private boolean checkCommand(String cmd) {
        try {
            boolean capture = !log.isDebugEnabled();
            getCommandOutput(capture, capture, false, cmd);
        } catch (MP4Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Command failed", e);
            }
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public File getArtworkFile(URL imageUrl) throws IOException {
        File tmp = downloadToTempFile(imageUrl, ".tmp");
        if (isAtomicParsleyJpeg(tmp)) {
            return tmp;
        } else {
            File jpegFile = convertImageToJpeg(tmp);
            if (!isAtomicParsleyJpeg(jpegFile)) {
                throw new IOException(MessageFormat.format("Unable to convert image {0} to a valid AtomicParsley image", imageUrl.toExternalForm()));
            }
            FileHelper.delete(tmp);
            return jpegFile;
        }
    }

    private boolean isAtomicParsleyJpeg(File imageFile) throws IOException {
        byte[] data = new byte[4];
        InputStream inStream = null;
        try {
            inStream = new FileInputStream(imageFile);
            inStream.read(data);
            if (data[0] == (byte) 0xFF && data[1] == (byte) 0xD8 && data[2] == (byte) 0xFF) {
                if (data[3] == (byte) 0xE0 || data[3] == (byte) 0xE1) {
                    return true;
                }
            }
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
        return false;
    }

    private static File convertImageToJpeg(File artworkFile) throws IOException {
        File jpgArtwork = FileHelper.createTempFile("artwork", ".jpg");
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Convert downloaded image {0} into jpg {1}", artworkFile, jpgArtwork));
        }
        BufferedImage bufferedImage = ImageIO.read(artworkFile);
        ImageIO.write(bufferedImage, "jpg", jpgArtwork);
        return jpgArtwork;
    }

    private static File downloadToTempFile(URL url, String extension) throws IOException {
        File file = FileHelper.createTempFile("artwork", extension);
        if (!file.delete()) {
            throw new IOException(MessageFormat.format("Unable to delete temp file {0}", file.getAbsolutePath()));
        }
        FileHelper.copy(url, file);
        return file;
    }
}
