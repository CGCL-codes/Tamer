package ti.targetinfo.symtable.bfdw;

public class eh_cie_fde {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected eh_cie_fde(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(eh_cie_fde obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            bfdwJNI.delete_eh_cie_fde(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setCie_inf(eh_cie_fde cie_inf) {
        bfdwJNI.set_eh_cie_fde_cie_inf(swigCPtr, eh_cie_fde.getCPtr(cie_inf));
    }

    public eh_cie_fde getCie_inf() {
        long cPtr = bfdwJNI.get_eh_cie_fde_cie_inf(swigCPtr);
        return (cPtr == 0) ? null : new eh_cie_fde(cPtr, false);
    }

    public void setSize(long size) {
        bfdwJNI.set_eh_cie_fde_size(swigCPtr, size);
    }

    public long getSize() {
        return bfdwJNI.get_eh_cie_fde_size(swigCPtr);
    }

    public void setOffset(long offset) {
        bfdwJNI.set_eh_cie_fde_offset(swigCPtr, offset);
    }

    public long getOffset() {
        return bfdwJNI.get_eh_cie_fde_offset(swigCPtr);
    }

    public void setNew_offset(long new_offset) {
        bfdwJNI.set_eh_cie_fde_new_offset(swigCPtr, new_offset);
    }

    public long getNew_offset() {
        return bfdwJNI.get_eh_cie_fde_new_offset(swigCPtr);
    }

    public void setFde_encoding(short fde_encoding) {
        bfdwJNI.set_eh_cie_fde_fde_encoding(swigCPtr, fde_encoding);
    }

    public short getFde_encoding() {
        return bfdwJNI.get_eh_cie_fde_fde_encoding(swigCPtr);
    }

    public void setLsda_encoding(short lsda_encoding) {
        bfdwJNI.set_eh_cie_fde_lsda_encoding(swigCPtr, lsda_encoding);
    }

    public short getLsda_encoding() {
        return bfdwJNI.get_eh_cie_fde_lsda_encoding(swigCPtr);
    }

    public void setLsda_offset(short lsda_offset) {
        bfdwJNI.set_eh_cie_fde_lsda_offset(swigCPtr, lsda_offset);
    }

    public short getLsda_offset() {
        return bfdwJNI.get_eh_cie_fde_lsda_offset(swigCPtr);
    }

    public void setCie(long cie) {
        bfdwJNI.set_eh_cie_fde_cie(swigCPtr, cie);
    }

    public long getCie() {
        return bfdwJNI.get_eh_cie_fde_cie(swigCPtr);
    }

    public void setRemoved(long removed) {
        bfdwJNI.set_eh_cie_fde_removed(swigCPtr, removed);
    }

    public long getRemoved() {
        return bfdwJNI.get_eh_cie_fde_removed(swigCPtr);
    }

    public void setAdd_augmentation_size(long add_augmentation_size) {
        bfdwJNI.set_eh_cie_fde_add_augmentation_size(swigCPtr, add_augmentation_size);
    }

    public long getAdd_augmentation_size() {
        return bfdwJNI.get_eh_cie_fde_add_augmentation_size(swigCPtr);
    }

    public void setAdd_fde_encoding(long add_fde_encoding) {
        bfdwJNI.set_eh_cie_fde_add_fde_encoding(swigCPtr, add_fde_encoding);
    }

    public long getAdd_fde_encoding() {
        return bfdwJNI.get_eh_cie_fde_add_fde_encoding(swigCPtr);
    }

    public void setMake_relative(long make_relative) {
        bfdwJNI.set_eh_cie_fde_make_relative(swigCPtr, make_relative);
    }

    public long getMake_relative() {
        return bfdwJNI.get_eh_cie_fde_make_relative(swigCPtr);
    }

    public void setMake_lsda_relative(long make_lsda_relative) {
        bfdwJNI.set_eh_cie_fde_make_lsda_relative(swigCPtr, make_lsda_relative);
    }

    public long getMake_lsda_relative() {
        return bfdwJNI.get_eh_cie_fde_make_lsda_relative(swigCPtr);
    }

    public void setNeed_lsda_relative(long need_lsda_relative) {
        bfdwJNI.set_eh_cie_fde_need_lsda_relative(swigCPtr, need_lsda_relative);
    }

    public long getNeed_lsda_relative() {
        return bfdwJNI.get_eh_cie_fde_need_lsda_relative(swigCPtr);
    }

    public void setPer_encoding_relative(long per_encoding_relative) {
        bfdwJNI.set_eh_cie_fde_per_encoding_relative(swigCPtr, per_encoding_relative);
    }

    public long getPer_encoding_relative() {
        return bfdwJNI.get_eh_cie_fde_per_encoding_relative(swigCPtr);
    }

    public eh_cie_fde() {
        this(bfdwJNI.new_eh_cie_fde(), true);
    }
}
