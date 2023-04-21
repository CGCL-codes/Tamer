package com.loribel.commons.gui.bo.vm;

import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import com.loribel.commons.GB_DemoInitializer;
import com.loribel.commons.business.GB_BOFactoryTools;
import com.loribel.commons.business.GB_BOMetaDataFactoryTools;
import com.loribel.commons.business.abstraction.GB_BOFactory;
import com.loribel.commons.business.abstraction.GB_BOMetaDataFactory;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.gui.GB_VMTools;
import com.loribel.commons.gui.abstraction.GB_VMDemo;
import com.loribel.commons.util.CTools;

/**
 * Demo for GB_BODemoTreeVM.
 *
 * @author Gregory Borelli
 */
public class GB_BODemoTreeVMDemo implements GB_VMDemo {

    public GB_BODemoTreeVMDemo() {
        super();
    }

    public static void main(String[] p) {
        GB_DemoInitializer.initAll();
        GB_VMDemo l_demo = new GB_BODemoTreeVMDemo();
        l_demo.show(true);
    }

    public JFrame show(boolean a_flagExitOnClose) {
        GB_BOFactory l_boFactory = GB_BOFactoryTools.getFactory();
        GB_BOMetaDataFactory l_boPropertyFactrory = GB_BOMetaDataFactoryTools.getFactory();
        String[] l_boNames = l_boPropertyFactrory.getBONames();
        int len = CTools.getSize(l_boNames);
        DefaultMutableTreeNode l_root = new DefaultMutableTreeNode("Business Objects");
        GB_BODemoTreeVM l_vm = new GB_BODemoTreeVM();
        l_vm.setRoot(l_root);
        for (int i = 0; i < len; i++) {
            System.out.println("BO Name: " + l_boNames[i]);
            GB_SimpleBusinessObject l_bo = l_boFactory.newBusinessObject(l_boNames[i]);
            l_vm.addNodeBOSingle(l_root, l_bo);
        }
        return GB_VMTools.showIntoFrame(l_vm, null, a_flagExitOnClose);
    }

    public Class getDemoClass() {
        return GB_BODemoTreeVM.class;
    }

    public boolean unregister() {
        return true;
    }
}
