package com.volantis.mcs.build.themes;

import com.volantis.mcs.build.GenerateUtilities;
import java.io.File;
import java.io.PrintStream;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Code generator for the KeywordMapperAccessor class.
 */
public class KeywordMapperAccessorGenerator extends AbstractThemeClassGenerator {

    /**
     * Name of the class that is being generated
     */
    private static String CLASS_NAME = "KeywordMapperAccessor";

    /**
     * Creates a new <code>KeywordMapperAccessorGenerator</code> instance.
     * @param generatedDir the directory were generated classes will be located.
     * @param packageName the genertated classes package name.
     */
    public KeywordMapperAccessorGenerator(File generatedDir, String packageName) {
        super(generatedDir, packageName);
    }

    /**
     * Generates the StylePropertyDetails class
     * @param propertyInfo provides details of the keyword mappers
     */
    public void generate(StylePropertyInfo[] propertyInfo) {
        System.out.println("  Generating the " + CLASS_NAME + " class");
        StringBuffer buffer = new StringBuffer();
        buffer.append(packageName).append(".").append(CLASS_NAME);
        String qualifiedClassName = buffer.toString();
        PrintStream out = openFileForClass(qualifiedClassName);
        GenerateUtilities.writeHeader(out, this.getClass().getName());
        out.println();
        out.println("package " + packageName + ";");
        SortedSet imports = new TreeSet();
        imports.add("java.util.Map");
        imports.add("java.util.HashMap");
        imports.add("com.volantis.mcs.themes.mappers.KeywordMapper");
        imports.add("com.volantis.mcs.themes.mappers.KeywordMapperFactory");
        GenerateUtilities.writeImports(out, imports);
        GenerateUtilities.writeJavaDocComment(out, "", new String[] { "Class that wraps a {@link KeywordMapperFactory} so that a", "KeywordMapper can be retrieved for a given property name" });
        out.println("public class " + CLASS_NAME + " {");
        GenerateUtilities.writeJavaDocComment(out, "    ", new String[] { "A map that will contain the property to " + "KeywordMapperFactory mappings" });
        out.println("    private Map properties;");
        writeConstructor(out, propertyInfo);
        writeGetKeywordMapperMethod(out);
        out.println("}");
    }

    /**
     * Generates the constructor
     * @param out the <code>PrintStream</code> to write to
     * @param propertyInfo the array of StylePropertyInfo objects
     */
    private void writeConstructor(PrintStream out, StylePropertyInfo[] propertyInfo) {
        GenerateUtilities.writeJavaDocComment(out, "    ", new String[] { "Creates a new <code>KeywordMapperAccessorGenerator" + "</code> instance", "@param factory the <code>KeywordMapperFactory</code> that " + "is to be wrapped" });
        StringBuffer buffer = new StringBuffer();
        buffer.append("    public ").append(CLASS_NAME).append("(KeywordMapperFactory factory) {");
        out.println(buffer.toString());
        out.println("        // initialize the property map");
        out.println("        properties = new HashMap();");
        for (int i = 0; i < propertyInfo.length; i++) {
            String enumName = propertyInfo[i].getEnumerationName();
            if (enumName != null) {
                String property = propertyInfo[i].getPropertyName();
                out.println("        // add the " + property + " property to the map");
                out.println("        properties.put(\"" + property + "\",");
                out.println("                       factory.get" + GenerateUtilities.getTitledString(enumName) + "KeywordMapper());");
            }
        }
        out.println("        // TODO the remaing code in this constructor is " + "hard coded in the");
        out.println("        // TODO  generator. It should be obtained from " + "the schema");
        out.println("        // add the background-position.first property " + "to the map");
        out.println("        properties.put(\"background-position.first\",");
        out.println("                       factory.getBackgroundXPosition" + "KeywordMapper());");
        out.println("        // add the background-position.second property " + "to the map");
        out.println("        properties.put(\"background-position.second\",");
        out.println("                       factory.getBackgroundYPosition" + "KeywordMapper());");
        out.println("        // add the mcs-mmflash-scaled-align.first " + "property to the map");
        out.println("        properties.put(\"mcs-mmflash-scaled-align.first\",");
        out.println("                       factory.getMarinerMMFlashXScaled" + "AlignKeywordMapper());");
        out.println("        // add the mcs-mmflash-scaled-align.second " + "property to the map");
        out.println("        properties.put(\"mcs-mmflash-scaled-align.second\",");
        out.println("                       factory.getMarinerMMFlashYScaled" + "AlignKeywordMapper());");
        out.println("    }");
    }

    /**
     * Generates the getKeywordMapper method
     * @param out the <code>PrintStream</code> to write to
     */
    private void writeGetKeywordMapperMethod(PrintStream out) {
        GenerateUtilities.writeJavaDocComment(out, "    ", new String[] { "Returns a <code>KeywordMapper<code> for the given property", "@param property the property", "@return a KeywordMapperFactory or null if the property does " + "not have an associated KeywordMapper" });
        out.println("    public KeywordMapper getKeywordMapper" + "(String property) {");
        out.println("        return (KeywordMapper) properties.get(property);");
        out.println("    }");
    }
}
