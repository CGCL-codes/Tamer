package fi.hip.gb.gridbank.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import fi.hip.gb.gridbank.GridBankException;
import fi.hip.gb.gridbank.cheques.GridCheque;
import fi.hip.gb.gridbank.server.ClientHandler;
import fi.hip.gb.utils.MainUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class GridBankClient {

    static Logger logger = Logger.getLogger(GridBankClient.class.getName());

    /** verbose output */
    private static boolean verbose = false;

    /** output stream */
    private static PrintStream out = System.out;

    private static Properties cProps = new Properties();

    private static String cmd;

    public static void main(String[] args) throws GridBankException, IOException {
        checkArguments(args);
        loadProperties(args);
        cmd = args[1];
        GBClientConnection cc = null;
        CommandHandler ch = null;
        if (cmd.equals("LIST-ACCOUNTS")) {
            printV("Listing accounts...");
            int port = MainUtils.getPort(args[args.length - 1]);
            if (port == 0) {
                port = new Integer(cProps.getProperty("ServerPort"));
            }
            printV("Using port: " + port);
            cc = new GBClientConnectionTLS(MainUtils.getHost(args[args.length - 1]), port, ClientProps.getProps());
            ch = new CommandHandler(cc, out);
            ch.listAccounts();
        } else if (cmd.equals("LIST-CHEQUES")) {
            printV("Listing cheques...");
            int port = MainUtils.getPort(args[args.length - 1]);
            if (port == 0) {
                port = new Integer(cProps.getProperty("ServerPort"));
            }
            printV("Using port: " + port);
            cc = new GBClientConnectionTLS(MainUtils.getHost(args[args.length - 1]), port, ClientProps.getProps());
            ch = new CommandHandler(cc, out);
            ch.listCheques();
        } else if (cmd.equals("CREATE-CHEQUE")) {
            printV("Creating cheque...");
            int port = MainUtils.getPort(args[args.length - 1]);
            String file = null;
            int account = -1;
            double value = -1;
            if (port == 0) {
                port = new Integer(cProps.getProperty("ServerPort"));
            }
            printV("Using port: " + port);
            if (MainUtils.argumentExists("-acc", args) && MainUtils.argumentExists("-value", args)) {
                try {
                    account = new Integer(MainUtils.getArgument("-acc", args));
                    value = new Double(MainUtils.getArgument("-value", args));
                } catch (Exception e) {
                    print(e.getMessage());
                    System.exit(0);
                }
            } else {
                print("Not enough arguments to create a cheque.");
            }
            if (MainUtils.argumentExists("-file", args)) {
                file = MainUtils.getArgument("-file", args);
            }
            cc = new GBClientConnectionTLS(MainUtils.getHost(args[args.length - 1]), port, ClientProps.getProps());
            ch = new CommandHandler(cc, out);
            GridCheque gc = null;
            try {
                gc = ch.createCheque(account, value, file);
                printV(gc.getPEMEncoded());
                printV(ClientHandler.listCheque(gc, null));
            } catch (Exception e) {
                print("\n" + e.getMessage());
            }
        } else if (cmd.equals("USE-CHEQUE")) {
            printV("Using cheque from file...");
            String file = null;
            if (MainUtils.argumentExists("-file", args)) {
                file = MainUtils.getArgument("-file", args);
            }
            if (file == null) {
                print("No file given or filename not valid.");
                return;
            }
            printV("File: " + file);
            String serviceURL = MainUtils.getHost(args[args.length - 1]);
            int servicePort = MainUtils.getPort(args[args.length - 1]);
            if (servicePort == 0) {
                servicePort = new Integer(cProps.getProperty("ServiceProviderPort"));
            }
            ch = new CommandHandler(null, out);
            SPConnection spc = new SPConnectionTLS(serviceURL, servicePort, ClientProps.getProps());
            String result = null;
            try {
                result = ch.useCheque(file, spc);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            print(result);
        } else if (cmd.equals("CREATE-AND-USE")) {
            printV("Creating cheque and using it...");
            String serviceURL = MainUtils.getHost(args[args.length - 1]);
            int servicePort = MainUtils.getPort(args[args.length - 1]);
            String bankURL = MainUtils.getHost(args[args.length - 2]);
            int bankPort = MainUtils.getPort(args[args.length - 2]);
            if (servicePort == 0) {
                servicePort = new Integer(cProps.getProperty("ServiceProviderPort"));
            }
            if (bankPort == 0) {
                bankPort = new Integer(cProps.getProperty("ServerPort"));
            }
            printV("Bank: " + bankURL + ":" + bankPort);
            printV("Service: " + serviceURL + ":" + servicePort);
            int account = -1;
            double value = -1;
            if (MainUtils.argumentExists("-acc", args)) {
                account = new Integer(MainUtils.getArgument("-acc", args));
            } else {
                print("No account specified.");
                return;
            }
            printV("Account: " + account);
            if (MainUtils.argumentExists("-value", args)) {
                value = new Double(MainUtils.getArgument("-value", args));
            } else {
                print("No value specified.");
                return;
            }
            SPConnection spc = new SPConnectionTLS(serviceURL, servicePort, ClientProps.getProps());
            cc = new GBClientConnectionTLS(bankURL, bankPort, ClientProps.getProps());
            ch = new CommandHandler(cc, out);
            String result = null;
            try {
                result = ch.createAndUse(account, value, spc);
            } catch (Exception e) {
                e.printStackTrace();
            }
            print(result);
        } else {
            print("\nUnknown command: " + cmd);
            printUsage();
            System.exit(0);
        }
    }

    /**
	 * 
	 * 
	 * @param args
	 */
    private static void checkArguments(String[] args) {
        if (args.length < 3 || MainUtils.argumentExists("-h", args) || MainUtils.argumentExists("--help", args)) {
            printUsage();
            System.exit(0);
        }
        if (MainUtils.argumentExists("-v", args)) {
            verbose = true;
        }
        printV("Arguments.length: " + args.length);
        for (String str : args) {
            printV("'" + str.trim() + "'");
        }
    }

    /**
	 * 
	 * @param args
	 */
    public static void loadProperties(String[] args) {
        printV("Loading properties...");
        try {
            cProps.load(new FileInputStream(new File(args[0])));
        } catch (FileNotFoundException e) {
            print("Properties file '" + args[0] + "' could not be found.\n\n");
            printUsage();
            System.exit(0);
        } catch (IOException e) {
            print("Properties file '" + args[0] + "' could not be accessed.\n\n");
            printUsage();
            System.exit(0);
        }
        PropertyConfigurator.configure(cProps.getProperty("log4j.configFile").trim());
        printV(cProps.toString());
        printV("Done.");
        printV("Initializing Crypto...");
        try {
            ClientProps.setProps(cProps);
        } catch (GridBankException e) {
            e.printStackTrace();
            System.exit(0);
        }
        printV("Done.");
    }

    /**
	 * 
	 *
	 */
    private static void printUsage() {
        String usage = "\n" + "********************************************************************************\n" + "\n" + "Usage:\n" + "java GridBankClient properties.file COMMAND [options] [GridBank-URL[:port]]\n" + "[SP-URL[:port]]\n" + "\n" + "********************************************************************************\n" + "\n" + "Options:\n" + "-v                          Verbose output\n" + "-h, --help                  Prints this message.\n" + "-acc #                      Account number\n" + "-value #                    Value of the cheque\n" + "-file <file.name>           File where to store the cheque of from which to\n" + "                            take it\n" + "\n" + "********************************************************************************\n" + "\n" + "Commands:\n" + "\n" + "LIST-ACCOUNTS    Lists users Accounts (and their balance) in the GridBank.\n" + "LIST-CHEQUES     Lists users OpenCheques in the GridBank.\n" + "CREATE-CHEQUE    Creates an open cheque and stores it to the given file.\n" + "USE-CHEQUE       Uses the cheque from the file specified.\n" + "CREATE-AND-USE   Creates and uses the cheque without storing it in between.\n" + "\n" + "********************************************************************************\n" + "\n" + "Examples: \n" + "\n" + "Lists user's accounts in the GridBank (uses default port defined in the \n" + "properties.file):\n" + "\n" + "java GridBankClient clientProperties.file LIST-ACCOUNTS GridBank.host\n" + "\n" + "\n" + "Lists user's cheques in the GridBank(Bank uses port 1234):\n" + "\n" + "java GridBankClient clientProperties.file LIST-CHEQUES GridBank.host:1234\n" + "\n" + "\n" + "Creates a cheque from account number 1234 with value 345 and stores\n" + "it to the cheque.file:\n" + "\n" + "java GridBankClient clientProperties.file CREATE-CHEQUE -acc 1234 -value 345 \n" + "-file cheque.file GridBank.host\n" + "\n" + "\n" + "Uses the cheque from file cheque.file with the service.provider.host:\n" + "\n" + "java GridBankClient clientProperties.file USE-CHEQUE -file cheque.file \n" + "service.provider.host:9988\n" + "\n" + "\n" + "Creates a cheque from account number 1234 with value 345 and uses it with \n" + "thw service.provider.host\n" + "\n" + "java GridBankClient clientProperties.file CREATE-AND-USE -acc 1234 -value 345\n" + "GridBank.host service.provider.host\n" + "\n" + "\n" + "********************************************************************************\n";
        System.out.println(usage);
    }

    public static void print(String str) {
        out.println(str);
        logger.info(str);
    }

    public static void printV(String str) {
        if (verbose) {
            out.println(str);
            logger.debug(str);
        }
    }
}
