package jp.ne.nifty.iga.midori.shell.dir;

import jp.ne.nifty.iga.midori.shell.eng.*;
import gnu.regexp.*;
import java.io.*;

/**
 * JMichelle (Java Midori Shell) : File Factory.
 *
 * $Revision: 1.3 $
 */
public class MdShellDirFileFactory {

    static RE urlProtocolRE;

    static {
        try {
            urlProtocolRE = new RE("^[A-Za-z0-9+.]+:.*$");
        } catch (REException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Get file object(abstracted).
	 *
	 * @param name target file name.
	 * @param mode file open mode:
	 * <ul>
	 *   <li> "r": read only
	 *   <li> "w": write(if not exist, create one)
	 * </ul>
	 * @param env shell environment.
	 * @return file object(abstracted).
	 */
    public static MdShellDirFileAbstract get(String name, String mode, MdShellEnv env) throws MdShellException {
        MdShellDirFileAbstract result = null;
        String target = name;
        String current = env.getCurrentDirectory().getName();
        if (name.startsWith("~")) {
            target = "file:" + (String) env.getShellVariable("home");
            if (target == null) {
                target = "file:" + System.getProperty("user.home");
            }
            target += File.separator + name;
        } else if (urlProtocolRE.isMatch(name)) {
            target = name.substring(0, name.indexOf(':')).toLowerCase() + name.substring(name.indexOf(':'));
        } else if (name.startsWith(File.separator)) {
            target = current.substring(0, current.indexOf(':') + 1) + name;
        } else {
            target = current + File.separator + name;
        }
        if (env.isDebug()) {
            System.err.println("DEBUG: Target is [" + target + "]");
        }
        if (target.startsWith("file:")) {
            target = target.substring(target.indexOf(':') + 1);
            File realFile = new File(target);
            if (mode.equals("r")) {
                if (!realFile.exists()) {
                    throw new MdShellCancelException();
                }
                if (!realFile.canRead()) {
                    throw new MdShellCancelException();
                }
            } else if (mode.equals("w")) {
                try {
                    realFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new MdShellCancelException();
                }
                if (!realFile.canWrite()) {
                    throw new MdShellCancelException();
                }
            } else {
                throw new MdShellCancelException();
            }
            result = new MdShellDirFileFileSystem(env, realFile);
        } else if (target.startsWith("ftp://")) {
            result = new MdShellDirFileFtpClient(env, target);
        } else {
        }
        return result;
    }
}
