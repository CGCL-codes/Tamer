package edu.nps.moves.xmlpg;

import java.io.*;
import java.util.*;

/**
 * Generates the c++ language source code files needed to read and write a protocol described
 * by an XML file. This is a counterpart to the JavaGenerator. This should generate .h and
 * .cpp files with ivars, getters, setters, marshaller, unmarshaler, constructors, and
 * destructors.
 *
 * @author DMcG
 * @author Andrew Sampson
 */
public class CppGenerator extends Generator {

    /**
     * ivars are often preceded by a special character. This sets what that character is, 
     * so that instance variable names will be preceded by a "_".
     */
    public static final String IVAR_PREFIX = "_";

    /** Maps the primitive types listed in the XML file to the cpp types */
    Properties types = new Properties();

    /** What primitive types should be marshalled as. This may be different from
    * the cpp get/set methods, ie an unsigned short might have ints as the getter/setter,
    * but is marshalled as a short.
    */
    Properties marshalTypes = new Properties();

    HashMap<ClassAttribute.ClassAttributeType, CppAttributeGenerator> writers = new HashMap<ClassAttribute.ClassAttributeType, CppAttributeGenerator>();

    public CppGenerator(List<GeneratedClass> pClassDescriptions, HashMap<String, GeneratedEnumeration> pEnumerations, String pDirectory, Properties pCppProperties, String pLicenseStatement) {
        super(pClassDescriptions, pEnumerations, pDirectory, pCppProperties, pLicenseStatement);
        types.setProperty("unsigned short", "unsigned short");
        types.setProperty("unsigned byte", "unsigned char");
        types.setProperty("unsigned int", "unsigned int");
        types.setProperty("byte", "char");
        types.setProperty("short", "short");
        types.setProperty("int", "int");
        types.setProperty("long", "long");
        types.setProperty("double", "double");
        types.setProperty("float", "float");
        marshalTypes.setProperty("unsigned short", "unsigned short");
        marshalTypes.setProperty("unsigned byte", "unsigned char");
        marshalTypes.setProperty("unsigned int", "unsigned int");
        marshalTypes.setProperty("byte", "char");
        marshalTypes.setProperty("short", "short");
        marshalTypes.setProperty("int", "int");
        marshalTypes.setProperty("long", "long");
        marshalTypes.setProperty("double", "double");
        marshalTypes.setProperty("float", "float");
        writers.put(ClassAttribute.ClassAttributeType.PADDING, new CppPaddingGenerator());
        writers.put(ClassAttribute.ClassAttributeType.PRIMITIVE, new CppPrimitiveGenerator());
        writers.put(ClassAttribute.ClassAttributeType.CLASSREF, new CppClassRefGenerator());
        writers.put(ClassAttribute.ClassAttributeType.ENUMREF, new CppEnumRefGenerator());
        writers.put(ClassAttribute.ClassAttributeType.FIXED_LIST, new CppFixedLengthListGenerator());
        writers.put(ClassAttribute.ClassAttributeType.VARIABLE_LIST, new CppVariableLengthListGenerator());
        writers.put(ClassAttribute.ClassAttributeType.BITFIELD, new CppBitfieldGenerator());
        writers.put(ClassAttribute.ClassAttributeType.BOOLEAN, new CppBooleanGenerator());
        writers.put(ClassAttribute.ClassAttributeType.UNSET, null);
    }

    protected String getOutputSubdirectoryName() {
        return "cpp";
    }

    /**
     * Generates the cpp source code classes
     */
    public void writeClasses() {
        this.createDirectory();
        this.writeMacroFile();
        Iterator it = classDescriptions.iterator();
        while (it.hasNext()) {
            try {
                GeneratedClass aClass = (GeneratedClass) it.next();
                this.writeHeaderFile(aClass);
                this.writeCppFile(aClass);
            } catch (Exception e) {
                System.out.println("error creating source code: " + e);
            }
        }
    }

