package au.edu.uq.itee.eresearch.dimer.webapp.app.view.xml;

import java.util.Collection;
import javax.jcr.RepositoryException;
import org.jdom2.Content;

public interface XMLFragment {

    Collection<Content> getContent() throws RepositoryException;
}
