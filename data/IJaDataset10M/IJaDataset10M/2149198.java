package makedep;

import java.io.*;
import java.util.*;

class SymbianProjectARM extends SymbianIDEProjectARM {

    Database iDatabase;

    VMSourceHandler iSrcHandler;

    BldInf iInf;

    public SymbianProjectARM(IDETool tool, String name, BldInf inf) {
        super(tool, name, TARGET);
        iSrcHandler = new VMSourceHandler(tool, this, "arm", "symbian");
        iInf = inf;
        System.out.println(this + " " + this.type);
    }

    void openDatabase() throws Exception {
        Platform platform = new WinGammaPlatform();
        platform.setupFileTemplates();
        long t = platform.defaultGrandIncludeThreshold();
        iDatabase = new Database(platform, t);
        tool().configurator.platform.put("arch", "arm");
        tool().configurator.platform.put("iarch", "arm");
        tool().configurator.platform.put("carch", "arm");
        tool().makeDirExist(getOutputDir());
        tool().makeDirExist(getOutputDir() + File.separator + "incls");
        iDatabase.setOutputDir(tool().getOutputFileFullPath(getOutputDir()));
        Properties globalProps = new Properties();
        if (tool().isOptionEnabled("ENABLE_JAVA_DEBUGGER")) {
            globalProps.put("ENABLE_JAVA_DEBUGGER", "true");
        }
        if (tool().isOptionEnabled("ENABLE_ROM_JAVA_DEBUGGER")) {
            globalProps.put("ENABLE_ROM_JAVA_DEBUGGER", "true");
        }
        if (tool().isOptionEnabled("ENABLE_ISOLATES")) {
            globalProps.put("ENABLE_ISOLATES", "true");
        }
        if (this.type == TARGET) {
            if (tool().isOptionEnabled("ENABLE_MONET")) {
                globalProps.put("ENABLE_MONET", "true");
            }
        }
        if (tool().isOptionEnabled("ENABLE_METHOD_TRAPS")) {
            globalProps.put("ENABLE_METHOD_TRAPS", "true");
        }
        if (tool().isOptionEnabled("ENABLE_DYNUPDATE")) {
            globalProps.put("ENABLE_DYNUPDATE", "true");
        }
        globalProps.put("ENABLE_ISOLATES", "true");
        globalProps.put("ENABLE_GENERATE_SOURCE_ROM_IMAGE", "false");
        globalProps.put("ENABLE_INTERPRETER_GENERATOR", "false");
        iDatabase.get(tool().getPlatformArg(), tool().getDatabaseArg(), globalProps);
        tool().configurator.platform.put("arch", "arm");
        tool().configurator.platform.put("iarch", "arm");
        tool().configurator.platform.put("carch", "arm");
        iDatabase.compute();
    }

    public void write() throws Exception {
        openDatabase();
        for (int i = 0; i < iInf.osVersions.size(); i++) {
            String osVersion = (String) iInf.osVersions.elementAt(i);
            if (getName().equals("cldc_vm_lib")) {
                openOutputFile(getName() + "\\vm" + osVersion + ".mmp");
            } else {
                openOutputFile(getName() + "\\" + getName() + osVersion + ".mmp");
            }
            writeHeader();
            writeTarget(osVersion);
            writeDefines(osVersion);
            writeSource();
            writeInclude();
            writeLibraries();
            writeStaticLibraries(osVersion);
            writeDefFileLine(osVersion);
            closeOutputFile();
        }
        writeIncls();
        writeJvmConfig();
        for (int i = 0; i < iInf.osVersions.size(); i++) {
            String osVersion = (String) iInf.osVersions.elementAt(i);
            createDefFile(osVersion);
        }
    }

    void writeDefines(String aOsVersion) throws Exception {
        Vector v = getBaseCppDefines();
        for (int i = 0; i < v.size(); i++) {
            putln("MACRO " + v.elementAt(i));
        }
        if (aOsVersion.equals("_s60v3fp0")) {
            putln("MACRO __DONT_USE_IN_CODE__MMP_ONLY__S60_V3_FP0__");
            putln("OPTION GCCE -O2 -fno-unit-at-a-time");
        } else if (aOsVersion.equals("_s60v2fp3")) {
            putln("MACRO __DONT_USE_IN_CODE__MMP_ONLY__S60_V2_FP3__");
        } else if (aOsVersion.equals("_s60v2fp2")) {
            putln("MACRO __DONT_USE_IN_CODE__MMP_ONLY__S60_V2_FP2__");
        } else if (aOsVersion.equals("_uiqv3fp0")) {
            putln("MACRO __DONT_USE_IN_CODE__MMP_ONLY__UIQ_V3_FP0__");
            putln("OPTION GCCE -O2 -fno-unit-at-a-time");
        }
    }

