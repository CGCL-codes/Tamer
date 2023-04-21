package com.simpledata.bc.uicomponents;

import javax.swing.ImageIcon;
import com.simpledata.bc.datamodel.Copiable;

/**
 * A class to keep Copy item 
 */
public class CopyItem {

    public Copiable copiable;

    public String title;

    public ImageIcon icon;

    private TarifViewer owner;

    public boolean deleted;

    CopyItem(TarifViewer owner, Copiable copiable, String title, ImageIcon icon, boolean deleted) {
        this.copiable = copiable;
        this.title = title;
        this.icon = icon;
        this.owner = owner;
        this.deleted = deleted;
    }

    public void drop() {
        owner.removeCopyItem(this);
    }
}
