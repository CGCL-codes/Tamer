package welo.setup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.map.DataMap;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import welo.dao.SetupDatabaseDao;
import welo.dao.common.NonThreadBaseDao;
import welo.dao.misc.DBUtil;
import welo.ioc.guice.CmsInjector;
import welo.model.jcr.CmsCss;
import welo.model.jcr.CmsCssSection;
import welo.model.db.CmsFolder;
import welo.model.db.CmsPage;
import welo.model.db.CmsPagePanel;
import welo.model.db.CmsPanel;
import welo.model.db.KeyValue;
import welo.model.db.Role;
import welo.model.db.UserDetail;
import welo.model.db.WebUser;
import welo.utility.AppConfig;
import welo.utility.ImageUtil;
import welo.utility.MD5;
import welo.utility.file.FileUtil;
import welo.wicket.panel.StringPanel;

/**
 * Setup tables and initial data for the database if necessary
 * @author james.yong
 *
 */
public class SetupRepository {

    public static String img_layout1 = "layout1.gif";

    public static String img_layout8 = "layout8.gif";

    public static String css_layout1 = "layout1.css";

    public static String css_layout8 = "layout8.css";

    public boolean setup(final ObjectContentManager ocm, final String pathToWebInf) throws Exception {
        DataDomain dDomain = Configuration.getSharedConfiguration().getDomain();
        DataMap dMap = dDomain.getMap("WeloMap");
        NonThreadBaseDao dao = new CmsInjector().get(NonThreadBaseDao.class);
        if (!dao.existTable("auto_pk_support")) {
            new DBUtil().createTables(dMap);
        }
        if (ocm.getObjects(CmsCssSection.class, "/css") == null) {
            setupJcrSeedData(ocm);
        }
        SetupDatabaseDao dao2 = new CmsInjector().get(SetupDatabaseDao.class);
        if (!dao2.isWebUserExist()) {
            setupDBSeedData(pathToWebInf);
        }
        return true;
    }

    protected void setupJcrSeedData(ObjectContentManager ocm) throws Exception {
        CmsCss cssBean1 = new CmsCss();
        cssBean1.setPath("/css");
        cssBean1.setName(css_layout1);
        cssBean1.setContent(FileUtil.getStreamContent(getClass().getResourceAsStream(css_layout1)));
        cssBean1.setPreviewImage(ImageUtil.convertStreamToByte(getClass().getResourceAsStream(img_layout1)));
        CmsCss cssBean8 = new CmsCss();
        cssBean8.setPath("/css");
        cssBean8.setName(css_layout8);
        cssBean8.setContent(FileUtil.getStreamContent(getClass().getResourceAsStream(css_layout8)));
        cssBean8.setPreviewImage(ImageUtil.convertStreamToByte(getClass().getResourceAsStream(img_layout8)));
        List<CmsCss> cmsCssList = new ArrayList();
        cmsCssList.add(cssBean1);
        cmsCssList.add(cssBean8);
        CmsCssSection cssSectionBean = new CmsCssSection();
        cssSectionBean.setPath("/css");
        cssSectionBean.setName("TJK Design");
        cssSectionBean.setDescription("http://www.tjkdesign.com/articles/one_html_markup_many_css_layouts.asp");
        cssSectionBean.setSections("header,menu,content,sub-section,footer");
        cssSectionBean.setCssList(cmsCssList);
        ocm.insert(cssSectionBean);
        ocm.save();
    }

