package pokeglobal.client.ui.hud;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import pokeglobal.client.network.PacketGenerator;
import pokeglobal.client.ui.base.Button;
import pokeglobal.client.ui.base.Frame;
import pokeglobal.client.ui.base.ScrollPane;
import pokeglobal.client.ui.base.TextArea;
import pokeglobal.client.ui.base.TextField;
import pokeglobal.client.ui.base.event.ActionEvent;
import pokeglobal.client.ui.base.event.ActionListener;
import pokeglobal.client.ui.base.event.MouseAdapter;
import pokeglobal.client.ui.base.event.MouseEvent;

public class ChatWindow extends Frame {

    static final long serialVersionUID = 8126828445828668638L;

    /**
	 * Auto-generated main method to display this JFrame
	 */
    private PacketGenerator packetGen;

    private TextArea chatList;

    private TextField chatType;

    private Font dpFont;

    public TextField getChatBox() {
        return chatType;
    }

    public ChatWindow(PacketGenerator out) {
        super();
        packetGen = out;
        initGUI();
    }

    private void initGUI() {
        this.setMinimumSize(206, 200);
        try {
            dpFont = new AngelCodeFont("pokeglobal/client/res/fonts/dp-small.fnt", "pokeglobal/client/res/fonts/dp-small.png");
            setTitle("Chat");
            this.setBackground(new Color(0, 0, 0, 85));
            this.setForeground(new Color(255, 255, 255));
            {
                chatList = new TextArea();
                chatList.setSize(380, 250);
                chatList.setBackground(new Color(0, 0, 0, 20));
                chatList.setForeground(new Color(255, 255, 255));
                chatList.setBorderRendered(false);
                chatList.setEditable(false);
                chatList.setFont(dpFont);
                getContentPane().add(chatList);
            }
            {
                chatType = new TextField();
                chatType.setName("chatType");
                chatType.setSize(350, 25);
                chatType.setLocation(50, 250);
                getContentPane().add(chatType);
                chatType.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        chatTypeActionPerformed(evt);
                    }
                });
            }
            this.getResizer().addMouseListener(new MouseAdapter() {

                public void mouseDragged(MouseEvent event) {
                    repositionUI();
                }
            });
            setSize(206, 500);
            repositionUI();
            chatType.grabFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chatTypeActionPerformed(ActionEvent evt) {
        if (chatType.getText() != null && chatType.getText().length() != 0) {
            packetGen.write("C" + chatType.getText() + "\r");
        }
        chatType.setText("");
        chatType.grabFocus();
    }

    public void appendText(String newChat) {
        int endex = newChat.indexOf(">");
        newChat = newChat.substring(0, endex + 1) + newChat.substring(endex + 1).replace(">", " is greater than ").replace("<", " is less than ");
        if (!chatList.getText().equals("")) chatList.setText(chatList.getText() + "\n" + newChat); else chatList.setText(newChat);
        chatList.setCaretPosition(chatList.getText().length());
        checkChatWindow();
    }

    private void repositionUI() {
        chatList.setWidth((int) getWidth() - 8);
        chatType.setLocation(0, (int) getHeight() - 50);
        chatType.setSize((int) getWidth(), 25);
        checkChatWindow();
    }

    private void checkChatWindow() {
        try {
            if (chatList.getLineCount() >= ((int) getHeight() - 48) / dpFont.getLineHeight()) {
                String[] s = chatList.getLinesAsText();
                chatList.setCaretPosition(0);
                chatList.setText("");
                int adj = (int) ((s.length - (getHeight() - getTitleBar().getHeight() - chatType.getHeight()) / dpFont.getLineHeight()) + 1);
                for (int i = adj; i < s.length; i++) {
                    chatList.setText(chatList.getText() + s[i]);
                    chatList.setCaretPosition(chatList.getText().length());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPacketGenerator(PacketGenerator p) {
        packetGen = p;
    }
}
