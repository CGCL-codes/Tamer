package net.sf.chellow.monad.types;

import net.sf.chellow.monad.ProgrammerException;
import net.sf.chellow.monad.UserException;
import net.sf.chellow.monad.VFMessage;
import net.sf.chellow.monad.VFParameter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class MonadFloat extends MonadObject {

    private Float floatValue;

    private Float min = null;

    private Float max = null;

    public static Attr toXml(Document doc, String name, float value) {
        Attr attr = doc.createAttribute(name);
        attr.setValue(Float.toString(value));
        return attr;
    }

    protected MonadFloat() {
    }

    public MonadFloat(String label, String floatString) throws UserException, ProgrammerException {
        this(null, label, floatString, null, null);
    }

    protected MonadFloat(String typeName, String floatString, float min, float max) throws UserException, ProgrammerException {
        this(typeName, null, floatString, new Float(min), new Float(max));
    }

    protected MonadFloat(String typeName, String name, String floatString, Float min, Float max) throws UserException, ProgrammerException {
        super(typeName, name);
        this.min = min;
        this.max = max;
        try {
            setFloat(new Float(floatString));
        } catch (NumberFormatException e) {
            throw UserException.newInvalidParameter(new VFMessage("malformed_float", new VFParameter("note", e.getMessage())));
        }
    }

    public MonadFloat(float floatValue) {
        setFloat(new Float(floatValue));
    }

    public MonadFloat(String floatString) throws UserException, ProgrammerException {
        this(null, floatString);
    }

    public Float getFloat() {
        return floatValue;
    }

    void setFloat(Float floatValue) {
        this.floatValue = floatValue;
    }

    public void update(Float floatValue) throws UserException, ProgrammerException {
        if ((min != null) && (floatValue.floatValue() < min.intValue())) {
            throw UserException.newInvalidParameter(new VFMessage(VFMessage.NUMBER_TOO_SMALL, new VFParameter[] { new VFParameter("number", floatValue.toString()), new VFParameter("min", min.toString()) }));
        }
        if ((max != null) && (floatValue.floatValue() > max.intValue())) {
            throw UserException.newInvalidParameter(new VFMessage(VFMessage.NUMBER_TOO_BIG, new VFParameter[] { new VFParameter("number", floatValue.toString()), new VFParameter("max", max.toString()) }));
        }
        setFloat(floatValue);
    }

    public Node toXML(Document doc) {
        Node node = doc.createAttribute(getLabel());
        node.setNodeValue(floatValue.toString());
        return node;
    }

    public String toString() {
        return floatValue.toString();
    }

    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj instanceof MonadFloat) {
            isEqual = ((MonadFloat) obj).getFloat().equals(getFloat());
        }
        return isEqual;
    }
}
