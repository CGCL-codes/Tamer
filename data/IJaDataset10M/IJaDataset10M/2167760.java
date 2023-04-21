package com.redhat.gs.mrlogistics.seam.admin;

import java.util.*;
import java.lang.*;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.annotations.datamodel.*;
import org.jboss.seam.log.Log;
import com.redhat.gs.mrlogistics.auth.*;
import com.redhat.gs.mrlogistics.data.*;
import java.sql.*;

@Stateful
@Name("import")
@Scope(ScopeType.CONVERSATION)
public class Import implements interfaceImport {

    @In
    private Session session;

    private UserRoleManager userRoleManager;

    private String host = "127.0.0.1";

    private String database = "dotpt";

    private String user = "root";

    private String password = "password";

    private String url = "";

    @Logger
    private Log log;

    @Begin(join = true)
    @Create
    public String begin() {
        userRoleManager = new UserRoleManager().setLog(log).setSession(session);
        return "import";
    }

    public String getdata() throws Exception {
        log.info("*******************************IMPORTING DOTPROJECT DATA!!!!!!!!");
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            System.out.println("MySQL Driver Found");
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found ... ");
            return "import";
        }
        Connection con;
        try {
            url = "jdbc:mysql://" + host + ":3306/" + database;
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established to " + url + "...");
        } catch (java.sql.SQLException e) {
            System.out.println("Connection couldn't be established to " + url);
            return "import";
        }
        HashMap map = new HashMap();
        Statement acs = con.createStatement();
        ResultSet asqls = acs.executeQuery("SELECT * FROM users,contacts WHERE users.user_contact = contacts.contact_id");
        while (asqls.next()) {
            String aid = (asqls.getObject("users.user_id").toString());
            String adata = (asqls.getObject("users.user_contact").toString());
            String adata2 = (asqls.getObject("users.user_username").toString());
            String adata3 = (asqls.getObject("users.user_password").toString());
            String adata4 = (asqls.getObject("users.user_parent").toString());
            String adata5 = (asqls.getObject("users.user_type").toString());
            String adata6 = (asqls.getObject("contacts.contact_first_name").toString());
            String adata7 = (asqls.getObject("contacts.contact_last_name").toString());
            String adata8 = (asqls.getObject("contacts.contact_email").toString());
            Person person = new Person();
            PersonSkills ps = new PersonSkills();
            ps.setPerson(person);
            person.setId(Integer.decode(aid).longValue());
            ContactCard cc = new ContactCard();
            person.setFirstName(adata6);
            person.setLastName(adata7);
            person.setEmailAddress(adata8);
            try {
                cc.setEmail(asqls.getObject("contacts.contact_email2").toString());
            } catch (Exception ex) {
            }
            try {
                cc.setAddress(asqls.getObject("contacts.contact_address1").toString());
            } catch (Exception ex) {
            }
            try {
                cc.setTown(asqls.getObject("contacts.contact_city").toString());
            } catch (Exception ex) {
            }
            try {
                cc.setState(asqls.getObject("contacts.contact_state").toString());
            } catch (Exception ex) {
            }
            try {
                cc.setZip(asqls.getObject("contacts.contact_zip").toString());
            } catch (Exception ex) {
            }
            try {
                cc.setPhone(asqls.getObject("contacts.contact_phone").toString());
            } catch (Exception ex) {
            }
            person.setContactInfo(cc);
            session.save(person);
            session.save(ps);
            session.save(cc);
            map.put(new Integer(aid), person);
        }
        asqls.close();
        try {
            Statement cs = con.createStatement();
            ResultSet sqls = cs.executeQuery("select * from companies;");
            while (sqls.next()) {
                String id = (sqls.getObject("company_id").toString());
                String data = (sqls.getObject("company_name").toString());
                Skill skill = new Skill();
                skill.setName(data);
                session.save(skill);
                System.out.println(id + " " + data);
                acs = con.createStatement();
                asqls = acs.executeQuery("SELECT * FROM projects WHERE project_company=" + id + ";");
                while (asqls.next()) {
                    String aid = (asqls.getObject("project_id").toString());
                    String adata = (asqls.getObject("project_name").toString());
                    System.out.println(" > " + aid + " " + adata);
                    Engagement engagement = new Engagement();
                    engagement.setName(adata);
                    try {
                        String description = (asqls.getObject("project_description").toString());
                        engagement.setDescription(description);
                    } catch (Exception ex) {
                    }
                    try {
                        java.util.Date start_date = asqls.getTimestamp("project_start_date");
                        engagement.setStartDate(start_date);
                    } catch (Exception ex) {
                    }
                    try {
                        java.util.Date end_date = asqls.getTimestamp("project_end_date");
                        engagement.setEndDate(end_date);
                    } catch (Exception ex) {
                    }
                    EngagementSkills es = new EngagementSkills();
                    es.setEngagement(engagement);
                    es.setSkill(skill);
                    ArrayList<EngagementSkills> engagementskills = new ArrayList<EngagementSkills>();
                    engagementskills.add(es);
                    skill.getEngagements().add(es);
                    engagement.setTotalHours(0);
                    session.save(engagement);
                    session.save(es);
                    Statement bcs = con.createStatement();
                    ResultSet bsqls = bcs.executeQuery("SELECT * FROM tasks,user_tasks WHERE tasks.task_id=user_tasks.task_id AND tasks.task_project=" + aid + ";");
                    while (bsqls.next()) {
                        String bid = (bsqls.getObject("tasks.task_id").toString());
                        String task_name = (bsqls.getObject("tasks.task_name").toString());
                        String uid = (bsqls.getObject("user_tasks.user_id").toString());
                        System.out.println("   >> " + bid + " " + task_name + " " + uid);
                        Assignment assignment = new Assignment();
                        assignment.setPerson((Person) map.get(Integer.decode(uid)));
                        assignment.setActivity(engagement);
                        assignment.setRole(task_name);
                        try {
                            List<DatePair> dates = new ArrayList<DatePair>();
                            java.util.Date start_date = bsqls.getTimestamp("tasks.task_start_date");
                            java.util.Date end_date = bsqls.getTimestamp("tasks.task_end_date");
                            for (java.util.Date d = start_date; d.before(end_date); d.setDate(d.getDate() + 1)) {
                                DatePair dp = new DatePair();
                                dp.setDate((java.util.Date) d.clone());
                                dp.setMinutes(8 * 60);
                                dp.setRealized(false);
                                dp.setAssignment(assignment);
                                dates.add(dp);
                                System.out.println(d);
                                session.save(dp);
                            }
                            DatePair dp = new DatePair();
                            dp.setDate(end_date);
                            dp.setAssignment(assignment);
                            dates.add(dp);
                            session.save(dp);
                            assignment.setDates(dates);
                        } catch (Exception ex) {
                        }
                        session.save(assignment);
                    }
                    bsqls.close();
                }
                asqls.close();
            }
            sqls.close();
        } catch (SQLException e) {
            System.out.println("Error executing sql statement");
            return "import";
        }
        return "login";
    }

    @End
    @Remove
    @Destroy
    public String destroy() {
        return "home";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String _host) {
        host = _host;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String _database) {
        database = _database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String _user) {
        user = _user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String _password) {
        password = _password;
    }
}
