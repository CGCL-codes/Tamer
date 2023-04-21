package megamek.client.ui.AWT;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import megamek.client.ui.Messages;
import megamek.client.ui.AWT.widget.AdvancedLabel;
import megamek.client.ui.AWT.widget.BackGroundDrawer;
import megamek.client.ui.AWT.widget.BufferedPanel;

/**
 * Every about dialog in MegaMek should have an identical look-and-feel.
 */
public class CommonAboutDialog extends Dialog {

    /**
     *
     */
    private static final long serialVersionUID = 4295988339023189039L;

    /**
     * We only need a single copy of the "about" title image that we share.
     */
    private static Image imgTitleImage = null;

    /**
     * Get the single title image in a threadsafe way.
     *
     * @param frame - a <code>Frame</code> object to instantiate the image.
     * @return the title <code>Image</code> common to all "about" dialogs.
     *         This value should <b>not</b> be <code>null</code>.
     */
    private static synchronized Image getTitleImage(Frame frame) {
        if (imgTitleImage == null) {
            Image image = frame.getToolkit().getImage("data/images/misc/megamek-splash2.gif");
            MediaTracker tracker = new MediaTracker(frame);
            tracker.addImage(image, 0);
            try {
                tracker.waitForID(0);
                imgTitleImage = image;
            } catch (InterruptedException exp) {
                exp.printStackTrace();
            }
        }
        return imgTitleImage;
    }

    /**
     * Create an "about" dialog for MegaMek.
     *
     * @param frame - the parent <code>Frame</code> for this dialog.
     */
    public CommonAboutDialog(Frame frame) {
        super(frame, Messages.getString("CommonAboutDialog.title"));
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        BufferedPanel panTitle = new BufferedPanel();
        Image imgSplash = CommonAboutDialog.getTitleImage(frame);
        BackGroundDrawer bgdTitle = new BackGroundDrawer(imgSplash);
        panTitle.addBgDrawer(bgdTitle);
        panTitle.setPreferredSize(imgSplash.getWidth(null), imgSplash.getHeight(null));
        StringBuffer buff = new StringBuffer();
        buff.append(Messages.getString("CommonAboutDialog.version")).append(megamek.MegaMek.VERSION).append(Messages.getString("CommonAboutDialog.timestamp")).append(new Date(megamek.MegaMek.TIMESTAMP).toString()).append(Messages.getString("CommonAboutDialog.javaVendor")).append(System.getProperty("java.vendor")).append(Messages.getString("CommonAboutDialog.javaVersion")).append(System.getProperty("java.version"));
        AdvancedLabel lblVersion = new AdvancedLabel(buff.toString());
        AdvancedLabel lblCopyright = new AdvancedLabel(Messages.getString("CommonAboutDialog.copyright"));
        AdvancedLabel lblAbout = new AdvancedLabel(Messages.getString("CommonAboutDialog.about"));
        Button butClose = new Button(Messages.getString("CommonAboutDialog.Close"));
        butClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                quit();
            }
        });
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(4, 4, 1, 1);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.ipadx = 10;
        c.ipady = 5;
        c.gridx = 0;
        c.gridy = 0;
        this.add(panTitle, c);
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1;
        this.add(lblVersion, c);
        c.gridy = 2;
        this.add(lblCopyright, c);
        c.gridy = 3;
        this.add(lblAbout, c);
        c.gridy = 4;
        this.add(butClose, c);
        pack();
        setLocationRelativeTo(frame);
        setResizable(false);
    }

    void quit() {
        setVisible(false);
    }
}
