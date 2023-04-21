package com.loribel.commons.gui.bo.metamodel.vm;

import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import com.loribel.commons.abstraction.GB_DialogDimensionOwner;
import com.loribel.commons.abstraction.GB_FocusComponentOwner;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.business.GB_BOIconTools;
import com.loribel.commons.business.GB_BOPropertyTools;
import com.loribel.commons.business.impl.bo.GB_BOPropertyBO;
import com.loribel.commons.gui.GB_ViewManagerAbstract;
import com.loribel.commons.gui.abstraction.GB_BOSwingFactory;
import com.loribel.commons.gui.abstraction.GB_VMPrototype;
import com.loribel.commons.gui.bo.GB_BOPanelSingleObject;
import com.loribel.commons.gui.bo.GB_BOStringMBOGuiTools;
import com.loribel.commons.gui.bo.metamodel.AA;
import com.loribel.commons.swing.GB_PanelRowsTools;
import com.loribel.commons.swing.tools.GB_TitleDecoratorTools;
import com.loribel.commons.util.STools;
import com.loribel.commons.util.impl.GB_LabelIconImpl;

/**
 * View manager for GB_BOBOPropertyBO.
 *
 * @author Gregory Borelli
 */
public class GB_BOBOPropertySimpleVM extends GB_ViewManagerAbstract {

    /**
     * Inner class
     */
    private class MyView extends GB_BOPanelSingleObject implements GB_FocusComponentOwner, GB_DialogDimensionOwner {

        private JComponent focusComp;

        MyView() {
            super(bo);
            GB_PanelRowsTools.setStyleView(this);
            if (useTitle) {
                this.setTitle(getLabelIcon());
            }
            init();
        }

        private JComponent buildOptionForDate() {
            GB_BOPanelSingleObject retour = newBOPanel(bo);
            String[] l_propertyNames = new String[] { GB_BOPropertyBO.BO_PROPERTY.MIN_VALUE, GB_BOPropertyBO.BO_PROPERTY.MAX_VALUE };
            retour.addPropertiesLine(l_propertyNames);
            retour.addRowFill(new JLabel());
            GB_LabelIcon l_title = new GB_LabelIconImpl("Options pour valeur de type Date");
            return GB_TitleDecoratorTools.decoreWithTitleDefault(retour, l_title);
        }

        private JComponent buildOptionForNumeric() {
            GB_BOPanelSingleObject retour = newBOPanel(bo);
            String[] l_propertyNames = new String[] { GB_BOPropertyBO.BO_PROPERTY.MIN_VALUE, GB_BOPropertyBO.BO_PROPERTY.MAX_VALUE };
            retour.addPropertiesLine(l_propertyNames);
            retour.addRowFill(new JLabel());
            GB_LabelIcon l_title = new GB_LabelIconImpl("Options pour valeur de type num�rique");
            return GB_TitleDecoratorTools.decoreWithTitleDefault(retour, l_title);
        }

        private JComponent buildOptionForRefId() {
            GB_BOPanelSingleObject retour = newBOPanel(bo);
            retour.addProperty(GB_BOPropertyBO.BO_PROPERTY.NAME);
            retour.addProperty(GB_BOPropertyBO.BO_PROPERTY.REF_ID);
            GB_LabelIcon l_title = new GB_LabelIconImpl(AA.TITLE_GENERAL);
            return GB_TitleDecoratorTools.decoreWithTitleDefault(retour, l_title);
        }

        private JComponent buildOptionForString() {
            GB_BOPanelSingleObject retour = newBOPanel(bo);
            String[] l_propertyNames = new String[] { GB_BOPropertyBO.BO_PROPERTY.FLAG_PASSWORD, GB_BOPropertyBO.BO_PROPERTY.FLAG_SCROLLABLE, GB_BOPropertyBO.BO_PROPERTY.FLAG_LINE_WRAP };
            retour.addPropertiesLine(l_propertyNames);
            l_propertyNames = new String[] { GB_BOPropertyBO.BO_PROPERTY.PREFERRED_SIZE, GB_BOPropertyBO.BO_PROPERTY.MIN_SIZE, GB_BOPropertyBO.BO_PROPERTY.MAX_SIZE };
            retour.addPropertiesLine(l_propertyNames);
            retour.addProperty(GB_BOPropertyBO.BO_PROPERTY.VALIDATION_REGEX);
            retour.addProperty(GB_BOPropertyBO.BO_PROPERTY.EDIT_REGEX);
            GB_LabelIcon l_title = new GB_LabelIconImpl("Options pour valeur de type String");
            return GB_TitleDecoratorTools.decoreWithTitleDefault(retour, l_title);
        }

