package edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom;

import org.w3c.dom.NodeList;
import edu.mit.lcs.haystack.Constants;
import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.Literal;
import edu.mit.lcs.haystack.rdf.RDFException;
import edu.mit.lcs.haystack.rdf.Resource;
import edu.mit.lcs.haystack.rdf.Statement;
import edu.mit.lcs.haystack.rdf.Utilities;
import edu.mit.lcs.haystack.server.extensions.wrapperinduction.WrapperManager;

/**
 * A Range represents a contiguous subset of the children of a node.
 */
public class Range {

    public int start;

    public int end;

    public Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getSize() {
        return this.end - this.start + 1;
    }

    public String toString() {
        return (this.getSize() == 1) ? String.valueOf(this.start) : "[" + this.start + "-" + this.end + "]";
    }

    public static Range fromString(String rangeString) {
        Range r;
        if (rangeString.startsWith("[")) {
            String cleanStr = rangeString.substring(1, rangeString.length() - 1);
            String startend[] = cleanStr.split("-");
            int start = Integer.parseInt(startend[0]);
            int end = Integer.parseInt(startend[1]);
            r = new Range(start, end);
        } else {
            int ends = Integer.parseInt(rangeString);
            r = new Range(ends, ends);
        }
        return r;
    }

    /**
     * Retrieves the children in this range of the given parent. Returns an
     * empty array if: end < start start < 0 end > length-1
     */
    public INode[] getChildRange(INode parent) {
        NodeList children = parent.getChildNodes();
        if (this.end < this.start || this.start < 0 || this.end > children.getLength() - 1) {
            return new INode[0];
        }
        INode[] childRange = new INode[this.end - this.start + 1];
        for (int i = this.start; i <= this.end; i++) {
            childRange[i - this.start] = (INode) children.item(i);
        }
        return childRange;
    }

    /**
     * This range contains the given range if the start and end points of the
     * given range are within (inclusive) the start and end points of this
     * range.
     */
    public boolean contains(Range other) {
        return (this.start <= other.start && this.end >= other.end);
    }

    public boolean contains(int siblingNo) {
        return (siblingNo >= this.start && siblingNo <= this.end);
    }

    /**
     * Makes a new range relative to this one. The other range must be contained
     * by this one.
     */
    public Range makeRelativeRange(Range other) {
        if (!this.contains(other)) return null;
        return new Range(other.start - this.start, other.end - this.start);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Range)) return false;
        return (((Range) o).start == this.start && ((Range) o).end == this.end);
    }

    public Object clone() {
        return new Range(this.start, this.end);
    }

    public Resource makeResource(IRDFContainer rdfc) throws RDFException {
        Resource rangeRes = Utilities.generateUniqueResource();
        rdfc.add(new Statement(rangeRes, Constants.s_rdf_type, WrapperManager.RANGE_CLASS));
        rdfc.add(new Statement(rangeRes, WrapperManager.RANGE_START_PROP, new Literal(String.valueOf(this.start))));
        rdfc.add(new Statement(rangeRes, WrapperManager.RANGE_END_PROP, new Literal(String.valueOf(this.end))));
        return rangeRes;
    }

    public static Range fromResource(Resource rangeRes, IRDFContainer rdfc) throws RDFException {
        return new Range(Integer.parseInt(rdfc.extract(rangeRes, WrapperManager.RANGE_START_PROP, null).getContent()), Integer.parseInt(rdfc.extract(rangeRes, WrapperManager.RANGE_END_PROP, null).getContent()));
    }
}
