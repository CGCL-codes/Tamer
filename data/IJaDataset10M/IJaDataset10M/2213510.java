package ti.targetinfo.symtable.bfdw;

public class internal_extra_pe_aouthdr {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected internal_extra_pe_aouthdr(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(internal_extra_pe_aouthdr obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            bfdwJNI.delete_internal_extra_pe_aouthdr(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setImageBase(long ImageBase) {
        bfdwJNI.set_internal_extra_pe_aouthdr_ImageBase(swigCPtr, ImageBase);
    }

    public long getImageBase() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_ImageBase(swigCPtr);
    }

    public void setSectionAlignment(long SectionAlignment) {
        bfdwJNI.set_internal_extra_pe_aouthdr_SectionAlignment(swigCPtr, SectionAlignment);
    }

    public long getSectionAlignment() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_SectionAlignment(swigCPtr);
    }

    public void setFileAlignment(long FileAlignment) {
        bfdwJNI.set_internal_extra_pe_aouthdr_FileAlignment(swigCPtr, FileAlignment);
    }

    public long getFileAlignment() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_FileAlignment(swigCPtr);
    }

    public void setMajorOperatingSystemVersion(short MajorOperatingSystemVersion) {
        bfdwJNI.set_internal_extra_pe_aouthdr_MajorOperatingSystemVersion(swigCPtr, MajorOperatingSystemVersion);
    }

    public short getMajorOperatingSystemVersion() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_MajorOperatingSystemVersion(swigCPtr);
    }

    public void setMinorOperatingSystemVersion(short MinorOperatingSystemVersion) {
        bfdwJNI.set_internal_extra_pe_aouthdr_MinorOperatingSystemVersion(swigCPtr, MinorOperatingSystemVersion);
    }

    public short getMinorOperatingSystemVersion() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_MinorOperatingSystemVersion(swigCPtr);
    }

    public void setMajorImageVersion(short MajorImageVersion) {
        bfdwJNI.set_internal_extra_pe_aouthdr_MajorImageVersion(swigCPtr, MajorImageVersion);
    }

    public short getMajorImageVersion() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_MajorImageVersion(swigCPtr);
    }

    public void setMinorImageVersion(short MinorImageVersion) {
        bfdwJNI.set_internal_extra_pe_aouthdr_MinorImageVersion(swigCPtr, MinorImageVersion);
    }

    public short getMinorImageVersion() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_MinorImageVersion(swigCPtr);
    }

    public void setMajorSubsystemVersion(short MajorSubsystemVersion) {
        bfdwJNI.set_internal_extra_pe_aouthdr_MajorSubsystemVersion(swigCPtr, MajorSubsystemVersion);
    }

    public short getMajorSubsystemVersion() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_MajorSubsystemVersion(swigCPtr);
    }

    public void setMinorSubsystemVersion(short MinorSubsystemVersion) {
        bfdwJNI.set_internal_extra_pe_aouthdr_MinorSubsystemVersion(swigCPtr, MinorSubsystemVersion);
    }

    public short getMinorSubsystemVersion() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_MinorSubsystemVersion(swigCPtr);
    }

    public void setReserved1(int Reserved1) {
        bfdwJNI.set_internal_extra_pe_aouthdr_Reserved1(swigCPtr, Reserved1);
    }

    public int getReserved1() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_Reserved1(swigCPtr);
    }

    public void setSizeOfImage(int SizeOfImage) {
        bfdwJNI.set_internal_extra_pe_aouthdr_SizeOfImage(swigCPtr, SizeOfImage);
    }

    public int getSizeOfImage() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_SizeOfImage(swigCPtr);
    }

    public void setSizeOfHeaders(int SizeOfHeaders) {
        bfdwJNI.set_internal_extra_pe_aouthdr_SizeOfHeaders(swigCPtr, SizeOfHeaders);
    }

    public int getSizeOfHeaders() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_SizeOfHeaders(swigCPtr);
    }

    public void setCheckSum(int CheckSum) {
        bfdwJNI.set_internal_extra_pe_aouthdr_CheckSum(swigCPtr, CheckSum);
    }

    public int getCheckSum() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_CheckSum(swigCPtr);
    }

    public void setSubsystem(short Subsystem) {
        bfdwJNI.set_internal_extra_pe_aouthdr_Subsystem(swigCPtr, Subsystem);
    }

    public short getSubsystem() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_Subsystem(swigCPtr);
    }

    public void setDllCharacteristics(short DllCharacteristics) {
        bfdwJNI.set_internal_extra_pe_aouthdr_DllCharacteristics(swigCPtr, DllCharacteristics);
    }

    public short getDllCharacteristics() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_DllCharacteristics(swigCPtr);
    }

    public void setSizeOfStackReserve(long SizeOfStackReserve) {
        bfdwJNI.set_internal_extra_pe_aouthdr_SizeOfStackReserve(swigCPtr, SizeOfStackReserve);
    }

    public long getSizeOfStackReserve() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_SizeOfStackReserve(swigCPtr);
    }

    public void setSizeOfStackCommit(long SizeOfStackCommit) {
        bfdwJNI.set_internal_extra_pe_aouthdr_SizeOfStackCommit(swigCPtr, SizeOfStackCommit);
    }

    public long getSizeOfStackCommit() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_SizeOfStackCommit(swigCPtr);
    }

    public void setSizeOfHeapReserve(long SizeOfHeapReserve) {
        bfdwJNI.set_internal_extra_pe_aouthdr_SizeOfHeapReserve(swigCPtr, SizeOfHeapReserve);
    }

    public long getSizeOfHeapReserve() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_SizeOfHeapReserve(swigCPtr);
    }

    public void setSizeOfHeapCommit(long SizeOfHeapCommit) {
        bfdwJNI.set_internal_extra_pe_aouthdr_SizeOfHeapCommit(swigCPtr, SizeOfHeapCommit);
    }

    public long getSizeOfHeapCommit() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_SizeOfHeapCommit(swigCPtr);
    }

    public void setLoaderFlags(int LoaderFlags) {
        bfdwJNI.set_internal_extra_pe_aouthdr_LoaderFlags(swigCPtr, LoaderFlags);
    }

    public int getLoaderFlags() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_LoaderFlags(swigCPtr);
    }

    public void setNumberOfRvaAndSizes(int NumberOfRvaAndSizes) {
        bfdwJNI.set_internal_extra_pe_aouthdr_NumberOfRvaAndSizes(swigCPtr, NumberOfRvaAndSizes);
    }

    public int getNumberOfRvaAndSizes() {
        return bfdwJNI.get_internal_extra_pe_aouthdr_NumberOfRvaAndSizes(swigCPtr);
    }

    public void setDataDirectory(IMAGE_DATA_DIRECTORY DataDirectory) {
        bfdwJNI.set_internal_extra_pe_aouthdr_DataDirectory(swigCPtr, IMAGE_DATA_DIRECTORY.getCPtr(DataDirectory));
    }

    public IMAGE_DATA_DIRECTORY getDataDirectory() {
        long cPtr = bfdwJNI.get_internal_extra_pe_aouthdr_DataDirectory(swigCPtr);
        return (cPtr == 0) ? null : new IMAGE_DATA_DIRECTORY(cPtr, false);
    }

    public internal_extra_pe_aouthdr() {
        this(bfdwJNI.new_internal_extra_pe_aouthdr(), true);
    }
}
