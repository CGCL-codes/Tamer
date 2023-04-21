package com.testonica.kickelhahn.core.formats.common;

import java.io.File;
import java.util.HashMap;
import com.testonica.common.settings.ui.SettingsPanel;
import com.testonica.common.utils.FileUtils;
import com.testonica.kickelhahn.core.KickelhahnInfo;
import com.testonica.kickelhahn.core.formats.bsdl.BSDLFormatInformation;
import com.testonica.kickelhahn.core.formats.nl.NLFormatInformation;
import com.testonica.kickelhahn.core.formats.simple.HTMLFormatInformation;
import com.testonica.kickelhahn.core.formats.simple.ImageFormatInformation;
import com.testonica.kickelhahn.core.formats.ssbdd.BDDFormatInformation;
import com.testonica.kickelhahn.core.formats.svf.SVFFormatInformation;
import com.testonica.kickelhahn.core.manager.project.ProjectFormatInformation;

public class FileInformator {

    private DefaultFormatInformation defaultFormatInformation;

    private BSDLFormatInformation bsdlFormatInformation;

    private SVFFormatInformation svfFormatInformation;

    private NLFormatInformation nlFormatInformation;

    private BDDFormatInformation bddFormatInformation;

    private ImageFormatInformation imageFormatInformation;

    private ProjectFormatInformation projectFormatInformation;

    private HTMLFormatInformation htmlFormatInformation;

    private HashMap<String, FormatInformation> fileInformations = new HashMap<String, FormatInformation>();

    public FileInformator(KickelhahnInfo info) {
        defaultFormatInformation = new DefaultFormatInformation(info);
        bsdlFormatInformation = new BSDLFormatInformation(info);
        svfFormatInformation = new SVFFormatInformation(info);
        nlFormatInformation = new NLFormatInformation(info);
        bddFormatInformation = new BDDFormatInformation(info);
        imageFormatInformation = new ImageFormatInformation(info);
        projectFormatInformation = new ProjectFormatInformation(info);
        htmlFormatInformation = new HTMLFormatInformation(info);
        fileInformations.put("", defaultFormatInformation);
        fileInformations.put("BSDL", bsdlFormatInformation);
        fileInformations.put("BSD", bsdlFormatInformation);
        fileInformations.put("BSM", bsdlFormatInformation);
        fileInformations.put("SVF", svfFormatInformation);
        fileInformations.put("NL", nlFormatInformation);
        fileInformations.put("AGM", bddFormatInformation);
        fileInformations.put("JPEG", imageFormatInformation);
        fileInformations.put("JPG", imageFormatInformation);
        fileInformations.put("BMP", imageFormatInformation);
        fileInformations.put("PNG", imageFormatInformation);
        fileInformations.put("TIFF", imageFormatInformation);
        fileInformations.put("GIF", imageFormatInformation);
        fileInformations.put("HTML", htmlFormatInformation);
        fileInformations.put("HTM", htmlFormatInformation);
        fileInformations.put("PROJECT.PROPERTIES", projectFormatInformation);
    }

    public String getFileInfoString(File file) {
        if (file == null) return "";
        String extension = FileUtils.getExtension(file).toUpperCase();
        if (fileInformations.get(extension) == null) return defaultFormatInformation.getInformationString(file);
        return fileInformations.get(extension).getInformationString(file);
    }

    public SettingsPanel getFileInfoPanel(File file) {
        if ((file == null) || (!file.exists()) || (file.isDirectory())) return null;
        String extension = FileUtils.getExtension(file).toUpperCase();
        if (fileInformations.get(extension) == null) return null;
        return fileInformations.get(extension).getInformationPanel(file);
    }

    public String getTooltip(File file) {
        if (file == null) return "";
        String extension = FileUtils.getExtension(file).toUpperCase();
        if (fileInformations.get(extension) == null) return "";
        return fileInformations.get(extension).getInformationTooltip(file);
    }

    public BDDFormatInformation getBDDFormatInformation() {
        return bddFormatInformation;
    }

    public BSDLFormatInformation getBSDLFormatInformation() {
        return bsdlFormatInformation;
    }

    public DefaultFormatInformation getDefaultFormatInformation() {
        return defaultFormatInformation;
    }

    public HashMap<String, FormatInformation> getFileInformations() {
        return fileInformations;
    }

    public ImageFormatInformation getImageFormatInformation() {
        return imageFormatInformation;
    }

    public HTMLFormatInformation getHTMLFormatInformation() {
        return htmlFormatInformation;
    }

    public NLFormatInformation getNLFormatInformation() {
        return nlFormatInformation;
    }

    public ProjectFormatInformation getProjectFormatInformation() {
        return projectFormatInformation;
    }

    public SVFFormatInformation getSVFFormatInformation() {
        return svfFormatInformation;
    }
}
