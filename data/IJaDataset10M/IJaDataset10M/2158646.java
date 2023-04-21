package com.pixelmed.dicom;

import java.util.StringTokenizer;
import java.rmi.server.UID;
import java.rmi.dgc.VMID;
import com.pixelmed.utils.MACAddress;

/**
 * <p>A class for generating new UIDs, deterministically or not, including potentially reproducible Study, Series and SOP Instance UIDs.</p>
 *
 * @author	dclunie
 */
public class UIDGenerator {

    private static final String identString = "@(#) $Header: /userland/cvs/pixelmed/imgbook/com/pixelmed/dicom/UIDGenerator.java,v 1.5 2005/01/23 14:21:22 dclunie Exp $";

    private static final String UIDGEN_ANY = "0";

    private static final String UIDGEN_INSTANCE_SOP = "1";

    private static final String UIDGEN_INSTANCE_STUDY = "2";

    private static final String UIDGEN_INSTANCE_SERIES = "3";

    private static final String UIDGEN_FRAMEOFREFERENCE = "4";

    private static final String UIDGEN_INSTANCE_DIR = "5";

    private static final String UIDGEN_DIMENSIONORGANIZATION = "6";

    private static final String UIDGEN_CONCATENATION = "7";

    private String stamp;

    private String longStamp;

    private static final String root = VersionAndConstants.uidRoot + "." + VersionAndConstants.uidQualifierForThisToolkit + "." + "1";

    private static final int maxStampComponentLength = 64 - root.length() - 1 - 3 - 2 - 5 - 5 - 5;

    private static final int maxLongStampComponentLength = 64 - root.length() - 1 - 3 - 2;

    private static VMID vmid = new VMID();

    private static long machineAddress = vmid.isUnique() ? (((long) vmid.hashCode()) & 0x0ffffffffl) : new MACAddress().getMACAddress();

    /**
	 * <p>Create a UID generator.</p>
	 *
	 * <p>This will use random and installation specific elements to create a unique root.</p>
	 *
	 */
    public UIDGenerator() {
        newStamp();
    }

    /**
	 * <p>Reinitialize the UID generator with a new stamp using random and installation specific elements to create a unique root.</p>
	 *
	 * <p>For example, use between invocations of getNewUID().</p>
	 *
	 */
    public void newStamp() {
        long ourMachine = machineAddress;
        String string = new UID().toString();
        StringTokenizer st = new StringTokenizer(string, ":");
        int ourUnique = Integer.valueOf(st.nextToken(), 16).intValue();
        long ourTime = Long.valueOf(st.nextToken(), 16).longValue();
        int ourCount = Short.valueOf(st.nextToken(), 16).shortValue() + 0x8000;
        String machineString = Long.toString(Math.abs(ourMachine));
        String vmString = Integer.toString(Math.abs(ourUnique));
        String timeString = Long.toString(Math.abs(ourTime));
        String countString = Integer.toString(Math.abs(ourCount));
        while (ourUnique > 10000 && machineString.length() + vmString.length() + timeString.length() + countString.length() > maxLongStampComponentLength) {
            ourUnique = ourUnique / 10;
            vmString = Integer.toString(Math.abs(ourUnique));
        }
        while (ourMachine > 0 && machineString.length() + vmString.length() + timeString.length() + countString.length() > maxLongStampComponentLength) {
            ourMachine = ourMachine / 10;
            machineString = Long.toString(Math.abs(ourMachine));
        }
        longStamp = machineString + "." + vmString + "." + timeString + "." + countString;
        while (ourUnique > 10000 && machineString.length() + vmString.length() + timeString.length() + countString.length() > maxStampComponentLength) {
            ourUnique = ourUnique / 10;
            vmString = Integer.toString(Math.abs(ourUnique));
        }
        while (ourMachine > 0 && machineString.length() + vmString.length() + timeString.length() + countString.length() > maxStampComponentLength) {
            ourMachine = ourMachine / 10;
            machineString = Long.toString(Math.abs(ourMachine));
        }
        stamp = machineString + "." + vmString + "." + timeString + "." + countString;
    }

    /**
	 * <p>Create a UID generator.</p>
	 *
	 * <p>This will use the supplied stamp rather than generating a unique root, to create repeatable UIDs.</p>
	 *
	 * @param	stamp	a String of dotted numeric values in UID form
	 */
    public UIDGenerator(String stamp) {
        this.stamp = stamp;
    }

    private String getLimitedLengthNumericPartOfStringOrZeroAsUIDExtension(String string) {
        String addition = ".0";
        if (string != null) {
            try {
                long numericPart = Math.abs(Long.parseLong(string));
                if (numericPart > 9999) {
                    numericPart = numericPart % 10000;
                }
                addition = "." + Long.toString(numericPart);
            } catch (NumberFormatException e) {
            }
        }
        return addition;
    }

