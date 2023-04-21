package org.contineo.web.document;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contineo.core.CryptBean;
import org.contineo.core.document.Document;
import org.contineo.core.document.DownloadTicket;
import org.contineo.core.document.History;
import org.contineo.core.document.dao.DocumentDAO;
import org.contineo.core.document.dao.DownloadTicketDAO;
import org.contineo.core.document.dao.HistoryDAO;
import org.contineo.core.i18n.DateBean;
import org.contineo.core.security.ExtMenu;
import org.contineo.core.security.Menu;
import org.contineo.core.security.dao.MenuDAO;
import org.contineo.util.Context;
import org.contineo.web.SessionManagement;
import org.contineo.web.StyleBean;
import org.contineo.web.i18n.Messages;
import org.contineo.web.navigation.MenuBarBean;
import org.contineo.web.navigation.MenuItem;
import org.contineo.web.navigation.PageContentBean;
import org.contineo.web.upload.InputFileBean;
import org.contineo.web.util.Constants;
import com.icesoft.faces.context.effects.JavascriptContext;

/**
 * The <code>DocumentRecord</code> class contains the base information for an
 * entry in a data table. This class is meant to represent a model and should
 * only contain base document data <p/>
 * 
 * @author Marco Meschieri
 * @version $Id: DocumentRecord.java,v 1.17 2006/08/29 16:33:46 marco Exp $
 * @since 3.0
 */
public class DocumentRecord extends MenuBarBean {

    protected static Log log = LogFactory.getLog(DocumentRecord.class);

    public static final String SPACER_IMAGE = "spacer.gif";

    protected String indentStyleClass = "";

    protected String rowStyleClass = "";

    protected String expandImage = SPACER_IMAGE;

    protected String contractImage = SPACER_IMAGE;

    protected String expandContractImage = SPACER_IMAGE;

    protected ArrayList parentDocumentsList;

    protected boolean expanded;

    protected boolean selected = false;

    protected ExtMenu menu;

    protected Document document;

    protected ArrayList<DocumentRecord> childRecords = new ArrayList<DocumentRecord>();

    protected MenuItem contextMenu;

    private String documentPath;

    /**
	 * <p>
	 * Creates a new <code>DocumentRecord</code>. This constructor should be
	 * used when creating DocumentRecord which will contain children
	 * </p>
	 * 
	 * @param expanded true, indicates that the specified node will be
	 *        expanded by default; otherwise, false.
	 */
    public DocumentRecord(ExtMenu menu, String indentStyleClass, String rowStyleClass, String expandImage, String contractImage, ArrayList parentDocumentsList, boolean expanded) {
        this.menu = menu;
        DocumentDAO docDao = (DocumentDAO) Context.getInstance().getBean(DocumentDAO.class);
        this.document = docDao.findByMenuId(menu.getMenuId());
        this.indentStyleClass = indentStyleClass;
        this.rowStyleClass = rowStyleClass;
        this.expandImage = expandImage;
        this.contractImage = contractImage;
        this.parentDocumentsList = parentDocumentsList;
        this.parentDocumentsList.add(this);
        this.expanded = expanded;
        if (this.expanded) {
            expandContractImage = contractImage;
            expandNodeAction();
        } else {
            expandContractImage = expandImage;
        }
    }

    /**
	 * <p>
	 * Creates a new <code>DocumentRecord</code>. This constructor should be
	 * used when creating a DocumentRecord which will be a child of some other
	 * DocumentRecord.
	 * </p>
	 * <p/>
	 * <p>
	 * The created DocumentRecord has no image states defined.
	 * </p>
	 * 
	 * @param document
	 * @param indentStyleClass
	 * @param rowStyleClass
	 */
    public DocumentRecord(ExtMenu menu, String indentStyleClass, String rowStyleClass) {
        this.menu = menu;
        DocumentDAO docDao = (DocumentDAO) Context.getInstance().getBean(DocumentDAO.class);
        this.document = docDao.findByMenuId(menu.getMenuId());
        this.indentStyleClass = indentStyleClass;
        this.rowStyleClass = rowStyleClass;
    }

    public DocumentRecord() {
        super();
    }

    public ExtMenu getMenu() {
        return menu;
    }

    public Document getDocument() {
        return document;
    }

    public boolean isDocumentFound() {
        return document != null;
    }

