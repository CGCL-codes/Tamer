package de.uni_bremen.informatik.sopra.web_logic;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import de.uni_bremen.informatik.sopra.db.Administrator;
import de.uni_bremen.informatik.sopra.db.Dozent;
import de.uni_bremen.informatik.sopra.db.Mensch;
import de.uni_bremen.informatik.sopra.db.Student;
import de.uni_bremen.informatik.sopra.db.Tutor;
import de.uni_bremen.informatik.sopra.web_generation.Content;
import de.uni_bremen.informatik.sopra.web_generation.Footer;
import de.uni_bremen.informatik.sopra.web_generation.FormFieldBool;
import de.uni_bremen.informatik.sopra.web_generation.FormFieldPass;
import de.uni_bremen.informatik.sopra.web_generation.FormFieldText;
import de.uni_bremen.informatik.sopra.web_generation.FormSubmit;
import de.uni_bremen.informatik.sopra.web_generation.Header;
import de.uni_bremen.informatik.sopra.web_generation.Link;
import de.uni_bremen.informatik.sopra.web_generation.PrintableForm;
import de.uni_bremen.informatik.sopra.web_generation.PrintableList;
import de.uni_bremen.informatik.sopra.web_generation.ResourceBundleStore;
import de.uni_bremen.informatik.sopra.web_generation.TableRow;

/**
 * The Main View. From here you navigate everywhere in our Web-Application.
 * 
 * @author Georg Lippold
 */
public class MainView extends HttpServlet {

