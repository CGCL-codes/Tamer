package net.sf.jasperreports.jsf.engine.converters;

import net.sf.jasperreports.jsf.engine.converters.XmlSourceConverter;
import net.sf.jasperreports.jsf.engine.JRDataSourceWrapper;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.convert.Source;
import net.sf.jasperreports.jsf.test.dummy.DummyUIReport;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Theories.class)
public class XmlSourceConverterTest {

    @DataPoint
    public static final Object NULL_VALUE = null;

    private MockFacesEnvironment facesEnv;

    private UIReport component;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        component = new DummyUIReport();
    }

    @After
    public void dispose() {
        facesEnv.release();
        facesEnv = null;
    }

    @Theory
    public void nullValueReturnsEmptyDS(Object value) {
        assumeThat(value, nullValue());
        component.setValue(value);
        XmlSourceConverter converter = new XmlSourceConverter();
        Source source = converter.createSource(facesEnv.getFacesContext(), component, value);
        assertThat(source, notNullValue());
        assertThat(source, is(JRDataSourceWrapper.class));
        JRDataSource ds = (JRDataSource) source.getWrappedSource();
        assertThat(ds, notNullValue());
        assertThat(ds, is(JREmptyDataSource.class));
    }
}
