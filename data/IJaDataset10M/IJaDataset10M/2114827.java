package org.eclipse.jdt.internal.compiler;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.problem.*;

public class DocumentElementParser extends Parser {

    IDocumentElementRequestor requestor;

    private int localIntPtr;

    private int lastFieldEndPosition;

    private int lastFieldBodyEndPosition;

    private int typeStartPosition;

    private long selectorSourcePositions;

    private int typeDims;

    private int extendsDim;

    private int declarationSourceStart;

    int[][] intArrayStack;

    int intArrayPtr;

    public DocumentElementParser(final IDocumentElementRequestor requestor, IProblemFactory problemFactory, CompilerOptions options) {
        super(new ProblemReporter(DefaultErrorHandlingPolicies.exitAfterAllProblems(), options, problemFactory), false);
        this.requestor = requestor;
        intArrayStack = new int[30][];
        this.options = options;
        this.javadocParser.checkDocComment = false;
        this.setMethodsFullRecovery(false);
        this.setStatementsRecovery(false);
    }

    public void checkComment() {
        pushOnIntArrayStack(this.getJavaDocPositions());
        boolean deprecated = false;
        int lastCommentIndex = -1;
        int commentPtr = scanner.commentPtr;
        nextComment: for (lastCommentIndex = scanner.commentPtr; lastCommentIndex >= 0; lastCommentIndex--) {
            int commentSourceStart = scanner.commentStarts[lastCommentIndex];
            if (modifiersSourceStart != -1 && modifiersSourceStart < commentSourceStart) {
                continue nextComment;
            }
            if (scanner.commentStops[lastCommentIndex] < 0) {
                continue nextComment;
            }
            deprecated = this.javadocParser.checkDeprecation(lastCommentIndex);
            break nextComment;
        }
        if (deprecated) {
            checkAndSetModifiers(ClassFileConstants.AccDeprecated);
        }
        if (commentPtr >= 0) {
            declarationSourceStart = scanner.commentStarts[0];
        }
    }

    protected void consumeClassBodyDeclaration() {
        super.consumeClassBodyDeclaration();
        Initializer initializer = (Initializer) astStack[astPtr];
        requestor.acceptInitializer(initializer.declarationSourceStart, initializer.declarationSourceEnd, intArrayStack[intArrayPtr--], 0, modifiersSourceStart, initializer.block.sourceStart, initializer.block.sourceEnd);
    }

    protected void consumeClassDeclaration() {
        super.consumeClassDeclaration();
        if (isLocalDeclaration()) {
            return;
        }
        requestor.exitClass(endStatementPosition, ((TypeDeclaration) astStack[astPtr]).declarationSourceEnd);
    }

    protected void consumeClassHeader() {
        super.consumeClassHeader();
        if (isLocalDeclaration()) {
            intArrayPtr--;
            return;
        }
        TypeDeclaration typeDecl = (TypeDeclaration) astStack[astPtr];
        TypeReference[] superInterfaces = typeDecl.superInterfaces;
        char[][] interfaceNames = null;
        int[] interfaceNameStarts = null;
        int[] interfaceNameEnds = null;
        if (superInterfaces != null) {
            int superInterfacesLength = superInterfaces.length;
            interfaceNames = new char[superInterfacesLength][];
            interfaceNameStarts = new int[superInterfacesLength];
            interfaceNameEnds = new int[superInterfacesLength];
            for (int i = 0; i < superInterfacesLength; i++) {
                TypeReference superInterface = superInterfaces[i];
                interfaceNames[i] = CharOperation.concatWith(superInterface.getTypeName(), '.');
                interfaceNameStarts[i] = superInterface.sourceStart;
                interfaceNameEnds[i] = superInterface.sourceEnd;
            }
        }
        scanner.commentPtr = -1;
        TypeReference superclass = typeDecl.superclass;
        if (superclass == null) {
            requestor.enterClass(typeDecl.declarationSourceStart, intArrayStack[intArrayPtr--], typeDecl.modifiers, typeDecl.modifiersSourceStart, typeStartPosition, typeDecl.name, typeDecl.sourceStart, typeDecl.sourceEnd, null, -1, -1, interfaceNames, interfaceNameStarts, interfaceNameEnds, scanner.currentPosition - 1);
        } else {
            requestor.enterClass(typeDecl.declarationSourceStart, intArrayStack[intArrayPtr--], typeDecl.modifiers, typeDecl.modifiersSourceStart, typeStartPosition, typeDecl.name, typeDecl.sourceStart, typeDecl.sourceEnd, CharOperation.concatWith(superclass.getTypeName(), '.'), superclass.sourceStart, superclass.sourceEnd, interfaceNames, interfaceNameStarts, interfaceNameEnds, scanner.currentPosition - 1);
        }
    }

