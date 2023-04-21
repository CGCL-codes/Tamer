package com.idna.dm.domain.testdataset.batch;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import com.idna.dm.dao.orm.domain.FlatDomain;

/**
 * @author vinay.nayak
 *
 */
@SuppressWarnings("unchecked")
public class DecisionQueue implements FlatDomain {

    public static final String DECISION_QUEUE_ID = "decision_queue_id";

    public static final String LOGIN_ID = "login_id";

    public static final String TEST_DATASET_ID = "test_dataset_id";

    public static final String DECISION_ID = "decision_id";

    public static final String RECORD_COUNT = "record_count";

    public static final String STATUS = "status";

    public static final String CREATED_DATE = "created_date";

    public static final String UPDATED_DATE = "updated_date";

    private Integer decisionQueueId;

    private String loginId;

    private String testDataSetId;

    private Integer decisionId;

    private Integer recordCount;

    private String status;

    private Date createdDate;

    private Date updatedDate;

    private List<Future<String>> futures;

    /**
	 * @return the decisionQueueId
	 */
    public Integer getDecisionQueueId() {
        return decisionQueueId;
    }

    /**
	 * @return the loginId
	 */
    public String getLoginId() {
        return loginId;
    }

    /**
	 * @return the testDataSetId
	 */
    public String getTestDataSetId() {
        return testDataSetId;
    }

    /**
	 * @return the decisionId
	 */
    public Integer getDecisionId() {
        return decisionId;
    }

    /**
	 * @return the recordCount
	 */
    public Integer getRecordCount() {
        return recordCount;
    }

    /**
	 * @return the status
	 */
    public String getStatus() {
        return status;
    }

    /**
	 * @return the createdDate
	 */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
	 * @return the updatedDate
	 */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
	 * @param decisionQueueId the decisionQueueId to set
	 */
    public void setDecisionQueueId(Integer decisionQueueId) {
        this.decisionQueueId = decisionQueueId;
    }

    /**
	 * @param loginId the loginId to set
	 */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
	 * @param testDataSetId the testDataSetId to set
	 */
    public void setTestDataSetId(String testDataSetId) {
        this.testDataSetId = testDataSetId;
    }

    /**
	 * @param decisionId the decisionId to set
	 */
    public void setDecisionId(Integer decisionId) {
        this.decisionId = decisionId;
    }

    /**
	 * @param recordCount the recordCount to set
	 */
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    /**
	 * @param status the status to set
	 */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
	 * @param createdDate the createdDate to set
	 */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
	 * @param updatedDate the updatedDate to set
	 */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public Integer getId() {
        return decisionQueueId;
    }

    @Override
    public String getName() {
        return loginId;
    }

    @Override
    public void setId(Integer id) {
    }

    @Override
    public void setName(String name) {
    }

    /**
	 * @return the futures
	 */
    public List<Future<String>> getFutures() {
        return futures;
    }

    /**
	 * @param futures the futures to set
	 */
    public void setFutures(List<Future<String>> futures) {
        this.futures = futures;
    }
}
