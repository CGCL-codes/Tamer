package tms.server.session;

import java.util.ArrayList;
import java.util.Iterator;
import tms.client.entities.Field;

/**
 * Entity to store records IDs and is used for the RecordBrowsing.
 * 
 * @author Werner Liebenberg
 * @author Martin Schlemmer
 * @author Wildrich Fourie
 */
public class RecordIds<E> extends ArrayList<Long> {

    private static final long serialVersionUID = 3415102006631725971L;

    private int currentIndex = -1;

    private Field customInputModelField = null;

    public RecordIds() {
    }

    public void addRecordId(Long id) {
        this.add(id);
    }

    public void addRecordId(long id) {
        this.addRecordId(new Long(id));
    }

    private void addAndSetRecordId(Long id) {
        this.addRecordId(id);
        this.setCurrentRecordIndex(this.size() - 1);
    }

    private Long getRecordId(int index) {
        if (isIndexWithinRange(index)) return this.get(index); else return null;
    }

    private Long getCurrentRecordId() {
        return this.getRecordId(currentIndex);
    }

    public void setCurrentRecordId(Long id) {
        if (this.size() == 0) {
            this.addRecordId(id);
            currentIndex = 0;
        } else {
            boolean found = false;
            for (int i = 0; i < this.size(); i++) {
                Long storedId = this.get(i);
                if (storedId.equals(id)) {
                    currentIndex = i;
                    found = true;
                    break;
                }
            }
            if (!found) addAndSetRecordId(id);
        }
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public Field getCustomInputModelField() {
        return customInputModelField;
    }

    public void setCustomInputModelField(Field field) {
        this.customInputModelField = field;
    }

    public boolean isCustomInputModelFieldSet() {
        return this.customInputModelField != null;
    }

    public void setCurrentRecordIndex(int index) {
        if (isIndexWithinRange(index)) currentIndex = index; else resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        if (this.size() == 0) currentIndex = -1; else currentIndex = 0;
    }

    public Long getFirstRecordId() {
        if (this.size() > 0) {
            this.setCurrentRecordIndex(0);
            return this.getCurrentRecordId();
        } else return null;
    }

    public Long peekPreviousRecordId() {
        if (hasPreviousRecordIds()) return getRecordId(currentIndex - 1);
        return getCurrentRecordId();
    }

    public Long peekNextRecordId() {
        if (hasMoreRecordIds()) return getRecordId(currentIndex + 1);
        return getCurrentRecordId();
    }

    public Long getPreviousRecordId() {
        if (hasPreviousRecordIds()) currentIndex--;
        return getCurrentRecordId();
    }

    public Long getNextRecordId() {
        if (hasMoreRecordIds()) currentIndex++;
        return getCurrentRecordId();
    }

    public Long getLastRecordId() {
        if (this.size() > 0) {
            this.setCurrentRecordIndex(this.size() - 1);
            return this.getCurrentRecordId();
        } else return null;
    }

    private boolean hasMoreRecordIds() {
        return currentIndex < this.size() - 1;
    }

    private boolean hasPreviousRecordIds() {
        return currentIndex > 0;
    }

    private boolean isIndexWithinRange(int index) {
        if (this.size() == 0) return false; else return (index >= 0 && index < this.size());
    }

    @Override
    public void clear() {
        super.clear();
        currentIndex = -1;
        customInputModelField = null;
    }

    @Override
    public String toString() {
        String str = "";
        Iterator<Long> itr = this.iterator();
        while (itr.hasNext()) {
            str += itr.next();
            if (itr.hasNext()) str += ",";
        }
        return str;
    }
}
