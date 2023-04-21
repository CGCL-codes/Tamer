package ti.targetinfo.symtable.bfdw;

public class Elf_External_Verneed {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected Elf_External_Verneed(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(Elf_External_Verneed obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            bfdwJNI.delete_Elf_External_Verneed(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setVn_version(SWIGTYPE_p_unsigned_char vn_version) {
        bfdwJNI.set_Elf_External_Verneed_vn_version(swigCPtr, SWIGTYPE_p_unsigned_char.getCPtr(vn_version));
    }

    public SWIGTYPE_p_unsigned_char getVn_version() {
        long cPtr = bfdwJNI.get_Elf_External_Verneed_vn_version(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_char(cPtr, false);
    }

    public void setVn_cnt(SWIGTYPE_p_unsigned_char vn_cnt) {
        bfdwJNI.set_Elf_External_Verneed_vn_cnt(swigCPtr, SWIGTYPE_p_unsigned_char.getCPtr(vn_cnt));
    }

    public SWIGTYPE_p_unsigned_char getVn_cnt() {
        long cPtr = bfdwJNI.get_Elf_External_Verneed_vn_cnt(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_char(cPtr, false);
    }

    public void setVn_file(SWIGTYPE_p_unsigned_char vn_file) {
        bfdwJNI.set_Elf_External_Verneed_vn_file(swigCPtr, SWIGTYPE_p_unsigned_char.getCPtr(vn_file));
    }

    public SWIGTYPE_p_unsigned_char getVn_file() {
        long cPtr = bfdwJNI.get_Elf_External_Verneed_vn_file(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_char(cPtr, false);
    }

    public void setVn_aux(SWIGTYPE_p_unsigned_char vn_aux) {
        bfdwJNI.set_Elf_External_Verneed_vn_aux(swigCPtr, SWIGTYPE_p_unsigned_char.getCPtr(vn_aux));
    }

    public SWIGTYPE_p_unsigned_char getVn_aux() {
        long cPtr = bfdwJNI.get_Elf_External_Verneed_vn_aux(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_char(cPtr, false);
    }

    public void setVn_next(SWIGTYPE_p_unsigned_char vn_next) {
        bfdwJNI.set_Elf_External_Verneed_vn_next(swigCPtr, SWIGTYPE_p_unsigned_char.getCPtr(vn_next));
    }

    public SWIGTYPE_p_unsigned_char getVn_next() {
        long cPtr = bfdwJNI.get_Elf_External_Verneed_vn_next(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_char(cPtr, false);
    }

    public Elf_External_Verneed() {
        this(bfdwJNI.new_Elf_External_Verneed(), true);
    }
}
