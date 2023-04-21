package frost.fcp;

import java.util.logging.Logger;

public class FcpFECUtilsSegmentHeader {

    private static Logger logger = Logger.getLogger(FcpFECUtilsSegmentHeader.class.getName());

    public String FECAlgorithm;

    public long CheckBlockOffset;

    public long SegmentNum;

    public long Segments;

    public long DataBlockOffset;

    public long CheckBlockSize;

    public long Offset;

    public long BlockCount;

    public long BlockSize;

    public long BlocksRequired;

    public long FileLength;

    public long CheckBlockCount;

    public FcpFECUtilsSegmentHeader() {
        logger.fine("SegmentHeader generated");
    }

    public void insertValue(String valueString) {
        if (valueString.startsWith("CheckBlockOffset=")) {
            CheckBlockOffset = Long.parseLong((valueString.split("="))[1], 16);
        } else if (valueString.startsWith("SegmentNum=")) {
            SegmentNum = Long.parseLong((valueString.split("="))[1], 16);
        } else if (valueString.startsWith("Segments=")) {
            Segments = Long.parseLong((valueString.split("="))[1], 16);
        } else if (valueString.startsWith("DataBlockOffset=")) {
            DataBlockOffset = Long.parseLong((valueString.split("="))[1], 16);
        } else if (valueString.startsWith("CheckBlockSize=")) {
            CheckBlockSize = Long.parseLong((valueString.split("="))[1], 16);
        } else if (valueString.startsWith("Offset=")) {
            Offset = Long.parseLong((valueString.split("="))[1], 16);
        } else if (valueString.startsWith("BlockCount=")) {
            BlockCount = Long.parseLong((valueString.split("="))[1], 16);
        } else if (valueString.startsWith("BlockSize=")) {
            BlockSize = Long.parseLong((valueString.split("="))[1], 16);
        } else if (valueString.startsWith("BlocksRequired=")) {
            BlocksRequired = Long.parseLong((valueString.split("="))[1], 16);
        } else if (valueString.startsWith("FileLength=")) {
            FileLength = Long.parseLong((valueString.split("="))[1], 16);
        } else if (valueString.startsWith("CheckBlockCount=")) {
            CheckBlockCount = Long.parseLong((valueString.split("="))[1], 16);
        } else if (valueString.startsWith("FECAlgorithm=")) {
            FECAlgorithm = (valueString.split("="))[1];
        } else logger.warning("Unknown value string: " + valueString);
    }

    public String reconstruct() {
        String result = "";
        result += "FECAlgorithm=" + FECAlgorithm + "\n";
        result += "CheckBlockOffset=" + Long.toHexString(CheckBlockOffset) + "\n";
        result += "SegmentNum=" + Long.toHexString(SegmentNum) + "\n";
        result += "Segments=" + Long.toHexString(Segments) + "\n";
        result += "DataBlockOffset=" + Long.toHexString(DataBlockOffset) + "\n";
        result += "CheckBlockSize=" + Long.toHexString(CheckBlockSize) + "\n";
        result += "Offset=" + Long.toHexString(Offset) + "\n";
        result += "BlockCount=" + Long.toHexString(BlockCount) + "\n";
        result += "BlockSize=" + Long.toHexString(BlockSize) + "\n";
        result += "BlocksRequired=" + Long.toHexString(BlocksRequired) + "\n";
        result += "FileLength=" + Long.toHexString(FileLength) + "\n";
        result += "CheckBlockCount=" + Long.toHexString(CheckBlockCount) + "\n";
        return result;
    }

    public String toString() {
        return reconstruct();
    }
}
