package proguard.shrink;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.visitor.*;

/**
 * This ClassFileVisitor recursively marks all inner classes
 * that are being used in the classes it visits.
 *
 * @see UsageMarker
 *
 * @author Eric Lafortune
 */
public class InnerUsageMarker implements ClassFileVisitor, CpInfoVisitor, AttrInfoVisitor, InnerClassesInfoVisitor {

    private boolean markingAttributes = true;

    private boolean used;

    public void visitProgramClassFile(ProgramClassFile programClassFile) {
        boolean classUsed = UsageMarker.isUsed(programClassFile);
        if (markingAttributes && classUsed) {
            markingAttributes = false;
            programClassFile.attributesAccept(this);
            markingAttributes = true;
        }
        used = classUsed;
    }

    public void visitLibraryClassFile(LibraryClassFile libraryClassFile) {
        used = true;
    }

    public void visitIntegerCpInfo(ClassFile classFile, IntegerCpInfo integerCpInfo) {
    }

    public void visitLongCpInfo(ClassFile classFile, LongCpInfo longCpInfo) {
    }

    public void visitFloatCpInfo(ClassFile classFile, FloatCpInfo floatCpInfo) {
    }

    public void visitDoubleCpInfo(ClassFile classFile, DoubleCpInfo doubleCpInfo) {
    }

    public void visitStringCpInfo(ClassFile classFile, StringCpInfo stringCpInfo) {
    }

    public void visitFieldrefCpInfo(ClassFile classFile, FieldrefCpInfo fieldrefCpInfo) {
    }

    public void visitInterfaceMethodrefCpInfo(ClassFile classFile, InterfaceMethodrefCpInfo interfaceMethodrefCpInfo) {
    }

    public void visitMethodrefCpInfo(ClassFile classFile, MethodrefCpInfo methodrefCpInfo) {
    }

    public void visitNameAndTypeCpInfo(ClassFile classFile, NameAndTypeCpInfo nameAndTypeCpInfo) {
    }

    public void visitClassCpInfo(ClassFile classFile, ClassCpInfo classCpInfo) {
        boolean classUsed = UsageMarker.isUsed(classCpInfo);
        if (!classUsed) {
            classCpInfo.referencedClassAccept(this);
            classUsed = used;
            if (classUsed) {
                UsageMarker.markAsUsed(classCpInfo);
                markCpEntry(classFile, classCpInfo.u2nameIndex);
            }
        }
        used = classUsed;
    }

    public void visitUtf8CpInfo(ClassFile classFile, Utf8CpInfo utf8CpInfo) {
        if (!UsageMarker.isUsed(utf8CpInfo)) {
            UsageMarker.markAsUsed(utf8CpInfo);
        }
    }

    public void visitUnknownAttrInfo(ClassFile classFile, UnknownAttrInfo unknownAttrInfo) {
    }

    public void visitEnclosingMethodAttrInfo(ClassFile classFile, EnclosingMethodAttrInfo enclosingMethodAttrInfo) {
    }

    public void visitConstantValueAttrInfo(ClassFile classFile, FieldInfo fieldInfo, ConstantValueAttrInfo constantValueAttrInfo) {
    }

    public void visitExceptionsAttrInfo(ClassFile classFile, MethodInfo methodInfo, ExceptionsAttrInfo exceptionsAttrInfo) {
    }

    public void visitCodeAttrInfo(ClassFile classFile, MethodInfo methodInfo, CodeAttrInfo codeAttrInfo) {
    }

    public void visitLineNumberTableAttrInfo(ClassFile classFile, MethodInfo methodInfo, CodeAttrInfo codeAttrInfo, LineNumberTableAttrInfo lineNumberTableAttrInfo) {
    }

    public void visitLocalVariableTableAttrInfo(ClassFile classFile, MethodInfo methodInfo, CodeAttrInfo codeAttrInfo, LocalVariableTableAttrInfo localVariableTableAttrInfo) {
    }

