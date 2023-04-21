package org.imsglobal.xsd.imsqti_v2p0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for delete.Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="delete.Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.imsglobal.org/xsd/imsqti_v2p0}delete.ContentGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "delete.Type", propOrder = { "expressionElementGroup" })
public class DeleteType {

    @XmlElements({ @XmlElement(name = "randomFloat", type = RandomFloatType.class), @XmlElement(name = "gte", type = GteType.class), @XmlElement(name = "product", type = ProductType.class), @XmlElement(name = "index", type = IndexType.class), @XmlElement(name = "integerModulus", type = IntegerModulusType.class), @XmlElement(name = "gt", type = GtType.class), @XmlElement(name = "anyN", type = AnyNType.class), @XmlElement(name = "patternMatch", type = PatternMatchType.class), @XmlElement(name = "default", type = DefaultType.class), @XmlElement(name = "integerToFloat", type = IntegerToFloatType.class), @XmlElement(name = "equalRounded", type = EqualRoundedType.class), @XmlElement(name = "customOperator", type = CustomOperatorType.class), @XmlElement(name = "member", type = MemberType.class), @XmlElement(name = "or", type = OrType.class), @XmlElement(name = "fieldValue", type = FieldValueType.class), @XmlElement(name = "durationGTE", type = DurationGTEType.class), @XmlElement(name = "substring", type = SubstringType.class), @XmlElement(name = "inside", type = InsideType.class), @XmlElement(name = "and", type = AndType.class), @XmlElement(name = "correct", type = CorrectType.class), @XmlElement(name = "isNull", type = IsNullType.class), @XmlElement(name = "null", type = NullType.class), @XmlElement(name = "randomInteger", type = RandomIntegerType.class), @XmlElement(name = "mapResponse", type = MapResponseType.class), @XmlElement(name = "ordered", type = OrderedType.class), @XmlElement(name = "delete", type = DeleteType.class), @XmlElement(name = "lte", type = LteType.class), @XmlElement(name = "integerDivide", type = IntegerDivideType.class), @XmlElement(name = "lt", type = LtType.class), @XmlElement(name = "truncate", type = TruncateType.class), @XmlElement(name = "round", type = RoundType.class), @XmlElement(name = "power", type = PowerType.class), @XmlElement(name = "contains", type = ContainsType.class), @XmlElement(name = "match", type = MatchType.class), @XmlElement(name = "variable", type = VariableType.class), @XmlElement(name = "equal", type = EqualType.class), @XmlElement(name = "random", type = RandomType.class), @XmlElement(name = "sum", type = SumType.class), @XmlElement(name = "not", type = NotType.class), @XmlElement(name = "mapResponsePoint", type = MapResponsePointType.class), @XmlElement(name = "divide", type = DivideType.class), @XmlElement(name = "baseValue", type = BaseValueType.class), @XmlElement(name = "multiple", type = MultipleType.class), @XmlElement(name = "durationLT", type = DurationLTType.class), @XmlElement(name = "subtract", type = SubtractType.class), @XmlElement(name = "stringMatch", type = StringMatchType.class) })
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
     * {@link RandomFloatType }
     * {@link GteType }
     * {@link ProductType }
     * {@link IndexType }
     * {@link IntegerModulusType }
     * {@link GtType }
     * {@link AnyNType }
     * {@link PatternMatchType }
     * {@link DefaultType }
     * {@link IntegerToFloatType }
     * {@link EqualRoundedType }
     * {@link CustomOperatorType }
     * {@link MemberType }
     * {@link OrType }
     * {@link FieldValueType }
     * {@link DurationGTEType }
     * {@link SubstringType }
     * {@link InsideType }
     * {@link AndType }
     * {@link CorrectType }
     * {@link IsNullType }
     * {@link NullType }
     * {@link RandomIntegerType }
     * {@link MapResponseType }
     * {@link OrderedType }
     * {@link DeleteType }
     * {@link LteType }
     * {@link IntegerDivideType }
     * {@link LtType }
     * {@link TruncateType }
     * {@link RoundType }
     * {@link PowerType }
     * {@link ContainsType }
     * {@link MatchType }
     * {@link VariableType }
     * {@link EqualType }
     * {@link RandomType }
     * {@link SumType }
     * {@link NotType }
     * {@link MapResponsePointType }
     * {@link DivideType }
     * {@link BaseValueType }
     * {@link MultipleType }
     * {@link DurationLTType }
     * {@link SubtractType }
     * {@link StringMatchType }
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
