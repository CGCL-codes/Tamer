package net.sourceforge.javautil.gui.swing.css;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.el.Expression;
import javax.el.ValueExpression;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.text.JTextComponent;
import net.sourceforge.javautil.common.ReflectionUtil;
import net.sourceforge.javautil.common.StringUtil;
import net.sourceforge.javautil.common.reflection.cache.ClassCache;
import net.sourceforge.javautil.common.reflection.cache.ClassDescriptor;
import net.sourceforge.javautil.common.reflection.cache.ClassField;
import net.sourceforge.javautil.common.reflection.cache.ClassMethod;
import net.sourceforge.javautil.css.IStyleApplicator;
import net.sourceforge.javautil.css.StyleApplicatorAbstract;
import net.sourceforge.javautil.css.StylePercentage;
import net.sourceforge.javautil.css.IStyleSheet;
import net.sourceforge.javautil.css.StyleState;
import net.sourceforge.javautil.css.Styles;
import net.sourceforge.javautil.gui.swing.SwingGUIContext;
import net.sourceforge.javautil.gui.swing.binding.BindingExpression;
import net.sourceforge.javautil.gui.swing.dsl.SwingDSL;

/**
 * This will allow application of styles to {@link Component}'s, allowing special 'style' and 'class' CSS attributes
 * on {@link JComponent}'s.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class SwingCSSStyleApplicator extends StyleApplicatorAbstract<Component> {

    protected ClassDescriptor borderFactory = ClassCache.getFor(BorderFactory.class);

    public SwingCSSStyleApplicator() {
        Map<String, List<ClassField>> fields = ClassCache.getFor(Color.class).getFields();
        for (String name : fields.keySet()) {
            for (ClassField field : fields.get(name)) {
                if (field.isStatic() && Color.class.isAssignableFrom(field.getFieldType())) {
                    this.colors.put(field.getName().toLowerCase(), field.getValue(null));
                }
            }
        }
        this.aliases.put("color", "foreground");
        this.aliases.put("background-color", "background");
        this.aliases.put("padding", "border");
        this.aliases.put("size", "preferredSize");
        this.aliases.put("position", "location");
        this.defineComposite("font", "font-family", "font-style", "font-size", "font-weight");
        this.defineComposite("size", "width", "height");
        this.defineComposite("padding", "padding-left", "padding-right", "padding-bottom", "padding-top");
        this.defineComposite("border", "border-left", "border-right", "border-bottom", "border-top").link("padding");
        this.defineComposite("margin", "margin-left", "margin-right", "margin-bottom", "margin-top");
        this.defineComposite("position", "top", "left");
    }

    @Override
    public boolean applyStyles(IStyleSheet sheet, Component target, boolean recursively) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(SwingGUIContext.instance().getUserInterface().getGuiClassLoader());
            return super.applyStyles(sheet, target, recursively);
        } finally {
            Thread.currentThread().setContextClassLoader(loader);
        }
    }

    @Override
    protected boolean applyStylesInternal(IStyleSheet sheet, Component target, boolean recursively) {
        boolean applied = super.applyStylesInternal(sheet, target, recursively);
        if (applied && target instanceof Container) {
            if (((Container) target).getComponentCount() > 0) target.invalidate();
        }
        return applied;
    }

    @Override
    public Object execute(String function, Object... arguments) {
        ClassMethod method = borderFactory.findMethod("create" + StringUtil.capitalize(function), arguments);
        if (method != null) {
            return method.invoke(null, arguments);
        } else {
            return super.execute(function, arguments);
        }
    }

    public SwingCSSState getState(Component target) {
        if (target instanceof JComponent) {
            SwingCSSState state = (SwingCSSState) ((JComponent) target).getClientProperty("css.state");
            if (state == null) ((JComponent) target).putClientProperty("css.state", state = new SwingCSSState((JComponent) target));
            return state;
        }
        return null;
    }

    @Override
    protected void clearStyles(Component target) {
        if (target instanceof JComponent) {
            this.getState(target).restoreOriginals();
        }
        target.setPreferredSize(null);
    }

    @Override
    protected void saveState(Component target, Styles styles, Set<String> appliedComposites) {
        if (target instanceof JComponent) this.getState(target).setApplied(styles, appliedComposites);
    }

    @Override
    protected Object createComposite(Composite composite, boolean linked, Component target, Object... values) {
        SwingCSSState state = target instanceof JComponent ? this.getState(target) : null;
        if ("font".equals(composite.getName())) {
            Font tf = state.getDefault("font", Font.class);
            String defaultStyle = tf != null && tf.isPlain() ? "Plain" : null;
            if (tf != null && defaultStyle == null) {
                if (tf.isBold() && tf.isItalic()) defaultStyle = "bolditalic"; else if (tf.isBold()) defaultStyle = "bold"; else if (tf.isItalic()) defaultStyle = "italic";
            } else defaultStyle = "Plain";
            String name = values[0] == null ? tf != null ? tf.getFamily() : "Tahoma" : (String) values[0];
            if (values[3] != null) values[1] = values[3];
            String style = values[1] == null ? defaultStyle : (String) values[1];
            int size = ReflectionUtil.coerce(int.class, values[2] == null ? tf != null ? tf.getSize() : 12 : values[2]);
            return Font.decode(name + "-" + style.toUpperCase() + "-" + size);
        } else if ("margin".equals(composite.getName())) {
            int left = ReflectionUtil.coerce(int.class, values[0] == null ? 0 : values[0]);
            int right = ReflectionUtil.coerce(int.class, values[1] == null ? 0 : values[1]);
            int bottom = ReflectionUtil.coerce(int.class, values[2] == null ? 0 : values[2]);
            int top = ReflectionUtil.coerce(int.class, values[3] == null ? 0 : values[3]);
            return new Insets(top, left, bottom, right);
        } else if ("size".equals(composite.getName())) {
            Dimension size = target instanceof Container && ((Container) target).getLayout() instanceof SwingCSSLayout ? target.getSize() : target.getPreferredSize();
            int width = 0;
            int height = 0;
            float wp = -1;
            float hp = -1;
            if (values[0] instanceof StylePercentage) {
                wp = ((StylePercentage) values[0]).getPercentage();
            } else {
                width = ReflectionUtil.coerce(int.class, values[0] == null ? size.getWidth() : values[0]);
            }
            if (values[1] instanceof StylePercentage) {
                hp = ((StylePercentage) values[1]).getPercentage();
            } else {
                height = ReflectionUtil.coerce(int.class, values[1] == null ? size.getHeight() : values[1]);
            }
            if (wp != -1 || hp != -1) {
                SwingCSSDynamicSize ds = null;
                for (ComponentListener cl : target.getParent().getComponentListeners()) {
                    if (cl instanceof SwingCSSDynamicSize) {
                        ds = (SwingCSSDynamicSize) cl;
                        break;
                    }
                }
                if (ds == null) target.getParent().addComponentListener(ds = new SwingCSSDynamicSize());
                state.setDynamicSize(ds);
                SwingCSSDynamicSize.setDimensions(ds.addComponent(target, wp, hp), target.getParent());
                size = target.getSize();
                if (wp == -1 && width != 0) size = new Dimension(width, (int) size.getHeight());
                if (hp == -1 && height != 0) size = new Dimension((int) size.getWidth(), height);
                return size;
            } else {
                return new Dimension((int) width, (int) height);
            }
        } else if ("position".equals(composite.getName())) {
            if (state != null) {
                state.setAbsoluteLocation(true);
            }
            int top = ReflectionUtil.coerce(int.class, values[0] == null ? 0 : values[0]);
            int left = ReflectionUtil.coerce(int.class, values[1] == null ? 0 : values[1]);
            return new Point(left, top);
        } else if ("padding".equals(composite.getName())) {
            if (target instanceof JComponent) {
                JComponent cmp = (JComponent) target;
                Border border = linked ? cmp.getBorder() : null;
                int left = ReflectionUtil.coerce(int.class, values[0] == null ? 0 : values[0]);
                int right = ReflectionUtil.coerce(int.class, values[1] == null ? 0 : values[1]);
                int bottom = ReflectionUtil.coerce(int.class, values[2] == null ? 0 : values[2]);
                int top = ReflectionUtil.coerce(int.class, values[3] == null ? 0 : values[3]);
                Border spacing = top > 0 || left > 0 || bottom > 0 || right > 0 ? BorderFactory.createEmptyBorder(top, left, bottom, right) : null;
                if (border != null) return spacing == null ? border : BorderFactory.createCompoundBorder(border, spacing); else return spacing;
            }
        } else if ("border".equals(composite.getName())) {
            if (target instanceof JComponent) {
                if (values[0] instanceof Border) {
                    return values[0];
                } else {
                    Border border = null;
                    for (int v = 0; v < 4; v++) {
                        if (values[v] == null) continue;
                        Object[] vals = values[v] instanceof String ? new Object[] { values[v], 1 } : (Object[]) values[v];
                        if ("none".equals(vals[0])) continue;
                        Color color = ReflectionUtil.coerce(Color.class, vals[0]);
                        int thickness = ReflectionUtil.coerce(int.class, vals[1] == null ? 1 : vals[1]);
                        MatteBorder mb = BorderFactory.createMatteBorder(v == 3 ? thickness : 0, v == 0 ? thickness : 0, v == 2 ? thickness : 0, v == 1 ? thickness : 0, color);
                        if (border == null) {
                            border = mb;
                        } else {
                            border = BorderFactory.createCompoundBorder(mb, border);
                        }
                    }
                    return border;
                }
            }
        }
        throw new UnsupportedOperationException("No such composite: " + composite.getName());
    }

    @Override
    protected void setInvalidProperty(Component target, String name, Object value) {
        if (target instanceof JComponent) {
            ((JComponent) target).putClientProperty("net.sf.javautil.properties." + name, value);
        }
    }

    public String getId(Component target) {
        return target.getName();
    }

    public Component getParent(Component target) {
        if (target instanceof JPopupMenu) {
            return ((JPopupMenu) target).getInvoker();
        }
        return target.getParent();
    }

    public int getPosition(Component target) {
        if (target.getParent() != null) {
            for (int c = 0; c < target.getParent().getComponentCount(); c++) {
                if (target.getParent().getComponent(c) == target) return c;
            }
        }
        return 0;
    }

    public String getStyle(Component target) {
        if (target instanceof JComponent) {
            return (String) ((JComponent) target).getClientProperty("css.style");
        }
        return null;
    }

    public int getTotalChildren(Component target) {
        if (target instanceof Container) {
            int extra = 0;
            if (target instanceof JFrame) {
                if (((JFrame) target).getJMenuBar() != null) extra++;
            }
            if (target instanceof RootPaneContainer) target = ((RootPaneContainer) target).getContentPane();
            return ((Container) target).getComponentCount() + extra;
        }
        return 0;
    }

    public Component getChild(Component parent, int idx) {
        int offset = parent instanceof JFrame && ((JFrame) parent).getJMenuBar() != null ? 1 : 0;
        if (idx == 0 && offset != 0) {
            return ((JFrame) parent).getJMenuBar();
        }
        if (parent instanceof RootPaneContainer) parent = ((RootPaneContainer) parent).getContentPane();
        return ((Container) parent).getComponent(idx - offset);
    }

    public Class<? extends Component> getType(String typeName) {
        for (String pkg : new String[] { "javax.swing.", "javax.swing.text.", "javax.swing.tree.", "javax.swing.filechooser.", "javax.swing.colorchooser.", "javax.awt." }) {
            try {
                return (Class<? extends Component>) Class.forName(pkg + typeName);
            } catch (ClassNotFoundException e) {
            }
        }
        return null;
    }

    public String getType(Component target) {
        return target.getClass().getSimpleName();
    }

    public boolean hasClass(Component target, String className) {
        if (target instanceof JComponent) {
            String[] cssClass = (String[]) ((JComponent) target).getClientProperty("css.class");
            if (cssClass != null) for (String cc : cssClass) {
                if (cc.equals(className)) return true;
            }
        }
        return false;
    }

    public Set<String> getPsuedoClasses(Component target) {
        if (target instanceof JComponent) {
            Set<String> classes = (Set<String>) ((JComponent) target).getClientProperty("css.psuedo");
            if (classes == null) ((JComponent) target).putClientProperty("css.psuedo", classes = new LinkedHashSet<String>());
            return classes;
        }
        return Collections.EMPTY_SET;
    }

    @Override
    protected int calculatePercentage(Component target, String name, int percentage) {
        return super.calculatePercentage(target, name, percentage);
    }

    protected final ClassDescriptor<SwingConstants> scdesc = ClassCache.getFor(SwingConstants.class);

    @Override
    protected <T> T coerceValue(Class<T> type, Object value) {
        if (type == int.class && !(value instanceof Integer)) {
            ClassField constant = scdesc.getField(ReflectionUtil.coerce(String.class, value).toUpperCase());
            if (constant != null && constant.isStatic()) {
                return (T) constant.getValue(null);
            }
        }
        return ReflectionUtil.coerce(type, value);
    }

    public Color color(int r, int g, int b) {
        return this.translateColor(r, g, b);
    }

    @Override
    protected Color translateColor(int r, int g, int b) {
        return new Color(r, g, b);
    }
}
