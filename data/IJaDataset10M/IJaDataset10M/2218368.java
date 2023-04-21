package net.sf.joafip.store.entity.conversion.input;

import java.util.LinkedList;
import java.util.List;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.store.entity.classinfo.ClassInfo;
import net.sf.joafip.store.entity.classinfo.FieldInfo;
import net.sf.joafip.store.entity.conversion.ClassNameAndIdentifierKey;

/**
 * conversion definition ( conversion file definition line model )
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class ConversionDefEntry extends ClassNameAndIdentifierKey {

    private final int toIdentifier;

    private final ClassInfo replacementClass;

    /** original field information, null if field list definition */
    private final FieldInfo originalFieldInfo;

    /** original field index, -1 if undefined */
    private final int originalFieldIndex;

    /** replacement field information */
    private final FieldInfo replacementFieldInfo;

    /** replacement field index, -1 if undefined */
    private final int replacementFieldIndex;

    /** list of field information of class */
    private final List<FieldInfo> fieldList;

    private final ClassInfo converterClass;

    private final ClassInfo staticConverterClass;

    /**
	 * create a field list definition
	 * 
	 * @param classInfo
	 *            for the class
	 * @param fromIdentifier
	 *            from identifier
	 * @param toIdentifier
	 *            to identifier
	 */
    public ConversionDefEntry(final ClassInfo classInfo, final int fromIdentifier, final int toIdentifier) {
        super(classInfo, fromIdentifier);
        this.toIdentifier = toIdentifier;
        this.replacementClass = ClassInfo.NULL;
        this.fieldList = new LinkedList<FieldInfo>();
        originalFieldInfo = null;
        originalFieldIndex = -1;
        replacementFieldInfo = null;
        replacementFieldIndex = -1;
        converterClass = ClassInfo.NULL;
        staticConverterClass = ClassInfo.NULL;
    }

    /**
	 * create a class conversion definition
	 * 
	 * @param classInfo
	 *            original class
	 * @param fromIdentifier
	 *            from identifier
	 * @param replacementClass
	 *            replacement class
	 * @param toIdentifier
	 *            to identifier
	 * @param converterClass
	 *            class of converter
	 */
    public ConversionDefEntry(final ClassInfo classInfo, final int fromIdentifier, final ClassInfo replacementClass, final int toIdentifier, final ClassInfo converterClass, final ClassInfo staticConverterClass) {
        super(classInfo, fromIdentifier);
        this.toIdentifier = toIdentifier;
        this.replacementClass = replacementClass;
        this.converterClass = converterClass;
        this.staticConverterClass = staticConverterClass;
        this.fieldList = new LinkedList<FieldInfo>();
        originalFieldInfo = null;
        originalFieldIndex = -1;
        replacementFieldInfo = null;
        replacementFieldIndex = -1;
    }

    /**
	 * create a field conversion definition
	 * 
	 * @param classInfo
	 *            original class
	 * @param fromIdentifier
	 *            from identifier
	 * @param replacementClass
	 *            replacement class
	 * @param toIdentifier
	 *            to identifier
	 * @param originalFieldInfo
	 *            original field
	 * @param replacementFieldInfo
	 *            replacement field
	 */
    public ConversionDefEntry(final ClassInfo classInfo, final int fromIdentifier, final ClassInfo replacementClass, final int toIdentifier, final FieldInfo originalFieldInfo, final int originalFieldIndex, final FieldInfo replacementFieldInfo, final int replacementFieldIndex) {
        super(classInfo, fromIdentifier);
        this.toIdentifier = toIdentifier;
        this.replacementClass = replacementClass;
        this.originalFieldInfo = originalFieldInfo;
        this.originalFieldIndex = originalFieldIndex;
        this.replacementFieldInfo = replacementFieldInfo;
        this.replacementFieldIndex = replacementFieldIndex;
        this.fieldList = new LinkedList<FieldInfo>();
        converterClass = ClassInfo.NULL;
        staticConverterClass = ClassInfo.NULL;
    }

    public int getToIdentifier() {
        return toIdentifier;
    }

    public ClassInfo getReplacementClass() {
        return replacementClass;
    }

    public void addToFieldList(final FieldInfo fieldInfo) {
        fieldList.add(fieldInfo);
    }

    public ClassInfo getConverterClass() {
        return converterClass;
    }

    public ClassInfo getStaticConverterClass() {
        return staticConverterClass;
    }

    public List<FieldInfo> getFieldList() {
        return fieldList;
    }

    public FieldInfo getOriginalFieldInfo() {
        return originalFieldInfo;
    }

    /**
	 * 
	 * @return original field index, -1 if undefined
	 */
    public int getOriginalFieldIndex() {
        return originalFieldIndex;
    }

    public FieldInfo getReplacementFieldInfo() {
        return replacementFieldInfo;
    }

    /**
	 * 
	 * @return replacement field index, -1 if undefined
	 */
    public int getReplacementFieldIndex() {
        return replacementFieldIndex;
    }

    public boolean isFieldReplacementDef() {
        return originalFieldInfo != null;
    }

    public boolean isClassFieldListDef() {
        return fieldList != null;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ConversionDefEntry [");
        builder.append(super.toString());
        builder.append(", replacementClass=");
        builder.append(replacementClass);
        builder.append(", toIdentifier=");
        builder.append(toIdentifier);
        builder.append(", converterClass=");
        builder.append(converterClass);
        builder.append(", fieldList=");
        builder.append(fieldList);
        builder.append(", originalFieldInfo=");
        builder.append(originalFieldInfo);
        builder.append(", replacementFieldInfo=");
        builder.append(replacementFieldInfo);
        builder.append("]");
        return builder.toString();
    }
}
