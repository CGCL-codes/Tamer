package org.nakedobjects.viewer.web.request;

import org.nakedobjects.object.InvalidEntryException;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjects;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.viewer.web.component.DebugPane;
import org.nakedobjects.viewer.web.component.Page;

/**
 * Represents a task that the user is working through. Is used for both editing objects and setting up
 * parameters for an action method.
 */
public abstract class Task {

    private int[] boundaries;

    private final String description;

    protected final String[] errors;

    protected String error;

    private final Naked[] entries;

    private final String[] entryText;

    protected final Naked[] initialState;

    private final String name;

    protected final String[] names;

    protected final boolean[] required;

    protected final int numberOfEntries;

    protected final Object[][] options;

    private int step;

    private final NakedObject target;

    protected final NakedObjectSpecification[] types;

    public Task(final String name, final String description, final NakedObject target, final int noFields) {
        this.name = name;
        this.description = description;
        this.target = target;
        initialState = new Naked[noFields];
        names = new String[noFields];
        options = new Object[noFields][];
        required = new boolean[noFields];
        types = new NakedObjectSpecification[noFields];
        numberOfEntries = noFields;
        entryText = new String[noFields];
        entries = new Naked[noFields];
        errors = new String[noFields];
    }

    void init(Context context) {
        for (int i = 0; i < entryText.length; i++) {
            Naked obj = initialState[i];
            if (obj == null) {
                entryText[i] = "";
            } else if (obj.getSpecification().isValue()) {
                entryText[i] = obj.titleString();
            } else if (obj.getSpecification().isObject()) {
                entryText[i] = context.registerNakedObject((NakedObject) obj);
            }
        }
        divyUpWork();
        setupDefaults();
    }

    public abstract Naked completeTask(Request request, Page page);

    private void copyForThisStep(Object[] source, Object[] destination) {
        for (int i = 0; i < noOfEntriesInThisStep(); i++) {
            destination[i] = source[firstEntryInThisStep() + i];
        }
    }

    public void checkInstances(NakedObject[] objects) {
    }

    public void debug(DebugPane debugPane) {
        debugPane.appendln("name: " + name);
        debugPane.appendln("number of steps: " + numberOfSteps());
        debugPane.appendln("current step: " + step);
        debugPane.appendln("target: " + target);
        for (int i = 0; i < names.length; i++) {
            debugPane.appendln("  " + i + "  " + names[i] + ":  " + types[i].getFullName() + " -> " + entries[i]);
        }
    }

    private void divyUpWork() {
        if (numberOfEntries == 0) {
            boundaries = new int[2];
        } else {
            int[] b = new int[numberOfEntries + 2];
            int count = 0;
            b[count++] = 0;
            NakedObjectSpecification type = types[0];
            boolean direct = type.isValue() || (type.isObject() && type.isLookup());
            for (int i = 1; i < numberOfEntries; i++) {
                type = types[i];
                if (true || direct && (type.isValue() || (type.isObject() && type.isLookup()))) {
                    continue;
                }
                b[count++] = i;
                direct = type.isValue() || (type.isObject() && type.isLookup());
            }
            b[count++] = numberOfEntries;
            boundaries = new int[count];
            System.arraycopy(b, 0, boundaries, 0, count);
        }
    }

    private int firstEntryInThisStep() {
        return boundaries[step];
    }

    public String getDescription() {
        return description;
    }

    public String getError() {
        return error;
    }

    /**
     * Returns an array of errors, one for each element in the task.
     */
    public String[] getErrors() {
        return errors;
    }

    public String[] getEntryText() {
        String[] array = new String[noOfEntriesInThisStep()];
        copyForThisStep(entryText, array);
        return array;
    }

    public String getName() {
        return name;
    }

    public String[] getNames() {
        String[] array = new String[noOfEntriesInThisStep()];
        copyForThisStep(names, array);
        return array;
    }

    public Object[][] getOptions() {
        Object[][] array = new Object[noOfEntriesInThisStep()][];
        copyForThisStep(options, array);
        return array;
    }

    public Naked[] getParameters(Request request) {
        return entries;
    }

    public boolean[] getRequired() {
        return required;
    }

    public int getStep() {
        return step;
    }

    public NakedObject getTarget() {
        return target;
    }

    public String[] getTrail() {
        String[] trail = new String[boundaries.length - 1];
        for (int i = 0; i < trail.length; i++) {
            trail[i] = "step " + i;
        }
        return trail;
    }

    public NakedObjectSpecification[] getTypes() {
        NakedObjectSpecification[] array = new NakedObjectSpecification[noOfEntriesInThisStep()];
        copyForThisStep(types, array);
        return array;
    }

    public boolean isEditing() {
        return false;
    }

    public void nextStep() {
        step++;
    }

    private int noOfEntriesInThisStep() {
        return boundaries[step + 1] - boundaries[step];
    }

    public int numberOfSteps() {
        return boundaries.length - 1;
    }

    public void previousStep() {
        step--;
    }

    public void setFromFields(Request request, Context context) {
        int fldNo = 0;
        for (int i = boundaries[step]; i < boundaries[step + 1]; i++) {
            NakedObjectSpecification type = types[i];
            String textEntry = request.getFieldEntry(fldNo++);
            entryText[i] = textEntry;
            try {
                errors[i] = null;
                entries[i] = setFromField(context, i, type, textEntry);
                if (required[i] && textEntry.equals("")) {
                    errors[i] = "Field required";
                }
            } catch (InvalidEntryException e) {
                errors[i] = e.getMessage();
            } catch (TextEntryParseException e) {
                errors[i] = e.getMessage();
            }
        }
    }

    private Naked setFromField(Context context, int i, NakedObjectSpecification type, String textEntry) {
        if (type.isValue()) {
            NakedValue val = NakedObjects.getObjectLoader().createValueInstance(type);
            val.parseTextEntry(textEntry);
            entryText[i] = val.titleString();
            return val;
        } else if (type.isObject()) {
            if (textEntry == null || textEntry.equals("") || textEntry.equals("null")) {
                return null;
            } else {
                return context.getNakedObject(textEntry);
            }
        } else {
            return null;
        }
    }

    private void setupDefaults() {
        for (int i = 0; i < entries.length; i++) {
            entries[i] = initialState[i];
        }
    }

    public abstract void checkForValidity(Request request);
}
