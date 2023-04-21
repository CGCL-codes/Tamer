package net.sf.jimo.modules.bot.impl;

import java.util.Dictionary;
import net.sf.jimo.modules.im.api.log.LogUtil;

public class ChatBot {

    protected String name = this.getClass().getName();

    public void listen(Dictionary properties) {
    }

    ;

    public final void print() {
        LogUtil.console(this.name + " loaded!");
    }

    ;
}
