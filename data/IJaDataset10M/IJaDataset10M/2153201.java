package at.langegger.xlwrap.server;

import org.joseki.DatasetDesc;
import org.joseki.Request;
import org.joseki.Response;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.sparql.core.DatasetImpl;

/**
 * @author dorgon, Andreas Langegger, andreas@langegger.at
 *
 */
public class XLWrapDatasetDesc extends DatasetDesc {

    private final XLWrapServer server;

    /**
	 * constructor, ignore resource for super class and acquire data set vie XLWrapServer
	 */
    public XLWrapDatasetDesc(XLWrapServer server) {
        super(null);
        this.server = server;
    }

    @Override
    public Dataset acquireDataset(Request request, Response response) {
        return new DatasetImpl(server.getModel());
    }
}
