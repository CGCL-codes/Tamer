package br.ufpe.cin.imersion.pattern.behavioral.command.FlexManager;

import br.ufpe.cin.imersion.pattern.behavioral.command.Command;

/**
 * @author Marcello Alves de Sales Junior <BR>
 * email: masj2@cin.ufpe.br <BR>
 * Jan 27, 2004 
 */
public class PhoneEnableBitCommand implements Command {

    private PhoneConnection connection;

    public static void main(String[] args) {
    }

    public PhoneEnableBitCommand(PhoneConnection connection) {
        this.connection = connection;
    }

    public void execute() {
        this.connection.enableBit();
    }
}
