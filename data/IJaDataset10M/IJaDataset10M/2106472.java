package com.narirelays.ems.utils.Interpreters.Excel;

import java.util.HashSet;
import com.narirelays.ems.utils.Interpreters.Excel.HTMLExcelBridge;
import com.narirelays.ems.utils.Interpreters.Excel.CellPosition;
import static com.narirelays.ems.utils.Interpreters.Utility.GeneralObjectCalculator.*;
import static com.narirelays.ems.utils.Interpreters.Utility.FunctionInvoker.*;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({ "all", "warnings", "unchecked" })
public class ExcelFormulaJavaTree extends TreeParser {

    public static final String[] tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BINARY", "CALL", "CELL", "CELLRANGE", "DECIMAL", "EscapeSequence", "Exponent", "FIXEDCELL", "FIXEDCELLRANGE", "HEXADECIMAL", "HexDigit", "HexPrefix", "IDENTIFIER", "LIST", "NEWLINE", "NOT", "OCTAL", "PI", "REAL", "REMOTECELL", "REMOTECELLRANGE", "STRING", "WS", "'%'", "'('", "')'", "'*'", "'+'", "','", "'-'", "'/'", "';'", "'^'" };

    public static final int EOF = -1;

    public static final int T__27 = 27;

    public static final int T__28 = 28;

    public static final int T__29 = 29;

    public static final int T__30 = 30;

    public static final int T__31 = 31;

    public static final int T__32 = 32;

    public static final int T__33 = 33;

    public static final int T__34 = 34;

    public static final int T__35 = 35;

    public static final int T__36 = 36;

    public static final int BINARY = 4;

    public static final int CALL = 5;

    public static final int CELL = 6;

    public static final int CELLRANGE = 7;

    public static final int DECIMAL = 8;

    public static final int EscapeSequence = 9;

    public static final int Exponent = 10;

    public static final int FIXEDCELL = 11;

    public static final int FIXEDCELLRANGE = 12;

    public static final int HEXADECIMAL = 13;

    public static final int HexDigit = 14;

    public static final int HexPrefix = 15;

    public static final int IDENTIFIER = 16;

    public static final int LIST = 17;

    public static final int NEWLINE = 18;

    public static final int NOT = 19;

    public static final int OCTAL = 20;

    public static final int PI = 21;

    public static final int REAL = 22;

    public static final int REMOTECELL = 23;

    public static final int REMOTECELLRANGE = 24;

    public static final int STRING = 25;

    public static final int WS = 26;

    public TreeParser[] getDelegates() {
        return new TreeParser[] {};
    }

    public ExcelFormulaJavaTree(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }

