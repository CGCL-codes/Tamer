package org.sablecc.objectmacro.launcher.syntax3.parser;

import org.sablecc.objectmacro.launcher.syntax3.lexer.*;
import org.sablecc.objectmacro.launcher.syntax3.node.*;
import org.sablecc.objectmacro.launcher.syntax3.analysis.*;
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
                                push(goTo(0), list);
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
                                push(goTo(3), list);
                            }
                            break;
                        case 7:
                            {
                                ArrayList list = new7();
                                push(goTo(3), list);
                            }
                            break;
                        case 8:
                            {
                                ArrayList list = new8();
                                push(goTo(4), list);
                            }
                            break;
                        case 9:
                            {
                                ArrayList list = new9();
                                push(goTo(4), list);
                            }
                            break;
                        case 10:
                            {
                                ArrayList list = new10();
                                push(goTo(5), list);
                            }
                            break;
                        case 11:
                            {
                                ArrayList list = new11();
                                push(goTo(5), list);
                            }
                            break;
                        case 12:
                            {
                                ArrayList list = new12();
                                push(goTo(5), list);
                            }
                            break;
                        case 13:
                            {
                                ArrayList list = new13();
                                push(goTo(6), list);
                            }
                            break;
                        case 14:
                            {
                                ArrayList list = new14();
                                push(goTo(6), list);
                            }
                            break;
                    }
                    break;
                case ACCEPT:
                    {
                        EOF node2 = (EOF) this.lexer.next();
                        PArgument node1 = (PArgument) pop().get(0);
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
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PArgument pargumentNode1;
        {
            LinkedList listNode3 = new LinkedList();
            {
                LinkedList listNode2 = new LinkedList();
                listNode2 = (LinkedList) nodeArrayList2.get(0);
                if (listNode2 != null) {
                    listNode3.addAll(listNode2);
                }
            }
            pargumentNode1 = new AShortOptionsArgument(listNode3);
        }
        nodeList.add(pargumentNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new1() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PArgument pargumentNode1;
        {
            PLongOption plongoptionNode2;
            plongoptionNode2 = (PLongOption) nodeArrayList2.get(0);
            pargumentNode1 = new ALongOptionArgument(plongoptionNode2);
        }
        nodeList.add(pargumentNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new2() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PArgument pargumentNode1;
        {
            TText ttextNode2;
            ttextNode2 = (TText) nodeArrayList1.get(0);
            pargumentNode1 = new APlainArgument(ttextNode2);
        }
        nodeList.add(pargumentNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new3() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode2 = new LinkedList();
        {
            PShortOption pshortoptionNode1;
            pshortoptionNode1 = (PShortOption) nodeArrayList1.get(0);
            if (pshortoptionNode1 != null) {
                listNode2.add(pshortoptionNode1);
            }
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new4() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode3 = new LinkedList();
        {
            LinkedList listNode1 = new LinkedList();
            PShortOption pshortoptionNode2;
            listNode1 = (LinkedList) nodeArrayList1.get(0);
            pshortoptionNode2 = (PShortOption) nodeArrayList2.get(0);
            if (listNode1 != null) {
                listNode3.addAll(listNode1);
            }
            if (pshortoptionNode2 != null) {
                listNode3.add(pshortoptionNode2);
            }
        }
        nodeList.add(listNode3);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new5() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PShortOption pshortoptionNode1;
        {
            TShortName tshortnameNode2;
            @SuppressWarnings("unused") Object nullNode3 = null;
            tshortnameNode2 = (TShortName) nodeArrayList1.get(0);
            pshortoptionNode1 = new AShortOption(tshortnameNode2, null);
        }
        nodeList.add(pshortoptionNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new6() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PShortOption pshortoptionNode1;
        pshortoptionNode1 = (PShortOption) nodeArrayList1.get(0);
        nodeList.add(pshortoptionNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new7() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PShortOption pshortoptionNode1;
        pshortoptionNode1 = (PShortOption) nodeArrayList1.get(0);
        nodeList.add(pshortoptionNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new8() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PShortOption pshortoptionNode1;
        {
            TShortName tshortnameNode2;
            POperand poperandNode3;
            tshortnameNode2 = (TShortName) nodeArrayList1.get(0);
            {
                @SuppressWarnings("unused") Object nullNode4 = null;
                poperandNode3 = new AOperand(null);
            }
            pshortoptionNode1 = new AShortOption(tshortnameNode2, poperandNode3);
        }
        nodeList.add(pshortoptionNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new9() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PShortOption pshortoptionNode1;
        {
            TShortName tshortnameNode2;
            POperand poperandNode3;
            tshortnameNode2 = (TShortName) nodeArrayList1.get(0);
            {
                TOperandText toperandtextNode4;
                toperandtextNode4 = (TOperandText) nodeArrayList3.get(0);
                poperandNode3 = new AOperand(toperandtextNode4);
            }
            pshortoptionNode1 = new AShortOption(tshortnameNode2, poperandNode3);
        }
        nodeList.add(pshortoptionNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new10() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PLongOption plongoptionNode1;
        {
            TLongName tlongnameNode2;
            @SuppressWarnings("unused") Object nullNode3 = null;
            tlongnameNode2 = (TLongName) nodeArrayList1.get(0);
            plongoptionNode1 = new ALongOption(tlongnameNode2, null);
        }
        nodeList.add(plongoptionNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new11() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PLongOption plongoptionNode1;
        {
            TLongName tlongnameNode2;
            POperand poperandNode3;
            tlongnameNode2 = (TLongName) nodeArrayList1.get(0);
            {
                @SuppressWarnings("unused") Object nullNode4 = null;
                poperandNode3 = new AOperand(null);
            }
            plongoptionNode1 = new ALongOption(tlongnameNode2, poperandNode3);
        }
        nodeList.add(plongoptionNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new12() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList3 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        PLongOption plongoptionNode1;
        {
            TLongName tlongnameNode2;
            POperand poperandNode3;
            tlongnameNode2 = (TLongName) nodeArrayList1.get(0);
            {
                TOperandText toperandtextNode4;
                toperandtextNode4 = (TOperandText) nodeArrayList3.get(0);
                poperandNode3 = new AOperand(toperandtextNode4);
            }
            plongoptionNode1 = new ALongOption(tlongnameNode2, poperandNode3);
        }
        nodeList.add(plongoptionNode1);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new13() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode2 = new LinkedList();
        {
            PShortOption pshortoptionNode1;
            pshortoptionNode1 = (PShortOption) nodeArrayList1.get(0);
            if (pshortoptionNode1 != null) {
                listNode2.add(pshortoptionNode1);
            }
        }
        nodeList.add(listNode2);
        return nodeList;
    }

    @SuppressWarnings("unchecked")
    ArrayList new14() {
        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();
        @SuppressWarnings("unused") ArrayList nodeArrayList2 = pop();
        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();
        LinkedList listNode3 = new LinkedList();
        {
            LinkedList listNode1 = new LinkedList();
            PShortOption pshortoptionNode2;
            listNode1 = (LinkedList) nodeArrayList1.get(0);
            pshortoptionNode2 = (PShortOption) nodeArrayList2.get(0);
            if (listNode1 != null) {
                listNode3.addAll(listNode1);
            }
            if (pshortoptionNode2 != null) {
                listNode3.add(pshortoptionNode2);
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
