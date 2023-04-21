package name.huzhenbo.java.patterns.responsibilitychain;

class NaiveButton implements HelpHandler {

    private HelpHandler successor;

    public NaiveButton(HelpHandler successor) {
        this.successor = successor;
    }

    public boolean hasHelp(Topic topic) {
        return Topic.BUTTON_TOPIC == topic;
    }

    public String handleHelp(Topic topic) {
        if (hasHelp(topic)) {
            return "button help";
        } else {
            return successor.handleHelp(topic);
        }
    }
}
