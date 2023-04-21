package org.imsglobal.xsd.imsqti_v2p0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for and.Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="and.Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.imsglobal.org/xsd/imsqti_v2p0}and.ContentGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "and.Type", propOrder = { "expressionElementGroup" })
public class AndType {

    @XmlElements({ @XmlElement(name = "gte", type = GteType.class), @XmlElement(name = "product", type = ProductType.class), @XmlElement(name = "lte", type = LteType.class), @XmlElement(name = "ordered", type = OrderedType.class), @XmlElement(name = "member", type = MemberType.class), @XmlElement(name = "power", type = PowerType.class), @XmlElement(name = "equal", type = EqualType.class), @XmlElement(name = "anyN", type = AnyNType.class), @XmlElement(name = "match", type = MatchType.class), @XmlElement(name = "patternMatch", type = PatternMatchType.class), @XmlElement(name = "isNull", type = IsNullType.class), @XmlElement(name = "randomInteger", type = RandomIntegerType.class), @XmlElement(name = "baseValue", type = BaseValueType.class), @XmlElement(name = "divide", type = DivideType.class), @XmlElement(name = "integerDivide", type = IntegerDivideType.class), @XmlElement(name = "customOperator", type = CustomOperatorType.class), @XmlElement(name = "index", type = IndexType.class), @XmlElement(name = "mapResponse", type = MapResponseType.class), @XmlElement(name = "and", type = AndType.class), @XmlElement(name = "random", type = RandomType.class), @XmlElement(name = "randomFloat", type = RandomFloatType.class), @XmlElement(name = "equalRounded", type = EqualRoundedType.class), @XmlElement(name = "truncate", type = TruncateType.class), @XmlElement(name = "not", type = NotType.class), @XmlElement(name = "multiple", type = MultipleType.class), @XmlElement(name = "subtract", type = SubtractType.class), @XmlElement(name = "null", type = NullType.class), @XmlElement(name = "substring", type = SubstringType.class), @XmlElement(name = "round", type = RoundType.class), @XmlElement(name = "gt", type = GtType.class), @XmlElement(name = "contains", type = ContainsType.class), @XmlElement(name = "integerToFloat", type = IntegerToFloatType.class), @XmlElement(name = "variable", type = VariableType.class), @XmlElement(name = "sum", type = SumType.class), @XmlElement(name = "lt", type = LtType.class), @XmlElement(name = "or", type = OrType.class), @XmlElement(name = "default", type = DefaultType.class), @XmlElement(name = "durationLT", type = DurationLTType.class), @XmlElement(name = "fieldValue", type = FieldValueType.class), @XmlElement(name = "delete", type = DeleteType.class), @XmlElement(name = "inside", type = InsideType.class), @XmlElement(name = "durationGTE", type = DurationGTEType.class), @XmlElement(name = "integerModulus", type = IntegerModulusType.class), @XmlElement(name = "stringMatch", type = StringMatchType.class), @XmlElement(name = "correct", type = CorrectType.class), @XmlElement(name = "mapResponsePoint", type = MapResponsePointType.class) })
    protected List<Object> expressionElementGroup;

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
     * {@link GteType }
     * {@link ProductType }
     * {@link LteType }
     * {@link OrderedType }
     * {@link MemberType }
     * {@link PowerType }
     * {@link EqualType }
     * {@link AnyNType }
     * {@link MatchType }
     * {@link PatternMatchType }
     * {@link IsNullType }
     * {@link RandomIntegerType }
     * {@link BaseValueType }
     * {@link DivideType }
     * {@link IntegerDivideType }
     * {@link CustomOperatorType }
     * {@link IndexType }
     * {@link MapResponseType }
     * {@link AndType }
     * {@link RandomType }
     * {@link RandomFloatType }
     * {@link EqualRoundedType }
     * {@link TruncateType }
     * {@link NotType }
     * {@link MultipleType }
     * {@link SubtractType }
     * {@link NullType }
     * {@link SubstringType }
     * {@link RoundType }
     * {@link GtType }
     * {@link ContainsType }
     * {@link IntegerToFloatType }
     * {@link VariableType }
     * {@link SumType }
     * {@link LtType }
     * {@link OrType }
     * {@link DefaultType }
     * {@link DurationLTType }
     * {@link FieldValueType }
     * {@link DeleteType }
     * {@link InsideType }
     * {@link DurationGTEType }
     * {@link IntegerModulusType }
     * {@link StringMatchType }
     * {@link CorrectType }
     * {@link MapResponsePointType }
     * 
     * 
     */
    public List<Object> getExpressionElementGroup() {
        if (expressionElementGroup == null) {
            expressionElementGroup = new ArrayList<Object>();
        }
        return this.expressionElementGroup;
    }
}
