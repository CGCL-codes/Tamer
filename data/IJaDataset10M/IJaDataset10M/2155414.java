package whf.framework.report.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;
import whf.framework.meta.entity.IProperty;
import whf.framework.report.export.util.ExportUtils;
import whf.framework.util.BeanUtils;
import whf.framework.util.StringUtils;
import whf.framework.web.tag.TableColumn;

/**
 * @author wanghaifeng
 * @create Dec 14, 2006 2:40:42 PM
 * 
 */
public class EntityCollectionDataSource extends JRAbstractBeanDataSource {

    private List rows = new ArrayList();

    private int position = -1;

    public EntityCollectionDataSource(Collection beans) {
        super(true);
        this.rows.addAll(beans);
        this.position = -1;
    }

    public void moveFirst() throws JRException {
        this.position = -1;
    }

    public boolean next() throws JRException {
        this.position++;
        return this.position < this.rows.size();
    }

    public Object getFieldValue(JRField field) throws JRException {
        if (this.position == -1) this.position = 0;
        Object bo = this.position >= 0 && this.position < this.rows.size() ? this.rows.get(this.position) : null;
        if (bo == null) return null;
        String property = field.getName();
        IProperty prop = null;
        if (ExportUtils.getTableModel() != null) {
            List<TableColumn> columns = ExportUtils.getTableModel().getColumns();
            for (TableColumn col : columns) {
                if (StringUtils.equals(property, col.getProperty())) {
                    prop = col.getPropertyObject();
                    break;
                }
            }
        }
        if (prop != null) {
            try {
                return BeanUtils.translatePropertyToString(bo, prop);
            } catch (Exception e) {
                return null;
            }
        } else if (!StringUtils.isEmpty(property)) {
            return BeanUtils.getSimpleProperty(bo, property);
        }
        return null;
    }
}
