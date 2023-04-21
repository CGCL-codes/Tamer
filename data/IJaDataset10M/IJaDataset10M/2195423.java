package com.netx.cubigraf.entities;

import com.netx.generics.sql.Table;
import com.netx.data.Entity;
import com.netx.data.DatabaseException;

public class TiposRolo extends Entity {

    public TiposRolo() {
        super();
    }

    public Table verTiposRolo() throws DatabaseException {
        return select("tipo_rolo_id, largura, comprimento");
    }
}
