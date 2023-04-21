package module;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import messages.Message;
import communication.ClientInfo;
import communication.Roles;
import elements.AttributeSet;
import elements.Element;
import game.Game;

public class LogicModule extends Module {

    private static final int TICKRATE = 100;

    private static final int TICKPERIOD = 1;

    private static final ModuleType[] DEFAULTTYPES = { ModuleType.Logic };

    public LogicModule(Game game, ComModule com) {
        super(game, com, DEFAULTTYPES);
        start();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    public void dispose() {
        if (!isdisposed()) {
            super.dispose();
        }
    }

    public void clientLost(ClientInfo cinfos) {
    }

    @Override
    protected void print(String p) {
        super.print("Logic module> " + p);
    }

    @Override
    protected void printerr(String p) {
        super.print("Logic module> " + p);
    }

    @Override
    protected void printwarn(String p) {
        super.printwarn("Logic module> " + p);
    }

    public void run() {
        setMaxSleepRepeats(TICKRATE * TICKPERIOD, 1000 * TICKPERIOD);
        while (running.get()) {
            try {
                if (messages.hasInMessages()) processMessage(messages.inPollAll());
            } catch (Exception e) {
                printerr(e.toString());
                e.printStackTrace();
            }
            game.elementModule.updateAllElements();
            game.elementModule.sendAllTickMessages();
            sleeprepeats();
        }
    }

    public void sendMessage(Message<LogicModule> msg) {
        messages.writeMessage(msg);
    }

    public void processUpdateTickMessage(LinkedList<Element> addedElements, Hashtable<Integer, AttributeSet> changedElementAttributes) {
        Iterator<Element> addedElementIter = addedElements.iterator();
        while (addedElementIter.hasNext()) {
            Element ele = addedElementIter.next();
            game.elementModule.receiveObject(ele);
            if (Game.DEBUG) Game.print("New element");
        }
        Enumeration<Integer> elementIDs = changedElementAttributes.keys();
        while (elementIDs.hasMoreElements()) {
            Integer eleId = elementIDs.nextElement();
            Element ele = game.elementModule.getObject(eleId);
            ele.importAttributeSet(changedElementAttributes.get(eleId));
            if (Game.DEBUG) Game.print("Update Attributes");
        }
    }
}
