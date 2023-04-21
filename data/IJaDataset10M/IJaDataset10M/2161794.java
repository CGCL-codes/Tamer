package test.xito.dialog;

import org.xito.dialog.*;
import javax.swing.*;
import java.awt.Frame;

public class BasicMessageIcon {

    public static void main(String args[]) {
        ImageIcon icon = new ImageIcon(BasicMessageIcon.class.getResource("ktip32.png"));
        DialogManager.showMessage((Frame) null, icon, "Title", "This is a Test of a Message with Icon");
    }
}
