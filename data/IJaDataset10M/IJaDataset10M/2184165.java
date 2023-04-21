package br.usp.ime.dojo.server;

import java.util.Set;
import br.usp.ime.dojo.client.ChatService;
import br.usp.ime.dojo.core.repositories.ChatRepository;
import br.usp.ime.dojo.core.repositories.RepositoryFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ChatServiceImpl extends RemoteServiceServlet implements ChatService {

    private ChatRepository chatRepository;

    public ChatServiceImpl() {
        chatRepository = RepositoryFactory.createChatRepository();
    }

    public void createChatRoom(String room) {
        chatRepository.createChatRoom(room);
        chatRepository.addUser("System", room);
    }

    public String checkNewMessages(String user, String room) {
        String result = "";
        int position = 0;
        if (chatRepository.containsUser(user, room)) position = chatRepository.getPositionUser(user, room); else return result;
        result = chatRepository.updateMessages(position, room);
        chatRepository.updateUser(user, room);
        return result;
    }

    public void sendMessage(String user, String room, String message) {
        if (chatRepository.containsUser(user, room)) {
            if (!message.equals("")) chatRepository.addMessage(user, room, message);
        }
    }

    public void registerUser(String user, String room) {
        if (!chatRepository.containsUser(user, room)) {
            chatRepository.addUser(user, room);
            chatRepository.addMessage("System", room, user + " has entered the room");
        }
    }

    public Set<String> getUsers(String room) {
        return chatRepository.getUsers(room);
    }

    public void unregisterUser(String user, String room) {
        if (chatRepository.containsUser(user, room)) {
            chatRepository.removeUser(user, room);
            chatRepository.addMessage("System", room, user + " has exited the room");
        }
    }
}
