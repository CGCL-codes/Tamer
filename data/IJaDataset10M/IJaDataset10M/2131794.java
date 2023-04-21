package andre.grids.db.exceptions;

/**
 *
 * @author andre
 */
public class DbDuplicatedEntry extends Exception {

    public DbDuplicatedEntry(Object proto) {
        super("O tipo: " + proto.getClass().toString() + " possui valores duplicados para o objeto: " + proto.toString());
    }
}
