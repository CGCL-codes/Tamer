package org.azrul.epice.web;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Calendar;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.ImageComponent;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.Link;
import com.sun.webui.jsf.component.Listbox;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.component.RadioButton;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.TextArea;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.model.Option;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.event.ValueChangeEvent;
import org.azrul.epice.dao.FileRepositoryDAO;
import org.azrul.epice.dao.factory.FileRepositoryDAOFactory;
import org.azrul.epice.domain.Item;
import org.azrul.epice.domain.Person;
import org.azrul.epice.dao.ItemDAO;
import org.azrul.epice.dao.factory.ItemDAOFactory;
import org.azrul.epice.dao.PersonDAO;
import org.azrul.epice.dao.factory.PersonDAOFactory;
import org.azrul.epice.dao.query.ChildrenItemsQuery;
import org.azrul.epice.dao.query.factory.ChildrenItemsQueryFactory;
import org.azrul.epice.domain.FileRepository;
import org.azrul.epice.manager.NewItemManager;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Azrul Hasni MADISA
 */
public class PgNewItem extends AbstractPageBean {

    private ItemDAO itemDAO = ItemDAOFactory.getInstance();

    private PersonDAO personDAO = PersonDAOFactory.getInstance();

    private FileRepositoryDAO fileRepoDAO = FileRepositoryDAOFactory.getInstance();

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private Page page1 = new Page();

    public Page getPage1() {
        return page1;
    }

    public void setPage1(Page p) {
        this.page1 = p;
    }

    private Html html1 = new Html();

    public Html getHtml1() {
        return html1;
    }

    public void setHtml1(Html h) {
        this.html1 = h;
    }

    private Head head1 = new Head();

    public Head getHead1() {
        return head1;
    }

    public void setHead1(Head h) {
        this.head1 = h;
    }

    private Link link1 = new Link();

    public Link getLink1() {
        return link1;
    }

    public void setLink1(Link l) {
        this.link1 = l;
    }

    private Body body1 = new Body();

    public Body getBody1() {
        return body1;
    }

    public void setBody1(Body b) {
        this.body1 = b;
    }

    private Form form1 = new Form();

    public Form getForm1() {
        return form1;
    }

    public void setForm1(Form f) {
        this.form1 = f;
    }

    private TextField tfTags = new TextField();

    public TextField getTfTags() {
        return tfTags;
    }

    public void setTfTags(TextField tf) {
        this.tfTags = tf;
    }

    private TextArea taAction = new TextArea();

    public TextArea getTaAction() {
        return taAction;
    }

    public void setTaAction(TextArea ta) {
        this.taAction = ta;
    }

    private TextField tfSupervisors = new TextField();

    public TextField getTfSupervisors() {
        return tfSupervisors;
    }

    public void setTfSupervisors(TextField tf) {
        this.tfSupervisors = tf;
    }

    private Button btnSubmit = new Button();

    public Button getBtnSubmit() {
        return btnSubmit;
    }

    public void setBtnSubmit(Button b) {
        this.btnSubmit = b;
    }

    private Button btnBack = new Button();

    public Button getBtnBack() {
        return btnBack;
    }

    public void setBtnBack(Button b) {
        this.btnBack = b;
    }

    private TextField tfSubject = new TextField();

    public TextField getTfSubject() {
        return tfSubject;
    }

    public void setTfSubject(TextField tf) {
        this.tfSubject = tf;
    }

    private Calendar calDeadline = new Calendar();

    public Calendar getCalDeadline() {
        return calDeadline;
    }

    public void setCalDeadline(Calendar c) {
        this.calDeadline = c;
    }

    private StaticText staticText1 = new StaticText();

    public StaticText getStaticText1() {
        return staticText1;
    }

    public void setStaticText1(StaticText st) {
        this.staticText1 = st;
    }

    private TextField tfTo = new TextField();

    public TextField getTfTo() {
        return tfTo;
    }

    public void setTfTo(TextField tf) {
        this.tfTo = tf;
    }

    private TextField tfLink = new TextField();

    public TextField getTfLink() {
        return tfLink;
    }

    public void setTfLink(TextField tf) {
        this.tfLink = tf;
    }

    private Listbox lbLinks = new Listbox();

    public Listbox getLbLinks() {
        return lbLinks;
    }

    public void setLbLinks(Listbox l) {
        this.lbLinks = l;
    }