    protected void consumeClassHeaderName1() {
        TypeDeclaration typeDecl = new TypeDeclaration(this.compilationUnit.compilationResult);
        if (nestedMethod[nestedType] == 0) {
            if (nestedType != 0) {
                typeDecl.bits |= ASTNode.IsMemberType;
            }
        } else {
            typeDecl.bits |= ASTNode.IsLocalType;
            markEnclosingMemberWithLocalType();
            blockReal();
        }
        long pos = identifierPositionStack[identifierPtr];
        typeDecl.sourceEnd = (int) pos;
        typeDecl.sourceStart = (int) (pos >>> 32);
        typeDecl.name = identifierStack[identifierPtr--];
        identifierLengthPtr--;
        typeStartPosition = typeDecl.declarationSourceStart = intStack[intPtr--];
        intPtr--;
        int declSourceStart = intStack[intPtr--];
        typeDecl.modifiersSourceStart = intStack[intPtr--];
        typeDecl.modifiers = intStack[intPtr--];
        if (typeDecl.declarationSourceStart > declSourceStart) {
            typeDecl.declarationSourceStart = declSourceStart;
        }
        int length;
        if ((length = this.expressionLengthStack[this.expressionLengthPtr--]) != 0) {
            System.arraycopy(this.expressionStack, (this.expressionPtr -= length) + 1, typeDecl.annotations = new Annotation[length], 0, length);
        }
        typeDecl.bodyStart = typeDecl.sourceEnd + 1;
        pushOnAstStack(typeDecl);
        typeDecl.javadoc = this.javadoc;
        this.javadoc = null;
    }

    protected void consumeCompilationUnit() {
        requestor.exitCompilationUnit(scanner.source.length - 1);
    }

    protected void consumeConstructorDeclaration() {
        super.consumeConstructorDeclaration();
        if (isLocalDeclaration()) {
            return;
        }
        ConstructorDeclaration cd = (ConstructorDeclaration) astStack[astPtr];
        requestor.exitConstructor(endStatementPosition, cd.declarationSourceEnd);
    }

    protected void consumeConstructorHeader() {
        super.consumeConstructorHeader();
        if (isLocalDeclaration()) {
            intArrayPtr--;
            return;
        }
        ConstructorDeclaration cd = (ConstructorDeclaration) astStack[astPtr];
        Argument[] arguments = cd.arguments;
        char[][] argumentTypes = null;
        char[][] argumentNames = null;
        int[] argumentTypeStarts = null;
        int[] argumentTypeEnds = null;
        int[] argumentNameStarts = null;
        int[] argumentNameEnds = null;
        if (arguments != null) {
            int argumentLength = arguments.length;
            argumentTypes = new char[argumentLength][];
            argumentNames = new char[argumentLength][];
            argumentNameStarts = new int[argumentLength];
            argumentNameEnds = new int[argumentLength];
            argumentTypeStarts = new int[argumentLength];
            argumentTypeEnds = new int[argumentLength];
            for (int i = 0; i < argumentLength; i++) {
                Argument argument = arguments[i];
                TypeReference argumentType = argument.type;
                argumentTypes[i] = returnTypeName(argumentType);
                argumentNames[i] = argument.name;
                argumentNameStarts[i] = argument.sourceStart;
                argumentNameEnds[i] = argument.sourceEnd;
                argumentTypeStarts[i] = argumentType.sourceStart;
                argumentTypeEnds[i] = argumentType.sourceEnd;
            }
        }
        TypeReference[] thrownExceptions = cd.thrownExceptions;
        char[][] exceptionTypes = null;
        int[] exceptionTypeStarts = null;
        int[] exceptionTypeEnds = null;
        if (thrownExceptions != null) {
            int thrownExceptionLength = thrownExceptions.length;
            exceptionTypes = new char[thrownExceptionLength][];
            exceptionTypeStarts = new int[thrownExceptionLength];
            exceptionTypeEnds = new int[thrownExceptionLength];
            for (int i = 0; i < thrownExceptionLength; i++) {
                TypeReference exception = thrownExceptions[i];
                exceptionTypes[i] = CharOperation.concatWith(exception.getTypeName(), '.');
                exceptionTypeStarts[i] = exception.sourceStart;
                exceptionTypeEnds[i] = exception.sourceEnd;
            }
        }
        requestor.enterConstructor(cd.declarationSourceStart, intArrayStack[intArrayPtr--], cd.modifiers, cd.modifiersSourceStart, cd.selector, cd.sourceStart, (int) (selectorSourcePositions & 0xFFFFFFFFL), argumentTypes, argumentTypeStarts, argumentTypeEnds, argumentNames, argumentNameStarts, argumentNameEnds, rParenPos, exceptionTypes, exceptionTypeStarts, exceptionTypeEnds, scanner.currentPosition - 1);
    }

