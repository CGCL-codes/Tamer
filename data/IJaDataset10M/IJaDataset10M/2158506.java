package de.uni_bremen.informatik.sopra.web_logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import de.uni_bremen.informatik.sopra.db.Administrator;
import de.uni_bremen.informatik.sopra.db.Gruppe;
import de.uni_bremen.informatik.sopra.db.Mensch;
import de.uni_bremen.informatik.sopra.db.Student;
import de.uni_bremen.informatik.sopra.db.Veranstaltung;
import de.uni_bremen.informatik.sopra.web_generation.Button;
import de.uni_bremen.informatik.sopra.web_generation.Content;
import de.uni_bremen.informatik.sopra.web_generation.Footer;
import de.uni_bremen.informatik.sopra.web_generation.FormFieldRadio;
import de.uni_bremen.informatik.sopra.web_generation.FormFieldText;
import de.uni_bremen.informatik.sopra.web_generation.FormSubmit;
import de.uni_bremen.informatik.sopra.web_generation.Header;
import de.uni_bremen.informatik.sopra.web_generation.InputField;
import de.uni_bremen.informatik.sopra.web_generation.PrintableForm;
import de.uni_bremen.informatik.sopra.web_generation.PrintableList;
import de.uni_bremen.informatik.sopra.web_generation.ResourceBundleStore;
import de.uni_bremen.informatik.sopra.web_generation.TableRow;

/**
 * Der Studenten-Dialog zur Gruppenverwaltung
 */
public class Groups extends HttpServlet {