    private Button btnAddToLinks = new Button();

    public Button getBtnAddToLinks() {
        return btnAddToLinks;
    }

    public void setBtnAddToLinks(Button b) {
        this.btnAddToLinks = b;
    }

    private Button btnRemoveFromLinks = new Button();

    public Button getBtnRemoveFromLinks() {
        return btnRemoveFromLinks;
    }

    public void setBtnRemoveFromLinks(Button b) {
        this.btnRemoveFromLinks = b;
    }

    private StaticText stTitle = new StaticText();

    public StaticText getStTitle() {
        return stTitle;
    }

    public void setStTitle(StaticText st) {
        this.stTitle = st;
    }

    private StaticText staticText3 = new StaticText();

    public StaticText getStaticText3() {
        return staticText3;
    }

    public void setStaticText3(StaticText st) {
        this.staticText3 = st;
    }

    private StaticText stSupervisors = new StaticText();

    public StaticText getStSupervisors() {
        return stSupervisors;
    }

    public void setStSupervisors(StaticText st) {
        this.stSupervisors = st;
    }

    private ImageComponent image1 = new ImageComponent();

    public ImageComponent getImage1() {
        return image1;
    }

    public void setImage1(ImageComponent ic) {
        this.image1 = ic;
    }

    private Button btnGotoAttachment = new Button();

    public Button getBtnGotoAttachment() {
        return btnGotoAttachment;
    }

    public void setBtnGotoAttachment(Button b) {
        this.btnGotoAttachment = b;
    }

    public FilteredOptions getLbEmailBuddiesDefaultOptions() {
        return getSessionBean1().getToUsersFilteredOptions();
    }

    public void setLbEmailBuddiesDefaultOptions(FilteredOptions dol) {
        getSessionBean1().setToUsersFilteredOptions(dol);
    }

    public FilteredOptions getLbSupervisorsDefaultOptions() {
        return getSessionBean1().getSupervisorsFilteredOptions();
    }

    public void setLbSupervisorsDefaultOptions(FilteredOptions dol) {
        getSessionBean1().setSupervisorsFilteredOptions(dol);
    }

