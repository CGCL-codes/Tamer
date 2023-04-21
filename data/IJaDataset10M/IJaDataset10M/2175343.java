package org.imsglobal.xsd.imsqti_v2p1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for not.Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="not.Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}not.ContentGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "not.Type", propOrder = { "and", "gt", "ordered", "divide", "customOperator", "random", "numberIncorrect", "substring", "not", "equalRounded", "multiple", "integerToFloat", "_null", "index", "testVariables", "integerDivide", "gte", "durationLT", "contains", "member", "lt", "correct", "numberSelected", "patternMatch", "product", "numberPresented", "power", "mapResponsePoint", "mapResponse", "randomFloat", "stringMatch", "variable", "integerModulus", "subtract", "durationGTE", "outcomeMaximum", "anyN", "round", "numberResponded", "baseValue", "_default", "inside", "containerSize", "equal", "outcomeMinimum", "or", "randomInteger", "isNull", "numberCorrect", "match", "lte", "sum", "truncate", "fieldValue", "delete" })
public class NotType {

    protected AndType and;

    protected GtType gt;

    protected OrderedType ordered;

    protected DivideType divide;

    protected CustomOperatorType customOperator;

    protected RandomType random;

    protected NumberIncorrectType numberIncorrect;

    protected SubstringType substring;

    protected NotType not;

    protected EqualRoundedType equalRounded;

    protected MultipleType multiple;

    protected IntegerToFloatType integerToFloat;

    @XmlElement(name = "null")
    protected NullType _null;

    protected IndexType index;

    protected TestVariablesType testVariables;

    protected IntegerDivideType integerDivide;

    protected GteType gte;

    protected DurationLTType durationLT;

    protected ContainsType contains;

    protected MemberType member;

    protected LtType lt;

    protected CorrectType correct;

    protected NumberSelectedType numberSelected;

    protected PatternMatchType patternMatch;

    protected ProductType product;

    protected NumberPresentedType numberPresented;

    protected PowerType power;

    protected MapResponsePointType mapResponsePoint;

    protected MapResponseType mapResponse;

    protected RandomFloatType randomFloat;

    protected StringMatchType stringMatch;

    protected VariableType variable;

    protected IntegerModulusType integerModulus;

    protected SubtractType subtract;

    protected DurationGTEType durationGTE;

    protected OutcomeMaximumType outcomeMaximum;

    protected AnyNType anyN;

    protected RoundType round;

    protected NumberRespondedType numberResponded;

    protected BaseValueType baseValue;

    @XmlElement(name = "default")
    protected DefaultType _default;

    protected InsideType inside;

    protected ContainerSizeType containerSize;

    protected EqualType equal;

    protected OutcomeMinimumType outcomeMinimum;

    protected OrType or;

    protected RandomIntegerType randomInteger;

    protected IsNullType isNull;

    protected NumberCorrectType numberCorrect;

    protected MatchType match;

    protected LteType lte;

    protected SumType sum;

    protected TruncateType truncate;

    protected FieldValueType fieldValue;

    protected DeleteType delete;

    /**
     * Gets the value of the and property.
     * 
     * @return
     *     possible object is
     *     {@link AndType }
     *     
     */
    public AndType getAnd() {
        return and;
    }

    /**
     * Sets the value of the and property.
     * 
     * @param value
     *     allowed object is
     *     {@link AndType }
     *     
     */
    public void setAnd(AndType value) {
        this.and = value;
    }

    /**
     * Gets the value of the gt property.
     * 
     * @return
     *     possible object is
     *     {@link GtType }
     *     
     */
    public GtType getGt() {
        return gt;
    }

    /**
     * Sets the value of the gt property.
     * 
     * @param value
     *     allowed object is
     *     {@link GtType }
     *     
     */
    public void setGt(GtType value) {
        this.gt = value;
    }

    /**
     * Gets the value of the ordered property.
     * 
     * @return
     *     possible object is
     *     {@link OrderedType }
     *     
     */
    public OrderedType getOrdered() {
        return ordered;
    }

    /**
     * Sets the value of the ordered property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderedType }
     *     
     */
    public void setOrdered(OrderedType value) {
        this.ordered = value;
    }

