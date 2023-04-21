package bop.datamodel;

import java.util.*;
import bop.util.DNA;

public class FeatureSet extends SpanSet implements FeatureI {

    protected String name = "";

    protected int translate_start, translate_end;

    protected AnnotationI annot;

    protected Vector evidence = new Vector();

    protected String description = "";

    protected Vector history = new Vector();

    protected Hashtable aspects = new Hashtable();

    public FeatureSet() {
        super();
        setType("transcript");
    }

    public FeatureSet(String id) {
        super();
        setType("transcript");
        this.id = id;
    }

    public void makePartOf(AnnotationI annot) {
        this.annot = annot;
    }

    public AnnotationI isPartOf() {
        return this.annot;
    }

    public void setSequence(Sequence seq) {
        if (seq != null) {
            this.seq = seq;
            seq.addFeature(this);
        }
    }

    public String getName() {
        if (name.equals("") && annot != null) return (annot.getFormalName()); else return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String text) {
        this.description = text;
    }

    public String getDescription() {
        return this.description;
    }

    /**
   *  Trying to allow avoidance of edge adjustment when doing 
   *  new-style addition of evidence in CloneCurator
   *  
   *  if adjustments are allowed, then use old way of adding evidence, 
   *    which will (for structural spans) try to adjust edges
   *  otherwise use new way, which doesn't adjust edges (except for 
   *    first piece of evidence)
   */
    public void addSpan(FeatureSpan span) {
        super.addSpan(span);
        span.setParent(this);
    }

    public void addSpan(ResultSpan result_span) {
        FeatureSpan feature_span = null;
        for (int i = 0; i < spans.size() && feature_span == null; i++) {
            FeatureSpan temp_span = (FeatureSpan) spans.elementAt(i);
            if (temp_span.overlaps(result_span)) {
                feature_span = temp_span;
                feature_span.extendWith(result_span);
            }
        }
        if (feature_span == null) {
            feature_span = new FeatureSpan();
            feature_span.setType(type);
            feature_span.setStart(result_span.getStart());
            feature_span.setEnd(result_span.getEnd());
            addSpan(feature_span);
        }
        this.extendWith(feature_span);
        feature_span.addEvidence(result_span, "structural");
    }

    public void addSimilarity(ResultSpan result_span) {
        FeatureSpan feature_span = null;
        for (int i = 0; i < spans.size() && feature_span == null; i++) {
            FeatureSpan temp_span = (FeatureSpan) spans.elementAt(i);
            if (temp_span.overlaps(result_span)) {
                feature_span = temp_span;
                feature_span.addEvidence(result_span, "similarity");
            }
        }
        if (feature_span == null) {
            System.out.println("Didn't add evidence, because it didn't match up with anything");
        }
    }

    public Vector getSpans() {
        return spans;
    }

    public void addEvidence(ResultI resultspan, String type) {
        Evidence e = new Evidence(this);
        e.setResultSpan(resultspan);
        e.setType(type);
        evidence.addElement(e);
    }

    public void addEvidence(EvidenceI e) {
        if (e != null && !evidence.contains(e)) {
            evidence.addElement(e);
        }
    }

    public boolean usesResult(ResultSpan span, Vector uses) {
        boolean used = false;
        for (int i = 0; i < spans.size(); i++) {
            FeatureSpan fs = (FeatureSpan) spans.elementAt(i);
            Vector evidence = fs.getEvidence();
            for (int k = 0; k < evidence.size(); k++) {
                Evidence e = (Evidence) evidence.elementAt(k);
                if (span == e.getResultSpan()) {
                    uses.addElement(fs);
                    used = true;
                }
            }
        }
        return used;
    }

    public Vector getSimilarities() {
        return getEvidence("similarity");
    }

    public Vector getStructure() {
        return getEvidence("structural");
    }

    public Vector getEvidence() {
        return evidence;
    }

