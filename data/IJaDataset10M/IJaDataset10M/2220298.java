package org.ddth.txbb.board.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.ddth.daf.Passport;
import org.ddth.daf.Resource;
import org.ddth.panda.bo.BaseBusinessObject;
import org.ddth.txbb.util.UnicodeNormalizer;

public class Zone extends BaseBusinessObject implements Resource, Comparable<Zone> {

    /**
	 * Auto-generated serial version UID
	 */
    private static final long serialVersionUID = 4014413416766016030L;

    protected int id;

    protected boolean isHidden;

    protected boolean isDeleted;

    protected int position;

    protected String title;

    protected String description;

    protected List<Integer> boxes;

    protected UnicodeNormalizer unicodeNormalizer;

    public Zone(int zoneId, boolean isHidden, boolean isDeleted, int zonePosition, String zoneTitle, String zoneDesc, String boxes) {
        this.id = zoneId;
        this.isHidden = isHidden;
        this.isDeleted = isDeleted;
        this.position = zonePosition;
        this.title = zoneTitle;
        this.description = zoneDesc;
        this.boxes = stringToBoxes(boxes);
    }

    public static List<Integer> stringToBoxes(String s) {
        if (s == null || s.equals("")) return new ArrayList<Integer>();
        List<Integer> result = new ArrayList<Integer>();
        String[] ss = s.split("\\|");
        for (int i = 0; i < ss.length; i++) {
            try {
                result.add(Integer.parseInt(ss[i]));
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static String boxesToString(int[] boxes) {
        if (boxes == null || boxes.length == 0) return "";
        StringBuffer sb = new StringBuffer(String.valueOf(boxes[0]));
        for (int i = 1; i < boxes.length; i++) sb.append("|" + boxes[i]);
        return sb.toString();
    }

    public static String boxesToString(Integer[] boxes) {
        if (boxes == null || boxes.length == 0) return "";
        StringBuffer sb = new StringBuffer(String.valueOf(boxes[0]));
        for (int i = 1; i < boxes.length; i++) sb.append("|" + boxes[i].intValue());
        return sb.toString();
    }

    public static String boxesToString(List<Integer> boxes) {
        if (boxes == null || boxes.size() == 0) return "";
        return boxesToString(boxes.toArray(new Integer[0]));
    }

    public int getResourceId() {
        return getId();
    }

    public boolean authorizePassport(Passport passport) {
        return false;
    }

    public int getId() {
        return this.id;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public void setVisibility(boolean visible) {
        this.isHidden = !visible;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void markDeleted() {
        this.isDeleted = true;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int newPosition) {
        this.position = newPosition;
    }

    private String titleForUrl = null;

    public String getTitleForUrl() {
        if (titleForUrl == null) {
            titleForUrl = unicodeNormalizer != null ? unicodeNormalizer.normalize(this.title) : this.title;
        }
        return titleForUrl;
    }

    public String getTitle() {
        return this.title != null ? this.title.trim() : "";
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public String getDescription() {
        return this.description != null ? this.description.trim() : "";
    }

    public void setDescription(String newDesc) {
        this.description = newDesc;
    }

    public synchronized void addBox(Box box) {
        if (box != null) addBox(box.getId());
    }

    public synchronized void addBox(Integer box) {
        if (box != null) addBox(box.intValue());
    }

    public synchronized void addBox(int box) {
        if (box != 0 && !this.boxes.contains(box)) this.boxes.add(box);
    }

    public List<Integer> getBoxes() {
        return Collections.unmodifiableList(this.boxes);
    }

    public synchronized void setBoxes(int[] newBoxes) {
        this.boxes.clear();
        if (newBoxes != null) for (int i = 0; i < newBoxes.length; i++) addBox(newBoxes[i]);
    }

    public synchronized void setBoxes(Integer[] newBoxes) {
        this.boxes.clear();
        if (newBoxes != null) for (int i = 0; i < newBoxes.length; i++) addBox(newBoxes[i]);
    }

    public synchronized void setBoxes(List<Integer> newBoxes) {
        this.boxes.clear();
        if (newBoxes != null) setBoxes(newBoxes.toArray(new Integer[0]));
    }

    public synchronized void movedownBox(Box box) {
        if (box != null) movedownBox(box.getId());
    }

    public synchronized void movedownBox(int boxId) {
        Integer prev = null, current = null;
        for (int i = 0, n = boxes.size(); i < n; i++) {
            prev = current;
            current = boxes.get(i);
            if (prev != null && prev == boxId) {
                boxes.set(i - 1, current);
                boxes.set(i, prev);
                break;
            }
        }
    }

    public synchronized void moveupBox(Box box) {
        if (box != null) moveupBox(box.getId());
    }

    public synchronized void moveupBox(int boxId) {
        Integer prev = null, current = null;
        for (int i = 0, n = boxes.size(); i < n; i++) {
            prev = current;
            current = boxes.get(i);
            if (current == boxId && prev != null) {
                boxes.set(i - 1, current);
                boxes.set(i, prev);
                break;
            }
        }
    }

    public synchronized void setUnicodeNormalizer(UnicodeNormalizer unicodeNormalizer) {
        this.unicodeNormalizer = unicodeNormalizer;
    }

    public int compareTo(Zone o) {
        if (o == null) return 1;
        int oId = o.getId();
        int oPos = o.getPosition();
        if (oId == this.id) return 0;
        if (oPos < this.position) return 1;
        if (oPos > this.position) return -1;
        if (oId < this.id) return 1;
        if (oId > this.id) return 1;
        return 0;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Zone)) return false;
        return compareTo((Zone) o) == 0;
    }
}
