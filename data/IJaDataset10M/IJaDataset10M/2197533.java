package com.ivis.xprocess.ui.datawrappers.project;

import org.eclipse.swt.graphics.Color;
import com.ivis.xprocess.core.Swimlane;
import com.ivis.xprocess.core.Viewpoint;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.Xrecord;
import com.ivis.xprocess.ui.datawrappers.IWrapper;
import com.ivis.xprocess.ui.tables.columns.definition.XProcessColumn;
import com.ivis.xprocess.ui.util.ViewpointUtil;

public class OrphanSwimlaneWrapper extends SwimlaneWrapper {

    public OrphanSwimlaneWrapper(IWrapper parent, Xrecord xrecord) {
        super(parent, xrecord);
    }

    @Override
    public String getLabel() {
        Viewpoint viewpoint = (Viewpoint) getParentWrapper().getElement();
        return viewpoint.getOrphanSwimlaneLabel();
    }

    @Override
    public boolean testAttribute(Object target, String name, String value) {
        return false;
    }

    public boolean canInplaceEdit(XProcessColumn column) {
        return true;
    }

    @Override
    public void setName(String name) {
        Viewpoint viewpoint = (Viewpoint) getParentWrapper().getElement();
        viewpoint.setOrphanSwimlaneLabel(name);
    }

    public Swimlane getSwimlane() {
        return null;
    }

    public boolean evaluateChild(Xelement childElement) {
        return false;
    }

    public void openEditor(Object selectedObject) {
    }

    public Color[] getColors() {
        return ViewpointUtil.getDefaultColours();
    }
}