    public Vector getEvidence(String type) {
        if (evidence == null || spans == null) {
            return null;
        }
        Vector clues = new Vector();
        for (int i = 0; i < evidence.size(); i++) {
            Evidence e = (Evidence) evidence.elementAt(i);
            String etype = e.getType();
            if (etype != null && etype.equals(type)) clues.addElement(e);
        }
        for (int i = 0; i < spans.size(); i++) {
            FeatureSpan span = (FeatureSpan) spans.elementAt(i);
            Vector span_clues = span.getEvidence(type);
            for (int k = 0; k < span_clues.size(); k++) {
                clues.addElement(span_clues.elementAt(k));
            }
        }
        return clues;
    }

    public void removeEvidence(ResultI resultspan) {
        boolean removed = false;
        int i = 0;
        while (i < evidence.size() && !removed) {
            Evidence e = (Evidence) evidence.elementAt(i);
            ResultI result = (ResultI) e.getResultSpan();
            if (resultspan == result) {
                evidence.removeElementAt(i);
                result.useAsEvidence(false);
                removed = true;
            } else if (result == resultspan.getParent()) {
                evidence.removeElementAt(i);
                result.useAsEvidence(false);
                removed = true;
            }
            i++;
        }
    }

    public void setTranslateStart(int val) {
        if (!withinSpan(val)) {
            val = getStart();
        }
        translate_start = val;
    }

    public void setTranslateEnd(int val) {
        if (!withinSpan(val)) {
            val = getEnd();
        }
        translate_end = val;
    }

    public FeatureSpan getExonOver(int base_number) {
        FeatureSpan enclose = null;
        int i = 0;
        while (enclose == null && i < spans.size()) {
            FeatureSpan fs = (FeatureSpan) spans.elementAt(i);
            if (fs.overlaps(base_number)) enclose = fs;
            i++;
        }
        return enclose;
    }

    public void encompassSpans() {
        setStart(0);
        setEnd(0);
        for (int i = 0; i < spans.size(); i++) {
            FeatureSpan span = (FeatureSpan) spans.elementAt(i);
            if (i == 0) {
                setStart(span.getStart());
                setEnd(span.getEnd());
            } else {
                extendWith(span);
            }
        }
    }

    public void setTranslateStart(String val) {
        setTranslateStart(Integer.parseInt(val));
    }

    public void setTranslateEnd(String val) {
        setTranslateEnd(Integer.parseInt(val));
    }

    public int getTranslateStart() {
        return translate_start;
    }

    public int getTranslateEnd() {
        return translate_end;
    }

    public int transcriptOffset(int base_no) {
        int offset = 0;
        boolean stop = false;
        FeatureSpan previous = null;
        if (base_no != 0 && getExonOver(base_no) != null) {
            if (isForward()) {
                for (int i = 0; i < spans.size() && !stop; i++) {
                    FeatureSpan span = (FeatureSpan) spans.elementAt(i);
                    if (previous == null) {
                        offset = base_no - span.getStart();
                    } else {
                        offset -= (span.getSeqStart() - previous.getSeqEnd()) - 1;
                    }
                    stop = (base_no <= span.getSeqEnd());
                    previous = span;
                }
            } else {
                for (int i = 0; i < spans.size() && !stop; i++) {
                    FeatureSpan span = (FeatureSpan) spans.elementAt(i);
                    if (previous == null) {
                        offset = span.getSeqStart() - base_no;
                    } else {
                        offset -= (previous.getSeqEnd() - span.getSeqStart()) - 1;
                    }
                    stop = (base_no >= span.getSeqEnd());
                    previous = span;
                }
            }
        }
        return offset;
    }

    public String get_cDNA() {
        StringBuffer dna = new StringBuffer();
        for (int i = 0; i < spans.size(); i++) {
            FeatureSpan span = (FeatureSpan) spans.elementAt(i);
            dna.append(seq.getResidues(span.getSeqStart() - 1, span.getSeqEnd() - 1));
        }
        return dna.toString();
    }

