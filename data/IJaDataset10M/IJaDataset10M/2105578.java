package hackerz.commands;

import hackerz.coreSoftware.Terminal;
import java.util.ArrayList;

/**
 * A library of commands that can be used in the virtual console.
 * @author Steffen Gates
 */
public class CommandLibrary {

    ArrayList<ConsoleCommand> commands;

    Terminal terminal;

    public CommandLibrary(Terminal terminal) {
        commands = new ArrayList<ConsoleCommand>();
        this.terminal = terminal;
    }

    /**
     * Add a new command to the library
     * @param root
     * @param args
     * @param id
     */
    public void addCommand(String root, ArrayList<String> args, int id) {
        commands.add(new ConsoleCommand(root, args, id));
    }

    /**
     * Add a new command to the library
     * @param root
     * @param args
     * @param id
     */
    public void addCommand(String root, String args, int id) {
        String[] list;
        if (args.length() == 0) {
            list = new String[0];
        } else {
            list = args.split(",");
        }
        commands.add(new ConsoleCommand(root, list, id));
    }

    /**
     * Return the ID associated with this command for use by the executeCommand
     * method.
     * class.
     * @param root
     * @param args
     * @return
     */
    public int getCommandID(String root, String[] args) {
        for (ConsoleCommand cc : commands) {
            if (cc.equals(root, args)) {
                return cc.getID();
            }
        }
        System.out.println("I couldn't find it!!!!! " + root + " - ");
        for (int i = 0; i < args.length; i++) {
            System.out.print(args[i] + " ");
        }
        System.out.println("");
        return -1;
    }

    /**
     * Execute the specific root/argument combo. Logic for each command can be
     * implemented here.
     *
     * -- I'd like to to this programtically, but can't think of a really good
     * -- way to do that... This list is short enough (< ~50 commands) that I'm
     * -- not too conerned about it at the moment.
     *
     * -- The biggest problem with this method is that the number associated with
     * -- a command must be known and cannot easily be changed, but since the
     * -- number is internal, that doesn't matter too much. See the Init class.
     *
     * @param root
     * @param args
     * @return
     */
    public boolean executeCommand(String root, String[] args) {
        switch(getCommandID(root, args)) {
            case 0:
                terminal.cap(false);
                break;
            case 1:
                if (args[0].compareTo("-g") == 0) {
                    terminal.cap(true);
                }
                break;
            case 2:
                terminal.terminate();
                break;
            case 3:
                terminal.disconnect();
                break;
            case 4:
                terminal.decipher();
                break;
            case 5:
                terminal.top(false);
                break;
            case 6:
                if (args[0].compareTo("-u") == 0) {
                    terminal.top(true);
                }
                break;
            case 7:
                terminal.help();
                break;
            case 8:
                boolean exists = false;
                if (args[0].contains(":")) {
                    String[] addressPort = args[0].split(":");
                    exists = terminal.serverList.lookup(addressPort[0], Integer.parseInt(addressPort[1]));
                    if (exists) {
                        terminal.connect(addressPort[0], Integer.parseInt(addressPort[1]));
                    } else {
                        terminal.println("Address Invalid. ");
                    }
                } else {
                    exists = terminal.serverList.lookup(args[0], 80);
                    if (exists) {
                        terminal.connect(args[0], 80);
                    } else {
                        terminal.println("Address Invalid. ");
                    }
                }
                break;
            case 9:
                String[] list = args[0].split(":");
                if (list.length > 1) {
                    terminal.analyze(list[0], Integer.parseInt(list[1]));
                } else {
                    terminal.analyze(args[0], 80);
                }
                break;
            case 10:
                terminal.scan(args[0], 80);
                break;
            case 11:
                terminal.upload(args[0]);
                break;
            case 12:
                terminal.download(args[0]);
                break;
            case 13:
                try {
                    terminal.kill(Integer.parseInt(args[0]));
                } catch (Exception e) {
                    return false;
                }
                break;
            case 14:
                terminal.cat(args[0]);
                break;
            case 15:
                if (args[1].compareTo(">") != 0) {
                    return false;
                }
                break;
            case 16:
                terminal.chmod(args[0], args[1]);
                break;
            case 17:
                terminal.compress(args[0]);
                break;
            case 18:
                terminal.decompress(args[0]);
                break;
            case 19:
                terminal.encrypt(args[0]);
                break;
            case 20:
                terminal.decrypt(args[0]);
                break;
            case 21:
                terminal.execute(args[0]);
                break;
            case 22:
                terminal.execute(args[0]);
                break;
            case 23:
                terminal.execute(args[0]);
                break;
            case 24:
                if (!args[1].equals("moniter")) {
                    return false;
                }
                terminal.bypass(0, args[1]);
                break;
            case 25:
                if (!args[1].equals("firewall")) {
                    return false;
                }
                terminal.bypass(1, args[1]);
                break;
            case 26:
                if (!args[1].equals("proxy")) {
                    return false;
                }
                terminal.bypass(2, args[1]);
                break;
            case 27:
                terminal.bounce();
                break;
            case 28:
                terminal.man(args[0]);
                break;
            case 29:
                terminal.ls(".");
                break;
            case 30:
                terminal.ls(args[0]);
                break;
            case 31:
                terminal.cd(args[0]);
                break;
            case 32:
                terminal.mkdir(args[0]);
                break;
            case 33:
                terminal.rm(args[0]);
                break;
            case 34:
                terminal.rmdir(args[0]);
                break;
            default:
                return false;
        }
        return true;
    }
}
