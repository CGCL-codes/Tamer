package net.jxta.impl.shell.bin.share;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.impl.shell.ShellApp;
import net.jxta.impl.shell.ShellEnv;
import net.jxta.impl.shell.ShellObject;
import net.jxta.peergroup.PeerGroup;

/**
 * share Shell command
 * <p/>
 * XXX:
 * This command should be a Codat Sharing Service (CMS). Since at this time
 * there is no available CMS, it uses the internal Core's Codat Sharing mechanism.
 * This must be changed as soon as there is an available CMS.
 * lomax@jxta.org
 */
public class share extends ShellApp {

    private DiscoveryService disco = null;

    ShellEnv env;

    public share() {
    }

    @Override
    public void stopApp() {
    }

    private int syntaxError() {
        println("Usage: share <StructuredDocument>");
        return ShellApp.appParamError;
    }

    public int startApp(String[] args) {
        if ((args == null) || (args.length != 1)) {
            return syntaxError();
        }
        env = getEnv();
        String name = args[0];
        ShellObject obj = env.get("stdgroup");
        PeerGroup group = (PeerGroup) obj.getObject();
        disco = group.getDiscoveryService();
        obj = env.get(name);
        if (obj == null) {
            println("share: cannot access " + name);
            return ShellApp.appMiscError;
        }
        Advertisement adv;
        try {
            adv = AdvertisementFactory.newAdvertisement((StructuredTextDocument) obj.getObject());
            publishAdv(adv);
        } catch (Exception e) {
            println("share: " + name + " is not a proper Document");
            return ShellApp.appMiscError;
        }
        return ShellApp.appNoError;
    }

    private void publishAdv(Advertisement adv) {
        try {
            disco.publish(adv);
        } catch (Exception ignored) {
        }
    }

    @Override
    public String getDescription() {
        return "Share an advertisement";
    }

    @Override
    public void help() {
        println("NAME");
        println("     share - share an advertisement");
        println(" ");
        println("SYNOPSIS");
        println("     share <advertisement>");
        println(" ");
        println("DESCRIPTION");
        println(" ");
        println("share an advertisement document in the current peer group.");
        println("The document is made visible to all the members of the peer group.");
        println("Advertisements are XML documents that can represent any JXTA objects");
        println("advertisement, environment variables). Documents are searched");
        println("either in the local peer cache or remotely via the 'search' command.");
        println(" ");
        println("OPTIONS");
        println(" ");
        println("EXAMPLE");
        println(" ");
        println("    JXTA>share mydoc ");
        println(" ");
        println("This example shares the document 'mydoc' into the current peer group.");
        println(" ");
        println("SEE ALSO");
        println("    peers search");
    }
}