    void writeLibraries() throws Exception {
        Vector v = getLibraries();
        for (int i = 0; i < v.size(); i++) {
            putln("LIBRARY " + v.elementAt(i));
        }
    }

    void writeStaticLibraries(String aOsVersion) throws Exception {
        Vector v = getStaticLibraries();
        for (int i = 0; i < v.size(); i++) {
            putln("STATICLIBRARY " + v.elementAt(i));
        }
        if (aOsVersion.equals("_s60v2fp3") || aOsVersion.equals("_s60v2fp2")) {
            putln("#if !defined(WINSCW)");
            putln("STATICLIBRARY ..\\..\\..\\gcc\\lib\\gcc-lib\\arm-epoc-pe\\2.9-psion-98r2\\libgcc.a");
            putln("#endif");
        }
    }

    void writeDefFileLine(String aOsVersion) throws Exception {
        putln();
        if (!aOsVersion.equals("_s60v3fp0") && !aOsVersion.equals("_uiqv3fp0")) {
            putln("exportunfrozen");
        }
        putln("deffile .\\cldc_vm_lib_arm" + aOsVersion + ".def");
        putln("NOSTRICTDEF");
    }

    void createDefFile(String aOsVersion) throws Exception {
        if (aOsVersion.equals("_s60v3fp0") || aOsVersion.equals("_uiqv3fp0")) {
            FileOutputStream out = new FileOutputStream(tool().getOutputFileFullPath(getOutputDir() + "\\cldc_vm_lib_arm" + aOsVersion + ".def"));
            out.write("EXPORTS\n".getBytes());
            out.write("_Z24ImplementationGroupProxyRi @ 1 NONAME ; CODE\n".getBytes());
            out.write("_Z9RunVMCodePKtPcR13CVMPropertiesR12CVMArgumentsP12MApplicationPKc @ 2 NONAME ; CODE\n".getBytes());
            out.write("_ZN13CVMProperties4AddLERK6TDesC8S2_ @ 3 NONAME ; CODE\n".getBytes());
            out.write("_ZN13CVMProperties5CountEv @ 4 NONAME ; CODE\n".getBytes());
            out.write("_ZN13CVMProperties5NewLCEv @ 5 NONAME ; CODE\n".getBytes());
            out.write("_ZTI12CVMArguments @ 6 NONAME ; DATA\n".getBytes());
            out.write("_ZTI12JVMDupStream @ 7 NONAME ; DATA\n".getBytes());
            out.write("_ZTI13CVMProperties @ 8 NONAME ; DATA\n".getBytes());
            out.write("_ZTI13JVMFileStream @ 9 NONAME ; DATA\n".getBytes());
            out.write("_ZTI13JVMNamedField @ 10 NONAME ; DATA\n".getBytes());
            out.write("_ZTI13JVMOopPrinter @ 11 NONAME ; DATA\n".getBytes());
            out.write("_ZTI13JVMUTF8Stream @ 12 NONAME ; DATA\n".getBytes());
            out.write("_ZTI14JVMSymbolField @ 13 NONAME ; DATA\n".getBytes());
            out.write("_ZTI15CVirtualMachine @ 14 NONAME ; DATA\n".getBytes());
            out.write("_ZTI16JVMDefaultStream @ 15 NONAME ; DATA\n".getBytes());
            out.write("_ZTI16JVMUnicodeStream @ 16 NONAME ; DATA\n".getBytes());
            out.write("_ZTI17JVMIndexableField @ 17 NONAME ; DATA\n".getBytes());
            out.write("_ZTI18JVMBytecodeClosure @ 18 NONAME ; DATA\n".getBytes());
            out.write("_ZTI20JVMStackmapGenerator @ 19 NONAME ; DATA\n".getBytes());
            out.write("_ZTI20JVMVerifyMethodCodes @ 20 NONAME ; DATA\n".getBytes());
            out.write("_ZTI21JVMConcatenatedStream @ 21 NONAME ; DATA\n".getBytes());
            out.write("_ZTI23JVMBytecodePrintClosure @ 22 NONAME ; DATA\n".getBytes());
            out.write("_ZTI24JVMByteArrayOutputStream @ 23 NONAME ; DATA\n".getBytes());
            out.write("_ZTI25JVMFixedArrayOutputStream @ 24 NONAME ; DATA\n".getBytes());
            out.write("_ZTV12CVMArguments @ 25 NONAME ; DATA\n".getBytes());
            out.write("_ZTV12JVMDupStream @ 26 NONAME ; DATA\n".getBytes());
            out.write("_ZTV13CVMProperties @ 27 NONAME ; DATA\n".getBytes());
            out.write("_ZTV13JVMFileStream @ 28 NONAME ; DATA\n".getBytes());
            out.write("_ZTV13JVMNamedField @ 29 NONAME ; DATA\n".getBytes());
            out.write("_ZTV13JVMOopPrinter @ 30 NONAME ; DATA\n".getBytes());
            out.write("_ZTV13JVMUTF8Stream @ 31 NONAME ; DATA\n".getBytes());
            out.write("_ZTV14JVMSymbolField @ 32 NONAME ; DATA\n".getBytes());
            out.write("_ZTV15CVirtualMachine @ 33 NONAME ; DATA\n".getBytes());
            out.write("_ZTV16JVMDefaultStream @ 34 NONAME ; DATA\n".getBytes());
            out.write("_ZTV16JVMUnicodeStream @ 35 NONAME ; DATA\n".getBytes());
            out.write("_ZTV17JVMIndexableField @ 36 NONAME ; DATA\n".getBytes());
            out.write("_ZTV18JVMBytecodeClosure @ 37 NONAME ; DATA\n".getBytes());
            out.write("_ZTV20JVMStackmapGenerator @ 38 NONAME ; DATA\n".getBytes());
            out.write("_ZTV20JVMVerifyMethodCodes @ 39 NONAME ; DATA\n".getBytes());
            out.write("_ZTV21JVMConcatenatedStream @ 40 NONAME ; DATA\n".getBytes());
            out.write("_ZTV23JVMBytecodePrintClosure @ 41 NONAME ; DATA\n".getBytes());
            out.write("_ZTV24JVMByteArrayOutputStream @ 42 NONAME ; DATA\n".getBytes());
            out.write("_ZTV25JVMFixedArrayOutputStream @ 43 NONAME ; DATA\n".getBytes());
            out.write("_ZN12CVMArguments5NewLCEv @ 44 NONAME ; CODE\n".getBytes());
            out.close();
        } else if (aOsVersion.equals("_s60v2fp3") || aOsVersion.equals("_s60v2fp2")) {
            FileOutputStream out = new FileOutputStream(tool().getOutputFileFullPath(getOutputDir() + "\\cldc_vm_lib_arm" + aOsVersion + ".def"));
            out.write("EXPORTS\n".getBytes());
            out.write("\tImplementationGroupProxy__FRi @ 1 NONAME R3UNUSED ; ImplementationGroupProxy(int &)\n".getBytes());
            out.write("\tRunVMCode__FPCUsPciPPcP12MApplication @ 2 NONAME ; RunVMCode(unsigned short const *, char *, int, char **, MApplication *)\n".getBytes());
            out.close();
        }
    }

