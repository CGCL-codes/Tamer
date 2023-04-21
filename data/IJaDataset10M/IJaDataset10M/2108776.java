package uk.ac.ebi.rhea.webapp.pub.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.rhea.domain.Direction;
import uk.ac.ebi.rhea.domain.ReactionParticipant;
import uk.ac.ebi.rhea.domain.Side;
import uk.ac.ebi.rhea.domain.XRef;
import uk.ac.ebi.rhea.find.ReactionFinder;
import uk.ac.ebi.rhea.webapp.pub.util.XRefsGroup.DirectionalXRef;
import uk.ac.ebi.rhea.webapp.pub.util.XRefsGroup.Group;

/**
 * Bean to manipulate cross references to a reaction.
 * @author rafalcan
 *
 */
public class XRefsBean {

    private static final Logger LOGGER = Logger.getLogger(XRefsBean.class);

    /**
	 * Organize xrefs in groups.
	 * @param finder
	 * @return
	 */
    public static Map<String, XRefsGroup> organizeXrefs(ReactionFinder finder) {
        Map<String, XRefsGroup> xrefsGroups = new HashMap<String, XRefsGroup>();
        List<DirectionalXRef> rpXrefs = null, enzXrefs = null;
        Map<Direction, Set<XRef>> familyXrefs = finder.getReaction().getFamilyXrefs();
        if (familyXrefs != null) {
            for (Entry<Direction, Set<XRef>> entry : familyXrefs.entrySet()) {
                Direction dir = entry.getKey();
                Set<XRef> xrefs = entry.getValue();
                for (XRef xref : xrefs) {
                    switch(xref.getDatabase()) {
                        case INTENZ:
                            if (enzXrefs == null) enzXrefs = new ArrayList<DirectionalXRef>();
                            enzXrefs.add(new DirectionalXRef(xref, dir));
                            break;
                        default:
                            if (rpXrefs == null) rpXrefs = new ArrayList<DirectionalXRef>();
                            rpXrefs.add(new DirectionalXRef(xref, dir));
                            break;
                    }
                }
            }
        }
        xrefsGroups.put(Group.RP.name(), new XRefsGroup(Group.RP, rpXrefs));
        xrefsGroups.put(Group.EN.name(), new XRefsGroup(Group.EN, enzXrefs));
        Collection<DirectionalXRef> participantsXrefs = new HashSet<DirectionalXRef>();
        for (Side side : Side.values()) {
            for (ReactionParticipant rp : finder.getReaction().getSide(side)) {
                XRef xref = rp.getCompound().getXref();
                if (xref != null) {
                    participantsXrefs.add(new DirectionalXRef(xref, finder.getReaction().getDirection()));
                }
            }
        }
        xrefsGroups.put(Group.SM.name(), new XRefsGroup(Group.SM, participantsXrefs));
        return xrefsGroups;
    }

    /**
	 * Cleans a MetaCyc URL from unwanted characters.
	 * @param metacycAccession
	 * @return
	 */
    public static String cleanMetaCycHref(String metacycUrl) {
        return metacycUrl.replaceAll("[,\\(\\)]", "");
    }

    /**
	 * Decodes characters from a MetaCyc accession.
	 * @param metacycAccession
	 * @return a string with URL-decoded characters.
	 */
    public static String decodeMetaCycLinkText(String metacycAccession) {
        try {
            return URLDecoder.decode(metacycAccession, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UTF-8", e);
            return metacycAccession;
        }
    }
}
