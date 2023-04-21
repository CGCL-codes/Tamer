package saadadb.generationclass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import saadadb.collection.CollectionManager;
import saadadb.command.ArgsParser;
import saadadb.command.ManageCollection;
import saadadb.database.Database;
import saadadb.dataloader.Loader;
import saadadb.dataloader.SchemaMapper;
import saadadb.exceptions.SaadaException;
import saadadb.meta.AttributeHandler;
import saadadb.sqltable.SQLTable;
import saadadb.util.ChangeKey;
import saadadb.util.ChangeType;
import saadadb.util.DefineType;
import saadadb.util.Messenger;

public class GenerationClassProduct {

    private static String separ = System.getProperty("file.separator");

    /** * @version $Id: GenerationClassProduct.java 118 2012-01-06 14:33:51Z laurent.mistahl $

	 * @param writer
	 * @throws GenerateClassException
	 * @throws IOException 
	 * @throws SaadaException 
	 */
    public static void createImport(FileWriter writer) throws IOException {
        writer.write("package generated." + Database.getName() + ";\n" + "/**Code generated**/\n" + "import saadadb.collection.*;\n" + "import saadadb.exceptions.*;\n" + "import java.sql.ResultSet;\n" + "import java.sql.SQLException;\n" + "\n");
    }

    /**
	 * @param writer
	 * @param type
	 * @param key
	 * @throws IOException 
	 */
    public static void createType(FileWriter writer, String type, String key) throws IOException {
        String pack = " = saadadb.util.SaadaConstant.";
        String pub = "\n public " + type + "  " + key;
        String vType = ChangeType.getType(type);
        if (!vType.equals("")) {
            switch(DefineType.getType(vType)) {
                case DefineType.FIELD_LONG:
                    pub += pack + "LONG";
                    break;
                case DefineType.FIELD_INT:
                    pub += pack + "INT";
                    break;
                case DefineType.FIELD_DOUBLE:
                    pub += pack + "DOUBLE";
                    break;
                case DefineType.FIELD_FLOAT:
                    pub += pack + "FLOAT";
                    break;
                case DefineType.FIELD_STRING:
                    pub += pack + "STRING";
                    break;
                case DefineType.FIELD_SHORT:
                    pub += pack + "SHORT";
                    break;
                case DefineType.FIELD_BYTE:
                    pub += pack + "BYTE";
                    break;
                case DefineType.FIELD_CHAR:
                    pub += pack + "CHAR";
                    break;
                case DefineType.FIELD_BOOLEAN:
                    pub += " ";
                    break;
                default:
                    pub += " ";
                    Messenger.printMsg(Messenger.ERROR, "GenerationClassProduct : Unknow type " + type);
                    break;
            }
        }
        pub += ";  \n";
        writer.write(pub);
    }

    /**
	 * @param writer
	 * @param type
	 * @param key
	 * @throws IOException 
	 */
    public static void createSetAndGet(FileWriter writer, String type, String key) throws IOException {
        writer.write("\n  public void set" + key + "(" + type + " " + key + "){\n" + "  	this." + key + " = " + key + ";\n" + "  }\n" + "  public " + type + " get" + key + "() throws SaadaException{\n" + "     if(loaded==false){\n" + "         loadBusinessAttribute();\n" + "     }\n" + "     return " + key + ";\n" + "  }\n");
    }

    /**
	 * @param writer
	 * @param name
	 * @throws IOException 
	 */
    public static void createConstructor(FileWriter writer, String name) throws IOException {
        writer.write("\n  public " + name + "(){\n" + "    super();\n" + "  }\n");
    }

    /**
	 * @param classHeader
	 * @param classpath
	 * @param classname
	 * @param superclass
	 * @throws SaadaException
	 * @throws IOException
	 */
    public static void buildJavaClass(LinkedHashMap<String, AttributeHandler> classHeader, String classpath, String classname, String superclass) throws Exception {
        File nameFile = new File(classpath + separ + classname + ".java");
        FileWriter writer = new FileWriter(nameFile, false);
        createImport(writer);
        writer.write("public class " + classname + "  extends " + superclass + "{  \n");
        Iterator it = classHeader.keySet().iterator();
        while (it.hasNext()) {
            AttributeHandler ah = classHeader.get(it.next());
            String type = ah.getType();
            String key = ah.getNameattr();
            if (key.indexOf("_HIERARCH") == 0) {
                key = ChangeKey.changeKeyHIERARCH(key);
            }
            createType(writer, type, key);
        }
        createConstructor(writer, classname);
        it = classHeader.keySet().iterator();
        while (it.hasNext()) {
            AttributeHandler ah = classHeader.get(it.next());
            String key = ah.getNameattr();
            String type = ah.getType();
            if (key.indexOf("_HIERARCH") == 0) {
                key = ChangeKey.changeKeyHIERARCH(key);
            }
            createSetAndGet(writer, type, key);
        }
        writer.write("}\n");
        writer.flush();
        writer.close();
        System.gc();
        Compile.compileIt(nameFile);
    }

    public static void main(String[] args) throws Exception {
        Database.init("Napoli");
        AttributeHandler ah = new AttributeHandler();
        ah.setNameattr("field1");
        ah.setNameorg("field1");
        ah.setType("String");
        LinkedHashMap<String, AttributeHandler> classHeader = new LinkedHashMap<String, AttributeHandler>();
        classHeader.put(ah.getNameattr(), ah);
        buildJavaClass(classHeader, Database.getRoot_dir() + "/class_mapping", "essai", "IMAGEUserColl");
        System.out.println("@@@@@ avant load " + SaadaClassReloader.reloadGeneratedClass("essai").getDeclaredFields().length);
        ;
        AttributeHandler ah2 = new AttributeHandler();
        ah2.setNameattr("field2");
        ah2.setNameorg("field2");
        ah2.setType("String");
        classHeader.put(ah2.getNameattr(), ah2);
        buildJavaClass(classHeader, Database.getRoot_dir() + "/class_mapping", "essai", "IMAGEUserColl");
        Class cl = SaadaClassReloader.reloadGeneratedClass("essai");
        System.out.println("@@@@@ apres load " + (Object) cl + " " + cl.getDeclaredFields().length);
        cl = SaadaClassReloader.forGeneratedName("essai");
        System.out.println("@@@@@ apres load " + (Object) cl + " " + cl.getDeclaredFields().length);
        SQLTable.beginTransaction();
        CollectionManager cm = new CollectionManager("AIPWFI");
        cm.empty(new ArgsParser(new String[] { "-collection=AIPWFI", "-category=image" }));
        SQLTable.commitTransaction();
        Database.getCachemeta().reload(true);
        Loader dl = new Loader(new String[] { "-collection=AIPWFI", "-category=image", "-novignette", "-filename=/Users/laurentmichel/IVOANaples/data/wfi_iap/images/AIP_XMM/1ES0102-72-I-BB_Ic_Iwp_ESO845-971-set_26.fits", "-repository=no", "-novignette", "-classfusion=WFIImage", "-ukw", "PGM=AIPWFI", "Napoli" });
        dl.load();
        dl = new Loader(new String[] { "-collection=AIPWFI", "-category=image", "-novignette", "-filename=/Users/laurentmichel/IVOANaples/data/wfi_iap/images/BLOX/A1882_B.ALLF.swarp.fits", "-repository=no", "-novignette", "-classfusion=WFIImage", "-ukw", "PGM=BLOX", "Napoli" });
        dl.load();
    }
}