    Vector getLibraries() {
        Vector v = new Vector();
        v.addElement("euser.lib");
        v.addElement("estlib.lib");
        v.addElement("esock.lib");
        v.addElement("insock.lib");
        v.addElement("efsrv.lib");
        v.addElement("ECOMPLUS.lib");
        v.addElement("smcm.lib");
        v.addElement("msgs.lib");
        v.addElement("gsmu.lib");
        v.addElement("etext.lib");
        v.addElement("bafl.lib");
        return v;
    }

    Vector getStaticLibraries() {
        Vector v = new Vector();
        v.addElement("pcsl_file.lib");
        v.addElement("pcsl_memory.lib");
        v.addElement("pcsl_network.lib");
        v.addElement("pcsl_print.lib");
        v.addElement("pcsl_string.lib");
        return v;
    }

    Vector getBaseCppDefines() {
        Vector v = new Vector();
        v.addElement("ENABLE_PCSL");
        v.addElement("ENABLE_MULTIPLE_ISOLATES");
        v.addElement("ROMIZING");
        v.addElement("ARM");
        return v;
    }

    void writeIncls() throws Exception {
        Platform platform = new SymbianPlatform(tool().getOutputFileFullPath(getOutputDir()), tool().getWorkSpaceArg());
        platform.setupFileTemplates();
        long t = platform.defaultGrandIncludeThreshold();
        Database localDatabase = new Database(platform, t);
        localDatabase.setResolveVpath(true);
        localDatabase.setWorkspace(tool().getWorkSpaceArg());
        Properties globalProps = new Properties();
        if (tool().isOptionEnabled("ENABLE_JAVA_DEBUGGER")) {
            globalProps.put("ENABLE_JAVA_DEBUGGER", "true");
        }
        if (tool().isOptionEnabled("ENABLE_ROM_JAVA_DEBUGGER")) {
            globalProps.put("ENABLE_ROM_JAVA_DEBUGGER", "true");
        }
        if (tool().isOptionEnabled("ENABLE_ISOLATES")) {
            globalProps.put("ENABLE_ISOLATES", "true");
        }
        if (this.type == TARGET) {
            if (tool().isOptionEnabled("ENABLE_MONET")) {
                globalProps.put("ENABLE_MONET", "true");
            }
        }
        if (tool().isOptionEnabled("ENABLE_METHOD_TRAPS")) {
            globalProps.put("ENABLE_METHOD_TRAPS", "true");
        }
        if (tool().isOptionEnabled("ENABLE_DYNUPDATE")) {
            globalProps.put("ENABLE_DYNUPDATE", "true");
        }
        globalProps.put("ENABLE_ISOLATES", "true");
        globalProps.put("ENABLE_GENERATE_SOURCE_ROM_IMAGE", "true");
        globalProps.put("ENABLE_INTERPRETER_GENERATOR", "true");
        globalProps.put("ENABLE_GENERATE_SOURCE_ROM_IMAGE", "true");
        globalProps.put("ENABLE_INTERPRETER_GENERATOR", "true");
        localDatabase.setOutputDir(tool().getOutputFileFullPath(getOutputDir()));
        localDatabase.get(tool().getPlatformArg(), tool().getDatabaseArg(), globalProps);
        localDatabase.compute();
        localDatabase.put();
    }

