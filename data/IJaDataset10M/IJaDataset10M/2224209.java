package org.signserver.cli;

import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import org.ejbca.util.CertTools;
import org.signserver.common.GlobalConfiguration;

/**
 * Commands that uploads a PEM certificate to a singers config.
 *
 * @version $Id: UploadSignerCertificateChainCommand.java,v 1.3 2007-03-14 08:16:49 herrvendil Exp $
 */
public class UploadSignerCertificateChainCommand extends BaseCommand {

    protected static final int HELP = 0;

    protected static final int TRYING = 1;

    protected static final int BADPEM = 2;

    protected static final int FAIL = 3;

    /**
     * Creates a new instance of SetPropertyCommand
     *
     * @param args command line arguments
     */
    public UploadSignerCertificateChainCommand(String[] args) {
        super(args);
    }

    /**
     * Runs the command
     *
     * @throws IllegalAdminCommandException Error in command args
     * @throws ErrorAdminCommandException Error running command
     */
    protected void execute(String hostname, String[] resources) throws IllegalAdminCommandException, ErrorAdminCommandException {
        if (args.length != 4) {
            throw new IllegalAdminCommandException(resources[HELP]);
        }
        try {
            int signerid = getWorkerId(args[1], hostname);
            checkThatWorkerIsSigner(signerid, hostname);
            String scope = args[2];
            if (scope.equalsIgnoreCase("NODE")) {
                scope = GlobalConfiguration.SCOPE_NODE;
            } else {
                if (scope.equalsIgnoreCase("GLOB")) {
                    scope = GlobalConfiguration.SCOPE_GLOBAL;
                } else {
                    throw new IllegalAdminCommandException(resources[FAIL]);
                }
            }
            String filename = args[3];
            Collection certs = CertTools.getCertsFromPEM(filename);
            if (certs.size() == 0) {
                throw new IllegalAdminCommandException(resources[BADPEM]);
            }
            this.getOutputStream().println(resources[TRYING]);
            Iterator iter = certs.iterator();
            while (iter.hasNext()) {
                X509Certificate cert = (X509Certificate) iter.next();
                printCert(cert);
                this.getOutputStream().println("\n");
            }
            getSignSession(hostname).uploadSignerCertificateChain(signerid, certs, scope);
        } catch (Exception e) {
            throw new ErrorAdminCommandException(e);
        }
    }

    public void execute(String hostname) throws IllegalAdminCommandException, ErrorAdminCommandException {
        String[] resources = { "Usage: signserver uploadsignercertificatechain <-host hostname (optional)> <signerid | name> <NODE | GLOB> <filename> \n" + "Example: signserver uploadsignercertificatechain 1 GLOB /home/user/signercertchain.pem\n\n", "Uploading the following signer certificates  : \n", "Error: scope must be one of 'glob' or 'node'", "Invalid PEM file, couldn't find any certificate" };
        execute(hostname, resources);
    }

    public int getCommandType() {
        return TYPE_EXECUTEONMASTER;
    }
}
