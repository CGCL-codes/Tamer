package eu.pisolutions.ocelot.document.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import eu.pisolutions.collections.map.InterceptingSortedMap;
import eu.pisolutions.lang.ToStringBuilder;
import eu.pisolutions.lang.Validations;
import eu.pisolutions.ocelot.document.DocumentNode;
import eu.pisolutions.ocelot.document.FlexibleDocumentNode;
import eu.pisolutions.ocelot.document.PdfNameConstants;
import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistry;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistryHelper;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContext;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContextHelper;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.io.DocumentReadingContextHelper;
import eu.pisolutions.ocelot.document.io.DocumentReadingException;
import eu.pisolutions.ocelot.object.ContainerPdfObjectElement;
import eu.pisolutions.ocelot.object.PdfArrayObject;
import eu.pisolutions.ocelot.object.PdfDictionaryObject;
import eu.pisolutions.ocelot.object.PdfNameObject;
import eu.pisolutions.ocelot.object.PdfObject;
import eu.pisolutions.ocelot.object.PdfObjectReference;
import eu.pisolutions.ocelot.version.PdfVersion;
import eu.pisolutions.ocelot.version.RequiredPdfVersionHelper;

/**
 * Sorted collection of {@link DocumentNode}s arranged as a tree.
 * <p>
 * When this document node is read, all the intermediate and leaf tree nodes are created and referenced.
 * Whenever the entries of this document node are modified, all the intermediate and leaf tree nodes are released, so they are created from scratch when this document node registers them.
 * </p>
 *
 * @author Laurent Pireyn
 * @param <K> the type of keys
 * @param <V> the type of document nodes
 */
public abstract class DocumentNodeTree<K extends Comparable<K>, V extends DocumentNode> extends FlexibleDocumentNode {

    public static final int DEFAULT_MAX_INTERMEDIATE_NODE_CHILD_COUNT = 50;

    public static final int DEFAULT_MAX_LEAF_NODE_ENTRY_COUNT = 50;

    private final class EntryMap extends InterceptingSortedMap<K, V> {

        public EntryMap() {
            super(new TreeMap<K, V>());
        }

        @Override
        protected void beforePutValue(V value) {
            Validations.notNull(value, "value");
            DocumentNodeTree.this.validateValue(value);
        }

        @Override
        protected void afterPutValue(V value) {
            DocumentNodeTree.this.modifyEntries();
        }

        @Override
        protected void afterRemoveValue(V value) {
            DocumentNodeTree.this.modifyEntries();
        }
    }

    /**
     * Node in the tree.
     */
    private abstract class TreeNode extends FlexibleDocumentNode {

        protected TreeNode(boolean indirect) {
            super(indirect);
        }

        public abstract K getFirstKey();

        public abstract K getLastKey();

        public final void readFromPdfObject(DocumentReadingContext context, PdfObject object) {
            final PdfDictionaryObject dictionary = DocumentReadingContextHelper.asPdfObjectOfType(PdfDictionaryObject.class, object);
            this.readSpecificEntriesFrom(context, dictionary);
        }

        public final PdfDictionaryObject createPdfObject(DocumentPdfObjectCreationContext context) {
            final PdfDictionaryObject dictionary = new PdfDictionaryObject();
            final K firstKey = this.getFirstKey();
            final K lastKey = this.getLastKey();
            if (firstKey != null && lastKey != null) {
                final PdfArrayObject array = new PdfArrayObject();
                array.add(DocumentNodeTree.this.createElement(firstKey));
                array.add(DocumentNodeTree.this.createElement(lastKey));
                dictionary.put(PdfNameConstants.LIMITS, array);
            }
            this.addSpecificEntriesTo(context, dictionary);
            return dictionary;
        }

        protected abstract void readSpecificEntriesFrom(DocumentReadingContext context, PdfDictionaryObject dictionary);

        protected abstract void addSpecificEntriesTo(DocumentPdfObjectCreationContext context, PdfDictionaryObject dictionary);
    }

    /**
     * Intermediate node in the tree.
     */
    private final class IntermediateTreeNode extends TreeNode {

        public final List<TreeNode> children = new ArrayList<TreeNode>();

        public IntermediateTreeNode(boolean indirect) {
            super(indirect);
        }

        @Override
        public K getFirstKey() {
            return !this.children.isEmpty() ? this.children.get(0).getFirstKey() : null;
        }

        @Override
        public K getLastKey() {
            return !this.children.isEmpty() ? this.children.get(this.children.size() - 1).getLastKey() : null;
        }