    void writeHeader() {
        putln("// Copyright 2007 BlueWhaleSystems Ltd.");
        putln();
    }

    void writeTarget(String aOsVersion) {
        if (aOsVersion.equals("_s60v2fp3") || aOsVersion.equals("_s60v2fp2")) {
            putln("TARGET\t\t" + getName() + ".lib");
            putln("TARGETTYPE\tlib");
        } else {
            putln("TARGET\t\t" + getName() + ".dll");
            putln("TARGETTYPE\tdll");
        }
        putln("UID\t0x1000008d 0x2000E279");
        if (aOsVersion.equals("_s60v3fp0")) {
            putln("CAPABILITY\tNetworkServices ReadUserData WriteUserData ReadDeviceData WriteDeviceData SwEvent");
        } else if (aOsVersion.equals("_uiqv3fp0")) {
            putln("CAPABILITY\tNetworkServices ReadUserData WriteUserData ReadDeviceData WriteDeviceData");
        }
        putln();
    }

    void addFile(Vector aElements, String aFile) {
        String file;
        if (aFile.startsWith(tool().getWorkSpaceArg())) {
            file = "..\\..\\.." + aFile.substring(tool().getWorkSpaceArg().length());
        } else {
            file = aFile;
        }
        aElements.addElement(file);
    }

    void writeSource() throws Exception {
        Vector sourceFiles = new Vector();
        for (Iterator iter = iDatabase.getAllFiles().iterator(); iter.hasNext(); ) {
            FileList fl = (FileList) iter.next();
            String fileName = fl.getName();
            if (isFileIncludedInSources(fileName)) {
                fileName = iSrcHandler.resolveFileForProject(fileName);
                addFile(sourceFiles, fileName);
            }
            if (fileName.endsWith("_symbian.hpp")) {
                iInf.addExport(iSrcHandler.resolveFileForProject(fileName));
            }
            if (fileName.endsWith("jvm.h")) {
                iInf.addExport(iSrcHandler.resolveFileForProject(fileName));
            }
            if (fileName.endsWith("kni.h")) {
                iInf.addExport(iSrcHandler.resolveFileForProject(fileName));
            }
            if (fileName.endsWith("kni_md.h")) {
                iInf.addExport(iSrcHandler.resolveFileForProject(fileName));
            }
            if (fileName.endsWith("sni.h")) {
                iInf.addExport(iSrcHandler.resolveFileForProject(fileName));
            }
            if (fileName.endsWith("jvmspi.h")) {
                iInf.addExport(iSrcHandler.resolveFileForProject(fileName));
            }
        }
        addExtraSources(sourceFiles);
        for (int i = 0; i < sourceFiles.size(); i++) {
            String fileName = (String) sourceFiles.elementAt(i);
            putln("SOURCE\t" + fileName);
        }
        switch(this.type) {
            case TARGET:
            case ROMGEN:
                writeAsmAndOopMapsFileRules();
                break;
        }
    }

    void writeAsmAndOopMapsFileRules() throws Exception {
        writeAsmAndOopMapsFileRules(true);
        writeAsmAndOopMapsFileRules(false);
        createDummyAsmAndOopMapsFiles(true);
        createDummyAsmAndOopMapsFiles(false);
    }

