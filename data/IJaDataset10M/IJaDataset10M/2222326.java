package de.sudokuloeser.finder.numberFound2cantBe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import de.sudokuloeser.finder.cantBe2numberFound.Complex1CantBeFinder;
import de.sudokuloeser.observer.Resetable;
import de.sudokuloeser.observer.SudokuEvent;
import de.sudokuloeser.observer.SudokuManager;

/**
 * @author daju
 * @since 1.0.0
 *
 */
public class StandartCantBe extends AbstractCantBe implements Resetable {

    ArrayList<AbstractCantBe> cantBes = new ArrayList<AbstractCantBe>();

    Complex1CantBeFinder complex1CantBeFinder = new Complex1CantBeFinder("complex1CantBeFinder");

    public StandartCantBe(String name, SudokuManager sudokuManager) {
        super(name, sudokuManager);
        init();
    }

    protected void init() {
        HorizontalCantBeRule cantBeRule = new HorizontalCantBeRule("HorizontalCantBeRule", sudokuManager);
        VerticalCantBeRule cantBeRule2 = new VerticalCantBeRule("VerticalCantBeRule", sudokuManager);
        BoxCantBeRule cantBeRule3 = new BoxCantBeRule("BoxCantBeRule", sudokuManager);
        OnePositonCantBeRule cantBeRule4 = new OnePositonCantBeRule("OnePositonCantBeRule", sudokuManager);
        cantBes.add(cantBeRule);
        cantBes.add(cantBeRule2);
        cantBes.add(cantBeRule3);
        cantBes.add(cantBeRule4);
    }

    @Override
    public Set<SudokuEvent> finderLogic(SudokuEvent numberFoundEvent) {
        Set<SudokuEvent> cantBeResult = new HashSet<SudokuEvent>();
        for (AbstractCantBe cantBe : cantBes) {
            cantBeResult.addAll(cantBe.finderLogic(numberFoundEvent));
        }
        Set<SudokuEvent> compl = complex1CantBeFinder.finderLogic(cantBeResult);
        cantBeResult.addAll(compl);
        return cantBeResult;
    }

    public void reset() {
        complex1CantBeFinder.reset();
    }
}
