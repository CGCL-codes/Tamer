package net.sf.webwarp.modules.partner.beans;

import net.sf.webwarp.modules.partner.type.impl.TypeInstaller;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class InstallTest extends APartnerTestBase {

    private TypeInstaller installer;

    private LocalSessionFactoryBean sessionFactoryBean;

    @Before
    public void setup() throws Exception {
        this.sessionFactoryBean.dropDatabaseSchema();
        this.sessionFactoryBean.createDatabaseSchema();
    }

    @Test
    public void testInstall() {
        installer.install();
    }

    public void setSessionFactoryBean(LocalSessionFactoryBean sessionFactoryBean) {
        this.sessionFactoryBean = sessionFactoryBean;
    }

    public void setTypeInstaller(TypeInstaller installer) {
        this.installer = installer;
    }
}
