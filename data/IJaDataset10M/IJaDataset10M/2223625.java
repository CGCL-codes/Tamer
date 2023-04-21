package de.frewert.vboxj.gui.swing;

import javax.swing.filechooser.FileFilter;

/**
 * <pre>
 * Copyright (C) 2003 Carsten Frewert. All Rights Reserved.
 * 
 * The VBox/J package (de.frewert.vboxj.*) is distributed under
 * the terms of the Artistic license.
 * </pre>
 * @author Carsten Frewert
 * &lt;<a href="mailto:frewert@users.sourceforge.net">
 * frewert@users.sourceforge.net</a>&gt;
 */
public abstract class MagicFileFilter extends FileFilter {

    public abstract String getExtension();
}