    private static final long serialVersionUID = 5066473981257720903L;

    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        final PrintableList page = new PrintableList(req, resp);
        final HttpSession session = req.getSession();
        final ResourceBundleStore rbs = new ResourceBundleStore();
        rbs.setBaseName("UeA-sopra");
        rbs.setLocale(req.getLocale());
        rbs.setServletName(this.getClass().getName());
        page.append(new Header(req, resp));
        boolean passfailed = false;
        final PrintableForm logout = new PrintableForm(req, resp, resp.encodeURL("Login"), "POST", "", null);
        logout.append(new FormSubmit(resp, "logout", rbs.getString("logout")));
        page.append(logout);
        try {
            final Mensch m = (Mensch) session.getAttribute("mensch");
            try {
                if (session.getAttribute("passfailed") != null) {
                    passfailed = true;
                    session.removeAttribute("passfailed");
                }
            } catch (final Exception e) {
            }
            if (passfailed) {
                page.append(new Content(resp, rbs.getString("passfailed")));
            }
            page.append(new Content(resp, rbs.getString("msg1") + " " + m.getVorname() + " " + m.getNachname() + rbs.getString("msg2")));
            final PrintableForm menschInfo = new PrintableForm(resp, resp.encodeURL("MainView"), "POST", "", null);
            menschInfo.append(new FormFieldText(resp, rbs.getString("prename"), "prename", 40, m.getVorname()));
            menschInfo.append(new FormFieldText(resp, rbs.getString("surname"), "surname", 40, m.getNachname()));
            final TableRow tr = new TableRow(resp);
            tr.append(new Content(resp, rbs.getString("mail")));
            tr.append(new Content(resp, m.getMail()));
            menschInfo.append(tr);
            menschInfo.append(new FormFieldBool(resp, rbs.getString("female"), "female", m.getFrau()));
            menschInfo.append(new FormFieldPass(resp, rbs.getString("pass"), "pass1", 20, m.getPasswort()));
            menschInfo.append(new FormFieldPass(resp, rbs.getString("pass_repeat"), "pass2", 20, m.getPasswort()));
            menschInfo.append(new FormSubmit(resp, rbs.getString("submit_mensch")));
            page.append(menschInfo);
            if (Administrator.istAdministrator(m)) {
                page.append(new Content(resp, "<HR>\n"));
                final Administrator a = new Administrator(m.getId());
                session.setAttribute("admin", a);
                page.append(new Content(resp, rbs.getString("admin1")));
                page.append(new Link(resp, resp.encodeURL("Accounts"), rbs.getString("accounts")));
                page.append(new Content(resp, "<HR>\n"));
                final PrintableForm adfrm = new PrintableForm(resp, resp.encodeURL("MainView"), "POST", "", null);
                adfrm.append(new FormSubmit(resp, "lock", rbs.getString("lock")));
                adfrm.append(new FormSubmit(resp, "unlock", rbs.getString("unlock")));
                adfrm.append(new FormSubmit(resp, "clear_results", rbs.getString("clear_results")));
                adfrm.append(new FormSubmit(resp, "new_results", rbs.getString("new_results")));
                page.append(adfrm);
                page.append(new Content(resp, "<HR>\n"));
            }
            if (Dozent.istDozent(m)) {
                page.append(new Content(resp, "<HR>\n"));
                final Dozent d = new Dozent(m.getId());
                session.setAttribute("dozent", d);
                page.append(new Content(resp, rbs.getString("dozent1")));
                page.append(new Link(resp, resp.encodeURL("Prof"), rbs.getString("prof")));
                page.append(new Content(resp, "<HR>\n"));
            }
            if (Tutor.istTutor(m)) {
                final Tutor t = new Tutor(m.getId());
                session.setAttribute("tutor", t);
                page.append(new Content(resp, rbs.getString("tutor1")));
            }
            if (Student.istStudent(m)) {
                try {
                    if (Administrator.isLocked() && !(Administrator.istAdministrator(m) || Dozent.istDozent(m))) {
                        resp.sendRedirect(resp.encodeURL("AccessDenied"));
                        return;
                    }
                } catch (final Exception e) {
                    session.setAttribute("error", e);
                    session.setAttribute("where", "Courses.doGet -> AccessDenied");
                    resp.sendRedirect(resp.encodeURL("Failure"));
                }
                page.append(new Content(resp, "<HR>\n"));
                final Student s = new Student(m.getId());
                session.setAttribute("student", s);
                page.append(new Content(resp, rbs.getString("student1")));
                page.append(new Link(resp, resp.encodeURL("Courses"), rbs.getString("courses")));
                page.append(new Link(resp, resp.encodeURL("Groups"), rbs.getString("groups")));
                page.append(new Content(resp, "<HR>\n"));
            }
            page.append(new Content(resp, rbs.getString("msg3")));
        } catch (final Exception e) {
            session.setAttribute("error", e);
            session.setAttribute("where", "MainView.doGet -> m");
            resp.sendRedirect(resp.encodeURL("Failure"));
        }
        page.append(logout);
        page.append(new Footer(req, resp));
        try {
            page.print();
        } catch (final Exception e) {
        }
    }

    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        final HttpSession session = req.getSession();
        final Mensch m = (Mensch) session.getAttribute("mensch");
        try {
            if (Administrator.isLocked() && !(Administrator.istAdministrator(m) || Dozent.istDozent(m))) {
                resp.sendRedirect(resp.encodeURL("AccessDenied"));
                return;
            }
        } catch (final Exception e) {
            session.setAttribute("error", e);
            session.setAttribute("where", "Courses.doGet -> AccessDenied");
            resp.sendRedirect(resp.encodeURL("Failure"));
        }
        try {
            if (Administrator.istAdministrator(m)) {
                if (req.getParameter("lock") != null) {
                    try {
                        Administrator.lockAnmeldung();
                    } catch (final Exception e) {
                        session.setAttribute("error", e);
                        session.setAttribute("where", "MainView.doPost -> lock()");
                        resp.sendRedirect(resp.encodeURL("Failure"));
                        return;
                    }
                }
                if (req.getParameter("unlock") != null) {
                    try {
                        Administrator.unlockAnmeldung();
                    } catch (final Exception e) {
                        session.setAttribute("error", e);
                        session.setAttribute("where", "MainView.doPost -> unlock()");
                        resp.sendRedirect(resp.encodeURL("Failure"));
                        return;
                    }
                }
                if (req.getParameter("clear_results") != null) {
                    try {
                        Administrator.clearAllErgebnisse();
                    } catch (final Exception e) {
                        session.setAttribute("error", e);
                        session.setAttribute("where", "MainView.doPost -> clearAllErgebnisse()");
                        resp.sendRedirect(resp.encodeURL("Failure"));
                        return;
                    }
                }
                if (req.getParameter("new_results") != null) {
                    final Results r = new Results();
                    r.start();
                }
            }
        } catch (final Exception e) {
            session.setAttribute("error", e);
            session.setAttribute("where", "MainView.doPost -> istAdministrator(m)");
            resp.sendRedirect(resp.encodeURL("Failure"));
            return;
        }
        String user = null;
        if (req.getParameter("user") != null) {
            user = StringToHtml.stringToHTMLString(req.getParameter("user"));
        }
        String pass1 = null;
        if (req.getParameter("pass1") != null) {
            pass1 = StringToHtml.stringToHTMLString(req.getParameter("pass1"));
        }
        String pass2 = null;
        if (req.getParameter("pass2") != null) {
            pass2 = StringToHtml.stringToHTMLString(req.getParameter("pass2"));
        }
        String prename = null;
        if (req.getParameter("prename") != null) {
            prename = StringToHtml.stringToHTMLString(req.getParameter("prename"));
        }
        String surname = null;
        if (req.getParameter("surname") != null) {
            surname = StringToHtml.stringToHTMLString(req.getParameter("surname"));
        }
        final String female = null;
        if (req.getParameter("female") != null) {
            StringToHtml.stringToHTMLString(req.getParameter("female"));
        }
        try {
            if (pass1.equals(pass2) && (pass1.length() > 5)) {
                m.setUsername(user);
                m.setPasswort(pass1);
                m.setVorname(prename);
                m.setNachname(surname);
                m.setFrau(new Boolean(female).booleanValue());
                try {
                    m.sync();
                } catch (final Exception e) {
                    session.setAttribute("error", e);
                    session.setAttribute("where", "MainView.doPost -> m.sync()");
                    resp.sendRedirect(resp.encodeURL("Failure"));
                    return;
                }
            } else {
                session.setAttribute("passfailed", "true");
                resp.sendRedirect(resp.encodeURL("MainView"));
                return;
            }
        } catch (final Exception e) {
        }
        session.removeAttribute("mensch");
        session.setAttribute("mensch", m);
        resp.sendRedirect(resp.encodeURL("MainView"));
        return;
    }
}
