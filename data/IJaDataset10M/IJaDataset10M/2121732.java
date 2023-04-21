package com.idna.dm.dao.sp.config.reporting;

import static com.idna.dm.dao.sp.util.SpParam.PI_DECISION_ID;
import static com.idna.dm.dao.sp.util.SpParam.PI_RULE_ID;
import static com.idna.dm.dao.sp.util.SpParam.PI_END_DATE;
import static com.idna.dm.dao.sp.util.SpParam.PI_LOGIN_ID;
import static com.idna.dm.dao.sp.util.SpParam.PI_OUTCOME_ID;
import static com.idna.dm.dao.sp.util.SpParam.PI_START_DATE;
import java.sql.Types;
import org.springframework.jdbc.core.SqlParameter;
import com.idna.dm.dao.orm.rowmapper.DecisionRuleLogSearchesReportRowRowMapper;
import com.idna.dm.dao.sp.config.AbstractRowMapperSpConfig;

public class ReportingDecisionRuleDetailSpConfig extends AbstractRowMapperSpConfig {

    public ReportingDecisionRuleDetailSpConfig() {
        super("rpt_decision_rule_detail", new DecisionRuleLogSearchesReportRowRowMapper());
    }

    public static final SqlParameter LOGIN_ID = new SqlParameter(PI_LOGIN_ID, Types.VARCHAR);

    public static final SqlParameter DECISION_ID = new SqlParameter(PI_DECISION_ID, Types.INTEGER);

    public static final SqlParameter RULE_ID = new SqlParameter(PI_RULE_ID, Types.INTEGER);

    public static final SqlParameter OUTCOME_ID = new SqlParameter(PI_OUTCOME_ID, Types.INTEGER);

    public static final SqlParameter START_DATE = new SqlParameter(PI_START_DATE, Types.VARCHAR);

    public static final SqlParameter END_DATE = new SqlParameter(PI_END_DATE, Types.VARCHAR);
}
