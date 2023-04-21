package de.fzi.injectj.model.impl.recoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import recoder.CrossReferenceServiceConfiguration;
import recoder.ParserException;
import recoder.ProgramFactory;
import recoder.abstraction.ClassType;
import recoder.abstraction.Constructor;
import recoder.abstraction.Type;
import recoder.convenience.Naming;
import recoder.io.SourceFileRepository;
import recoder.java.CompilationUnit;
import recoder.java.Identifier;
import recoder.java.Import;
import recoder.java.PackageSpecification;
import recoder.java.ProgramElement;
import recoder.java.declaration.ClassDeclaration;
import recoder.java.declaration.ConstructorDeclaration;
import recoder.java.declaration.Extends;
import recoder.java.declaration.FieldDeclaration;
import recoder.java.declaration.FieldSpecification;
import recoder.java.declaration.Implements;
import recoder.java.declaration.InterfaceDeclaration;
import recoder.java.declaration.MemberDeclaration;
import recoder.java.declaration.MethodDeclaration;
import recoder.java.declaration.TypeDeclaration;
import recoder.java.reference.PackageReference;
import recoder.java.reference.TypeReference;
import recoder.java.reference.TypeReferenceContainer;
import recoder.kit.MiscKit;
import recoder.kit.NoProblem;
import recoder.kit.PackageKit;
import recoder.kit.ProblemReport;
import recoder.kit.Transformation;
import recoder.kit.TypeKit;
import recoder.kit.UnitKit;
import recoder.list.ClassTypeList;
import recoder.list.ConstructorList;
import recoder.list.ConstructorReferenceList;
import recoder.list.FieldSpecificationMutableList;
import recoder.list.ImportMutableList;
import recoder.list.MemberDeclarationArrayList;
import recoder.list.MemberDeclarationMutableList;
import recoder.list.ModifierMutableList;
import recoder.list.TypeDeclarationArrayList;
import recoder.list.TypeDeclarationMutableList;
import recoder.list.TypeReferenceArrayList;
import recoder.list.TypeReferenceList;
import recoder.list.TypeReferenceMutableList;
import de.fzi.injectj.language.CodeMapper;
import de.fzi.injectj.model.Annotation;
import de.fzi.injectj.model.AttributeWeavepoint;
import de.fzi.injectj.model.ClassWeavepoint;
import de.fzi.injectj.model.FragmentType;
import de.fzi.injectj.model.InterfaceWeavepoint;
import de.fzi.injectj.model.MemberWeavepoint;
import de.fzi.injectj.model.MethodType;
import de.fzi.injectj.model.MethodWeavepoint;
import de.fzi.injectj.model.PackageWeavepoint;
import de.fzi.injectj.model.RollbackNotSupportedHandler;
import de.fzi.injectj.model.RollbackHandler;
import de.fzi.injectj.model.TypeWeavepoint;
import de.fzi.injectj.model.SourceElement;
import de.fzi.injectj.model.exception.AnnotationException;
import de.fzi.injectj.model.exception.ConformityException;
import de.fzi.injectj.model.exception.DefaultModelException;
import de.fzi.injectj.model.exception.InvalidIdentifierException;
import de.fzi.injectj.model.exception.ModelException;
import de.fzi.injectj.model.exception.NameclashException;
import de.fzi.injectj.model.exception.ReferenceException;
import de.fzi.injectj.model.exception.SyntaxException;
import de.fzi.injectj.model.exception.TypeNotFoundException;
import de.fzi.injectj.model.impl.recoder.transformation.RenameClass;
import de.fzi.injectj.util.StringUtil;

/**
 * @author Thomas Genssler
 * @author Volker Kuttruff
 * @author Olaf Seng
 * @author Sven Luzar
 * @author Tobias Gutzmann
 * 
 * This class is part of the Inject/J (http://injectj.sf.net) project. Inject/J
 * is free software, available under the terms and conditions of the GNU public
 * license.
 * 
 *  
 */
public final class RecoderSourceClass extends RecoderClass {

    private final ClassDeclaration recoderClass;

    public RecoderSourceClass(MemberWeavepoint parent, ClassDeclaration recoderClass) {
        super(parent);
        if (recoderClass == null) throw new IllegalArgumentException();
        this.recoderClass = recoderClass;
    }

    private static class AddToMembersComputations {

        List constructorsAdded = new ArrayList();

        List methodsAdded = new ArrayList();

        List attributesAdded = new ArrayList();

        ModelException modelException = null;

        List fieldDecls = new ArrayList();
    }

    private AddToMembersComputations pre__addToMembers(FragmentType javaSource) {
        AddToMembersComputations result = new AddToMembersComputations();
        ProgramFactory factory = recoderClass.getFactory();
        String source = javaSource.getSourceString();
        String dummyClassSource = "class ____DummyClass____ {\n" + source + "\n}";
        TypeDeclaration dummyClass = null;
        try {
            dummyClass = factory.parseTypeDeclaration(dummyClassSource);
            MiscKit.unindent(dummyClass);
        } catch (ParserException e) {
            String message = CodeMapper.getText("SYNTAX_ERROR_IN_JAVA_CODE");
            message = StringUtil.replace(message, "{1}", source) + "\n\nParser message: " + e.getMessage() + "\n";
            result.modelException = new SyntaxException(message);
            return result;
        }
        MemberDeclarationMutableList newMembers = dummyClass.getMembers();
        MemberDeclarationMutableList members = recoderClass.getMembers();
        if (members == null) {
            members = new MemberDeclarationArrayList(newMembers.size());
            recoderClass.setMembers(members);
        } else if (members.size() == 0) members.ensureCapacity(newMembers.size());
        for (int i = 0; i < newMembers.size(); i++) {
            MemberDeclaration member = newMembers.getMemberDeclaration(i);
            if (member instanceof FieldDeclaration) {
                FieldDeclaration fieldDecl = (FieldDeclaration) member;
                result.fieldDecls.add(fieldDecl);
                FieldSpecificationMutableList fieldList = fieldDecl.getFieldSpecifications();
                for (int j = 0; j < fieldList.size(); j++) {
                    FieldSpecification fieldSpec = fieldList.getFieldSpecification(j);
                    assert fieldSpec.getASTParent() == fieldDecl;
                    RecoderAttribute ra = new RecoderAttribute(this, fieldSpec);
                    result.attributesAdded.add(ra);
                }
            } else if (member instanceof ConstructorDeclaration) {
                ConstructorDeclaration consDecl = (ConstructorDeclaration) member;
                RecoderConstructor rc = new RecoderConstructor(this, consDecl);
                result.constructorsAdded.add(rc);
            } else if (member instanceof MethodDeclaration) {
                MethodDeclaration methodDecl = (MethodDeclaration) member;
                RecoderMethod rm = new RecoderMethod(this, methodDecl);
                result.methodsAdded.add(rm);
            } else {
                result.modelException = new SyntaxException("New member isn't a FieldDeclaration or a MethodDeclaration");
            }
        }
        return result;
    }

