package net.emtg.doing.main;

import java.io.IOException;
import java.util.Hashtable;
import javax.microedition.midlet.MIDlet;
import net.emtg.doing.pomodoro.PIMTaskManager;
import net.emtg.doing.pomodoro.Pomodoro;
import net.emtg.doing.pomodoro.TaskManager;
import net.emtg.doing.time.SequenceTimer;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;

/**
 * @author emiguel
 */
public class Doing extends MIDlet {

    private static Doing instance;

    private static final String RESOURCE = "/theme.res";

    public static final String TITLE = "Doing!";

    private Pomodoro pomodoro;

    private TaskManager taskManager;

    DoingManager doingManager;

    public Doing() {
        super();
        instance = this;
    }

    public void startApp() {
        try {
            Display.init(this);
            Form splash = new Form();
            splash.getStyle().setBgImage(Image.createImage("/icons/splash.png"));
            splash.show();
            final Resources r = Resources.open(RESOURCE);
            Display.getInstance().callSerially(new Runnable() {

                public void run() {
                    try {
                        try {
                            taskManager = new PIMTaskManager();
                            taskManager.loadTasks();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pomodoro = new Pomodoro(taskManager, SequenceTimer.getInstance());
                        initForm(r);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Dialog.show("error", ex.toString(), "ok", null);
                        destroyApp(true);
                    }
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        taskManager.finalization();
        notifyDestroyed();
    }

    private void initForm(Resources r) throws Exception {
        Hashtable hashL10N = r.getL10N("en-us", "en-us");
        UIManager.getInstance().setThemeProps(r.getTheme("Theme2"));
        UIManager.getInstance().setResourceBundle(hashL10N);
        doingManager = new DoingManager(pomodoro);
        doingManager.start();
    }

    public static Doing getInstance() {
        return instance;
    }

    public static String getI18NLabel(String str) {
        return UIManager.getInstance().localize(str, str);
    }
}
