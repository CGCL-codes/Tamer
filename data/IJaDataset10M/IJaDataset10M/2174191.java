package persistence.beans;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Enumeration;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.EventHandler;
import java.beans.Introspector;
import persistence.PersistentArray;
import persistence.Connection;

class NullPersistenceDelegate extends PersistenceDelegate {

    public NullPersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        return null;
    }

    public void writeObject(Object oldInstance, Encoder out) {
    }
}

class PrimitivePersistenceDelegate extends PersistenceDelegate {

    public PrimitivePersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected boolean mutatesTo(Object oldInstance, Object newInstance) {
        return oldInstance.equals(newInstance);
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        return new Expression(connection, oldInstance, oldInstance.getClass(), "new", new Object[] { oldInstance.toString() });
    }
}

class ArrayPersistenceDelegate extends PersistenceDelegate {

    public ArrayPersistenceDelegate(Connection connection) {
        super(connection);
        defaultPersistenceDelegate = new DefaultPersistenceDelegate(connection);
    }

    DefaultPersistenceDelegate defaultPersistenceDelegate;

    protected boolean mutatesTo(Object oldInstance, Object newInstance) {
        return (newInstance != null && oldInstance.getClass() == newInstance.getClass() && Array.getLength(oldInstance) == Array.getLength(newInstance));
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        Class oldClass = oldInstance.getClass();
        return new Expression(connection, oldInstance, Array.class, "newInstance", new Object[] { oldClass.getComponentType(), new Integer(Array.getLength(oldInstance)) });
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        int n = Array.getLength(oldInstance);
        for (int i = 0; i < n; i++) {
            Object index = new Integer(i);
            Expression oldGetExp = new Expression(connection, oldInstance, "get", new Object[] { index });
            Expression newGetExp = new Expression(connection, newInstance, "get", new Object[] { index });
            try {
                Object oldValue = oldGetExp.getValue();
                Object newValue = newGetExp.getValue();
                out.writeExpression(oldGetExp);
                if (!MetaData.equals(newValue, out.get(oldValue))) {
                    defaultPersistenceDelegate.invokeStatement(oldInstance, "set", new Object[] { index, oldValue }, out);
                }
            } catch (Exception e) {
                out.getExceptionListener().exceptionThrown(e);
            }
        }
    }
}

class persistence_PersistentArray_PersistenceDelegate extends DefaultPersistenceDelegate {

    public persistence_PersistentArray_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected boolean mutatesTo(Object oldInstance, Object newInstance) {
        persistence.Array oldO = (persistence.Array) oldInstance;
        persistence.Array newO = newInstance == null ? null : (persistence.Array) newInstance;
        return (newO != null && oldO.typeCode() == newO.typeCode() && oldO.length() == newO.length());
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        persistence.Array oldO = (persistence.Array) oldInstance;
        return new Expression(connection, oldInstance, PersistentArray.class, "newInstance", new Object[] { new Character(oldO.typeCode()), new Integer(oldO.length()) });
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        persistence.Array oldO = (persistence.Array) oldInstance;
        persistence.Array newO = (persistence.Array) newInstance;
        int n = oldO.length();
        for (int i = 0; i < n; i++) {
            Object index = new Integer(i);
            Expression oldGetExp = new Expression(connection, oldInstance, "get", new Object[] { index });
            Expression newGetExp = new Expression(connection, newInstance, "get", new Object[] { index });
            try {
                Object oldValue = oldGetExp.getValue();
                Object newValue = newGetExp.getValue();
                out.writeExpression(oldGetExp);
                if (!MetaData.equals(newValue, out.get(oldValue))) {
                    invokeStatement(oldInstance, "set", new Object[] { index, oldValue }, out);
                }
            } catch (Exception e) {
                out.getExceptionListener().exceptionThrown(e);
            }
        }
    }
}

class ProxyPersistenceDelegate extends PersistenceDelegate {