    /**
	 * Gets the description of the record
	 * 
	 * @return description of the record
	 */
    public String getDescription() {
        return menu.getMenuText();
    }

    public String getDisplayDescription() {
        return StringUtils.abbreviate(menu.getMenuText(), 68);
    }

    public ArrayList<DocumentRecord> getChildRecords() {
        return childRecords;
    }

    /**
	 * Utility method to add all child nodes to the parent dataTable list.
	 */
    private void expandNodeAction() {
        if ((childRecords != null) && (childRecords.size() > 0)) {
            int index = parentDocumentsList.indexOf(this);
            parentDocumentsList.addAll(index + 1, childRecords);
        }
    }

    /**
	 * Utility method to remove all child nodes from the parent dataTable list.
	 */
    private void contractNodeAction() {
        if ((childRecords != null) && (childRecords.size() > 0)) {
            parentDocumentsList.removeAll(childRecords);
        }
    }

    /**
	 * Adds a child sales record to this sales group.
	 * 
	 * @param documentRecord child document record to add to this record.
	 */
    public void addChildRecord(DocumentRecord documentRecord) {
        if ((this.childRecords != null) && (documentRecord != null)) {
            this.childRecords.add(documentRecord);
            if (expanded) {
                contractNodeAction();
                expandNodeAction();
            }
        }
    }

    /**
	 * Toggles the expanded state of this DocumentRecord.
	 */
    public void toggleSubGroupAction(ActionEvent event) {
        setExpanded(!expanded);
    }

    /**
	 * Gets the style class name used to define the first column of a document
	 * record row. This first column is where a expand/contract image is placed.
	 * 
	 * @return indent style class as defined in css file
	 */
    public String getIndentStyleClass() {
        return indentStyleClass;
    }

    /**
	 * Gets the style class name used to define all other columns in the
	 * document record row, except the first column.
	 * 
	 * @return style class as defined in css file
	 */
    public String getRowStyleClass() {
        return rowStyleClass;
    }

    /**
	 * Gets the image which will represent either the expanded or contracted
	 * state of the <code>DocumentRecord</code>.
	 * 
	 * @return name of image to draw
	 */
    public String getExpandContractImage() {
        return expandContractImage;
    }

    public int getId() {
        return document.getDocId();
    }

