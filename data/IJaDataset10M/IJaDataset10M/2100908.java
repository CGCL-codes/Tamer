package edu.columbia.hypercontent.editors.vcard.commands;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import edu.columbia.hypercontent.DocumentFactory;
import edu.columbia.hypercontent.L10n;
import edu.columbia.hypercontent.editors.BaseSessionData;
import edu.columbia.hypercontent.editors.ICommand;
import edu.columbia.hypercontent.editors.vcard.SessionData;
import edu.columbia.hypercontent.editors.vcard.model.LdapVCardLoader;
import edu.columbia.hypercontent.editors.vcard.model.RDFConverter;
import edu.columbia.hypercontent.editors.vcard.model.VCard;
import edu.columbia.hypercontent.vocabulary.VCARD;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: Sep 8, 2003
 * Time: 3:48:55 PM
 * To change this template use Options | File Templates.
 */
public class SearchDirectory implements ICommand {

    protected static Random random = new Random();

    public void execute(BaseSessionData baseSession) throws Exception {
        SessionData session = (SessionData) baseSession;
        String search = session.runtimeData.getParameter("search");
        VCard[] results = LdapVCardLoader.getVCards(search);
        if (results.length == 0) {
            session.feedback = L10n.getLocalizedString(session, "NO_MATCH", search);
        } else if (results.length == 1) {
            session.card = results[0];
        } else {
            RDFConverter.removeAllVCards(session.model);
            for (int i = 0; i < results.length; i++) {
                VCard result = results[i];
                String uri = String.valueOf(random.nextLong());
                Resource vcr = session.model.createResource(uri);
                vcr.addProperty(RDF.type, VCARD.VCARD);
                RDFConverter.vCardToRDF(result, vcr);
            }
            session.xml = DocumentFactory.getRDFDocument(session.model, session.global.getProject().getResolver());
        }
    }
}
