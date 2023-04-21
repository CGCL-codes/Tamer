package org.jogre.bura.client;

import org.jogre.client.TableConnectionThread;
import org.jogre.client.awt.JogreClientFrame;
import org.jogre.client.awt.JogreTableFrame;

public class BuraClientFrame extends JogreClientFrame {

    public BuraClientFrame(String[] args) {
        super(args);
    }

    public JogreTableFrame getJogreTableFrame(TableConnectionThread conn) {
        return new BuraTableFrame(conn);
    }

    public static void main(String[] args) {
        BuraClientFrame frame = new BuraClientFrame(args);
    }
}
