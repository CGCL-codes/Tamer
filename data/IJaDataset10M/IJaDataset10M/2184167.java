package net.sourceforge.processdash.data.repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import net.sourceforge.processdash.data.ListData;
import net.sourceforge.processdash.data.SaveableData;
import net.sourceforge.processdash.data.SimpleData;
import net.sourceforge.processdash.data.compiler.CompiledScript;

public class SearchFunction implements SaveableData, RepositoryListener, DataListener, Comparator, DataNameFilter.PrefixLocal {

    protected SearchFactory factory;

    protected String name = null, prefix = null;

    protected String start, tag, tag2;

    protected CompiledScript script;

    protected DataRepository data;

    protected ListData value, externalValue;

    protected int chopTagLength;

    private volatile boolean valueQueried = false;

    /** a list of the conditions created by this SearchFunction. */
    protected Set condList = Collections.synchronizedSet(new HashSet());

    public SearchFunction(SearchFactory factory, String name, String start, String tag, CompiledScript expression, DataRepository data, String prefix) {
        this.factory = factory;
        this.name = name;
        this.start = start;
        storeTag(tag);
        this.script = expression;
        this.data = data;
        this.prefix = prefix;
        this.value = new ListData();
        this.externalValue = null;
        chopTagLength = (this.tag.startsWith("/") ? this.tag.length() : 0);
        this.value.setEditable(false);
        data.addRepositoryListener(this, start);
    }

    private void storeTag(String tagName) {
        if (tagName.startsWith("/")) {
            tag = tagName;
            tag2 = tagName.substring(1);
        } else if (tagName.startsWith(" ") || tagName.length() == 0) {
            tag = tag2 = tagName;
        } else {
            tag = "/" + tagName;
            tag2 = tagName;
        }
    }

    private static final String CONDITION_NAME = "_SearchCondition_///";

    private static volatile int UNIQUE_ID = 0;

    /** Create a unique name for a condition expression for the given prefix */
    private String getAnonymousConditionName(String prefix) {
        int id;
        synchronized (SearchFunction.class) {
            id = UNIQUE_ID++;
        }
        return DataRepository.createDataName(DataRepository.anonymousPrefix + prefix, CONDITION_NAME + id);
    }

    /** Return the prefix that corresponds to the named condition expression */
    private String getConditionPrefix(String conditionName) {
        int pos = conditionName.indexOf(CONDITION_NAME);
        if (pos == -1) return null;
        return conditionName.substring(DataRepository.anonymousPrefix.length(), pos - 1);
    }

    /** If the dataName is a matching tag, return the corresponding prefix.
     *  Otherwise return null. */
    private String getTagPrefix(String dataName) {
        if (!dataName.endsWith(tag)) return null;
        if (!dataName.startsWith(start)) return null;
        return dataName.substring(0, dataName.length() - chopTagLength);
    }

    /** A collection of threads for whom we are currently handling
     *  thread events.
     */
    private Set eventThreads = Collections.synchronizedSet(new HashSet());

    /** Add a prefix to our list, doing our best to keep the list
     *  sorted in hierarchy order.
     *
     * @return true if the value of the list changed.
     */
    private boolean doAdd(String prefix) {
        int pos = Collections.binarySearch(value.asList(), prefix, this);
        if (pos >= 0) return false; else if (value.asList().contains(prefix)) {
            value.sortContents(this);
            return true;
        } else {
            value.insert(prefix, -1 - pos);
            return true;
        }
    }

    public boolean acceptPrefixLocalName(String prefix, String localName) {
        return (localName.equals(tag2) || localName.endsWith(tag));
    }

    public void dataAdded(String dataName) {
        String dataPrefix = getTagPrefix(dataName);
        if (dataPrefix == null) return;
        if (script == null) {
            data.addActiveDataListener(dataName, this, name, false);
            if (test2(data.getSimpleValue(dataName))) {
                if (doAdd(dataPrefix)) doNotify();
            }
        } else {
            String condName = getAnonymousConditionName(dataPrefix);
            SaveableData condition = new CompiledFunction(condName, script, data, dataPrefix);
            data.putValue(condName, condition);
            condList.add(condName);
            data.addActiveDataListener(condName, this, name, false);
            if (test(condition.getSimpleValue())) {
                if (doAdd(dataPrefix)) doNotify();
            }
        }
        eventThreads.remove(Thread.currentThread());
    }

    public void dataRemoved(String dataName) {
        String dataPrefix = getTagPrefix(dataName);
        if (dataPrefix == null) return;
        String condNamePrefix = dataPrefix + "/" + CONDITION_NAME;
        Iterator i = condList.iterator();
        String condName;
        while (i.hasNext()) {
            condName = (String) i.next();
            if (condName.startsWith(condNamePrefix)) {
                data.removeDataListener(condName, this);
                condList.remove(condName);
                break;
            }
        }
        if (value.remove(dataPrefix)) doNotify();
        eventThreads.remove(Thread.currentThread());
    }

    private boolean test(SimpleData data) {
        return (data != null && data.test());
    }

    private boolean test2(SimpleData data) {
        return (data != null);
    }

    private boolean isCondition(String name) {
        return (condList.contains(name));
    }

    private boolean handleDataEvent(DataEvent e) {
        String dataName = e.getName();
        String dataPrefix;
        boolean include;
        if (isCondition(dataName)) {
            dataPrefix = getConditionPrefix(dataName);
            include = test(e.getValue());
        } else {
            dataPrefix = getTagPrefix(dataName);
            if (dataPrefix == null) return false;
            include = test2(e.getValue());
        }
        if (include) return doAdd(dataPrefix); else return value.remove(dataPrefix);
    }

    public void dataValueChanged(DataEvent e) {
        boolean needToNotify = handleDataEvent(e);
        if (needToNotify) doNotify();
    }

    public void dataValuesChanged(Vector v) {
        boolean needToNotify = false;
        if (v == null || v.size() == 0) return;
        for (int i = v.size(); i-- > 0; ) if (handleDataEvent((DataEvent) v.elementAt(i))) needToNotify = true;
        if (needToNotify) doNotify();
    }

    protected void doNotify() {
        externalValue = null;
        if (valueQueried) data.valueRecalculated(name, this);
    }

    public boolean isEditable() {
        return false;
    }

    public void setEditable(boolean e) {
    }

    public boolean isDefined() {
        return true;
    }

    public void setDefined(boolean d) {
    }

    public String saveString() {
        return "";
    }

    public SimpleData getSimpleValue() {
        valueQueried = true;
        if (externalValue == null && value != null) externalValue = new ListData(value);
        return externalValue;
    }

    public void dispose() {
        if (data == null) return;
        data.removeRepositoryListener(this);
        data.deleteDataListener(this);
        condList = null;
        data = null;
        name = prefix = start = tag = null;
        script = null;
        value = null;
    }

    public SaveableData getEditable(boolean editable) {
        return this;
    }

    public int compare(Object o1, Object o2) {
        return data.compareNames(o1 + "/tag", o2 + "/tag");
    }
}
