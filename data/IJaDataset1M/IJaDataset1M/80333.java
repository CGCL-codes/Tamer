package org.plc.inventaire.data;

/**
 * 
 * @author pierreluc
 * @date 2010-11-01
 *
 */
public final class MovementsStructure extends DBStructure {

    public MovementsStructure() {
        super("movements");
        populate(Movement.class);
    }

    @ColumnType(ColumnType.TYPE.DATE)
    public final DBColumn date = new DBColumn(Long.class);

    public final DBColumn productId = new DBColumn(String.class);

    public final DBColumn qty = new DBColumn(Double.class);

    public final DBColumn reason = new DBColumn(String.class);
}
