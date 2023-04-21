package net.sf.chellow.physical;

import java.util.List;
import net.sf.chellow.monad.DeployerException;
import net.sf.chellow.monad.DesignerException;
import net.sf.chellow.monad.Hiber;
import net.sf.chellow.monad.Invocation;
import net.sf.chellow.monad.MonadUtils;
import net.sf.chellow.monad.ProgrammerException;
import net.sf.chellow.monad.Urlable;
import net.sf.chellow.monad.UserException;
import net.sf.chellow.monad.types.MonadUri;
import net.sf.chellow.monad.types.UriPathElement;
import net.sf.chellow.ui.Chellow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Sscs implements Urlable {

    public static final UriPathElement URI_ID;

    static {
        try {
            URI_ID = new UriPathElement("sscs");
        } catch (UserException e) {
            throw new RuntimeException(e);
        } catch (ProgrammerException e) {
            throw new RuntimeException(e);
        }
    }

    public Sscs() {
    }

    public UriPathElement getUrlId() {
        return URI_ID;
    }

    public MonadUri getUri() throws ProgrammerException, UserException {
        return Chellow.getUrlableRoot().getUri().resolve(getUrlId()).append("/");
    }

    @SuppressWarnings("unchecked")
    public void httpGet(Invocation inv) throws DesignerException, ProgrammerException, UserException, DeployerException {
        Document doc = MonadUtils.newSourceDocument();
        Element source = doc.getDocumentElement();
        for (Ssc ssc : (List<Ssc>) Hiber.session().createQuery("from Ssc ssc order by ssc.code").list()) {
            source.appendChild(ssc.toXML(doc));
        }
        inv.sendOk(doc);
    }

    public void httpPost(Invocation inv) throws ProgrammerException, UserException {
    }

    public Ssc getChild(UriPathElement uriId) throws UserException, ProgrammerException {
        return Ssc.getSsc(Long.parseLong(uriId.getString()));
    }

    public void httpDelete(Invocation inv) throws ProgrammerException, UserException {
    }
}
