package org.imsglobal.xsd.imsqti_v2p0;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anyN.Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="anyN.Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.imsglobal.org/xsd/imsqti_v2p0}anyN.ContentGroup"/>
 *       &lt;attGroup ref="{http://www.imsglobal.org/xsd/imsqti_v2p0}anyN.AttrGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "anyN.Type", propOrder = { "expressionElementGroup" })
public class AnyNType {

    @XmlElements({ @XmlElement(name = "inside", type = InsideType.class), @XmlElement(name = "correct", type = CorrectType.class), @XmlElement(name = "member", type = MemberType.class), @XmlElement(name = "power", type = PowerType.class), @XmlElement(name = "round", type = RoundType.class), @XmlElement(name = "durationLT", type = DurationLTType.class), @XmlElement(name = "substring", type = SubstringType.class), @XmlElement(name = "subtract", type = SubtractType.class), @XmlElement(name = "divide", type = DivideType.class), @XmlElement(name = "truncate", type = TruncateType.class), @XmlElement(name = "product", type = ProductType.class), @XmlElement(name = "gt", type = GtType.class), @XmlElement(name = "integerToFloat", type = IntegerToFloatType.class), @XmlElement(name = "and", type = AndType.class), @XmlElement(name = "isNull", type = IsNullType.class), @XmlElement(name = "variable", type = VariableType.class), @XmlElement(name = "multiple", type = MultipleType.class), @XmlElement(name = "lt", type = LtType.class), @XmlElement(name = "mapResponse", type = MapResponseType.class), @XmlElement(name = "stringMatch", type = StringMatchType.class), @XmlElement(name = "contains", type = ContainsType.class), @XmlElement(name = "sum", type = SumType.class), @XmlElement(name = "randomFloat", type = RandomFloatType.class), @XmlElement(name = "index", type = IndexType.class), @XmlElement(name = "randomInteger", type = RandomIntegerType.class), @XmlElement(name = "lte", type = LteType.class), @XmlElement(name = "baseValue", type = BaseValueType.class), @XmlElement(name = "ordered", type = OrderedType.class), @XmlElement(name = "default", type = DefaultType.class), @XmlElement(name = "patternMatch", type = PatternMatchType.class), @XmlElement(name = "match", type = MatchType.class), @XmlElement(name = "null", type = NullType.class), @XmlElement(name = "gte", type = GteType.class), @XmlElement(name = "or", type = OrType.class), @XmlElement(name = "not", type = NotType.class), @XmlElement(name = "durationGTE", type = DurationGTEType.class), @XmlElement(name = "random", type = RandomType.class), @XmlElement(name = "mapResponsePoint", type = MapResponsePointType.class), @XmlElement(name = "delete", type = DeleteType.class), @XmlElement(name = "fieldValue", type = FieldValueType.class), @XmlElement(name = "customOperator", type = CustomOperatorType.class), @XmlElement(name = "anyN", type = AnyNType.class), @XmlElement(name = "equalRounded", type = EqualRoundedType.class), @XmlElement(name = "integerDivide", type = IntegerDivideType.class), @XmlElement(name = "equal", type = EqualType.class), @XmlElement(name = "integerModulus", type = IntegerModulusType.class) })
    protected List<Object> expressionElementGroup;

    @XmlAttribute(required = true)
    protected BigInteger min;

    @XmlAttribute(required = true)
    protected BigInteger max;

    /**
     * Gets the value of the expressionElementGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the expressionElementGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExpressionElementGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InsideType }
     * {@link CorrectType }
     * {@link MemberType }
     * {@link PowerType }
     * {@link RoundType }
     * {@link DurationLTType }
     * {@link SubstringType }
     * {@link SubtractType }
     * {@link DivideType }
     * {@link TruncateType }
     * {@link ProductType }
     * {@link GtType }
     * {@link IntegerToFloatType }
     * {@link AndType }
     * {@link IsNullType }
     * {@link VariableType }
     * {@link MultipleType }
     * {@link LtType }
     * {@link MapResponseType }
     * {@link StringMatchType }
     * {@link ContainsType }
     * {@link SumType }
     * {@link RandomFloatType }
     * {@link IndexType }
     * {@link RandomIntegerType }
     * {@link LteType }
     * {@link BaseValueType }
     * {@link OrderedType }
     * {@link DefaultType }
     * {@link PatternMatchType }
     * {@link MatchType }
     * {@link NullType }
     * {@link GteType }
     * {@link OrType }
     * {@link NotType }
     * {@link DurationGTEType }
     * {@link RandomType }
     * {@link MapResponsePointType }
     * {@link DeleteType }
     * {@link FieldValueType }
     * {@link CustomOperatorType }
     * {@link AnyNType }
     * {@link EqualRoundedType }
     * {@link IntegerDivideType }
     * {@link EqualType }
     * {@link IntegerModulusType }
     * 
     * 
     */
    public List<Object> getExpressionElementGroup() {
        if (expressionElementGroup == null) {
            expressionElementGroup = new ArrayList<Object>();
        }
        return this.expressionElementGroup;
    }

    /**
     * Gets the value of the min property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMin() {
        return min;
    }

    /**
     * Sets the value of the min property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMin(BigInteger value) {
        this.min = value;
    }

    /**
     * Gets the value of the max property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMax() {
        return max;
    }

    /**
     * Sets the value of the max property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMax(BigInteger value) {
        this.max = value;
    }
}