    public ExcelFormulaJavaTree(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() {
        return ExcelFormulaJavaTree.tokenNames;
    }

    public String getGrammarFileName() {
        return "E:\\workspace\\java\\ems\\src\\com\\narirelays\\ems\\utils\\Interpreters\\Excel\\ExcelFormulaJavaTree.g";
    }

    /** Points to functions tracked by tree builder. */
    private int currentSheet = 0;

    private HashSet<String> evaluateStack;

    private boolean isNewEvaluator = true;

    private HTMLExcelBridge excel = null;

    /** Set up an evaluator with a node stream; and a set of function definition ASTs. */
    public ExcelFormulaJavaTree(CommonTreeNodeStream nodes, HTMLExcelBridge _excel, HashSet<String> _evaluateStack, int _currentSheet, boolean _isNewEvaluator) {
        this(nodes);
        this.excel = _excel;
        this.isNewEvaluator = _isNewEvaluator;
        this.evaluateStack = _evaluateStack;
        this.currentSheet = _currentSheet;
    }

    public final Object expr() throws RecognitionException {
        Object value = null;
        CommonTree STRING1 = null;
        CommonTree BINARY2 = null;
        CommonTree OCTAL3 = null;
        CommonTree DECIMAL4 = null;
        CommonTree HEXADECIMAL5 = null;
        CommonTree REAL6 = null;
        CommonTree CELL7 = null;
        CommonTree CELLRANGE8 = null;
        CommonTree FIXEDCELL9 = null;
        CommonTree FIXEDCELLRANGE10 = null;
        CommonTree REMOTECELL11 = null;
        CommonTree REMOTECELLRANGE12 = null;
        Object a = null;
        Object b = null;
        Object call13 = null;
        try {
            int alt1 = 19;
            switch(input.LA(1)) {
                case 31:
                    {
                        alt1 = 1;
                    }
                    break;
                case 33:
                    {
                        alt1 = 2;
                    }
                    break;
                case 30:
                    {
                        alt1 = 3;
                    }
                    break;
                case 34:
                    {
                        alt1 = 4;
                    }
                    break;
                case 27:
                    {
                        alt1 = 5;
                    }
                    break;
                case 36:
                    {
                        alt1 = 6;
                    }
                    break;
                case STRING:
                    {
                        alt1 = 7;
                    }
                    break;
                case BINARY:
                    {
                        alt1 = 8;
                    }
                    break;
                case OCTAL:
                    {
                        alt1 = 9;
                    }
                    break;
                case DECIMAL:
                    {
                        alt1 = 10;
                    }
                    break;
                case HEXADECIMAL:
                    {
                        alt1 = 11;
                    }
                    break;
                case REAL:
                    {
                        alt1 = 12;
                    }
                    break;
                case CELL:
                    {
                        alt1 = 13;
                    }
                    break;
                case CELLRANGE:
                    {
                        alt1 = 14;
                    }
                    break;
                case FIXEDCELL:
                    {
                        alt1 = 15;
                    }
                    break;
                case FIXEDCELLRANGE:
                    {
                        alt1 = 16;
                    }
                    break;
                case REMOTECELL:
                    {
                        alt1 = 17;
                    }
                    break;
                case REMOTECELLRANGE:
                    {
                        alt1 = 18;
                    }
                    break;
                case CALL:
                    {
                        alt1 = 19;
                    }
                    break;
                default:
                    NoViableAltException nvae = new NoViableAltException("", 1, 0, input);
                    throw nvae;
            }
            switch(alt1) {
                case 1:
                    {
                        match(input, 31, FOLLOW_31_in_expr60);
                        match(input, Token.DOWN, null);
                        pushFollow(FOLLOW_expr_in_expr64);
                        a = expr();
                        state._fsp--;
                        pushFollow(FOLLOW_expr_in_expr68);
                        b = expr();
                        state._fsp--;
                        match(input, Token.UP, null);
                        value = add(a, b);
                    }
                    break;
                case 2:
                    {
                        match(input, 33, FOLLOW_33_in_expr88);
                        match(input, Token.DOWN, null);
                        pushFollow(FOLLOW_expr_in_expr92);
                        a = expr();
                        state._fsp--;
                        pushFollow(FOLLOW_expr_in_expr96);
                        b = expr();
                        state._fsp--;
                        match(input, Token.UP, null);
                        value = subtract(a, b);
                    }
                    break;
                case 3:
                    {
                        match(input, 30, FOLLOW_30_in_expr116);
                        match(input, Token.DOWN, null);
                        pushFollow(FOLLOW_expr_in_expr120);
                        a = expr();
                        state._fsp--;
                        pushFollow(FOLLOW_expr_in_expr124);
                        b = expr();
                        state._fsp--;
                        match(input, Token.UP, null);
                        value = multiply(a, b);
                    }
                    break;
                case 4:
                    {
                        match(input, 34, FOLLOW_34_in_expr144);
                        match(input, Token.DOWN, null);
                        pushFollow(FOLLOW_expr_in_expr148);
                        a = expr();
                        state._fsp--;
                        pushFollow(FOLLOW_expr_in_expr152);
                        b = expr();
                        state._fsp--;
                        match(input, Token.UP, null);
                        value = divide(a, b);
                    }
                    break;
                case 5:
                    {
                        match(input, 27, FOLLOW_27_in_expr172);
                        match(input, Token.DOWN, null);
                        pushFollow(FOLLOW_expr_in_expr176);
                        a = expr();
                        state._fsp--;
                        pushFollow(FOLLOW_expr_in_expr180);
                        b = expr();
                        state._fsp--;
                        match(input, Token.UP, null);
                        value = remainder(a, b);
                    }
                    break;
                case 6:
                    {
                        match(input, 36, FOLLOW_36_in_expr200);
                        match(input, Token.DOWN, null);
                        pushFollow(FOLLOW_expr_in_expr204);
                        a = expr();
                        state._fsp--;
                        pushFollow(FOLLOW_expr_in_expr208);
                        b = expr();
                        state._fsp--;
                        match(input, Token.UP, null);
                        value = power(a, b);
                    }
                    break;
                case 7:
                    {
                        STRING1 = (CommonTree) match(input, STRING, FOLLOW_STRING_in_expr227);
                        value = (STRING1 != null ? STRING1.getText() : null);
                    }
                    break;
                case 8:
                    {
                        BINARY2 = (CommonTree) match(input, BINARY, FOLLOW_BINARY_in_expr244);
                        value = getBinaryStringValue((BINARY2 != null ? BINARY2.getText() : null));
                    }
                    break;
                case 9:
                    {
                        OCTAL3 = (CommonTree) match(input, OCTAL, FOLLOW_OCTAL_in_expr276);
                        value = getOctalStringValue((OCTAL3 != null ? OCTAL3.getText() : null));
                    }
                    break;
                case 10:
                    {
                        DECIMAL4 = (CommonTree) match(input, DECIMAL, FOLLOW_DECIMAL_in_expr307);
                        value = getDecimalStringValue((DECIMAL4 != null ? DECIMAL4.getText() : null));
                    }
                    break;
                case 11:
                    {
                        HEXADECIMAL5 = (CommonTree) match(input, HEXADECIMAL, FOLLOW_HEXADECIMAL_in_expr322);
                        value = getHexadecimalStringValue((HEXADECIMAL5 != null ? HEXADECIMAL5.getText() : null));
                    }
                    break;
                case 12:
                    {
                        REAL6 = (CommonTree) match(input, REAL, FOLLOW_REAL_in_expr340);
                        value = getRealStringValue((REAL6 != null ? REAL6.getText() : null));
                    }
                    break;
                case 13:
                    {
                        CELL7 = (CommonTree) match(input, CELL, FOLLOW_CELL_in_expr358);
                        value = excel.evaluate(HTMLExcelBridge.parseCellPosition(currentSheet, (CELL7 != null ? CELL7.getText() : null)));
                    }
                    break;
                case 14:
                    {
                        CELLRANGE8 = (CommonTree) match(input, CELLRANGE, FOLLOW_CELLRANGE_in_expr374);
                        value = excel.evaluate(HTMLExcelBridge.parseCellRangePosition(currentSheet, (CELLRANGE8 != null ? CELLRANGE8.getText() : null)));
                    }
                    break;
                case 15:
                    {
                        FIXEDCELL9 = (CommonTree) match(input, FIXEDCELL, FOLLOW_FIXEDCELL_in_expr389);
                        value = excel.evaluate(HTMLExcelBridge.parseCellPosition(currentSheet, (FIXEDCELL9 != null ? FIXEDCELL9.getText() : null)));
                    }
                    break;
                case 16:
                    {
                        FIXEDCELLRANGE10 = (CommonTree) match(input, FIXEDCELLRANGE, FOLLOW_FIXEDCELLRANGE_in_expr416);
                        value = excel.evaluate(HTMLExcelBridge.parseCellRangePosition(currentSheet, (FIXEDCELLRANGE10 != null ? FIXEDCELLRANGE10.getText() : null)));
                    }
                    break;
                case 17:
                    {
                        REMOTECELL11 = (CommonTree) match(input, REMOTECELL, FOLLOW_REMOTECELL_in_expr431);
                        value = excel.evaluate(excel.parseRemoteCellPosition((REMOTECELL11 != null ? REMOTECELL11.getText() : null)));
                    }
                    break;
                case 18:
                    {
                        REMOTECELLRANGE12 = (CommonTree) match(input, REMOTECELLRANGE, FOLLOW_REMOTECELLRANGE_in_expr457);
                        value = excel.evaluate(excel.parseRemoteCellRangePosition((REMOTECELLRANGE12 != null ? REMOTECELLRANGE12.getText() : null)));
                    }
                    break;
                case 19:
                    {
                        pushFollow(FOLLOW_call_in_expr480);
                        call13 = call();
                        state._fsp--;
                        value = call13;
                    }
                    break;
            }
        } catch (ExcelException re) {
            value = re.getMessage();
        } finally {
        }
        return value;
    }

    public final Object call() throws RecognitionException {
        Object value = null;
        CommonTree IDENTIFIER14 = null;
        CommonTree IDENTIFIER15 = null;
        ExcelFormulaJavaTree.expseq_return args = null;
        try {
            int alt2 = 2;
            int LA2_0 = input.LA(1);
            if ((LA2_0 == CALL)) {
                int LA2_1 = input.LA(2);
                if ((LA2_1 == DOWN)) {
                    int LA2_2 = input.LA(3);
                    if ((LA2_2 == IDENTIFIER)) {
                        int LA2_3 = input.LA(4);
                        if ((LA2_3 == UP)) {
                            alt2 = 1;
                        } else if ((LA2_3 == LIST)) {
                            alt2 = 2;
                        } else {
                            NoViableAltException nvae = new NoViableAltException("", 2, 3, input);
                            throw nvae;
                        }
                    } else {
                        NoViableAltException nvae = new NoViableAltException("", 2, 2, input);
                        throw nvae;
                    }
                } else {
                    NoViableAltException nvae = new NoViableAltException("", 2, 1, input);
                    throw nvae;
                }
            } else {
                NoViableAltException nvae = new NoViableAltException("", 2, 0, input);
                throw nvae;
            }
            switch(alt2) {
                case 1:
                    {
                        match(input, CALL, FOLLOW_CALL_in_call538);
                        match(input, Token.DOWN, null);
                        IDENTIFIER14 = (CommonTree) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_call540);
                        match(input, Token.UP, null);
                        String fn = (IDENTIFIER14 != null ? IDENTIFIER14.getText() : null);
                        System.out.println("Call function:" + fn);
                    }
                    break;
                case 2:
                    {
                        match(input, CALL, FOLLOW_CALL_in_call565);
                        match(input, Token.DOWN, null);
                        IDENTIFIER15 = (CommonTree) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_call567);
                        pushFollow(FOLLOW_expseq_in_call571);
                        args = expseq();
                        state._fsp--;
                        match(input, Token.UP, null);
                        String fn = (IDENTIFIER15 != null ? IDENTIFIER15.getText() : null);
                        int pcount = (args != null ? ((CommonTree) args.start) : null) != null ? (args != null ? ((CommonTree) args.start) : null).getChildCount() : 0;
                        if (pcount == 0) {
                            try {
                                value = invokeMethod(fn, null);
                            } catch (Exception e) {
                                value = e.getMessage();
                            }
                        } else {
                            List<Object> paraValues = new ArrayList<Object>();
                            CommonTree paraTree = null;
                            for (int i = 0; i < pcount; i++) {
                                Tree pAtI = (args != null ? ((CommonTree) args.start) : null).getChild(i);
                                paraTree = (CommonTree) pAtI;
                                if (isNewEvaluator) {
                                    CommonTreeNodeStream nodes = new CommonTreeNodeStream(paraTree);
                                    ExcelFormulaJavaTree evaluator = new ExcelFormulaJavaTree(nodes, excel, evaluateStack, currentSheet, true);
                                    Object exprValue = evaluator.expr();
                                    if (exprValue instanceof CellPosition) {
                                        CellPosition cp = (CellPosition) exprValue;
                                        for (int rowi = cp.sRowNum; rowi <= cp.eRowNum; rowi++) {
                                            for (int coli = cp.sColNum; coli <= cp.eColNum; coli++) {
                                                paraValues.add(excel.evaluate(evaluateStack, cp.sheetNum, rowi, coli));
                                            }
                                        }
                                    } else {
                                        paraValues.add(exprValue);
                                    }
                                } else {
                                    CommonTreeNodeStream commonInput = (CommonTreeNodeStream) input;
                                    int exprStartIndex = paraTree.getTokenStartIndex();
                                    commonInput.push(exprStartIndex);
                                    paraValues.add(expr());
                                    commonInput.pop();
                                }
                            }
                            try {
                                value = invokeMethod(fn, paraValues);
                            } catch (Exception e) {
                                value = e.getMessage();
                            }
                        }
                    }
                    break;
            }
        } catch (ExcelException re) {
            value = re.getMessage();
        } finally {
        }
        return value;
    }

    public static class expseq_return extends TreeRuleReturnScope {
    }

    ;

    public final ExcelFormulaJavaTree.expseq_return expseq() throws RecognitionException {
        ExcelFormulaJavaTree.expseq_return retval = new ExcelFormulaJavaTree.expseq_return();
        retval.start = input.LT(1);
        Object args = null;
        try {
            {
                match(input, LIST, FOLLOW_LIST_in_expseq644);
                match(input, Token.DOWN, null);
                int cnt3 = 0;
                loop3: do {
                    int alt3 = 2;
                    int LA3_0 = input.LA(1);
                    if (((LA3_0 >= BINARY && LA3_0 <= DECIMAL) || (LA3_0 >= FIXEDCELL && LA3_0 <= HEXADECIMAL) || LA3_0 == OCTAL || (LA3_0 >= REAL && LA3_0 <= STRING) || LA3_0 == 27 || (LA3_0 >= 30 && LA3_0 <= 31) || (LA3_0 >= 33 && LA3_0 <= 34) || LA3_0 == 36)) {
                        alt3 = 1;
                    }
                    switch(alt3) {
                        case 1:
                            {
                                pushFollow(FOLLOW_expr_in_expseq648);
                                args = expr();
                                state._fsp--;
                            }
                            break;
                        default:
                            if (cnt3 >= 1) break loop3;
                            EarlyExitException eee = new EarlyExitException(3, input);
                            throw eee;
                    }
                    cnt3++;
                } while (true);
                match(input, Token.UP, null);
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }

    public static final BitSet FOLLOW_31_in_expr60 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_expr_in_expr64 = new BitSet(new long[] { 0x00000016CBD039F0L });

    public static final BitSet FOLLOW_expr_in_expr68 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_33_in_expr88 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_expr_in_expr92 = new BitSet(new long[] { 0x00000016CBD039F0L });

    public static final BitSet FOLLOW_expr_in_expr96 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_30_in_expr116 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_expr_in_expr120 = new BitSet(new long[] { 0x00000016CBD039F0L });

    public static final BitSet FOLLOW_expr_in_expr124 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_34_in_expr144 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_expr_in_expr148 = new BitSet(new long[] { 0x00000016CBD039F0L });

    public static final BitSet FOLLOW_expr_in_expr152 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_27_in_expr172 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_expr_in_expr176 = new BitSet(new long[] { 0x00000016CBD039F0L });

    public static final BitSet FOLLOW_expr_in_expr180 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_36_in_expr200 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_expr_in_expr204 = new BitSet(new long[] { 0x00000016CBD039F0L });

    public static final BitSet FOLLOW_expr_in_expr208 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_STRING_in_expr227 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_BINARY_in_expr244 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_OCTAL_in_expr276 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_DECIMAL_in_expr307 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_HEXADECIMAL_in_expr322 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_REAL_in_expr340 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_CELL_in_expr358 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_CELLRANGE_in_expr374 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_FIXEDCELL_in_expr389 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_FIXEDCELLRANGE_in_expr416 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_REMOTECELL_in_expr431 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_REMOTECELLRANGE_in_expr457 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_call_in_expr480 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_CALL_in_call538 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_IDENTIFIER_in_call540 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_CALL_in_call565 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_IDENTIFIER_in_call567 = new BitSet(new long[] { 0x0000000000020000L });

    public static final BitSet FOLLOW_expseq_in_call571 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_LIST_in_expseq644 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_expr_in_expseq648 = new BitSet(new long[] { 0x00000016CBD039F8L });
}