    protected void consumeConstructorHeaderName() {
        ConstructorDeclaration cd = new ConstructorDeclaration(this.compilationUnit.compilationResult);
        cd.selector = identifierStack[identifierPtr];
        selectorSourcePositions = identifierPositionStack[identifierPtr--];
        identifierLengthPtr--;
        cd.declarationSourceStart = intStack[intPtr--];
        cd.modifiersSourceStart = intStack[intPtr--];
        cd.modifiers = intStack[intPtr--];
        int length;
        if ((length = this.expressionLengthStack[this.expressionLengthPtr--]) != 0) {
            System.arraycopy(this.expressionStack, (this.expressionPtr -= length) + 1, cd.annotations = new Annotation[length], 0, length);
        }
        cd.javadoc = this.javadoc;
        this.javadoc = null;
        cd.sourceStart = (int) (selectorSourcePositions >>> 32);
        pushOnAstStack(cd);
        cd.sourceEnd = lParenPos;
        cd.bodyStart = lParenPos + 1;
    }

    protected void consumeDefaultModifiers() {
        checkComment();
        pushOnIntStack(modifiers);
        pushOnIntStack(-1);
        pushOnIntStack(declarationSourceStart >= 0 ? declarationSourceStart : scanner.startPosition);
        resetModifiers();
        pushOnExpressionStackLengthStack(0);
    }

    protected void consumeDiet() {
        super.consumeDiet();
        pushOnIntArrayStack(this.getJavaDocPositions());
    }

    protected void consumeEnterCompilationUnit() {
        requestor.enterCompilationUnit();
    }

    protected void consumeEnterVariable() {
        boolean isLocalDeclaration = isLocalDeclaration();
        if (!isLocalDeclaration && (variablesCounter[nestedType] != 0)) {
            requestor.exitField(lastFieldBodyEndPosition, lastFieldEndPosition);
        }
        char[] varName = identifierStack[identifierPtr];
        long namePosition = identifierPositionStack[identifierPtr--];
        int extendedTypeDimension = intStack[intPtr--];
        AbstractVariableDeclaration declaration;
        if (nestedMethod[nestedType] != 0) {
            declaration = new LocalDeclaration(varName, (int) (namePosition >>> 32), (int) namePosition);
        } else {
            declaration = new FieldDeclaration(varName, (int) (namePosition >>> 32), (int) namePosition);
        }
        identifierLengthPtr--;
        TypeReference type;
        int variableIndex = variablesCounter[nestedType];
        int typeDim = 0;
        if (variableIndex == 0) {
            if (nestedMethod[nestedType] != 0) {
                declaration.declarationSourceStart = intStack[intPtr--];
                declaration.modifiersSourceStart = intStack[intPtr--];
                declaration.modifiers = intStack[intPtr--];
                type = getTypeReference(typeDim = intStack[intPtr--]);
                pushOnAstStack(type);
            } else {
                type = getTypeReference(typeDim = intStack[intPtr--]);
                pushOnAstStack(type);
                declaration.declarationSourceStart = intStack[intPtr--];
                declaration.modifiersSourceStart = intStack[intPtr--];
                declaration.modifiers = intStack[intPtr--];
            }
            int length;
            if ((length = this.expressionLengthStack[this.expressionLengthPtr--]) != 0) {
                System.arraycopy(this.expressionStack, (this.expressionPtr -= length) + 1, declaration.annotations = new Annotation[length], 0, length);
            }
        } else {
            type = (TypeReference) astStack[astPtr - variableIndex];
            typeDim = type.dimensions();
            AbstractVariableDeclaration previousVariable = (AbstractVariableDeclaration) astStack[astPtr];
            declaration.declarationSourceStart = previousVariable.declarationSourceStart;
            declaration.modifiers = previousVariable.modifiers;
            declaration.modifiersSourceStart = previousVariable.modifiersSourceStart;
            final Annotation[] annotations = previousVariable.annotations;
            if (annotations != null) {
                final int annotationsLength = annotations.length;
                System.arraycopy(annotations, 0, declaration.annotations = new Annotation[annotationsLength], 0, annotationsLength);
            }
        }
        localIntPtr = intPtr;
        if (extendedTypeDimension == 0) {
            declaration.type = type;
        } else {
            int dimension = typeDim + extendedTypeDimension;
            declaration.type = this.copyDims(type, dimension);
        }
        variablesCounter[nestedType]++;
        nestedMethod[nestedType]++;
        pushOnAstStack(declaration);
        int[] javadocPositions = intArrayStack[intArrayPtr];
        if (!isLocalDeclaration) {
            requestor.enterField(declaration.declarationSourceStart, javadocPositions, declaration.modifiers, declaration.modifiersSourceStart, returnTypeName(declaration.type), type.sourceStart, type.sourceEnd, typeDims, varName, (int) (namePosition >>> 32), (int) namePosition, extendedTypeDimension, extendedTypeDimension == 0 ? -1 : endPosition);
        }
    }

