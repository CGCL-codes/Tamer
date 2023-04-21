package yaw.cjef.transformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import yaw.cjef.templates.bu.ModelBase;
import yaw.cjef.templates.bu.model.addMember.EnumMappingType;
import yaw.cjef.templates.bu.model.addMember.EnumMemberType;
import yaw.cjef.templates.bu.model.addMember.ModelMember;
import yaw.cjef.templates.bu.model.addMember.ModelMemberClassMapEntry;
import yaw.cjef.templates.bu.model.addMember.ModelMemberCollection;
import yaw.cjef.templates.bu.model.addMember.ModelMemberForwardReference;
import yaw.cjef.templates.bu.model.addMember.ModelMemberReference;
import yaw.cjef.templates.bu.model.addMember.ModelMemberReverseReference;
import yaw.cjef.templates.bu.model.addMember.ModelMemberSearchPart;
import yaw.cjef.templates.bu.model.addMember.ModelMemberSubCollection;
import yaw.cjef.templates.bu.model.addMember.ModelMemberSubObject;
import yaw.cjef.templates.bu.model.addMember.ModelMemberVar;
import yaw.cjef.templates.bu.model.createModel.EnumPersistence;
import yaw.cjef.util.TMMWizard;
import yaw.core.mda.ModelTransformerAdapter;
import yaw.core.mda.TransformationFailed;
import yaw.core.uml.metamodel.uml.AggregationType;
import yaw.core.uml.metamodel.uml.UMLAssociation;
import yaw.core.uml.metamodel.uml.UMLAssociationEnd;
import yaw.core.uml.metamodel.uml.UMLAttribute;
import yaw.core.uml.metamodel.uml.UMLClass;
import yaw.core.uml.metamodel.uml.UMLType;
import yaw.core.uml.metamodel.uml.VisibilityType;
import yaw.core.uml.migration.ModelProfile;
import yaw.core.uml.util.UMLUtil;

public abstract class TransformModelBase extends ModelTransformerAdapter<UMLClass> {

    public static String getSubPackageName(TMMWizard model, String packageName) {
        String projectPackage = model.lookupProjectPackage(packageName);
        String subPackage = packageName.replace(projectPackage, "");
        if (subPackage.length() > 0 && subPackage.charAt(0) == '.') return subPackage.substring(1); else return subPackage;
    }

    protected void transformMember(ModelBase model, UMLAssociation ignore) {
        List<UMLAttribute> addDelayed = new ArrayList<UMLAttribute>();
        Iterator<UMLAssociation> iterAssoc = umlType.getAssociations();
        while (iterAssoc.hasNext()) {
            UMLAssociation next = iterAssoc.next();
            if (ignore != next) addAssoc(next, model, addDelayed);
        }
        Iterator<UMLAttribute> iterAttr = addDelayed.iterator();
        while (iterAttr.hasNext()) {
            addMember(iterAttr.next(), model);
        }
        iterAttr = umlType.getAttributes();
        while (iterAttr.hasNext()) {
            addMember(iterAttr.next(), model);
        }
    }

    protected UMLAttribute findSubClassMapEnum(UMLClass umlClass) {
        Iterator<UMLAttribute> it = umlClass.getAttributes();
        while (it.hasNext()) {
            UMLAttribute attr = it.next();
            if (attr.getStereotype() != null && ModelProfile.MEMBER_STEREOTYPE_CLASSMAPVALUE.equals(attr.getStereotype())) {
                return attr;
            }
        }
        return null;
    }

    protected static EnumPersistence mapPersistence(UMLClass umlClass) {
        if (ModelProfile.CLASS_STEREOTYPE_EMBEDDABLE.equals(umlClass.getStereotype())) return EnumPersistence.PERSISTENTEMBEDDED;
        String persType = umlClass.getTaggedValue(ModelProfile.TVN_CLASS_PERSISTENCE);
        if (persType == null) return EnumPersistence.NULL; else if (ModelProfile.CLASS_TV_PERSISTENCE_PARENT.equals(persType)) return EnumPersistence.PERSISTENTPARENT; else if (ModelProfile.CLASS_TV_PERSISTENCE_PERSISTENT.equals(persType)) return EnumPersistence.PERSISTENT; else if (ModelProfile.CLASS_TV_PERSISTENCE_TRANSIENT.equals(persType)) return EnumPersistence.TRANSITORY; else return EnumPersistence.NULL;
    }

    protected static String[] splitComment(String description) {
        if (description == null) return new String[] { "", "" };
        int pos = description.indexOf('\n');
        if (pos == -1) pos = description.indexOf('.');
        if (pos == -1) return new String[] { "", description }; else return new String[] { description.substring(0, pos) + ".", description.substring(pos + 1) };
    }