    public ProxyPersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        Class type = oldInstance.getClass();
        java.lang.reflect.Proxy p = (java.lang.reflect.Proxy) oldInstance;
        java.lang.reflect.InvocationHandler ih = java.lang.reflect.Proxy.getInvocationHandler(p);
        if (ih instanceof EventHandler) {
            EventHandler eh = (EventHandler) ih;
            Vector args = new Vector();
            args.add(type.getInterfaces()[0]);
            args.add(eh.getTarget());
            args.add(eh.getAction());
            if (eh.getEventPropertyName() != null) {
                args.add(eh.getEventPropertyName());
            }
            if (eh.getListenerMethodName() != null) {
                args.setSize(4);
                args.add(eh.getListenerMethodName());
            }
            return new Expression(connection, oldInstance, EventHandler.class, "create", args.toArray());
        }
        return new Expression(connection, oldInstance, java.lang.reflect.Proxy.class, "newProxyInstance", new Object[] { type.getClassLoader(), type.getInterfaces(), ih });
    }
}

class java_lang_String_PersistenceDelegate extends PersistenceDelegate {

    public java_lang_String_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        return null;
    }

    public void writeObject(Object oldInstance, Encoder out) {
    }
}

class java_lang_Class_PersistenceDelegate extends PersistenceDelegate {

    public java_lang_Class_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        Class c = (Class) oldInstance;
        if (c.isPrimitive()) {
            Field field = null;
            try {
                field = ReflectionUtils.typeToClass(c).getDeclaredField("TYPE");
            } catch (NoSuchFieldException ex) {
                System.err.println("Unknown primitive type: " + c);
            }
            return new Expression(connection, oldInstance, field, "get", new Object[] { null });
        } else if (oldInstance == String.class) {
            return new Expression(connection, oldInstance, "", "getClass", new Object[] {});
        } else if (oldInstance == Class.class) {
            return new Expression(connection, oldInstance, String.class, "getClass", new Object[] {});
        } else {
            return new Expression(connection, oldInstance, Class.class, "forName", new Object[] { c.getName() });
        }
    }
}

class java_lang_reflect_Field_PersistenceDelegate extends PersistenceDelegate {

    public java_lang_reflect_Field_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        Field f = (Field) oldInstance;
        return new Expression(connection, oldInstance, f.getDeclaringClass(), "getField", new Object[] { f.getName() });
    }
}

class java_lang_reflect_Method_PersistenceDelegate extends PersistenceDelegate {

    public java_lang_reflect_Method_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        Method m = (Method) oldInstance;
        return new Expression(connection, oldInstance, m.getDeclaringClass(), "getMethod", new Object[] { m.getName(), m.getParameterTypes() });
    }
}

class java_util_Collection_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_util_Collection_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        java.util.Collection oldO = (java.util.Collection) oldInstance;
        java.util.Collection newO = (java.util.Collection) newInstance;
        if (newO.size() != 0) {
            invokeStatement(oldInstance, "clear", new Object[] {}, out);
        }
        for (Iterator i = oldO.iterator(); i.hasNext(); ) {
            invokeStatement(oldInstance, "add", new Object[] { i.next() }, out);
        }
    }
}

class java_util_List_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_util_List_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        java.util.List oldO = (java.util.List) oldInstance;
        java.util.List newO = (java.util.List) newInstance;
        int oldSize = oldO.size();
        int newSize = (newO == null) ? 0 : newO.size();
        if (oldSize < newSize) {
            invokeStatement(oldInstance, "clear", new Object[] {}, out);
            newSize = 0;
        }
        for (int i = 0; i < newSize; i++) {
            Object index = new Integer(i);
            Expression oldGetExp = new Expression(connection, oldInstance, "get", new Object[] { index });
            Expression newGetExp = new Expression(connection, newInstance, "get", new Object[] { index });
            try {
                Object oldValue = oldGetExp.getValue();
                Object newValue = newGetExp.getValue();
                out.writeExpression(oldGetExp);
                if (!MetaData.equals(newValue, out.get(oldValue))) {
                    invokeStatement(oldInstance, "set", new Object[] { index, oldValue }, out);
                }
            } catch (Exception e) {
                out.getExceptionListener().exceptionThrown(e);
            }
        }
        for (int i = newSize; i < oldSize; i++) {
            invokeStatement(oldInstance, "add", new Object[] { oldO.get(i) }, out);
        }
    }
}

