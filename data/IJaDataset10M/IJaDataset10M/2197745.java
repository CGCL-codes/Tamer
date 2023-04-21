package ti.targetinfo.symtable.bfdw;

public class elf_obj_tdata {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected elf_obj_tdata(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(elf_obj_tdata obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            bfdwJNI.delete_elf_obj_tdata(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setElf_header(Elf_Internal_Ehdr elf_header) {
        bfdwJNI.set_elf_obj_tdata_elf_header(swigCPtr, Elf_Internal_Ehdr.getCPtr(elf_header));
    }

    public Elf_Internal_Ehdr getElf_header() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_elf_header(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Ehdr(cPtr, false);
    }

    public void setElf_sect_ptr(SWIGTYPE_p_p_elf_internal_shdr elf_sect_ptr) {
        bfdwJNI.set_elf_obj_tdata_elf_sect_ptr(swigCPtr, SWIGTYPE_p_p_elf_internal_shdr.getCPtr(elf_sect_ptr));
    }

    public SWIGTYPE_p_p_elf_internal_shdr getElf_sect_ptr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_elf_sect_ptr(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_p_elf_internal_shdr(cPtr, false);
    }

    public void setPhdr(elf_internal_phdr phdr) {
        bfdwJNI.set_elf_obj_tdata_phdr(swigCPtr, elf_internal_phdr.getCPtr(phdr));
    }

    public elf_internal_phdr getPhdr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_phdr(swigCPtr);
        return (cPtr == 0) ? null : new elf_internal_phdr(cPtr, false);
    }

    public void setSegment_map(elf_segment_map segment_map) {
        bfdwJNI.set_elf_obj_tdata_segment_map(swigCPtr, elf_segment_map.getCPtr(segment_map));
    }

    public elf_segment_map getSegment_map() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_segment_map(swigCPtr);
        return (cPtr == 0) ? null : new elf_segment_map(cPtr, false);
    }

    public void setStrtab_ptr(SWIGTYPE_p_elf_strtab_hash strtab_ptr) {
        bfdwJNI.set_elf_obj_tdata_strtab_ptr(swigCPtr, SWIGTYPE_p_elf_strtab_hash.getCPtr(strtab_ptr));
    }

    public SWIGTYPE_p_elf_strtab_hash getStrtab_ptr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_strtab_ptr(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_elf_strtab_hash(cPtr, false);
    }

    public void setNum_locals(int num_locals) {
        bfdwJNI.set_elf_obj_tdata_num_locals(swigCPtr, num_locals);
    }

    public int getNum_locals() {
        return bfdwJNI.get_elf_obj_tdata_num_locals(swigCPtr);
    }

    public void setNum_globals(int num_globals) {
        bfdwJNI.set_elf_obj_tdata_num_globals(swigCPtr, num_globals);
    }

    public int getNum_globals() {
        return bfdwJNI.get_elf_obj_tdata_num_globals(swigCPtr);
    }

    public void setNum_elf_sections(long num_elf_sections) {
        bfdwJNI.set_elf_obj_tdata_num_elf_sections(swigCPtr, num_elf_sections);
    }

    public long getNum_elf_sections() {
        return bfdwJNI.get_elf_obj_tdata_num_elf_sections(swigCPtr);
    }

    public void setNum_section_syms(int num_section_syms) {
        bfdwJNI.set_elf_obj_tdata_num_section_syms(swigCPtr, num_section_syms);
    }

    public int getNum_section_syms() {
        return bfdwJNI.get_elf_obj_tdata_num_section_syms(swigCPtr);
    }

    public void setSection_syms(SWIGTYPE_p_p_bfd_symbol section_syms) {
        bfdwJNI.set_elf_obj_tdata_section_syms(swigCPtr, SWIGTYPE_p_p_bfd_symbol.getCPtr(section_syms));
    }

    public SWIGTYPE_p_p_bfd_symbol getSection_syms() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_section_syms(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_p_bfd_symbol(cPtr, false);
    }

    public void setSymtab_hdr(Elf_Internal_Shdr symtab_hdr) {
        bfdwJNI.set_elf_obj_tdata_symtab_hdr(swigCPtr, Elf_Internal_Shdr.getCPtr(symtab_hdr));
    }

    public Elf_Internal_Shdr getSymtab_hdr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_symtab_hdr(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Shdr(cPtr, false);
    }

    public void setShstrtab_hdr(Elf_Internal_Shdr shstrtab_hdr) {
        bfdwJNI.set_elf_obj_tdata_shstrtab_hdr(swigCPtr, Elf_Internal_Shdr.getCPtr(shstrtab_hdr));
    }

    public Elf_Internal_Shdr getShstrtab_hdr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_shstrtab_hdr(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Shdr(cPtr, false);
    }

    public void setStrtab_hdr(Elf_Internal_Shdr strtab_hdr) {
        bfdwJNI.set_elf_obj_tdata_strtab_hdr(swigCPtr, Elf_Internal_Shdr.getCPtr(strtab_hdr));
    }

    public Elf_Internal_Shdr getStrtab_hdr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_strtab_hdr(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Shdr(cPtr, false);
    }

    public void setDynsymtab_hdr(Elf_Internal_Shdr dynsymtab_hdr) {
        bfdwJNI.set_elf_obj_tdata_dynsymtab_hdr(swigCPtr, Elf_Internal_Shdr.getCPtr(dynsymtab_hdr));
    }

    public Elf_Internal_Shdr getDynsymtab_hdr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_dynsymtab_hdr(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Shdr(cPtr, false);
    }

    public void setDynstrtab_hdr(Elf_Internal_Shdr dynstrtab_hdr) {
        bfdwJNI.set_elf_obj_tdata_dynstrtab_hdr(swigCPtr, Elf_Internal_Shdr.getCPtr(dynstrtab_hdr));
    }

    public Elf_Internal_Shdr getDynstrtab_hdr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_dynstrtab_hdr(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Shdr(cPtr, false);
    }

    public void setDynversym_hdr(Elf_Internal_Shdr dynversym_hdr) {
        bfdwJNI.set_elf_obj_tdata_dynversym_hdr(swigCPtr, Elf_Internal_Shdr.getCPtr(dynversym_hdr));
    }

    public Elf_Internal_Shdr getDynversym_hdr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_dynversym_hdr(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Shdr(cPtr, false);
    }

    public void setDynverref_hdr(Elf_Internal_Shdr dynverref_hdr) {
        bfdwJNI.set_elf_obj_tdata_dynverref_hdr(swigCPtr, Elf_Internal_Shdr.getCPtr(dynverref_hdr));
    }

    public Elf_Internal_Shdr getDynverref_hdr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_dynverref_hdr(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Shdr(cPtr, false);
    }

    public void setDynverdef_hdr(Elf_Internal_Shdr dynverdef_hdr) {
        bfdwJNI.set_elf_obj_tdata_dynverdef_hdr(swigCPtr, Elf_Internal_Shdr.getCPtr(dynverdef_hdr));
    }

    public Elf_Internal_Shdr getDynverdef_hdr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_dynverdef_hdr(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Shdr(cPtr, false);
    }

    public void setSymtab_shndx_hdr(Elf_Internal_Shdr symtab_shndx_hdr) {
        bfdwJNI.set_elf_obj_tdata_symtab_shndx_hdr(swigCPtr, Elf_Internal_Shdr.getCPtr(symtab_shndx_hdr));
    }

    public Elf_Internal_Shdr getSymtab_shndx_hdr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_symtab_shndx_hdr(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Shdr(cPtr, false);
    }

    public void setSymtab_section(long symtab_section) {
        bfdwJNI.set_elf_obj_tdata_symtab_section(swigCPtr, symtab_section);
    }

    public long getSymtab_section() {
        return bfdwJNI.get_elf_obj_tdata_symtab_section(swigCPtr);
    }

    public void setShstrtab_section(long shstrtab_section) {
        bfdwJNI.set_elf_obj_tdata_shstrtab_section(swigCPtr, shstrtab_section);
    }

    public long getShstrtab_section() {
        return bfdwJNI.get_elf_obj_tdata_shstrtab_section(swigCPtr);
    }

    public void setStrtab_section(long strtab_section) {
        bfdwJNI.set_elf_obj_tdata_strtab_section(swigCPtr, strtab_section);
    }

    public long getStrtab_section() {
        return bfdwJNI.get_elf_obj_tdata_strtab_section(swigCPtr);
    }

    public void setDynsymtab_section(long dynsymtab_section) {
        bfdwJNI.set_elf_obj_tdata_dynsymtab_section(swigCPtr, dynsymtab_section);
    }

    public long getDynsymtab_section() {
        return bfdwJNI.get_elf_obj_tdata_dynsymtab_section(swigCPtr);
    }

    public void setSymtab_shndx_section(long symtab_shndx_section) {
        bfdwJNI.set_elf_obj_tdata_symtab_shndx_section(swigCPtr, symtab_shndx_section);
    }

    public long getSymtab_shndx_section() {
        return bfdwJNI.get_elf_obj_tdata_symtab_shndx_section(swigCPtr);
    }

    public void setDynversym_section(long dynversym_section) {
        bfdwJNI.set_elf_obj_tdata_dynversym_section(swigCPtr, dynversym_section);
    }

    public long getDynversym_section() {
        return bfdwJNI.get_elf_obj_tdata_dynversym_section(swigCPtr);
    }

    public void setDynverdef_section(long dynverdef_section) {
        bfdwJNI.set_elf_obj_tdata_dynverdef_section(swigCPtr, dynverdef_section);
    }

    public long getDynverdef_section() {
        return bfdwJNI.get_elf_obj_tdata_dynverdef_section(swigCPtr);
    }

    public void setDynverref_section(long dynverref_section) {
        bfdwJNI.set_elf_obj_tdata_dynverref_section(swigCPtr, dynverref_section);
    }

    public long getDynverref_section() {
        return bfdwJNI.get_elf_obj_tdata_dynverref_section(swigCPtr);
    }

    public void setNext_file_pos(long next_file_pos) {
        bfdwJNI.set_elf_obj_tdata_next_file_pos(swigCPtr, next_file_pos);
    }

    public long getNext_file_pos() {
        return bfdwJNI.get_elf_obj_tdata_next_file_pos(swigCPtr);
    }

    public void setGp(long gp) {
        bfdwJNI.set_elf_obj_tdata_gp(swigCPtr, gp);
    }

    public long getGp() {
        return bfdwJNI.get_elf_obj_tdata_gp(swigCPtr);
    }

    public void setGp_size(long gp_size) {
        bfdwJNI.set_elf_obj_tdata_gp_size(swigCPtr, gp_size);
    }

    public long getGp_size() {
        return bfdwJNI.get_elf_obj_tdata_gp_size(swigCPtr);
    }

    public void setCore_signal(int core_signal) {
        bfdwJNI.set_elf_obj_tdata_core_signal(swigCPtr, core_signal);
    }

    public int getCore_signal() {
        return bfdwJNI.get_elf_obj_tdata_core_signal(swigCPtr);
    }

    public void setCore_pid(int core_pid) {
        bfdwJNI.set_elf_obj_tdata_core_pid(swigCPtr, core_pid);
    }

    public int getCore_pid() {
        return bfdwJNI.get_elf_obj_tdata_core_pid(swigCPtr);
    }

    public void setCore_lwpid(int core_lwpid) {
        bfdwJNI.set_elf_obj_tdata_core_lwpid(swigCPtr, core_lwpid);
    }

    public int getCore_lwpid() {
        return bfdwJNI.get_elf_obj_tdata_core_lwpid(swigCPtr);
    }

    public void setCore_program(String core_program) {
        bfdwJNI.set_elf_obj_tdata_core_program(swigCPtr, core_program);
    }

    public String getCore_program() {
        return bfdwJNI.get_elf_obj_tdata_core_program(swigCPtr);
    }

    public void setCore_command(String core_command) {
        bfdwJNI.set_elf_obj_tdata_core_command(swigCPtr, core_command);
    }

    public String getCore_command() {
        return bfdwJNI.get_elf_obj_tdata_core_command(swigCPtr);
    }

    public void setSym_hashes(SWIGTYPE_p_p_elf_link_hash_entry sym_hashes) {
        bfdwJNI.set_elf_obj_tdata_sym_hashes(swigCPtr, SWIGTYPE_p_p_elf_link_hash_entry.getCPtr(sym_hashes));
    }

    public SWIGTYPE_p_p_elf_link_hash_entry getSym_hashes() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_sym_hashes(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_p_elf_link_hash_entry(cPtr, false);
    }

    public void setDt_name(String dt_name) {
        bfdwJNI.set_elf_obj_tdata_dt_name(swigCPtr, dt_name);
    }

    public String getDt_name() {
        return bfdwJNI.get_elf_obj_tdata_dt_name(swigCPtr);
    }

    public void setProgram_header_size(long program_header_size) {
        bfdwJNI.set_elf_obj_tdata_program_header_size(swigCPtr, program_header_size);
    }

    public long getProgram_header_size() {
        return bfdwJNI.get_elf_obj_tdata_program_header_size(swigCPtr);
    }

    public void setLine_info(SWIGTYPE_p_void line_info) {
        bfdwJNI.set_elf_obj_tdata_line_info(swigCPtr, SWIGTYPE_p_void.getCPtr(line_info));
    }

    public SWIGTYPE_p_void getLine_info() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_line_info(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setFind_line_info(SWIGTYPE_p_mips_elf_find_line find_line_info) {
        bfdwJNI.set_elf_obj_tdata_find_line_info(swigCPtr, SWIGTYPE_p_mips_elf_find_line.getCPtr(find_line_info));
    }

    public SWIGTYPE_p_mips_elf_find_line getFind_line_info() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_find_line_info(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_mips_elf_find_line(cPtr, false);
    }

    public void setDwarf1_find_line_info(SWIGTYPE_p_dwarf1_debug dwarf1_find_line_info) {
        bfdwJNI.set_elf_obj_tdata_dwarf1_find_line_info(swigCPtr, SWIGTYPE_p_dwarf1_debug.getCPtr(dwarf1_find_line_info));
    }

    public SWIGTYPE_p_dwarf1_debug getDwarf1_find_line_info() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_dwarf1_find_line_info(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_dwarf1_debug(cPtr, false);
    }

    public void setDwarf2_find_line_info(SWIGTYPE_p_void dwarf2_find_line_info) {
        bfdwJNI.set_elf_obj_tdata_dwarf2_find_line_info(swigCPtr, SWIGTYPE_p_void.getCPtr(dwarf2_find_line_info));
    }

    public SWIGTYPE_p_void getDwarf2_find_line_info() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_dwarf2_find_line_info(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public void setLocal_stubs(SWIGTYPE_p_p_bfd_section local_stubs) {
        bfdwJNI.set_elf_obj_tdata_local_stubs(swigCPtr, SWIGTYPE_p_p_bfd_section.getCPtr(local_stubs));
    }

    public SWIGTYPE_p_p_bfd_section getLocal_stubs() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_local_stubs(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_p_bfd_section(cPtr, false);
    }

    public void setEh_frame_hdr(asection eh_frame_hdr) {
        bfdwJNI.set_elf_obj_tdata_eh_frame_hdr(swigCPtr, asection.getCPtr(eh_frame_hdr));
    }

    public asection getEh_frame_hdr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_eh_frame_hdr(swigCPtr);
        return (cPtr == 0) ? null : new asection(cPtr, false);
    }

    public void setGroup_sect_ptr(SWIGTYPE_p_p_elf_internal_shdr group_sect_ptr) {
        bfdwJNI.set_elf_obj_tdata_group_sect_ptr(swigCPtr, SWIGTYPE_p_p_elf_internal_shdr.getCPtr(group_sect_ptr));
    }

    public SWIGTYPE_p_p_elf_internal_shdr getGroup_sect_ptr() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_group_sect_ptr(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_p_elf_internal_shdr(cPtr, false);
    }

    public void setNum_group(int num_group) {
        bfdwJNI.set_elf_obj_tdata_num_group(swigCPtr, num_group);
    }

    public int getNum_group() {
        return bfdwJNI.get_elf_obj_tdata_num_group(swigCPtr);
    }

    public void setCverdefs(long cverdefs) {
        bfdwJNI.set_elf_obj_tdata_cverdefs(swigCPtr, cverdefs);
    }

    public long getCverdefs() {
        return bfdwJNI.get_elf_obj_tdata_cverdefs(swigCPtr);
    }

    public void setCverrefs(long cverrefs) {
        bfdwJNI.set_elf_obj_tdata_cverrefs(swigCPtr, cverrefs);
    }

    public long getCverrefs() {
        return bfdwJNI.get_elf_obj_tdata_cverrefs(swigCPtr);
    }

    public void setStack_flags(long stack_flags) {
        bfdwJNI.set_elf_obj_tdata_stack_flags(swigCPtr, stack_flags);
    }

    public long getStack_flags() {
        return bfdwJNI.get_elf_obj_tdata_stack_flags(swigCPtr);
    }

    public void setRelro(int relro) {
        bfdwJNI.set_elf_obj_tdata_relro(swigCPtr, relro);
    }

    public int getRelro() {
        return bfdwJNI.get_elf_obj_tdata_relro(swigCPtr);
    }

    public void setVerdef(Elf_Internal_Verdef verdef) {
        bfdwJNI.set_elf_obj_tdata_verdef(swigCPtr, Elf_Internal_Verdef.getCPtr(verdef));
    }

    public Elf_Internal_Verdef getVerdef() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_verdef(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Verdef(cPtr, false);
    }

    public void setVerref(Elf_Internal_Verneed verref) {
        bfdwJNI.set_elf_obj_tdata_verref(swigCPtr, Elf_Internal_Verneed.getCPtr(verref));
    }

    public Elf_Internal_Verneed getVerref() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_verref(swigCPtr);
        return (cPtr == 0) ? null : new Elf_Internal_Verneed(cPtr, false);
    }

    public void setElf_data_symbol(asymbol elf_data_symbol) {
        bfdwJNI.set_elf_obj_tdata_elf_data_symbol(swigCPtr, asymbol.getCPtr(elf_data_symbol));
    }

    public asymbol getElf_data_symbol() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_elf_data_symbol(swigCPtr);
        return (cPtr == 0) ? null : new asymbol(cPtr, false);
    }