    public void visitLocalVariableTypeTableAttrInfo(ClassFile classFile, MethodInfo methodInfo, CodeAttrInfo codeAttrInfo, LocalVariableTypeTableAttrInfo localVariableTypeTableAttrInfo) {
    }

    public void visitSourceFileAttrInfo(ClassFile classFile, SourceFileAttrInfo sourceFileAttrInfo) {
    }

    public void visitSourceDirAttrInfo(ClassFile classFile, SourceDirAttrInfo sourceDirAttrInfo) {
    }

    public void visitDeprecatedAttrInfo(ClassFile classFile, DeprecatedAttrInfo deprecatedAttrInfo) {
    }

    public void visitSyntheticAttrInfo(ClassFile classFile, SyntheticAttrInfo syntheticAttrInfo) {
    }

    public void visitSignatureAttrInfo(ClassFile classFile, SignatureAttrInfo signatureAttrInfo) {
    }

    public void visitRuntimeVisibleAnnotationAttrInfo(ClassFile classFile, RuntimeVisibleAnnotationsAttrInfo runtimeVisibleAnnotationsAttrInfo) {
    }

    public void visitRuntimeInvisibleAnnotationAttrInfo(ClassFile classFile, RuntimeInvisibleAnnotationsAttrInfo runtimeInvisibleAnnotationsAttrInfo) {
    }

    public void visitRuntimeVisibleParameterAnnotationAttrInfo(ClassFile classFile, RuntimeVisibleParameterAnnotationsAttrInfo runtimeVisibleParameterAnnotationsAttrInfo) {
    }

    public void visitRuntimeInvisibleParameterAnnotationAttrInfo(ClassFile classFile, RuntimeInvisibleParameterAnnotationsAttrInfo runtimeInvisibleParameterAnnotationsAttrInfo) {
    }

    public void visitAnnotationDefaultAttrInfo(ClassFile classFile, AnnotationDefaultAttrInfo annotationDefaultAttrInfo) {
    }

    public void visitInnerClassesAttrInfo(ClassFile classFile, InnerClassesAttrInfo innerClassesAttrInfo) {
        boolean attributeUsed = false;
        for (int i = 0; i < innerClassesAttrInfo.u2numberOfClasses; i++) {
            visitInnerClassesInfo(classFile, innerClassesAttrInfo.classes[i]);
            attributeUsed |= used;
        }
        if (attributeUsed) {
            UsageMarker.markAsUsed(innerClassesAttrInfo);
            markCpEntry(classFile, innerClassesAttrInfo.u2attrNameIndex);
        }
    }

    public void visitInnerClassesInfo(ClassFile classFile, InnerClassesInfo innerClassesInfo) {
        boolean innerClassesInfoUsed = UsageMarker.isUsed(innerClassesInfo);
        if (!innerClassesInfoUsed) {
            int u2innerClassInfoIndex = innerClassesInfo.u2innerClassInfoIndex;
            int u2outerClassInfoIndex = innerClassesInfo.u2outerClassInfoIndex;
            int u2innerNameIndex = innerClassesInfo.u2innerNameIndex;
            innerClassesInfoUsed = true;
            if (u2innerClassInfoIndex != 0) {
                markCpEntry(classFile, u2innerClassInfoIndex);
                innerClassesInfoUsed &= used;
            }
            if (u2outerClassInfoIndex != 0) {
                markCpEntry(classFile, u2outerClassInfoIndex);
                innerClassesInfoUsed &= used;
            }
            if (innerClassesInfoUsed) {
                UsageMarker.markAsUsed(innerClassesInfo);
                if (u2innerNameIndex != 0) {
                    markCpEntry(classFile, u2innerNameIndex);
                }
            }
        }
        used = innerClassesInfoUsed;
    }

    /**
     * Marks the given constant pool entry of the given class. This includes
     * visiting any other referenced constant pool entries.
     */
    private void markCpEntry(ClassFile classFile, int index) {
        classFile.constantPoolEntryAccept(index, this);
    }
}
