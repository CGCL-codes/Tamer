package jaxil.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jaxil.Connector;
import jaxil.LateBoxer;
import jaxil.annotation.Boxer;
import jaxil.annotation.End;
import jaxil.annotation.Inject;
import jaxil.annotation.Singleton;
import jaxil.annotation.Type;
import jaxil.impl.box.ArrayCollectionBean;
import jaxil.impl.box.EnumLateBoxer;
import jaxil.var.JActionUpdater;
import jaxil.var.JUpdater;
import jaxil.var.Updater;
import jaxil.var.Var;
import jaxil.var.VarUpdater;

public class JConnector implements Connector, Serializable {

    private static final long serialVersionUID = 4024851197563879174L;

    private final Class<?> c;

    private Object ref;

    private List<Entry<CollectionBean>> collectionbeans = new ArrayList<Entry<CollectionBean>>();

    private JInjector[] injectors;

    private static String GET = "get";

    private static String SET = "set";

    private static String IS = "is";

    private HashMap<Class<?>, LateBoxer> box;

    private HashMap<Class<?>, Class<?>> collectionbox;

    private Method end = null;

    private boolean singleton;

    private Object instance = null;

    public JConnector(Class<?> c, HashMap<Class<?>, LateBoxer> box, HashMap<Class<?>, Class<?>> collectionbox) {
        this.c = c;
        singleton = c.getAnnotation(Singleton.class) != null;
        this.box = box;
        this.collectionbox = collectionbox;
        int i;
        Field[] fields = c.getDeclaredFields();
        ArrayList<JInjector> injectors = new ArrayList<JInjector>();
        String name;
        Method injectMethod;
        for (i = 0; i < fields.length; i++) {
            if (fields[i].getAnnotation(Inject.class) != null) {
                name = Character.toUpperCase(fields[i].getName().charAt(0)) + fields[i].getName().substring(1);
                injectMethod = getSetMethod(name, new Object[] {});
                if (injectMethod == null) System.err.println("JConnector : cannot create injection method in " + c.getName() + " at " + name); else injectors.add(new JInjector(injectMethod));
            }
        }
        this.injectors = new JInjector[injectors.size()];
        if (injectors.size() > 0) injectors.toArray(this.injectors);
        Method[] m = c.getMethods();
        for (i = 0; i < m.length; i++) {
            if (m[i].getAnnotation(End.class) != null) {
                end = m[i];
                break;
            }
        }
    }

