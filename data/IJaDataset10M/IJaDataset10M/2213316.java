package relex.output;

import java.util.HashMap;
import java.util.UUID;
import relex.frame.Frame;
import relex.ParsedSentence;

/**
 * Implements OpenCog XML output of the semantic frames.
 *
 * Copyright (c) 2008 Linas Vepstas <linas@linas.org>
 */
public class FrameXML {

    private ParsedSentence sent;

    private HashMap<String, String> id_map = null;

    private Frame frame;

    public FrameXML() {
        sent = null;
        id_map = null;
        frame = new Frame();
    }

    public void setParse(ParsedSentence s, HashMap<String, String> im) {
        sent = s;
        id_map = im;
    }

    private String printFrames() {
        String ret = "";
        String fin = SimpleView.printRelationsAlt(sent.getLeft());
        String[] fms = frame.process(fin);
        for (String fm : fms) {
            if (fm.charAt(0) != '^') continue;
            int uscore = fm.indexOf('_');
            if (0 > uscore) continue;
            fm = fm.substring(uscore + 1);
            int colon = fm.indexOf(':');
            if (0 > colon) continue;
            String frm = fm.substring(0, colon);
            fm = fm.substring(colon + 1);
            int lparen = fm.indexOf('(');
            if (0 > lparen) continue;
            String felt = fm.substring(0, lparen);
            fm = fm.substring(lparen + 1);
            int rparen = fm.indexOf(')');
            if (0 > rparen) continue;
            String cpt1 = fm.substring(0, rparen);
            String cpt2 = null;
            ret += "<!-- " + frm + ":" + felt + "(" + cpt1 + ") -->\n";
            int comma = cpt1.indexOf(',');
            if (0 < comma) {
                cpt2 = cpt1.substring(comma + 1);
                cpt1 = cpt1.substring(0, comma);
            }
            if (cpt1 == null) continue;
            Boolean cpt1_is_ling = false;
            Boolean cpt2_is_ling = false;
            if (cpt1.charAt(0) == '#') cpt1_is_ling = true;
            if (cpt2 != null) {
                cpt2 = id_map.get(cpt2);
                if (cpt2 == null) continue;
                if (cpt2.charAt(0) == '#') cpt2_is_ling = true;
            }
            ret += "  <DefinedFrameNode name=\"#" + frm + "\"/>\n";
            ret += "  <DefinedFrameElementNode name=\"#" + frm + ":" + felt + "\"/>\n";
            ret += "  <FrameElementLink>\n";
            ret += "    <Element class=\"DefinedFrameNode\" name=\"#" + frm + "\"/>\n";
            ret += "    <Element class=\"DefinedFrameElementNode\" name=\"#" + frm + ":" + felt + "\"/>\n";
            ret += "  </FrameElementLink>\n";
            ret += "  <InheritanceLink>\n";
            if (cpt1_is_ling) {
                ret += "    <Element class=\"DefinedLinguisticConceptNode\" name=\"" + cpt1 + "\"/>\n";
            } else {
                ret += "    <Element class=\"ConceptNode\" name=\"" + cpt1 + "\"/>\n";
            }
            ret += "    <Element class=\"DefinedFrameNode\" name=\"#" + frm + "\"/>\n";
            ret += "  </InheritanceLink>\n";
            if (cpt2 == null) continue;
            UUID guid = UUID.randomUUID();
            ret += "  <ListLink name=\"" + frm + "_" + guid + "\">\n";
            if (cpt1_is_ling) {
                ret += "    <Element class=\"DefinedLinguisticConceptNode\" name=\"" + cpt1 + "\"/>\n";
            } else {
                ret += "    <Element class=\"ConceptNode\" name=\"" + cpt1 + "\"/>\n";
            }
            if (cpt2_is_ling) {
                ret += "    <Element class=\"DefinedLinguisticConceptNode\" name=\"" + cpt2 + "\"/>\n";
            } else {
                ret += "    <Element class=\"ConceptNode\" name=\"" + cpt2 + "\"/>\n";
            }
            ret += "  </ListLink>\n";
            ret += "  <EvaluationLink>\n";
            ret += "    <Element class=\"DefinedFrameElementNode\" name=\"#" + frm + ":" + felt + "\"/>\n";
            ret += "    <Element class=\"ListLink\" name=\"" + frm + "_" + guid + "\"/>\n";
            ret += "  </EvaluationLink>\n";
        }
        return ret;
    }

    public String toString() {
        String ret = "";
        ret += "<list>\n";
        ret += printFrames();
        ret += "</list>\n";
        return ret;
    }
}
