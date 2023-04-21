package org.debellor.core.exceptions.cell;

import org.debellor.core.Cell;
import org.debellor.core.exceptions.DebellorException;

/**
 * @author Marcin Wojnarski
 *
 */
public class CellException extends DebellorException {

    public final Cell cell;

    public CellException(Cell cell, Throwable cause) {
        super("A method of " + cell + " cell has failed", cause);
        this.cell = cell;
    }

    public CellException(Cell cell, Cell.CellMethod cellMethod, Throwable cause) {
        super("Method " + cellMethod + "() of " + cell + " cell has failed", cause);
        this.cell = cell;
    }

    public CellException(Cell cell, Cell.CellMethod cellMethod, String cause) {
        super("Method " + cellMethod + "() of " + cell + " cell has failed. " + cause);
        this.cell = cell;
    }

    public CellException(Cell cell, String cause) {
        super("A method of " + cell + " cell has failed. " + cause);
        this.cell = cell;
    }

    public CellException(Cell cell, String method, String cause) {
        super("Method " + method + "() of " + cell + " cell has failed. " + cause);
        this.cell = cell;
    }
}