    /**
     * Gets the value of the divide property.
     * 
     * @return
     *     possible object is
     *     {@link DivideType }
     *     
     */
    public DivideType getDivide() {
        return divide;
    }

    /**
     * Sets the value of the divide property.
     * 
     * @param value
     *     allowed object is
     *     {@link DivideType }
     *     
     */
    public void setDivide(DivideType value) {
        this.divide = value;
    }

    /**
     * Gets the value of the customOperator property.
     * 
     * @return
     *     possible object is
     *     {@link CustomOperatorType }
     *     
     */
    public CustomOperatorType getCustomOperator() {
        return customOperator;
    }

    /**
     * Sets the value of the customOperator property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomOperatorType }
     *     
     */
    public void setCustomOperator(CustomOperatorType value) {
        this.customOperator = value;
    }

    /**
     * Gets the value of the random property.
     * 
     * @return
     *     possible object is
     *     {@link RandomType }
     *     
     */
    public RandomType getRandom() {
        return random;
    }

    /**
     * Sets the value of the random property.
     * 
     * @param value
     *     allowed object is
     *     {@link RandomType }
     *     
     */
    public void setRandom(RandomType value) {
        this.random = value;
    }

    /**
     * Gets the value of the numberIncorrect property.
     * 
     * @return
     *     possible object is
     *     {@link NumberIncorrectType }
     *     
     */
    public NumberIncorrectType getNumberIncorrect() {
        return numberIncorrect;
    }

    /**
     * Sets the value of the numberIncorrect property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumberIncorrectType }
     *     
     */
    public void setNumberIncorrect(NumberIncorrectType value) {
        this.numberIncorrect = value;
    }

    /**
     * Gets the value of the substring property.
     * 
     * @return
     *     possible object is
     *     {@link SubstringType }
     *     
     */
    public SubstringType getSubstring() {
        return substring;
    }

    /**
     * Sets the value of the substring property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubstringType }
     *     
     */
    public void setSubstring(SubstringType value) {
        this.substring = value;
    }

    /**
     * Gets the value of the not property.
     * 
     * @return
     *     possible object is
     *     {@link NotType }
     *     
     */
    public NotType getNot() {
        return not;
    }

    /**
     * Sets the value of the not property.
     * 
     * @param value
     *     allowed object is
     *     {@link NotType }
     *     
     */
    public void setNot(NotType value) {
        this.not = value;
    }

    /**
     * Gets the value of the equalRounded property.
     * 
     * @return
     *     possible object is
     *     {@link EqualRoundedType }
     *     
     */
    public EqualRoundedType getEqualRounded() {
        return equalRounded;
    }

    /**
     * Sets the value of the equalRounded property.
     * 
     * @param value
     *     allowed object is
     *     {@link EqualRoundedType }
     *     
     */
    public void setEqualRounded(EqualRoundedType value) {
        this.equalRounded = value;
    }

    /**
     * Gets the value of the multiple property.
     * 
     * @return
     *     possible object is
     *     {@link MultipleType }
     *     
     */
    public MultipleType getMultiple() {
        return multiple;
    }

    /**
     * Sets the value of the multiple property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultipleType }
     *     
     */
    public void setMultiple(MultipleType value) {
        this.multiple = value;
    }

    /**
     * Gets the value of the integerToFloat property.
     * 
     * @return
     *     possible object is
     *     {@link IntegerToFloatType }
     *     
     */
    public IntegerToFloatType getIntegerToFloat() {
        return integerToFloat;
    }

    /**
     * Sets the value of the integerToFloat property.
     * 
     * @param value
     *     allowed object is
     *     {@link IntegerToFloatType }
     *     
     */
    public void setIntegerToFloat(IntegerToFloatType value) {
        this.integerToFloat = value;
    }

    /**
     * Gets the value of the null property.
     * 
     * @return
     *     possible object is
     *     {@link NullType }
     *     
     */
    public NullType getNull() {
        return _null;
    }

    /**
     * Sets the value of the null property.
     * 
     * @param value
     *     allowed object is
     *     {@link NullType }
     *     
     */
    public void setNull(NullType value) {
        this._null = value;
    }

