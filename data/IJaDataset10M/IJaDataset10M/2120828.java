package com.loribel.commons.gui.bo.metamodel.pilotage;

import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;
import com.loribel.commons.GB_FwkGuiInitializer;
import com.loribel.commons.business.GB_BOFactoryTools;
import com.loribel.commons.business.GB_BOMetaDataFactoryTools;
import com.loribel.commons.business.abstraction.GB_BOMetaData;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.exception.GB_LoadException;
import com.loribel.commons.gui.abstraction.GB_VMDemo;
import com.loribel.commons.gui.bo.metamodel.options.GB_BOPilotageOptions;
import com.loribel.commons.gui.bo.metamodel.options.GB_BOPilotageOptionsImpl;
import com.loribel.commons.gui.demo.GB_TNDemoAbstract;

/**
 * Demo.
 *
 * @author Gregory Borelli
 */
public class GB_BOPilotageTNDemo extends GB_TNDemoAbstract {

    public GB_BOPilotageTNDemo() {
    }

    public static void main(String[] args) {
        GB_FwkGuiInitializer.initAll();
        GB_VMDemo l_demo = new GB_BOPilotageTNDemo();
        l_demo.show(true);
    }

    protected DefaultMutableTreeNode buildRoot() {
        File l_file = new File("C:/TEMP/pilotage/remax-qc.enum.bo.xml");
        try {
            GB_SimpleBusinessObject[] l_bos = GB_BOFactoryTools.getFactory().loadFromXmlFile(l_file);
            GB_BOMetaDataFactoryTools.getFactory().addBOMetaData((GB_BOMetaData) l_bos[0]);
        } catch (GB_LoadException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        GB_BOPilotageModel l_model = new GB_BOPilotageModelFile(l_file);
        GB_BOPilotageOptions l_options = new GB_BOPilotageOptionsImpl();
        DefaultMutableTreeNode l_root = new GB_BOPilotageTN(l_model, l_options);
        return l_root;
    }

    public Class getDemoClass() {
        return GB_BOPilotageTN.class;
    }

    public boolean unregister() {
        return true;
    }
}
