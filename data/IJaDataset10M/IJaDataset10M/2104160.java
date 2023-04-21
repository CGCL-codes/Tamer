package de.grogra.pf.ui.edit;

import java.util.*;
import de.grogra.util.*;
import de.grogra.graph.*;
import de.grogra.persistence.*;
import de.grogra.pf.registry.*;
import de.grogra.pf.ui.*;
import de.grogra.pf.ui.edit.PropertyEditorTree.*;
import de.grogra.pf.ui.registry.AttributeItem;
import de.grogra.pf.ui.tree.*;
import de.grogra.pf.ui.util.LockProtectedCommand;
import de.grogra.reflect.*;

public class GraphSelectionImpl extends SelectionBase implements GraphSelection {

    final Object[] objects;

    final boolean[] isNode;

    final GraphState[] states;

    class Tree extends PropertyEditorTree implements AttributeChangeListener, ChangeBoundaryListener {

        Tree(Context c) {
            super(c);
            ((Node) root).setDecription(GraphSelectionImpl.this);
        }

        @Override
        protected void firstListenerAdded() {
            add: for (int i = 0; i < states.length; i++) {
                GraphState gs = states[i];
                for (int j = 0; j < i; j++) {
                    if (states[j] == gs) {
                        continue add;
                    }
                }
                gs.getGraph().addAttributeChangeListener(this);
                gs.getGraph().addChangeBoundaryListener(this);
            }
        }

        @Override
        protected void allListenersRemoved() {
            remove: for (int i = 0; i < states.length; i++) {
                GraphState gs = states[i];
                for (int j = 0; j < i; j++) {
                    if (states[j] == gs) {
                        continue remove;
                    }
                }
                gs.getGraph().removeAttributeChangeListener(this);
                gs.getGraph().removeChangeBoundaryListener(this);
            }
        }

        public void attributeChanged(AttributeChangeEvent e) {
            if (contains(e.getGraphState().getGraph(), e.getObject(), e.isNode())) {
                fireChanged(e);
                if ((size() == 1) && Attributes.NAME.isContained(e.getDependentAttributes())) {
                    valueForPathChanged(root.getTreePath(), null);
                }
            }
        }

        public void beginChange(GraphState gs) {
            boolean c = false;
            for (int i = 0; i < objects.length; i++) {
                if ((states[i] == gs) && (objects[i] != null) && (gs.getGraph().getLifeCycleState(objects[i], isNode[i]) != Graph.PERSISTENT)) {
                    objects[i] = null;
                    c = true;
                }
            }
            if (c) {
                fireChanged(null);
                if (size() == 0) {
                    GraphSelectionImpl.this.remove();
                } else {
                    valueForPathChanged(root.getTreePath(), null);
                }
            }
        }

        public void endChange(GraphState gs) {
        }

        public int getPriority() {
            return TOPOLOGY_PRIORITY;
        }

        @Override
        protected boolean isNodeAffectedBy(PropertyNode node, Object changeEvent) {
            GraphProperty p = (GraphProperty) node.getProperty();
            AttributeChangeEvent e = (AttributeChangeEvent) changeEvent;
            return (e == null) || (e.getAttribute() == null) || ((e.getAttribute() == p.attr) ? Reflection.overlaps(p.field, null, e.getSubField(), e.getIndices()) : p.attr.isContained(e.getDependentAttributes()));
        }
    }

    class GraphProperty extends Property {

        final Attribute attr;

        final FieldChain field;

        GraphProperty(Context ctx, Attribute a, FieldChain field) {
            super(ctx, (field == null) ? a.getType() : field.getField(field.length() - 1).getType());
            this.attr = a;
            this.field = field;
            if (field != null) {
                Field f = field.getField(field.length() - 1);
                if (f instanceof PersistenceField) {
                    setQuantity(((PersistenceField) f).getQuantity());
                }
            } else {
                setQuantity(a.getQuantity());
            }
        }

        @Override
        public String toString() {
            return '{' + attr.toString() + ',' + field + '}';
        }