    protected void consumeExitVariableWithInitialization() {
        super.consumeExitVariableWithInitialization();
        nestedMethod[nestedType]--;
        lastFieldEndPosition = scanner.currentPosition - 1;
        lastFieldBodyEndPosition = ((AbstractVariableDeclaration) astStack[astPtr]).initialization.sourceEnd;
    }

    protected void consumeExitVariableWithoutInitialization() {
        super.consumeExitVariableWithoutInitialization();
        nestedMethod[nestedType]--;
        lastFieldEndPosition = scanner.currentPosition - 1;
        lastFieldBodyEndPosition = scanner.startPosition - 1;
    }

    protected void consumeFieldDeclaration() {
        int variableIndex = variablesCounter[nestedType];
        super.consumeFieldDeclaration();
        intArrayPtr--;
        if (isLocalDeclaration()) return;
        if (variableIndex != 0) {
            requestor.exitField(lastFieldBodyEndPosition, lastFieldEndPosition);
        }
    }

    protected void consumeFormalParameter(boolean isVarArgs) {
        identifierLengthPtr--;
        char[] parameterName = identifierStack[identifierPtr];
        long namePositions = identifierPositionStack[identifierPtr--];
        int extendedDimensions = this.intStack[this.intPtr--];
        int endOfEllipsis = 0;
        if (isVarArgs) {
            endOfEllipsis = this.intStack[this.intPtr--];
        }
        int firstDimensions = this.intStack[this.intPtr--];
        final int typeDimensions = firstDimensions + extendedDimensions;
        TypeReference type = getTypeReference(typeDimensions);
        if (isVarArgs) {
            type = copyDims(type, typeDimensions + 1);
            if (extendedDimensions == 0) {
                type.sourceEnd = endOfEllipsis;
            }
            type.bits |= ASTNode.IsVarArgs;
        }
        intPtr -= 3;
        Argument arg = new Argument(parameterName, namePositions, type, intStack[intPtr + 1]);
        int length;
        if ((length = this.expressionLengthStack[this.expressionLengthPtr--]) != 0) {
            System.arraycopy(this.expressionStack, (this.expressionPtr -= length) + 1, arg.annotations = new Annotation[length], 0, length);
        }
        pushOnAstStack(arg);
        intArrayPtr--;
    }

    protected void consumeInterfaceDeclaration() {
        super.consumeInterfaceDeclaration();
        if (isLocalDeclaration()) {
            return;
        }
        requestor.exitInterface(endStatementPosition, ((TypeDeclaration) astStack[astPtr]).declarationSourceEnd);
    }

    protected void consumeInterfaceHeader() {
        super.consumeInterfaceHeader();
        if (isLocalDeclaration()) {
            intArrayPtr--;
            return;
        }
        TypeDeclaration typeDecl = (TypeDeclaration) astStack[astPtr];
        TypeReference[] superInterfaces = typeDecl.superInterfaces;
        char[][] interfaceNames = null;
        int[] interfaceNameStarts = null;
        int[] interfacenameEnds = null;
        int superInterfacesLength = 0;
        if (superInterfaces != null) {
            superInterfacesLength = superInterfaces.length;
            interfaceNames = new char[superInterfacesLength][];
            interfaceNameStarts = new int[superInterfacesLength];
            interfacenameEnds = new int[superInterfacesLength];
        }
        if (superInterfaces != null) {
            for (int i = 0; i < superInterfacesLength; i++) {
                TypeReference superInterface = superInterfaces[i];
                interfaceNames[i] = CharOperation.concatWith(superInterface.getTypeName(), '.');
                interfaceNameStarts[i] = superInterface.sourceStart;
                interfacenameEnds[i] = superInterface.sourceEnd;
            }
        }
        scanner.commentPtr = -1;
        requestor.enterInterface(typeDecl.declarationSourceStart, intArrayStack[intArrayPtr--], typeDecl.modifiers, typeDecl.modifiersSourceStart, typeStartPosition, typeDecl.name, typeDecl.sourceStart, typeDecl.sourceEnd, interfaceNames, interfaceNameStarts, interfacenameEnds, scanner.currentPosition - 1);
    }

