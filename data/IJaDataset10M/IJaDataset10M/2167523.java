package de.fzi.injectj.model.impl.recoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import recoder.ParserException;
import recoder.ProgramFactory;
import recoder.abstraction.ClassType;
import recoder.abstraction.Field;
import recoder.abstraction.Method;
import recoder.bytecode.ClassFile;
import recoder.bytecode.MemberInfo;
import recoder.bytecode.MethodInfo;
import recoder.io.ArchiveDataLocation;
import recoder.io.DataFileLocation;
import recoder.io.DataLocation;
import recoder.java.Comment;
import recoder.java.CompilationUnit;
import recoder.java.Expression;
import recoder.java.ProgramElement;
import recoder.java.ScopeDefiningElement;
import recoder.java.SourceElement;
import recoder.java.Statement;
import recoder.java.StatementBlock;
import recoder.java.StatementContainer;
import recoder.java.VariableScope;
import recoder.java.declaration.ClassInitializer;
import recoder.java.declaration.LocalVariableDeclaration;
import recoder.java.declaration.MethodDeclaration;
import recoder.java.declaration.Modifier;
import recoder.java.declaration.TypeDeclaration;
import recoder.java.declaration.VariableSpecification;
import recoder.java.expression.Assignment;
import recoder.java.expression.operator.ComparativeOperator;
import recoder.java.expression.operator.Equals;
import recoder.java.expression.operator.GreaterOrEquals;
import recoder.java.expression.operator.GreaterThan;
import recoder.java.expression.operator.LessOrEquals;
import recoder.java.expression.operator.LessThan;
import recoder.java.expression.operator.New;
import recoder.java.expression.operator.NotEquals;
import recoder.java.reference.MethodReference;
import recoder.java.reference.VariableReference;
import recoder.java.statement.Branch;
import recoder.java.statement.Case;
import recoder.java.statement.Catch;
import recoder.java.statement.Default;
import recoder.java.statement.Else;
import recoder.java.statement.Finally;
import recoder.java.statement.LabeledStatement;
import recoder.java.statement.LoopStatement;
import recoder.java.statement.SynchronizedBlock;
import recoder.java.statement.Then;
import recoder.java.statement.Try;
import recoder.kit.MiscKit;
import recoder.kit.UnitKit;
import recoder.list.ModifierList;
import recoder.list.ProgramElementList;
import recoder.list.StatementArrayList;
import recoder.list.StatementList;
import recoder.list.StatementMutableList;
import recoder.list.VariableSpecificationList;
import de.fzi.injectj.model.ClassWeavepoint;
import de.fzi.injectj.model.AttributeWeavepoint;
import de.fzi.injectj.model.FragmentType;
import de.fzi.injectj.model.MethodType;
import de.fzi.injectj.model.TypeWeavepoint;
import de.fzi.injectj.model.PackageWeavepoint;
import de.fzi.injectj.model.engine.InjectJModel;
import de.fzi.injectj.model.exception.ConformityException;
import de.fzi.injectj.model.exception.SyntaxException;

/** This class contains some useful static methods for working with the
 *  recoder as Mop Provider.
 *
 * @author Volker Kuttruff
 * @author <a href="mailto:Sven.Luzar@web.de">Sven Luzar</a>
 * */
public abstract class RecoderUtil {

