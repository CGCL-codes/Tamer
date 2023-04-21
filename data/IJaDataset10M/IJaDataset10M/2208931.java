package fi.hip.gb.disk.client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import com.twmacinta.util.MD5;
import fi.hip.gb.disk.FileManager;
import fi.hip.gb.disk.Performance;
import fi.hip.gb.disk.conf.Config;
import fi.hip.gb.disk.fec.OptimizedDecoder;
import fi.hip.gb.disk.fec.OptimizedEncoder;
import fi.hip.gb.disk.info.FileInfo;
import fi.hip.gb.disk.info.User;
import fi.hip.gb.disk.info.UserBean;
import fi.hip.gb.disk.transport.Transport;
import fi.hip.gb.disk.transport.TransportCoordinator;
import fi.hip.gb.disk.transport.TransportFactory;

/**
 * Commandline client for testing GB-DISK features. Contains operation
 * to run the Coordinator locally, manage users, as well as to print other debug
 * information. Some test cases can also be run throught this class.
 *  
 * @author Juho Karppinen
 */
public class TestDemo {

    static Log log;

    private Performance perf;

    public static void main(String[] args) throws Exception {
        new TestDemo(args);
    }

    /**
     * Run the client.
     * @param args
     * @throws Exception
     */
    public TestDemo(String[] args) throws Exception {
        URL logConf = getClass().getClassLoader().getResource("src/etc/log4j.xml");
        if (logConf != null) {
            System.out.println("Loading log configs from " + logConf.toString());
            DOMConfigurator.configure(logConf);
        } else {
            System.out.println("Log4j configs not found!");
        }
        log = LogFactory.getLog(TestDemo.class);
        String op = "help";
        String fileName = "test.file";
        for (int i = 0; i < args.length; i++) {
            if (i == 0) {
                op = args[i].toLowerCase();
            } else if (i == 1) {
                fileName = args[i];
            }
        }
        new Config().initialise("gb-disk.conf", "");
        this.perf = new Performance();
        if ("hash".equals(op)) {
            System.out.println("Hash: " + MD5.asHex(MD5.getHash(new File(fileName))));
            if (MD5.hashesEqual(MD5.getHash(new File(fileName)), MD5.getHash(new File(fileName)))) {
                System.out.println("files equal");
            } else {
                System.out.println("files different");
            }
        } else if ("se".equals(op)) {
            try {
                Config.setFileInfoProperties(null);
                while (true) {
                    Thread.sleep(100);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(0);
            }
        } else if ("fe".equals(op)) {
            try {
                while (true) {
                    Thread.sleep(100);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(0);
            }
        } else if ("group".equals(op)) {
            try {
                System.out.println("Storage elements:");
                System.out.println(Config.getScheduler());
                System.out.println("\n\nInfo system:");
                System.out.println(Config.getFileInfosys() + "\n");
                System.out.println("\n\nGroups members:");
                System.out.println(Config.getGroupInfosys() + "\n");
                while (true) {
                    Thread.sleep(100);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(0);
            }
        } else if ("list".equals(op)) {
            try {
                FileInfo[] files = Config.getFileInfosys().listFiles("");
                for (int i = 0; i < files.length; i++) {
                    System.out.println(files[i].getFilePath() + " size of " + files[i].getFileSize() + " bytes");
                }
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(0);
            }
        } else if ("search".equals(op)) {
            try {
                FileInfo[] files = Config.getFileInfosys().advancedFind("", fileName);
                System.out.println("Found " + files.length + " files for query " + fileName);
                for (int i = 0; i < files.length; i++) {
                    System.out.println(files[i].getFilePath() + " size of " + files[i].getFileSize() + " bytes");
                }
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(0);
            }
        } else if ("put".equals(op)) {
            FileManager fm = new FileManager();
            File sourceFile = new File(fileName);
            if (sourceFile.exists() == false) {
                sourceFile = new File(System.getProperty("user.dir") + "/" + fileName);
            }
            fm.put(sourceFile.getName(), sourceFile);
            fm.run();
        } else if ("get".equals(op)) {
            FileManager fm = new FileManager();
            if (fileName.startsWith("/") == false) {
                fileName = System.getProperty("user.dir") + "/" + fileName;
            }
            File targetFile = new File(fileName);
            fm.get(targetFile.getName(), targetFile);
            fm.run();
        } else if ("delete".equals(op)) {
            FileManager fm = new FileManager();
            fm.delete(fileName);
            fm.run();
        } else if ("encode".equals(op)) {
            File sourceFile = new File(fileName);
            FileInfo fileInfo = Config.getFileInfosys().createNewFile(fileName, Config.getK(), Config.getN(), Config.getPacketSize(), sourceFile.length());
            OptimizedEncoder encoder = new OptimizedEncoder();
            encoder.simpleEncode(sourceFile, new File(Config.getTempDir()), fileInfo);
            fileInfo.commit();
        } else if ("decode".equals(op)) {
            OptimizedDecoder decoder = new OptimizedDecoder();
            FileInfo fileInfo = Config.getFileInfosys().findFile(fileName);
            File targetFile = new File(fileName);
            int[] stripeOrder = new int[fileInfo.getN()];
            for (int i = 0; i < fileInfo.getN(); i++) {
                stripeOrder[i] = fileInfo.getStripeInfo(i).getOrder();
            }
            decoder.simpleDecode(new File(Config.getTempDir()), targetFile, fileInfo, stripeOrder);
        } else if ("housework".equals(op)) {
            Logger.getRootLogger().removeAppender("CONSOLE");
            FileInfo[] files = Config.getFileInfosys().listFiles("");
            for (int i = 0; i < files.length; i++) {
                checkRecursively(files[i]);
            }
            System.out.println("done");
        } else if ("adduser".equals(op)) {
            User user = new UserBean();
            user.setName(fileName);
            user.setCredentials(args[2]);
            user.addMembership("user");
            for (int i = 3; i < args.length; i++) {
                user.addMembership(args[i]);
            }
            Config.getSecurityInfosys().addUser(user);
            System.out.println("Users added");
        } else if ("rmuser".equals(op)) {
            User[] users = Config.getSecurityInfosys().listUsers();
            for (int i = 0; i < users.length; i++) {
                if (users[i].getName().equals(args[1])) {
                    System.out.println("removing user " + args[1]);
                    Config.getSecurityInfosys().removeUser(users[i]);
                }
            }
            System.out.println("Users removed");
        } else if ("listusers".equals(op)) {
            User[] users = Config.getSecurityInfosys().listUsers();
            for (int i = 0; i < users.length; i++) {
                System.out.println("user " + users[i]);
            }
        } else if ("addgroup".equals(op)) {
            User[] users = Config.getSecurityInfosys().listUsers();
            for (int i = 0; i < users.length; i++) {
                if (users[i].getName().equals(args[1])) {
                    for (int g = 2; g < args.length; g++) {
                        System.out.println("user " + args[1] + " add to member of " + args[g]);
                        users[i].addMembership(args[g]);
                    }
                }
            }
            System.out.println("Group added");
        } else if ("passwd".equals(op)) {
            User[] users = Config.getSecurityInfosys().listUsers();
            for (int i = 0; i < users.length; i++) {
                if (users[i].getName().equals(args[1])) {
                    System.out.println("passwd for user " + args[1]);
                    users[i].setCredentials(args[2]);
                }
            }
            System.out.println("Password set");
        } else if ("test".equals(op)) {
            SimpleDateFormat dFormat = new SimpleDateFormat("EE dd.MMM - HH:mm:ss");
            log.fatal("------------------------------------------------------\n");
            log.fatal("starting new set at " + dFormat.format(new Date()) + "\n");
            int runs = 1;
            int threads = Integer.parseInt(args[2]);
            StringTokenizer tokenizer = new StringTokenizer(fileName, ";", false);
            String[] destHostList = new String[tokenizer.countTokens()];
            for (int i = 0; tokenizer.hasMoreTokens(); i++) {
                destHostList[i] = tokenizer.nextToken();
            }
            for (int i = 0; i < threads; i++) {
                log.fatal("starting new THREAD " + dFormat.format(new Date()) + "\n");
                new ClientTestCase(runs, destHostList, threads).start();
            }
        } else {
            System.out.println("Unknown parameter: " + op);
            System.out.println("Usage: java JAVA_OPTS fi.hip.gb.disk.perf.TestDemo operation filename");
            System.out.println("       Available operations:");
            System.out.println("             help    - prints this message");
            System.out.println("             put    - adds a file into distributed storage");
            System.out.println("             get    - gets a file from distributed storage");
            System.out.println("             delete - delete the file from distributed storage");
            System.out.println("             list  - list all files in info system");
            System.out.println("             search - search partial file names");
            System.out.println("             fe     - start as front-end");
            System.out.println("             se     - start as storage-element");
            System.out.println("             group  - print list of available storage servers");
            System.out.println("             housework - check all files in the storage");
            System.out.println("             adduser - username password group1 group2...");
            System.out.println("             rmuser - username");
            System.out.println("             passwd - username newpassword");
            System.out.println("             addgroup - username group1 group2");
            System.out.println("             group  - print list of available storage servers");
            System.out.println("             encode - encode the file locally");
            System.out.println("             decode - decode the file locally");
            System.out.println("       Available JAVA_OPTS:");
            System.out.println("             -Dbind.address  - sets the ip address to use for communication");
        }
        perf.done();
    }

    private void checkRecursively(FileInfo info) {
        if (info.isDirectory()) {
            FileInfo[] files = Config.getFileInfosys().listFiles(info.getFilePath());
            for (int i = 0; i < files.length; i++) {
                checkRecursively(files[i]);
            }
        } else {
            try {
                System.out.print("Checking " + info.getFilePath());
                TransportCoordinator tc = new TransportCoordinator(this.perf, info.getN());
                tc.checkStripes(info);
                if (tc.getFinishedTransfers().size() != info.getN()) {
                    System.out.println(" : " + (info.getN() - tc.getFinishedTransfers().size()) + " stripes missing out of " + info.getN());
                } else {
                    System.out.println(" : ok");
                }
            } catch (IOException ioe) {
                System.out.println(" : failed " + ioe.getMessage());
            }
        }
    }

    private class ClientTestCase extends Thread {

        private int runs;

        private String[] destinations;

        private int testID;

        public ClientTestCase(int runs, String[] destHostList, int testID) {
            this.runs = runs;
            this.destinations = destHostList;
            this.testID = testID;
        }

        public void run() {
            try {
                Random rangen = new Random();
                for (int i = 0; i < runs; i++) {
                    File file = new File("test." + rangen.nextInt(1000000));
                    Transport client = TransportFactory.createTransport(this.destinations[i % this.destinations.length]);
                    int size = ((rangen.nextInt(5) * 20) + 20) + testID;
                    FileMaker.makeMegaFile(size, file);
                    Performance perf = new Performance();
                    perf.set(Performance.FILE_SIZE, (long) size);
                    try {
                        perf.set(Performance.OPERATION, "PUT");
                        client.put(file.getName(), file);
                        perf.throughput(Performance.THROUGHPUT);
                        perf.done();
                        perf.set(Performance.OPERATION, "GET");
                        client.get(file.getName(), file);
                        perf.throughput(Performance.THROUGHPUT);
                        perf.done();
                        client.delete(file.getName());
                    } catch (Exception e) {
                        log.fatal("TestDemo Client Exception: ", e);
                    } finally {
                        file.delete();
                    }
                }
            } catch (Exception e) {
                log.fatal("TestDemo FAILED: ", e);
            }
        }
    }
}
