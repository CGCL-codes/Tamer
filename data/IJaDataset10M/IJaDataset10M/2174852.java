package org.sergioveloso.spott.command.artifact;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Collections;
import org.sergioveloso.spott.command.Command;
import org.sergioveloso.spott.config.DispatchProperties;
import org.sergioveloso.spott.model.entity.Artifact;
import org.sergioveloso.spott.model.db.ArtifactDAO;
import org.sergioveloso.spott.model.db.DAOFactory;
import org.sergioveloso.spott.exception.DAOException;

public class ListArtifactsCommand extends Command {

    @Override
    public int execute(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        this.log("Processing request.");
        try {
            ArtifactDAO dao = DAOFactory.createArtifactHibernateDAO();
            List<Artifact> artifacts = dao.getArtifacts();
            Collections.sort(artifacts);
            req.setAttribute("artifactList", artifacts);
            log("Returning list of Artifacts.");
        } catch (DAOException de) {
            log("got DAOException: " + de.getMessage());
            signal(de, "got DAOException", "There was an error accessing the Database");
        }
        return DispatchProperties.RET_ARTIFACTS;
    }
}
