package org.ouobpo.tools.amazonchecker;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void configuationLoading() {
        Configuration config = Configuration.instance();
        assertThat(config.version(), is("1.0.0"));
        assertThat(config.browser(), is("explorer"));
        assertThat(config.isBrowserSet(), is(true));
        assertThat(config.proxyHost(), is("127.0.0.1"));
        assertThat(config.proxyPort(), is(80));
        assertThat(config.isProxySet(), is(true));
    }
}
