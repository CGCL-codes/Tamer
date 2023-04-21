package uk.ac.ebi.intact.util.uniprotExport.filters;

import psidev.psi.mi.tab.model.*;
import uk.ac.ebi.enfin.mi.cluster.EncoreInteractionForScoring;
import uk.ac.ebi.intact.model.CvAliasType;
import uk.ac.ebi.intact.util.uniprotExport.results.contexts.MiClusterContext;
import uk.ac.ebi.intact.util.uniprotExport.writers.WriterUtils;
import java.util.*;

/**
 * This class contains utility methods for filtering binary interactions and manipulating the interactions
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>01/02/11</pre>
 */
public class FilterUtils {

    private static final String UNIPROT = "uniprotkb";

    private static final String CONFIDENCE_NAME = "intactPsiscore";

    /**
     * Add the gene name of these interactors in the cluster context if it is not already present
     * @param interactorA
     * @param intactA
     * @param interactorB
     * @param intactB
     * @param context
     */
    public static void processGeneNames(Interactor interactorA, String intactA, Interactor interactorB, String intactB, MiClusterContext context) {
        String geneNameA = retrieveInteractorGeneName(interactorA);
        String geneNameB = retrieveInteractorGeneName(interactorB);
        Map<String, String> geneNames = context.getGeneNames();
        if (geneNameA != null && !geneNames.containsKey(geneNameA)) {
            context.getGeneNames().put(intactA, geneNameA);
        }
        if (geneNameB != null && !geneNames.containsKey(geneNameB)) {
            context.getGeneNames().put(intactB, geneNameB);
        }
    }

    /**
     *
     * @param interactor
     * @returnthe gene name of an interactor. If it doesn't exist, will return the locus name and if it doesn't exist, the ORF name
     */
    public static String retrieveInteractorGeneName(Interactor interactor) {
        Collection<Alias> aliases = interactor.getAliases();
        String geneName = null;
        String locusName = null;
        String orf = null;
        if (!aliases.isEmpty()) {
            for (Alias alias : interactor.getAliases()) {
                if (UNIPROT.equalsIgnoreCase(alias.getDbSource())) {
                    if (CvAliasType.GENE_NAME.equalsIgnoreCase(alias.getAliasType())) {
                        geneName = alias.getName();
                    } else if (CvAliasType.LOCUS_NAME.equalsIgnoreCase(alias.getAliasType())) {
                        locusName = alias.getName();
                    } else if (CvAliasType.ORF_NAME.equalsIgnoreCase(alias.getAliasType())) {
                        orf = alias.getName();
                    }
                }
            }
        }
        if (geneName == null && !interactor.getAlternativeIdentifiers().isEmpty()) {
            for (CrossReference ref : interactor.getAlternativeIdentifiers()) {
                if (UNIPROT.equalsIgnoreCase(ref.getDatabase())) {
                    if (CvAliasType.GENE_NAME.equalsIgnoreCase(ref.getText())) {
                        geneName = ref.getIdentifier();
                    } else if (CvAliasType.LOCUS_NAME.equalsIgnoreCase(ref.getText())) {
                        locusName = ref.getIdentifier();
                    } else if (CvAliasType.ORF_NAME.equalsIgnoreCase(ref.getText())) {
                        orf = ref.getIdentifier();
                    }
                }
            }
        }
        if (geneName == null) {
            if (locusName == null) {
                if (orf == null) {
                    geneName = "-";
                } else {
                    geneName = orf;
                }
            } else {
                geneName = locusName;
            }
        }
        return geneName;
    }

    /**
     *
     * @param interactorAccs
     * @return String [2] with the uniprot ac, intact ac of the interactor
     */
    public static String[] extractUniprotAndIntactAcFromAccs(Map<String, String> interactorAccs) {
        String interactorAcc = null;
        String intactAc = null;
        for (Map.Entry<String, String> entry : interactorAccs.entrySet()) {
            if (WriterUtils.INTACT.equalsIgnoreCase(entry.getKey())) {
                intactAc = entry.getValue();
            } else if (WriterUtils.UNIPROT.equalsIgnoreCase(entry.getKey())) {
                interactorAcc = entry.getValue();
            }
        }
        return new String[] { interactorAcc, intactAc };
    }

    /**
     *
     * @param references
     * @return the intact cross references
     */
    public static Set<String> extractIntactAcFrom(Collection<CrossReference> references) {
        Set<String> intactAcs = new HashSet<String>(references.size());
        for (CrossReference ref : references) {
            if (WriterUtils.INTACT.equalsIgnoreCase(ref.getDatabase())) {
                intactAcs.add(ref.getIdentifier());
            }
        }
        return intactAcs;
    }

