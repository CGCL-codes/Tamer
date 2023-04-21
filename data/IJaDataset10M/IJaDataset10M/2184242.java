package gui.debug;

import gui.GuiInterface;
import java.util.Scanner;
import config.ConfigInterface;
import controller.ControllerInterface;

public class DebugGui implements GuiInterface {

    private ControllerInterface controller;

    private ConfigInterface config;

    public DebugGui(ControllerInterface controller) {
        this.controller = controller;
        this.config = controller.getConfig();
        startPromt();
    }

    private void startPromt() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Debug GUI");
        System.out.println("SyncIT version " + controller.getVersion());
        String line;
        while (true) {
            try {
                System.out.print("Command: ");
                line = sc.nextLine();
                if (line.equals("help")) {
                    printHelp();
                } else if (line.equals("quit")) {
                    System.out.println("Bye");
                    break;
                } else if (line.startsWith("getvariable")) {
                    String[] split = line.split(" ");
                    String path = (split[1].equals("_")) ? "" : split[1];
                    System.out.println(path + "/" + split[2] + " = " + config.getVariable(path, split[2]));
                } else if (line.startsWith("getlist")) {
                    String[] split = line.split(" ");
                    String path = (split[1].equals("_")) ? "" : split[1];
                    int number = new Integer(split[2]);
                    System.out.println(path + "/" + split[2] + "/" + split[3] + " = " + config.getListVariable(path, number, split[3]));
                } else {
                    System.out.println("type 'help' for a list of commands");
                }
            } catch (Exception e) {
                System.out.println("Command error!");
            }
        }
        controller.terminate();
    }

    private void printHelp() {
        System.out.println("getvariable path name  gets the config value of 'name' with 'path'");
        System.out.println("getlist path # name  gets the config list value of 'name' with 'path' and 'number'");
        System.out.println("quit  to exit SyncIT");
        System.out.println("help  shows this help text");
    }
}
