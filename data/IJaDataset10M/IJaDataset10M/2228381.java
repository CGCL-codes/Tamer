package com.entelience.provider.risk;

import org.apache.log4j.Logger;
import com.entelience.objects.AuthorizationException;
import com.entelience.objects.PageCounter;
import com.entelience.objects.risk.Criteria;
import com.entelience.objects.risk.RiskCriteriaId;
import com.entelience.objects.risk.RiskId;
import com.entelience.sql.Db;
import com.entelience.sql.DbHelper;
import com.entelience.sql.IdHelper;
import com.entelience.util.DateHelper;
import com.entelience.util.Logs;
import com.entelience.util.PageHelper;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * manage evaluation criteria
 * a criteria has a category
 * and a risk can have several criteria
 *  when doing the review of a risk, the user fills an evaluation for each criteria linked to the risk
 */
public class DbCriteria {

    private DbCriteria() {
    }

    protected static final Logger _logger = Logs.getLogger();

    public static RiskCriteriaId createCriteria(Db db, Criteria criteria, int modifier) throws SQLException {
        try {
            db.enter();
            DbReferences.checkRiskCategoryExists(db, criteria.getCategoryId());
            if (DbHelper.nullify(criteria.getReference()) == null) throw new IllegalArgumentException("Cannot create a criteria without a reference");
            if (DbHelper.nullify(criteria.getCriteriaDescription()) == null) throw new IllegalArgumentException("Cannot create a criteria without an evaluation description");
            if (!("text".equals(criteria.getExpectedAnswerType()) || "integer".equals(criteria.getExpectedAnswerType()) || "boolean".equals(criteria.getExpectedAnswerType()))) throw new IllegalArgumentException("Expected criteria type must be integer, boolean or text");
            PreparedStatement pst = db.prepareStatement("INSERT INTO risk.e_criteria (reference, description, criteria_description, e_category_id, expected_answer_type) VALUES (?, ?, ?, ?, ?)  RETURNING e_criteria_id, obj_ser");
            pst.setString(1, DbHelper.nullify(criteria.getReference()));
            pst.setString(2, DbHelper.nullify(criteria.getDescription()));
            pst.setString(3, DbHelper.nullify(criteria.getCriteriaDescription()));
            pst.setInt(4, criteria.getCategoryId());
            pst.setString(5, DbHelper.nullify(criteria.getExpectedAnswerType()));
            return IdHelper.getRiskCriteriaId(pst);
        } finally {
            db.exit();
        }
    }

    public static RiskCriteriaId updateCriteria(Db db, Criteria criteria, int modifier) throws SQLException, AuthorizationException {
        try {
            db.enter();
            Criteria oldCriteria = getCriteria(db, criteria.getCriteriaId());
            if (oldCriteria == null) throw new IllegalArgumentException("The criteria " + criteria.getCriteriaId() + " could not be found and therefore not be updated");
            PreparedStatement pstChk = db.prepareStatement("SELECT COUNT(*) FROM risk.e_risk_to_criteria WHERE e_criteria_id = ?");
            pstChk.setInt(1, criteria.getCriteriaId().getId());
            Integer nbRisks = DbHelper.getKey(pstChk);
            if (nbRisks != null && nbRisks.intValue() > 0) {
                if (!oldCriteria.getExpectedAnswerType().equals(criteria.getExpectedAnswerType())) throw new AuthorizationException("The criteria " + criteria.getCriteriaId() + " cannot have its expected answer type change because it is used by " + nbRisks + " risks");
            }
            if (!("text".equals(criteria.getExpectedAnswerType()) || "integer".equals(criteria.getExpectedAnswerType()) || "boolean".equals(criteria.getExpectedAnswerType()))) throw new IllegalArgumentException("Expected criteria type must be integer, boolean or text");
            DbReferences.checkRiskCategoryExists(db, criteria.getCategoryId());
            if (DbHelper.nullify(criteria.getReference()) == null) throw new IllegalArgumentException("Cannot update a criteria without a reference");
            if (DbHelper.nullify(criteria.getCriteriaDescription()) == null) throw new IllegalArgumentException("Cannot update a criteria without an evaluation description");
            PreparedStatement pst = db.prepareStatement("UPDATE risk.e_criteria SET reference = ?, description = ?, criteria_description = ?, e_category_id = ?, expected_answer_type = ? WHERE e_criteria_id = ? RETURNING e_criteria_id, obj_ser");
            pst.setString(1, DbHelper.nullify(criteria.getReference()));
            pst.setString(2, DbHelper.nullify(criteria.getDescription()));
            pst.setString(3, DbHelper.nullify(criteria.getCriteriaDescription()));
            pst.setInt(4, criteria.getCategoryId());
            pst.setString(5, criteria.getExpectedAnswerType());
            pst.setInt(6, criteria.getCriteriaId().getId());
            return IdHelper.getRiskCriteriaId(pst);
        } finally {
            db.exit();
        }
    }

