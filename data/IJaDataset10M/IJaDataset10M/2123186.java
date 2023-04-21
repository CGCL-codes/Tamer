package com.jot.system.client;

import java.util.Random;
import com.jot.system.callbacks.JSexe;
import com.jot.system.callbacks.JSproc;
import com.jot.system.entities.ImMessageList;
import com.jot.system.entities.User;
import com.jot.system.entities.Widget;
import com.jot.system.pjson.Entity;
import com.jot.system.pjson.Guid;
import com.jot.system.utils.Crypto;
import com.jot.system.utils.EndTimer;

/**
 * 
 * @author alanwootton
 * 
 */
public class ClientScriptSetupUser extends JSproc {

    public ClientScriptSetupUser(int sizeHint) {
        this.sizeHint = sizeHint;
        int totalSavesPerSecond = 250;
        if (sizeHint < 1000) {
            totalSavesPerSecond = totalSavesPerSecond / 4;
        }
        loadingLinksDelay = (sizeHint * 1000) / totalSavesPerSecond;
        if (loadingLinksDelay > 150 * 1000) loadingLinksDelay = 150000;
        init();
    }

    int sizeHint = 100;

    EndTimer timer;

    String message;

    int tmpi;

    User newUser;

    EndTimer loopTimer;

    int loadingLinksDelay = 1000;

    int loadLinksIndex = 0;

    Entity loadingLink = null;

    ClientScriptSetupUser.E createRandomUser;

    ClientScriptSetupUser.E loadAllMessages;

    static final int userCount = 1000 * 1000;

    static final int threadCount = 500 * 1000;

    static final int widgetCount = 250 * 1000;

    static final int linkPerWidget = 10;

    static Guid getNGuid(int i, int max, String comment) {
        i = Math.abs(i);
        i = i % max;
        String s = Crypto.SHA1_Base64((comment + i).getBytes());
        s = s.substring(0, 15) + comment + i;
        return new Guid(s);
    }

    static Guid getUserGuid(int i) {
        return getNGuid(i, userCount, "User");
    }

    static Guid getThreadGuid(int i) {
        return getNGuid(i, threadCount, "ImTh");
    }

    static Guid getWidgetGuid(int i) {
        return getNGuid(i, widgetCount, "Wid");
    }

    static Widget makeWidget(int i) {
        Widget w = new Widget();
        w.guid = getWidgetGuid(i);
        Random rand = new Random(i);
        int count = linkPerWidget - (linkPerWidget / 2) + rand.nextInt(linkPerWidget / 2);
        for (int j = 0; j < count; j++) {
            ImMessageList list = new ImMessageList();
            list.guid = getThreadGuid(rand.nextInt());
            list.isStub = true;
            w.add(list);
        }
        return w;
    }

    public Random rand = new Random();

    protected void init() {
        createRandomUser = new E() {

            public void run() {
                tmpi = rand.nextInt();
                Guid tmpGuid = getUserGuid(tmpi);
                newUser = new User();
                newUser.guid = tmpGuid;
                newUser.root = makeWidget(tmpi);
                CALL(new LoadOrSave(newUser));
            }
        };
        new E() {

            public void run() {
                newUser = (User) returnValue;
                runner.api.setUser(newUser);
                newUser.root = makeWidget(tmpi);
                CALL(new LoadOrSave(newUser.root));
            }
        };
        new E() {

            public void run() {
                newUser.root = (Widget) returnValue;
                loadLinksIndex = 0;
                ;
            }
        };
        loadAllMessages = new E() {

            public void run() {
            }
        };
        new E() {

            public void run() {
            }
        };
        new E() {

            public void run() {
                if (!loopTimer.done()) {
                    RETRY(loadingLinksDelay);
                    return;
                }
                GOTO(loadAllMessages);
            }
        };
        new E() {

            public void run() {
                System.out.println("did we fall off the end?????");
                RETURN(null);
            }
        };
    }

    abstract class E extends JSexe {

        E() {
            super(getScript());
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < widgetCount; i++) {
            Widget w = makeWidget(i);
            if (w.links.size() <= 10) System.out.println("err on " + i);
        }
    }
}
