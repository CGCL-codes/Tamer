package se.vgregion.portal.cs.service;

import org.jsoup.nodes.Document;
import se.vgregion.portal.cs.domain.Form;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-12-27 13:49
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface LoginformService {

    List<Form> extract(Document doc);
}