    public int getMenuId() {
        return menu.getMenuId();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getIcon() {
        return getMenu().getMenuIcon();
    }

    @Override
    public boolean equals(Object arg0) {
        if ((arg0 == null) || (document == null)) {
            return false;
        } else {
            return document.equals(((DocumentRecord) arg0).getDocument());
        }
    }

    public String getDocumentPath() {
        if (documentPath == null) {
            try {
                Menu curMenu = getMenu();
                Collection<Menu> parentColl = curMenu.getParents();
                ArrayList<Menu> parentList = new ArrayList<Menu>(parentColl);
                StringBuilder sb = new StringBuilder();
                boolean first = true;
                for (Menu menu : parentList) {
                    if (!first) sb.append(" / ");
                    if (menu.getMenuId() == Menu.MENUID_HOME) continue;
                    if (menu.getMenuId() == Menu.MENUID_DOCUMENTS) {
                        String menuText = Messages.getMessage(menu.getMenuText());
                        sb.append(menuText);
                    } else {
                        sb.append(menu.getMenuText());
                    }
                    first = false;
                }
                documentPath = sb.toString();
            } catch (Throwable th) {
                logger.warn("Exception getDocumentPath() " + th.getMessage(), th);
            }
        }
        return documentPath;
    }

    /**
	 * Creates the context menu associated with this record
	 * 
	 * @see org.contineo.web.navigation.MenuBarBean#createMenuItems()
	 */
    protected void createMenuItems() {
        model.clear();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map session = facesContext.getExternalContext().getSessionMap();
        String username = (String) session.get(Constants.AUTH_USERNAME);
        ExtMenu menu = getMenu();
        contextMenu = createMenuItem(" ", "context-" + menu.getMenuId(), null, "#{documentRecord.noaction}", null, StyleBean.getImagePath("options_small.png"), true, null, null);
        model.add(contextMenu);
        if (menu.getWritable()) {
            if ((menu.getDocStatus() == Document.DOC_CHECKED_OUT) && menu.getCheckoutUser().equals(username)) {
                contextMenu.getChildren().add(createMenuItem(Messages.getMessage("msg.jsp.checkin"), "checkin-" + menu.getMenuId(), null, "#{documentRecord.checkin}", null, StyleBean.getImagePath("checkin.png"), true, null, null));
            } else if (menu.getDocStatus() == Document.DOC_CHECKED_IN) {
                contextMenu.getChildren().add(createMenuItem(Messages.getMessage("msg.jsp.checkout"), "checkout-" + menu.getMenuId(), null, "#{documentRecord.checkout}", null, StyleBean.getImagePath("checkout.png"), true, null, null));
            }
            contextMenu.getChildren().add(createMenuItem(Messages.getMessage("msg.jsp.foldercontent.edit"), "edit-" + menu.getMenuId(), null, "#{documentRecord.edit}", null, StyleBean.getImagePath("document_edit.png"), true, null, null));
            contextMenu.getChildren().add(createMenuItem(Messages.getMessage("msg.jsp.foldercontent.rights"), "rights-" + menu.getMenuId(), null, "#{documentRecord.rights}", null, StyleBean.getImagePath("document_lock.png"), true, null, null));
        }
        if (menu.getMenuType() == Menu.MENUTYPE_FILE) {
            contextMenu.getChildren().add(createMenuItem(Messages.getMessage("msg.jsp.versions"), "versions-" + menu.getMenuId(), null, "#{documentRecord.versions}", null, StyleBean.getImagePath("versions.png"), true, "_blank", null));
            contextMenu.getChildren().add(createMenuItem(Messages.getMessage("msg.jsp.similardocs"), "similar-" + menu.getMenuId(), null, "#{searchForm.searchSimilar}", null, StyleBean.getImagePath("similar.png"), true, "_blank", null));
            contextMenu.getChildren().add(createMenuItem(Messages.getMessage("msg.jsp.discuss"), "articles-" + menu.getMenuId(), null, "#{documentRecord.articles}", null, StyleBean.getImagePath("comments.png"), true, "_blank", null));
            contextMenu.getChildren().add(createMenuItem(Messages.getMessage("msg.jsp.sendasemail"), "sendasmail-" + menu.getMenuId(), null, "#{documentRecord.sendAsEmail}", null, StyleBean.getImagePath("editmail.png"), true, "_blank", null));
            contextMenu.getChildren().add(createMenuItem(Messages.getMessage("msg.jsp.sendticket"), "sendticket-" + menu.getMenuId(), null, "#{documentRecord.sendAsTicket}", null, StyleBean.getImagePath("ticket.png"), true, "_blank", null));
            contextMenu.getChildren().add(createMenuItem(Messages.getMessage("msg.jsp.foldercontent.info"), "info-" + menu.getMenuId(), null, "#{documentRecord.info}", null, StyleBean.getImagePath("info.png"), true, "_blank", null));
            contextMenu.getChildren().add(createMenuItem(Messages.getMessage("msg.jsp.history"), "history-" + menu.getMenuId(), null, "#{documentRecord.history}", null, StyleBean.getImagePath("history.png"), true, "_blank", null));
        }
    }

    public String noaction() {
        return null;
    }

    public String edit() {
        Application application = FacesContext.getCurrentInstance().getApplication();
        DocumentNavigation documentNavigation = ((DocumentNavigation) application.createValueBinding("#{documentNavigation}").getValue(FacesContext.getCurrentInstance()));
        documentNavigation.setSelectedPanel(new PageContentBean("updateDocument"));
        DocumentEditForm docForm = ((DocumentEditForm) application.createValueBinding("#{documentForm}").getValue(FacesContext.getCurrentInstance()));
        docForm.reset();
        docForm.init(this);
        docForm.setReadOnly(false);
        return null;
    }

    /**
	 * Executes the checkout and the related document's download
	 */
    public String checkout() {
        String username = SessionManagement.getUsername();
        MenuDAO mdao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);
        DocumentDAO ddao = (DocumentDAO) Context.getInstance().getBean(DocumentDAO.class);
        if (SessionManagement.isValid()) {
            try {
                if (mdao.isWriteEnable(document.getMenuId(), username)) {
                    if (document.getDocStatus() == Document.DOC_CHECKED_IN) {
                        Menu menu = mdao.findByPrimaryKey(document.getMenuId());
                        document.setCheckoutUser(username);
                        document.setDocStatus(Document.DOC_CHECKED_OUT);
                        document.setMenu(menu);
                        ddao.store(document);
                        History history = new History();
                        history.setDocId(document.getDocId());
                        history.setDate(DateBean.toCompactString());
                        history.setUsername(username);
                        history.setEvent(History.CHECKOUT);
                        HistoryDAO historyDAO = (HistoryDAO) Context.getInstance().getBean(HistoryDAO.class);
                        historyDAO.store(history);
                        JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "alert('" + Messages.getMessage("msg.checkout.alert") + "');");
                        try {
                            int elementIndex = 0;
                            for (int i = 0; i < contextMenu.getChildren().size(); i++) {
                                MenuItem mitem = (MenuItem) contextMenu.getChildren().get(i);
                                if (mitem.getId().indexOf("checkout") != -1) {
                                    elementIndex = i;
                                    break;
                                }
                            }
                            MenuItem checkinMenuItem = createMenuItem(Messages.getMessage("msg.jsp.checkin"), "checkin-" + menu.getMenuId(), null, "#{documentRecord.checkin}", null, StyleBean.getImagePath("checkin.png"), true, null, null);
                            contextMenu.getChildren().set(elementIndex, checkinMenuItem);
                        } catch (RuntimeException e) {
                        } catch (Exception ex) {
                        }
                    } else {
                        Messages.addLocalizedError("errors.noaccess");
                    }
                } else {
                    Messages.addLocalizedError("errors.noaccess");
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                Messages.addLocalizedError("errors.error");
            }
        } else {
            return "login";
        }
        return null;
    }

