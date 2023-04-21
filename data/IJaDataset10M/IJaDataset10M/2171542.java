package pcgen.cdom.actor;

import pcgen.cdom.base.ChooseActor;
import pcgen.cdom.base.ConcretePrereqObject;
import pcgen.cdom.base.LSTWriteable;
import pcgen.core.PlayerCharacter;

public class RemoveActor extends ConcretePrereqObject implements LSTWriteable, ChooseActor {

    public void execute(PlayerCharacter pc) {
    }

    public String getLSTformat() {
        return null;
    }

    @Override
    public int hashCode() {
        return -30;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RemoveActor;
    }
}
