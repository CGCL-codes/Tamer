package xxl.core.indexStructures;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import xxl.core.collections.Lists;
import xxl.core.collections.MappedList;
import xxl.core.functions.AbstractFunction;
import xxl.core.io.converters.Converter;
import xxl.core.util.Interval1D;

/** This class implements a B+Tree.
 * 
 * For a detailed discussion see Douglas Comer: 
 * "The Ubiquitous B-Tree",
 * ACM Comput. Surv. 11(2), 121-137, 1979.
 */
public class BTree extends ORTree {

    /** Returns the interval containing the given entry.
	 * 
	 * @param entry an entry of the nodes of the b-tree
	 * @return the interval containing the entry
	 */
    public Interval1D interval(Object entry) {
        return (Interval1D) descriptor(entry);
    }

    public Tree.Node createNode(int level) {
        return new Node().initialize(level, new ArrayList());
    }

    public Descriptor computeDescriptor(Collection collection) {
        List entries = (List) collection;
        return Descriptors.union(descriptor(entries.get(0)), descriptor(entries.get(entries.size() - 1)));
    }

    /** <tt>Node</tt> is the class used to represent leaf- and non-leaf nodes of <tt>BTree</tt>.
	 *	Nodes are stored in containers.
	 *
	 *	@see Tree.Node
	 *  @see ORTree.Node
	 */
    public class Node extends ORTree.Node {

        /**
		 * @param right flag indicating if the left (<tt>false</tt>) or right (<tt>true</tt>) border should be returned
		 * @return list of borders
		 */
        protected List borders(final boolean right) {
            return new MappedList((List) this.entries, new AbstractFunction() {

                public Object invoke(Object object) {
                    return interval(object).border(right);
                }
            });
        }

        public Iterator query(Descriptor queryDescriptor) {
            Interval1D interval = (Interval1D) queryDescriptor;
            int[] indices = new int[2];
            for (int i = 0; i < 2; i++) {
                indices[i] = Lists.indexOf(borders(i == 0), interval.border(i != 0), interval.comparator(), i != 0);
                if (indices[i] < 0) indices[i] = -indices[i] - 1 - i;
            }
            return ((List) entries).subList(indices[0], indices[1] + 1).iterator();
        }

        protected ORTree.IndexEntry chooseSubtree(Descriptor descriptor, Iterator entries) {
            Interval1D interval = (Interval1D) descriptor;
            int index = Collections.binarySearch(borders(false), interval.border(false), interval.comparator());
            return (IndexEntry) ((List) this.entries).get(index >= 0 ? index : index == -1 ? 0 : -index - 2);
        }

        protected void grow(Object data, Stack path) {
            Interval1D interval = interval(data);
            int index = Collections.binarySearch(borders(false), interval.border(false), interval.comparator());
            ((List) entries).add(index >= 0 ? index : -index - 1, data);
        }

        protected Tree.Node.SplitInfo split(Stack path) {
            Node node = (Node) node(path);
            List[] entryLists = new List[] { (List) node.entries, (List) entries };
            List subList = entryLists[0].subList((node.splitMinNumber() + node.splitMaxNumber()) / 2, entryLists[0].size());
            entryLists[1].addAll(subList);
            subList.clear();
            ((IndexEntry) indexEntry(path)).descriptor = computeDescriptor(entryLists[0]);
            return new SplitInfo(path).initialize(computeDescriptor(entryLists[1]));
        }
    }

    /** Gets a suitable Converter to serialize the tree's entries.
	 * 
	 * @param borderConverter a converter to convert borders
	 * @param comparator a comparator for data objects stored in the tree
	 * @return a converter for entries of the tree
	 */
    public Converter indexEntryConverter(final Converter borderConverter, final Comparator comparator) {
        return indexEntryConverter(new Converter() {

            public Object read(DataInput dataInput, Object object) throws IOException {
                return new Interval1D(borderConverter.read(dataInput, null), borderConverter.read(dataInput, null), comparator);
            }

            public void write(DataOutput dataOutput, Object object) throws IOException {
                Interval1D interval = (Interval1D) object;
                borderConverter.write(dataOutput, interval.border(false));
                borderConverter.write(dataOutput, interval.border(true));
            }
        });
    }

    /** Gets a suitable Converter to serialize the tree's nodes.
	 * 
	 * @param objectConverter a converter to convert the data objects stored in the tree
	 * @param borderConverter a converter to convert borders
	 * @param comparator a comparator for data objects stored in the tree
	 * @return a NodeConverter
	 */
    public Converter nodeConverter(Converter objectConverter, final Converter borderConverter, final Comparator comparator) {
        return nodeConverter(objectConverter, indexEntryConverter(borderConverter, comparator));
    }
}
