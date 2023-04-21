package com.izforge.izpack.installer;

import com.izforge.izpack.uninstaller.SelfModifier;
import com.izforge.izpack.util.Debug;
import java.awt.HeadlessException;
import java.io.File;
import java.lang.reflect.Method;

/**
 * Main class, for starting the installer if it was build to support more than one volume.
 *
 * @author Dennis Reil, <Dennis.Reil@reddot.de>
 */
public class MultiVolumeInstaller {

    protected static String mediadirectory;

    /**
     * @param args
     */
    public static void main(String[] args) {
        ProgressDialog progressDialog = null;
        try {
            progressDialog = new ProgressDialog();
            progressDialog.startProgress();
        } catch (HeadlessException ex) {
            Debug.log("Progress will not be shown. No display found.");
        }
        MultiVolumeInstaller.setMediadirectory(new File(".").getParent());
        if ((args.length > 0) && ("-direct".equals(args[0]))) {
            String[] newargs;
            if (args.length > 1) {
                newargs = new String[args.length - 1];
                System.arraycopy(args, 1, newargs, 0, args.length - 1);
            } else {
                newargs = new String[0];
            }
            MultiVolumeInstaller.install(newargs);
        } else {
            try {
                long maxmem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
                Debug.trace("Currently using maximum memory of " + maxmem + "m");
                long maxpermgensize = maxmem / 4;
                Class<MultiVolumeInstaller> clazz = MultiVolumeInstaller.class;
                Method target = clazz.getMethod("install", new Class[] { String[].class });
                String[] newargs = new String[args.length + 4];
                System.arraycopy(args, 0, newargs, 4, args.length);
                newargs[0] = "-Xmx" + maxmem + "m";
                newargs[1] = "-XX:MaxPermSize=" + maxpermgensize + "m";
                newargs[2] = "-mediadir";
                newargs[3] = SelfModifier.findJarFile(clazz).getParent();
                System.out.println("Setting mediadir: " + newargs[1]);
                MultiVolumeInstaller.setMediadirectory(SelfModifier.findJarFile(clazz).getParent());
                new SelfModifier(target).invoke(newargs);
            } catch (Exception e) {
                Debug.trace(e);
            }
        }
        if (progressDialog != null) {
            progressDialog.stopProgress();
        }
    }

    public static String getMediadirectory() {
        return MultiVolumeInstaller.mediadirectory;
    }

    public static void setMediadirectory(String mediadirectory) {
        MultiVolumeInstaller.mediadirectory = mediadirectory;
    }

    public static void install(String[] args) {
        if ((args.length >= 2) && ("-mediadir".equals(args[0]))) {
            MultiVolumeInstaller.setMediadirectory(args[1]);
            if (args.length > 2) {
                String[] newargs = new String[args.length - 2];
                System.arraycopy(args, 2, newargs, 0, args.length - 2);
                args = newargs;
            } else {
                args = new String[0];
            }
        }
        Installer.main(args);
    }
}
