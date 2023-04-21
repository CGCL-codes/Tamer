package edu.byu.ece.edif.tools.sterilize.lutreplace;

import java.util.ArrayList;
import edu.byu.ece.edif.core.EdifCell;
import edu.byu.ece.edif.core.EdifCellInstance;
import edu.byu.ece.edif.core.EdifEnvironment;
import edu.byu.ece.edif.core.EdifNameConflictException;

/**
 * Provides a mechanism to copy an EdifEnvironment and replace instances
 * in the environment with different instances.
 * 
 * This class overrides several of the parent class methods to operate
 * differently during the copy process.
 * 
 * This class does not actually do the replacement. Instead, it builds a
 * copy and saves all the information necessary for adding replacement
 * circuitry at a later time by a different class.
 */
public class BasicEdifEnvironmentCopyReplace extends AbstractEdifEnvironmentCopyReplace {

    /**
	 * Perform the copy. Note that several of the methods in this class
	 * will be called by the parent methods.
	 * 
	 * @param env
	 * @param replacements A list of EdifCell objects that should be replaced.
	 * @throws EdifNameConflictException
	 */
    public BasicEdifEnvironmentCopyReplace(EdifEnvironment env, ArrayList<EdifCell> replacements) throws EdifNameConflictException {
        super(env);
        _cellTypesToReplace = replacements;
        createEdifEnvironment();
    }

    /**
	 * Overrides the parent addChildEdifCellInstance method. This method
	 * will check to see if the instance is of a type that needs to be 
	 * replaced. If so, it will not add the instance and create a
	 * ReplacementContext for this instance. If it doesn't match, the
	 * instance will be added as usual.
	 */
    protected void addChildEdifCellInstance(EdifCell origCell, EdifCell newCell, EdifCellInstance oldChildInstance) throws EdifNameConflictException {
        EdifCell replaceCell = matchingReplacementCell(oldChildInstance);
        if (replaceCell != null) {
            TemplateCellReplacementContext rc = new TemplateCellReplacementContext(replaceCell, newCell, oldChildInstance);
            _oldInstancesToReplace.put(oldChildInstance, rc);
        } else super.addChildEdifCellInstance(origCell, newCell, oldChildInstance);
    }

    protected EdifCell matchingReplacementCell(EdifCellInstance oldChildInstance) {
        EdifCell oldChildType = oldChildInstance.getCellType();
        if (_oldCellsNOTToReplace.contains(oldChildType)) return null;
        if (_oldCellsToReplace.contains(oldChildType)) return oldChildType;
        for (EdifCell replaceCell : _cellTypesToReplace) {
            if (oldChildType.equalsName(replaceCell) && oldChildType.equalsInterface(replaceCell)) {
                _oldCellsToReplace.add(oldChildType);
                return replaceCell;
            }
        }
        _oldCellsNOTToReplace.add(oldChildType);
        return null;
    }

    protected ArrayList<EdifCell> _oldCellsToReplace = new ArrayList<EdifCell>();

    protected ArrayList<EdifCell> _oldCellsNOTToReplace = new ArrayList<EdifCell>();

    protected ArrayList<EdifCell> _cellTypesToReplace;
}
