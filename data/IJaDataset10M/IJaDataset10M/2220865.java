package jm2pc.client.shell;

import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import jm2pc.client.JM2PCMIDlet;
import jm2pc.utils.Constants;

public class FormShell extends Form implements CommandListener {

    private JM2PCMIDlet midlet;

    private TextField tfWait;

    private TextField tfLineNumbers;

    private TextField tfCommand;

    private StringItem siResponse;

    private Command cmShowOptions;

    private Command cmBack;

    private Command cmSend;

    private Command cmClear;

    private boolean visibleOptions;

    public FormShell(JM2PCMIDlet midlet) {
        super(midlet.messages.getMessage("shell"));
        this.midlet = midlet;
        tfWait = new TextField(midlet.messages.getMessage("wait"), "2000", 6, TextField.NUMERIC);
        tfLineNumbers = new TextField(midlet.messages.getMessage("returnLines"), "40", 2, TextField.NUMERIC);
        visibleOptions = true;
        tfCommand = new TextField("$:", null, 100, TextField.ANY);
        siResponse = new StringItem("$:", "");
        cmSend = new Command(midlet.messages.getMessage("send"), Command.OK, 1);
        cmBack = new Command(midlet.messages.getMessage("back"), Command.BACK, 2);
        cmShowOptions = new Command(midlet.messages.getMessage("options"), Command.SCREEN, 3);
        cmClear = new Command(midlet.messages.getMessage("clear"), Command.SCREEN, 4);
        append(tfWait);
        append(tfLineNumbers);
        append(tfCommand);
        append(siResponse);
        addCommand(cmBack);
        addCommand(cmSend);
        addCommand(cmShowOptions);
        addCommand(cmClear);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmSend) {
            String cmd = tfCommand.getString();
            String tempoEspera = tfWait.getString();
            String numLinhas = tfLineNumbers.getString();
            if ((cmd.trim().length() == 0) || (tempoEspera.trim().length() == 0) || (numLinhas.trim().length() == 0)) {
                midlet.showAlert(midlet.messages.getMessage("errorRun"), AlertType.ERROR, false, this);
                return;
            }
            StringBuffer sb = new StringBuffer();
            sb.append(Constants.CMD_PROCESSO_TP_CONSOLE);
            sb.append(' ');
            sb.append(tfWait.getString());
            sb.append(' ');
            sb.append(tfLineNumbers.getString());
            sb.append(' ');
            sb.append(cmd);
            midlet.sendCommand(Constants.CMD_PROCESSO, sb.toString());
        } else if (c == cmBack) {
            midlet.getDisplayManager().popDisplayable();
        } else if (c == cmShowOptions) {
            if (visibleOptions) hideOptions(); else showOptions();
        } else if (c == cmClear) {
            siResponse.setLabel("$:");
            siResponse.setText("");
            tfCommand.setString("");
        }
    }

    private void setResposta(String resposta) {
        siResponse.setLabel(tfCommand.getString());
        siResponse.setText(resposta);
    }

    public void update() throws IOException {
        DataInputStream dataIn = new DataInputStream(midlet.getIn());
        setResposta(dataIn.readUTF());
    }

    public void hideOptions() {
        delete(0);
        delete(0);
        visibleOptions = false;
    }

    public void showOptions() {
        insert(0, tfWait);
        insert(1, tfLineNumbers);
        visibleOptions = true;
    }
}
