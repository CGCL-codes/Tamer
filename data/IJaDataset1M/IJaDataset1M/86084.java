package net.sf.jvdr.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.exlp.util.DateUtil;
import net.sf.jvdr.data.ejb.VdrUser;
import net.sf.jvdr.data.ejb.VdrUserSession;
import net.sf.jvdr.data.facade.VdrPersistence;
import net.sf.jwan.servlet.exception.WanRenderException;
import net.sf.jwan.servlet.gui.elements.AbstractWanServletPage;
import net.sf.jwan.servlet.gui.elements.WanDiv;
import net.sf.jwan.servlet.gui.elements.WanParagraph;
import net.sf.jwan.servlet.gui.form.WanForm;
import net.sf.jwan.servlet.gui.form.WanFormFieldSet;
import net.sf.jwan.servlet.gui.form.WanFormInputText;
import net.sf.jwan.servlet.gui.layer.WanContentLayer;
import net.sf.jwan.servlet.gui.renderable.WanRenderable;
import net.sf.jwan.servlet.util.ServletForm;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.kisner.util.io.ObjectIO;

public class LoginServlet extends AbstractWanServletPage {

    static Log logger = LogFactory.getLog(LoginServlet.class);

    public static final long serialVersionUID = 1;

    private static enum LoginResult {

        Unknown, InActive, Authenticated, WrongPassword, Admin
    }

    ;

    private Configuration config;

