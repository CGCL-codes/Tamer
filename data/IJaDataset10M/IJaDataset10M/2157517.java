package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Date;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.model.domain.Site;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

public class SiteTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");

    SiteMapper siteMapper = ctx.getBean("siteMapper", SiteMapper.class);

    @Test
    public void test() {
        insert();
        selectAndUpdate();
        delete();
    }

    public void insert() {
        Site site = new Site();
        site.setSiteId("test_www_sohu_com_2");
        site.setSiteName("搜狐");
        site.setSiteType("general");
        site.setSiteDomain("www.sohu.com");
        site.setUrl("http://www.sohu.com");
        siteMapper.insert(site);
    }

    public void selectAndUpdate() {
        List<Site> sites = siteMapper.getSites(buildParam(0, 10, "id desc", "com"));
        for (Site site : sites) {
            System.out.println(site.getSiteId());
        }
        Site site = siteMapper.getBySiteId("test_www_sohu_com_2");
        site.setUrl("http://www.sohu.com/");
        site.setLastUpdateUser("system");
        site.setUpdateTime(new Date());
        siteMapper.update(site);
    }

    public void delete() {
        siteMapper.deleteBySiteId("test_www_sohu_com_2");
    }
}
