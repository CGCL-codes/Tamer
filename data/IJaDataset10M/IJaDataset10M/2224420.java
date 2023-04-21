package org.vardb.genomes;

import java.util.ArrayList;
import java.util.List;

public class CLocus {

    protected String m_locus_tag;

    protected String m_version;

    protected String m_fref;

    protected int m_start;

    protected int m_end;

    protected String m_sequence;

    protected String m_protein_id;

    protected Integer m_codon_start;

    protected String m_translation;

    protected String m_protein;

    protected String m_product;

    protected String m_transcript_id;

    protected Integer m_geneid;

    protected Integer m_gi;

    protected String m_uniprot;

    protected String m_goa;

    protected List<Feature> m_features = new ArrayList<Feature>();

    protected List<String> m_notes = new ArrayList<String>();

    protected List<CrossRef> m_crossrefs = new ArrayList<CrossRef>();

    public String getLocus_tag() {
        return m_locus_tag;
    }

    public void setLocus_tag(String locus_tag) {
        m_locus_tag = locus_tag;
    }

    public String getVersion() {
        return m_version;
    }

    public void setVersion(String version) {
        m_version = version;
    }

    public String getFref() {
        return m_fref;
    }

    public void setFref(String fref) {
        m_fref = fref;
    }

    public int getStart() {
        return m_start;
    }

    public void setStart(int start) {
        m_start = start;
    }

    public int getEnd() {
        return m_end;
    }

    public void setEnd(int end) {
        m_end = end;
    }

    public String getSequence() {
        return m_sequence;
    }

    public void setSequence(String sequence) {
        m_sequence = sequence;
    }

    public String getProtein_id() {
        return m_protein_id;
    }

    public void setProtein_id(String protein_id) {
        m_protein_id = protein_id;
    }

    public Integer getCodon_start() {
        return m_codon_start;
    }

    public void setCodon_start(Integer codon_start) {
        m_codon_start = codon_start;
    }

    public String getTranslation() {
        return m_translation;
    }

    public void setTranslation(String translation) {
        m_translation = translation;
    }

    public String getProtein() {
        return m_protein;
    }

    public void setProtein(String protein) {
        m_protein = protein;
    }

    public String getProduct() {
        return m_product;
    }

    public void setProduct(String product) {
        m_product = product;
    }

    public String getTranscript_id() {
        return m_transcript_id;
    }

    public void setTranscript_id(String transcript_id) {
        m_transcript_id = transcript_id;
    }

    public Integer getGeneid() {
        return m_geneid;
    }

    public void setGeneid(Integer geneid) {
        m_geneid = geneid;
    }

    public Integer getGi() {
        return m_gi;
    }

    public void setGi(Integer gi) {
        m_gi = gi;
    }

    public String getUniprot() {
        return m_uniprot;
    }

    public void setUniprot(String uniprot) {
        m_uniprot = uniprot;
    }

    public String getGoa() {
        return m_goa;
    }

    public void setGoa(String goa) {
        m_goa = goa;
    }

    public List<Feature> getFeatures() {
        return m_features;
    }

    public List<CrossRef> getCrossrefs() {
        return m_crossrefs;
    }

    public void add(Feature feature) {
        feature.setStart(feature.getStart() - m_start + 1);
        feature.setEnd(feature.getEnd() - m_start + 1);
        m_features.add(feature);
    }

    public void addCrossref(String name, String value) {
        addCrossref(new CrossRef(name, value));
    }

    public void addCrossref(CrossRef crossref) {
        if (crossref.getName().equals("GeneID")) m_geneid = new Integer(crossref.getValue());
        if (crossref.getName().equals("GI")) m_gi = new Integer(crossref.getValue());
        if (crossref.getName().equals("GOA")) m_goa = crossref.getValue();
        if (crossref.getName().equals("UniProtKB/TrEMBL")) m_uniprot = crossref.getValue();
        m_crossrefs.add(crossref);
    }

    public void addNote(String note) {
        m_notes.add(note);
    }

    public String getNotes() {
        StringBuilder buffer = new StringBuilder();
        for (String note : m_notes) {
            buffer.append(note);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public List<Exon> getExons() {
        List<Exon> exons = new ArrayList<Exon>();
        for (Feature feature : m_features) {
            if (feature instanceof Exon) exons.add((Exon) feature);
        }
        return exons;
    }

    public static class Feature {

        protected int m_start;

        protected int m_end;

        public int getStart() {
            return m_start;
        }

        public void setStart(int start) {
            m_start = start;
        }

        public int getEnd() {
            return m_end;
        }

        public void setEnd(int end) {
            m_end = end;
        }
    }

    public static class Exon extends Feature {
    }

    public static class CrossRef {

        protected String m_name;

        protected String m_value;

        public CrossRef(String name, String value) {
            m_name = name;
            m_value = value;
        }

        public String getName() {
            return m_name;
        }

        public void setName(String name) {
            m_name = name;
        }

        public String getValue() {
            return m_value;
        }

        public void setValue(String value) {
            m_value = value;
        }
    }
}
