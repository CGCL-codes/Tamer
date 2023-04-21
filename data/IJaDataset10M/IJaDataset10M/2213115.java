package educate.sis.admission;

import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpSession;
import lebah.db.Db;
import lebah.db.SQLRenderer;
import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ComposeOfferLetterModule extends lebah.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/sis/compose_letter.vm";
        String submit = getParam("command");
        String[] fieldList = new String[] { "{applicant_name}", "{address1}", "{address2}", "{address3}", "{poscode}", "{city}", "{state}", "{program_code}", "{program_name}" };
        context.put("fieldList", fieldList);
        context.put("fieldNameList", "{applicant_name}\n{address1}\n{address2}\n{address3}\n{poscode}\n{city}\n{state}\n{program_code}\n{program_name}");
        context.put("contentTemplate", getContentTemplate());
        if ("save_letter".equals(submit)) {
            saveLetter("offer_letter2");
        }
        context.put("contentText", getContentText("offer_letter2"));
        Template template = engine.getTemplate(template_name);
        return template;
    }

    String getContentText(String id) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            sql = "select content from letter_compose where letter_id = '" + id + "'";
            ResultSet rs = stmt.executeQuery(sql);
            String str = "";
            if (rs.next()) str = lebah.db.Db.getString(rs, "content");
            return str;
        } finally {
            if (db != null) db.close();
        }
    }

    void saveLetter(String id) throws Exception {
        Db db = null;
        String sql = "";
        String content = getParam("ta");
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            {
                r.add("letter_id");
                r.add("letter_id", id);
                sql = r.getSQLSelect("letter_compose");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true;
            }
            if (!found) {
                r.clear();
                r.add("letter_id", id);
                r.add("content", content);
                sql = r.getSQLInsert("letter_compose");
                stmt.executeUpdate(sql);
            } else {
                r.clear();
                r.update("letter_id", id);
                r.add("content", content);
                sql = r.getSQLUpdate("letter_compose");
                stmt.executeUpdate(sql);
            }
        } finally {
            if (db != null) db.close();
        }
    }

    String getContentTemplate() {
        return "{applicant_name}\n" + "{address1}\n" + "{address2}\n" + "{address3}\n" + "{city}\n" + "{poscode}\n" + "{state}\n\n\n" + "Dear {applicant_name}\n\n" + "I am very please to inform you of you acceptance to USA University as a freshman for Fall " + "2004. You have been admitted to the Academic Center for Entering Students (ACES) at the " + "USA University Main Campus.\n\n" + "The ACES program is design for students who are not yet established in the academic major " + "(see enclosed description). As an ACES student, you will be encouraged to explore a wide " + "range of academic majors available at the University. Through individual advising and " + "collaboration with all of the USA University Schools, ACES advisors will work to connect you " + "with majors that fit well with your academic strengths and future goals.\n\n" + "We are confident that you will continue your high level of academic performance throughout " + "the remainder of this year. This offer of admission is contingent upon your continued success. " + "The Undergraduate Admissions Office requires that an official copy of your final transcript be " + "forwarded upon completion of your high school course work. Please review carefully the " + "enclosed materials that pertain to required deposits, deadlines, and other critical information. " + "Best wishes and congratulations!\n\n" + "Sincerely;\n" + "Randy Stahlberg\n" + "Director of Admissions\n";
    }
}
