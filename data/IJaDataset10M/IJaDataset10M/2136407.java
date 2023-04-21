package integrationtier.mock;

import core.Titul;
import core.VapolevytoDVDException;
import integrationtier.ITitulDAO;

/**
 *
 * @author lucky
 */
public class TitulDAO implements ITitulDAO {

    public Titul vratTitul(int id) throws VapolevytoDVDException {
        Titul t = new Titul();
        t.setId(1);
        t.setNazev("Test Film");
        t.setDelka(120);
        t.setRok(2009);
        return t;
    }
}
