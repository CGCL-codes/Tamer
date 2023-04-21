package vehicles.routine;

import java.util.Date;
import java.util.ResourceBundle;
import com.systop.core.model.BaseModel;
import com.systop.core.service.BaseGenericsManager;
import com.systop.core.util.DateUtil;
import com.systop.core.util.ResourceBundleUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 应客户要求，日常管理所有模块加入按照日期范围查询，所以，这里做一个父类处理相同的情况
 * 
 * @param <T>
 * @param <M>
 */
public class RoutineAction<T extends BaseModel, M extends BaseGenericsManager<T>> extends DefaultCrudAction<T, M> {

    /**
	 * 日期范围起点
	 */
    private Date dateFrom;

    /**
	 * 日期范围终点
	 */
    private Date dateTo;

    public static final String DATE_PATTERN = ResourceBundleUtil.getString(ResourceBundle.getBundle("application"), "date.format", "");

    /**
	 * 为原有的SQL加入日期查询条件
	 * @param originalSql 原来的SQL。如果之前没有任何查询条件，则应加入where 1=1 否则，这里无法判断是否加入AND
	 * @param fieldName 日期字段名称
	 * @return
	 */
    protected String fixSql(String originalSql, String fieldName) {
        if (dateFrom == null || dateTo == null) {
            return originalSql;
        }
        StringBuffer sql = new StringBuffer(originalSql);
        if (dateFrom != null) {
            sql.append(" AND ").append(fieldName).append(">='").append(DateUtil.getDateTime(DATE_PATTERN, dateFrom)).append("'");
        }
        if (dateTo != null) {
            sql.append(" AND ").append(fieldName).append("<='").append(DateUtil.getDateTime(DATE_PATTERN, dateTo)).append("'");
        }
        return sql.toString();
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
