package proteowizard.pwiz.RAMPAdapter;

public class pwiz_swigbindings implements pwiz_swigbindingsConstants {

    public static RAMPFILE rampOpenFile(String filename) {
        long cPtr = pwiz_swigbindingsJNI.rampOpenFile(filename);
        return (cPtr == 0) ? null : new RAMPFILE(cPtr, false);
    }

    public static void rampCloseFile(RAMPFILE pFI) {
        pwiz_swigbindingsJNI.rampCloseFile(RAMPFILE.getCPtr(pFI), pFI);
    }

    public static String rampConstructInputFileName(String basename) {
        return pwiz_swigbindingsJNI.rampConstructInputFileName__SWIG_0(basename);
    }

    public static String rampConstructInputFileName(String buf, int buflen, String basename) {
        return pwiz_swigbindingsJNI.rampConstructInputFileName__SWIG_1(buf, buflen, basename);
    }

    public static String rampConstructInputPath(String buf, int inbuflen, String dir_in, String basename) {
        return pwiz_swigbindingsJNI.rampConstructInputPath(buf, inbuflen, dir_in, basename);
    }

    public static int rampValidateOrDeriveInputFilename(String inbuf, int inbuflen, String spectrumName) {
        return pwiz_swigbindingsJNI.rampValidateOrDeriveInputFilename(inbuf, inbuflen, spectrumName);
    }

    public static String rampTrimBaseName(String buf) {
        return pwiz_swigbindingsJNI.rampTrimBaseName(buf);
    }

    public static String rampValidFileType(String buf) {
        return pwiz_swigbindingsJNI.rampValidFileType(buf);
    }

    public static SWIGTYPE_p_p_char rampListSupportedFileTypes() {
        long cPtr = pwiz_swigbindingsJNI.rampListSupportedFileTypes();
        return (cPtr == 0) ? null : new SWIGTYPE_p_p_char(cPtr, false);
    }

    public static int rampSelfTest(String filename) {
        return pwiz_swigbindingsJNI.rampSelfTest(filename);
    }

    public static SWIGTYPE_p_off_t getIndexOffset(RAMPFILE pFI) {
        return new SWIGTYPE_p_off_t(pwiz_swigbindingsJNI.getIndexOffset(RAMPFILE.getCPtr(pFI), pFI), true);
    }

    public static SWIGTYPE_p_off_t readIndex(RAMPFILE pFI, SWIGTYPE_p_off_t indexOffset, SWIGTYPE_p_int iLastScan) {
        long cPtr = pwiz_swigbindingsJNI.readIndex(RAMPFILE.getCPtr(pFI), pFI, SWIGTYPE_p_off_t.getCPtr(indexOffset), SWIGTYPE_p_int.getCPtr(iLastScan));
        return (cPtr == 0) ? null : new SWIGTYPE_p_off_t(cPtr, false);
    }

    public static void readHeader(RAMPFILE pFI, SWIGTYPE_p_off_t lScanIndex, ScanHeaderStruct scanHeader) {
        pwiz_swigbindingsJNI.readHeader(RAMPFILE.getCPtr(pFI), pFI, SWIGTYPE_p_off_t.getCPtr(lScanIndex), ScanHeaderStruct.getCPtr(scanHeader), scanHeader);
    }

    public static int readMsLevel(RAMPFILE pFI, SWIGTYPE_p_off_t lScanIndex) {
        return pwiz_swigbindingsJNI.readMsLevel(RAMPFILE.getCPtr(pFI), pFI, SWIGTYPE_p_off_t.getCPtr(lScanIndex));
    }

    public static double readStartMz(RAMPFILE pFI, SWIGTYPE_p_off_t lScanIndex) {
        return pwiz_swigbindingsJNI.readStartMz(RAMPFILE.getCPtr(pFI), pFI, SWIGTYPE_p_off_t.getCPtr(lScanIndex));
    }

    public static double readEndMz(RAMPFILE pFI, SWIGTYPE_p_off_t lScanIndex) {
        return pwiz_swigbindingsJNI.readEndMz(RAMPFILE.getCPtr(pFI), pFI, SWIGTYPE_p_off_t.getCPtr(lScanIndex));
    }

    public static int readPeaksCount(RAMPFILE pFI, SWIGTYPE_p_off_t lScanIndex) {
        return pwiz_swigbindingsJNI.readPeaksCount(RAMPFILE.getCPtr(pFI), pFI, SWIGTYPE_p_off_t.getCPtr(lScanIndex));
    }

