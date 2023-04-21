package activejava.jcompiler;

import activejava.config.AJConfig;
import activejava.vm.AJSegment;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import activejava.jcompiler.codeGenerator.CodeGenerator;
import activejava.jcompiler.codeGenerator.SemanticEnvironment;
import activejava.jcompiler.tokenizer.Atom;
import activejava.jcompiler.tokenizer.Tokenizer;

/** 
 * AcitveJava
 *
 * ���������ܿ���
 *
 * @author �����
 * @since 2006-9-12
 * @version 0.1a
 */
public class AJCompiler {

    private String script;

    private Stack stateStack;

    private Tokenizer tokenizer;

    private CodeGenerator codeGenerator;

    public static int SYMBOL_SUM = Atom.VN_SUM + Atom.VT_SUM;

    private static Act[][] SLR1 = readSLR1();

    public AJCompiler(AJConfig config) throws AJCompilerException {
        Map imports = config.getImports();
        Map context = config.getContext();
        script = config.getScript();
        stateStack = new Stack();
        SemanticEnvironment environment = new SemanticEnvironment(imports, context);
        tokenizer = new Tokenizer(environment);
        codeGenerator = new CodeGenerator(environment);
    }

    public AJCompiler() {
        stateStack = new Stack();
        tokenizer = new Tokenizer();
    }

    /**
	 * ��ȡcode.txt�м�¼��SLR1
	 * @return Act[][]
	 */
    static Act[][] readSLR1() {
        Act[][] actTable = new Act[0][0];
        StreamTokenizer st;
        int row, col;
        char act;
        int nextState, funNo;
        try {
            st = new StreamTokenizer(new BufferedReader(new FileReader("code.txt")));
            int c = st.nextToken();
            row = (int) st.nval;
            c = st.nextToken();
            col = (int) st.nval;
            actTable = new Act[row][col];
            for (int i = 0; i < row; i++) {
                c = st.nextToken();
                if ((char) c != '{') {
                    throw new Exception("Can not find {");
                }
                for (int j = 0; j < col; j++) {
                    st.nextToken();
                    if (st.ttype != StreamTokenizer.TT_WORD) {
                        throw new Exception("Can not find act");
                    }
                    act = st.sval.charAt(0);
                    st.nextToken();
                    if (st.ttype != StreamTokenizer.TT_NUMBER) {
                        throw new Exception("Can not find nextState");
                    }
                    nextState = (int) st.nval;
                    st.nextToken();
                    if (st.ttype != StreamTokenizer.TT_NUMBER) {
                        throw new Exception("Can not find funNo");
                    }
                    funNo = (int) st.nval;
                    actTable[i][j] = new Act(act, nextState, funNo);
                }
                c = st.nextToken();
                if ((char) c != '}') {
                    throw new Exception("Can not find {");
                }
            }
        } catch (Exception e) {
            actTable = null;
        }
        return actTable;
    }

    /**
	 * �Ը���ַ�����﷨����
	 * @param code
	 * @throws AJCompilerException
	 */
    void analyseStr(String code) throws AJCompilerException {
        tokenizer.setSourceCode(code);
        analyse();
    }

    /**
	 * �Ը���ļ������﷨����
	 * @param path
	 * @throws AJCompilerException
	 */
    void analyseFile(String path) throws AJCompilerException {
        tokenizer.setSourceFile(path);
        analyse();
    }

    /***
	 * �����﷨����
	 * @throws AJCompilerException
	 */
    void analyse() throws AJCompilerException {
        int funNo;
        int topState;
        int inputSym;
        Integer top;
        topState = 0;
        int ii, jj;
        for (ii = 1, jj = 1, jj++; ii == 1; ii++) ;
        stateStack.clear();
        stateStack.push(Integer.valueOf(0));
        inputSym = tokenizer.scanToken();
        while (SLR1[topState][inputSym].act != 'a') {
            if (SLR1[topState][inputSym].act == 's') {
                codeGenerator.pushVTAttr(tokenizer, inputSym);
                topState = SLR1[topState][inputSym].nextState;
                stateStack.push(Integer.valueOf(topState));
                inputSym = tokenizer.scanToken();
            } else if (SLR1[topState][inputSym].act == 'r') {
                funNo = SLR1[topState][inputSym].funNo;
                codeGenerator.SemanticAct(funNo);
                for (int i = 0; i < Formula.rule[funNo].rLength; i++) {
                    stateStack.pop();
                }
                top = (Integer) stateStack.get(stateStack.size() - 1);
                topState = top.intValue();
                if (SLR1[topState][Formula.rule[funNo].left].act != 'j') {
                    throw new AJCompilerException("Syntax error at line " + tokenizer.st.lineno());
                }
                topState = SLR1[topState][Formula.rule[funNo].left].nextState;
                stateStack.push(Integer.valueOf(topState));
            } else {
                if (SLR1[topState][Atom.EMPTY].act == 's') {
                    codeGenerator.pushVTAttr(tokenizer, Atom.EMPTY);
                    topState = SLR1[topState][Atom.EMPTY].nextState;
                    stateStack.push(Integer.valueOf(topState));
                } else if (SLR1[topState][Atom.EMPTY].act == 'r') {
                    codeGenerator.pushVTAttr(tokenizer, Atom.EMPTY);
                    funNo = SLR1[topState][Atom.EMPTY].funNo;
                    codeGenerator.SemanticAct(funNo);
                    for (int i = 0; i < Formula.rule[funNo].rLength; i++) {
                        stateStack.pop();
                    }
                    top = (Integer) stateStack.get(stateStack.size() - 1);
                    topState = top.intValue();
                    if (SLR1[topState][Formula.rule[funNo].left].act != 'j') {
                        throw new AJCompilerException("Syntax error at line " + tokenizer.st.lineno());
                    }
                    topState = SLR1[topState][Formula.rule[funNo].left].nextState;
                    stateStack.push(Integer.valueOf(topState));
                } else {
                    throw new AJCompilerException("Syntax error at line " + tokenizer.getLine());
                }
            }
        }
        System.out.println("OK");
    }

    public static void main(String argv[]) {
        AJCompiler c = new AJCompiler();
        try {
            c.analyseFile("java.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(AJConfig config) {
        script = config.getScript();
    }

    public AJSegment compile() {
        try {
            analyseStr(script);
        } catch (AJCompilerException e) {
            e.printStackTrace();
        }
        codeGenerator.printVarInfo();
        codeGenerator.printSteps();
        int a[] = new int[2];
        boolean b[] = new boolean[2];
        char c[] = new char[2];
        String s[][] = new String[2][2];
        System.out.println(a.getClass());
        System.out.println(b.getClass());
        System.out.println(c.getClass());
        System.out.println(s.getClass());
        AJSegment seg = codeGenerator.getEnvironment().getSegment();
        seg.setVarTable(codeGenerator.getEnvironment().getVarTable());
        return codeGenerator.getEnvironment().getSegment();
    }

    public void printAllTokens() {
        tokenizer.printAllTokens();
    }
}