class java_util_Map_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_util_Map_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        java.util.Map oldMap = (java.util.Map) oldInstance;
        java.util.Map newMap = (java.util.Map) newInstance;
        if (newMap != null) {
            java.util.Iterator newKeys = newMap.keySet().iterator();
            while (newKeys.hasNext()) {
                Object newKey = newKeys.next();
                if (!oldMap.containsKey(newKey)) {
                    invokeStatement(oldInstance, "remove", new Object[] { newKey }, out);
                }
            }
        }
        java.util.Iterator oldKeys = oldMap.keySet().iterator();
        while (oldKeys.hasNext()) {
            Object oldKey = oldKeys.next();
            Expression oldGetExp = new Expression(connection, oldInstance, "get", new Object[] { oldKey });
            Expression newGetExp = new Expression(connection, newInstance, "get", new Object[] { oldKey });
            try {
                Object oldValue = oldGetExp.getValue();
                Object newValue = newGetExp.getValue();
                out.writeExpression(oldGetExp);
                if (!MetaData.equals(newValue, out.get(oldValue))) {
                    invokeStatement(oldInstance, "put", new Object[] { oldKey, oldValue }, out);
                }
            } catch (Exception e) {
                out.getExceptionListener().exceptionThrown(e);
            }
        }
    }
}

class java_util_AbstractCollection_PersistenceDelegate extends java_util_Collection_PersistenceDelegate {

    public java_util_AbstractCollection_PersistenceDelegate(Connection connection) {
        super(connection);
    }
}

class persistence_util_AbstractCollection_PersistenceDelegate extends java_util_Collection_PersistenceDelegate {

    public persistence_util_AbstractCollection_PersistenceDelegate(Connection connection) {
        super(connection);
    }
}

class java_util_AbstractList_PersistenceDelegate extends java_util_List_PersistenceDelegate {

    public java_util_AbstractList_PersistenceDelegate(Connection connection) {
        super(connection);
    }
}

class persistence_util_AbstractList_PersistenceDelegate extends java_util_List_PersistenceDelegate {

    public persistence_util_AbstractList_PersistenceDelegate(Connection connection) {
        super(connection);
    }
}

class java_util_AbstractMap_PersistenceDelegate extends java_util_Map_PersistenceDelegate {

    public java_util_AbstractMap_PersistenceDelegate(Connection connection) {
        super(connection);
    }
}

class java_util_Hashtable_PersistenceDelegate extends java_util_Map_PersistenceDelegate {

    public java_util_Hashtable_PersistenceDelegate(Connection connection) {
        super(connection);
    }
}

class persistence_util_AbstractMap_PersistenceDelegate extends java_util_Map_PersistenceDelegate {

    public persistence_util_AbstractMap_PersistenceDelegate(Connection connection) {
        super(connection);
    }
}

class java_beans_beancontext_BeanContextSupport_PersistenceDelegate extends java_util_Collection_PersistenceDelegate {

    public java_beans_beancontext_BeanContextSupport_PersistenceDelegate(Connection connection) {
        super(connection);
    }
}

class StaticFieldsPersistenceDelegate extends PersistenceDelegate {

    public StaticFieldsPersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void installFields(Encoder out, Class cls) {
        Field fields[] = cls.getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (Object.class.isAssignableFrom(field.getType())) {
                out.writeExpression(new Expression(connection, field, "get", new Object[] { null }));
            }
        }
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        throw new RuntimeException("Unrecognized instance: " + oldInstance);
    }

    public void writeObject(Object oldInstance, Encoder out) {
        if (out.getAttribute(this) == null) {
            out.setAttribute(this, Boolean.TRUE);
            installFields(out, oldInstance.getClass());
        }
        super.writeObject(oldInstance, out);
    }
}

class java_awt_SystemColor_PersistenceDelegate extends StaticFieldsPersistenceDelegate {

    public java_awt_SystemColor_PersistenceDelegate(Connection connection) {
        super(connection);
    }
}

class java_awt_font_TextAttribute_PersistenceDelegate extends StaticFieldsPersistenceDelegate {

    public java_awt_font_TextAttribute_PersistenceDelegate(Connection connection) {
        super(connection);
    }
}

class java_awt_MenuShortcut_PersistenceDelegate extends PersistenceDelegate {

    public java_awt_MenuShortcut_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        java.awt.MenuShortcut m = (java.awt.MenuShortcut) oldInstance;
        return new Expression(connection, oldInstance, m.getClass(), "new", new Object[] { new Integer(m.getKey()), Boolean.valueOf(m.usesShiftModifier()) });
    }
}

