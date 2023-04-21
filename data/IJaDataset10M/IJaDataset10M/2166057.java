package userinterface;

import java.awt.EventQueue;

public class TestFrame {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    StartFrame startFrame = new StartFrame();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
