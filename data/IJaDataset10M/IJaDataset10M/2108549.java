package org.gbif.checklistbank.model.rowmapper.rs;

import org.gbif.checklistbank.model.VernacularNameFull;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VernacularFullNameRowMapper extends VernacularNameRowMapperBase<VernacularNameFull> {

    private static final String SELECT_ADDITION = ",sex.term as sex,life_stage.term as life_stage,organism_part.term as organism_part,area.term as area,vernacular_name";

    private static final String FROM = "vernacular_name vn join vernacular_name_string vns on vn.name_string_fk=vns.id  left join term sex on sex_fk=sex.id left join term life_stage on life_stage_fk=life_stage.id left join term organism_part on organism_part_fk=organism_part.id left join term area on area_fk=area.id";

    public VernacularFullNameRowMapper() {
        super(VernacularNameRowMapperBase.SELECT_ALL + SELECT_ADDITION, FROM);
    }

    @Override
    public VernacularNameFull mapRow(ResultSet rs) throws SQLException {
        VernacularNameFull vname = mapRow(rs, new VernacularNameFull());
        vname.setSex(rs.getString("sex"));
        vname.setLifeStage(rs.getString("life_stage"));
        vname.setOrganismPart(rs.getString("organism_part"));
        vname.setNameString(rs.getString("vernacular_name"));
        vname.setArea(rs.getString("area"));
        return vname;
    }
}