class java_awt_Component_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_awt_Component_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        java.awt.Component c = (java.awt.Component) oldInstance;
        java.awt.Component c2 = (java.awt.Component) newInstance;
        if (!(oldInstance instanceof java.awt.Window)) {
            String[] fieldNames = new String[] { "background", "foreground", "font" };
            for (int i = 0; i < fieldNames.length; i++) {
                String name = fieldNames[i];
                Object oldValue = ReflectionUtils.getPrivateField(oldInstance, java.awt.Component.class, name, out.getExceptionListener());
                Object newValue = (newInstance == null) ? null : ReflectionUtils.getPrivateField(newInstance, java.awt.Component.class, name, out.getExceptionListener());
                if (oldValue != null && !oldValue.equals(newValue)) {
                    invokeStatement(oldInstance, "set" + NameGenerator.capitalize(name), new Object[] { oldValue }, out);
                }
            }
        }
        java.awt.Container p = c.getParent();
        if (p == null || p.getLayout() == null && !(p instanceof javax.swing.JLayeredPane)) {
            boolean locationCorrect = c.getLocation().equals(c2.getLocation());
            boolean sizeCorrect = c.getSize().equals(c2.getSize());
            if (!locationCorrect && !sizeCorrect) {
                invokeStatement(oldInstance, "setBounds", new Object[] { c.getBounds() }, out);
            } else if (!locationCorrect) {
                invokeStatement(oldInstance, "setLocation", new Object[] { c.getLocation() }, out);
            } else if (!sizeCorrect) {
                invokeStatement(oldInstance, "setSize", new Object[] { c.getSize() }, out);
            }
        }
    }
}

class java_awt_Container_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_awt_Container_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        if (oldInstance instanceof javax.swing.JScrollPane) {
            return;
        }
        java.awt.Container oldC = (java.awt.Container) oldInstance;
        java.awt.Component[] oldChildren = oldC.getComponents();
        java.awt.Container newC = (java.awt.Container) newInstance;
        java.awt.Component[] newChildren = (newC == null) ? new java.awt.Component[0] : newC.getComponents();
        for (int i = newChildren.length; i < oldChildren.length; i++) {
            invokeStatement(oldInstance, "add", new Object[] { oldChildren[i] }, out);
        }
    }
}

class java_awt_Choice_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_awt_Choice_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        java.awt.Choice m = (java.awt.Choice) oldInstance;
        java.awt.Choice n = (java.awt.Choice) newInstance;
        for (int i = n.getItemCount(); i < m.getItemCount(); i++) {
            invokeStatement(oldInstance, "add", new Object[] { m.getItem(i) }, out);
        }
    }
}

class java_awt_Menu_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_awt_Menu_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        java.awt.Menu m = (java.awt.Menu) oldInstance;
        java.awt.Menu n = (java.awt.Menu) newInstance;
        for (int i = n.getItemCount(); i < m.getItemCount(); i++) {
            invokeStatement(oldInstance, "add", new Object[] { m.getItem(i) }, out);
        }
    }
}

class java_awt_MenuBar_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_awt_MenuBar_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        java.awt.MenuBar m = (java.awt.MenuBar) oldInstance;
        java.awt.MenuBar n = (java.awt.MenuBar) newInstance;
        for (int i = n.getMenuCount(); i < m.getMenuCount(); i++) {
            invokeStatement(oldInstance, "add", new Object[] { m.getMenu(i) }, out);
        }
    }
}

class java_awt_List_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_awt_List_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        java.awt.List m = (java.awt.List) oldInstance;
        java.awt.List n = (java.awt.List) newInstance;
        for (int i = n.getItemCount(); i < m.getItemCount(); i++) {
            invokeStatement(oldInstance, "add", new Object[] { m.getItem(i) }, out);
        }
    }
}

