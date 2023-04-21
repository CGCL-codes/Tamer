package jy2.rendszer.jogosultsagok;

import java.sql.SQLException;
import jy2.common.db.DataSet;
import jy2.common.db.Database;
import jy2.common.db.Field;
import jy2.common.db.FieldDef;

/**
 *
 * @author yehuitragasitscs
 */
public class ModulDataSet extends DataSet {

    /** Creates a new instance of DataSet */
    public ModulDataSet(Database database) throws ClassNotFoundException, SQLException {
        this.tableName = "modul";
        this.selectSQL = "Select first ? " + "m.id, m.kod, m.megnevezes, " + "m.felhasznalo_id, k.nev, m.belyeg " + "from modul m, felhasznalo f, Kapcsolat k " + "Where (m.felhasznalo_id=f.id) " + "And (f.kapcsolat_id=k.ID) ";
        this.deleteSQL = "{call DEL_MODUL(?,?)}";
        this.refreshSQL = "Select m.id, m.kod, m.megnevezes, " + "m.felhasznalo_id, k.nev, m.belyeg " + "from modul m, felhasznalo f, Kapcsolat k " + "Where (m.felhasznalo_id=f.id) " + "And (f.kapcsolat_id=k.ID) " + "And (m.id=?)";
        this.generatorSQL = "SELECT gen_id(GEN_MODUL, 1) FROM RDB$DATABASE";
        setDatabase(database);
        statement = getDatabase().getConnection().createStatement();
        fields = new Field[6];
        setField(0, new Field(5, true, "ID", "m.id", true, true, null));
        setField(1, new Field(5, true, "K�d", "m.kod", true, true, (new FieldDef())));
        setField(2, new Field(30, true, "Megnevez�s", "m.megnevezes", true, true, (new FieldDef())));
        setField(3, new Field(15, true, "L�trehoz�", "m.felhasznalo_id", false, true, null));
        setField(4, new Field(20, true, "L�trehozta", "k.nev", true, true, null));
        setField(5, new Field(12, true, "L�trehozva", "m.belyeg", true, true, null));
        selectData(selectSQL);
        getField(1).FieldDef().setRequired(true);
        getField(2).FieldDef().setRequired(true);
        getField(1).FieldDef().setMaxLength(5);
        getField(2).FieldDef().setMaxLength(20);
    }
}
