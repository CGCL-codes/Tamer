package org.fao.geonet.kernel.mef;

import static org.fao.geonet.kernel.mef.MEFConstants.FILE_INFO;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.zip.ZipFile;
import jeeves.exceptions.BadFormatEx;
import jeeves.interfaces.Logger;
import jeeves.utils.Log;
import jeeves.utils.Xml;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.util.ISODate;
import org.fao.geonet.util.ZipUtil;
import org.jdom.Element;

public class MEF2Visitor implements Visitor {

    public void visit(File mefFile, MEFVisitor v) throws Exception {
        Element info = handleXml(mefFile, v);
    }

    /**
	 * TODO : use log system instead of system.out
	 */
    public Element handleXml(File mefFile, MEFVisitor v) throws Exception {
        Logger log = Log.createLogger(Geonet.MEF);
        int nbMetadata = 0;
        Element md = null;
        Element fc = null;
        Element info = new Element("info");
        File unzipDir = new File(mefFile.getParentFile(), "unzipping");
        if (unzipDir.exists()) ZipUtil.deleteAllFiles(unzipDir);
        ZipUtil.extract(new ZipFile(mefFile), unzipDir);
        File metadata = getMetadataDirectory(unzipDir);
        if (metadata.getParentFile().equals(unzipDir)) {
            log.debug("metadata folder is directly under the unzip temporary folder.");
        } else {
            File[] lstmdDir = metadata.getParentFile().getParentFile().listFiles();
            for (int i = 0; i < lstmdDir.length; i++) {
                File file = lstmdDir[i];
                if (file != null && file.isDirectory()) {
                    File metadataDir = new File(file, "metadata");
                    if (metadataDir == null) throw new BadFormatEx("Missing metadata folder in MEF file " + mefFile.getName() + ". Maybe the file is in MEF v1 format?");
                    File[] xmlFiles = metadataDir.listFiles();
                    if (xmlFiles == null || xmlFiles.length < 1) throw new BadFormatEx("Missing xml document in metadata folder in MEF file " + mefFile.getName() + ".");
                    File fileInfo = new File(file, FILE_INFO);
                    if (fileInfo.exists()) {
                        info = Xml.loadFile(fileInfo);
                    }
                    v.handleMetadataFiles(xmlFiles, nbMetadata);
                    v.handleFeatureCat(fc, nbMetadata);
                    v.handleInfo(info, nbMetadata);
                    handleBin(file, v, info, nbMetadata);
                    nbMetadata++;
                }
            }
        }
        ZipUtil.deleteAllFiles(unzipDir);
        return info;
    }

    public void handleBin(File file, MEFVisitor v, Element info, int index) throws Exception {
        List pubFiles = null;
        List prvFiles = null;
        if (info.getChildren().size() != 0) {
            pubFiles = info.getChild("public").getChildren();
            prvFiles = info.getChild("private").getChildren();
        }
        File publicFile = new File(file, MEFConstants.DIR_PUBLIC);
        File privateFile = new File(file, MEFConstants.DIR_PRIVATE);
        File overviewsFile = new File(file, MEFConstants.DIR_OVERVIEWS);
        String fname;
        if (publicFile.exists() && pubFiles.size() != 0) {
            File[] files = publicFile.listFiles();
            for (File f : files) {
                fname = f.getName();
                v.handlePublicFile(fname, getChangeDate(pubFiles, fname), new FileInputStream(f), index);
            }
        }
        if (privateFile.exists() && prvFiles.size() != 0) {
            File[] files = privateFile.listFiles();
            for (File f : files) {
                fname = f.getName();
                v.handlePrivateFile(fname, getChangeDate(prvFiles, fname), new FileInputStream(f), index);
            }
        }
        if (overviewsFile.exists()) {
            File[] files = overviewsFile.listFiles();
            for (File f : files) {
                String date = new ISODate().getDate() + "T" + new ISODate().getTime();
                v.handlePublicFile(f.getName(), date, new FileInputStream(f), index);
            }
        }
    }

    /**
	 * getFeatureCalalogFile method return feature catalog xml file if exists
	 * @param file
	 * @return File
	 */
    private File getFeatureCalalogFile(File file) {
        File tmp = null;
        File fcRepo = new File(file, MEFConstants.SCHEMA);
        if (fcRepo.exists()) {
            File fc = new File(fcRepo, MEFConstants.FILE_METADATA);
            if (fc.exists()) tmp = fc; else {
                File[] files = fcRepo.listFiles();
                if (files.length != 0) tmp = files[0];
            }
        }
        return tmp;
    }

    private File getMetadataDirectory(File dir) {
        File metadata = new File(dir, "metadata");
        if (!(metadata.exists() && metadata.isDirectory())) {
            File[] list = dir.listFiles();
            for (int i = 0; i < list.length; i++) {
                File file = list[i];
                if (file.isDirectory()) {
                    metadata = getMetadataDirectory(file);
                }
            }
        }
        return metadata;
    }

    private static String getChangeDate(List files, String fileName) throws Exception {
        for (Object f : files) {
            Element file = (Element) f;
            String name = file.getAttributeValue("name");
            String date = file.getAttributeValue("changeDate");
            if (name.equals(fileName)) return date;
        }
        throw new Exception("File not found in info.xml : " + fileName);
    }
}
