package de.unitrier.dblp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{}article" maxOccurs="unbounded"/>
 *         &lt;element ref="{}inproceedings" maxOccurs="unbounded"/>
 *         &lt;element ref="{}proceedings" maxOccurs="unbounded"/>
 *         &lt;element ref="{}book" maxOccurs="unbounded"/>
 *         &lt;element ref="{}incollection" maxOccurs="unbounded"/>
 *         &lt;element ref="{}phdthesis" maxOccurs="unbounded"/>
 *         &lt;element ref="{}mastersthesis" maxOccurs="unbounded"/>
 *         &lt;element ref="{}www" maxOccurs="unbounded"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "article", "inproceedings", "proceedings", "book", "incollection", "phdthesis", "mastersthesis", "www" })
@XmlRootElement(name = "dblp")
public class Dblp {

    protected List<Article> article;

    protected List<Inproceedings> inproceedings;

    protected List<Proceedings> proceedings;

    protected List<Book> book;

    protected List<Incollection> incollection;

    protected List<Phdthesis> phdthesis;

    protected List<Mastersthesis> mastersthesis;

    protected List<Www> www;

    /**
     * Gets the value of the article property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the article property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArticle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Article }
     * 
     * 
     */
    public List<Article> getArticle() {
        if (article == null) {
            article = new ArrayList<Article>();
        }
        return this.article;
    }

    /**
     * Gets the value of the inproceedings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inproceedings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInproceedings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Inproceedings }
     * 
     * 
     */
    public List<Inproceedings> getInproceedings() {
        if (inproceedings == null) {
            inproceedings = new ArrayList<Inproceedings>();
        }
        return this.inproceedings;
    }

    /**
     * Gets the value of the proceedings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the proceedings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProceedings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Proceedings }
     * 
     * 
     */
    public List<Proceedings> getProceedings() {
        if (proceedings == null) {
            proceedings = new ArrayList<Proceedings>();
        }
        return this.proceedings;
    }

    /**
     * Gets the value of the book property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the book property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBook().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Book }
     * 
     * 
     */
    public List<Book> getBook() {
        if (book == null) {
            book = new ArrayList<Book>();
        }
        return this.book;
    }

    /**
     * Gets the value of the incollection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the incollection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncollection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Incollection }
     * 
     * 
     */
    public List<Incollection> getIncollection() {
        if (incollection == null) {
            incollection = new ArrayList<Incollection>();
        }
        return this.incollection;
    }

    /**
     * Gets the value of the phdthesis property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the phdthesis property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPhdthesis().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Phdthesis }
     * 
     * 
     */
    public List<Phdthesis> getPhdthesis() {
        if (phdthesis == null) {
            phdthesis = new ArrayList<Phdthesis>();
        }
        return this.phdthesis;
    }

    /**
     * Gets the value of the mastersthesis property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mastersthesis property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMastersthesis().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Mastersthesis }
     * 
     * 
     */
    public List<Mastersthesis> getMastersthesis() {
        if (mastersthesis == null) {
            mastersthesis = new ArrayList<Mastersthesis>();
        }
        return this.mastersthesis;
    }

    /**
     * Gets the value of the www property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the www property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWww().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Www }
     * 
     * 
     */
    public List<Www> getWww() {
        if (www == null) {
            www = new ArrayList<Www>();
        }
        return this.www;
    }
}
