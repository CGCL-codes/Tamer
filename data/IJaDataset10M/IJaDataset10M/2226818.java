package scrum.server.collaboration;

import ilarkesto.base.time.DateAndTime;
import scrum.server.project.Project;

public class ChatMessageDao extends GChatMessageDao {

    public ChatMessage postChatMessage(Project project, String text) {
        return postChatMessage(project, text, DateAndTime.now());
    }

    public ChatMessage postChatMessage(Project project, String text, DateAndTime dateAndTime) {
        ChatMessage msg = newEntityInstance();
        msg.setProject(project);
        msg.setDateAndTime(dateAndTime);
        msg.setText(text);
        saveEntity(msg);
        return msg;
    }

    @Override
    public void ensureIntegrity() {
        super.ensureIntegrity();
        for (ChatMessage message : getEntities()) {
            deleteEntity(message);
        }
    }
}
