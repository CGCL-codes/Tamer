package com.loribel.tools.java.tree;

import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.gui.GB_TreeVM;
import com.loribel.commons.gui.GB_VMTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.tools.GB_ToolsInitializer;
import com.loribel.tools.java.bo.GB_JavaProjectBO;

public abstract class GB_JavaReportsTNDemo {

    public static void main(String[] p) {
        GB_ToolsInitializer.initAll();
        try {
            GB_JavaProjectBO l_javaProject = new GB_JavaProjectBO();
            GB_LabelIcon l_labelIcon = GB_LabelIconTools.newLabelIcon("Reports");
            GB_JavaReportsTN l_root = new GB_JavaReportsTN(l_labelIcon, l_javaProject);
            GB_TreeVM l_vm = new GB_TreeVM();
            l_vm.setRoot(l_root);
            GB_VMTools.showIntoFrameTest(l_vm);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
