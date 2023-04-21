package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import de.objectcode.time4u.server.web.gwt.utils.client.UtilsClientBundle;
import de.objectcode.time4u.server.web.gwt.utils.client.event.DataPageEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.event.DataPageHandler;
import de.objectcode.time4u.server.web.gwt.utils.client.event.HasDataPageHandlers;
import de.objectcode.time4u.server.web.gwt.utils.client.service.IDataPage;

public class DataPager extends Composite implements HasDataPageHandlers {

    private FlexTable controlTable = new FlexTable();

    private PushButton leftButton;

    private PushButton rightButton;

    private int numberOfPages = 1;

    private int currentPage = 0;

    public DataPager() {
        initWidget(controlTable);
        setStyleName("utils-dataPager");
        leftButton = new PushButton(new Image(UtilsClientBundle.INSTANCE.left()));
        leftButton.getUpDisabledFace().setImage(new Image(UtilsClientBundle.INSTANCE.leftDisabled()));
        leftButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (currentPage > 0) {
                    DataPageEvent.fire(DataPager.this, currentPage - 1);
                }
            }
        });
        rightButton = new PushButton(new Image(UtilsClientBundle.INSTANCE.right()));
        rightButton.getUpDisabledFace().setImage(new Image(UtilsClientBundle.INSTANCE.rightDisabled()));
        rightButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (currentPage < numberOfPages - 1) {
                    DataPageEvent.fire(DataPager.this, currentPage + 1);
                }
            }
        });
        controlTable.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Cell cell = controlTable.getCellForEvent(event);
                if (cell != null) {
                    int rowNum = cell.getRowIndex();
                    int colNum = cell.getCellIndex();
                    if (rowNum == 0 && colNum > 0 && colNum <= numberOfPages) {
                        DataPageEvent.fire(DataPager.this, colNum - 1);
                    }
                }
            }
        });
        updateControls(true);
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public HandlerRegistration addDataPageHandler(DataPageHandler handler) {
        return addHandler(handler, DataPageEvent.getType());
    }

    public void setDataPage(IDataPage<?> dataPage) {
        int newNumberOfPages;
        if (dataPage.getTotalNumber() <= 0) newNumberOfPages = 1; else newNumberOfPages = (dataPage.getTotalNumber() - 1) / dataPage.getPageSize() + 1;
        currentPage = dataPage.getPageNumber();
        if (newNumberOfPages != numberOfPages) {
            numberOfPages = newNumberOfPages;
            updateControls(true);
        } else {
            updateControls(false);
        }
    }

    protected void updateControls(boolean recreate) {
        if (recreate) {
            controlTable.removeAllRows();
            controlTable.setWidget(0, 0, leftButton);
            controlTable.getCellFormatter().setStyleName(0, 0, "utils-dataPager-left");
            for (int i = 0; i < numberOfPages; i++) {
                controlTable.setText(0, i + 1, String.valueOf(i + 1));
            }
            controlTable.setWidget(0, numberOfPages + 1, rightButton);
            controlTable.getCellFormatter().setStyleName(0, numberOfPages + 1, "utils-dataPager-right");
        }
        for (int i = 0; i < numberOfPages; i++) {
            controlTable.getCellFormatter().setStyleName(0, i + 1, i == currentPage ? "utils-dataPager-currentPage" : "utils-dataPager-page");
        }
        leftButton.setEnabled(currentPage > 0);
        rightButton.setEnabled(currentPage < numberOfPages - 1);
    }
}
