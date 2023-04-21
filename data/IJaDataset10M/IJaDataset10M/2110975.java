package edu.uw.tcss558.team1.server;

import com.aivik.wordspell.engine.BKSuggestion;
import com.aivik.wordspell.engine.KSpellCheckEngine;
import edu.uw.tcss558.team1.server.pojo.History;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author EwigKL
 */
@WebService(endpointInterface = "edu.uw.tcss558.team1.server.Service")
public class ServiceImpl implements Service {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(ServiceImpl.class);

    /**
     * Check if a certain word is misspelled.
     *
     * @param aWord The word to check if it is misspelled.
     * @return TRUE if it is misspelled, FALSE if it is not misspelled and NULL
     * if there a problem.
     */
    @Override
    public Boolean isWordMisspelled(String aWord) {
        return KSpellCheckEngine.getInstance().isWordMisspelled(aWord);
    }

    /**
     * Get a list of suggestions for a given word.
     *
     * @param aWord The word to get suggestions for.
     * @return a list of strings, each string is a suggestion for the word.
     */
    @Override
    public List<String> getSuggestions(String aWord) {
        List<String> listString = new ArrayList<String>();
        try {
            List<BKSuggestion> list = KSpellCheckEngine.getInstance().getSpellingSuggestions(aWord, Constants.MAX_NUM_OF_SUGGESTIONS);
            listString = new ArrayList<String>();
            for (BKSuggestion suggestion : list) {
                listString.add(suggestion.getWord());
            }
        } catch (Exception exception) {
            logger.error("Error", exception);
        }
        return listString;
    }

    @Override
    public List<String> getHistorys() {
        Session session = null;
        List<String> list = new ArrayList<String>();
        try {
            try {
                SessionFactory factory = HibernateUtil.getSessionFactory();
                session = factory.openSession();
                Query query = session.createQuery("from History order by hisDateTimeWordChecked desc");
                query.setMaxResults(20);
                List<History> listHistory = (List<History>) query.list();
                for (History history : listHistory) {
                    list.add(history.getHisWord());
                }
            } catch (Exception exception) {
                logger.error("Error", exception);
            } finally {
                session.close();
            }
        } catch (Exception exception) {
            logger.error("Error", exception);
        }
        return list;
    }

    @Override
    public Boolean isLoginOk(String aUsername, String aPassword) {
        if (aUsername != null && aPassword != null) {
            if (aUsername.equals("admin") && aPassword.equals("admin")) {
                return true;
            }
        }
        return false;
    }
}