    public boolean canAddToMembers(FragmentType javaSource) {
        return pre__addToMembers(javaSource).modelException == null;
    }

    public void addToMembers(FragmentType javaSource) throws ModelException {
        final RecoderRoot root = (RecoderRoot) getModel().getRoot();
        final Transformation rollbackIndex = (Transformation) root.createRollbackIndex();
        AddToMembersComputations atmc = pre__addToMembers(javaSource);
        if (isChecked() & atmc.modelException != null) throw atmc.modelException;
        for (int i = 0, max = atmc.fieldDecls.size(); i < max; i++) {
            addMember((FieldDeclaration) atmc.fieldDecls.get(i));
        }
        for (int i = 0, max = atmc.methodsAdded.size(); i < max; i++) {
            RecoderMethod meth = (RecoderMethod) atmc.methodsAdded.get(i);
            addMember((MemberDeclaration) meth.getMopObject());
        }
        for (int i = 0, max = atmc.constructorsAdded.size(); i < max; i++) {
            RecoderConstructor cons = (RecoderConstructor) atmc.constructorsAdded.get(i);
            addMember((MemberDeclaration) cons.getMopObject());
        }
        for (int i = 0, max = atmc.attributesAdded.size(); i < max; i++) {
            RecoderAttribute att = (RecoderAttribute) atmc.attributesAdded.get(i);
            attributeAdded(att);
            assert att.isConsistent();
        }
        for (int i = 0, max = atmc.methodsAdded.size(); i < max; i++) {
            RecoderMethod meth = (RecoderMethod) atmc.methodsAdded.get(i);
            methodAdded(meth);
            assert meth.isConsistent();
        }
        for (int i = 0, max = atmc.constructorsAdded.size(); i < max; i++) {
            RecoderConstructor cons = (RecoderConstructor) atmc.constructorsAdded.get(i);
            constructorAdded(cons);
            assert cons.isConsistent();
        }
        root.getTransactionManager().addToNotifyOnRollback(new RollbackHandler() {

            public void committed() {
            }

            public void rolledBack() {
                constructors = null;
                methods = null;
                attributes = null;
                root.changeHistory.rollback(rollbackIndex);
            }

            public TypeWeavepoint[] getAffectedTypeWeavepoints() {
                TypeWeavepoint result[] = new TypeWeavepoint[1];
                result[0] = RecoderSourceClass.this;
                return result;
            }
        });
        assert isConsistent();
        assert root.assertConsistency();
    }

    private ModelException pre__addImport(String src) {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        if (src.endsWith("*")) {
            String pname = src.substring(0, src.lastIndexOf("."));
            if (!pname.equals("") && !getNameChecker().isValidPackageIdentifier(pname)) {
                return new InvalidIdentifierException("Can't create import '" + src + "' is not a valid identifier");
            }
            recoder.abstraction.Package referencedPackage = root.nameInfo.getPackage(pname);
            if (referencedPackage == null) {
                return new ReferenceException("Can't create import '" + src + "', because package '" + pname + "' doesn't exist.");
            }
            ClassTypeList typeList = referencedPackage.getTypes();
            if (typeList.size() == 0) {
                return new ReferenceException("Can't create import '" + src + "', package contains no classes");
            }
        } else {
            recoder.abstraction.Type type = root.nameInfo.getType(src);
            if (type == null) {
                return new TypeNotFoundException(CodeMapper.getText("CANT_CREATE_IMPORT", src));
            }
        }
        return null;
    }

    public void addImport(String src) throws ModelException {
        final RecoderRoot root = (RecoderRoot) getModel().getRoot();
        root.getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
        CompilationUnit unit = UnitKit.getCompilationUnit(recoderClass);
        ProgramFactory factory = recoderClass.getFactory();
        Import importElement = null;
        if (isChecked()) {
            ModelException me = pre__addImport(src);
            if (me != null) throw me;
        }
        if (src.endsWith("*")) {
            src = src.substring(0, src.lastIndexOf("."));
            recoder.abstraction.Package packageRef = root.nameInfo.getPackage(src);
            PackageReference packRef = PackageKit.createPackageReference(factory, packageRef);
            assert (packRef != null);
            importElement = factory.createImport(packRef);
        } else {
            TypeReference typeReference = TypeKit.createTypeReference(factory, src);
            assert (typeReference != null);
            importElement = factory.createImport(typeReference, false);
        }
        addImportIfNecessary(root, unit, importElement);
        assert isConsistent();
        assert root.assertConsistency();
    }