    /**
	 * Executes the checkin and the related document's download
	 */
    public String checkin() {
        if (SessionManagement.isValid()) {
            Application application = FacesContext.getCurrentInstance().getApplication();
            DocumentNavigation documentNavigation = ((DocumentNavigation) application.createValueBinding("#{documentNavigation}").getValue(FacesContext.getCurrentInstance()));
            documentNavigation.setSelectedPanel(new PageContentBean("checkin"));
            DocumentEditForm docForm = ((DocumentEditForm) application.createValueBinding("#{documentForm}").getValue(FacesContext.getCurrentInstance()));
            docForm.reset();
            docForm.init(this);
            InputFileBean fileForm = ((InputFileBean) application.createValueBinding("#{inputFile}").getValue(FacesContext.getCurrentInstance()));
            fileForm.reset();
        } else {
            return "login";
        }
        return null;
    }

    /**
	 * Shows all versions of this document
	 */
    public String versions() {
        String username = SessionManagement.getUsername();
        MenuDAO mdao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);
        if (SessionManagement.isValid()) {
            try {
                if (mdao.isWriteEnable(document.getMenuId(), username)) {
                    Application application = FacesContext.getCurrentInstance().getApplication();
                    VersionsRecordsManager versionsManager = ((VersionsRecordsManager) application.createValueBinding("#{versionsRecordsManager}").getValue(FacesContext.getCurrentInstance()));
                    versionsManager.selectDocument(document);
                    DocumentNavigation documentNavigation = ((DocumentNavigation) application.createValueBinding("#{documentNavigation}").getValue(FacesContext.getCurrentInstance()));
                    documentNavigation.setSelectedPanel(new PageContentBean("versions"));
                } else {
                    Messages.addError(Messages.getMessage("errors.noaccess"));
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                Messages.addError(Messages.getMessage("errors.error"));
            }
        } else {
            return "login";
        }
        return null;
    }

