package frost;

import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.*;
import frost.FcpTools.*;
import fillament.util.*;

/**
 * Requests a key from freenet
 * @author <a href=mailto:jantho666@hotmail.com>Jan-Thomas Czornack</a>
 */
public class FcpRequest {

    static final boolean DEBUG = true;

    static final int chunkSize = 262144;

    private static ThreadLocal healingQueue = new ThreadLocal();

    private static ThreadLocal healer = new ThreadLocal();

    private static class HealerThread extends Thread {

        private WorkQueue wq;

        private String Name;

        public HealerThread(WorkQueue wq, String Name) {
            this.Name = Name;
            this.wq = wq;
        }

        public void run() {
            System.out.println("healer starting for " + Name);
            String[][] results = new String[1][2];
            while (wq.hasMore()) {
                File block = (File) wq.next();
                System.out.println("\ntrying to heal " + Name + " with " + block.getPath());
                Thread inserter = new putKeyThread("CHK@", block, 5, results, 0, true);
                inserter.run();
                block.delete();
            }
            System.out.println("healer ending for " + Name);
        }
    }

    private static int getActiveThreads(Thread[] threads) {
        int count = 0;
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null) {
                if (threads[i].isAlive()) count++;
            }
        }
        return count;
    }

    public static boolean getSplitFile(String key, File target, int htl) {
        String blockCount = SettingsFun.getValue(target.getPath(), "SplitFile.BlockCount");
        String splitFileSize = SettingsFun.getValue(target.getPath(), "SplitFile.Size");
        String splitFileBlocksize = SettingsFun.getValue(target.getPath(), "SplitFile.Blocksize");
        int maxThreads = 3;
        maxThreads = frame1.frostSettings.getIntValue("splitfileDownloadThreads");
        int intBlockCount = 0;
        try {
            intBlockCount = Integer.parseInt(blockCount, 16);
        } catch (NumberFormatException e) {
        }
        int intSplitFileSize = -1;
        try {
            intSplitFileSize = Integer.parseInt(splitFileSize, 16);
        } catch (NumberFormatException e) {
        }
        int intSplitFileBlocksize = -1;
        try {
            intSplitFileBlocksize = Integer.parseInt(splitFileBlocksize, 16);
        } catch (NumberFormatException e) {
        }
        int[] blockNumbers = new int[intBlockCount];
        for (int i = 0; i < intBlockCount; i++) blockNumbers[i] = i + 1;
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < intBlockCount; i++) {
            int tmp = blockNumbers[i];
            int randomNumber = Math.abs(rand.nextInt()) % intBlockCount;
            blockNumbers[i] = blockNumbers[randomNumber];
            blockNumbers[randomNumber] = tmp;
        }
        boolean success = true;
        boolean[] results = new boolean[intBlockCount];
        Thread[] threads = new Thread[intBlockCount];
        for (int i = 0; i < intBlockCount; i++) {
            int j = blockNumbers[i];
            String chk = SettingsFun.getValue(target.getPath(), "SplitFile.Block." + Integer.toHexString(j));
            if (DEBUG) System.out.println("Requesting: SplitFile.Block." + Integer.toHexString(j) + "=" + chk);
            while (getActiveThreads(threads) >= maxThreads) mixed.wait(5000);
            int checkSize = intSplitFileBlocksize;
            if (blockNumbers[i] == intBlockCount && intSplitFileBlocksize != -1) checkSize = intSplitFileSize - (intSplitFileBlocksize * (intBlockCount - 1));
            threads[i] = new getKeyThread(chk, new File(frame1.keypool + target.getName() + "-chunk-" + j), htl, results, i, checkSize);
            threads[i].start();
        }
        while (getActiveThreads(threads) > 0) {
            if (DEBUG) System.out.println("Active Splitfile request remaining (htl " + htl + "): " + getActiveThreads(threads));
            mixed.wait(5000);
        }
        for (int i = 0; i < intBlockCount; i++) {
            if (!results[i]) {
                success = false;
                if (DEBUG) System.out.println("NO SUCCESS");
            } else {
                if (DEBUG) System.out.println("SUCCESS");
            }
        }
        if (success) {
            FileOutputStream fileOut;
            try {
                fileOut = new FileOutputStream(target);
                if (DEBUG) System.out.println("Connecting chunks");
                for (int i = 1; i <= intBlockCount; i++) {
                    if (DEBUG) System.out.println("Adding chunk " + i + " to " + target.getName());
                    File toRead = new File(frame1.keypool + target.getName() + "-chunk-" + i);
                    fileOut.write(FileAccess.readByteArray(toRead));
                    toRead.deleteOnExit();
                    toRead.delete();
                }
                fileOut.close();
            } catch (IOException e) {
                if (DEBUG) System.out.println("Write Error: " + target.getPath());
            }
        } else {
            target.delete();
            if (DEBUG) System.out.println("!!!!!! Download of " + target.getName() + " failed.");
        }
        return success;
    }

    public static boolean getFECSplitFile(String key, File target, int htl) {
        Vector toHeal = new Vector();
        healingQueue.set(new WorkQueue());
        HealerThread hl = new HealerThread((WorkQueue) (healingQueue.get()), target.getPath());
        healer.set(hl);
        Vector segmentHeaders = null;
        FcpFECUtils fecutils = new FcpFECUtils(frame1.frostSettings.getValue("nodeAddress"), frame1.frostSettings.getIntValue("nodePort"));
        boolean success = true;
        int maxThreads = 3;
        maxThreads = frame1.frostSettings.getIntValue("splitfileDownloadThreads");
        long splitFileSize = -1;
        try {
            splitFileSize = Long.parseLong(SettingsFun.getValue(target.getPath(), "SplitFile.Size"), 16);
        } catch (NumberFormatException e) {
        }
        {
            synchronized (fecutils.getClass()) {
                try {
                    segmentHeaders = fecutils.FECSegmentFile("OnionFEC_a_1_2", splitFileSize);
                } catch (Exception e) {
                    System.out.println("exeption in getfecsplit " + e.toString());
                }
            }
        }
        for (int segmentCnt = 0; segmentCnt < segmentHeaders.size(); segmentCnt++) {
            FcpFECUtilsSegmentHeader currentSegment = (FcpFECUtilsSegmentHeader) segmentHeaders.get(segmentCnt);
            int blockCount = (int) currentSegment.BlockCount;
            int blockSize = (int) currentSegment.BlockSize;
            int checkBlockSize = (int) currentSegment.CheckBlockSize;
            int checkBlockCount = (int) currentSegment.CheckBlockCount;
            int requiredBlocks = (int) currentSegment.BlocksRequired;
            int chunkBase = (int) currentSegment.DataBlockOffset;
            int checkBase = (int) currentSegment.CheckBlockOffset;
            int totalBlocks = blockCount + checkBlockCount;
            int blockNo = 0;
            int[] blockNumbers = new int[totalBlocks];
            for (int i = 0; i < totalBlocks; i++) blockNumbers[i] = i;
            Random rand = new Random(System.currentTimeMillis());
            for (int i = 0; i < totalBlocks; i++) {
                int tmp = blockNumbers[i];
                int randomNumber = Math.abs(rand.nextInt()) % totalBlocks;
                blockNumbers[i] = blockNumbers[randomNumber];
                blockNumbers[randomNumber] = tmp;
            }
            boolean[] results = new boolean[totalBlocks];
            Thread[] threads = new Thread[totalBlocks];
            int successfullBlocks = 0;
            int availableChunks = 0;
            for (int i = 0; i < totalBlocks; i++) {
                File chunk;
                int j = blockNumbers[i];
                results[j] = false;
                if (j < blockCount) {
                    if (getKeyThread.checkKey(SettingsFun.getValue(target.getPath(), "SplitFile.Block." + Integer.toHexString(j + chunkBase + 1)), new File(new StringBuffer().append(frame1.keypool).append(target.getName()).append("-chunk-").append((j + chunkBase + 1)).toString()), blockSize)) {
                        results[j] = true;
                        availableChunks++;
                        successfullBlocks++;
                    }
                } else {
                    if (getKeyThread.checkKey(SettingsFun.getValue(target.getPath(), "SplitFile.Block." + Integer.toHexString(j + chunkBase + 1)), new File(new StringBuffer().append(frame1.keypool).append(target.getName()).append("-check-").append((j + checkBase + 1)).toString()), checkBlockSize)) {
                        results[j] = true;
                        successfullBlocks++;
                    }
                }
            }
            System.out.println(new StringBuffer().append("Found ").append(availableChunks).append(" chunks for segment ").append(segmentCnt).toString());
            if (availableChunks == requiredBlocks) {
                System.out.println("Already have all chunks for segment " + segmentCnt);
                for (int i = 0; i < totalBlocks; i++) {
                    int j = blockNumbers[i];
                    if (j >= blockCount) {
                        File chunk = new File(new StringBuffer().append(frame1.keypool).append(target.getName()).append("-check-").append((j + checkBase + 1)).toString());
                        if (chunk.exists()) chunk.delete();
                    }
                }
                continue;
            }
            int i = 0;
            while (successfullBlocks < requiredBlocks) {
                int j;
                String chk;
                while ((i < totalBlocks) && (results[blockNumbers[i]])) i++;
                if (i < totalBlocks) {
                    j = blockNumbers[i];
                    System.out.println(new StringBuffer().append("FcpRequest i = ").append(i).append(", j = ").append(j).append(", successfulBlock = ").append(successfullBlocks).append(", activeThreads = ").append(getActiveThreads(threads)).toString());
                    if (j < blockCount) chk = SettingsFun.getValue(target.getPath(), "SplitFile.Block." + Integer.toHexString(j + chunkBase + 1)); else chk = SettingsFun.getValue(target.getPath(), "SplitFile.CheckBlock." + Integer.toHexString(j + checkBase - blockCount + 1));
                    while (getActiveThreads(threads) >= maxThreads) {
                        mixed.wait(5000);
                    }
                    successfullBlocks = 0;
                    for (int k = 0; k < totalBlocks; k++) {
                        if (results[k]) successfullBlocks++;
                    }
                    if (getActiveThreads(threads) + successfullBlocks < requiredBlocks) {
                        if (j < blockCount) threads[j] = new getKeyThread(chk, new File(new StringBuffer().append(frame1.keypool).append(target.getName()).append("-chunk-").append((j + chunkBase + 1)).toString()), htl, results, j, blockSize); else threads[j] = new getKeyThread(chk, new File(new StringBuffer().append(frame1.keypool).append(target.getName()).append("-check-").append((j + checkBase + 1)).toString()), htl, results, j, checkBlockSize);
                        threads[j].start();
                        i++;
                    } else {
                        mixed.wait(5000);
                    }
                } else {
                    System.out.println("Reached totalBlocks, still outstanding: " + getActiveThreads(threads));
                    if (getActiveThreads(threads) == 0) {
                        success = false;
                        break;
                    }
                    mixed.wait(5000);
                }
            }
            while (getActiveThreads(threads) > 0) {
                if (DEBUG) System.out.println("Should not occur: Active Splitfile request remaining (htl " + htl + "): " + getActiveThreads(threads));
                mixed.wait(5000);
            }
            if (!success) {
                if (DEBUG) System.out.println("NO SUCCESS Segment " + (int) currentSegment.SegmentNum);
            } else {
                if (DEBUG) System.out.println("SUCCESS Segment " + (int) currentSegment.SegmentNum);
                String blockList = new String();
                String checkList = new String();
                String requestList = new String();
                int suppliedBlocks = 0;
                int suppliedChecks = 0;
                for (i = 0; i < blockCount; i++) {
                    if (results[i]) {
                        if (blockList.length() > 0) blockList += ",";
                        blockList += Integer.toHexString(i);
                        suppliedBlocks++;
                    } else {
                        if (requestList.length() > 0) requestList += ",";
                        requestList += Integer.toHexString(i);
                    }
                }
                for (i = 0; i < checkBlockCount; i++) {
                    if (results[i + blockCount]) {
                        if (checkList.length() > 0) checkList += ",";
                        checkList += Integer.toHexString(i + blockCount);
                        suppliedChecks++;
                    } else {
                        if (requestList.length() > 0) requestList += ",";
                        requestList += Integer.toHexString(i + blockCount);
                    }
                }
                Socket fcpSock;
                BufferedInputStream fcpIn;
                PrintStream fcpOut;
                {
                    synchronized (fecutils.getClass()) {
                        try {
                            fcpSock = new Socket(InetAddress.getByName(frame1.frostSettings.getValue("nodeAddress")), frame1.frostSettings.getIntValue("nodePort"));
                            fcpSock.setSoTimeout(1800000);
                            fcpOut = new PrintStream(fcpSock.getOutputStream());
                            fcpIn = new BufferedInputStream(fcpSock.getInputStream());
                            String headerString = new StringBuffer().append("SegmentHeader\n").append(currentSegment.reconstruct()).append("EndMessage\n").toString();
                            String dataHeaderString = new StringBuffer().append("\0\0\0\2FECDecodeSegment\n").append("BlockList=").append(blockList).append("\n").append("CheckList=").append(checkList).append("\n").append("RequestedList=").append(requestList).append("\n").append("MetadataLength=").append(Long.toHexString(headerString.length())).append("\n").append("DataLength=").append(Long.toHexString(headerString.length() + suppliedBlocks * currentSegment.BlockSize + suppliedChecks * currentSegment.CheckBlockSize)).append("\n").append("Data\n").append(headerString).toString();
                            System.out.print(dataHeaderString);
                            fcpOut.print(dataHeaderString);
                            String[] Elements = blockList.split(",");
                            byte[] buffer;
                            for (i = 0; i < Elements.length; i++) {
                                int bytesRead;
                                System.out.println("Transferring data for chunk " + Elements[i]);
                                File chunkFile = new File(frame1.keypool + target.getName() + "-chunk-" + (Integer.parseInt(Elements[i], 16) + chunkBase + 1));
                                chunkFile.deleteOnExit();
                                FileInputStream inFile = new FileInputStream(chunkFile);
                                buffer = new byte[(int) currentSegment.BlockSize];
                                bytesRead = inFile.read(buffer);
                                if (bytesRead < buffer.length) {
                                    System.out.println("Not enough input data for chunk " + (Integer.parseInt(Elements[i], 16) + chunkBase + 1) + " - filling");
                                    for (int j = bytesRead; j < buffer.length; j++) buffer[j] = 0;
                                }
                                inFile.close();
                                fcpOut.write(buffer);
                            }
                            Elements = checkList.split(",");
                            for (i = 0; i < Elements.length; i++) {
                                int bytesRead;
                                System.out.println("Transferring data for check " + Elements[i]);
                                File checkFile = new File(frame1.keypool + target.getName() + "-check-" + (Integer.parseInt(Elements[i], 16) + checkBase + 1));
                                checkFile.deleteOnExit();
                                FileInputStream inFile = new FileInputStream(checkFile);
                                buffer = new byte[(int) currentSegment.CheckBlockSize];
                                bytesRead = inFile.read(buffer);
                                if (bytesRead < buffer.length) {
                                    System.out.println("Not enough input data for check " + (Integer.parseInt(Elements[i], 16) + chunkBase + 1) + " - filling");
                                    for (int j = bytesRead; j < buffer.length; j++) buffer[j] = 0;
                                }
                                inFile.close();
                                fcpOut.write(buffer);
                            }
                            Elements = requestList.split(",");
                            int chunkNo = 0;
                            File uploadMe = null;
                            FileOutputStream outFile = null;
                            {
                                String currentLine;
                                long BlockSize = currentSegment.BlockSize;
                                long CheckSize = currentSegment.CheckBlockSize;
                                int chunkPtr = 0;
                                int length = 0;
                                if (DEBUG) System.out.println("Expecting chunk " + Elements[chunkNo] + "\n");
                                do {
                                    int index = Integer.parseInt(Elements[chunkNo], 16);
                                    currentLine = fecutils.getLine(fcpIn).trim();
                                    if (DEBUG) System.out.println(currentLine);
                                    if (currentLine.startsWith("Length=")) {
                                        length = Integer.parseInt((currentLine.split("="))[1], 16);
                                    }
                                    if (currentLine.equals("Data")) {
                                        int currentRead;
                                        buffer = new byte[(int) length];
                                        if (uploadMe == null) {
                                            if (index >= blockCount) {
                                                uploadMe = new File(new StringBuffer().append(frame1.keypool).append(target.getName()).append("-check-").append((index + checkBase + 1)).toString());
                                            } else {
                                                uploadMe = new File(new StringBuffer().append(frame1.keypool).append(target.getName()).append("-chunk-").append((index + chunkBase + 1)).toString());
                                            }
                                            outFile = new FileOutputStream(uploadMe);
                                            System.out.println("Recovering " + uploadMe.getName());
                                        }
                                        currentRead = fcpIn.read(buffer);
                                        while (currentRead < length) {
                                            currentRead += fcpIn.read(buffer, currentRead, length - currentRead);
                                        }
                                        outFile.write(buffer);
                                        chunkPtr += currentRead;
                                        if (chunkPtr > BlockSize) System.out.println("!!!!! Unsupported length");
                                        if ((chunkPtr == BlockSize) || (chunkPtr == CheckSize)) {
                                            outFile.close();
                                            System.out.println("added to heal queue " + uploadMe.getPath());
                                            toHeal.add(uploadMe.getPath());
                                            ((WorkQueue) healingQueue.get()).add(uploadMe);
                                            uploadMe = null;
                                            chunkNo++;
                                            chunkPtr = 0;
                                        }
                                    }
                                } while (currentLine.length() > 0 && chunkNo < Elements.length);
                            }
                            fcpOut.close();
                            fcpIn.close();
                            fcpSock.close();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            System.out.println("\nexception in getfecsplit 2" + e.toString());
                        }
                    }
                }
            }
        }
        if (success) {
            ((Thread) healer.get()).start();
            FileOutputStream fileOut;
            try {
                fileOut = new FileOutputStream(target);
                if (DEBUG) System.out.println("Connecting chunks");
                long bytesWritten = 0;
                int blockCount = 0;
                long remaining;
                long thisRead;
                while (bytesWritten < splitFileSize) {
                    if (DEBUG) System.out.println("Adding chunk " + blockCount + " to " + target.getName());
                    File toRead = new File(new StringBuffer().append(frame1.keypool).append(target.getName()).append("-chunk-").append((blockCount + 1)).toString());
                    FileInputStream fileIn = new FileInputStream(toRead);
                    remaining = splitFileSize - bytesWritten;
                    thisRead = toRead.length();
                    if (remaining < thisRead) thisRead = remaining;
                    byte[] buffer = new byte[(int) thisRead];
                    fileIn.read(buffer);
                    fileOut.write(buffer);
                    bytesWritten += thisRead;
                    if (!toHeal.contains(toRead.getPath())) {
                        toRead.deleteOnExit();
                        toRead.delete();
                    }
                    blockCount++;
                }
                fileOut.close();
            } catch (IOException e) {
                if (DEBUG) System.out.println("Write Error: " + target.getPath());
            }
        } else {
            target.delete();
            if (DEBUG) System.out.println("!!!!!! Download of " + target.getName() + " failed.");
        }
        return success;
    }

    /**
     * getFile retrieves a file from Freenet. It does detect if this file is a redirect, a splitfile or
     * just a simple file. It checks the size for the file and returns false if sizes do not match.
     * Size is ignored if it is no integer value (NumberFormatException).
     * @param key The key to retrieve. All to Freenet known key formats are allowed (passed to node via FCP).
     * @param size Size of the file in bytes. Is ignored if not an integer value or -1 (splitfiles do not need this setting).
     * @param target Target path
     * @param htl request htl
     * @param doRedirect If true, getFile redirects if possible and downloads the file it was redirected to.
     * @return True if download was successful, else false.
     */
    public static boolean getFile(String key, String size, String target, String htl, boolean doRedirect) {
        int intHtl = 0;
        try {
            intHtl = Integer.parseInt(htl);
        } catch (NumberFormatException e) {
        }
        return getFile(key, size, new File(target), intHtl, doRedirect);
    }

    public static boolean getFile(String key, String size, File target, String htl, boolean doRedirect) {
        int intHtl = 0;
        try {
            intHtl = Integer.parseInt(htl);
        } catch (NumberFormatException e) {
        }
        return getFile(key, size, target, intHtl, doRedirect);
    }

    public static boolean getFile(String key, String size, File target, int htl, boolean doRedirect) {
        File tempFile = new File(target + ".tmp");
        if (DEBUG) System.out.println("Retrieving " + key + " to " + tempFile.getPath());
        if (getKey(key, size, tempFile, htl)) {
            String[] content = FileAccess.readFile(tempFile).split("\n");
            if (tempFile.length() <= 65536 && doRedirect) {
                boolean isRedirect = false;
                for (int i = 0; i < content.length; i++) if (content[i].startsWith("Redirect.Target")) isRedirect = true;
                if (isRedirect) {
                    String redirect = SettingsFun.getValue(tempFile.getPath(), "Redirect.Target");
                    if (DEBUG) System.out.println("Redirecting to " + redirect);
                    boolean success = getKey(redirect, "Unknown", tempFile, htl);
                    if (success) {
                        if (target.isFile()) target.delete();
                        tempFile.renameTo(target);
                    } else {
                        tempFile.delete();
                    }
                    return success;
                }
            }
            boolean isSplitfile = false;
            for (int i = 0; i < content.length; i++) if (content[i].startsWith("SplitFile.Size")) isSplitfile = true;
            if (isSplitfile) {
                boolean success;
                String format = SettingsFun.getValue(tempFile.getPath(), "Info.Format");
                if (format.equals("Frost/FEC")) success = getFECSplitFile(key, tempFile, htl); else success = getSplitFile(key, tempFile, htl);
                if (success) {
                    if (target.isFile()) target.delete();
                    tempFile.renameTo(target);
                } else {
                    tempFile.delete();
                }
                return success;
            }
            if (target.isFile()) target.delete();
            tempFile.renameTo(target);
            return true;
        } else {
            tempFile.delete();
            return false;
        }
    }

    public static boolean getKey(String key, String size, File target, int htl) {
        if (key.indexOf("null") != -1) return false;
        try {
            FcpConnection connection = new FcpConnection(frame1.frostSettings.getValue("nodeAddress"), frame1.frostSettings.getValue("nodePort"));
            try {
                connection.getKeyToFile(key, target.getPath(), htl);
            } catch (FcpToolsException e) {
                if (DEBUG) System.out.println("FcpToolsException " + e);
            } catch (IOException e) {
                if (DEBUG) System.out.println("IOException " + e);
            }
        } catch (FcpToolsException e) {
            if (DEBUG) System.out.println("FcpToolsException " + e);
            frame1.displayWarning(e.toString());
        } catch (UnknownHostException e) {
            if (DEBUG) System.out.println("UnknownHostException " + e);
            frame1.displayWarning(e.toString());
        } catch (IOException e) {
            if (DEBUG) System.out.println("IOException " + e);
            frame1.displayWarning(e.toString());
        }
        int intSize = -1;
        try {
            intSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            intSize = -1;
        }
        if (target.length() > 0) {
            if (intSize == -1 || target.length() == intSize || intSize >= chunkSize) {
                if (DEBUG) System.out.println("File " + target.getName() + " with key " + key + " has correct size :)");
                return true;
            }
        }
        target.delete();
        if (DEBUG) System.out.println("File " + target.getName() + " with key " + key + " not found or wrong size :(");
        return false;
    }
}