    private static void addImportIfNecessary(RecoderRoot root, CompilationUnit unit, Import importElement) {
        ImportMutableList importList = unit.getImports();
        recoder.abstraction.Package newPck = null;
        recoder.abstraction.Package importPck = null;
        String importName = "";
        TypeReference typeRef = importElement.getTypeReference();
        PackageReference packRef = importElement.getPackageReference();
        if (typeRef != null) {
            String fullName = Naming.toPathName(typeRef);
            ClassType t = ((RecoderType) root.getTypeWeavepoint(fullName)).getClassType();
            newPck = t.getPackage();
            importName = t.getFullName();
        } else if (packRef != null) {
            newPck = root.getSourceInfo().getPackage(packRef);
            importName = newPck.getFullName() + ".*";
        }
        Import[] imports = importList.toImportArray();
        boolean alreadyImported = false;
        for (int i = 0; (i < imports.length) && !alreadyImported; i++) {
            String name = "";
            TypeReference ref1 = imports[i].getTypeReference();
            PackageReference pr = null;
            Type t1 = null;
            if (imports[i].getPackageReference() != null) {
                pr = imports[i].getPackageReference();
                importPck = root.getSourceInfo().getPackage(pr);
                name = importPck.getFullName() + ".*";
            } else {
                t1 = root.getSourceInfo().getType(ref1);
                pr = ref1.getPackageReference();
                importPck = root.getSourceInfo().getPackage(pr);
                name = t1.getFullName();
            }
            if (importElement.isMultiImport()) {
                alreadyImported = name.equals(importName);
            } else {
                if (imports[i].isMultiImport()) {
                    alreadyImported = (newPck == importPck);
                } else {
                    alreadyImported = name.equals(importName);
                }
            }
        }
        if (!alreadyImported) {
            root.getTransactionManager().addToNotifyOnRollback(new DefaultRollbackHandler(root));
            importList.add(importElement);
            importElement.setParent(unit);
            root.changeHistory.attached(importElement);
        }
        assert root.assertConsistency();
    }

    public void setSuperClass(String name) throws ModelException {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        Extends extended = recoderClass.getExtendedTypes();
        if (extended != null) {
            root.getTransactionManager().addToNotifyOnRollback(new DefaultRollbackHandler(root, this));
            int oldPos = extended.getASTParent().getChildPositionCode(extended);
            recoderClass.replaceChild(extended, null);
            root.changeHistory.detached(extended, oldPos);
        }
        if (name != null) addToExtends(name);
        assert isConsistent();
        assert root.assertConsistency();
    }

    public void setSuperClass(ClassWeavepoint cwp) throws ModelException {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        Extends extended = recoderClass.getExtendedTypes();
        if (extended != null) {
            root.getTransactionManager().addToNotifyOnRollback(new DefaultRollbackHandler(root, this));
            int oldPos = extended.getASTParent().getChildPositionCode(extended);
            recoderClass.replaceChild(extended, null);
            root.changeHistory.detached(extended, oldPos);
        }
        if (cwp != null) addToExtends(cwp);
        assert isConsistent();
        assert root.assertConsistency();
    }

    public void addToExtends(TypeWeavepoint typeref) throws ModelException {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        assert (typeref != null);
        Extends extended = recoderClass.getExtendedTypes();
        Extends newExtended = null;
        if (extended != null && extended.getChildCount() != 0) {
            throw new DefaultModelException("Class '" + getName() + "' is already extending a class!");
        }
        if (!(typeref instanceof RecoderClass)) throw new SyntaxException("Java class " + getFullName() + " can only extend classes while " + typeref.getFullName() + " is not a class.");
        root.getTransactionManager().addToNotifyOnRollback(new DefaultRollbackHandler((RecoderRoot) getModel().getRoot(), this));
        ProgramFactory factory = recoderClass.getFactory();
        Type t = null;
        TypeReference tr = null;
        if (typeref instanceof RecoderSourceClass) {
            RecoderSourceClass target = (RecoderSourceClass) typeref;
            t = target.recoderClass;
        } else if (typeref instanceof RecoderBytecodeClass) {
            RecoderBytecodeClass target = (RecoderBytecodeClass) typeref;
            if (target.isPrimitiveType()) throw new SyntaxException("Class " + this.getFullName() + " cannot extend primitive type " + target.getFullName());
            t = target.recoderType;
        }
        assert (t != null);
        tr = TypeKit.createTypeReference(factory, t);
        newExtended = factory.createExtends(tr);
        recoderClass.setExtendedTypes(newExtended);
        recoderClass.makeParentRoleValid();
        root.changeHistory.attached(newExtended);
        if (((RecoderClass) typeref).isAbstract()) RecoderTypeHelper.implementAbstractMethods(this, typeref);
        assert isConsistent();
        assert root.assertConsistency();
    }

    public void addToExtends(String javaSource) throws ModelException {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        TypeWeavepoint twp = root.getTypeWeavepoint(javaSource);
        if (twp == null) throw new TypeNotFoundException("Class.addToExtendsByName(" + javaSource + ")");
        addToExtends(twp);
        assert isConsistent();
    }

    public void addToImplements(InterfaceWeavepoint itf) throws ModelException {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        Implements implemented = recoderClass.getImplementedTypes();
        ProgramFactory factory = recoderClass.getFactory();
        Type t = null;
        TypeReference tr = null;
        if (itf instanceof RecoderSourceInterface) {
            RecoderSourceInterface target = (RecoderSourceInterface) itf;
            t = target.interfaceDeclaration;
        } else if (itf instanceof RecoderBytecodeInterface) {
            RecoderBytecodeInterface target = (RecoderBytecodeInterface) itf;
            t = target.classFile;
        } else {
            throw new IllegalArgumentException();
        }
        assert t != null;
        tr = TypeKit.createTypeReference(factory, t);
        if (implemented == null) {
            root.getTransactionManager().addToNotifyOnRollback(new DefaultRollbackHandler(root));
            implemented = factory.createImplements(tr);
            recoderClass.setImplementedTypes(implemented);
            implemented.setParent(recoderClass);
            root.changeHistory.attached(implemented);
        } else {
            TypeReferenceMutableList implementedTypesList = implemented.getSupertypes();
            boolean contains = false;
            for (int i = 0; i < implementedTypesList.size(); i++) {
                if (implementedTypesList.getTypeReference(i).getName().equals(tr.getName())) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                root.getTransactionManager().addToNotifyOnRollback(new DefaultRollbackHandler(root));
                implementedTypesList.add(tr);
                implemented.makeParentRoleValid();
                root.changeHistory.attached(tr);
            }
        }
        RecoderTypeHelper.implementAbstractMethods(this, itf);
        assert isConsistent();
        assert root.assertConsistency();
    }

