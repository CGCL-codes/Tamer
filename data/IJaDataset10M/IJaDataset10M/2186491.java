package se.kth.cid.conzilla.metadata;

import se.kth.cid.conzilla.app.*;
import se.kth.cid.conzilla.browse.*;
import se.kth.cid.conzilla.edit.*;
import se.kth.cid.conzilla.tool.*;
import se.kth.cid.conzilla.controller.*;
import se.kth.cid.conzilla.content.*;
import se.kth.cid.component.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/** 
 *  @author Matthias Palmer.
 */
public class MetadataExtra implements Extra {

    FrameMetaDataDisplayer metadataDisplayer;

    public MetadataExtra() {
    }

    public boolean initExtra(ConzillaKit kit) {
        metadataDisplayer = new FrameMetaDataDisplayer();
        return true;
    }

    public String getName() {
        return "MetadataExtra";
    }

    public void refreshExtra() {
    }

    public boolean saveExtra() {
        return true;
    }

    public void exitExtra() {
    }

    public void extendMenu(ToolsMenu menu, final MapController c) {
        if (menu.getName().equals(BrowseMapManagerFactory.BROWSE_MENU)) {
            ((MapToolsMenu) menu).addMapMenuItem(new InfoMapTool(c, metadataDisplayer), 300);
        } else if (menu.getName().equals(ContentMenu.CONTENT_MENU)) {
            final ContentMenu cm = (ContentMenu) menu;
            final MapController mc = c;
            cm.addTool(new ContentTool("INFO", MetadataExtra.class.getName()) {

                public void actionPerformed(ActionEvent e) {
                    Component comp = mc.getContentSelector().getContent(contentIndex);
                    metadataDisplayer.showMetaData(comp);
                }
            }, 200);
        } else if (menu.getName().equals(EditMapManagerFactory.EDIT_MENU_NEURON)) {
            ((MapToolsMenu) menu).addMapMenuItem(new InfoMapTool(c, metadataDisplayer), 1100);
        } else if (menu.getName().equals(EditMapManagerFactory.EDIT_MENU_AXON)) {
            ((MapToolsMenu) menu).addMapMenuItem(new InfoMapTool(c, metadataDisplayer), 1400);
        } else if (menu.getName().equals(EditMapManagerFactory.EDIT_MENU_MAP)) {
            ((MapToolsMenu) menu).addMapMenuItem(new InfoMapTool(c, metadataDisplayer), 300);
        }
    }

    public void addExtraFeatures(final MapController c, final Object o, String location, String hint) {
    }
}
