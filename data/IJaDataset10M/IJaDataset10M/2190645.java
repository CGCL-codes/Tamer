package net.sf.oai4j.verbs;

import java.util.Calendar;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.oai4j.OAIConfiguration;
import net.sf.oai4j.Renderable;
import net.sf.oai4j.RenderableList;
import net.sf.oai4j.RequestHandler;
import net.sf.oai4j.ResumptionCache;
import net.sf.oai4j.ResumptionToken;
import net.sf.oai4j.exceptions.BadArgumentException;
import net.sf.oai4j.exceptions.BadResumptionTokenException;
import net.sf.oai4j.exceptions.RenderableException;

public class ListRecords extends Verb {

    private Calendar from;

    private Calendar until;

    private String metadataPrefix;

    private String set;

    private ResumptionToken resumptionToken;

    public ListRecords(HttpServletRequest req, RequestHandler repository, OAIConfiguration config, ResumptionCache resumptionCache) throws RenderableException {
        super(req, repository, config, resumptionCache);
    }

    @Override
    public void checkParameters(HttpServletRequest req) throws BadArgumentException, BadResumptionTokenException {
        final Map params = req.getParameterMap();
        if (params.size() > 5) {
            throw new BadArgumentException("too much parameters");
        }
        boolean hasResToken = false;
        for (final Object o : params.keySet()) {
            final String arg = (String) o;
            if (!arg.equals("verb") && !arg.equals("metadataPrefix") && !arg.equals("from") && !arg.equals("until") && !arg.equals("set") && !arg.equals("resumptionToken")) {
                throw new BadArgumentException("unknown argument " + arg);
            }
            final Object vals[] = (Object[]) params.get(o);
            if (vals.length > 1) {
                throw new BadArgumentException("more then one (" + vals.length + ") occurrence of " + arg);
            }
            if (arg.equals("resumptionToken")) {
                hasResToken = true;
            }
        }
        if (!hasResToken) {
            metadataPrefix = getStringRequired("metadataPrefix");
            from = getCalendar("from");
            until = getCalendar("until");
            set = getString("set");
            if (from != null && until != null && from.after(until)) {
                throw new BadArgumentException("from cannot be after until");
            }
        } else if (hasResToken && params.size() > 2) {
            throw new BadArgumentException("resumptionToken must be exclusive");
        }
        resumptionToken = getResumptionToken();
    }

    @Override
    public String renderResponse() throws RenderableException {
        RenderableList<? extends Renderable> renderables = null;
        if (resumptionToken != null) {
            renderables = reqHandler.listRecords(resumptionToken);
        } else {
            renderables = reqHandler.listRecords(from, until, metadataPrefix, set);
        }
        resumptionCache.purge(resumptionToken);
        resumptionToken = renderables.getToken();
        if (resumptionToken != null) {
            resumptionCache.putToken(resumptionToken);
        }
        final StringBuilder b = new StringBuilder();
        b.append("<ListRecords>");
        b.append(renderables.render());
        b.append("</ListRecords>");
        return b.toString();
    }
}
