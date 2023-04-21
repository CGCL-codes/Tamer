package com.googlecode.harapeko.examples;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.googlecode.harapeko.Harapeko;
import com.googlecode.harapeko.annotation.JButtonResource;
import com.googlecode.harapeko.annotation.JFrameResource;
import com.googlecode.harapeko.annotation.JPasswordFieldResource;
import com.googlecode.harapeko.annotation.JTextFieldResource;
import com.googlecode.harapeko.annotation.layout.GridLayout;

@GridLayout(rows = 3, cols = 2, layout = { "__user name__", "userName", "__password__", "password", "okButton", "cancelButton" })
@JFrameResource(title = "EmbeddedLabel", defaultCloseOperation = JFrame.EXIT_ON_CLOSE)
public class EmbeddedLabel extends JFrame {

    @JTextFieldResource
    JTextField userName;

    @JPasswordFieldResource
    JPasswordField password;

    @JButtonResource(text = "OK")
    JButton okButton;

    @JButtonResource(text = "Cancel")
    JButton cancelButton;

    public static void main(String[] args) {
        Harapeko.launch(EmbeddedLabel.class);
    }
}
