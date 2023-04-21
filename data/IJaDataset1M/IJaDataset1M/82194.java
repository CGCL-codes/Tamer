package org.fao.fenix.web.client.tablewizard;

import java.util.ArrayList;
import java.util.List;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.widget.Info;
import net.mygwt.ui.client.widget.MessageBox;
import net.mygwt.ui.client.widget.table.Table;
import net.mygwt.ui.client.widget.table.TableColumn;
import net.mygwt.ui.client.widget.table.TableColumnModel;
import net.mygwt.ui.client.widget.table.TableItem;
import org.fao.fenix.web.client.Fenix;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;

public class FenixTable {

    private int rows = 0;

    private ArrayList rowValues = new ArrayList();

    private ArrayList[] recordsList;

    /**
	 * Generate a view of a given dataset.
	 * 
	 * @param datasetID
	 *            Dataset ID.
	 * @param list
	 *            This list contains all columns headers.
	 */
    public void generateSimpleTable(final Long datasetID, final List list) {
        FenixTableWizard.panel.remove(1);
        AsyncCallback innerCallback = new AsyncCallback() {

            public void onSuccess(Object result) {
                TableColumn[] columns = new TableColumn[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    columns[i] = new TableColumn((String) list.get(i), .35f);
                    columns[i].minWidth = 100;
                }
                TableColumnModel cm = new TableColumnModel(columns);
                final Table tbl = new Table(Style.MULTI | Style.HORIZONTAL, cm);
                for (int i = 0; i < ((List) result).size(); i++) {
                    List tmp = (List) ((List) result).get(i);
                    Object[] values = new Object[list.size()];
                    for (int j = 0; j < tmp.size(); j++) {
                        values[j] = (String) tmp.get(j);
                    }
                    TableItem item = new TableItem(values);
                    tbl.add(item);
                }
                tbl.setSize("350px", "250px");
                FenixTableWizard.panel.add(tbl, DockPanel.EAST);
            }

            public void onFailure(Throwable caught) {
                Info.show("Service call failed!", "Service call to {0} failed", "getRecords(" + String.valueOf(datasetID) + ", " + String.valueOf(list.size()) + ")");
            }
        };
        Fenix.resourceExplorerService.getRecordsWithLabel(datasetID, list, innerCallback);
    }

    public void generateMathTable(final List idList, final String joinDimension, final String conditionDimension, String operation) {
        FenixTableWizard.panel.remove(1);
        ArrayList columnNames = new ArrayList();
        columnNames.add(joinDimension);
        columnNames.add(conditionDimension);
        columnNames.add("value");
        recordsList = new ArrayList[idList.size()];
        for (int i = 0; i < idList.size(); i++) {
            final int index = i;
            Fenix.resourceExplorerService.getRecords(Long.parseLong((String) idList.get(i)), columnNames, new AsyncCallback() {

                public void onSuccess(Object result) {
                    recordsList[index] = (ArrayList) result;
                    if (index == idList.size() - 1) {
                        TableColumn[] columns = new TableColumn[3];
                        columns[0] = new TableColumn(joinDimension, .35f);
                        columns[1] = new TableColumn(conditionDimension, .35f);
                        columns[2] = new TableColumn("value", .35f);
                        columns[0].minWidth = 100;
                        columns[1].minWidth = 100;
                        columns[2].minWidth = 100;
                        TableColumnModel cm = new TableColumnModel(columns);
                        final Table tbl = new Table(Style.MULTI | Style.HORIZONTAL, cm);
                        for (int z = 0; z < recordsList.length - 1; z++) {
                            List uno = (List) recordsList[z];
                            List unoTest = (List) recordsList[z + 1];
                            for (int q = 0; q < uno.size(); q++) {
                                List due = (List) uno.get(q);
                                boolean match = false;
                                MessageBox debug = new MessageBox(Style.ICON_INFO, Style.CLOSE | Style.RESIZE);
                                String message = "";
                                for (int m = 0; m < uno.size(); m++) {
                                    List dueTest = (List) uno.get(m);
                                    Object[] values = new Object[due.size()];
                                    message += "<BR>" + (String) due.get(0) + " VS " + (String) dueTest.get(0) + ", " + (String) due.get(1) + " VS " + (String) dueTest.get(1) + "</BR>";
                                    String testStringA = (String) due.get(0);
                                    String testStringB = (String) dueTest.get(0);
                                    String testStringC = (String) due.get(1);
                                    String testStringD = (String) dueTest.get(0);
                                    if (testStringA.equals(testStringB)) {
                                        for (int k = 0; k < due.size(); k++) {
                                            values[k] = (String) due.get(k);
                                        }
                                        TableItem item = new TableItem(values);
                                        tbl.add(item);
                                    }
                                }
                            }
                        }
                        tbl.setSize("350px", "250px");
                        FenixTableWizard.panel.add(tbl, DockPanel.EAST);
                    }
                }

                public void onFailure(Throwable caught) {
                    Info.show("Service call failed!", "Service call to {0} failed", "getRecords()");
                }
            });
        }
    }
}
