package com.google.code.gtkjfilechooser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * <p>
 * Version Information for GTK+
 * </p>
 * <p>
 * We get the GTK version from the shared library file, for example
 * "libgtk-x11-2.0.so.0.600.7" is GTK+ 2.6.7, "libgtk-x11-2.0.so.0.1600.1" is
 * GTK+ 2.16.1 an so on...
 * </p>
 * 
 * 
 * 
 * @see {@link http
 *      ://library.gnome.org/devel/gtk/unstable/gtk-Feature-Test-Macros.html}
 * @see {@link http://git.gnome.org/cgit/gtk+/tree/gtk/gtkversion.h.in}
 * 
 * @author Costantino Cerbo
 * 
 */
public class GtkVersion {

    static int gtkMajorVersion;

    static int gtkMinorVersion;

    static int gtkMicroVersion;

    static {
        gtkMajorVersion = 2;
        gtkMinorVersion = Short.MAX_VALUE;
        gtkMicroVersion = Short.MAX_VALUE;
        try {
            String[] files = new File("/usr/lib").list(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith("libgtk-x11-2.0.so.0.");
                }
            });
            if (files != null) {
                String lib = files[0];
                int i1 = lib.lastIndexOf('.');
                int i0 = lib.lastIndexOf('.', i1 - 1);
                String minor = lib.substring(i0 + 1, i1);
                String micro = lib.substring(i1 + 1);
                gtkMinorVersion = Integer.parseInt(minor) / 100;
                gtkMicroVersion = Integer.parseInt(micro);
            }
        } catch (Throwable e) {
            Log.debug(e.getMessage());
        }
    }

    /**
	 * Checks that the GTK+ library in use is compatible (the same or newer)
	 * with the given version.
	 * 
	 * @param required_major
	 * @param required_minor
	 * @param required_micro
	 * @return {@code null} if the GTK+ library is compatible with the given
	 *         version, or a string describing the version mismatch. The
	 *         returned string is owned by GTK+ and should not be modified or
	 *         freed.
	 */
    public static boolean check(int major, int minor, int micro) {
        return (gtkMajorVersion > (major) || (gtkMajorVersion == (major) && gtkMinorVersion > (minor)) || (gtkMajorVersion == (major) && gtkMinorVersion == (minor) && gtkMicroVersion >= (micro)));
    }

    public static String getCurrent() {
        return gtkMajorVersion + "." + gtkMinorVersion + "." + gtkMicroVersion;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(GtkVersion.getCurrent());
    }
}