    public String getTranslation() {
        String dna = get_cDNA();
        int start_offset = transcriptOffset(translate_start);
        int end_offset = transcriptOffset(translate_end);
        if (end_offset > 0 && end_offset < dna.length()) {
            dna = dna.substring(start_offset, end_offset);
        } else {
            dna = dna.substring(start_offset);
        }
        System.err.println("Translation start (" + start_offset + ") " + "and end (" + end_offset + ") " + "for cDNA of length " + dna.length());
        String aa = (DNA.translate(dna, DNA.FRAME_ONE, DNA.ONE_LETTER_CODE));
        return aa;
    }

    public void addHistory(History h) {
        if (h != null) {
            int index = history.size();
            int insert_position = 0;
            while (index > 0 && insert_position == 0) {
                History old_news = (History) history.elementAt(index - 1);
                if (!old_news.before(h)) insert_position = index;
                index--;
            }
            if (insert_position >= history.size()) history.addElement(h); else history.insertElementAt(h, insert_position);
        }
    }

    public String getDate() {
        String date_str = "";
        if (history.size() > 0) {
            History h = (History) history.elementAt(0);
            date_str = h.getDate().toString();
        }
        return date_str;
    }

    public int getVersion() {
        int version = 1;
        if (history.size() > 0) {
            History h = (History) history.elementAt(0);
            version = h.getVersion();
        }
        return version;
    }

    public void addAspect(Aspect a) {
        Vector a_list = (Vector) aspects.get(a.getType());
        if (a_list == null) {
            a_list = new Vector();
            aspects.put(a.getType(), a_list);
        }
        if (!a_list.contains(a)) {
            a_list.addElement(a);
        }
    }

    public Vector getAspect(String type) {
        return ((Vector) aspects.get(type));
    }

    public String toXML() {
        String indent = "    ";
        String quoter = "\"";
        StringBuffer buf = new StringBuffer();
        buf.append(indent + "<feature_set");
        if (!getID().equals("")) buf.append(" id=" + quoter + getID() + quoter);
        buf.append(" annotation=" + quoter + annot.getFormalName() + quoter + ">\n");
        buf.append(indent + "  <name>" + getName() + "</name>\n");
        buf.append(indent + "  <type>" + getType() + "</type>\n");
        if (history.size() > 0) {
            History h = (History) history.elementAt(0);
            buf.append(indent + "  <version>" + h.getVersion() + "</version>\n");
            buf.append(indent + "  <date>" + h.getDateString() + "</date>\n");
            if (!(h.getAuthor()).equals("")) {
                buf.append(indent + "  <author>" + h.getAuthor() + "</author>\n");
            }
        }
        Enumeration aspect_keys = aspects.keys();
        while (aspect_keys.hasMoreElements()) {
            String type = (String) aspect_keys.nextElement();
            Vector terms = getAspect(type);
            buf.append("annotation aspect type= " + type + "\n");
            for (int i = 0; i < terms.size(); i++) {
                Aspect aspt = (Aspect) terms.elementAt(i);
                buf.append(aspt.toTextString());
            }
        }
        for (int i = 0; i < spans.size(); i++) {
            FeatureSpan fs = (FeatureSpan) spans.elementAt(i);
            buf.append(fs.toXML());
        }
        buf.append(indent + "</feature_set>\n");
        return buf.toString();
    }

    public String toTextString() {
        StringBuffer buf = new StringBuffer();
        buf.append("*** feature set ***\n");
        buf.append("feature set id= " + getID() + "\n");
        buf.append("feature set name= " + getName() + "\n");
        for (int i = 0; i < evidence.size(); i++) {
            Evidence ev = (Evidence) evidence.elementAt(i);
            buf.append(ev.toTextString());
        }
        for (int i = 0; i < spans.size(); i++) {
            FeatureSpan fs = (FeatureSpan) spans.elementAt(i);
            buf.append(fs.toTextString());
        }
        return buf.toString();
    }
}
