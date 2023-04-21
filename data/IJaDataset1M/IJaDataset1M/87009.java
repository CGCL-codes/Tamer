package de.banh.bibo.servlet;

import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.banh.bibo.exceptions.CannotSearchEntityException;
import de.banh.bibo.model.Manager;

public class BenutzerSuchenServlet extends AbstractSuchenServlet {

    private static final long serialVersionUID = 6816825448054965333L;

    private static Logger logger = Logger.getLogger(BenutzerSuchenServlet.class.getName());

    public BenutzerSuchenServlet() {
        super("_benutzer_suchen.jsp");
    }

    @Override
    protected void executeSearch(HttpServletRequest req, HttpServletResponse res, String suchtext) throws CannotSearchEntityException {
        Manager mgr = getBiboManager(req);
        logger.info("BenutzerSuchenServlet hat sich einen Manager mittels getBiboManager(req) geholt.");
        setSuchergebnis(mgr.getBenutzerManager().getBenutzer(suchtext));
    }
}
