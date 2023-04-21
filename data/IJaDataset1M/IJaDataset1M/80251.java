package columnasMenu;

import javax.microedition.lcdui.*;
import java.io.IOException;
import javax.microedition.rms.RecordStoreException;

public class CMyTextBox extends TextBox implements CommandListener {

    private Display currentDisplay;

    private Displayable anteriorDisplayable;

    private Command cmdAceptar;

    private CMenuCanvas menuJuego;

    public CMyTextBox(String title, Display d, CMenuCanvas m) {
        super(title, null, 12, TextField.ANY);
        currentDisplay = d;
        menuJuego = m;
        cmdAceptar = new Command("Aceptar", Command.OK, 0);
        this.addCommand(cmdAceptar);
        setCommandListener(this);
    }

    public void setDisplayable(Displayable d) {
        anteriorDisplayable = d;
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == cmdAceptar) {
            try {
                menuJuego.ActualizaPuntuacion();
            } catch (Exception ex) {
            }
            currentDisplay.setCurrent(anteriorDisplayable);
        }
    }
}
