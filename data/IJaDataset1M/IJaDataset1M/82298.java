package org.identifylife.harvest.dao.impl.jdbc;

import java.util.List;
import javax.sql.DataSource;
import org.identifylife.harvest.dao.COLTaxaDAO;
import org.identifylife.harvest.model.COLTaxa;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * @author mike
 *
 */
public class COLTaxaDAOImpl implements COLTaxaDAO {

    protected static final String QUERY_COLTAXA_BY_PARENT_ID_SQL = "SELECT record_id AS id, name, lsid, parent_id, taxon AS rank from col2009ac.taxa where parent_id=?";

    private SimpleJdbcTemplate simpleJdbcTemplate;

    public List<COLTaxa> getCOLTaxonChildren(long parentId) {
        boolean limit = false;
        if (!limit) return this.simpleJdbcTemplate.query(QUERY_COLTAXA_BY_PARENT_ID_SQL, new ColTaxaRowMapper(), parentId);
        if (parentId == 0) return this.simpleJdbcTemplate.query(QUERY_COLTAXA_BY_PARENT_ID_SQL, new ColTaxaRowMapper(), parentId); else return this.simpleJdbcTemplate.query(QUERY_COLTAXA_BY_PARENT_ID_SQL + " LIMIT 2", new ColTaxaRowMapper(), parentId);
    }

    public COLTaxaDAOImpl(DataSource dataSource) {
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
    }
}
