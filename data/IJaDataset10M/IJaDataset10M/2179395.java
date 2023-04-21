package de.bioutils.fasta.impl;

import java.io.Serializable;
import java.util.Map;
import net.sf.kerner.commons.StringUtils;
import net.sf.kerner.commons.Utils;
import net.sf.kerner.commons.io.IOUtils;
import de.bioutils.fasta.FASTAElementSymbolSequence;
import de.bioutils.fasta.FASTAFile;
import de.bioutils.symbol.SymbolSequence;

/**
 * 
 *
 * Default implementation for {@link FASTAElementSymbolSequence}.
 * 
 * <p>
 * <b>Example:</b>
 * <pre>
 * TODO example
 * </pre>
 * </p>
 *
 * @author <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version 2010-10-16
 *
 */
public class FASTAElementSymbolSequenceImpl extends AbstractFASTAElement<SymbolSequence> implements FASTAElementSymbolSequence {

    private static final long serialVersionUID = -415616616035451659L;

    /**
	 * This {@link FASTAElementSymbolSequenceImpl}'s sequence.
	 */
    protected final SymbolSequence seq;

    /**
	 * 
	 * 
	 * TODO description
	 *
	 * @param header
	 * @param methainfo
	 * @param seq
	 */
    public FASTAElementSymbolSequenceImpl(String header, Map<String, Serializable> methainfo, SymbolSequence seq) {
        super(header, methainfo);
        Utils.checkForNull(seq);
        this.seq = seq;
    }

    /**
	 * 
	 * 
	 * TODO description
	 *
	 * @param header
	 * @param seq
	 */
    public FASTAElementSymbolSequenceImpl(String header, SymbolSequence seq) {
        super(header);
        Utils.checkForNull(seq);
        this.seq = seq;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((seq == null) ? 0 : seq.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof FASTAElementSymbolSequenceImpl)) return false;
        FASTAElementSymbolSequenceImpl other = (FASTAElementSymbolSequenceImpl) obj;
        if (seq == null) {
            if (other.seq != null) return false;
        } else if (!seq.equals(other.seq)) return false;
        return true;
    }

    public SymbolSequence getSequence() {
        return seq;
    }

    public int getSequenceLength() {
        return getSequence().getLength();
    }

    public String toString(boolean includeMethaInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append(FASTAFile.HEADER_IDENT);
        sb.append(getHeader(includeMethaInfo));
        sb.append(IOUtils.NEW_LINE_STRING);
        sb.append(StringUtils.formatStringToMultiLinesStrings(seq.toString(), lineLength));
        return sb.toString();
    }
}