    /** Returns the StatementMutableList a given statement is located in. If the
	  * statement isn't located in a StatementMutableList (for example for the statement
	  * 'int i=0' found in statement 'for (int i=0; i<c; i++) ...'), null is returned.
	  *
	  * @param statement the statement to search the StatementMutableList for
	  * @return the StatementMutableList the given statement is located in, or null
	  *         if such a list doesn't exist.
	  */
    static StatementMutableList getStatementListForStatement(Statement statement) {
        StatementMutableList result = null;
        StatementContainer statementContainer = statement.getStatementContainer();
        if (statementContainer == null) return null;
        if (statementContainer instanceof StatementBlock) {
            result = ((StatementBlock) statementContainer).getBody();
        } else if (statementContainer instanceof MethodDeclaration) {
            StatementBlock methodBody = ((MethodDeclaration) statementContainer).getBody();
            if (methodBody != null) {
                result = methodBody.getBody();
            }
        } else if (statementContainer instanceof ClassInitializer) {
            StatementBlock initializerBody = ((ClassInitializer) statementContainer).getBody();
            if (initializerBody != null) {
                result = initializerBody.getBody();
            }
        } else if (statementContainer instanceof Try) {
            StatementBlock tryBlock = ((Try) statementContainer).getBody();
            if (tryBlock != null) {
                result = tryBlock.getBody();
            }
        } else if (statementContainer instanceof SynchronizedBlock) {
            StatementBlock synchronizedBlock = ((SynchronizedBlock) statementContainer).getBody();
            if (synchronizedBlock != null) {
                result = synchronizedBlock.getBody();
            }
        } else if (statementContainer instanceof Branch) {
            if (statementContainer instanceof Case) {
                result = ((Case) statementContainer).getBody();
            } else if (statementContainer instanceof Default) {
                result = ((Default) statementContainer).getBody();
            }
        } else if (statementContainer instanceof LabeledStatement) {
            return getStatementListForStatement(((LabeledStatement) statementContainer).getStatementContainer().getStatementAt(0));
        }
        return result;
    }

    /** Try to create a StatementMutableList for a given statement. This is useful, if
	  * the given statement is the single statement of a for/while/do loop or an if/else
	  * construct. Using this method on the statement 'doSomething()' in the for-loop
	  * 'for (...) doSomething()'
	  * results in the transformed loop
	  * 'for (...) {
	  *    doSomething()
	  * }
	  * Generally, if an statement block can be used instead of the given single statement,
	  * a new statement block will be created, containing the given statement.
	  *
	  * @param statement the statement to create a statement block instead
	  * @return the statement list of the generated statement block containing the given statement,
	  *         or null if a statement block can't be used at this position.
	  */
    static StatementMutableList tryCreateStatementListForStatement(RecoderRoot root, Statement statement) {
        StatementMutableList result = null;
        StatementContainer statementContainer = statement.getStatementContainer();
        if (statementContainer == null) return result;
        if (statementContainer instanceof Branch) {
            if (statementContainer instanceof Catch) {
                ProgramFactory factory = statement.getFactory();
                StatementBlock newCatchBlock = factory.createStatementBlock();
                newCatchBlock.setBody(new StatementArrayList(statement));
                ((Catch) statementContainer).setBody(newCatchBlock);
                registerNewStatementBlock(root, newCatchBlock, statement);
                result = newCatchBlock.getBody();
            } else if (statementContainer instanceof Finally) {
                ProgramFactory factory = statement.getFactory();
                StatementBlock newFinallyBlock = factory.createStatementBlock();
                newFinallyBlock.setBody(new StatementArrayList(statement));
                ((Finally) statementContainer).setBody(newFinallyBlock);
                registerNewStatementBlock(root, newFinallyBlock, statement);
                result = newFinallyBlock.getBody();
            }
            if (statementContainer instanceof Then) {
                ProgramFactory factory = statement.getFactory();
                StatementBlock newThenBlock = factory.createStatementBlock();
                newThenBlock.setBody(new StatementArrayList(statement));
                ((Then) statementContainer).setBody(newThenBlock);
                registerNewStatementBlock(root, newThenBlock, statement);
                result = newThenBlock.getBody();
            } else if (statementContainer instanceof Else) {
                ProgramFactory factory = statement.getFactory();
                StatementBlock newElseBlock = factory.createStatementBlock();
                newElseBlock.setBody(new StatementArrayList(statement));
                ((Else) statementContainer).setBody(newElseBlock);
                registerNewStatementBlock(root, newElseBlock, statement);
                result = newElseBlock.getBody();
            }
        } else if (statementContainer instanceof LoopStatement) {
            LoopStatement loopStatement = (LoopStatement) statementContainer;
            Statement body = loopStatement.getBody();
            if (body.equals(statement)) {
                ProgramFactory factory = statement.getFactory();
                StatementBlock newForBlock = factory.createStatementBlock();
                newForBlock.setBody(new StatementArrayList(body));
                loopStatement.setBody(newForBlock);
                registerNewStatementBlock(root, newForBlock, body);
                result = newForBlock.getBody();
            }
        } else if (statementContainer instanceof LabeledStatement) {
            return tryCreateStatementListForStatement(root, (LabeledStatement) statementContainer);
        }
        return result;
    }