    /**
     * Gets the value of the index property.
     * 
     * @return
     *     possible object is
     *     {@link IndexType }
     *     
     */
    public IndexType getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndexType }
     *     
     */
    public void setIndex(IndexType value) {
        this.index = value;
    }

    /**
     * Gets the value of the testVariables property.
     * 
     * @return
     *     possible object is
     *     {@link TestVariablesType }
     *     
     */
    public TestVariablesType getTestVariables() {
        return testVariables;
    }

    /**
     * Sets the value of the testVariables property.
     * 
     * @param value
     *     allowed object is
     *     {@link TestVariablesType }
     *     
     */
    public void setTestVariables(TestVariablesType value) {
        this.testVariables = value;
    }

    /**
     * Gets the value of the integerDivide property.
     * 
     * @return
     *     possible object is
     *     {@link IntegerDivideType }
     *     
     */
    public IntegerDivideType getIntegerDivide() {
        return integerDivide;
    }

    /**
     * Sets the value of the integerDivide property.
     * 
     * @param value
     *     allowed object is
     *     {@link IntegerDivideType }
     *     
     */
    public void setIntegerDivide(IntegerDivideType value) {
        this.integerDivide = value;
    }

    /**
     * Gets the value of the gte property.
     * 
     * @return
     *     possible object is
     *     {@link GteType }
     *     
     */
    public GteType getGte() {
        return gte;
    }

    /**
     * Sets the value of the gte property.
     * 
     * @param value
     *     allowed object is
     *     {@link GteType }
     *     
     */
    public void setGte(GteType value) {
        this.gte = value;
    }

    /**
     * Gets the value of the durationLT property.
     * 
     * @return
     *     possible object is
     *     {@link DurationLTType }
     *     
     */
    public DurationLTType getDurationLT() {
        return durationLT;
    }

    /**
     * Sets the value of the durationLT property.
     * 
     * @param value
     *     allowed object is
     *     {@link DurationLTType }
     *     
     */
    public void setDurationLT(DurationLTType value) {
        this.durationLT = value;
    }

    /**
     * Gets the value of the contains property.
     * 
     * @return
     *     possible object is
     *     {@link ContainsType }
     *     
     */
    public ContainsType getContains() {
        return contains;
    }

    /**
     * Sets the value of the contains property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainsType }
     *     
     */
    public void setContains(ContainsType value) {
        this.contains = value;
    }

    /**
     * Gets the value of the member property.
     * 
     * @return
     *     possible object is
     *     {@link MemberType }
     *     
     */
    public MemberType getMember() {
        return member;
    }

    /**
     * Sets the value of the member property.
     * 
     * @param value
     *     allowed object is
     *     {@link MemberType }
     *     
     */
    public void setMember(MemberType value) {
        this.member = value;
    }

    /**
     * Gets the value of the lt property.
     * 
     * @return
     *     possible object is
     *     {@link LtType }
     *     
     */
    public LtType getLt() {
        return lt;
    }

    /**
     * Sets the value of the lt property.
     * 
     * @param value
     *     allowed object is
     *     {@link LtType }
     *     
     */
    public void setLt(LtType value) {
        this.lt = value;
    }

    /**
     * Gets the value of the correct property.
     * 
     * @return
     *     possible object is
     *     {@link CorrectType }
     *     
     */
    public CorrectType getCorrect() {
        return correct;
    }

    /**
     * Sets the value of the correct property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorrectType }
     *     
     */
    public void setCorrect(CorrectType value) {
        this.correct = value;
    }

    /**
     * Gets the value of the numberSelected property.
     * 
     * @return
     *     possible object is
     *     {@link NumberSelectedType }
     *     
     */
    public NumberSelectedType getNumberSelected() {
        return numberSelected;
    }

    /**
     * Sets the value of the numberSelected property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumberSelectedType }
     *     
     */
    public void setNumberSelected(NumberSelectedType value) {
        this.numberSelected = value;
    }

    /**
     * Gets the value of the patternMatch property.
     * 
     * @return
     *     possible object is
     *     {@link PatternMatchType }
     *     
     */
    public PatternMatchType getPatternMatch() {
        return patternMatch;
    }

    /**
     * Sets the value of the patternMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link PatternMatchType }
     *     
     */
    public void setPatternMatch(PatternMatchType value) {
        this.patternMatch = value;
    }

    /**
     * Gets the value of the product property.
     * 
     * @return
     *     possible object is
     *     {@link ProductType }
     *     
     */
    public ProductType getProduct() {
        return product;
    }

    /**
     * Sets the value of the product property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductType }
     *     
     */
    public void setProduct(ProductType value) {
        this.product = value;
    }

    /**
     * Gets the value of the numberPresented property.
     * 
     * @return
     *     possible object is
     *     {@link NumberPresentedType }
     *     
     */
    public NumberPresentedType getNumberPresented() {
        return numberPresented;
    }

    /**
     * Sets the value of the numberPresented property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumberPresentedType }
     *     
     */
    public void setNumberPresented(NumberPresentedType value) {
        this.numberPresented = value;
    }

    /**
     * Gets the value of the power property.
     * 
     * @return
     *     possible object is
     *     {@link PowerType }
     *     
     */
    public PowerType getPower() {
        return power;
    }

    /**
     * Sets the value of the power property.
     * 
     * @param value
     *     allowed object is
     *     {@link PowerType }
     *     
     */
    public void setPower(PowerType value) {
        this.power = value;
    }

    /**
     * Gets the value of the mapResponsePoint property.
     * 
     * @return
     *     possible object is
     *     {@link MapResponsePointType }
     *     
     */
    public MapResponsePointType getMapResponsePoint() {
        return mapResponsePoint;
    }

    /**
     * Sets the value of the mapResponsePoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link MapResponsePointType }
     *     
     */
    public void setMapResponsePoint(MapResponsePointType value) {
        this.mapResponsePoint = value;
    }

    /**
     * Gets the value of the mapResponse property.
     * 
     * @return
     *     possible object is
     *     {@link MapResponseType }
     *     
     */
    public MapResponseType getMapResponse() {
        return mapResponse;
    }

    /**
     * Sets the value of the mapResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link MapResponseType }
     *     
     */
    public void setMapResponse(MapResponseType value) {
        this.mapResponse = value;
    }

    /**
     * Gets the value of the randomFloat property.
     * 
     * @return
     *     possible object is
     *     {@link RandomFloatType }
     *     
     */
    public RandomFloatType getRandomFloat() {
        return randomFloat;
    }

    /**
     * Sets the value of the randomFloat property.
     * 
     * @param value
     *     allowed object is
     *     {@link RandomFloatType }
     *     
     */
    public void setRandomFloat(RandomFloatType value) {
        this.randomFloat = value;
    }

    /**
     * Gets the value of the stringMatch property.
     * 
     * @return
     *     possible object is
     *     {@link StringMatchType }
     *     
     */
    public StringMatchType getStringMatch() {
        return stringMatch;
    }

    /**
     * Sets the value of the stringMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link StringMatchType }
     *     
     */
    public void setStringMatch(StringMatchType value) {
        this.stringMatch = value;
    }

    /**
     * Gets the value of the variable property.
     * 
     * @return
     *     possible object is
     *     {@link VariableType }
     *     
     */
    public VariableType getVariable() {
        return variable;
    }

    /**
     * Sets the value of the variable property.
     * 
     * @param value
     *     allowed object is
     *     {@link VariableType }
     *     
     */
    public void setVariable(VariableType value) {
        this.variable = value;
    }

    /**
     * Gets the value of the integerModulus property.
     * 
     * @return
     *     possible object is
     *     {@link IntegerModulusType }
     *     
     */
    public IntegerModulusType getIntegerModulus() {
        return integerModulus;
    }

    /**
     * Sets the value of the integerModulus property.
     * 
     * @param value
     *     allowed object is
     *     {@link IntegerModulusType }
     *     
     */
    public void setIntegerModulus(IntegerModulusType value) {
        this.integerModulus = value;
    }

    /**
     * Gets the value of the subtract property.
     * 
     * @return
     *     possible object is
     *     {@link SubtractType }
     *     
     */
    public SubtractType getSubtract() {
        return subtract;
    }

    /**
     * Sets the value of the subtract property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubtractType }
     *     
     */
    public void setSubtract(SubtractType value) {
        this.subtract = value;
    }

    /**
     * Gets the value of the durationGTE property.
     * 
     * @return
     *     possible object is
     *     {@link DurationGTEType }
     *     
     */
    public DurationGTEType getDurationGTE() {
        return durationGTE;
    }

    /**
     * Sets the value of the durationGTE property.
     * 
     * @param value
     *     allowed object is
     *     {@link DurationGTEType }
     *     
     */
    public void setDurationGTE(DurationGTEType value) {
        this.durationGTE = value;
    }

    /**
     * Gets the value of the outcomeMaximum property.
     * 
     * @return
     *     possible object is
     *     {@link OutcomeMaximumType }
     *     
     */
    public OutcomeMaximumType getOutcomeMaximum() {
        return outcomeMaximum;
    }

    /**
     * Sets the value of the outcomeMaximum property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutcomeMaximumType }
     *     
     */
    public void setOutcomeMaximum(OutcomeMaximumType value) {
        this.outcomeMaximum = value;
    }

    /**
     * Gets the value of the anyN property.
     * 
     * @return
     *     possible object is
     *     {@link AnyNType }
     *     
     */
    public AnyNType getAnyN() {
        return anyN;
    }

    /**
     * Sets the value of the anyN property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyNType }
     *     
     */
    public void setAnyN(AnyNType value) {
        this.anyN = value;
    }

    /**
     * Gets the value of the round property.
     * 
     * @return
     *     possible object is
     *     {@link RoundType }
     *     
     */
    public RoundType getRound() {
        return round;
    }

    /**
     * Sets the value of the round property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoundType }
     *     
     */
    public void setRound(RoundType value) {
        this.round = value;
    }

    /**
     * Gets the value of the numberResponded property.
     * 
     * @return
     *     possible object is
     *     {@link NumberRespondedType }
     *     
     */
    public NumberRespondedType getNumberResponded() {
        return numberResponded;
    }

    /**
     * Sets the value of the numberResponded property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumberRespondedType }
     *     
     */
    public void setNumberResponded(NumberRespondedType value) {
        this.numberResponded = value;
    }

    /**
     * Gets the value of the baseValue property.
     * 
     * @return
     *     possible object is
     *     {@link BaseValueType }
     *     
     */
    public BaseValueType getBaseValue() {
        return baseValue;
    }

    /**
     * Sets the value of the baseValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseValueType }
     *     
     */
    public void setBaseValue(BaseValueType value) {
        this.baseValue = value;
    }

    /**
     * Gets the value of the default property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultType }
     *     
     */
    public DefaultType getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultType }
     *     
     */
    public void setDefault(DefaultType value) {
        this._default = value;
    }

    /**
     * Gets the value of the inside property.
     * 
     * @return
     *     possible object is
     *     {@link InsideType }
     *     
     */
    public InsideType getInside() {
        return inside;
    }

    /**
     * Sets the value of the inside property.
     * 
     * @param value
     *     allowed object is
     *     {@link InsideType }
     *     
     */
    public void setInside(InsideType value) {
        this.inside = value;
    }

    /**
     * Gets the value of the containerSize property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerSizeType }
     *     
     */
    public ContainerSizeType getContainerSize() {
        return containerSize;
    }

    /**
     * Sets the value of the containerSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerSizeType }
     *     
     */
    public void setContainerSize(ContainerSizeType value) {
        this.containerSize = value;
    }

    /**
     * Gets the value of the equal property.
     * 
     * @return
     *     possible object is
     *     {@link EqualType }
     *     
     */
    public EqualType getEqual() {
        return equal;
    }

    /**
     * Sets the value of the equal property.
     * 
     * @param value
     *     allowed object is
     *     {@link EqualType }
     *     
     */
    public void setEqual(EqualType value) {
        this.equal = value;
    }

    /**
     * Gets the value of the outcomeMinimum property.
     * 
     * @return
     *     possible object is
     *     {@link OutcomeMinimumType }
     *     
     */
    public OutcomeMinimumType getOutcomeMinimum() {
        return outcomeMinimum;
    }

    /**
     * Sets the value of the outcomeMinimum property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutcomeMinimumType }
     *     
     */
    public void setOutcomeMinimum(OutcomeMinimumType value) {
        this.outcomeMinimum = value;
    }

    /**
     * Gets the value of the or property.
     * 
     * @return
     *     possible object is
     *     {@link OrType }
     *     
     */
    public OrType getOr() {
        return or;
    }

    /**
     * Sets the value of the or property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrType }
     *     
     */
    public void setOr(OrType value) {
        this.or = value;
    }

    /**
     * Gets the value of the randomInteger property.
     * 
     * @return
     *     possible object is
     *     {@link RandomIntegerType }
     *     
     */
    public RandomIntegerType getRandomInteger() {
        return randomInteger;
    }

    /**
     * Sets the value of the randomInteger property.
     * 
     * @param value
     *     allowed object is
     *     {@link RandomIntegerType }
     *     
     */
    public void setRandomInteger(RandomIntegerType value) {
        this.randomInteger = value;
    }

    /**
     * Gets the value of the isNull property.
     * 
     * @return
     *     possible object is
     *     {@link IsNullType }
     *     
     */
    public IsNullType getIsNull() {
        return isNull;
    }

    /**
     * Sets the value of the isNull property.
     * 
     * @param value
     *     allowed object is
     *     {@link IsNullType }
     *     
     */
    public void setIsNull(IsNullType value) {
        this.isNull = value;
    }

    /**
     * Gets the value of the numberCorrect property.
     * 
     * @return
     *     possible object is
     *     {@link NumberCorrectType }
     *     
     */
    public NumberCorrectType getNumberCorrect() {
        return numberCorrect;
    }

    /**
     * Sets the value of the numberCorrect property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumberCorrectType }
     *     
     */
    public void setNumberCorrect(NumberCorrectType value) {
        this.numberCorrect = value;
    }

    /**
     * Gets the value of the match property.
     * 
     * @return
     *     possible object is
     *     {@link MatchType }
     *     
     */
    public MatchType getMatch() {
        return match;
    }

    /**
     * Sets the value of the match property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatchType }
     *     
     */
    public void setMatch(MatchType value) {
        this.match = value;
    }

    /**
     * Gets the value of the lte property.
     * 
     * @return
     *     possible object is
     *     {@link LteType }
     *     
     */
    public LteType getLte() {
        return lte;
    }

    /**
     * Sets the value of the lte property.
     * 
     * @param value
     *     allowed object is
     *     {@link LteType }
     *     
     */
    public void setLte(LteType value) {
        this.lte = value;
    }

    /**
     * Gets the value of the sum property.
     * 
     * @return
     *     possible object is
     *     {@link SumType }
     *     
     */
    public SumType getSum() {
        return sum;
    }

    /**
     * Sets the value of the sum property.
     * 
     * @param value
     *     allowed object is
     *     {@link SumType }
     *     
     */
    public void setSum(SumType value) {
        this.sum = value;
    }

    /**
     * Gets the value of the truncate property.
     * 
     * @return
     *     possible object is
     *     {@link TruncateType }
     *     
     */
    public TruncateType getTruncate() {
        return truncate;
    }

    /**
     * Sets the value of the truncate property.
     * 
     * @param value
     *     allowed object is
     *     {@link TruncateType }
     *     
     */
    public void setTruncate(TruncateType value) {
        this.truncate = value;
    }

    /**
     * Gets the value of the fieldValue property.
     * 
     * @return
     *     possible object is
     *     {@link FieldValueType }
     *     
     */
    public FieldValueType getFieldValue() {
        return fieldValue;
    }

    /**
     * Sets the value of the fieldValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldValueType }
     *     
     */
    public void setFieldValue(FieldValueType value) {
        this.fieldValue = value;
    }

    /**
     * Gets the value of the delete property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteType }
     *     
     */
    public DeleteType getDelete() {
        return delete;
    }

    /**
     * Sets the value of the delete property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteType }
     *     
     */
    public void setDelete(DeleteType value) {
        this.delete = value;
    }
}
