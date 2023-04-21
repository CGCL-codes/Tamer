package base;

import java.io.Serializable;
import config.CONSTANTS;
import game.enums.E_ACTION;
import interfaces.IGetActionFunctor;
import interfaces.IInteractable;

public class DefaultActionFunctor implements IGetActionFunctor, Serializable {

    private static final long serialVersionUID = -5018125792379765565L;

    @Override
    public E_ACTION getAction(IInteractable theOther_) {
        return CONSTANTS.DEFAULT_ACTION;
    }
}
