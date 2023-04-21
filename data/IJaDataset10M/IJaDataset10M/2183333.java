package ti.targetinfo.td;

import java.util.Hashtable;
import ti.exceptions.ProgrammingErrorException;
import ti.mcore.u.log.PlatoLogger;

/**
 * A padding rule is used by the <code>PrimitiveDecoder</code> to help extract
 * primitives from raw data and pack primitives into raw data.  The layout of the
 * primitive within the raw data depends on the padding rules used by the compiler
 * of the transceiver software.  In order to properly extract primitives from raw
 * data or pack primitives into raw data, the <code>PrimitiveDecoder</code> needs
 * to know the padding-rule used by the compiler.
 * 
 * @author Rob Clark
 */
public abstract class PaddingRule {

    private static final PlatoLogger LOGGER = PlatoLogger.getLogger(PaddingRule.class);

    /**
   * Each padding rule class has a unique index number, which is used to compute
   * the position of cached values within ComplexTypeDescriptor's cache.  Thie
   * ensures that a cached value computed with one PaddingRule is not inadvertantly
   * used by another PaddingRule. 
   */
    private int pridx = getPaddingRuleIndex(getClass());

    /**
   * The visitor pattern is supported by the type descriptors as a convenient
   * mechanism for parsing a type-descriptor tree in order to compute padding.
   */
    public interface TypeDescriptorVisitor {

        public void visit(BasicTypeDescriptor td);

        public void visit(ArrayTypeDescriptor td);

        public void visit(PointerTypeDescriptor td);

        public void visit(StructTypeDescriptor td);

        public void visit(UnionTypeDescriptor td);

        public void visit(FunctionTypeDescriptor td);
    }

    public static final BasicTypeDescriptor BASIC_TYPE_4 = new BasicTypeDescriptor(BasicTypeDescriptor.UNSIGNED, 4);

    public static final BasicTypeDescriptor BASIC_TYPE_2 = new BasicTypeDescriptor(BasicTypeDescriptor.UNSIGNED, 2);

    public static final BasicTypeDescriptor BASIC_TYPE_1 = new BasicTypeDescriptor(BasicTypeDescriptor.UNSIGNED, 1);

    public static final BasicTypeDescriptor BASIC_TYPE_0 = new BasicTypeDescriptor(BasicTypeDescriptor.VOID, 0);

    /**
   * no-padding rule used for packed structs/unions
   */
    public static final PaddingRule getDefaultPaddingRule() {
        if (_defaultPaddingRule == null) _defaultPaddingRule = new DefaultPaddingRule();
        return _defaultPaddingRule;
    }

    private static PaddingRule _defaultPaddingRule;

    /**
   * Figure out how much padding is inserted by the compiler before the next field.
   * This will depend on the type of the next field, as described by the type-
   * descriptor, and on the current position in the raw data (<code>bytePtr</code>).
   * 
   * @param bytePtr     the current position within the primitive
   * @param td          the type descriptor
   * @return the number of padding bytes the compiler inserted before the next field
   */
    public abstract int prePadding(int bytePtr, TypeDescriptor td);

    /**
   * Figure out how much padding is inserted by the compiler after the next field.
   * This will depend on the type of the next field, as described by the type-
   * descriptor, and on the current position in the raw data (<code>bytePtr</code>).
   * 
   * @param bytePtr     the current position within the primitive
   * @param td          the type descriptor
   * @return the number of padding bytes the compiler inserted before the next field
   */
    public abstract int postPadding(int bytePtr, TypeDescriptor td);

    /**
   * Get the basic type which is equivalent in size to a pointer.  Normally this
   * would be a {@link #BASIC_TYPE_4}, but this method should be overriden in
   * cases of an architecture that doesn't have 32bit pointers.
   */
    public BasicTypeDescriptor getPointerType() {
        return BASIC_TYPE_4;
    }

