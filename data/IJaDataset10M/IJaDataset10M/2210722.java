package ti.targetinfo.symtable.bfdw;

public class gotplt_union {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected gotplt_union(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(gotplt_union obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            bfdwJNI.delete_gotplt_union(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setRefcount(int refcount) {
        bfdwJNI.set_gotplt_union_refcount(swigCPtr, refcount);
    }

    public int getRefcount() {
        return bfdwJNI.get_gotplt_union_refcount(swigCPtr);
    }

    public void setOffset(long offset) {
        bfdwJNI.set_gotplt_union_offset(swigCPtr, offset);
    }

    public long getOffset() {
        return bfdwJNI.get_gotplt_union_offset(swigCPtr);
    }

    public void setGlist(SWIGTYPE_p_got_entry glist) {
        bfdwJNI.set_gotplt_union_glist(swigCPtr, SWIGTYPE_p_got_entry.getCPtr(glist));
    }

    public SWIGTYPE_p_got_entry getGlist() {
        long cPtr = bfdwJNI.get_gotplt_union_glist(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_got_entry(cPtr, false);
    }

    public void setPlist(SWIGTYPE_p_plt_entry plist) {
        bfdwJNI.set_gotplt_union_plist(swigCPtr, SWIGTYPE_p_plt_entry.getCPtr(plist));
    }

    public SWIGTYPE_p_plt_entry getPlist() {
        long cPtr = bfdwJNI.get_gotplt_union_plist(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_plt_entry(cPtr, false);
    }

    public gotplt_union() {
        this(bfdwJNI.new_gotplt_union(), true);
    }
}
