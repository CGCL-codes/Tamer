package net.sourceforge.texlipse.bibparser.parser;

import net.sourceforge.texlipse.bibparser.lexer.*;
import net.sourceforge.texlipse.bibparser.node.*;
import net.sourceforge.texlipse.bibparser.analysis.*;
import java.util.*;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

@SuppressWarnings("nls")
public class Parser {

    public final Analysis ignoredTokens = new AnalysisAdapter();

    protected ArrayList nodeList;

    private final Lexer lexer;

    private final ListIterator stack = new LinkedList().listIterator();

    private int last_pos;

    private int last_line;

    private Token last_token;

    private final TokenIndex converter = new TokenIndex();

    private final int[] action = new int[2];

    private static final int SHIFT = 0;

    private static final int REDUCE = 1;

    private static final int ACCEPT = 2;

    private static final int ERROR = 3;

    public Parser(@SuppressWarnings("hiding") Lexer lexer) {
        this.lexer = lexer;
    }

    @SuppressWarnings({ "unchecked", "unused" })
    private void push(int numstate, ArrayList listNode) throws ParserException, LexerException, IOException {
        this.nodeList = listNode;
        if (!this.stack.hasNext()) {
            this.stack.add(new State(numstate, this.nodeList));
            return;
        }
        State s = (State) this.stack.next();
        s.state = numstate;
        s.nodes = this.nodeList;
    }

    private int goTo(int index) {
        int state = state();
        int low = 1;
        int high = gotoTable[index].length - 1;
        int value = gotoTable[index][0][1];
        while (low <= high) {
            int middle = (low + high) / 2;
            if (state < gotoTable[index][middle][0]) {
                high = middle - 1;
            } else if (state > gotoTable[index][middle][0]) {
                low = middle + 1;
            } else {
                value = gotoTable[index][middle][1];
                break;
            }
        }
        return value;
    }

    private int state() {
        State s = (State) this.stack.previous();
        this.stack.next();
        return s.state;
    }

    private ArrayList pop() {
        return ((State) this.stack.previous()).nodes;
    }

    private int index(Switchable token) {
        this.converter.index = -1;
        token.apply(this.converter);
        return this.converter.index;
    }

