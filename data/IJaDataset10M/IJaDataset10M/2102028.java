package com.foursoft.fourever.draw.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.foursoft.component.Config;
import com.foursoft.component.exception.ComponentInternalException;
import com.foursoft.component.util.ImageFileDescription;
import com.foursoft.fourever.draw.DrawManager;
import com.foursoft.fourever.draw.ImageFileProvider;
import com.foursoft.fourever.draw.VModellUtil;
import com.foursoft.fourever.objectmodel.ObjectModel;
import com.foursoft.fourever.vmodell.regular.Projekttypvariante;
import com.foursoft.fourever.vmodell.regular.VModellManager;
import com.foursoft.fourever.vmodell.regular.Vorgehensbaustein;
import com.foursoft.fourever.xmlfileio.Document;
import com.foursoft.fourever.xmlfileio.XMLFileIOManager;
import com.foursoft.fourever.xmlfileio.exception.DocumentLockedException;
import com.foursoft.fourever.xmlfileio.exception.SchemaProcessingException;
import com.foursoft.fourever.xmlfileio.exception.XMLProcessingException;
import de.tuc.in.sse.weit.export.openoffice.OpenOfficeManager;

/**
 * @author sihling
 * 
 */
public class DrawManagerImpl implements DrawManager {

    /** singleton component instance */
    private static DrawManager instance = null;

    private static VModellManager vmodellManager = null;

    private static XMLFileIOManager xmlManager = null;

    private static OpenOfficeManager ooManager = null;

    private static DrawConfigImpl config = new DrawConfigImpl();

    /** log for all component implementation classes */
    public static Log log = null;

    public Config getConfig() {
        return config;
    }

    public static DrawManager getInstance() {
        return instance;
    }

    public static DrawConfigImpl getTypedConfig() {
        return config;
    }

    public void setConfig(Config config) {
        assert (config instanceof DrawConfigImpl);
        DrawManagerImpl.config = (DrawConfigImpl) config;
    }

    /**
	 * Create the component manager, thus initializing the component.
	 * 
	 * @param myLog
	 *            the Log to use
	 * @return the singleton instance of the ObjectModelManager
	 */
    public static DrawManager createInstance(XMLFileIOManager xmlManager, VModellManager vmodellManager, OpenOfficeManager ooManager, Log myLog) {
        if (instance != null) {
            throw new ComponentInternalException("Could not initialize the component manager - already initialized");
        }
        instance = new DrawManagerImpl();
        DrawManagerImpl.xmlManager = xmlManager;
        DrawManagerImpl.vmodellManager = vmodellManager;
        DrawManagerImpl.ooManager = ooManager;
        if (myLog == null) {
            throw new ComponentInternalException("Could not initialize the log");
        }
        log = myLog;
        return instance;
    }

    public static VModellManager getVModellManager() {
        return vmodellManager;
    }

    public static XMLFileIOManager getXMLManager() {
        return xmlManager;
    }

    public static OpenOfficeManager getOOManager() {
        return ooManager;
    }

    public void destroy() {
        log.debug("Destroying the DrawManager.");
    }

    /**
	 * Returns a image provider for the given xml model
	 * 
	 * @param xmlModel
	 *            the object model as xml file
	 */
    public ImageFileProvider<Projekttypvariante> getPDSImageProvider(File xmlModel) throws IOException {
        assert (xmlManager != null);
        Document vmodell = null;
        try {
            vmodell = xmlManager.openDocument(xmlModel, false, false);
        } catch (SchemaProcessingException ex) {
            String message = "Cannot open and process model in file " + xmlModel;
            log.error(message, ex);
            throw new IOException(message);
        } catch (XMLProcessingException ex) {
            String message = "Cannot open and process model in file " + xmlModel;
            log.error(message, ex);
            throw new IOException(message);
        } catch (DocumentLockedException ex) {
            String message = "Document is locked: " + xmlModel;
            log.error(message, ex);
            throw new IOException(message);
        }
        return getPDSImageProvider(vmodell.getObjectModel());
    }

    public ImageFileProvider<Projekttypvariante> getPDSImageProvider(ObjectModel model) {
        if (model != null) {
            return new PDSImageProviderImpl(model);
        } else {
            return null;
        }
    }

    /**
	 * Returns a image provider for the given xml model
	 * 
	 * @param xmlModel
	 *            the object model as xml file
	 */
    public ImageFileProvider<Vorgehensbaustein> getVBImageProvider(File xmlModel) throws IOException {
        assert (xmlManager != null);
        Document vmodell = null;
        try {
            vmodell = xmlManager.openDocument(xmlModel, false, false);
        } catch (SchemaProcessingException ex) {
            String message = "Cannot open and process model in file " + xmlModel;
            log.error(message, ex);
            throw new IOException(message);
        } catch (XMLProcessingException ex) {
            String message = "Cannot open and process model in file " + xmlModel;
            log.error(message, ex);
            throw new IOException(message);
        } catch (DocumentLockedException ex) {
            String message = "Document is locked: " + xmlModel;
            log.error(message, ex);
            throw new IOException(message);
        }
        return getVBImageProvider(vmodell.getObjectModel());
    }

