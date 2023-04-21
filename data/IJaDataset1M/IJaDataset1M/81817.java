package rm.gwt.appspace.client.store;

import rm.gwt.appspace.client.IsApplication;
import rm.gwt.appspace.client.util.SortEnum;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

public interface IsStore {

    public IsApplication getApplication();

    public StoreConstants getConstants();

    public void refresh();

    public boolean gotoFirstPage();

    public boolean gotoPreviousPage();

    public boolean gotoNextPage();

    public int getPageSize();

    public int getPageNumber();

    public SortEnum getSortEnum(int gCol);

    public int getGridRowCount();

    public int getGridColCount();

    public boolean isEditable(int gCol);

    public boolean isSortable(int gCol);

    public String getWidth(int gCol);

    public String getCellText(int gRow, int gCol);

    public HorizontalAlignmentConstant getAlignment(int gCol);

    public boolean doSort(int gCol);

    public Widget getEditorWidget(int gCol, int gRow);

    public Widget getContextWidget();

    public void setContextRecord(int gRow);
}
