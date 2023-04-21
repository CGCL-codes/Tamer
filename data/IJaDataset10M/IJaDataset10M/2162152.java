package business.comandi;

import java.util.HashMap;
import domain.AbstractOggettoEntita;
import domain.wrapper.IWrapperEntity;

public abstract class AbstractCommand implements ICommand {

    protected AbstractOggettoEntita entita;

    protected IWrapperEntity wrap;

    protected HashMap<String, AbstractOggettoEntita> mappaCache;

    @Override
    public boolean doCommand() {
        if (execute()) {
            scriviLogExecute(true);
            return true;
        } else {
            scriviLogExecute(false);
            return false;
        }
    }

    @Override
    public boolean undoCommand() {
        if (unExecute()) {
            scriviLogUnExecute(true);
            return true;
        } else {
            scriviLogUnExecute(false);
            return false;
        }
    }

    @Override
    public abstract boolean execute();

    @Override
    public abstract boolean unExecute();

    @Override
    public abstract void scriviLogExecute(boolean isComandoEseguito);

    @Override
    public abstract void scriviLogUnExecute(boolean isComandoEseguito);
}