    public void addToImplements(String name) throws ModelException {
        assert (name != null);
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        InterfaceWeavepoint iwp = null;
        TypeWeavepoint tw = root.getTypeWeavepoint(name);
        if (!(tw instanceof InterfaceWeavepoint)) {
            throw new DefaultModelException("Cannot implement class weavepoint: " + name);
        }
        iwp = (InterfaceWeavepoint) tw;
        addToImplements(iwp);
        assert isConsistent();
    }

    private ModelException pre__rename(String newClassName) {
        if (!getNameChecker().isValidClassIdentifier(newClassName)) {
            String message = CodeMapper.getText("NO_VALID_CLASS_IDENTIFIER", newClassName);
            return new InvalidIdentifierException(message);
        }
        PackageSpecification ps = UnitKit.getCompilationUnit(recoderClass).getPackageSpecification();
        String newQualifiedName = ps == null ? newClassName : Naming.toPathName(ps.getPackageReference(), newClassName);
        if (getModel().getRoot().getTypeWeavepoint(newQualifiedName) != null) {
            String message = CodeMapper.getText("CANT_RENAME_CLASS_EXISTS", getName(), newQualifiedName);
            return new NameclashException(message);
        }
        return null;
    }

    public boolean canRename(String newClassName) {
        return (pre__rename(newClassName) == null);
    }

    public void rename(String newClassName) throws ModelException {
        final RecoderRoot root = (RecoderRoot) getModel().getRoot();
        final Transformation rollbackIndex = (Transformation) root.createRollbackIndex();
        if (isChecked()) {
            ModelException me = pre__rename(newClassName);
            if (me != null) throw me;
        }
        RenameClass transformation = new RenameClass((CrossReferenceServiceConfiguration) root.serviceConfiguration, recoderClass, newClassName);
        ProblemReport report = transformation.analyze();
        if (isChecked() && !(report instanceof NoProblem)) {
            throw new RuntimeException("Cannot rename class due to unexpected behaviour from recoder:\n" + report.toString());
        }
        root.unregisterClass(this);
        transformation.transform();
        root.registerClass(this);
        updateConstructorTableKeys();
        root.getTransactionManager().addToNotifyOnRollback(new RollbackHandler() {

            public void committed() {
            }

            public void rolledBack() {
                constructors = null;
                root.unregisterClass(RecoderSourceClass.this);
                root.changeHistory.rollback(rollbackIndex);
                root.registerClass(RecoderSourceClass.this);
            }

            public TypeWeavepoint[] getAffectedTypeWeavepoints() {
                TypeWeavepoint result[] = new TypeWeavepoint[1];
                result[0] = RecoderSourceClass.this;
                return result;
            }
        });
        assert isConsistent();
        assert root.assertConsistency();
    }

    public void addClass(String className, FragmentType classSource) throws ModelException {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        root.getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
        if (className.indexOf('.') != -1) throw new InvalidIdentifierException("PACKAGE_IDENTIFIER_NOT_ALLOWED");
        String source = "class " + className + "\n{\n" + classSource.getSourceString() + "\n}\n";
        CompilationUnit cu = UnitKit.getCompilationUnit(recoderClass);
        TypeDeclaration newType = null;
        try {
            newType = recoderClass.getFactory().parseTypeDeclaration(source);
            MiscKit.unindent(newType);
        } catch (ParserException e) {
            String message = CodeMapper.getText("SYNTAX_ERROR_IN_JAVA_CODE", source);
            throw new SyntaxException(message);
        }
        assert (newType instanceof ClassDeclaration);
        ClassDeclaration newClass = (ClassDeclaration) newType;
        newClass.setProgramModelInfo(recoderClass.getProgramModelInfo());
        TypeDeclarationMutableList typeDecls = cu.getDeclarations();
        typeDecls.add(newClass);
        newClass.setParent(cu);
        root.changeHistory.attached(newClass);
        RecoderSourceClass newRecoderClass = new RecoderSourceClass(null, newClass);
        newRecoderClass.setModel(this);
        root.registerClass(newRecoderClass);
        assert isConsistent();
        assert root.assertConsistency();
    }

    private ModelException pre__delete() {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        TypeReferenceList typeReferences = root.crossReferencer.getReferences(recoderClass);
        String surroundingClassName;
        for (int i = 0, max = typeReferences.size(); i < max; i++) {
            TypeReference typeRef = typeReferences.getTypeReference(i);
            TypeWeavepoint surroundingClass = RecoderUtil.findParentTypeWeavepoint(root, typeRef);
            if (surroundingClass != null) {
                surroundingClassName = surroundingClass.getFullName();
                if (!surroundingClassName.equals(getFullName())) {
                    return new ReferenceException("Precondition failed: Class '" + getFullName() + "' is referenced outside itself.\n Referenced from " + surroundingClassName);
                }
            }
        }
        ConstructorList constructorList = recoderClass.getConstructors();
        for (int i = 0, max = constructorList.size(); i < max; i++) {
            Constructor constructor = constructorList.getConstructor(i);
            ConstructorReferenceList constructorReferences = root.crossReferencer.getReferences(constructor);
            for (int j = 0, max2 = constructorReferences.size(); j < max2; j++) {
                surroundingClassName = RecoderUtil.findParentTypeWeavepoint(root, constructorReferences.getConstructorReference(j)).getFullName();
                if (!surroundingClassName.equals(getFullName())) {
                    return new ReferenceException("Precondition failed: Constructor of class '" + getFullName() + "' is referenced outside the class itself.");
                }
            }
        }
        return null;
    }