    /**
   * Get the storage size for an enum type.  The default implementation of this 
   * should be sufficient for most compilers.  It takes the minimum enum size
   * (see {@link #getMinimumEnumSize}) and increases it until it is large
   * enough to accomodate the largest enum value.  So most padding rules should
   * just overload {@link #getMinimumEnumSize} if necessary.
   */
    public final int getEnumSize(EnumTypeDescriptor etd) {
        int sz = getMinimumEnumSize();
        for (String symbol : etd.getSymbols()) {
            long val = etd.getEnumValue(symbol);
            while (val > Math.pow(2L, sz)) sz *= 2;
        }
        return sz;
    }

    /**
   * This can be overloaded if the minimum size is anything other than one byte
   */
    protected int getMinimumEnumSize() {
        return BASIC_TYPE_1.getSize(this);
    }

    /**
   * Figure out the padded length of a "struct".  This takes as an argument an
   * a type-descriptor, and returns the size of the type, including padding.
   * 
   * @param td          the type descriptor
   * @return the padded size of the "struct"
   */
    public int computePaddedLength(TypeDescriptor td) {
        PaddingRule pr = this;
        if ((td instanceof ComplexTypeDescriptor) && ((ComplexTypeDescriptor) td).isPacked()) pr = getDefaultPaddingRule();
        return computePaddedLength(pr, td);
    }

    private static final int computePaddedLength(final PaddingRule pr, TypeDescriptor td) {
        synchronized (td) {
            int result = pr.getCachedLength(td);
            if (result == -1) {
                final int[] length = new int[1];
                td.accept(new TypeDescriptorVisitor() {

                    public void visit(BasicTypeDescriptor td) {
                        length[0] += pr.prePadding(length[0], td);
                        length[0] += td.getSize(pr);
                        length[0] += pr.postPadding(length[0], td);
                    }

                    public void visit(ArrayTypeDescriptor td) {
                        length[0] += pr.prePadding(length[0], td);
                        for (int i = 0; i < td.getLength(); i++) td.getType().accept(this);
                        length[0] += pr.postPadding(length[0], td);
                    }

                    public void visit(PointerTypeDescriptor td) {
                        pr.getPointerType().accept(this);
                    }

                    public void visit(StructTypeDescriptor td) {
                        if (td.isPacked() && (pr != getDefaultPaddingRule())) {
                            length[0] += computePaddedLength(getDefaultPaddingRule(), td);
                        } else {
                            length[0] += pr.prePadding(length[0], td);
                            for (int i = 0; i < td.getMemberCount(); i++) td.getMemberType(i).accept(this);
                            length[0] += pr.postPadding(length[0], td);
                        }
                    }

                    public void visit(UnionTypeDescriptor td) {
                        if (td.isPacked() && (pr != getDefaultPaddingRule())) {
                            length[0] += computePaddedLength(getDefaultPaddingRule(), td);
                        } else {
                            int max = 0;
                            for (int i = 0; i < td.getMemberCount(); i++) max = Math.max(max, computePaddedLength(pr, td.getMemberType(i)));
                            length[0] += pr.prePadding(length[0], td);
                            length[0] += max;
                            length[0] += pr.postPadding(length[0], td);
                        }
                    }

                    public void visit(FunctionTypeDescriptor td) {
                        pr.getPointerType().accept(this);
                    }
                });
                result = length[0];
                LOGGER.dbg(pr.pridx + ": cached length miss: " + result);
                pr.setCachedLength(td, result);
            }
            return result;
        }
    }

    /**
   * Figure out the padded offset of a field of a "struct" or "union".  This
   * takes as an argument a type descriptor, and returns the offset from the
   * start of the struct of the specified member, taking into account padding.
   * 
   * @param td       the type descriptor
   * @param name     the name of the field
   * @return the padded offset into the struct of the specified member
   */
    public int computePaddedOffset(ComplexTypeDescriptor td, String name) {
        if (td.isPacked()) return computePaddedOffset(getDefaultPaddingRule(), td, name);
        return computePaddedOffset(this, td, name);
    }

