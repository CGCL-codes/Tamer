package com.volantis.mcs.build.themes.definitions.impl;

import com.volantis.mcs.build.themes.definitions.DefinitionsFactory;
import com.volantis.mcs.build.themes.definitions.Property;
import com.volantis.mcs.build.themes.definitions.Rule;
import com.volantis.mcs.build.themes.definitions.Rules;
import com.volantis.mcs.build.themes.definitions.TypeDefinition;
import com.volantis.mcs.build.themes.definitions.types.ChoiceType;
import com.volantis.mcs.build.themes.definitions.types.FractionType;
import com.volantis.mcs.build.themes.definitions.types.Keyword;
import com.volantis.mcs.build.themes.definitions.types.Keywords;
import com.volantis.mcs.build.themes.definitions.types.PairType;
import com.volantis.mcs.build.themes.definitions.types.TypeRef;
import com.volantis.mcs.build.themes.definitions.types.impl.ChoiceTypeImpl;
import com.volantis.mcs.build.themes.definitions.types.impl.FractionTypeImpl;
import com.volantis.mcs.build.themes.definitions.types.impl.KeywordImpl;
import com.volantis.mcs.build.themes.definitions.types.impl.KeywordsImpl;
import com.volantis.mcs.build.themes.definitions.types.impl.PairTypeImpl;
import com.volantis.mcs.build.themes.definitions.types.impl.TypeRefImpl;
import com.volantis.mcs.build.themes.definitions.values.AngleValue;
import com.volantis.mcs.build.themes.definitions.values.ColorValue;
import com.volantis.mcs.build.themes.definitions.values.FractionValue;
import com.volantis.mcs.build.themes.definitions.values.FrequencyValue;
import com.volantis.mcs.build.themes.definitions.values.InheritValue;
import com.volantis.mcs.build.themes.definitions.values.IntegerValue;
import com.volantis.mcs.build.themes.definitions.values.KeywordReference;
import com.volantis.mcs.build.themes.definitions.values.LengthValue;
import com.volantis.mcs.build.themes.definitions.values.ListValue;
import com.volantis.mcs.build.themes.definitions.values.PairValue;
import com.volantis.mcs.build.themes.definitions.values.PercentageValue;
import com.volantis.mcs.build.themes.definitions.values.StringValue;
import com.volantis.mcs.build.themes.definitions.values.TimeValue;
import com.volantis.mcs.build.themes.definitions.values.impl.AngleValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.ColorValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.FractionValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.FrequencyValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.InheritValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.IntegerValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.KeywordReferenceImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.LengthValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.ListValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.PairValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.PercentageValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.StringValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.TimeValueImpl;

/**
 * Default concrete implementation of DefinitionsFactory, creating simple
 * implementation classes for each of the object types.
 */
public class DefaultDefinitionsFactory extends DefinitionsFactory {

    public Property createProperty() {
        return new PropertyImpl();
    }

    public Rules createRules() {
        return new RulesImpl();
    }

    public Rule createRule() {
        return new RuleImpl();
    }

    public KeywordReference createKeywordReference() {
        return new KeywordReferenceImpl();
    }

    public Keywords createKeywords() {
        return new KeywordsImpl();
    }

    public Keyword createKeyword() {
        return new KeywordImpl();
    }

    public TypeDefinition createTypeDefinition() {
        return new TypeDefinitionImpl();
    }

    public ChoiceType createChoiceType() {
        return new ChoiceTypeImpl();
    }

    public PairType createPairType() {
        return new PairTypeImpl();
    }

    public FractionType createFractionType() {
        return new FractionTypeImpl();
    }

    public TypeRef createTypeRef() {
        return new TypeRefImpl();
    }

    public IntegerValue createIntegerValue() {
        return new IntegerValueImpl();
    }

    public AngleValue createAngleValue() {
        return new AngleValueImpl();
    }

    public StringValue createStringValue() {
        return new StringValueImpl();
    }

    public PercentageValue createPercentageValue() {
        return new PercentageValueImpl();
    }

    public PairValue createPairValue() {
        return new PairValueImpl();
    }

    public ListValue createListValue() {
        return new ListValueImpl();
    }

    public InheritValue createInheritValue() {
        return new InheritValueImpl();
    }

    public TimeValue createTimeValue() {
        return new TimeValueImpl();
    }

    public ColorValue createColorValue() {
        return new ColorValueImpl();
    }

    public LengthValue createLengthValue() {
        return new LengthValueImpl();
    }

    public FrequencyValue createFrequencyValue() {
        return new FrequencyValueImpl();
    }

    public FractionValue createFractionValue() {
        return new FractionValueImpl();
    }
}
