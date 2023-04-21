package tr.com.srdc.isurf.gs1.ucc.ean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for EventTacticTypeCodeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventTacticTypeCodeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="consumerIncentiveTacticTypeCode" type="{urn:ean.ucc:plan:2}ConsumerIncentiveTacticTypeCodeListType"/>
 *         &lt;element name="displayTacticTypeCode" type="{urn:ean.ucc:plan:2}DisplayTacticTypeCodeListType"/>
 *         &lt;element name="featureTacticTypeCode" type="{urn:ean.ucc:plan:2}FeatureTacticTypeCodeListType"/>
 *         &lt;element name="tradeItemPackingLabelingTypeCode" type="{urn:ean.ucc:plan:2}TradeItemPackingLabelingTypeCodeListType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventTacticTypeCodeType", namespace = "", propOrder = { "consumerIncentiveTacticTypeCode", "displayTacticTypeCode", "featureTacticTypeCode", "tradeItemPackingLabelingTypeCode" })
public class EventTacticTypeCodeType {

    protected ConsumerIncentiveTacticTypeCodeListType consumerIncentiveTacticTypeCode;

    protected DisplayTacticTypeCodeListType displayTacticTypeCode;

    protected FeatureTacticTypeCodeListType featureTacticTypeCode;

    protected TradeItemPackingLabelingTypeCodeListType tradeItemPackingLabelingTypeCode;

    /**
     * Gets the value of the consumerIncentiveTacticTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link ConsumerIncentiveTacticTypeCodeListType }
     *     
     */
    public ConsumerIncentiveTacticTypeCodeListType getConsumerIncentiveTacticTypeCode() {
        return consumerIncentiveTacticTypeCode;
    }

    /**
     * Sets the value of the consumerIncentiveTacticTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsumerIncentiveTacticTypeCodeListType }
     *     
     */
    public void setConsumerIncentiveTacticTypeCode(ConsumerIncentiveTacticTypeCodeListType value) {
        this.consumerIncentiveTacticTypeCode = value;
    }

    /**
     * Gets the value of the displayTacticTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayTacticTypeCodeListType }
     *     
     */
    public DisplayTacticTypeCodeListType getDisplayTacticTypeCode() {
        return displayTacticTypeCode;
    }

    /**
     * Sets the value of the displayTacticTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayTacticTypeCodeListType }
     *     
     */
    public void setDisplayTacticTypeCode(DisplayTacticTypeCodeListType value) {
        this.displayTacticTypeCode = value;
    }

    /**
     * Gets the value of the featureTacticTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link FeatureTacticTypeCodeListType }
     *     
     */
    public FeatureTacticTypeCodeListType getFeatureTacticTypeCode() {
        return featureTacticTypeCode;
    }

    /**
     * Sets the value of the featureTacticTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link FeatureTacticTypeCodeListType }
     *     
     */
    public void setFeatureTacticTypeCode(FeatureTacticTypeCodeListType value) {
        this.featureTacticTypeCode = value;
    }

    /**
     * Gets the value of the tradeItemPackingLabelingTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link TradeItemPackingLabelingTypeCodeListType }
     *     
     */
    public TradeItemPackingLabelingTypeCodeListType getTradeItemPackingLabelingTypeCode() {
        return tradeItemPackingLabelingTypeCode;
    }

    /**
     * Sets the value of the tradeItemPackingLabelingTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link TradeItemPackingLabelingTypeCodeListType }
     *     
     */
    public void setTradeItemPackingLabelingTypeCode(TradeItemPackingLabelingTypeCodeListType value) {
        this.tradeItemPackingLabelingTypeCode = value;
    }
}