    public static RiskCriteriaId deleteCriteria(Db db, RiskCriteriaId criteriaId) throws SQLException, AuthorizationException {
        try {
            db.enter();
            Criteria oldCriteria = getCriteria(db, criteriaId);
            if (oldCriteria == null) throw new IllegalArgumentException("The criteria " + criteriaId + " could not be found and therefore not be deleted");
            PreparedStatement pstChk = db.prepareStatement("SELECT COUNT(*) FROM risk.e_risk_to_criteria WHERE e_criteria_id = ?");
            pstChk.setInt(1, criteriaId.getId());
            Integer nbRisks = DbHelper.getKey(pstChk);
            if (nbRisks != null && nbRisks.intValue() > 0) {
                throw new AuthorizationException("The criteria " + criteriaId + " could not be deleted because it is used by " + nbRisks + " risks");
            }
            PreparedStatement pst = db.prepareStatement("UPDATE risk.e_criteria SET deleted = true WHERE e_criteria_id = ? RETURNING e_criteria_id, obj_ser");
            pst.setInt(1, criteriaId.getId());
            return IdHelper.getRiskCriteriaId(pst);
        } finally {
            db.exit();
        }
    }

    public static Criteria getCriteria(Db db, RiskCriteriaId criteriaId) throws SQLException {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT c.e_criteria_id, c.obj_ser, c.reference, c.description, c.criteria_description, cat.e_category_id, cat.category_name, c.creation_date, c.obj_lm, c.expected_answer_type FROM risk.e_criteria c INNER JOIN risk.e_category cat ON c.e_category_id = cat.e_category_id WHERE e_criteria_id = ?");
            pst.setInt(1, criteriaId.getId());
            List<Criteria> l = getCriteriaListFromResultSet(db.executeQuery(pst));
            if (l == null || l.isEmpty()) return null;
            return l.get(0);
        } finally {
            db.exit();
        }
    }

    public static PageCounter countCriteriaLinkableToRisk(Db db, RiskId riskId, Integer categoryId) throws Exception {
        try {
            db.enter();
            StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM risk.e_criteria c INNER JOIN risk.e_category cat ON c.e_category_id = cat.e_category_id WHERE NOT deleted ");
            if (riskId != null) sql.append(" AND e_criteria_id NOT IN (SELECT e_criteria_id FROM risk.e_risk_to_criteria WHERE e_risk_id = ?) ");
            if (categoryId != null) sql.append(" AND cat.e_category_id = ? ");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            int i = 1;
            if (riskId != null) pst.setInt(i++, riskId.getId());
            if (categoryId != null) pst.setInt(i++, categoryId);
            Integer counter = DbHelper.getKey(pst);
            if (counter != null) {
                return PageHelper.getPageCounter(db, counter.intValue());
            }
            return new PageCounter(0, 0);
        } finally {
            db.exit();
        }
    }

    public static List<Criteria> listCriteriaLinkableToRisk(Db db, RiskId riskId, Integer categoryId, String order, String way, Integer pageNumber) throws Exception {
        try {
            db.enter();
            StringBuffer sql = new StringBuffer("SELECT c.e_criteria_id, c.obj_ser, c.reference, c.description, c.criteria_description, cat.e_category_id, cat.category_name, c.creation_date, c.obj_lm, c.expected_answer_type FROM risk.e_criteria c INNER JOIN risk.e_category cat ON c.e_category_id = cat.e_category_id WHERE NOT deleted ");
            if (riskId != null) sql.append(" AND e_criteria_id NOT IN (SELECT e_criteria_id FROM risk.e_risk_to_criteria WHERE e_risk_id = ?) ");
            if (categoryId != null) sql.append(" AND cat.e_category_id = ? ");
            StringBuffer orderPart = new StringBuffer(" ORDER BY ");
            if ("reference".equalsIgnoreCase(order)) orderPart.append(" c.reference "); else if ("description".equalsIgnoreCase(order)) orderPart.append(" c.description "); else if ("criteriaDescription".equalsIgnoreCase(order)) orderPart.append(" c.criteria_description "); else if ("categoryName".equalsIgnoreCase(order)) orderPart.append(" cat.category_name "); else if ("expectedAnswerType".equalsIgnoreCase(order)) orderPart.append(" c.expected_answer_type "); else orderPart.append(" c.reference ");
            if ("asc".equalsIgnoreCase(way)) orderPart.append(" ASC "); else if ("desc".equalsIgnoreCase(way)) orderPart.append(" DESC "); else orderPart.append(" ASC ");
            sql.append(orderPart);
            if (pageNumber != null) {
                sql.append(" LIMIT ? OFFSET ?");
            }
            PreparedStatement pst = db.prepareStatement(sql.toString());
            int i = 1;
            if (riskId != null) pst.setInt(i++, riskId.getId());
            if (categoryId != null) pst.setInt(i++, categoryId);
            if (pageNumber != null) {
                int limit = PageHelper.getSqlLimit(db);
                pst.setInt(i++, limit);
                pst.setInt(i++, PageHelper.getSqlOffset(pageNumber.intValue(), limit));
            }
            return getCriteriaListFromResultSet(db.executeQuery(pst));
        } finally {
            db.exit();
        }
    }

    public static List<Criteria> listCriteriaLinkedToRisk(Db db, RiskId riskId) throws SQLException {
        try {
            db.enter();
            PreparedStatement pst = db.prepareStatement("SELECT c.e_criteria_id, c.obj_ser, c.reference, c.description, c.criteria_description, cat.e_category_id, cat.category_name, c.creation_date, c.obj_lm, c.expected_answer_type FROM risk.e_criteria c INNER JOIN risk.e_category cat ON c.e_category_id = cat.e_category_id WHERE e_criteria_id IN (SELECT e_criteria_id FROM risk.e_risk_to_criteria WHERE e_risk_id = ?) AND NOT deleted ORDER BY reference, creation_date");
            pst.setInt(1, riskId.getId());
            return getCriteriaListFromResultSet(db.executeQuery(pst));
        } finally {
            db.exit();
        }
    }

    protected static List<Criteria> getCriteriaListFromResultSet(ResultSet rs) throws SQLException {
        List<Criteria> l = new ArrayList<Criteria>();
        if (rs.next()) {
            do {
                Criteria criter = new Criteria();
                criter.setCriteriaId(IdHelper.getRiskCriteriaId(rs, 1, 2));
                criter.setReference(rs.getString(3));
                criter.setDescription(rs.getString(4));
                criter.setCriteriaDescription(rs.getString(5));
                criter.setCategoryId(rs.getInt(6));
                criter.setCategory(rs.getString(7));
                criter.setCreationDate(DateHelper.toDate(rs.getTimestamp(8)));
                criter.setLastModificationDate(DateHelper.toDate(rs.getTimestamp(9)));
                criter.setExpectedAnswerType(rs.getString(10));
                l.add(criter);
            } while (rs.next());
        } else {
            _logger.debug("DbRisk - getRiskListFromResultSet - no row found");
        }
        return l;
    }

    public static void linkCriteriaToRisk(Db db, RiskId riskId, RiskCriteriaId criteriaId) throws SQLException {
        try {
            db.enter();
            DbRisk.ensureRiskValid(db, riskId);
            PreparedStatement pstChk = db.prepareStatement("SELECT e_criteria_id FROM risk.e_risk_to_criteria WHERE e_risk_id = ? AND e_criteria_id = ?");
            pstChk.setInt(1, riskId.getId());
            pstChk.setInt(2, criteriaId.getId());
            if (!DbHelper.noRows(pstChk)) throw new IllegalArgumentException("Cannot link a criteria to a risk twice ");
            PreparedStatement pst = db.prepareStatement("INSERT INTO risk.e_risk_to_criteria (e_risk_id, e_criteria_id) VALUES (?, ?)");
            pst.setInt(1, riskId.getId());
            pst.setInt(2, criteriaId.getId());
            int res = db.executeUpdate(pst);
            if (res != 1) throw new IllegalStateException("Error when linking a criteria to a risk");
        } finally {
            db.exit();
        }
    }

    public static void unlinkCriteriaFromRisk(Db db, RiskId riskId, RiskCriteriaId criteriaId) throws SQLException, AuthorizationException {
        try {
            db.enter();
            DbRisk.ensureRiskValid(db, riskId);
            PreparedStatement pstEval = db.prepareStatement("SELECT COUNT(*) FROM risk.e_risk_review rr INNER JOIN risk.e_risk_evaluation re ON re.e_risk_review_id = rr.e_risk_review_id WHERE rr.e_risk_id = ? AND NOT rr.deleted AND re.e_criteria_id = ?");
            pstEval.setInt(1, riskId.getId());
            pstEval.setInt(2, criteriaId.getId());
            int counter = DbHelper.getIntKey(pstEval);
            if (counter > 0) throw new AuthorizationException("Cannot unlink a criteria from a risk as there are evaluations");
            PreparedStatement pstChk = db.prepareStatement("SELECT e_criteria_id FROM risk.e_risk_to_criteria WHERE e_risk_id = ? AND e_criteria_id = ?");
            pstChk.setInt(1, riskId.getId());
            pstChk.setInt(2, criteriaId.getId());
            if (DbHelper.noRows(pstChk)) throw new IllegalArgumentException("Cannot unlink a criteria from a risk if they are not linked ");
            PreparedStatement pst = db.prepareStatement("DELETE FROM  risk.e_risk_to_criteria WHERE e_risk_id = ? AND e_criteria_id = ?");
            pst.setInt(1, riskId.getId());
            pst.setInt(2, criteriaId.getId());
            int res = db.executeUpdate(pst);
            if (res != 1) throw new IllegalStateException("Error when unlinking a criteria to a risk");
        } finally {
            db.exit();
        }
    }
}
