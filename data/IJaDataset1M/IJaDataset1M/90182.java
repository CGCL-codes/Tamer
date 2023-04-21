package com.loribel.commons.gui.editor;

import javax.swing.JComponent;
import com.loribel.commons.GB_FwkGuiInitializer;
import com.loribel.commons.LNG;
import com.loribel.commons.abstraction.GB_StringLngSet;
import com.loribel.commons.abstraction.GB_SwingDemo;
import com.loribel.commons.gui.demo.GB_SwingDemoAbstract;
import com.loribel.commons.swing.GB_PanelRowsTitle;
import com.loribel.commons.swing.GB_PanelRowsTools;
import com.loribel.commons.swing.tools.GB_SwingDemoTools;
import com.loribel.commons.util.types.GB_StringLngImpl;

/**
 * Demo.
 *
 * @author Gregory Borelli
 */
public class GB_StringLngPanelBuilderDemo extends GB_SwingDemoAbstract {

    public static void main(String[] args) {
        GB_FwkGuiInitializer.initImagesFactory();
        LNG.setLngs(new String[] { "fr", "en", "es", "de", "it" }, 3);
        GB_SwingDemo l_demo = new GB_StringLngPanelBuilderDemo();
        GB_SwingDemoTools.showIntoFrame(l_demo);
    }

    public Class getDemoClass() {
        return GB_StringLngPanelBuilder.class;
    }

    public JComponent getDemoComponent() {
        GB_PanelRowsTitle retour = new GB_PanelRowsTitle();
        GB_PanelRowsTools.setStyleView(retour);
        retour.setTitle(getTitle());
        GB_StringLngSet l_strLng = new GB_StringLngImpl();
        GB_StringLngPanelBuilder l_builder = new GB_StringLngPanelBuilder("text 1 (visu)", l_strLng, false);
        retour.addRowFill(l_builder.buildComponent());
        l_builder = new GB_StringLngPanelBuilder("text 1 (edit)", l_strLng, true);
        retour.addRowFill(l_builder.buildComponent());
        l_strLng = new GB_StringLngImpl();
        l_builder = new GB_StringLngPanelBuilder("text 2", l_strLng, true);
        l_builder.setNbLines(10);
        retour.addRowFill(l_builder.buildComponent());
        l_strLng = new GB_StringLngImpl();
        l_builder = new GB_StringLngPanelBuilder("text 3", l_strLng, LNG.getLngs(), true, true);
        retour.addRowFill(l_builder.buildComponent());
        retour.addRowEnd();
        return retour;
    }
}
