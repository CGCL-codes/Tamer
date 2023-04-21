package net.sourceforge.nattable.group;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import net.sourceforge.nattable.persistence.IPersistable;
import net.sourceforge.nattable.util.ObjectUtils;

/**
 * Tracks:
 *  Columns (by index) in a column Group. Does not keep the column indexes in any defined order.
 *  Expand/collapse state of the Group.
 *  Name of the Column Group (CG)
 */
public class ColumnGroupModel implements IPersistable {

    private static final String PERSISTENCE_KEY_COLUMN_GROUPS = ".columnGroups";

    /** Column group header name to column indexes */
    private final LinkedHashMap<String, ColumnGroup> nameToColumnGroup = new LinkedHashMap<String, ColumnGroup>();

    private final LinkedHashMap<Integer, String> indexToColumnGroupName = new LinkedHashMap<Integer, String>();

    private final Collection<IColumnGroupModelListener> listeners = new HashSet<IColumnGroupModelListener>();

    public void registerColumnGroupModelListener(IColumnGroupModelListener listener) {
        listeners.add(listener);
    }

    public void unregisterColumnGroupModelListener(IColumnGroupModelListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (IColumnGroupModelListener listener : listeners) {
            listener.columnGroupModelChanged();
        }
    }

    public void saveState(String prefix, Properties properties) {
        StringBuilder strBuilder = new StringBuilder();
        for (String columnGroupName : nameToColumnGroup.keySet()) {
            ColumnGroup columnGroup = nameToColumnGroup.get(columnGroupName);
            if (columnGroup.members.size() == 0) {
                continue;
            }
            strBuilder.append(columnGroupName);
            strBuilder.append('=');
            strBuilder.append(columnGroup.collapsed ? "collapsed" : "expanded");
            strBuilder.append(':');
            strBuilder.append(columnGroup.collapseable ? "collapseable" : "uncollapseable");
            strBuilder.append(':');
            strBuilder.append(columnGroup.unbreakable ? "unbreakable" : "breakable");
            strBuilder.append(':');
            for (Integer member : columnGroup.members) {
                strBuilder.append(member);
                strBuilder.append(',');
            }
            strBuilder.append('|');
        }
        properties.setProperty(prefix + PERSISTENCE_KEY_COLUMN_GROUPS, strBuilder.toString());
    }

    public void loadState(String prefix, Properties properties) {
        String property = properties.getProperty(prefix + PERSISTENCE_KEY_COLUMN_GROUPS);
        if (property != null) {
            clear();
            StringTokenizer columnGroupTokenizer = new StringTokenizer(property, "|");
            while (columnGroupTokenizer.hasMoreTokens()) {
                String columnGroupToken = columnGroupTokenizer.nextToken();
                int separatorIndex = columnGroupToken.indexOf('=');
                String columnGroupName = columnGroupToken.substring(0, separatorIndex);
                ColumnGroup columnGroup = new ColumnGroup(columnGroupName);
                nameToColumnGroup.put(columnGroupName, columnGroup);
                String[] columnGroupProperties = columnGroupToken.substring(separatorIndex + 1).split(":");
                String state = columnGroupProperties[0];
                if ("collapsed".equals(state)) {
                    columnGroup.collapsed = true;
                } else if ("expanded".equals(state)) {
                    columnGroup.collapsed = false;
                } else {
                    throw new IllegalArgumentException(state + " not one of 'expanded' or 'collapsed'");
                }
                state = columnGroupProperties[1];
                if ("collapseable".equals(state)) {
                    columnGroup.collapseable = true;
                } else if ("uncollapseable".equals(state)) {
                    columnGroup.collapseable = false;
                } else {
                    throw new IllegalArgumentException(state + " not one of 'uncollapseable' or 'collapseable'");
                }
                state = columnGroupProperties[2];
                if ("breakable".equals(state)) {
                    columnGroup.unbreakable = false;
                } else if ("unbreakable".equals(state)) {
                    columnGroup.unbreakable = true;
                } else {
                    throw new IllegalArgumentException(state + " not one of 'breakable' or 'unbreakable'");
                }
                String indexes = columnGroupProperties[3];
                StringTokenizer indexTokenizer = new StringTokenizer(indexes, ",");
                while (indexTokenizer.hasMoreTokens()) {
                    Integer index = Integer.valueOf(indexTokenizer.nextToken());
                    columnGroup.members.add(index);
                    indexToColumnGroupName.put(index, columnGroupName);
                }
            }
        }
    }

