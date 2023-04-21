package my.mas.jdl.common;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import java.util.Properties;
import my.mas.jdl.pkg.resources.PropFileAppTrial;
import my.mas.jdl.test.Resources;
import org.junit.Test;

public class PropFileTest {

    @Test
    public void getProperties() {
        Properties properties = null;
        assertThat(properties, nullValue());
        properties = PropFile.getProperties("/" + Resources.APP_TRY, true);
        assertThat(properties, notNullValue());
        assertThat(properties.getProperty(Resources.MAP_ID), is(Resources.MAP_NAME));
        assertThat(properties.getProperty(Resources.MAP_KEY), is(Resources.MAP_VALUE));
        properties = PropFile.getProperties(FileUtil.getFile(Resources.APP_TRY).toString(), false);
        assertThat(properties, notNullValue());
        assertThat(properties.getProperty(Resources.MAP_ID), is(Resources.MAP_NAME));
        assertThat(properties.getProperty(Resources.MAP_KEY), is(Resources.MAP_VALUE));
    }

    @Test
    public void getProperty() {
        assertThat(PropFileAppTrial.getProperty(Resources.MAP_ID), is(Resources.MAP_NAME));
        assertThat(PropFileAppTrial.getProperty(Resources.MAP_KEY), is(Resources.MAP_VALUE));
        assertThat(PropFileAppTrial.getProperty("code"), nullValue());
        assertThat(PropFileAppTrial.getProperty("descr"), nullValue());
    }
}
