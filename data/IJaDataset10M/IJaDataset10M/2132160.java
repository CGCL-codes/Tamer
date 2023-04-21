package tranchesocketstresstestclient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.tranche.TrancheServer;
import org.tranche.add.AddFileTool;
import org.tranche.add.AddFileToolReport;
import org.tranche.get.GetFileTool;
import org.tranche.get.GetFileToolReport;
import org.tranche.hash.Base64;
import org.tranche.hash.span.HashSpan;
import org.tranche.logs.SimpleLog;
import org.tranche.network.ConnectionUtil;
import org.tranche.network.NetworkUtil;
import org.tranche.network.StatusTableRow;
import org.tranche.server.PropagationExceptionWrapper;
import org.tranche.server.PropagationReturnWrapper;
import org.tranche.users.UserZipFile;
import org.tranche.util.DebugUtil;
import org.tranche.util.IOUtil;
import org.tranche.util.PersistentFileUtil;
import org.tranche.util.RandomUtil;
import org.tranche.util.TestUtil;
import org.tranche.util.Text;
import stress.StressTestConfig;
import stress.client.Configuration;
import stress.util.StressTestUtil;

/**
 *
 * @author Bryan Smith - bryanesmith@gmail.com
 */
public class Main {

    public static final boolean isLogging = false;

    /**
     *
     */
    private static final boolean isDebug = false;

    private static final Random r = new Random();

