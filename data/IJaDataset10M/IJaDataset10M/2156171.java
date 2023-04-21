package peakml.io.dac;

import peakml.util.*;

/**
 * Collection of all the header information stored in a MassLynx raw data file. This
 * is general information, see {@link DACScanStats} for more information on each scan.
 * <p />
 * Note: The class {@link DACExperimentText} contains additional information on the
 * ionisation mode and data type.
 */
public class DACHeader {

    /**
	 * Reads the contents of the header stored in the given file.
	 * 
	 * @param			The file to open.
	 * @return			RTCODE_SUCCESS when all is ok, RTCODE_FILE_NOT_FOUND when the file is not found.
	 */
    public native int open(String filename);

    public String getAcquDate() {
        return AcquDate;
    }

    public String getAcquName() {
        return AcquName;
    }

    public String getAcquTime() {
        return AcquTime;
    }

    public int[] getAnalogOffset() {
        return AnalogOffset;
    }

    public int getAutosamplerType() {
        return AutosamplerType;
    }

    public String getBottleNumber() {
        return BottleNumber;
    }

    public String getConditions() {
        return Conditions;
    }

    public int getEncrypted() {
        return Encrypted;
    }

    public String getGasName() {
        return GasName;
    }

    public String getInstrument() {
        return Instrument;
    }

    public String getInstrumentType() {
        return InstrumentType;
    }

    public String getJobCode() {
        return JobCode;
    }

    public String getLabName() {
        return LabName;
    }

    public int getMuxStream() {
        return MuxStream;
    }

    public String getPepFileName() {
        return PepFileName;
    }

    public String getPlateDesc() {
        return PlateDesc;
    }

    public String getProcess() {
        return Process;
    }

    public int getResolved() {
        return Resolved;
    }

    public String getSampleDesc() {
        return SampleDesc;
    }

    public String getSampleID() {
        return SampleID;
    }

    public double getSolventDelay() {
        return SolventDelay;
    }

    public String getSubmitter() {
        return Submitter;
    }

    public String getTaskCode() {
        return TaskCode;
    }

    public String getUserName() {
        return UserName;
    }

    public int getVersionMajor() {
        return VersionMajor;
    }

    public int getVersionMinor() {
        return VersionMinor;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("DACHeader {\n");
        str.append("  AcquDate:        " + AcquDate + "\n");
        str.append("  AcquName:        " + AcquName + "\n");
        str.append("  AcquTime:        " + AcquTime + "\n");
        str.append("  AnalogOffset:    " + DataTypes.toString(AnalogOffset) + "\n");
        str.append("  AutosamplerType: " + AutosamplerType + "\n");
        str.append("  BottleNumber:    " + BottleNumber + "\n");
        str.append("  Conditions:      " + Conditions + "\n");
        str.append("  Encrypted:       " + Encrypted + "\n");
        str.append("  GasName:         " + GasName + "\n");
        str.append("  Instrument:      " + Instrument + "\n");
        str.append("  InstrumentType:  " + InstrumentType + "\n");
        str.append("  JobCode:         " + JobCode + "\n");
        str.append("  LabName:         " + LabName + "\n");
        str.append("  MuxStream:       " + MuxStream + "\n");
        str.append("  PepFileName:     " + PepFileName + "\n");
        str.append("  PlateDesc:       " + PlateDesc + "\n");
        str.append("  Process:         " + Process + "\n");
        str.append("  Resolved:        " + Resolved + "\n");
        str.append("  SampleDesc:      " + SampleDesc + "\n");
        str.append("  SampleID:        " + SampleID + "\n");
        str.append("  SolventDelay:    " + SolventDelay + "\n");
        str.append("  Submitter:       " + Submitter + "\n");
        str.append("  TaskCode:        " + TaskCode + "\n");
        str.append("  UserName:        " + UserName + "\n");
        str.append("  VersionMajor:    " + VersionMajor + "\n");
        str.append("  VersionMinor:    " + VersionMinor + "\n");
        str.append("}\n");
        return str.toString();
    }

    protected String AcquDate;

    protected String AcquName;

    protected String AcquTime;

    protected int AnalogOffset[];

    protected int AutosamplerType;

    protected String BottleNumber;

    protected String Conditions;

    protected int Encrypted;

    protected String GasName;

    protected String Instrument;

    protected String InstrumentType;

    protected String JobCode;

    protected String LabName;

    protected int MuxStream;

    protected String PepFileName;

    protected String PlateDesc;

    protected String Process;

    protected int Resolved;

    protected String SampleDesc;

    protected String SampleID;

    protected double SolventDelay;

    protected String Submitter;

    protected String TaskCode;

    protected String UserName;

    protected int VersionMajor;

    protected int VersionMinor;
}