class java_awt_BorderLayout_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_awt_BorderLayout_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        String[] locations = { "north", "south", "east", "west", "center" };
        String[] names = { java.awt.BorderLayout.NORTH, java.awt.BorderLayout.SOUTH, java.awt.BorderLayout.EAST, java.awt.BorderLayout.WEST, java.awt.BorderLayout.CENTER };
        for (int i = 0; i < locations.length; i++) {
            Object oldC = ReflectionUtils.getPrivateField(oldInstance, java.awt.BorderLayout.class, locations[i], out.getExceptionListener());
            Object newC = ReflectionUtils.getPrivateField(newInstance, java.awt.BorderLayout.class, locations[i], out.getExceptionListener());
            if (oldC != null && newC == null) {
                invokeStatement(oldInstance, "addLayoutComponent", new Object[] { oldC, names[i] }, out);
            }
        }
    }
}

class java_awt_CardLayout_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_awt_CardLayout_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        Hashtable tab = (Hashtable) ReflectionUtils.getPrivateField(oldInstance, java.awt.CardLayout.class, "tab", out.getExceptionListener());
        if (tab != null) {
            for (Enumeration e = tab.keys(); e.hasMoreElements(); ) {
                Object child = e.nextElement();
                invokeStatement(oldInstance, "addLayoutComponent", new Object[] { child, (String) tab.get(child) }, out);
            }
        }
    }
}

class java_awt_GridBagLayout_PersistenceDelegate extends DefaultPersistenceDelegate {

    public java_awt_GridBagLayout_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        Hashtable comptable = (Hashtable) ReflectionUtils.getPrivateField(oldInstance, java.awt.GridBagLayout.class, "comptable", out.getExceptionListener());
        if (comptable != null) {
            for (Enumeration e = comptable.keys(); e.hasMoreElements(); ) {
                Object child = e.nextElement();
                invokeStatement(oldInstance, "addLayoutComponent", new Object[] { child, comptable.get(child) }, out);
            }
        }
    }
}

class javax_swing_JFrame_PersistenceDelegate extends DefaultPersistenceDelegate {

    public javax_swing_JFrame_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        java.awt.Window oldC = (java.awt.Window) oldInstance;
        java.awt.Window newC = (java.awt.Window) newInstance;
        boolean oldV = oldC.isVisible();
        boolean newV = newC.isVisible();
        if (newV != oldV) {
            boolean executeStatements = out.executeStatements;
            out.executeStatements = false;
            invokeStatement(oldInstance, "setVisible", new Object[] { Boolean.valueOf(oldV) }, out);
            out.executeStatements = executeStatements;
        }
    }
}

class javax_swing_DefaultListModel_PersistenceDelegate extends DefaultPersistenceDelegate {

    public javax_swing_DefaultListModel_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        javax.swing.DefaultListModel m = (javax.swing.DefaultListModel) oldInstance;
        javax.swing.DefaultListModel n = (javax.swing.DefaultListModel) newInstance;
        for (int i = n.getSize(); i < m.getSize(); i++) {
            invokeStatement(oldInstance, "add", new Object[] { m.getElementAt(i) }, out);
        }
    }
}

class javax_swing_DefaultComboBoxModel_PersistenceDelegate extends DefaultPersistenceDelegate {

    public javax_swing_DefaultComboBoxModel_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        javax.swing.DefaultComboBoxModel m = (javax.swing.DefaultComboBoxModel) oldInstance;
        for (int i = 0; i < m.getSize(); i++) {
            invokeStatement(oldInstance, "addElement", new Object[] { m.getElementAt(i) }, out);
        }
    }
}

class javax_swing_tree_DefaultMutableTreeNode_PersistenceDelegate extends DefaultPersistenceDelegate {

    public javax_swing_tree_DefaultMutableTreeNode_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        javax.swing.tree.DefaultMutableTreeNode m = (javax.swing.tree.DefaultMutableTreeNode) oldInstance;
        javax.swing.tree.DefaultMutableTreeNode n = (javax.swing.tree.DefaultMutableTreeNode) newInstance;
        for (int i = n.getChildCount(); i < m.getChildCount(); i++) {
            invokeStatement(oldInstance, "add", new Object[] { m.getChildAt(i) }, out);
        }
    }
}

class javax_swing_ToolTipManager_PersistenceDelegate extends PersistenceDelegate {

    public javax_swing_ToolTipManager_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        return new Expression(connection, oldInstance, javax.swing.ToolTipManager.class, "sharedInstance", new Object[] {});
    }
}

class javax_swing_JComponent_PersistenceDelegate extends DefaultPersistenceDelegate {

