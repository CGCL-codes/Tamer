package de.iritgo.aktera.comm;

import de.iritgo.aktera.model.Command;
import de.iritgo.aktera.model.Input;
import de.iritgo.aktera.model.Output;
import de.iritgo.aktera.model.ResponseElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class is the superclass for all ResponseElement implementations in this
 * package. It provides the attribute and nested element handling that all response elements
 * have in common, and checks the parameters to ensure they are serializable versions
 */
public class AbstractMessageResponseElement implements ResponseElement, Serializable {

    private String name = null;

    private List nestedElements = new ArrayList();

    private Map attributes = null;

    public void removeAttribute(String key) {
        if (attributes != null) {
            synchronized (attributes) {
                attributes.remove(key);
            }
        }
    }

    public void add(ResponseElement re) {
        if (re == null) {
            return;
        }
        if (nestedElements == null) {
            nestedElements = new ArrayList();
        }
        if (!(re instanceof AbstractMessageResponseElement)) {
            throw new IllegalArgumentException("Nested element '" + re.getName() + "' was of type '" + re.getClass().getName() + "'");
        }
        synchronized (nestedElements) {
            nestedElements.add(re);
        }
    }

    public void remove(ResponseElement re) {
        if (re == null) {
            return;
        }
        if (nestedElements != null) {
            synchronized (nestedElements) {
                nestedElements.remove(re);
            }
        }
    }

    public void setAttribute(String key, Object value) {
        if (key == null) {
            return;
        }
        if (attributes == null) {
            attributes = new HashMap();
        }
        if (value == null) {
            return;
        }
        if (!(value instanceof Serializable)) {
            throw new IllegalArgumentException("Attribute '" + key + "' was not serializable. It was of type '" + value.getClass().getName() + "'");
        }
        if (value instanceof ResponseElement) {
            if (!(value instanceof AbstractMessageResponseElement)) {
                throw new IllegalArgumentException("Attribute '" + key + "' was a '" + value.getClass().getName() + "'");
            }
        }
        synchronized (attributes) {
            attributes.put(key, value);
        }
    }

    public Object getAttribute(String key) {
        Object returnValue = null;
        if (key != null) {
            if (attributes != null) {
                returnValue = attributes.get(key);
            }
        }
        return returnValue;
    }

    public Map getAttributes() {
        Map returnValue = null;
        if (attributes == null) {
            returnValue = new HashMap();
        } else {
            returnValue = attributes;
        }
        return returnValue;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public List getAll() {
        return nestedElements;
    }

    protected void copyFrom(ResponseElement re) {
        if (re == null) {
            return;
        }
        List nested = re.getAll();
        if (nested != null) {
            ResponseElement oneNested = null;
            for (Iterator in = nested.iterator(); in.hasNext(); ) {
                oneNested = (ResponseElement) in.next();
                if (oneNested instanceof Command) {
                    add(new CommandMessage((Command) oneNested));
                } else if (oneNested instanceof Input) {
                    add(new InputMessage((Input) oneNested));
                } else if (oneNested instanceof Output) {
                    add(new OutputMessage((Output) oneNested));
                } else {
                    throw new IllegalArgumentException("Nested element " + oneNested.getName() + " of element " + getName() + " is not a valid type. It is " + oneNested.getClass().getName());
                }
            }
        }
        Map attribs = re.getAttributes();
        String oneAttribKey = null;
        Object oneAttribValue = null;
        for (Iterator ia = attribs.keySet().iterator(); ia.hasNext(); ) {
            oneAttribKey = (String) ia.next();
            oneAttribValue = attribs.get(oneAttribKey);
            if (oneAttribValue == null) {
                setAttribute(oneAttribKey, null);
            } else if (oneAttribValue instanceof Command) {
                setAttribute(oneAttribKey, new CommandMessage((Command) oneAttribValue));
            } else if (oneAttribValue instanceof Input) {
                setAttribute(oneAttribKey, new InputMessage((Input) oneAttribValue));
            } else if (oneAttribValue instanceof Output) {
                setAttribute(oneAttribKey, new OutputMessage((Output) oneAttribValue));
            } else if (oneAttribValue instanceof Serializable) {
                setAttribute(oneAttribKey, oneAttribValue);
            } else {
                throw new IllegalArgumentException("Attribute '" + oneAttribKey + "' of element '" + getName() + "' is not serializable. It is a '" + oneAttribValue.getClass().getName() + "'");
            }
        }
    }
}
