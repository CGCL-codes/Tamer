package br.ufpe.cin.imersion.pattern.behavioral.command.fan;

import br.ufpe.cin.imersion.pattern.behavioral.command.Command;

/**
 * @author Marcello Alves de Sales Junior <BR>
 * email: masj2@cin.ufpe.br <BR>
 * Jan 27, 2004 
 */
public class FanOnCommand implements Command {

    private Fan myFan;

    public FanOnCommand(Fan F) {
        myFan = F;
    }

    public void execute() {
        myFan.startRotate();
    }
}
