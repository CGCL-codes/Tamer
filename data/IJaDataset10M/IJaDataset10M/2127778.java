package org.apache.myfaces.view.facelets.el;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.el.FunctionMapper;
import org.apache.myfaces.view.facelets.util.ReflectionUtil;

/**
 * Default implementation of the FunctionMapper
 * 
 * @see java.lang.reflect.Method
 * @see javax.el.FunctionMapper
 * 
 * @author Jacob Hookom
 * @version $Id: DefaultFunctionMapper.java,v 1.6 2008/07/13 19:01:43 rlubke Exp $
 */
public final class DefaultFunctionMapper extends FunctionMapper implements Externalizable {

    private static final long serialVersionUID = 1L;

    private Map<String, Function> _functions = null;

    public Method resolveFunction(String prefix, String localName) {
        if (_functions != null) {
            Function f = (Function) _functions.get(prefix + ":" + localName);
            return f.getMethod();
        }
        return null;
    }

    public void addFunction(String prefix, String localName, Method m) {
        if (_functions == null) {
            _functions = new HashMap<String, Function>();
        }
        Function f = new Function(prefix, localName, m);
        synchronized (this) {
            _functions.put(prefix + ":" + localName, f);
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(_functions);
    }

    @SuppressWarnings("unchecked")
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        _functions = (Map<String, Function>) in.readObject();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(128);
        sb.append("FunctionMapper[\n");
        for (Function function : _functions.values()) {
            sb.append(function).append('\n');
        }
        sb.append(']');
        return sb.toString();
    }

    private static class Function implements Externalizable {

        private static final long serialVersionUID = 1L;

        protected transient Method _m;

        protected String _owner;

        protected String _name;

        protected String[] _types;

        protected String _prefix;

        protected String _localName;

        /**
         * 
         */
        public Function(String prefix, String localName, Method m) {
            if (localName == null) {
                throw new NullPointerException("LocalName cannot be null");
            }
            if (m == null) {
                throw new NullPointerException("Method cannot be null");
            }
            _prefix = prefix;
            _localName = localName;
            _m = m;
        }

        public Function() {
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeUTF(_prefix != null ? _prefix : "");
            out.writeUTF(_localName);
            out.writeUTF(_m.getDeclaringClass().getName());
            out.writeUTF(_m.getName());
            out.writeObject(ReflectionUtil.toTypeNameArray(this._m.getParameterTypes()));
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            _prefix = in.readUTF();
            if ("".equals(_prefix)) _prefix = null;
            _localName = in.readUTF();
            _owner = in.readUTF();
            _name = in.readUTF();
            _types = (String[]) in.readObject();
        }

        public Method getMethod() {
            if (_m == null) {
                try {
                    Class<?> t = ReflectionUtil.forName(_owner);
                    Class<?>[] p = ReflectionUtil.toTypeArray(_types);
                    _m = t.getMethod(_name, p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return _m;
        }

        public boolean matches(String prefix, String localName) {
            if (_prefix != null) {
                if (prefix == null) return false;
                if (!_prefix.equals(prefix)) return false;
            }
            return _localName.equals(localName);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Function) {
                return hashCode() == obj.hashCode();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (_prefix + _localName).hashCode();
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer(32);
            sb.append("Function[");
            if (_prefix != null) {
                sb.append(_prefix).append(':');
            }
            sb.append(_name).append("] ");
            sb.append(_m);
            return sb.toString();
        }
    }
}
