package org.apache.xmlbeans.impl.schema;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaAttributeModel;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.common.XBeanDebug;
import org.apache.xmlbeans.impl.common.QNameHelper;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.math.BigInteger;

public class StscChecker {

    public static void checkAll() {
        StscState state = StscState.get();
        List allSeenTypes = new ArrayList();
        allSeenTypes.addAll(Arrays.asList(state.documentTypes()));
        allSeenTypes.addAll(Arrays.asList(state.attributeTypes()));
        allSeenTypes.addAll(Arrays.asList(state.redefinedGlobalTypes()));
        allSeenTypes.addAll(Arrays.asList(state.globalTypes()));
        for (int i = 0; i < allSeenTypes.size(); i++) {
            SchemaType gType = (SchemaType) allSeenTypes.get(i);
            if (!state.noPvr() && !gType.isDocumentType()) {
                checkRestriction((SchemaTypeImpl) gType);
            }
            checkFields((SchemaTypeImpl) gType);
            allSeenTypes.addAll(Arrays.asList(gType.getAnonymousTypes()));
        }
        checkSubstitutionGroups(state.globalElements());
    }

    /**
     * The following code checks rule #5 of http://www.w3.org/TR/xmlschema-1/#coss-ct
     * as well as attribute + element default/fixed validity.
     */
    public static void checkFields(SchemaTypeImpl sType) {
        if (sType.isSimpleType()) return;
        XmlObject location = sType.getParseObject();
        SchemaAttributeModel sAttrModel = sType.getAttributeModel();
        if (sAttrModel != null) {
            SchemaLocalAttribute[] sAttrs = sAttrModel.getAttributes();
            QName idAttr = null;
            for (int i = 0; i < sAttrs.length; i++) {
                XmlObject attrLocation = ((SchemaLocalAttributeImpl) sAttrs[i])._parseObject;
                if (XmlID.type.isAssignableFrom(sAttrs[i].getType())) {
                    if (idAttr == null) {
                        idAttr = sAttrs[i].getName();
                    } else {
                        StscState.get().error(XmlErrorCodes.ATTR_GROUP_PROPERTIES$TWO_IDS, new Object[] { QNameHelper.pretty(idAttr), sAttrs[i].getName() }, attrLocation != null ? attrLocation : location);
                    }
                    if (sAttrs[i].getDefaultText() != null) {
                        StscState.get().error(XmlErrorCodes.ATTR_PROPERTIES$ID_FIXED_OR_DEFAULT, null, attrLocation != null ? attrLocation : location);
                    }
                } else {
                    String valueConstraint = sAttrs[i].getDefaultText();
                    if (valueConstraint != null) {
                        try {
                            XmlAnySimpleType val = sAttrs[i].getDefaultValue();
                            if (!val.validate()) throw new Exception();
                            SchemaPropertyImpl sProp = (SchemaPropertyImpl) sType.getAttributeProperty(sAttrs[i].getName());
                            if (sProp != null && sProp.getDefaultText() != null) {
                                sProp.setDefaultValue(new XmlValueRef(val));
                            }
                        } catch (Exception e) {
                            String constraintName = (sAttrs[i].isFixed() ? "fixed" : "default");
                            XmlObject constraintLocation = location;
                            if (attrLocation != null) {
                                constraintLocation = attrLocation.selectAttribute("", constraintName);
                                if (constraintLocation == null) constraintLocation = attrLocation;
                            }
                            StscState.get().error(XmlErrorCodes.ATTR_PROPERTIES$CONSTRAINT_VALID, new Object[] { QNameHelper.pretty(sAttrs[i].getName()), constraintName, valueConstraint, QNameHelper.pretty(sAttrs[i].getType().getName()) }, constraintLocation);
                        }
                    }
                }
            }
        }
        checkElementDefaults(sType.getContentModel(), location, sType);
    }

