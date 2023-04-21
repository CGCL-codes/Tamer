package br.edu.ufcg.ourgridportal.server.brokerportalcomponent;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.search.SentDateTerm;
import org.hsqldb.SessionManager;
import org.ourgrid.common.interfaces.to.GridProcessState;
import org.ourgrid.common.interfaces.to.JobEndedInterested;
import br.edu.ufcg.ourgridportal.server.mail.MyEmail;
import br.edu.ufcg.ourgridportal.server.mail.Sender;
import br.edu.ufcg.ourgridportal.server.persistencia.Persistence;
import br.edu.ufcg.ourgridportal.server.services.SessionsManager;
import br.edu.ufcg.ourgridportal.server.users.User;
import br.edu.ufcg.ourgridportal.server.util.JobsManager;

/**
 * This is the main core of the execution. It literally controls all the steps
 * that must be done. The Controller follows the singleton programming pattern -
 * there is no need of more than one instance of the Controller. It contains a
 * workers list and a requests list. It must wake up all the workers (threads)
 * the first time it is executed, for them to do their work. It also references
 * the path the unpacked files will be stored in disk.
 * 
 * @version 1.0 - July 28th 2007
 * @see org.ourgrid.portal.server.logic.ControllerIF
 * @see org.ourgrid.portal.server.logic.Request
 * @see org.ourgrid.portal.server.logic.Worker
 * @see org.ourgrid.portal.server.logic.script.ScriptExecutor
 * @see org.ourgrid.portal.server.logic.script.ScriptExecutorIF
 */
public class JobInterested implements JobEndedInterested {

    SessionsManager session = SessionsManager.getInstance();

    private JobsManager jobsManager = JobsManager.getInstance();

    public void jobEnded(int jobid, GridProcessState state) {
        System.out.println("O job com id " + jobid + " terminou com o estado" + state.name());
        MyEmail e = new MyEmail();
        User user = Persistence.getUser(jobsManager.getOwnerJob(jobid));
        System.out.println(user.getEmail());
        e.envia(user.getEmail(), "[OURGRID-PORTAL] Notificação de Termino de JOB", "Olá senhor" + "\n" + user.getName() + "\n" + "O jobid" + jobid + "enviado por voce terminou com Status " + state.toString());
    }

    public void schedulerHasBeenShutdown() {
        System.out.println("schedulerHasBeenShutdown()");
    }
}