    protected void consumeInterfaceHeaderName1() {
        TypeDeclaration typeDecl = new TypeDeclaration(this.compilationUnit.compilationResult);
        if (nestedMethod[nestedType] == 0) {
            if (nestedType != 0) {
                typeDecl.bits |= ASTNode.IsMemberType;
            }
        } else {
            typeDecl.bits |= ASTNode.IsLocalType;
            markEnclosingMemberWithLocalType();
            blockReal();
        }
        long pos = identifierPositionStack[identifierPtr];
        typeDecl.sourceEnd = (int) pos;
        typeDecl.sourceStart = (int) (pos >>> 32);
        typeDecl.name = identifierStack[identifierPtr--];
        identifierLengthPtr--;
        typeStartPosition = typeDecl.declarationSourceStart = intStack[intPtr--];
        intPtr--;
        int declSourceStart = intStack[intPtr--];
        typeDecl.modifiersSourceStart = intStack[intPtr--];
        typeDecl.modifiers = this.intStack[this.intPtr--] | ClassFileConstants.AccInterface;
        if (typeDecl.declarationSourceStart > declSourceStart) {
            typeDecl.declarationSourceStart = declSourceStart;
        }
        int length;
        if ((length = this.expressionLengthStack[this.expressionLengthPtr--]) != 0) {
            System.arraycopy(this.expressionStack, (this.expressionPtr -= length) + 1, typeDecl.annotations = new Annotation[length], 0, length);
        }
        typeDecl.bodyStart = typeDecl.sourceEnd + 1;
        pushOnAstStack(typeDecl);
        typeDecl.javadoc = this.javadoc;
        this.javadoc = null;
    }

    protected void consumeInternalCompilationUnit() {
    }

    protected void consumeInternalCompilationUnitWithTypes() {
        int length;
        if ((length = this.astLengthStack[this.astLengthPtr--]) != 0) {
            this.compilationUnit.types = new TypeDeclaration[length];
            this.astPtr -= length;
            System.arraycopy(this.astStack, this.astPtr + 1, this.compilationUnit.types, 0, length);
        }
    }

    protected void consumeLocalVariableDeclaration() {
        super.consumeLocalVariableDeclaration();
        intArrayPtr--;
    }

    protected void consumeMethodDeclaration(boolean isNotAbstract) {
        super.consumeMethodDeclaration(isNotAbstract);
        if (isLocalDeclaration()) {
            return;
        }
        MethodDeclaration md = (MethodDeclaration) astStack[astPtr];
        requestor.exitMethod(endStatementPosition, md.declarationSourceEnd);
    }

    protected void consumeMethodHeader() {
        super.consumeMethodHeader();
        if (isLocalDeclaration()) {
            intArrayPtr--;
            return;
        }
        MethodDeclaration md = (MethodDeclaration) astStack[astPtr];
        TypeReference returnType = md.returnType;
        char[] returnTypeName = returnTypeName(returnType);
        Argument[] arguments = md.arguments;
        char[][] argumentTypes = null;
        char[][] argumentNames = null;
        int[] argumentTypeStarts = null;
        int[] argumentTypeEnds = null;
        int[] argumentNameStarts = null;
        int[] argumentNameEnds = null;
        if (arguments != null) {
            int argumentLength = arguments.length;
            argumentTypes = new char[argumentLength][];
            argumentNames = new char[argumentLength][];
            argumentNameStarts = new int[argumentLength];
            argumentNameEnds = new int[argumentLength];
            argumentTypeStarts = new int[argumentLength];
            argumentTypeEnds = new int[argumentLength];
            for (int i = 0; i < argumentLength; i++) {
                Argument argument = arguments[i];
                TypeReference argumentType = argument.type;
                argumentTypes[i] = returnTypeName(argumentType);
                argumentNames[i] = argument.name;
                argumentNameStarts[i] = argument.sourceStart;
                argumentNameEnds[i] = argument.sourceEnd;
                argumentTypeStarts[i] = argumentType.sourceStart;
                argumentTypeEnds[i] = argumentType.sourceEnd;
            }
        }
        TypeReference[] thrownExceptions = md.thrownExceptions;
        char[][] exceptionTypes = null;
        int[] exceptionTypeStarts = null;
        int[] exceptionTypeEnds = null;
        if (thrownExceptions != null) {
            int thrownExceptionLength = thrownExceptions.length;
            exceptionTypeStarts = new int[thrownExceptionLength];
            exceptionTypeEnds = new int[thrownExceptionLength];
            exceptionTypes = new char[thrownExceptionLength][];
            for (int i = 0; i < thrownExceptionLength; i++) {
                TypeReference exception = thrownExceptions[i];
                exceptionTypes[i] = CharOperation.concatWith(exception.getTypeName(), '.');
                exceptionTypeStarts[i] = exception.sourceStart;
                exceptionTypeEnds[i] = exception.sourceEnd;
            }
        }
        requestor.enterMethod(md.declarationSourceStart, intArrayStack[intArrayPtr--], md.modifiers, md.modifiersSourceStart, returnTypeName, returnType.sourceStart, returnType.sourceEnd, typeDims, md.selector, md.sourceStart, (int) (selectorSourcePositions & 0xFFFFFFFFL), argumentTypes, argumentTypeStarts, argumentTypeEnds, argumentNames, argumentNameStarts, argumentNameEnds, rParenPos, extendsDim, extendsDim == 0 ? -1 : endPosition, exceptionTypes, exceptionTypeStarts, exceptionTypeEnds, scanner.currentPosition - 1);
    }

