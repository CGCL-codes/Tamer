package au.org.tpac.portal.repository;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import au.org.tpac.portal.domain.DatasetCoverage;
import au.org.tpac.portal.repository.RowMappers.DatasetIdMapper;
import au.org.tpac.portal.repository.RowMappers.DatasetCoverageMapper;

/**
 * The Class JdbcDatasetCoverageDao.
 */
public class JdbcDatasetCoverageDao extends SimpleJdbcDaoSupport implements DatasetCoverageDao {

    /** The logger. */
    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public final List<Integer> findDatasetIds(final List<Integer> tagIds, final String polygonText, Date fromDate, Date toDate) {
        int numberOfTags = 0;
        if (tagIds != null) {
            numberOfTags = tagIds.size();
        }
        if (polygonText == null && numberOfTags == 0 && fromDate == null && toDate == null) {
            return null;
        }
        String operator = "";
        String sql = "SELECT DISTINCT ID FROM datasets where ";
        if (numberOfTags > 0) {
            sql += buildTagSelectClause(tagIds);
            operator = " AND ";
        }
        if (polygonText != null) {
            sql += operator + buildPolygonSelectClause(polygonText);
            operator = " AND ";
        }
        if (fromDate != null && toDate == null) {
            sql += operator + buildDateSelectClause(">=", "fromDate", fromDate);
            operator = " AND ";
        }
        if (toDate != null && fromDate == null) {
            sql += operator + buildDateSelectClause("<=", "toDate", toDate);
            operator = " AND ";
        }
        if (toDate != null && fromDate != null) {
            sql += operator + buildDateSelectClause(">=", "toDate", fromDate);
            operator = " AND ";
            sql += operator + buildDateSelectClause("<=", "fromDate", toDate);
        }
        List<Integer> result = getSimpleJdbcTemplate().query(sql, new DatasetIdMapper());
        return result;
    }

    /**
     * Builds the date select clause.
     *
     * @param operator the operator
     * @param columnName the column name
     * @param date the date
     * @return the string
     */
    private String buildDateSelectClause(String operator, String columnName, Date date) {
        long time = date.getTime();
        String result = "(ID IN (SELECT DATASET_ID FROM dataset_meta WHERE FIELD_KEY = '" + columnName + "' AND FIELD_VALUE " + operator + time + "))";
        return result;
    }

    /**
	 * Builds the polygon select clause.
	 *
	 * @param polygonText the polygon text
	 * @return the string
	 */
    private String buildPolygonSelectClause(final String polygonText) {
        String result = "(ID IN " + "( SELECT DISTINCT DATASET_ID FROM dataset_coverages WHERE (MBRINTERSECTS(WGS84_BOUNDING_BOX," + polygonText + " ))) " + "OR ID IN " + "( SELECT DISTINCT DATASET_ID FROM dataset_coverages WHERE (MBRINTERSECTS(WGS84_BOUNDING_BOX_WORKAROUND," + polygonText + " ))))";
        return result;
    }

    /**
     * Builds the tag select clause.
     *
     * @param tagIds the tag ids
     * @return the string
     */
    private String buildTagSelectClause(final List<Integer> tagIds) {
        if (tagIds == null || tagIds.size() == 0) {
            return "";
        }
        String clause = "(";
        String operator = "";
        for (Integer tagId : tagIds) {
            clause += operator + " ID in (SELECT DATASET_ID FROM dataset_tags WHERE TAG_ID = " + tagId.toString() + ") ";
            operator = " AND";
        }
        clause += ")";
        return clause;
    }

    @Override
    public DatasetCoverage findCoverage(int datasetId) {
        String sql = "SELECT ID,DATASET_ID,CATEGORY_ID,CAST(ASTEXT(WGS84_BOUNDING_BOX) AS CHAR) AS 'WGS84_BOUNDING_BOX',CAST(ASTEXT(WGS84_BOUNDING_BOX_WORKAROUND) AS CHAR) AS 'WGS84_BOUNDING_BOX_WORKAROUND' FROM dataset_coverages WHERE DATASET_ID = ";
        List<DatasetCoverage> datasetCoverages = getSimpleJdbcTemplate().query(sql + datasetId, new DatasetCoverageMapper());
        if (datasetCoverages.size() > 0) {
            return datasetCoverages.get(0);
        }
        return null;
    }

    @Override
    public void insertDatasetCoverage(DatasetCoverage datasetCoverage) {
        String geometry1String = formatGeometryString(datasetCoverage.getWgs84BoundingBox());
        String geometry2String = formatGeometryString(datasetCoverage.getWgs84BoundingBoxWorkaround());
        if (geometry1String == null) {
            logger.error("Attempting to insert null value into geometry column - ignoring");
            return;
        }
        if (geometry2String == null) {
            geometry2String = geometry1String;
        }
        String sql = "INSERT INTO dataset_coverages (ID,DATASET_ID,CATEGORY_ID,WGS84_BOUNDING_BOX,WGS84_BOUNDING_BOX_WORKAROUND) VALUES ( ";
        sql += datasetCoverage.getId() + ",";
        sql += datasetCoverage.getDatasetId() + ",";
        sql += datasetCoverage.getCategoryId() + ",";
        sql += geometry1String + ",";
        sql += geometry2String + ")";
        getSimpleJdbcTemplate().update(sql);
    }

    @Override
    public void updateDatasetCoverage(DatasetCoverage datasetCoverage) {
        String geometry1String = formatGeometryString(datasetCoverage.getWgs84BoundingBox());
        String geometry2String = formatGeometryString(datasetCoverage.getWgs84BoundingBoxWorkaround());
        if (geometry1String == null) {
            logger.error("Attempting to update null value into geometry column - ignoring");
            return;
        }
        if (geometry2String == null) {
            geometry2String = geometry1String;
        }
        String sql = "UPDATE dataset_coverages SET dataset_id = " + datasetCoverage.getDatasetId();
        sql += ", category_id = " + datasetCoverage.getCategoryId();
        sql += ", wgs84_bounding_box = " + geometry1String;
        sql += ", wgs84_bounding_box_workaround = " + geometry2String;
        sql += " WHERE id = " + datasetCoverage.getId();
        getSimpleJdbcTemplate().update(sql);
    }

    /**
     * Format geometry string.
     *
     * @param polygonText the polygon text
     * @return the geometry string
     */
    private String formatGeometryString(String polygonText) {
        if (polygonText == null) {
            return null;
        } else {
            return "GeomFromText('" + polygonText + "')";
        }
    }

    /**
     * Builds the intersect clause.
     * 
     * @param tagIds the tag ids
     * 
     * @return the string
     */
    private String buildIntersectClause(final List<Integer> tagIds) {
        String clause = "";
        String operator1 = "";
        String operator2 = "";
        int count = 1;
        for (Integer tagId : tagIds) {
            clause += operator1 + " (SELECT DATASET_ID FROM dataset_tags WHERE TAG_ID = " + tagId.toString() + ") AS TMP" + count + operator2;
            operator1 = " INNER JOIN";
            operator2 = " USING (DATASET_ID)";
            count++;
        }
        return clause;
    }
}
