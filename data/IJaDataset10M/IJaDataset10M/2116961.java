package com.android.dx.dex.file;

import com.android.dx.rop.annotation.Annotations;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;

/**
 * Set of annotations, where no annotation type appears more than once.
 */
public final class AnnotationSetItem extends OffsettedItem {

    /** the required alignment for instances of this class */
    private static final int ALIGNMENT = 4;

    /** the size of an entry int the set: one {@code uint} */
    private static final int ENTRY_WRITE_SIZE = 4;

    /** {@code non-null;} the set of annotations */
    private final Annotations annotations;

    /**
     * {@code non-null;} set of annotations as individual items in an array.
     * <b>Note:</b> The contents have to get sorted by type id before
     * writing.
     */
    private final AnnotationItem[] items;

    /**
     * Constructs an instance.
     * 
     * @param annotations {@code non-null;} set of annotations
     */
    public AnnotationSetItem(Annotations annotations) {
        super(ALIGNMENT, writeSize(annotations));
        this.annotations = annotations;
        this.items = new AnnotationItem[annotations.size()];
        int at = 0;
        for (Annotation a : annotations.getAnnotations()) {
            items[at] = new AnnotationItem(a);
            at++;
        }
    }

    /**
     * Gets the write size for the given set.
     * 
     * @param annotations {@code non-null;} the set
     * @return {@code > 0;} the write size
     */
    private static int writeSize(Annotations annotations) {
        try {
            return (annotations.size() * ENTRY_WRITE_SIZE) + 4;
        } catch (NullPointerException ex) {
            throw new NullPointerException("list == null");
        }
    }

    /**
     * Gets the underlying annotations of this instance
     * 
     * @return {@code non-null;} the annotations
     */
    public Annotations getAnnotations() {
        return annotations;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return annotations.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    protected int compareTo0(OffsettedItem other) {
        AnnotationSetItem otherSet = (AnnotationSetItem) other;
        return annotations.compareTo(otherSet.annotations);
    }

    /** {@inheritDoc} */
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_ANNOTATION_SET_ITEM;
    }

    /** {@inheritDoc} */
    @Override
    public String toHuman() {
        return annotations.toString();
    }

    /** {@inheritDoc} */
    public void addContents(DexFile file) {
        MixedItemSection byteData = file.getByteData();
        int size = items.length;
        for (int i = 0; i < size; i++) {
            items[i] = byteData.intern(items[i]);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void place0(Section addedTo, int offset) {
        AnnotationItem.sortByTypeIdIndex(items);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeTo0(DexFile file, AnnotatedOutput out) {
        boolean annotates = out.annotates();
        int size = items.length;
        if (annotates) {
            out.annotate(0, offsetString() + " annotation set");
            out.annotate(4, "  size: " + Hex.u4(size));
        }
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            AnnotationItem item = items[i];
            int offset = item.getAbsoluteOffset();
            if (annotates) {
                out.annotate(4, "  entries[" + Integer.toHexString(i) + "]: " + Hex.u4(offset));
                items[i].annotateTo(out, "    ");
            }
            out.writeInt(offset);
        }
    }
}
