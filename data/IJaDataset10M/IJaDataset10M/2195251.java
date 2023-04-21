package com.weespers.remote;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Display;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;
import com.weespers.ui.player.actions.PauseAction;
import com.weespers.ui.player.actions.PlayAction;
import com.weespers.ui.player.actions.StopAction;
import com.weespers.ui.playlist.actions.PlayNextItemAction;

public class RemoteControl implements IntellitypeListener {

    public static RemoteControl INSTANCE = new RemoteControl();

    private RemoteControl() {
        configure();
    }

    private static int NEXT = 4145;

    private static int PLAY = 4142;

    private static int PAUSE = 4143;

    private static int STOP = 13;

    public void configure() {
        try {
            JIntellitype.getInstance();
            JIntellitype.getInstance().addIntellitypeListener(this);
        } catch (Exception ex) {
        }
    }

    @Override
    public void onIntellitype(int code) {
        if (code == NEXT) {
            run(PlayNextItemAction.INSTANCE);
        } else if (code == PLAY) {
            run(new PlayAction());
        } else if (code == PAUSE) {
            run(new PauseAction());
        } else if (code == STOP) {
            run(new StopAction());
        } else {
            System.err.println("Pressed ->" + code);
        }
    }

    protected void run(final IAction action) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                System.err.println("Running ->" + action);
                action.run();
            }
        });
    }
}