    protected void consumeMethodHeaderExtendedDims() {
        MethodDeclaration md = (MethodDeclaration) astStack[astPtr];
        int extendedDims = intStack[intPtr--];
        extendsDim = extendedDims;
        if (extendedDims != 0) {
            TypeReference returnType = md.returnType;
            md.sourceEnd = endPosition;
            int dims = returnType.dimensions() + extendedDims;
            md.returnType = this.copyDims(returnType, dims);
            if (currentToken == TokenNameLBRACE) {
                md.bodyStart = endPosition + 1;
            }
        }
    }

    protected void consumeMethodHeaderName(boolean isAnnotationMethod) {
        MethodDeclaration md = null;
        if (isAnnotationMethod) {
            md = new AnnotationMethodDeclaration(this.compilationUnit.compilationResult);
        } else {
            md = new MethodDeclaration(this.compilationUnit.compilationResult);
        }
        md.selector = identifierStack[identifierPtr];
        selectorSourcePositions = identifierPositionStack[identifierPtr--];
        identifierLengthPtr--;
        md.returnType = getTypeReference(typeDims = intStack[intPtr--]);
        md.declarationSourceStart = intStack[intPtr--];
        md.modifiersSourceStart = intStack[intPtr--];
        md.modifiers = intStack[intPtr--];
        int length;
        if ((length = this.expressionLengthStack[this.expressionLengthPtr--]) != 0) {
            System.arraycopy(this.expressionStack, (this.expressionPtr -= length) + 1, md.annotations = new Annotation[length], 0, length);
        }
        md.javadoc = this.javadoc;
        this.javadoc = null;
        md.sourceStart = (int) (selectorSourcePositions >>> 32);
        pushOnAstStack(md);
        md.bodyStart = scanner.currentPosition - 1;
    }

    protected void consumeModifiers() {
        checkComment();
        pushOnIntStack(modifiers);
        pushOnIntStack(modifiersSourceStart);
        pushOnIntStack(declarationSourceStart >= 0 ? declarationSourceStart : modifiersSourceStart);
        resetModifiers();
    }

    protected void consumePackageDeclarationName() {
        pushOnIntArrayStack(this.getJavaDocPositions());
        super.consumePackageDeclarationName();
        ImportReference importReference = compilationUnit.currentPackage;
        requestor.acceptPackage(importReference.declarationSourceStart, importReference.declarationSourceEnd, intArrayStack[intArrayPtr--], CharOperation.concatWith(importReference.getImportName(), '.'), importReference.sourceStart);
    }

    protected void consumePackageDeclarationNameWithModifiers() {
        pushOnIntArrayStack(this.getJavaDocPositions());
        super.consumePackageDeclarationNameWithModifiers();
        ImportReference importReference = compilationUnit.currentPackage;
        requestor.acceptPackage(importReference.declarationSourceStart, importReference.declarationSourceEnd, intArrayStack[intArrayPtr--], CharOperation.concatWith(importReference.getImportName(), '.'), importReference.sourceStart);
    }

    protected void consumePushModifiers() {
        checkComment();
        pushOnIntStack(modifiers);
        if (modifiersSourceStart < 0) {
            pushOnIntStack(-1);
            pushOnIntStack(declarationSourceStart >= 0 ? declarationSourceStart : scanner.startPosition);
        } else {
            pushOnIntStack(modifiersSourceStart);
            pushOnIntStack(declarationSourceStart >= 0 ? declarationSourceStart : modifiersSourceStart);
        }
        resetModifiers();
        pushOnExpressionStackLengthStack(0);
    }

    protected void consumePushRealModifiers() {
        checkComment();
        pushOnIntStack(modifiers);
        if (modifiersSourceStart < 0) {
            pushOnIntStack(-1);
            pushOnIntStack(declarationSourceStart >= 0 ? declarationSourceStart : scanner.startPosition);
        } else {
            pushOnIntStack(modifiersSourceStart);
            pushOnIntStack(declarationSourceStart >= 0 ? declarationSourceStart : modifiersSourceStart);
        }
        resetModifiers();
    }

    protected void consumeSingleStaticImportDeclarationName() {
        pushOnIntArrayStack(this.getJavaDocPositions());
        super.consumeSingleStaticImportDeclarationName();
        ImportReference importReference = (ImportReference) astStack[astPtr];
        requestor.acceptImport(importReference.declarationSourceStart, importReference.declarationSourceEnd, intArrayStack[intArrayPtr--], CharOperation.concatWith(importReference.getImportName(), '.'), importReference.sourceStart, false, ClassFileConstants.AccStatic);
    }

    protected void consumeSingleTypeImportDeclarationName() {
        pushOnIntArrayStack(this.getJavaDocPositions());
        super.consumeSingleTypeImportDeclarationName();
        ImportReference importReference = (ImportReference) astStack[astPtr];
        requestor.acceptImport(importReference.declarationSourceStart, importReference.declarationSourceEnd, intArrayStack[intArrayPtr--], CharOperation.concatWith(importReference.getImportName(), '.'), importReference.sourceStart, false, ClassFileConstants.AccDefault);
    }

