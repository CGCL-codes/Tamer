package messages;

public class AndMessage extends Message {

    public AndMessage(String botName, String type) {
        super(botName, type);
        System.out.println("Two or more conditions for BOT " + botName + " have to do with " + type);
    }
}
