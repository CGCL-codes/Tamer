package org.klco.openkeyvalfs.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import org.klco.openkeyvalfs.Utilities;
import org.klco.openkeyvalfs.model.interaction.OpenKeyValInteraction;
import org.klco.openkeyvalfs.model.OpenKeyValConfig;
import org.klco.openkeyvalfs.model.OpenKeyValFile;
import org.klco.openkeyvalfs.model.OpenKeyValFolder;
import org.klco.openkeyvalfs.model.OpenKeyValObject;

/**
 * A simplisitc  mostly wrong Command Line Interface for the OpenKeyVal.org filesystem.
 * Just call the main method and enter 'help' for more information.
 *
 * @author daniel.klco
 * @version 20101105
 */
public class CLI {

    /** the properties key for the version value for this application */
    public static final String VERSION_KEY = "VERSION";

    /**
     * The main method for the CLI version of the FileSystem for OpenKeyVal.org.
     *  Contains a quick and dirty interpreter and parser for text interaction.
     * 
     * @param args no arguments needed
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        echo("FileSystem for OpenKeyVal.org v" + Utilities.getApplicationProperty(VERSION_KEY) + "\n");
        OpenKeyValConfig core = new OpenKeyValConfig();
        String base = null;
        while (base == null || base.trim().length() == 0 || !isValidBase(base)) {
            base = prompt("Please enter base key: ");
        }
        core.setBaseKey(base);
        String command = "";
        while (!command.equals("quit")) {
            if (command.trim().length() == 0) {
            } else if (command.startsWith("help")) {
                echo("Valid commands\n\tconnect [base key] - reconnect with a new base key" + "\n\tls [path] - list the contents of the folder at that path" + "\n\tren [id] [newname] - rename a file or folder" + "\n\tdownload [id] [file] - download the specified node to the file" + "\n\tupload [id] [file] - upload the specified file to the node" + "\n\trm [parentid] [childid] - remove the specified node (will remove child nodes)" + "\n\tmkdir [parentid] [name] - make a new directory with the specified name" + "\n\tupdate [id] [path] - update the file with the specified id with the specified file" + "\n\ttree [id] - list all of the nodes under the specified node" + "\n\tgetnode [id] [chunk] - retrieves the raw value from the node and chunk");
            } else if (command.startsWith("connect")) {
                if (command.split(" ").length == 1) {
                    echo("Invalid call to list, please specify base key.");
                    command = "";
                    continue;
                }
                base = command.substring(command.indexOf(" ") + 1);
                core.setBaseKey(base);
                OpenKeyValInteraction itr = OpenKeyValInteraction.getInteraction(core);
            } else if (command.startsWith("ls")) {
                if (command.split(" ").length == 1) {
                    echo("Invalid call to list, should be in the format list [path].");
                    command = "";
                    continue;
                }
                String data = command.substring(command.indexOf(" ") + 1);
                OpenKeyValFolder root = (OpenKeyValFolder) OpenKeyValObject.load(core, 0);
                try {
                    OpenKeyValFolder fldr = null;
                    if (data.equals("/")) {
                        fldr = root;
                    } else {
                        fldr = root.getObjectAtPath(data);
                    }
                    fldr.loadChildren();
                    echo("\nContents of " + fldr.getId() + " - " + data + " Count " + fldr.getChildCount() + "\n");
                    for (int i = 0; i < fldr.getChildCount(); i++) {
                        OpenKeyValObject child = fldr.getChild(i);
                        if (child instanceof OpenKeyValFolder) {
                            echo("/");
                        }
                        echo(child.getName() + " - " + child.getId() + " - " + new Date(child.getLastModified()) + "\n");
                    }
                } catch (IOException e) {
                    echo("Unable to find folder at " + data);
                }
            } else if (command.startsWith("ren")) {
                if (command.split(" ").length < 3) {
                    echo("Invalid ren command, should be in the form 'mkdir [id] [newname]");
                    command = "";
                    continue;
                }
                try {
                    String data = command.substring(command.indexOf(" ") + 1);
                    int id = Integer.parseInt(data.substring(0, data.indexOf(" ")));
                    String name = data.substring(data.indexOf(" ") + 1);
                    OpenKeyValObject obj = OpenKeyValObject.load(core, id);
                    obj.setName(name);
                    obj.save();
                    echo("Rename successful");
                } catch (Exception e) {
                    echo("Failed to rename due to exception: " + e.getClass() + " " + e.getMessage());
                }
            } else if (command.startsWith("download")) {
                if (command.split(" ").length < 3) {
                    echo("Invalid download command, should be in the form 'download [id] [path]");
                    command = "";
                    continue;
                }
                try {
                    String data = command.substring(command.indexOf(" ") + 1);
                    int id = Integer.parseInt(data.substring(0, data.indexOf(" ")));
                    String name = data.substring(data.indexOf(" ") + 1);
                    OpenKeyValFile file = (OpenKeyValFile) OpenKeyValObject.load(core, id);
                    file.downloadFile(new File(name));
                    echo("File " + name + " Downloaded");
                } catch (Exception e) {
                    echo("Failed to download file due to exception: " + e.getClass() + " " + e.getMessage());
                    e.printStackTrace();
                }
            } else if (command.startsWith("update")) {
                if (command.split(" ").length < 3) {
                    echo("Invalid update command, should be in the form 'update [id] [path]");
                    command = "";
                    continue;
                }
                try {
                    String data = command.substring(command.indexOf(" ") + 1);
                    int id = Integer.parseInt(data.substring(0, data.indexOf(" ")));
                    String toUpload = data.substring(data.indexOf(" ") + 1);
                    OpenKeyValFile file = (OpenKeyValFile) OpenKeyValObject.load(core, id);
                    file.updateContents(new File(toUpload));
                    echo("File " + file.getName() + " Updated");
                } catch (Exception e) {
                    echo("Failed to update file due to exception: " + e.getClass() + " " + e.getMessage());
                }
            } else if (command.startsWith("upload")) {
                if (command.split(" ").length < 3) {
                    echo("Invalid upload command, should be in the form 'upload [parentid] [path]");
                    command = "";
                    continue;
                }
                try {
                    String data = command.substring(command.indexOf(" ") + 1);
                    int id = Integer.parseInt(data.substring(0, data.indexOf(" ")));
                    String name = data.substring(data.indexOf(" ") + 1);
                    OpenKeyValFolder fldr = (OpenKeyValFolder) OpenKeyValObject.load(core, id);
                    File file = new File(name);
                    if (file.isDirectory()) {
                        File[] children = file.listFiles();
                        for (int i = 0; i < children.length; i++) {
                            if (children[i].isFile()) {
                                echo("Uploading file " + (i + 1) + " of " + children.length + " " + children[i].getAbsolutePath() + "\n");
                                OpenKeyValFile.upload(core, fldr, children[i]);
                            }
                        }
                        echo("Files in " + name + " Uploaded");
                    } else {
                        OpenKeyValFile.upload(core, fldr, file);
                        echo("File " + name + " uploaded");
                    }
                } catch (Exception e) {
                    echo("Failed to upload file due to exception: " + e.getClass() + " " + e.getMessage());
                    e.printStackTrace();
                }
            } else if (command.startsWith("mkdir")) {
                if (command.split(" ").length < 3) {
                    echo("Invalid mkdir command, should be in the form 'mkdir [parentid] [name]");
                    command = "";
                    continue;
                }
                try {
                    String data = command.substring(command.indexOf(" ") + 1);
                    int id = Integer.parseInt(data.substring(0, data.indexOf(" ")));
                    String name = data.substring(data.indexOf(" ") + 1);
                    OpenKeyValFolder fldr = (OpenKeyValFolder) OpenKeyValObject.load(core, id);
                    OpenKeyValFolder.create(core, fldr, name);
                    echo("Folder " + name + " Created");
                } catch (Exception e) {
                    echo("Failed to create folder due to exception: " + e.getClass() + " " + e.getMessage());
                }
            } else if (command.startsWith("rm")) {
                if (command.split(" ").length < 2) {
                    echo("Invalid rm, command, should be in the form 'rm [childid]");
                    command = "";
                    continue;
                }
                try {
                    OpenKeyValObject obj = OpenKeyValObject.load(core, Integer.parseInt(command.substring(command.indexOf(" ") + 1)));
                    OpenKeyValFolder fldr = (OpenKeyValFolder) OpenKeyValObject.load(core, obj.getParent().getId());
                    fldr.loadChildren();
                    fldr.removeChild(obj);
                    echo("Object deleted");
                } catch (Exception e) {
                    echo("Failed to delete object due to exception: " + e.getClass() + " " + e.getMessage());
                    e.printStackTrace();
                }
            } else if (command.startsWith("getnode")) {
                if (command.split(" ").length < 3) {
                    echo("Invalid getnode, command, should be in the form 'getnode [id] [chunk]");
                    command = "";
                    continue;
                }
                try {
                    String data = command.substring(command.indexOf(" ") + 1);
                    int id = Integer.parseInt(data.substring(0, data.indexOf(" ")));
                    int chunk = Integer.parseInt(data.substring(data.indexOf(" ") + 1));
                    OpenKeyValInteraction intr = OpenKeyValInteraction.getInteraction(core);
                    echo("Node Value:\n" + intr.get(id, chunk) + "\n");
                } catch (Exception e) {
                    echo("Failed to get node due to exception: " + e.getClass() + " " + e.getMessage());
                }
            } else if (command.startsWith("tree")) {
                if (command.split(" ").length == 1) {
                    echo("Please specify an object to start at");
                    command = "";
                    continue;
                }
                try {
                    int id = Integer.parseInt(command.substring(command.indexOf(" ") + 1));
                    OpenKeyValObject obj = OpenKeyValObject.load(core, id);
                    if (obj == null) {
                        echo("Could not find object: " + id);
                    } else {
                        obj.dump(0);
                    }
                } catch (Exception e) {
                    echo("Failed to create tree due to exception: " + e.getClass() + " " + e.getMessage());
                }
            } else {
                echo("Unknown command: " + command);
            }
            command = prompt("\n> ");
        }
    }

    /**
     * Prints the specified string to the System.out
     *
     * @param echo the string to echo
     */
    public static void echo(String echo) {
        System.out.print(echo);
    }

    /**
     * Prompts the user with the specified text and returns the user's response.
     * 
     * @param prompt the text to response
     * @return the text the user entered
     */
    public static String prompt(String prompt) {
        echo(prompt);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String response = null;
        try {
            response = br.readLine();
        } catch (IOException ioe) {
            System.err.println("IO error trying to read reponse");
            System.exit(1);
        }
        return response;
    }

    private static boolean isValidBase(String base) {
        return base.matches("^[A-Za-z0-9_-]+$");
    }
}
