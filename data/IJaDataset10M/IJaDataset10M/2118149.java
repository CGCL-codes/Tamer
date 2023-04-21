package org.debellor.core.exceptions.cell;

import org.debellor.core.Cell;
import org.debellor.core.Cell.CellMethod;

/**
 * @author Marcin Wojnarski
 *
 */
public class CellIsFixedException extends CellException {

    public CellIsFixedException(Cell cell, CellMethod method) {
        super(cell, method, "Cell is always built, it cannot be erased or built again (it is not buildable)");
    }
}