    public boolean canDelete() {
        return (pre__delete() == null);
    }

    public SourceElement delete() throws ModelException {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        root.getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
        if (isChecked()) {
            ModelException me = pre__delete();
            if (me != null) throw me;
        }
        TypeReferenceList classReferences = root.crossReferencer.getReferences(recoderClass);
        TypeReference typeRef = null;
        CompilationUnit unit = null;
        for (int i = 0; i < classReferences.size(); i++) {
            typeRef = classReferences.getTypeReference(i);
            TypeWeavepoint surroundingClass = RecoderUtil.findParentTypeWeavepoint(root, typeRef);
            if (surroundingClass == null) {
                unit = UnitKit.getCompilationUnit(typeRef);
                TypeReferenceContainer container = typeRef.getParent();
                if (container instanceof Import) {
                    Import imp = (Import) container;
                    ImportMutableList importList = unit.getImports();
                    root.changeHistory.detached(imp, imp.getASTParent().getChildPositionCode(imp));
                    int idx = importList.indexOf(imp);
                    importList.remove(idx);
                }
            }
        }
        CompilationUnit compilationUnit = UnitKit.getCompilationUnit(recoderClass);
        int childPositionCode = compilationUnit.getChildPositionCode(recoderClass);
        if (childPositionCode == -1) {
            throw new RuntimeException("Can't delete non-top-level class '" + getFullName() + "' - not implemented yet");
        }
        if (compilationUnit.getDeclarations().size() == 1) {
            root.changeHistory.detached(compilationUnit, 0);
        } else {
            compilationUnit.replaceChild(recoderClass, null);
            root.changeHistory.detached(recoderClass, compilationUnit, childPositionCode);
        }
        root.unregisterClass(this);
        setInvalid();
        assert root.assertConsistency();
        return this;
    }

    void attributeAdded(RecoderAttribute added) {
        if (attributes != null) attributes.put(added.getName(), added);
    }

    void attributeRemoved(RecoderAttribute removed) {
        if (attributes != null) attributes.remove(removed.getName());
    }

    void attributeReplaced(RecoderAttribute replaced, RecoderAttribute replacement) {
        if (attributes != null) {
            attributeRemoved(replaced);
            attributes.put(replacement.getName(), replacement);
        }
    }

    void attributeRenamed(RecoderAttribute renamed, String oldname) {
        if (attributes != null) {
            attributes.remove(oldname);
            attributes.put(renamed.getName(), renamed);
        }
    }

    void methodAdded(RecoderMethod added) {
        if (methods != null) methods.put(added.getSignature(), added);
    }

    void methodRemoved(RecoderMethod removed) {
        if (methods != null) methods.remove(removed.getSignature());
    }

    void methodReplaced(RecoderMethod replaced, RecoderMethod replacement) {
        if (methods != null) {
            methodRemoved(replaced);
            methodAdded(replacement);
        }
    }

    void methodRenamed(RecoderMethod renamed, String oldSignature) {
        if (methods != null) {
            methods.remove(oldSignature);
            methods.put(renamed.getSignature(), renamed);
        }
    }

    void constructorAdded(RecoderConstructor added) {
        if (constructors != null) constructors.put(added.getSignature(), added);
    }

    void constructorRemoved(RecoderConstructor removed) {
        if (constructors != null) constructors.remove(removed.getSignature());
    }

    void constructorReplaced(RecoderConstructor replaced, RecoderConstructor replacement) {
        if (constructors != null) {
            constructorRemoved(replaced);
            constructors.put(replacement.getSignature(), replacement);
        }
    }

    public String toString() {
        return recoderClass.toSource();
    }

    private ModelException pre__clone(String newName) {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        throw new RuntimeException("Not implemented yet");
    }

    public boolean canClone(String newName) {
        return (pre__clone(newName) == null);
    }

    /**
     * @see de.fzi.injectj.model.ClassWeavepoint#cloneClass(String)
     * @pre isValidClassIdentifier(newName)
     * @pre !classExists(newName)
     */
    public ClassWeavepoint clone(String newName) throws ModelException {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        root.getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
        newName = newName.substring(newName.lastIndexOf(".") + 1);
        if (!getNameChecker().isValidClassIdentifier(newName)) {
            throw new InvalidIdentifierException(newName);
        }
        if (root.getTypeWeavepoint(newName) != null) throw new NameclashException("cloneClass failed");
        Identifier newIdentifierName = recoderClass.getFactory().createIdentifier(newName);
        ClassDeclaration cd = recoderClass;
        Extends ext = (cd.getExtendedTypes());
        ext = (Extends) (ext != null ? ext.deepClone() : null);
        Implements impl = (cd.getImplementedTypes());
        impl = (Implements) (impl != null ? impl.deepClone() : null);
        MemberDeclarationMutableList members = cd.getMembers();
        members = (MemberDeclarationMutableList) (members != null ? members.deepClone() : null);
        if (members != null) {
            for (int i = 0; i < members.size(); i++) {
                MemberDeclaration currentMember = members.getMemberDeclaration(i);
                if (currentMember instanceof ConstructorDeclaration) ((ConstructorDeclaration) currentMember).setIdentifier(new Identifier(newName));
            }
        }
        ClassDeclaration newClass = recoderClass.getFactory().createClassDeclaration((ModifierMutableList) (cd.getModifiers().deepClone()), newIdentifierName, ext, impl, members);
        MiscKit.unindent(newClass);
        RecoderSourceClass newRecoderClass = null;
        CompilationUnit cu_old = UnitKit.getCompilationUnit(recoderClass);
        if (!recoderClass.isPublic()) {
            TypeDeclarationMutableList typeDecls = cu_old.getDeclarations();
            typeDecls.add(newClass);
            newClass.setParent(cu_old);
            root.changeHistory.attached(newClass);
            newRecoderClass = new RecoderSourceClass(null, newClass);
            newRecoderClass.setModel(this);
            root.registerClass(newRecoderClass);
        } else {
            CompilationUnit cu = recoderClass.getFactory().createCompilationUnit(cu_old.getPackageSpecification(), cu_old.getImports(), new TypeDeclarationArrayList(newClass));
            root.changeHistory.attached(cu);
            root.updateModel();
            TypeDeclaration primaryType = cu.getPrimaryTypeDeclaration();
            for (int i = 0; i < cu.getTypeDeclarationCount(); i++) {
                TypeDeclaration typeDecl = cu.getTypeDeclarationAt(i);
                if (typeDecl instanceof ClassDeclaration) {
                    RecoderSourceClass rc = new RecoderSourceClass(null, (ClassDeclaration) typeDecl);
                    rc.setModel(this);
                    root.registerClass(rc);
                    if (cu.getTypeDeclarationAt(i) == primaryType) newRecoderClass = rc;
                } else if (typeDecl instanceof InterfaceDeclaration) {
                    RecoderSourceInterface ri = new RecoderSourceInterface(null, (InterfaceDeclaration) typeDecl);
                    ri.setModel(this);
                    root.registerInterface(ri);
                }
            }
        }
        assert (newRecoderClass != null);
        assert newRecoderClass.isConsistent();
        assert root.assertConsistency();
        return newRecoderClass;
    }

