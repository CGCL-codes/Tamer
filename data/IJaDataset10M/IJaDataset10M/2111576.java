package br.com.jonasluz.velhinha.mobile.screen;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import br.com.jonasluz.velhinha.mobile.Commands;
import br.com.jonasluz.velhinha.mobile.Controller;
import br.com.jonasluz.velhinha.mobile.Messages;

public class MainScreen extends List implements CommandListener {

    Controller game;

    public MainScreen(Controller game) {
        super(Messages.TITLE, List.IMPLICIT);
        this.game = game;
        String[] menu = new String[] { Messages.OPT_NEW_1, Messages.OPT_NEW_2, Messages.OPT_ABOUT };
        for (int i = 0; i < menu.length; i++) {
            String option = menu[i];
            append(option, null);
        }
        ;
        addCommand(SELECT_COMMAND);
        addCommand(Commands.CMD_EXIT);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == SELECT_COMMAND) {
            switch(getSelectedIndex()) {
                case 0:
                    game.selectOponent();
                    break;
                case 1:
                    game.newGame((byte) -1);
                    break;
                case 2:
                    game.credits();
                    break;
            }
            ;
        } else if (c == Commands.CMD_EXIT) {
            game.exit();
        }
    }
}
