package thoto.jamyda.data;

import java.util.ArrayList;
import java.util.List;
import thoto.jamyda.utils.DataUtils;

public class SectionData extends AbstractData implements Comparable<SectionData> {

    private String guiSectionName;

    private List<ItemData> itemList;

    /**
	 * Creates a new {@link SectionData} 
	 * 
	 * @param name The name of this data object
	 * @param guiSectionName Optional name of the section in which to be displayed in GUI. Set null to use parameter <code>name</code> instead. 
	 */
    public SectionData(String name, String guiSectionName) {
        super(name);
        this.guiSectionName = guiSectionName;
        if (null == guiSectionName || guiSectionName.trim().length() == 0) {
            this.guiSectionName = name;
        }
    }

    /**
	 * Add a new {@link ItemData} to this section
	 * 
	 * @param item The (non-null) item to add
	 */
    public void add(ItemData item) {
        if (item == null) return;
        if (this.itemList == null) this.itemList = new ArrayList<ItemData>();
        if (this.itemList.contains(item) == false) this.itemList.add(item);
    }

    public String toXML(String indent) {
        if (indent == null) indent = "";
        String guiNameParam = "";
        if (false == getGuiSectionName().equals(getName())) {
            guiNameParam = " " + XMLConstants.GUI_NAME + "=\"" + DataUtils.toValidXML(getGuiSectionName()) + "\"";
        }
        String s = "\n" + indent + "<" + XMLConstants.SECTION + " " + XMLConstants.NAME + "=\"" + DataUtils.toValidXML(getName()) + "\"" + guiNameParam + ">";
        if (this.itemList != null) for (int a = 0, z = this.itemList.size(); a < z; a++) s += this.itemList.get(a).toXML(indent + XMLConstants.INDENT);
        s += "\n" + indent + "</" + XMLConstants.SECTION + ">";
        return s;
    }

    /**
	 *
	 * @see thoto.jamyda.data.AbstractData#getClone()
	 */
    public AbstractData getClone() {
        SectionData clone = new SectionData(getName(), getGuiSectionName());
        if (getItemList() != null) {
            for (int a = 0, z = getItemList().size(); a < z; a++) clone.add((ItemData) getItemList().get(a).getClone());
        }
        return clone;
    }

    /**
	 *
	 * @see thoto.jamyda.data.AbstractData#toDosBox()
	 */
    public String toDosBox() {
        return toDosBox(true);
    }

    /**
	 * An extended method for decide, whether to get the DOSBox representation of this
	 * object with or without it's section header.<br>
	 * This is needed for exporting section data in a compressed way.
	 *  
	 * @param withSectionHeader True to add section header, false to override it.
	 * 
	 * @return A string representation of this object for DOSBox configuration files
	 */
    public String toDosBox(boolean withSectionHeader) {
        String str = "";
        if (withSectionHeader) {
            str = "[" + getName() + "]\n";
        }
        if (getItemList() != null) for (int a = 0, z = getItemList().size(); a < z; a++) str += getItemList().get(a).toDosBox();
        return str;
    }

    /**
	 *
	 * @see thoto.jamyda.data.AbstractData#getElementCount()
	 */
    public int getElementCount() {
        if (this.itemList != null) return this.itemList.size();
        return 0;
    }

    /**
	 * @return the itemList
	 */
    public List<ItemData> getItemList() {
        return itemList;
    }

    /**
	 * Returns 
	 *
	 * @return the guiSectionName
	 */
    public String getGuiSectionName() {
        return guiSectionName;
    }

    /**
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(SectionData o) {
        if (null == o) {
            return -1;
        }
        return getGuiSectionName().compareToIgnoreCase(o.getGuiSectionName());
    }
}