    /**
     *
     * @param interactorAccs
     * @return uniprot ac of the interactor
     */
    public static String extractUniprotAcFromAccs(Map<String, String> interactorAccs) {
        String interactorAcc = null;
        for (Map.Entry<String, String> entry : interactorAccs.entrySet()) {
            if (WriterUtils.UNIPROT.equalsIgnoreCase(entry.getKey())) {
                interactorAcc = entry.getValue();
            }
        }
        return interactorAcc;
    }

    /**
     *
     * @param interactorAccs
     * @return uniprot ac of the interactor
     */
    public static String extractUniprotAcFromCrossReferences(Collection<CrossReference> interactorAccs) {
        String interactorAcc = null;
        for (CrossReference entry : interactorAccs) {
            if (WriterUtils.UNIPROT.equalsIgnoreCase(entry.getDatabase())) {
                interactorAcc = entry.getIdentifier();
            }
        }
        return interactorAcc;
    }

    /**
     *
     * @param interactorAccs
     * @return The intact ac of the interactor
     */
    public static String extractIntactAcFromAccs(Map<String, String> interactorAccs) {
        String interactorAcc = null;
        for (Map.Entry<String, String> entry : interactorAccs.entrySet()) {
            if (WriterUtils.INTACT.equalsIgnoreCase(entry.getKey())) {
                interactorAcc = entry.getValue();
            }
        }
        return interactorAcc;
    }

    /**
     *
     * @param interactorAccs
     * @return the intact ac of the interactor
     */
    public static String extractIntactAcFromOtherAccs(Map<String, List<String>> interactorAccs) {
        String intactAc = null;
        for (Map.Entry<String, List<String>> entry : interactorAccs.entrySet()) {
            if (WriterUtils.INTACT.equalsIgnoreCase(entry.getKey()) && !entry.getValue().isEmpty()) {
                intactAc = entry.getValue().iterator().next();
            }
        }
        return intactAc;
    }

    /**
     *
     * @param interactorAccs
     * @return The intact ac of the interactor
     */
    public static List<String> extractAllIntactAcFromAccs(Map<String, String> interactorAccs) {
        List<String> intactAcs = new ArrayList<String>(interactorAccs.size());
        for (Map.Entry<String, String> entry : interactorAccs.entrySet()) {
            if (WriterUtils.INTACT.equalsIgnoreCase(entry.getKey())) {
                intactAcs.add(entry.getValue());
            }
        }
        return intactAcs;
    }

    /**
     *
     * @param interactorAccs
     * @return the intact ac of the interactor
     */
    public static List<String> extractAllIntactAcFromOtherAccs(Map<String, List<String>> interactorAccs) {
        List<String> intactAcs = Collections.EMPTY_LIST;
        for (Map.Entry<String, List<String>> entry : interactorAccs.entrySet()) {
            if (WriterUtils.INTACT.equalsIgnoreCase(entry.getKey()) && !entry.getValue().isEmpty()) {
                intactAcs = entry.getValue();
            }
        }
        return intactAcs;
    }

    /**
     *
     * @param interactorAccs
     * @returnthe uniprot acs of the interactor
     */
    public static String extractUniprotAcFromOtherAccs(Map<String, List<String>> interactorAccs) {
        String uniprotAc = null;
        for (Map.Entry<String, List<String>> entry : interactorAccs.entrySet()) {
            if (WriterUtils.UNIPROT.equalsIgnoreCase(entry.getKey()) && !entry.getValue().isEmpty()) {
                uniprotAc = entry.getValue().iterator().next();
            }
        }
        return uniprotAc;
    }

    /**
     *
     * @param references
     * @return a set of pubmed Ids for this interaction
     */
    public static Set<String> extractPubmedIdsFrom(Collection<CrossReference> references) {
        Set<String> pubmedIds = new HashSet<String>(references.size());
        for (CrossReference ref : references) {
            if (WriterUtils.PUBMED.equalsIgnoreCase(ref.getDatabase())) {
                pubmedIds.add(ref.getIdentifier());
            }
        }
        return pubmedIds;
    }

    /**
     *
     * @param interaction
     * @return the computed Mi cluster score for this interaction
     */
    public static double getMiClusterScoreFor(EncoreInteractionForScoring interaction) {
        List<Confidence> confidenceValues = interaction.getConfidenceValues();
        return extractMiClusterScoreFrom(confidenceValues);
    }

    private static double extractMiClusterScoreFrom(List<Confidence> confidenceValues) {
        double score = 0;
        for (Confidence confidenceValue : confidenceValues) {
            if (confidenceValue.getType().equalsIgnoreCase(CONFIDENCE_NAME)) {
                score = Double.parseDouble(confidenceValue.getValue());
            }
        }
        return score;
    }

    /**
     *
     * @param interaction
     * @return the computed Mi cluster score for this interaction
     */
    public static double getMiClusterScoreFor(BinaryInteraction interaction) {
        List<Confidence> confidenceValues = interaction.getConfidenceValues();
        return extractMiClusterScoreFrom(confidenceValues);
    }
}
