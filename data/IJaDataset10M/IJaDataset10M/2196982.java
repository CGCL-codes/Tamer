package org.furthurnet.xmlparser;

import javax.swing.JPopupMenu;
import org.furthurnet.servergui.FileSetInfo;

public interface PopupProviderShared {

    public JPopupMenu getPopup(FileSetInfo fsi);
}
