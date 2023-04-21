package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>CompetenceHandler</code> manages the locally chached
 * {@link Competence} instances.
 * 
 * @author Michael
 */
public class CompetenceHandler implements Handler<Competence> {

    private List<Competence> competenceList = Collections.synchronizedList(new ArrayList<Competence>());

    private Logger log = LoggerFactory.getLogger(CompetenceHandler.class);

    @Override
    public void add(MessageIoSession session, Message<Competence> message) throws SQLException, ServiceException {
        synchronized (competenceList) {
            competenceList.addAll(message.getObjects());
        }
    }

    @Override
    public void execute(MessageIoSession session, Message<Competence> message) throws SQLException, ServiceException {
        log.debug(MessageType.EXEC + " called but currently not implemented");
    }

    @Override
    public void get(MessageIoSession session, Message<Competence> message) throws SQLException, ServiceException {
        synchronized (competenceList) {
            for (Competence competence : message.getObjects()) {
                int index = competenceList.indexOf(competence);
                if (index == -1) {
                    competenceList.add(competence);
                } else {
                    competenceList.set(index, competence);
                }
            }
        }
    }

    @Override
    public void remove(MessageIoSession session, Message<Competence> message) throws SQLException, ServiceException {
        synchronized (competenceList) {
            competenceList.removeAll(message.getObjects());
        }
    }

    @Override
    public void update(MessageIoSession session, Message<Competence> message) throws SQLException, ServiceException {
        synchronized (competenceList) {
            for (Competence updatedCompetence : message.getObjects()) {
                if (!competenceList.contains(updatedCompetence)) continue;
                int index = competenceList.indexOf(updatedCompetence);
                competenceList.set(index, updatedCompetence);
            }
        }
    }

    /**
	 * Returns the first instance that exactly matches the
	 * {@link Competence#getCompetenceName()}
	 * 
	 * @param competenceName
	 *            name the name of the competence to get
	 * @return the competence with the given name or null if nothing found
	 */
    public Competence getCompetenceByName(String competenceName) {
        synchronized (competenceList) {
            Iterator<Competence> iter = competenceList.iterator();
            while (iter.hasNext()) {
                Competence competence = iter.next();
                if (competence.getCompetenceName().equalsIgnoreCase(competenceName)) {
                    return competence;
                }
            }
        }
        return null;
    }

    /**
	 * Returns a new array containing the managed <code>Competence</code>
	 * instances.
	 * 
	 * @return an array containing the <code>Conpetence</code> instances.
	 */
    @Override
    public Competence[] toArray() {
        return competenceList.toArray(new Competence[competenceList.size()]);
    }
}
