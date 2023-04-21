package ch.intertec.storybook.view.net;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.Timer;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import net.miginfocom.swing.MigLayout;
import ch.intertec.storybook.toolkit.I18N;
import ch.intertec.storybook.toolkit.net.NetTools;
import ch.intertec.storybook.toolkit.swing.SwingTools;

@SuppressWarnings("serial")
public class BrowserPanel extends JEditorPane implements HyperlinkListener {

    private Timer timer;

    private String url;

    private int width;

    private int height;

    public BrowserPanel(String url) {
        this(url, 200, 200);
    }

    public BrowserPanel(String url, int width, int height) {
        super();
        this.url = url;
        this.width = width;
        this.height = height;
        setContentType("text/html");
        setEditable(false);
        timer = new Timer(100, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loadPage();
            }
        });
        initGUI();
        addHyperlinkListener(this);
    }

    private void initGUI() {
        MigLayout layout = new MigLayout("wrap,fill", "[]", "[]");
        setLayout(layout);
        setPreferredSize(new Dimension(width, height));
        timer.start();
    }

    private void loadPage() {
        try {
            setPage(url);
            timer.stop();
            SwingTools.setDefaultCursor(this);
        } catch (IOException e) {
            timer.stop();
            SwingTools.setDefaultCursor(this);
            setText(I18N.getMsg("msg.error.internet.connection.failed", url));
        }
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent evt) {
        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                NetTools.openBrowser(evt.getURL().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
