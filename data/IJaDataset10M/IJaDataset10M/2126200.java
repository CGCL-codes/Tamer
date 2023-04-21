package jdos.shell;

import jdos.dos.drives.Drive_virtual;
import jdos.misc.Log;
import java.util.Vector;

public class AutoexecObject {

    private static Vector autoexec_strings = new Vector();

    private boolean installed = false;

    private String buf;

    public static void Shutdown() {
        autoexec_strings = new Vector();
    }

    public void Install(String in) {
        if (installed) Log.exit("autoexec: allready created " + buf);
        installed = true;
        buf = in;
        autoexec_strings.add(buf);
        CreateAutoexec();
        if (Shell.first_shell != null) {
            if (buf.startsWith("set ")) {
                String env = buf.substring(4);
                int pos = env.indexOf("=");
                if (pos < 0) {
                    Shell.first_shell.SetEnv(env, "");
                } else {
                    Shell.first_shell.SetEnv(env.substring(0, pos), env.substring(pos + 1));
                }
            }
        }
    }

    public void InstallBefore(String in) {
        if (installed) Log.exit("autoexec: already created " + buf);
        installed = true;
        buf = in;
        autoexec_strings.add(buf);
        CreateAutoexec();
    }

    public static StringBuffer autoexec_data = new StringBuffer();

    private void CreateAutoexec() {
        if (Shell.first_shell != null) Drive_virtual.VFILE_Remove("AUTOEXEC.BAT");
        autoexec_data = new StringBuffer();
        for (int i = 0; i < autoexec_strings.size(); i++) {
            String s = (String) autoexec_strings.elementAt(i);
            autoexec_data.append(s);
            autoexec_data.append("\r\n");
        }
        if (Shell.first_shell != null) {
            byte[] b = autoexec_data.toString().getBytes();
            Drive_virtual.VFILE_Register("AUTOEXEC.BAT", b, b.length);
        }
    }
}
