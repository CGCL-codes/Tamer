package uk.ac.ebi.intact.confidence.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * BinaryInteraction with Attributes.
 *
 * @author Irina Armean (iarmean@ebi.ac.uk)
 * @version $Id$
 * @since 1.6.0
 *        <pre>
 *        07-Dec-2007
 *        </pre>
 */
public class BinaryInteractionAttributes extends BinaryInteraction {

    private List<Attribute> attributes;

    public BinaryInteractionAttributes(Identifier firstIdentifier, Identifier secondIdentifier, Confidence confidence) {
        super(firstIdentifier, secondIdentifier, confidence);
        attributes = new ArrayList<Attribute>();
    }

    public BinaryInteractionAttributes(Identifier firstIdentifier, Identifier secondIdentifier, List<Attribute> attribute, Confidence confidence) {
        this(firstIdentifier, secondIdentifier, confidence);
        if (attribute != null) {
            this.attributes = attribute;
        }
    }

    public Identifier getFirstId() {
        return super.getFirstId();
    }

    public Identifier getSecondId() {
        return super.getSecondId();
    }

    public Confidence getConfidence() {
        return super.getConfidence();
    }

    public void setConfidence(Confidence conf) {
        super.setConfidence(conf);
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Attribute> getGoAttributes() {
        List<Attribute> gos = new ArrayList<Attribute>();
        for (Iterator<Attribute> iterator = attributes.iterator(); iterator.hasNext(); ) {
            Attribute attribute = iterator.next();
            if (attribute.getFirstElement().getClass().equals(GoIdentifierImpl.class)) {
                gos.add(attribute);
            }
        }
        return gos;
    }

    public List<Attribute> getIpAttributes() {
        List<Attribute> ips = new ArrayList<Attribute>();
        for (Iterator<Attribute> iterator = attributes.iterator(); iterator.hasNext(); ) {
            Attribute attribute = iterator.next();
            if (attribute.getFirstElement().getClass().equals(InterProIdentifierImpl.class)) {
                ips.add(attribute);
            }
        }
        return ips;
    }

    public List<Attribute> getSeqAttributes() {
        List<Attribute> seqs = new ArrayList<Attribute>();
        for (Iterator<Attribute> iterator = attributes.iterator(); iterator.hasNext(); ) {
            Attribute attribute = iterator.next();
            if (attribute.getFirstElement().getClass().equals(UniprotIdentifierImpl.class)) {
                seqs.add(attribute);
            }
        }
        return seqs;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttributes(List<Attribute> attributes) {
        for (int i = 0; i < attributes.size(); i++) {
            addAttribute(attributes.get(i));
        }
    }

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public void removeAttribute(Attribute attribute) {
        attributes.remove(attribute);
    }

    public String convertToString() {
        String result = super.convertToString();
        for (Iterator<Attribute> attributeIterator = attributes.iterator(); attributeIterator.hasNext(); ) {
            Attribute attribute = attributeIterator.next();
            result += "," + attribute.convertToString();
        }
        return result;
    }

    public String convertToModleInputString() {
        String result = "";
        for (Iterator<Attribute> attributeIterator = attributes.iterator(); attributeIterator.hasNext(); ) {
            Attribute attribute = attributeIterator.next();
            result += attribute.convertToString() + " ";
        }
        result += this.getConfidence();
        return result;
    }
}