    void writeAsmAndOopMapsFileRules(boolean isAsm) {
        String srcName, stem, ext;
        if (isAsm) {
            stem = "Interpreter_arm";
            ext = ".s";
        } else {
            stem = "OopMaps";
            ext = ".cpp";
        }
        srcName = ".\\product\\" + stem + ext;
        putln("SOURCE\t" + srcName);
    }

    void createDummyAsmAndOopMapsFiles(boolean isAsm) throws Exception {
        if (isAsm) {
            String asmName = "Interpreter_i386.s";
            PrintWriter writer = tool().openOutputFile(asmName);
            writer.println("\t;Dummy file.");
            writer.println("\t;Will be overwritten during compilation.");
            writer.println("\t;This file is auto-generated.");
            writer.println("\t;Do not edit.");
            writer.close();
        } else {
            String asmName = "OopMaps.cpp";
            PrintWriter writer = tool().openOutputFile(asmName);
            writer.println("// Dummy file.");
            writer.println("// Will be overwritten during compilation.");
            writer.println("// This file is auto-generated.");
            writer.println("// Do not edit.");
            writer.close();
        }
    }

    public boolean isFileIncludedInSources(String fileName) {
        fileName = fileName.toLowerCase();
        if (!fileName.endsWith(".cpp") && !fileName.endsWith(".c")) {
            return false;
        }
        if (fileName.equals("nativestable.cpp")) {
            return false;
        }
        if (fileName.equals("romimage.cpp")) {
            return false;
        }
        return true;
    }

    void writeInclude() {
        Vector vpaths = iSrcHandler.getVpaths();
        for (int i = 0; i < vpaths.size(); i++) {
            String path = (String) vpaths.elementAt(i);
            String file;
            if (getRelativeSourceFile(path).startsWith(tool().getWorkSpaceArg())) {
                file = "..\\..\\.." + getRelativeSourceFile(path).substring(tool().getWorkSpaceArg().length());
            } else {
                file = getRelativeSourceFile(path);
            }
            putln("USERINCLUDE \"" + file + "\" ");
        }
        putln("USERINCLUDE ..\\" + getOutputDir() + File.separator + "incls");
        putln("USERINCLUDE .");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\security\\crypto\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\ams\\suitestore\\internal_api\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\ams\\suitestore\\installer_api\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\ams\\ams_base\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\ams\\ams_base_cldc\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\ams\\suitestore\\common_api\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\ams\\suitestore\\task_manager_api\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\ams\\suitestore\\recordstore_api\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\ams\\platform_request\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\highlevelui\\annunciator\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\lowlevelui\\graphics_api\\gxapi_native\\native");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\security\\file_digest\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\core\\suspend_resume\\sr_main\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\core\\suspend_resume\\sr_vm\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\core\\suspend_resume\\sr_port\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\rms\\record_store\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\jsr75\\src\\cldc_application\\native\\share");
        putln("USERINCLUDE ..\\..\\..\\..\\jsr75\\src\\cldc_application\\native\\share\\inc");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\core\\string\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\push\\push_timer\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\javacall\\implementation\\symbian");
        putln("USERINCLUDE ..\\..\\..\\..\\javacall\\implementation\\share\\utils\\inc");
        putln("USERINCLUDE ..\\..\\..\\..\\javacall\\interface");
        putln("USERINCLUDE ..\\..\\..\\..\\javacall\\interface\\common");
        putln("USERINCLUDE ..\\..\\..\\..\\javacall\\interface\\jsr211_chapi");
        putln("USERINCLUDE ..\\..\\..\\..\\abstractions\\src\\cldc_application\\native\\symbian");
        putln("USERINCLUDE ..\\..\\..\\..\\abstractions\\src\\cldc_application\\native\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\abstractions\\src\\share\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\jsr211\\src\\share\\core\\native\\symbian");
        putln("USERINCLUDE ..\\..\\..\\..\\jsr211\\src\\share\\core\\native\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\jsr211\\src\\cldc_application\\core\\native\\inc");
        putln("USERINCLUDE ..\\..\\..\\..\\restricted_crypto\\src\\restricted_crypto\\reference\\native");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\i18n\\i18n_port\\include");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\i18n\\i18n_main\\reference\\native");
        putln("USERINCLUDE ..\\..\\..\\..\\midp\\src\\i18n\\i18n_main\\include");
        putln("SYSTEMINCLUDE \\epoc32\\include \\epoc32\\include\\libc ..\\generated . \\epoc32\\include\\mmf\\plugin");
    }
}
