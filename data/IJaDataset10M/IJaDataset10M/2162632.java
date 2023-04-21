package getionary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Control implements ActionListener {

    String s;

    GuiTranslator gui;

    Req req;

    public Control() {
        req = new Req();
        gui = new GuiTranslator();
        gui.b.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        s = gui.field.getText();
        System.out.println(s);
        String r = req.send(s);
        gui.text.setText(r);
    }

    public void sendReq(String s) {
        String r = req.send(s);
        gui.text.setText(r);
    }
}
