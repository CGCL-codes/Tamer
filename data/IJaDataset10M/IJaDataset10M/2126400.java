package civquest.swing.fieldview;

import civquest.core.GameDataAccessor;
import civquest.util.Coordinate;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import swifu.main.Function;
import swifu.main.FunctionException;

/** A FieldView that works by using some other FieldViews.
 *  See implementors like LayeredFieldView for examples. <p>
 *
 *  Has to implement FieldViewManager because it controls FieldViews although
 *  it is a FieldView itself - see class-comment of FieldViewManager for 
 *  details.
 */
public abstract class CompositeFieldView extends AbstractFieldView implements FieldViewManager {

    protected List<FieldView> children = new ArrayList<FieldView>();

    public CompositeFieldView(Coordinate position, FieldViewManager parent, GameDataAccessor gameData) {
        super(position, parent, gameData);
    }

    public void newData(GameDataAccessor gameData) {
        this.gameData = gameData;
        for (FieldView child : children) {
            child.newData(gameData);
        }
    }

    /** Adds the given fieldView (as a child) to this CompositeFieldView.
	 * @param fieldView any FieldView (don't try to build up a cyclic 
	 *                  fieldView-graph!!!)
	 * @return the parameter fieldView
	 */
    protected FieldView addFieldView(FieldView fieldView) {
        children.add(fieldView);
        fieldView.newData(gameData);
        return fieldView;
    }

    public Set<Function> getFunctionsForParent() throws FunctionException {
        return null;
    }

    public void executeFunction(String name, Coordinate position) {
    }

    public void beforeEvent(Point position) {
    }

    public void afterEvent(Point position) {
    }

    /*************** Implementation of FieldViewManager **********************/
    public void repaintChildFieldView(FieldView fieldView) {
        parent.repaintChildFieldView(this);
    }

    /*************************************************************************/
    public boolean addToHardEnabledPieces(String piece) {
        boolean superRetValue = super.addToHardEnabledPieces(piece);
        Iterator iterator = children.listIterator();
        while (iterator.hasNext()) {
            ((FieldView) (iterator.next())).addToHardEnabledPieces(piece);
        }
        return superRetValue;
    }

    public boolean removeFromHardEnabledPieces(String piece) {
        boolean superRetValue = super.removeFromHardEnabledPieces(piece);
        Iterator iterator = children.listIterator();
        while (iterator.hasNext()) {
            ((FieldView) (iterator.next())).removeFromHardEnabledPieces(piece);
        }
        return superRetValue;
    }

    public boolean addToHardDisabledPieces(String piece) {
        boolean superRetValue = super.addToHardDisabledPieces(piece);
        Iterator iterator = children.listIterator();
        while (iterator.hasNext()) {
            ((FieldView) (iterator.next())).addToHardDisabledPieces(piece);
        }
        return superRetValue;
    }

    public boolean removeFromHardDisabledPieces(String piece) {
        boolean superRetValue = super.removeFromHardDisabledPieces(piece);
        Iterator iterator = children.listIterator();
        while (iterator.hasNext()) {
            ((FieldView) (iterator.next())).removeFromHardDisabledPieces(piece);
        }
        return superRetValue;
    }

    public void addToSoftDisabledPieces(String piece) {
        super.addToSoftDisabledPieces(piece);
        Iterator iterator = children.listIterator();
        while (iterator.hasNext()) {
            ((FieldView) (iterator.next())).addToSoftDisabledPieces(piece);
        }
    }

    public boolean removeFromSoftDisabledPieces(String piece) {
        boolean superRetValue = super.removeFromSoftDisabledPieces(piece);
        Iterator iterator = children.listIterator();
        while (iterator.hasNext()) {
            ((FieldView) (iterator.next())).removeFromSoftDisabledPieces(piece);
        }
        return superRetValue;
    }
}
