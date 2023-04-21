package org.mcisb.sbml;

import java.io.*;
import java.util.*;
import org.mcisb.excel.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.eco.*;
import org.mcisb.util.*;
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class SbmlEvidenceWriter {

    /**
	 * 
	 */
    private final SbmlUtils sbmlUtils = new SbmlUtils();

    /**
	 * 
	 * @param sbmlIn
	 * @param excel
	 * @param sbmlOut
	 * @throws Exception
	 */
    public void jamborise(final File sbmlIn, final File excel, final File sbmlOut) throws Exception {
        final SBMLDocument document = sbmlUtils.readSBML(sbmlIn).getDocument();
        final Model model = document.getModel();
        final ExcelReader reader = new ExcelReader(excel);
        final List<List<Object>> data = reader.getData("Sheet1");
        for (final List<Object> row : data) {
            if (row.get(0) == null) {
                break;
            }
            final Reaction reaction = model.getReaction(row.get(0).toString().trim());
            for (String pmid : row.get(1).toString().trim().split(",")) {
                sbmlUtils.addOntologyTerm(reaction, OntologyUtils.getInstance().getOntologyTerm(Ontology.PUBMED, Integer.toString(Float.valueOf(pmid.trim()).intValue())), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_DESCRIBED_BY);
            }
            for (String eco : row.get(2).toString().trim().split(",")) {
                final OntologyTerm ontologyTerm = CollectionUtils.getFirst(EcoUtils.getInstance().search(eco.trim()));
                if (ontologyTerm != null) {
                    sbmlUtils.addOntologyTerm(reaction, ontologyTerm, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_DESCRIBED_BY);
                }
            }
            final String human = row.get(3) == null ? "" : row.get(3).toString().trim();
            if (human.length() > 0 && !reaction.getNotesString().contains("Human Y/N")) {
                System.out.println(reaction.getId());
                System.out.println(reaction.getNotesString());
            }
            final String notes = row.get(4) == null ? "" : row.get(4).toString().trim();
            if (notes.length() > 0 && !reaction.getNotesString().contains(notes)) {
                System.out.println(reaction.getId());
                System.out.println(reaction.getNotesString());
                System.out.println(notes);
                System.out.println();
                System.out.println();
            }
        }
        final File temp = File.createTempFile("temp", ".xml");
        sbmlUtils.writeSBML(new SbmlDocument(document), temp, true, false);
        sbmlUtils.format(temp, sbmlOut, true, false);
    }

    /**
	 * 
	 * @param sbmlIn
	 * @param excel
	 * @param sbmlOut
	 * @throws Exception
	 */
    public void jamborise2(final File sbmlIn, final File excel, final File sbmlOut) throws Exception {
        final SBMLDocument document = sbmlUtils.readSBML(sbmlIn).getDocument();
        final Model model = document.getModel();
        final ExcelReader reader = new ExcelReader(excel);
        final List<List<Object>> data = reader.getData("Sheet1");
        final Map<String, OntologyTerm> reactionIdPrefixToEcoTerm = new HashMap<String, OntologyTerm>();
        final OntologyTerm noEvidence = OntologyUtils.getInstance().getOntologyTerm(Ontology.EVIDENCE_CODE, "ECO:0000000");
        for (final List<Object> row : data) {
            if (row.get(0) == null) {
                break;
            }
            final String reactionIdPrefix = row.get(0).toString().trim();
            for (String eco : row.get(2).toString().trim().split(",")) {
                final OntologyTerm ontologyTerm = EcoUtils.getInstance().getOntologyTerm(eco.trim());
                if (ontologyTerm != null) {
                    reactionIdPrefixToEcoTerm.put(reactionIdPrefix, ontologyTerm);
                }
            }
        }
        for (int l = 0; l < model.getNumReactions(); l++) {
            final Reaction reaction = model.getReaction(l);
            String reactionId = reaction.getId().replaceAll("R_", "");
            int index = reactionId.lastIndexOf("_");
            if (index != -1) {
                reactionId = reactionId.substring(0, index);
            }
            reactionId = "R_" + reactionId;
            final OntologyTerm ecoTerm = reactionIdPrefixToEcoTerm.get(reactionId);
            if (ecoTerm != null) {
                final Map<OntologyTerm, Object[]> allOntologyTerms = sbmlUtils.getOntologyTerms(reaction);
                reaction.unsetAnnotation();
                for (Iterator<Map.Entry<OntologyTerm, Object[]>> iterator = allOntologyTerms.entrySet().iterator(); iterator.hasNext(); ) {
                    if (iterator.next().getKey().equals(noEvidence)) {
                        iterator.remove();
                    }
                }
                allOntologyTerms.put(ecoTerm, new Object[] { CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_DESCRIBED_BY });
                sbmlUtils.addOntologyTerms(reaction, allOntologyTerms);
            }
        }
        final File temp = File.createTempFile("temp", ".xml");
        sbmlUtils.writeSBML(new SbmlDocument(document), temp, true, false);
        sbmlUtils.format(temp, sbmlOut, true, false);
    }

    /**
	 * 
	 * @param sbmlIn
	 * @param excel
	 * @param sbmlOut
	 * @throws Exception
	 */
    public void jamborise3(final File sbmlIn, final File sbmlOut) throws Exception {
        final SBMLDocument document = sbmlUtils.readSBML(sbmlIn).getDocument();
        final Model model = document.getModel();
        checkEvidence(model);
        final File temp = File.createTempFile("temp", ".xml");
        sbmlUtils.writeSBML(new SbmlDocument(document), temp, true, false);
        sbmlUtils.format(temp, sbmlOut, true, false);
    }

    /**
	 * 
	 * @param model
	 * @throws Exception
	 */
    private void checkEvidence(final Model model) throws Exception {
        final OntologyTerm noEvidence = OntologyUtils.getInstance().getOntologyTerm(Ontology.EVIDENCE_CODE, "ECO:0000000");
        final OntologyTerm sanDiego = OntologyUtils.getInstance().getOntologyTerm(Ontology.PUBMED, "17267599");
        for (int l = 0; l < model.getNumSpecies(); l++) {
            final SBase sbase = model.getSpecies(l);
            final Collection<OntologyTerm> ontologyTerms = new TreeSet<OntologyTerm>();
            for (Map.Entry<OntologyTerm, Object[]> entry : sbmlUtils.getOntologyTerms(sbase).entrySet()) {
                if (entry.getKey().getOntologyName().equals(Ontology.EVIDENCE_CODE)) {
                    ontologyTerms.add(entry.getKey());
                } else if (entry.getKey().getOntologyName().equals(Ontology.PUBMED)) {
                    ontologyTerms.add(entry.getKey());
                }
            }
            if (ontologyTerms.size() == 0) {
                sbmlUtils.addOntologyTerm(sbase, noEvidence, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_DESCRIBED_BY);
                sbmlUtils.addOntologyTerm(sbase, sanDiego, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_DESCRIBED_BY);
            }
        }
        for (int l = 0; l < model.getNumReactions(); l++) {
            final NamedSBase sbase = model.getReaction(l);
            final Collection<OntologyTerm> ontologyTerms = new TreeSet<OntologyTerm>();
            for (Map.Entry<OntologyTerm, Object[]> entry : sbmlUtils.getOntologyTerms(sbase).entrySet()) {
                if (entry.getKey().getOntologyName().equals(Ontology.EVIDENCE_CODE)) {
                    ontologyTerms.add(entry.getKey());
                } else if (entry.getKey().getOntologyName().equals(Ontology.PUBMED)) {
                    ontologyTerms.add(entry.getKey());
                }
            }
            if (ontologyTerms.size() == 0) {
                sbmlUtils.addOntologyTerm(sbase, noEvidence, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_DESCRIBED_BY);
                sbmlUtils.addOntologyTerm(sbase, sanDiego, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_DESCRIBED_BY);
            }
            if (!sbase.isSetSBOTerm()) {
                sbase.setSBOTerm(526);
                System.out.println(sbase.getId());
            }
        }
    }

    /**
	 * 
	 * @param args
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        new SbmlEvidenceWriter().jamborise3(new File(args[0]), new File(args[1]));
    }
}