    private static final int computePaddedOffset(PaddingRule pr, ComplexTypeDescriptor td, String name) {
        if (td instanceof UnionTypeDescriptor) return 0;
        synchronized (td) {
            int idx = td.getMemberIdx(name);
            if (idx == -1) throw new ProgrammingErrorException(name + " is not a member of this struct!");
            int length = pr.getCachedMemberOffset(td, idx);
            if (length == -1) {
                length = 0;
                for (int i = 0; ; i++) {
                    TypeDescriptor memberTD = td.getMemberType(i);
                    PaddingRule memberPaddingRule = pr;
                    if ((memberTD instanceof StructTypeDescriptor) && ((StructTypeDescriptor) memberTD).isPacked()) memberPaddingRule = getDefaultPaddingRule();
                    length += memberPaddingRule.prePadding(length, memberTD);
                    if (i >= idx) break;
                    length += memberPaddingRule.computePaddedLength(memberTD);
                    length += memberPaddingRule.postPadding(length, memberTD);
                }
                LOGGER.dbg(pr.pridx + ": cache offset miss: " + td.getMemberName(idx) + ": " + length);
                pr.setCachedMemberOffset(td, idx, length);
            }
            return length;
        }
    }

    /**
   * Set a cached padded offset.
   * 
   * @param ctd   the complex type descriptor
   * @param idx   the member index for padded offset
   * @return the cached value, or <code>-1</code> if not cached
   */
    private final int getCachedMemberOffset(ComplexTypeDescriptor ctd, int idx) {
        int off = idx + (pridx * ctd.getMemberCount());
        if (off >= ctd.cachedOffsets.length) return -1;
        return ctd.cachedOffsets[off];
    }

    /**
   * Set a cached padded offset.  Note: call synchronized on <code>ctd</code>.
   * 
   * @param ctd   the complex type descriptor
   * @param idx   the member index for padded offset
   * @param val   the value to cache
   */
    private final void setCachedMemberOffset(ComplexTypeDescriptor ctd, int idx, int val) {
        int off = idx + (pridx * ctd.getMemberCount());
        if (off >= ctd.cachedOffsets.length) {
            int n = lastPaddingRuleIdx * ctd.getMemberCount();
            int[] tmp = new int[n];
            System.arraycopy(ctd.cachedOffsets, 0, tmp, 0, ctd.cachedOffsets.length);
            for (int i = ctd.cachedOffsets.length; i < n; i++) tmp[i] = -1;
            ctd.cachedOffsets = tmp;
        }
        ctd.cachedOffsets[off] = val;
    }

    /**
   * Set a cached padded offset.
   * 
   * @param td    the complex type descriptor
   * @return the cached value, or <code>-1</code> if not cached
   */
    private final int getCachedLength(TypeDescriptor td) {
        if (pridx >= td.cachedLengths.length) return -1;
        return td.cachedLengths[pridx];
    }

    /**
   * Set a cached padded offset.  Note: call synchronized on <code>td</code>.
   * 
   * @param td    the complex type descriptor
   * @param val   the value to cache
   */
    private final void setCachedLength(TypeDescriptor td, int val) {
        if (pridx >= td.cachedLengths.length) {
            int n = lastPaddingRuleIdx + 1;
            int[] tmp = new int[n];
            System.arraycopy(td.cachedLengths, 0, tmp, 0, td.cachedLengths.length);
            for (int i = td.cachedLengths.length; i < n; i++) tmp[i] = -1;
            td.cachedLengths = tmp;
        }
        td.cachedLengths[pridx] = val;
    }

    private static int lastPaddingRuleIdx;

    private static Hashtable class2idx;

    private static synchronized int getPaddingRuleIndex(Class cls) {
        if (class2idx == null) class2idx = new Hashtable();
        Integer idx = (Integer) (class2idx.get(cls));
        if (idx == null) {
            idx = new Integer(lastPaddingRuleIdx++);
            class2idx.put(cls, idx);
            LOGGER.dbg("*** pridx: " + idx + " (" + cls.getName() + ")");
        }
        return idx.intValue();
    }

    public String toString() {
        String name = getClass().getName();
        int idx = name.lastIndexOf('.');
        if (idx != -1) name = name.substring(idx + 1);
        return name;
    }
}