    /**
     * Microsoft C++ requires a macro file to generate dlls. The preprocessor will import this and
     * resolve it to an empty string in the gcc/unix world. In the Microsoft C++ world, the macro
     * will resolve and do something useful about creating libraries.
     */
    public void writeMacroFile() {
        System.out.println("Creating microsoft library macro file");
        String headerFile = "msLibMacro";
        try {
            String headerFullPath = directory + "/" + headerFile + ".h";
            File outputFile = new File(headerFullPath);
            outputFile.createNewFile();
            PrintWriter pw = new PrintWriter(outputFile);
            String libMacro = languageProperties.getProperty("microsoftLibMacro");
            String library = languageProperties.getProperty("microsoftLibDef");
            pw.println("#ifndef " + headerFile.toUpperCase() + "_H");
            pw.println("#define " + headerFile.toUpperCase() + "_H");
            pw.println("#if defined(_MSC_VER) || defined(__CYGWIN__) || defined(__MINGW32__) || defined( __BCPLUSPLUS__)  || defined( __MWERKS__)");
            pw.println("#  ifdef EXPORT_LIBRARY");
            pw.println("#    define " + "EXPORT_MACRO" + " __declspec(dllexport)");
            pw.println("#  else");
            pw.println("#    define EXPORT_MACRO  __declspec(dllimport)");
            pw.println("#  endif");
            pw.println("#else");
            pw.println("#  define " + "EXPORT_MACRO");
            pw.println("#endif");
            pw.println("#endif");
            pw.flush();
            pw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
 * Generate a c++ header file for the classes
 */
    public void writeHeaderFile(GeneratedClass aClass) {
        try {
            String name = aClass.getName();
            System.out.println("Creating C++ header file for " + name);
            String headerFullPath = directory + "/" + name + ".h";
            File outputFile = new File(headerFullPath);
            outputFile.createNewFile();
            PrintWriter pw = new PrintWriter(outputFile);
            pw.println("/*");
            pw.println(licenseStatement);
            pw.println("*/");
            pw.println();
            pw.println("#ifndef " + aClass.getName().toUpperCase() + "_H");
            pw.println("#define " + aClass.getName().toUpperCase() + "_H");
            pw.println();
            String namespace = languageProperties.getProperty("namespace");
            if (namespace == null) namespace = ""; else namespace = namespace + "/";
            pw.println("#include <vector>");
            pw.println("#include <iostream>");
            pw.println("#include <" + namespace + "DataStream.h>");
            String msMacroFile = "msLibMacro";
            if (msMacroFile != null) {
                pw.println("#include <" + namespace + msMacroFile + ".h>");
            }
            pw.println();
            if (aClass.getParentClass() != null) {
                pw.println("#include <" + namespace + aClass.getParentClass().getName() + ".h>");
            }
            for (int idx = 0; idx < aClass.getClassAttributes().size(); idx++) {
                ClassAttribute anAttribute = (ClassAttribute) aClass.getClassAttributes().get(idx);
                CppAttributeGenerator writer = writers.get(anAttribute.getAttributeKind());
                writer.writeIncludeStatements(pw, aClass, anAttribute, namespace);
            }
            pw.println();
            pw.println();
            namespace = languageProperties.getProperty("namespace");
            if (namespace != null) {
                pw.println("namespace " + namespace);
                pw.println("{");
            }
            if (aClass.getComment() != null && !aClass.getComment().equals("")) {
                pw.println("//! " + aClass.getComment());
            }
            String macroName = languageProperties.getProperty("microsoftLibMacro");
            if (aClass.getParentClass() == null) pw.println("class EXPORT_MACRO " + aClass.getName()); else pw.println("class EXPORT_MACRO " + aClass.getName() + " : public " + aClass.getParentClass().getName());
            pw.println("{");
            pw.println("\npublic:");
            writeEnumerations(pw, aClass);
            pw.println("\t" + aClass.getName() + "();");
            pw.println("\tvirtual ~" + aClass.getName() + "();");
            pw.println();
            for (int idx = 0; idx < aClass.getClassAttributes().size(); idx++) {
                ClassAttribute anAttribute = (ClassAttribute) aClass.getClassAttributes().get(idx);
                CppAttributeGenerator writer = writers.get(anAttribute.getAttributeKind());
                writer.writeGetterMethodPrototype(pw, aClass, anAttribute);
            }
            pw.println();
            for (int idx = 0; idx < aClass.getClassAttributes().size(); idx++) {
                ClassAttribute anAttribute = (ClassAttribute) aClass.getClassAttributes().get(idx);
                CppAttributeGenerator writer = writers.get(anAttribute.getAttributeKind());
                writer.writeSetterMethodPrototype(pw, aClass, anAttribute);
            }
            pw.println();
            pw.println("\tvirtual void marshal(DataStream& dataStream);");
            pw.println("\tvirtual void unmarshal(DataStream& dataStream);");
            pw.println();
            pw.println("\tbool operator ==(const " + aClass.getName() + "& rhs) const;");
            pw.println("\tvirtual unsigned int length() const;");
            pw.println("\nprotected:");
            for (int idx = 0; idx < aClass.getClassAttributes().size(); idx++) {
                ClassAttribute anAttribute = (ClassAttribute) aClass.getClassAttributes().get(idx);
                CppAttributeGenerator writer = writers.get(anAttribute.getAttributeKind());
                writer.writeDeclaration(pw, aClass, anAttribute);
            }
            pw.println("\n};");
            if (namespace != null) {
                pw.println("\n}");
            }
            pw.println("\n#endif");
            pw.flush();
            pw.close();
        } catch (Exception e) {
            System.out.println("While writing C++ header file for class \"" + aClass.getName() + "\", encountered exception:");
            System.out.println(e);
        }
    }

    public void writeCppFile(GeneratedClass aClass) {
        try {
            String name = aClass.getName();
            System.out.println("Creating C++ source file for " + name);
            String headerFullPath = directory + "/" + name + ".cpp";
            File outputFile = new File(headerFullPath);
            outputFile.createNewFile();
            PrintWriter pw = new PrintWriter(outputFile);
            pw.println("/*");
            pw.println(licenseStatement);
            pw.println("*/");
            pw.println();
            String namespace = languageProperties.getProperty("namespace");
            if (namespace == null) namespace = ""; else namespace = namespace + "/";
            pw.println("#include <" + namespace + aClass.getName() + ".h>");
            pw.println();
            namespace = languageProperties.getProperty("namespace");
            if (namespace != null) {
                pw.println("using namespace " + namespace + ";\n");
            }
            pw.println();
            this.writeCtor(pw, aClass);
            this.writeDtor(pw, aClass);
            for (int idx = 0; idx < aClass.getClassAttributes().size(); idx++) {
                ClassAttribute anAttribute = (ClassAttribute) aClass.getClassAttributes().get(idx);
                CppAttributeGenerator writer = writers.get(anAttribute.getAttributeKind());
                writer.writeGetterMethodBody(pw, aClass, anAttribute);
            }
            for (int idx = 0; idx < aClass.getClassAttributes().size(); idx++) {
                ClassAttribute anAttribute = (ClassAttribute) aClass.getClassAttributes().get(idx);
                CppAttributeGenerator writer = writers.get(anAttribute.getAttributeKind());
                writer.writeSetterMethodBody(pw, aClass, anAttribute);
            }
            this.writeMarshalMethod(pw, aClass);
            this.writeUnmarshalMethod(pw, aClass);
            this.writeEqualityOperator(pw, aClass);
            writeLengthMethod(pw, aClass);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            System.out.println("While writing C++ source file for class \"" + aClass.getName() + "\", encountered exception:");
            System.out.println(e);
        }
    }

    /**
 * Write the code for an equality operator. This allows you to compare
 * two objects for equality.
 * The code should look like
 * 
 * bool operator ==(const ClassName& rhs)
 * return (_ivar1==rhs._ivar1 && _var2 == rhs._ivar2 ...)
 *
 */
    public void writeEqualityOperator(PrintWriter pw, GeneratedClass aClass) {
        try {
            pw.println();
            pw.println("bool " + aClass.getName() + "::operator ==(const " + aClass.getName() + "& rhs) const");
            pw.println("{");
            pw.println("\tbool ivarsEqual = true;");
            pw.println();
            GeneratedClass parentClass = aClass.getParentClass();
            if (parentClass != null) {
                pw.println("\tivarsEqual = " + parentClass.getName() + "::operator==(rhs);");
                pw.println();
            }
            for (int idx = 0; idx < aClass.getClassAttributes().size(); idx++) {
                ClassAttribute anAttribute = (ClassAttribute) aClass.getClassAttributes().get(idx);
                CppAttributeGenerator writer = writers.get(anAttribute.getAttributeKind());
                writer.writeEqualityTestStatement(pw, aClass, anAttribute);
            }
            pw.println();
            pw.println("\treturn ivarsEqual;");
            pw.println("}");
        } catch (Exception e) {
            System.out.println("While writing equality method for class \"" + aClass.getName() + "\", encountered exception:");
            System.out.println(e);
        }
    }

    /**
 * Write the code for a method that marshals out the object into a DIS format
 * byte array.
 */
    public void writeMarshalMethod(PrintWriter pw, GeneratedClass aClass) {
        try {
            pw.println();
            pw.println("void " + aClass.getName() + "::" + "marshal(DataStream& dataStream)");
            pw.println("{");
            if (aClass.getParentClass() != null) {
                String superclassName = aClass.getParentClass().getName();
                pw.println("\t// marshal information in superclass first");
                pw.println("\t" + superclassName + "::marshal(dataStream);");
                pw.println();
            }
            for (int idx = 0; idx < aClass.getClassAttributes().size(); idx++) {
                ClassAttribute anAttribute = (ClassAttribute) aClass.getClassAttributes().get(idx);
                CppAttributeGenerator writer = writers.get(anAttribute.getAttributeKind());
                writer.writeMarshallingStatement(pw, aClass, anAttribute);
            }
            pw.println("}");
        } catch (Exception e) {
            System.out.println("While writing marshalling C++ code for class \"" + aClass.getName() + "\", encountered exception:");
            System.out.println(e);
        }
    }

    public void writeUnmarshalMethod(PrintWriter pw, GeneratedClass aClass) {
        try {
            pw.println();
            pw.println("void " + aClass.getName() + "::" + "unmarshal(DataStream& dataStream)");
            pw.println("{");
            if (aClass.getParentClass() != null) {
                String superclassName = aClass.getParentClass().getName();
                pw.println("\t// unmarshal information in superclass first");
                pw.println("\t" + superclassName + "::unmarshal(dataStream);");
                pw.println();
            }
            for (int idx = 0; idx < aClass.getClassAttributes().size(); idx++) {
                ClassAttribute anAttribute = (ClassAttribute) aClass.getClassAttributes().get(idx);
                CppAttributeGenerator writer = writers.get(anAttribute.getAttributeKind());
                writer.writeUnmarshallingStatement(pw, aClass, anAttribute);
            }
            pw.println("}");
        } catch (Exception e) {
            System.out.println("While writing unmarshalling C++ code for class \"" + aClass.getName() + "\", encountered exception:");
            System.out.println(e);
        }
    }

    protected void writeLengthMethod(PrintWriter pw, GeneratedClass aClass) {
        pw.println();
        pw.println("unsigned int " + aClass.getName() + "::length() const");
        pw.println("{");
        pw.println("\tunsigned int result = 0;");
        if (aClass.getParentClass() != null) pw.println("\tresult += " + aClass.getParentClass().getName() + "::length();");
        for (ClassAttribute attr : aClass.getClassAttributes()) {
            CppAttributeGenerator writer = writers.get(attr.getAttributeKind());
            if (writer != null) {
                writer.writeLengthRetrievalStatement(pw, aClass, attr);
            } else {
                System.out.println("While writing length() method in C++ code for class \"" + aClass.getName() + "\", could not find a writer for attribute \"" + attr.getName() + "\" (which is of attribute type \"" + attr.getAttributeKind() + "\")");
            }
        }
        pw.println("\treturn result;");
        pw.println("}\n");
    }

    /** 
 * Write a constructor. This uses an initialization list to initialize the various object
* ivars in the class. God, C++ is a PITA. The result should be something like
* Foo::Foo() : bar(Bar(), baz(Baz()
*/
    protected void writeCtor(PrintWriter pw, GeneratedClass aClass) {
        pw.print(aClass.getName() + "::" + aClass.getName() + "()");
        if (aClass.getParentClass() != null) {
            pw.println(" : " + aClass.getParentClass().getName() + "()");
        } else {
            pw.println();
        }
        pw.println("{");
        for (ClassAttribute anAttribute : aClass.getAttributeOverrides()) {
            CppAttributeGenerator writer = writers.get(anAttribute.getAttributeKind());
            writer.writeInitializationStatement(pw, aClass, anAttribute);
        }
        for (ClassAttribute anAttribute : aClass.getClassAttributes()) {
            CppAttributeGenerator writer = writers.get(anAttribute.getAttributeKind());
            writer.writeInitializationStatement(pw, aClass, anAttribute);
        }
        pw.println("}\n");
    }

    /**
 * Generate a destructor method, which deallocates objects
 */
    protected void writeDtor(PrintWriter pw, GeneratedClass aClass) {
        pw.println(aClass.getName() + "::~" + aClass.getName() + "()");
        pw.println("{");
        pw.println("}\n");
    }

    /**  */
    protected void writeEnumerations(PrintWriter pw, GeneratedClass aClass) {
        List enumList = aClass.getClassEnumerations();
        Iterator iterator = enumList.iterator();
        while (iterator.hasNext()) {
            GeneratedEnumeration e = (GeneratedEnumeration) iterator.next();
            if (e.getComment() != null && !e.getComment().equals("")) pw.println("\t//! " + e.getComment());
            pw.println("\tenum " + formatEnumTypeName(e.getName()));
            pw.println("\t{");
            List namesValues = e.getNameValuePairs();
            Iterator pairIter = namesValues.iterator();
            while (pairIter.hasNext()) {
                GeneratedEnumeration.EnumPair pair = (GeneratedEnumeration.EnumPair) pairIter.next();
                pw.print("\t\t" + formatEnumValueName(e.getName(), pair.name) + " = " + Integer.toString(pair.value));
                if (pairIter.hasNext()) pw.println(","); else pw.println();
            }
            pw.println("\t};");
            pw.println();
        }
    }

    protected String formatVariableName(String unadornedName) {
        return IVAR_PREFIX + unadornedName;
    }

    protected String formatEnumTypeName(String enumType) {
        return enumType + "Enum";
    }

    protected String formatEnumValueName(String enumType, String value) {
        return enumType + value;
    }

    protected abstract class CppAttributeGenerator {

        public abstract void writeIncludeStatements(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute, String path);

        public abstract void writeDeclaration(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute);

        public abstract void writeInitializationStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute);

        public abstract void writeGetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute);

        public abstract void writeSetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute);

