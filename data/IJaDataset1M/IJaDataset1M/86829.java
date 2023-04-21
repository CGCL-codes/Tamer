package tk.bot;

public class BotTalkType extends BotBasicType implements BotCommandType {

    TKBot parent;

    static BotCommandInterpreter[] interpreters;

    public BotTalkType(TKBot parent) {
        this.parent = parent;
    }

    public void process(String info) {
        super.process(info);
    }

    public String getCommandID() {
        return "1005";
    }

    public TKBot getBot() {
        return parent;
    }

    public BotCommandInterpreter[] getInterpreters() {
        return interpreters;
    }

    public void setInterpreters(BotCommandInterpreter[] interpreters) {
        this.interpreters = interpreters;
    }
}
