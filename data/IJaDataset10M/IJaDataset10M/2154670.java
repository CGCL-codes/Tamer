package com.google.code.java2objc.converters;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import java.util.List;
import java.util.Set;
import com.google.code.java2objc.code.ObjcStatementBlock;
import com.google.code.java2objc.code.ObjcType;
import com.google.common.collect.Lists;
import com.googlecode.java2objc.objc.CompilationContext;
import com.googlecode.java2objc.objc.ObjcEnumType;
import com.googlecode.java2objc.objc.ObjcEnumType.ObjcEnumEntry;
import com.googlecode.java2objc.objc.ObjcField;
import com.googlecode.java2objc.objc.UserDefinedObjcTypeBuilder;

/**
 * Provides utility methods to convert {@link TypeDeclaration} to {@link ObjcType}
 * 
 * @author Inderjeet Singh
 */
public final class TypeConverter {

    private final CompilationContext context;

    private final Set<ObjcType> imports;

    public TypeConverter(CompilationContext context, Set<ObjcType> imports) {
        this.context = context;
        this.imports = imports;
    }

    public ObjcType to(TypeDeclaration type, String pkgName) {
        return to(type, pkgName, null);
    }

    public ObjcType to(TypeDeclaration type, String pkgName, ObjcType containingClass) {
        String comments = null;
        if (type.getJavaDoc() != null) {
            comments = type.getJavaDoc().getContent();
        }
        ObjcType objcType = createType(type, containingClass);
        context.getTypeRepo().put(pkgName, objcType.getName(), objcType);
        UserDefinedObjcTypeBuilder typeBuilder = new UserDefinedObjcTypeBuilder(context, objcType, comments, containingClass);
        buildType(typeBuilder, objcType, type, pkgName);
        return typeBuilder.build();
    }

    private ObjcType createType(TypeDeclaration type, ObjcType containingClass) {
        ObjcType objcType;
        if (type instanceof ClassOrInterfaceDeclaration) {
            objcType = new ObjcType(context, context.prefix(type.getName()), ((ClassOrInterfaceDeclaration) type).isInterface(), imports);
        } else if (type instanceof EnumDeclaration) {
            List<ObjcEnumEntry> entries = Lists.newArrayList();
            for (EnumConstantDeclaration entry : ((EnumDeclaration) type).getEntries()) entries.add(new ObjcEnumEntry(context, entry));
            objcType = new ObjcEnumType(context, context.prefix(type.getName()), imports, entries, containingClass);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + type.getClass().getSimpleName());
        }
        return objcType;
    }

    private void buildType(UserDefinedObjcTypeBuilder typeBuilder, ObjcType objcType, TypeDeclaration type, String pkgName) {
        if (type instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) type).getExtends() != null) {
            if (((ClassOrInterfaceDeclaration) type).isInterface()) {
                for (ClassOrInterfaceType implementedClass : ((ClassOrInterfaceDeclaration) type).getExtends()) {
                    typeBuilder.addProtocol(context.getTypeRepo().getOrCreate(implementedClass));
                }
            } else {
                typeBuilder.setBaseClass(context.getTypeRepo().getOrCreate(((ClassOrInterfaceDeclaration) type).getExtends().iterator().next()));
            }
        }
        List<ClassOrInterfaceType> implemented = null;
        if (type instanceof ClassOrInterfaceDeclaration) implemented = ((ClassOrInterfaceDeclaration) type).getImplements(); else if (type instanceof EnumDeclaration) implemented = ((EnumDeclaration) type).getImplements();
        if (implemented != null) {
            for (ClassOrInterfaceType implementedClass : implemented) {
                typeBuilder.addProtocol(context.getTypeRepo().getOrCreate(implementedClass));
            }
        }
        List<BodyDeclaration> members = type.getMembers();
        if (members != null) {
            for (BodyDeclaration member : members) {
                if (member instanceof FieldDeclaration) {
                    FieldDeclaration field = (FieldDeclaration) member;
                    ObjcField objcField = new ObjcField(context, field);
                    typeBuilder.addField(objcField);
                } else if (member instanceof MethodDeclaration) {
                    typeBuilder.addMethod(context.getMethodConverter().to((MethodDeclaration) member));
                } else if (member instanceof ConstructorDeclaration) {
                    typeBuilder.addMethod(context.getMethodConverter().to((ConstructorDeclaration) member));
                } else if (member instanceof InitializerDeclaration) {
                    InitializerDeclaration init = (InitializerDeclaration) member;
                    if (init.isStatic()) {
                        typeBuilder.addMethod(context.getMethodConverter().to((InitializerDeclaration) member));
                    } else {
                        typeBuilder.addInitializer((ObjcStatementBlock) context.getStatementConverter().to(((InitializerDeclaration) member).getBlock()));
                    }
                } else if (member instanceof ClassOrInterfaceDeclaration || member instanceof EnumDeclaration) {
                    typeBuilder.addSubType(context.getTypeConverter().to((TypeDeclaration) member, pkgName, objcType));
                }
            }
        }
    }
}
