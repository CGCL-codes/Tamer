package welo.page.admin.website;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import welo.dao.CmsDaoFactory;
import welo.db.cayenne.CayenneHelper;
import welo.model.LabelValueBean;
import welo.model.db.CmsPage;
import welo.model.db.CmsPagePanel;
import welo.model.db.CmsPanel;
import welo.model.db.KeyValue;
import welo.model.jcr.CmsCss;
import welo.model.jcr.CmsCssSection;
import com.magicform.component.SimpleContainer;
import welo.wicket.form.LabelAndDropDown;
import welo.wicket.form.LabelAndTextField;
import welo.servlet.listener.AppContextListener;
import welo.utility.AppConfig;
import welo.utility.WeloUtil;
import welo.utility.jcr.JcrHelper;

/**
 * Help to construct the page. Capture the required panel for each div.
 * 
 * @see welo.model.db.CmsPage
 * @author james.yong
 */
public class PnCmsMgtPage extends Panel {

    /**
	 * Name for the page
	 */
    private int page_uuid;

    private String pageName;

    private boolean isHomePage;

    private String pageLink;

    protected String nameOfLayoutCss;

    private List<LabelValueBean> panelList;

    /**
	 * Id of the parent CmsPage
	 */
    private LabelValueBean parentPageId;

    public PnCmsMgtPage(String id, int uuid) throws Exception {
        super(id);
        init(uuid);
    }

    private void init(int uuid) throws Exception {
        this.isHomePage = false;
        this.page_uuid = uuid;
        CmsPage bean = CmsDaoFactory.getCmsPageDao().getCmsPageBean(this.page_uuid);
        this.pageName = bean.getName();
        String homepage_Uuid = CmsDaoFactory.getKeyValueDao().getValue(AppConfig.KEY_HOMEPAGE);
        if (homepage_Uuid.equals(bean.getId())) {
            setHomePage(true);
        } else {
            setHomePage(false);
        }
        nameOfLayoutCss = bean.getLayoutCss();
        parentPageId = bean.getPageParent() != null ? new LabelValueBean("", bean.getPageParent().getId() + "") : new LabelValueBean("", "");
        pageLink = bean.getPageLink();
        List<CmsPagePanel> map = bean.getCmsPagePanel();
        List<String> sections = WeloUtil.String2List(bean.getSections());
        panelList = new ArrayList<LabelValueBean>();
        for (String section : sections) {
            boolean istaken = false;
            for (CmsPagePanel pPanel : map) {
                if (section.equals(pPanel.getDivName())) {
                    panelList.add(new LabelValueBean(section, pPanel.getPanel().getId() + ""));
                    istaken = true;
                    break;
                }
            }
            if (!istaken) panelList.add(new LabelValueBean(section, ""));
        }
        add(new FeedbackPanel("feedback"));
        initForm();
    }

