package org.signserver.cli;

/**
 * Factory for General signserver Commands.
 *
 * @version $Id: SignServerCommandFactory.java,v 1.2 2007-03-07 07:41:19 herrvendil Exp $
 */
public class SignServerCommandFactory {

    /**
     * Cannot create an instance of this class, only use static methods.
     */
    private SignServerCommandFactory() {
    }

    /**
     * Returns an Admin Command object based on contents in args[0].
     *
     * @param args array of arguments typically passed from main().
     *
     * @return Command object or null if args[0] does not specify a valid command.
     */
    public static IAdminCommand getCommand(String[] args) {
        if (args.length < 1) {
            return null;
        }
        if (args[0].equalsIgnoreCase("getstatus")) {
            return new GetStatusCommand(args);
        }
        if (args[0].equalsIgnoreCase("getconfig")) {
            return new GetConfigCommand(args);
        }
        if (args[0].equalsIgnoreCase("reload")) {
            return new ReloadCommand(args);
        }
        if (args[0].equalsIgnoreCase("setproperty")) {
            return new SetPropertyCommand(args);
        }
        if (args[0].equalsIgnoreCase("setproperties")) {
            return new SetPropertiesCommand(args);
        }
        if (args[0].equalsIgnoreCase("setpropertyfromfile")) {
            return new SetPropertyFromFileCommand(args);
        }
        if (args[0].equalsIgnoreCase("removeproperty")) {
            return new RemovePropertyCommand(args);
        }
        if (args[0].equalsIgnoreCase("dumpproperties")) {
            return new DumpPropertiesCommand(args);
        }
        if (args[0].equalsIgnoreCase("listauthorizedclients")) {
            return new ListAuthorizedClientsCommand(args);
        }
        if (args[0].equalsIgnoreCase("addauthorizedclient")) {
            return new AddAuthorizedClientCommand(args);
        }
        if (args[0].equalsIgnoreCase("removeauthorizedclient")) {
            return new RemoveAuthorizedClientCommand(args);
        }
        if (args[0].equalsIgnoreCase("uploadsignercertificate")) {
            return new UploadSignerCertificateCommand(args);
        }
        if (args[0].equalsIgnoreCase("uploadsignercertificatechain")) {
            return new UploadSignerCertificateChainCommand(args);
        }
        if (args[0].equalsIgnoreCase("activatesigntoken")) {
            return new ActivateSignTokenCommand(args);
        }
        if (args[0].equalsIgnoreCase("deactivatesigntoken")) {
            return new DeactivateSignTokenCommand(args);
        }
        if (args[0].equalsIgnoreCase("generatecertreq")) {
            return new GenerateCertReqCommand(args);
        }
        if (args[0].equalsIgnoreCase("archive")) {
            return getArchiveCommand(args);
        }
        return null;
    }

    private static IAdminCommand getArchiveCommand(String[] args) {
        if (args.length < 2) {
            return null;
        }
        if (args[1].equalsIgnoreCase("findfromarchiveid")) {
            return new FindFromArchiveIdCommand(args);
        }
        if (args[1].equalsIgnoreCase("findfromrequestip")) {
            return new FindFromRequestIPCommand(args);
        }
        if (args[1].equalsIgnoreCase("findfromrequestcert")) {
            return new FindFromRequestCertCommand(args);
        }
        return null;
    }
}
