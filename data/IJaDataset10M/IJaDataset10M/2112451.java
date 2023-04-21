package edu.mit.lcs.haystack.adenine.compilers.javaByteCode;

import edu.mit.lcs.haystack.adenine.AdenineException;
import edu.mit.lcs.haystack.adenine.instructions.ModelInstruction;
import edu.mit.lcs.haystack.adenine.interpreter.Interpreter;
import edu.mit.lcs.haystack.adenine.parser2.IAttributeVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IExpressionVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IModelVisitor;
import edu.mit.lcs.haystack.adenine.tokenizer.Location;
import edu.mit.lcs.haystack.adenine.tokenizer.SymbolToken;
import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.LocalRDFContainer;
import edu.mit.lcs.haystack.rdf.RDFException;
import edu.mit.lcs.haystack.rdf.Resource;
import edu.mit.lcs.haystack.rdf.Statement;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

/**
 * @author David Huynh
 */
public class ModelVisitor extends ParserVisitorBase2 implements IModelVisitor {

    protected int m_subjectIndex;

    public ModelVisitor(TopLevelVisitor topLevelVisitor, CodeFrame codeFrame) {
        super(topLevelVisitor, codeFrame);
    }

    public IExpressionVisitor onSubject(Location location) {
        m_codeFrame.getMethodGen().addLineNumber(m_codeFrame.getInstructionList().append(InstructionConstants.DUP), location.getTrueLine());
        return new ExpressionVisitor(m_topLevelVisitor, m_codeFrame) {

            public void end(Location endLocation) {
                onDoneSubject(endLocation);
            }
        };
    }

    public IAttributeVisitor onAttribute(SymbolToken semicolonT) {
        return new AttributeVisitor(m_topLevelVisitor, m_codeFrame) {

            boolean m_hasAttribute = false;

            public IExpressionVisitor onPredicate(Location location) {
                ModelVisitor.this.m_codeFrame.getMethodGen().addLineNumber(ModelVisitor.this.m_codeFrame.getInstructionList().append(InstructionConstants.DUP2), m_startLocation.getTrueLine());
                m_hasAttribute = true;
                return super.onPredicate(location);
            }

            public void end(Location endLocation) {
                if (m_hasAttribute) {
                    onDoneOneStatement(endLocation);
                }
            }
        };
    }

    public void onModelEnd(SymbolToken rightBraceT) {
        m_codeFrame.getMethodGen().addLineNumber(m_codeFrame.getInstructionList().append(InstructionConstants.POP), rightBraceT.getSpan().getStart().getTrueLine());
    }

    public void onModelStart(SymbolToken leftBraceT) {
        InstructionList iList = m_codeFrame.getInstructionList();
        MethodGen mg = m_codeFrame.getMethodGen();
        int line = leftBraceT.getSpan().getStart().getTrueLine();
        mg.addLineNumber(iList.append(m_codeFrame.getInstructionFactory().createNew(JavaByteCodeUtilities.s_typeLocalRDFContainer)), line);
        mg.addLineNumber(iList.append(InstructionConstants.DUP), line);
        mg.addLineNumber(iList.append(m_codeFrame.getInstructionFactory().createInvoke(JavaByteCodeUtilities.s_typeLocalRDFContainer.getClassName(), "<init>", Type.VOID, Type.NO_ARGS, org.apache.bcel.Constants.INVOKESPECIAL)), line);
        mg.addLineNumber(iList.append(InstructionConstants.THIS), line);
        mg.addLineNumber(iList.append(m_codeFrame.getInstructionFactory().createGetField(m_codeFrame.getClassGen().getClassName(), "__dynamicenvironment__", JavaByteCodeUtilities.s_typeDynamicEnvironment)), line);
        mg.addLineNumber(iList.append(m_codeFrame.getInstructionFactory().createInvoke(JavaByteCodeUtilities.s_typeDynamicEnvironment.getClassName(), "getTarget", JavaByteCodeUtilities.s_typeIRDFContainer, Type.NO_ARGS, org.apache.bcel.Constants.INVOKEVIRTUAL)), line);
        m_subjectIndex = m_codeFrame.getMethodGen().addLocalVariable(Interpreter.generateIdentifier(), JavaByteCodeUtilities.s_typeResource, null, null).getIndex();
    }

    protected void onDoneSubject(Location location) {
        InstructionList iList = m_codeFrame.getInstructionList();
        MethodGen mg = m_codeFrame.getMethodGen();
        int line = location.getTrueLine();
        mg.addLineNumber(iList.append(m_codeFrame.getInstructionFactory().createInvoke("edu.mit.lcs.haystack.adenine.compilers.javaByteCode.ModelVisitor", "resolveSubject", JavaByteCodeUtilities.s_typeResource, new Type[] { JavaByteCodeUtilities.s_typeIRDFContainer, Type.OBJECT }, org.apache.bcel.Constants.INVOKESTATIC)), line);
        mg.addLineNumber(iList.append(new ASTORE(m_subjectIndex)), line);
    }

    protected void onDoneOneStatement(Location location) {
        InstructionList iList = m_codeFrame.getInstructionList();
        MethodGen mg = m_codeFrame.getMethodGen();
        int line = location.getTrueLine();
        mg.addLineNumber(iList.append(new ALOAD(m_subjectIndex)), line);
        mg.addLineNumber(iList.append(m_codeFrame.getInstructionFactory().createInvoke("edu.mit.lcs.haystack.adenine.compilers.javaByteCode.ModelVisitor", "addStatement", Type.VOID, new Type[] { JavaByteCodeUtilities.s_typeLocalRDFContainer, JavaByteCodeUtilities.s_typeIRDFContainer, Type.OBJECT, Type.OBJECT, JavaByteCodeUtilities.s_typeResource }, org.apache.bcel.Constants.INVOKESTATIC)), line);
    }

    public static void addStatement(LocalRDFContainer rdfc, IRDFContainer target, Object predicate, Object object, Resource subject) throws AdenineException, RDFException {
        rdfc.add(new Statement(subject, (Resource) ModelInstruction.extractNode(predicate, target), ModelInstruction.extractNode(object, target)));
    }

    public static Resource resolveSubject(IRDFContainer target, Object subject) throws AdenineException, RDFException {
        try {
            return (Resource) ModelInstruction.extractNode(subject, target);
        } catch (ClassCastException e) {
            throw new AdenineException("Subject of statement (" + subject + ") is not a resource", e);
        }
    }
}
