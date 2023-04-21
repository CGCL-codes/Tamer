package net.sourceforge.modelintegra.core.metamodel.extension.util;

import net.sourceforge.modelintegra.core.configuration.ConfigurationHelper;
import net.sourceforge.modelintegra.core.data.DataSingleton;
import net.sourceforge.modelintegra.core.metamodel.mimodel.MimodelPackage;
import net.sourceforge.modelintegra.core.metamodel.mimodel.Model;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import java.io.File;
import java.io.IOException;

public class XMIHelper {

    private static final Logger LOGGER = Logger.getLogger(XMIHelper.class.getName());

    /**
	 * Writes the model to an file in XMI format
	 */
    public static void exportXMI(String pFileName) {
        LOGGER.debug("start");
        DataSingleton ds = DataSingleton.getInstance();
        try {
            ResourceSet resourceSet = new ResourceSetImpl();
            resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
            resourceSet.getPackageRegistry().put(MimodelPackage.eNS_URI, MimodelPackage.eINSTANCE);
            org.eclipse.emf.common.util.URI fileURI = org.eclipse.emf.common.util.URI.createFileURI(new File(ConfigurationHelper.REPORT_OUTPUT_DIRECTORY + "content/" + pFileName).getAbsolutePath());
            Resource aResource = resourceSet.createResource(fileURI);
            ((XMLResource) aResource).setEncoding("ISO-8859-1");
            aResource.getContents().add(ds.getMimodel());
            aResource.save(null);
            aResource.unload();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.debug("end");
    }

    /**
	 * Loads the model from an file in XMI format
	 */
    public static void importXMI() {
        DataSingleton ds = DataSingleton.getInstance();
        try {
            ResourceSet resourceSet = new ResourceSetImpl();
            resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
            resourceSet.getPackageRegistry().put(MimodelPackage.eNS_URI, MimodelPackage.eINSTANCE);
            URI fileURI = URI.createFileURI(new File(ConfigurationHelper.REPORT_OUTPUT_DIRECTORY + "content/mi.xmi").getAbsolutePath());
            Resource aResource = resourceSet.getResource(fileURI, true);
            ds.setMimodel((Model) aResource.getContents().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
