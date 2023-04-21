package ie.ucd.clops.dsl.structs;

import ie.ucd.clops.dsl.errors.DSLParseResult;
import ie.ucd.clops.dsl.errors.DuplicateOptionIdentifier;
import ie.ucd.clops.dsl.parser.SourceLocation;
import ie.ucd.clops.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionStoreDescription {

    /** true if the object is sealed (immutable). */
    private boolean isPacked;

    private final List<OptionDescription> optionDescriptions;

    private final DSLParseResult problems;

    private final Map<String, OptionDescription> optionNameMap;

    private final Map<String, OptionGroupDescription> optionGroupNameMap;

    /** keep track of all options and groups. */
    private final Map<String, OptionDescription> optionAndGroupNameMap;

    private final Map<Pair<String, String>, SourceLocation> propertyNameLocationMap;

    private final Map<Pair<String, String>, SourceLocation> propertyValueLocationMap;

    private final List<OptionGroupDescription> optionGroupDescriptions;

    public OptionStoreDescription() {
        problems = new DSLParseResult();
        optionDescriptions = new ArrayList<OptionDescription>();
        optionAndGroupNameMap = new HashMap<String, OptionDescription>();
        optionNameMap = new HashMap<String, OptionDescription>();
        optionGroupNameMap = new HashMap<String, OptionGroupDescription>();
        optionGroupDescriptions = new ArrayList<OptionGroupDescription>();
        propertyNameLocationMap = new HashMap<Pair<String, String>, SourceLocation>();
        propertyValueLocationMap = new HashMap<Pair<String, String>, SourceLocation>();
    }

    public void addOptionDescription(OptionDescription optionDescription) {
        assert (!isPacked);
        OptionDescription other = optionNameMap.get(optionDescription.getIdentifier());
        if (other != null) {
            problems.addError(new DuplicateOptionIdentifier(optionDescription.getSourceLocation(), optionDescription.getIdentifier(), other.getSourceLocation()));
        }
        optionDescriptions.add(optionDescription);
        optionAndGroupNameMap.put(optionDescription.getIdentifier(), optionDescription);
        optionNameMap.put(optionDescription.getIdentifier(), optionDescription);
    }

    public List<OptionDescription> getOptionDescriptions() {
        return optionDescriptions;
    }

    public void addOptionGroupDescription(OptionGroupDescription group) {
        assert (!isPacked);
        OptionGroupDescription other = optionGroupNameMap.get(group.getIdentifier());
        if (other != null) {
            problems.addError(new DuplicateOptionIdentifier(group.getSourceLocation(), group.getIdentifier(), other.getSourceLocation()));
        }
        optionGroupDescriptions.add(group);
        optionAndGroupNameMap.put(group.getIdentifier(), group);
        optionGroupNameMap.put(group.getIdentifier(), group);
    }

    public List<OptionGroupDescription> getOptionGroupDescriptions() {
        return optionGroupDescriptions;
    }

    public OptionDescription getOptionDescriptionForIdentifier(String id) {
        return optionNameMap.get(id);
    }

    public OptionGroupDescription getOptionGroupDescriptionForIdentifier(String id) {
        return optionGroupNameMap.get(id);
    }

    public String getOptionValueTypeParameterisationForIdentifier(String identifier) {
        final OptionDescription od = getOptionDescriptionForIdentifier(identifier);
        return od == null ? null : od.getType().getOptionValueTypeParameterisation();
    }

    /** 
   * Packs the object: make sure that every value has
   * been properly set and makes it immutable.
   */
    public void pack() {
        isPacked = true;
        for (OptionGroupDescription og : optionGroupDescriptions) {
            og.expand(optionNameMap, optionGroupNameMap);
        }
    }

    public Map<String, OptionDescription> getOptionNameMap() {
        return optionNameMap;
    }

    public Map<String, OptionGroupDescription> getOptionGroupNameMap() {
        return optionGroupNameMap;
    }

    public Map<String, OptionDescription> getOptionAndGroupNameMap() {
        return optionAndGroupNameMap;
    }

    public DSLParseResult getParseResult() {
        return problems;
    }

    public void setPropertyNameLocation(Pair<String, String> property, SourceLocation location) {
        propertyNameLocationMap.put(property, location);
    }

    public SourceLocation getPropertyNameLocation(Pair<String, String> property) {
        return propertyNameLocationMap.get(property);
    }

    public void setPropertyValueLocation(Pair<String, String> property, SourceLocation location) {
        propertyValueLocationMap.put(property, location);
    }

    public SourceLocation getPropertyValueLocation(Pair<String, String> property) {
        return propertyValueLocationMap.get(property);
    }
}
