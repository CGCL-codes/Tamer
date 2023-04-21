package org.gridtrust.enforce.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gridtrust.Array_IndividualState;
import org.gridtrust.CompositeState;
import org.gridtrust.Constants;
import org.gridtrust.IndividualState;
import org.gridtrust.enforce.Array_Attribute;
import org.gridtrust.enforce.Attribute;
import org.gridtrust.enforce.SubjectPolicy;
import org.gridtrust.enforce.dao.AttributeDao;
import org.gridtrust.enforce.dao.AttributeHistoryDao;
import org.gridtrust.enforce.dao.DaoFactory;
import org.gridtrust.enforce.dao.SubjectPolicyDao;
import org.gridtrust.enforce.dto.AttributeDTO;
import org.gridtrust.enforce.util.EnforceUtil;
import org.gridtrust.ppm.impl.policy.normalizer.PolicyNormalizer;
import org.gridtrust.ppm.impl.policy.normalizer.jaxb.JAXBPolicyNormalizer;
import org.gridtrust.util.DBUtil;

public class JDBCSubjectPolicyDao extends AbstractJDBCDao implements SubjectPolicyDao {

    private static final Log log = LogFactory.getLog(JDBCSubjectPolicyDao.class);

    public CompositeState storeSubjectPolicies(SubjectPolicy[] policyList) {
        Connection con = null;
        PreparedStatement pStmt = null;
        boolean oneFailure = false;
        boolean oneSuccess = false;
        CompositeState storeState = new CompositeState();
        storeState.setOverAllState(Constants.COMPOSITE_STATE_FAILED);
        try {
            con = getConnection();
            con.setAutoCommit(false);
            String sql = "INSERT INTO SUBJECT_POLICY (SUBJECT_ID, POLICY, POLICY_RELATION_REF) VALUES (?, ?, ?)";
            String selectSql = "SELECT LAST_INSERT_ID()";
            IndividualState[] individualStateList = new IndividualState[policyList.length];
            int policyCounter = 0;
            boolean normalizePolicies = false;
            Properties props = EnforceUtil.getEnforceProperties();
            if (props != null) {
                normalizePolicies = Boolean.parseBoolean(props.getProperty(Constants.ENFORCE_POLICIES_NORMALIZE));
            } else {
                normalizePolicies = true;
            }
            PolicyNormalizer normalizer = null;
            if (normalizePolicies) {
                log.debug("Normalize policies");
                normalizer = new JAXBPolicyNormalizer();
            }
            for (SubjectPolicy subjectPolicy : policyList) {
                log.debug("Storing subject policy ");
                if (subjectPolicy != null) {
                    IndividualState individualState = new IndividualState();
                    try {
                        boolean isAllAttributeSuccess = false;
                        Array_Attribute subjectAttributeArray = subjectPolicy.getAttributes();
                        if (subjectAttributeArray != null && subjectAttributeArray.getItem() != null) {
                            DaoFactory daoFactory = DaoFactory.getDaoFactory();
                            AttributeDao attributeDao = daoFactory.getAttributeDao();
                            Attribute[] attributeList = subjectAttributeArray.getItem();
                            if (attributeList != null) {
                                for (Attribute attribute : attributeList) {
                                    AttributeDTO subjectAttribute = new AttributeDTO();
                                    subjectAttribute.setAttributeInfo(attribute);
                                    IndividualState attributeCreateState = attributeDao.createAttribute(subjectAttribute);
                                    if (!attributeCreateState.isState()) {
                                        log.error("Attribute creation error " + attribute.getAttributeName());
                                        isAllAttributeSuccess = false;
                                        break;
                                    } else {
                                        log.debug("Attribute create sucess " + attribute.getAttributeName());
                                        isAllAttributeSuccess = true;
                                    }
                                }
                            }
                        } else {
                            isAllAttributeSuccess = true;
                        }
                        if (isAllAttributeSuccess) {
                            pStmt = con.prepareStatement(sql);
                            pStmt.setString(1, subjectPolicy.getSubjectId());
                            String policyContent = subjectPolicy.getPolicyContent();
                            if (normalizePolicies) {
                                policyContent = normalizer.normalizePolicy(policyContent);
                            }
                            pStmt.setString(2, policyContent);
                            pStmt.setInt(3, Constants.XACML_1_1);
                            pStmt.executeUpdate();
                            pStmt = con.prepareStatement(selectSql);
                            ResultSet rs = pStmt.executeQuery();
                            if (rs.next()) {
                                int policyId = rs.getInt(1);
                                individualState.setServiceId(policyId + "");
                                individualState.setState(Constants.INDIVIDUAL_STATE_SUCCESS);
                                con.commit();
                                oneSuccess = true;
                            } else {
                                individualState.setState(Constants.INDIVIDUAL_STATE_FAILED);
                                individualState.setFailureReason(Constants.UNKNOWN_DB_ERROR);
                                oneFailure = true;
                            }
                        } else {
                            individualState.setState(Constants.INDIVIDUAL_STATE_FAILED);
                            individualState.setFailureReason(Constants.INVALID_ATTRIBUTE_PROVIDED);
                            oneFailure = true;
                        }
                    } catch (Exception e) {
                        log.error("Subject :: policy storing failed ", e);
                        oneFailure = true;
                    }
                    individualStateList[policyCounter] = individualState;
                    policyCounter++;
                }
            }
            if (oneFailure && oneSuccess) {
                storeState.setOverAllState(Constants.COMPOSITE_STATE_PARTIAL);
            } else if (oneSuccess && !oneFailure) {
                storeState.setOverAllState(Constants.COMPOSITE_STATE_SUCCESS);
            }
            storeState.setStateList(new Array_IndividualState(individualStateList));
        } catch (Exception e) {
            log.error("Subject policy insert error ", e);
        } finally {
            DBUtil.closeStatement(pStmt);
            DBUtil.returnConnection(con);
        }
        return storeState;
    }

