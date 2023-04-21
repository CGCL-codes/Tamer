package jastgen.frontend;

import java.util.*;
import jastgen.nodes.*;

public class ByCaper {

    public static enum Token {

        eof, ASTER, BRACEL, BRACER, BRACKETL, BRACKETR, COLON, COMMA, DOT, EXCL, GT, IDENTIFIER, IMPORT, INTERFACE, LT, METHODBODY, PACKAGE, SEMICOLON, STATIC
    }

    public static interface SemanticAction {

        void syntaxError();

        void stackOverflow();

        ClassDef buildClassDef(Type arg1, List arg2);

        ClassDef buildClassDef(Type arg1, Type arg2, List arg3);

        Field buildField(Type arg1, boolean arg2, String arg3);

        Field buildField(Type arg1, boolean arg2, String arg3, String arg4);

        Import buildImport(boolean arg1, List arg2);

        InterfaceDef buildInterfaceDef(Type arg1, List arg2);

        List buildList();

        List buildList(List arg1, Field arg2);

        List buildList(List arg1, Import arg2);

        List buildList(List arg1, Object arg2);

        List buildList(List arg1, String arg2);

        List buildList(List arg1, Type arg2);

        List buildList(Object arg1);

        List buildList(String arg1);

        List buildList(Type arg1);

        List through(List arg1);

        Module buildModule(List arg1, List arg2, List arg3);

        Object through(ClassDef arg1);

        Object through(InterfaceDef arg1);

        Type buildTypeDef(String arg1);

        Type buildTypeDef(String arg1, List arg2);

        boolean buildFalse();

        boolean buildTrue();
    }

    public static class Parser {

        private static class StackFrame {

            public final State state;

            public final Object value;

            public StackFrame(State state, Object value) {
                this.state = state;
                this.value = value;
            }
        }

        private static class Stack {

            private final ArrayList<StackFrame> main = new ArrayList<StackFrame>();

            private final ArrayList<StackFrame> temp = new ArrayList<StackFrame>();

            private int gap = 0;

            public Stack() {
            }

            public void resetTemp() {
                gap = main.size();
                temp.clear();
            }

            public void commitTemp() {
                main.ensureCapacity(gap + temp.size());
                main.subList(gap, main.size()).clear();
                main.addAll(temp);
            }

            public boolean push(StackFrame f) {
                temp.add(f);
                return true;
            }

            public void pop(int n) {
                if (temp.size() < n) {
                    n -= temp.size();
                    temp.clear();
                    gap -= n;
                } else {
                    temp.subList(temp.size() - n, temp.size()).clear();
                }
            }

            public StackFrame peek() {
                if (!temp.isEmpty()) {
                    return temp.get(temp.size() - 1);
                } else {
                    return main.get(gap - 1);
                }
            }

            public StackFrame get(int base, int i) {
                int n = temp.size();
                if (base - i <= n) {
                    return temp.get(n - (base - i));
                } else {
                    return main.get(gap - (base - n) + i);
                }
            }

            public void clear() {
                main.clear();
            }
        }

        public Parser(SemanticAction sa) {
            this.sa = sa;
            reset();
        }

        public void reset() {
            error = false;
            accepted = false;
            stack.clear();
            stack.resetTemp();
            if (pushToStack(state0, null)) {
                stack.commitTemp();
            } else {
                sa.stackOverflow();
                error = true;
            }
        }

        public boolean post(Token token, Object value) {
            assert (!error);
            stack.resetTemp();
            while (stack.peek().state.state(token, value)) ;
            if (!error) {
                stack.commitTemp();
            }
            return accepted || error;
        }

        /**
		 * The result of isError() is returned together
		 * in the C++ version and C# version.
		 * Please call isError() to check in the Java version.
		 */
        public Object accept() {
            assert (accepted || error);
            if (error) return null;
            return acceptedValue;
        }

        public boolean isError() {
            return error;
        }

        private final Stack stack = new Stack();

        private final SemanticAction sa;

        private boolean accepted;

        private boolean error;

        private Object acceptedValue;

