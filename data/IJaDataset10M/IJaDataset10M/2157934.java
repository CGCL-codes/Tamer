package vavi.util.archive.rar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import vavi.util.Debug;
import vavi.util.archive.ComArchive;
import vavi.util.archive.Entry;

/**
 * RAR �A�[�J�C�u����������T�[�r�X�v���o�C�_�ł��D
 * (COM �o�[�W����)
 * 
 * @target 1.1
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030211 nsano initial version <br>
 */
public class ComRarArchive extends ComArchive {

    /** */
    public ComRarArchive(File file) throws IOException {
        super(file, TYPE_RAR);
    }

    /** */
    private static final MessageFormat commandLineBase = new MessageFormat("x -o -q \"{0}\" \"{1}\" \"{2}\"");

    /** */
    protected String getCommandString(Entry entry) {
        String commandLine = commandLineBase.format(new Object[] { file.getPath(), System.getProperty("java.io.tmpdir"), entry.getName() });
        Debug.println("commandLine: " + commandLine);
        return commandLine;
    }

    /** */
    protected String getTemporaryFileName(Entry entry) {
        return System.getProperty("java.io.tmpdir") + entry.getName();
    }

    /** */
    public static void main(String[] args) throws Exception {
        ComRarArchive rar = new ComRarArchive(new File(args[0]));
        Entry entry = rar.getEntry(args[1]);
        System.err.println("entry: " + entry);
        InputStream is = rar.getInputStream(entry);
        System.err.println("is: " + is);
    }
}
