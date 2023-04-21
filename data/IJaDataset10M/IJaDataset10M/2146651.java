package hotciv.factories;

import hotciv.strategies.*;
import hotciv.variants.*;

public class BetaCivFactory implements AbstractFactory {

    @Override
    public WinnerStrategy createWinnerStrategy() {
        return new BetaCivWinnerStrategy();
    }

    @Override
    public AgingStrategy createAgingStrategy() {
        return new BetaCivAgingStrategy();
    }

    @Override
    public LayoutStrategy createLayoutStrategy() {
        return new AlphaCivLayoutStrategy();
    }

    @Override
    public UnitActionStrategy createUnitActionStrategy() {
        return new AlphaCivUnitActionStrategy();
    }

    @Override
    public MoveUnitStrategy createMoveUnitStrategy() {
        return new AlphaCivMoveUnitStrategy();
    }

    @Override
    public AttackStrategy createAttackStrategy() {
        return new AlphaCivAttackStrategy();
    }
}
