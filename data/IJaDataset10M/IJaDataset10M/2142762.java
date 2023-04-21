package wtanaka.praya.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import wtanaka.debug.Debug;
import wtanaka.debug.DebugSetupJPanel;
import wtanaka.praya.images.ImageDir;

/**
 * This console is good for dumping error messages.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @author Wesley Tanaka
 * @version $Name:  $ $Date: 2003/12/17 01:27:21 $
 **/
public class Console extends JLessLeakyFrame {

    public static final int MAX_SIZE = 100000;

    JTextArea m_textArea;

    boolean m_sizeHasBeenSet = false;

    private static Console s_singleton = null;

    private static synchronized void initSingleton() {
        if (s_singleton == null) {
            s_singleton = new Console();
        }
    }

    public static Console getInstance() {
        if (s_singleton == null) {
            initSingleton();
        }
        return s_singleton;
    }

    /**
    * Constructor.
    **/
    public Console() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageDir.class.getResource("prayaicon.jpg")));
        this.setTitle("Praya: Debug Console");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(m_textArea = new JTextArea()), BorderLayout.CENTER);
        {
            final JPanel butts = new JPanel();
            butts.setLayout(new FlowLayout(FlowLayout.RIGHT));
            {
                final JButton jb = new JButton("Setup");
                jb.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        DebugSetupJPanel.showOrRaiseInstance(JOptionPane.getFrameForComponent(Console.this));
                    }
                });
                butts.add(jb);
            }
            {
                final JButton jb = new JButton("Clear");
                jb.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        m_textArea.setText("");
                    }
                });
                butts.add(jb);
            }
            {
                final JButton jb = new JButton("Close");
                jb.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        setVisible(false);
                    }
                });
                butts.add(jb);
            }
            getContentPane().add(butts, BorderLayout.SOUTH);
        }
    }

    /**
    * Caps the size of this Console to MAX_SIZE characters.
    **/
    private void capSize() {
        Debug.assrt(SwingUtilities.isEventDispatchThread());
        final String currentText = m_textArea.getText();
        final int amountOver = currentText.length() - MAX_SIZE;
        if (amountOver > 0) {
            m_textArea.setText(currentText.substring(amountOver + MAX_SIZE / 10));
        }
    }

    /**
    * Show this instance of Console, as well as cap the size, if
    * necessary.  Is thread safe.
    **/
    public void safeShow() {
        if (SwingUtilities.isEventDispatchThread()) {
            capSize();
            if (!m_sizeHasBeenSet) {
                setSize(400, 400);
                m_sizeHasBeenSet = true;
            }
            if (!isVisible()) {
                GuiUtil.setStateNormal(Console.this);
            }
            setVisible(true);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    safeShow();
                }
            });
        }
    }

    public PrintWriter getPrintWriter() {
        return new PrintWriter(System.err) {

            /**
          * Writes a single character
          **/
            public void write(int c) {
                m_textArea.append(String.valueOf((char) c));
                safeShow();
            }

            public void println(String s) {
                print(s + "\n");
            }

            public void print(String s) {
                m_textArea.append(s);
                safeShow();
            }
        };
    }
}
