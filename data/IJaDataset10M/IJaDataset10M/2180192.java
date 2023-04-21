package com.gampire.pc.action;

import java.rmi.RemoteException;
import com.gampire.pc.control.BattleFieldController;
import com.gampire.pc.control.BattleFieldControllerRemote;
import com.gampire.pc.model.BattleField;

public class StartGameActionRemote extends GameActionRemote {

    private final BattleFieldController battleFieldController;

    private final BattleFieldControllerRemote battleFieldControllerRemote;

    private final BattleField battleField;

    public StartGameActionRemote(BattleFieldController battleFieldController, BattleField battleField) {
        this.battleFieldController = battleFieldController;
        this.battleFieldControllerRemote = battleFieldController.getBattleFieldControllerRemote();
        this.battleField = battleField;
    }

    @Override
    public void run() {
        if (battleFieldControllerRemote != null) {
            try {
                battleFieldControllerRemote.startGameRemote(battleField.getBattleFieldBean());
            } catch (RemoteException e) {
                battleFieldController.setBattleFieldControllerRemote(null);
            }
        }
    }
}