    protected void consumeStaticImportOnDemandDeclarationName() {
        pushOnIntArrayStack(this.getJavaDocPositions());
        super.consumeStaticImportOnDemandDeclarationName();
        ImportReference importReference = (ImportReference) astStack[astPtr];
        requestor.acceptImport(importReference.declarationSourceStart, importReference.declarationSourceEnd, intArrayStack[intArrayPtr--], CharOperation.concatWith(importReference.getImportName(), '.'), importReference.sourceStart, true, ClassFileConstants.AccStatic);
    }

    protected void consumeStaticInitializer() {
        super.consumeStaticInitializer();
        Initializer initializer = (Initializer) astStack[astPtr];
        requestor.acceptInitializer(initializer.declarationSourceStart, initializer.declarationSourceEnd, intArrayStack[intArrayPtr--], ClassFileConstants.AccStatic, intStack[intPtr--], initializer.block.sourceStart, initializer.declarationSourceEnd);
    }

    protected void consumeStaticOnly() {
        checkComment();
        pushOnIntStack(modifiersSourceStart);
        pushOnIntStack(scanner.currentPosition);
        pushOnIntStack(declarationSourceStart >= 0 ? declarationSourceStart : modifiersSourceStart);
        jumpOverMethodBody();
        nestedMethod[nestedType]++;
        resetModifiers();
    }

    protected void consumeTypeImportOnDemandDeclarationName() {
        pushOnIntArrayStack(this.getJavaDocPositions());
        super.consumeTypeImportOnDemandDeclarationName();
        ImportReference importReference = (ImportReference) astStack[astPtr];
        requestor.acceptImport(importReference.declarationSourceStart, importReference.declarationSourceEnd, intArrayStack[intArrayPtr--], CharOperation.concatWith(importReference.getImportName(), '.'), importReference.sourceStart, true, ClassFileConstants.AccDefault);
    }

    public int flushCommentsDefinedPriorTo(int position) {
        return lastFieldEndPosition = super.flushCommentsDefinedPriorTo(position);
    }

    public CompilationUnitDeclaration endParse(int act) {
        if (scanner.recordLineSeparator) {
            requestor.acceptLineSeparatorPositions(scanner.getLineEnds());
        }
        return super.endParse(act);
    }

    public void initialize(boolean initializeNLS) {
        super.initialize(initializeNLS);
        intArrayPtr = -1;
    }

    public void initialize() {
        super.initialize();
        intArrayPtr = -1;
    }

    private boolean isLocalDeclaration() {
        int nestedDepth = nestedType;
        while (nestedDepth >= 0) {
            if (nestedMethod[nestedDepth] != 0) {
                return true;
            }
            nestedDepth--;
        }
        return false;
    }

    protected void parse() {
        this.diet = true;
        super.parse();
    }

    public void parseCompilationUnit(ICompilationUnit unit) {
        char[] regionSource = unit.getContents();
        try {
            initialize(true);
            goForCompilationUnit();
            referenceContext = compilationUnit = new CompilationUnitDeclaration(problemReporter(), new CompilationResult(unit, 0, 0, this.options.maxProblemsPerUnit), regionSource.length);
            scanner.resetTo(0, regionSource.length);
            scanner.setSource(regionSource);
            parse();
        } catch (AbortCompilation ex) {
        }
    }

    public void parseConstructor(char[] regionSource) {
        try {
            initialize();
            goForClassBodyDeclarations();
            referenceContext = compilationUnit = new CompilationUnitDeclaration(problemReporter(), new CompilationResult(regionSource, 0, 0, this.options.maxProblemsPerUnit), regionSource.length);
            scanner.resetTo(0, regionSource.length);
            scanner.setSource(regionSource);
            parse();
        } catch (AbortCompilation ex) {
        }
    }

    public void parseField(char[] regionSource) {
        try {
            initialize();
            goForFieldDeclaration();
            referenceContext = compilationUnit = new CompilationUnitDeclaration(problemReporter(), new CompilationResult(regionSource, 0, 0, this.options.maxProblemsPerUnit), regionSource.length);
            scanner.resetTo(0, regionSource.length);
            scanner.setSource(regionSource);
            parse();
        } catch (AbortCompilation ex) {
        }
    }

    public void parseImport(char[] regionSource) {
        try {
            initialize();
            goForImportDeclaration();
            referenceContext = compilationUnit = new CompilationUnitDeclaration(problemReporter(), new CompilationResult(regionSource, 0, 0, this.options.maxProblemsPerUnit), regionSource.length);
            scanner.resetTo(0, regionSource.length);
            scanner.setSource(regionSource);
            parse();
        } catch (AbortCompilation ex) {
        }
    }

