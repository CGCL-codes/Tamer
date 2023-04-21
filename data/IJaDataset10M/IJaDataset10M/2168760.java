package com.google.gwt.sample.expenses.client.place;

import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.gwt.sample.expenses.shared.EmployeeProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * A place in the app that shows a list of reports.
 */
public class ReportListPlace extends Place {

    /**
   * Tokenizer, which by all rights should have been code generated. Stay tuned.
   */
    @Prefix("l")
    public static class Tokenizer implements PlaceTokenizer<ReportListPlace> {

        static final String SEPARATOR = "!";

        private static final String NO_ID = "n";

        private final RequestFactory requests;

        public Tokenizer(RequestFactory requests) {
            this.requests = requests;
        }

        public ReportListPlace getPlace(String token) {
            String bits[] = token.split(SEPARATOR);
            if (bits.length != 2) {
                return null;
            }
            String department = URL.decodePathSegment(bits[0]);
            String reporterIdToken = bits[1];
            EntityProxyId<EmployeeProxy> proxyId = NO_ID.equals(reporterIdToken) ? null : requests.<EmployeeProxy>getProxyId(reporterIdToken);
            return new ReportListPlace(proxyId, department);
        }

        public String getToken(ReportListPlace place) {
            EntityProxyId<EmployeeProxy> id = place.getEmployeeId();
            String idToken = id == null ? NO_ID : requests.getHistoryToken(id);
            return URL.encodePathSegment(place.getDepartment()) + SEPARATOR + idToken;
        }
    }

    public static final ReportListPlace ALL = new ReportListPlace(null, "");

    private final EntityProxyId<EmployeeProxy> employeeId;

    private final String department;

    public ReportListPlace(EntityProxyId<EmployeeProxy> employeeId, String department) {
        this.employeeId = employeeId;
        this.department = department;
    }

    /**
   * @return the department searched for, or null for none
   */
    public String getDepartment() {
        return department;
    }

    /**
   * @return the employee to focus on, or null for none
   */
    public EntityProxyId<EmployeeProxy> getEmployeeId() {
        return employeeId;
    }
}