    public boolean isModified() {
        boolean res = true;
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        SourceFileRepository repository = root.serviceConfiguration.getSourceFileRepository();
        CompilationUnit cu = UnitKit.getCompilationUnit(recoderClass);
        res = !repository.isUpToDate(cu);
        return res;
    }

    public FragmentType getSource() {
        return new RecoderSourceFragment(recoderClass.toSource(), recoderClass);
    }

    public boolean hasSource() {
        return true;
    }

    public ClassType getClassType() {
        return recoderClass;
    }

    protected recoder.abstraction.Type getType() {
        return recoderClass;
    }

    public void addProperty(String type, String propertyName, FragmentType getterSource, FragmentType setterSource) throws ModelException {
        String add = type + " " + propertyName + ";\n";
        if (getterSource != null) {
            add += "\n" + getterSource.getSourceString() + "\n";
        }
        if (setterSource != null) {
            add += "\n" + setterSource.getSourceString() + "\n";
        }
        addToMembers(new RecoderSourceFragment(add));
        assert isConsistent();
    }

    public void addProperty(String type, String propertyName, String modifier, boolean createGetter, boolean createSetter) throws ModelException {
        if (!createGetter && !createSetter) {
            throw new ConformityException("at least getter or setter needs to be created...");
        }
        String propWithOneUC = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        FragmentType getterSource = null, setterSource = null;
        if (createGetter) getterSource = new RecoderSourceFragment("public " + type + " get" + propWithOneUC + "() {\n" + "return " + propertyName + ";\n" + "}");
        if (createSetter) setterSource = new RecoderSourceFragment("public void set" + propWithOneUC + "(" + type + " " + propertyName + ") {\n" + "this." + propertyName + " = " + propertyName + ";\n" + "}");
        addProperty(type, propertyName, getterSource, setterSource);
        assert isConsistent();
    }

    private ModelException pre__move(PackageWeavepoint target) {
        if (target.contains(getName())) return new NameclashException("Type with name " + getName() + " already exists in package " + target.getFullName());
        return null;
    }

    public boolean canMove(PackageWeavepoint target) {
        return (pre__move(target) == null);
    }

    public void move(PackageWeavepoint target) throws ModelException {
        if (this.getSurroundingPackage() == target) {
            return;
        }
        if (isChecked()) {
            ModelException me = pre__move(target);
            if (me != null) throw me;
        }
        getModel().getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
        RecoderPackage targetPkg = (RecoderPackage) target;
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        CompilationUnit cu = UnitKit.getCompilationUnit(recoderClass);
        List classesToMove = RecoderUtil.getTypeWeavepoints(root, cu.getDeclarations());
        TypeReferenceMutableList allReferences = new TypeReferenceArrayList();
        Iterator iter = classesToMove.iterator();
        while (iter.hasNext()) {
            RecoderClass tmp = (RecoderClass) iter.next();
            allReferences.add(root.crossReferencer.getReferences(tmp.getClassType()));
            Type at = tmp.getClassType();
            while ((at = root.nameInfo.getArrayType(at)) != null) {
                allReferences.add(root.crossReferencer.getReferences(at));
            }
        }
        iter = classesToMove.iterator();
        while (iter.hasNext()) {
            RecoderClass tmp = (RecoderClass) iter.next();
            root.unregisterClass(tmp);
        }
        recoder.abstraction.Package newPkg = targetPkg.getRecoderPackage();
        ProgramFactory factory = recoderClass.getFactory();
        PackageSpecification packSpec = factory.createPackageSpecification(PackageKit.createPackageReference(factory, newPkg));
        PackageSpecification oldPkgSpec = cu.getPackageSpecification();
        cu.replaceChild(oldPkgSpec, packSpec);
        root.changeHistory.replaced(oldPkgSpec, packSpec);
        iter = classesToMove.iterator();
        while (iter.hasNext()) {
            RecoderClass tmp = (RecoderClass) iter.next();
            root.registerClass(tmp);
        }
        ensureImports();
        HashSet cuHash = new HashSet();
        PackageReference proto = PackageKit.createPackageReference(factory, recoderClass.getPackage());
        for (int i = 0, max = allReferences.size(); i < max; i++) {
            PackageReference oldpr;
            TypeReference tr = allReferences.getTypeReference(i);
            if ((oldpr = tr.getPackageReference()) != null) {
                PackageReference newpr = (PackageReference) proto.deepClone();
                oldpr.getASTParent().replaceChild(oldpr, newpr);
                root.changeHistory.replaced(oldpr, newpr);
            } else {
                cuHash.add(UnitKit.getCompilationUnit(tr));
            }
        }
        Import importProto = factory.createImport(TypeKit.createTypeReference(factory, recoderClass.getFullName()), false);
        iter = cuHash.iterator();
        while (iter.hasNext()) {
            CompilationUnit ncu = (CompilationUnit) iter.next();
            ImportMutableList il = ncu.getImports();
            Import newImport = (Import) importProto.deepClone();
            newImport.setParent(ncu);
            il.add(newImport);
            root.changeHistory.attached(newImport);
        }
        iter = cuHash.iterator();
        while (iter.hasNext()) {
            CompilationUnit ncu = (CompilationUnit) iter.next();
            ((RecoderType) root.getTypeWeavepoint(ncu.getTypeDeclarationAt(0).getFullName())).ensureImports();
        }
        assert isConsistent();
    }