    private static void checkElementDefaults(SchemaParticle model, XmlObject location, SchemaType parentType) {
        if (model == null) return;
        switch(model.getParticleType()) {
            case SchemaParticle.SEQUENCE:
            case SchemaParticle.CHOICE:
            case SchemaParticle.ALL:
                SchemaParticle[] children = model.getParticleChildren();
                for (int i = 0; i < children.length; i++) {
                    checkElementDefaults(children[i], location, parentType);
                }
                break;
            case SchemaParticle.ELEMENT:
                String valueConstraint = model.getDefaultText();
                if (valueConstraint != null) {
                    if (model.getType().isSimpleType() || model.getType().getContentType() == SchemaType.SIMPLE_CONTENT) {
                        try {
                            XmlAnySimpleType val = model.getDefaultValue();
                            XmlOptions opt = new XmlOptions();
                            opt.put(XmlOptions.VALIDATE_TEXT_ONLY);
                            if (!val.validate(opt)) throw new Exception();
                            SchemaPropertyImpl sProp = (SchemaPropertyImpl) parentType.getElementProperty(model.getName());
                            if (sProp != null && sProp.getDefaultText() != null) {
                                sProp.setDefaultValue(new XmlValueRef(val));
                            }
                        } catch (Exception e) {
                            String constraintName = (model.isFixed() ? "fixed" : "default");
                            XmlObject constraintLocation = location.selectAttribute("", constraintName);
                            StscState.get().error(XmlErrorCodes.ELEM_PROPERTIES$CONSTRAINT_VALID, new Object[] { QNameHelper.pretty(model.getName()), constraintName, valueConstraint, QNameHelper.pretty(model.getType().getName()) }, (constraintLocation == null ? location : constraintLocation));
                        }
                    } else if (model.getType().getContentType() == SchemaType.MIXED_CONTENT) {
                        if (!model.getType().getContentModel().isSkippable()) {
                            String constraintName = (model.isFixed() ? "fixed" : "default");
                            XmlObject constraintLocation = location.selectAttribute("", constraintName);
                            StscState.get().error(XmlErrorCodes.ELEM_DEFAULT_VALID$MIXED_AND_EMPTIABLE, new Object[] { QNameHelper.pretty(model.getName()), constraintName, valueConstraint }, (constraintLocation == null ? location : constraintLocation));
                        } else {
                            SchemaPropertyImpl sProp = (SchemaPropertyImpl) parentType.getElementProperty(model.getName());
                            if (sProp != null && sProp.getDefaultText() != null) {
                                sProp.setDefaultValue(new XmlValueRef(XmlString.type.newValue(valueConstraint)));
                            }
                        }
                    } else if (model.getType().getContentType() == SchemaType.ELEMENT_CONTENT) {
                        XmlObject constraintLocation = location.selectAttribute("", "default");
                        StscState.get().error(XmlErrorCodes.ELEM_DEFAULT_VALID$SIMPLE_TYPE_OR_MIXED, new Object[] { QNameHelper.pretty(model.getName()), valueConstraint, "element" }, (constraintLocation == null ? location : constraintLocation));
                    } else if (model.getType().getContentType() == SchemaType.EMPTY_CONTENT) {
                        XmlObject constraintLocation = location.selectAttribute("", "default");
                        StscState.get().error(XmlErrorCodes.ELEM_DEFAULT_VALID$SIMPLE_TYPE_OR_MIXED, new Object[] { QNameHelper.pretty(model.getName()), valueConstraint, "empty" }, (constraintLocation == null ? location : constraintLocation));
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * The following code only checks rule #5 of http://www.w3.org/TR/xmlschema-1/#derivation-ok-restriction
     *  (Everything else can and should be done in StscResolver, because we can give more detailed line # info there
     */
    public static boolean checkRestriction(SchemaTypeImpl sType) {
        if (sType.getDerivationType() == SchemaType.DT_RESTRICTION && !sType.isSimpleType()) {
            StscState state = StscState.get();
            XmlObject location = sType.getParseObject();
            SchemaType baseType = sType.getBaseType();
            if (baseType.isSimpleType()) {
                state.error(XmlErrorCodes.SCHEMA_COMPLEX_TYPE$COMPLEX_CONTENT, new Object[] { QNameHelper.pretty(baseType.getName()) }, location);
                return false;
            }
            switch(sType.getContentType()) {
                case SchemaType.SIMPLE_CONTENT:
                    switch(baseType.getContentType()) {
                        case SchemaType.SIMPLE_CONTENT:
                            SchemaType cType = sType.getContentBasedOnType();
                            if (cType != baseType) {
                                SchemaType bType = baseType;
                                while (bType != null && !bType.isSimpleType()) bType = bType.getContentBasedOnType();
                                if (bType != null && !bType.isAssignableFrom(cType)) {
                                    state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$SC_NOT_DERIVED, null, location);
                                    return false;
                                }
                            }
                            break;
                        case SchemaType.MIXED_CONTENT:
                            if (baseType.getContentModel() != null && !baseType.getContentModel().isSkippable()) {
                                state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$SC_AND_MIXED_EMPTIABLE, null, location);
                                return false;
                            }
                            break;
                        default:
                            state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$SC_AND_SIMPLE_TYPE_OR_MIXED, null, location);
                            return false;
                    }
                    break;
                case SchemaType.EMPTY_CONTENT:
                    switch(baseType.getContentType()) {
                        case SchemaType.EMPTY_CONTENT:
                            break;
                        case SchemaType.MIXED_CONTENT:
                        case SchemaType.ELEMENT_CONTENT:
                            if (baseType.getContentModel() != null && !baseType.getContentModel().isSkippable()) {
                                state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$EMPTY_AND_ELEMENT_OR_MIXED_EMPTIABLE, null, location);
                                return false;
                            }
                            break;
                        default:
                            state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$EMPTY_AND_NOT_SIMPLE, null, location);
                            return false;
                    }
                    break;
                case SchemaType.MIXED_CONTENT:
                    if (baseType.getContentType() != SchemaType.MIXED_CONTENT) {
                        state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$ELEMENT_OR_MIXED_AND_MIXED, null, location);
                        return false;
                    }
                case SchemaType.ELEMENT_CONTENT:
                    if (baseType.getContentType() == SchemaType.EMPTY_CONTENT) {
                        state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$ELEMENT_OR_MIXED_AND_EMPTY, null, location);
                        return false;
                    }
                    if (baseType.getContentType() == SchemaType.SIMPLE_CONTENT) {
                        state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$ELEMENT_OR_MIXED_AND_SIMPLE, null, location);
                        return false;
                    }
                    SchemaParticle baseModel = baseType.getContentModel();
                    SchemaParticle derivedModel = sType.getContentModel();
                    assert (baseModel != null && derivedModel != null);
                    if (baseModel == null || derivedModel == null) {
                        XBeanDebug.logStackTrace("Null models that weren't caught by EMPTY_CONTENT: " + baseType + " (" + baseModel + "), " + sType + " (" + derivedModel + ")");
                        state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$ELEMENT_OR_MIXED_AND_VALID, null, location);
                        return false;
                    }
                    List errors = new ArrayList();
                    boolean isValid = isParticleValidRestriction(baseModel, derivedModel, errors, location);
                    if (!isValid) {
                        if (errors.size() == 0) state.error(XmlErrorCodes.COMPLEX_TYPE_RESTRICTION$ELEMENT_OR_MIXED_AND_VALID, null, location); else state.getErrorListener().add(errors.get(errors.size() - 1));
                        return false;
                    }
            }
        }
        return true;
    }

    /**
     * This function takes in two schema particle types, a baseModel, and a derived model and returns true if the
     * derivedModel can be egitimately be used for restriction.  Errors are put into the errors collections.
     * @param baseModel - The base schema particle
     * @param derivedModel - The derived (restricted) schema particle
     * @param errors - Invalid restriction errors are put into this collection
     * @param context
     * @return boolean, true if valid restruction, false if invalid restriction
     * @
     */
    public static boolean isParticleValidRestriction(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        boolean restrictionValid = false;
        if (baseModel.equals(derivedModel)) {
            restrictionValid = true;
        } else {
            switch(baseModel.getParticleType()) {
                case SchemaParticle.ELEMENT:
                    switch(derivedModel.getParticleType()) {
                        case SchemaParticle.ELEMENT:
                            restrictionValid = nameAndTypeOK((SchemaLocalElement) baseModel, (SchemaLocalElement) derivedModel, errors, context);
                            break;
                        case SchemaParticle.WILDCARD:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[] { printParticle(baseModel), printParticle(derivedModel) }, context));
                            restrictionValid = false;
                            break;
                        case SchemaParticle.ALL:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[] { printParticle(baseModel), printParticle(derivedModel) }, context));
                            restrictionValid = false;
                            break;
                        case SchemaParticle.CHOICE:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[] { printParticle(baseModel), printParticle(derivedModel) }, context));
                            restrictionValid = false;
                            break;
                        case SchemaParticle.SEQUENCE:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[] { printParticle(baseModel), printParticle(derivedModel) }, context));
                            restrictionValid = false;
                            break;
                        default:
                            assert false : XBeanDebug.logStackTrace("Unknown schema type for Derived Type");
                    }
                    break;
                case SchemaParticle.WILDCARD:
                    switch(derivedModel.getParticleType()) {
                        case SchemaParticle.ELEMENT:
                            restrictionValid = nsCompat(baseModel, (SchemaLocalElement) derivedModel, errors, context);
                            break;
                        case SchemaParticle.WILDCARD:
                            restrictionValid = nsSubset(baseModel, derivedModel, errors, context);
                            break;
                        case SchemaParticle.ALL:
                            restrictionValid = nsRecurseCheckCardinality(baseModel, derivedModel, errors, context);
                            break;
                        case SchemaParticle.CHOICE:
                            restrictionValid = nsRecurseCheckCardinality(baseModel, derivedModel, errors, context);
                            break;
                        case SchemaParticle.SEQUENCE:
                            restrictionValid = nsRecurseCheckCardinality(baseModel, derivedModel, errors, context);
                            break;
                        default:
                            assert false : XBeanDebug.logStackTrace("Unknown schema type for Derived Type");
                    }
                    break;
                case SchemaParticle.ALL:
                    switch(derivedModel.getParticleType()) {
                        case SchemaParticle.ELEMENT:
                            restrictionValid = recurseAsIfGroup(baseModel, derivedModel, errors, context);
                            break;
                        case SchemaParticle.WILDCARD:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[] { printParticle(baseModel), printParticle(derivedModel) }, context));
                            restrictionValid = false;
                            break;
                        case SchemaParticle.ALL:
                            restrictionValid = recurse(baseModel, derivedModel, errors, context);
                            break;
                        case SchemaParticle.CHOICE:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[] { printParticle(baseModel), printParticle(derivedModel) }, context));
                            restrictionValid = false;
                            break;
                        case SchemaParticle.SEQUENCE:
                            restrictionValid = recurseUnordered(baseModel, derivedModel, errors, context);
                            break;
                        default:
                            assert false : XBeanDebug.logStackTrace("Unknown schema type for Derived Type");
                    }
                    break;
                case SchemaParticle.CHOICE:
                    switch(derivedModel.getParticleType()) {
                        case SchemaParticle.ELEMENT:
                            restrictionValid = recurseAsIfGroup(baseModel, derivedModel, errors, context);
                            break;
                        case SchemaParticle.WILDCARD:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[] { printParticle(baseModel), printParticle(derivedModel) }, context));
                            restrictionValid = false;
                            break;
                        case SchemaParticle.ALL:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[] { printParticle(baseModel), printParticle(derivedModel) }, context));
                            restrictionValid = false;
                            break;
                        case SchemaParticle.CHOICE:
                            restrictionValid = recurseLax(baseModel, derivedModel, errors, context);
                            break;
                        case SchemaParticle.SEQUENCE:
                            restrictionValid = mapAndSum(baseModel, derivedModel, errors, context);
                            break;
                        default:
                            assert false : XBeanDebug.logStackTrace("Unknown schema type for Derived Type");
                    }
                    break;
                case SchemaParticle.SEQUENCE:
                    switch(derivedModel.getParticleType()) {
                        case SchemaParticle.ELEMENT:
                            restrictionValid = recurseAsIfGroup(baseModel, derivedModel, errors, context);
                            break;
                        case SchemaParticle.WILDCARD:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[] { printParticle(baseModel), printParticle(derivedModel) }, context));
                            restrictionValid = false;
                            break;
                        case SchemaParticle.ALL:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[] { printParticle(baseModel), printParticle(derivedModel) }, context));
                            restrictionValid = false;
                            break;
                        case SchemaParticle.CHOICE:
                            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION$INVALID_RESTRICTION, new Object[] { printParticle(baseModel), printParticle(derivedModel) }, context));
                            restrictionValid = false;
                            break;
                        case SchemaParticle.SEQUENCE:
                            restrictionValid = recurse(baseModel, derivedModel, errors, context);
                            break;
                        default:
                            assert false : XBeanDebug.logStackTrace("Unknown schema type for Derived Type");
                    }
                    break;
                default:
                    assert false : XBeanDebug.logStackTrace("Unknown schema type for Base Type");
            }
        }
        return restrictionValid;
    }

    private static boolean mapAndSum(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        assert baseModel.getParticleType() == SchemaParticle.CHOICE;
        assert derivedModel.getParticleType() == SchemaParticle.SEQUENCE;
        boolean mapAndSumValid = true;
        SchemaParticle[] derivedParticleArray = derivedModel.getParticleChildren();
        SchemaParticle[] baseParticleArray = baseModel.getParticleChildren();
        for (int i = 0; i < derivedParticleArray.length; i++) {
            SchemaParticle derivedParticle = derivedParticleArray[i];
            boolean foundMatch = false;
            for (int j = 0; j < baseParticleArray.length; j++) {
                SchemaParticle baseParticle = baseParticleArray[j];
                if (isParticleValidRestriction(baseParticle, derivedParticle, errors, context)) {
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                mapAndSumValid = false;
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_MAP_AND_SUM$MAP, new Object[] { printParticle(derivedParticle) }, context));
                return false;
            }
        }
        BigInteger derivedRangeMin = derivedModel.getMinOccurs().multiply(BigInteger.valueOf(derivedModel.getParticleChildren().length));
        BigInteger derivedRangeMax = null;
        BigInteger UNBOUNDED = null;
        if (derivedModel.getMaxOccurs() == UNBOUNDED) {
            derivedRangeMax = null;
        } else {
            derivedRangeMax = derivedModel.getMaxOccurs().multiply(BigInteger.valueOf(derivedModel.getParticleChildren().length));
        }
        if (derivedRangeMin.compareTo(baseModel.getMinOccurs()) < 0) {
            mapAndSumValid = false;
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_MAP_AND_SUM$SUM_MIN_OCCURS_GTE_MIN_OCCURS, new Object[] { derivedRangeMin.toString(), baseModel.getMinOccurs().toString() }, context));
        } else if (baseModel.getMaxOccurs() != UNBOUNDED && (derivedRangeMax == UNBOUNDED || derivedRangeMax.compareTo(baseModel.getMaxOccurs()) > 0)) {
            mapAndSumValid = false;
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_MAP_AND_SUM$SUM_MAX_OCCURS_LTE_MAX_OCCURS, new Object[] { derivedRangeMax == UNBOUNDED ? "unbounded" : derivedRangeMax.toString(), baseModel.getMaxOccurs().toString() }, context));
        }
        return mapAndSumValid;
    }

    private static boolean recurseAsIfGroup(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        assert (baseModel.getParticleType() == SchemaParticle.ALL && derivedModel.getParticleType() == SchemaParticle.ELEMENT) || (baseModel.getParticleType() == SchemaParticle.CHOICE && derivedModel.getParticleType() == SchemaParticle.ELEMENT) || (baseModel.getParticleType() == SchemaParticle.SEQUENCE && derivedModel.getParticleType() == SchemaParticle.ELEMENT);
        SchemaParticleImpl asIfPart = new SchemaParticleImpl();
        asIfPart.setParticleType(baseModel.getParticleType());
        asIfPart.setMinOccurs(BigInteger.ONE);
        asIfPart.setMaxOccurs(BigInteger.ONE);
        asIfPart.setParticleChildren(new SchemaParticle[] { derivedModel });
        return isParticleValidRestriction(baseModel, asIfPart, errors, context);
    }

    private static boolean recurseLax(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        assert baseModel.getParticleType() == SchemaParticle.CHOICE && derivedModel.getParticleType() == SchemaParticle.CHOICE;
        boolean recurseLaxValid = true;
        if (!occurrenceRangeOK(baseModel, derivedModel, errors, context)) {
            return false;
        }
        SchemaParticle[] derivedParticleArray = derivedModel.getParticleChildren();
        SchemaParticle[] baseParticleArray = baseModel.getParticleChildren();
        int i = 0, j = 0;
        for (; i < derivedParticleArray.length && j < baseParticleArray.length; ) {
            SchemaParticle derivedParticle = derivedParticleArray[i];
            SchemaParticle baseParticle = baseParticleArray[j];
            if (isParticleValidRestriction(baseParticle, derivedParticle, errors, context)) {
                i++;
                j++;
            } else {
                j++;
            }
        }
        if (i < derivedParticleArray.length) {
            recurseLaxValid = false;
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE_LAX$MAP, new Object[] { printParticles(baseParticleArray, i) }, context));
        }
        return recurseLaxValid;
    }

    private static boolean recurseUnordered(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        assert baseModel.getParticleType() == SchemaParticle.ALL && derivedModel.getParticleType() == SchemaParticle.SEQUENCE;
        boolean recurseUnorderedValid = true;
        if (!occurrenceRangeOK(baseModel, derivedModel, errors, context)) {
            return false;
        }
        SchemaParticle[] baseParticles = baseModel.getParticleChildren();
        HashMap baseParticleMap = new HashMap(10);
        Object MAPPED = new Object();
        for (int i = 0; i < baseParticles.length; i++) baseParticleMap.put(baseParticles[i].getName(), baseParticles[i]);
        SchemaParticle[] derivedParticles = derivedModel.getParticleChildren();
        for (int i = 0; i < derivedParticles.length; i++) {
            Object baseParticle = baseParticleMap.get(derivedParticles[i].getName());
            if (baseParticle == null) {
                recurseUnorderedValid = false;
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE_UNORDERED$MAP, new Object[] { printParticle(derivedParticles[i]) }, context));
                break;
            } else {
                if (baseParticle == MAPPED) {
                    recurseUnorderedValid = false;
                    errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE_UNORDERED$MAP_UNIQUE, new Object[] { printParticle(derivedParticles[i]) }, context));
                    break;
                } else {
                    SchemaParticle matchedBaseParticle = (SchemaParticle) baseParticle;
                    if (derivedParticles[i].getMaxOccurs() == null || derivedParticles[i].getMaxOccurs().compareTo(BigInteger.ONE) > 0) {
                        recurseUnorderedValid = false;
                        errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE_UNORDERED$MAP_MAX_OCCURS_1, new Object[] { printParticle(derivedParticles[i]), printMaxOccurs(derivedParticles[i].getMinOccurs()) }, context));
                        break;
                    }
                    if (!isParticleValidRestriction(matchedBaseParticle, derivedParticles[i], errors, context)) {
                        recurseUnorderedValid = false;
                        break;
                    }
                    baseParticleMap.put(derivedParticles[i].getName(), MAPPED);
                }
            }
        }
        if (recurseUnorderedValid) {
            Set baseParticleCollection = baseParticleMap.keySet();
            for (Iterator iterator = baseParticleCollection.iterator(); iterator.hasNext(); ) {
                QName baseParticleQName = (QName) iterator.next();
                if (baseParticleMap.get(baseParticleQName) != MAPPED && !((SchemaParticle) baseParticleMap.get(baseParticleQName)).isSkippable()) {
                    recurseUnorderedValid = false;
                    errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE_UNORDERED$UNMAPPED_ARE_EMPTIABLE, new Object[] { printParticle((SchemaParticle) baseParticleMap.get(baseParticleQName)) }, context));
                }
            }
        }
        return recurseUnorderedValid;
    }

    private static boolean recurse(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        boolean recurseValid = true;
        if (!occurrenceRangeOK(baseModel, derivedModel, errors, context)) {
            return false;
        }
        SchemaParticle[] derivedParticleArray = derivedModel.getParticleChildren();
        SchemaParticle[] baseParticleArray = baseModel.getParticleChildren();
        int i = 0, j = 0;
        for (; i < derivedParticleArray.length && j < baseParticleArray.length; ) {
            SchemaParticle derivedParticle = derivedParticleArray[i];
            SchemaParticle baseParticle = baseParticleArray[j];
            if (isParticleValidRestriction(baseParticle, derivedParticle, errors, context)) {
                i++;
                j++;
            } else {
                if (baseParticle.isSkippable()) {
                    j++;
                } else {
                    recurseValid = false;
                    errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE$MAP_VALID, new Object[] { printParticle(derivedParticle), printParticle(derivedModel), printParticle(baseParticle), printParticle(baseModel) }, context));
                    break;
                }
            }
        }
        if (i < derivedParticleArray.length) {
            recurseValid = false;
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE$MAP, new Object[] { printParticle(derivedModel), printParticle(baseModel), printParticles(derivedParticleArray, i) }, context));
        } else {
            if (j < baseParticleArray.length) {
                ArrayList particles = new ArrayList(baseParticleArray.length);
                for (int k = j; k < baseParticleArray.length; k++) {
                    if (!baseParticleArray[k].isSkippable()) {
                        particles.add(baseParticleArray[k]);
                    }
                }
                if (particles.size() > 0) {
                    recurseValid = false;
                    errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_RECURSE$UNMAPPED_ARE_EMPTIABLE, new Object[] { printParticle(baseModel), printParticle(derivedModel), printParticles(particles) }, context));
                }
            }
        }
        return recurseValid;
    }

    private static boolean nsRecurseCheckCardinality(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        assert baseModel.getParticleType() == SchemaParticle.WILDCARD;
        assert (derivedModel.getParticleType() == SchemaParticle.ALL) || (derivedModel.getParticleType() == SchemaParticle.CHOICE) || (derivedModel.getParticleType() == SchemaParticle.SEQUENCE);
        boolean nsRecurseCheckCardinality = true;
        SchemaParticleImpl asIfPart = new SchemaParticleImpl();
        asIfPart.setParticleType(baseModel.getParticleType());
        asIfPart.setWildcardProcess(baseModel.getWildcardProcess());
        asIfPart.setWildcardSet(baseModel.getWildcardSet());
        asIfPart.setMinOccurs(BigInteger.ZERO);
        asIfPart.setMaxOccurs(null);
        asIfPart.setTransitionRules(baseModel.getWildcardSet(), true);
        asIfPart.setTransitionNotes(baseModel.getWildcardSet(), true);
        SchemaParticle[] particleChildren = derivedModel.getParticleChildren();
        for (int i = 0; i < particleChildren.length; i++) {
            SchemaParticle particle = particleChildren[i];
            switch(particle.getParticleType()) {
                case SchemaParticle.ELEMENT:
                    nsRecurseCheckCardinality = nsCompat(asIfPart, (SchemaLocalElement) particle, errors, context);
                    break;
                case SchemaParticle.WILDCARD:
                    nsRecurseCheckCardinality = nsSubset(asIfPart, particle, errors, context);
                    break;
                case SchemaParticle.ALL:
                case SchemaParticle.CHOICE:
                case SchemaParticle.SEQUENCE:
                    nsRecurseCheckCardinality = nsRecurseCheckCardinality(asIfPart, particle, errors, context);
                    break;
            }
            if (!nsRecurseCheckCardinality) {
                break;
            }
        }
        if (nsRecurseCheckCardinality) {
            nsRecurseCheckCardinality = checkGroupOccurrenceOK(baseModel, derivedModel, errors, context);
        }
        return nsRecurseCheckCardinality;
    }

    private static boolean checkGroupOccurrenceOK(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        boolean groupOccurrenceOK = true;
        BigInteger minRange = BigInteger.ZERO;
        BigInteger maxRange = BigInteger.ZERO;
        switch(derivedModel.getParticleType()) {
            case SchemaParticle.ALL:
            case SchemaParticle.SEQUENCE:
                minRange = getEffectiveMinRangeAllSeq(derivedModel);
                maxRange = getEffectiveMaxRangeAllSeq(derivedModel);
                break;
            case SchemaParticle.CHOICE:
                minRange = getEffectiveMinRangeChoice(derivedModel);
                maxRange = getEffectiveMaxRangeChoice(derivedModel);
                break;
        }
        if (minRange.compareTo(baseModel.getMinOccurs()) < 0) {
            groupOccurrenceOK = false;
            errors.add(XmlError.forObject(XmlErrorCodes.OCCURRENCE_RANGE$MIN_GTE_MIN, new Object[] { printParticle(derivedModel), printParticle(baseModel) }, context));
        }
        BigInteger UNBOUNDED = null;
        if (baseModel.getMaxOccurs() != UNBOUNDED) {
            if (maxRange == UNBOUNDED) {
                groupOccurrenceOK = false;
                errors.add(XmlError.forObject(XmlErrorCodes.OCCURRENCE_RANGE$MAX_LTE_MAX, new Object[] { printParticle(derivedModel), printParticle(baseModel) }, context));
            } else {
                if (maxRange.compareTo(baseModel.getMaxOccurs()) > 0) {
                    groupOccurrenceOK = false;
                    errors.add(XmlError.forObject(XmlErrorCodes.OCCURRENCE_RANGE$MAX_LTE_MAX, new Object[] { printParticle(derivedModel), printParticle(baseModel) }, context));
                }
            }
        }
        return groupOccurrenceOK;
    }

    private static BigInteger getEffectiveMaxRangeChoice(SchemaParticle derivedModel) {
        BigInteger maxRange = BigInteger.ZERO;
        BigInteger UNBOUNDED = null;
        boolean nonZeroParticleChildFound = false;
        BigInteger maxOccursInWildCardOrElement = BigInteger.ZERO;
        BigInteger maxOccursInGroup = BigInteger.ZERO;
        SchemaParticle[] particleChildren = derivedModel.getParticleChildren();
        for (int i = 0; i < particleChildren.length; i++) {
            SchemaParticle particle = particleChildren[i];
            switch(particle.getParticleType()) {
                case SchemaParticle.WILDCARD:
                case SchemaParticle.ELEMENT:
                    if (particle.getMaxOccurs() == UNBOUNDED) {
                        maxRange = UNBOUNDED;
                    } else {
                        if (particle.getIntMaxOccurs() > 0) {
                            nonZeroParticleChildFound = true;
                            if (particle.getMaxOccurs().compareTo(maxOccursInWildCardOrElement) > 0) {
                                maxOccursInWildCardOrElement = particle.getMaxOccurs();
                            }
                        }
                    }
                    break;
                case SchemaParticle.ALL:
                case SchemaParticle.SEQUENCE:
                    maxRange = getEffectiveMaxRangeAllSeq(particle);
                    if (maxRange != UNBOUNDED) {
                        if (maxRange.compareTo(maxOccursInGroup) > 0) {
                            maxOccursInGroup = maxRange;
                        }
                    }
                    break;
                case SchemaParticle.CHOICE:
                    maxRange = getEffectiveMaxRangeChoice(particle);
                    if (maxRange != UNBOUNDED) {
                        if (maxRange.compareTo(maxOccursInGroup) > 0) {
                            maxOccursInGroup = maxRange;
                        }
                    }
                    break;
            }
            if (maxRange == UNBOUNDED) {
                break;
            }
        }
        if (maxRange != UNBOUNDED) {
            if (nonZeroParticleChildFound && derivedModel.getMaxOccurs() == UNBOUNDED) {
                maxRange = UNBOUNDED;
            } else {
                maxRange = derivedModel.getMaxOccurs().multiply(maxOccursInWildCardOrElement.add(maxOccursInGroup));
            }
        }
        return maxRange;
    }

    private static BigInteger getEffectiveMaxRangeAllSeq(SchemaParticle derivedModel) {
        BigInteger maxRange = BigInteger.ZERO;
        BigInteger UNBOUNDED = null;
        boolean nonZeroParticleChildFound = false;
        BigInteger maxOccursTotal = BigInteger.ZERO;
        BigInteger maxOccursInGroup = BigInteger.ZERO;
        SchemaParticle[] particleChildren = derivedModel.getParticleChildren();
        for (int i = 0; i < particleChildren.length; i++) {
            SchemaParticle particle = particleChildren[i];
            switch(particle.getParticleType()) {
                case SchemaParticle.WILDCARD:
                case SchemaParticle.ELEMENT:
                    if (particle.getMaxOccurs() == UNBOUNDED) {
                        maxRange = UNBOUNDED;
                    } else {
                        if (particle.getIntMaxOccurs() > 0) {
                            nonZeroParticleChildFound = true;
                            maxOccursTotal = maxOccursTotal.add(particle.getMaxOccurs());
                        }
                    }
                    break;
                case SchemaParticle.ALL:
                case SchemaParticle.SEQUENCE:
                    maxRange = getEffectiveMaxRangeAllSeq(particle);
                    if (maxRange != UNBOUNDED) {
                        if (maxRange.compareTo(maxOccursInGroup) > 0) {
                            maxOccursInGroup = maxRange;
                        }
                    }
                    break;
                case SchemaParticle.CHOICE:
                    maxRange = getEffectiveMaxRangeChoice(particle);
                    if (maxRange != UNBOUNDED) {
                        if (maxRange.compareTo(maxOccursInGroup) > 0) {
                            maxOccursInGroup = maxRange;
                        }
                    }
                    break;
            }
            if (maxRange == UNBOUNDED) {
                break;
            }
        }
        if (maxRange != UNBOUNDED) {
            if (nonZeroParticleChildFound && derivedModel.getMaxOccurs() == UNBOUNDED) {
                maxRange = UNBOUNDED;
            } else {
                maxRange = derivedModel.getMaxOccurs().multiply(maxOccursTotal.add(maxOccursInGroup));
            }
        }
        return maxRange;
    }

    private static BigInteger getEffectiveMinRangeChoice(SchemaParticle derivedModel) {
        SchemaParticle[] particleChildren = derivedModel.getParticleChildren();
        if (particleChildren.length == 0) return BigInteger.ZERO;
        BigInteger minRange = null;
        for (int i = 0; i < particleChildren.length; i++) {
            SchemaParticle particle = particleChildren[i];
            switch(particle.getParticleType()) {
                case SchemaParticle.WILDCARD:
                case SchemaParticle.ELEMENT:
                    if (minRange == null || minRange.compareTo(particle.getMinOccurs()) > 0) {
                        minRange = particle.getMinOccurs();
                    }
                    break;
                case SchemaParticle.ALL:
                case SchemaParticle.SEQUENCE:
                    BigInteger mrs = getEffectiveMinRangeAllSeq(particle);
                    if (minRange == null || minRange.compareTo(mrs) > 0) {
                        minRange = mrs;
                    }
                    break;
                case SchemaParticle.CHOICE:
                    BigInteger mrc = getEffectiveMinRangeChoice(particle);
                    if (minRange == null || minRange.compareTo(mrc) > 0) {
                        minRange = mrc;
                    }
                    break;
            }
        }
        if (minRange == null) minRange = BigInteger.ZERO;
        minRange = derivedModel.getMinOccurs().multiply(minRange);
        return minRange;
    }

    private static BigInteger getEffectiveMinRangeAllSeq(SchemaParticle derivedModel) {
        BigInteger minRange = BigInteger.ZERO;
        SchemaParticle[] particleChildren = derivedModel.getParticleChildren();
        BigInteger particleTotalMinOccurs = BigInteger.ZERO;
        for (int i = 0; i < particleChildren.length; i++) {
            SchemaParticle particle = particleChildren[i];
            switch(particle.getParticleType()) {
                case SchemaParticle.WILDCARD:
                case SchemaParticle.ELEMENT:
                    particleTotalMinOccurs = particleTotalMinOccurs.add(particle.getMinOccurs());
                    break;
                case SchemaParticle.ALL:
                case SchemaParticle.SEQUENCE:
                    particleTotalMinOccurs = particleTotalMinOccurs.add(getEffectiveMinRangeAllSeq(particle));
                    break;
                case SchemaParticle.CHOICE:
                    particleTotalMinOccurs = particleTotalMinOccurs.add(getEffectiveMinRangeChoice(particle));
                    break;
            }
        }
        minRange = derivedModel.getMinOccurs().multiply(particleTotalMinOccurs);
        return minRange;
    }

    private static boolean nsSubset(SchemaParticle baseModel, SchemaParticle derivedModel, Collection errors, XmlObject context) {
        assert baseModel.getParticleType() == SchemaParticle.WILDCARD;
        assert derivedModel.getParticleType() == SchemaParticle.WILDCARD;
        boolean nsSubset = false;
        if (occurrenceRangeOK(baseModel, derivedModel, errors, context)) {
            if (baseModel.getWildcardSet().inverse().isDisjoint(derivedModel.getWildcardSet())) {
                nsSubset = true;
            } else {
                nsSubset = false;
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_NS_SUBST$WILDCARD_SUBSET, new Object[] { printParticle(derivedModel), printParticle(baseModel) }, context));
            }
        } else {
            nsSubset = false;
        }
        return nsSubset;
    }

    private static boolean nsCompat(SchemaParticle baseModel, SchemaLocalElement derivedElement, Collection errors, XmlObject context) {
        assert baseModel.getParticleType() == SchemaParticle.WILDCARD;
        boolean nsCompat = false;
        if (baseModel.getWildcardSet().contains(derivedElement.getName())) {
            if (occurrenceRangeOK(baseModel, (SchemaParticle) derivedElement, errors, context)) {
                nsCompat = true;
            } else {
            }
        } else {
            nsCompat = false;
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_DERIVATION_NS_COMPAT$WILDCARD_VALID, new Object[] { printParticle((SchemaParticle) derivedElement), printParticle(baseModel) }, context));
        }
        return nsCompat;
    }

    private static boolean nameAndTypeOK(SchemaLocalElement baseElement, SchemaLocalElement derivedElement, Collection errors, XmlObject context) {
        if (!((SchemaParticle) baseElement).canStartWithElement(derivedElement.getName())) {
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$NAME, new Object[] { printParticle((SchemaParticle) derivedElement), printParticle((SchemaParticle) baseElement) }, context));
            return false;
        }
        if (!baseElement.isNillable() && derivedElement.isNillable()) {
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$NILLABLE, new Object[] { printParticle((SchemaParticle) derivedElement), printParticle((SchemaParticle) baseElement) }, context));
            return false;
        }
        if (!occurrenceRangeOK((SchemaParticle) baseElement, (SchemaParticle) derivedElement, errors, context)) {
            return false;
        }
        if (!checkFixed(baseElement, derivedElement, errors, context)) {
            return false;
        }
        if (!checkIdentityConstraints(baseElement, derivedElement, errors, context)) {
            return false;
        }
        if (!typeDerivationOK(baseElement.getType(), derivedElement.getType(), errors, context)) {
            return false;
        }
        if (!blockSetOK(baseElement, derivedElement, errors, context)) {
            return false;
        }
        return true;
    }

    private static boolean blockSetOK(SchemaLocalElement baseElement, SchemaLocalElement derivedElement, Collection errors, XmlObject context) {
        if (baseElement.blockRestriction() && !derivedElement.blockRestriction()) {
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$DISALLOWED_SUBSTITUTIONS, new Object[] { printParticle((SchemaParticle) derivedElement), "restriction", printParticle((SchemaParticle) baseElement) }, context));
            return false;
        }
        if (baseElement.blockExtension() && !derivedElement.blockExtension()) {
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$DISALLOWED_SUBSTITUTIONS, new Object[] { printParticle((SchemaParticle) derivedElement), "extension", printParticle((SchemaParticle) baseElement) }, context));
            return false;
        }
        if (baseElement.blockSubstitution() && !derivedElement.blockSubstitution()) {
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$DISALLOWED_SUBSTITUTIONS, new Object[] { printParticle((SchemaParticle) derivedElement), "substitution", printParticle((SchemaParticle) baseElement) }, context));
            return false;
        }
        return true;
    }

    private static boolean typeDerivationOK(SchemaType baseType, SchemaType derivedType, Collection errors, XmlObject context) {
        boolean typeDerivationOK = false;
        if (baseType.isAssignableFrom(derivedType)) {
            typeDerivationOK = checkAllDerivationsForRestriction(baseType, derivedType, errors, context);
        } else {
            typeDerivationOK = false;
            errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$TYPE_VALID, new Object[] { printType(derivedType), printType(baseType) }, context));
        }
        return typeDerivationOK;
    }

    private static boolean checkAllDerivationsForRestriction(SchemaType baseType, SchemaType derivedType, Collection errors, XmlObject context) {
        boolean allDerivationsAreRestrictions = true;
        SchemaType currentType = derivedType;
        Set possibleTypes = null;
        if (baseType.getSimpleVariety() == SchemaType.UNION) possibleTypes = new HashSet(Arrays.asList(baseType.getUnionConstituentTypes()));
        while (!baseType.equals(currentType) && possibleTypes != null && !possibleTypes.contains(currentType)) {
            if (currentType.getDerivationType() == SchemaType.DT_RESTRICTION) {
                currentType = currentType.getBaseType();
            } else {
                allDerivationsAreRestrictions = false;
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$TYPE_RESTRICTED, new Object[] { printType(derivedType), printType(baseType), printType(currentType) }, context));
                break;
            }
        }
        return allDerivationsAreRestrictions;
    }

    private static boolean checkIdentityConstraints(SchemaLocalElement baseElement, SchemaLocalElement derivedElement, Collection errors, XmlObject context) {
        boolean identityConstraintsOK = true;
        SchemaIdentityConstraint[] baseConstraints = baseElement.getIdentityConstraints();
        SchemaIdentityConstraint[] derivedConstraints = derivedElement.getIdentityConstraints();
        for (int i = 0; i < derivedConstraints.length; i++) {
            SchemaIdentityConstraint derivedConstraint = derivedConstraints[i];
            if (checkForIdentityConstraintExistence(baseConstraints, derivedConstraint)) {
                identityConstraintsOK = false;
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$IDENTITY_CONSTRAINTS, new Object[] { printParticle((SchemaParticle) derivedElement), printParticle((SchemaParticle) baseElement) }, context));
                break;
            }
        }
        return identityConstraintsOK;
    }

    private static boolean checkForIdentityConstraintExistence(SchemaIdentityConstraint[] baseConstraints, SchemaIdentityConstraint derivedConstraint) {
        boolean identityConstraintExists = false;
        for (int i = 0; i < baseConstraints.length; i++) {
            SchemaIdentityConstraint baseConstraint = baseConstraints[i];
            if (baseConstraint.getName().equals(derivedConstraint.getName())) {
                identityConstraintExists = true;
                break;
            }
        }
        return identityConstraintExists;
    }

    private static boolean checkFixed(SchemaLocalElement baseModel, SchemaLocalElement derivedModel, Collection errors, XmlObject context) {
        boolean checkFixed = false;
        if (baseModel.isFixed()) {
            if (baseModel.getDefaultText().equals(derivedModel.getDefaultText())) {
                checkFixed = true;
            } else {
                errors.add(XmlError.forObject(XmlErrorCodes.PARTICLE_RESTRICTION_NAME_AND_TYPE$FIXED, new Object[] { printParticle((SchemaParticle) derivedModel), derivedModel.getDefaultText(), printParticle((SchemaParticle) baseModel), baseModel.getDefaultText() }, context));
                checkFixed = false;
            }
        } else {
            checkFixed = true;
        }
        return checkFixed;
    }

    private static boolean occurrenceRangeOK(SchemaParticle baseParticle, SchemaParticle derivedParticle, Collection errors, XmlObject context) {
        boolean occurrenceRangeOK = false;
        if (derivedParticle.getMinOccurs().compareTo(baseParticle.getMinOccurs()) >= 0) {
            if (baseParticle.getMaxOccurs() == null) {
                occurrenceRangeOK = true;
            } else {
                if (derivedParticle.getMaxOccurs() != null && baseParticle.getMaxOccurs() != null && derivedParticle.getMaxOccurs().compareTo(baseParticle.getMaxOccurs()) <= 0) {
                    occurrenceRangeOK = true;
                } else {
                    occurrenceRangeOK = false;
                    errors.add(XmlError.forObject(XmlErrorCodes.OCCURRENCE_RANGE$MAX_LTE_MAX, new Object[] { printParticle(derivedParticle), printMaxOccurs(derivedParticle.getMaxOccurs()), printParticle(baseParticle), printMaxOccurs(baseParticle.getMaxOccurs()) }, context));
                }
            }
        } else {
            occurrenceRangeOK = false;
            errors.add(XmlError.forObject(XmlErrorCodes.OCCURRENCE_RANGE$MIN_GTE_MIN, new Object[] { printParticle(derivedParticle), derivedParticle.getMinOccurs().toString(), printParticle(baseParticle), baseParticle.getMinOccurs().toString() }, context));
        }
        return occurrenceRangeOK;
    }

    private static String printParticles(List parts) {
        return printParticles((SchemaParticle[]) parts.toArray(new SchemaParticle[parts.size()]));
    }

    private static String printParticles(SchemaParticle[] parts) {
        return printParticles(parts, 0, parts.length);
    }

    private static String printParticles(SchemaParticle[] parts, int start) {
        return printParticles(parts, start, parts.length);
    }

    private static String printParticles(SchemaParticle[] parts, int start, int end) {
        StringBuffer buf = new StringBuffer(parts.length * 30);
        for (int i = start; i < end; ) {
            buf.append(printParticle(parts[i]));
            if (++i != end) buf.append(", ");
        }
        return buf.toString();
    }

    private static String printParticle(SchemaParticle part) {
        switch(part.getParticleType()) {
            case SchemaParticle.ALL:
                return "<all>";
            case SchemaParticle.CHOICE:
                return "<choice>";
            case SchemaParticle.ELEMENT:
                return "<element name=\"" + QNameHelper.pretty(part.getName()) + "\">";
            case SchemaParticle.SEQUENCE:
                return "<sequence>";
            case SchemaParticle.WILDCARD:
                return "<any>";
            default:
                return "??";
        }
    }

    private static String printMaxOccurs(BigInteger bi) {
        if (bi == null) return "unbounded";
        return bi.toString();
    }

    private static String printType(SchemaType type) {
        if (type.getName() != null) return QNameHelper.pretty(type.getName());
        return type.toString();
    }

    private static void checkSubstitutionGroups(SchemaGlobalElement[] elts) {
        StscState state = StscState.get();
        for (int i = 0; i < elts.length; i++) {
            SchemaGlobalElement elt = elts[i];
            SchemaGlobalElement head = elt.substitutionGroup();
            if (head != null) {
                SchemaType headType = head.getType();
                SchemaType tailType = elt.getType();
                XmlObject parseTree = ((SchemaGlobalElementImpl) elt)._parseObject;
                if (!headType.isAssignableFrom(tailType)) {
                    state.error(XmlErrorCodes.ELEM_PROPERTIES$SUBSTITUTION_VALID, new Object[] { QNameHelper.pretty(elt.getName()), QNameHelper.pretty(head.getName()) }, parseTree);
                } else if (head.finalExtension() && head.finalRestriction()) {
                    state.error(XmlErrorCodes.ELEM_PROPERTIES$SUBSTITUTION_FINAL, new Object[] { QNameHelper.pretty(elt.getName()), QNameHelper.pretty(head.getName()), "#all" }, parseTree);
                } else if (!headType.equals(tailType)) {
                    if (head.finalExtension() && tailType.getDerivationType() == SchemaType.DT_EXTENSION) {
                        state.error(XmlErrorCodes.ELEM_PROPERTIES$SUBSTITUTION_FINAL, new Object[] { QNameHelper.pretty(elt.getName()), QNameHelper.pretty(head.getName()), "extension" }, parseTree);
                    } else if (head.finalRestriction() && tailType.getDerivationType() == SchemaType.DT_RESTRICTION) {
                        state.error(XmlErrorCodes.ELEM_PROPERTIES$SUBSTITUTION_FINAL, new Object[] { QNameHelper.pretty(elt.getName()), QNameHelper.pretty(head.getName()), "restriction" }, parseTree);
                    }
                }
            }
        }
    }
}
