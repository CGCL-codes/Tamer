package tk.bot;

import java.util.*;

public class BotUnknownType extends BotBasicType implements BotCommandType {

    TKBot parent;

    static BotCommandInterpreter[] interpreters;

    public BotUnknownType(TKBot parent) {
        this.parent = parent;
    }

    public void process(String info) {
        super.process(info);
    }

    public String getCommandID() {
        return "";
    }

    public TKBot getBot() {
        return parent;
    }

    public BotCommandType addBotCommandInterpreter(BotCommandInterpreter interpreter) {
        return super.addBotCommandInterpreter(interpreter);
    }

    public void install(TKBot bot) {
        bot.getFilter().setDefault(this);
    }

    public void remove(TKBot bot) {
        bot.getFilter().setDefault(null);
    }

    public BotCommandInterpreter[] getInterpreters() {
        return interpreters;
    }

    public void setInterpreters(BotCommandInterpreter[] interpreters) {
        this.interpreters = interpreters;
    }
}
