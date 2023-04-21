package org.catacombae.hfsexplorer.testcode;

import org.catacombae.io.*;
import org.catacombae.hfsexplorer.*;
import org.catacombae.hfsexplorer.partitioning.*;
import org.catacombae.hfsexplorer.win32.*;
import java.io.*;
import org.catacombae.jparted.lib.ps.gpt.GPTPartitionType;

/**
 * 17 August 2008: Changing partition type of partition 2 to microsoft reserved.
 */
public class RepairMyGPTPlease5 {

    private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        long runTimeStamp = System.currentTimeMillis();
        RandomAccessStream llf;
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) llf = new WritableWin32File(args[0]); else llf = new FileStream(args[0]);
        final GUIDPartitionTable originalGpt = new GUIDPartitionTable(llf, 0);
        MutableGUIDPartitionTable gpt = new MutableGUIDPartitionTable(originalGpt);
        if (originalGpt.isValid() && gpt.isValid()) {
            final int blockSize = 512;
            GPTHeader hdr = gpt.getHeader();
            final byte[] mbr = new byte[blockSize];
            byte[] backup1 = new byte[blockSize + hdr.getNumberOfPartitionEntries() * hdr.getSizeOfPartitionEntry()];
            llf.seek(0);
            llf.readFully(mbr);
            llf.readFully(backup1);
            String backupFilename1 = "gpt_mbr_tables-" + runTimeStamp + ".backup";
            System.out.print("Backing up MBR and GPT primary header and table to \"" + backupFilename1 + "\"...");
            FileOutputStream backupFile1 = new FileOutputStream(backupFilename1);
            backupFile1.write(mbr);
            backupFile1.write(backup1);
            backupFile1.close();
            System.out.println("done!");
            byte[] backup2 = new byte[hdr.getNumberOfPartitionEntries() * hdr.getSizeOfPartitionEntry() + blockSize];
            llf.seek(hdr.getBackupLBA() * blockSize - hdr.getNumberOfPartitionEntries() * hdr.getSizeOfPartitionEntry());
            llf.read(backup2);
            String backupFilename2 = "gpt_backup_table-" + runTimeStamp + ".backup";
            System.out.print("Backing up GPT backup header and table to \"" + backupFilename2 + "\"...");
            FileOutputStream backupFile2 = new FileOutputStream(backupFilename2);
            backupFile2.write(backup2);
            backupFile2.close();
            System.out.println("done!");
            byte[] efiSystemPartitionType = GPTPartitionType.PARTITION_TYPE_EFI_SYSTEM.getBytes();
            byte[] microsoftBasicDataType = GPTPartitionType.PARTITION_TYPE_PRIMARY_PARTITION.getBytes();
            byte[] appleHfsType = GPTPartitionType.PARTITION_TYPE_APPLE_HFS.getBytes();
            byte[] microsoftReservedType = GPTPartitionType.PARTITION_TYPE_MICROSOFT_RESERVED.getBytes();
            System.out.print("Checking if the first partition has type \"EFI System Partition\"...");
            byte[] currentType1 = gpt.getEntry(0).getPartitionTypeGUID();
            if (!Util.arraysEqual(currentType1, efiSystemPartitionType)) {
                System.out.println("failed! Halting program.");
                System.exit(0);
            }
            System.out.println("yes.");
            System.out.print("Checking if the second partition has type \"Microsoft Reserved\"...");
            byte[] currentType2 = gpt.getEntry(1).getPartitionTypeGUID();
            if (!Util.arraysEqual(currentType2, microsoftReservedType)) {
                System.out.println("failed! Halting program.");
                System.exit(0);
            }
            System.out.println("yes.");
            System.out.println("All seems to be as expected.");
            System.out.println("Modifying GPT data in memory:");
            System.out.print("  - Setting partition type of partition 2 to Microsoft Basic Data...");
            MutableGPTEntry modifiedPrimaryEntry2 = gpt.getMutablePrimaryEntry(1);
            MutableGPTEntry modifiedBackupEntry2 = gpt.getMutableBackupEntry(1);
            modifiedPrimaryEntry2.setPartitionTypeGUID(microsoftBasicDataType, 0);
            modifiedBackupEntry2.setPartitionTypeGUID(microsoftBasicDataType, 0);
            System.out.println("done.");
            MutableGPTHeader primaryHeader = gpt.getMutablePrimaryHeader();
            MutableGPTHeader backupHeader = gpt.getMutableBackupHeader();
            System.out.print("  - Checking if calculated entries checksums match...");
            int entriesChecksum1 = gpt.calculatePrimaryEntriesChecksum();
            int entriesChecksum2 = gpt.calculateBackupEntriesChecksum();
            if (entriesChecksum1 != entriesChecksum2) {
                System.out.println("failed! Halting program.");
                System.exit(0);
            }
            System.out.println("yes.");
            primaryHeader.setPartitionEntryArrayCRC32(entriesChecksum1);
            backupHeader.setPartitionEntryArrayCRC32(entriesChecksum1);
            System.out.print("  - Checking if gpt.isValid() == false as it should be...");
            if (gpt.isValid()) {
                System.out.println("failed! Halting program.");
                System.exit(0);
            }
            System.out.println("yes.");
            System.out.print("  - Calculating header checksums...");
            primaryHeader.setCRC32Checksum(gpt.calculatePrimaryHeaderChecksum());
            backupHeader.setCRC32Checksum(gpt.calculateBackupHeaderChecksum());
            System.out.println("done.");
            System.out.print("  - Checking if gpt.isValid() == true as it now should be...");
            if (!gpt.isValid()) {
                System.out.println("failed! Halting program.");
                System.exit(0);
            }
            System.out.println("yes.");
            System.out.println("The table is now ready to be written down to disk.");
            System.out.print("Press enter to view the original table:");
            stdin.readLine();
            originalGpt.print(System.out, "");
            System.out.print("Press enter to view the modified table:");
            stdin.readLine();
            gpt.print(System.out, "");
            System.out.print("If you want to write this table to disk, type \"yes\" here: ");
            String answer = stdin.readLine();
            if (answer.equals("yes")) {
                System.out.print("Getting binary data for primary and backup tables...");
                byte[] newPrimaryGPT = gpt.getPrimaryTableBytes();
                byte[] newBackupGPT = gpt.getBackupTableBytes();
                System.out.println("done.");
                String newdataFilename1 = "gpt_mbr_tables-" + runTimeStamp + ".new";
                System.out.print("Writing old MBR and new GPT primary header and table to \"" + newdataFilename1 + "\"...");
                FileOutputStream newdataFile1 = new FileOutputStream(newdataFilename1);
                newdataFile1.write(mbr);
                newdataFile1.write(newPrimaryGPT);
                newdataFile1.close();
                System.out.println("done!");
                String newdataFilename2 = "gpt_backup_table-" + runTimeStamp + ".new";
                System.out.print("Writing new GPT backup header and table to \"" + newdataFilename2 + "\"...");
                FileOutputStream newdataFile2 = new FileOutputStream(newdataFilename2);
                newdataFile2.write(newBackupGPT);
                newdataFile2.close();
                System.out.println("done!");
                System.out.print("Writing primary table...");
                llf.seek(gpt.getPrimaryTableBytesOffset());
                llf.write(newPrimaryGPT);
                System.out.println("done!");
                System.out.print("Writing backup table...");
                llf.seek(gpt.getBackupTableBytesOffset());
                llf.write(newBackupGPT);
                System.out.println("done!");
                System.out.println();
                System.out.println("Checking the newly written GPT...");
                GUIDPartitionTable newGpt = new GUIDPartitionTable(llf, 0);
                newGpt.print(System.out, "");
                if (newGpt.isValid()) System.out.println("The GPT on disk is valid!"); else {
                    System.out.println("INVALID GPT ON DISK! FATAL ERROR!");
                    System.out.println("Try to restore the original GPT tables from the backups files:");
                    System.out.println("  " + backupFilename1);
                    System.out.println("  " + backupFilename2);
                    System.out.println("(dd in linux might do the job)");
                }
            } else System.out.println("Exiting program without modifying anything.");
        } else System.out.println("Could not proceed! Detected an invalid GUID Partition Table on disk.");
        llf.close();
    }
}