    public javax_swing_JComponent_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        int statementCount = 0;
        javax.swing.JComponent c = (javax.swing.JComponent) oldInstance;
        String[] fieldNames = new String[] { "minimumSize", "preferredSize", "maximumSize" };
        for (int i = 0; i < fieldNames.length; i++) {
            String name = fieldNames[i];
            Object value = ReflectionUtils.getPrivateField(c, javax.swing.JComponent.class, name, out.getExceptionListener());
            if (value != null) {
                invokeStatement(oldInstance, "set" + NameGenerator.capitalize(name), new Object[] { value }, out);
            }
        }
    }
}

class javax_swing_JTabbedPane_PersistenceDelegate extends DefaultPersistenceDelegate {

    public javax_swing_JTabbedPane_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        javax.swing.JTabbedPane p = (javax.swing.JTabbedPane) oldInstance;
        for (int i = 0; i < p.getTabCount(); i++) {
            invokeStatement(oldInstance, "addTab", new Object[] { p.getTitleAt(i), p.getIconAt(i), p.getComponentAt(i) }, out);
        }
    }
}

class javax_swing_JMenu_PersistenceDelegate extends DefaultPersistenceDelegate {

    public javax_swing_JMenu_PersistenceDelegate(Connection connection) {
        super(connection);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        javax.swing.JMenu m = (javax.swing.JMenu) oldInstance;
        java.awt.Component[] c = m.getMenuComponents();
        for (int i = 0; i < c.length; i++) {
            invokeStatement(oldInstance, "add", new Object[] { c[i] }, out);
        }
    }
}

class MetaData {

    Connection connection;

    private Hashtable internalPersistenceDelegates = new Hashtable();

    private Hashtable transientProperties = new Hashtable();

    private PersistenceDelegate nullPersistenceDelegate;

    private PersistenceDelegate primitivePersistenceDelegate;

    private PersistenceDelegate defaultPersistenceDelegate;

    private PersistenceDelegate arrayPersistenceDelegate;

    private PersistenceDelegate proxyPersistenceDelegate;