    List getReferencedTypes() {
        List result = new ArrayList();
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        result = RecoderHelper.computeReferencedTypes(recoderClass, root);
        return result;
    }

    public void ensureImports() {
        RecoderHelper.addImports(this, getReferencedTypes(), true, true);
    }

    public void normalizeImports() {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        RecoderHelper.normalizeImports(root, recoderClass, true, true, false, false);
    }

    /**
     * @param javaSource
     * @throws ModelException
     * @see de.fzi.injectj.model.ClassWeavepoint#addMethod(de.fzi.injectj.model.SourceFragment)
     */
    public MethodWeavepoint addMethod(FragmentType javaSource) throws ModelException {
        ((RecoderRoot) getModel().getRoot()).getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
        MethodDeclaration m = RecoderHelper.isMethodDeclaration(javaSource);
        if (m == null) throw new SyntaxException("Source fragment " + javaSource.getSourceString() + " is not a valid method for class " + getFullName());
        addMember(m);
        RecoderMethod rm = new RecoderMethod(this, m);
        methodAdded(rm);
        assert rm.isConsistent();
        assert isConsistent();
        return rm;
    }

    /**
     * @param javaSource
     * @throws ModelException
     * @see de.fzi.injectj.model.ClassWeavepoint#addAttribute(de.fzi.injectj.model.SourceFragment)
     */
    public AttributeWeavepoint addAttribute(FragmentType javaSource) throws ModelException {
        FieldDeclaration f = RecoderHelper.isFieldDeclaration(javaSource);
        if (f == null) throw new SyntaxException("Source fragment " + javaSource.getSourceString() + " is not a valid attribute for class " + getFullName());
        FieldSpecificationMutableList fieldList = f.getFieldSpecifications();
        if (fieldList.size() != 1) throw new SyntaxException("The fragment does not represent a valid attribute (<modifiers> <type> <attName> [= <initExpr]; ): " + javaSource.toString());
        ((RecoderRoot) getModel().getRoot()).getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
        addMember(f);
        FieldSpecification fieldSpec = fieldList.getFieldSpecification(0);
        RecoderAttribute ra = new RecoderAttribute(this, fieldSpec);
        attributeAdded(ra);
        assert ra.isConsistent();
        assert isConsistent();
        return ra;
    }

    /**
     * callers should handle transaction rollback. This method does NOT take care
     * of rolling back changed on the recoder AST!
     * @param m the member to be added.
     */
    private void addMember(MemberDeclaration m) {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        MemberDeclarationMutableList members = recoderClass.getMembers();
        if (members == null) {
            recoderClass.setMembers(new MemberDeclarationArrayList());
        }
        members.add(m);
        m.setMemberParent(recoderClass);
        root.changeHistory.attached(m);
    }

    private ModelException pre__move(TypeWeavepoint target) {
        throw new RuntimeException("not implemented yet");
    }

    /**
     * @param target
     * @return
     * @see de.fzi.injectj.model.MemberWeavepoint#canMove(de.fzi.injectj.model.ClassWeavepoint)
     */
    public boolean canMove(TypeWeavepoint target) {
        return pre__move(target) == null;
    }

    /**
     * @param target
     * @throws ModelException
     * @see de.fzi.injectj.model.MemberWeavepoint#move(de.fzi.injectj.model.ClassWeavepoint)
     */
    public void move(TypeWeavepoint target) throws ModelException {
        if (isChecked()) {
            ModelException me = pre__move(target);
            if (me != null) throw me;
        }
        throw new RuntimeException("Not implemented yet: RecoderSourceClass.move()");
    }

    public void addInterface(FragmentType source) throws ModelException {
        throw new RuntimeException("Not implemented yet: RecoderSourceClass.addInterface()");
    }

    /**
     * @throws ModelException
     * @see de.fzi.injectj.model.ClassWeavepoint#makeConcrete()
     */
    public void makeConcrete() throws ModelException {
        if (!isAbstract()) return;
        setModifiers("!abstract");
        RecoderTypeHelper.implementAbstractMethods(this, null);
        assert isConsistent();
    }

    public List getLeadingComments() {
        return RecoderHelper.getLeadingComments(this);
    }

    public void setLeadingComments(List comments) {
        ((RecoderRoot) getModel().getRoot()).getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
        RecoderHelper.setLeadingComments(this, comments);
    }

    public List getTailingComments() {
        return RecoderHelper.getTailingComments(this);
    }

    public void setTailingComments(List comments) {
        ((RecoderRoot) getModel().getRoot()).getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
        RecoderHelper.setTailingComments(this, comments);
    }

    public TypeWeavepoint getSurroundingType() {
        return RecoderUtil.findParentTypeWeavepoint((RecoderRoot) getModel().getRoot(), recoderClass);
    }

    public MethodType getSurroundingMethod() {
        return RecoderUtil.findParentMethod((RecoderRoot) getModel().getRoot(), recoderClass);
    }