        @Override
        public void registerNodes(DocumentNodeRegistry registry) {
            new DocumentNodeRegistryHelper(registry).registerNodes(this.children);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).appendSuper(super.toString()).append("children", this.children).toString();
        }

        @Override
        protected void readSpecificEntriesFrom(DocumentReadingContext context, PdfDictionaryObject dictionary) {
            for (final ContainerPdfObjectElement element : DocumentReadingContextHelper.asElementOfType(PdfArrayObject.class, DocumentReadingContextHelper.getRequiredPdfDictionaryObjectValue(dictionary, PdfNameConstants.KIDS))) {
                final TreeNode node = context.getNode(DocumentReadingContextHelper.asElementOfType(PdfObjectReference.class, element).getIdentifier(), DocumentNodeTree.this.treeNodeFactory);
                if (node != null) {
                    this.children.add(node);
                }
            }
        }

        @Override
        protected void addSpecificEntriesTo(DocumentPdfObjectCreationContext context, PdfDictionaryObject dictionary) {
            final PdfArrayObject array = new PdfArrayObject();
            for (final TreeNode child : this.children) {
                array.add(new PdfObjectReference(context.getIdentifier(child)));
            }
            dictionary.put(PdfNameConstants.KIDS, array);
        }
    }

    /**
     * Leaf node in the tree.
     */
    private final class LeafTreeNode extends TreeNode {

        public final SortedMap<K, V> entries = new TreeMap<K, V>();

        public LeafTreeNode(boolean indirect) {
            super(indirect);
        }

        @Override
        public K getFirstKey() {
            return !this.entries.isEmpty() ? this.entries.firstKey() : null;
        }

        @Override
        public K getLastKey() {
            return !this.entries.isEmpty() ? this.entries.lastKey() : null;
        }

        @Override
        public void registerNodes(DocumentNodeRegistry registry) {
            new DocumentNodeRegistryHelper(registry).registerNodes(this.entries.values());
        }

        @Override
        protected void readSpecificEntriesFrom(DocumentReadingContext context, PdfDictionaryObject dictionary) {
            final DocumentReadingContextHelper helper = new DocumentReadingContextHelper(context);
            final PdfArrayObject array = DocumentReadingContextHelper.asElementOfType(PdfArrayObject.class, DocumentReadingContextHelper.getRequiredPdfDictionaryObjectValue(dictionary, DocumentNodeTree.this.getEntriesKey()));
            final int elementCount = array.size();
            if (elementCount % 2 != 0) {
                throw new DocumentReadingException("Odd element count in PDF array object for tree: " + elementCount);
            }
            final DocumentNodeFactory<V> factory = DocumentNodeTree.this.getValueFactory();
            for (int i = 0; i < elementCount; i += 2) {
                final V value = helper.getNode(array.get(i + 1), factory);
                if (value != null) {
                    this.entries.put(DocumentNodeTree.this.readKey(array.get(i)), value);
                }
            }
            DocumentNodeTree.this.entries.putAll(this.entries);
        }

        @Override
        protected void addSpecificEntriesTo(DocumentPdfObjectCreationContext context, PdfDictionaryObject dictionary) {
            final DocumentPdfObjectCreationContextHelper helper = new DocumentPdfObjectCreationContextHelper(context);
            final PdfArrayObject array = new PdfArrayObject();
            for (final Entry<K, V> entry : this.entries.entrySet()) {
                array.add(DocumentNodeTree.this.createElement(entry.getKey()));
                array.add(helper.getElement(entry.getValue()));
            }
            dictionary.put(DocumentNodeTree.this.getEntriesKey(), array);
        }
    }

    /**
     * {@link TreeNode} factory.
     */
    private final class TreeNodeFactory extends Object implements DocumentNodeFactory<TreeNode> {

        public TreeNodeFactory() {
            super();
        }

        public TreeNode createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            final PdfDictionaryObject dictionary = DocumentReadingContextHelper.asPdfObjectOfType(PdfDictionaryObject.class, object);
            if (dictionary.containsKey(PdfNameConstants.KIDS)) {
                return new IntermediateTreeNode(indirect);
            }
            if (dictionary.containsKey(DocumentNodeTree.this.getEntriesKey())) {
                return new LeafTreeNode(indirect);
            }
            throw new DocumentReadingException("Cannot determine node type: " + dictionary);
        }
    }

    private final EntryMap entries = new EntryMap();

    private final TreeNodeFactory treeNodeFactory = new TreeNodeFactory();

    private TreeNode rootTreeNode;

    private int maxIntermediateNodeChildCount = DocumentNodeTree.DEFAULT_MAX_INTERMEDIATE_NODE_CHILD_COUNT;

    private int maxLeafNodeEntryCount = DocumentNodeTree.DEFAULT_MAX_LEAF_NODE_ENTRY_COUNT;

    protected DocumentNodeTree() {
        super();
    }

    protected DocumentNodeTree(boolean indirect) {
        super(indirect);
    }

    public final SortedMap<K, V> getEntries() {
        return this.entries;
    }

    public final int getMaxIntermediateNodeChildCount() {
        return this.maxIntermediateNodeChildCount;
    }

    public final void setMaxIntermediateNodeChildCount(int maxIntermediateNodeChildCount) {
        Validations.greaterThanOrEqualTo(maxIntermediateNodeChildCount, 2, "max intermediate node child count");
        this.maxIntermediateNodeChildCount = maxIntermediateNodeChildCount;
    }

    public final int getMaxLeafNodeEntryCount() {
        return this.maxLeafNodeEntryCount;
    }

    public final void setMaxLeafNodeEntryCount(int maxLeafNodeEntryCount) {
        Validations.greaterThanOrEqualTo(maxLeafNodeEntryCount, 1, "max leaf node entry count");
        this.maxLeafNodeEntryCount = maxLeafNodeEntryCount;
    }

    @Override
    public PdfVersion getRequiredPdfVersion() {
        return new RequiredPdfVersionHelper().addPdfVersionRequirements(this.entries.values()).getRequiredPdfVersion();
    }

    public final void readFromPdfObject(DocumentReadingContext context, PdfObject object) {
        final TreeNode rootTreeNode = this.treeNodeFactory.createNode(context, true, object);
        rootTreeNode.readFromPdfObject(context, object);
        this.rootTreeNode = rootTreeNode;
    }

    @Override
    public final void registerNodes(DocumentNodeRegistry registry) {
        if (this.rootTreeNode == null) {
            this.rootTreeNode = this.createTreeNode(this.isIndirect(), new ArrayList<Entry<K, V>>(this.entries.entrySet()));
        }
        this.rootTreeNode.registerNodes(registry);
    }

    public final PdfDictionaryObject createPdfObject(DocumentPdfObjectCreationContext context) {
        assert this.rootTreeNode != null;
        return this.rootTreeNode.createPdfObject(context);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("entries", this.entries).append("maxIntermediateNodeChildCount", this.maxIntermediateNodeChildCount).append("maxLeafNodeEntryCount", this.maxLeafNodeEntryCount).toString();
    }

    protected void validateValue(V value) {
    }

    protected abstract PdfNameObject getEntriesKey();

    protected abstract DocumentNodeFactory<V> getValueFactory();

    protected abstract K readKey(ContainerPdfObjectElement element);

    protected abstract ContainerPdfObjectElement createElement(K key);

    private void modifyEntries() {
        this.modify();
        this.rootTreeNode = null;
    }

    private TreeNode createTreeNode(boolean indirect, List<Entry<K, V>> entries) {
        if (entries.size() > this.maxLeafNodeEntryCount) {
            return this.createIntermediateTreeNode(indirect, entries);
        }
        return this.createLeafTreeNode(indirect, entries);
    }

    private IntermediateTreeNode createIntermediateTreeNode(boolean indirect, List<Entry<K, V>> entries) {
        final int listSize = entries.size();
        int childCount = listSize / this.maxLeafNodeEntryCount;
        if (listSize % this.maxLeafNodeEntryCount != 0) {
            ++childCount;
        }
        if (childCount > this.maxIntermediateNodeChildCount) {
            childCount = this.maxIntermediateNodeChildCount;
        }
        int childSize = listSize / childCount;
        if (listSize % childCount != 0) {
            ++childSize;
        }
        final IntermediateTreeNode intermediateTreeNode = new IntermediateTreeNode(indirect);
        for (int first = 0; first < listSize; first += childSize) {
            final int last = Math.min(first + childSize, listSize);
            intermediateTreeNode.children.add(this.createTreeNode(true, entries.subList(first, last)));
        }
        return intermediateTreeNode;
    }

    private LeafTreeNode createLeafTreeNode(boolean indirect, List<Entry<K, V>> entries) {
        final LeafTreeNode leafTreeNode = new LeafTreeNode(indirect);
        for (final Entry<K, V> entry : entries) {
            leafTreeNode.entries.put(entry.getKey(), entry.getValue());
        }
        return leafTreeNode;
    }
}
