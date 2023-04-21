package whf.framework.report;

import java.util.List;
import whf.framework.datasource.DataRow;
import whf.framework.datasource.DataSource;
import whf.framework.meta.entity.IMeta;
import whf.framework.security.UserContext;
import whf.framework.util.StringUtils;
import whf.framework.web.tag.ext.IColumn;

/**
 * @author King
 * @create Jan 17, 2008 1:58:03 PM
 */
public class ReportDescriptor {

    /**
	 * 报表类型
	 */
    private String reportType;

    private IMeta meta;

    /**
	 * 报表显示列定义
	 */
    private List<IColumn> columns;

    /**
	 * 报表数据源
	 */
    private DataSource<DataRow> dataSource;

    /**
	 * 当前用户上下文
	 */
    private UserContext userContext;

    public ReportDescriptor(UserContext userContext, DataSource<DataRow> dataSource, List<IColumn> columns, IMeta meta) {
        this.dataSource = dataSource;
        this.columns = columns;
        this.userContext = userContext;
        this.meta = meta;
    }

    /**
	 * @return the reportType
	 */
    public String getReportType() {
        return reportType;
    }

    /**
	 * @param reportType
	 *            the reportType to set
	 */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    /**
	 * @return the meta
	 */
    public IMeta getMeta() {
        return meta;
    }

    /**
	 * @param meta the meta to set
	 */
    public void setMeta(IMeta meta) {
        this.meta = meta;
    }

    /**
	 * @return the columns
	 */
    public List<IColumn> getColumns() {
        return columns;
    }

    /**
	 * @param columns
	 *            the columns to set
	 */
    public void setColumns(List<IColumn> columns) {
        this.columns = columns;
    }

    /**
	 * @return the dataSource
	 */
    public DataSource<DataRow> getDataSource() {
        return dataSource;
    }

    /**
	 * @param dataSource
	 *            the dataSource to set
	 */
    public void setDataSource(DataSource<DataRow> dataSource) {
        this.dataSource = dataSource;
    }

    /**
	 * @return the userContext
	 */
    public UserContext getUserContext() {
        return userContext;
    }

    /**
	 * @param userContext
	 *            the userContext to set
	 */
    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }

    public String getHeaderTitle() {
        if (this.meta != null) {
            if (!StringUtils.isEmpty(this.meta.getName())) {
                return this.meta.getName();
            } else if (!StringUtils.isEmpty(this.meta.getCode())) {
                return this.meta.getCode();
            } else if (!StringUtils.isEmpty(this.meta.getReportTemplate())) {
                return this.meta.getReportTemplate();
            } else {
                return String.valueOf(this.meta.getId());
            }
        } else {
            return "Report";
        }
    }

    public String getFileName() {
        return this.getHeaderTitle();
    }
}
