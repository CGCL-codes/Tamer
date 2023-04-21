package org.eclipse.swt.internal.gnome;

public class GnomeVFSMimeApplication {

    public int id;

    public int name;

    public int command;

    public boolean can_open_multiple_files;

    public int expects_uris;

    public int supported_uri_schemes;

    public boolean requires_terminal;

    public static final int sizeof = GNOME.GnomeVFSMimeApplication_sizeof();
}
