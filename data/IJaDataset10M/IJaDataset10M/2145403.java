package jfireeagle.examples.swing;

import javax.swing.*;
import jfireeagle.FireEagleClient;

public class ClientFrame extends JFrame {

    private FireEagleClient client;

    public ClientFrame(FireEagleClient c) {
        client = c;
        this.setSize(700, 500);
        this.add(new ClientPanel(c));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Fire Eagle demo");
    }
}