    /** Registers the new generated statement block to RECODER SourceInfo Service.
	 *  Eliminates containing URQs.
	 *
	 * 	@param root 				A reference to the recoder root
	 *  @param newStatementBlock 	The new generated statement block 
	 * 								conatining the single statement
	 * 	@param statement 			The statement contained in the new statement block
	 */
    private static void registerNewStatementBlock(RecoderRoot root, StatementBlock newStatementBlock, Statement statement) {
        if (root.getSourceInfo() != null) {
            MethodDeclaration method = null;
            ProgramElement programElement = statement;
            while (true) {
                if (programElement instanceof MethodDeclaration) {
                    method = (MethodDeclaration) programElement;
                    break;
                }
                programElement = programElement.getASTParent();
                if (programElement == null) {
                    throw new RuntimeException("Can't find method in which statement '" + statement.toSource() + "' is located in");
                }
            }
            method.makeAllParentRolesValid();
            StatementMutableList statementList = newStatementBlock.getBody();
            for (int i = 0; i < statementList.size(); i++) {
                root.changeHistory.attached(statementList.getStatement(i));
            }
            root.changeHistory.attached(newStatementBlock);
        }
    }

    /** Returns the index of a statement in the given statement list. Returns
	  * the same value as StatementList.indexOf(), except for LabeledStatements. If
	  * the given statement is the body of a LabeledStatement (=if the statement's
	  * StatementContainer is an instance of LabeledStatement), then the index of
	  * this LabeledStatement object in the list is returned.
	  *
	  * @param statement the statement to search the index for
	  * @param list the list in which the statement is located
	  * @return the index of the statement in the list, or -1 if the statement can't be
	  *         found
	  */
    static int getIndexOfStatement(Statement statement, StatementList list) {
        int result = list.indexOf(statement);
        if (result >= 0) return result;
        if (statement.getStatementContainer() instanceof LabeledStatement) {
            Statement labeledStatement = (LabeledStatement) statement.getStatementContainer();
            return list.indexOf(labeledStatement);
        }
        return -1;
    }

    static boolean isShadowingAttribute(recoder.java.Statement newStatement, ProgramElement anchor) {
        if (newStatement instanceof LocalVariableDeclaration) {
            TypeDeclaration typeDecl = MiscKit.getParentTypeDeclaration(anchor);
            if (typeDecl == null) return false;
            VariableSpecificationList list = ((LocalVariableDeclaration) newStatement).getVariableSpecifications();
            for (int i = 0; i < list.size(); i++) {
                VariableSpecification varSpec = list.getVariableSpecification(i);
                if (varSpec != null) {
                    if (typeDecl.getVariableInScope(varSpec.getName()) != null) return true;
                }
            }
        }
        return false;
    }

    static boolean isShadowingVariable(recoder.java.Statement newStatement, ProgramElement anchor) {
        if (newStatement instanceof LocalVariableDeclaration) {
            ProgramElement temp = anchor;
            ScopeDefiningElement scope = MiscKit.getScopeDefiningElement(temp);
            ScopeDefiningElement newScope = null;
            VariableSpecificationList list = ((LocalVariableDeclaration) newStatement).getVariableSpecifications();
            while (true) {
                if (scope instanceof VariableScope) {
                    for (int i = 0; i < list.size(); i++) {
                        VariableSpecification varSpec = list.getVariableSpecification(i);
                        if (varSpec != null) {
                            if (((VariableScope) scope).getVariableInScope(varSpec.getName()) != null) return true;
                        }
                    }
                }
                newScope = scope;
                while (newScope == scope && !(temp instanceof TypeDeclaration)) {
                    temp = temp.getASTParent();
                    newScope = MiscKit.getScopeDefiningElement(temp);
                }
                if (temp instanceof TypeDeclaration) break;
                scope = newScope;
            }
        }
        return false;
    }

    static recoder.java.Statement getStatement(ProgramElement element) {
        ProgramElement programElement = element;
        while (true) {
            if (programElement instanceof recoder.java.Statement) {
                return (recoder.java.Statement) programElement;
            }
            if (programElement instanceof MethodDeclaration) {
                return null;
            }
            programElement = programElement.getASTParent();
            if (programElement == null) {
                return null;
            }
        }
    }

