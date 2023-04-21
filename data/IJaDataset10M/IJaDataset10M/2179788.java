package net.sf.osadm.docbook.plugin.wizards.releasenotes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NewReleaseChapterModel {

    public static final String WHAT_IS_NEW = "what is new";

    public static final String UPGRADE_NOTES = "upgrade notes";

    public static final String CHANGES_DYNAMIC = "changes-dynamic";

    public static final String CHANGES_STATIC = "changes-static";

    public static final String COMPONENTS_AFFECTED = "components affected";

    public static final String DATA_MODEL_CHANGES = "data-model changes";

    public static final String MAPPING_RULE_CHANGES = "mapping-rule changes";

    public static final String MAPPING_TABLE_CHANGES = "mapping-table changes";

    public static final String FILE_TYPE_DOCBOOK = "docbook";

    public static final String FILE_TYPE_TSV = "tsv";

    private Map<String, Boolean> isRequiredMap = new HashMap<String, Boolean>();

    private Map<String, String> fileNameMap = new HashMap<String, String>();

    private Map<String, String> fileTypeMap = new HashMap<String, String>();

    private static Set<String> fileTypeSet = new HashSet<String>();

    private static final List<String> sectionList = new ArrayList<String>();

    static {
        sectionList.add(WHAT_IS_NEW);
        sectionList.add(UPGRADE_NOTES);
        sectionList.add(CHANGES_DYNAMIC);
        sectionList.add(CHANGES_STATIC);
        sectionList.add(COMPONENTS_AFFECTED);
        sectionList.add(DATA_MODEL_CHANGES);
        sectionList.add(MAPPING_RULE_CHANGES);
        sectionList.add(MAPPING_TABLE_CHANGES);
        fileTypeSet.add(FILE_TYPE_TSV);
        fileTypeSet.add(FILE_TYPE_DOCBOOK);
    }

    public static List<String> getSectionList() {
        return sectionList;
    }

    public static Set<String> getFileTypeSet() {
        return fileTypeSet;
    }

    public void addSection(String section) {
        isRequiredMap.put(section, Boolean.TRUE);
    }

    public void addSection(String section, String fileType, String fileName) {
        isRequiredMap.put(section, Boolean.TRUE);
        fileTypeMap.put(section, fileType);
        fileNameMap.put(section, fileName);
    }

    public Boolean isSectionRequired(String section) {
        if (isRequiredMap.get(section) == null) {
            return Boolean.FALSE;
        }
        return isRequiredMap.get(section);
    }

    public String getFileType(String section) {
        return fileTypeMap.get(section);
    }

    public String getFileName(String section) {
        return fileNameMap.get(section);
    }
}
