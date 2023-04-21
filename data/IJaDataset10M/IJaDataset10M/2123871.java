package edu.iit.cs;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeClass;

public class AbstractDaoTest {

    private WootubeDao wootubeDao;

    public void setWootubeDao(WootubeDao wootubeDao) {
        this.wootubeDao = wootubeDao;
    }

    public WootubeDao getWootubeDao() {
        return wootubeDao;
    }

    @BeforeClass(alwaysRun = true)
    public void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "classpath:wootube-datasource.xml", "classpath:wootube-app-context.xml" });
        setWootubeDao((WootubeDao) context.getBean("wootubeDao"));
    }
}
