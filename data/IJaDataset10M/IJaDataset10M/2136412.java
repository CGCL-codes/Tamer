package gui.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class AboutDialog extends JDialog {

    private static final String ABOUT = "О программе";

    private static final String OK = "Ок";

    private static final String COPYRIGHT = "Copyright (c) Sergey Bylokhov";

    private static final String OS = "System: " + System.getProperty("os.name") + " version " + System.getProperty("os.version") + " running on " + System.getProperty("os.arch");

    private final String REVISION = "Version 0.2 Alpha Revison: " + getCoreVersion();

    /**
     * Get svn version.
     *
     * @return Возвращает строку которая сожержит версию и рефизию программы
     */
    private String getCoreVersion() {
        final String implementationVersion = getClass().getPackage().getImplementationVersion();
        if (implementationVersion == null) {
            return "Developer version";
        }
        return implementationVersion;
    }

    /**
     * Constructs a gui.AboutDialog.
     */
    public AboutDialog() {
        setTitle(ABOUT);
        setModal(true);
        final Box main = Box.createVerticalBox();
        main.setBorder(BorderFactory.createEmptyBorder(32, 42, 32, 42));
        final JLabel jLabel = new JLabel(COPYRIGHT);
        final JLabel jLabelRevision = new JLabel(REVISION);
        final JLabel jLabelOS = new JLabel(OS);
        main.add(jLabel);
        main.add(jLabelRevision);
        main.add(jLabelOS);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        final JButton cancel = new JButton(OK);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        add(main);
        add(cancel);
        pack();
        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((d.width - getWidth()) / 2, (d.height - getHeight()) / 2, getWidth(), getHeight());
        setVisible(true);
    }
}
