package net.community.chest.swing.component.filechooser;

import net.community.chest.reflect.EnumStringInstantiator;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jul 26, 2007 1:34:26 PM
 */
public class FileSelectionModeStringInstantiator extends EnumStringInstantiator<FileSelectionMode> {

    public FileSelectionModeStringInstantiator() {
        super(FileSelectionMode.class, false);
        setValues(FileSelectionMode.VALUES);
    }

    public static final FileSelectionModeStringInstantiator DEFAULT = new FileSelectionModeStringInstantiator();
}