    public void cleanAllPolicies() {
        Connection con = null;
        PreparedStatement pStmt = null;
        try {
            DaoFactory daoFactory = DaoFactory.getDaoFactory();
            AttributeDao attribDao = daoFactory.getAttributeDao();
            attribDao.cleanAllAttributes();
            AttributeHistoryDao attributeHistory = daoFactory.getAttributeHistoryDao();
            attributeHistory.cleanAttributeHistory();
            con = getConnection();
            con.setAutoCommit(false);
            String sql = "DELETE FROM SUBJECT_POLICY";
            pStmt = con.prepareStatement(sql);
            pStmt.executeUpdate();
            con.commit();
        } catch (Exception e) {
            log.error("Subject policy clean error ", e);
        } finally {
            DBUtil.closeStatement(pStmt);
            DBUtil.returnConnection(con);
        }
    }

    public IndividualState deleteSubjectPolicy(int policyId) {
        IndividualState deleteState = new IndividualState();
        Connection con = null;
        PreparedStatement pStmt = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            String sql = "DELETE FROM SUBJECT_POLICY WHERE SUBJECT_POLICY_ID = ?";
            pStmt = con.prepareStatement(sql);
            int count = pStmt.executeUpdate();
            if (count > 0) {
                con.commit();
                deleteState.setState(Constants.INDIVIDUAL_STATE_SUCCESS);
            }
        } catch (Exception e) {
            log.error("Subject policy delete error ", e);
        } finally {
            DBUtil.closeStatement(pStmt);
            DBUtil.returnConnection(con);
        }
        return deleteState;
    }

    public SubjectPolicy findSubjectPolicyBySubjectId(String subjectId) {
        SubjectPolicy subjectPolicy = null;
        Connection con = null;
        PreparedStatement pStmt = null;
        try {
            con = getConnection();
            String sql = "SELECT SUBJECT_POLICY_ID, POLICY, POLICY_RELATION_REF FROM SUBJECT_POLICY WHERE SUBJECT_ID = ? ";
            pStmt = con.prepareStatement(sql);
            pStmt.setString(1, subjectId);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                subjectPolicy = new SubjectPolicy();
                subjectPolicy.setSubjectId(subjectId);
                subjectPolicy.setPolicyContent(rs.getString("POLICY"));
                subjectPolicy.setPolicyInfoRef(rs.getInt("POLICY_RELATION_REF"));
            }
        } catch (Exception e) {
            log.error("Subject policy find error ", e);
        } finally {
            DBUtil.closeStatement(pStmt);
            DBUtil.returnConnection(con);
        }
        return subjectPolicy;
    }
}
