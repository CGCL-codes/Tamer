package phsperformance.gui.dialog;

import phsperformance.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class AboutDialog extends JDialog {

    public static ImageIcon ICON = new ImageIcon(AboutDialog.class.getResource(PhsPerformanceMain.RESOURCE_BASE_DIR + "speed_kmh.png"));

    public static ImageIcon CROSS = new ImageIcon(AboutDialog.class.getResource(PhsPerformanceMain.RESOURCE_BASE_DIR + "cross.png"));

    public AboutDialog(Frame owner) {
        super(owner, "About PHS performance tool", true);
        JPanel content = (JPanel) getContentPane();
        content.setBorder(new EmptyBorder(15, 15, 15, 15));
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        JLabel lbl = new JLabel("<HTML><CENTER>PPT 1.0.0</CENTER></HTML>", ICON, JLabel.CENTER);
        lbl.setVerticalTextPosition(JLabel.BOTTOM);
        lbl.setHorizontalTextPosition(JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setAlignmentX(JLabel.CENTER);
        p.add(lbl);
        lbl = new JLabel("PHS performance tool");
        lbl.setFont(new Font("Arial", Font.PLAIN, 10));
        p.add(lbl);
        getContentPane().add(p, BorderLayout.CENTER);
        final JButton btOK = new JButton("Close", CROSS);
        ActionListener lst = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        btOK.addActionListener(lst);
        p = new JPanel();
        p.add(btOK);
        getRootPane().setDefaultButton(btOK);
        getRootPane().registerKeyboardAction(lst, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getContentPane().add(p, BorderLayout.SOUTH);
        WindowListener wl = new WindowAdapter() {

            public void windowOpened(WindowEvent e) {
                btOK.requestFocus();
            }
        };
        addWindowListener(wl);
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }
}