    public boolean isAnonymousClass() {
        return (recoderClass.getContainingClassType() == null) && (recoderClass.getStatementContainer() == null) && (recoderClass.getName() == null);
    }

    public boolean isLocalType() {
        return (recoderClass.getContainingClassType() == null) && (recoderClass.getStatementContainer() != null) && (recoderClass.getName() != null);
    }

    private ModelException pre__setModifier(ModifierCombination mc) {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        if (mc.setFinal) {
            ClassTypeList subtypes = root.crossReferencer.getSubtypes(recoderClass);
            if (subtypes != null && subtypes.size() > 0) {
                return new ReferenceException(CodeMapper.getText("CANT_MAKE_CLASS_FINAL"));
            }
        }
        if ((mc.setPublic) && !isTopLevelClass() && !isMemberClass()) {
            return new ReferenceException(CodeMapper.getText("CANT_MAKE_CLASS_PUBLIC"));
        }
        if (!isMemberClass()) {
            if (mc.setProtected) return new ReferenceException(CodeMapper.getText("CANT_MAKE_CLASS_PROTECTED"));
            if (mc.setPrivate) return new ReferenceException(CodeMapper.getText("CANT_MAKE_CLASS_PRIVATE"));
            if (mc.setStatic) return new ReferenceException(CodeMapper.getText("CANT_MAKE_CLASS_STATIC"));
        }
        return null;
    }

    public void setModifiers(String source) throws ModelException {
        ModifierCombination mc = new ModifierCombination(source, this);
        String dummySource = mc.resolveModifiers(this);
        if (isChecked()) {
            ModelException me = pre__setModifier(mc);
            if (me != null) throw me;
        }
        dummySource += " class _dummyClass {}";
        TypeDeclaration dummyClass = null;
        ProgramFactory factory = recoderClass.getFactory();
        try {
            ((RecoderRoot) getModel().getRoot()).getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
            dummyClass = factory.parseTypeDeclaration(dummySource);
            recoderClass.setModifiers(dummyClass.getModifiers());
            recoderClass.makeParentRoleValid();
        } catch (ParserException e) {
            throw new IllegalArgumentException("Invalid modifier specified in '" + source + "'");
        }
        assert isConsistent();
    }

    public void addMemberInterface(String modifierList, String interfaceName, FragmentType source) throws ModelException {
        ((RecoderRoot) getModel().getRoot()).getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
        RecoderTypeHelper.addTypeToType(this, modifierList, interfaceName, true, source);
        assert isConsistent();
        assert ((RecoderRoot) getModel().getRoot()).assertConsistency();
    }

    public void addMemberClass(String modifierCombination, String className, FragmentType classSource) throws ModelException {
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        root.getTransactionManager().addToNotifyOnRollback(new RollbackNotSupportedHandler());
        if (className.indexOf('.') != -1) throw new InvalidIdentifierException("PACKAGE_IDENTIFIER_NOT_ALLOWED");
        String source = modifierCombination + " class " + className + "\n{\n" + classSource.getSourceString() + "\n}\n";
        TypeDeclaration newClass = null;
        try {
            newClass = recoderClass.getFactory().parseTypeDeclaration(source);
            MiscKit.unindent(newClass);
        } catch (ParserException e) {
            String message = CodeMapper.getText("SYNTAX_ERROR_IN_JAVA_CODE", source);
            throw new SyntaxException(message);
        }
        assert newClass instanceof ClassDeclaration;
        MemberDeclarationMutableList memberList = recoderClass.getMembers();
        memberList.add(newClass);
        newClass.setParent(recoderClass);
        root.changeHistory.attached(newClass);
        if (memberTypes != null) {
            RecoderSourceClass rsc = new RecoderSourceClass(this, (ClassDeclaration) newClass);
            memberTypes.add(rsc);
        }
        assert isConsistent();
        assert root.assertConsistency();
    }

    public boolean canAddMemberClass(String modifierList, String className, FragmentType source) {
        return RecoderTypeHelper.pre__addTypeToType(this, modifierList, className, false, source) == null;
    }

    public boolean canAddMemberInterface(String modifierList, String interfaceName, FragmentType source) {
        return RecoderTypeHelper.pre__addTypeToType(this, modifierList, interfaceName, true, source) == null;
    }

    /**
     * @see de.fzi.injectj.model.TypeWeavepoint#isPrimitiveType()
     */
    public boolean isPrimitiveType() {
        return false;
    }

    public boolean isConsistent() {
        if (super.isConsistent() == false) return false;
        return recoderClass != null;
    }

    public Annotation getAnnotation() throws AnnotationException {
        return RecoderCommentsHelper.getAnnotation(this);
    }

    public void setAnnotation(List listOfList) throws AnnotationException {
        RecoderAnnotation annotation = new RecoderAnnotation(this, listOfList);
    }

    public boolean canReplace(FragmentType source) {
        return false;
    }

    public int getColumn() {
        return RecoderUtil.getColumn(recoderClass);
    }

    public int getLine() {
        return RecoderUtil.getLine(recoderClass);
    }

    public String getPosition() {
        return RecoderUtil.getPosition(recoderClass);
    }

    public PackageWeavepoint getSurroundingPackage() {
        return RecoderUtil.findParentPackageWeavepoint((RecoderRoot) getModel().getRoot(), recoderClass);
    }

    public SourceElement replace(FragmentType source) throws ModelException {
        return null;
    }

    private boolean isMemberClass() {
        return (recoderClass.getContainingClassType() instanceof ClassDeclaration) && (recoderClass.getStatementContainer() == null) && (recoderClass.getName() != null);
    }

    private boolean isTopLevelClass() {
        return ((recoderClass.getContainingClassType() instanceof recoder.abstraction.Package) || (recoderClass.getContainingClassType() == null)) && (recoderClass.getStatementContainer() == null) && (recoderClass.getName() != null);
    }
}
