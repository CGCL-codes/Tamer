package wtanaka.praya.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import wtanaka.praya.images.ImageDir;

/**
 * Proposed new widget for text in a message drop.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @todo Use This Instead Of JTextArea
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2003/12/17 01:27:21 $
 **/
public class DefaultMessage extends JTextArea {

    public DefaultMessage(String msg) {
        super(msg);
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
        this.setMinimumSize(new Dimension(10, 10));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.setEditable(false);
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                String url = getHyperlinkContaining(viewToModel(evt.getPoint()));
                if (url != null) {
                    try {
                        Runtime.getRuntime().exec("netscape " + url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String getHyperlinkContaining(int i) {
        int min = 0;
        if (i - 5 > min) min = i - 5;
        int max = i;
        int http = getText().lastIndexOf("http", i);
        if (http >= 0) {
            String startURL = getText().substring(http);
            final char[] nonURLChars = { ' ', '<', '>', '\n', '\t' };
            for (i = 0; i < nonURLChars.length; ++i) {
                int nonURLChar = startURL.indexOf(nonURLChars[i]);
                if (nonURLChar >= 0) {
                    startURL = startURL.substring(0, nonURLChar);
                }
            }
            if (http + startURL.length() > i) {
                return startURL;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        JLessLeakyFrame frame = new JLessLeakyFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageDir.class.getResource("prayaicon.jpg")));
        frame.setTitle("Default Message");
        frame.getContentPane().add(new DefaultMessage("Have you tried ofb?\n" + "http://www.ofb.net/"));
        frame.setSize(200, 200);
        frame.setVisible(true);
    }
}