    protected void setupDBSeedData(String pathToWebInf) throws Exception {
        ObjectContext context = Configuration.getSharedConfiguration().getDomain("Welo").createDataContext();
        Role r = (Role) context.newObject(Role.class);
        r.setRoleId("PA");
        r.setDescription("Property agent");
        Role r1 = (Role) context.newObject(Role.class);
        r1.setRoleId("NPA");
        r1.setDescription("Not a property agent");
        Role r2 = (Role) context.newObject(Role.class);
        r2.setRoleId("ADMIN");
        r2.setDescription("Adminstrator");
        UserDetail ud = (UserDetail) context.newObject(UserDetail.class);
        ud.setGivenName("Admin");
        ud.setSurname("Demo");
        ud.setGender("M");
        ud.setEmail("welocms@gmail.com");
        ud.setJoinedDate(new Date());
        UserDetail ud1 = (UserDetail) context.newObject(UserDetail.class);
        ud1.setGivenName("Agent");
        ud1.setSurname("Demo");
        ud1.setGender("M");
        ud1.setEmail("welocms@gmail.com");
        ud1.setJoinedDate(new Date());
        WebUser wu = (WebUser) context.newObject(WebUser.class);
        wu.setLoginName("admin");
        wu.setPassword(MD5.crypt("admin"));
        wu.setUserDetail(ud);
        wu.setUserRole(r2);
        CmsPanel emptyPanel = (CmsPanel) context.newObject(CmsPanel.class);
        emptyPanel.setName("_EMPTY");
        emptyPanel.setClassName(StringPanel.class.getCanonicalName());
        CmsPage page_parent = (CmsPage) context.newObject(CmsPage.class);
        page_parent.setName("Parent");
        page_parent.setLayoutCss(css_layout8);
        page_parent.setPageLink("parent");
        CmsPagePanel headerPagePanel = (CmsPagePanel) context.newObject(CmsPagePanel.class);
        headerPagePanel.setDivName("header");
        headerPagePanel.setPanel(emptyPanel);
        CmsPagePanel footerPagePanel = (CmsPagePanel) context.newObject(CmsPagePanel.class);
        footerPagePanel.setDivName("footer");
        footerPagePanel.setPanel(emptyPanel);
        page_parent.addToCmsPagePanel(headerPagePanel);
        page_parent.addToCmsPagePanel(footerPagePanel);
        CmsPagePanel contentPagePanel = (CmsPagePanel) context.newObject(CmsPagePanel.class);
        contentPagePanel.setDivName("content");
        contentPagePanel.setPanel(emptyPanel);
        CmsPagePanel menuPagePanel = (CmsPagePanel) context.newObject(CmsPagePanel.class);
        menuPagePanel.setDivName("menu");
        menuPagePanel.setPanel(emptyPanel);
        CmsPagePanel subsectionPagePanel = (CmsPagePanel) context.newObject(CmsPagePanel.class);
        subsectionPagePanel.setDivName("sub-section");
        subsectionPagePanel.setPanel(emptyPanel);
        CmsPage page_test = (CmsPage) context.newObject(CmsPage.class);
        page_test.setName("Test");
        page_test.setLayoutCss(css_layout8);
        page_test.setPageParent(page_parent);
        page_test.addToCmsPagePanel(contentPagePanel);
        page_test.addToCmsPagePanel(menuPagePanel);
        page_test.addToCmsPagePanel(subsectionPagePanel);
        page_test.setPageLink("introduction/main/test");
        CmsFolder folder_main = (CmsFolder) context.newObject(CmsFolder.class);
        folder_main.setName("Main");
        CmsFolder folder_intro = (CmsFolder) context.newObject(CmsFolder.class);
        folder_intro.setName("Introduction");
        CmsFolder folder_website = (CmsFolder) context.newObject(CmsFolder.class);
        folder_website.setName("Website");
        folder_main.setRelParentFolder(folder_intro);
        folder_main.addToChildPage(page_test);
        folder_intro.setRelParentFolder(folder_website);
        folder_intro.addToRelChildFolder(folder_main);
        folder_website.addToRelChildFolder(folder_intro);
        folder_website.addToChildPage(page_parent);
        context.commitChanges();
        KeyValue kv = (KeyValue) context.newObject(KeyValue.class);
        kv.setKeyword(AppConfig.KEY_HOMEPAGE);
        kv.setValue(DataObjectUtils.intPKForObject(page_test) + "");
        context.commitChanges();
    }
}