    @SuppressWarnings("unchecked")
    public Start parse() throws ParserException, LexerException, IOException {
        push(0, null);
        List<Node> ign = null;
        while (true) {
            while (index(this.lexer.peek()) == -1) {
                if (ign == null) {
                    ign = new LinkedList<Node>();
                }
                ign.add(this.lexer.next());
            }
            if (ign != null) {
                this.ignoredTokens.setIn(this.lexer.peek(), ign);
                ign = null;
            }
            this.last_pos = this.lexer.peek().getPos();
            this.last_line = this.lexer.peek().getLine();
            this.last_token = this.lexer.peek();
            int index = index(this.lexer.peek());
            this.action[0] = Parser.actionTable[state()][0][1];
            this.action[1] = Parser.actionTable[state()][0][2];
            int low = 1;
            int high = Parser.actionTable[state()].length - 1;
            while (low <= high) {
                int middle = (low + high) / 2;
                if (index < Parser.actionTable[state()][middle][0]) {
                    high = middle - 1;
                } else if (index > Parser.actionTable[state()][middle][0]) {
                    low = middle + 1;
                } else {
                    this.action[0] = Parser.actionTable[state()][middle][1];
                    this.action[1] = Parser.actionTable[state()][middle][2];
                    break;
                }
            }
            switch(this.action[0]) {
                case SHIFT:
                    {
                        ArrayList list = new ArrayList();
                        list.add(this.lexer.next());
                        push(this.action[1], list);
                    }
                    break;
                case REDUCE:
                    switch(this.action[1]) {
                        case 0:
                            {
                                ArrayList list = new0();
                                push(goTo(0), list);
                            }
                            break;
                        case 1:
                            {
                                ArrayList list = new1();
                                push(goTo(0), list);
                            }
                            break;
                        case 2:
                            {
                                ArrayList list = new2();
                                push(goTo(1), list);
                            }
                            break;
                        case 3:
                            {
                                ArrayList list = new3();
                                push(goTo(1), list);
                            }
                            break;
                        case 4:
                            {
                                ArrayList list = new4();
                                push(goTo(1), list);
                            }
                            break;
                        case 5:
                            {
                                ArrayList list = new5();
                                push(goTo(2), list);
                            }
                            break;
                        case 6:
                            {
                                ArrayList list = new6();
                                push(goTo(2), list);
                            }
                            break;
                        case 7:
                            {
                                ArrayList list = new7();
                                push(goTo(2), list);
                            }
                            break;
                        case 8:
                            {
                                ArrayList list = new8();
                                push(goTo(2), list);
                            }
                            break;
                        case 9:
                            {
                                ArrayList list = new9();
                                push(goTo(2), list);
                            }
                            break;
                        case 10:
                            {
                                ArrayList list = new10();
                                push(goTo(2), list);
                            }
                            break;
                        case 11:
                            {
                                ArrayList list = new11();
                                push(goTo(2), list);
                            }
                            break;
                        case 12:
                            {
                                ArrayList list = new12();
                                push(goTo(2), list);
                            }
                            break;
                        case 13:
                            {
                                ArrayList list = new13();
                                push(goTo(3), list);
                            }
                            break;
                        case 14:
                            {
                                ArrayList list = new14();
                                push(goTo(3), list);
                            }
                            break;
                        case 15:
                            {
                                ArrayList list = new15();
                                push(goTo(3), list);
                            }
                            break;
                        case 16:
                            {
                                ArrayList list = new16();
                                push(goTo(3), list);
                            }
                            break;
                        case 17:
                            {
                                ArrayList list = new17();
                                push(goTo(4), list);
                            }
                            break;
                        case 18:
                            {
                                ArrayList list = new18();
                                push(goTo(5), list);
                            }
                            break;
                        case 19:
                            {
                                ArrayList list = new19();
                                push(goTo(5), list);
                            }
                            break;
                        case 20:
                            {
                                ArrayList list = new20();
                                push(goTo(6), list);
                            }
                            break;
                        case 21:
                            {
                                ArrayList list = new21();
                                push(goTo(6), list);
                            }
                            break;
                        case 22:
                            {
                                ArrayList list = new22();
                                push(goTo(6), list);
                            }
                            break;
                        case 23:
                            {
                                ArrayList list = new23();
                                push(goTo(6), list);
                            }
                            break;
                        case 24:
                            {
                                ArrayList list = new24();
                                push(goTo(6), list);
                            }
                            break;
                        case 25:
                            {
                                ArrayList list = new25();
                                push(goTo(7), list);
                            }
                            break;
                        case 26:
                            {
                                ArrayList list = new26();
                                push(goTo(8), list);
                            }
                            break;
                        case 27:
                            {
                                ArrayList list = new27();
                                push(goTo(8), list);
                            }
                            break;
                        case 28:
                            {
                                ArrayList list = new28();
                                push(goTo(9), list);
                            }
                            break;
                        case 29:
                            {
                                ArrayList list = new29();
                                push(goTo(9), list);
                            }
                            break;
                        case 30:
                            {
                                ArrayList list = new30();
                                push(goTo(10), list);
                            }
                            break;
                        case 31:
                            {
                                ArrayList list = new31();
                                push(goTo(10), list);
                            }
                            break;
                    }
                    break;
                case ACCEPT:
                    {
                        EOF node2 = (EOF) this.lexer.next();
                        PBibtex node1 = (PBibtex) pop().get(0);
                        Start node = new Start(node1, node2);
                        return node;
                    }
                case ERROR:
                    throw new ParserException(this.last_token, "[" + this.last_line + "," + this.last_pos + "] " + Parser.errorMessages[Parser.errors[this.action[1]]]);
            }
        }
    }

