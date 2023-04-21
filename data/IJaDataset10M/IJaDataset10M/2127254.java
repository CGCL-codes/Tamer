package saf.ui;

import saf.common.ActionType;
import saf.common.MoveType;

public interface IFighter {

    public int getPosition();

    public int getHealth();

    public int getMaxHealth();

    public MoveType getMoveState();

    public ActionType getActionState();

    public String getName();
}