    public static SWIGTYPE_p_double readPeaks(RAMPFILE pFI, SWIGTYPE_p_off_t lScanIndex) {
        long cPtr = pwiz_swigbindingsJNI.readPeaks(RAMPFILE.getCPtr(pFI), pFI, SWIGTYPE_p_off_t.getCPtr(lScanIndex));
        return (cPtr == 0) ? null : new SWIGTYPE_p_double(cPtr, false);
    }

    public static void readRunHeader(RAMPFILE pFI, SWIGTYPE_p_off_t pScanIndex, RunHeaderStruct runHeader, int iLastScan) {
        pwiz_swigbindingsJNI.readRunHeader(RAMPFILE.getCPtr(pFI), pFI, SWIGTYPE_p_off_t.getCPtr(pScanIndex), RunHeaderStruct.getCPtr(runHeader), runHeader, iLastScan);
    }

    public static void readMSRun(RAMPFILE pFI, RunHeaderStruct runHeader) {
        pwiz_swigbindingsJNI.readMSRun(RAMPFILE.getCPtr(pFI), pFI, RunHeaderStruct.getCPtr(runHeader), runHeader);
    }

    public static InstrumentStruct getInstrumentStruct(RAMPFILE pFI) {
        long cPtr = pwiz_swigbindingsJNI.getInstrumentStruct(RAMPFILE.getCPtr(pFI), pFI);
        return (cPtr == 0) ? null : new InstrumentStruct(cPtr, false);
    }

    public static void setRampOption(int option) {
        pwiz_swigbindingsJNI.setRampOption(option);
    }

    public static int isScanAveraged(ScanHeaderStruct scanHeader) {
        return pwiz_swigbindingsJNI.isScanAveraged(ScanHeaderStruct.getCPtr(scanHeader), scanHeader);
    }

    public static int isScanMergedResult(ScanHeaderStruct scanHeader) {
        return pwiz_swigbindingsJNI.isScanMergedResult(ScanHeaderStruct.getCPtr(scanHeader), scanHeader);
    }

    public static void getScanSpanRange(ScanHeaderStruct scanHeader, SWIGTYPE_p_int startScanNum, SWIGTYPE_p_int endScanNum) {
        pwiz_swigbindingsJNI.getScanSpanRange(ScanHeaderStruct.getCPtr(scanHeader), scanHeader, SWIGTYPE_p_int.getCPtr(startScanNum), SWIGTYPE_p_int.getCPtr(endScanNum));
    }

    public static ScanCacheStruct getScanCache(int size) {
        long cPtr = pwiz_swigbindingsJNI.getScanCache(size);
        return (cPtr == 0) ? null : new ScanCacheStruct(cPtr, false);
    }

    public static void freeScanCache(ScanCacheStruct cache) {
        pwiz_swigbindingsJNI.freeScanCache(ScanCacheStruct.getCPtr(cache), cache);
    }

    public static void clearScanCache(ScanCacheStruct cache) {
        pwiz_swigbindingsJNI.clearScanCache(ScanCacheStruct.getCPtr(cache), cache);
    }

    public static ScanHeaderStruct readHeaderCached(ScanCacheStruct cache, int seqNum, RAMPFILE pFI, SWIGTYPE_p_off_t lScanIndex) {
        long cPtr = pwiz_swigbindingsJNI.readHeaderCached(ScanCacheStruct.getCPtr(cache), cache, seqNum, RAMPFILE.getCPtr(pFI), pFI, SWIGTYPE_p_off_t.getCPtr(lScanIndex));
        return (cPtr == 0) ? null : new ScanHeaderStruct(cPtr, false);
    }

    public static int readMsLevelCached(ScanCacheStruct cache, int seqNum, RAMPFILE pFI, SWIGTYPE_p_off_t lScanIndex) {
        return pwiz_swigbindingsJNI.readMsLevelCached(ScanCacheStruct.getCPtr(cache), cache, seqNum, RAMPFILE.getCPtr(pFI), pFI, SWIGTYPE_p_off_t.getCPtr(lScanIndex));
    }

    public static SWIGTYPE_p_double readPeaksCached(ScanCacheStruct cache, int seqNum, RAMPFILE pFI, SWIGTYPE_p_off_t lScanIndex) {
        long cPtr = pwiz_swigbindingsJNI.readPeaksCached(ScanCacheStruct.getCPtr(cache), cache, seqNum, RAMPFILE.getCPtr(pFI), pFI, SWIGTYPE_p_off_t.getCPtr(lScanIndex));
        return (cPtr == 0) ? null : new SWIGTYPE_p_double(cPtr, false);
    }
}
