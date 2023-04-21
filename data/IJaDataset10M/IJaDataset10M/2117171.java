package Repository.RepositoryAuthors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import javax.mail.MessagingException;
import Logic.LogicChairMaintenance.AuthorFeedback.SendMail;
import Repository.Entities.Appeal;
import Repository.Entities.ContactPerson;
import Repository.Entities.IAppeal;
import Repository.Entities.IContactPerson;
import Repository.RepositoryChairMaintenance.Configuration;
import Repository.RepositoryChairMaintenance.Mysql;

/**
 * class responsible for all the operations with the appeals
 * @author G7
 *
 */
public class AppealRep implements IAppealRep {

    /**
	 * empty constructor
	 */
    public AppealRep() {
    }

    /**
	 * @param sessionId 
	 * @param appeal 
	 * @return result of submission of the appeal
	 * 
	 */
    public int create(String sessionId, IAppeal appeal) {
        try {
            ResultSet res = Connect.query("Select id_paper from abstracts where id_login = " + sessionId);
            res.next();
            String paperId = res.getString("id_paper");
            res = Connect.query("select decision from papers WHERE id_paper = " + paperId);
            if (!res.next()) return 3;
            if (!res.getString(1).equals("Rejected")) return 2;
            res = Connect.query("select id_paper FROM appeals WHERE id_paper = " + paperId);
            if (!res.next()) {
                Connect.update("INSERT into appeals(id_appeal,id_paper,justification) VALUES (NULL," + paperId + ", '" + appeal.getJustification() + "')");
                Mysql db = new Mysql();
                db.select("select id_login from pc_members where isPCC=1");
                LinkedList<Integer> list = new LinkedList<Integer>();
                while (!db.eof()) {
                    list.add(db.getInt("id_login"));
                    db.moveNext();
                }
                LinkedList<String> emails = new LinkedList<String>();
                db.select("select title from abstracts WHERE id_paper=" + paperId);
                String title = db.getString("title");
                for (int id : list) {
                    db.select("select email from logins WHERE id_login=" + id);
                    emails.add(db.getString("email"));
                    SendMail mail = new SendMail(emails, "New appeal requested", "New appeal was Requested for the paper: " + title);
                    try {
                        mail.send();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
                return 1;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
	 * get the appeals not evaluated yet 
	 * @return the iterator of the appeals
	 */
    public Iterator<Appeal> getAppeals() {
        ArrayList<Appeal> list = new ArrayList<Appeal>();
        Mysql db = new Mysql();
        try {
            db.select("select id_paper,justification from appeals WHERE decision= 'Undefined'");
            while (!db.eof()) {
                Appeal ap = new Appeal();
                Integer paperId = db.getInt("id_paper");
                ap.setPaperId(paperId);
                ap.setJustification(db.getString("justification"));
                list.add(ap);
                db.moveNext();
            }
            for (Appeal i : list) {
                db.select("select title from abstracts WHERE id_paper = " + i.getPaperId());
                i.setTitle(db.getString("title"));
            }
            return list.iterator();
        } catch (Exception ex) {
            return null;
        } finally {
            db.close();
        }
    }

    /**
	 * rejects the appeal chaging the database and sending the information by email
	 * @param paper_id 
	 * 
	 */
    public void rejectAppeal(int paper_id) {
        Mysql db = new Mysql();
        try {
            Connect.update("UPDATE appeals SET decision= 'Rejected' WHERE id_paper= " + paper_id);
            db.select("select id_login from abstracts where id_paper = " + paper_id);
            int id_login = db.getInt("id_login");
            db.select("select email from logins WHERE id_login= " + id_login);
            String email = db.getString("email");
            LinkedList<String> list = new LinkedList<String>();
            list.add(email);
            SendMail mail = new SendMail(list, "Appeal Rejected", "Your appeal was Rejected by the Commitee");
            mail.send();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param paper_id 
	 * @return the list of reviewers that didnt make any review of a certain paper
	 */
    public LinkedList<IContactPerson> getReviewers(int paper_id) {
        Mysql db = new Mysql();
        db.select("select id_login from pc_members where isPCC = 0");
        LinkedList<String> temp = new LinkedList<String>();
        while (!db.eof()) {
            temp.add(db.getString("id_login"));
            db.moveNext();
        }
        db.select("select id_login from paper_assignments WHERE id_paper = " + paper_id);
        LinkedList<String> temp1 = new LinkedList<String>();
        while (!db.eof()) {
            temp1.add(db.getString("id_login"));
            db.moveNext();
        }
        Iterator<String> it = temp.iterator();
        while (it.hasNext()) {
            for (String s2 : temp1) {
                if (it.next().equals(s2)) {
                    it.remove();
                    break;
                }
            }
        }
        LinkedList<IContactPerson> revNames = new LinkedList<IContactPerson>();
        for (String s : temp) {
            db.select("select id_login,firstName,lastName,email from logins WHERE id_login = " + s);
            IContactPerson contact = new ContactPerson();
            contact.setFirstName(db.getString("firstName"));
            contact.setLastName(db.getString("lastName"));
            contact.setEmail(db.getString("email"));
            revNames.add(contact);
        }
        return revNames;
    }

    /**
	 * It is triggered when the appeal is accepted for a certain paper.
	 * @param paper_id 
	 * 
	 */
    public void acceptAppeal(int paper_id) {
        try {
            Configuration config = new Configuration();
            Mysql db = new Mysql();
            db.select("select id_login from maintainers");
            String id = db.getString("id_login");
            Timestamp time = config.readAppealDeadline(id);
            time.setDate(time.getDate() + 3);
            config.updateReviewDeadline(id, time);
            Connect.update("UPDATE appeals SET decision= 'Accepted' WHERE id_paper= " + paper_id);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param contact
	 * @return returns the id of the reviwer
	 */
    public int getReviewId(IContactPerson contact) {
        Mysql db = new Mysql();
        db.select("select id_login from logins WHERE firstName = '" + contact.getFirstName() + "' AND lastName = '" + contact.getLastName() + "' AND email = '" + contact.getEmail() + "'");
        return db.getInt("id_login");
    }

    /**
	 * Removes the reviews of a certain paper
	 * @param paper_id
	 */
    public void removeReviews(Integer paper_id) {
        try {
            Connect.update("DELETE from reviews WHERE id_paper = " + paper_id);
            Connect.update("DELETE from paper_assignments WHERE id_paper = " + paper_id);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
