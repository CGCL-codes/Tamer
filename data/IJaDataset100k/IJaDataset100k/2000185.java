package com.bluemarsh.jswat.product.project;

import javax.swing.Action;
import javax.swing.Icon;
import org.netbeans.modules.project.uiapi.ActionsFactory;
import org.netbeans.spi.project.ui.support.ProjectActionPerformer;
import org.openide.util.ContextAwareAction;

/**
 * Satisfy requirements of the projectuiapi module. We do not support
 * projects so the implementation is empty.
 *
 * @author Nathan Fiedler
 */
public class ActionsFactoryImpl implements ActionsFactory {

    @Override
    public Action setAsMainProjectAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action customizeProjectAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action openSubprojectsAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action closeProjectAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action newFileAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action deleteProjectAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action copyProjectAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action moveProjectAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action newProjectAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ContextAwareAction projectCommandAction(String string, String string1, Icon icon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action projectSensitiveAction(ProjectActionPerformer arg0, String arg1, Icon arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action mainProjectCommandAction(String arg0, String arg1, Icon arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action mainProjectSensitiveAction(ProjectActionPerformer arg0, String arg1, Icon arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action fileCommandAction(String arg0, String arg1, Icon arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action renameProjectAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Action setProjectConfigurationAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
