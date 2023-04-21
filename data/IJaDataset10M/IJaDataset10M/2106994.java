package net.sf.javarisk.three.game.engine;

import static net.sf.javarisk.tools.GeneralTools.createArray;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import net.sf.javarisk.three.event.FieldChangedPublisher;
import net.sf.javarisk.three.event.GameAdapter;
import net.sf.javarisk.three.event.RoundBeginsEvent;
import net.sf.javarisk.three.game.GameField;
import net.sf.javarisk.three.game.Player;
import net.sf.javarisk.three.game.Unit;
import net.sf.javarisk.tools.GeneralTools;

/**
 * <p>
 * The <code>UnitProviderForNewRound</code> is responsible for creating new units at the begin of a round.
 * </p>
 * 
 * @author <a href='mailto:sebastiankirsch@users.sourceforge.net'>Sebastian Kirsch</a>
 * @version 0.1; $Rev: 243 $
 * @since 0.1
 */
@NotThreadSafe
public class UnitProviderForNewRound extends GameAdapter {

    private static final Logger LOG = Logger.getLogger(UnitProviderForNewRound.class.getName());

    @Nonnull
    private final FieldChangedPublisher fieldChangedPublisher;

    @Nonnegative
    private final int newUnitsForCity;

    @Nonnegative
    private final int newUnitsForOwnedNeighbor;

    private GameField cityField;

    /**
     * Creates a new instance of <code>UnitProviderForNewRound</code> that adds the specified number of units to each
     * city.
     * 
     * @throws IllegalArgumentException
     *             if <code>newUnitsForCity</code> is less than <code>1</code> or <code>newUnitsForOwnedNeighbor</code>
     *             is less than <code>0</code>
     * @since 0.1
     */
    public UnitProviderForNewRound(@Nonnull FieldChangedPublisher fieldChangedPublisher, @Nonnegative int newUnitsForCity, @Nonnegative int newUnitsForOwnedNeighbor) throws IllegalArgumentException {
        this.fieldChangedPublisher = GeneralTools.notNull(fieldChangedPublisher, "fieldChangedPublisher");
        this.newUnitsForCity = GeneralTools.greaterZero(newUnitsForCity, "newUnitsForCity");
        this.newUnitsForOwnedNeighbor = GeneralTools.nonNegative(newUnitsForOwnedNeighbor, "newUnitsForOwnedNeighbor");
    }

    @Nonnull
    @Override
    public String toString() {
        return "UnitProviderForNewRound: a GameListener that adds new units to cities at the beginning of each round";
    }

    @Override
    public void roundBegins(RoundBeginsEvent event) {
        for (GameField field : event.getBoard()) {
            addNewUnitsToCities(field);
        }
    }

    private void addNewUnitsToCities(@Nonnull GameField field) {
        if (field.getCity() == null) {
            return;
        }
        this.cityField = field;
        addUnitsToCityField();
        this.fieldChangedPublisher.fireFieldChangedEvent(field);
    }

    private void addUnitsToCityField() {
        int numberOfNewUnits = this.newUnitsForCity;
        numberOfNewUnits += calculateNumberOfNewUnitsForOwnedNeighbors();
        addNewUnits(numberOfNewUnits);
    }

    private int calculateNumberOfNewUnitsForOwnedNeighbors() {
        int numberOfNewUnits = 0;
        Player owner = this.cityField.getOwner();
        for (GameField neighbor : this.cityField.getNeighbors()) {
            if (owner.equals(neighbor.getOwner())) {
                numberOfNewUnits += this.newUnitsForOwnedNeighbor;
            }
        }
        return numberOfNewUnits;
    }

    private void addNewUnits(@Nonnegative int numberOfNewUnits) {
        for (int i = numberOfNewUnits; i-- > 0; ) {
            Unit unit = new Unit(this.cityField);
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.log(Level.FINEST, "Added unit for city.", createArray(this.cityField, unit));
            }
        }
        if (LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "Added " + this.newUnitsForCity + " unit(s) for city & " + (numberOfNewUnits - this.newUnitsForCity) + " unit(s) for owned neighbors.", this.cityField);
        }
    }
}