    private void initForm() throws Exception {
        Form form = new Lay3Col1TopPanelForm("constructorPanelForm");
        Panel tfName = new LabelAndTextField("layoutPanelName", "Name", new PropertyModel(this, "pageName"), true);
        form.add(tfName);
        Panel tfPageLink = new LabelAndTextField("page_link", "Page Link", new PropertyModel(this, "pageLink"), true);
        form.add(tfPageLink);
        CheckBox cb2 = new CheckBox("isHomePage", new PropertyModel(this, "isHomePage"));
        cb2.setLabel(new Model("Is Home Page?"));
        SimpleFormComponentLabel sfc2 = new SimpleFormComponentLabel("sfc2", cb2);
        form.add(sfc2);
        form.add(cb2);
        final SimpleContainer sc = new SimpleContainer("container");
        sc.setLabel(new Model("DIVs"));
        final List<LabelValueBean> list3 = CayenneHelper.convertToLvBean(CmsDaoFactory.getCmsPanelDao().getCmsPanels(), "name", "id");
        final ListView lw = new ListView("list", new PropertyModel(this, "panelList")) {

            @Override
            protected void populateItem(ListItem item) {
                item.add(new Label("index", new Model((item.getIndex() + 1) + "")));
                LabelValueBean bean = (LabelValueBean) item.getModelObject();
                item.add(new TextField("label", new PropertyModel(bean, "label")).setEnabled(false));
                DropDownChoice ddc = null;
                try {
                    ddc = new DropDownChoice("value1", new Model(bean), list3, new ChoiceRenderer("label", "value"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                item.add(ddc);
            }
        };
        lw.setOutputMarkupId(true);
        sc.add(lw);
        SimpleFormComponentLabel sfc3 = new SimpleFormComponentLabel("sfc3", sc);
        form.add(sfc3);
        form.add(sc);
        ObjectContentManager ocm = AppContextListener.getOCM();
        List list = (List) ocm.getObjects(JcrHelper.getOCMQuery(ocm, CmsCss.class));
        List nameList = new ArrayList();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            CmsCss object = (CmsCss) iterator.next();
            nameList.add(object.getName());
        }
        LabelAndDropDown ddcName = new LabelAndDropDown("nameOfLayoutCss", "Css for Layout", new PropertyModel(this, "nameOfLayoutCss"), nameList);
        ddcName.addToFormComponent(new AjaxFormComponentUpdatingBehavior("onchange") {

            protected void onUpdate(AjaxRequestTarget target) {
                System.out.println("nameOfLayoutCss=" + nameOfLayoutCss);
                target.addComponent(sc);
            }
        });
        form.add(ddcName);
        List<CmsPage> parentList = CmsDaoFactory.getCmsPageDao().getCmsPageBeans();
        for (CmsPage bean : parentList) {
            if (page_uuid == bean.getId()) {
                parentList.remove(bean);
                break;
            }
        }
        List<LabelValueBean> list2 = new ArrayList<LabelValueBean>();
        list2.add(new LabelValueBean("", ""));
        for (CmsPage bean : parentList) {
            list2.add(new LabelValueBean(bean.getName(), bean.getId() + ""));
        }
        LabelAndDropDown ddc = new LabelAndDropDown("parent", "Child of", new PropertyModel(this, "parentPageId"), list2, new ChoiceRenderer("label", "value"));
        form.add(ddc);
        System.out.println("=m=>" + parentPageId);
        for (LabelValueBean object : list2) {
            System.out.println("object=" + object);
        }
        add(form);
        add(new SubmitLink("submit", form));
    }

    class Lay3Col1TopPanelForm extends Form {

        public Lay3Col1TopPanelForm(String id) {
            super(id);
        }

        @Override
        public void onSubmit() {
            info(getModelObjectAsString());
            try {
                Map<String, CmsPanel> map = new HashMap<String, CmsPanel>();
                for (LabelValueBean bean : getPanelList()) {
                    CmsPanel cpbean = null;
                    if (bean.getValue() != null) {
                        cpbean = CmsDaoFactory.getCmsPanelDao().getCmsPanelFromName(bean.getValue());
                    }
                    map.put(bean.getLabel(), cpbean);
                }
                if (isHomePage()) {
                    KeyValue kvalue = CmsDaoFactory.getKeyValueDao().getBean(AppConfig.KEY_HOMEPAGE);
                    kvalue.setValue(getPage_uuid() + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isHomePage() {
        return isHomePage;
    }

    public void setHomePage(boolean isHomePage) {
        this.isHomePage = isHomePage;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public List<LabelValueBean> getPanelList() {
        return panelList;
    }

    public void setPanelList(List<LabelValueBean> panelList) {
        this.panelList = panelList;
    }

    public String getNameOfLayoutCss() {
        return nameOfLayoutCss;
    }

    public void setNameOfLayoutCss(String nameOfLayoutCss) {
        this.nameOfLayoutCss = nameOfLayoutCss;
    }

    public LabelValueBean getParentPageId() {
        return parentPageId;
    }

    public void setParentPageId(LabelValueBean parentPageId) {
        this.parentPageId = parentPageId;
    }

    public int getPage_uuid() {
        return page_uuid;
    }

    public void setPage_uuid(int page_uuid) {
        this.page_uuid = page_uuid;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }
}
