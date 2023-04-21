package net.jxta.impl.shell.bin.peers;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.impl.shell.GetOpt;
import net.jxta.impl.shell.ShellApp;
import net.jxta.impl.shell.ShellEnv;
import net.jxta.impl.shell.ShellObject;
import net.jxta.impl.util.TimeUtils;
import net.jxta.protocol.PeerAdvertisement;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

/**
 * peers command lists peers known locally, and discovers peers
 */
public class peers extends ShellApp {

    private ShellEnv env;

    private DiscoveryService discovery = null;

    private int threshold = 10;

    private String pid = null;

    private String attr = null;

    private String val = null;

    private boolean nflag = false;

    private boolean rflag = false;

    private boolean lflag = false;

    private boolean pflag = false;

    public peers() {
    }

    /**
     * {@inheritDoc}
     */
    public int startApp(String[] args) {
        boolean flush = false;
        env = getEnv();
        discovery = getGroup().getDiscoveryService();
        GetOpt getopt = new GetOpt(args, "rflp:n:a:v:");
        int c;
        try {
            while ((c = getopt.getNextOption()) != -1) {
                switch(c) {
                    case 'p':
                        pid = getopt.getOptionArg();
                        pflag = true;
                        break;
                    case 'r':
                        rflag = true;
                        break;
                    case 'l':
                        lflag = true;
                        break;
                    case 'f':
                        flush = true;
                        break;
                    case 'n':
                        nflag = true;
                        threshold = Integer.parseInt(getopt.getOptionArg());
                        break;
                    case 'a':
                        attr = getopt.getOptionArg();
                        break;
                    case 'v':
                        val = getopt.getOptionArg();
                        if ((val.length() == 1) && (val.indexOf("*") != -1)) {
                            consoleMessage("* is not allowed, you must specify at least one char");
                            return ShellApp.appNoError;
                        }
                        break;
                    default:
                        consoleMessage("Illegal option");
                        shortHelp();
                        return ShellApp.appParamError;
                }
            }
            if (flush) {
                try {
                    Enumeration eachAdv = discovery.getLocalAdvertisements(DiscoveryService.PEER, attr, val);
                    while (eachAdv.hasMoreElements()) {
                        Advertisement anAdv = (Advertisement) eachAdv.nextElement();
                        discovery.flushAdvertisement(anAdv);
                    }
                    discovery.publish(getGroup().getPeerAdvertisement());
                    removeEnv();
                } catch (IOException e) {
                    printStackTrace("Flush failed", e);
                    return ShellApp.appMiscError;
                }
                return ShellApp.appNoError;
            }
            if (rflag || pflag) {
                return (discover(pid, attr, val));
            } else {
                return (getLocal(attr, val));
            }
        } catch (Throwable ex) {
            printStackTrace("Exception in command.", ex);
            return ShellApp.appMiscError;
        }
    }

    private int discover(String address, String attr, String val) {
        discovery.getRemoteAdvertisements(address, DiscoveryService.PEER, attr, val, threshold, null);
        consoleMessage("peer discovery message sent");
        return ShellApp.appNoError;
    }

    private int getLocal(String attr, String val) {
        Enumeration res;
        try {
            res = discovery.getLocalAdvertisements(DiscoveryService.PEER, attr, val);
        } catch (Exception e) {
            printStackTrace("Discovery Failed", e);
            return ShellApp.appMiscError;
        }
        int j = 0;
        while (res.hasMoreElements()) {
            PeerAdvertisement peer = (PeerAdvertisement) res.nextElement();
            env.add("peer" + j, new ShellObject<PeerAdvertisement>("PeerAdvertisement", peer));
            String name = peer.getName();
            if ((null == name) || name.equals("")) {
                name = "(Anonymous Peer)";
            }
            if (lflag) {
                long life = discovery.getAdvLifeTime(peer);
                long exp = discovery.getAdvExpirationTime(peer);
                println("peer" + j + ": ID = " + peer.getPeerID() + " name = " + name + "\nExpires on : " + new Date(TimeUtils.toAbsoluteTimeMillis(life)) + "\nFor Others : " + exp + " ms");
            } else {
                println("peer" + j + ": name = " + name);
            }
            j++;
            if (nflag && j == threshold) {
                break;
            }
        }
        return ShellApp.appNoError;
    }

    @Override
    public String getDescription() {
        return "Discover peers";
    }

    public void shortHelp() {
        println("NAME");
        println("     peers - discover peers ");
        println(" ");
        println("SYNOPSIS");
        println("     peers [-p peerid name attribute] ");
        println("           [-n n] limit the number of responses to n from a single peer");
        println("           [-r] discovers peers using propagate");
        println("           [-l] displays peer id as a hex string");
        println("           [-a] specify Attribute name to limit discovery to");
        println("           [-v] specify Attribute value to limit discovery to. wild card is allowed");
        println("           [-f] flush peer advertisements");
    }

    @Override
    public void help() {
        shortHelp();
        println(" ");
        println("DESCRIPTION");
        println(" ");
        println("use \"peers\" to discover other peers within a peer group or at a");
        println("specified peer location. Running \"peers\" command with no options lists");
        println("only the peers already known by the peer (cached). The '-r' option is used");
        println("to send a propagate request to find new peers.");
        println("peers stores results in the local cache, and inserts ");
        println("advertisement(s) into the environment, using the default");
        println("naming: peerX where X is a growing integer number.");
        println(" ");
        println("OPTIONS");
        println("-p peerid");
        println("     discovers peers at a given peer location");
        println("-r");
        println("     discovers peers using remote propagation");
        println("-l");
        println("     displays peer id as a hex string");
        println("-a");
        println("     specify Attribute name to limit discovery to");
        println("-v");
        println("     specify Attribute value to limit discovery to");
        println("-n");
        println("     limit the number of responses to n from a single peer");
        println("-f");
        println("     flush peer advertisements");
        println(" ");
        println("EXAMPLE");
        println(" ");
        println("    JXTA>peers -r");
        println("or");
        println("    JXTA>peers -r -aName -vluxor* ");
        println(" ");
        println("SEE ALSO");
        println("    whoami chpgrp join groups");
    }

    private void removeEnv() {
        String peerenv = "peer0";
        int i = 0;
        while (env.contains(peerenv)) {
            env.remove(peerenv);
            peerenv = "peer" + ++i;
        }
    }
}
