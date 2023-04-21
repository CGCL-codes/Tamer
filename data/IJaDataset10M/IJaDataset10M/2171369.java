package net.sf.jvdr.http.servlet.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jvdr.data.ejb.VdrUser;
import net.sf.jvdr.data.facade.VdrPersistence;
import net.sf.jwan.servlet.exception.WanRenderException;
import net.sf.jwan.servlet.gui.elements.WanDiv;
import net.sf.jwan.servlet.gui.elements.WanParagraph;
import net.sf.jwan.servlet.gui.form.WanForm;
import net.sf.jwan.servlet.gui.form.WanFormFieldSet;
import net.sf.jwan.servlet.gui.form.WanFormInputCheckBox;
import net.sf.jwan.servlet.gui.layer.AbstractWanServletLayer;
import net.sf.jwan.servlet.gui.renderable.WanRenderable;
import net.sf.jwan.servlet.util.ServletForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AdminUserMngmtServlet extends AbstractWanServletLayer {

    static Log logger = LogFactory.getLog(AdminUserMngmtServlet.class);

    public static final long serialVersionUID = 1;

    public AdminUserMngmtServlet() {
        super("lAdmUserMng");
        layerTitle = "Benutzerverwaltung";
        layerServletPath = "async";
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        alWanRenderables.clear();
        response.setContentType("text/xml");
        response.setStatus(HttpServletResponse.SC_OK);
        processSubmittedForm(request);
        PrintWriter out = response.getWriter();
        try {
            out.println(renderAsync());
        } catch (WanRenderException e) {
            logger.error(e);
        } finally {
            out.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        alWanRenderables.clear();
        response.setContentType("text/xml");
        response.setStatus(HttpServletResponse.SC_OK);
        WanDiv wd = new WanDiv();
        wd.setDivclass(WanDiv.DivClass.NONE);
        wd.addContent(createForm(request));
        alWanRenderables.add(wd);
        PrintWriter out = response.getWriter();
        try {
            out.println(renderAsync());
        } catch (WanRenderException e) {
            logger.error(e);
        } finally {
            out.close();
        }
    }

    public WanRenderable createForm(HttpServletRequest request) {
        VdrPersistence vdrP = (VdrPersistence) getServletContext().getAttribute(VdrPersistence.class.getSimpleName());
        VdrUser vdrUser = (VdrUser) request.getSession().getAttribute(VdrUser.class.getSimpleName());
        ServletForm form = new ServletForm(request);
        List<VdrUser> lVdrUser = (List<VdrUser>) vdrP.findObjects(VdrUser.class.getSimpleName());
        logger.trace(lVdrUser.size() + " Benutzer gefunden");
        WanForm wf = new WanForm();
        wf.setRightButtonTitel("Save");
        wf.setAction("admin");
        WanFormFieldSet wffsActive = new WanFormFieldSet("Aktive Benutzer");
        wf.addFieldSet(wffsActive);
        for (VdrUser vu : lVdrUser) {
            if (vu.isActive()) {
                WanFormInputCheckBox box = new WanFormInputCheckBox(vu.getName());
                box.setName("u" + vu.getId());
                box.setId("id" + vu.getId());
                box.setChecked(vu.isActive());
                if (vdrUser.getId() == vu.getId()) {
                    box.setDisabled(true);
                }
                wffsActive.addInput(box);
            }
        }
        WanFormFieldSet wffsInActive = new WanFormFieldSet("Aktive Benutzer");
        wf.addFieldSet(wffsInActive);
        for (VdrUser vu : lVdrUser) {
            if (!vu.isActive()) {
                WanFormInputCheckBox box = new WanFormInputCheckBox(vu.getName());
                box.setName("u" + vu.getId());
                box.setId("id" + vu.getId());
                box.setChecked(vu.isActive());
                if (vdrUser.getId() == vu.getId()) {
                    box.setDisabled(true);
                }
                wffsInActive.addInput(box);
            }
        }
        return wf;
    }

    public void processSubmittedForm(HttpServletRequest request) {
        VdrPersistence vdrP = (VdrPersistence) getServletContext().getAttribute(VdrPersistence.class.getSimpleName());
        ServletForm form = new ServletForm(request);
        for (VdrUser vu : (List<VdrUser>) vdrP.findObjects(VdrUser.class.getSimpleName())) {
            boolean active = form.getBoolean("u" + vu.getId(), false);
            if (active != vu.isActive()) {
                vu.setActive(active);
                vdrP.updateObject(vu);
            }
        }
        WanDiv div = new WanDiv();
        div.setDivclass(WanDiv.DivClass.iBlock);
        div.addContent(new WanParagraph("Einstellungen gespeichert"));
        alWanRenderables.add(div);
    }
}