    /** Checks if modifiers in list1 are the same as in list2
	 *
	 */
    static boolean hasSameModifiers(ModifierList list1, ModifierList list2) {
        if (list1 == null && list2 == null) return true;
        if ((list1 == null & list2 != null) || (list1 != null & list2 == null)) return false;
        for (int i = 0; i < list1.size(); i++) {
            Modifier modifier1 = list1.getModifier(i);
            if (modifier1 == null) continue;
            boolean foundModifier = false;
            for (int j = 0; j < list2.size(); j++) {
                Modifier modifier2 = list2.getModifier(j);
                if (modifier2 == null) continue;
                if (modifier2.toSource().trim().equals(modifier1.toSource().trim())) foundModifier = true;
            }
            if (!foundModifier) return false;
        }
        return true;
    }

    /** Returns a string representation of the given list.
	  */
    static String printList(ProgramElementList list) {
        if (list == null) return "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            buffer = buffer.append(list.getProgramElement(i).toSource()).append("\n");
        }
        return buffer.toString();
    }

    static TypeWeavepoint findBytecodeTypeWeavepoint(RecoderRoot root, ClassType cl) {
        TypeWeavepoint result = null;
        result = root.getTypeWeavepoint(cl.getFullName());
        if (!(result instanceof RecoderBytecodeClass)) {
            return null;
        }
        assert result != null;
        return result;
    }

    static TypeWeavepoint findParentClassWeavepoint(RecoderRoot root, MemberInfo mi) {
        TypeWeavepoint result = null;
        ClassType parent = mi.getParent();
        if (parent instanceof ClassFile) {
            ClassFile cf = (ClassFile) parent;
            result = root.getTypeWeavepoint(cf.getFullName());
            assert result != null;
        }
        return result;
    }

    /**
	 * returns the parent type declaration. Does not return pe if pe is a type declaration itself 
	 * (this changed after version 0.80) 
	 * @param root
	 * @param pe
	 * @return
	 */
    static TypeWeavepoint findParentTypeWeavepoint(RecoderRoot root, ProgramElement pe) {
        TypeWeavepoint result = null;
        TypeWeavepoint outermostTd = findOutermostTypeWeavepoint(root, pe);
        TypeDeclaration td = MiscKit.getParentTypeDeclaration(pe);
        if (td != null) {
            if (outermostTd == td) {
                result = root.getTypeWeavepoint(td.getFullName());
                assert result != null;
            } else {
                result = root.getTypeWeavepoint(outermostTd.getFullName());
                String relativeName = td.getFullName().substring(result.getFullName().length());
                StringTokenizer st = new StringTokenizer(relativeName, ".");
                while (st.hasMoreTokens()) {
                    String n = st.nextToken();
                    if (!Character.isJavaIdentifierStart(n.charAt(0))) {
                        boolean found = false;
                        List meths = result.getMethods();
                        for (int i = 0; !found && i < meths.size(); i++) {
                            RecoderMethod rm = ((RecoderMethod) meths.get(i));
                            List nc = rm.getLocalClasses();
                            for (int j = 0; j < nc.size(); j++) {
                                RecoderClass rc = (RecoderClass) nc.get(j);
                                if (rc.getName().equals(n)) {
                                    found = true;
                                    result = rc;
                                    break;
                                }
                            }
                        }
                        assert found;
                    } else {
                        result = result.getMemberType(n);
                    }
                    assert result != null;
                }
            }
        }
        return result;
    }

    /**
	 * returns the top level TypeWeavepoint <code>pe</code> is contained in, or <code>null</code>
	 * if there is no such one (e.g., a compilation unit or import)
	 * @param root
	 * @param pe
	 * @return
	 */
    static TypeWeavepoint findOutermostTypeWeavepoint(RecoderRoot root, ProgramElement pe) {
        TypeDeclaration temp, td = MiscKit.getParentTypeDeclaration(pe);
        temp = td;
        while (temp != null) {
            td = temp;
            temp = MiscKit.getParentTypeDeclaration(td);
        }
        TypeWeavepoint result = null;
        if (td != null) {
            result = root.getTypeWeavepoint(td.getFullName());
            assert (result != null) : "inconsistency: type weavepoint must be there if type declaration is!";
        }
        return result;
    }

    static List findParentTypes(RecoderRoot root, ProgramElementList elementList) {
        ArrayList result = new ArrayList();
        Hashtable lookup = new Hashtable();
        ProgramElement pe;
        for (int i = 0; i < elementList.size(); i++) {
            pe = elementList.getProgramElement(i);
            TypeWeavepoint parent = findParentTypeWeavepoint(root, pe);
            if (parent != null) {
                if (lookup.get(parent.getFullName()) == parent) continue;
                result.add(parent);
                lookup.put(parent.getFullName(), parent);
            }
        }
        return result;
    }

    /**
	 * like findParentTypes, but a TypeDeclaration returns its own TypeWeavepoint
	 * @param root
	 * @param elementList
	 * @return
	 */
    static List getTypeWeavepoints(RecoderRoot root, ProgramElementList elementList) {
        ArrayList result = new ArrayList();
        Hashtable lookup = new Hashtable();
        ProgramElement pe;
        for (int i = 0; i < elementList.size(); i++) {
            pe = elementList.getProgramElement(i);
            TypeWeavepoint parent = pe instanceof TypeDeclaration ? root.getTypeWeavepoint(((TypeDeclaration) pe).getFullName()) : findParentTypeWeavepoint(root, pe);
            if (parent != null) {
                if (lookup.get(parent.getFullName()) == parent) continue;
                result.add(parent);
                lookup.put(parent.getFullName(), parent);
            }
        }
        return result;
    }

    static List findParentAttributes(RecoderRoot root, ProgramElementList elementList) {
        List result = new ArrayList();
        Hashtable lookup = new Hashtable();
        ProgramElement pe;
        AttributeWeavepoint attribute = null;
        for (int i = 0; i < elementList.size(); i++) {
            pe = elementList.getProgramElement(i);
            while (pe != null && !(pe instanceof TypeDeclaration)) {
                if (pe instanceof Field) {
                    Field field = (Field) pe;
                    TypeWeavepoint parent = findParentTypeWeavepoint(root, pe);
                    if (parent != null) {
                        attribute = parent.getAttribute(field.getName());
                        if (lookup.containsKey(attribute.getFullName())) break;
                        result.add(attribute);
                        lookup.put(attribute.getFullName(), attribute);
                        break;
                    }
                }
                pe = pe.getASTParent();
            }
        }
        return result;
    }

    /** Returns a unique list with method weave points.
	 *  The parameter element list specifies a list of 
	 *  recoder program elements. The method tries
	 *  to find these elements in the syntax tree.
	 *  After that the method tries to find the 
	 *  parent method for the found program element.
	 *  At last the parent method will transform
	 *  to a method weave point and will add to
	 *  the return list.
	 * 
	 * @param root 			The root from the syntax tree
	 * @param elementList 	List with programElements to inspect
	 * 
	 * @see de.fzi.injectj.model.MethodWeavepoint
	 */
    static List findParentMethods(RecoderRoot root, ProgramElementList elementList) {
        List result = new ArrayList();
        Hashtable lookup = new Hashtable();
        ProgramElement pe;
        MethodType methodWeavepoint = null;
        for (int i = 0; i < elementList.size(); i++) {
            pe = elementList.getProgramElement(i);
            while (!(pe instanceof TypeDeclaration)) {
                if (pe instanceof Method) {
                    Method method = (Method) pe;
                    TypeWeavepoint parent = findParentTypeWeavepoint(root, pe);
                    if (parent != null) {
                        methodWeavepoint = parent.getMethod(RecoderMethodHelper.getSignature(method));
                        if (lookup.containsKey(methodWeavepoint.getSignature())) break;
                        result.add(methodWeavepoint);
                        lookup.put(methodWeavepoint.getSignature(), methodWeavepoint);
                        break;
                    }
                }
                pe = pe.getASTParent();
                if (pe == null) break;
            }
        }
        return result;
    }

    static MethodType findMethodWeavepointForBytecodeMethod(RecoderRoot root, MethodInfo method) {
        MethodType result = null;
        ClassType cl = method.getContainingClassType();
        TypeWeavepoint parent = findBytecodeTypeWeavepoint(root, cl);
        if (parent != null) {
            result = parent.getMethod(RecoderMethodHelper.getSignature(method));
        }
        return result;
    }

    static MethodType findParentMethod(RecoderRoot root, ProgramElement element) {
        MethodType result = null;
        ProgramElement pe = element;
        while (!(pe instanceof TypeDeclaration) && pe != null) {
            if (pe instanceof Method) {
                Method method = (Method) pe;
                TypeWeavepoint parent = findParentTypeWeavepoint(root, pe);
                if (parent != null) {
                    result = parent.getMethod(RecoderMethodHelper.getSignature(method));
                    if ((result == null) && (parent instanceof ClassWeavepoint)) {
                        result = ((ClassWeavepoint) parent).getConstructor(RecoderMethodHelper.getSignature(method));
                    }
                }
            }
            pe = pe.getASTParent();
        }
        return result;
    }

    static PackageWeavepoint findParentPackageWeavepoint(RecoderRoot root, ProgramElement element) {
        PackageWeavepoint result = null;
        ProgramElement pe = element;
        while (!(pe instanceof TypeDeclaration) && (pe != null)) {
            pe = pe.getASTParent();
        }
        if (pe instanceof TypeDeclaration) {
            result = root.getPackage(((TypeDeclaration) pe).getPackage().getFullName());
        }
        return result;
    }

    static List findParentLocalDeclarations(InjectJModel model, ProgramElementList elementList) {
        ArrayList result = new ArrayList();
        ProgramElement pe;
        HashSet visitedLVDs = new HashSet();
        for (int i = 0; i < elementList.size(); i++) {
            pe = elementList.getProgramElement(i);
            while (pe != null && (!(pe instanceof TypeDeclaration) || !(pe instanceof Method))) {
                if (pe instanceof LocalVariableDeclaration) {
                    LocalVariableDeclaration lvd = (LocalVariableDeclaration) pe;
                    if (!visitedLVDs.contains(lvd)) {
                        visitedLVDs.add(lvd);
                        for (int lvdc = 0; lvdc < lvd.getVariables().size(); lvdc++) {
                            VariableSpecification varSpec = lvd.getVariables().getVariableSpecification(lvdc);
                            RecoderDeclaration rd = new RecoderDeclaration(varSpec);
                            rd.setModel(model);
                            result.add(rd);
                        }
                    }
                    break;
                }
                pe = pe.getASTParent();
            }
        }
        return result;
    }

    static List findParentAccesses(InjectJModel model, ProgramElementList elementList) {
        List result = new ArrayList();
        RecoderRoot root = (RecoderRoot) model.getRoot();
        ProgramElement pe;
        for (int i = 0, max = elementList.size(); i < max; i++) {
            pe = elementList.getProgramElement(i);
            while (pe != null && (!(pe instanceof TypeDeclaration) || !(pe instanceof Method))) {
                if (pe instanceof New) {
                    New _new = (New) pe;
                    RecoderAccess ra = new RecoderConstructorAccess(_new);
                    ra.setModel(model);
                    result.add(ra);
                }
                if (pe instanceof MethodReference) {
                    MethodReference methodRef = (MethodReference) pe;
                    RecoderAccess ra = new RecoderMethodAccess(methodRef);
                    ra.setModel(model);
                    result.add(ra);
                    break;
                }
                if (pe instanceof VariableReference) {
                    VariableReference variableRef = (VariableReference) pe;
                    RecoderAccess ra = new RecoderVariableAccess(variableRef);
                    ra.setModel(model);
                    result.add(ra);
                    break;
                }
                pe = pe.getASTParent();
            }
        }
        return result;
    }

    static List findParentAssignments(InjectJModel model, ProgramElementList elementList) {
        List result = new ArrayList();
        ProgramElement pe;
        for (int i = 0; i < elementList.size(); i++) {
            pe = elementList.getProgramElement(i);
            while (pe != null && (!(pe instanceof TypeDeclaration) || !(pe instanceof Method))) {
                if (pe instanceof Assignment) {
                    Assignment assignment = (Assignment) pe;
                    RecoderAssignment ra = new RecoderAssignment(assignment);
                    ra.setModel(model);
                    result.add(ra);
                    break;
                }
                pe = pe.getASTParent();
            }
        }
        return result;
    }

    static int getLine(SourceElement element) {
        while (element != null && element.getStartPosition() == SourceElement.Position.UNDEFINED && element instanceof ProgramElement) {
            element = ((ProgramElement) element).getASTParent();
        }
        if ((element != null) && (element instanceof SourceElement)) return element.getStartPosition().getLine();
        return -1;
    }

    static int getColumn(SourceElement element) {
        while (element != null && element.getStartPosition() == SourceElement.Position.UNDEFINED && element instanceof ProgramElement) {
            element = ((ProgramElement) element).getASTParent();
        }
        if (element != null) return element.getStartPosition().getColumn();
        return -1;
    }

    static String getPosition(SourceElement element) {
        CompilationUnit unit = null;
        if (element instanceof ProgramElement) {
            unit = UnitKit.getCompilationUnit((ProgramElement) element);
        } else if (element instanceof Comment) {
            unit = UnitKit.getCompilationUnit(((Comment) element).getParent());
        }
        String result = null;
        if (unit != null) {
            DataLocation dataLoc = unit.getOriginalDataLocation();
            if (dataLoc instanceof DataFileLocation) {
                java.io.File file = ((DataFileLocation) dataLoc).getFile();
                try {
                    if (file != null) result = file.getCanonicalPath(); else result = "<unknown file>";
                } catch (IOException e) {
                    result = null;
                }
            }
            if (dataLoc instanceof ArchiveDataLocation) {
                java.util.zip.ZipFile zipFile = ((ArchiveDataLocation) dataLoc).getFile();
                if (zipFile != null) result = "<Archive: " + zipFile.getName() + ">";
            }
        }
        if (result == null) result = "<unknown position>";
        int line = getLine(element);
        if (line != -1) result = result + ", line " + line; else result = result + ", <unknown line>";
        return result.replace('\\', '/');
    }

    static String listToString(ProgramElementList list) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            buffer.append(list.getProgramElement(i).toSource());
            if (i + 1 < list.size()) buffer.append(", ");
        }
        return buffer.toString();
    }

    /**
	 * Takes an expression and negates it, either by switching a compare operator,
	 * or by adding a logical "not" to it. Makes deep clones of the original expressions
	 * @param boolean expression the expression to be negated. 
	 * 		  It is not checked if expression really computes to a boolean value!
	 * @return the expression which is the original one but negated.
	 */
    static Expression negateExpression(Expression expression) {
        assert expression != null;
        ProgramFactory pf = expression.getFactory();
        if (expression instanceof ComparativeOperator) {
            ComparativeOperator co = (ComparativeOperator) expression;
            Expression lhs = (Expression) co.getExpressionAt(0).deepClone(), rhs = (Expression) co.getExpressionAt(1).deepClone();
            if (co instanceof Equals) return pf.createNotEquals(lhs, rhs);
            if (co instanceof NotEquals) return pf.createEquals(lhs, rhs);
            if (co instanceof GreaterThan) return pf.createLessOrEquals(lhs, rhs);
            if (co instanceof GreaterOrEquals) return pf.createLessThan(lhs, rhs);
            if (co instanceof LessThan) return pf.createGreaterOrEquals(lhs, rhs);
            if (co instanceof LessOrEquals) return pf.createGreaterThan(lhs, rhs);
            assert false;
        }
        Expression result = pf.createLogicalNot((Expression) expression.deepClone());
        return result;
    }

    /**
	 * replaces a given ProgramElement with a sourcefragment which is parsed by
	 * JavaProgramFactory.parseExpression.
	 * @param root
	 * @param jpe
	 * @param replacementSource
	 * @throws ConformityException
	 * @throws SyntaxException
	 */
    static void replaceExpression(RecoderRoot root, ProgramElement pe, FragmentType replacementSource) throws ConformityException, SyntaxException {
        try {
            Expression expr = pe.getFactory().parseExpression(replacementSource.getSourceString());
            pe.getASTParent().replaceChild(pe, expr);
            root.changeHistory.replaced(pe, expr);
        } catch (ParserException e) {
            throw new SyntaxException("SyntaxErrorInJavaCode");
        } catch (ClassCastException cce) {
            throw new ConformityException("Replacement code cannot replace old code");
        }
    }
}
