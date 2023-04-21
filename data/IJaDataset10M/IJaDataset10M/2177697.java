package org.gbif.portal.dao.impl.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.gbif.portal.model.TaxonConcept;
import org.gbif.portal.model.TaxonConceptLite;
import org.springframework.jdbc.core.RowMapper;

/**
 * Utility to create a TaxonConcept for a row
 * @author trobertson
 */
public class TaxonConceptRowMapper implements RowMapper {

    /**
	 * Reuse
	 */
    protected TaxonNameRowMapper tnrm = new TaxonNameRowMapper();

    /**
	 * The factory
	 */
    public TaxonConceptLite mapRow(ResultSet rs, int rowNumber) throws SQLException {
        TaxonConcept tc = new TaxonConcept();
        tc.setId(rs.getLong("tc.id"));
        tc.setParentId(rs.getLong("tc.parent_concept_id"));
        tc.setRank(rs.getInt("tc.rank"));
        tc.setAccepted(rs.getBoolean("tc.is_accepted"));
        tc.setDataProviderId(rs.getLong("tc.data_provider_id"));
        tc.setDataResourceId(rs.getLong("tc.data_provider_id"));
        tc.setPartnerConceptId(rs.getLong("tc.partner_concept_id"));
        tc.setNubConcept(rs.getBoolean("tc.is_nub_concept"));
        tc.setPriority(rs.getInt("tc.priority"));
        tc.setKingdomConceptId(getLongOrNull(rs, "tc.kingdom_concept_id"));
        tc.setPhylumConceptId(getLongOrNull(rs, "tc.phylum_concept_id"));
        tc.setClassConceptId(getLongOrNull(rs, "tc.class_concept_id"));
        tc.setOrderConceptId(getLongOrNull(rs, "tc.order_concept_id"));
        tc.setFamilyConceptId(getLongOrNull(rs, "tc.family_concept_id"));
        tc.setGenusConceptId(getLongOrNull(rs, "tc.genus_concept_id"));
        tc.setSpeciesConceptId(getLongOrNull(rs, "tc.species_concept_id"));
        tc.setTaxonName(tnrm.mapRow(rs, rowNumber));
        return tc;
    }

    protected Long getLongOrNull(ResultSet rs, String column) throws SQLException {
        Long value = rs.getLong(column);
        if (value.longValue() > 0) {
            return value;
        } else {
            return null;
        }
    }
}
