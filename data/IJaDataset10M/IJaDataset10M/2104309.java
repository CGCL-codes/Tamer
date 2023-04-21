package boardgamebox.swingui;

import boardgamebox.swingui.BoardGameBoxFrame;
import boardgamebox.swingui.util.ComponentFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A dialog that shows information about this application
 * $Id: AboutDialog.java,v 1.1.1.1 2008/12/15 15:19:42 larsdam Exp $
 */
public class AboutDialog extends JDialog {

    /**
     * Text that is show in the about dialog
     */
    private static final String infoText = "BoardGameBox was initially created by Lars Dam (larsdam@gmail.com)   \n" + "\n" + "Project is located at: http://sourceforge.net/projects/boardgamebox\n" + "Report bugs to: http://sourceforge.net/tracker/?group_id=132669&atid=724879  \n" + "Report feature request to: http://sourceforge.net/tracker/?group_id=132669&atid=724882  \n" + "\n" + "BoardGameBox uses icons from: http://www.famfamfam.com/lab/icons/silk/   \n" + "BoardGameBox uses Simon Tuffs One-JAR tool: http://one-jar.sourceforge.net/\n" + "\n" + "Version " + BoardGameBoxFrame.version;

    private JTextArea infoTextArea = ComponentFactory.createTextArea();

    private JScrollPane infoScrollPane = ComponentFactory.createScrollPane(infoTextArea);

    private JPanel buttonsPanel = ComponentFactory.createPanel();

    private JButton closeButton = ComponentFactory.createButton("Close");

    private Frame owner;

    public AboutDialog(Frame owner) {
        super(owner);
        this.owner = owner;
        setTitle("About BoardGameBox");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(BorderLayout.CENTER, infoScrollPane);
        getContentPane().add(BorderLayout.SOUTH, buttonsPanel);
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(closeButton);
        infoTextArea.setEditable(false);
        infoTextArea.setText(infoText);
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        closeButton.setDefaultCapable(true);
        getRootPane().setDefaultButton(closeButton);
    }

    public void invoke() {
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
