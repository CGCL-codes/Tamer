package com.ehx.classifiers.model;

import java.io.StringWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;

/**
 * <p>Java class for classifiers complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="classifiers"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="classifier" type="{}classifier" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * @version $Rev: 75 $
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "classifiers", propOrder = { "classifier" })
public class Classifiers extends Util<Classifiers> implements Comparable<Classifiers> {

    private static final Logger logger = Logger.getLogger(Classifiers.class);

    protected List<Classifier> classifier;

    @Override
    public int compareTo(Classifiers c) {
        if (this.classifier.size() < c.getClassifier().size()) {
            logger.trace("this.classifier.size() < c.getClassifier().size(). Result is -1.");
            return -1;
        }
        if (this.classifier.size() > c.getClassifier().size()) {
            logger.trace("this.classifier.size() > c.getClassifier().size(). Result is 1.");
            return 1;
        }
        Collections.sort(this.classifier);
        Collections.sort(c.getClassifier());
        Iterator itr1 = this.classifier.iterator();
        Iterator itr2 = c.getClassifier().iterator();
        while (itr1.hasNext() && itr2.hasNext()) {
            Classifier c1 = (Classifier) itr1.next();
            Classifier c2 = (Classifier) itr2.next();
            int cComparison = c1.compareTo(c2);
            if (cComparison != 0) {
                return cComparison;
            }
        }
        assert this.equals(c) : "compareTo inconsistent with equals";
        logger.trace("The result is 0");
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Classifiers)) return false;
        Classifiers that = (Classifiers) o;
        Collections.sort(this.getClassifier());
        Collections.sort(that.getClassifier());
        Iterator itr1 = this.getClassifier().iterator();
        Iterator itr2 = that.getClassifier().iterator();
        while (itr1.hasNext() && itr2.hasNext()) {
            Classifier c1 = (Classifier) itr1.next();
            Classifier c2 = (Classifier) itr2.next();
            boolean cComparison = c1.equals(c2);
            if (!cComparison) return cComparison;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 39;
        if (this.classifier != null && this.classifier.size() > 0) {
            result *= this.classifier.size();
        }
        return result;
    }

    /**
     * Gets the value of the classifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Classifier }
     * 
     * 
     */
    public List<Classifier> getClassifier() {
        if (classifier == null) {
            classifier = new ArrayList<Classifier>();
        }
        return this.classifier;
    }
}
