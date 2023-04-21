package org.yournamehere.client.reestimation;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.*;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.yournamehere.client.ScrumManagerPanel;
import org.yournamehere.client.release.ReleasesListPanel;
import org.yournamehere.client.sprint.SprintListPanel;
import org.yournamehere.client.task.TasksActions;

/**
 * Generate a pannel with the reestimaion list.
 * @author David
 */
public class ReestimationPanel extends ScrumManagerPanel {

    private Window winModalNewReestimation = new Window();

    private ListGrid reestimationGrid = new ListGrid();

    private Menu menu = new Menu();

    private final String idTaskSelected;

    private List<String> drawn = new ArrayList<String>();

    private final ReleasesListPanel releaseList;

    private final SprintListPanel sprintList;

    private ReestimationOperations ro = new ReestimationOperations();

    private TaskOperations tko = new TaskOperations();

    private final int widthReestimationWin = 460;

    private final int heightReestimationWin = 125;

    private static final int HEIGHT_EXTRA = 40;

    /**
     * Constructor of the class.
     */
    public ReestimationPanel(ReleasesListPanel releaseList, String idItemSelected, Canvas canvasPrincipal) {
        super();
        this.releaseList = releaseList;
        this.sprintList = null;
        this.idItemSelected = "";
        this.idTaskSelected = idItemSelected;
        this.canvasPrincipal = canvasPrincipal;
        setSize("55%", "55%");
        init();
    }

    /**
     * Constructor of the class.
     */
    public ReestimationPanel(SprintListPanel sprintList, String idItemSelected, Canvas canvasPrincipal) {
        super();
        this.sprintList = sprintList;
        this.releaseList = null;
        this.idItemSelected = "";
        this.idTaskSelected = idItemSelected;
        this.canvasPrincipal = canvasPrincipal;
        setSize("55%", "55%");
        init();
    }

    /**
     * Start the panel.
     */
    private void init() {
        createPrincipalWindow();
        ro.getListReestimation(idTaskSelected, LOADPRINCIPAL);
    }

    /**
     * create a grid with the principal structure.
     * @return
     */
    private Canvas createGridReestimations() {
        Canvas canvas = new Canvas();
        reestimationGrid.setWidth100();
        reestimationGrid.setHeight100();
        reestimationGrid.setEmptyCellValue("-");
        ListGridField nameField = new ListGridField("Date", constants.date());
        nameField.setWidth("50%");
        ListGridField initialEstimationField = new ListGridField("Estimation", constants.timeEstimated());
        initialEstimationField.setWidth("50%");
        reestimationGrid.setFields(nameField, initialEstimationField);
        reestimationGrid.setSortField(0);
        final int sizeData = 50;
        reestimationGrid.setDataPageSize(sizeData);
        reestimationGrid.setAutoFetchData(true);
        popupReestimation();
        reestimationGrid.setContextMenu(menu);
        reestimationGrid.addRecordClickHandler(new RecordClickHandler() {

            public void onRecordClick(RecordClickEvent event) {
                ListGridRecord record = (ListGridRecord) event.getRecord();
                idItemSelected = record.getAttribute("id");
                popupReestimation();
                reestimationGrid.setContextMenu(menu);
            }
        });
        reestimationGrid.addRowContextClickHandler(new RowContextClickHandler() {

            @Override
            public void onRowContextClick(RowContextClickEvent event) {
                ListGridRecord record = (ListGridRecord) event.getRecord();
                idItemSelected = record.getAttribute("id");
                popupReestimation();
                reestimationGrid.setContextMenu(menu);
            }
        });
        canvas.addChild(reestimationGrid);
        return canvas;
    }

