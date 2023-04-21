package ch.intertec.storybook.view.net;

import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;
import ch.intertec.storybook.toolkit.net.ClientHttpRequest;

@SuppressWarnings("serial")
public class GoogleMapsDialog extends JDialog {

    public GoogleMapsDialog() {
        super();
        initGUI();
    }

    private void initGUI() {
        MigLayout layout = new MigLayout("flowy");
        setLayout(layout);
        System.out.println("starting...");
        ClientHttpRequest chr;
        try {
            chr = new ClientHttpRequest("http://localhost/storybook/googlemaps/read.php");
            chr.setParameter("test0", "this is a java test");
            chr.setParameter("test1", "blah");
            chr.post();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JButton closeBt = new JButton("close");
        add(closeBt);
        System.out.println("done.");
    }
}
