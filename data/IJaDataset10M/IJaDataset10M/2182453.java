package com.loribel.commons.gui.bo.person;

import javax.swing.JComponent;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.bo.GB_PersonBO;
import com.loribel.commons.gui.GB_ViewManagerAbstract;
import com.loribel.commons.gui.abstraction.GB_VMPrototype;
import com.loribel.commons.gui.bo.GB_BOPanelSingleObject;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * Classe GB_PersonVM
 *
 * @author Gr�gory Borelli
 */
public class GB_PersonVM2 extends GB_ViewManagerAbstract {

    private GB_PersonBO bo;

    private boolean useTitle;

    public GB_PersonVM2(GB_PersonBO a_person, boolean a_useTitle) {
        super();
        bo = a_person;
        useTitle = a_useTitle;
    }

    public JComponent buildView() {
        return new MyView();
    }

    public GB_LabelIcon getLabelIcon() {
        return GB_LabelIconTools.newLabelIcon("GB_PersonVM2");
    }

    /**
     * Inner class.
     */
    public static class MyPrototype implements GB_VMPrototype {

        public GB_ViewManager newViewManager(Object a_model, boolean a_useTitle) {
            return new GB_PersonVM2((GB_PersonBO) a_model, a_useTitle);
        }
    }

    /**
     * Inner class.
     */
    private class MyView extends GB_BOPanelSingleObject {

        MyView() {
            super(bo);
            init();
        }

        private void init() {
            this.setFactoryNotEdit();
            if (useTitle) {
                this.setStyleView(-1);
            } else {
                this.setHGap(5);
            }
            this.addAllProperties();
            this.addRowEnd();
        }
    }
}