        private JComponent buildOptionGeneral() {
            GB_BOPanelSingleObject retour = newBOPanel(bo);
            GB_BOSwingFactory l_factory = retour.addProperty(GB_BOPropertyBO.BO_PROPERTY.NAME);
            focusComp = l_factory.getMainComponent();
            if (editType) {
                retour.addProperty(GB_BOPropertyBO.BO_PROPERTY.TYPE);
            } else {
                if (GB_BOPropertyTools.isAcceptDefault(bo)) {
                    retour.addProperty(GB_BOPropertyBO.BO_PROPERTY.DEFAULT_VALUE);
                }
            }
            retour.addProperty(GB_BOPropertyBO.BO_PROPERTY.UNIT);
            String[] l_propertyNames = new String[] { GB_BOPropertyBO.BO_PROPERTY.FLAG_VISIBLE, GB_BOPropertyBO.BO_PROPERTY.FLAG_READ_ONLY, GB_BOPropertyBO.BO_PROPERTY.FLAG_OPTIONAL, GB_BOPropertyBO.BO_PROPERTY.FLAG_ENUMERATION };
            retour.addPropertiesLine(l_propertyNames);
            GB_LabelIcon l_title = new GB_LabelIconImpl(AA.TITLE_GENERAL);
            return GB_TitleDecoratorTools.decoreWithTitleDefault(retour, l_title);
        }

        public JComponent getFocusComponent() {
            return focusComp;
        }

        public Dimension getSizeForDialog() {
            return new Dimension(650, 400);
        }

        private void init() {
            String l_refId = bo.getRefId();
            if (STools.isNotNull(l_refId)) {
                this.addRowFill(buildOptionForRefId());
                this.addRowEnd();
                return;
            }
            this.addRowFill(buildOptionGeneral());
            this.addRow(5);
            if (editType || GB_BOPropertyTools.isString(bo)) {
                this.addRowFill(buildOptionForString());
                this.addRow(5);
            }
            if (!editType && GB_BOPropertyTools.isNumber(bo)) {
                this.addRowFill(buildOptionForNumeric());
                this.addRow(5);
            }
            this.addRowFill2(GB_BOStringMBOGuiTools.buildPanelLabelDescDefault(bo));
        }
    }

    /**
     * Inner class Prototype.
     */
    public static class VMPrototype implements GB_VMPrototype {

        public GB_ViewManager newViewManager(Object a_model, boolean a_useTitle) {
            return new GB_BOBOPropertySimpleVM((GB_BOPropertyBO) a_model, a_useTitle);
        }
    }

    private GB_BOPropertyBO bo;

    private boolean editType = true;

    private boolean useTitle;

    public GB_BOBOPropertySimpleVM(GB_BOPropertyBO a_property, boolean a_useTitle) {
        super();
        bo = a_property;
        useTitle = a_useTitle;
    }

    protected JComponent buildView() {
        return new MyView();
    }

    public GB_LabelIcon getLabelIcon() {
        Icon l_icon = GB_BOIconTools.getIconForBO(bo.getBOName());
        String l_label = AA.TITLE_PROPERTY_GENERAL;
        return new GB_LabelIconImpl(l_label, l_icon);
    }

    GB_BOPanelSingleObject newBOPanel(GB_BOPropertyBO a_bo) {
        GB_BOPanelSingleObject retour = new GB_BOPanelSingleObject(a_bo);
        retour.setFactoryDefault();
        return retour;
    }

    public void setEditType(boolean a_editType) {
        editType = a_editType;
    }
}
