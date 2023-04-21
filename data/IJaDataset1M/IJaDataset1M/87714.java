package org.edits.etaf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import org.edits.annotation.SimpleTextAnnotator;

/**
 * <p>
 * Java class for AnnotatedText complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="AnnotatedText">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnnotatedText", propOrder = { "content" })
public class AnnotatedText {

    private static SimpleTextAnnotator annotator = new SimpleTextAnnotator();

    @XmlTransient
    private List<Annotation> annotations;

    @XmlValue
    protected String content;

    @XmlAttribute
    protected String id;

    public String annotatedText() {
        if (!isTextAnnotation()) return content;
        StringBuilder b = new StringBuilder();
        for (Annotation a : annotations()) {
            if (a.isLabel()) continue;
            b.append(a.form() + " ");
        }
        return b.toString().trim();
    }

    public List<Annotation> annotations() {
        if (annotations == null) {
            read();
        }
        return annotations;
    }

    public Annotation get(String id) {
        return get(id, annotations());
    }

    /**
	 * Gets the value of the content property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getContent() {
        return content;
    }

    /**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getId() {
        return id;
    }

    public boolean isTextAnnotation() {
        content = content.trim();
        if (!content.contains("\n")) return false;
        String text = content.substring(0, content.indexOf("\n"));
        return text.contains("ID") || text.contains("LEMMA") || text.contains("FORM") || text.contains("POSTAG") || text.contains("CPOSTAG");
    }

    public boolean isTree() {
        return isTree(annotations);
    }

    public void read() {
        annotations = new ArrayList<Annotation>();
        if (content == null) return;
        if (!isTextAnnotation()) {
            try {
                annotator.annotate(this);
            } catch (Exception e) {
            }
            return;
        }
        StringTokenizer toker = new StringTokenizer(content, "\n", false);
        String tt = toker.nextToken();
        StringTokenizer toker2 = new StringTokenizer(tt, "\t", false);
        List<String> ids = new ArrayList<String>();
        while (toker2.hasMoreTokens()) ids.add(toker2.nextToken());
        while (toker.hasMoreTokens()) {
            String token = toker.nextToken();
            if (token.length() == 0) continue;
            annotations.add(Annotation.readFromString(token, ids));
        }
    }

    public String root() {
        for (Annotation a : annotations()) if (a.isRoot()) return a.attribute(Annotation.ID);
        return null;
    }

    /**
	 * Sets the value of the content property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setContent(String value) {
        this.content = value;
    }

    /**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setId(String value) {
        this.id = value;
    }

    public String text() {
        if (!isTextAnnotation()) {
            return content;
        }
        return annotatedText();
    }

    @Override
    public String toString() {
        List<String> ids = new ArrayList<String>();
        for (Annotation a : annotations()) {
            for (String s : a.keySet()) if (!ids.contains(s)) ids.add(s);
        }
        Collections.sort(ids, new Annotation());
        if (ids.size() == 0) return null;
        StringBuilder b = new StringBuilder();
        b.append(ids.get(0));
        for (int i = 1; i < ids.size(); i++) b.append("\t" + ids.get(i));
        for (Annotation key : annotations) b.append("\n" + key.toString(ids));
        return b.toString();
    }

    public void write() {
        if (annotations != null && annotations.size() > 0) setContent(toString());
    }

    public static Annotation get(String id, List<Annotation> annotations) {
        for (Annotation a : annotations) {
            if (id.equals(a.get(Annotation.ID))) return a;
        }
        return null;
    }

    public static boolean isTree(List<Annotation> annotations) {
        for (Annotation a : annotations) if (a.attribute(Annotation.DEPREL) != null) return true;
        return false;
    }
}