    protected void addAssoc(UMLAssociation modelAssoc, ModelBase model, List<UMLAttribute> addDelayed) {
        if (modelAssoc.getEndPoint1().getSupplier().equals(modelAssoc.getEndPoint2().getSupplier())) {
            if (!modelAssoc.getEndPoint1().getSupplier().equals(umlType.getURI())) throw new TransformationFailed("Invalid association");
            makeAssoc(modelAssoc, modelAssoc.getEndPoint1(), modelAssoc.getEndPoint2(), model, addDelayed);
            makeAssoc(modelAssoc, modelAssoc.getEndPoint2(), modelAssoc.getEndPoint1(), model, addDelayed);
        } else if (modelAssoc.getEndPoint1().getSupplier().equals(umlType.getURI())) {
            makeAssoc(modelAssoc, modelAssoc.getEndPoint1(), modelAssoc.getEndPoint2(), model, addDelayed);
        } else if (modelAssoc.getEndPoint2().getSupplier().equals(umlType.getURI())) {
            makeAssoc(modelAssoc, modelAssoc.getEndPoint2(), modelAssoc.getEndPoint1(), model, addDelayed);
        } else throw new TransformationFailed("Invalid association");
    }

    protected void makeAssoc(UMLAssociation modelAssoc, UMLAssociationEnd ep1, UMLAssociationEnd ep2, ModelBase model, List<UMLAttribute> addDelayed) {
        if (ep1.getName() == null) return;
        ModelMemberReference member = null;
        if (isReference(ep1, ep2) || isSubObjectCollectionReference(ep1, ep2)) {
            UMLAssociationEnd ep = ep1;
            ep1 = ep2;
            ep2 = ep;
            member = new ModelMemberForwardReference();
        } else if (isSubObject(ep1, ep2)) {
            member = new ModelMemberSubObject();
        } else if (isSearchPart(ep1, ep2)) {
            member = new ModelMemberSearchPart();
        } else if (isEnumeration(ep1, ep2)) {
            UMLAttribute attr = UMLUtil.convertAssoc2Attr(ep1, ep2);
            if (attr.getStereotype() == null) attr.setStereotype(ModelProfile.MEMBER_STEREOTYPE_ATTRIBUTE);
            addDelayed.add(attr);
            return;
        } else if (supportsAllAssocTypes()) {
            if (isReverse(ep1, ep2)) {
                member = new ModelMemberReverseReference();
            } else if (isCollection(ep1, ep2)) {
                member = new ModelMemberCollection();
            } else if (isSubCollection(ep1, ep2)) {
                member = new ModelMemberSubCollection();
            } else {
                reportMissingAssoc(modelAssoc);
                return;
            }
        } else {
            ctx.reportWarning("Unsupported association: " + modelAssoc.toString());
            return;
        }
        member.init(model);
        member.setSubsystemMaster(model.lookupProjectPackage(ep1.getSupplier().getPackage()));
        member.setSubPackageNameMaster(getSubPackageName(model, ep1.getSupplier().getPackage()));
        member.setModelNameMaster(ep1.getSupplier().getName());
        member.setMemberNameMaster(ep1.getName());
        member.setMemberDisplayNameMaster(ep1.getTaggedValue(ModelProfile.TVN_MEMBER_DISPLAYNAME));
        member.setMemberTypeMaster(mapMemberType(ep1, ep2));
        member.setMasterCollectionMinSize(ep1.getMinOccur());
        member.setMasterCollectionMaxSize(ep1.getMaxOccur());
        member.setMappingTypeMaster(mapMappingType(ep1, ep2));
        member.setSQLNameMaster(ep1.getTaggedValue(ModelProfile.TVN_MEMBER_COLUMNNAME));
        member.setCommentMaster(ep1.getDescription());
        member.setGenAccessCode(ep1.getVisibility() != null && ep1.getVisibility() == VisibilityType.PUBLIC);
        member.setSubsystemDetail(model.lookupProjectPackage(ep2.getSupplier().getPackage()));
        member.setSubPackageNameDetail(getSubPackageName(model, ep2.getSupplier().getPackage()));
        member.setModelNameDetail(ep2.getSupplier().getName());
        member.setMemberNameDetail(ep2.getName());
        member.setMemberDisplayNameDetail(ep2.getTaggedValue(ModelProfile.TVN_MEMBER_DISPLAYNAME));
        member.setMemberTypeDetail(mapMemberType(ep2, ep1));
        member.setCommentDetail(ep2.getDescription());
        member.setMappingTypeDetail(mapMappingType(ep2, ep1));
        member.setSQLNameDetail(ep2.getTaggedValue(ModelProfile.TVN_MEMBER_COLUMNNAME));
        member.setNullAllowedDetail(ep2.isOptional());
        member.setGenAccessCodeOverride(ep1.isDerived() || ep2.isDerived());
        model.member.add(member);
    }

