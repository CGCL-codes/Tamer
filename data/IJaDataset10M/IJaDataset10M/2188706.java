package game.conversation;

import java.util.List;
import ui.interfaces.IMetaData;

public class ConversationChoice implements IMetaData {

    private final List<String> options;

    public ConversationChoice(List<String> options) {
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }
}
