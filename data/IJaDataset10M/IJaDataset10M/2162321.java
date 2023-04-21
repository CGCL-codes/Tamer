package com.k_int.codec.comp;

import com.k_int.codec.runtime.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CodecBuilderInfo {

    private static Log log = LogFactory.getLog(CodecBuilderInfo.class);

    public static String builtinTypeInfo[][] = { { "java.math.BigInteger", null }, { "com.k_int.codec.runtime.AsnBitString", null }, { null, null }, { "java.util.Vector", null }, { null, null }, { null, null }, { null, null }, { null, null }, { "java.math.BigInteger", null }, { "byte[]", null }, { "int[]", null }, { "java.math.BigDecimal", null }, { "java.lang.Boolean", null }, { "com.k_int.codec.runtime.AsnNull", null } };

    private static int inline_codec_counter = 0;

    private static CodecBuilderInfo self = null;

    String pkg_name = null;

    String pkg_dir = null;

    private Vector import_list = new Vector();

    private Hashtable modules = new Hashtable();

    boolean default_tagging_is_explicit = true;

    String current_module_name = null;

    public static synchronized CodecBuilderInfo getInfo() {
        if (self == null) {
            self = new CodecBuilderInfo();
        }
        return self;
    }

    public CodecBuilderInfo() {
        registerModule("Builtin", true, false);
        ModuleInfo bim = lookupModule("Builtin");
        bim.registerType("Integer", new BuiltinTypeInfo("Integer", true, SerializationManager.UNIVERSAL, SerializationManager.INTEGER, false, null, "BigInteger", bim));
        bim.registerType("BitString", new BuiltinTypeInfo("BitString", true, SerializationManager.UNIVERSAL, SerializationManager.BITSTRING, false, null, "AsnBitString", bim));
        bim.registerType("OID", new BuiltinTypeInfo("OID", true, SerializationManager.UNIVERSAL, SerializationManager.OID, false, null, "int[]", bim));
        bim.registerType("OctetString", new BuiltinTypeInfo("OctetString", true, SerializationManager.UNIVERSAL, SerializationManager.OCTETSTRING, false, null, "byte[]", bim));
        bim.registerType("BOOL", new BuiltinTypeInfo("BOOL", true, SerializationManager.UNIVERSAL, SerializationManager.BOOLEAN, false, null, "Boolean", bim));
        bim.registerType("SetOrSequenceOf", new BuiltinTypeInfo("SetOrSequenceOf", true, SerializationManager.UNIVERSAL, SerializationManager.SEQUENCEOF, false, null, "java.util.Vector", bim));
        bim.registerType("Any", new BuiltinTypeInfo("Any", true, SerializationManager.UNIVERSAL, SerializationManager.ANY, false, null, "Object", bim));
        bim.registerType("Enumerated", new BuiltinTypeInfo("Enumerated", true, SerializationManager.UNIVERSAL, SerializationManager.ENUMERATED, false, null, "BigInteger", bim));
        bim.registerType("NULL", new BuiltinTypeInfo("NULL", true, SerializationManager.UNIVERSAL, SerializationManager.NULL, false, null, "com.k_int.codec.runtime.AsnNull", bim));
        registerModule("AsnUseful", false, false);
        ModuleInfo uim = lookupModule("AsnUseful");
        uim.registerType("ObjectDescriptor", new DefinedTypeInfo("ObjectDescriptor", true, SerializationManager.UNIVERSAL, 7, true, "OctetString", "java.lang.String", uim));
        uim.registerType("NumericString", new DefinedTypeInfo("NumericString", true, SerializationManager.UNIVERSAL, 18, true, "OctetString", "java.lang.String", uim));
        uim.registerType("PrintableString", new DefinedTypeInfo("PrintableString", true, SerializationManager.UNIVERSAL, 19, true, "OctetString", "java.lang.String", uim));
        uim.registerType("TeletexString", new DefinedTypeInfo("TeletexString", true, SerializationManager.UNIVERSAL, 20, true, "OctetString", "java.lang.String", uim));
        uim.registerType("T61String", new DefinedTypeInfo("T61String", true, SerializationManager.UNIVERSAL, 20, true, "OctetString", "java.lang.String", uim));
        uim.registerType("VideotexString", new DefinedTypeInfo("VideotexString", true, SerializationManager.UNIVERSAL, 21, true, "OctetString", "java.lang.String", uim));
        uim.registerType("IA5String", new DefinedTypeInfo("IA5String", true, SerializationManager.UNIVERSAL, 22, true, "OctetString", "java.lang.String", uim));
        uim.registerType("GraphicString", new DefinedTypeInfo("GraphicString", true, SerializationManager.UNIVERSAL, 25, true, "OctetString", "java.lang.String", uim));
        uim.registerType("VisibleString", new DefinedTypeInfo("VisibleString", true, SerializationManager.UNIVERSAL, 26, true, "OctetString", "java.lang.String", uim));
        uim.registerType("ISO646String", new DefinedTypeInfo("ISO646String", true, SerializationManager.UNIVERSAL, 26, true, "OctetString", "java.lang.String", uim));
        uim.registerType("GeneralString", new DefinedTypeInfo("GeneralString", true, SerializationManager.UNIVERSAL, 26, true, "OctetString", "java.lang.String", uim));
        uim.registerType("UTCTime", new DefinedTypeInfo("UTCTime", true, SerializationManager.UNIVERSAL, 23, true, "OctetString", "java.lang.String", uim));
        uim.registerType("GeneralizedTime", new DefinedTypeInfo("GeneralizedTime", true, SerializationManager.UNIVERSAL, 24, true, "OctetString", "java.lang.String", uim));
        uim.registerType("EXTERNAL", new SequenceTypeInfo("EXTERNAL", true, SerializationManager.UNIVERSAL, 8, true, "SetOrSequenceOf", null, uim));
        uim.registerType("encoding_inline0", new ChoiceTypeInfo("encoding_inline0", true, -1, -1, false, "ChoiceType", null, uim));
    }

    public static int getNextInlineCounter() {
        return inline_codec_counter++;
    }

    public ModuleInfo lookupModule(String module_name) {
        return (ModuleInfo) (modules.get(module_name));
    }

    public String getInternalClass(int index) {
        return builtinTypeInfo[index][0];
    }

    public String getCurrentModuleName() {
        return current_module_name;
    }

    public ModuleInfo getCurrentModule() {
        return lookupModule(current_module_name);
    }

    public void setCurrentModuleName(String s) {
        current_module_name = s;
    }

    public void registerModule(String module_reference, boolean default_explicit_tagging, boolean create_java) {
        log.debug("      registerModule(" + module_reference + "," + default_explicit_tagging + ")");
        ModuleInfo mi = (ModuleInfo) modules.get(module_reference);
        if (mi == null) {
            modules.put(module_reference, new ModuleInfo(module_reference, default_explicit_tagging, create_java));
        } else {
            mi.setDefaultExplicitTagging(default_explicit_tagging);
            mi.setCreateJava(create_java);
        }
    }

    public void registerType(String module, String type, TypeInfo ti) {
        log.debug("registerType(" + module + "," + type + ",...)");
        ModuleInfo mi = (ModuleInfo) modules.get(module);
        if (null != mi) {
            mi.registerType(type, ti);
        } else {
            log.warn("Unknown module reference");
            System.exit(1);
        }
    }

    public void setCurrentPackageName(String pkg_name) {
        this.pkg_name = pkg_name;
    }

    public void setCurrentPackageDir(String pkg_dir) {
        this.pkg_dir = pkg_dir;
    }

    public void defaultTaggingIsImplicit() {
        this.default_tagging_is_explicit = false;
    }

    public void defaultTaggingIsExplicit() {
        this.default_tagging_is_explicit = true;
    }

    public String getCurrentPackageName() {
        return pkg_name;
    }

    public String getCurrentPackageDir() {
        return pkg_dir;
    }

    public boolean defaultTagModeIsExplicit() {
        return default_tagging_is_explicit;
    }

    public void resetImportList() {
        import_list.clear();
    }

    public void addImport(String imp) {
        log.debug("Adding import..." + imp);
        import_list.add(imp);
    }

    String getFullyQualifiedClassName(String classname) {
        for (Enumeration e = import_list.elements(); e.hasMoreElements(); ) {
            String current = (String) e.nextElement();
            if (current.endsWith("." + classname)) return current;
        }
        return null;
    }

    public void writeModuleImports(FileWriter codec_writer) {
        try {
            for (Enumeration e = import_list.elements(); e.hasMoreElements(); ) codec_writer.write("import " + e.nextElement() + ";\n");
        } catch (java.io.IOException ioe) {
            log.warn(ioe.toString());
        }
    }

    public void create() {
        log.debug("Processing Modules.............");
        for (Enumeration e = modules.elements(); e.hasMoreElements(); ) {
            inline_codec_counter = 0;
            ModuleInfo mi = (ModuleInfo) e.nextElement();
            System.out.println("Processing " + mi.getModulePackageName() + " " + mi.createJava());
            if (mi.createJava()) mi.createCode(); else log.debug("Skipping module......" + mi.getModulePackageName());
        }
    }

    public void registerImport(String module_reference, String type_reference) {
        log.debug("Registering import of " + module_reference + "." + type_reference + " in module " + getCurrentModuleName());
        getCurrentModule().registerImport(module_reference, type_reference);
    }

    public void createTypeInfoFor(String type_reference, ASTType t) {
        CodecBuilderInfo info = CodecBuilderInfo.getInfo();
        log.debug("createTypeInfoFor(" + type_reference + ")");
        boolean has_tagging = false;
        int tag_class = -1;
        int tag_number = -1;
        boolean is_implicit = (info.default_tagging_is_explicit == true ? false : true);
        String base_type = null;
        if (t.which == 1) {
            ASTBuiltinType bit = (ASTBuiltinType) (t.jjtGetChild(0));
            if (bit.which == 6) {
                has_tagging = true;
                ASTTaggedType tt = (ASTTaggedType) (bit.jjtGetChild(0));
                is_implicit = tt.isImplicit();
                t = tt.getType();
                ASTTag tag = tt.getTag();
                if (tag.hasTagClass) tag_class = tag.getTagClass().tag_class; else tag_class = 0x80;
                ASTClassNumber cn = tag.getClassNumber();
                if (cn.which == 1) {
                    tag_number = cn.getNumber().getNumber().intValue();
                } else {
                    log.warn("Unhandled tag number type");
                    System.exit(0);
                }
            }
        }
        TypeInfo ti = null;
        if (t.which == 1) {
            ASTBuiltinType bit = (ASTBuiltinType) (t.jjtGetChild(0));
            switch(bit.which) {
                case 0:
                    ti = new IntegerTypeInfo(type_reference, true, tag_class, tag_number, is_implicit, bit.getTypeName(), info.getInternalClass(bit.which), info.getCurrentModule());
                    break;
                case 1:
                    ti = new BitStringTypeInfo(type_reference, true, tag_class, tag_number, is_implicit, bit.getTypeName(), info.getInternalClass(bit.which), info.getCurrentModule());
                    break;
                case 2:
                    ASTSetOrSequenceType sos = (ASTSetOrSequenceType) (bit.jjtGetChild(0));
                    if (sos.which == 1) {
                        log.warn("Set not yet handled");
                        System.exit(1);
                    } else {
                        ti = new SequenceTypeInfo(type_reference, true, tag_class, tag_number, is_implicit, bit.getTypeName(), info.getInternalClass(bit.which), info.getCurrentModule());
                        sos.getSequenceMembers((SequenceTypeInfo) ti);
                    }
                    break;
                case 3:
                    log.debug("Processing SetOrSequenceOf Type");
                    ASTSetOrSequenceOfType soso = (ASTSetOrSequenceOfType) (bit.jjtGetChild(0));
                    ASTType sot = null;
                    ASTSizeConstraint sc = null;
                    if (soso.has_size_constraint) {
                        sc = (ASTSizeConstraint) (soso.jjtGetChild(0));
                        sot = (ASTType) (soso.jjtGetChild(1));
                    } else {
                        sot = (ASTType) (soso.jjtGetChild(0));
                    }
                    String subtype_reference = null;
                    if (sot.which == 1) {
                        ASTBuiltinType sub_bit = (ASTBuiltinType) (sot.jjtGetChild(0));
                        log.debug("SEQUENCEOF Built in type (" + sub_bit.which + ")");
                        switch(sub_bit.which) {
                            case 2:
                                log.debug("SEQUENCEOF SEQUENCE " + info.getInternalClass(bit.which));
                                subtype_reference = type_reference + "Item" + getNextInlineCounter();
                                createTypeInfoFor(subtype_reference, sot);
                                break;
                            case 4:
                                log.debug("SEQUENCEOF CHOICE " + info.getInternalClass(bit.which));
                                subtype_reference = type_reference + "_choice" + getNextInlineCounter();
                                createTypeInfoFor(subtype_reference, sot);
                                break;
                            default:
                                log.debug("cbi SEQUENCEOF something ok.. " + sub_bit.which + " " + info.getInternalClass(sub_bit.which));
                                subtype_reference = sub_bit.getTypeName();
                                break;
                        }
                    } else {
                        ASTDefinedType sub_dt = (ASTDefinedType) (sot.jjtGetChild(0));
                        log.debug("SEQUENCEOF defined type " + sub_dt.getTypeReference().typeref);
                        subtype_reference = sub_dt.getTypeReference().typeref;
                    }
                    if (soso.which == 1) {
                        log.debug("SetOf not yet handled");
                        System.exit(1);
                    } else {
                        ti = new SequenceOfTypeInfo(type_reference, true, tag_class, tag_number, is_implicit, bit.getTypeName(), "java.util.Vector", info.getCurrentModule(), subtype_reference);
                    }
                    break;
                case 4:
                    ti = new ChoiceTypeInfo(type_reference, true, tag_class, tag_number, is_implicit, bit.getTypeName(), info.getInternalClass(bit.which), info.getCurrentModule());
                    ((ASTChoiceType) (bit.jjtGetChild(0))).getChoiceMembers((ChoiceTypeInfo) ti);
                    break;
                case 5:
                    System.out.println("Better add typeinfo for selection");
                    System.exit(1);
                    break;
                case 6:
                    log.debug("Fatal error: Can't have tagged type under a tagged type");
                    System.exit(0);
                    break;
                case 7:
                    System.out.println("Better add typeinfo for Any");
                    System.exit(1);
                    break;
                case 8:
                    ti = new EnumTypeInfo(type_reference, true, tag_class, tag_number, is_implicit, bit.getTypeName(), info.getInternalClass(bit.which), info.getCurrentModule());
                    ((ASTEnumeratedType) (bit.jjtGetChild(0))).getEnumerations((EnumTypeInfo) ti);
                    break;
                case 9:
                    ti = new OctetStringTypeInfo(type_reference, true, tag_class, tag_number, is_implicit, bit.getTypeName(), info.getInternalClass(bit.which), info.getCurrentModule());
                    break;
                case 10:
                    ti = new OIDTypeInfo(type_reference, true, tag_class, tag_number, is_implicit, bit.getTypeName(), info.getInternalClass(bit.which), info.getCurrentModule());
                    break;
                case 11:
                    System.out.println("Better add typeinfo for Real");
                    System.exit(1);
                    break;
                case 12:
                    System.out.println("Better add typeinfo for Boolean");
                    System.exit(1);
                    break;
                case 13:
                    ti = new NullTypeInfo(type_reference, true, tag_class, tag_number, is_implicit, bit.getTypeName(), info.getInternalClass(bit.which), info.getCurrentModule());
                    break;
                default:
                    System.out.println("Unhandled internal type");
                    System.exit(1);
                    break;
            }
            info.registerType(info.getCurrentModuleName(), type_reference, ti);
        } else {
            ASTDefinedType dt = (ASTDefinedType) (t.jjtGetChild(0));
            log.debug("Defined Type: ");
            ti = new DefinedTypeInfo(type_reference, false, tag_class, tag_number, is_implicit, dt.getTypeReference().typeref, null, info.getCurrentModule());
            info.registerType(info.getCurrentModuleName(), type_reference, ti);
        }
    }
}