    protected boolean supportsAllAssocTypes() {
        return true;
    }

    protected void reportMissingAssoc(UMLAssociation assoc) {
        UMLType sup1 = umlType.getModel().lookupType(assoc.getEndPoint1().getSupplier());
        UMLType sup2 = umlType.getModel().lookupType(assoc.getEndPoint2().getSupplier());
        StringBuilder buff = new StringBuilder();
        buff.append("Unprecise association - not generated:\n");
        buff.append(assoc.getEndPoint1().getSupplier());
        if (sup1 == null) buff.append("???");
        buff.append(" -> ");
        buff.append(assoc.getEndPoint2().getSupplier());
        if (sup2 == null) buff.append("???");
        buff.append("\n\n");
        ctx.reportWarning(buff.toString());
    }

    protected boolean isReference(UMLAssociationEnd ep1, UMLAssociationEnd ep2) {
        return isModelTarget(ep2) && ep1.getMaxOccur() != null && ep1.getMaxOccur() == 1 && ep1.getStereotype() == null;
    }

    protected boolean isCollection(UMLAssociationEnd ep1, UMLAssociationEnd ep2) {
        return isModelTarget(ep2) && (ep1.getMaxOccur() == null || ep1.getMaxOccur() > 1);
    }

    protected boolean isReverse(UMLAssociationEnd ep1, UMLAssociationEnd ep2) {
        return isModelTarget(ep2) && ep1.getStereotype() != null && ModelProfile.MEMBER_STEREOTYPE_REVERSE.equals(ep1.getStereotype());
    }

    /**
   * Ist das eine Subobjekt-Referenz ?<br>
   * Wird auf ein Subobjekt verweisen muss es als Subobjekt-Referent verwendet werden.
   * 
   * @param ep1 Endpunkt in aktueller Klasse
   * @param ep2 Endpunkt in Zielklasse
   * @return true wenn eine Subobjekt-Referenz
   */
    protected boolean isSubObject(UMLAssociationEnd ep1, UMLAssociationEnd ep2) {
        return isEmbeddedTarget(ep2) && ep1.getMaxOccur() != null && ep1.getMaxOccur() == 1;
    }

    /**
   * Ist das eine Master-Referenz einer Collection auf ein Subobjekt ?<br>
   * Nötig für das Erkennen, wie man auf ein Subobjekt verweist. Eine Collection wird erkannt, wenn es ein
   * Range wie (0..*) oder ähnlich definiert.
   * 
   * @param ep Endpunkt
   * @return true, wenn die Masterreferenz für eine Collection
   */
    private boolean isSubObjectCollectionReference(UMLAssociationEnd ep1, UMLAssociationEnd ep2) {
        return isEmbeddedTarget(ep2) && ep1.getMaxOccur() != null && ep1.getMaxOccur() == 1 && isACollection(ep2);
    }

    private boolean isACollection(UMLAssociationEnd ep) {
        return ep.getMinOccur() != null && (ep.getMaxOccur() == null || ep.getMaxOccur() > 1);
    }

    protected boolean isSubCollection(UMLAssociationEnd ep1, UMLAssociationEnd ep2) {
        return isEmbeddedTarget(ep2) && ep1.getMaxOccur() != null && ep1.getMaxOccur() > 1;
    }

    protected boolean isSearchPart(UMLAssociationEnd ep1, UMLAssociationEnd ep2) {
        return isSearchPartTarget(ep2) && ep1.getMaxOccur() != null && ep1.getMaxOccur() == 1;
    }

    protected boolean isEnumeration(UMLAssociationEnd ep1, UMLAssociationEnd ep2) {
        return isEnumTarget(ep2) && ep1.getMaxOccur() != null && ep1.getMaxOccur() == 1;
    }

    protected boolean isModelTarget(UMLAssociationEnd ep) {
        return checkStereotype(ep, ModelProfile.CLASS_STEREOTYPE_MODEL);
    }

    protected boolean isEmbeddedTarget(UMLAssociationEnd ep) {
        return checkStereotype(ep, ModelProfile.CLASS_STEREOTYPE_EMBEDDABLE);
    }

