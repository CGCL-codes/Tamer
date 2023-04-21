package ray.mgocc.swing;

import java.applet.Applet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import ray.mgocc.title.Title;

public class App extends JApplet {

    public static void main(String[] args) {
        App app = new App();
        app.showFrame(App.class.getName());
    }

    private Frame frame;

    public void showFrame(String title) {
        Frame f = new Frame(title);
        WindowListener l = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                stop();
                destroy();
                int NORMAL_STATUS = 0;
                System.exit(NORMAL_STATUS);
            }
        };
        f.addWindowListener(l);
        init();
        start();
        f.add("Center", this);
        f.pack();
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int DEFAULT_SIZE = 1000;
        int height = (int) (screenSize.height * 0.5);
        int width = (int) (screenSize.width * 0.5);
        f.setBounds(inset, inset, height, width);
        f.setVisible(true);
        this.frame = f;
    }

    private LoginPanel login = null;

    private ray.App app = null;

    private JDesktopPane top = null;

    public void init() {
        top = new JDesktopPane();
        top.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        setJMenuBar(getMenu());
        setContentPane(top);
        app = new ray.App();
        app.init();
        app.parse();
        JInternalFrame titleList = new TitleListPanel(app.getTitles(), new TitleSelecter(app.getTitles()));
        titleList.pack();
        titleList.setVisible(true);
        top.add(titleList);
    }

    class TitleSelecter extends HashMap<Title, TitlePanel> implements HyperlinkListener {

        TitleSelecter(Collection<Title> titles) {
            for (Title title : titles) {
                put(title, null);
            }
        }

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                selectTitle(e.getDescription());
            }
        }

        private TitlePanel createFrame(Title title) {
            TitlePanel frame = new TitlePanel(title);
            frame.pack();
            frame.setVisible(true);
            return frame;
        }

        private Title getTitle(String title) {
            Title hit = null;
            for (Title sample : keySet()) {
                if (sample.getName().equals(title)) {
                    hit = sample;
                }
            }
            return hit;
        }

        public void selectTitle(final String name) {
            TitlePanel frame;
            Title title = getTitle(name);
            frame = get(title);
            if (null == frame) {
                frame = createFrame(title);
                put(title, frame);
                top.add(frame);
            }
            frame.toFront();
            try {
                frame.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
            }
        }
    }

    public void start() {
    }

    public JMenuBar getMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu app = new JMenu("app");
        app.add(new JMenuItem("exit"));
        bar.add(app);
        JMenu net = new JMenu("net");
        net.add(new JMenuItem("logout"));
        net.add(new JMenuItem("update"));
        net.add(new JMenuItem("select"));
        bar.add(net);
        JMenu file = new JMenu("file");
        file.add(new JMenuItem("open"));
        bar.add(file);
        JMenu view = new JMenu("view");
        view.add(new JMenuItem("score"));
        view.add(new JMenuItem("title"));
        bar.add(view);
        return bar;
    }

    public Container getContainer() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.add(login);
        return panel;
    }

    public void login() {
    }

    public void showLoginDialog() {
        JDialog dialog = new JDialog(frame, true);
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.add(new JPasswordField());
        panel.add(new JTextField());
        dialog.add(panel);
        dialog.show();
    }
}
