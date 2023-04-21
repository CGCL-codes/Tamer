package argModelChecker;

import java.io.StreamTokenizer;
import java.util.StringTokenizer;
import java.util.Hashtable;
import Jona.Variable;

public class ArgModelNode {

    public ArgModelNode[] children;

    public ArgModelNode parent;

    public Hashtable transitions = new Hashtable(10);

    public ArgModelNode() {
    }

    public void addArgModelNode(ArgModelNode amn1) {
        if (children != null) {
            children = new ArgModelNode[1];
            children[0] = amn1;
        } else {
            children = addToList(children, amn1);
            transitions.put(":", "OtherState");
        }
    }

    private ArgModelNode[] addToList(ArgModelNode[] list, ArgModelNode element) {
        int i, length;
        ArgModelNode[] temp;
        length = list.length;
        temp = new ArgModelNode[length + 1];
        for (i = 0; i < length; ++i) {
            if (list[i] == element) {
                return list;
            }
            temp[i] = list[i];
        }
        temp[length] = element;
        return temp;
    }

    public void handleState(StreamTokenizer st, Hashtable rowTable) throws Exception {
        String word = "";
        int c = st.nextToken();
        if (c != StreamTokenizer.TT_EOL && c != StreamTokenizer.TT_EOF) {
            if (c == StreamTokenizer.TT_WORD) word = st.sval.toLowerCase(); else word = "" + (char) (st.ttype);
            System.out.println("Found word: " + word);
        }
        Class nodetype = (Class) transitions.get(word);
        if (nodetype != null) {
            ArgModelNode node = (ArgModelNode) nodetype.newInstance();
            node.handleState(st, rowTable);
        } else if ((nodetype = (Class) transitions.get("WORD")) != null) {
            wordHandler(st, rowTable);
            if (nodetype != null) {
                ArgModelNode node = (ArgModelNode) nodetype.newInstance();
                node.handleState(st, rowTable);
            }
        }
    }

    public void wordHandler(StreamTokenizer st, Hashtable rowTable) throws Exception {
        System.out.println("Wrong wordhandler!!");
    }
}

class rootState extends ArgModelNode {

    public rootState() {
        transitions.put("WORD", assignTokenState.class);
    }

    public void wordHandler(StreamTokenizer st, Hashtable rowTable) throws Exception {
        System.out.println("rootState::wordHandler");
        rowTable.put("Label", st.sval);
    }
}

class assignTokenState extends ArgModelNode {

    public assignTokenState() {
        transitions.put(":", labelAssignState.class);
        transitions.put("=", variableAssignState.class);
    }
}

class labelAssignState extends ArgModelNode {

    public labelAssignState() {
    }
}

class variableAssignState extends ArgModelNode {

    public variableAssignState() {
        transitions.put("WORD", assignTokenState.class);
    }

    public void wordHandler(StreamTokenizer st, Hashtable rowTable) throws Exception {
        System.out.println("VariableAssignStage::wordHandler");
        String word = st.sval.toLowerCase();
        Variable.put(rowTable.get("Label"), word);
    }
}