    protected boolean isSearchPartTarget(UMLAssociationEnd ep) {
        return checkStereotype(ep, ModelProfile.CLASS_STEREOTYPE_SEARCHPART);
    }

    protected boolean isEnumTarget(UMLAssociationEnd ep) {
        return checkStereotype(ep, ModelProfile.CLASS_STEREOTYPE_ENUMERATION);
    }

    protected boolean checkStereotype(UMLAssociationEnd ep, String classStereotypeModel) {
        UMLType target = this.umlType.getModel().lookupType(ep.getSupplier());
        if (target == null) return false;
        String type = target.getStereotype();
        if (type == null) return false;
        return classStereotypeModel.equals(type);
    }

    protected EnumMappingType mapMappingType(UMLAssociationEnd ep1, UMLAssociationEnd ep2) {
        if (isEmbeddedTarget(ep2) && !isACollection(ep2)) return EnumMappingType.EMBEDDED;
        return EnumMappingType.NULL;
    }

    protected EnumMemberType mapMemberType(UMLAssociationEnd ep1, UMLAssociationEnd ep2) {
        if (isCollection(ep1, ep2)) {
            if (ep1.getAggregation() != null) {
                if (ep1.getAggregation() == AggregationType.COMPOSITE) return EnumMemberType.COLLECTION_DEL_CASCADING; else if (ep1.getAggregation() == AggregationType.SHARED) return EnumMemberType.COLLECTION_SET_NULL;
            }
            return EnumMemberType.COLLECTION;
        } else if (isReference(ep1, ep2)) {
            if (ep1.getAggregation() != null) {
                if (ep1.getAggregation() == AggregationType.COMPOSITE) return EnumMemberType.REFERENCE_DEL_CASCADE;
            }
            return EnumMemberType.REFERENCE;
        } else if (isReverse(ep1, ep2)) {
            if (ep1.getAggregation() != null) {
                if (ep1.getAggregation() == AggregationType.COMPOSITE) return EnumMemberType.REVERSE_REFERENCE_DEL_CASCADING; else if (ep1.getAggregation() == AggregationType.SHARED) return EnumMemberType.REVERSE_REFERENCE_SET_NULL;
            }
            return EnumMemberType.REVERSE_REFERENCE;
        } else if (isSubObject(ep1, ep2)) {
            return EnumMemberType.SUBOBJECT;
        } else if (isSubCollection(ep1, ep2)) {
            return EnumMemberType.SUB_COLLECTION;
        } else return EnumMemberType.NULL;
    }

    protected void addMember(UMLAttribute attr, ModelBase model) {
        if (attr.getStereotype() == null) return;
        ModelMember modelMember;
        if (ModelProfile.MEMBER_STEREOTYPE_CLASSMAPENUM.equals(attr.getStereotype())) {
            modelMember = makeMemberClassMapEnum(attr, model);
        } else if (ModelProfile.MEMBER_STEREOTYPE_ATTRIBUTE.equals(attr.getStereotype())) {
            UMLType targetType = this.umlType.getModel().lookupType(attr.getType());
            if (targetType != null && targetType.getStereotype() != null) {
                if (ModelProfile.CLASS_STEREOTYPE_MODEL.equals(targetType.getStereotype())) {
                    addAssoc(UMLUtil.convertAttr2Assoc(attr, umlType.getURI()), model, null);
                    return;
                } else if (ModelProfile.CLASS_STEREOTYPE_EMBEDDABLE.equals(targetType.getStereotype())) {
                    addAssoc(UMLUtil.convertAttr2Assoc(attr, umlType.getURI()), model, null);
                    return;
                } else if (ModelProfile.CLASS_STEREOTYPE_ENUMERATION.equals(targetType.getStereotype())) modelMember = makeMemberEnum(attr, model); else modelMember = makeMemberVar(attr, model);
            } else if (attr.getType().getPackage().equalsIgnoreCase("cJEF")) modelMember = makeMemberEnum(attr, model); else modelMember = makeMemberVar(attr, model);
        } else return;
        modelMember.memberName = attr.getName();
        modelMember.notNull = attr.isMandatory();
        modelMember.setMemberDisplayName(attr.getTaggedValue(ModelProfile.TVN_MEMBER_DISPLAYNAME));
        modelMember.setSqlName(attr.getTaggedValue(ModelProfile.TVN_MEMBER_COLUMNNAME));
        modelMember.setComment(attr.getDescription());
        if (modelMember.memberType.equals("String")) {
            modelMember.initMember = "TMStringType.Null";
        } else if (modelMember.memberType.startsWith("TM")) {
            modelMember.initMember = modelMember.memberType + ".Null";
        }
        if (isGenAccessCodeForPrivateMember()) {
            modelMember.setGenAccessCode(true);
        } else {
            modelMember.setGenAccessCode(attr.getVisibility() == VisibilityType.PUBLIC);
        }
        model.member.add(modelMember);
    }

