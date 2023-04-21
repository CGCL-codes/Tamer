package org.imsglobal.xsd.imsqti_v2p0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for durationGTE.Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="durationGTE.Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.imsglobal.org/xsd/imsqti_v2p0}durationGTE.ContentGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "durationGTE.Type", propOrder = { "expressionElementGroup" })
public class DurationGTEType {

    @XmlElements({ @XmlElement(name = "isNull", type = IsNullType.class), @XmlElement(name = "inside", type = InsideType.class), @XmlElement(name = "variable", type = VariableType.class), @XmlElement(name = "product", type = ProductType.class), @XmlElement(name = "null", type = NullType.class), @XmlElement(name = "integerToFloat", type = IntegerToFloatType.class), @XmlElement(name = "gte", type = GteType.class), @XmlElement(name = "power", type = PowerType.class), @XmlElement(name = "truncate", type = TruncateType.class), @XmlElement(name = "randomFloat", type = RandomFloatType.class), @XmlElement(name = "baseValue", type = BaseValueType.class), @XmlElement(name = "integerModulus", type = IntegerModulusType.class), @XmlElement(name = "sum", type = SumType.class), @XmlElement(name = "round", type = RoundType.class), @XmlElement(name = "delete", type = DeleteType.class), @XmlElement(name = "randomInteger", type = RandomIntegerType.class), @XmlElement(name = "durationGTE", type = DurationGTEType.class), @XmlElement(name = "default", type = DefaultType.class), @XmlElement(name = "equalRounded", type = EqualRoundedType.class), @XmlElement(name = "correct", type = CorrectType.class), @XmlElement(name = "match", type = MatchType.class), @XmlElement(name = "subtract", type = SubtractType.class), @XmlElement(name = "fieldValue", type = FieldValueType.class), @XmlElement(name = "contains", type = ContainsType.class), @XmlElement(name = "mapResponsePoint", type = MapResponsePointType.class), @XmlElement(name = "index", type = IndexType.class), @XmlElement(name = "stringMatch", type = StringMatchType.class), @XmlElement(name = "equal", type = EqualType.class), @XmlElement(name = "random", type = RandomType.class), @XmlElement(name = "substring", type = SubstringType.class), @XmlElement(name = "not", type = NotType.class), @XmlElement(name = "multiple", type = MultipleType.class), @XmlElement(name = "mapResponse", type = MapResponseType.class), @XmlElement(name = "gt", type = GtType.class), @XmlElement(name = "customOperator", type = CustomOperatorType.class), @XmlElement(name = "member", type = MemberType.class), @XmlElement(name = "divide", type = DivideType.class), @XmlElement(name = "lte", type = LteType.class), @XmlElement(name = "anyN", type = AnyNType.class), @XmlElement(name = "ordered", type = OrderedType.class), @XmlElement(name = "or", type = OrType.class), @XmlElement(name = "integerDivide", type = IntegerDivideType.class), @XmlElement(name = "durationLT", type = DurationLTType.class), @XmlElement(name = "patternMatch", type = PatternMatchType.class), @XmlElement(name = "lt", type = LtType.class), @XmlElement(name = "and", type = AndType.class) })
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
     * {@link IsNullType }
     * {@link InsideType }
     * {@link VariableType }
     * {@link ProductType }
     * {@link NullType }
     * {@link IntegerToFloatType }
     * {@link GteType }
     * {@link PowerType }
     * {@link TruncateType }
     * {@link RandomFloatType }
     * {@link BaseValueType }
     * {@link IntegerModulusType }
     * {@link SumType }
     * {@link RoundType }
     * {@link DeleteType }
     * {@link RandomIntegerType }
     * {@link DurationGTEType }
     * {@link DefaultType }
     * {@link EqualRoundedType }
     * {@link CorrectType }
     * {@link MatchType }
     * {@link SubtractType }
     * {@link FieldValueType }
     * {@link ContainsType }
     * {@link MapResponsePointType }
     * {@link IndexType }
     * {@link StringMatchType }
     * {@link EqualType }
     * {@link RandomType }
     * {@link SubstringType }
     * {@link NotType }
     * {@link MultipleType }
     * {@link MapResponseType }
     * {@link GtType }
     * {@link CustomOperatorType }
     * {@link MemberType }
     * {@link DivideType }
     * {@link LteType }
     * {@link AnyNType }
     * {@link OrderedType }
     * {@link OrType }
     * {@link IntegerDivideType }
     * {@link DurationLTType }
     * {@link PatternMatchType }
     * {@link LtType }
     * {@link AndType }
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
