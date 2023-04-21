package com.res.java.translation.symbol;

import java.util.ArrayList;
import com.res.cobol.syntaxtree.DataDescriptionEntry;
import com.res.java.lib.CobolSymbol;
import com.res.java.lib.Constants;
import com.res.java.util.NameUtil;

public class SymbolProperties implements Cloneable {

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public short getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(short levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getPictureString() {
        return pictureString;
    }

    public void setPictureString(String pictureString) {
        this.pictureString = pictureString;
    }

    public short getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(short dataUsage) {
        this.dataUsage = dataUsage;
    }

    public CobolSymbol getJavaType() {
        return javaType;
    }

    public void setJavaType(CobolSymbol javaType) {
        if (javaType != null) javaType.usage = (byte) this.dataUsage;
        this.javaType = javaType;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public SymbolProperties getParent() {
        return parent;
    }

    public void setParent(SymbolProperties parent) {
        this.parent = parent;
    }

    public ArrayList<SymbolProperties> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SymbolProperties> children) {
        this.children = children;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public SymbolProperties getMajorIndex() {
        return majorIndex;
    }

    public void setMajorIndex(SymbolProperties fallThruPara) {
        this.majorIndex = fallThruPara;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public boolean getIsFiller() {
        return isFiller;
    }

    public void setIsFiller(boolean isFiller) {
        this.isFiller = isFiller;
    }

    public ArrayList<String> getGroupDictionary() {
        return groupDictionary;
    }

    public void setGroupDictionary(ArrayList<String> groupDictionary) {
        this.groupDictionary = groupDictionary;
    }

    public ArrayList<SymbolProperties> getParagraphList() {
        return paragraphList;
    }

    public void setParagraphList(ArrayList<SymbolProperties> paragraphList) {
        this.paragraphList = paragraphList;
    }

    public int getParagraphMark() {
        return paragraphMark;
    }

    public void setParagraphMark(int paragraphMark) {
        this.paragraphMark = paragraphMark;
    }

    public boolean getIsFormat() {
        return isFormat;
    }

    public void setIsFormat(boolean isFormat) {
        this.isFormat = isFormat;
    }

    public boolean getRef() {
        return ref;
    }

    public void setRef(boolean ref) {
        this.ref = ref;
    }

    public boolean getMod() {
        return mod;
    }

    public void setMod(boolean mod) {
        this.mod = mod;
    }

    public boolean getIsSuppressed() {
        return isSuppressed;
    }

    public void setIsSuppressed(boolean isSuppressed) {
        this.isSuppressed = isSuppressed;
    }

    public String getJavaName1() {
        return javaName1;
    }

    public void setJavaName1(String javaName1) {
        this.javaName1 = javaName1;
    }

    public String getJavaName2() {
        return javaName2;
    }

    public void setJavaName2(String javaName2) {
        this.javaName2 = javaName2;
    }

    public boolean getImportLib() {
        return importLib;
    }

    public void setImportLib(boolean importLib) {
        this.importLib = importLib;
    }

    public boolean getImportBigDecimal() {
        return importBigDecimal;
    }

    public void setImportBigDecimal(boolean importBigDecimal) {
        this.importBigDecimal = importBigDecimal;
    }

    public boolean isUsedNativeJavaTypes() {
        return usedNativeJavaTypes;
    }

    public void setUsedNativeJavaTypes(boolean usedNativeJavaTypes) {
        this.usedNativeJavaTypes = usedNativeJavaTypes;
    }

    public boolean isNumericTested() {
        return isNumericTested;
    }

    public void setNumericTested(boolean isNumericTested) {
        this.isNumericTested = isNumericTested;
    }

    public boolean isAlphabeticTested() {
        return isAlphabeticTested;
    }

    public void setAlphabeticTested(boolean isAlphabeticTested) {
        this.isAlphabeticTested = isAlphabeticTested;
    }

    public boolean isAlphabeticLowerTested() {
        return isAlphabeticLowerTested;
    }

    public void setAlphabeticLowerTested(boolean isAlphabeticLowerTested) {
        this.isAlphabeticLowerTested = isAlphabeticLowerTested;
    }

    public boolean isAlphabeticUpperTested() {
        return isAlphabeticUpperTested;
    }

    public void setAlphabeticUpperTested(boolean isAlphabeticUpperTested) {
        this.isAlphabeticUpperTested = isAlphabeticUpperTested;
    }

    public void setFiller(boolean isFiller) {
        this.isFiller = isFiller;
    }

    public void setFormat(boolean isFormat) {
        this.isFormat = isFormat;
    }

    public void setSuppressed(boolean isSuppressed) {
        this.isSuppressed = isSuppressed;
    }

    public int getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
    }

    public String getMaxOccurs() {
        if (dependingOnOccurs != null) return NameUtil.getJavaName(dependingOnOccurs, false);
        return String.valueOf(maxOccurs);
    }

    public int getMaxOccursInt() {
        return maxOccurs;
    }

    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    public ArrayList<SymbolProperties> getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(ArrayList<SymbolProperties> fs) {
        this.fileStatus = fs;
    }

    public ArrayList<String> getIndexesWorkSpace() {
        return indexesWorkSpace;
    }

    public void setIndexesWorkSpace(ArrayList<String> indexesWorkSpace) {
        this.indexesWorkSpace = indexesWorkSpace;
    }

    public boolean isOccurs() {
        return isOccurs;
    }

    public void setOccurs(boolean isOccurs) {
        this.isOccurs = isOccurs;
    }

    public boolean isVaryingOccurs() {
        return isVaryingOccurs;
    }

    public void setVaryingOccurs(boolean isVaryingOccurs) {
        this.isVaryingOccurs = isVaryingOccurs;
    }

    public ArrayList<SymbolProperties> getIndexedByOccurs() {
        return indexedByOccurs;
    }

    public void setIndexedByOccurs(ArrayList<SymbolProperties> indexedByOccurs) {
        this.indexedByOccurs = indexedByOccurs;
    }

    public int getNoOccursSubscripts() {
        return noOccursSubscripts;
    }

    public void setNoOccursSubscripts(int noOccursSubscripts) {
        this.noOccursSubscripts = noOccursSubscripts;
    }

    public void setNoLivingFillers(int noLivingFillers) {
        this.noLivingFillers = noLivingFillers;
    }

    public int getNoLivingFillers() {
        return noLivingFillers;
    }

    public void setUnAdjustedOffset(int unAdjustedOffset) {
        this.unAdjustedOffset = unAdjustedOffset;
    }

    public int getUnAdjustedOffset() {
        return unAdjustedOffset;
    }

    public boolean isAParentInOccurs() {
        return isAParentInOccurs;
    }

    public void setAParentInOccurs(boolean isAParentInOccurs) {
        this.isAParentInOccurs = isAParentInOccurs;
    }

    public void setOccursParents(ArrayList<SymbolProperties> occursParents) {
        OccursParents = occursParents;
    }

    public SymbolProperties getRedefines() {
        return redefines;
    }

    public void setRedefines(SymbolProperties redefines) {
        this.redefines = redefines;
    }

    public ArrayList<SymbolProperties> getOccursParents() {
        return OccursParents;
    }

    public void setRedefinedBy(ArrayList<SymbolProperties> redefinedBy) {
        this.redefinedBy = redefinedBy;
    }

    public ArrayList<SymbolProperties> getRedefinedBy() {
        return redefinedBy;
    }

    public ArrayList<CoupleValue> getValues() {
        return values;
    }

    public void setValues(ArrayList<CoupleValue> values) {
        this.values = values;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean isSigned) {
        this.isSigned = isSigned;
    }

    public boolean isBlankWhenZero() {
        return isBlankWhenZero;
    }

    public void setBlankWhenZero(boolean isBlankWhenZero) {
        this.isBlankWhenZero = isBlankWhenZero;
    }

    public boolean isCurrency() {
        return isCurrency;
    }

    public void setCurrency(boolean isCurrency) {
        this.isCurrency = isCurrency;
    }

    public boolean isSignLeading() {
        return isSignLeading;
    }

    public void setSignLeading(boolean isSignLeading) {
        this.isSignLeading = isSignLeading;
    }

    public boolean isSignSeparate() {
        return isSignSeparate;
    }

    public void setSignSeparate(boolean isSignSeparate) {
        this.isSignSeparate = isSignSeparate;
    }

    public boolean isJustifiedRight() {
        return isJustifiedRight;
    }

    public void setJustifiedRight(boolean isJustifiedRight) {
        this.isJustifiedRight = isJustifiedRight;
    }

    public boolean isExternal() {
        return isExternal;
    }

    public void setExternal(boolean isExternal) {
        this.isExternal = isExternal;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public void setNoSingleParagraph(boolean noSingleParagraph) {
        this.noSingleParagraph = noSingleParagraph;
    }

    public boolean isNoSingleParagraph() {
        return noSingleParagraph;
    }

    public void setSubstringWorkSpace(ArrayList<String> substringWorkSpace) {
        this.substringWorkSpace = substringWorkSpace;
    }

    public ArrayList<String> getSubstringWorkSpace() {
        return substringWorkSpace;
    }

    public void setAChildHasRedefines(boolean isAChildHasRedefines) {
        this.isAChildHasRedefines = isAChildHasRedefines;
    }

    public boolean isAChildHasRedefines() {
        return isAChildHasRedefines;
    }

    public void setDataDescriptionEntry(DataDescriptionEntry dataDescriptionEntry) {
        this.dataDescriptionEntry = dataDescriptionEntry;
    }

    public DataDescriptionEntry getDataDescriptionEntry() {
        return dataDescriptionEntry;
    }

    public void setOtherData1(int otherData1) {
        this.otherData1 = otherData1;
    }

    public int getOtherData1() {
        return otherData1;
    }

    public void setDoWriteClassFile(boolean doWriteClassFile) {
        this.doWriteClassFile = doWriteClassFile;
    }

    public boolean isDoWriteClassFile() {
        return doWriteClassFile;
    }

    private String dataName;

    private String pictureString;

    private String javaName1;

    private String javaName2;

    private CobolSymbol javaType;

    private SymbolProperties parent;

    private ArrayList<SymbolProperties> children;

    private short levelNumber;

    private short dataUsage;

    private short type;

    private int offset;

    private int length;

    private int adjustedLength;

    public int getAdjustedLength() {
        return adjustedLength;
    }

    public void setAdjustedLength(int adjustedLength) {
        this.adjustedLength = adjustedLength;
    }

    private int paragraphMark;

    private SymbolProperties majorIndex;

    private String value1;

    private String value2;

    private ArrayList<String> groupDictionary;

    private ArrayList<SymbolProperties> paragraphList;

    private int minOccurs;

    private int maxOccurs;

    private SymbolProperties dependingOnOccurs = null;

    private ArrayList<SymbolProperties> fileStatus;

    private ArrayList<SymbolProperties> indexedByOccurs;

    private ArrayList<SymbolProperties> OccursParents;

    private int noOccursSubscripts;

    private int noLivingFillers;

    private int otherData1;

    private int unAdjustedOffset;

    private String otherName;

    private Object otherData2;

    private ArrayList<Object> otherData;

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public void setOtherData2(Object otherData2) {
        this.otherData2 = otherData2;
    }

    public Object getOtherData2() {
        return otherData2;
    }

    private SymbolProperties redefines;

    private ArrayList<SymbolProperties> redefinedBy;

    private ArrayList<CoupleValue> values;

    private boolean hasRenames;

    private SymbolProperties inCopyBook;

    public SymbolProperties getInCopyBook() {
        return inCopyBook;
    }

    public void setInCopyBook(SymbolProperties isInCopyBook) {
        this.inCopyBook = isInCopyBook;
    }

    public boolean hasRenames() {
        return this.hasRenames;
    }

    public void setHasRenames(boolean hr) {
        this.hasRenames = hr;
    }

    public void setOtherData(ArrayList<Object> otherData) {
        this.otherData = otherData;
    }

    public ArrayList<Object> getOtherData() {
        return otherData;
    }

    public void setFillerGetter(String fillerGetter) {
        this.fillerGetter = fillerGetter;
    }

    public String getFillerGetter() {
        return fillerGetter;
    }

    public void setFillerSetter(String fillerSetter) {
        this.fillerSetter = fillerSetter;
    }

    public String getFillerSetter() {
        return fillerSetter;
    }

    public boolean isFileOpenedInput() {
        return isFileInput;
    }

    public void setFileOpenedInput(boolean isFileInput) {
        this.isFileInput = isFileInput;
    }

    public boolean isFileOpenedOutput() {
        return isFileOutput;
    }

    public void setFileOpenedOutput(boolean isFileOutput) {
        this.isFileOutput = isFileOutput;
    }

    public boolean isFileOpenedIO() {
        return isFileInputOutput;
    }

    public void setFileOpenedIO(boolean isFileIO) {
        this.isFileInputOutput = isFileIO;
    }

    public void setFileOpenedExtend(boolean isFileExtend) {
        this.isFileExtend = isFileExtend;
    }

    public boolean isFileOpenedExtend() {
        return isFileExtend;
    }

    public void setVarying(boolean isVarying) {
        this.isVarying = isVarying;
    }

    public boolean isVarying() {
        return isVarying;
    }

    public void setVaryingLen(boolean isVaryingLen) {
        this.isVaryingLen = isVaryingLen;
    }

    public boolean isVaryingLen() {
        return isVaryingLen;
    }

    public void setVaryingArray(boolean isVaryingArray) {
        this.isVaryingArray = isVaryingArray;
    }

    public boolean isVaryingArray() {
        return isVaryingArray;
    }

    private boolean isOccurs;

    private boolean isVaryingOccurs;

    private boolean isFiller;

    private boolean isFormat;

    private boolean ref;

    private boolean mod;

    private boolean isSuppressed;

    private boolean importLib;

    private boolean importBigDecimal;

    private boolean usedNativeJavaTypes;

    private boolean isAChildHasRedefines;

    private boolean isAChildHasDependingOn;

    private boolean isNumericTested;

    private boolean isAlphabeticTested;

    private boolean isAlphabeticLowerTested;

    private boolean isAlphabeticUpperTested;

    private boolean isSpacesTested;

    private boolean isAParentInOccurs;

    private boolean isSigned;

    private boolean isBlankWhenZero;

    private boolean isCurrency;

    private boolean isSignLeading;

    private boolean isSignSeparate;

    private boolean isJustifiedRight;

    private boolean isExternal;

    private boolean isGlobal;

    private boolean doWriteClassFile;

    private boolean noSingleParagraph;

    private boolean isFileInput;

    private boolean isFileOutput;

    private boolean isFileInputOutput;

    private boolean isFileExtend;

    private boolean isVarying;

    private boolean isVaryingArray;

    private boolean isVaryingLen;

    private boolean consolidatePara;

    private boolean fromRESLibrary;

    private boolean isGotoTarget;

    private boolean isIndexRedefines;

    private boolean isAsBytesAccessor;

    private boolean isLinkageSection;

    private boolean isIndexedFileRecord;

    private boolean isAllIndexes = true;

    private boolean isAlteredParagraph = false;

    private ArrayList<String> indexesWorkSpace;

    private ArrayList<String> substringWorkSpace;

    private String fillerGetter;

    private String fillerSetter;

    private DataDescriptionEntry dataDescriptionEntry;

    private SymbolProperties sibling;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean isData() {
        return (this.getType() == SymbolConstants.DATA && this.getDataUsage() < Constants.POINTER);
    }

    public boolean isElementData() {
        return (isData() && this.getPictureString() != null);
    }

    public boolean isGroupData() {
        return (isData() && this.getPictureString() == null && this.getLevelNumber() != 88);
    }

    public boolean is01Group() {
        return (isData() && this.getPictureString() == null && this.getLevelNumber() == 1);
    }

    public boolean isProgram() {
        return (this.getType() == SymbolConstants.PROGRAM);
    }

    public boolean isTopFiller() {
        if (!getIsFiller() || isElementData()) return false;
        SymbolProperties par = getParent();
        while (par != null && par.getIsFiller()) par = par.getParent();
        return !par.is01Group();
    }

    public boolean isFile() {
        return this.type == SymbolConstants.FILE && this.getParent().type == SymbolConstants.PROGRAM;
    }

    public boolean isSequentialFile() {
        return isFile() && this.getDataUsage() == com.res.java.lib.Constants.SEQUENTIAL;
    }

    public boolean isIndexedFile() {
        return isFile() && this.getDataUsage() == com.res.java.lib.Constants.INDEXED;
    }

    public boolean isRandomFile() {
        return isFile() && this.getDataUsage() == com.res.java.lib.Constants.RELATIVE;
    }

    public boolean isLineSequentialFile() {
        return isFile() && this.getDataUsage() == com.res.java.lib.Constants.LINE_SEQUENTIAL;
    }

    public boolean isSequentialAccess() {
        return isFile() && this.getOtherData1() == com.res.java.lib.Constants.SEQUENTIAL_ACCESS;
    }

    public boolean isRandomAccess() {
        return isFile() && this.getOtherData1() == com.res.java.lib.Constants.RANDOM_ACCESS;
    }

    public boolean isDynamicAccess() {
        return isFile() && this.getOtherData1() == com.res.java.lib.Constants.DYNAMIC_ACCESS;
    }

    public boolean hasChildren() {
        return this.children != null && this.children.size() > 0;
    }

    public String toString() {
        return dataName;
    }

    public void setSibling(SymbolProperties sibling) {
        this.sibling = sibling;
    }

    public SymbolProperties getSibling() {
        return sibling;
    }

    public void setConsolidateParagraph(boolean consolidatePara) {
        this.consolidatePara = consolidatePara;
    }

    public boolean isConsolidateParagraph() {
        return consolidatePara;
    }

    public void setFromRESLibrary(boolean fromRESLibrary) {
        this.fromRESLibrary = fromRESLibrary;
    }

    public boolean isFromRESLibrary() {
        return fromRESLibrary;
    }

    public void setGotoTarget(boolean isGotoTarget) {
        this.isGotoTarget = isGotoTarget;
    }

    public boolean isGotoTarget() {
        return isGotoTarget;
    }

    public SymbolProperties() {
        otherData2 = null;
    }

    public class CoupleValue {

        public ExpressionString value1;

        public ExpressionString value2;

        public CoupleValue(String v1, String v2) {
            value1 = new ExpressionString(v1);
            value2 = new ExpressionString(v2);
        }

        public CoupleValue(ExpressionString v1, ExpressionString v2) {
            value1 = v1;
            value2 = v2;
        }
    }

    public boolean isFloatingPoint() {
        return dataUsage == CobolSymbol.COMPUTATIONAL1 || dataUsage == CobolSymbol.COMPUTATIONAL2;
    }

    public void setSpacesTested(boolean isSpacesTested) {
        this.isSpacesTested = isSpacesTested;
    }

    public boolean isSpacesTested() {
        return isSpacesTested;
    }

    public void setIndexRedefines(boolean isIndexRedefines) {
        this.isIndexRedefines = isIndexRedefines;
    }

    public boolean isIndexRedefines() {
        return isIndexRedefines;
    }

    public void setAllIndexes(boolean isAllIndexes) {
        this.isAllIndexes = isAllIndexes;
    }

    public boolean isAllIndexes() {
        return isAllIndexes;
    }

    public void setAsBytesAccessor(boolean isAsBytesAccessor) {
        this.isAsBytesAccessor = isAsBytesAccessor;
    }

    public boolean isAsBytesAccessor() {
        return isAsBytesAccessor;
    }

    public SymbolProperties findNearestCobolBean() {
        if (this.is01Group()) return this;
        if (this.isData()) {
            SymbolProperties par = this;
            do par = par.getParent(); while (par != null && par.getLevelNumber() != 1 && !par.isProgram());
            return par;
        }
        return null;
    }

    public void setLinkageSection(boolean isLinkageSection) {
        this.isLinkageSection = isLinkageSection;
    }

    public boolean isLinkageSection() {
        return isLinkageSection;
    }

    public boolean isTopLevelData() {
        return levelNumber == 01 || levelNumber == 77;
    }

    public void setDependingOnOccurs(SymbolProperties dependingOnOccurs) {
        this.dependingOnOccurs = dependingOnOccurs;
    }

    public SymbolProperties getDependingOnOccurs() {
        return dependingOnOccurs;
    }

    public String getCobolBeanName() {
        if (this.isProgram()) return "this"; else return this.getJavaName1();
    }

    public boolean equals(SymbolProperties to) {
        return this.dataName.equalsIgnoreCase(to.dataName);
    }

    public void setAChildHasDependingOn(boolean isAChildHasDependingOn) {
        this.isAChildHasDependingOn = isAChildHasDependingOn;
    }

    public boolean isAChildHasDependingOn() {
        return isAChildHasDependingOn;
    }

    public void setIndexedFileRecord(boolean isIndexedFileRecord) {
        this.isIndexedFileRecord = isIndexedFileRecord;
    }

    public boolean isIndexedFileRecord() {
        return isIndexedFileRecord;
    }

    public void setAlteredParagraph(boolean isAlteredParagraph) {
        this.isAlteredParagraph = isAlteredParagraph;
    }

    public boolean isAlteredParagraph() {
        return isAlteredParagraph;
    }
}