    public static void main(String[] args) throws Exception {
        StressTestConfig.load();
        Configuration.loadConfigurationForClientInSeparateJVM(args[0]);
        File myTempDir = new File("/tmp/client_jars_temp/" + System.currentTimeMillis() + Math.abs(r.nextLong()));
        myTempDir.mkdirs();
        System.setProperty("java.io.tmpdir", myTempDir.getAbsolutePath());
        printTracer("System.getProperty(\"java.io.tmpdir\"): " + System.getProperty("java.io.tmpdir"));
        int result = -1;
        TestUtil.setTestingManualNetworkStatusTable(false);
        TestUtil.setTesting(false);
        String host = null;
        try {
            long time = -System.currentTimeMillis();
            if (args.length < 7) {
                System.out.println("Must provide 7 arguments, you provided " + args.length + " arguments.");
                System.out.println("Your " + args.length + " arguments:");
                for (String a : args) {
                    System.out.println("  - " + a);
                }
                System.out.println("Arguments: numFiles<Integer> maxFileSize<Long> url<String> minProjectSize<Long> maxProjectSize<Long>");
                System.exit(1);
            }
            Thread waitThread = new Thread("Limited wait thread") {

                @Override
                public void run() {
                    NetworkUtil.setDebug(false);
                    DebugUtil.setDebug(false);
                    NetworkUtil.waitForStartup();
                }
            };
            waitThread.setDaemon(true);
            waitThread.setPriority(Thread.MIN_PRIORITY);
            waitThread.start();
            waitThread.join(1000 * 60);
            System.out.println("Finished waiting for startup... (server IP: " + Configuration.getServerIP() + ")");
            System.out.println("Waiting for network status table to have at least one entry...");
            boolean statusTableWait = NetworkUtil.getStatus().getRows().isEmpty();
            final long startStatusTableWait = System.currentTimeMillis();
            int statusTableTickCount = 0;
            int statusTableTickSize = 10 * 1000;
            final long statusTableTimeout = 2 * 60 * 1000;
            while (statusTableWait) {
                Thread.sleep(statusTableTickSize);
                statusTableTickCount++;
                if (statusTableTickCount % 3 == 0) {
                    System.out.println("    ... still waiting; has been " + Text.getPrettyEllapsedTimeString(statusTableTickCount * statusTableTickSize));
                }
                statusTableWait = NetworkUtil.getStatus().getRows().isEmpty();
                if (statusTableWait && statusTableTickCount * statusTableTickSize > statusTableTimeout) {
                    System.out.println("Client timed out. Cannot wait for status table any longer, waited: " + Text.getPrettyEllapsedTimeString(statusTableTickCount * statusTableTickSize));
                    System.exit(2);
                }
            }
            System.out.println("Waited " + Text.getPrettyEllapsedTimeString(System.currentTimeMillis() - startStatusTableWait) + " for status table to have at least one server.");
            System.out.println("Number of rows in status table: " + NetworkUtil.getStatus().size());
            for (StatusTableRow row : NetworkUtil.getStatus().getRows()) {
                System.out.println("    - " + row.getURL() + " (online: " + row.isOnline() + ")");
            }
            System.out.println();
            System.out.println("Number of startup URLs: " + NetworkUtil.getStartupServerURLs().size());
            for (String startupURL : NetworkUtil.getStartupServerURLs()) {
                System.out.println("    - " + startupURL);
            }
            try {
                final int numFiles = Integer.parseInt(args[1]);
                final long maxFileSize = Long.parseLong(args[2]);
                String url = args[3];
                final long minProjectSize = Long.parseLong(args[4]);
                final long maxProjectSize = Long.parseLong(args[5]);
                final boolean isUseBatch = Boolean.parseBoolean(args[6]);
                host = IOUtil.parseHost(url);
                System.out.println("Connecting to: " + url);
                TrancheServer ts = ConnectionUtil.connectURL(url, true);
                if (ts == null) {
                    throw new NullPointerException("Could not connect to host: " + host);
                }
                String[] hostArr = { host };
                PropagationReturnWrapper prw = ts.getNonces(hostArr, 1);
                if (prw.isVoid()) {
                    throw new Exception("Cannot get nonce from host: " + host);
                } else {
                    Object o = prw.getReturnValueObject();
                    if (o != null) {
                        StatusTableRow row = NetworkUtil.getStatus().getRow(host);
                        System.out.println("HashSpan objs found for " + host + ": " + row.getHashSpans().size());
                        for (HashSpan hs : row.getHashSpans()) {
                            System.err.println("    - Start: " + hs.getFirst());
                            System.err.println("       Last: " + hs.getLast());
                        }
                        System.out.println("Target HashSpan objs found for " + host + ": " + row.getTargetHashSpans().size());
                        for (HashSpan hs : row.getTargetHashSpans()) {
                            System.err.println("    - Start: " + hs.getFirst());
                            System.err.println("       Last: " + hs.getLast());
                        }
                    } else {
                        throw new Exception("Cannot get nonce from host (said not void, but was null): " + host);
                    }
                }
                System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                System.out.println("STARTING CLIENT AT: " + Text.getFormattedDate(System.currentTimeMillis()));
                System.out.println("  - url: " + url + " (Host: " + host + ")");
                System.out.println("  - " + numFiles + " with maximum size of " + Text.getFormattedBytes(maxFileSize));
                System.out.println("  - Projects between " + Text.getFormattedBytes(minProjectSize) + " and " + Text.getFormattedBytes(maxProjectSize));
                System.out.println("  - Using batch?: " + isUseBatch);
                System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                System.out.flush();
                System.out.println("Client waiting for server to become readable/writable...");
                boolean wait = !NetworkUtil.getStatus().getRow(host).isWritable() || !NetworkUtil.getStatus().getRow(host).isReadable();
                final long startWait = System.currentTimeMillis();
                int tickCount = 0;
                int tickSize = 10 * 1000;
                final long timeout = 30 * 60 * 1000;
                while (wait) {
                    Thread.sleep(tickSize);
                    tickCount++;
                    if (tickCount % 3 == 0) {
                        System.out.println("    ... still waiting; has been " + Text.getPrettyEllapsedTimeString(tickCount * tickSize) + " [writable?: " + NetworkUtil.getStatus().getRow(host).isWritable() + "; readable?: " + NetworkUtil.getStatus().getRow(host).isReadable() + "]");
                    }
                    wait = !NetworkUtil.getStatus().getRow(host).isWritable() || !NetworkUtil.getStatus().getRow(host).isReadable();
                    if (wait && tickCount * tickSize > timeout) {
                        System.out.println("Client timed out. Cannot wait for server to become readable/writable any longer, waited: " + Text.getPrettyEllapsedTimeString(tickCount * tickSize));
                        System.exit(2);
                    }
                }
                System.out.println("Waited " + Text.getPrettyEllapsedTimeString(System.currentTimeMillis() - startWait) + " for server to become readable/writable.");
                System.out.println("DEBUG> Is writable?: " + NetworkUtil.getStatus().getRow(host).isWritable());
                System.out.println("DEBUG> Is readable?: " + NetworkUtil.getStatus().getRow(host).isReadable());
                if (isLogging) {
                    File rtsLog = new File(PersistentFileUtil.getPersistentDirectory(), "RTS-" + System.currentTimeMillis() + ".log");
                    rtsLog.createNewFile();
                    SimpleLog.setLog(SimpleLog.RTS_LOG, rtsLog);
                    System.out.println("Set RTS log to: " + rtsLog.getAbsolutePath());
                    File gftLog = new File(PersistentFileUtil.getPersistentDirectory(), "GFT-" + System.currentTimeMillis() + ".log");
                    gftLog.createNewFile();
                    SimpleLog.setLog(SimpleLog.GFT_LOG, gftLog);
                    System.out.println("Set GFT log to: " + gftLog.getAbsolutePath());
                }
                result = runTest(numFiles, maxFileSize, url, minProjectSize, maxProjectSize, isUseBatch);
                if (isLogging) {
                    SimpleLog.setEnabled(false);
                    System.out.println("Flushed log files.");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            time += System.currentTimeMillis();
            printTracer("Tool took " + Text.getPrettyEllapsedTimeString(time));
        } catch (Exception ex) {
            System.err.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            ex.printStackTrace(System.err);
            System.exit(1);
        } finally {
            if (host != null) {
                ConnectionUtil.unlockConnection(host);
            }
            IOUtil.recursiveDeleteWithWarning(myTempDir);
        }
        System.exit(result);
    }

    public static int runTest(int numFiles, long maxFileSize, String url, long minProjectSize, long maxProjectSize, boolean isUseBatch) {
        File saveTo = null;
        File project = null;
        boolean isError = false;
        long totalBytesToUpload = numFiles * maxFileSize;
        long totalBytesUploaded = 0;
        printTracer("Total size of client upload: " + Text.getFormattedBytes(totalBytesToUpload));
        long projectCount = 0;
        final String userName = RandomUtil.getString(RandomUtil.getInt(10) + 5);
        final String userPass = String.valueOf(RandomUtil.getLong());
        UserZipFile signedUser = null;
        int test = 0;
        try {
            signedUser = StressTestUtil.getNewSignedUserZipFile(userName, userPass);
            signedUser.setPassphrase(userPass);
            System.out.println();
            System.out.println("Stress test user:");
            System.out.println("    - Valid: " + StressTestUtil.getUserZipFile().isValid());
            System.out.println("        * Not valid before: " + StressTestUtil.getCertificate().getNotBefore());
            System.out.println("    - Expired: " + StressTestUtil.getUserZipFile().isExpired());
            System.out.println("        * Not valid after: " + StressTestUtil.getCertificate().getNotAfter());
            System.out.println();
            System.out.println("Signed user:");
            System.out.println("    - Valid: " + signedUser.isValid());
            System.out.println("        * Not valid before: " + signedUser.getCertificate().getNotBefore());
            System.out.println("    - Expired: " + signedUser.isExpired());
            System.out.println("        * Not valid after: " + signedUser.getCertificate().getNotAfter());
            System.out.println();
            while (totalBytesUploaded < totalBytesToUpload) {
                test++;
                long nextProjectSize = -1;
                if (minProjectSize == maxProjectSize) {
                    nextProjectSize = minProjectSize;
                } else {
                    int differenceMinMax = Integer.MAX_VALUE;
                    if (maxProjectSize - minProjectSize < differenceMinMax) {
                        differenceMinMax = (int) (maxProjectSize - minProjectSize);
                    }
                    boolean isStartFromMin = r.nextBoolean();
                    if (isStartFromMin) {
                        nextProjectSize = minProjectSize + r.nextInt(differenceMinMax);
                    } else {
                        nextProjectSize = maxProjectSize - r.nextInt(differenceMinMax);
                    }
                    if (nextProjectSize + totalBytesUploaded >= totalBytesToUpload) {
                        nextProjectSize = totalBytesToUpload - totalBytesUploaded;
                    }
                }
                totalBytesUploaded += nextProjectSize;
                projectCount++;
                int thisNumFiles = Math.round((float) nextProjectSize / (float) maxFileSize) + 1;
                printTracer("Next project size: " + Text.getFormattedBytes(nextProjectSize) + " (" + thisNumFiles + " num file(s) with max size of " + maxFileSize + ")");
                project = StressTestUtil.createTestProject(thisNumFiles, maxFileSize);
                String passphrase = null;
                if (r.nextInt(4) == 0) {
                    byte[] passphraseBytes = new byte[20];
                    r.nextBytes(passphraseBytes);
                    passphrase = Base64.encodeBytes(passphraseBytes);
                }
                final Set<String> serverHostNames = new HashSet();
                serverHostNames.add(IOUtil.parseHost(url));
                AddFileTool aft = new AddFileTool();
                StressTestUtil.getUserZipFile().isExpired();
                aft.setUserCertificate(signedUser.getCertificate());
                aft.setUserPrivateKey(signedUser.getPrivateKey());
                aft.setServersToUse(serverHostNames);
                AddFileTool.setDebug(isDebug);
                aft.setCompress(false);
                if (passphrase != null) {
                    System.out.println("Setting passphrase: " + passphrase);
                    aft.setPassphrase(passphrase);
                }
                aft.setFile(project);
                System.out.println("Starting upload #" + test + ": " + Text.getFormattedDate(System.currentTimeMillis()));
                AddFileToolReport uploadReport = aft.execute();
                if (uploadReport.isFailed()) {
                    System.err.println("Upload failed with " + uploadReport.getFailureExceptions().size() + " error(s):");
                    for (PropagationExceptionWrapper pew : uploadReport.getFailureExceptions()) {
                        System.err.println("    - " + pew.exception.getClass().getSimpleName() + " <" + pew.host + ">: " + pew.exception.getMessage());
                        pew.exception.printStackTrace(System.err);
                    }
                    return 1;
                }
                GetFileTool gft = new GetFileTool();
                gft.setServersToUse(serverHostNames);
                gft.setHash(uploadReport.getHash());
                gft.setContinueOnFailure(false);
                if (passphrase != null) {
                    gft.setPassphrase(passphrase);
                }
                GetFileTool.setDebug(isDebug);
                gft.setBatch(isUseBatch);
                saveTo = new File(project.getParentFile(), "~" + project.getName());
                saveTo.mkdirs();
                gft.setSaveFile(saveTo);
                System.out.println("Starting download #" + test + ": " + Text.getFormattedDate(System.currentTimeMillis()));
                GetFileToolReport downloadReport = gft.getDirectory();
                IOUtil.recursiveDeleteWithWarning(saveTo);
                printTracer("Finished upload/download(isError=" + isError + ")");
                if (downloadReport.isFailed()) {
                    System.err.println("Download failed with " + downloadReport.getFailureExceptions().size() + " error(s):");
                    for (PropagationExceptionWrapper pew : downloadReport.getFailureExceptions()) {
                        System.err.println("    - " + pew.exception.getClass().getSimpleName() + " <" + pew.host + ">: " + pew.exception.getMessage());
                        pew.exception.printStackTrace(System.err);
                    }
                    return 1;
                }
            }
            printTracer("Client uploaded total of " + projectCount + " project(s) totaling size " + Text.getFormattedBytes(totalBytesUploaded));
        } catch (Exception ex) {
            System.out.println("=-=-=-= CLIENT THREAD FAILED AT " + Text.getFormattedDate(System.currentTimeMillis()) + ", finished " + projectCount + " projects =-=-=-=");
            ex.printStackTrace();
            return 1;
        } finally {
            if (project != null) {
                IOUtil.recursiveDeleteWithWarning(project);
            }
            if (saveTo != null) {
                IOUtil.recursiveDeleteWithWarning(saveTo);
            }
            try {
                IOUtil.safeDelete(signedUser.getFile());
            } catch (Exception e) {
            }
        }
        return 0;
    }

    private static void printTracer(String msg) {
        if (isDebug) {
            System.out.println("TRANCHE_SOCKET_STRESS_TEST_CLIENT.MAIN> " + msg);
        }
    }
}
