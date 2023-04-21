package org.mcisb.subliminal.metacyc;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.chebi.*;
import org.mcisb.ontology.kegg.*;
import org.mcisb.util.chem.*;
import org.mcisb.util.task.*;
import org.mcisb.util.xml.*;

/**
 * @author Neil Swainston
 */
class MetaCycCompoundsParser extends AbstractTask {

    /**
	 * 
	 */
    private final File file;

    /**
	 * @param file 
	 */
    public MetaCycCompoundsParser(final File file) {
        this.file = file;
    }

    @Override
    protected Serializable doTask() throws Exception {
        final String UNIQUE_ID = "UNIQUE-ID - ";
        final String DBLINKS = "DBLINKS - (";
        final String DBLINKS_REGEXP = "DBLINKS - \\(";
        final String CHEMICAL_FORMULA = "CHEMICAL-FORMULA - (";
        final String CHEMICAL_FORMULA_REGEXP = "CHEMICAL-FORMULA - \\(";
        final String COMMON_NAME = "COMMON-NAME -";
        final String SYNONYMS = "SYNONYMS -";
        final String SEPARATOR = "//";
        final String EMPTY_STRING = "";
        final int DB = 0;
        final int ID = 1;
        final int ELEMENT = 0;
        final int STOICHIOMETRY = 1;
        final HashMap<String, Collection<OntologyTerm>> idToOntologyTerms = new HashMap<String, Collection<OntologyTerm>>();
        Collection<OntologyTerm> ontologyTerms = null;
        Map<String, Integer> chemicalComposition = null;
        Collection<String> synonyms = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()));
            String line = null;
            String id = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(UNIQUE_ID)) {
                    ontologyTerms = new LinkedHashSet<OntologyTerm>();
                    chemicalComposition = new TreeMap<String, Integer>();
                    synonyms = new LinkedHashSet<String>();
                    id = line.replaceAll(UNIQUE_ID, EMPTY_STRING);
                    idToOntologyTerms.put(id, ontologyTerms);
                    synonyms.add(id);
                } else if (line.startsWith(DBLINKS)) {
                    final String[] terms = line.replaceAll(DBLINKS_REGEXP, EMPTY_STRING).split("\\s|\\)");
                    final Ontology ontology = OntologyFactory.getOntology(terms[DB].replaceAll("CHEBI", Ontology.CHEBI).replaceAll("LIGAND-CPD", Ontology.KEGG_COMPOUND));
                    if (ontology != null && !ontology.getName().equals(Ontology.PUBCHEM_COMPOUND) && !ontology.getName().equals(Ontology.PUBCHEM_SUBSTANCE)) {
                        final OntologyTerm ontologyTerm = OntologyUtils.getInstance().getOntologyTerm(ontology.getName(), terms[ID].replaceAll("\"", EMPTY_STRING));
                        if (ontologyTerms != null && ontologyTerm != null) {
                            ontologyTerms.add(ontologyTerm);
                        }
                    }
                } else if (line.startsWith(CHEMICAL_FORMULA)) {
                    final String[] terms = line.replaceAll(CHEMICAL_FORMULA_REGEXP, EMPTY_STRING).replaceAll("\\)", EMPTY_STRING).split("\\s|\\)");
                    if (chemicalComposition != null) {
                        chemicalComposition.put(terms[ELEMENT], Integer.valueOf(terms[STOICHIOMETRY]));
                    }
                } else if (line.startsWith(COMMON_NAME) || line.startsWith(SYNONYMS)) {
                    if (synonyms != null) {
                        synonyms.add(XmlUtils.stripTags(line.replaceAll(COMMON_NAME, EMPTY_STRING).replaceAll(SYNONYMS, EMPTY_STRING).trim()));
                    }
                } else if (line.startsWith(SEPARATOR)) {
                    if (ontologyTerms != null && synonyms != null && ontologyTerms.size() == 0) {
                        for (final String synonym : synonyms) {
                            for (final OntologyTerm ontologyTerm : ChebiUtils.getInstance().search(synonym)) {
                                ontologyTerms.add(ontologyTerm);
                            }
                        }
                    }
                    if (synonyms != null) {
                        validate(ontologyTerms, chemicalComposition, synonyms);
                    }
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return idToOntologyTerms;
    }

    /**
	 * 
	 * @param ontologyTerms
	 * @param chemicalComposition
	 * @param metaCycSynonyms 
	 * @throws Exception 
	 */
    private void validate(final Collection<OntologyTerm> ontologyTerms, final Map<String, Integer> chemicalComposition, final Collection<String> metaCycSynonyms) throws Exception {
        for (Iterator<OntologyTerm> iterator = ontologyTerms.iterator(); iterator.hasNext(); ) {
            try {
                final OntologyTerm ontologyTerm = iterator.next();
                String formula = null;
                if (ontologyTerm instanceof ChebiTerm) {
                    formula = ((ChebiTerm) ontologyTerm).getFormula();
                } else if (ontologyTerm instanceof KeggCompoundTerm) {
                    formula = ((KeggCompoundTerm) ontologyTerm).getFormula();
                }
                if (formula != null && chemicalComposition != null && chemicalComposition.size() > 0) {
                    if (!ChemUtils.match(ChemUtils.getFormula(chemicalComposition), formula, true)) {
                        iterator.remove();
                        continue;
                    }
                }
                final Collection<String> ontologyTermSynonyms = ontologyTerm.getSynonyms();
                ontologyTermSynonyms.add(ontologyTerm.getName());
                if (!isSynonym(metaCycSynonyms, ontologyTermSynonyms)) {
                    iterator.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
                iterator.remove();
            }
        }
    }

    /**
	 * 
	 * @param synonyms1 
	 * @param synonyms2 
	 * @return boolean
	 */
    private boolean isSynonym(final Collection<String> synonyms1, final Collection<String> synonyms2) {
        for (String synonym1 : synonyms1) {
            final String alphanumeric1 = getAlphanumericString(synonym1.toLowerCase(Locale.getDefault()));
            for (String synonym2 : synonyms2) {
                if (synonym2 != null) {
                    final String alphanumeric2 = getAlphanumericString(synonym2.toLowerCase(Locale.getDefault()));
                    if (alphanumeric2.equals(alphanumeric1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
	 * 
	 * @param s
	 * @return
	 */
    private String getAlphanumericString(final String s) {
        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }
}