        @Override
        public void setValue(Object value) {
            UI.getJobManager(getContext()).execute(new ForAll() {

                @Override
                void runImpl(GraphState gs, Object obj, boolean node, Object arg) {
                    AttributeAccessor acc = gs.getGraph().getAccessor(obj, node, attr);
                    if (acc != null) {
                        if (field == null) {
                            attr.set(obj, node, arg, gs);
                        } else {
                            ((ObjectAttributeAccessor) acc).setSubfield(obj, field, null, arg, gs);
                        }
                    }
                }
            }, value, getContext(), JobManager.ACTION_FLAGS);
        }

        public String getCommandName() {
            return null;
        }

        @Override
        public Object getValue() {
            Object value = null;
            boolean first = true;
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] != null) {
                    Object v = attr.get(objects[i], isNode[i], states[i]);
                    if (first) {
                        first = false;
                        value = v;
                    } else if (!Utils.equal(value, v)) {
                        value = null;
                        break;
                    }
                }
            }
            if ((value != null) && (field != null)) {
                for (int i = 0; i < field.length(); i++) {
                    try {
                        if (!field.getField(i).getDeclaringType().isInstance(value)) {
                            value = null;
                        }
                        value = Reflection.get(value, field.getField(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
            return value;
        }

        @Override
        public boolean isWritable() {
            return GraphSelectionImpl.this.isWritable(attr);
        }

        @Override
        public Property createSubProperty(Type actualType, Field f, int i) {
            return new GraphProperty(getContext(), attr, new FieldChainImpl(field, f));
        }
    }

    abstract class ForAll extends LockProtectedCommand {

        private int index = 0;

        ForAll() {
            super(states[0].getGraph(), true, JobManager.ACTION_FLAGS);
        }

        @Override
        protected void runImpl(Object arg, Context ctx) {
            GraphState gs = states[index];
            while (index < objects.length) {
                if (states[index] != gs) {
                    resource = states[index].getGraph();
                    UI.getJobManager(ctx).runLater(this, arg, ctx, JobManager.ACTION_FLAGS);
                    return;
                }
                if (objects[index] != null) {
                    runImpl(states[index], objects[index], isNode[index], arg);
                }
                index++;
            }
        }

        abstract void runImpl(GraphState gs, Object obj, boolean node, Object arg);
    }

    public GraphSelectionImpl(Context ctx, GraphState[] states, Object[] objects, boolean[] isNode) {
        super(ctx);
        this.states = states;
        this.objects = objects;
        this.isNode = isNode;
    }

    public GraphSelectionImpl(Context ctx, GraphState state, Object obj, boolean asNode) {
        super(ctx);
        states = new GraphState[] { state };
        objects = new Object[] { obj };
        isNode = new boolean[] { asNode };
    }

    public int size() {
        int n = 0;
        for (int i = objects.length - 1; i >= 0; i--) {
            if (objects[i] != null) {
                n++;
            }
        }
        return n;
    }

    private int index(int i) {
        for (int k = 0; k < objects.length; k++) {
            if ((objects[k] != null) && (--i < 0)) {
                return k;
            }
        }
        return -1;
    }

    public GraphState getGraphState(int i) {
        return states[index(i)];
    }

    public Object getObject(int i) {
        return objects[index(i)];
    }

    public boolean isNode(int i) {
        return isNode[index(i)];
    }

    public boolean contains(Graph graph, Object object, boolean asNode) {
        for (int i = objects.length - 1; i >= 0; i--) {
            if ((graph == states[i].getGraph()) && (object == objects[i]) && (asNode == isNode[i])) {
                return true;
            }
        }
        return false;
    }

    boolean isWritable(Attribute attr) {
        for (int i = objects.length - 1; i >= 0; i--) {
            if (objects[i] != null) {
                AttributeAccessor a = states[i].getGraph().getAccessor(objects[i], isNode[i], attr);
                if ((a == null) || !a.isWritable(objects[i], states[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GraphSelectionImpl)) {
            return false;
        }
        GraphSelectionImpl g = (GraphSelectionImpl) o;
        if (g.objects.length != objects.length) {
            return false;
        }
        for (int i = 0; i < objects.length; i++) {
            if ((isNode[i] != g.isNode[i]) || (objects[i] != g.objects[i])) {
                return false;
            }
        }
        return true;
    }

    public Object getDescription(String type) {
        if (size() == 1) {
            int i = 0;
            while (objects[i] == null) {
                i++;
            }
            Object o = objects[i];
            if (o instanceof Described) {
                return ((Described) o).getDescription(type);
            }
            if (Utils.isStringDescription(type)) {
                String n = (String) states[i].getGraph().getDescription(o, isNode[i], SHORT_DESCRIPTION);
                return (n == null) ? UI.getClassDescription(o.getClass()) : UI.getClassDescription(o.getClass()) + " [" + n + ']';
            } else {
                return states[i].getGraph().getDescription(o, isNode[i], type);
            }
        } else if (Utils.isStringDescription(type)) {
            return UI.I18N.msg("selection.n-objects." + NAME, size());
        } else {
            return null;
        }
    }

    @Override
    protected PropertyEditor getEditorFor(Property p, Item item) {
        if (((GraphProperty) p).attr != null) {
            AttributeItem i = AttributeItem.get(context.getWorkbench(), ((GraphProperty) p).attr);
            if (i != null) {
                PropertyEditor e = i.getEditor();
                if (e != null) {
                    return e;
                }
            }
        }
        return super.getEditorFor(p, item);
    }

    @Override
    protected UITree getHierarchySource() {
        return new RegistryAdapter(context);
    }

    @Override
    protected Object getHierarchySourceRoot(UITree source) {
        return Item.resolveItem(UI.getRegistry(context), "/attributes");
    }

    @Override
    protected PropertyEditorTree createTree() {
        return new Tree(context);
    }

    @Override
    protected Node createPropertyNodes(PropertyEditorTree t, Property p, UITree sourceTree, Object sourceNode) {
        return ((sourceNode instanceof AttributeItem) && (((GraphProperty) p).attr != null) && ((AttributeItem) sourceNode).correspondsTo(((GraphProperty) p).attr)) ? createPropertyNodes(t, p, (Item) sourceNode) : null;
    }

    @Override
    protected Node createPropertyNodesInGroup(PropertyEditorTree t, Property p, UITree sourceTree, Object sourceGroup) {
        return ((Item) sourceGroup).hasName("others") ? createPropertyNodes(t, p, null) : null;
    }

    @Override
    protected String getLabelFor(Property p) {
        return (String) ((GraphProperty) p).attr.getDescription(NAME);
    }

    @Override
    protected List getProperties(PropertyEditorTree tree) {
        ArrayList list = null;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                List l = Arrays.asList(states[i].getGraph().getAttributes(objects[i], isNode[i]));
                if (list == null) {
                    list = new ArrayList(l);
                } else {
                    for (int j = list.size() - 1; j >= 0; j--) {
                        if (!l.contains(list.get(j))) {
                            list.remove(j);
                        }
                    }
                }
            }
        }
        for (int j = list.size() - 1; j >= 0; j--) {
            Attribute a = (Attribute) list.get(j);
            if (isWritable(a)) {
                list.set(j, new GraphProperty(tree.getContext(), a, null));
            } else {
                list.remove(j);
            }
        }
        return list;
    }

    @Override
    public int getCapabilities() {
        return DELETABLE | HIERARCHICAL;
    }

    @Override
    public void delete(boolean includeChildren) {
        UI.getJobManager(context).execute(new ForAll() {

            @Override
            void runImpl(GraphState gs, Object obj, boolean node, Object arg) {
                if (node) {
                    if (obj instanceof de.grogra.graph.impl.Node) {
                        de.grogra.graph.impl.Node n = (de.grogra.graph.impl.Node) obj;
                        if (n.getGraph() != null) {
                            n.removeAll(n.getGraph().getActiveTransaction());
                        }
                    }
                }
            }
        }, null, context, JobManager.ACTION_FLAGS);
    }
}