    public ImageFileProvider<Vorgehensbaustein> getVBImageProvider(ObjectModel model) {
        if (model != null) {
            return new VBImageProviderImpl(model);
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        if (args.length < 6 || !args[0].equals("-f") || !args[2].equals("-p") || (!args[4].equals("-o") & !args[4].equals("-d"))) {
            System.err.println("Usage: DrawManagerImpl -f <V-Modell-XT.xml> -p <path-to-soffice.exe> [-o <output-directory> | -d] [-vbs | -ptvs]");
            System.err.println("       -o and -d are alternatives. If set to -d, the design mode is started, where no images files");
            System.err.println("       are created, but the layout templates are overwritten.");
            System.exit(1);
        }
        ClassPathXmlApplicationContext appContext = null;
        Log log = LogFactory.getLog("com.foursoft.fourever.draw");
        File vmxt = new File(args[1]);
        if (!vmxt.exists() || vmxt.isDirectory() || !vmxt.canRead()) {
            System.err.println("Cannot access file " + args[1] + " - aborting.");
            System.exit(1);
        }
        File ooFile = new File(args[3]);
        if (!ooFile.exists()) {
            System.err.println("Cannot find OpenOffice : " + args[5]);
            System.exit(1);
        }
        File outDir = null;
        int argOffset = 6;
        if (args[4].equals("-d")) {
            File dummy = null;
            argOffset = 5;
            try {
                dummy = File.createTempFile("dummy", ".txt");
                dummy.deleteOnExit();
                outDir = new File(dummy.getParent());
            } catch (IOException e) {
                System.err.println("Cannot create a temporary file!");
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            outDir = new File(args[5]);
            if (!outDir.exists() || !outDir.isDirectory()) {
                System.err.println("Output directory " + args[3] + " does not exist.");
                System.exit(1);
            }
        }
        boolean createVBs = "-vbs".equalsIgnoreCase(args[argOffset]);
        try {
            appContext = new ClassPathXmlApplicationContext("generator.xml");
        } catch (BeansException ex) {
            log.error("Could not initialize 4Ever framework - aborting.", ex);
            System.exit(1);
        }
        assert (appContext != null);
        DrawManager dManager = (DrawManager) appContext.getBean("drawmanager");
        DrawManagerImpl.ooManager.setOpenOfficePath(ooFile.toString());
        if (args[4].equals("-d")) ((DrawConfigImpl) dManager.getConfig()).setLayoutOverwrite(true);
        List<File> createdFiles = new ArrayList<File>();
        if (createVBs) {
            try {
                ImageFileProvider<Vorgehensbaustein> vbProvider = dManager.getVBImageProvider(vmxt);
                VModellUtil.setVorgehensbausteine(vbProvider.getPossibleElements());
                for (Vorgehensbaustein vb : vbProvider.getPossibleElements()) {
                    System.out.println("Creating picture for Vorgehensbaustein " + vb.getName() + " (#" + vb.getId() + ")");
                    File outFile = new File(outDir, "VB_" + vb.getId() + "." + dManager.getVBImageFileDescription().getFormatExtension());
                    createdFiles.addAll(vbProvider.getImage(vb, outFile, dManager.getVBImageFileDescription()));
                }
            } catch (IOException ex) {
                log.error("Can't create temp image file - aborting.", ex);
                System.exit(1);
            }
        } else {
            try {
                ImageFileProvider<Projekttypvariante> ptvProvider = dManager.getPDSImageProvider(vmxt);
                for (Projekttypvariante ptv : ptvProvider.getPossibleElements()) {
                    log.debug("Creating picture for Projekttypvariante " + ptv.getName() + " (#" + ptv.getId() + ")");
                    File outFile = new File(outDir, "PTV_" + ptv.getId() + "." + dManager.getPTVImageFileDescription().getFormatExtension());
                    HashMap<String, Double> options = new HashMap<String, Double>();
                    options.put(PDSImageProviderImpl.SCALEFACTOR_KEY, dManager.getSnippetScaleFactor());
                    if (args[4].equals("-d")) {
                        options.put("nosnippets", 1.0);
                    }
                    createdFiles.addAll(ptvProvider.getImage(ptv, outFile, dManager.getPTVImageFileDescription(), options));
                }
            } catch (IOException ex) {
                log.error("Can't create image file - aborting.", ex);
                System.exit(1);
            }
        }
        if (args[4].equals("-d")) {
            for (File f : createdFiles) {
                f.delete();
            }
        }
        appContext.close();
    }

    public ImageFileDescription getPTVImageFileDescription() {
        return config.getImageFileDescriptionPTV();
    }

    public ImageFileDescription getPTVImageFileDescriptionLow() {
        return config.getImageFileDescriptionPTVLow();
    }

    public ImageFileDescription getVBImageFileDescription() {
        return config.getImageFileDescriptionVB();
    }

    public ImageFileDescription getVBImageFileDescriptionLow() {
        return config.getImageFileDescriptionVBLow();
    }

    public double getSnippetScaleFactor() {
        return config.getSnippetScaleFactor();
    }

    public double getSnippetScaleFactorLow() {
        return config.getSnippetScaleFactorLow();
    }
}
