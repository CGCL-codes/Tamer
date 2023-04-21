package jy2.cikk;

import java.sql.SQLException;
import jy2.common.db.DataSet;
import jy2.common.db.Database;
import jy2.common.db.Field;
import jy2.common.db.FieldDef;

/**
 *
 * @author yehuitragasitscs
 */
public class CikkDataSet extends DataSet {

    /** Creates a new instance of DataSet */
    public CikkDataSet(Database database) throws ClassNotFoundException, SQLException {
        this.tableName = "cikk";
        this.selectSQL = "Select First ? " + "c.id, c.kod, c.nev, c.leiras, c. kep, " + "c.cikkcsoport_id, i.nev As cikkcsoport, " + "c.cikktipus_id, t.nev As cikktipus, " + "c.mennyisegi_egyseg_id, m.nev AS mennyisegi_egyseg, " + "c.afa_id, a.afa, " + "c.foszallito_id, u.nev As FoSzallito, " + "c.foraktar_id, r.nev As FoRaktar, " + "c.elszar, c.abeszar, c.vonalkod, " + "c.vtsz_id, v.vtsz, " + "c.felhasznalo_id, k.nev as fnev, c.belyeg " + "From Cikk c, CikkCsoport i, cikktipus T, mennyisegi_egyseg m, afa a, " + "Ugyfel u, Raktar r, vtsz v, felhasznalo f, kapcsolat k " + "Where (C.cikkcsoport_id=i.id) " + "And (c.cikktipus_id=t.id) " + "And (C.mennyisegi_egyseg_id=m.id) " + "And (C.afa_id=a.id) " + "And (c.foszallito_id=u.id) " + "And (c.foraktar_id=r.id) " + "And (c.vtsz_id=v.id) " + "And (c.felhasznalo_id=f.id) " + "And (f.kapcsolat_id=k.id)";
        this.deleteSQL = "{call DEL_" + tableName + "(?,?)}";
        this.refreshSQL = "Select " + "c.id, c.kod, c.nev, c.leiras, c. kep, " + "c.cikkcsoport_id, i.nev As cikkcsoport, " + "c.cikktipus_id, t.nev As cikktipus, " + "c.mennyisegi_egyseg_id, m.nev AS mennyisegi_egyseg, " + "c.afa_id, a.afa, " + "c.foszallito_id, u.nev As FoSzallito, " + "c.foraktar_id, r.nev As FoRaktar, " + "c.elszar, c.abeszar, c.vonalkod, " + "c.vtsz_id, v.vtsz, " + "c.felhasznalo_id, k.nev as fnev, c.belyeg " + "From Cikk c, CikkCsoport i, cikktipus T, mennyisegi_egyseg m, afa a, " + "Ugyfel u, Raktar r, vtsz v, felhasznalo f, kapcsolat k " + "Where (C.cikkcsoport_id=i.id) " + "And (c.cikktipus_id=t.id) " + "And (C.mennyisegi_egyseg_id=m.id) " + "And (c.afa_id=a.id) " + "And (c.foszallito_id=u.id) " + "And (c.foraktar_id=r.id) " + "And (c.vtsz_id=v.id) " + "And (c.felhasznalo_id=f.id) " + "and (c.id=?) ";
        this.generatorSQL = "SELECT gen_id(GEN_" + tableName + ", 1) FROM RDB$DATABASE";
        setDatabase(database);
        statement = getDatabase().getConnection().createStatement();
        fields = new Field[25];
        setField(0, new Field(5, false, "ID", "c.id", true, true, null));
        setField(1, new Field(10, true, "K�d", "c.kod", true, true, (new FieldDef())));
        setField(2, new Field(40, true, "Cikk", "c.nev", true, true, (new FieldDef())));
        setField(3, new Field(5, true, "Le�r�s", "c.leiras", false, true, null));
        setField(4, new Field(5, true, "K�p", "c. kep", false, false, null));
        setField(5, new Field(5, true, "CikkcsoportID", "c.cikkcsoport_id", false, true, (new FieldDef())));
        setField(6, new Field(40, true, "Cikkcsoport", "i.nev", true, false, null));
        setField(7, new Field(5, true, "Cikkt�pusID", "c.cikktipus_id", false, true, (new FieldDef())));
        setField(8, new Field(40, true, "Cikkt�pus", "t.nev", true, true, null));
        setField(9, new Field(5, true, "MeID", "c.mennyisegi_egyseg_id", false, true, (new FieldDef())));
        setField(10, new Field(10, true, "Me", "m.nev", true, false, null));
        setField(11, new Field(5, true, "�faID", "c.afa_id", false, true, (new FieldDef())));
        setField(12, new Field(5, true, "�fa", "a.afa", true, false, null));
        setField(13, new Field(5, true, "F�sz�ll�t�ID", "c.foszallito_id", false, true, (new FieldDef())));
        setField(14, new Field(40, true, "F�sz�ll�t�", "u.nev", true, false, null));
        setField(15, new Field(5, true, "F�rakt�rID", "c.foraktar_id", false, true, (new FieldDef())));
        setField(16, new Field(40, true, "F�rakt�r", "r.nev", true, false, null));
        setField(17, new Field(5, true, "Elsz�r", "c.elszar", true, true, (new FieldDef())));
        setField(18, new Field(5, true, "ABesz�r", "c.abeszar", true, true, (new FieldDef())));
        setField(19, new Field(10, true, "Vonalk�d", "c.vonalkod", true, true, (new FieldDef())));
        setField(20, new Field(5, true, "VtszID", "c.vtsz_id", false, true, (new FieldDef())));
        setField(21, new Field(50, true, "Vtsz", "v.vtsz", true, false, null));
        setField(22, new Field(15, true, "L�trehoz�", "c.felhasznalo_id", false, true, null));
        setField(23, new Field(20, true, "L�trehozta", "k.nev", true, false, null));
        setField(24, new Field(12, true, "L�trehozva", "c.belyeg", true, true, null));
        selectData(selectSQL);
        getField(1).FieldDef().setRequired(true);
        getField(1).FieldDef().setMaxLength(20);
        getField(2).FieldDef().setRequired(true);
        getField(1).FieldDef().setMaxLength(50);
        getField(5).FieldDef().setRequired(true);
        getField(7).FieldDef().setRequired(true);
        getField(9).FieldDef().setRequired(true);
        getField(11).FieldDef().setRequired(true);
        getField(13).FieldDef().setRequired(true);
        getField(15).FieldDef().setRequired(true);
        getField(17).FieldDef().setNumber(true);
        getField(18).FieldDef().setNumber(true);
        getField(19).FieldDef().setMaxLength(20);
        getField(20).FieldDef().setRequired(true);
    }
}
