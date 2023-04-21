package samples.chat;

import javax.swing.UIManager;

public class Chat {

    boolean packFrame = false;

    public Chat(String[] args) {
        Frame frame = new Frame();
        if (args.length > 0) {
            frame.setSubject(args[0]);
        }
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Chat(args);
    }
}
