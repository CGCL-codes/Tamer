package org.yuzz.xml;

import static org.yuzz.xml.NodeStatics.n;
import static org.yuzz.xml.NodeStatics.t;
import static org.yuzz.xml.NodeStatics.td;
import static org.yuzz.xml.NodeStatics.tr;
import org.yuzz.functor.Fun;
import org.yuzz.functor.TreeNode;
import org.yuzz.functor.Fun.F;
import org.yuzz.functor.Fun.F2;
import org.yuzz.functor.Predicate.Pred1;
import org.yuzz.functor.Procedure.Proc2;
import org.yuzz.xml.Xhtml.Table;
import org.yuzz.xml.Xhtml.Tr;

public class NodeOperators {

    public static final class StringGridToTable extends Fun.F2<Table, String[], Table> {

        public Table f(Table table, String[] row) {
            Tr tr = tr();
            for (String cell : row) {
                tr.add(td(t(cell)));
            }
            table.add(tr);
            return table;
        }
    }

    public static final class AddNodes<Root extends Node, Child extends Node> extends F2<Child, Root, Root> {

        @Override
        public Root f(Child child, Root root) {
            root.add(child);
            return root;
        }
    }

    public static final class AddNodesProc<Root extends Node, Child extends Node> extends Proc2<Root, Child> {

        @Override
        public void f(Root root, Child child) {
            root.add(child);
        }
    }

    public static class AttributeTest extends Pred1<TreeNode> {

        private final String _attributeName;

        private final String _value;

        public AttributeTest(String attribute, String value) {
            _attributeName = attribute;
            _value = value;
        }

        @Override
        public boolean test(TreeNode tn) {
            Node n = (Node) tn;
            return _value.equalsIgnoreCase(n.get(_attributeName));
        }
    }

    public static class TagNameTest extends Pred1<TreeNode> {

        private final String _tagName;

        public TagNameTest(String tagName) {
            _tagName = tagName;
        }

        @Override
        public boolean test(TreeNode tn) {
            Node n = (Node) tn;
            return _tagName.equalsIgnoreCase(n.getTag());
        }
    }

    public static class MkNode extends F<String, Node> {

        @Override
        public Node f(String tag) {
            return n(tag);
        }
    }
}
