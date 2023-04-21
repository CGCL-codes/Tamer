package de.alexanderwilden.jatoli.events;

import java.util.ArrayList;
import java.util.List;
import de.alexanderwilden.jatoli.JatoliUtils;

public class ChatUpdateBuddyEvent extends JatoliEvent {

    private String chatRoomId;

    private boolean inside;

    private List<String> users;

    ChatUpdateBuddyEvent(String rawString) {
        super(rawString);
        StringBuffer buffer = new StringBuffer(rawString);
        chatRoomId = JatoliUtils.nextElement(buffer);
        inside = (JatoliUtils.nextElement(buffer).equals("T") ? true : false);
        String temp = JatoliUtils.nextElement(buffer);
        users = new ArrayList<String>();
        while (temp.length() > 0) {
            users.add(temp);
            temp = JatoliUtils.nextElement(buffer);
        }
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public boolean isInside() {
        return inside;
    }

    public List<String> getUsers() {
        return users;
    }

    @Override
    public String getEventCommand() {
        return "CHAT_UPDATE_BUDDY";
    }
}
