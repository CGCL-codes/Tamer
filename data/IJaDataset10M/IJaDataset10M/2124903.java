package org.nakedobjects.nos.client.dnd.content;

import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.adapter.NakedValue;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.reflect.Allow;
import org.nakedobjects.nof.core.reflect.Veto;
import org.nakedobjects.nof.core.util.DebugString;
import org.nakedobjects.nof.core.util.ToString;
import org.nakedobjects.nos.client.dnd.Content;
import org.nakedobjects.nos.client.dnd.ValueParameter;
import org.nakedobjects.nos.client.dnd.drawing.Image;
import org.nakedobjects.nos.client.dnd.image.ImageFactory;

public class ValueParameterImpl extends AbstractValueContent implements ValueParameter {

    private final NakedValue object;

    private final String name;

    private final NakedObjectSpecification specification;

    private final boolean isRequired;

    private final NakedValue[] options;

    private int noLines;

    private int typicalLength;

    private int maxLength;

    private final String description;

    private final boolean canWrap;

    public ValueParameterImpl(final String name, final String description, final Naked naked, final NakedObjectSpecification specification, final boolean required, final NakedValue[] options, final int noLines, final boolean canWrap, final int maxLength, final int typicalLength) {
        this.name = name;
        this.description = description;
        this.specification = specification;
        this.isRequired = required;
        this.options = options;
        this.noLines = noLines;
        this.canWrap = canWrap;
        this.maxLength = maxLength;
        this.typicalLength = typicalLength;
        object = (NakedValue) naked;
    }

    public void debugDetails(final DebugString debug) {
        debug.appendln("name", name);
        debug.appendln("required", isRequired);
        debug.appendln("object", object);
    }

    public void entryComplete() {
    }

    public String getIconName() {
        return "";
    }

    public Image getIconPicture(final int iconHeight) {
        return ImageFactory.getInstance().loadIcon("value", 12, null);
    }

    public Naked getNaked() {
        return object;
    }

    public int getNoLines() {
        return noLines;
    }

    public NakedValue getObject() {
        return object;
    }

    public Naked[] getOptions() {
        return options;
    }

    public boolean isEmpty() {
        return object.isEmpty();
    }

    public boolean isRequired() {
        return isRequired;
    }

    public boolean canWrap() {
        return canWrap;
    }

    public void clear() {
        object.clear();
    }

    public boolean isTransient() {
        return true;
    }

    public boolean isValue() {
        return true;
    }

    public boolean isOptionEnabled() {
        return options != null && options.length > 0;
    }

    public String title() {
        return object.titleString();
    }

    public String toString() {
        ToString toString = new ToString(this);
        toString.append("object", object);
        return toString.toString();
    }

    public String getParameterName() {
        return name;
    }

    public NakedObjectSpecification getSpecification() {
        return specification;
    }

    public Naked drop(final Content sourceContent) {
        return null;
    }

    public Consent canDrop(final Content sourceContent) {
        return Veto.DEFAULT;
    }

    public void parseTextEntry(final String entryText) {
        object.parseTextEntry(entryText);
    }

    public String getDescription() {
        String title = object == null ? "" : ": " + object.titleString();
        String type = name.indexOf(specification.getShortName()) == -1 ? "" : " (" + specification.getShortName() + ")";
        return name + type + title + " " + description;
    }

    public String getHelp() {
        return null;
    }

    public String getId() {
        return null;
    }

    public Consent isEditable() {
        return Allow.DEFAULT;
    }

    public int getMaximumLength() {
        return maxLength;
    }

    public int getTypicalLineLength() {
        return typicalLength;
    }
}
