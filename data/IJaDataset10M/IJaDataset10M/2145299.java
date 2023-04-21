package com.idna.dm.dao.sp.config;

import java.sql.Types;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import com.idna.dm.dao.orm.rowmapper.DecisionRuleLogSearchesReportRowRowMapper;

public class DecisionRuleLogSearchesSpConfig extends AbstractSpConfig<RowMapper> {

    public DecisionRuleLogSearchesSpConfig() {
        super("decision_rule_log_searches", new DecisionRuleLogSearchesReportRowRowMapper());
    }

    public static final SqlParameter LOGIN_ID = new SqlParameter("@pi_login_id", Types.VARCHAR);

    public static final SqlParameter DECISION_ID = new SqlParameter("@pi_decision_id", Types.INTEGER);

    public static final SqlParameter RULE_ID = new SqlParameter("@pi_rule_id", Types.INTEGER);

    public static final SqlParameter USE_TEST_DATA = new SqlParameter("@pi_use_test_data", Types.BIT);

    public static final SqlParameter OUTCOME_ID = new SqlParameter("@pi_outcome_id", Types.INTEGER);

    public static final SqlParameter SEARCH_ID = new SqlOutParameter("search_id", Types.VARCHAR);

    public static final SqlParameter SEARCH_DATE = new SqlOutParameter("search_date", Types.VARCHAR);

    public static final SqlParameter DECISION = new SqlOutParameter("decision", Types.VARCHAR);

    public static final SqlParameter PRODUCT = new SqlOutParameter("product", Types.VARCHAR);

    public static final SqlParameter LOGIN = new SqlOutParameter("login", Types.VARCHAR);

    public static final SqlParameter SUBJECT = new SqlOutParameter("subject", Types.VARCHAR);

    public static final SqlParameter CLIENT_REF = new SqlOutParameter("client_ref", Types.VARCHAR);

    public static final SqlParameter RULE_NAME = new SqlOutParameter("rule_name", Types.VARCHAR);
}
