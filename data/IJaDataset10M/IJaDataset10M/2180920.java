package com.wpjr.screen;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import com.wpjr.entity.Code;
import com.wpjr.midlet.CodeMemoMIDlet;

public class CodeForm extends Form implements CommandListener {

    private TextField name, password;

    private Command ok, exit;

    private CodeMemoMIDlet theMidlet;

    public CodeForm(CodeMemoMIDlet theMidlet) {
        super("New Code");
        this.theMidlet = theMidlet;
        init();
    }

    private void init() {
        name = new TextField("Name:", "", 20, TextField.ANY);
        password = new TextField("Code:", "", 40, TextField.ANY);
        append(name);
        append(password);
        ok = new Command("Ok", Command.OK, 1);
        exit = new Command("Exit", Command.EXIT, 1);
        addCommand(ok);
        addCommand(exit);
        setCommandListener(this);
        theMidlet.getTheDisplay().setCurrent(this);
    }

    public void commandAction(Command arg0, Displayable arg1) {
        if (arg0.equals(ok)) {
            Code code = new Code();
            code.setName(name.getString());
            code.setCode(password.getString());
            theMidlet.addCode(code);
            theMidlet.reloadCodesFromRMS();
        } else if (arg0.equals(exit)) {
            theMidlet.exit();
        }
    }
}
