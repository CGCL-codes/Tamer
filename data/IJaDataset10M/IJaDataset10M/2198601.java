package org.fao.fenix.web.client;

import java.util.Iterator;
import java.util.List;
import org.fao.fenix.domain4.client.perspective.Project;
import org.fao.fenix.web.client.services.ResourceExplorerService;
import org.fao.fenix.web.client.services.ResourceExplorerServiceAsync;
import org.fao.fenix.web.client.util.FenixServiceEntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;

/**
 * A Composite widget that abstracts a DynaTableWidget and a data provider tied to the <@link SchoolCalendarService> RPC
 * endpoint.
 */
public class SchoolCalendarWidget extends Composite {

    /**
     * A data provider that bridges the provides row level updates from the data available through a <@link
     * SchoolCalendarService>.
     */
    public class CalendarProvider implements DynaTableDataProvider {

        private final SchoolCalendarServiceAsync calService;

        private final ResourceExplorerServiceAsync resourceExplorerService;

        private int lastMaxRows = -1;

        private Person[] lastPeople;

        private int lastStartRow = -1;

        public CalendarProvider() {
            calService = (SchoolCalendarServiceAsync) GWT.create(SchoolCalendarService.class);
            resourceExplorerService = (ResourceExplorerServiceAsync) GWT.create(ResourceExplorerService.class);
            ServiceDefTarget target = (ServiceDefTarget) calService;
            ServiceDefTarget targetResourceExplorerService = (ServiceDefTarget) resourceExplorerService;
            FenixServiceEntryPoint ep = new FenixServiceEntryPoint();
            String urlPojo = ep.getServiceEntryPointPojo(GWT.getModuleBaseURL(), GWT.getModuleName());
            String urlResourceExplorer = ep.getServiceEntryPointResourceExplorer(GWT.getModuleBaseURL(), GWT.getModuleName());
            System.out.println("GWT.getModuleBaseURL()" + GWT.getModuleBaseURL());
            System.out.println("url" + urlPojo);
            target.setServiceEntryPoint(urlPojo);
            targetResourceExplorerService.setServiceEntryPoint(urlResourceExplorer);
        }

        public void updateRowData(final int startRow, final int maxRows, final RowDataAcceptor acceptor) {
            if (startRow == lastStartRow) {
                if (maxRows == lastMaxRows) {
                    pushResults(acceptor, startRow, lastPeople);
                    return;
                }
            }
            calService.getPeople(startRow, maxRows, new AsyncCallback() {

                public void onFailure(Throwable caught) {
                    acceptor.failed(caught);
                }

                public void onSuccess(Object result) {
                    Person[] people = (Person[]) result;
                    lastStartRow = startRow;
                    lastMaxRows = maxRows;
                    lastPeople = people;
                    pushResults(acceptor, startRow, people);
                }
            });
            resourceExplorerService.getProjects(new AsyncCallback() {

                public void onFailure(Throwable caught) {
                    acceptor.failed(caught);
                }

                public void onSuccess(Object result) {
                    List projectList = (List) result;
                    for (Iterator iterator = projectList.iterator(); iterator.hasNext(); ) {
                        Project project = (Project) iterator.next();
                        System.out.println("project.getResourceId() = " + project.getResourceId());
                        System.out.println("project.getDescription() = " + project.getDescription());
                    }
                }
            });
        }

        private void pushResults(RowDataAcceptor acceptor, int startRow, Person[] people) {
            String[][] rows = new String[people.length][];
            for (int i = 0, n = rows.length; i < n; i++) {
                Person person = people[i];
                rows[i] = new String[3];
                rows[i][0] = person.getName();
                rows[i][1] = person.getDescription();
                rows[i][2] = person.getSchedule(daysFilter);
            }
            acceptor.accept(startRow, rows);
        }
    }

    private final CalendarProvider calProvider = new CalendarProvider();

    private final boolean[] daysFilter = new boolean[] { true, true, true, true, true, true, true };

    private final DynaTableWidget dynaTable;

    private Command pendingRefresh;

    public SchoolCalendarWidget(int visibleRows) {
        String[] columns = new String[] { "Name", "Description", "Schedule" };
        String[] styles = new String[] { "name", "desc", "sched" };
        dynaTable = new DynaTableWidget(calProvider, columns, styles, visibleRows);
        initWidget(dynaTable);
    }

    protected boolean getDayIncluded(int day) {
        return daysFilter[day];
    }

    protected void onLoad() {
        dynaTable.refresh();
    }

    protected void setDayIncluded(int day, boolean included) {
        if (daysFilter[day] == included) {
            return;
        }
        daysFilter[day] = included;
        if (pendingRefresh == null) {
            pendingRefresh = new Command() {

                public void execute() {
                    pendingRefresh = null;
                    dynaTable.refresh();
                }
            };
            DeferredCommand.addCommand(pendingRefresh);
        }
    }
}
