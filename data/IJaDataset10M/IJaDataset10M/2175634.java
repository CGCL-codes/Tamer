package de.juwimm.cms.gui.controls;

/**
 * <p>Title: ConQuest</p>
 * <p>Description: Enterprise Content Management</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Revision: 1.3 $
 */
public interface ReloadablePanel extends UnloadablePanel {

    public void reload();

    public void save();
}
