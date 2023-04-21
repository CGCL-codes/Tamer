package Logic.LogicChairMaintenance.Maintenance;

import java.sql.Timestamp;
import java.util.Iterator;
import Logic.LogicChairMaintenance.SecurityManager.*;
import Repository.Entities.ICameraReady;
import Repository.Entities.IPaper;
import Repository.Entities.Paper;
import Repository.RepositoryChairMaintenance.ConferenceSession;
import Repository.RepositoryChairMaintenance.Configuration;
import Repository.RepositoryChairMaintenance.IConferenceSession;
import Repository.RepositoryChairMaintenance.IConfiguration;

/**
 * @author G05
 * @version 0.5.1
 * @since 0.4
 */
public class Maintenance implements IMaintenance {

    IConfiguration repository = new Configuration();

    IConferenceSession confsession = new ConferenceSession();

    IAccount account = new Account();

    ISession session = new Session();

    IPaper pape = new Paper();

    /**
		 * 
		 */
    public void addConferenceSession(String sessionId, String title) {
        if (session.validSession(sessionId) == true) repository.createSession(sessionId, title);
    }

    /**
		 * 
		 */
    public void addPaperToConferenceSession(String sessionId, Integer paperId, String tittle) {
        if (session.validSession(sessionId) == true) repository.createPaperInSession(sessionId, paperId, tittle);
    }

    /**
		 * 
		 */
    public void addTopic(String sessionId, String topic) {
        if (session.validSession(sessionId) == true) repository.createTopic(sessionId, topic);
    }

    /**
		 * 
		 */
    public Timestamp getAbstractDeadline(String sessionId) {
        if (session.validSession(sessionId) == true) return repository.readAbstractDeadline(sessionId); else return null;
    }

    /**
		 * 
		 */
    public Timestamp getBiddingDeadline(String sessionId) {
        if (session.validSession(sessionId) == true) return repository.readBiddingDeadline(sessionId); else return null;
    }

    /**
		 * 
		 */
    public Timestamp getCameraReadyDeadline(String sessionId) {
        if (session.validSession(sessionId) == true) return repository.readCameraReadyDeadline(sessionId); else return null;
    }

    /**
		 * 
		 */
    public Iterator<IConferenceSession> getConferenceSessions(String sessionId) {
        if (session.validSession(sessionId) == true) return repository.readSessions(sessionId); else return null;
    }

    /**
		 * 
		 */
    public Timestamp getPaperDeadline(String sessionId) {
        if (session.validSession(sessionId) == true) return repository.readPaperDeadline(sessionId); else return null;
    }

    /**
		 * 
		 */
    public Iterator<IPaper> getPapersInConferenceSession(String sessionId, String tittle) {
        if (session.validSession(sessionId) == true) return repository.readPaperInConferenceSession(sessionId, tittle); else return null;
    }

    /**
		 * 
		 */
    public Timestamp getReviewDeadline(String sessionId) {
        if (session.validSession(sessionId) == true) return repository.readReviewDeadline(sessionId); else return null;
    }

    /**
		 * 
		 */
    public Iterator<String> getTopics(String sessionId) {
        if (session.validSession(sessionId) == true) return repository.readTopics(sessionId); else return null;
    }

    /**
		 * 
		 */
    public void removeConferenceSession(String sessionId, String title) {
        if (session.validSession(sessionId) == true) repository.deleteSession(sessionId, title);
    }

    /**
		 * 
		 */
    public void removePaperFromConferenceSession(String sessionId, Integer PaperId, String title) {
        if (session.validSession(sessionId) == true) repository.deletePaperFromSession(sessionId, PaperId, title);
    }

    /**
		 * 
		 */
    public void setAbstractDeadline(String sessionId, Timestamp date) {
        if (session.validSession(sessionId) == true) {
            if (repository.readAbstractDeadline(sessionId) == null) repository.createAbstractDeadLine(sessionId, date); else repository.updateAbstractDeadline(sessionId, date);
        }
    }

    /**
		 * 
		 */
    public void setBiddingDeadline(String sessionId, Timestamp date) {
        if (session.validSession(sessionId) == true) {
            if (repository.readBiddingDeadline(sessionId) == null) repository.createBiddingDeadline(sessionId, date); else repository.updateBiddingDeadline(sessionId, date);
        }
    }

    /**
		 * 
		 */
    public void setCameraReadyDeadline(String sessionId, Timestamp date) {
        if (session.validSession(sessionId) == true) {
            if (repository.readCameraReadyDeadline(sessionId) == null) repository.createCameraReadyDeadline(sessionId, date); else repository.updateCameraReadyDeadline(sessionId, date);
        }
    }

    /**
		 * 
		 */
    public void setPaperDeadline(String sessionId, Timestamp date) {
        if (session.validSession(sessionId) == true) {
            if (repository.readPaperDeadline(sessionId) == null) repository.createPaperDeadline(sessionId, date); else repository.updatePaperDeadline(sessionId, date);
        }
    }

    /**
		 * 
		 */
    public void setReviewDeadline(String sessionId, Timestamp date) {
        if (session.validSession(sessionId) == true) {
            if (repository.readReviewDeadline(sessionId) == null) repository.createReviewDeadline(sessionId, date); else repository.updateReviewDeadline(sessionId, date);
        }
    }

    /**
		 * 
		 */
    public String addPCChair(String sessionId, String firstName, String lastName, String email, String username, String password) {
        if (session.validSession(sessionId) == true) {
            Boolean creation = account.createPCChair(sessionId, firstName, lastName, email, username, password);
            if (creation == true) return "Criado"; else return "N�o Criado";
        } else return "N�o Criado";
    }

    /**
		 * 
		 */
    public String addPCMember(String sessionId, String firstName, String lastName, String email, String username, String password) {
        if (session.validSession(sessionId) == true) {
            Boolean creation = account.createPCMemberAccount(sessionId, firstName, lastName, email, username, password);
            if (creation == true) return "Criado"; else return "N�o Criado";
        } else return "N�o Criado";
    }

    /**
		 * 
		 */
    public Iterator<ICameraReady> getAllPapers(String sessionId) {
        if (session.validSession(sessionId) == true) {
            return repository.readAllCameraReadyPapers(sessionId);
        } else return null;
    }
}
