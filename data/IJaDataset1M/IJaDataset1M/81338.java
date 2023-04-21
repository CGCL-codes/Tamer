package com.loribel.commons.gui.bo.comparator;

import java.awt.Dimension;
import java.util.List;
import javax.swing.JFrame;
import com.loribel.commons.GB_FwkGuiInitializer;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObjectSet;
import com.loribel.commons.gui.GB_VMTools;
import com.loribel.commons.gui.abstraction.GB_VMDemo;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.impl.GB_CoupleValuesBOImpl;

/**
 * 
 * GB_BODifferenceVMDemo 
 *
 * @author Fran�ois Gravel
 */
public class GB_BODifferenceVMDemo implements GB_VMDemo {

    public static void main(String[] args) {
        GB_FwkGuiInitializer.initAll();
        GB_VMDemo l_demo = new GB_BODifferenceVMDemo();
        l_demo.show(true);
    }

    public JFrame show(boolean a_flagExitOnClose) {
        GB_SimpleBusinessObjectSet l_bo = GB_BODiffereneDemoTools.getBOTest();
        GB_SimpleBusinessObjectSet l_boDiff = GB_BODiffereneDemoTools.getBOTest();
        l_bo.setReadOnly(false);
        l_boDiff.setReadOnly(false);
        GB_ComparatorOptions l_option = new GB_ComparatorOptions();
        l_option.setUseOnlyVisible(false);
        l_option.setLabelIcon1(GB_LabelIconTools.newLabelIcon("Nouvel objet"));
        l_option.setLabelIcon2(GB_LabelIconTools.newLabelIcon("Ancien"));
        List l_propertyList = GB_BOPropertyDifferencreTools.findAllPropertiesDiff(l_bo, l_boDiff, null, l_option);
        GB_CoupleValuesBOImpl l_coupleValue = new GB_CoupleValuesBOImpl(l_bo, l_boDiff);
        GB_ViewManager l_vm = new GB_BODifferenceVM(l_coupleValue, l_propertyList, true, l_option);
        Dimension l_dim = new Dimension(800, 600);
        JFrame l_frame = GB_VMTools.showIntoFrame(l_vm, null, l_dim, a_flagExitOnClose);
        return l_frame;
    }

    public Class getDemoClass() {
        return GB_BODifferenceVM.class;
    }

    public boolean unregister() {
        return true;
    }
}