    /**
	 * Creates the column group if one does not exist with the given name
	 * and adds the column indexes to it.
	 * @see ColumnGroupModel#insertColumnIndexes(String, int...);
	 */
    public void addColumnsIndexesToGroup(String colGroupName, int... bodyColumnIndexs) {
        if (nameToColumnGroup.get(colGroupName) == null) {
            ColumnGroup group = new ColumnGroup(colGroupName);
            nameToColumnGroup.put(colGroupName, group);
        }
        insertColumnIndexes(colGroupName, bodyColumnIndexs);
        notifyListeners();
    }

    /**
	 * This method will add column index(s) to an existing group
	 * @param colGroupName to add the indexes to
	 * @param columnIndexToInsert
	 * @return FALSE if:
	 * 		The column group is frozen
	 * 		Index is already s part of a column group
	 */
    public boolean insertColumnIndexes(String colGroupName, int... columnIndexesToInsert) {
        LinkedList<Integer> members = new LinkedList<Integer>();
        LinkedHashMap<Integer, String> indexToColumnGroupName = new LinkedHashMap<Integer, String>();
        ColumnGroup columnGroup = nameToColumnGroup.get(colGroupName);
        if (columnGroup.unbreakable) {
            return false;
        }
        for (int columnIndexToInsert : columnIndexesToInsert) {
            final Integer index = Integer.valueOf(columnIndexToInsert);
            if (isPartOfAGroup(columnIndexToInsert)) {
                return false;
            }
            members.add(index);
            indexToColumnGroupName.put(index, colGroupName);
        }
        this.indexToColumnGroupName.putAll(indexToColumnGroupName);
        columnGroup.members.addAll(members);
        notifyListeners();
        return true;
    }

    /**
	 * Add static columns identified by <code>staticColumnIndexes</code> to the
	 * given columnGroup <code>colGroupName</code>. Static columns remains 
	 * visible when a column group is collapsed.
	 * 
	 * @param colGroupName to add the indexes to
	 * @param staticColumnIndexes
	 */
    public void setStaticColumnIndexesByGroup(String colGroupName, int[] staticColumnIndexes) {
        if (nameToColumnGroup.get(colGroupName) == null) {
            ColumnGroup group = new ColumnGroup(colGroupName);
            nameToColumnGroup.put(colGroupName, group);
        }
        insertStaticColumnIndexes(colGroupName, staticColumnIndexes);
        notifyListeners();
    }

    /**
	 * This method will add static column index(s) to an existing group
	 * 
	 * @param colGroupName to add the indexes to
	 * @param columnIndexToInsert
	 */
    public void insertStaticColumnIndexes(String colGroupName, int... columnIndexesToInsert) {
        LinkedList<Integer> staticColumnIndexes = new LinkedList<Integer>();
        ColumnGroup columnGroup = nameToColumnGroup.get(colGroupName);
        for (int columnIndexToInsert : columnIndexesToInsert) {
            final Integer index = Integer.valueOf(columnIndexToInsert);
            staticColumnIndexes.add(index);
        }
        columnGroup.staticColumnIndexes.addAll(staticColumnIndexes);
        notifyListeners();
    }

    private ColumnGroup getColumnGroupForIndex(int bodyColumnIndex) {
        Integer key = Integer.valueOf(bodyColumnIndex);
        if (indexToColumnGroupName.containsKey(key)) {
            return nameToColumnGroup.get(indexToColumnGroupName.get(key));
        }
        return null;
    }