    MetaData(Connection connection) {
        this.connection = connection;
        nullPersistenceDelegate = new NullPersistenceDelegate(connection);
        primitivePersistenceDelegate = new PrimitivePersistenceDelegate(connection);
        defaultPersistenceDelegate = new DefaultPersistenceDelegate(connection);
        registerConstructor("java.util.Date", new String[] { "time" });
        registerConstructor("java.beans.Statement", new String[] { "target", "methodName", "arguments" });
        registerConstructor("java.beans.Expression", new String[] { "target", "methodName", "arguments" });
        registerConstructor("java.beans.EventHandler", new String[] { "target", "action", "eventPropertyName", "listenerMethodName" });
        registerConstructor("java.awt.Point", new String[] { "x", "y" });
        registerConstructor("java.awt.Dimension", new String[] { "width", "height" });
        registerConstructor("java.awt.Rectangle", new String[] { "x", "y", "width", "height" });
        registerConstructor("java.awt.Insets", new String[] { "top", "left", "bottom", "right" });
        registerConstructor("java.awt.Color", new String[] { "red", "green", "blue", "alpha" });
        registerConstructor("java.awt.Font", new String[] { "name", "style", "size" });
        registerConstructor("java.awt.Cursor", new String[] { "type" });
        registerConstructor("java.awt.GridBagConstraints", new String[] { "gridx", "gridy", "gridwidth", "gridheight", "weightx", "weighty", "anchor", "fill", "insets", "ipadx", "ipady" });
        registerConstructor("java.awt.ScrollPane", new String[] { "scrollbarDisplayPolicy" });
        registerConstructor("javax.swing.plaf.FontUIResource", new String[] { "name", "style", "size" });
        registerConstructor("javax.swing.plaf.ColorUIResource", new String[] { "red", "green", "blue" });
        registerConstructor("javax.swing.tree.TreePath", new String[] { "path" });
        registerConstructor("javax.swing.OverlayLayout", new String[] { "target" });
        registerConstructor("javax.swing.BoxLayout", new String[] { "target", "axis" });
        registerConstructor("javax.swing.DefaultCellEditor", new String[] { "component" });
        registerConstructor("javax.swing.JSplitPane", new String[] { "orientation" });
        registerConstructor("javax.swing.ImageIcon", new String[] { "description" });
        registerConstructor("javax.swing.JButton", new String[] { "label" });
        registerConstructor("javax.swing.border.BevelBorder", new String[] { "bevelType", "highlightOuter", "highlightInner", "shadowOuter", "shadowInner" });
        registerConstructor("javax.swing.plaf.BorderUIResource$BevelBorderUIResource", new String[] { "bevelType", "highlightOuter", "highlightInner", "shadowOuter", "shadowInner" });
        registerConstructor("javax.swing.border.CompoundBorder", new String[] { "outsideBorder", "insideBorder" });
        registerConstructor("javax.swing.plaf.BorderUIResource$CompoundBorderUIResource", new String[] { "outsideBorder", "insideBorder" });
        registerConstructor("javax.swing.border.EmptyBorder", new String[] { "top", "left", "bottom", "right" });
        registerConstructor("javax.swing.plaf.BorderUIResource$EmptyBorderUIResource", new String[] { "top", "left", "bottom", "right" });
        registerConstructor("javax.swing.border.EtchedBorder", new String[] { "etchType", "highlight", "shadow" });
        registerConstructor("javax.swing.plaf.BorderUIResource$EtchedBorderUIResource", new String[] { "etchType", "highlight", "shadow" });
        registerConstructor("javax.swing.border.LineBorder", new String[] { "lineColor", "thickness" });
        registerConstructor("javax.swing.plaf.BorderUIResource$LineBorderUIResource", new String[] { "lineColor", "thickness" });
        registerConstructor("javax.swing.border.MatteBorder", new String[] { "top", "left", "bottom", "right", "tileIcon" });
        registerConstructor("javax.swing.plaf.BorderUIResource$MatteBorderUIResource", new String[] { "top", "left", "bottom", "right", "tileIcon" });
        registerConstructor("javax.swing.border.SoftBevelBorder", new String[] { "bevelType", "highlightOuter", "highlightInner", "shadowOuter", "shadowInner" });
        registerConstructor("javax.swing.border.TitledBorder", new String[] { "border", "title", "titleJustification", "titlePosition", "titleFont", "titleColor" });
        registerConstructor("javax.swing.plaf.BorderUIResource$TitledBorderUIResource", new String[] { "border", "title", "titleJustification", "titlePosition", "titleFont", "titleColor" });
        removeProperty("java.awt.geom.RectangularShape", "frame");
        removeProperty("java.awt.Rectangle", "bounds");
        removeProperty("java.awt.Dimension", "size");
        removeProperty("java.awt.Point", "location");
        removeProperty("java.awt.Component", "foreground");
        removeProperty("java.awt.Component", "background");
        removeProperty("java.awt.Component", "font");
        removeProperty("java.awt.Component", "visible");
        removeProperty("java.awt.ScrollPane", "scrollPosition");
        removeProperty("javax.swing.JComponent", "minimumSize");
        removeProperty("javax.swing.JComponent", "preferredSize");
        removeProperty("javax.swing.JComponent", "maximumSize");
        removeProperty("javax.swing.ImageIcon", "image");
        removeProperty("javax.swing.ImageIcon", "imageObserver");
        removeProperty("javax.swing.JMenu", "accelerator");
        removeProperty("javax.swing.JMenuItem", "accelerator");
        removeProperty("javax.swing.JMenuBar", "helpMenu");
        removeProperty("javax.swing.JScrollPane", "verticalScrollBar");
        removeProperty("javax.swing.JScrollPane", "horizontalScrollBar");
        removeProperty("javax.swing.JScrollPane", "rowHeader");
        removeProperty("javax.swing.JScrollPane", "columnHeader");
        removeProperty("javax.swing.JViewport", "extentSize");
        removeProperty("javax.swing.table.JTableHeader", "defaultRenderer");
        removeProperty("javax.swing.JList", "cellRenderer");
        removeProperty("javax.swing.JList", "selectedIndices");
        removeProperty("javax.swing.DefaultListSelectionModel", "leadSelectionIndex");
        removeProperty("javax.swing.DefaultListSelectionModel", "anchorSelectionIndex");
        removeProperty("javax.swing.JComboBox", "selectedIndex");
        removeProperty("javax.swing.JTabbedPane", "selectedIndex");
        removeProperty("javax.swing.JTabbedPane", "selectedComponent");
        removeProperty("javax.swing.AbstractButton", "disabledIcon");
        removeProperty("javax.swing.JLabel", "disabledIcon");
        removeProperty("javax.swing.text.JTextComponent", "caret");
        removeProperty("javax.swing.text.JTextComponent", "caretPosition");
        removeProperty("javax.swing.text.JTextComponent", "selectionStart");
        removeProperty("javax.swing.text.JTextComponent", "selectionEnd");
    }

