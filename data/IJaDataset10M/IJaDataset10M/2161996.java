package sf.net.algotrade.iblink.ui.test;

import java.awt.Component;
import javax.swing.JOptionPane;

public class Main {

    @SuppressWarnings("deprecation")
    public static void main(String args[]) {
        SampleFrame sampleFrame = new SampleFrame();
        sampleFrame.show();
    }

    public static void inform(Component parent, String str) {
        showMsg(parent, str, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showMsg(Component parent, String str, int type) {
        JOptionPane.showMessageDialog(parent, str, "IB Java Test Client", type);
    }
}