    @SuppressWarnings("unchecked")
    ArrayList new0() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        PBibtex pbibtexNode1;
        {
            LinkedList listNode2 = new LinkedList();
            {
            }
            pbibtexNode1 = new ABibtex(listNode2);
        }
        nodeList.add(pbibtexNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new1() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PBibtex pbibtexNode1;
        {
            LinkedList listNode3 = new LinkedList();
            {
                LinkedList listNode2 = new LinkedList();
                listNode2 = (LinkedList) nodeArrayList1.get(0);
                if (listNode2 != null) {
                    listNode3.addAll(listNode2);
                }
            }
            pbibtexNode1 = new ABibtex(listNode3);
        }
        nodeList.add(pbibtexNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new2() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PBibEntry pbibentryNode1;
        {
            PStringEntry pstringentryNode2;
            pstringentryNode2 = (PStringEntry) nodeArrayList1.get(0);
            pbibentryNode1 = new ABibstreBibEntry(pstringentryNode2);
        }
        nodeList.add(pbibentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new3() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PBibEntry pbibentryNode1;
        {
            PEntry pentryNode2;
            pentryNode2 = (PEntry) nodeArrayList1.get(0);
            pbibentryNode1 = new ABibeBibEntry(pentryNode2);
        }
        nodeList.add(pbibentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new4() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PBibEntry pbibentryNode1;
        {
            TTaskcomment ttaskcommentNode2;
            ttaskcommentNode2 = (TTaskcomment) nodeArrayList1.get(0);
            pbibentryNode1 = new ABibtaskBibEntry(ttaskcommentNode2);
        }
        nodeList.add(pbibentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new5() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList6 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PStringEntry pstringentryNode1;
        {
            TIdentifier tidentifierNode2;
            TStringLiteral tstringliteralNode3;
            tidentifierNode2 = (TIdentifier) nodeArrayList3.get(0);
            tstringliteralNode3 = (TStringLiteral) nodeArrayList5.get(0);
            pstringentryNode1 = new AStrbraceStringEntry(tidentifierNode2, tstringliteralNode3);
        }
        nodeList.add(pstringentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new6() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList7 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList6 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PStringEntry pstringentryNode1;
        {
            TIdentifier tidentifierNode2;
            TStringLiteral tstringliteralNode3;
            tidentifierNode2 = (TIdentifier) nodeArrayList3.get(0);
            tstringliteralNode3 = (TStringLiteral) nodeArrayList6.get(0);
            pstringentryNode1 = new AStrbraceStringEntry(tidentifierNode2, tstringliteralNode3);
        }
        nodeList.add(pstringentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new7() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList7 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList6 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PStringEntry pstringentryNode1;
        {
            TIdentifier tidentifierNode2;
            TStringLiteral tstringliteralNode3;
            tidentifierNode2 = (TIdentifier) nodeArrayList3.get(0);
            tstringliteralNode3 = (TStringLiteral) nodeArrayList5.get(0);
            pstringentryNode1 = new AStrbraceStringEntry(tidentifierNode2, tstringliteralNode3);
        }
        nodeList.add(pstringentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new8() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList8 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList7 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList6 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PStringEntry pstringentryNode1;
        {
            TIdentifier tidentifierNode2;
            TStringLiteral tstringliteralNode3;
            tidentifierNode2 = (TIdentifier) nodeArrayList3.get(0);
            tstringliteralNode3 = (TStringLiteral) nodeArrayList6.get(0);
            pstringentryNode1 = new AStrbraceStringEntry(tidentifierNode2, tstringliteralNode3);
        }
        nodeList.add(pstringentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new9() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList6 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PStringEntry pstringentryNode1;
        {
            TIdentifier tidentifierNode2;
            TStringLiteral tstringliteralNode3;
            tidentifierNode2 = (TIdentifier) nodeArrayList3.get(0);
            tstringliteralNode3 = (TStringLiteral) nodeArrayList5.get(0);
            pstringentryNode1 = new AStrparenStringEntry(tidentifierNode2, tstringliteralNode3);
        }
        nodeList.add(pstringentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new10() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList7 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList6 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PStringEntry pstringentryNode1;
        {
            TIdentifier tidentifierNode2;
            TStringLiteral tstringliteralNode3;
            tidentifierNode2 = (TIdentifier) nodeArrayList3.get(0);
            tstringliteralNode3 = (TStringLiteral) nodeArrayList6.get(0);
            pstringentryNode1 = new AStrparenStringEntry(tidentifierNode2, tstringliteralNode3);
        }
        nodeList.add(pstringentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new11() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList7 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList6 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PStringEntry pstringentryNode1;
        {
            TIdentifier tidentifierNode2;
            TStringLiteral tstringliteralNode3;
            tidentifierNode2 = (TIdentifier) nodeArrayList3.get(0);
            tstringliteralNode3 = (TStringLiteral) nodeArrayList5.get(0);
            pstringentryNode1 = new AStrparenStringEntry(tidentifierNode2, tstringliteralNode3);
        }
        nodeList.add(pstringentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new12() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList8 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList7 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList6 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PStringEntry pstringentryNode1;
        {
            TIdentifier tidentifierNode2;
            TStringLiteral tstringliteralNode3;
            tidentifierNode2 = (TIdentifier) nodeArrayList3.get(0);
            tstringliteralNode3 = (TStringLiteral) nodeArrayList6.get(0);
            pstringentryNode1 = new AStrparenStringEntry(tidentifierNode2, tstringliteralNode3);
        }
        nodeList.add(pstringentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new13() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PEntry pentryNode1;
        {
            PEntryDef pentrydefNode2;
            TIdentifier tidentifierNode3;
            LinkedList listNode5 = new LinkedList();
            TRBrace trbraceNode6;
            pentrydefNode2 = (PEntryDef) nodeArrayList1.get(0);
            tidentifierNode3 = (TIdentifier) nodeArrayList3.get(0);
            {
                LinkedList listNode4 = new LinkedList();
                listNode4 = (LinkedList) nodeArrayList4.get(0);
                if (listNode4 != null) {
                    listNode5.addAll(listNode4);
                }
            }
            trbraceNode6 = (TRBrace) nodeArrayList5.get(0);
            pentryNode1 = new AEntrybraceEntry(pentrydefNode2, tidentifierNode3, listNode5, trbraceNode6);
        }
        nodeList.add(pentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new14() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList6 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PEntry pentryNode1;
        {
            PEntryDef pentrydefNode2;
            TIdentifier tidentifierNode3;
            LinkedList listNode5 = new LinkedList();
            TRBrace trbraceNode6;
            pentrydefNode2 = (PEntryDef) nodeArrayList1.get(0);
            tidentifierNode3 = (TIdentifier) nodeArrayList3.get(0);
            {
                LinkedList listNode4 = new LinkedList();
                listNode4 = (LinkedList) nodeArrayList4.get(0);
                if (listNode4 != null) {
                    listNode5.addAll(listNode4);
                }
            }
            trbraceNode6 = (TRBrace) nodeArrayList6.get(0);
            pentryNode1 = new AEntrybraceEntry(pentrydefNode2, tidentifierNode3, listNode5, trbraceNode6);
        }
        nodeList.add(pentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new15() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PEntry pentryNode1;
        {
            PEntryDef pentrydefNode2;
            TIdentifier tidentifierNode3;
            LinkedList listNode5 = new LinkedList();
            TRParen trparenNode6;
            pentrydefNode2 = (PEntryDef) nodeArrayList1.get(0);
            tidentifierNode3 = (TIdentifier) nodeArrayList3.get(0);
            {
                LinkedList listNode4 = new LinkedList();
                listNode4 = (LinkedList) nodeArrayList4.get(0);
                if (listNode4 != null) {
                    listNode5.addAll(listNode4);
                }
            }
            trparenNode6 = (TRParen) nodeArrayList5.get(0);
            pentryNode1 = new AEntryparenEntry(pentrydefNode2, tidentifierNode3, listNode5, trparenNode6);
        }
        nodeList.add(pentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new16() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList6 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PEntry pentryNode1;
        {
            PEntryDef pentrydefNode2;
            TIdentifier tidentifierNode3;
            LinkedList listNode5 = new LinkedList();
            TRParen trparenNode6;
            pentrydefNode2 = (PEntryDef) nodeArrayList1.get(0);
            tidentifierNode3 = (TIdentifier) nodeArrayList3.get(0);
            {
                LinkedList listNode4 = new LinkedList();
                listNode4 = (LinkedList) nodeArrayList4.get(0);
                if (listNode4 != null) {
                    listNode5.addAll(listNode4);
                }
            }
            trparenNode6 = (TRParen) nodeArrayList6.get(0);
            pentryNode1 = new AEntryparenEntry(pentrydefNode2, tidentifierNode3, listNode5, trparenNode6);
        }
        nodeList.add(pentryNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new17() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PEntryDef pentrydefNode1;
        {
            TEntryName tentrynameNode2;
            tentrynameNode2 = (TEntryName) nodeArrayList1.get(0);
            pentrydefNode1 = new AEntryDef(tentrynameNode2);
        }
        nodeList.add(pentrydefNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new18() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PKeyvalDecl pkeyvaldeclNode1;
        {
            TIdentifier tidentifierNode2;
            PValOrSid pvalorsidNode3;
            LinkedList listNode4 = new LinkedList();
            tidentifierNode2 = (TIdentifier) nodeArrayList2.get(0);
            pvalorsidNode3 = (PValOrSid) nodeArrayList4.get(0);
            {
            }
            pkeyvaldeclNode1 = new AKeyvalDecl(tidentifierNode2, pvalorsidNode3, listNode4);
        }
        nodeList.add(pkeyvaldeclNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new19() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList5 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList4 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PKeyvalDecl pkeyvaldeclNode1;
        {
            TIdentifier tidentifierNode2;
            PValOrSid pvalorsidNode3;
            LinkedList listNode5 = new LinkedList();
            tidentifierNode2 = (TIdentifier) nodeArrayList2.get(0);
            pvalorsidNode3 = (PValOrSid) nodeArrayList4.get(0);
            {
                LinkedList listNode4 = new LinkedList();
                listNode4 = (LinkedList) nodeArrayList5.get(0);
                if (listNode4 != null) {
                    listNode5.addAll(listNode4);
                }
            }
            pkeyvaldeclNode1 = new AKeyvalDecl(tidentifierNode2, pvalorsidNode3, listNode5);
        }
        nodeList.add(pkeyvaldeclNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new20() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PValOrSid pvalorsidNode1;
        {
            TStringLiteral tstringliteralNode2;
            tstringliteralNode2 = (TStringLiteral) nodeArrayList1.get(0);
            pvalorsidNode1 = new AValueBValOrSid(tstringliteralNode2);
        }
        nodeList.add(pvalorsidNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new21() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PValOrSid pvalorsidNode1;
        {
            @SuppressWarnings("unused") Object nullNode2 = null;
            pvalorsidNode1 = new AValueQValOrSid(null);
        }
        nodeList.add(pvalorsidNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new22() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PValOrSid pvalorsidNode1;
        {
            TStringLiteral tstringliteralNode2;
            tstringliteralNode2 = (TStringLiteral) nodeArrayList2.get(0);
            pvalorsidNode1 = new AValueQValOrSid(tstringliteralNode2);
        }
        nodeList.add(pvalorsidNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new23() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PValOrSid pvalorsidNode1;
        {
            TNumber tnumberNode2;
            tnumberNode2 = (TNumber) nodeArrayList1.get(0);
            pvalorsidNode1 = new ANumValOrSid(tnumberNode2);
        }
        nodeList.add(pvalorsidNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new24() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PValOrSid pvalorsidNode1;
        {
            TIdentifier tidentifierNode2;
            tidentifierNode2 = (TIdentifier) nodeArrayList1.get(0);
            pvalorsidNode1 = new AIdValOrSid(tidentifierNode2);
        }
        nodeList.add(pvalorsidNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new25() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PConcat pconcatNode1;
        {
            PValOrSid pvalorsidNode2;
            pvalorsidNode2 = (PValOrSid) nodeArrayList2.get(0);
            pconcatNode1 = new AConcat(pvalorsidNode2);
        }
        nodeList.add(pconcatNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new26() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode2 = new LinkedList();
        {
            PBibEntry pbibentryNode1;
            pbibentryNode1 = (PBibEntry) nodeArrayList1.get(0);
            if (pbibentryNode1 != null) {
                listNode2.add(pbibentryNode1);
            }
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new27() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode3 = new LinkedList();
        {
            LinkedList listNode1 = new LinkedList();
            PBibEntry pbibentryNode2;
            listNode1 = (LinkedList) nodeArrayList1.get(0);
            pbibentryNode2 = (PBibEntry) nodeArrayList2.get(0);
            if (listNode1 != null) {
                listNode3.addAll(listNode1);
            }
            if (pbibentryNode2 != null) {
                listNode3.add(pbibentryNode2);
            }
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new28() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode2 = new LinkedList();
        {
            PKeyvalDecl pkeyvaldeclNode1;
            pkeyvaldeclNode1 = (PKeyvalDecl) nodeArrayList1.get(0);
            if (pkeyvaldeclNode1 != null) {
                listNode2.add(pkeyvaldeclNode1);
            }
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new29() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode3 = new LinkedList();
        {
            LinkedList listNode1 = new LinkedList();
            PKeyvalDecl pkeyvaldeclNode2;
            listNode1 = (LinkedList) nodeArrayList1.get(0);
            pkeyvaldeclNode2 = (PKeyvalDecl) nodeArrayList2.get(0);
            if (listNode1 != null) {
                listNode3.addAll(listNode1);
            }
            if (pkeyvaldeclNode2 != null) {
                listNode3.add(pkeyvaldeclNode2);
            }
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new30() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode2 = new LinkedList();
        {
            PConcat pconcatNode1;
            pconcatNode1 = (PConcat) nodeArrayList1.get(0);
            if (pconcatNode1 != null) {
                listNode2.add(pconcatNode1);
            }
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new31() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode3 = new LinkedList();
        {
            LinkedList listNode1 = new LinkedList();
            PConcat pconcatNode2;
            listNode1 = (LinkedList) nodeArrayList1.get(0);
            pconcatNode2 = (PConcat) nodeArrayList2.get(0);
            if (listNode1 != null) {
                listNode3.addAll(listNode1);
            }
            if (pconcatNode2 != null) {
                listNode3.add(pconcatNode2);
            }
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    private static int[][][] actionTable;

    private static int[][][] gotoTable;

    private static String[] errorMessages;

    private static int[] errors;

    static {
        try {
            DataInputStream s = new DataInputStream(new BufferedInputStream(Parser.class.getResourceAsStream("parser.dat")));
            int length = s.readInt();
            Parser.actionTable = new int[length][][];
            for (int i = 0; i < Parser.actionTable.length; i++) {
                length = s.readInt();
                Parser.actionTable[i] = new int[length][3];
                for (int j = 0; j < Parser.actionTable[i].length; j++) {
                    for (int k = 0; k < 3; k++) {
                        Parser.actionTable[i][j][k] = s.readInt();
                    }
                }
            }
            length = s.readInt();
            gotoTable = new int[length][][];
            for (int i = 0; i < gotoTable.length; i++) {
                length = s.readInt();
                gotoTable[i] = new int[length][2];
                for (int j = 0; j < gotoTable[i].length; j++) {
                    for (int k = 0; k < 2; k++) {
                        gotoTable[i][j][k] = s.readInt();
                    }
                }
            }
            length = s.readInt();
            errorMessages = new String[length];
            for (int i = 0; i < errorMessages.length; i++) {
                length = s.readInt();
                StringBuffer buffer = new StringBuffer();
                for (int j = 0; j < length; j++) {
                    buffer.append(s.readChar());
                }
                errorMessages[i] = buffer.toString();
            }
            length = s.readInt();
            errors = new int[length];
            for (int i = 0; i < errors.length; i++) {
                errors[i] = s.readInt();
            }
            s.close();
        } catch (Exception e) {
            throw new RuntimeException("The file \"parser.dat\" is either missing or corrupted.");
        }
    }
}
