package net.fortytwo.restpipe;

import org.restlet.resource.Variant;
import org.restlet.resource.Representation;
import java.util.List;
import java.util.LinkedList;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.rdf.RDFUtils;

/**
 * Author: josh
 * Date: Feb 27, 2008
 * Time: 2:14:21 PM
 */
public class DebugSink implements RepresentationSink<RippleException> {

    public String getComment() {
        return "just a debugging tool";
    }

    public List<Variant> getVariants() {
        try {
            return RDFUtils.getRdfVariants();
        } catch (RippleException e) {
            e.logError();
            return new LinkedList<Variant>();
        }
    }

    public void put(final Representation rep) throws RippleException {
        System.out.println("received representation:\n" + rep);
    }
}
