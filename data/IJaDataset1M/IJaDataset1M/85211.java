package jsattrak.about;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AboutDialog extends JDialog implements FadeListener {

    private JComponent contentPane;

    private Timer animation;

    private FadingPanel glassPane;

    /** Creates a new instance of AboutDialog */
    public AboutDialog(String version, JFrame parent, String wwjVersion) {
        super(parent, true);
        glassPane = new FadingPanel(this);
        setGlassPane(glassPane);
        buildContentPane();
        addContents(version, wwjVersion);
        startAnimation();
        setSize(new Dimension(420, 300));
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void buildContentPane() {
        contentPane = new CurvesPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);
    }

    private void addContents(String version, String wwjVersion) {
        JPanel form = new JPanel();
        form.setLayout(null);
        Insets insets = form.getInsets();
        JLabel title = new JLabel("JSatTrak");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        Dimension size = title.getPreferredSize();
        title.setBounds(145 + insets.left, 60 + insets.top, size.width, size.height);
        form.add(title);
        JLabel icon = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/logo/JSatTrakLogo_64.png"))));
        size = icon.getPreferredSize();
        icon.setBounds(55 + insets.left, 35 + insets.top, size.width, size.height);
        form.add(icon);
        JLabel subtitle = new JLabel("Java Satellite Tracker");
        subtitle.setForeground(Color.BLACK);
        size = subtitle.getPreferredSize();
        subtitle.setBounds(140 + insets.left, 90 + insets.top, size.width, size.height);
        form.add(subtitle);
        int vertSpace = 125;
        int vertDelta = 15;
        int hozSpace = 45;
        int indent = 30;
        JLabel mainText = new JLabel("Created and Developed by:");
        size = mainText.getPreferredSize();
        mainText.setBounds(hozSpace + insets.left, vertSpace + insets.top, size.width, size.height);
        mainText.setForeground(Color.BLACK);
        form.add(mainText);
        vertSpace = vertSpace + vertDelta;
        JLabel mainText2 = new JLabel("Shawn E. Gano, shawn@gano.name");
        size = mainText2.getPreferredSize();
        mainText2.setBounds(hozSpace + indent + insets.left, vertSpace + insets.top, size.width, size.height);
        mainText2.setForeground(Color.BLACK);
        form.add(mainText2);
        vertSpace = vertSpace + vertDelta;
        JLabel glpLogo = new JLabel("");
        glpLogo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/lgplv3-88x31.png"))));
        size = glpLogo.getPreferredSize();
        glpLogo.setBounds(hozSpace + insets.left, 175 + insets.top, size.width, size.height);
        form.add(glpLogo);
        JLabel verText = new JLabel(version);
        verText.setFont(new Font("Arial", Font.BOLD, 12));
        size = verText.getPreferredSize();
        verText.setBounds(205 + insets.left, 175 + insets.top, size.width, size.height);
        verText.setForeground(Color.BLACK);
        form.add(verText);
        JLabel verText2 = new JLabel(wwjVersion);
        size = verText2.getPreferredSize();
        verText2.setBounds(205 + insets.left, 195 + insets.top, size.width, size.height);
        verText2.setForeground(Color.BLACK);
        form.add(verText2);
        form.setOpaque(false);
        contentPane.add(form, BorderLayout.CENTER);
    }

    private void startAnimation() {
        animation = new Timer(50, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                contentPane.repaint();
            }
        });
        animation.start();
    }

    public void fadeInFinished() {
        glassPane.setVisible(false);
    }

    public void fadeOutFinished() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                contentPane = new CirclesPanel();
                contentPane.setLayout(new BorderLayout());
                WaitAnimation waitAnimation = new WaitAnimation();
                contentPane.add(waitAnimation, BorderLayout.CENTER);
                setContentPane(contentPane);
                validate();
                glassPane.switchDirection();
            }
        });
    }

    public String getTitle() {
        return "About";
    }
}