    private boolean updateBean(String bean) {
        int i;
        String rbean = realbeanName(bean);
        String isbean = IS + rbean;
        String getbean = GET + rbean;
        boolean changed = false;
        Method[] m = c.getMethods();
        Type type;
        CollectionBean collection;
        for (i = 0; i < m.length; i++) {
            if (m[i].getName().equals(getbean) || m[i].getName().equals(isbean)) {
                if (m[i].getParameterTypes().length == 0) {
                    changed = true;
                    String returnClassName = m[i].getReturnType().getCanonicalName();
                    try {
                        if (collectionbox.containsKey(m[i].getReturnType())) {
                            type = m[i].getAnnotation(Type.class);
                            collection = (CollectionBean) collectionbox.get(m[i].getReturnType()).newInstance();
                            collection.setBean(rbean);
                            if (type != null) {
                                if (box.containsKey(type.value())) collection.setBox(box.get(type.value())); else {
                                    Boxer boxer = m[i].getAnnotation(Boxer.class);
                                    if (boxer != null) {
                                        LateBoxer lateboxer = boxer.value().newInstance();
                                        collection.setBox(lateboxer);
                                        box.put(type.value(), lateboxer);
                                    }
                                }
                            }
                            collectionbeans.add(new Entry<CollectionBean>(rbean, collection));
                        } else if (returnClassName.endsWith("[]")) {
                            Class<?> simpletype = Class.forName(returnClassName.substring(0, returnClassName.length() - 2));
                            collection = new ArrayCollectionBean();
                            collection.setBean(rbean);
                            ((ArrayCollectionBean) collection).setItemClass(simpletype);
                            if (box.containsKey(simpletype)) collection.setBox(box.get(simpletype)); else {
                                Boxer boxer = m[i].getAnnotation(Boxer.class);
                                if (boxer != null) {
                                    LateBoxer lateboxer = boxer.value().newInstance();
                                    collection.setBox(lateboxer);
                                    box.put(simpletype, lateboxer);
                                }
                            }
                            collectionbeans.add(new Entry<CollectionBean>(rbean, collection));
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return changed;
    }

    @Override
    public Object create() throws InstantiationException, IllegalAccessException {
        Object result = null;
        result = c.newInstance();
        for (int i = 0; i < injectors.length; i++) injectors[i].inject(result);
        return result;
    }

    @Override
    public Object get(String bean) {
        return get(ref, bean);
    }

    @Override
    public String getClassName() {
        return c.getCanonicalName();
    }

    @Override
    public Class<?> getConnectorClass() {
        return c;
    }

    @Override
    public void set(String bean, Object... o) {
        set(ref, bean, o);
    }

    @Override
    public Object get(Object ref, String bean) {
        Method m = getGetMethod(bean);
        if (m == null) return null;
        Object result = null;
        try {
            result = m.invoke(ref);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Object getRef() {
        return ref;
    }

    @Override
    public void set(Object ref, String bean, Object... o) {
        Method m = getSetMethod(bean, o);
        if (m == null) {
            int i;
            List<Entry<CollectionBean>> list = collectionbeans;
            for (i = 0; i < list.size(); i++) {
                if (bean.startsWith(list.get(i).getKey())) {
                    list.get(i).getValue().add(new JEntry(bean, o));
                    break;
                }
            }
            if (i == list.size()) {
                if (updateBean(bean)) set(ref, bean, o);
            }
        } else try {
            if (o.length == 1) m.invoke(ref, o[0]); else m.invoke(ref);
        } catch (IllegalArgumentException e) {
            if (m.getParameterTypes().length > 0) {
                System.err.println("argument " + o[0].getClass());
                System.err.println("expected " + m.getParameterTypes()[0]);
            }
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println("target " + m.getDeclaringClass());
            System.err.println("ref " + ref.getClass());
            e.printStackTrace();
        }
    }

    @Override
    public void setRef(Object o) {
        ref = o;
    }

    @Override
    public Class<?>[] getType(String bean, Object... o) {
        Method m = getSetMethod(bean, o);
        if (m == null) return null;
        return m.getParameterTypes();
    }

    @Override
    public Var createVar(Object ref, String bean) {
        Method m = getGetMethod(bean);
        if (m == null) return null;
        return new Var(ref, m);
    }

    @Override
    public void flush() {
        if (collectionbeans.size() > 0) {
            for (Entry<CollectionBean> entry : collectionbeans) {
                set(entry.getKey(), entry.getValue().flush());
            }
        }
        if (end != null) try {
            end.invoke(ref);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Updater createUpdater(Object ref, String bean, Object... o) {
        Method mset = getSetMethod(bean, o);
        if (mset.getParameterTypes().length > 0) return new JUpdater(ref, mset); else return new JActionUpdater(ref, mset);
    }

    @Override
    public VarUpdater createVarUpdater(Object ref, String bean, Object... o) {
        Method mset = getSetMethod(bean, o);
        Method mget = getGetMethod(bean);
        return new VarUpdater(ref, mget, mset);
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    @Override
    public void setInstance(Object instance) {
        this.instance = instance;
    }

    @Override
    public Object getInstance() {
        return instance;
    }

    private String realbeanName(String bean) {
        String out = bean;
        char car;
        for (int i = 0; i < bean.length(); i++) {
            car = bean.charAt(i);
            if (car == '$') {
                out = bean.substring(0, i);
                break;
            }
        }
        return out;
    }

    private Method getGetMethod(String bean) {
        Method m = null;
        try {
            m = c.getMethod(GET + bean);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            try {
                m = c.getMethod(IS + bean);
            } catch (SecurityException e1) {
            } catch (NoSuchMethodException e1) {
            }
        }
        if (m == null) m = getMethod(bean, (Object[]) null);
        return m;
    }

    private Method getSetMethod(String bean, Object... o) {
        Method m = null;
        String name = SET + bean;
        try {
            m = c.getMethod(name);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Method[] ms = c.getMethods();
            Method possible = null;
            int j;
            for (int i = 0; i < ms.length; i++) {
                if (ms[i].getName().equals(name)) {
                    if (o == null) {
                        m = ms[i];
                        break;
                    } else if (o.length == ms[i].getParameterTypes().length) {
                        Class<?>[] p = ms[i].getParameterTypes();
                        for (j = 0; j < o.length; j++) {
                            if (o[j] != null) if (p[j] != o[j].getClass()) break;
                        }
                        if (j == o.length) {
                            m = ms[i];
                            break;
                        } else possible = ms[i];
                    } else if (possible == null) possible = ms[i];
                }
            }
            if (possible != null && m == null) m = possible;
        }
        if (m == null) m = getMethod(bean, o);
        return m;
    }

    private Method getMethod(String name, Object... o) {
        if (o != null) if (o.length == 1) if (o[0] == null) o = null;
        Method m = null;
        Method[] ms = c.getMethods();
        Method possible = null;
        int j;
        for (int i = 0; i < ms.length; i++) {
            if (ms[i].getName().equals(name)) {
                if (o == null && ms[i].getParameterTypes().length == 0) {
                    m = ms[i];
                    break;
                } else if (o == null) {
                    m = ms[i];
                } else if (o.length == ms[i].getParameterTypes().length) {
                    Class<?>[] p = ms[i].getParameterTypes();
                    for (j = 0; j < o.length; j++) {
                        if (o[j] != null) if (p[j] != o[j].getClass()) break;
                    }
                    if (j == o.length) {
                        m = ms[i];
                        break;
                    } else possible = ms[i];
                } else if (possible == null) possible = ms[i];
            }
        }
        if (possible != null && m == null) m = possible;
        return m;
    }

    @Override
    public Object[] parseArg(JEntry e) {
        Class<?>[] t;
        String[] s = e.getValue();
        Object[] o = new Object[s.length];
        t = getType(e.getKey(), (Object[]) e.getValue());
        for (int i = 0; i < s.length; i++) {
            if (t != null) {
                if (t.length > i) {
                    if (box.containsKey(t[i])) {
                        o[i] = box.get(t[i]).parse(s[i]);
                        continue;
                    }
                }
            }
            if (s[i].startsWith("(Object)") && t != null) {
                o[i] = JFactory.getInstance().create(s[i].substring(8));
            } else if (s[i].startsWith("(Enum)") && t != null) {
                o[i] = EnumLateBoxer.getInstance().parse(s[i].substring(6));
            } else if (s[i].startsWith("(Class)") && t != null) {
                try {
                    o[i] = Class.forName(s[i].substring(7));
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            } else o[i] = s[i];
        }
        return o;
    }
}
