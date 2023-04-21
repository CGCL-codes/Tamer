package gwttest.server;

import java.util.Date;
import java.util.List;
import gwttest.client.samplesurvey.model.Survey;
import gwttest.client.samplesurvey.service.PersistenceService;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PersistenceServiceImpl extends RemoteServiceServlet implements PersistenceService {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5609326171728515316L;

    /**
	 * Manages transactions with the datastore.
	 */
    private static final PersistenceManager pm = PMF.get().getPersistenceManager();

    @SuppressWarnings("unchecked")
    public Survey getSurvey(String name) {
        Query query = pm.newQuery("select from" + Survey.class.getName() + " where name =='" + name + "'");
        List<Survey> results = (List<Survey>) query.execute();
        return results.isEmpty() ? new Survey(name, null, new Date().toString(), new Date().toString()) : results.get(0);
    }

    public void saveSurvey(Survey survey) {
        Survey existingSurvey = this.getSurvey(survey.getName());
        if (existingSurvey == null) {
        }
        pm.makePersistent(survey);
        pm.close();
    }
}
