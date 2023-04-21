package org.mcisb.google.dataviewer;

import java.util.*;
import com.google.wave.api.*;
import com.google.wave.api.event.*;
import org.mcisb.util.*;

/**
 * @author Neil Swainston
 */
public class WaveomicsRobot extends AbstractRobot {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    private static final String APPLICATION = "waveomics";

    /**
	 * 
	 */
    private static final String DOMAIN = "http://" + APPLICATION + ".appspot.com/";

    /**
	 * 
	 */
    private static final String GADGET_URL = DOMAIN + "data.xml";

    /**
	 * 
	 */
    private static final String UNIPROT_REGULAR_EXPRESSION = "(?=.*)([A-Z0-9]{4,6}\\_[A-Z]{5})(?=.*)";

    /**
	 * 
	 */
    private static final String EC_TERM_REGULAR_EXPRESSION = "(?=.*)(\\d+\\.-\\.-\\.-|\\d+\\.\\d+\\.-\\.-|\\d+\\.\\d+\\.\\d+\\.-|\\d+\\.\\d+\\.\\d+\\.\\d+)(?=.*)";

    @Override
    public void onBlipSubmitted(final BlipSubmittedEvent event) {
        super.onBlipSubmitted(event);
        final Blip blip = event.getBlip();
        deleteElements(blip);
        for (final Element element : getElements(blip)) {
            blip.append(element);
        }
    }

    /**
	 * 
	 * @param blip 
	 * @param elementMap 
	 */
    private void deleteElements(final Blip blip) {
        final Map<Integer, Element> elements = blip.getElements();
        final List<Integer> keys = new ArrayList<Integer>(elements.keySet());
        Collections.reverse(keys);
        final List<Integer> elementIndexesToDelete = new ArrayList<Integer>();
        for (final Integer key : keys) {
            final Element element = elements.get(key);
            if (element.isGadget() && ((Gadget) element).getUrl().startsWith(GADGET_URL)) {
                elementIndexesToDelete.add(key);
            }
        }
        for (final Integer index : elementIndexesToDelete) {
            final BlipContentRefs ref = blip.at(index.intValue());
            ref.delete();
        }
    }

    /**
	 * 
	 * @param blip 
	 * @return Collection<Element>
	 */
    private Collection<Element> getElements(final Blip blip) {
        final String KINETICS_SERVICE = "kinetics";
        final String PROTEOMICS_SERVICE = "proteomics";
        final Collection<Element> elements = new ArrayList<Element>();
        final Collection<String> ecTermMatches = new LinkedHashSet<String>(RegularExpressionUtils.getMatches(blip.getContent(), EC_TERM_REGULAR_EXPRESSION));
        final Collection<String> uniProtIdMatches = new LinkedHashSet<String>(RegularExpressionUtils.getMatches(blip.getContent(), UNIPROT_REGULAR_EXPRESSION));
        for (String ecTerm : ecTermMatches) {
            elements.add(getGadget(KINETICS_SERVICE, ecTerm));
        }
        for (String uniProtId : uniProtIdMatches) {
            elements.add(getGadget(PROTEOMICS_SERVICE, uniProtId));
        }
        return elements;
    }

    @Override
    public String getRobotProfilePageUrl() {
        return DOMAIN;
    }

    @Override
    public String getRobotAvatarUrl() {
        return DOMAIN + "avatar.png";
    }

    @Override
    protected String getRobotName() {
        return "waveomics";
    }

    /**
	 * 
	 * @param serviceName
	 * @param id
	 * @return Gadget
	 */
    private Gadget getGadget(final String serviceName, final String id) {
        return new Gadget(GADGET_URL + "?service=" + serviceName + "&id=" + id);
    }
}
