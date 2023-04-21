package ihmManager;

import ioManager.ExportInformation;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JButton;
import databaseManager.MembersDB;

/**
 * 
 * @author Imad BOU-SAID
 * @since 08/02/2010
 * @lastUpdate 22/02/2010
 * 
 * @version 1.10
 * 
 * Display Members list .
 * Extends from TextAreaWindow.
 * Used by users and SuperUser level
 *
 */
public class DisplayMemberList extends TextAreaWindow {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3677784903383509580L;

    /**
	 * Constructor used by users
	 * @param title the frame title
	 * @param text the text to display
	 * @param comName the com name
	 */
    public DisplayMemberList(String title, Vector<String> text, String comName) {
        super(title, text, comName);
        this.comName = comName;
        this.text = text;
        createCommonComponentsForTextAreaWindow();
        this.jtaNewWindow.setEditable(false);
        createComponents();
        createCommonPanels();
        createPanels();
    }

    /**
	 * Constructor used by SuperUser
	 * @param title the frame title
	 * @param text the text to display
	 */
    public DisplayMemberList(String title, Vector<String> text) {
        super(title, text);
        this.text = text;
        createCommonComponentsForTextAreaWindow();
        this.jtaNewWindow.setEditable(false);
        createComponents();
        createCommonPanels();
        createPanels();
    }

    @SuppressWarnings("unchecked")
    public void createComponents() {
        this.jbSaveToFile = new JButton(EXPORT_TO_FILE);
        this.jbSaveToFile.setBackground(BUTTON_BG);
        this.jbSaveToFile.setForeground(BUTTON_TEXT);
        jbSaveToFile.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                jbSaveToFile.setForeground(BUTTON_TEXT_MOUSE_ENTERED);
                jbSaveToFile.setBackground(BUTTON_BG_MOUSE_ENTERED);
            }

            public void mouseExited(MouseEvent arg0) {
                jbSaveToFile.setForeground(BUTTON_TEXT_MOUSE_EXITED);
                jbSaveToFile.setBackground(BUTTON_BG_MOUSE_EXITED);
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                jbSaveToFile.setForeground(BUTTON_TEXT_MOUSE_PRESSED);
                jbSaveToFile.setBackground(BUTTON_BG_MOUSE_PRESSED);
            }
        });
        Collections.sort(text);
        for (int i = 0; i < text.size(); i++) {
            String member = text.get(i).toString();
            MembersDB mbd = new MembersDB();
            String memberID = mbd.getMemberIDForaMember(member.split(DELIMITER)[1], member.split(DELIMITER)[2], member.split(DELIMITER)[4], Integer.parseInt(member.split(DELIMITER)[5]));
            String memberInformation = mbd.getLightMemberInformation(memberID);
            this.setTextArea(memberInformation.split(DELIMITER)[1].toUpperCase() + " " + memberInformation.split(DELIMITER)[2] + " " + memberInformation.split(DELIMITER)[3] + " " + memberInformation.split(DELIMITER)[4] + " " + memberInformation.split(DELIMITER)[5] + " " + memberInformation.split(DELIMITER)[6] + " " + memberInformation.split(DELIMITER)[7] + " " + memberInformation.split(DELIMITER)[8] + " " + "\n");
        }
        jbSaveToFile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent aeSave) {
                ExportInformation ei = new ExportInformation(DisplayMemberList.this.comName);
                ei.exportMembersInformation();
            }
        });
    }

    public void createPanels() {
        panel.add(jbSaveToFile);
        pane.add(panel, BorderLayout.PAGE_END);
        this.setVisible(true);
        this.setSize(500, 350);
    }
}
