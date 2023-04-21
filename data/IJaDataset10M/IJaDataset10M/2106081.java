package info.joseluismartin.reporting.datasource;

import info.joseluismartin.dao.Page;
import info.joseluismartin.dao.PageableDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Adapter to use PageableDataSource and Pages as JRDataSource.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class PageJRDatasourceAdapter extends JRAbstractBeanDataSource {

    private Page<Object> page;

    private Object currentObject;

    private int index = 0;

    /**
	 * 
	 */
    public PageJRDatasourceAdapter(PageableDataSource<Object> ds) {
        this(true);
        page = new Page<Object>();
        page.setPageableDataSource(ds);
    }

    /**
	 * @param isUseFieldDescription
	 */
    public PageJRDatasourceAdapter(boolean isUseFieldDescription) {
        super(isUseFieldDescription);
    }

    /**
	 * {@inheritDoc}
	 */
    public void moveFirst() throws JRException {
        page.firstPage();
    }

    /**
	 * {@inheritDoc}
	 */
    public Object getFieldValue(JRField field) throws JRException {
        try {
            return getBeanProperty(currentObject, field.getName());
        } catch (Exception e) {
            throw new JRException(e);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean next() throws JRException {
        if (getCurrentObject()) {
            return true;
        }
        if (page.hasNext()) {
            page.nextPage();
            index = 0;
            if (getCurrentObject()) ;
            return true;
        }
        return false;
    }

    private boolean getCurrentObject() {
        if (index < page.getData().size()) {
            currentObject = page.getData().get(index++);
            return true;
        }
        return false;
    }

    /**
	 * @return the page
	 */
    public Page<Object> getPage() {
        return page;
    }

    /**
	 * @param page the page to set
	 */
    public void setPage(Page<Object> page) {
        this.page = page;
        if (page.getData() == null || page.getData().size() == 0) page.load();
    }
}
