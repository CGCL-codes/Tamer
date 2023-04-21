package net.sf.jasperreports.jsf.engine.converters;

import net.sf.jasperreports.jsf.engine.JRDataSourceWrapper;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.convert.Source;

/**
 *
 * @author aalonsodominguez
 */
public class ResultSetSourceConverter extends SourceConverterBase {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8234770955803696671L;

    private static final Logger logger = Logger.getLogger(ResultSetSourceConverter.class.getPackage().getName(), Constants.LOG_MESSAGES_BUNDLE);

    @Override
    protected Source createSource(FacesContext context, UIComponent component, Object value) {
        JRDataSource dataSource;
        final ResultSet rs = (ResultSet) value;
        if (rs == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020", component.getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        } else {
            dataSource = new JRResultSetDataSource(rs);
        }
        return new JRDataSourceWrapper(dataSource);
    }
}
