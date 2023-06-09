package proguard.obfuscate;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.visitor.*;
import proguard.util.*;
import java.util.*;

/**
 * This ClassFileVisitor marks all attributes that should be kept in the classes
 * it visits.
 *
 * @see AttributeShrinker
 *
 * @author Eric Lafortune
 */
public class AttributeUsageMarker implements ClassFileVisitor, MemberInfoVisitor, AttrInfoVisitor, InnerClassesInfoVisitor {

    private static final Object USED = new Object();

    private boolean keepAllAttributes;

    private boolean keepAllUnknownAttributes;

    private boolean keepAllKnownAttributes;

    private StringMatcher keepAttributes;

    private boolean keepInnerClassNameAttribute;

    private boolean keepEnclosingMethodAttribute;

    private boolean keepLineNumberTableAttribute;

    private boolean keepLocalVariableTableAttribute;

    private boolean keepLocalVariableTypeTableAttribute;

    private boolean keepSourceFileAttribute;

    private boolean keepSourceDirAttribute;

    private boolean keepDeprecatedAttribute;

    private boolean keepSyntheticAttribute;

    private boolean keepSignatureAttribute;

    private boolean keepRuntimeVisibleAnnotationsAttribute;

    private boolean keepRuntimeInvisibleAnnotationsAttribute;

    private boolean keepRuntimeVisibleParameterAnnotationsAttribute;

    private boolean keepRuntimeInvisibleParameterAnnotationsAttribute;

    private boolean keepAnnotationDefaultAttribute;

    /**
     * Specifies to keep all optional attributes.
     */
    public void setKeepAllAttributes() {
        keepAllAttributes = true;
    }

    /**
     * Specifies to keep all unknown attributes.
     */
    public void setKeepAllUnknownAttributes() {
        keepAllUnknownAttributes = true;
    }

    /**
     * Specifies to keep all known attributes.
     */
    public void setKeepAllKnownAttributes() {
        keepAllKnownAttributes = true;
    }

    /**
     * Specifies to keep optional attributes with the given names. The attribute
     * names may contain "*" or "?" wildcards, and they may be preceded by the
     * "!" negator.
     */
    public void setKeepAttributes(List attributeNames) {
        keepAttributes = new BasicListMatcher(attributeNames);
        keepInnerClassNameAttribute = keepAttributes.matches(ClassConstants.ATTR_InnerClasses);
        keepEnclosingMethodAttribute = keepAttributes.matches(ClassConstants.ATTR_EnclosingMethod);
        keepLineNumberTableAttribute = keepAttributes.matches(ClassConstants.ATTR_LineNumberTable);
        keepLocalVariableTableAttribute = keepAttributes.matches(ClassConstants.ATTR_LocalVariableTable);
        keepLocalVariableTypeTableAttribute = keepAttributes.matches(ClassConstants.ATTR_LocalVariableTypeTable);
        keepSourceFileAttribute = keepAttributes.matches(ClassConstants.ATTR_SourceFile);
        keepSourceDirAttribute = keepAttributes.matches(ClassConstants.ATTR_SourceDir);
        keepDeprecatedAttribute = keepAttributes.matches(ClassConstants.ATTR_Deprecated);
        keepSyntheticAttribute = keepAttributes.matches(ClassConstants.ATTR_Synthetic);
        keepSignatureAttribute = keepAttributes.matches(ClassConstants.ATTR_Signature);
        keepRuntimeVisibleAnnotationsAttribute = keepAttributes.matches(ClassConstants.ATTR_RuntimeVisibleAnnotations);
        keepRuntimeInvisibleAnnotationsAttribute = keepAttributes.matches(ClassConstants.ATTR_RuntimeInvisibleAnnotations);
        keepRuntimeVisibleParameterAnnotationsAttribute = keepAttributes.matches(ClassConstants.ATTR_RuntimeVisibleParameterAnnotations);
        keepRuntimeInvisibleParameterAnnotationsAttribute = keepAttributes.matches(ClassConstants.ATTR_RuntimeInvisibleParameterAnnotations);
        keepAnnotationDefaultAttribute = keepAttributes.matches(ClassConstants.ATTR_AnnotationDefault);
    }

    public void visitProgramClassFile(ProgramClassFile programClassFile) {
        programClassFile.fieldsAccept(this);
        programClassFile.methodsAccept(this);
        programClassFile.attributesAccept(this);
    }

    public void visitLibraryClassFile(LibraryClassFile libraryClassFile) {
    }

    public void visitProgramFieldInfo(ProgramClassFile programClassFile, ProgramFieldInfo programFieldInfo) {
        visitMemberInfo(programClassFile, programFieldInfo);
    }

    public void visitProgramMethodInfo(ProgramClassFile programClassFile, ProgramMethodInfo programMethodInfo) {
        visitMemberInfo(programClassFile, programMethodInfo);
    }

    private void visitMemberInfo(ProgramClassFile programClassFile, ProgramMemberInfo programMemberInfo) {
        programMemberInfo.attributesAccept(programClassFile, this);
    }

    public void visitLibraryFieldInfo(LibraryClassFile libraryClassFile, LibraryFieldInfo libraryFieldInfo) {
    }

    public void visitLibraryMethodInfo(LibraryClassFile libraryClassFile, LibraryMethodInfo libraryMethodInfo) {
    }

    public void visitUnknownAttrInfo(ClassFile classFile, UnknownAttrInfo unknownAttrInfo) {
        if (keepAllAttributes || keepAllUnknownAttributes || (keepAttributes != null && keepAttributes.matches(unknownAttrInfo.getAttributeName(classFile)))) {
            markAsUsed(unknownAttrInfo);
        }
    }

