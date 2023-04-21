package com.google.code.javastorage.cli.cmd;

/**
 * 
 * @author thomas.scheuchzer@gmail.com
 * 
 */
public class Quit extends AbstractCommand {

    @Override
    public void execute(String[] args) {
        print("Bye!");
        System.exit(0);
    }
}
