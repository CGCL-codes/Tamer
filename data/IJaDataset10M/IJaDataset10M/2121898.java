package com.nullfish.app.jfd2.command.embed;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import jp.gr.java_conf.dangan.util.lha.LhaHeader;
import jp.gr.java_conf.dangan.util.lha.LhaOutputStream;
import jp.hishidama.zip.ZipEntry;
import jp.hishidama.zip.ZipOutputStream;
import com.nullfish.app.jfd2.JFD;
import com.nullfish.app.jfd2.JFDModel;
import com.nullfish.app.jfd2.command.Command;
import com.nullfish.app.jfd2.dialog.ConfigurationInfo;
import com.nullfish.app.jfd2.dialog.DialogUtilities;
import com.nullfish.app.jfd2.dialog.JFDDialog;
import com.nullfish.app.jfd2.resource.JFDResource;
import com.nullfish.lib.ui.Choice;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

/**
 * 圧縮コマンド
 * 
 * @author shunji
 */
public class PackCommand extends Command {

    public static final String ARCHIVE_NAME = "archive";

    public static final String ARCHIVE_PASSWORD = "password";

    public static final String FORMAT = "format";

    public static final String ZIP = "zip";

    public static final String LHA = "lha";

    private VFile workingFile;

    public void doExecute() throws VFSException {
        JFD jfd = getJFD();
        JFDModel model = jfd.getModel();
        VFile selectedFile = model.getSelectedFile();
        VFile[] markedFiles = model.getMarkedFiles();
        if (markedFiles == null || markedFiles.length == 0) {
            markedFiles = new VFile[1];
            markedFiles[0] = selectedFile;
        }
        JFDDialog dialog = null;
        try {
            dialog = jfd.createDialog();
            dialog.setTitle(JFDResource.LABELS.getString("title_pack"));
            dialog.addMessage(JFDResource.MESSAGES.getString("message_pack"));
            dialog.addButton(JFDDialog.OK, JFDResource.LABELS.getString("ok"), 'o', true);
            dialog.addButton(JFDDialog.CANCEL, JFDResource.LABELS.getString("cancel"), 'c', false);
            dialog.addTextField(ARCHIVE_NAME, selectedFile.getName(), true);
            dialog.addTextField(ARCHIVE_PASSWORD, "", true, JFDResource.LABELS.getString("zip_password"));
            Choice[] formats = { new Choice(ZIP, "ZIP", 'z'), new Choice(LHA, "LHA", 'l') };
            dialog.addChooser(FORMAT, "", formats, 2, ZIP, new ConfigurationInfo(jfd.getCommonConfiguration(), "pack_format"), false);
            dialog.pack();
            dialog.setVisible(true);
            String answer = dialog.getButtonAnswer();
            if (answer == null || JFDDialog.CANCEL.equals(answer)) {
                return;
            }
            String archiveName = dialog.getTextFieldAnswer(ARCHIVE_NAME);
            VFile target = null;
            try {
                target = VFS.getInstance().getFile(archiveName);
            } catch (Exception e) {
            }
            if (target == null) {
                target = currentDir.getRelativeFile(archiveName);
            }
            if (target == null) {
                return;
            }
            dialog.applyConfig();
            jfd.setPrimaryCommand(this);
            if (dialog.getChooserAnswer(FORMAT).equals(ZIP)) {
                if (!"zip".equals(target.getFileName().getLowerExtension())) {
                    target = VFS.getInstance().getFile(target.getAbsolutePath() + ".zip");
                }
                if (target.exists(this) && target.isDirectory()) {
                    String[] messages = { JFDResource.MESSAGES.getString("wrong_path"), archiveName };
                    DialogUtilities.showMessageDialog(jfd, messages, JFDResource.LABELS.getString("title_pack"));
                    return;
                }
                ZipOutputStream zos = null;
                try {
                    zos = new ZipOutputStream(target.getOutputStream(this));
                    String encoding = (String) jfd.getCommonConfiguration().getParam("zip_pack_encoding", System.getProperty("file.encoding"));
                    zos.setEncoding(encoding);
                    String password = dialog.getTextFieldAnswer(ARCHIVE_PASSWORD);
                    if (password.length() > 0) {
                        zos.setPassword(password.getBytes(encoding));
                    }
                    for (int i = 0; i < markedFiles.length; i++) {
                        packZip(zos, markedFiles[i], currentDir);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        zos.flush();
                    } catch (Exception e) {
                    }
                    try {
                        zos.close();
                    } catch (Exception e) {
                    }
                }
            } else {
                if (!"lzh".equals(target.getFileName().getLowerExtension())) {
                    target = VFS.getInstance().getFile(target.getAbsolutePath() + ".lzh");
                }
                if (target.exists(this) && target.isDirectory()) {
                    String[] messages = { JFDResource.MESSAGES.getString("wrong_path"), archiveName };
                    DialogUtilities.showMessageDialog(jfd, messages, JFDResource.LABELS.getString("title_pack"));
                    return;
                }
                LhaOutputStream los = null;
                try {
                    los = new LhaOutputStream(target.getOutputStream(this));
                    for (int i = 0; i < markedFiles.length; i++) {
                        packLha(los, markedFiles[i], currentDir);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        los.flush();
                    } catch (Exception e) {
                    }
                    try {
                        los.close();
                    } catch (Exception e) {
                    }
                }
            }
        } finally {
            if (dialog != null) {
                dialog.dispose();
            }
        }
    }