    /**
     * Configure the principal window by adding the grid indicated.
     */
    private void createPrincipalWindow() {
        final int heightAddReesWin = 430;
        createWindowCenter(windowPrincipal, WIDTH_MODAL_WINDOW, heightAddReesWin, constants.reestimationMenu());
        tko.manageTask(idTaskSelected, LOADPRINCIPAL);
        windowPrincipal.addItem(createGridReestimations());
        windowPrincipal.setShowMinimizeButton(true);
        windowPrincipal.setRedrawOnResize(true);
        windowPrincipal.setCanDragResize(true);
        windowPrincipal.addCloseClickHandler(new CloseClickHandler() {

            public void onCloseClick(CloseClientEvent event) {
                windowPrincipal.destroy();
            }
        });
        canvasPrincipal.addChild(windowPrincipal);
        canvasPrincipal.show();
    }

    /**
     * Add the given information to the principal grid
     * 
     * @param dateEstimation
     * @param estimation
     * @param id
     */
    private void addInformationReestimation(Date dateEstimation, Float estimation, String id) {
        if (!exist(id)) {
            ListGridRecord rec = new ListGridRecord();
            String dateFormatted = formatDate(dateEstimation);
            rec.setAttribute("Date", dateFormatted);
            rec.setAttribute("Estimation", estimation);
            rec.setAttribute("id", id);
            drawn.add(id);
            reestimationGrid.addData(rec);
        }
    }

    /**
     * generate a date, according the format: day, number month year
     * @param date
     * @return
     */
    private String formatDate(Date date) {
        DateTimeFormat formatter = DateTimeFormat.getFormat("EEE, d MMM yyyy");
        return formatter.format(date);
    }

    /**
     * create a window with a empty form. Will add a new product.
     */
    private void createFormNewReestimation() {
        createWindowCenter(winModalNewReestimation, widthReestimationWin, heightReestimationWin, "New Reestimation");
        winModalNewReestimation.addItem(addFormCreateNewReestimation());
        configureFormWindow(winModalNewReestimation);
        winModalNewReestimation.addItem(errorMessage);
    }

    /**
     * creates a form with the information empty
     * @return
     */
    private DynamicForm addFormCreateNewReestimation() {
        return ro.formReestimation(idTaskSelected, new Date(), Float.valueOf(0), ADD);
    }