    static boolean equals(Object o1, Object o2) {
        return (o1 == null) ? (o2 == null) : o1.equals(o2);
    }

    public static synchronized void setPersistenceDelegate(Class type, PersistenceDelegate persistenceDelegate) {
        setBeanAttribute(type, "persistenceDelegate", persistenceDelegate);
    }

    public synchronized PersistenceDelegate getPersistenceDelegate(Class type) {
        if (type == null) {
            return nullPersistenceDelegate;
        }
        if (ReflectionUtils.isPrimitive(type)) {
            return primitivePersistenceDelegate;
        }
        if (type.isArray()) {
            if (arrayPersistenceDelegate == null) {
                arrayPersistenceDelegate = new ArrayPersistenceDelegate(connection);
            }
            return arrayPersistenceDelegate;
        }
        try {
            if (java.lang.reflect.Proxy.isProxyClass(type)) {
                if (proxyPersistenceDelegate == null) {
                    proxyPersistenceDelegate = new ProxyPersistenceDelegate(connection);
                }
                return proxyPersistenceDelegate;
            }
        } catch (Exception e) {
        }
        String typeName = type.getName();
        if (getBeanAttribute(type, "transient_init") == null) {
            Vector tp = (Vector) transientProperties.get(typeName);
            if (tp != null) {
                for (int i = 0; i < tp.size(); i++) {
                    setPropertyAttribute(type, (String) tp.get(i), "transient", Boolean.TRUE);
                }
            }
            setBeanAttribute(type, "transient_init", Boolean.TRUE);
        }
        PersistenceDelegate pd = (PersistenceDelegate) getBeanAttribute(type, "persistenceDelegate");
        if (pd == null) {
            pd = (PersistenceDelegate) internalPersistenceDelegates.get(typeName);
            if (pd != null) {
                return pd;
            }
            internalPersistenceDelegates.put(typeName, defaultPersistenceDelegate);
            try {
                String name = type.getName();
                Class c = Class.forName("persistence.beans." + name.replace('.', '_') + "_PersistenceDelegate");
                pd = (PersistenceDelegate) c.getConstructor(new Class[] { Connection.class }).newInstance(new Object[] { connection });
                internalPersistenceDelegates.put(typeName, pd);
            } catch (ClassNotFoundException e) {
            } catch (Exception e) {
                System.err.println("Internal error: " + e);
            }
        }
        return (pd != null) ? pd : defaultPersistenceDelegate;
    }

    public static BeanInfo getBeanInfo(Class type) {
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(type);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return info;
    }

    private static PropertyDescriptor getPropertyDescriptor(Class type, String propertyName) {
        BeanInfo info = getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor pd = propertyDescriptors[i];
            if (propertyName.equals(pd.getName())) {
                return pd;
            }
        }
        return null;
    }

    private static void setPropertyAttribute(Class type, String property, String attribute, Object value) {
        PropertyDescriptor pd = getPropertyDescriptor(type, property);
        if (pd == null) {
            System.err.println("Warning: property " + property + " is not defined on " + type);
            return;
        }
        pd.setValue(attribute, value);
    }

    private static void setBeanAttribute(Class type, String attribute, Object value) {
        getBeanInfo(type).getBeanDescriptor().setValue(attribute, value);
    }

    private static Object getBeanAttribute(Class type, String attribute) {
        return getBeanInfo(type).getBeanDescriptor().getValue(attribute);
    }

    private synchronized void registerConstructor(String typeName, String[] constructor) {
        internalPersistenceDelegates.put(typeName, new DefaultPersistenceDelegate(connection, constructor));
    }

    private void removeProperty(String typeName, String property) {
        Vector tp = (Vector) transientProperties.get(typeName);
        if (tp == null) {
            tp = new Vector();
            transientProperties.put(typeName, tp);
        }
        tp.add(property);
    }
}