    public void parseInitializer(char[] regionSource) {
        try {
            initialize();
            goForInitializer();
            referenceContext = compilationUnit = new CompilationUnitDeclaration(problemReporter(), new CompilationResult(regionSource, 0, 0, this.options.maxProblemsPerUnit), regionSource.length);
            scanner.resetTo(0, regionSource.length);
            scanner.setSource(regionSource);
            parse();
        } catch (AbortCompilation ex) {
        }
    }

    public void parseMethod(char[] regionSource) {
        try {
            initialize();
            goForGenericMethodDeclaration();
            referenceContext = compilationUnit = new CompilationUnitDeclaration(problemReporter(), new CompilationResult(regionSource, 0, 0, this.options.maxProblemsPerUnit), regionSource.length);
            scanner.resetTo(0, regionSource.length);
            scanner.setSource(regionSource);
            parse();
        } catch (AbortCompilation ex) {
        }
    }

    public void parsePackage(char[] regionSource) {
        try {
            initialize();
            goForPackageDeclaration();
            referenceContext = compilationUnit = new CompilationUnitDeclaration(problemReporter(), new CompilationResult(regionSource, 0, 0, this.options.maxProblemsPerUnit), regionSource.length);
            scanner.resetTo(0, regionSource.length);
            scanner.setSource(regionSource);
            parse();
        } catch (AbortCompilation ex) {
        }
    }

    public void parseType(char[] regionSource) {
        try {
            initialize();
            goForTypeDeclaration();
            referenceContext = compilationUnit = new CompilationUnitDeclaration(problemReporter(), new CompilationResult(regionSource, 0, 0, this.options.maxProblemsPerUnit), regionSource.length);
            scanner.resetTo(0, regionSource.length);
            scanner.setSource(regionSource);
            parse();
        } catch (AbortCompilation ex) {
        }
    }

    /**
 * Returns this parser's problem reporter initialized with its reference context.
 * Also it is assumed that a problem is going to be reported, so initializes
 * the compilation result's line positions.
 * 
 * @return ProblemReporter
 */
    public ProblemReporter problemReporter() {
        problemReporter.referenceContext = referenceContext;
        return problemReporter;
    }

    protected void pushOnIntArrayStack(int[] positions) {
        int stackLength = this.intArrayStack.length;
        if (++this.intArrayPtr >= stackLength) {
            System.arraycopy(this.intArrayStack, 0, this.intArrayStack = new int[stackLength + StackIncrement][], 0, stackLength);
        }
        intArrayStack[intArrayPtr] = positions;
    }

    protected void resetModifiers() {
        super.resetModifiers();
        declarationSourceStart = -1;
    }

    protected boolean resumeOnSyntaxError() {
        return false;
    }

    private char[] returnTypeName(TypeReference type) {
        int dimension = type.dimensions();
        if (dimension != 0) {
            char[] dimensionsArray = new char[dimension * 2];
            for (int i = 0; i < dimension; i++) {
                dimensionsArray[i * 2] = '[';
                dimensionsArray[(i * 2) + 1] = ']';
            }
            return CharOperation.concat(CharOperation.concatWith(type.getTypeName(), '.'), dimensionsArray);
        }
        return CharOperation.concatWith(type.getTypeName(), '.');
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("intArrayPtr = " + intArrayPtr + "\n");
        buffer.append(super.toString());
        return buffer.toString();
    }

    /**
 * INTERNAL USE ONLY
 */
    protected TypeReference typeReference(int dim, int localIdentifierPtr, int localIdentifierLengthPtr) {
        int length;
        TypeReference ref;
        if ((length = identifierLengthStack[localIdentifierLengthPtr]) == 1) {
            if (dim == 0) {
                ref = new SingleTypeReference(identifierStack[localIdentifierPtr], identifierPositionStack[localIdentifierPtr--]);
            } else {
                ref = new ArrayTypeReference(identifierStack[localIdentifierPtr], dim, identifierPositionStack[localIdentifierPtr--]);
                ref.sourceEnd = endPosition;
            }
        } else {
            if (length < 0) {
                ref = TypeReference.baseTypeReference(-length, dim);
                ref.sourceStart = intStack[localIntPtr--];
                if (dim == 0) {
                    ref.sourceEnd = intStack[localIntPtr--];
                } else {
                    localIntPtr--;
                    ref.sourceEnd = endPosition;
                }
            } else {
                char[][] tokens = new char[length][];
                localIdentifierPtr -= length;
                long[] positions = new long[length];
                System.arraycopy(identifierStack, localIdentifierPtr + 1, tokens, 0, length);
                System.arraycopy(identifierPositionStack, localIdentifierPtr + 1, positions, 0, length);
                if (dim == 0) ref = new QualifiedTypeReference(tokens, positions); else ref = new ArrayQualifiedTypeReference(tokens, dim, positions);
            }
        }
        return ref;
    }
}