    protected ModelMember makeMemberVar(UMLAttribute attr, ModelBase model) {
        ModelMemberVar var = new ModelMemberVar();
        var.init(model);
        var.memberType = attr.getType().getName();
        Integer columnLength = getColumnLength(attr);
        var.sqlType = getSQLTypeFromJavaType(attr.getType().getName(), columnLength);
        if (columnLength == null) {
            if (attr.getType().getName().equals("String")) var.length = 20;
        } else var.length = columnLength;
        return var;
    }

    protected ModelMember makeMemberClassMapEnum(UMLAttribute attr, ModelBase model) {
        ModelMemberClassMapEntry cme = new ModelMemberClassMapEntry();
        cme.init(model);
        cme.memberType = model.getEnumPraefix(cme.subsystemEnum) + attr.getType().getName();
        cme.sqlType = "Integer";
        return cme;
    }

    private ModelMember makeMemberEnum(UMLAttribute attr, ModelBase model) {
        ModelMemberVar var = new ModelMemberVar();
        var.init(model);
        var.sqlType = "Integer";
        var.isEnumeration = true;
        var.memberType = attr.getType().getName();
        String enumPackage = attr.getType().getPackage();
        if (enumPackage.equalsIgnoreCase("cJEF")) {
            if (!var.memberType.startsWith("CT")) var.memberType = "CT" + var.memberType;
            var.subsystemEnum = null;
            var.setCJEFType(true);
        } else if (!var.memberType.startsWith("CT")) {
            String enumProject = model.lookupProjectPackage(enumPackage);
            if (model.getProjectPackage().equalsIgnoreCase(enumProject)) {
                var.memberType = model.getEnumPraefix() + var.memberType;
            } else {
                var.subsystemEnum = enumProject;
                var.memberType = model.getEnumPraefix(var.subsystemEnum) + var.memberType;
            }
        }
        var.initMember = var.memberType + ".Null";
        return var;
    }

    protected Integer getColumnLength(UMLAttribute attr) {
        String value = attr.getTaggedValue(ModelProfile.TVN_MEMBER_COLUMNLENGTH);
        if (value == null) return null;
        try {
            return Integer.decode(value);
        } catch (NumberFormatException e) {
            ctx.reportWarning("Unerwarter Wert für " + ModelProfile.TVN_MEMBER_COLUMNLENGTH + "=" + value);
            return null;
        }
    }

    protected String getSQLTypeFromJavaType(String javaType, Integer maxLength) {
        if (javaType == null) {
            throw new TransformationFailed("Typangabe des Attributes fehlt.");
        } else if (javaType.equals("String")) {
            if (maxLength != null && maxLength == -1) return "Clob"; else return "Varchar";
        } else if (javaType.equals("TMBinary")) return "Blob"; else if (javaType.equals("int") || javaType.equals("Integer")) return "Integer"; else if (javaType.equals("long") || javaType.equals("Long")) return "Bigint"; else if (javaType.equals("Boolean") || javaType.equals("boolean")) return "Bit"; else if (javaType.equals("TMMoney")) return "Numeric"; else if (javaType.equals("TMDate")) return "Date"; else if (javaType.equals("TMTime")) return "Time"; else if (javaType.equals("TMDateTime")) return "Timestamp"; else if (javaType.equals("BigDecimal")) return "Numeric"; else if (javaType.equals("Double") || javaType.equals("double")) return "Double"; else if (javaType.equals("Float") || javaType.equals("float")) return "Float";
        throw new TransformationFailed("Für den Java-Type: '" + javaType + "' kann kein SQL-Type ermittelt werden. " + "Bitte überprüfen Sie die Schreibweise: (int,Integer,long,Long,Boolean,boolean,String,float,Float,double,Double,BigDecimal,TMMoney,TMDate,TMTime,TMDateTime)");
    }

    protected boolean isGenAccessCodeForPrivateMember() {
        String result = this.ctx.getActiveProject().getWizardProperties().getString("DontGenerateAccessCodeForPrivateMembers");
        if (result == null || result.equalsIgnoreCase("false")) return true;
        if (Boolean.valueOf(result)) return false;
        String[] stereotype = getRelatedStereotype();
        for (String string : stereotype) {
            if (result.contains(string)) return false;
        }
        return true;
    }
}
