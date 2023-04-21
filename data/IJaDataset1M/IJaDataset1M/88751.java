package com.loribel.tools.java.vm;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import com.loribel.commons.business.GB_BOTools;
import com.loribel.commons.business.abstraction.GB_BOAccessor;
import com.loribel.commons.gui.GB_ViewManagerAbstract;
import com.loribel.commons.gui.bo.GB_BOPanelSingleObject;
import com.loribel.commons.swing.GB_ButtonAction;
import com.loribel.commons.swing.tools.GB_ButtonBarTools;
import com.loribel.commons.swing.tools.GB_SwingTools;
import com.loribel.tools.java.bo.GB_JavaProjectBO;

/**
 * View Manager for GB_JavaProject.
 */
public class GB_JavaProjectVM extends GB_ViewManagerAbstract {

    private GB_JavaProjectBO javaProject;

    private boolean useTitle;

    private GB_JavaProjectLink link;

    public GB_JavaProjectVM(GB_JavaProjectBO a_javaProject, boolean a_useTitle, GB_JavaProjectLink a_link) {
        javaProject = a_javaProject;
        useTitle = a_useTitle;
        link = a_link;
    }

    public GB_JavaProjectVM(GB_JavaProjectBO a_javaProject, boolean a_useTitle) {
        this(a_javaProject, a_useTitle, new GB_JavaProjectLinkImpl());
    }

    /**
     * buildView
     *
     * @return JComponent
     */
    protected JComponent buildView() {
        return new MyView();
    }

    /**
     * Inner class MyView.
     */
    private class MyView extends GB_BOPanelSingleObject {

        MyView() {
            super(javaProject);
            init();
        }

        private void init() {
            this.setHGap(5);
            if (useTitle) {
                this.setTitle("Java Project");
                this.addRow(10);
            }
            this.addAllProperties();
            if (!javaProject.isReadOnly() && (link != null)) {
                this.addRowFill(buildButtonsBar());
            }
            this.addRowEnd();
        }

        private JComponent buildButtonsBar() {
            Collection c = new ArrayList();
            c.add(new MyButtonLoad());
            c.add(new MyButtonSave());
            return GB_ButtonBarTools.newButtonsBar(null, c, true);
        }
    }

    /**
     * Button Save
     */
    private class MyButtonSave extends GB_ButtonAction {

        public MyButtonSave() {
            super(AA.BUTTON_SAVE);
            if (link == null) {
                this.setEnabled(false);
            }
        }

        public void doAction() throws Exception {
            Component l_parent = GB_SwingTools.getRoot(this);
            String l_name = javaProject.getProjectName();
            if (link.isJavaProjectExist(l_name, GB_BOAccessor.OPTION.DEFAULT)) {
                int r = JOptionPane.showConfirmDialog(l_parent, "Le projet " + l_name + " existe d�j�, voulez-vous le remplacer?");
                if (r != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            link.saveJavaProject(javaProject);
            JOptionPane.showMessageDialog(l_parent, "Le projet " + l_name + " a �t� sauvegard� avec succ�s.");
        }
    }

    /**
     * Button Load
     */
    private class MyButtonLoad extends GB_ButtonAction {

        public MyButtonLoad() {
            super(AA.BUTTON_LOAD_);
            if (link == null) {
                this.setEnabled(false);
            }
        }

        public void doAction() throws Exception {
            Component l_parent = GB_SwingTools.getRoot(this);
            String l_name = javaProject.getProjectName();
            l_name = GB_JavaProjectGuiTools.showDialogChooseProject(l_parent, l_name);
            if (l_name == null) {
                return;
            }
            GB_JavaProjectBO l_newJavaProject = link.loadJavaProject(l_name);
            if (l_newJavaProject == null) {
                JOptionPane.showMessageDialog(l_parent, "Le projet " + l_name + " n'existe pas!");
            } else {
                GB_BOTools.copyValues(javaProject, l_newJavaProject);
            }
        }
    }
}
