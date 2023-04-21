package com.loribel.tools.template.vm;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_LabelIconOwner;
import com.loribel.commons.abstraction.GB_Unregisterable;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.business.abstraction.GB_BOConfigurable;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.exception.GB_ConfigException;
import com.loribel.commons.gui.GB_ViewManagerAbstract;
import com.loribel.commons.gui.abstraction.GB_VMPrototype;
import com.loribel.commons.gui.bo.GB_BOCompEnabledMgr;
import com.loribel.commons.gui.bo.GB_BOPanelSingleObject;
import com.loribel.commons.swing.GB_PanelRowsTitle;
import com.loribel.commons.swing.tools.GB_BorderTools;
import com.loribel.commons.util.GB_UnregisterableList;
import com.loribel.tools.template.abstraction.GB_JavaGeneratorCst;
import com.loribel.tools.template.abstraction.GB_TJava;
import com.loribel.tools.template.bo.GB_JavaGeneratorCstBO;

/**
 *
 */
public class GB_TJavaVM extends GB_ViewManagerAbstract {

    protected GB_TJava template;

    private GB_UnregisterableList unregisterableList;

    private GB_JavaGeneratorCstBO generatorCst;

    private JComponent groupCst;

    public GB_TJavaVM(GB_TJava a_template) {
        template = a_template;
    }

    /**
     * Build the view.
     */
    protected JComponent buildView() {
        if (template instanceof GB_BOConfigurable) {
            return buildViewForBO((GB_BOConfigurable) template);
        }
        return new JLabel("No configuration available: " + template.getClass());
    }

    protected boolean removeView() {
        boolean r = super.removeView();
        if (r && (unregisterableList != null)) {
            unregisterableList.unregister();
        }
        return r;
    }

    protected void register(GB_Unregisterable a_toRegister) {
        if (unregisterableList == null) {
            unregisterableList = new GB_UnregisterableList();
        }
        unregisterableList.add(a_toRegister);
    }

    public void loadDataToView() {
        registerCompEnabledMgr();
    }

    /**
     * Manage enabled / disabled components.
     * <ul>
     *   <li>property useTab -> !separator
     *   <li>property genHtmlTable -> !useSeparator !ignoreEmptyCol
     * </ul>
     *
     * @param a_comp JComponent
     */
    private void registerCompEnabledMgr() {
        GB_BOCompEnabledMgr l_compMgr;
        if (generatorCst != null) {
            l_compMgr = new GB_BOCompEnabledMgr(generatorCst, GB_JavaGeneratorCstBO.BO_PROPERTY.USE_CLIPBOARD, Boolean.TRUE);
            l_compMgr.addPropertyToDisabled(groupCst, GB_JavaGeneratorCstBO.BO_PROPERTY.SOURCE_DIR);
            l_compMgr.updateComponents();
            register(l_compMgr);
        }
    }

    /**
     * Build the view.
     */
    protected JComponent buildViewForBO(GB_BOConfigurable a_configurable) {
        GB_PanelRowsTitle retour = new GB_PanelRowsTitle();
        retour.setTitle(getLabelIcon());
        JComponent l_comp;
        l_comp = buildGroupConfig(a_configurable);
        if (l_comp != null) {
            retour.addRowFill(l_comp);
        }
        groupCst = buildGroupCst();
        if (groupCst != null) {
            retour.addRowFill(groupCst);
        }
        retour.addRowEnd();
        return retour;
    }

    /**
     * Build the view.
     */
    protected JComponent buildGroupCst() {
        GB_JavaGeneratorCst l_cst = template.getGeneratorCst();
        if (l_cst instanceof GB_JavaGeneratorCstBO) {
            generatorCst = (GB_JavaGeneratorCstBO) l_cst;
            GB_BOPanelSingleObject retour = new GB_BOPanelSingleObject(generatorCst);
            retour.addAllProperties();
            Border l_border = GB_BorderTools.newGroupTitleBorder("Constantes pour le g�n�rateur de code");
            retour.setBorder(l_border);
            return retour;
        } else {
            return null;
        }
    }

    /**
     * Build the view.
     */
    protected JComponent buildGroupConfig(GB_BOConfigurable a_configurable) {
        GB_SimpleBusinessObject l_bo = null;
        try {
            l_bo = a_configurable.getBOConfig();
        } catch (GB_ConfigException e) {
            return new JLabel("Configuration error: " + e.getMessage());
        }
        GB_BOPanelSingleObject retour = new GB_BOPanelSingleObject(l_bo);
        retour.addAllProperties();
        Border l_border = GB_BorderTools.newGroupTitleBorder("Configuration");
        retour.setBorder(l_border);
        return retour;
    }

    public GB_LabelIcon getLabelIcon() {
        if (template instanceof GB_LabelIconOwner) {
            return ((GB_LabelIconOwner) template).getLabelIcon();
        }
        return null;
    }

    /**
     * Return a prototype.
     */
    public static GB_VMPrototype newPrototype() {
        GB_VMPrototype retour = new GB_VMPrototype() {

            public GB_ViewManager newViewManager(Object a_model, boolean a_useTitle) {
                return new GB_TJavaVM((GB_TJava) a_model);
            }
        };
        return retour;
    }
}
