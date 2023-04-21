package org.ss.mobot.core.backend.plugin;

import org.ss.mobot.core.BotMessage;
import org.ss.mobot.core.frontend.IMThreadInterface;

public abstract class AbstractMPT implements Runnable {

    private final IMThreadInterface bot;

    private BotMessage msgObj;

    public AbstractMPT(IMThreadInterface bot, BotMessage msgObj) {
        this.bot = bot;
        this.msgObj = msgObj;
    }

    public IMThreadInterface getBot() {
        return bot;
    }

    public BotMessage getMsgObj() {
        return msgObj;
    }
}