        private boolean pushToStack(State s, Object v) {
            assert (!error);
            if (stack.push(new StackFrame(s, v))) return true;
            error = true;
            sa.stackOverflow();
            return false;
        }

        private Object getFromStack(int base, int i) {
            return stack.get(base, i).value;
        }

        private static interface State {

            boolean gotof(int i, Object v);

            boolean state(Token t, Object v);
        }

        private final State state0 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 11:
                        return pushToStack(state1, v);
                    case 13:
                        return pushToStack(state2, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case PACKAGE:
                        pushToStack(state5, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state1 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case eof:
                        accepted = true;
                        acceptedValue = getFromStack(1, 0);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state2 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 8:
                        return pushToStack(state3, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        {
                            List r = sa.buildList();
                            stack.pop(0);
                            return stack.peek().state.gotof(8, r);
                        }
                    case IMPORT:
                        {
                            List r = sa.buildList();
                            stack.pop(0);
                            return stack.peek().state.gotof(8, r);
                        }
                    case INTERFACE:
                        {
                            List r = sa.buildList();
                            stack.pop(0);
                            return stack.peek().state.gotof(8, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state3 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 4:
                        return pushToStack(state8, v);
                    case 14:
                        return pushToStack(state4, v);
                    case 12:
                        return pushToStack(state19, v);
                    case 6:
                        return pushToStack(state21, v);
                    case 0:
                        return pushToStack(state22, v);
                    case 15:
                        return pushToStack(state28, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        pushToStack(state47, value);
                        return false;
                    case IMPORT:
                        pushToStack(state9, value);
                        return false;
                    case INTERFACE:
                        pushToStack(state23, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state4 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 12:
                        return pushToStack(state20, v);
                    case 6:
                        return pushToStack(state21, v);
                    case 0:
                        return pushToStack(state22, v);
                    case 15:
                        return pushToStack(state28, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case eof:
                        {
                            List arg0 = (List) getFromStack(3, 0);
                            List arg1 = (List) getFromStack(3, 1);
                            List arg2 = (List) getFromStack(3, 2);
                            Module r = sa.buildModule(arg0, arg1, arg2);
                            stack.pop(3);
                            return stack.peek().state.gotof(11, r);
                        }
                    case IDENTIFIER:
                        pushToStack(state47, value);
                        return false;
                    case INTERFACE:
                        pushToStack(state23, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state5 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 2:
                        return pushToStack(state6, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        pushToStack(state16, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state6 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case DOT:
                        pushToStack(state17, value);
                        return false;
                    case SEMICOLON:
                        pushToStack(state7, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state7 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        {
                            List arg0 = (List) getFromStack(3, 1);
                            List r = sa.through(arg0);
                            stack.pop(3);
                            return stack.peek().state.gotof(13, r);
                        }
                    case IMPORT:
                        {
                            List arg0 = (List) getFromStack(3, 1);
                            List r = sa.through(arg0);
                            stack.pop(3);
                            return stack.peek().state.gotof(13, r);
                        }
                    case INTERFACE:
                        {
                            List arg0 = (List) getFromStack(3, 1);
                            List r = sa.through(arg0);
                            stack.pop(3);
                            return stack.peek().state.gotof(13, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state8 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        {
                            List arg0 = (List) getFromStack(2, 0);
                            Import arg1 = (Import) getFromStack(2, 1);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(2);
                            return stack.peek().state.gotof(8, r);
                        }
                    case IMPORT:
                        {
                            List arg0 = (List) getFromStack(2, 0);
                            Import arg1 = (Import) getFromStack(2, 1);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(2);
                            return stack.peek().state.gotof(8, r);
                        }
                    case INTERFACE:
                        {
                            List arg0 = (List) getFromStack(2, 0);
                            Import arg1 = (Import) getFromStack(2, 1);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(2);
                            return stack.peek().state.gotof(8, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state9 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 10:
                        return pushToStack(state10, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        {
                            boolean r = sa.buildFalse();
                            stack.pop(0);
                            return stack.peek().state.gotof(10, r);
                        }
                    case STATIC:
                        pushToStack(state43, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state10 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 5:
                        return pushToStack(state11, v);
                    case 2:
                        return pushToStack(state13, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        pushToStack(state16, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state11 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case SEMICOLON:
                        pushToStack(state12, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state12 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        {
                            boolean arg0 = (Boolean) getFromStack(4, 1);
                            List arg1 = (List) getFromStack(4, 2);
                            Import r = sa.buildImport(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(4, r);
                        }
                    case IMPORT:
                        {
                            boolean arg0 = (Boolean) getFromStack(4, 1);
                            List arg1 = (List) getFromStack(4, 2);
                            Import r = sa.buildImport(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(4, r);
                        }
                    case INTERFACE:
                        {
                            boolean arg0 = (Boolean) getFromStack(4, 1);
                            List arg1 = (List) getFromStack(4, 2);
                            Import r = sa.buildImport(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(4, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state13 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case DOT:
                        pushToStack(state14, value);
                        return false;
                    case SEMICOLON:
                        {
                            List arg0 = (List) getFromStack(1, 0);
                            List r = sa.through(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(5, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state14 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case ASTER:
                        pushToStack(state15, value);
                        return false;
                    case IDENTIFIER:
                        pushToStack(state18, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state15 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case SEMICOLON:
                        {
                            List arg0 = (List) getFromStack(3, 0);
                            String arg1 = (String) getFromStack(3, 2);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(3);
                            return stack.peek().state.gotof(5, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state16 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case DOT:
                        {
                            String arg0 = (String) getFromStack(1, 0);
                            List r = sa.buildList(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(2, r);
                        }
                    case SEMICOLON:
                        {
                            String arg0 = (String) getFromStack(1, 0);
                            List r = sa.buildList(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(2, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state17 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        pushToStack(state18, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state18 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case DOT:
                        {
                            List arg0 = (List) getFromStack(3, 0);
                            String arg1 = (String) getFromStack(3, 2);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(3);
                            return stack.peek().state.gotof(2, r);
                        }
                    case SEMICOLON:
                        {
                            List arg0 = (List) getFromStack(3, 0);
                            String arg1 = (String) getFromStack(3, 2);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(3);
                            return stack.peek().state.gotof(2, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state19 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case eof:
                        {
                            Object arg0 = (Object) getFromStack(1, 0);
                            List r = sa.buildList(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(14, r);
                        }
                    case IDENTIFIER:
                        {
                            Object arg0 = (Object) getFromStack(1, 0);
                            List r = sa.buildList(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(14, r);
                        }
                    case INTERFACE:
                        {
                            Object arg0 = (Object) getFromStack(1, 0);
                            List r = sa.buildList(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(14, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state20 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case eof:
                        {
                            List arg0 = (List) getFromStack(2, 0);
                            Object arg1 = (Object) getFromStack(2, 1);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(2);
                            return stack.peek().state.gotof(14, r);
                        }
                    case IDENTIFIER:
                        {
                            List arg0 = (List) getFromStack(2, 0);
                            Object arg1 = (Object) getFromStack(2, 1);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(2);
                            return stack.peek().state.gotof(14, r);
                        }
                    case INTERFACE:
                        {
                            List arg0 = (List) getFromStack(2, 0);
                            Object arg1 = (Object) getFromStack(2, 1);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(2);
                            return stack.peek().state.gotof(14, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state21 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case eof:
                        {
                            InterfaceDef arg0 = (InterfaceDef) getFromStack(1, 0);
                            Object r = sa.through(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(12, r);
                        }
                    case IDENTIFIER:
                        {
                            InterfaceDef arg0 = (InterfaceDef) getFromStack(1, 0);
                            Object r = sa.through(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(12, r);
                        }
                    case INTERFACE:
                        {
                            InterfaceDef arg0 = (InterfaceDef) getFromStack(1, 0);
                            Object r = sa.through(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(12, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state22 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case eof:
                        {
                            ClassDef arg0 = (ClassDef) getFromStack(1, 0);
                            Object r = sa.through(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(12, r);
                        }
                    case IDENTIFIER:
                        {
                            ClassDef arg0 = (ClassDef) getFromStack(1, 0);
                            Object r = sa.through(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(12, r);
                        }
                    case INTERFACE:
                        {
                            ClassDef arg0 = (ClassDef) getFromStack(1, 0);
                            Object r = sa.through(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(12, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state23 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 15:
                        return pushToStack(state24, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        pushToStack(state47, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state24 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACEL:
                        pushToStack(state25, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state25 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 7:
                        return pushToStack(state26, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACER:
                        {
                            List r = sa.buildList();
                            stack.pop(0);
                            return stack.peek().state.gotof(7, r);
                        }
                    case IDENTIFIER:
                        {
                            List r = sa.buildList();
                            stack.pop(0);
                            return stack.peek().state.gotof(7, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state26 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 3:
                        return pushToStack(state37, v);
                    case 15:
                        return pushToStack(state38, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACER:
                        pushToStack(state27, value);
                        return false;
                    case IDENTIFIER:
                        pushToStack(state47, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state27 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case eof:
                        {
                            Type arg0 = (Type) getFromStack(5, 1);
                            List arg1 = (List) getFromStack(5, 3);
                            InterfaceDef r = sa.buildInterfaceDef(arg0, arg1);
                            stack.pop(5);
                            return stack.peek().state.gotof(6, r);
                        }
                    case IDENTIFIER:
                        {
                            Type arg0 = (Type) getFromStack(5, 1);
                            List arg1 = (List) getFromStack(5, 3);
                            InterfaceDef r = sa.buildInterfaceDef(arg0, arg1);
                            stack.pop(5);
                            return stack.peek().state.gotof(6, r);
                        }
                    case INTERFACE:
                        {
                            Type arg0 = (Type) getFromStack(5, 1);
                            List arg1 = (List) getFromStack(5, 3);
                            InterfaceDef r = sa.buildInterfaceDef(arg0, arg1);
                            stack.pop(5);
                            return stack.peek().state.gotof(6, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state28 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACEL:
                        pushToStack(state29, value);
                        return false;
                    case COLON:
                        pushToStack(state32, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state29 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 7:
                        return pushToStack(state30, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACER:
                        {
                            List r = sa.buildList();
                            stack.pop(0);
                            return stack.peek().state.gotof(7, r);
                        }
                    case IDENTIFIER:
                        {
                            List r = sa.buildList();
                            stack.pop(0);
                            return stack.peek().state.gotof(7, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state30 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 3:
                        return pushToStack(state37, v);
                    case 15:
                        return pushToStack(state38, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACER:
                        pushToStack(state31, value);
                        return false;
                    case IDENTIFIER:
                        pushToStack(state47, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state31 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case eof:
                        {
                            Type arg0 = (Type) getFromStack(4, 0);
                            List arg1 = (List) getFromStack(4, 2);
                            ClassDef r = sa.buildClassDef(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(0, r);
                        }
                    case IDENTIFIER:
                        {
                            Type arg0 = (Type) getFromStack(4, 0);
                            List arg1 = (List) getFromStack(4, 2);
                            ClassDef r = sa.buildClassDef(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(0, r);
                        }
                    case INTERFACE:
                        {
                            Type arg0 = (Type) getFromStack(4, 0);
                            List arg1 = (List) getFromStack(4, 2);
                            ClassDef r = sa.buildClassDef(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(0, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state32 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 15:
                        return pushToStack(state33, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        pushToStack(state47, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state33 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACEL:
                        pushToStack(state34, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state34 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 7:
                        return pushToStack(state35, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACER:
                        {
                            List r = sa.buildList();
                            stack.pop(0);
                            return stack.peek().state.gotof(7, r);
                        }
                    case IDENTIFIER:
                        {
                            List r = sa.buildList();
                            stack.pop(0);
                            return stack.peek().state.gotof(7, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state35 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 3:
                        return pushToStack(state37, v);
                    case 15:
                        return pushToStack(state38, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACER:
                        pushToStack(state36, value);
                        return false;
                    case IDENTIFIER:
                        pushToStack(state47, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state36 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case eof:
                        {
                            Type arg0 = (Type) getFromStack(6, 0);
                            Type arg1 = (Type) getFromStack(6, 2);
                            List arg2 = (List) getFromStack(6, 4);
                            ClassDef r = sa.buildClassDef(arg0, arg1, arg2);
                            stack.pop(6);
                            return stack.peek().state.gotof(0, r);
                        }
                    case IDENTIFIER:
                        {
                            Type arg0 = (Type) getFromStack(6, 0);
                            Type arg1 = (Type) getFromStack(6, 2);
                            List arg2 = (List) getFromStack(6, 4);
                            ClassDef r = sa.buildClassDef(arg0, arg1, arg2);
                            stack.pop(6);
                            return stack.peek().state.gotof(0, r);
                        }
                    case INTERFACE:
                        {
                            Type arg0 = (Type) getFromStack(6, 0);
                            Type arg1 = (Type) getFromStack(6, 2);
                            List arg2 = (List) getFromStack(6, 4);
                            ClassDef r = sa.buildClassDef(arg0, arg1, arg2);
                            stack.pop(6);
                            return stack.peek().state.gotof(0, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state37 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACER:
                        {
                            List arg0 = (List) getFromStack(2, 0);
                            Field arg1 = (Field) getFromStack(2, 1);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(2);
                            return stack.peek().state.gotof(7, r);
                        }
                    case IDENTIFIER:
                        {
                            List arg0 = (List) getFromStack(2, 0);
                            Field arg1 = (Field) getFromStack(2, 1);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(2);
                            return stack.peek().state.gotof(7, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state38 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 9:
                        return pushToStack(state39, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case EXCL:
                        pushToStack(state44, value);
                        return false;
                    case IDENTIFIER:
                        {
                            boolean r = sa.buildFalse();
                            stack.pop(0);
                            return stack.peek().state.gotof(9, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state39 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        pushToStack(state40, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state40 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case METHODBODY:
                        pushToStack(state42, value);
                        return false;
                    case SEMICOLON:
                        pushToStack(state41, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state41 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACER:
                        {
                            Type arg0 = (Type) getFromStack(4, 0);
                            boolean arg1 = (Boolean) getFromStack(4, 1);
                            String arg2 = (String) getFromStack(4, 2);
                            Field r = sa.buildField(arg0, arg1, arg2);
                            stack.pop(4);
                            return stack.peek().state.gotof(3, r);
                        }
                    case IDENTIFIER:
                        {
                            Type arg0 = (Type) getFromStack(4, 0);
                            boolean arg1 = (Boolean) getFromStack(4, 1);
                            String arg2 = (String) getFromStack(4, 2);
                            Field r = sa.buildField(arg0, arg1, arg2);
                            stack.pop(4);
                            return stack.peek().state.gotof(3, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state42 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACER:
                        {
                            Type arg0 = (Type) getFromStack(4, 0);
                            boolean arg1 = (Boolean) getFromStack(4, 1);
                            String arg2 = (String) getFromStack(4, 2);
                            String arg3 = (String) getFromStack(4, 3);
                            Field r = sa.buildField(arg0, arg1, arg2, arg3);
                            stack.pop(4);
                            return stack.peek().state.gotof(3, r);
                        }
                    case IDENTIFIER:
                        {
                            Type arg0 = (Type) getFromStack(4, 0);
                            boolean arg1 = (Boolean) getFromStack(4, 1);
                            String arg2 = (String) getFromStack(4, 2);
                            String arg3 = (String) getFromStack(4, 3);
                            Field r = sa.buildField(arg0, arg1, arg2, arg3);
                            stack.pop(4);
                            return stack.peek().state.gotof(3, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state43 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        {
                            boolean r = sa.buildTrue();
                            stack.pop(1);
                            return stack.peek().state.gotof(10, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state44 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        {
                            boolean r = sa.buildTrue();
                            stack.pop(1);
                            return stack.peek().state.gotof(9, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state45 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 15:
                        return pushToStack(state50, v);
                    case 1:
                        return pushToStack(state48, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        pushToStack(state47, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state46 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                switch(nonterminalIndex) {
                    case 15:
                        return pushToStack(state51, v);
                    default:
                        assert (false);
                        return false;
                }
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case IDENTIFIER:
                        pushToStack(state47, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state47 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACEL:
                        {
                            String arg0 = (String) getFromStack(1, 0);
                            Type r = sa.buildTypeDef(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(15, r);
                        }
                    case COLON:
                        {
                            String arg0 = (String) getFromStack(1, 0);
                            Type r = sa.buildTypeDef(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(15, r);
                        }
                    case COMMA:
                        {
                            String arg0 = (String) getFromStack(1, 0);
                            Type r = sa.buildTypeDef(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(15, r);
                        }
                    case EXCL:
                        {
                            String arg0 = (String) getFromStack(1, 0);
                            Type r = sa.buildTypeDef(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(15, r);
                        }
                    case GT:
                        {
                            String arg0 = (String) getFromStack(1, 0);
                            Type r = sa.buildTypeDef(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(15, r);
                        }
                    case IDENTIFIER:
                        {
                            String arg0 = (String) getFromStack(1, 0);
                            Type r = sa.buildTypeDef(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(15, r);
                        }
                    case LT:
                        pushToStack(state45, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state48 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case COMMA:
                        pushToStack(state46, value);
                        return false;
                    case GT:
                        pushToStack(state49, value);
                        return false;
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state49 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case BRACEL:
                        {
                            String arg0 = (String) getFromStack(4, 0);
                            List arg1 = (List) getFromStack(4, 2);
                            Type r = sa.buildTypeDef(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(15, r);
                        }
                    case COLON:
                        {
                            String arg0 = (String) getFromStack(4, 0);
                            List arg1 = (List) getFromStack(4, 2);
                            Type r = sa.buildTypeDef(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(15, r);
                        }
                    case COMMA:
                        {
                            String arg0 = (String) getFromStack(4, 0);
                            List arg1 = (List) getFromStack(4, 2);
                            Type r = sa.buildTypeDef(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(15, r);
                        }
                    case EXCL:
                        {
                            String arg0 = (String) getFromStack(4, 0);
                            List arg1 = (List) getFromStack(4, 2);
                            Type r = sa.buildTypeDef(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(15, r);
                        }
                    case GT:
                        {
                            String arg0 = (String) getFromStack(4, 0);
                            List arg1 = (List) getFromStack(4, 2);
                            Type r = sa.buildTypeDef(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(15, r);
                        }
                    case IDENTIFIER:
                        {
                            String arg0 = (String) getFromStack(4, 0);
                            List arg1 = (List) getFromStack(4, 2);
                            Type r = sa.buildTypeDef(arg0, arg1);
                            stack.pop(4);
                            return stack.peek().state.gotof(15, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state50 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case COMMA:
                        {
                            Type arg0 = (Type) getFromStack(1, 0);
                            List r = sa.buildList(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(1, r);
                        }
                    case GT:
                        {
                            Type arg0 = (Type) getFromStack(1, 0);
                            List r = sa.buildList(arg0);
                            stack.pop(1);
                            return stack.peek().state.gotof(1, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };

        private final State state51 = new State() {

            public boolean gotof(int nonterminalIndex, Object v) {
                assert (false);
                return false;
            }

            public boolean state(Token token, Object value) {
                switch(token) {
                    case COMMA:
                        {
                            List arg0 = (List) getFromStack(3, 0);
                            Type arg1 = (Type) getFromStack(3, 2);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(3);
                            return stack.peek().state.gotof(1, r);
                        }
                    case GT:
                        {
                            List arg0 = (List) getFromStack(3, 0);
                            Type arg1 = (Type) getFromStack(3, 2);
                            List r = sa.buildList(arg0, arg1);
                            stack.pop(3);
                            return stack.peek().state.gotof(1, r);
                        }
                    default:
                        sa.syntaxError();
                        error = true;
                        return false;
                }
            }
        };
    }
}
