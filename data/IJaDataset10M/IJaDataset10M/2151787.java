package edu.mit.lcs.haystack.adenine.compilers.javaByteCode;

import edu.mit.lcs.haystack.adenine.AdenineException;
import edu.mit.lcs.haystack.adenine.constructs.IImportJavaVisitor;
import edu.mit.lcs.haystack.adenine.tokenizer.GenericToken;
import edu.mit.lcs.haystack.adenine.tokenizer.LiteralToken;
import edu.mit.lcs.haystack.adenine.tokenizer.Location;
import edu.mit.lcs.haystack.core.CoreLoader;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

/**
 * @author David Huynh
 */
public class ImportJavaVisitor extends ConstructVisitorBase implements IImportJavaVisitor {

    public ImportJavaVisitor(TopLevelVisitor visitor, CodeFrame codeFrame) {
        super(visitor, codeFrame);
    }

    public void onClass(GenericToken klass) {
        InstructionList iList = m_codeFrame.getInstructionList();
        MethodGen mg = m_codeFrame.getMethodGen();
        int line = klass.getSpan().getStart().getTrueLine();
        mg.addLineNumber(iList.append(InstructionConstants.DUP), line);
        mg.addLineNumber(iList.append(new PUSH(m_codeFrame.getConstantPoolGen(), klass.getContent())), line);
        mg.addLineNumber(iList.append(m_codeFrame.getInstructionFactory().createInvoke(this.getClass().getName(), "resolveClass", JavaByteCodeUtilities.s_typeClass, new Type[] { Type.STRING, Type.STRING }, org.apache.bcel.Constants.INVOKESTATIC)), line);
        m_codeFrame.generateNewVariablePut(klass.getContent(), iList, m_codeFrame.getMethodGen(), line);
    }

    public void onImportJava(GenericToken importjavaKeyword, LiteralToken pakkage) {
        InstructionList iList = m_codeFrame.getInstructionList();
        m_codeFrame.getMethodGen().addLineNumber(iList.append(new PUSH(m_codeFrame.getConstantPoolGen(), pakkage.getContent())), pakkage.getSpan().getStart().getTrueLine());
    }

    public void end(Location endLocation) {
        super.end(endLocation);
        m_codeFrame.getMethodGen().addLineNumber(m_codeFrame.getInstructionList().append(InstructionConstants.POP), endLocation.getTrueLine());
    }

    public static Class resolveClass(String pakkage, String klass) throws AdenineException {
        String className = pakkage + "." + klass;
        Class result = CoreLoader.loadClass(className);
        if (result == null) throw new AdenineException("importjava specifies unknown class " + className);
        return result;
    }
}
