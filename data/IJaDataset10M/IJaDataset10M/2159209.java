package sketch.generator;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import randoop.util.Files;
import sketch.SketchSequence;
import sketch.ast.ASTFormatter;
import sketch.util.Globals;

public class JUnitTestCreator {

    public static boolean format = true;

    public static void createJUnitTest(String testCollection, String dir, String fileName, List<ImportDeclaration> imports) {
        createJUnitTest(testCollection, dir, fileName, null, imports);
    }

    public static File createJUnitTest(String testCollection, String dir, String fileName, String packageName, List<ImportDeclaration> imports) {
        String[] import_strs = new String[imports.size()];
        for (int i = 0; i < imports.size(); i++) {
            import_strs[i] = imports.get(i).toString();
        }
        return createJUnitTest(testCollection, dir, fileName, packageName, import_strs);
    }

    public static File createJUnitTest(String testCollection, String dir, String fileName, String packageName, List<ImportDeclaration> imports, boolean remove_sketch_import) {
        String[] import_strs = new String[imports.size()];
        for (int i = 0; i < imports.size(); i++) {
            import_strs[i] = imports.get(i).toString();
        }
        return createJUnitTest(testCollection, dir, fileName, packageName, import_strs, false);
    }

    public static File createJUnitTest(String testCollection, String dir, String fileName, String[] imports) {
        return createJUnitTest(testCollection, dir, fileName, null, imports);
    }

    public static File createJUnitTest(String testCollection, String dir, String fileName, String packageName, String[] imports) {
        return createJUnitTest(testCollection, dir, fileName, packageName, imports, true);
    }

    public static File createJUnitTest(String testCollection, String dir, String fileName, String packageName, String[] imports, boolean remove_sketch_import) {
        StringBuilder sb = new StringBuilder();
        if (packageName != null) {
            sb.append("package " + packageName + ";");
        }
        for (String ip : imports) {
            if (ip.split(" ")[1].trim().startsWith("sketch.") && remove_sketch_import) {
                continue;
            }
            sb.append(ip);
        }
        sb.append(Globals.lineSep);
        sb.append(Globals.lineSep);
        sb.append("import junit.framework.TestCase;");
        sb.append(Globals.lineSep);
        sb.append("public class " + fileName + " extends TestCase {");
        sb.append(Globals.lineSep);
        sb.append(testCollection);
        sb.append("}");
        sb.append(Globals.lineSep);
        try {
            File file = new File(dir + "/" + fileName + ".java");
            String formatWholeFile = sb.toString();
            if (format) {
                try {
                    formatWholeFile = ASTFormatter.formatUnit(sb.toString());
                } catch (Exception e) {
                    System.err.println("Format error!");
                }
            }
            Files.writeToFile(formatWholeFile, file);
            System.out.println("Write to file: " + file.getAbsolutePath());
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("No file is created please check permission");
    }

    public static File createJUnitTest(Collection<SketchSequence> collection, String dir, String fileName, String[] imports) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (SketchSequence sequence : collection) {
            String testStr = createSingleTest(sequence, "test" + (count++));
            sb.append(testStr);
        }
        return createJUnitTest(sb.toString(), dir, fileName, imports);
    }

    public static String createSingleTest(SketchSequence sequence, String junitName) {
        StringBuilder sb = new StringBuilder();
        sb.append("public void " + junitName + "(){");
        sb.append(Globals.lineSep);
        sb.append(indent(sequence.toString(), 4));
        sb.append("}");
        sb.append(Globals.lineSep);
        return sb.toString();
    }

    public static String indent(String line, int num) {
        String space = "";
        for (int i = 0; i < num; i++) {
            space = "" + " ";
        }
        String[] lines = line.split(Globals.lineSep);
        String[] afterIndent = new String[lines.length];
        int count = 0;
        for (String singleLine : lines) {
            afterIndent[count] = space + singleLine;
            count++;
        }
        StringBuilder sb = new StringBuilder();
        for (String after : afterIndent) {
            sb.append(after);
            sb.append(Globals.lineSep);
        }
        return sb.toString();
    }
}
