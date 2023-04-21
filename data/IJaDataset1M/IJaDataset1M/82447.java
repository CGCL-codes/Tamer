package ru.nsu.ccfit.pm.econ.engine.events;

import ru.nsu.ccfit.pm.econ.common.engine.events.IUGameEvent;
import ru.nsu.ccfit.pm.econ.engine.roles.BankEngine;

/**
 * Internal engine event. Used when new bank is added
 * 
 * @author pupatenko
 * 
 */
public class NewBankEngineEvent extends GameEventEngine implements IUGameEvent {

    private BankEngine bank;

    public NewBankEngineEvent(BankEngine bank) {
        super();
        this.bank = bank;
    }

    public BankEngine getBank() {
        return bank;
    }
}
