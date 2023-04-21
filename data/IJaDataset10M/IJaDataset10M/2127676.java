package org.equanda.tool.export.configuration;

import org.equanda.tool.shared.ToolException;
import org.equanda.tool.shared.configuration.Section;
import org.equanda.util.IniFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

/**
 * Configuration class for export application - section "settings"
 *
 * @author NetRom team
 */
public class ExportSectionSettings extends Section {

    /** section name */
    private static final String INI_SECTION_SETTINGS = "settings";

    /** Settings section attributes */
    public static final String INI_SETTINGS_SECTION_ATTRIBUTE_EXPORTDIRECTORY = "exportDirectory";

    public static final String INI_SETTINGS_SECTION_ATTRIBUTE_SCANPERIOD = "scanPeriod";

    public static final String INI_SETTINGS_SECTION_ATTRIBUTE_EXPORT_INFO = "exportInfo";

    public static final String INI_SETTINGS_SECTION_ATTRIBUTE_EXPORT_INI = "exportIni";

    public static final String INI_SETTINGS_SECTION_ATTRIBUTE_EXPORT_SCRIPT = "script";

    public static final String INI_OBJECT_SECTION_ATTRIBUTE_SCRIPT = "script";

    private long scanPeriod;

    private String exportInfo;

    private IniFile exportIni;

    private String exportDirectory;

    private Collection<String> evalScripts;

    public ExportSectionSettings(IniFile ini, String sectionPrefix) throws ToolException {
        super(ini, sectionPrefix);
    }

    public void initDefaults() throws ToolException {
        scanPeriod = -1;
        exportInfo = EMPTY_STRING;
        exportIni = null;
        exportDirectory = ".";
    }

    public void loadSection(IniFile ini, String resolvedSectionId) throws ToolException {
        Hashtable<String, String> section = ini.getSection(resolvedSectionId);
        if (section == null) return;
        String value;
        value = section.get(INI_SETTINGS_SECTION_ATTRIBUTE_SCANPERIOD);
        try {
            if (value != null) scanPeriod = Long.parseLong(value);
        } catch (Exception ignore) {
        }
        value = section.get(INI_SETTINGS_SECTION_ATTRIBUTE_EXPORT_INFO);
        if (value != null) exportInfo = value;
        value = section.get(INI_SETTINGS_SECTION_ATTRIBUTE_EXPORT_INI);
        exportIni = ini;
        if (value != null) {
            try {
                exportIni = new IniFile(value, true);
            } catch (IOException e) {
                throw new ToolException("Cannot load exportIni " + value, e);
            }
        }
        value = section.get(INI_SETTINGS_SECTION_ATTRIBUTE_EXPORTDIRECTORY);
        if (value != null && !value.equals(EMPTY_STRING)) exportDirectory = value;
        evalScripts = new ArrayList<String>();
        boolean hasMore = true;
        for (int index = 1; hasMore; index++) {
            String name = ini.getValue(resolvedSectionId, INI_OBJECT_SECTION_ATTRIBUTE_SCRIPT + index);
            if (name == null || name.equals("")) {
                hasMore = false;
            } else {
                evalScripts.add(name);
            }
        }
    }

    public String getSectionId() {
        return INI_SECTION_SETTINGS;
    }

    /** Getters for the database section attributes */
    public long getScanPeriod() {
        return scanPeriod;
    }

    public String getExportInfo() {
        return exportInfo;
    }

    public IniFile getExportIni() {
        return exportIni;
    }

    public String getExportDirectory() {
        return exportDirectory;
    }

    /** Get list of scripts names which can be used to extend the ExportContext evaluator */
    public Collection<String> getScripts() {
        return evalScripts;
    }
}
