package jp.go.aist.six.oval.model.results;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import jp.go.aist.six.oval.model.OvalObject;
import jp.go.aist.six.oval.model.common.MessageType;

/**
 * The TestedItem holds a reference to a system characteristic item
 * that matched the object specified in a test.
 *
 * @author	Akihito Nakamura, AIST
 * @version $Id: TestedItemType.java 2025 2011-09-21 03:05:00Z nakamura5akihito $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class TestedItemType implements OvalObject {

    private final Collection<MessageType> message = new ArrayList<MessageType>();

    private int item_id;

    private ResultEnumeration result;

    /**
     * Constructor.
     */
    public TestedItemType() {
    }

    public TestedItemType(final int item_id, final ResultEnumeration result) {
        setItemID(item_id);
        setResult(result);
    }

    /**
     */
    public void setMessage(final Collection<? extends MessageType> message) {
        if (this.message != message) {
            this.message.clear();
            if (message != null && message.size() > 0) {
                this.message.addAll(message);
            }
        }
    }

    public Collection<MessageType> getMessage() {
        return this.message;
    }

    public Iterator<MessageType> iterateMessage() {
        return this.message.iterator();
    }

    /**
     */
    public void setItemID(final int item_id) {
        this.item_id = item_id;
    }

    public int getItemID() {
        return this.item_id;
    }

    public void setResult(final ResultEnumeration result) {
        this.result = result;
    }

    public ResultEnumeration getResult() {
        return this.result;
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int hash = 17;
        int item_id = getItemID();
        hash = prime * hash + item_id;
        ResultEnumeration result = getResult();
        hash = prime * hash + ((result == null) ? 0 : result.hashCode());
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TestedItemType)) {
            return false;
        }
        TestedItemType other = (TestedItemType) obj;
        int other_item_id = other.getItemID();
        int this_item_id = this.getItemID();
        if (this_item_id == other_item_id) {
            ResultEnumeration other_reault = other.getResult();
            ResultEnumeration this_result = this.getResult();
            if (this_result == other_reault) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "tested_item[item_id=" + getItemID() + ", result=" + getResult() + "]";
    }
}
