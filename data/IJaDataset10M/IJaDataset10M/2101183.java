package org.jnp.server;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.naming.Binding;
import javax.naming.CannotProceedException;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.Reference;
import javax.naming.spi.ResolveResult;
import org.jnp.interfaces.Naming;
import org.jnp.interfaces.NamingContext;
import org.jnp.interfaces.NamingParser;
import org.jboss.logging.Logger;

/**
 * The JNDI naming server implementation class.
 * 
 * @author Rickard Oberg
 * @author patriot1burke
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57199 $
 */
public class NamingServer implements Naming, java.io.Serializable {

    private static Logger log = Logger.getLogger(NamingServer.class);

    /** @since 1.12 at least */
    private static final long serialVersionUID = 4183855539507934373L;

    protected Hashtable table = new Hashtable();

    protected Name prefix;

    protected NamingParser parser = new NamingParser();

    protected NamingServer parent;

    public NamingServer() throws NamingException {
        this(null, null);
    }

    public NamingServer(Name prefix, NamingServer parent) throws NamingException {
        if (prefix == null) prefix = parser.parse("");
        this.prefix = prefix;
        this.parent = parent;
    }

    public synchronized void bind(Name name, Object obj, String className) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException();
        } else if (name.size() > 1) {
            Object ctx = getObject(name);
            if (ctx != null) {
                if (ctx instanceof NamingServer) {
                    ((NamingServer) ctx).bind(name.getSuffix(1), obj, className);
                } else if (ctx instanceof Reference) {
                    if (((Reference) ctx).get("nns") != null) {
                        CannotProceedException cpe = new CannotProceedException();
                        cpe.setResolvedObj(ctx);
                        cpe.setRemainingName(name.getSuffix(1));
                        throw cpe;
                    } else {
                        throw new NotContextException();
                    }
                } else {
                    throw new NotContextException();
                }
            } else {
                throw new NameNotFoundException();
            }
        } else {
            if (name.get(0).equals("")) {
                throw new InvalidNameException();
            } else {
                try {
                    getBinding(name);
                    throw new NameAlreadyBoundException();
                } catch (NameNotFoundException e) {
                    setBinding(name, obj, className);
                }
            }
        }
    }

    public synchronized void rebind(Name name, Object obj, String className) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException();
        } else if (name.size() > 1) {
            Object ctx = getObject(name);
            if (ctx instanceof NamingServer) {
                ((NamingServer) ctx).rebind(name.getSuffix(1), obj, className);
            } else if (ctx instanceof Reference) {
                if (((Reference) ctx).get("nns") != null) {
                    CannotProceedException cpe = new CannotProceedException();
                    cpe.setResolvedObj(ctx);
                    cpe.setRemainingName(name.getSuffix(1));
                    throw cpe;
                } else {
                    throw new NotContextException();
                }
            } else {
                throw new NotContextException();
            }
        } else {
            if (name.get(0).equals("")) {
                throw new InvalidNameException();
            } else {
                setBinding(name, obj, className);
            }
        }
    }

    public synchronized void unbind(Name name) throws NamingException {
        if (name.isEmpty()) {
            throw new InvalidNameException();
        } else if (name.size() > 1) {
            Object ctx = getObject(name);
            if (ctx instanceof NamingServer) {
                ((NamingServer) ctx).unbind(name.getSuffix(1));
            } else if (ctx instanceof Reference) {
                if (((Reference) ctx).get("nns") != null) {
                    CannotProceedException cpe = new CannotProceedException();
                    cpe.setResolvedObj(ctx);
                    cpe.setRemainingName(name.getSuffix(1));
                    throw cpe;
                } else {
                    throw new NotContextException();
                }
            } else {
                throw new NotContextException();
            }
        } else {
            if (name.get(0).equals("")) {
                throw new InvalidNameException();
            } else {
                if (getBinding(name) != null) {
                    removeBinding(name);
                } else {
                    throw new NameNotFoundException();
                }
            }
        }
    }

    public Object lookup(Name name) throws NamingException {
        Object result;
        if (name.isEmpty()) {
            result = new NamingContext(null, (Name) (prefix.clone()), getRoot());
        } else if (name.size() > 1) {
            Object ctx = getObject(name);
            if (ctx instanceof NamingServer) {
                result = ((NamingServer) ctx).lookup(name.getSuffix(1));
            } else if (ctx instanceof Reference) {
                if (((Reference) ctx).get("nns") != null) {
                    CannotProceedException cpe = new CannotProceedException();
                    cpe.setResolvedObj(ctx);
                    cpe.setRemainingName(name.getSuffix(1));
                    throw cpe;
                }
                result = new ResolveResult(ctx, name.getSuffix(1));
            } else {
                throw new NotContextException();
            }
        } else {
            if (name.get(0).equals("")) {
                result = new NamingContext(null, prefix, getRoot());
            } else {
                Object res = getObject(name);
                if (res instanceof NamingServer) {
                    Name fullName = (Name) (prefix.clone());
                    fullName.addAll(name);
                    result = new NamingContext(null, fullName, getRoot());
                } else result = res;
            }
        }
        return result;
    }

    public Collection list(Name name) throws NamingException {
        if (name.isEmpty()) {
            Vector list = new Vector();
            Enumeration keys = table.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                Binding b = getBinding(key);
                list.addElement(new NameClassPair(b.getName(), b.getClassName(), true));
            }
            return list;
        } else {
            Object ctx = getObject(name);
            if (ctx instanceof NamingServer) {
                return ((NamingServer) ctx).list(name.getSuffix(1));
            } else if (ctx instanceof Reference) {
                if (((Reference) ctx).get("nns") != null) {
                    CannotProceedException cpe = new CannotProceedException();
                    cpe.setResolvedObj(ctx);
                    cpe.setRemainingName(name.getSuffix(1));
                    throw cpe;
                } else {
                    throw new NotContextException();
                }
            } else {
                throw new NotContextException();
            }
        }
    }

    public Collection listBindings(Name name) throws NamingException {
        if (name.isEmpty()) {
            Collection bindings = table.values();
            Collection newBindings = new Vector(bindings.size());
            Iterator iter = bindings.iterator();
            while (iter.hasNext()) {
                Binding b = (Binding) iter.next();
                if (b.getObject() instanceof NamingServer) {
                    Name n = (Name) prefix.clone();
                    n.add(b.getName());
                    newBindings.add(new Binding(b.getName(), b.getClassName(), new NamingContext(null, n, getRoot())));
                } else {
                    newBindings.add(b);
                }
            }
            return newBindings;
        } else {
            Object ctx = getObject(name);
            if (ctx instanceof NamingServer) {
                return ((NamingServer) ctx).listBindings(name.getSuffix(1));
            } else if (ctx instanceof Reference) {
                if (((Reference) ctx).get("nns") != null) {
                    CannotProceedException cpe = new CannotProceedException();
                    cpe.setResolvedObj(ctx);
                    cpe.setRemainingName(name.getSuffix(1));
                    throw cpe;
                } else {
                    throw new NotContextException();
                }
            } else {
                throw new NotContextException();
            }
        }
    }

    public Context createSubcontext(Name name) throws NamingException {
        if (name.size() == 0) throw new InvalidNameException("Cannot pass an empty name to createSubcontext");
        NamingException ex = null;
        Context subCtx = null;
        if (name.size() > 1) {
            Object ctx = getObject(name);
            if (ctx != null) {
                Name subCtxName = name.getSuffix(1);
                if (ctx instanceof NamingServer) {
                    subCtx = ((NamingServer) ctx).createSubcontext(subCtxName);
                } else if (ctx instanceof Reference) {
                    if (((Reference) ctx).get("nns") != null) {
                        CannotProceedException cpe = new CannotProceedException();
                        cpe.setResolvedObj(ctx);
                        cpe.setRemainingName(subCtxName);
                        throw cpe;
                    } else {
                        ex = new NotContextException();
                        ex.setResolvedName(name.getPrefix(0));
                        ex.setRemainingName(subCtxName);
                        throw ex;
                    }
                } else {
                    ex = new NotContextException();
                    ex.setResolvedName(name.getPrefix(0));
                    ex.setRemainingName(subCtxName);
                    throw ex;
                }
            } else {
                ex = new NameNotFoundException();
                ex.setRemainingName(name);
                throw ex;
            }
        } else {
            Object binding = table.get(name.get(0));
            if (binding != null) {
                ex = new NameAlreadyBoundException();
                ex.setResolvedName(prefix);
                ex.setRemainingName(name);
                throw ex;
            } else {
                Name fullName = (Name) prefix.clone();
                fullName.addAll(name);
                NamingServer subContext = new NamingServer(fullName, this);
                setBinding(name, subContext, NamingContext.class.getName());
                subCtx = new NamingContext(null, fullName, getRoot());
            }
        }
        return subCtx;
    }

    public Naming getRoot() {
        if (parent == null) return this; else return parent.getRoot();
    }

    private void setBinding(Name name, Object obj, String className) {
        String n = name.toString();
        table.put(n, new Binding(n, className, obj, true));
    }

    private Binding getBinding(String key) throws NameNotFoundException {
        Binding b = (Binding) table.get(key);
        if (b == null) {
            if (log.isTraceEnabled()) {
                StringBuffer tmp = new StringBuffer("No binding for: " + key);
                tmp.append(" in context ");
                tmp.append(this.prefix);
                tmp.append(", bindings:\n");
                Iterator bindings = table.values().iterator();
                while (bindings.hasNext()) {
                    Binding value = (Binding) bindings.next();
                    tmp.append(value.getName());
                    tmp.append('=');
                    if (value.getObject() != null) tmp.append(value.getObject().toString()); else tmp.append("null");
                    tmp.append('\n');
                }
                log.trace(tmp.toString());
            }
            throw new NameNotFoundException(key + " not bound");
        }
        return b;
    }

    private Binding getBinding(Name key) throws NameNotFoundException {
        return getBinding(key.get(0));
    }

    private Object getObject(Name key) throws NameNotFoundException {
        return getBinding(key).getObject();
    }

    private void removeBinding(Name name) {
        table.remove(name.get(0));
    }
}
