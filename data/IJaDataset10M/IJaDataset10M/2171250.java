package toolers.Core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import toolers.Tools;
import toolers.beans.Table;

/**
 *
 * @author Praca
 */
class DictionaryChooserFactory {

    static void generateDictionaryChooser(Object[] selectedValues, File dictionaryFolder, String packages, int mode) throws Exception {
        for (int i = 0; i < selectedValues.length; i++) {
            try {
                Table table = (Table) selectedValues[i];
                String domainName = Tools.convertTablesIntoDomainFileNames(table.getName());
                File domainFile = Tools.prepareFile(dictionaryFolder, domainName + "DictionaryChooser.java");
                generateDictionary(table, domainName, domainFile, packages, mode);
            } catch (CreatingFileException ex) {
                continue;
            }
        }
    }

    private static void generateDictionary(Table table, String domainName, File domainFile, String packages, int mode) {
        try {
            FileWriter fw = new FileWriter(domainFile);
            fw.write("\n");
            fw.write("package " + packages + "." + FileGenerator.CLIENT_FOLDER_NAME + "." + FileGenerator.DICTIONARIES_FOLDER_NAME + ";\n");
            fw.write("\n");
            fw.write("import " + FileGenerator.DictionaryChooserImpl_CLASS_PATH + ";\n");
            fw.write("import " + FileGenerator.Dictionaryable_CLASS_PATH + ";\n");
            fw.write("import " + packages + "." + FileGenerator.SHARED_FOLDER_NAME + "." + Tools.modeSelector(mode, FileGenerator.DOMAINS_FOLDER_NAME, FileGenerator.DTOS_FOLDER_NAME) + "." + domainName + ";\n");
            fw.write("\n");
            fw.write("public class " + domainName + "DictionaryChooser extends DictionaryChooserImpl<" + domainName + ",Long> {\n");
            fw.write("\n");
            fw.write("\n");
            fw.write("    @Override\n");
            fw.write("    public " + FileGenerator.Dictionaryable_CLASS_NAME + "<" + domainName + "> getDictionary() {\n");
            fw.write("        return new " + domainName + "Dictionary();\n");
            fw.write("    }\n");
            fw.write("}\n");
            fw.flush();
        } catch (IOException iOException) {
        }
    }
}