    private void packZip(ZipOutputStream zos, VFile file, VFile baseDir) throws VFSException, IOException {
        if (file.isDirectory(this)) {
            ZipEntry entry = new ZipEntry(baseDir.getRelation(file) + "/");
            zos.putNextEntry(entry);
            zos.closeEntry();
            VFile[] children = file.getChildren(this);
            for (int i = 0; i < children.length; i++) {
                packZip(zos, children[i], baseDir);
            }
        } else {
            ZipEntry entry = new ZipEntry(baseDir.getRelation(file));
            zos.putNextEntry(entry);
            outputFile(zos, file);
            zos.closeEntry();
        }
    }

    private void packLha(LhaOutputStream los, VFile file, VFile baseDir) throws VFSException, IOException {
        workingFile = file;
        if (file.isDirectory(this)) {
            VFile[] children = file.getChildren(this);
            for (int i = 0; i < children.length; i++) {
                packLha(los, children[i], baseDir);
            }
        } else {
            LhaHeader entry = new LhaHeader(baseDir.getRelation(file).replaceAll("/", "\\\\"));
            los.putNextEntry(entry);
            outputFile(los, file);
            los.closeEntry();
        }
    }

    private void outputFile(OutputStream os, VFile file) throws IOException, VFSException {
        workingFile = file;
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(file.getInputStream(this));
            byte[] buffer = new byte[4096];
            int l = 0;
            while (true) {
                l = bis.read(buffer);
                if (l <= 0) {
                    return;
                }
                os.write(buffer, 0, l);
            }
        } finally {
            try {
                bis.close();
            } catch (Exception e) {
            }
            try {
                os.flush();
            } catch (Exception e) {
            }
        }
    }

    /**
	 * 作業経過メッセージを取得する。
	 * 
	 * @return
	 */
    public String getProgressMessage() {
        VFile file = workingFile;
        if (file == null) {
            return "";
        }
        Object[] param = { workingFile.getName() };
        return MessageFormat.format(JFDResource.LABELS.getString("packing"), param);
    }

    public FileSystem[] getUsingFileSystems() {
        if (currentDir.isRoot() && currentDir.getFileSystem().getMountPoint() != null) {
            FileSystem[] rtn = { currentDir.getFileSystem(), currentDir.getFileSystem().getMountPoint().getFileSystem() };
            return rtn;
        }
        return null;
    }

    public boolean closesUnusingFileSystem() {
        return false;
    }
}
