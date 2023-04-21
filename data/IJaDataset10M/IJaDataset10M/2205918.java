package org.marcont.portal.rest.server.users;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import java.io.FileReader;
import javax.servlet.*;
import javax.servlet.http.*;
import org.marcont.portal.rest.server.RestServlet;
import org.marcont.portal.rest.server.ServiceException;
import org.marcont.portal.user.UserException;
import org.marcont.portal.user.UserManager;
import org.marcont.services.definitions.grounding.Grounding_DOT_owlFactory;
import org.marcont.services.definitions.grounding.MalformedRequestError;
import org.marcont.services.definitions.grounding.RestProcessGrounding;
import org.marcont.services.definitions.grounding.SuccessReport;

/**
 *
 * @author katar
 * @version
 */
public class Register extends RestServlet {

    public Register() {
        super("users/Register.owl", "RegisterUserService");
    }

    private void checkPasswordsEquality() throws ServiceException {
        if (!parameters.get("password").equals(parameters.get("repeatedPassword"))) {
            MalformedRequestError mre = Grounding_DOT_owlFactory.createMalformedRequestError("http://www.marcont.org#PasswordsDoNotMatch", model);
            mre.setHasDescription("Passwords do not match");
            mre.addConcernsParameter(inputs.get("password"));
            mre.addConcernsParameter(inputs.get("repeatedPassword"));
            errorsReport.addHasError(mre);
            throw new ServiceException(HttpServletResponse.SC_BAD_REQUEST, errorsReport);
        }
    }

    protected Object execute() {
        return null;
    }
}