    /**
	 * Shows rights for this document
	 */
    public String rights() {
        String username = SessionManagement.getUsername();
        MenuDAO mdao = (MenuDAO) Context.getInstance().getBean(MenuDAO.class);
        if (SessionManagement.isValid()) {
            try {
                if (mdao.isWriteEnable(document.getMenuId(), username)) {
                    Application application = FacesContext.getCurrentInstance().getApplication();
                    RightsRecordsManager versionsManager = ((RightsRecordsManager) application.createValueBinding("#{rightsRecordsManager}").getValue(FacesContext.getCurrentInstance()));
                    versionsManager.selectDocument(document);
                    DocumentNavigation documentNavigation = ((DocumentNavigation) application.createValueBinding("#{documentNavigation}").getValue(FacesContext.getCurrentInstance()));
                    documentNavigation.setSelectedPanel(new PageContentBean("rights"));
                } else {
                    Messages.addError(Messages.getMessage("errors.noaccess"));
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                Messages.addError(Messages.getMessage("errors.error"));
            }
        } else {
            return "login";
        }
        return null;
    }

    /**
	 * Shows the articles for this document
	 */
    public String articles() {
        if (SessionManagement.isValid()) {
            try {
                Application application = FacesContext.getCurrentInstance().getApplication();
                ArticlesRecordsManager articlesManager = ((ArticlesRecordsManager) application.createValueBinding("#{articlesRecordsManager}").getValue(FacesContext.getCurrentInstance()));
                articlesManager.selectDocument(document);
                DocumentNavigation documentNavigation = ((DocumentNavigation) application.createValueBinding("#{documentNavigation}").getValue(FacesContext.getCurrentInstance()));
                documentNavigation.setSelectedPanel(new PageContentBean("articles"));
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                Messages.addError(Messages.getMessage("errors.error"));
            }
        } else {
            return "login";
        }
        return null;
    }

    /**
	 * Shows the history of this document
	 */
    public String history() {
        if (SessionManagement.isValid()) {
            Application application = FacesContext.getCurrentInstance().getApplication();
            DocumentNavigation documentNavigation = ((DocumentNavigation) application.createValueBinding("#{documentNavigation}").getValue(FacesContext.getCurrentInstance()));
            documentNavigation.setSelectedPanel(new PageContentBean("history"));
            HistoryRecordsManager manager = ((HistoryRecordsManager) application.createValueBinding("#{historyRecordsManager}").getValue(FacesContext.getCurrentInstance()));
            manager.selectDocument(this.getDocument());
        } else {
            return "login";
        }
        return null;
    }

    public String info() {
        edit();
        Application application = FacesContext.getCurrentInstance().getApplication();
        DocumentEditForm docForm = ((DocumentEditForm) application.createValueBinding("#{documentForm}").getValue(FacesContext.getCurrentInstance()));
        docForm.init(this);
        docForm.setReadOnly(true);
        return null;
    }

    public String sendAsEmail() {
        Application application = FacesContext.getCurrentInstance().getApplication();
        DocumentNavigation documentNavigation = ((DocumentNavigation) application.createValueBinding("#{documentNavigation}").getValue(FacesContext.getCurrentInstance()));
        documentNavigation.setSelectedPanel(new PageContentBean("email"));
        EMailForm emailForm = ((EMailForm) application.createValueBinding("#{emailForm}").getValue(FacesContext.getCurrentInstance()));
        emailForm.reset();
        emailForm.setSelectedDocument(getDocument());
        emailForm.getAttachments().add(getDocument().getMenu());
        emailForm.setAuthor(SessionManagement.getUser().getEmail());
        return null;
    }

    public String sendAsTicket() {
        Application application = FacesContext.getCurrentInstance().getApplication();
        DocumentNavigation documentNavigation = ((DocumentNavigation) application.createValueBinding("#{documentNavigation}").getValue(FacesContext.getCurrentInstance()));
        documentNavigation.setSelectedPanel(new PageContentBean("email"));
        EMailForm emailForm = ((EMailForm) application.createValueBinding("#{emailForm}").getValue(FacesContext.getCurrentInstance()));
        emailForm.reset();
        emailForm.setSelectedDocument(getDocument());
        emailForm.setAuthor(SessionManagement.getUser().getEmail());
        String username = SessionManagement.getUsername();
        Date date = new Date();
        String temp = DateFormat.getDateInstance().format(date) + username;
        String ticketid = CryptBean.cryptString(temp);
        DownloadTicket ticket = new DownloadTicket();
        ticket.setTicketId(ticketid);
        ticket.setMenuId(getMenuId());
        ticket.setUsername(username);
        DownloadTicketDAO ticketDao = (DownloadTicketDAO) Context.getInstance().getBean(DownloadTicketDAO.class);
        ticketDao.store(ticket);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getRequestURL();
        String address = "http://";
        address += (request.getServerName() + ":");
        address += request.getServerPort();
        address += request.getContextPath();
        address += ("/download-ticket?ticketId=" + ticketid);
        emailForm.setText("URL: " + address);
        return null;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        if (expanded) {
            expandContractImage = contractImage;
            expandNodeAction();
        } else {
            expandContractImage = expandImage;
            contractNodeAction();
        }
    }
}