        public abstract void writeGetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute);

        public abstract void writeSetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute);

        public abstract void writeMarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute);

        public abstract void writeUnmarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute);

        public abstract void writeEqualityTestStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute);

        public abstract void writeLengthRetrievalStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute);

        /** 
	 * returns a string with the first letter capitalized. 
	 */
        public String initialCap(String aString) {
            StringBuffer stb = new StringBuffer(aString);
            stb.setCharAt(0, Character.toUpperCase(aString.charAt(0)));
            return new String(stb);
        }
    }

    protected class CppPaddingGenerator extends CppAttributeGenerator {

        public void writeIncludeStatements(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute, String path) {
        }

        public void writeDeclaration(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
        }

        public void writeInitializationStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
        }

        public void writeGetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
        }

        public void writeSetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
        }

        public void writeGetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
        }

        public void writeSetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
        }

        public void writeMarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            PaddingAttribute padding = (PaddingAttribute) anAttribute;
            pw.println("\tdataStream << " + "( (" + marshalTypes.getProperty(padding.getPaddingType()) + ")" + padding.getDefaultValue() + " ); // padding");
        }

        public void writeUnmarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            PaddingAttribute padding = (PaddingAttribute) anAttribute;
            pw.println("\t{\n\t\t// skip over some padding");
            pw.println("\t\t" + marshalTypes.getProperty(padding.getPaddingType()) + " temp;");
            pw.println("\t\tdataStream >> temp;");
            pw.println("\t}");
        }

        public void writeEqualityTestStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
        }

        public void writeLengthRetrievalStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            PaddingAttribute padding = (PaddingAttribute) anAttribute;
            pw.println("\tresult += sizeof( " + marshalTypes.getProperty(padding.getPaddingType()) + " ); // padding");
        }
    }

    protected class CppPrimitiveGenerator extends CppAttributeGenerator {

        public void writeIncludeStatements(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute, String path) {
        }

        public void writeDeclaration(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            PrimitiveAttribute primitive = (PrimitiveAttribute) anAttribute;
            if (primitive.getComment() != null && !primitive.getComment().equals("")) pw.println("\t//! " + primitive.getComment());
            pw.println("\t" + types.get(primitive.getPrimitiveType()) + " " + formatVariableName(primitive.getName()) + ";");
            pw.println();
        }

        public void writeInitializationStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            PrimitiveAttribute primitive = (PrimitiveAttribute) anAttribute;
            String defaultValue = primitive.getDefaultValue();
            String initialValue = "0";
            String ivarType = primitive.getPrimitiveType();
            if (ivarType.equalsIgnoreCase("float") || ivarType.equalsIgnoreCase("double")) initialValue = "0.0";
            if (defaultValue != null) initialValue = defaultValue;
            pw.println("\t" + formatVariableName(primitive.getName()) + " = " + initialValue + ";");
        }

        public void writeGetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            PrimitiveAttribute primitive = (PrimitiveAttribute) anAttribute;
            pw.println("\t" + types.get(primitive.getPrimitiveType()) + " " + "get" + initialCap(primitive.getName()) + "() const;");
        }

        public void writeSetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            PrimitiveAttribute primitive = (PrimitiveAttribute) anAttribute;
            pw.println("\tvoid " + "set" + initialCap(primitive.getName()) + "(" + types.get(primitive.getPrimitiveType()) + " pX);");
        }

        public void writeGetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            PrimitiveAttribute primitive = (PrimitiveAttribute) anAttribute;
            pw.println(types.get(primitive.getPrimitiveType()) + " " + aClass.getName() + "::" + "get" + initialCap(primitive.getName()) + "() const");
            pw.println("{");
            pw.println("\treturn " + formatVariableName(primitive.getName()) + ";");
            pw.println("}\n");
        }

        public void writeSetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            PrimitiveAttribute primitive = (PrimitiveAttribute) anAttribute;
            pw.println("void " + aClass.getName() + "::" + "set" + initialCap(primitive.getName()) + "(" + types.get(primitive.getPrimitiveType()) + " pX)");
            pw.println("{");
            pw.println("\t" + formatVariableName(primitive.getName()) + " = pX;");
            pw.println("}\n");
        }

        public void writeMarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            pw.println("\tdataStream << " + formatVariableName(anAttribute.getName()) + ";");
        }

        public void writeUnmarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            pw.println("\tdataStream >> " + formatVariableName(anAttribute.getName()) + ";");
        }

        public void writeEqualityTestStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            pw.println("\tif( ! (" + formatVariableName(anAttribute.getName()) + " == rhs." + formatVariableName(anAttribute.getName()) + ") ) ivarsEqual = false;");
        }

        public void writeLengthRetrievalStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            PrimitiveAttribute primitive = (PrimitiveAttribute) anAttribute;
            pw.println("\tresult += sizeof( " + marshalTypes.getProperty(primitive.getPrimitiveType()) + " ); // " + formatVariableName(primitive.getName()));
        }
    }

    protected class CppClassRefGenerator extends CppAttributeGenerator {

        public void writeIncludeStatements(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute, String path) {
            ClassRefAttribute classRef = (ClassRefAttribute) anAttribute;
            pw.println("#include <" + path + classRef.getClassType() + ".h>");
        }

        public void writeDeclaration(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            ClassRefAttribute classRef = (ClassRefAttribute) anAttribute;
            if (classRef.getComment() != null && !classRef.getComment().equals("")) pw.println("\t//! " + classRef.getComment());
            pw.println("\t" + classRef.getClassType() + " " + formatVariableName(classRef.getName()) + ";");
            pw.println();
        }

        public void writeInitializationStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
        }

        public void writeGetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            ClassRefAttribute classRef = (ClassRefAttribute) anAttribute;
            pw.println("\t" + classRef.getClassType() + "& " + "get" + initialCap(classRef.getName()) + "();");
            pw.println("\tconst " + classRef.getClassType() + "& get" + initialCap(classRef.getName()) + "() const;");
        }

        public void writeSetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            ClassRefAttribute classRef = (ClassRefAttribute) anAttribute;
            pw.println("\tvoid set" + initialCap(classRef.getName()) + "(" + classRef.getClassType() + " &pX);");
        }

        public void writeGetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            ClassRefAttribute classRef = (ClassRefAttribute) anAttribute;
            pw.println(classRef.getClassType() + "& " + aClass.getName() + "::" + "get" + initialCap(classRef.getName()) + "()");
            pw.println("{");
            pw.println("\treturn " + formatVariableName(classRef.getName()) + ";");
            pw.println("}\n");
            pw.println("const " + classRef.getClassType() + "& " + aClass.getName() + "::" + "get" + initialCap(classRef.getName()) + "() const");
            pw.println("{");
            pw.println("\treturn " + formatVariableName(classRef.getName()) + ";");
            pw.println("}\n");
        }

        public void writeSetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            ClassRefAttribute classRef = (ClassRefAttribute) anAttribute;
            pw.println("void " + aClass.getName() + "::" + "set" + initialCap(classRef.getName()) + "(" + classRef.getClassType() + " &pX)");
            pw.println("{");
            pw.println("\t" + formatVariableName(classRef.getName()) + " = pX;");
            pw.println("}\n");
        }

        public void writeMarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            pw.println("\t" + formatVariableName(anAttribute.getName()) + ".marshal( dataStream );");
        }

        public void writeUnmarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            pw.println("\t" + formatVariableName(anAttribute.getName()) + ".unmarshal( dataStream );");
        }

        public void writeEqualityTestStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            pw.println("\tif( ! (" + formatVariableName(anAttribute.getName()) + " == rhs." + formatVariableName(anAttribute.getName()) + ") ) ivarsEqual = false;");
        }

        public void writeLengthRetrievalStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            pw.println("\tresult += " + formatVariableName(anAttribute.getName()) + ".length(); // " + formatVariableName(anAttribute.getName()));
        }
    }

    protected class CppEnumRefGenerator extends CppAttributeGenerator {

        public void writeIncludeStatements(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute, String path) {
        }

        public void writeDeclaration(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            EnumRefAttribute enumRef = (EnumRefAttribute) anAttribute;
            if (enumRef.getComment() != null && !enumRef.getComment().equals("")) {
                pw.println("\t//! " + enumRef.getComment());
            }
            pw.println("\t" + formatEnumTypeName(enumRef.getEnumName()) + " " + formatVariableName(enumRef.getName()) + ";\n");
        }

        public void writeInitializationStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            EnumRefAttribute enumRef = (EnumRefAttribute) anAttribute;
            pw.println("\t" + formatVariableName(enumRef.getName()) + " = " + aClass.getName() + "::" + formatEnumValueName(enumRef.getEnumName(), enumRef.getDefaultValue()) + ";");
        }

        public void writeGetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            EnumRefAttribute enumRef = (EnumRefAttribute) anAttribute;
            pw.println("\t" + formatEnumTypeName(enumRef.getEnumName()) + " " + "get" + initialCap(enumRef.getName()) + "() const;");
        }

        public void writeSetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            EnumRefAttribute enumRef = (EnumRefAttribute) anAttribute;
            pw.println("\tvoid " + "set" + initialCap(enumRef.getName()) + "( " + formatEnumTypeName(enumRef.getEnumName()) + " pX );");
        }

        public void writeGetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            EnumRefAttribute enumRef = (EnumRefAttribute) anAttribute;
            pw.println(aClass.getName() + "::" + formatEnumTypeName(enumRef.getEnumName()) + " " + aClass.getName() + "::" + "get" + initialCap(enumRef.getName()) + "() const");
            pw.println("{");
            pw.println("\treturn " + formatVariableName(enumRef.getName()) + ";");
            pw.println("}\n");
        }

        public void writeSetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            EnumRefAttribute enumRef = (EnumRefAttribute) anAttribute;
            pw.println("void " + aClass.getName() + "::" + "set" + initialCap(enumRef.getName()) + "( " + aClass.getName() + "::" + formatEnumTypeName(enumRef.getEnumName()) + " pX )");
            pw.println("{");
            pw.println("\t" + formatVariableName(enumRef.getName()) + " = pX;");
            pw.println("}\n");
        }

        public void writeMarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            EnumRefAttribute enumRef = (EnumRefAttribute) anAttribute;
            String marshalType = marshalTypes.getProperty(enumRef.getSerializedType());
            pw.println("\tdataStream << " + "(" + marshalType + ")" + formatVariableName(enumRef.getName()) + ";");
        }

        public void writeUnmarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            EnumRefAttribute enumRef = (EnumRefAttribute) anAttribute;
            String marshalType = marshalTypes.getProperty(enumRef.getSerializedType());
            pw.println("\t{");
            pw.println("\t\t" + marshalType + " temp = 0;");
            pw.println("\t\tdataStream >> temp;");
            pw.println("\t\t" + formatVariableName(enumRef.getName()) + " = temp;");
            pw.println("\t}");
        }

        public void writeEqualityTestStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            pw.println("\tif( ! (" + formatVariableName(anAttribute.getName()) + " == rhs." + formatVariableName(anAttribute.getName()) + ") ) ivarsEqual = false;");
        }

        public void writeLengthRetrievalStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            EnumRefAttribute enumRef = (EnumRefAttribute) anAttribute;
            pw.println("\tresult += sizeof( " + marshalTypes.getProperty(enumRef.getSerializedType()) + " ); // " + formatVariableName(anAttribute.getName()));
        }
    }

    protected class CppFixedLengthListGenerator extends CppAttributeGenerator {

        public void writeIncludeStatements(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute, String path) {
            FixedListAttribute fixedList = (FixedListAttribute) anAttribute;
            if (!fixedList.getElementTypeIsPrimitive()) {
                pw.println("#include <" + path + fixedList.getListElementType() + ".h>");
            }
        }

        public void writeDeclaration(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            FixedListAttribute fixedList = (FixedListAttribute) anAttribute;
            if (fixedList.getComment() != null && !fixedList.getComment().equals("")) pw.println("\t//! " + fixedList.getComment());
            pw.println("\t" + types.get(fixedList.getListElementType()) + " " + formatVariableName(fixedList.getName()) + "[" + fixedList.getListLength() + "];");
            pw.println();
        }

        public void writeInitializationStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            FixedListAttribute fixedList = (FixedListAttribute) anAttribute;
            if (fixedList.getElementTypeIsPrimitive()) {
                pw.println("\tfor( int i = 0; i < " + fixedList.getListLength() + "; i++ )");
                pw.println("\t{");
                pw.println("\t\t" + formatVariableName(fixedList.getName()) + "[i] = 0;");
                pw.println("\t}");
            }
        }

        public void writeGetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            FixedListAttribute fixedList = (FixedListAttribute) anAttribute;
            String arrayType = this.getArrayType(fixedList.getListElementType());
            pw.println("\t" + arrayType + "*  get" + initialCap(fixedList.getName()) + "();");
            pw.println("\tconst " + arrayType + "*  get" + initialCap(fixedList.getName()) + "() const;");
        }

        public void writeSetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            FixedListAttribute fixedList = (FixedListAttribute) anAttribute;
            String arrayType = this.getArrayType(fixedList.getListElementType());
            pw.println("\tvoid set" + initialCap(fixedList.getName()) + "( " + arrayType + " *pX);");
        }

        public void writeGetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            FixedListAttribute fixedList = (FixedListAttribute) anAttribute;
            pw.println(this.getArrayType(fixedList.getListElementType()) + "* " + aClass.getName() + "::" + "get" + initialCap(fixedList.getName()) + "()");
            pw.println("{");
            pw.println("\treturn " + formatVariableName(fixedList.getName()) + ";");
            pw.println("}\n");
            pw.println("const " + this.getArrayType(fixedList.getListElementType()) + "* " + aClass.getName() + "::" + "get" + initialCap(fixedList.getName()) + "() const");
            pw.println("{");
            pw.println("\treturn " + formatVariableName(fixedList.getName()) + ";");
            pw.println("}\n");
        }

        public void writeSetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            FixedListAttribute fixedList = (FixedListAttribute) anAttribute;
            pw.println("void " + aClass.getName() + "::" + "set" + initialCap(fixedList.getName()) + "(" + this.getArrayType(fixedList.getListElementType()) + "* x)");
            pw.println("{");
            pw.println("\tfor( int i = 0; i < " + fixedList.getListLength() + "; i++ )");
            pw.println("\t{");
            pw.println("\t\t" + formatVariableName(fixedList.getName()) + "[i] = x[i];");
            pw.println("\t}");
            pw.println("}\n");
        }

        public void writeMarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            FixedListAttribute fixedList = (FixedListAttribute) anAttribute;
            pw.println("\tfor( int idx = 0; idx < " + fixedList.getListLength() + "; idx++ )");
            pw.println("\t{");
            if (fixedList.getElementTypeIsPrimitive()) {
                pw.println("\t\tdataStream << " + formatVariableName(fixedList.getName()) + "[idx];");
            } else {
                pw.println("\t\t" + formatVariableName(fixedList.getName()) + "[idx].marshal( dataStream );");
            }
            pw.println("\t}");
        }

        public void writeUnmarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            FixedListAttribute fixedList = (FixedListAttribute) anAttribute;
            pw.println("\tfor( int idx = 0; idx < " + fixedList.getListLength() + "; idx++ )");
            pw.println("\t{");
            if (fixedList.getElementTypeIsPrimitive()) {
                pw.println("\t\tdataStream >> " + formatVariableName(fixedList.getName()) + "[idx];");
            } else {
                pw.println("\t\t" + formatVariableName(fixedList.getName()) + "[idx].unmarshal( dataStream );");
            }
            pw.println("\t}");
        }

        public void writeEqualityTestStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            FixedListAttribute fixedList = (FixedListAttribute) anAttribute;
            pw.println();
            pw.println("\tfor( int idx = 0; idx < " + fixedList.getListLength() + "; idx++ )");
            pw.println("\t{");
            pw.println("\t\tif( !(" + formatVariableName(fixedList.getName()) + "[idx] == rhs." + formatVariableName(fixedList.getName()) + "[idx]) ) ivarsEqual = false;");
            pw.println("\t}");
            pw.println();
        }

        public void writeLengthRetrievalStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            FixedListAttribute fixedList = (FixedListAttribute) anAttribute;
            if (fixedList.getElementTypeIsPrimitive()) {
                pw.println("\tresult += sizeof( " + marshalTypes.getProperty(fixedList.getListElementType()) + " ) * " + fixedList.getListLength() + "; // " + formatVariableName(anAttribute.getName()));
            } else {
                pw.println("\t// " + formatVariableName(anAttribute.getName()));
                pw.println("\tfor( int idx = 0; idx < " + fixedList.getListLength() + "; idx++ )");
                pw.println("\t{");
                pw.println("\t\tresult += " + formatVariableName(fixedList.getName()) + "[idx].length();");
                pw.println("\t}");
            }
        }

        /**
	 * Some code to figure out the characters to use for array types. We may have arrays of either primitives
	 * or classes; this figures out which it is and returns the right string.
	 */
        protected String getArrayType(String xmlType) {
            String marshalType = marshalTypes.getProperty(xmlType);
            if (marshalType == null) {
                return xmlType;
            } else {
                return marshalType;
            }
        }
    }

    protected class CppVariableLengthListGenerator extends CppAttributeGenerator {

        public void writeIncludeStatements(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute, String path) {
            VariableListAttribute variableList = (VariableListAttribute) anAttribute;
            if (!variableList.getElementTypeIsPrimitive()) {
                pw.println("#include <" + path + variableList.getListElementType() + ".h>");
            }
        }

        public void writeDeclaration(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            VariableListAttribute variableList = (VariableListAttribute) anAttribute;
            if (variableList.getComment() != null && !variableList.getComment().equals("")) pw.println("\t//! " + variableList.getComment());
            pw.println("\tstd::vector<" + variableList.getListElementType() + "> " + formatVariableName(variableList.getName()) + ";");
            pw.println();
        }

        public void writeInitializationStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
        }

        public void writeGetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            VariableListAttribute variableList = (VariableListAttribute) anAttribute;
            pw.println("\tstd::vector<" + variableList.getListElementType() + ">& " + "get" + initialCap(variableList.getName()) + "();");
            pw.println("\tconst std::vector<" + variableList.getListElementType() + ">& " + "get" + initialCap(variableList.getName()) + "() const;");
        }

        public void writeSetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            VariableListAttribute variableList = (VariableListAttribute) anAttribute;
            pw.println("\tvoid set" + initialCap(variableList.getName()) + "(const std::vector<" + variableList.getListElementType() + "> &pX);");
        }

        public void writeGetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            VariableListAttribute variableList = (VariableListAttribute) anAttribute;
            pw.println("std::vector<" + variableList.getListElementType() + ">& " + aClass.getName() + "::" + "get" + initialCap(variableList.getName()) + "()");
            pw.println("{");
            pw.println("\treturn " + formatVariableName(variableList.getName()) + ";");
            pw.println("}\n");
            pw.println("const std::vector<" + variableList.getListElementType() + ">& " + aClass.getName() + "::" + "get" + initialCap(variableList.getName()) + "() const");
            pw.println("{");
            pw.println("\treturn " + formatVariableName(variableList.getName()) + ";");
            pw.println("}\n");
        }

        public void writeSetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            VariableListAttribute variableList = (VariableListAttribute) anAttribute;
            pw.println("void " + aClass.getName() + "::" + "set" + initialCap(variableList.getName()) + "(const std::vector<" + variableList.getListElementType() + ">& pX)");
            pw.println("{");
            pw.println("\t" + formatVariableName(variableList.getName()) + " = pX;");
            pw.println("}\n");
        }

        public void writeMarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            VariableListAttribute variableList = (VariableListAttribute) anAttribute;
            pw.println("\tfor( int idx = 0; idx < " + formatVariableName(variableList.getName()) + ".size(); idx++ )");
            pw.println("\t{");
            if (variableList.getElementTypeIsPrimitive()) {
                String marshalType = marshalTypes.getProperty(variableList.getListElementType());
                pw.println("\t\tdataStream << (" + marshalType + ")" + formatVariableName(variableList.getName()) + "[idx];");
            } else {
                pw.println("\t\t" + formatVariableName(variableList.getName()) + "[idx].marshal( dataStream );");
            }
            pw.println("\t}");
        }

        public void writeUnmarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            VariableListAttribute variableList = (VariableListAttribute) anAttribute;
            pw.println("\tfor( int idx = 0; idx < " + formatVariableName(variableList.getCountFieldName()) + "; idx++ )");
            pw.println("\t{");
            if (variableList.getElementTypeIsPrimitive()) {
                String marshalType = marshalTypes.getProperty(variableList.getListElementType());
                pw.println("\t\t" + marshalType + " temp;");
                pw.println("\t\tdataStream >> temp;");
                pw.println("\t\t" + formatVariableName(variableList.getName()) + ".push_back( temp );");
            } else {
                pw.println("\t\t" + variableList.getListElementType() + " temp;");
                pw.println("\t\ttemp.unmarshal( dataStream );");
                pw.println("\t\t" + formatVariableName(variableList.getName()) + ".push_back( temp );");
            }
            pw.println("\t}");
        }

        public void writeEqualityTestStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            pw.println();
            pw.println("\tif( " + formatVariableName(anAttribute.getName()) + ".size() != rhs." + formatVariableName(anAttribute.getName()) + ".size() ) ivarsEqual = false;");
            pw.println("\tfor( int idx = 0; idx < " + formatVariableName(anAttribute.getName()) + ".size(); idx++ )");
            pw.println("\t{");
            pw.println("\t\tif( ! (" + formatVariableName(anAttribute.getName()) + "[idx] == rhs." + formatVariableName(anAttribute.getName()) + "[idx]) ) ivarsEqual = false;");
            pw.println("\t}");
            pw.println();
        }

        public void writeLengthRetrievalStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            VariableListAttribute variableList = (VariableListAttribute) anAttribute;
            if (variableList.getElementTypeIsPrimitive()) {
                pw.println("\tresult += sizeof( " + marshalTypes.getProperty(variableList.getListElementType()) + " ) * " + formatVariableName(variableList.getName()) + ".size(); // " + formatVariableName(anAttribute.getName()));
            } else {
                pw.println("\t// " + formatVariableName(anAttribute.getName()));
                pw.println("\tfor( int idx = 0; idx < " + formatVariableName(variableList.getName()) + ".size(); idx++ )");
                pw.println("\t{");
                pw.println("\t\tresult += " + formatVariableName(variableList.getName()) + "[idx].length();");
                pw.println("\t}");
            }
        }
    }

    protected class CppBitfieldGenerator extends CppAttributeGenerator {

        HashMap<ClassAttribute.ClassAttributeType, CppAttributeGenerator> writers = new HashMap<ClassAttribute.ClassAttributeType, CppAttributeGenerator>();

        public CppBitfieldGenerator() {
            writers.put(ClassAttribute.ClassAttributeType.PADDING, new CppPaddingGenerator());
            writers.put(ClassAttribute.ClassAttributeType.PRIMITIVE, new CppPrimitiveGenerator());
            writers.put(ClassAttribute.ClassAttributeType.ENUMREF, new CppEnumRefGenerator());
            writers.put(ClassAttribute.ClassAttributeType.BOOLEAN, new CppBooleanGenerator());
        }

        protected int createUnshiftedMask(int numConsecutiveOnes) {
            return (0x1 << numConsecutiveOnes) - 1;
        }

        public void writeIncludeStatements(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute, String path) {
        }

        public void writeDeclaration(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BitfieldAttribute bitfield = (BitfieldAttribute) anAttribute;
            List<ClassAttribute> entries = bitfield.getBitfieldEntries();
            for (ClassAttribute entry : entries) {
                CppAttributeGenerator writer = writers.get(entry.getAttributeKind());
                writer.writeDeclaration(pw, aClass, entry);
            }
        }

        public void writeInitializationStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BitfieldAttribute bitfield = (BitfieldAttribute) anAttribute;
            List<ClassAttribute> entries = bitfield.getBitfieldEntries();
            for (ClassAttribute entry : entries) {
                CppAttributeGenerator writer = writers.get(entry.getAttributeKind());
                writer.writeInitializationStatement(pw, aClass, entry);
            }
        }

        public void writeGetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BitfieldAttribute bitfield = (BitfieldAttribute) anAttribute;
            List<ClassAttribute> entries = bitfield.getBitfieldEntries();
            for (ClassAttribute entry : entries) {
                CppAttributeGenerator writer = writers.get(entry.getAttributeKind());
                writer.writeGetterMethodPrototype(pw, aClass, entry);
            }
        }

        public void writeSetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BitfieldAttribute bitfield = (BitfieldAttribute) anAttribute;
            List<ClassAttribute> entries = bitfield.getBitfieldEntries();
            for (ClassAttribute entry : entries) {
                CppAttributeGenerator writer = writers.get(entry.getAttributeKind());
                writer.writeSetterMethodPrototype(pw, aClass, entry);
            }
        }

        public void writeGetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BitfieldAttribute bitfield = (BitfieldAttribute) anAttribute;
            List<ClassAttribute> entries = bitfield.getBitfieldEntries();
            for (ClassAttribute entry : entries) {
                CppAttributeGenerator writer = writers.get(entry.getAttributeKind());
                writer.writeGetterMethodBody(pw, aClass, entry);
            }
        }

        public void writeSetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BitfieldAttribute bitfield = (BitfieldAttribute) anAttribute;
            List<ClassAttribute> entries = bitfield.getBitfieldEntries();
            for (ClassAttribute entry : entries) {
                CppAttributeGenerator writer = writers.get(entry.getAttributeKind());
                writer.writeSetterMethodBody(pw, aClass, entry);
            }
        }

        public void writeMarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BitfieldAttribute bitfield = (BitfieldAttribute) anAttribute;
            String marshalType = marshalTypes.getProperty(bitfield.getSerializedType());
            pw.println("\t{");
            pw.println("\t\t" + marshalType + " temp = 0;");
            int entryOffset = 8;
            List<ClassAttribute> entries = bitfield.getBitfieldEntries();
            for (ClassAttribute entry : entries) {
                entryOffset -= ((BitfieldAttribute.Entry) entry).getNumberOfBits();
                pw.print("\t\ttemp = temp | ( ( ");
                if (entry.getAttributeKind() == ClassAttribute.ClassAttributeType.ENUMREF || entry.getAttributeKind() == ClassAttribute.ClassAttributeType.PRIMITIVE || entry.getAttributeKind() == ClassAttribute.ClassAttributeType.BOOLEAN) {
                    pw.print(formatVariableName(entry.getName()));
                } else if (entry.getAttributeKind() == ClassAttribute.ClassAttributeType.PADDING) {
                    pw.print("0");
                }
                pw.print(" & " + createUnshiftedMask(((BitfieldAttribute.Entry) entry).getNumberOfBits()) + " ) ");
                pw.println("<< " + entryOffset + " );");
            }
            pw.println("\t\tdataStream << (" + marshalType + ")temp;");
            pw.println("\t}");
        }

        public void writeUnmarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BitfieldAttribute bitfield = (BitfieldAttribute) anAttribute;
            String marshalType = marshalTypes.getProperty(bitfield.getSerializedType());
            pw.println("\t{");
            pw.println("\t\t" + marshalType + " temp = 0;");
            pw.println("\t\tdataStream >> temp;");
            int entryOffset = 8;
            List<ClassAttribute> entries = bitfield.getBitfieldEntries();
            for (ClassAttribute entry : entries) {
                entryOffset -= ((BitfieldAttribute.Entry) entry).getNumberOfBits();
                String decodingString = "( temp >> " + entryOffset + " ) & " + createUnshiftedMask(((BitfieldAttribute.Entry) entry).getNumberOfBits());
                if (entry.getAttributeKind() == ClassAttribute.ClassAttributeType.ENUMREF || entry.getAttributeKind() == ClassAttribute.ClassAttributeType.PRIMITIVE || entry.getAttributeKind() == ClassAttribute.ClassAttributeType.BOOLEAN) {
                    pw.println("\t\t" + formatVariableName(entry.getName()) + " = " + decodingString + ";");
                } else if (entry.getAttributeKind() == ClassAttribute.ClassAttributeType.PADDING) {
                }
            }
            pw.println("\t}");
        }

        public void writeEqualityTestStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BitfieldAttribute bitfield = (BitfieldAttribute) anAttribute;
            List<ClassAttribute> entries = bitfield.getBitfieldEntries();
            for (ClassAttribute entry : entries) {
                CppAttributeGenerator writer = writers.get(entry.getAttributeKind());
                writer.writeEqualityTestStatement(pw, aClass, entry);
            }
        }

        public void writeLengthRetrievalStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BitfieldAttribute bitfield = (BitfieldAttribute) anAttribute;
            pw.println("\tresult += sizeof( " + marshalTypes.getProperty(bitfield.getSerializedType()) + " ); // " + formatVariableName(anAttribute.getName()));
        }
    }

    protected class CppBooleanGenerator extends CppAttributeGenerator {

        public void writeIncludeStatements(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute, String path) {
        }

        public void writeDeclaration(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BooleanAttribute boolAttr = (BooleanAttribute) anAttribute;
            if (boolAttr.getComment() != null && !boolAttr.getComment().equals("")) {
                pw.println("\t//! " + boolAttr.getComment());
            }
            pw.println("\tbool " + formatVariableName(boolAttr.getName()) + ";\n");
        }

        public void writeInitializationStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BooleanAttribute boolAttr = (BooleanAttribute) anAttribute;
            pw.print("\t" + formatVariableName(boolAttr.getName()) + " = ");
            if (boolAttr.getDefaultValue()) pw.println("true;"); else pw.println("false;");
        }

        public void writeGetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BooleanAttribute boolAttr = (BooleanAttribute) anAttribute;
            pw.println("\tbool get" + initialCap(boolAttr.getName()) + "() const;");
        }

        public void writeSetterMethodPrototype(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BooleanAttribute boolAttr = (BooleanAttribute) anAttribute;
            pw.println("\tvoid " + "set" + initialCap(boolAttr.getName()) + "( bool pX );");
        }

        public void writeGetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BooleanAttribute boolAttr = (BooleanAttribute) anAttribute;
            pw.println("bool " + aClass.getName() + "::" + "get" + initialCap(boolAttr.getName()) + "() const");
            pw.println("{");
            pw.println("\treturn " + formatVariableName(boolAttr.getName()) + ";");
            pw.println("}\n");
        }

        public void writeSetterMethodBody(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BooleanAttribute boolAttr = (BooleanAttribute) anAttribute;
            pw.println("void " + aClass.getName() + "::" + "set" + initialCap(boolAttr.getName()) + "( bool pX )");
            pw.println("{");
            pw.println("\t" + formatVariableName(boolAttr.getName()) + " = pX;");
            pw.println("}\n");
        }

        public void writeMarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BooleanAttribute boolAttr = (BooleanAttribute) anAttribute;
            String marshalType = marshalTypes.getProperty(boolAttr.getSerializedType());
            pw.println("\tdataStream << " + "(" + marshalType + ")" + formatVariableName(boolAttr.getName()) + ";");
        }

        public void writeUnmarshallingStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BooleanAttribute boolAttr = (BooleanAttribute) anAttribute;
            String marshalType = marshalTypes.getProperty(boolAttr.getSerializedType());
            pw.println("\t{");
            pw.println("\t\t" + marshalType + " temp = 0;");
            pw.println("\t\tdataStream >> temp;");
            pw.println("\t\t" + formatVariableName(boolAttr.getName()) + " = temp;");
            pw.println("\t}");
        }

        public void writeEqualityTestStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            pw.println("\tif( ! (" + formatVariableName(anAttribute.getName()) + " == rhs." + formatVariableName(anAttribute.getName()) + ") ) ivarsEqual = false;");
        }

        public void writeLengthRetrievalStatement(PrintWriter pw, GeneratedClass aClass, ClassAttribute anAttribute) {
            BooleanAttribute boolAttr = (BooleanAttribute) anAttribute;
            pw.println("\tresult += sizeof( " + marshalTypes.getProperty(boolAttr.getSerializedType()) + " ); // " + formatVariableName(boolAttr.getName()));
        }
    }
}
