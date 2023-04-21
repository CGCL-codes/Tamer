package net.sf.mailand;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import net.sf.mailand.model.Folder;
import net.sf.mailand.model.Mail;
import net.sf.mailand.model.MailDomain;
import net.sf.mailand.model.StorageManager;
import net.sf.mailand.model.User;
import net.sf.mailand.pop3.server.MailAccess;
import net.sf.mailand.pop3.server.MailAccessFactory;
import net.sf.mailand.pop3.server.Pop3Server;
import net.sf.mailand.smtp.command.Domain;

/**
 * Management point of mailand.
 * 
 * database mailservers (smtp) pop3 imap web ui
 */
public final class Mailand implements Runnable {

    private static Set<Long> lockedMaildrops = Collections.synchronizedSet(new HashSet<Long>());

    private static class MailandMailAccess implements MailAccess {

        private EntityManager em;

        private User user;

        private long lockedFolderId;

        private Mail[] mails;

        public boolean authenticate(String name, String password) {
            em = StorageManager.instance().emf.createEntityManager();
            em.getTransaction().begin();
            user = StorageManager.instance().getDomain(em, Domain.create("test.de")).getUser(em, name);
            boolean result = false;
            if (user != null && user.isPasswordValid(password)) {
                Folder pop3Inbox = user.pop3Inbox(em);
                boolean isLocked = false;
                synchronized (lockedMaildrops) {
                    isLocked = lockedMaildrops.contains(pop3Inbox.getId());
                    if (!isLocked) {
                        lockedMaildrops.add(pop3Inbox.getId());
                    }
                }
                if (!isLocked) {
                    lockedFolderId = pop3Inbox.getId();
                    lockedMaildrops.add(pop3Inbox.getId());
                    mails = new Mail[pop3Inbox.mails().size()];
                    int index = 0;
                    for (Mail mail : pop3Inbox.mails()) {
                        mails[index] = mail;
                        index++;
                    }
                    result = true;
                } else {
                    result = false;
                }
            }
            return result;
        }

        public boolean delete(int index) {
            return true;
        }

        public String get(int index) {
            Mail message = mails[index - 1];
            return message.getText();
        }

        public int getNumberOfMessages() {
            return mails.length;
        }

        public boolean quit() {
            em.getTransaction().commit();
            lockedMaildrops.remove(lockedFolderId);
            return true;
        }

        public void reset() {
            em.getTransaction().rollback();
            em.getTransaction().begin();
        }

        public void terminate() {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            lockedMaildrops.remove(lockedFolderId);
        }
    }

    public void run() {
        EntityManager em = StorageManager.instance().createEntityManager();
        em.getTransaction().begin();
        MailDomain testDomain = StorageManager.instance().addDomain(em, Domain.create("test.de"));
        User user = testDomain.addUser(em, "test");
        user.setPassword("password");
        em.getTransaction().commit();
        MailServer mailServer = new MailServer(testDomain.domain());
        mailServer.start();
        MailAccessFactory mailAccessFactory = new MailAccessFactory() {

            public MailAccess createMailAccess() {
                return new MailandMailAccess();
            }
        };
        Pop3Server pop3Server = new Pop3Server(mailAccessFactory);
        try {
            SocketSessionServerFeeder socket = new SocketSessionServerFeeder(new ServerSocket(1100), pop3Server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Mailand().run();
    }
}