    private static final long serialVersionUID = 4265691269564693346L;

    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        final HttpSession session = req.getSession();
        try {
            if (Administrator.isLocked()) {
                resp.sendRedirect(resp.encodeURL("AccessDenied"));
                return;
            }
        } catch (final Exception e) {
            session.setAttribute("error", e);
            session.setAttribute("where", "Courses.doGet -> AccessDenied");
            resp.sendRedirect(resp.encodeURL("Failure"));
        }
        ArrayList invited = null;
        ArrayList member = null;
        ArrayList owner = null;
        final PrintableList page = new PrintableList(resp);
        final ResourceBundleStore rbs = new ResourceBundleStore();
        rbs.setBaseName("UeA-sopra");
        rbs.setLocale(req.getLocale());
        rbs.setServletName(this.getClass().getName());
        page.append(new Header(req, resp));
        final PrintableForm logout = new PrintableForm(resp, resp.encodeURL("Login"), "POST");
        logout.append(new FormSubmit(resp, "logout", rbs.getString("logout")));
        page.append(logout);
        Student s = null;
        boolean invite_failed = false;
        boolean newgrp_submit_failed = false;
        boolean invited_join_failed = false;
        boolean invited_reject_failed = false;
        boolean member_quit_failed = false;
        boolean dissolve_failed = false;
        boolean grp_name_failed = false;
        boolean grp_in_course = false;
        try {
            if (session.getAttribute("invite failed") != null) {
                invite_failed = true;
            }
            if (session.getAttribute("newgroup_submit failed") != null) {
                newgrp_submit_failed = true;
            }
            if (session.getAttribute("invited_join failed") != null) {
                invited_join_failed = true;
            }
            if (session.getAttribute("invited_reject failed") != null) {
                invited_reject_failed = true;
            }
            if (session.getAttribute("member_quit failed") != null) {
                member_quit_failed = true;
            }
            if (session.getAttribute("dissolve failed") != null) {
                dissolve_failed = true;
            }
            if (session.getAttribute("grp_name failed") != null) {
                grp_name_failed = true;
            }
            if (session.getAttribute("grp_in_course") != null) {
                grp_in_course = true;
            }
        } catch (final Exception e) {
        }
        try {
            s = (Student) session.getAttribute("student");
        } catch (final Exception e) {
            session.setAttribute("error", e);
            session.setAttribute("where", "Groups.doGet -> student s");
            resp.sendRedirect(resp.encodeURL("Failure"));
            return;
        }
        if (invite_failed) {
            page.append(new Content(resp, rbs.getString("invite_failed")));
        }
        if (newgrp_submit_failed) {
            page.append(new Content(resp, rbs.getString("newgrp_submit_failed")));
        }
        if (invited_join_failed) {
            page.append(new Content(resp, rbs.getString("invited_join_failed")));
        }
        if (invited_reject_failed) {
            page.append(new Content(resp, rbs.getString("invited_reject_failed")));
        }
        if (member_quit_failed) {
            page.append(new Content(resp, rbs.getString("member_quit_failed")));
        }
        if (dissolve_failed) {
            page.append(new Content(resp, rbs.getString("dissolve_failed")));
        }
        if (grp_name_failed) {
            page.append(new Content(resp, rbs.getString("grp_name_failed")));
        }
        if (grp_in_course) {
            page.append(new Content(resp, rbs.getString("grp_alreay_present")));
        }
        page.append(new Content(resp, "<HR>"));
        page.append(new Content(resp, rbs.getString("grp_view")));
        final PrintableForm grpform = new PrintableForm(resp, resp.encodeURL("Groups"), "POST", "", null);
        try {
            invited = s.eingeladen();
            member = s.drin();
            owner = s.getGruppeGruender();
        } catch (final Exception e) {
            session.setAttribute("error", e);
            session.setAttribute("where", "Groups.doGet -> s.group-operations");
            resp.sendRedirect(resp.encodeURL("Failure"));
            return;
        }
        if (!invited.isEmpty()) {
            TableRow tr = new TableRow(resp);
            tr.append(new Content(resp, ""));
            tr.append(new Content(resp, rbs.getString("invited")));
            grpform.append(tr);
            final Iterator inv_it = invited.iterator();
            while (inv_it.hasNext()) {
                final Gruppe g = (Gruppe) inv_it.next();
                if (g.countAllIn() < g.getMax()) {
                    grpform.append(new FormFieldRadio(resp, g.getName() + " <BR> " + g.getVeranstaltung().getName(), "invited", g.getName()));
                }
            }
            tr = new TableRow(resp);
            tr.append(new Content(resp, ""));
            tr.append(new Button(resp, "invited_join", rbs.getString("join")));
            grpform.append(tr);
            tr = new TableRow(resp);
            tr.append(new Content(resp, ""));
            tr.append(new Button(resp, "invited_reject", rbs.getString("reject")));
            grpform.append(tr);
            tr = new TableRow(resp);
            tr.append(new Content(resp, "<HR>\n"));
            grpform.append(tr);
        }
        if (!member.isEmpty()) {
            TableRow tr = new TableRow(resp);
            tr.append(new Content(resp, ""));
            tr.append(new Content(resp, rbs.getString("member")));
            grpform.append(tr);
            tr = new TableRow(resp);
            tr.append(new Content(resp, ""));
            final PrintableList p = new PrintableList(resp);
            p.append(new Content(resp, rbs.getString("invite_user") + "<BR>"));
            p.append(new InputField(resp, "invite_user1", 20));
            p.append(new Button(resp, "invite_user", rbs.getString("invite")));
            tr.append(p);
            grpform.append(tr);
            final Iterator mbr_it = member.iterator();
            while (mbr_it.hasNext()) {
                final Gruppe g = (Gruppe) mbr_it.next();
                grpform.append(new FormFieldRadio(resp, g.getName() + " <BR> " + g.getVeranstaltung().getName(), "member", g.getName()));
            }
            tr = new TableRow(resp);
            tr.append(new Content(resp, ""));
            tr.append(new Button(resp, "member_quit", rbs.getString("quit")));
            grpform.append(tr);
            tr = new TableRow(resp);
            tr.append(new Content(resp, "<HR>\n"));
            grpform.append(tr);
        }
        if (!owner.isEmpty()) {
            TableRow tr = new TableRow(resp);
            tr.append(new Content(resp, ""));
            tr.append(new Content(resp, rbs.getString("owner")));
            grpform.append(tr);
            tr = new TableRow(resp);
            tr.append(new Content(resp, ""));
            final PrintableList p = new PrintableList(resp);
            p.append(new Content(resp, rbs.getString("invite_user") + "<BR>"));
            p.append(new InputField(resp, "invite_user2", 20));
            p.append(new Button(resp, "invite", rbs.getString("invite")));
            tr.append(p);
            grpform.append(tr);
            final Iterator own_it = owner.iterator();
            while (own_it.hasNext()) {
                final Gruppe g = (Gruppe) own_it.next();
                grpform.append(new FormFieldRadio(resp, g.getName() + " <BR> " + g.getVeranstaltung().getName(), "owner", g.getName()));
            }
            tr = new TableRow(resp);
            tr.append(new Content(resp, ""));
            tr.append(new Button(resp, "dissolve", rbs.getString("dissolve")));
            grpform.append(tr);
        }
        page.append(grpform);
        page.append(new Content(resp, "<HR>"));
        try {
        } catch (final Exception e) {
        }
        ArrayList veranst = null;
        try {
            final Veranstaltung v = new Veranstaltung();
            veranst = v.getAllVeranstaltungen();
        } catch (final Exception e) {
            session.setAttribute("error", e);
            session.setAttribute("where", "Groups.doGet -> Veranstaltung.getAllVeranstaltungen()");
            resp.sendRedirect(resp.encodeURL("Failure"));
            return;
        }
        page.append(new Content(resp, rbs.getString("new_group")));
        final PrintableForm newgrp = new PrintableForm(resp, resp.encodeURL("Groups"), "POST");
        newgrp.append(new FormFieldText(resp, rbs.getString("grp_name"), "group", 20));
        final TableRow tr = new TableRow(resp);
        tr.append(new Content(resp, ""));
        tr.append(new Content(resp, rbs.getString("which_course")));
        newgrp.append(tr);
        final Iterator it = veranst.iterator();
        while (it.hasNext()) {
            final Veranstaltung v = (Veranstaltung) it.next();
            newgrp.append(new FormFieldRadio(resp, v.getName(), "course", v.getVak() + ""));
        }
        newgrp.append(new FormSubmit(resp, "newgrp_submit", rbs.getString("submit")));
        page.append(newgrp);
        page.append(new Content(resp, "<HR>"));
        page.append(new Footer(req, resp));
        try {
            page.print();
        } catch (final Exception e) {
            session.setAttribute("error", e);
            session.setAttribute("where", "Groups.doGet -> page.print()");
            resp.sendRedirect(resp.encodeURL("Failure"));
            return;
        }
    }

    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        final HttpSession session = req.getSession();
        try {
            if (Administrator.isLocked()) {
                resp.sendRedirect(resp.encodeURL("AccessDenied"));
                return;
            }
        } catch (final Exception e) {
            session.setAttribute("error", e);
            session.setAttribute("where", "Courses.doGet -> AccessDenied");
            resp.sendRedirect(resp.encodeURL("Failure"));
        }
        Mensch m;
        try {
            m = (Mensch) session.getAttribute("mensch");
        } catch (final Exception e) {
            session.setAttribute("error", e);
            session.setAttribute("where", "Groups.doPost() -> instanciate Mensch");
            resp.sendRedirect(resp.encodeURL("Failure"));
            return;
        }
        if (req.getParameter("invite") != null) {
            try {
                if (Gruppe.istVergeben(StringToHtml.stringToHTMLString(req.getParameter("member")))) {
                    final Gruppe g = new Gruppe(StringToHtml.stringToHTMLString(req.getParameter("member")));
                    final Mensch k = new Mensch(StringToHtml.stringToHTMLString(req.getParameter("invite_user1")));
                    if (!g.istEingeladen(k.getId())) {
                        g.addEingeladen(k.getId());
                    }
                } else if (Gruppe.istVergeben(StringToHtml.stringToHTMLString(req.getParameter("owner")))) {
                    final Gruppe g = new Gruppe(StringToHtml.stringToHTMLString(req.getParameter("owner")));
                    final Mensch k = new Mensch(StringToHtml.stringToHTMLString(req.getParameter("invite_user2")));
                    if (!g.istEingeladen(k.getId())) {
                        g.addEingeladen(k.getId());
                    }
                } else {
                    session.setAttribute("invite failed", "true");
                    resp.sendRedirect(resp.encodeURL("Groups"));
                    return;
                }
            } catch (final Exception e) {
                session.setAttribute("error", e);
                session.setAttribute("where", "Groups.doPost() -> newgrp-submit");
                resp.sendRedirect(resp.encodeURL("Failure"));
                return;
            }
        }
        if (req.getParameter("newgrp_submit") != null) {
            try {
                if (Gruppe.istVergeben(StringToHtml.stringToHTMLString(req.getParameter("group")))) {
                    session.setAttribute("newgrp_submit failed", "true");
                    resp.sendRedirect(resp.encodeURL("Groups"));
                    return;
                } else {
                    final Student s = new Student(m.getId());
                    final ArrayList gruppen = s.drin();
                    gruppen.addAll(s.getGruppeGruender());
                    boolean grp_in_course = false;
                    final int vak = Integer.parseInt(req.getParameter("course"));
                    final Iterator grp_it = gruppen.iterator();
                    while (grp_it.hasNext()) {
                        final Gruppe g = (Gruppe) grp_it.next();
                        if (g.getVeranstaltung().getVak() == vak) {
                            grp_in_course = true;
                            session.setAttribute("grp_in_course", "true");
                            break;
                        }
                    }
                    if (!grp_in_course) {
                        final Gruppe g = new Gruppe(StringToHtml.stringToHTMLString(req.getParameter("group")), m, Integer.parseInt(req.getParameter("course")));
                        g.sync();
                        g.addStudent(m.getId());
                    }
                }
            } catch (final Exception e) {
                session.setAttribute("error", e);
                session.setAttribute("where", "Groups.doPost() -> newgrp-submit");
                resp.sendRedirect(resp.encodeURL("Failure"));
                return;
            }
        }
        if (req.getParameter("invited_join") != null) {
            try {
                if (Gruppe.istVergeben(StringToHtml.stringToHTMLString(req.getParameter("invited")))) {
                    final Gruppe g = new Gruppe(StringToHtml.stringToHTMLString(req.getParameter("invited")));
                    final Student s = new Student(m.getId());
                    final ArrayList gruppen = s.drin();
                    gruppen.addAll(s.getGruppeGruender());
                    boolean grp_in_course = false;
                    final int vak = g.getVeranstaltung().getVak();
                    final Iterator grp_it = gruppen.iterator();
                    while (grp_it.hasNext()) {
                        final Gruppe ng = (Gruppe) grp_it.next();
                        if (ng.getVeranstaltung().getVak() == vak) {
                            grp_in_course = true;
                            session.setAttribute("grp_in_course", "true");
                            break;
                        }
                    }
                    if ((!grp_in_course) && (g.getMax() > g.countAllIn())) {
                        g.addStudent(m.getId());
                    }
                } else {
                    session.setAttribute("invited_join failed", "true");
                    resp.sendRedirect(resp.encodeURL("Groups"));
                    return;
                }
            } catch (final Exception e) {
                session.setAttribute("error", e);
                session.setAttribute("where", "Groups.doPost() -> invited_join");
                resp.sendRedirect(resp.encodeURL("Failure"));
                return;
            }
        }
        if (req.getParameter("invited_reject") != null) {
            try {
                if (Gruppe.istVergeben(StringToHtml.stringToHTMLString(req.getParameter("invited")))) {
                    final Gruppe g = new Gruppe(StringToHtml.stringToHTMLString(req.getParameter("invited")));
                    g.removeEingeladen(m.getId());
                } else {
                    session.setAttribute("invited_reject failed", "true");
                    resp.sendRedirect(resp.encodeURL("Groups"));
                    return;
                }
            } catch (final Exception e) {
                session.setAttribute("error", e);
                session.setAttribute("where", "Groups.doPost() -> invited_reject");
                resp.sendRedirect(resp.encodeURL("Failure"));
                return;
            }
        }
        if (req.getParameter("member_quit") != null) {
            try {
                if (Gruppe.istVergeben(StringToHtml.stringToHTMLString(req.getParameter("member")))) {
                    final Gruppe g = new Gruppe(StringToHtml.stringToHTMLString(req.getParameter("member")));
                    g.removeStudent(m.getId());
                } else {
                    session.setAttribute("member_quit failed", "true");
                    resp.sendRedirect(resp.encodeURL("Groups"));
                    return;
                }
            } catch (final Exception e) {
                session.setAttribute("error", e);
                session.setAttribute("where", "Groups.doPost() -> member_quit");
                resp.sendRedirect(resp.encodeURL("Failure"));
                return;
            }
        }
        if (req.getParameter("dissolve") != null) {
            try {
                if (Gruppe.istVergeben(StringToHtml.stringToHTMLString(req.getParameter("owner")))) {
                    Gruppe g = new Gruppe(StringToHtml.stringToHTMLString(req.getParameter("owner")));
                    g.removeGruppe();
                    g = null;
                } else {
                    session.setAttribute("dissolve failed", "true");
                    resp.sendRedirect(resp.encodeURL("Groups"));
                    return;
                }
            } catch (final Exception e) {
                session.setAttribute("error", e);
                session.setAttribute("where", "Groups.doPost() -> dissolve");
                resp.sendRedirect(resp.encodeURL("Failure"));
                return;
            }
        }
        resp.sendRedirect(resp.encodeURL("Groups"));
        return;
    }
}
