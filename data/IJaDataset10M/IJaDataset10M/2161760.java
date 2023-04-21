package org.gbif.checklistbank.model.rowmapper.rs;

import org.gbif.checklistbank.model.VernacularName;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class VernacularNameRowMapperBase<T extends VernacularName> extends RowMapperBase<T> {

    protected static final String SELECT_ALL = "vn.id,area_fk,language,life_stage_fk,name_string_fk,organism_part_fk,sex_fk,temporal,usage_fk";

    private static final String ID_COLUMN = "vn.id";

    public VernacularNameRowMapperBase(String from) {
        super(SELECT_ALL, from);
    }

    public VernacularNameRowMapperBase(String select, String from) {
        super(select, from);
    }

    @Override
    public String idColumn() {
        return ID_COLUMN;
    }

    public T mapRow(ResultSet rs, T vname) throws SQLException {
        vname.setId((Integer) rs.getObject("id"));
        vname.setAreaId((Integer) rs.getObject("area_fk"));
        vname.setLanguage(rs.getString("language"));
        vname.setLifeStageId((Integer) rs.getObject("life_stage_fk"));
        vname.setNameStringId((Integer) rs.getObject("name_string_fk"));
        vname.setOrganismPartId((Integer) rs.getObject("organism_part_fk"));
        vname.setSexId((Integer) rs.getObject("sex_fk"));
        vname.setTemporal(rs.getString("temporal"));
        vname.setUsageId((Integer) rs.getObject("usage_fk"));
        return vname;
    }
}
