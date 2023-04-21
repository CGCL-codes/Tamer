package edu.psu.citeseerx.oai.verbs;

import javax.servlet.http.HttpServletRequest;
import org.jdom.Element;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * Utility class to generate the anwser when a badVerb error occurs.
 * @author Pradeep Teregowda
 * @author Juan Pablo Fernandez Ramirez
 * @version $Rev: 958 $ $Date: 2009-02-17 16:49:07 -0500 (Tue, 17 Feb 2009) $
 */
public class BadVerb extends AbstractVerb {

    protected static final String[] expectedArguments = { "verb:true", "identifier:false", "metadataPrefix:false", "until:false", "from:false", "set:false", "resumptionToken:false" };

    public BadVerb() {
        super();
        for (int i = 0; i < expectedArguments.length; ++i) {
            String[] values = expectedArguments[i].split(":");
            addArgument(values[0], Boolean.parseBoolean(values[1]));
        }
    }

    @Override
    protected Element doProcess(HttpServletRequest request, Element root) throws OAIVerbException {
        String verb = null;
        verb = ServletRequestUtils.getStringParameter(request, "verb", "");
        addError(new OAIError("Illegal OAI-PMH verb: " + verb, OAIError.BAD_VERB_ERROR));
        throw new OAIVerbException(getErrors());
    }
}
