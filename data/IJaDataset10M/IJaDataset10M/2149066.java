package info.obscured.deliverator;

import com.jeta.forms.components.panel.FormPanel;
import javax.swing.*;
import java.awt.*;

public class Deliverator extends JFrame {

    public Deliverator() {
        super("Deliverator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setSize(300, 230);
        Container contentPane = getContentPane();
        FormPanel mainForm = new FormPanel("info/obscured/deliverator/MainForm.jfrm");
        contentPane.setLayout(new BorderLayout());
        contentPane.add(mainForm, BorderLayout.CENTER);
        pack();
    }

    public static void main(String[] args) {
        Deliverator deliverator = new Deliverator();
        deliverator.setVisible(true);
    }
}
