package net.sf.jasperreports.jsf.fill.providers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.jsf.component.UIDataSource;
import net.sf.jasperreports.jsf.fill.AbstractJRDataSourceFiller;
import net.sf.jasperreports.jsf.fill.FillerException;

public class CustomDataSourceFiller extends AbstractJRDataSourceFiller {

    private static final Logger logger = Logger.getLogger(BeanFiller.class.getPackage().getName(), "net.sf.jasperreports.jsf.LogMessages");

    protected CustomDataSourceFiller(final UIDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected JRDataSource getJRDataSource(final FacesContext context) throws FillerException {
        JRDataSource dataSource = (JRDataSource) getDataSourceComponent().getValue();
        if (dataSource == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020", getDataSourceComponent().getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        }
        return dataSource;
    }
}
