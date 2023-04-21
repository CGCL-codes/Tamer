package com.gampire.pc.action;

import java.rmi.RemoteException;
import java.util.concurrent.Future;
import com.gampire.pc.control.BattleFieldController;
import com.gampire.pc.control.BattleFieldControllerRemote;

public class StopGameActionRemote extends GameActionRemote {

    private final BattleFieldControllerRemote battleFieldControllerRemote;

    public StopGameActionRemote(BattleFieldController battleFieldController) {
        battleFieldControllerRemote = battleFieldController.getBattleFieldControllerRemote();
    }

    @Override
    public Future<?> submit() {
        stopAll();
        return super.submit();
    }

    @Override
    public void run() {
        try {
            battleFieldControllerRemote.stopGameRemote();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