    public void setElf_text_symbol(asymbol elf_text_symbol) {
        bfdwJNI.set_elf_obj_tdata_elf_text_symbol(swigCPtr, asymbol.getCPtr(elf_text_symbol));
    }

    public asymbol getElf_text_symbol() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_elf_text_symbol(swigCPtr);
        return (cPtr == 0) ? null : new asymbol(cPtr, false);
    }

    public void setElf_data_section(asection elf_data_section) {
        bfdwJNI.set_elf_obj_tdata_elf_data_section(swigCPtr, asection.getCPtr(elf_data_section));
    }

    public asection getElf_data_section() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_elf_data_section(swigCPtr);
        return (cPtr == 0) ? null : new asection(cPtr, false);
    }

    public void setElf_text_section(asection elf_text_section) {
        bfdwJNI.set_elf_obj_tdata_elf_text_section(swigCPtr, asection.getCPtr(elf_text_section));
    }

    public asection getElf_text_section() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_elf_text_section(swigCPtr);
        return (cPtr == 0) ? null : new asection(cPtr, false);
    }

    public void setDyn_lib_class(dynamic_lib_link_class dyn_lib_class) {
        bfdwJNI.set_elf_obj_tdata_dyn_lib_class(swigCPtr, dyn_lib_class.swigValue());
    }

    public dynamic_lib_link_class getDyn_lib_class() {
        return dynamic_lib_link_class.swigToEnum(bfdwJNI.get_elf_obj_tdata_dyn_lib_class(swigCPtr));
    }

    public void setLinker(int linker) {
        bfdwJNI.set_elf_obj_tdata_linker(swigCPtr, linker);
    }

    public int getLinker() {
        return bfdwJNI.get_elf_obj_tdata_linker(swigCPtr);
    }

    public void setBad_symtab(int bad_symtab) {
        bfdwJNI.set_elf_obj_tdata_bad_symtab(swigCPtr, bad_symtab);
    }

    public int getBad_symtab() {
        return bfdwJNI.get_elf_obj_tdata_bad_symtab(swigCPtr);
    }

    public void setFlags_init(int flags_init) {
        bfdwJNI.set_elf_obj_tdata_flags_init(swigCPtr, flags_init);
    }

    public int getFlags_init() {
        return bfdwJNI.get_elf_obj_tdata_flags_init(swigCPtr);
    }

    public elf_obj_tdata_local_got getLocal_got() {
        long cPtr = bfdwJNI.get_elf_obj_tdata_local_got(swigCPtr);
        return (cPtr == 0) ? null : new elf_obj_tdata_local_got(cPtr, false);
    }

    public elf_obj_tdata() {
        this(bfdwJNI.new_elf_obj_tdata(), true);
    }
}
