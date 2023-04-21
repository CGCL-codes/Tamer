package sql.dml;

import sql.ddl.Schema;
import sql.ddl.Tabela;
import sql.ddl.TokensSQL;

public class DDL implements TokensSQL {

    public String create(Tabela tabela) {
        return tabela.codigoSQL();
    }

    public String drop(Tabela tabela, boolean cascade) {
        return DROP + SPACE + TABLE + SPACE + tabela.getALLName() + (cascade ? SPACE + CASCADE : NONVALUE);
    }

    public String drop(Schema schema) {
        return schema.drop();
    }

    public String create(Schema schema) {
        return schema.create();
    }
}