    /**
	 * <p>Get a new UID for any purpose.</p>
	 *
	 * <p>This will always be the same for this instance of the UIDGenerator, unless newStamp() has been called since the last time.</p>
	 *
	 * @return			the UID
	 * @exception			if result is too long or otherwise not a valid UID
	 */
    public String getNewUID() throws Exception {
        String uid = root + "." + longStamp + "." + UIDGEN_ANY;
        validateUID(uid);
        return uid;
    }

    /**
	 * <p>Get a different new UID for any purpose.</p>
	 *
	 * <p>This will never be the same twice, since newStamp() is called.</p>
	 *
	 * @return			the UID
	 * @exception			if result is too long or otherwise not a valid UID
	 */
    public String getAnotherNewUID() throws Exception {
        newStamp();
        return getNewUID();
    }

    /**
	 * <p>Get a Study Instance UID.</p>
	 *
	 * <p>This will be the same for this instance of the UIDGenerator and the same parameter values.</p>
	 *
	 * <p>Only use this if you really need reproducible UIDs; otherwise use getNewUID().</p>
	 *
	 * @param	studyID		least significant 4 digits of leading numeric part is used
	 * @return			the UID
	 * @exception			if result is too long or otherwise not a valid UID
	 */
    public String getNewStudyInstanceUID(String studyID) throws Exception {
        String uid = root + "." + stamp + "." + UIDGEN_INSTANCE_STUDY + getLimitedLengthNumericPartOfStringOrZeroAsUIDExtension(studyID);
        validateUID(uid);
        return uid;
    }

    /**
	 * <p>Get a Series Instance UID.</p>
	 *
	 * <p>This will be the same for this instance of the UIDGenerator and the same parameter values.</p>
	 *
	 * <p>Only use this if you really need reproducible UIDs; otherwise use getNewUID().</p>
	 *
	 * @param	studyID		least significant 4 digits of leading numeric part is used
	 * @param	seriesNumber	least significant 4 digits of leading numeric part is used
	 * @return			the UID
	 * @exception			if result is too long or otherwise not a valid UID
	 */
    public String getNewSeriesInstanceUID(String studyID, String seriesNumber) throws Exception {
        String uid = root + "." + stamp + "." + UIDGEN_INSTANCE_SERIES + getLimitedLengthNumericPartOfStringOrZeroAsUIDExtension(studyID) + getLimitedLengthNumericPartOfStringOrZeroAsUIDExtension(seriesNumber);
        validateUID(uid);
        return uid;
    }

    /**
	 * <p>Get a SOP Instance UID.</p>
	 *
	 * <p>This will be the same for this instance of the UIDGenerator and the same parameter values.</p>
	 *
	 * <p>Only use this if you really need reproducible UIDs; otherwise use getNewUID().</p>
	 *
	 * @param	studyID		least significant 4 digits of leading numeric part is used
	 * @param	seriesNumber	least significant 4 digits of leading numeric part is used
	 * @param	instanceNumber	least significant 4 digits of leading numeric part is used
	 * @return			the UID
	 * @exception			if result is too long or otherwise not a valid UID
	 */
    public String getNewSOPInstanceUID(String studyID, String seriesNumber, String instanceNumber) throws Exception {
        String uid = root + "." + stamp + "." + UIDGEN_INSTANCE_SOP + getLimitedLengthNumericPartOfStringOrZeroAsUIDExtension(studyID) + getLimitedLengthNumericPartOfStringOrZeroAsUIDExtension(seriesNumber) + getLimitedLengthNumericPartOfStringOrZeroAsUIDExtension(instanceNumber);
        validateUID(uid);
        return uid;
    }

    private static final void validateUID(String uid) throws Exception {
        if (uid.length() > 64) {
            throw new Exception("Generated UID exceeds 64 characters");
        }
    }

    /**
	 * <p>Test generating SOP Instance UIDs.</p>
	 *
	 * @param	arg		a single numeric argument that is the number of UIDs to generate
	 * @param	seriesNumber
	 * @param	instanceNumber
	 * @return			the UID
	 */
    public static final void main(String arg[]) {
        try {
            int count = Integer.parseInt(arg[0]);
            String uids[] = new String[count];
            long startTime = System.currentTimeMillis();
            UIDGenerator generator = new UIDGenerator();
            for (int i = 0; i < count; ++i) {
                uids[i] = generator.getAnotherNewUID();
            }
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            double timePerUID = (double) totalTime / count;
            System.out.println("count=" + count + ", total time=" + totalTime + " ms, time per UID=" + timePerUID + " ms, uids/ms=" + (1 / timePerUID));
            System.err.println("uids[0]=\"" + uids[0] + "\"");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
