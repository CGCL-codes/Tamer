package ti.targetinfo.symtable.bfdw;

public class bfd_link_hash_entry_u {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected bfd_link_hash_entry_u(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(bfd_link_hash_entry_u obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            bfdwJNI.delete_bfd_link_hash_entry_u(swigCPtr);
        }
        swigCPtr = 0;
    }

    public bfd_link_hash_entry_u_c getC() {
        long cPtr = bfdwJNI.get_bfd_link_hash_entry_u_c(swigCPtr);
        return (cPtr == 0) ? null : new bfd_link_hash_entry_u_c(cPtr, false);
    }

    public bfd_link_hash_entry_u_i getI() {
        long cPtr = bfdwJNI.get_bfd_link_hash_entry_u_i(swigCPtr);
        return (cPtr == 0) ? null : new bfd_link_hash_entry_u_i(cPtr, false);
    }

    public bfd_link_hash_entry_u_def getDef() {
        long cPtr = bfdwJNI.get_bfd_link_hash_entry_u_def(swigCPtr);
        return (cPtr == 0) ? null : new bfd_link_hash_entry_u_def(cPtr, false);
    }

    public bfd_link_hash_entry_u_undef getUndef() {
        long cPtr = bfdwJNI.get_bfd_link_hash_entry_u_undef(swigCPtr);
        return (cPtr == 0) ? null : new bfd_link_hash_entry_u_undef(cPtr, false);
    }

    public bfd_link_hash_entry_u() {
        this(bfdwJNI.new_bfd_link_hash_entry_u(), true);
    }
}
