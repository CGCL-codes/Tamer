package hci.gnomex.controller;

import hci.gnomex.model.ArrayCoordinate;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;

public class DeleteSlideSet extends GNomExCommand implements Serializable {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteSlideSet.class);

    private Integer idSlideProduct = null;

    public void validate() {
    }

    public void loadCommand(HttpServletRequest request, HttpSession session) {
        if (request.getParameter("idSlideProduct") != null && !request.getParameter("idSlideProduct").equals("")) {
            idSlideProduct = new Integer(request.getParameter("idSlideProduct"));
        } else {
            this.addInvalidField("idSlideProduct", "idSlideProduct is required.");
        }
        if (isValid()) {
            setResponsePage(this.SUCCESS_JSP);
        } else {
            setResponsePage(this.ERROR_JSP);
        }
    }

    public Command execute() throws RollBackCommandException {
        try {
            Session sess = HibernateSession.currentSession(this.getUsername());
            if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
                SlideProduct slideProduct = (SlideProduct) sess.load(SlideProduct.class, idSlideProduct);
                if (this.isValid()) {
                    Iterator sdIter = slideProduct.getSlideDesigns().iterator();
                    while (sdIter.hasNext()) {
                        SlideDesign sd = (SlideDesign) sdIter.next();
                        List arrayCoords = sess.createQuery("SELECT ac from ArrayCoordinate ac where ac.idSlideDesign = " + sd.getIdSlideDesign()).list();
                        for (Iterator i = arrayCoords.iterator(); i.hasNext(); ) {
                            ArrayCoordinate ac = (ArrayCoordinate) i.next();
                            sess.delete(ac);
                        }
                    }
                    sess.delete(slideProduct);
                    sess.flush();
                }
                this.xmlResult = "<SUCCESS/>";
                setResponsePage(this.SUCCESS_JSP);
            } else {
                this.addInvalidField("insufficient permission", "Insufficient permissions to delete slide set.");
                setResponsePage(this.ERROR_JSP);
            }
        } catch (Exception e) {
            log.error("An exception has occurred in DeleteSlideSet ", e);
            e.printStackTrace();
            throw new RollBackCommandException(e.getMessage());
        } finally {
            try {
                HibernateSession.closeSession();
            } catch (Exception e) {
            }
        }
        if (isValid()) {
            setResponsePage(this.SUCCESS_JSP);
        } else {
            setResponsePage(this.ERROR_JSP);
        }
        return this;
    }
}