    public LoginServlet(Configuration config) {
        this.config = config;
        title = "jVDR Login";
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Back ...");
        response.setContentType("text/xml");
        response.setStatus(HttpServletResponse.SC_OK);
        ServletForm form = new ServletForm(request);
        LoginResult loginResult = proceedLogin(response, form);
        VdrPersistence vdrP = (VdrPersistence) getServletContext().getAttribute(VdrPersistence.class.getSimpleName());
        VdrUser vu = vdrP.fVdrUserByName(form.get("name"));
        WanContentLayer wL = getSubmitLayer();
        WanDiv wd = new WanDiv();
        wd.setDivclass(WanDiv.DivClass.iBlock);
        wd.addContent(informUser(loginResult, vu));
        wL.addContent(wd);
        wL.setAsyncZone("waHome");
        PrintWriter out = response.getWriter();
        try {
            out.println(wL.renderAsync());
        } catch (WanRenderException e) {
            logger.error(e);
        } finally {
            out.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        alSublayer.clear();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");
        WanContentLayer mainLayer = new WanContentLayer("Home");
        mainLayer.setLayerTitle("Home");
        mainLayer.addContent(genLoginForm());
        setMainLayer(mainLayer);
        addSubLayer(getSubmitLayer());
        PrintWriter out = response.getWriter();
        try {
            out.println(render());
        } catch (WanRenderException e) {
            logger.error(e);
        } finally {
            out.close();
        }
    }

    private WanContentLayer getSubmitLayer() {
        WanContentLayer wl = new WanContentLayer("lLogin");
        wl.setLayerTitle("Login Result");
        return wl;
    }

    private WanRenderable genLoginForm() {
        WanForm wf = new WanForm();
        wf.setRightButtonTitel("Save");
        wf.setAction("/jvdr/login");
        WanFormFieldSet wffs = new WanFormFieldSet();
        wffs.setName("Allgemein");
        wf.addFieldSet(wffs);
        WanFormInputText textName = new WanFormInputText();
        textName.setName(VdrUser.Key.name.toString());
        textName.setPlaceholderText("Enter username");
        wffs.addInput(textName);
        WanFormInputText textPassword = new WanFormInputText();
        textPassword.setName(VdrUser.Key.password.toString());
        textPassword.setPlaceholderText("Enter password");
        wffs.addInput(textPassword);
        return wf;
    }

    private LoginResult proceedLogin(HttpServletResponse response, ServletForm form) {
        String name = form.get(VdrUser.Key.name.toString());
        String password = form.get(VdrUser.Key.password.toString());
        LoginResult loginresult = LoginResult.Unknown;
        VdrPersistence vdrP = (VdrPersistence) getServletContext().getAttribute(VdrPersistence.class.getSimpleName());
        VdrUser vu = vdrP.fVdrUserByName(name);
        if (vu == null) {
            vu = new VdrUser();
            vu.setName(name);
            vu.setActive(false);
            vu.setPassword(password);
            logger.debug("Will persist");
            vu = (VdrUser) vdrP.newObject(vu);
        }
        if (vu.getName().equals(name) && vu.getPassword().equals(password)) {
            boolean overrideAdmin = false;
            int anzAdmin = config.getStringArray("admin/userid").length;
            for (int i = 1; i <= anzAdmin; i++) {
                if (config.getInt("admin/userid[" + i + "]") == vu.getId()) {
                    overrideAdmin = true;
                }
            }
            if (vu.isActive() || overrideAdmin) {
                int cookieValid = 60 * 60 * 24 * 30;
                Date d = new Date();
                Date validUntil = new Date(d.getTime() + (1000 * cookieValid));
                Random rnd = new Random();
                String hash = vu.getName() + vu.getPassword() + DateUtil.mtjsms(d) + DateUtil.dayName(d) + rnd.nextInt(10000000);
                String session = ObjectIO.printByteStream(ObjectIO.getHash(hash), false);
                VdrUserSession vdrSession = new VdrUserSession();
                vdrSession.setVdruser(vu);
                vdrSession.setSession(session);
                vdrSession.setValidUntil(validUntil);
                vdrP.newObject(vdrSession);
                Cookie cookie = new Cookie("jvdr", session);
                cookie.setMaxAge(cookieValid);
                response.addCookie(cookie);
                if (vu.isActive()) {
                    loginresult = LoginResult.Authenticated;
                } else {
                    vu.setActive(true);
                    vdrP.updateObject(vu);
                    loginresult = LoginResult.Admin;
                }
            } else {
                loginresult = LoginResult.InActive;
            }
        } else if (vu.getName().equals(name) && !vu.getPassword().equals(password)) {
            loginresult = LoginResult.WrongPassword;
        }
        return loginresult;
    }

    private List<WanRenderable> informUser(LoginResult loginResult, VdrUser vu) {
        List<WanRenderable> lRenderable = new ArrayList<WanRenderable>();
        WanParagraph wp;
        switch(loginResult) {
            case InActive:
                wp = new WanParagraph();
                wp.setHeader("Neuer Benutzer: " + vu.getName());
                wp.setContent("Warten Sie, bis Sie freigeschaltet sind");
                lRenderable.add(wp);
                break;
            case Authenticated:
                wp = new WanParagraph();
                wp.setHeader("Angemeldet");
                wp.setContent(vu.getName() + ", Sie sind erfolgreich angemeldet");
                lRenderable.add(wp);
                wp = new WanParagraph();
                wp.setContent("<a href=\"/jvdr\">Hier</a> geht es zur Hauptseite");
                lRenderable.add(wp);
                break;
            case Admin:
                wp = new WanParagraph();
                wp.setHeader("Angemeldet");
                wp.setContent(vu.getName() + ", Sie sind erfolgreich angemeldet und als Admin eingetragen.");
                lRenderable.add(wp);
                wp = new WanParagraph();
                wp.setContent("<a href=\"/jvdr\">Hier</a> geht es zur Hauptseite");
                lRenderable.add(wp);
                break;
            case WrongPassword:
                wp = new WanParagraph();
                wp.setHeader("Falsches Kennwort");
                wp.setContent(vu.getName() + ", Sie haben ein falsschens Kennwort eingegeben.");
                lRenderable.add(wp);
                break;
            default:
                wp = new WanParagraph();
                wp.setHeader("Fehler");
                wp.setContent("Bitte Entwickler kontaktieren");
                lRenderable.add(wp);
                break;
        }
        return lRenderable;
    }
}
