package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import main.TaironaChatRoomPrivate;
import main.TaironaChatRoomTemplate;
import main.TaironaClient;
import main.TaironaStringLocale;
import java.util.Iterator;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ChatRoomsDialog extends javax.swing.JDialog implements ActionListener, ListSelectionListener, Observer {

    protected JPanel jPanelButtons;

    protected JButton jButtonEnter;

    protected JButton JButtonExit;

    protected JButton jButtonLeave;

    protected String rooms[];

    protected TaironaMainWindow mainPanel;

    protected JList jListRooms;

    protected JScrollPane jScrolllRooms;

    protected JButton jButtonUpdate;

    protected JButton jButtonCreateRoom;

    protected TaironaStringLocale m_stringLocale;

    protected TaironaClient m_client;

    protected ArrayList<String> m_alistRoomNames;

    /**
	 * @param frame
	 */
    public ChatRoomsDialog(JFrame frame) {
        super(frame);
        mainPanel = (TaironaMainWindow) frame;
        m_client = mainPanel.getClient();
        m_stringLocale = mainPanel.getStringsLocale();
        initGUI();
        updateRooms();
        this.requestFocus();
    }

    /**
	 * 
	 */
    private void updateRooms() {
        m_alistRoomNames = new ArrayList<String>();
        Iterator it = mainPanel.getClient().getRoomsIterator();
        DefaultListModel rooms = new DefaultListModel();
        if (it != null) {
            while (it.hasNext()) {
                TaironaChatRoomTemplate tmpRoom = (TaironaChatRoomTemplate) it.next();
                rooms.addElement(new String(tmpRoom.getName() + " (" + tmpRoom.getCreatorName() + ")"));
                m_alistRoomNames.add(new String(tmpRoom.getName()));
            }
        }
        jListRooms.setModel(rooms);
    }

    /**
	 * 
	 */
    private void initGUI() {
        try {
            getContentPane().setBackground(new java.awt.Color(0, 128, 192));
            setTitle(m_stringLocale.getString(this, "chatrooms"));
            setModal(true);
            setResizable(false);
            getContentPane().setLayout(null);
            setName("ChatRoomsDialog");
            {
                jPanelButtons = new JPanel();
                getContentPane().add(jPanelButtons);
                jPanelButtons.setBackground(new java.awt.Color(0, 128, 192));
                jPanelButtons.setBounds(252, 0, 112, 140);
                {
                    jButtonEnter = new JButton();
                    jPanelButtons.add(jButtonEnter);
                    jButtonEnter.setText(m_stringLocale.getString(this, "enterroom"));
                    jButtonEnter.setBounds(280, 14, 91, 21);
                    jButtonEnter.setPreferredSize(new java.awt.Dimension(112, 21));
                    jButtonEnter.addActionListener(this);
                }
                {
                    jButtonLeave = new JButton();
                    jPanelButtons.add(jButtonLeave);
                    jButtonLeave.setText(m_stringLocale.getString(this, "roominfo"));
                    jButtonLeave.setBounds(284, 41, 84, 22);
                    jButtonLeave.setSize(112, 21);
                    jButtonLeave.setPreferredSize(new java.awt.Dimension(112, 21));
                    jButtonLeave.addActionListener(this);
                }
                {
                    jButtonCreateRoom = new JButton();
                    jPanelButtons.add(jButtonCreateRoom);
                    jButtonCreateRoom.setText(m_stringLocale.getString(this, "createroom"));
                    jButtonCreateRoom.setBounds(259, 70, 112, 21);
                    jButtonCreateRoom.setPreferredSize(new java.awt.Dimension(112, 21));
                    jButtonCreateRoom.addActionListener(this);
                }
                {
                    jButtonUpdate = new JButton();
                    jPanelButtons.add(jButtonUpdate);
                    jButtonUpdate.setText(m_stringLocale.getString(this, "updatelist"));
                    jButtonUpdate.setBounds(259, 98, 112, 21);
                    jButtonUpdate.setPreferredSize(new java.awt.Dimension(112, 21));
                    jButtonUpdate.addActionListener(this);
                }
                {
                    JButtonExit = new JButton();
                    jPanelButtons.add(JButtonExit);
                    JButtonExit.setText(m_stringLocale.getString(null, "dismiss"));
                    JButtonExit.setBounds(259, 126, 112, 21);
                    JButtonExit.setPreferredSize(new java.awt.Dimension(112, 21));
                    JButtonExit.addActionListener(this);
                }
            }
            {
                jScrolllRooms = new JScrollPane();
                getContentPane().add(jScrolllRooms);
                jScrolllRooms.setBounds(7, 7, 231, 161);
                jScrolllRooms.setAutoscrolls(true);
                {
                    DefaultListModel jListModel = new DefaultListModel();
                    if (rooms != null) {
                        for (int i = 0; i < rooms.length; i++) jListModel.addElement(rooms[i]);
                    }
                    jListRooms = new JList();
                    jScrolllRooms.setViewportView(jListRooms);
                    jListRooms.setBounds(7, 7, 49, 154);
                    jListRooms.setModel(jListModel);
                    jListRooms.addListSelectionListener(this);
                }
            }
            this.setBounds(mainPanel.getX() + (mainPanel.getWidth() - 379) / 2, mainPanel.getY() + (mainPanel.getHeight() - 209) / 2, 379, 209);
            this.setSize(379, 209);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent E) {
        if (E.getSource() == this.jButtonEnter) {
            System.out.println("Entering room...");
            goEnter();
            mainPanel.roomOwner(false);
            dispose();
            return;
        }
        if (E.getSource() == this.jButtonLeave) {
            showInfo();
            return;
        }
        if (E.getSource() == this.jButtonCreateRoom) {
            CreateRoomDialog crd = new CreateRoomDialog(mainPanel);
            crd.setVisible(true);
            dispose();
            return;
        }
        if (E.getSource() == this.jButtonUpdate) {
            updateRooms();
        }
        if (E.getSource() == this.JButtonExit) {
            mainPanel.disposeRoomsDialog();
            dispose();
        }
    }

    /**
	 * Gather all of the information we have about this room,
	 * construct a big String to contain it, and display it
	 * in a dialog box
	 */
    protected void showInfo() {
        String infoString = "";
        int index = jListRooms.getSelectedIndex();
        System.out.println("index : " + (String) jListRooms.getSelectedValue());
        if (index >= 0) {
            TaironaChatRoomTemplate selectedRoom = mainPanel.getClient().getRoom((String) m_alistRoomNames.get(index));
            infoString += m_stringLocale.getString(this, "roomname") + " " + selectedRoom.getName() + "\n" + m_stringLocale.getString(this, "creator") + " " + selectedRoom.getCreatorName() + "\n" + m_stringLocale.getString(this, "private") + " ";
            if (selectedRoom instanceof TaironaChatRoomPrivate) {
                infoString += m_stringLocale.getString(this, "yes") + "\n" + m_stringLocale.getString(this, "invited") + " ";
                if (((TaironaChatRoomPrivate) selectedRoom).isInvited()) infoString += m_stringLocale.getString(this, "yes"); else infoString += m_stringLocale.getString(this, "no");
            } else infoString += m_stringLocale.getString(this, "no");
            infoString += "\n" + m_stringLocale.getString(this, "users") + " " + selectedRoom.getNumberOfUsers() + "\n\n";
            Iterator it = selectedRoom.getUserIterator();
            while (it.hasNext()) {
                infoString += (String) it.next();
                if (it.hasNext()) infoString += ", ";
            }
            Object[] opts = { m_stringLocale.getString(null, "dismiss") };
            JOptionPane.showOptionDialog(this, infoString, m_stringLocale.getString(this, "information"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opts, opts[0]);
        }
    }

    /**
	 *  The user wants to join the selected chat room. Send a message to the server
	 */
    protected void goEnter() {
        int index = jListRooms.getSelectedIndex();
        System.out.println("Trying to enter room " + index);
        if (index >= 0) {
            TaironaChatRoomTemplate selectedRoom = mainPanel.getClient().getRoom((String) m_alistRoomNames.get(index));
            if (mainPanel.getClient().getMe().getChatroom().getName().equals(selectedRoom.getName())) {
                Object[] opts = { "OK" };
                JOptionPane.showOptionDialog(this, m_stringLocale.getString(this, "alreadyinchat"), "Tairona", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opts, opts[0]);
                return;
            }
            String password = "";
            try {
                if ((selectedRoom instanceof TaironaChatRoomPrivate) && !((TaironaChatRoomPrivate) selectedRoom).isInvited()) {
                    password = JOptionPane.showInputDialog(null, m_stringLocale.getString(null, "enterpassword"));
                    m_client.sendEnterRoom(selectedRoom.getName(), true, password);
                } else m_client.sendEnterRoom(selectedRoom.getName(), false, password);
            } catch (IOException e) {
                mainPanel.getClient().lostConnection();
                return;
            }
        } else {
            System.out.println("Selected index is not valid!!");
        }
    }

    public void valueChanged(ListSelectionEvent E) {
        if (E.getSource() == jListRooms) {
            boolean somethingSelected = (jListRooms.getSelectedIndices().length > 0);
            jButtonEnter.setEnabled(somethingSelected);
            jButtonLeave.setEnabled(somethingSelected);
        }
    }

    public void update(Observable arg0, Object arg1) {
        updateRooms();
    }
}
