package org.moltools.apps.probemaker.seq.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.moltools.apps.probemaker.ProbeMakerConstants;
import org.moltools.apps.probemaker.seq.Probe;
import org.moltools.design.data.Target;
import org.moltools.design.data.impl.DefaultPropertySet;
import org.moltools.design.data.impl.SimpleProperty;
import org.moltools.design.properties.MutablePropertySet;
import org.moltools.design.properties.PropertySet;
import org.moltools.design.utils.DesignUtils;
import org.moltools.lib.seq.NucleotideSequence;
import org.moltools.lib.seq.SequenceEdit;
import org.moltools.lib.seq.SequenceView;
import org.moltools.lib.seq.impl.SimpleChangeableNucleotideSequence;

/**
 * A class for representing a probe. Different types
 * of probes, e.g. Padlock Probes, ould extend this class. Since this class
 * inherits from the sequence class, it may be handled as a sequence. This class
 * also has fields and methods specifically used for probes. This class
 * currently does not support methods of the sequence class that change the
 * sequence, but only the methods that return the sequence data in some way
 * without changing it.<br>
 * Common properties of probes are that they have a target, two probe arms
 * (5' and 3') and a number of tags. These tags may be internal (between the
 * probe arms) or external, as specified by the subclass.<br>
 * Probes also have a structure and a quality.</p>
 * 
 * @author Johan Stenberg
 * @version 1.0
 */
public abstract class AbstractProbe extends SimpleChangeableNucleotideSequence implements Probe, ProbeMakerConstants, SequenceView {

    /**The number of tags (blocks) between the 5' and 3' arms*/
    protected int downstreamBlocks;

    /**The Tags in this probe (5'->3' order)*/
    protected List<NucleotideSequence> tags;

    /**Whether tags have been allocated to this probe. This is used to determine
   * the way to check the probe as a possible ligation template of other probes.*/
    protected boolean tagsAllocated;

    /**The DefaultTSSPair that this probe contains*/
    protected TSSPair tssPair;

    /**The number of tags upstream of the 5' arm*/
    protected int upstreamBlocks;

    protected MutablePropertySet mps = new DefaultPropertySet();

    /**Creates a new probe*/
    public AbstractProbe(TSSPair pair, byte type) {
        super("", type);
        tags = new ArrayList<NucleotideSequence>();
        tagsAllocated = false;
        setTSSPair(pair);
    }

    public void addTag(NucleotideSequence tag) {
        tags.add(tag);
        sequence.replace(0, sequence.length(), composeSequence());
    }

    public void addTagAt(NucleotideSequence tag, int pos) {
        tags.add(pos, tag);
        sequence.replace(0, sequence.length(), composeSequence());
    }

    public PropertySet getPropertySet() {
        return getMutablePropertySet();
    }

    public MutablePropertySet getMutablePropertySet() {
        return mps;
    }

    /**Overridden to throw an UnsupportedOperationException, as editing of Probe objects is ot allowed*/
    @Override
    public void edit(SequenceEdit edit) {
        throw new UnsupportedOperationException("Operation not supported for probes");
    }

    public abstract int getBlockNoOfTag(int tagno);

    public int getDownstreamBlockCount() {
        return downstreamBlocks;
    }

    public int getEndOfSequence(int blockno) {
        if (blockno < 1 || blockno > getSequenceCount()) {
            throw new IllegalArgumentException("No such block (" + blockno + ")");
        }
        int currentpos = 0;
        for (int i = 1; i <= blockno; i++) {
            currentpos += getSequence(i).length();
        }
        return currentpos;
    }

    public int getSequenceCount() {
        return getTags().size() + 2;
    }

    public int getSequenceNumberAt(int pos) {
        if (pos < 1 || pos > length()) {
            throw new IllegalArgumentException("Position out of bounds: " + pos + "/" + length());
        }
        int currentpos = 0;
        for (int i = 1; i <= getSequenceCount(); i++) {
            currentpos += getSequence(i).length();
            if (pos <= currentpos) {
                return i;
            }
        }
        throw new IllegalArgumentException("Position not found: " + pos + "/" + length());
    }

    public List<NucleotideSequence> getSequences() {
        NucleotideSequence[] seqs = new NucleotideSequence[2 + tags.size()];
        for (int i = 0; i < seqs.length; i++) seqs[i] = getSequence(i + 1);
        return Arrays.asList(seqs);
    }

    public int getStartOfSequence(int blockno) {
        if (blockno < 1 || blockno > getSequenceCount()) {
            throw new IllegalArgumentException("No such block (" + blockno + ")");
        }
        int currentpos = 1;
        for (int i = 1; i < blockno; i++) {
            currentpos += getSequence(i).length();
        }
        return currentpos;
    }

    public NucleotideSequence getTagAt(int pos) {
        return tags.get(pos);
    }

    public abstract int getTagNoOfBlock(int blockno);

    public List<NucleotideSequence> getTags() {
        return tags;
    }

    public Target getTarget() {
        return tssPair.getTarget();
    }

    public TSSPair getTSSPair() {
        return tssPair;
    }

    public int getUpstreamBlockCount() {
        return upstreamBlocks;
    }

    public String getViewName() {
        return getName();
    }

    public String getViewSequence() {
        String view = "";
        for (int i = 1; i <= getSequenceCount(); i++) {
            if (i > 1) view += BLOCK_SEPARATOR;
            view += getSequence(i).seqString();
        }
        return view;
    }

    /**Overridden to take the blocks into account*/
    @Override
    public int length() {
        int length = 0;
        for (int i = 1; i <= getSequenceCount(); i++) {
            length += getSequence(i).length();
        }
        return length;
    }

    public void removeAllTags() {
        tags.clear();
        sequence.replace(0, sequence.length(), composeSequence());
    }

    public void removeTagAt(int pos) {
        tags.remove(pos);
        sequence.replace(0, sequence.length(), composeSequence());
    }

    public void setTagsAllocated(boolean alloc) {
        tagsAllocated = alloc;
    }

    public boolean tagsAllocated() {
        return tagsAllocated;
    }

    /**Assemble the parts (subsequences) of the Probe*/
    protected String composeSequence() {
        StringBuffer sequencestring = new StringBuffer();
        for (int i = 1; i <= getSequenceCount(); i++) {
            NucleotideSequence si = getSequence(i);
            sequencestring.append(si == null ? "" : si.seqString());
        }
        return sequencestring.toString();
    }

    /**Set the TSSPair of this probe*/
    protected void setTSSPair(TSSPair pair) {
        tssPair = pair;
        String targetID = pair == null ? null : DesignUtils.getTargetID(pair);
        mps.setProperty(new SimpleProperty(KEY_TARGET_ID, targetID, false));
        sequence.replace(0, sequence.length(), composeSequence());
    }
}
