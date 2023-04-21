package net.sf.istcontract.aws.analyser.ontology;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author hodik
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonitorContractDetails", propOrder = { "id", "clauses" })
public class MonitorContractDetails {

    @XmlElement(required = true)
    protected String id;

    @XmlElement(name = "ActionReport")
    protected List<String> clauses;

    /**
     * Gets the value of the command property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the command property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    public List<String> getClauses() {
        if (clauses == null) {
            clauses = new ArrayList<String>();
        }
        return this.clauses;
    }
}
