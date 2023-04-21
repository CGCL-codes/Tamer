package com.loribel.tools.xml;

import com.loribel.commons.abstraction.GB_ActionBeforeExit;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_Launcher;
import com.loribel.commons.gui.GB_VMTools;
import com.loribel.commons.swing.GB_Frame;
import com.loribel.commons.util.GB_DebugTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.GB_LauncherTools;
import com.loribel.tools.GB_ToolsInitializer;
import com.loribel.tools.xml.vm.GB_XmlFileVM;

/**
 * Class to launch Xml-Edit.
 *
 * @author Gr�gory Borelli
 */
public class GB_XmlEditLauncher implements GB_Launcher {

    private boolean flagExit = false;

    public GB_XmlEditLauncher() {
    }

    private void debug(String a_msg) {
        GB_DebugTools.debug(this, a_msg);
    }

    public static void main(String[] p) {
        GB_ToolsInitializer.initAll();
        GB_XmlEditLauncher l_launcher = new GB_XmlEditLauncher();
        l_launcher.initialize();
        l_launcher.flagExit = true;
        l_launcher.start();
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = AA.MSG_TITLE_XML_EDITOR;
        return GB_LabelIconTools.newLabelIcon(l_label);
    }

    public void initialize() {
    }

    public void start() {
        GB_ActionBeforeExit l_actionBeforeExit = new MyActionBeforeExit();
        GB_XmlFileVM l_viewManager = new GB_XmlFileVM(l_actionBeforeExit);
        GB_Frame l_frame = (GB_Frame) GB_VMTools.showIntoFrame(l_viewManager, AA.MSG_TITLE_XML_EDITOR, flagExit);
        l_frame.setFullScreen(70);
        l_frame.validate();
    }

    /**
     * Inner class for action before exit.
     */
    private static class MyActionBeforeExit implements GB_ActionBeforeExit {

        public void doBeforeExit() {
            GB_LauncherTools.doBeforeExit();
        }
    }
}