    public void visitInnerClassesAttrInfo(ClassFile classFile, InnerClassesAttrInfo innerClassesAttrInfo) {
        markAsUsed(innerClassesAttrInfo);
        if (!keepAllAttributes && !keepAllKnownAttributes && !keepInnerClassNameAttribute) {
            innerClassesAttrInfo.innerClassEntriesAccept(classFile, this);
        }
    }

    public void visitEnclosingMethodAttrInfo(ClassFile classFile, EnclosingMethodAttrInfo enclosingMethodAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepEnclosingMethodAttribute) {
            markAsUsed(enclosingMethodAttrInfo);
        }
    }

    public void visitConstantValueAttrInfo(ClassFile classFile, FieldInfo fieldInfo, ConstantValueAttrInfo constantValueAttrInfo) {
        markAsUsed(constantValueAttrInfo);
    }

    public void visitExceptionsAttrInfo(ClassFile classFile, MethodInfo methodInfo, ExceptionsAttrInfo exceptionsAttrInfo) {
        markAsUsed(exceptionsAttrInfo);
    }

    public void visitCodeAttrInfo(ClassFile classFile, MethodInfo methodInfo, CodeAttrInfo codeAttrInfo) {
        markAsUsed(codeAttrInfo);
        codeAttrInfo.attributesAccept(classFile, methodInfo, this);
    }

    public void visitLineNumberTableAttrInfo(ClassFile classFile, MethodInfo methodInfo, CodeAttrInfo codeAttrInfo, LineNumberTableAttrInfo lineNumberTableAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepLineNumberTableAttribute) {
            markAsUsed(lineNumberTableAttrInfo);
        }
    }

    public void visitLocalVariableTableAttrInfo(ClassFile classFile, MethodInfo methodInfo, CodeAttrInfo codeAttrInfo, LocalVariableTableAttrInfo localVariableTableAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepLocalVariableTableAttribute) {
            markAsUsed(localVariableTableAttrInfo);
        }
    }

    public void visitLocalVariableTypeTableAttrInfo(ClassFile classFile, MethodInfo methodInfo, CodeAttrInfo codeAttrInfo, LocalVariableTypeTableAttrInfo localVariableTypeTableAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepLocalVariableTypeTableAttribute) {
            markAsUsed(localVariableTypeTableAttrInfo);
        }
    }

    public void visitSourceFileAttrInfo(ClassFile classFile, SourceFileAttrInfo sourceFileAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepSourceFileAttribute) {
            markAsUsed(sourceFileAttrInfo);
        }
    }

    public void visitSourceDirAttrInfo(ClassFile classFile, SourceDirAttrInfo sourceDirAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepSourceDirAttribute) {
            markAsUsed(sourceDirAttrInfo);
        }
    }

    public void visitDeprecatedAttrInfo(ClassFile classFile, DeprecatedAttrInfo deprecatedAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepDeprecatedAttribute) {
            markAsUsed(deprecatedAttrInfo);
        }
    }

    public void visitSyntheticAttrInfo(ClassFile classFile, SyntheticAttrInfo syntheticAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepSyntheticAttribute) {
            markAsUsed(syntheticAttrInfo);
        }
    }

    public void visitSignatureAttrInfo(ClassFile classFile, SignatureAttrInfo signatureAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepSignatureAttribute) {
            markAsUsed(signatureAttrInfo);
        }
    }

    public void visitRuntimeVisibleAnnotationAttrInfo(ClassFile classFile, RuntimeVisibleAnnotationsAttrInfo runtimeVisibleAnnotationsAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepRuntimeVisibleAnnotationsAttribute) {
            markAsUsed(runtimeVisibleAnnotationsAttrInfo);
        }
    }

    public void visitRuntimeInvisibleAnnotationAttrInfo(ClassFile classFile, RuntimeInvisibleAnnotationsAttrInfo runtimeInvisibleAnnotationsAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepRuntimeInvisibleAnnotationsAttribute) {
            markAsUsed(runtimeInvisibleAnnotationsAttrInfo);
        }
    }

    public void visitRuntimeVisibleParameterAnnotationAttrInfo(ClassFile classFile, RuntimeVisibleParameterAnnotationsAttrInfo runtimeVisibleParameterAnnotationsAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepRuntimeVisibleParameterAnnotationsAttribute) {
            markAsUsed(runtimeVisibleParameterAnnotationsAttrInfo);
        }
    }

    public void visitRuntimeInvisibleParameterAnnotationAttrInfo(ClassFile classFile, RuntimeInvisibleParameterAnnotationsAttrInfo runtimeInvisibleParameterAnnotationsAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepRuntimeInvisibleParameterAnnotationsAttribute) {
            markAsUsed(runtimeInvisibleParameterAnnotationsAttrInfo);
        }
    }

    public void visitAnnotationDefaultAttrInfo(ClassFile classFile, AnnotationDefaultAttrInfo annotationDefaultAttrInfo) {
        if (keepAllAttributes || keepAllKnownAttributes || keepAnnotationDefaultAttribute) {
            markAsUsed(annotationDefaultAttrInfo);
        }
    }

    public void visitInnerClassesInfo(ClassFile classFile, InnerClassesInfo innerClassesInfo) {
        innerClassesInfo.u2innerNameIndex = 0;
    }

    /**
     * Marks the given VisitorAccepter as being used (or useful).
     * In this context, the VisitorAccepter will be an AttrInfo object.
     */
    private static void markAsUsed(VisitorAccepter visitorAccepter) {
        visitorAccepter.setVisitorInfo(USED);
    }

    /**
     * Returns whether the given VisitorAccepter has been marked as being used.
     * In this context, the VisitorAccepter will be an AttrInfo object.
     */
    static boolean isUsed(VisitorAccepter visitorAccepter) {
        return visitorAccepter.getVisitorInfo() == USED;
    }
}
