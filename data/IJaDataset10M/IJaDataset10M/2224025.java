package org.mcisb.sbml;

import java.io.*;
import java.util.*;
import org.mcisb.excel.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.sbo.*;
import org.mcisb.util.*;
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class Holzhutter {

    /**
	 * 
	 */
    private final SbmlUtils sbmlUtils = new SbmlUtils();

    /**
	 * 
	 */
    private final ExcelReader reader;

    /**
	 * 
	 */
    private final File fileOut;

    /**
	 * 
	 * @param excelFile
	 * @param fileOut 
	 * @throws IOException 
	 */
    public Holzhutter(final File excelFile, final File fileOut) throws IOException {
        reader = new ExcelReader(excelFile);
        this.fileOut = fileOut;
    }

    /**
	 * @throws Exception 
	 * 
	 */
    public void doTask() throws Exception {
        final List<List<Object>> metaboliteData = reader.getData("Compounds");
        final List<List<Object>> reactionData = reader.getData("Reactions");
        final Map<String, Object[]> speciesIdToDetails = new HashMap<String, Object[]>();
        for (List<Object> row : metaboliteData) {
            final String speciesId = (String) row.get(reader.getColumnNames("Compounds").indexOf("HC-ID"));
            final String name = (String) row.get(reader.getColumnNames("Compounds").indexOf("Name"));
            final Map<OntologyTerm, int[]> ontologyTerms = new HashMap<OntologyTerm, int[]>();
            final Object[] details = new Object[2];
            details[0] = name.trim();
            details[1] = ontologyTerms;
            speciesIdToDetails.put(speciesId.trim(), details);
        }
        final SBMLDocument document = new SBMLDocument();
        document.setLevelAndVersion(2, 4);
        final Model model = document.createModel();
        Compartment compartment = model.createCompartment();
        compartment.setId("c");
        compartment.setName("Cytoplasm");
        compartment.setSize(1.0);
        compartment = model.createCompartment();
        compartment.setId("r");
        compartment.setName("Endoplasmic reticulum");
        compartment.setSize(1.0);
        compartment = model.createCompartment();
        compartment.setId("m");
        compartment.setName("Mitochondrion");
        compartment.setSize(1.0);
        compartment = model.createCompartment();
        compartment.setId("s");
        compartment.setName("Extracellular");
        compartment.setSize(1.0);
        compartment = model.createCompartment();
        compartment.setId("g");
        compartment.setName("Golgi");
        compartment.setSize(1.0);
        compartment = model.createCompartment();
        compartment.setId("n");
        compartment.setName("Nucleus");
        compartment.setSize(1.0);
        compartment = model.createCompartment();
        compartment.setId("l");
        compartment.setName("Lysosome");
        compartment.setSize(1.0);
        compartment = model.createCompartment();
        compartment.setId("p");
        compartment.setName("Peroxisome");
        compartment.setSize(1.0);
        compartment = model.createCompartment();
        compartment.setId("b");
        compartment.setName("Bile");
        compartment.setSize(1.0);
        addReactions(model, reactionData, speciesIdToDetails);
        final File temp = File.createTempFile("temp", ".xml");
        sbmlUtils.writeSBML(model, temp, true, false);
        sbmlUtils.format(temp, fileOut, true, false);
    }

    /**
	 * 
	 * @param inModel
	 * @param reactionId
	 * @param reactionData 
	 * @param metaboliteData 
	 * @param speciesIdToDetails 
	 * @throws Exception 
	 */
    private void addReactions(final Model inModel, final List<List<Object>> reactionData, final Map<String, Object[]> speciesIdToDetails) throws Exception {
        final int recon1Index = reader.getColumnNames("Reactions").indexOf("Recon1");
        final int reactionIdIndex = reader.getColumnNames("Reactions").indexOf("r-ID");
        final int reactionIndex = reader.getColumnNames("Reactions").indexOf("Equation in HC-ID");
        final int reactionCategoryIndex = reader.getColumnNames("Reactions").indexOf("Reaction Category");
        final int referencesIndex = reader.getColumnNames("Reactions").indexOf("References");
        for (int i = 1; i < reactionData.size(); i++) {
            final List<Object> row = reactionData.get(i);
            final Object recon1Id = row.size() > recon1Index ? row.get(recon1Index) : null;
            if (recon1Id == null) {
                continue;
            }
            final String reactionString = row.get(reactionIndex).toString();
            final Reaction reaction = inModel.createReaction();
            reaction.setId(row.get(reactionIdIndex).toString());
            reaction.setName(reaction.getId());
            reaction.setReversible(reactionString.contains("<=>"));
            final String[] reactantsAndProducts = reactionString.split("(<=>)|(-->)|(<--)");
            final String[] reactants = reactantsAndProducts[0].trim().split("\\+");
            final String[] products = reactantsAndProducts.length == 1 ? new String[0] : reactantsAndProducts[1].trim().split("\\+");
            for (String reactant : reactants) {
                addSpeciesReference(reactant.trim(), speciesIdToDetails, inModel, reaction, !reactionString.contains("<--"));
            }
            for (String product : products) {
                addSpeciesReference(product.trim(), speciesIdToDetails, inModel, reaction, reactionString.contains("<--"));
            }
            final Set<String> compartments = new HashSet<String>();
            for (int l = 0; l < reaction.getNumReactants(); l++) {
                compartments.add(inModel.getSpecies(reaction.getReactant(l).getSpecies()).getCompartment());
            }
            for (int l = 0; l < reaction.getNumProducts(); l++) {
                compartments.add(inModel.getSpecies(reaction.getProduct(l).getSpecies()).getCompartment());
            }
            reaction.setSBOTerm(compartments.size() == 1 ? SboUtils.BIOCHEMICAL_REACTION : SboUtils.TRANSPORT_REACTION);
            String reactionCategoryString = (String) row.get(reactionCategoryIndex);
            for (String ecTerm : RegularExpressionUtils.getMatches(reactionCategoryString.trim(), RegularExpressionUtils.EC_REGEX)) {
                sbmlUtils.addOntologyTerm(reaction, OntologyUtils.getInstance().getOntologyTerm(Ontology.EC, ecTerm), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_VERSION_OF);
            }
            for (String tcdbTerm : RegularExpressionUtils.getMatches(reactionCategoryString.trim(), "(?<=TCDB:)\\d+\\.[A-Z]\\.\\d+\\.\\d+\\.\\d+(?=.*)")) {
                sbmlUtils.addOntologyTerm(reaction, OntologyUtils.getInstance().getOntologyTerm(Ontology.TCDB, tcdbTerm), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_VERSION_OF);
            }
            String referencesString = row.size() > referencesIndex ? (String) row.get(referencesIndex) : null;
            if (referencesString != null) {
                for (String pubMedTerm : RegularExpressionUtils.getMatches(referencesString.trim(), "(?<=PMID:)\\d+(?=.*)")) {
                    sbmlUtils.addOntologyTerm(reaction, OntologyUtils.getInstance().getOntologyTerm(Ontology.PUBMED, pubMedTerm), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_DESCRIBED_BY);
                }
            }
        }
    }

    /**
	 * 
	 * @param reactionComponentId 
	 * @param speciesIdToDetails
	 * @param model
	 * @param reaction
	 * @param isReactant
	 * @throws Exception
	 */
    private void addSpeciesReference(final String reactionComponentId, final Map<String, Object[]> speciesIdToDetails, final Model model, final Reaction reaction, final boolean isReactant) throws Exception {
        final String stoichiometryPrefix = CollectionUtils.getFirst(RegularExpressionUtils.getMatches(reactionComponentId, "^\\d*\\s+"));
        String id = reactionComponentId;
        double stoichiometry = 1;
        if (stoichiometryPrefix != null) {
            stoichiometry = Double.parseDouble(stoichiometryPrefix.trim());
            id = id.substring(stoichiometryPrefix.length());
        }
        Object[] details = speciesIdToDetails.get(id.substring(0, id.length() - 2));
        if (model.getSpecies(id) == null) {
            Species species = model.createSpecies();
            species.setId(id);
            species.setCompartment(id.substring(id.length() - 1));
            species.setName((String) details[0]);
            sbmlUtils.addOntologyTerms(species, (Map<OntologyTerm, Object[]>) details[1]);
            species.setSBOTerm(SboUtils.SIMPLE_CHEMICAL);
            if (!species.isSetCompartment()) {
                System.out.println(id);
            }
        }
        final SpeciesReference ref = isReactant ? reaction.createReactant() : reaction.createProduct();
        ref.setSpecies(id);
        ref.setStoichiometry(stoichiometry);
    }

    /**
	 * @param args
	 * @throws Exception 
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException, Exception {
        new Holzhutter(new File(args[0]), new File(args[1])).doTask();
    }
}