    /**
     * Configure the menu that will be the context menu.
     */
    private void popupReestimation() {
        menu.clear();
        menu = new Menu();
        MenuItem productItem = new MenuItem(constants.addReestimation(), "clock.jpg");
        com.smartgwt.client.widgets.menu.events.ClickHandler prodaux = new com.smartgwt.client.widgets.menu.events.ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                createFormNewReestimation();
            }
        };
        productItem.addClickHandler(prodaux);
        if (!idItemSelected.equals("")) {
            MenuItem deleteItem = new MenuItem(constants.delete(), "cancel.png");
            com.smartgwt.client.widgets.menu.events.ClickHandler auxdeletetheme = new com.smartgwt.client.widgets.menu.events.ClickHandler() {

                @Override
                public void onClick(MenuItemClickEvent event) {
                    if (com.google.gwt.user.client.Window.confirm(constants.sureDeleteReestimation())) {
                        ro.deleteReestimation(idItemSelected);
                        reestimationGrid.removeSelectedData();
                    }
                }
            };
            deleteItem.addClickHandler(auxdeletetheme);
            MenuItem modifyItem = new MenuItem(constants.modify(), "configure.png");
            com.smartgwt.client.widgets.menu.events.ClickHandler modifyhandler = new com.smartgwt.client.widgets.menu.events.ClickHandler() {

                @Override
                public void onClick(MenuItemClickEvent event) {
                    ro.manageReestimation(idTaskSelected, idItemSelected, MODIFY);
                }
            };
            modifyItem.addClickHandler(modifyhandler);
            menu.setVisible(false);
            menu.addItem(productItem);
            menu.addItem(deleteItem);
            menu.addItem(modifyItem);
        } else {
            menu.setVisible(false);
            menu.addItem(productItem);
        }
    }

    /**
     * create a window with a completed form with the information given. Will modify a reestimation
     * @param idTaskSelected
     * @param date
     * @param estimation
     */
    private void modifyReestimationForm(final String idTaskSelected, final Date date, final Float estimation) {
        createWindowCenter(winModalNewReestimation, widthReestimationWin, heightReestimationWin, constants.modifyReestimation());
        winModalNewReestimation.addItem(ro.formReestimation(idTaskSelected, date, estimation, MODIFY));
        configureFormWindow(winModalNewReestimation);
        winModalNewReestimation.addItem(errorMessage);
    }

    public void giveLastReestimation() {
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable arg0) {
                giveLastReestimation();
            }

            public void onSuccess(Object result) {
                Float value = (Float) result;
                if (releaseList != null) releaseList.updateEstimation(idTaskSelected, value); else sprintList.updateEstimation(idTaskSelected, value);
            }
        };
        getService().getLastEstimation(idTaskSelected, callback);
    }

    /**
     * Check if a id has been drawn in the grid.
     * @param id
     * @return
     */
    private boolean exist(String id) {
        return drawn.contains(id);
    }

    public final Window getWindow() {
        return windowPrincipal;
    }

    class ReestimationOperations extends ReestimationActions {

        @Override
        public void getListReestimationAction(final List<String> list, final String idTask, final int action) {
            Iterator iterReestimation = list.iterator();
            String idReestimation;
            while (iterReestimation.hasNext()) {
                idReestimation = (String) iterReestimation.next();
                ro.manageReestimation(idTask, idReestimation, action);
            }
        }

        @Override
        public void manageReestimationAction(String idTask, String idReestimation, Date date, Float estimation, int action) {
            if (action == LOADPRINCIPAL) {
                addInformationReestimation(date, estimation, idReestimation);
            } else if (action == MODIFY) modifyReestimationForm(idTaskSelected, date, estimation);
        }

        @Override
        public void formReestimationAction(String idTask, Date date, Float estimation, int action, DynamicForm form) {
            try {
                if (action == ADD) {
                    Criteria criteria = form.getValuesAsCriteria();
                    Date selectedDate = JSOHelper.getAttributeAsDate(criteria.getJsObj(), "fdate");
                    ro.addReestimation(idTask, selectedDate, Float.valueOf(form.getValueAsString("fName")));
                } else if (action == MODIFY) {
                    Criteria criteria = form.getValuesAsCriteria();
                    Date selectedDate = JSOHelper.getAttributeAsDate(criteria.getJsObj(), "fdate");
                    ro.modifyReestimation(idTask, selectedDate, Float.valueOf(form.getValueAsString("fName")), idItemSelected);
                }
                restartWinModal(winModalNewReestimation);
                errorMessage.setVisible(false);
            } catch (Exception e) {
                showError(widthReestimationWin, heightReestimationWin + HEIGHT_EXTRA, constants.errorData(), winModalNewReestimation, ERROR);
            }
        }

        @Override
        public void deleteReestimationAction(final String idReestimation) {
            drawn.remove(idReestimation);
            reestimationGrid.removeSelectedData();
            giveLastReestimation();
        }

        @Override
        public void addReestimationAction(String idReestimation, String idTask, Date date, Float estimation) {
            addInformationReestimation(date, estimation, idReestimation);
            giveLastReestimation();
        }

        @Override
        public void modifyReestimationAction(String idReestimation, String idTask, Date date, Float estimation) {
            reestimationGrid.removeSelectedData();
            drawn.remove(idReestimation);
            addInformationReestimation(date, estimation, idReestimation);
            giveLastReestimation();
        }
    }

    class TaskOperations extends TasksActions {

        @Override
        public void manageTaskAction(String id, String name, String description, Float inEstimation, Float currEstimation, boolean assigned, boolean planned, int action, Boolean done) {
            if (action == LOADPRINCIPAL) {
                getWindow().setTitle(constants.reestimationMenu() + ": " + name);
            }
        }
    }
}
