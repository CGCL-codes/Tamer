package com.eric.mss.client.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;
import com.eric.mss.client.view.MSSMSLoginFrameMediator;
import com.eric.mss.client.view.MSSMSMainFrameMediator;
import com.eric.mss.client.view.MSSMSMediator;

public class PrepViewCommand extends SimpleCommand {

    @Override
    public void execute(INotification notification) {
        facade.registerMediator(new MSSMSMediator());
        facade.registerMediator(new MSSMSLoginFrameMediator());
        facade.registerMediator(new MSSMSMainFrameMediator());
    }
}
