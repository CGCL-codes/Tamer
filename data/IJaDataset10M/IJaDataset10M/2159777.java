package net.alteiar.serveur;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import net.alteiar.app.client.Client;
import net.alteiar.shared.StringUtils;
import net.alteiar.shared.Worker;
import net.alteiar.shared.message.Message;
import net.alteiar.shared.message.MessageAskJoueurs;
import net.alteiar.shared.message.MessageDeconnexion;
import net.alteiar.shared.message.MessageType;

public class ServerTransfertWorker extends Worker {

    private final Client soc;

    private final Server serv;

    public ServerTransfertWorker(Server serveur, Client client) {
        soc = client;
        serv = serveur;
    }

    @Override
    public void doAction() throws Exception {
        try {
            Message msg = soc.receiveMessage();
            if (msg.containtDestinataire(Message.DEST_TO_EVERYONE)) {
                msg.clearDestinataire();
                Set<String> allUser = serv.getAllUser();
                Iterator<String> it = allUser.iterator();
                while (it.hasNext()) {
                    String nom = it.next();
                    msg.addDestinataire(nom);
                }
            }
            if (msg.containtDestinataire(Message.DEST_TO_SERVER)) {
                switch(msg.getObjet()) {
                    case OBJET_INFO_USER:
                        Message message = new Message(Message.DEST_TO_SERVER, soc.getNom(), MessageType.OBJET_INFO_USER, StringUtils.EMPTY);
                        sendMessageTo(soc, message);
                        break;
                    case OBJET_DISCONNECT:
                        soc.sendMessage(new MessageDeconnexion());
                        serv.removeUser(soc.getNom());
                        finish();
                        break;
                    case OBJET_UPDATE:
                        serv.sendMessageToAll(msg);
                        break;
                    default:
                        break;
                }
            } else {
                switch(msg.getObjet()) {
                    case OBJECT_FILENAME:
                        for (String destinataire : msg.getDestinataire()) {
                            Client dest = serv.get(destinataire);
                            sendMessageTo(dest, msg);
                        }
                        File tmp = new File("file.tmp");
                        soc.receiveFile(tmp);
                        for (String destinataire : msg.getDestinataire()) {
                            Client dest = serv.get(destinataire);
                            dest.sendFile(tmp);
                        }
                        tmp.delete();
                        break;
                    case OBJECT_IMAGE:
                        for (String destinataire : msg.getDestinataire()) {
                            Client dest = serv.get(destinataire);
                            sendMessageTo(dest, msg);
                        }
                        BufferedImage img = soc.receiveBuffered();
                        for (String destinataire : msg.getDestinataire()) {
                            Client dest = serv.get(destinataire);
                            dest.sendImage(img);
                        }
                        break;
                    default:
                        for (String destinataire : msg.getDestinataire()) {
                            Client dest = serv.get(destinataire);
                            if (dest == null) {
                                Message returnMsg = new Message(Message.DEST_UNKNOW_DEST, msg.getMessage());
                                returnMsg.addDestinataire(destinataire);
                                sendMessageTo(soc, returnMsg);
                            } else {
                                sendMessageTo(dest, msg);
                            }
                        }
                }
            }
        } catch (IOException ex) {
            serv.removeUser(soc.getNom());
            finish();
        }
    }

    public void sendMessageTo(Client destinataire, Message msg) throws IOException {
        switch(msg.getObjet()) {
            case OBJET_INFO_USER:
                StringBuilder message = new StringBuilder(msg.getMessage());
                for (String str : serv.getAllUser()) {
                    message.append(str);
                    message.append(MessageAskJoueurs.JOUEUR_SEPARATOR);
                }
                msg.setMessage(message.toString());
                destinataire.sendMessage(msg);
                break;
            default:
                destinataire.sendMessage(msg);
                break;
        }
    }
}
