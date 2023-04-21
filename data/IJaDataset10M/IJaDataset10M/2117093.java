package ti.plato.ui.views.manager.util;

public class Convert {

    public static String backwardCompatibility_convertFromNameToSecondaryId(String name) {
        return name.replace(":", "<colon>");
    }

    public static String backwardCompatibility_convertFromSecondaryIdToName(String secondaryId) {
        return secondaryId.replace("<colon>", ":");
    }

    public static String convertFromFileToSecondaryId(String file) {
        file = file.replace(".sp1.", "\\");
        file = file.replace(".sp2.", "/");
        file = file.replace(".sp3.", ":");
        file = file.replace(".sp4.", "*");
        file = file.replace(".sp5.", "?");
        file = file.replace(".sp6.", "\"");
        file = file.replace(".sp7.", "<");
        file = file.replace(".sp8.", ">");
        file = file.replace(".sp9.", "|");
        return file;
    }

    public static String convertFromSecondaryIdToFile(String secondaryId) {
        secondaryId = secondaryId.replace("\\", ".sp1.");
        secondaryId = secondaryId.replace("/", ".sp2.");
        secondaryId = secondaryId.replace(":", ".sp3.");
        secondaryId = secondaryId.replace("*", ".sp4.");
        secondaryId = secondaryId.replace("?", ".sp5.");
        secondaryId = secondaryId.replace("\"", ".sp6.");
        secondaryId = secondaryId.replace("<", ".sp7.");
        secondaryId = secondaryId.replace(">", ".sp8.");
        secondaryId = secondaryId.replace("|", ".sp9.");
        return secondaryId;
    }
}