    public String getColumnGroupNameForIndex(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex)) {
            return getColumnGroupForIndex(bodyColumnIndex).name;
        }
        return null;
    }

    public List<Integer> getColumnIndexesForColumnGroupName(String name) {
        ColumnGroup group = nameToColumnGroup.get(name);
        if (group != null) return group.getMembers();
        return Collections.emptyList();
    }

    /**
	 * @return Unmodifiable list of other column indexes in the same group as this index
	 */
    public List<Integer> getColumnIndexesInGroup(int bodyColumnIndex) {
        ColumnGroup group = getColumnGroupForIndex(bodyColumnIndex);
        if (group == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(group.members);
    }

    /**
	 * @return Unmodifiable list of static column indexes (which remains visible
	 * when the group is collapsed) in the same group as this index
	 */
    public List<Integer> getStaticColumnIndexesInGroup(int bodyColumnIndex) {
        ColumnGroup group = getColumnGroupForIndex(bodyColumnIndex);
        if (group == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(group.staticColumnIndexes);
    }

    /**
	 * @return all the indexes which belong to groups
	 */
    public List<Integer> getAllIndexesInGroups() {
        List<Integer> indexes = new LinkedList<Integer>();
        for (ColumnGroup columnGroup : nameToColumnGroup.values()) {
            indexes.addAll(columnGroup.members);
        }
        return indexes;
    }

    public boolean isPartOfAGroup(int bodyColumnIndex) {
        return indexToColumnGroupName.containsKey(Integer.valueOf(bodyColumnIndex));
    }

    public void clear() {
        nameToColumnGroup.clear();
        indexToColumnGroupName.clear();
    }

    /**
	 * @return Number of column Groups in the model.
	 */
    public int size() {
        return nameToColumnGroup.size();
    }

    /**
	 * Number of columns in the Group which the bodyColumnIndex belongs to.
	 */
    public int sizeOfGroup(int bodyColumnIndex) {
        return getColumnIndexesInGroup(bodyColumnIndex).size();
    }

    /**
	 * Number of static columns in the Group which the bodyColumnIndex belongs
	 * to.
	 */
    public int sizeOfStaticColumns(int bodyColumnIndex) {
        return getStaticColumnIndexesInGroup(bodyColumnIndex).size();
    }

    /**
	 * @return TRUE if no column groups exist
	 */
    public boolean isEmpty() {
        return nameToColumnGroup.size() == 0;
    }

    /**
	 * @return TRUE if the column group this index belongs to is collapsed
	 */
    public boolean isCollapsed(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex)) {
            return getColumnGroupForIndex(bodyColumnIndex).collapsed;
        }
        return false;
    }

    public boolean isCollapsed(String columnGroupName) {
        boolean collpased = false;
        if (nameToColumnGroup.containsKey(columnGroupName)) {
            collpased = nameToColumnGroup.get(columnGroupName).collapsed;
        }
        return collpased;
    }

    /**
	 * @return TRUE if the column group this index is collapseable
	 */
    public boolean isCollapseable(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex)) {
            return getColumnGroupForIndex(bodyColumnIndex).collapseable;
        }
        return false;
    }

    public boolean isCollapseable(String columnGroupName) {
        boolean collpaseable = false;
        if (nameToColumnGroup.containsKey(columnGroupName)) {
            collpaseable = nameToColumnGroup.get(columnGroupName).collapseable;
        }
        return collpaseable;
    }

    /**
	 * @return TRUE if <code>bodyColumnIndex</code> is contained in the list
	 * of static columns of the column group this index belongs to
	 */
    public boolean isStaticColumn(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex)) {
            return getColumnGroupForIndex(bodyColumnIndex).staticColumnIndexes.contains(bodyColumnIndex);
        }
        return false;
    }

    /**
	 * @return TRUE if a group by this name exists
	 */
    public boolean isAGroup(String cellValue) {
        return nameToColumnGroup.containsKey(cellValue);
    }

    /**
	 * @return Total number of columns hidden for all the collapsed columns.
	 */
    public int getCollapsedColumnCount() {
        int count = 0;
        for (String groupName : nameToColumnGroup.keySet()) {
            ColumnGroup columnGroup = nameToColumnGroup.get(groupName);
            if (columnGroup.collapsed) {
                int staticColumnIndexesCount = columnGroup.getStaticColumnIndexesCount();
                count = count + columnGroup.getMemberCount() - Math.max(staticColumnIndexesCount, 1);
            }
        }
        return count;
    }

    protected void collapse(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex)) {
            getColumnGroupForIndex(bodyColumnIndex).collapsed = true;
        }
        notifyListeners();
    }

    protected void expand(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex)) {
            getColumnGroupForIndex(bodyColumnIndex).collapsed = false;
        }
        notifyListeners();
    }

    /**
	 * @return TRUE if index successfully removed from its group.
	 */
    public boolean removeColumnFromGroup(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex) && !isPartOfAnUnbreakableGroup(bodyColumnIndex)) {
            ColumnGroup group = getColumnGroupForIndex(bodyColumnIndex);
            removeColumn(bodyColumnIndex, group);
            notifyListeners();
            return true;
        }
        return false;
    }

    private void removeColumn(int bodyColumnIndex, ColumnGroup group) {
        final LinkedList<Integer> members = group.members;
        members.remove(Integer.valueOf(bodyColumnIndex));
        indexToColumnGroupName.remove(Integer.valueOf(bodyColumnIndex));
        if (members.size() == 0) {
            nameToColumnGroup.remove(group.name);
        }
        notifyListeners();
    }

    /**
	 * @param columnIndex
	 * @return The position of the index within the column group
	 */
    public int getColumnGroupPositionFromIndex(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex)) {
            ColumnGroup columnGroup = getColumnGroupForIndex(bodyColumnIndex);
            return columnGroup.members.indexOf(Integer.valueOf(bodyColumnIndex));
        }
        return -1;
    }

    /**
	 * Toggle the expand/collapse state of the Column Group
	 */
    public ColumnGroup toggleColumnGroupExpandCollapse(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex)) {
            ColumnGroup columnGroup = getColumnGroupForIndex(bodyColumnIndex);
            columnGroup.collapsed = !columnGroup.collapsed;
            notifyListeners();
            return columnGroup;
        }
        return null;
    }

    /**
	 * If a group is marked as unbreakable, the composition of the group cannot be changed.
	 *    Columns cannot be added or removed from the group.
	 *    Columns may be reorder within the group.
	 * @return TRUE if the operation was successful.
	 * @see NTBL 393
	 */
    public boolean setGroupUnBreakable(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex)) {
            getColumnGroupForIndex(bodyColumnIndex).unbreakable = true;
            return true;
        }
        return false;
    }

    public boolean setGroupUnCollapseable(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex)) {
            getColumnGroupForIndex(bodyColumnIndex).collapseable = false;
            return true;
        }
        return false;
    }

    public boolean isPartOfAnUnbreakableGroup(int bodyColumnIndex) {
        if (isPartOfAGroup(bodyColumnIndex)) {
            return getColumnGroupForIndex(bodyColumnIndex).unbreakable;
        }
        return false;
    }

    public class ColumnGroup {

        /** Body column indexes */
        private final LinkedList<Integer> members = new LinkedList<Integer>();

        /** column indexes which remain visible when collapsing this group */
        private LinkedList<Integer> staticColumnIndexes = new LinkedList<Integer>();

        final String name;

        boolean collapsed = false;

        boolean collapseable = true;

        public boolean unbreakable = false;

        ColumnGroup(String groupName) {
            this.name = groupName;
        }

        int getMemberCount() {
            return members.size();
        }

        public List<Integer> getMembers() {
            return members;
        }

        public List<Integer> getMembersSorted() {
            List<Integer> sortedMembers = Collections.unmodifiableList(members);
            Collections.sort(sortedMembers);
            return sortedMembers;
        }

        /**
		 * @return the column indexes which remains visible when collapsing
		 * this group
		 */
        public LinkedList<Integer> getStaticColumnIndexes() {
            return staticColumnIndexes;
        }

        /**
		 * @return the number of columns which remains visible when collapsing
		 * this group
		 */
        int getStaticColumnIndexesCount() {
            return staticColumnIndexes.size();
        }

        @Override
        public String toString() {
            return "Column Group:\n\t name: " + name + "\n\t collapsed: " + collapsed + "\n\t unbreakable: " + unbreakable + "\n\t members: " + ObjectUtils.toString(members) + "\n" + "\n\t staticColumns: " + ObjectUtils.toString(staticColumnIndexes) + "\n";
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Column Group Model:\n");
        for (String groupName : nameToColumnGroup.keySet()) {
            buffer.append(nameToColumnGroup.get(groupName));
        }
        return buffer.toString();
    }
}
