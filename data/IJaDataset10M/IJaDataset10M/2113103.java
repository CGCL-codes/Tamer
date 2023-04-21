package ElementDesigner;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * is not integrated in the project
 * 
 * @author Michael Gärtner
 */
public class HTMLHelp extends JFrame implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    Toolkit toolkit = Toolkit.getDefaultToolkit();

    String lang;

    /** Creates new form HTMLHelp */
    public HTMLHelp(String lang) {
        this.lang = lang;
        Dimension scrSize;
        int width = 1000, height = 800;
        this.getContentPane().setLayout(new BorderLayout(0, 0));
        this.initComponents();
        this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        scrSize = this.toolkit.getScreenSize();
        this.setLocation((scrSize.width / 2) - (width / 2), (scrSize.height / 2) - (height / 2));
        this.setSize(width, height);
        this.setTitle("LogicSim Help");
        this.setVisible(true);
        try {
            File f = new File("Website");
            File[] files = f.listFiles();
            String url = new File("Website/index.html").getAbsolutePath();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().startsWith(lang) && files[i].getName().endsWith(".html")) {
                    url = files[i].getAbsolutePath();
                }
            }
            url = url.replaceAll("\\\\", "/");
            url = "file:///" + url;
            url = url.replaceAll(" ", "%20");
            this.jTextPane1.setPage(url);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.setVisible(false);
            this.dispose();
        }
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        int id = e.getID();
        if (id == MouseEvent.MOUSE_CLICKED) {
            this.setVisible(false);
            this.dispose();
        }
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            this.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private void initComponents() {
        this.jButton_ok = new JButton();
        this.jScrollPane1 = new JScrollPane();
        this.jTextPane1 = new JTextPane();
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.jButton_ok.setText("OK");
        this.jButton_ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                HTMLHelp.this.jButton_okActionPerformed(evt);
            }
        });
        this.getContentPane().add(this.jButton_ok, BorderLayout.SOUTH);
        this.jTextPane1.setEditable(false);
        this.jTextPane1.setMinimumSize(new java.awt.Dimension(400, 300));
        this.jScrollPane1.setViewportView(this.jTextPane1);
        this.getContentPane().add(this.jScrollPane1, BorderLayout.CENTER);
        this.pack();
    }

    private void jButton_okActionPerformed(ActionEvent evt) {
        this.dispose();
        this.setVisible(false);
    }

    private JButton jButton_ok;

    private JScrollPane jScrollPane1;

    private JTextPane jTextPane1;
}
