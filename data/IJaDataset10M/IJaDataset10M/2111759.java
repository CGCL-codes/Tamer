package sql.ddl;

import java.util.ArrayList;
import sql.ddl.coluna.ForeignKey;

public class ListaForeignKey extends ArrayList<ForeignKey> implements TokensSQL {

    private static final long serialVersionUID = 79517044562516255L;

    public String codigoSQL() {
        String s = NONVALUE;
        for (ForeignKey a : this) {
            s += VIRGUL + a.codigoSQL();
        }
        return s;
    }
}