    private Label label1 = new Label();

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label l) {
        this.label1 = l;
    }

    private Label label8 = new Label();

    public Label getLabel8() {
        return label8;
    }

    public void setLabel8(Label l) {
        this.label8 = l;
    }

    private Label label3 = new Label();

    public Label getLabel3() {
        return label3;
    }

    public void setLabel3(Label l) {
        this.label3 = l;
    }

    private Label label5 = new Label();

    public Label getLabel5() {
        return label5;
    }

    public void setLabel5(Label l) {
        this.label5 = l;
    }

    private Label label2 = new Label();

    public Label getLabel2() {
        return label2;
    }

    public void setLabel2(Label l) {
        this.label2 = l;
    }

    private Label lbCurrentSupervisors = new Label();

    public Label getLbCurrentSupervisors() {
        return lbCurrentSupervisors;
    }

    public void setLbCurrentSupervisors(Label l) {
        this.lbCurrentSupervisors = l;
    }

    private Label label9 = new Label();

    public Label getLabel9() {
        return label9;
    }

    public void setLabel9(Label l) {
        this.label9 = l;
    }

    private Label label4 = new Label();

    public Label getLabel4() {
        return label4;
    }

    public void setLabel4(Label l) {
        this.label4 = l;
    }

    private String toUserFilter = "";

    private String supervisorFilter = "";

    private String link;

    private String selectedLink;

    private TextField tfDeadlineTime = new TextField();

    public TextField getTfDeadlineTime() {
        return tfDeadlineTime;
    }

    public void setTfDeadlineTime(TextField tf) {
        this.tfDeadlineTime = tf;
    }

    private RadioButton rbDeadlineAM = new RadioButton();

    public RadioButton getRbDeadlineAM() {
        return rbDeadlineAM;
    }

    public void setRbDeadlineAM(RadioButton rb) {
        this.rbDeadlineAM = rb;
    }

    private RadioButton rbDeadlinePM = new RadioButton();

    public RadioButton getRbDeadlinePM() {
        return rbDeadlinePM;
    }

    public void setRbDeadlinePM(RadioButton rb) {
        this.rbDeadlinePM = rb;
    }

    private Label label6 = new Label();

    public Label getLabel6() {
        return label6;
    }

    public void setLabel6(Label l) {
        this.label6 = l;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public PgNewItem() {
    }

    /**
     * <p>Callback method that is called whenever a page is navigated to,
     * either directly via a URL, or indirectly via page navigation.
     * Customize this method to acquire resources that will be needed
     * for event handlers and lifecycle methods, whether or not this
     * page is performing post back processing.</p>
     * 
     * <p>Note that, if the current request is a postback, the property
     * values of the components do <strong>not</strong> represent any
     * values submitted with this request.  Instead, they represent the
     * property values that were saved for this view when it was rendered.</p>
     */
    @Override
    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("PgNewItem Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
        if (("PgReceivedItem").equals(getSessionBean1().getPreviousPage())) {
            stTitle.setText("Delegate item");
            Item parent = getSessionBean1().getParent();
            if (parent != null) {
                if (getSessionBean1().getCurrentFileRepository() == null) {
                    getSessionBean1().setCurrentFileRepository(parent.getFileRepository());
                }
                getTfSubject().setText("FWD:" + parent.getSubject());
                getTaAction().setText("=============================================\n" + parent.getDescription());
                if (parent.getDeadLine() != null) {
                    calDeadline.setValue(parent.getDeadLine());
                    GregorianCalendar gcal = (GregorianCalendar) GregorianCalendar.getInstance();
                    gcal.setTime(parent.getDeadLine());
                    tfDeadlineTime.setValue(gcal.getTime());
                    if (gcal.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
                        rbDeadlineAM.setSelected(true);
                        rbDeadlinePM.setSelected(false);
                    } else {
                        rbDeadlineAM.setSelected(false);
                        rbDeadlinePM.setSelected(true);
                    }
                } else {
                    calDeadline.setRendered(false);
                    label5.setRendered(false);
                    label6.setRendered(false);
                    tfDeadlineTime.setRendered(false);
                    rbDeadlineAM.setRendered(false);
                    rbDeadlinePM.setRendered(false);
                }
                StringBuilder emailsSup = new StringBuilder();
                for (Person sup : parent.getSupervisors()) {
                    emailsSup.append(sup.getEmail() + ",");
                }
                int commaPosition = emailsSup.lastIndexOf(",");
                if (commaPosition == emailsSup.length() - 1 && commaPosition > -1) {
                    emailsSup.deleteCharAt(commaPosition);
                }
                stSupervisors.setText(emailsSup.toString());
                StringBuilder tags = new StringBuilder();
                if (!parent.getTags().isEmpty()) {
                    for (String tag : parent.getTags()) {
                        tags.append(tag);
                        tags.append(",");
                    }
                    tags.deleteCharAt(tags.lastIndexOf(","));
                    tfTags.setText(tags.toString());
                }
            }
            Item editedItem = getSessionBean1().getCurrentEditedItem();
            if (editedItem != null) {
                setupUIFromItem(editedItem);
                getSessionBean1().setCurrentEditedItem(null);
            }
            stSupervisors.setVisible(true);
            lbCurrentSupervisors.setVisible(true);
        } else if (("PgSentItem").equals(getSessionBean1().getPreviousPage())) {
            stTitle.setText("Edit item");
            Item editedItem = getSessionBean1().getCurrentEditedItem();
            if (editedItem != null) {
                setupUIFromItem(editedItem);
                getSessionBean1().setCurrentEditedItem(null);
            }
            stSupervisors.setVisible(false);
            lbCurrentSupervisors.setVisible(false);
        } else {
            stTitle.setText("New item");
            Item editedItem = getSessionBean1().getCurrentEditedItem();
            if (editedItem != null) {
                setupUIFromItem(editedItem);
                getSessionBean1().setCurrentEditedItem(null);
            } else {
                if (("REFERENCE").equals(getSessionBean1().getItemType())) {
                    calDeadline.setRendered(false);
                    label5.setRendered(false);
                    label6.setRendered(false);
                    tfDeadlineTime.setRendered(false);
                    rbDeadlineAM.setRendered(false);
                    rbDeadlinePM.setRendered(false);
                } else {
                    calDeadline.setRendered(true);
                    label5.setRendered(true);
                    label6.setRendered(true);
                    tfDeadlineTime.setRendered(true);
                    rbDeadlineAM.setRendered(true);
                    rbDeadlinePM.setRendered(true);
                }
                stSupervisors.setVisible(false);
                lbCurrentSupervisors.setVisible(false);
                tfDeadlineTime.setText(ResourceBundle.getBundle("epice").getString("DEFAULT_DEADLINE_TIME"));
                if (ResourceBundle.getBundle("epice").getString("DEFAULT_DEADLINE_AM_PM").equals("AM")) {
                    rbDeadlineAM.setValue(true);
                    rbDeadlinePM.setValue(false);
                } else {
                    rbDeadlineAM.setValue(false);
                    rbDeadlinePM.setValue(true);
                }
            }
        }
        Person user = getSessionBean1().getCurrentUser();
        FilteredOptions toUsersOptions = new FilteredOptions(user.getBuddies());
        getSessionBean1().setToUsersFilteredOptions(toUsersOptions);
        FilteredOptions supervisorsOptions = new FilteredOptions(user.getSupervisors());
        getSessionBean1().setSupervisorsFilteredOptions(supervisorsOptions);
    }

    /**
     * <p>Callback method that is called after the component tree has been
     * restored, but before any event processing takes place.  This method
     * will <strong>only</strong> be called on a postback request that
     * is processing a form submit.  Customize this method to allocate
     * resources that will be required in your event handlers.</p>
     */
    @Override
    public void preprocess() {
    }

    /**
     * <p>Callback method that is called just before rendering takes place.
     * This method will <strong>only</strong> be called for the page that
     * will actually be rendered (and not, for example, on a page that
     * handled a postback and then navigated to a different page).  Customize
     * this method to allocate resources that will be required for rendering
     * this page.</p>
     */
    @Override
    public void prerender() {
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called (regardless of whether
     * or not this was the page that was actually rendered).  Customize this
     * method to release resources acquired in the <code>init()</code>,
     * <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).</p>
     */
    @Override
    public void destroy() {
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    public void lbSupervisor_processValueChange(ValueChangeEvent event) {
    }

    public String btnAddSupervisor_action() {
        return null;
    }

    public String btnSubmit_action() {
        SessionBean1 sessionBean1 = getSessionBean1();
        String toEmails = (String) getTfTo().getText();
        String supervisors = (String) getTfSupervisors().getText();
        String tags = (String) getTfTags().getText();
        Date deadLine = null;
        if (calDeadline.getSelectedDate() != null) {
            GregorianCalendar gcalDeadLineDate = (GregorianCalendar) GregorianCalendar.getInstance();
            gcalDeadLineDate.setTime(calDeadline.getSelectedDate());
            Date deadlineTime = (Date) tfDeadlineTime.getValue();
            if (deadlineTime != null) {
                GregorianCalendar gcalHM = (GregorianCalendar) GregorianCalendar.getInstance();
                gcalHM.setTime(deadlineTime);
                gcalDeadLineDate.set(GregorianCalendar.HOUR, gcalHM.get(GregorianCalendar.HOUR));
                gcalDeadLineDate.set(GregorianCalendar.MINUTE, gcalHM.get(GregorianCalendar.MINUTE));
                if (rbDeadlineAM.getSelected() != null && rbDeadlinePM.getSelected() != null) {
                    if (rbDeadlineAM.getSelected().equals(true) && rbDeadlinePM.getSelected().equals(false)) {
                        gcalDeadLineDate.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
                    } else if (rbDeadlineAM.getSelected().equals(false) && rbDeadlinePM.getSelected().equals(true)) {
                        gcalDeadLineDate.set(GregorianCalendar.AM_PM, GregorianCalendar.PM);
                    }
                }
            }
            deadLine = gcalDeadLineDate.getTime();
        }
        String action = (String) (getTaAction().getText());
        String subject = (String) (getTfSubject().getText());
        NewItemManager newItemManager = new NewItemManager();
        List<String> linkList = new ArrayList<String>();
        Option[] linkOptions = getSessionBean1().getLinkOptions();
        for (Option linkOption : linkOptions) {
            linkList.add((String) linkOption.getValue());
        }
        Set<Item> newItems = newItemManager.createAndSentNewItems(getSessionBean1().getCurrentUser(), toEmails, supervisors, tags, deadLine, action, subject, getSessionBean1().getCurrentFileRepository(), getSessionBean1().getParent(), linkList);
        if (newItems != null) {
            if (newItems.iterator().hasNext()) {
                getApplicationBean1().addTags(newItems.iterator().next().getTags());
                Item parent = newItems.iterator().next().getParent();
                if (parent != null) {
                    sessionBean1.setCurrentReceivedItem(parent);
                    sessionBean1.refreshSearchItemsDP(itemDAO.runItemsQuery(sessionBean1.getCurrentUser(), sessionBean1.getSearchItemsQuery()));
                    ChildrenItemsQuery query = ChildrenItemsQueryFactory.getInstance();
                    query.setParentItem(parent);
                    sessionBean1.setChildrenItemsQuery(query);
                    sessionBean1.refreshChildrenItemsDP(itemDAO.runItemsQuery(sessionBean1.getCurrentUser(), sessionBean1.getChildrenItemsQuery()));
                }
                sessionBean1.setParent(parent);
                sessionBean1.setCurrentFileRepository(newItems.iterator().next().getFileRepository());
                Item newItem = newItems.iterator().next();
                personDAO.addBuddies(sessionBean1.getCurrentUser(), newItem.getToUsers());
            }
            sessionBean1.setCurrentFileRepository(newItems.iterator().next().getFileRepository());
            for (Item it : newItems) {
                if (sessionBean1.getCurrentUser().equals(it.getToUser())) {
                    sessionBean1.setCurrentReceivedItem(it);
                    sessionBean1.setPreviousPage("PgMenu");
                    return "gotoReceivedItem";
                }
            }
            sessionBean1.setLinks(new HashSet<URL>());
            return btnBack_action();
        } else {
            return null;
        }
    }

    public String btnBack_action() {
        if (("PgReceivedItem").equals(getSessionBean1().getPreviousPage())) {
            getSessionBean1().popPreviousPage();
            return "gotoReceivedItem";
        } else if (("PgSentItem").equals(getSessionBean1().getPreviousPage())) {
            getSessionBean1().popPreviousPage();
            return "gotoSentItem";
        } else {
            getSessionBean1().popPreviousPage();
            return "gotoMenu";
        }
    }

    public void ddAcceptReject_processValueChange(ValueChangeEvent event) {
    }

    public String btnDelegate_action() {
        return null;
    }

    public String btnRemoveFromLinks_action() {
        return null;
    }

    public String btnAddToRecipients_action() {
        return null;
    }

    public String btnGotoAttachment_action() {
        SessionBean1 sessionBean1 = getSessionBean1();
        String toEmails = (String) getTfTo().getText();
        String supervisors = (String) getTfSupervisors().getText();
        String tags = (String) getTfTags().getText();
        if (sessionBean1.getCurrentFileRepository() == null) {
            int state = 0;
            if (getSessionBean1().getParent() == null) {
                state = 1;
            } else {
                if (getSessionBean1().getParent().getFileRepository() == null) {
                    state = 1;
                } else {
                    state = 2;
                }
            }
            if (state == 1) {
                FileRepository fileRepository = fileRepoDAO.create(sessionBean1.getCurrentUser(), "File Repository For " + sessionBean1.getCurrentUser().getName());
                sessionBean1.setEnableUpload(true);
                sessionBean1.setCurrentFileRepository(fileRepository);
            } else {
                FileRepository fileRepository = fileRepoDAO.refresh(sessionBean1.getParent().getFileRepository());
                sessionBean1.setEnableUpload(true);
                sessionBean1.setCurrentFileRepository(fileRepository);
                sessionBean1.getCurrentFileRepository().refreshDataProvider(sessionBean1.getCurrentUser());
            }
        } else {
            FileRepository fileRepository = fileRepoDAO.refresh(sessionBean1.getCurrentFileRepository());
            sessionBean1.setCurrentFileRepository(fileRepository);
            sessionBean1.getCurrentFileRepository().refreshDataProvider(sessionBean1.getCurrentUser());
            getSessionBean1().setEnableUpload(true);
        }
        Date deadline = null;
        if (calDeadline.getSelectedDate() != null) {
            GregorianCalendar gcalDeadLineDate = (GregorianCalendar) GregorianCalendar.getInstance();
            gcalDeadLineDate.setTime(calDeadline.getSelectedDate());
            Date deadlineTime = (Date) tfDeadlineTime.getValue();
            if (deadlineTime != null) {
                GregorianCalendar gcalHM = (GregorianCalendar) GregorianCalendar.getInstance();
                gcalHM.setTime(deadlineTime);
                gcalDeadLineDate.set(GregorianCalendar.HOUR, gcalHM.get(GregorianCalendar.HOUR));
                gcalDeadLineDate.set(GregorianCalendar.MINUTE, gcalHM.get(GregorianCalendar.MINUTE));
                if (rbDeadlineAM.getSelected() != null && rbDeadlinePM.getSelected() != null) {
                    if (rbDeadlineAM.getSelected().equals(true) && rbDeadlinePM.getSelected().equals(false)) {
                        gcalDeadLineDate.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
                    } else if (rbDeadlineAM.getSelected().equals(false) && rbDeadlinePM.getSelected().equals(true)) {
                        gcalDeadLineDate.set(GregorianCalendar.AM_PM, GregorianCalendar.PM);
                    }
                }
            }
            deadline = gcalDeadLineDate.getTime();
        }
        String action = (String) (getTaAction().getText());
        String subject = (String) (getTfSubject().getText());
        NewItemManager newItemManager = new NewItemManager();
        List<String> linkList = new ArrayList<String>();
        Option[] linkOptions = getSessionBean1().getLinkOptions();
        for (Option linkOption : linkOptions) {
            linkList.add((String) linkOption.getValue());
        }
        Set<Item> newItems = newItemManager.createNewItemsWithoutPersisting(getSessionBean1().getCurrentUser(), toEmails, supervisors, tags, deadline, action, subject, getSessionBean1().getCurrentFileRepository(), getSessionBean1().getParent(), linkList);
        if (!newItems.isEmpty()) {
            sessionBean1.setCurrentEditedItem(newItems.iterator().next());
        }
        sessionBean1.setPreviousPage("PgNewItem");
        return "gotoFileRepository";
    }

    private void setupUIFromItem(Item item) {
        StringBuilder toUsersEmail = new StringBuilder();
        for (Person toUser : item.getToUsers()) {
            toUsersEmail.append(toUser.getEmail() + ",");
        }
        int commaPositionTo = toUsersEmail.lastIndexOf(",");
        if (commaPositionTo == toUsersEmail.length() - 1 && commaPositionTo > -1) {
            toUsersEmail.deleteCharAt(toUsersEmail.lastIndexOf(","));
        }
        getTfTo().setText(toUsersEmail.toString());
        getTfSubject().setText(item.getSubject());
        getTaAction().setText(item.getDescription());
        if (item.getDeadLine() != null) {
            getCalDeadline().setSelectedDate(item.getDeadLine());
            GregorianCalendar gcal = (GregorianCalendar) GregorianCalendar.getInstance();
            gcal.setTime(item.getDeadLine());
            SimpleDateFormat sdf = new SimpleDateFormat(ResourceBundle.getBundle("epice").getString("HM_ONLY_DATE_FORMAT"));
            tfDeadlineTime.setText(sdf.format(gcal.getTime()));
            if (gcal.get(GregorianCalendar.AM_PM) == GregorianCalendar.AM) {
                rbDeadlineAM.setSelected(true);
                rbDeadlinePM.setSelected(false);
            } else {
                rbDeadlineAM.setSelected(false);
                rbDeadlinePM.setSelected(true);
            }
        } else {
            calDeadline.setRendered(false);
            label5.setRendered(false);
            label6.setRendered(false);
            tfDeadlineTime.setRendered(false);
            rbDeadlineAM.setRendered(false);
            rbDeadlinePM.setRendered(false);
        }
        StringBuilder tags = new StringBuilder();
        if (!item.getTags().isEmpty()) {
            for (String tag : item.getTags()) {
                tags.append(tag);
                tags.append(",");
            }
            tags.deleteCharAt(tags.lastIndexOf(","));
            getTfTags().setText(tags.toString());
        }
        StringBuilder emailsSup = new StringBuilder();
        for (Person sup : item.getSupervisors()) {
            emailsSup.append(sup.getEmail() + ",");
        }
        int commaPositionSup = emailsSup.lastIndexOf(",");
        if (commaPositionSup == emailsSup.length() - 1 && commaPositionSup > -1) {
            emailsSup.deleteCharAt(emailsSup.lastIndexOf(","));
        }
        tfSupervisors.setText(emailsSup.toString());
        try {
            for (String l : item.getLinks()) {
                URL url = new URL(l.toString());
                getSessionBean1().getLinks().add(url);
            }
        } catch (MalformedURLException e) {
            Logger.getLogger(PgNewItem.class.getName()).log(Level.SEVERE, null, e);
        }
        if (getSessionBean1().getCurrentFileRepository() == null) {
            getSessionBean1().setCurrentFileRepository(item.getFileRepository());
        }
    }

    public void setToUserFilter(String value) {
        int index = value.lastIndexOf(',');
        if (index >= 0) {
            getSessionBean1().getToUsersFilteredOptions().filter(getSessionBean1().getCurrentUser().getBuddies(), value.substring(index + 1));
        } else {
            getSessionBean1().getToUsersFilteredOptions().filter(getSessionBean1().getCurrentUser().getBuddies(), value);
        }
        toUserFilter = value;
    }

    public String getToUserFilter() {
        return toUserFilter;
    }

    public void setSupervisorFilter(String value) {
        int index = value.lastIndexOf(',');
        if (index >= 0) {
            getSessionBean1().getSupervisorsFilteredOptions().filter(getSessionBean1().getCurrentUser().getSupervisors(), value.substring(index + 1).trim());
        } else {
            getSessionBean1().getSupervisorsFilteredOptions().filter(getSessionBean1().getCurrentUser().getSupervisors(), value.trim());
        }
        supervisorFilter = value;
    }

    public String getSupervisorFilter() {
        return supervisorFilter;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        if (link == null) {
            this.link = null;
            return;
        }
        if (link.equals("")) {
            this.link = "";
            return;
        }
        try {
            if (link.indexOf("://") == -1) {
                this.link = "http://" + link;
            } else {
                this.link = link;
            }
            getSessionBean1().getLinks().add(new URL(this.link));
            this.link = "";
        } catch (MalformedURLException ex) {
            Logger.getLogger(PgNewItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getSelectedLink() {
        return selectedLink;
    }

    public void setSelectedLink(String selectedLink) {
        if (selectedLink == null) {
            this.selectedLink = null;
            return;
        }
        if (selectedLink.equals("")) {
            this.selectedLink = "";
            return;
        }
        try {
            if (selectedLink.indexOf("://") == -1) {
                this.selectedLink = "http://" + selectedLink;
            } else {
                this.selectedLink = selectedLink;
            }
            getSessionBean1().getLinks().remove(new URL(this.selectedLink));
        } catch (MalformedURLException ex) {
            Logger.getLogger(PgNewItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Option[] tagsFilter(String word) {
        String suffix = word;
        String prefix = "";
        int lastComma = word.lastIndexOf(',');
        if (lastComma >= 0) {
            suffix = word.substring(word.lastIndexOf(',') + 1);
            prefix = word.substring(0, word.lastIndexOf(','));
        }
        List<Option> filteredValues = new ArrayList<Option>();
        if (lastComma >= 0) {
            suffix = suffix.trim();
            for (String t : getApplicationBean1().getTags()) {
                if (suffix.length() == 0 || t.indexOf(suffix) >= 0) {
                    filteredValues.add(new Option(prefix + ", " + t, t));
                }
            }
        } else {
            for (String t : getApplicationBean1().getTags()) {
                if (t.indexOf(suffix) >= 0) {
                    filteredValues.add(new Option(t, t));
                }
            }
        }
        return filteredValues.toArray(new Option[1]);
    }

    public Option[] buddiesEmailFilter(String word) {
        String suffix = word;
        String prefix = "";
        int lastComma = word.lastIndexOf(',');
        if (lastComma >= 0) {
            suffix = word.substring(word.lastIndexOf(',') + 1);
            prefix = word.substring(0, word.lastIndexOf(','));
        }
        List<Option> filteredValues = new ArrayList<Option>();
        if (lastComma >= 0) {
            suffix = suffix.toLowerCase().trim();
            for (String email : getSessionBean1().getBuddiesEmail()) {
                if (suffix.length() == 0 || email.toLowerCase().indexOf(suffix) >= 0) {
                    filteredValues.add(new Option(prefix + ", " + email, email));
                }
            }
        } else {
            for (String email : getSessionBean1().getBuddiesEmail()) {
                if (email.toLowerCase().indexOf(suffix) >= 0) {
                    filteredValues.add(new Option(email, email));
                }
            }
        }
        return filteredValues.toArray(new Option[1]);
    }

    public void rbNegotiatedDeadlineAM_processValueChange(ValueChangeEvent event) {
    }

    public void cbReference_processValueChange(ValueChangeEvent event) {
    }
}
