package ti.targetinfo.symtable.bfdw;

public class elf_internal_sym {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected elf_internal_sym(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(elf_internal_sym obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            bfdwJNI.delete_elf_internal_sym(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setSt_value(long st_value) {
        bfdwJNI.set_elf_internal_sym_st_value(swigCPtr, st_value);
    }

    public long getSt_value() {
        return bfdwJNI.get_elf_internal_sym_st_value(swigCPtr);
    }

    public void setSt_size(long st_size) {
        bfdwJNI.set_elf_internal_sym_st_size(swigCPtr, st_size);
    }

    public long getSt_size() {
        return bfdwJNI.get_elf_internal_sym_st_size(swigCPtr);
    }

    public void setSt_name(long st_name) {
        bfdwJNI.set_elf_internal_sym_st_name(swigCPtr, st_name);
    }

    public long getSt_name() {
        return bfdwJNI.get_elf_internal_sym_st_name(swigCPtr);
    }

    public void setSt_info(short st_info) {
        bfdwJNI.set_elf_internal_sym_st_info(swigCPtr, st_info);
    }

    public short getSt_info() {
        return bfdwJNI.get_elf_internal_sym_st_info(swigCPtr);
    }

    public void setSt_other(short st_other) {
        bfdwJNI.set_elf_internal_sym_st_other(swigCPtr, st_other);
    }

    public short getSt_other() {
        return bfdwJNI.get_elf_internal_sym_st_other(swigCPtr);
    }

    public void setSt_shndx(long st_shndx) {
        bfdwJNI.set_elf_internal_sym_st_shndx(swigCPtr, st_shndx);
    }

    public long getSt_shndx() {
        return bfdwJNI.get_elf_internal_sym_st_shndx(swigCPtr);
    }

    public elf_internal_sym() {
        this(bfdwJNI.new_elf_internal_sym(), true);
    }
}
