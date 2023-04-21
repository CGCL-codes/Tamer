package org.verus.ngl.web.administration;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Window;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.verus.ngl.sl.bprocess.administration.PatronCategory;
import org.verus.ngl.sl.objectmodel.administration.PATRON_CATEGORY;
import org.verus.ngl.sl.utilities.MasterLog;
import org.verus.ngl.sl.utilities.NGLBeanFactory;
import org.verus.ngl.utilities.NGLUtility;
import org.verus.ngl.utilities.logging.NGLLogging;
import org.verus.ngl.web.beans.administration.HistoryLogComposer;
import org.verus.ngl.web.util.NGLUserSession;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;
import org.verus.ngl.utilities.logging.NGLLogging;

/**
 *
 * @author VSPL1234
 */
public class UserCategoryComposer extends Window implements Composer {

    private List enableUserDetails = new ArrayList();

    private List disableUserDetails = new ArrayList();

    private SimpleListModel gridModel = null;

    private RowRenderer rowRenderer = null;

    private SimpleListModel gridModelDisabledItems = null;

    private RowRenderer rowRendererDisabledItems = null;

    public UserCategoryComposer() {
        NGLLogging.getFineLogger().fine("=============in UserCategoryComposer class constructor=======================");
        try {
            NGLLogging.getFineLogger().fine("Object created");
            NGLLogging.getFineLogger().fine(getFellows().toString());
            PatronCategory patroncategory = (PatronCategory) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("patron_category");
            NGLUserSession nus = new NGLUserSession();
            setAttribute("libraryId", nus.getLibraryId());
            System.out.println("in UserCategoryComposer   @@@@@@@@@@@@@@                   " + nus.getLibraryId());
            enableUserDetails = patroncategory.bm_PatronCategoryName(nus.getDatabaseId(), Integer.parseInt(nus.getLibraryId()), org.verus.ngl.sl.objectmodel.status.PATRON_CATEGORY.STATUS_ENABLED);
            prepareGridData();
            rowRenderer = new RowRenderer() {

                @Override
                public void render(Row arg0, Object arg1) throws Exception {
                    PATRON_CATEGORY p_c = (PATRON_CATEGORY) arg1;
                    new Label(p_c.getPatronCategoryName()).setParent(arg0);
                    if (p_c.getUserType() == null) {
                        new Label(p_c.getUserType()).setParent(arg0);
                    } else {
                        if (p_c.getUserType().equals("A")) {
                            new Label("Staff of Library").setParent(arg0);
                        } else {
                            new Label("General User").setParent(arg0);
                        }
                    }
                    Hbox labelLast = new Hbox();
                    Button bnEdit = new Button("", "/images/16/wi0062-16.png");
                    bnEdit.setAttribute("userId", p_c.getPATRON_CATEGORYPK().getPatronCategoryId());
                    bnEdit.setAttribute("libraryId", p_c.getPATRON_CATEGORYPK().getLibraryId());
                    bnEdit.addForward("onClick", "", "onEditClick");
                    bnEdit.setOrient("vertical");
                    bnEdit.setParent(labelLast);
                    Button bnHistory = new Button("", "/images/16/wi0122-16.png");
                    bnHistory.setAttribute("userId", p_c.getPATRON_CATEGORYPK().getPatronCategoryId());
                    bnHistory.setAttribute("libraryId", p_c.getPATRON_CATEGORYPK().getLibraryId());
                    bnHistory.setAttribute("status", p_c.getStatus());
                    bnHistory.addForward("onClick", "", "onHistoryClick");
                    bnHistory.setOrient("vertical");
                    bnHistory.setParent(labelLast);
                    Button bnDelete = new Button("", "/images/16/edit-clear.png");
                    bnDelete.setAttribute("userId", p_c.getPATRON_CATEGORYPK().getPatronCategoryId());
                    bnDelete.setAttribute("libraryId", p_c.getPATRON_CATEGORYPK().getLibraryId());
                    bnDelete.addForward("onClick", "", "onDisableClick");
                    bnDelete.setOrient("vertical");
                    bnDelete.setParent(labelLast);
                    Button staff_privilages = new Button("", "");
                    staff_privilages.setLabel("Staff privileges");
                    staff_privilages.setParent(labelLast);
                    Button circulation_privilages = new Button("", "");
                    circulation_privilages.setLabel("Circulation privileges");
                    circulation_privilages.setParent(labelLast);
                    labelLast.setParent(arg0);
                }
            };
            rowRendererDisabledItems = new RowRenderer() {

                @Override
                public void render(Row arg0, Object arg1) throws Exception {
                    PATRON_CATEGORY p_c = (PATRON_CATEGORY) arg1;
                    new Label(p_c.getPatronCategoryName()).setParent(arg0);
                    if (p_c.getUserType() == null) {
                        new Label(p_c.getUserType()).setParent(arg0);
                    } else {
                        if (p_c.getUserType().equals("A")) {
                            new Label("Staff of Library").setParent(arg0);
                        } else {
                            new Label("General User").setParent(arg0);
                        }
                    }
                    Hbox labelLast = new Hbox();
                    Button bnHistory = new Button("", "/images/16/wi0122-16.png");
                    bnHistory.setAttribute("userId", p_c.getPATRON_CATEGORYPK().getPatronCategoryId());
                    bnHistory.setAttribute("libraryId", p_c.getPATRON_CATEGORYPK().getLibraryId());
                    bnHistory.setAttribute("status", p_c.getStatus());
                    bnHistory.addForward("onClick", "", "onHistoryClick");
                    bnHistory.setOrient("vertical");
                    bnHistory.setParent(labelLast);
                    Button bnDelete = new Button("", "/images/16/edit-delete.png");
                    bnDelete.setAttribute("userId", p_c.getPATRON_CATEGORYPK().getPatronCategoryId());
                    bnDelete.setAttribute("libraryId", p_c.getPATRON_CATEGORYPK().getLibraryId());
                    bnDelete.setAttribute("status", p_c.getStatus());
                    bnDelete.addForward("onClick", "", "onDeleteClick");
                    bnDelete.setOrient("vertical");
                    bnDelete.setParent(labelLast);
                    Button bnUndo = new Button("", "/images/16/edit-undo.png");
                    bnUndo.setAttribute("userId", p_c.getPATRON_CATEGORYPK().getPatronCategoryId());
                    bnUndo.setAttribute("libraryId", p_c.getPATRON_CATEGORYPK().getLibraryId());
                    bnUndo.setAttribute("status", p_c.getStatus());
                    bnUndo.addForward("onClick", "", "onEnableClick");
                    bnUndo.setOrient("vertical");
                    bnUndo.setParent(labelLast);
                    labelLast.setParent(arg0);
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareGridData() {
        NGLLogging.getFineLogger().fine("-------in prepareGridData-------------");
        System.out.println("before prepare grid data" + getUserDetails().size());
        Object[] obj = new Object[getUserDetails().size()];
        NGLLogging.getFineLogger().fine("-------after prepareGridData-------------");
        System.out.println("-------after prepareGridData-------------" + getUserDetails().size());
        for (int i = 0; i < getUserDetails().size(); i++) {
            PATRON_CATEGORY P_C = (PATRON_CATEGORY) getUserDetails().get(i);
            obj[i] = P_C;
        }
        Object[] temparr = new Object[getUserDetails().size()];
        ;
        for (int i = 0; i < getUserDetails().size(); i++) {
            for (int j = i + 1; j < getUserDetails().size(); j++) {
                String temp = ((PATRON_CATEGORY) obj[i]).getPatronCategoryName();
                String temp1 = ((PATRON_CATEGORY) obj[j]).getPatronCategoryName();
                int k = temp.compareToIgnoreCase(temp1);
                if (k > 0) {
                    temparr[i] = obj[i];
                    obj[i] = obj[j];
                    obj[j] = temparr[i];
                }
            }
        }
        setGridModel(new SimpleListModel(obj, true));
    }

    private void prepareGridDataDisabledUsers() {
        NGLLogging.getFineLogger().fine("in prepareGridDataDisabledItems");
        Object[] obj = new Object[getDisableUserDetails().size()];
        NGLLogging.getFineLogger().fine("-------after prepareGridDatadisable items-------------");
        System.out.println("-------after prepareGridDatadisableitems-------------" + getDisableUserDetails().size());
        for (int i = 0; i < getDisableUserDetails().size(); i++) {
            PATRON_CATEGORY P_C = (PATRON_CATEGORY) getDisableUserDetails().get(i);
            obj[i] = P_C;
        }
        Object[] temparr = new Object[getDisableUserDetails().size()];
        ;
        for (int i = 0; i < getDisableUserDetails().size(); i++) {
            for (int j = i + 1; j < getDisableUserDetails().size(); j++) {
                String temp = ((PATRON_CATEGORY) obj[i]).getPatronCategoryName();
                String temp1 = ((PATRON_CATEGORY) obj[j]).getPatronCategoryName();
                int k = temp.compareToIgnoreCase(temp1);
                if (k > 0) {
                    temparr[i] = obj[i];
                    obj[i] = obj[j];
                    obj[j] = temparr[i];
                }
            }
        }
        setGridModelDisabledItems(new SimpleListModel(obj, true));
    }

    public void onEnteringUserName(Event evt) {
        NGLLogging.getFineLogger().fine("in onEnteringuserName");
        String userName = ((Textbox) getFellow("userName")).getValue();
        NGLLogging.getFineLogger().fine("in onEnteringPatronName<==========>" + userName);
        String type = "";
        boolean tr = Boolean.parseBoolean(NGLUtility.getInstance().getTestedString(((Radio) getFellow("Generaluser")).isChecked()));
        if (tr) {
            type = org.verus.ngl.sl.objectmodel.status.PATRON_CATEGORY.GENERAL_USER;
        } else {
            type = org.verus.ngl.sl.objectmodel.status.PATRON_CATEGORY.STAFF_OF_THE_LIBRARY;
        }
        if (userName.trim().equals("")) {
            ((Label) getFellow("userNameMessage")).setValue(Labels.getLabel("message.ThisFieldCannotBeEmpty"));
            ((Label) getFellow("userNameMessage")).focus();
            ((Button) getFellow("userButton")).setDisabled(true);
        } else {
            PatronCategory patroncategory = (PatronCategory) NGLBeanFactory.getInstance().getBean("patron_category");
            NGLUserSession nus = new NGLUserSession();
            Object val = getAttribute("MODE");
            int count = 0;
            if (val != null && val.equals("EDIT")) {
                NGLLogging.getFineLogger().fine("Edit mode entered====" + val);
                Integer libraryId = new Integer(getAttribute("libraryId").toString());
                Integer userId = new Integer(getAttribute("userId").toString());
                NGLLogging.getFineLogger().fine("in onEnteringPatronName<==========>" + libraryId + "------" + userId + "-----" + userName);
                String databaseId = "" + nus.getDatabaseId();
                count = patroncategory.bm_checkPatronNameDuplicatesAtEdit(nus.getDatabaseId(), new Integer(nus.getLibraryId()), userId, userName, type);
                NGLLogging.getFineLogger().fine("Count is: " + count);
            } else {
                count = patroncategory.bm_checkPatronNameDuplicates(nus.getDatabaseId(), new Integer(nus.getLibraryId()), userName.toUpperCase());
                NGLLogging.getFineLogger().fine("after bm_checkPatronNameDuplicates() in PatronComposer class----" + count);
            }
            if (count > 0) {
                NGLLogging.getFineLogger().fine("Edit mode entered if(count>0) of if block");
                NGLLogging.getFineLogger().fine("in onEnteringPatronName<==========>" + count);
                ((Label) getFellow("patronNameMessage")).setValue(Labels.getLabel("message.CheckTheDataEnteredSystemDetectedDuplicateEntry"));
                ((Label) getFellow("patronNameMessage")).focus();
                ((Button) getFellow("patronNameMessage")).setDisabled(true);
            } else {
                NGLLogging.getFineLogger().fine("Edit mode entered if(count>0) of else block=====" + count);
                ((Label) getFellow("patronNameMessage")).setValue(Labels.getLabel("message.DataEnteredCanBeUsed"));
                NGLLogging.getFineLogger().fine("Edit mode entered if(count>0) of else block after getFellow()---" + ((Label) getFellow("patronNameMessage")));
                ((Button) getFellow("saveButton")).setDisabled(false);
            }
        }
    }

    public void onClickingSave(Event event) {
        NGLLogging.getFineLogger().entering("UserCategoryComposer", "onClickingSave()");
        PatronCategory patroncategory = (PatronCategory) NGLBeanFactory.getInstance().getBean("patron_category");
        NGLUserSession nus = new NGLUserSession();
        MasterLog masterLog = new MasterLog();
        String log = masterLog.bm_appendLog("", nus.getUserId(), Integer.parseInt(nus.getLibraryId()), new Date().getTime(), MasterLog.ACTIVITY_CREATE);
        String userName = NGLUtility.getInstance().getTestedString(((Textbox) getFellow("userName")).getValue());
        String type = "";
        boolean tr = Boolean.parseBoolean(NGLUtility.getInstance().getTestedString(((Radio) getFellow("Generaluser")).isChecked()));
        if (tr) {
            type = org.verus.ngl.sl.objectmodel.status.PATRON_CATEGORY.GENERAL_USER;
        } else {
            type = org.verus.ngl.sl.objectmodel.status.PATRON_CATEGORY.STAFF_OF_THE_LIBRARY;
        }
        NGLLogging.getFineLogger().fine("--------type in the save--------" + type);
        if (userName.trim().equals("")) {
            ((Label) getFellow("patronNameMessage")).setValue(Labels.getLabel("message.ThisFieldCannotBeEmpty"));
            ((Label) getFellow("patronNameMessage")).focus();
            ((Button) getFellow("saveButton")).setDisabled(true);
        } else {
            if (getAttribute("MODE").toString().equals("NEW")) {
                int count = patroncategory.bm_checkPatronNameDuplicates(nus.getDatabaseId(), new Integer(nus.getLibraryId()), userName.toUpperCase());
                if (count > 0) {
                    ((Label) getFellow("patronNameMessage")).setValue(Labels.getLabel("message.CheckTheDataEnteredSystemDetectedDuplicateEntry"));
                } else {
                    int returnValue = patroncategory.bm_savePatronName(nus.getDatabaseId(), Integer.parseInt(nus.getUserId()), new Integer(nus.getLibraryId()), userName, type, log, org.verus.ngl.sl.objectmodel.status.PATRON_CATEGORY.STATUS_ENABLED);
                    if (returnValue == 0) {
                        ((Label) getParent().getFellow("message")).setValue(Labels.getLabel("message.DataSaved"));
                        refreshTabledata(event);
                        detach();
                    } else {
                        ((Label) getFellow("message")).setValue(Labels.getLabel("message.UnexpectedErrorPleaseTryAgain"));
                        NGLLogging.getFineLogger().fine("Failure");
                    }
                }
            } else if (getAttribute("MODE").toString().equals("EDIT")) {
                String libraryId = getAttribute("libraryId").toString();
                String userId = getAttribute("userId").toString();
                String preLog = patroncategory.bm_getLogPatronName(nus.getDatabaseId(), Integer.parseInt(libraryId), Integer.parseInt(userId));
                String editLog = masterLog.bm_appendLog(preLog, nus.getUserId(), Integer.parseInt(libraryId), new Date().getTime(), MasterLog.ACTIVITY_MODIFY);
                int count = patroncategory.bm_checkPatronNameDuplicatesAtEdit(nus.getDatabaseId(), new Integer(libraryId), new Integer(userId), userName.toUpperCase(), type);
                NGLLogging.getFineLogger().fine("Count is: " + count);
                if (count > 0) {
                    ((Label) getFellow("patronNameMessage")).setValue(Labels.getLabel("message.CheckTheDataEnteredSystemDetectedDuplicateEntry"));
                } else {
                    int retVal = patroncategory.bm_editPatronName(nus.getDatabaseId(), Integer.parseInt(userId), new Integer(libraryId), userName, type, editLog, org.verus.ngl.sl.objectmodel.status.PATRON_CATEGORY.STATUS_ENABLED);
                    if (retVal == 0) {
                        ((Label) getParent().getFellow("message")).setValue(Labels.getLabel("message.DataUpdated"));
                        NGLLogging.getFineLogger().fine("after if(retval=0) in Edit Mode---" + ((Label) getParent().getFellow("message")));
                        refreshTabledata(event);
                        detach();
                    } else {
                        ((Label) getFellow("message")).setValue(Labels.getLabel("message.UnexpectedErrorPleaseTryAgain"));
                    }
                }
            }
        }
    }

    public void refreshTabledata(Event event) {
        NGLLogging.getFineLogger().fine("in refreshTabledata");
        PatronCategory patroncategory = (PatronCategory) NGLBeanFactory.getInstance().getBean("patron_category");
        NGLUserSession nus = new NGLUserSession();
        enableUserDetails = patroncategory.bm_PatronCategoryName(nus.getDatabaseId(), Integer.parseInt(nus.getLibraryId()), org.verus.ngl.sl.objectmodel.status.COURSE.STATUS_ENABLED);
        prepareGridData();
        if (event.getTarget().getId().equals("winx")) {
            Grid grid = (Grid) event.getTarget().getParent().getFellow("mainTable");
            grid.setModel(gridModel);
        } else {
            Grid grid = (Grid) getFellow("mainTable");
            grid.setModel(gridModel);
        }
    }

    public void refreshTabledataDisabledUsers(Event event) {
        NGLLogging.getFineLogger().fine("in refreshTabledataDisabledItems");
        PatronCategory patroncategory = (PatronCategory) NGLBeanFactory.getInstance().getBean("patron_category");
        NGLUserSession nus = new NGLUserSession();
        setDisableUserDetails(patroncategory.bm_PatronCategoryName(nus.getDatabaseId(), Integer.parseInt(nus.getLibraryId()), org.verus.ngl.sl.objectmodel.status.COURSE.STATUS_DISABLED));
        prepareGridDataDisabledUsers();
        Grid grid = (Grid) getFellow("disabledItemsTable");
        grid.setModel(gridModelDisabledItems);
    }

    public void onEditClick(Event evt) {
        NGLLogging.getFineLogger().fine("in onEditClick()");
        ForwardEvent fe = (ForwardEvent) evt;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component comp = me.getTarget();
        NGLLogging.getFineLogger().fine("component created" + comp);
        String libraryId = comp.getAttribute("libraryId").toString();
        NGLLogging.getFineLogger().fine("got libraryId" + libraryId);
        String userId = comp.getAttribute("userId").toString();
        NGLLogging.getFineLogger().fine("gotuserId" + userId);
        NGLLogging.getFineLogger().fine("Edit clicked: " + libraryId + "-----" + userId);
        Window win = (Window) Executions.createComponents("/jsp/administration/UserCategoryNew.zul", evt.getTarget(), null);
        win.setMaximizable(true);
        win.setClosable(true);
        win.setAttribute("MODE", "EDIT");
        win.setAttribute("userId", userId);
        win.setAttribute("libraryId", libraryId);
        PatronCategory patroncategory = (PatronCategory) NGLBeanFactory.getInstance().getBean("patron_category");
        NGLUserSession nus = new NGLUserSession();
        Vector binderDeatils = patroncategory.bm_getUsers(Integer.parseInt(nus.getLibraryId()), nus.getDatabaseId(), Integer.parseInt(userId), org.verus.ngl.sl.objectmodel.status.PATRON_CATEGORY.STATUS_ENABLED);
        Hashtable hashtable = (Hashtable) binderDeatils.get(0);
        ((Textbox) win.getFellow("userName")).setValue(hashtable.get("userName").toString());
        if ((hashtable.get("type").toString()).equals("A")) {
            ((Radio) win.getFellow("Staff")).setChecked(true);
        } else {
            ((Radio) win.getFellow("Generaluser")).setChecked(true);
        }
        win.setTitle(Labels.getLabel("label.EditUser"));
        try {
            win.doModal();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public void onOpeningDisabledUsers(Event event) {
        NGLLogging.getFineLogger().fine("in onOpeningDisabledItems()");
        ForwardEvent fe = (ForwardEvent) event;
        if (((Groupbox) getFellow("disabledItems")).isOpen()) {
            PatronCategory patroncategory = (PatronCategory) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("patron_category");
            NGLUserSession nus = new NGLUserSession();
            setDisableUserDetails(patroncategory.bm_PatronCategoryName(nus.getDatabaseId(), Integer.parseInt(nus.getLibraryId()), org.verus.ngl.sl.objectmodel.status.PATRON_CATEGORY.STATUS_DISABLED));
            prepareGridDataDisabledUsers();
            Grid grid = (Grid) getFellow("disabledItemsTable");
            grid.setModel(gridModelDisabledItems);
        }
    }

    public void onDisableClick(Event evt) {
        NGLLogging.getFineLogger().fine("in onDisableClick()");
        ForwardEvent fe = (ForwardEvent) evt;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component comp = me.getTarget();
        String libraryId = comp.getAttribute("libraryId").toString();
        String userId = comp.getAttribute("userId").toString();
        PatronCategory patroncategory = (PatronCategory) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("patron_category");
        NGLUserSession nus = new NGLUserSession();
        MasterLog masterLog = new MasterLog();
        String preLog = patroncategory.bm_getLogPatronName(nus.getDatabaseId(), Integer.parseInt(libraryId), Integer.parseInt(userId));
        String log = masterLog.bm_appendLog(preLog, nus.getUserId(), Integer.parseInt(nus.getLibraryId()), new Date().getTime(), masterLog.ACTIVITY_DISABLE);
        System.out.println("the log for enable click is================" + log);
        NGLLogging.getFineLogger().fine("in onDisableClick()**********>" + nus.getUserId());
        int retVal = patroncategory.bm_changeStatus(nus.getDatabaseId(), userId, new Integer(libraryId), org.verus.ngl.sl.objectmodel.status.COURSE.STATUS_DISABLED, log);
        if (retVal == 0) {
            System.out.println("chanded the status and got return value================" + retVal);
            ((Label) getFellow("message")).setValue(Labels.getLabel("message.UserDisabledToReEnableTheUserGoToDisabledItemsAndEnableTheUser"));
            refreshTabledata(evt);
            refreshTabledataDisabledUsers(evt);
        } else {
            ((Label) getFellow("message")).setValue(Labels.getLabel("message.UnexpectedErrorPleaseTryAgain"));
        }
    }

    public void onEnableClick(Event evt) {
        NGLLogging.getFineLogger().fine("in onEnableClick()");
        ForwardEvent fe = (ForwardEvent) evt;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component comp = me.getTarget();
        String libraryId = comp.getAttribute("libraryId").toString();
        String userId = comp.getAttribute("userId").toString();
        PatronCategory patroncategory = (PatronCategory) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("patron_category");
        NGLUserSession nus = new NGLUserSession();
        MasterLog masterLog = new MasterLog();
        String preLog = patroncategory.bm_getLogPatronName(nus.getDatabaseId(), Integer.parseInt(libraryId), Integer.parseInt(userId));
        String log = masterLog.bm_appendLog(preLog, nus.getUserId(), Integer.parseInt(nus.getLibraryId()), new Date().getTime(), MasterLog.ACTIVITY_ENABLE);
        System.out.println("the log for enable click is================" + log);
        int retVal = patroncategory.bm_changeStatus(nus.getDatabaseId(), userId, new Integer(libraryId), org.verus.ngl.sl.objectmodel.status.COURSE.STATUS_ENABLED, log);
        if (retVal == 0) {
            ((Label) getFellow("message")).setValue(Labels.getLabel("message.UserEnabled"));
            refreshTabledata(evt);
            refreshTabledataDisabledUsers(evt);
        } else {
            ((Label) getFellow("message")).setValue(Labels.getLabel("message.UnexpectedErrorPleaseTryAgain"));
        }
    }

    public void onHistoryClick(Event evt) {
        NGLLogging.getFineLogger().fine("onHistoryClick");
        ForwardEvent fe = (ForwardEvent) evt;
        MouseEvent me = (MouseEvent) fe.getOrigin();
        Component comp = me.getTarget();
        String libraryId = comp.getAttribute("libraryId").toString();
        String userId = comp.getAttribute("userId").toString();
        String status = comp.getAttribute("status").toString();
        NGLLogging.getFineLogger().fine("History clicked: " + libraryId + "-----" + userId + "----" + status);
        PatronCategory patroncategory = (PatronCategory) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("patron_category");
        NGLUserSession nus = new NGLUserSession();
        String log = patroncategory.bm_getLogPatronName(nus.getDatabaseId(), Integer.parseInt(libraryId), Integer.parseInt(userId));
        NGLLogging.getFineLogger().fine(log);
        HistoryLogComposer win = (HistoryLogComposer) Executions.createComponents("/jsp/administration/HistoryLog.zul", evt.getTarget(), null);
        win.setMaximizable(true);
        win.setSizable(true);
        win.setClosable(true);
        win.onloadPage(log);
        try {
            win.doModal();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public void onDeleteClick(Event evt) {
        NGLLogging.getFineLogger().fine("onDeleteClick");
        int val = Messagebox.CANCEL;
        try {
            val = Messagebox.show(Labels.getLabel("message.WantToDeleteRecord"), Labels.getLabel("label.Question"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
        } catch (Exception exp) {
        }
        if (val == Messagebox.OK) {
            ForwardEvent fe = (ForwardEvent) evt;
            MouseEvent me = (MouseEvent) fe.getOrigin();
            Component comp = me.getTarget();
            String libraryId = comp.getAttribute("libraryId").toString();
            String userId = comp.getAttribute("userId").toString();
            PatronCategory patroncategory = (PatronCategory) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("patron_category");
            NGLUserSession nus = new NGLUserSession();
            int retVal = patroncategory.bm_DeletePatronDetails(nus.getDatabaseId(), Integer.parseInt(libraryId), Integer.parseInt(userId));
            if (retVal == 0) {
                ((Label) getFellow("message")).setValue(Labels.getLabel("UserDeleted"));
                refreshTabledata(evt);
                refreshTabledataDisabledUsers(evt);
            } else {
                ((Label) getFellow("message")).setValue(Labels.getLabel("message.UnexpectedErrorPleaseTryAgain"));
            }
        }
    }

    public void onClickingCancel(Event event) {
        detach();
    }

    @Override
    public void doAfterCompose(Component arg0) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List getUserDetails() {
        return enableUserDetails;
    }

    public void setUserDetails(Vector userDetails) {
        this.enableUserDetails = userDetails;
    }

    public SimpleListModel getGridModel() {
        return gridModel;
    }

    public void setGridModel(SimpleListModel gridModel) {
        this.gridModel = gridModel;
    }

    public RowRenderer getRowRenderer() {
        return rowRenderer;
    }

    public void setRowRenderer(RowRenderer rowRenderer) {
        this.rowRenderer = rowRenderer;
    }

    public SimpleListModel getGridModelDisabledItems() {
        return gridModelDisabledItems;
    }

    public void setGridModelDisabledItems(SimpleListModel gridModelDisabledItems) {
        this.gridModelDisabledItems = gridModelDisabledItems;
    }

    public RowRenderer getRowRendererDisabledItems() {
        return rowRendererDisabledItems;
    }

    public void setRowRendererDisabledItems(RowRenderer rowRendererDisabledItems) {
        this.rowRendererDisabledItems = rowRendererDisabledItems;
    }

    public List getDisableUserDetails() {
        return disableUserDetails;
    }

    public void setDisableUserDetails(List disableUserDetails) {
        this.disableUserDetails = disableUserDetails;
    }
}
