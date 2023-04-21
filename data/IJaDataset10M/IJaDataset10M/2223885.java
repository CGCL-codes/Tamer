package com.mindquarry.management.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;

public class UserManagementClient {

    private static final String ADMIN_LOGIN = "admin";

    private static final String ADMIN_PWD = "admin";

    private static final String O_REPO = "r";

    private static final String O_DEL = "d";

    private static Log log;

    /**
	 * The options object for the command line applications.
	 */
    private static final Options options;

    static {
        Option repo = new Option(O_REPO, "repository", true, "The endpoint where the repository is available.");
        repo.setRequired(true);
        Option del = new Option(O_DEL, "delete", false, "Delete a user.");
        del.setOptionalArg(true);
        options = new Options();
        options.addOption(repo);
        options.addOption(del);
    }

    public static void main(String[] args) {
        log = LogFactory.getLog(UserManagementClient.class);
        log.info("Starting user management client...");
        CommandLine line = null;
        CommandLineParser parser = new GnuParser();
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            log.error("Parsing of command line failed.");
            printUsage();
            return;
        }
        System.out.print("Please enter your login ID: ");
        String user = readString();
        user = user.trim();
        System.out.print("Please enter your new password: ");
        String password = readString();
        password = password.trim();
        password = new String(DigestUtils.md5(password));
        password = new String(Base64.encodeBase64(password.getBytes()));
        UserManagementClient manager = new UserManagementClient();
        try {
            if (line.hasOption(O_DEL)) {
                manager.deleteUser(line.getOptionValue(O_REPO), ADMIN_LOGIN, ADMIN_PWD, user, password);
            } else {
                manager.changePwd(line.getOptionValue(O_REPO), ADMIN_LOGIN, ADMIN_PWD, user, password);
            }
        } catch (Exception e) {
            log.error("Error while applying password changes.", e);
        }
        log.info("User management client finished successfully.");
    }

    private static String readString() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String user = null;
        try {
            user = br.readLine();
        } catch (IOException e) {
            log.error("IO error trying to read from command line!", e);
            System.exit(1);
        }
        return user;
    }

    private void deleteUser(String repoLocation, String adminID, String adminPwd, String user, String pwd) throws Exception {
        Session session = login(repoLocation, adminID, adminPwd);
        Node root = session.getRootNode();
        Node userNode;
        try {
            userNode = root.getNode("users/" + user);
            PropertyIterator allRefs = userNode.getReferences();
            while (allRefs.hasNext()) {
                Property ref = allRefs.nextProperty();
                ref.getParent().remove();
            }
            userNode.remove();
        } catch (PathNotFoundException e) {
            log.error("The requested user was not found.");
            System.exit(-1);
            return;
        }
        session.save();
    }

    private void changePwd(String repoLocation, String adminID, String adminPwd, String user, String pwd) throws Exception {
        Session session = login(repoLocation, adminID, adminPwd);
        Node root = session.getRootNode();
        Node pwdNode;
        try {
            pwdNode = root.getNode("users/" + user + "/jcr:content/password/text");
        } catch (PathNotFoundException e) {
            log.error("The requested user was not found.");
            System.exit(-1);
            return;
        }
        pwdNode.setProperty("xt:characters", pwd);
        session.save();
        log.info("Password changed.");
    }

    private Session login(String repoLocation, String login, String pwd) throws MalformedURLException, NotBoundException, RemoteException, LoginException, RepositoryException {
        ClientRepositoryFactory factory = new ClientRepositoryFactory();
        Repository repo = factory.getRepository(repoLocation);
        Session session = repo.login(new SimpleCredentials(login, pwd.toCharArray()));
        return session;
    }

    /**
	 * Automatically generate and print the help statement.
	 */
    private static void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar mindquarry-pwd-change-<version>.jar", options, true);
    }
}
