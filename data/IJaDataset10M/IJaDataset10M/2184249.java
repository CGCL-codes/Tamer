package net.sourceforge.hlagile.fomgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EnumeratedDataCodeGenerator extends FOM_CodeGenerator {

    public void loadFomDataTypes(Document document, String basePackageName) {
        System.out.println("Loading Enumerated Data Typs from FOM...");
        this.FOM = document;
        this.packageName = basePackageName + ".enumeratedDataRepresentations";
        NodeList enumeratedDataTypes = FOM.getElementsByTagName("enumeratedData");
        if (enumeratedDataTypes.getLength() == 0) return;
        for (int i = 0; i < enumeratedDataTypes.getLength(); i++) {
            Element enumeratedData = (Element) enumeratedDataTypes.item(i);
            String name = enumeratedData.getAttribute("name");
            if (!name.startsWith("HLA")) FOM_Util.addFomDataType(name, packageName + "." + name); else FOM_Util.addFomDataType(name, FOM_Util.getInstance().hlaEncoderPackage + name);
        }
    }

    public void generateSourceCode(String outputFolder) {
        System.out.println("Generating Enumerated Data Type source code...");
        createOutputFolder(outputFolder);
        NodeList enumeratedDataTypes = FOM.getElementsByTagName("enumeratedData");
        if (enumeratedDataTypes.getLength() == 0) return;
        for (int i = 0; i < enumeratedDataTypes.getLength(); i++) {
            Element enumeratedData = (Element) enumeratedDataTypes.item(i);
            String name = enumeratedData.getAttribute("name");
            if (!name.startsWith("HLA")) writeSourceCode(enumeratedData);
        }
    }

    public void writeSourceCode(Element enumeratedData) {
        String name = enumeratedData.getAttribute("name");
        String nameNotes = enumeratedData.getAttribute("nameNotes");
        String semantics = enumeratedData.getAttribute("semantics");
        String representation = enumeratedData.getAttribute("representation");
        String fileName = this.outputFolderName + "/" + name + ".java";
        File outputFile = new File(fileName);
        try {
            outputFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write("package " + packageName + ";\r\n");
            writer.write("\r\n");
            writer.write("import " + FOM_Util.getInstance().hlaEncoderPackage + "DataElement;\r\n");
            writer.write("import " + FOM_Util.getInstance().hlaEncoderPackage + "ByteWrapper;\r\n");
            writer.write("import " + FOM_Util.getInstance().rtiExceptionPackage + "\r\n");
            writer.write("import " + FOM_Util.getInstance().hlaEncoderPackage + "DecoderException;\r\n");
            if (representation.startsWith("HLA")) {
                writer.write("import " + FOM_Util.getInstance().omtEncoderPackage + "\r\n");
                writer.write("import " + FOM_Util.getInstance().encoderFactoryPackage + "\r\n");
                writer.write("import " + FOM_Util.getInstance().hlaEncoderPackage + "" + representation + ";\r\n");
            } else if (FOM_Util.FomDataTypeExists(representation)) writer.write("import " + FOM_Util.getFomPackageName(representation) + ";\r\n");
            writer.write("\r\n");
            writer.write("public class " + name + " implements DataElement\r\n");
            writer.write("{\r\n");
            writer.write("\r\n");
            writer.write("  private " + representation + " value;\r\n");
            writer.write("  private static EncoderFactory ef;\r\n");
            writer.write("\r\n");
            HashSet<String> enumSet = new HashSet<String>();
            NodeList enumerators = enumeratedData.getElementsByTagName("enumerator");
            for (int i = 0; i < enumerators.getLength(); i++) {
                Element enumerator = (Element) enumerators.item(i);
                String attrName = enumerator.getAttribute("name");
                String attrValue = enumerator.getAttribute("values");
                if (enumSet.contains(attrName)) writer.write("//*** duplicate enumerator");
                enumSet.add(attrName);
                writer.write("  public static final " + representation + " " + attrName + " = ef.create" + representation + "((" + getJavaType(representation) + ") " + attrValue + ");\r\n");
            }
            writer.write("\r\n");
            writer.write("  public " + name + "() throws RTIinternalError\r\n");
            writer.write("  {\r\n");
            writer.write("    ef = RtiFactoryFactory.getRtiFactory().getEncoderFactory();\r\n");
            writer.write("  }\r\n");
            writer.write("\r\n");
            writer.write("  public " + representation + " get()\r\n");
            writer.write("  {\r\n");
            writer.write("    return value;\r\n");
            writer.write("  }\r\n");
            writer.write("\r\n");
            writer.write("  public void set(" + representation + " newValue)\r\n");
            writer.write("  {\r\n");
            writer.write("    value = newValue;\r\n");
            writer.write("  }\r\n");
            writer.write("\r\n");
            writer.write("  public void decode(byte[] newValue) throws DecoderException\r\n");
            writer.write("  {\r\n");
            writer.write("    value.decode(newValue);\r\n");
            writer.write("  }\r\n");
            writer.write("\r\n");
            writer.write("  public void decode(ByteWrapper byteWrapper) throws DecoderException\r\n");
            writer.write("  {\r\n");
            writer.write("    value.decode(byteWrapper);\r\n");
            writer.write("  }\r\n");
            writer.write("\r\n");
            writer.write("  public void encode(ByteWrapper byteWrapper)\r\n");
            writer.write("  {\r\n");
            writer.write("    value.encode(byteWrapper);\r\n");
            writer.write("  }\r\n");
            writer.write("\r\n");
            writer.write("  public int getEncodedLength()\r\n");
            writer.write("  {\r\n");
            writer.write("    return value.getEncodedLength();\r\n");
            writer.write("  }\r\n");
            writer.write("\r\n");
            writer.write("  public int getOctetBoundary()\r\n");
            writer.write("  {\r\n");
            writer.write("    return value.getOctetBoundary();\r\n");
            writer.write("  }\r\n");
            writer.write("\r\n");
            writer.write("  public byte[] toByteArray()\r\n");
            writer.write("  {\r\n");
            writer.write("    return value.toByteArray();\r\n");
            writer.write("  }\r\n");
            writer.write("\r\n");
            writer.write("}\r\n");
            writer.write("\r\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getJavaType(String representation) {
        if (!representation.startsWith("HLA")) return representation; else {
            if (representation.contains("float32")) return "float"; else if (representation.contains("float64")) return "double"; else if (representation.contains("integer16")) return "short"; else if (representation.contains("integer32")) return "int"; else if (representation.contains("char")) return "String"; else if (representation.contains("octet")) return "byte"; else return representation;
        }
    }
}
