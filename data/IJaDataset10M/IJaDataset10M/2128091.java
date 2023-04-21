package de.bioutils.fasta;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.kerner.commons.StringUtils;
import net.sf.kerner.commons.file.FileUtils;
import de.bioutils.symbol.SymbolSequence;

/**
 * <p>
 * description // TODO
 * </p>
 * 
 * <p>
 * example // TODO
 * </p>
 * 
 * @author Alexander Kerner
 * 
 */
public class FASTAElementImpl implements FASTAElement {

    private static final long serialVersionUID = -415616616035451659L;

    private final SymbolSequence seq;

    private final String header;

    private volatile int lineLength = FASTAFile.DEFAULT_LINE_LENGTH;

    private Map<String, Serializable> map = new LinkedHashMap<String, Serializable>();

    public FASTAElementImpl(String header, SymbolSequence seq, Map<String, Serializable> methainfo) {
        if (header == null || seq == null || methainfo == null) throw new NullPointerException();
        this.header = header;
        this.seq = seq;
        this.map.putAll(methainfo);
    }

    public FASTAElementImpl(String header, SymbolSequence seq) {
        if (header == null || seq == null) throw new NullPointerException();
        this.header = header;
        this.seq = seq;
    }

    public String getHeader() {
        return header;
    }

    public String getHeader(boolean includeMethaInfo) {
        if (!includeMethaInfo) return header;
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        for (Entry<String, Serializable> e : getMethaInfo().entrySet()) {
            sb.append("[");
            sb.append(e);
            sb.append("]");
            sb.append(" ");
        }
        return (header + sb.toString()).trim();
    }

    public int getLineLength() {
        return lineLength;
    }

    public SymbolSequence getSequence() {
        return seq;
    }

    public void setLineLength(int length) {
        if (length < 1) throw new NumberFormatException();
        this.lineLength = length;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean includeMethaInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append(FASTAFile.HEADER_IDENT);
        sb.append(getHeader(includeMethaInfo));
        sb.append(FileUtils.NEW_LINE);
        sb.append(StringUtils.formatMultiLineStringToLength(seq.toString(), lineLength));
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((header == null) ? 0 : header.hashCode());
        result = prime * result + ((seq == null) ? 0 : seq.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof FASTAElementImpl)) return false;
        FASTAElementImpl other = (FASTAElementImpl) obj;
        if (header == null) {
            if (other.header != null) return false;
        } else if (!header.equals(other.header)) return false;
        if (seq == null) {
            if (other.seq != null) return false;
        } else if (!seq.equals(other.seq)) return false;
        return true;
    }

    public void clearMethaInfo() {
        map.clear();
    }

    public Serializable getMethaInfo(String ident) {
        if (ident == null) throw new NullPointerException();
        return map.get(ident);
    }

    public Map<String, Serializable> getMethaInfo() {
        return new LinkedHashMap<String, Serializable>(map);
    }

    public void setMethaInfo(String ident, Serializable info) {
        if (ident == null || info == null) throw new NullPointerException();
        map.put(ident, info);
    }

    public void setMethaInfo(Map<String, Serializable> map) {
        if (map == null) throw new NullPointerException();
        this.map.clear();
        this.map.putAll(map);
    }
}
