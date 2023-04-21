package net.alteiar.gestionInitiative.communication;

import java.io.IOException;
import java.util.ArrayList;
import net.alteiar.app.client.Client;
import net.alteiar.app.client.ClientReceiverWorker;
import net.alteiar.app.client.MessageReceiver;
import net.alteiar.shared.ExceptionTool;
import net.alteiar.shared.message.Message;
import net.alteiar.shared.message.MessageNewJoueur;
import net.alteiar.shared.message.MessageType;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ClientJoueur extends Client implements MessageReceiver {

    private final boolean m_isServeur;

    private final boolean m_isMJ;

    private final ArrayList<MessageReceiver> m_messageReceivers;

    private final ArrayList<MyMessageReceiver> m_myMessageReceivers;

    public ClientJoueur(String addr, int port, String nom, boolean isServeur, boolean isMj) throws IOException {
        super(addr, port, nom);
        m_isServeur = isServeur;
        Thread receive = new Thread(new ClientReceiverWorker(this, this));
        receive.start();
        try {
            this.sendVerifyMessage(new MessageNewJoueur(this.getNom() + " vient de se connecter"));
        } catch (IOException e) {
            ExceptionTool.showError(e);
        }
        this.m_isMJ = isMj;
        m_messageReceivers = new ArrayList<MessageReceiver>();
        m_myMessageReceivers = new ArrayList<MyMessageReceiver>();
    }

    public void sendJoueurObject(int type, JoueurObjectList<?> object) throws IOException {
        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        sendMyMessage(type, sortie.outputString(object.toXml()));
    }

    public void sendMyMessage(int type, String message) throws IOException {
        Message msg = new Message();
        msg.setObjet(MessageType.OBJET_MESSAGE);
        msg.addDestinataire(Message.DEST_TO_EVERYONE);
        MyMessage mes = new MyMessage(type, message);
        msg.setMessage(mes.toString());
        this.sendVerifyMessage(msg);
    }

    public void addMessageReceiver(MessageReceiver receiver) {
        m_messageReceivers.add(receiver);
    }

    public void addMyMessageReceiver(MyMessageReceiver receiver) {
        m_myMessageReceivers.add(receiver);
    }

    @Override
    public void receiveMessage(Message msg) {
        switch(msg.getObjet()) {
            case OBJET_UPDATE:
                break;
            case OBJET_MESSAGE:
                MyMessage message = MyMessage.valueOf(msg.getMessage());
                for (MyMessageReceiver receiver : m_myMessageReceivers) {
                    receiver.receive(message);
                }
                break;
        }
        for (MessageReceiver receiver : m_messageReceivers) {
            receiver.receiveMessage(msg);
        }
    }

    public boolean isMJ() {
        return m_isMJ;
    }

    public boolean isServeur() {
        return m_isServeur;
    }
}
