package makedep;

class SymbianLoop extends NMakefile {

    IDEProject iProj;

    public SymbianLoop(IDETool tool, String name, IDEProject proj) {
        super(tool, name + "\\" + name + ".mak");
        iProj = proj;
    }

    public void writeContents() throws Exception {
        writeHeader();
        putln();
        writeSymbianRules();
        writeRules();
    }

    void writeHeader() throws Exception {
        putln("# Copyright 2007");
        putln();
        putln();
        putln();
    }

    void writeSymbianRules() throws Exception {
        putln("all : dummy.cpp");
        putln();
        putln("MAKMAKE FINAL FREEZE LIB CLEAN CLEANALL CLEANLIB RESOURCE RELEASABLES SAVESPACE : ");
        putln();
        putln("BLD : all");
        putln();
    }

    void writeRules() throws Exception {
        putln("LOOPGEN = ..\\loopgen\\loopgen.exe");
        putln("CLDC_DIR = ..\\cldc_vm_lib");
        putln("ROMGEN_DIR = ..\\romgen");
        putln();
        putln("Dummy.cpp: $(LOOPGEN)");
        puttabln("@echo Creating Interpreter Loops");
        puttabln("$(LOOPGEN) -generate +TraceBytecodes +GenerateDebugAssembly +GenerateInlineAsm +GenerateROMImage -outputdir $(CLDC_DIR)\\Debug");
        puttabln("$(LOOPGEN) +GenerateOopMaps -outputdir $(CLDC_DIR)\\Debug");
        puttabln("$(LOOPGEN) -generateoptimized +TraceBytecodes -outputdir $(CLDC_DIR)\\Release");
        puttabln("$(LOOPGEN) +GenerateOopMaps -outputdir $(CLDC_DIR)\\Release");
        puttabln("$(LOOPGEN) -generateoptimized +TraceBytecodes -outputdir $(CLDC_DIR)\\Product");
        puttabln("$(LOOPGEN) +GenerateOopMaps -outputdir $(CLDC_DIR)\\Product");
        puttabln("$(LOOPGEN) -generateoptimized +TraceBytecodes -outputdir $(ROMGEN_DIR)\\Release");
        puttabln("$(LOOPGEN) +GenerateOopMaps -outputdir $(ROMGEN_DIR)\\Release");
        putln();
    }
}
