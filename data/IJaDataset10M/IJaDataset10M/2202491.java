package tei.cr.component.ag;

import agtk.ag.AGException;
import agtk.ag.AGSet;
import agtk.ag.Anchor;
import agtk.ag.Identifiers;
import java.util.Iterator;
import tei.cr.merger.rule.Rule;
import tei.cr.utils.stax.StAXFragment;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import java.util.Set;
import java.util.Arrays;

class AG {

    private final AGSet set;

    private final String agset_id = "CR_agset";

    private final String timeline_id;

    private final String ag_id;

    private AG() throws AGException {
        set = new AGSet(agset_id);
        timeline_id = set.createTimeline(agset_id);
        ag_id = set.createAG(agset_id, timeline_id, "foo");
    }

    public AG(StAXFragment fragment) throws AGException {
        this();
        buildAG(fragment);
    }

    private void buildAG(StAXFragment fragment) throws AGException {
        StringBuffer text = new StringBuffer();
        for (int i = 0; i < fragment.size(); i++) {
            XMLEvent e = fragment.getXMLEvent(i);
            if (e.isStartElement()) {
                int startOffset = fragment.getCoordinate(i);
                int endOffset = fragment.getCoordinate(fragment.getCoordinate(i));
                addElement(e.asStartElement(), startOffset, endOffset);
            } else if (e.isCharacters()) {
                text.append(e.asCharacters().getData());
            }
        }
    }

    private void addElement(StartElement e, int startOffset, int endOffset) throws AGException {
        String anchor1_id = set.createAnchor(ag_id, startOffset, "characters", "");
        String anchor2_id = set.createAnchor(ag_id, endOffset, "characters", "");
        String annotation_id = set.createAnnotation(ag_id, anchor1_id, anchor2_id, "element");
        set.setFeature(annotation_id, "__name__", e.getName().getLocalPart());
        addAttributeOnAnnotation(e.getAttributes(), annotation_id);
    }

    private void addAttributeOnAnnotation(Iterator attributes, String annotation_id) throws AGException {
        while (attributes.hasNext()) {
            Attribute a = (Attribute) attributes.next();
            set.setFeature(annotation_id, a.getName().getLocalPart(), a.getValue());
        }
    }

    public StAXFragment ToXML(Rule[] rules) throws AGException {
        Set<String> anchors = (Set<String>) set.getAnchorSet(ag_id);
        Anchor[] orderedAnchor = orderAnchors(anchors);
        int[] anchorGroups = groupAnchorByOffset(orderedAnchor);
        int group = 0;
        for (int i = 0; i < anchorGroups.length; i++) {
            if (anchorGroups[i] == group) {
            } else {
                group = anchorGroups[i];
            }
        }
        return null;
    }

    private void doGroup() {
    }

    public int[] groupAnchorByOffset(Anchor[] orderedAnchor) throws AGException {
        int[] anchorByGroup = new int[orderedAnchor.length];
        int grpNumber = 0;
        int offset = -1;
        for (int i = 0; i < orderedAnchor.length; i++) {
            if (orderedAnchor[i].getOffset() != offset) {
                grpNumber++;
                anchorByGroup[i] = grpNumber;
                offset = (int) orderedAnchor[i].getOffset();
            } else {
                anchorByGroup[i] = grpNumber;
            }
        }
        return anchorByGroup;
    }

    private Anchor[] orderAnchors(Set<String> anchors_id) throws AGException {
        int index = 0;
        Anchor[] anchors = new Anchor[anchors_id.size()];
        for (Iterator<String> iter = anchors_id.iterator(); iter.hasNext(); ) {
            anchors[index] = Identifiers.getAnchorRef(iter.next());
            index++;
        }
        Arrays.sort(anchors);
        return anchors;
    }
}
