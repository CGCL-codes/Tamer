package org.mcisb.subliminal;

import java.io.*;
import java.util.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.kegg.*;
import org.mcisb.subliminal.balance.*;
import org.mcisb.subliminal.kegg.*;
import org.mcisb.subliminal.merge.*;
import org.mcisb.subliminal.metacyc.*;
import org.mcisb.subliminal.protonate.*;
import org.mcisb.util.io.*;

/**
 * 
 * @author Neil Swainston
 */
public class Path2ModelsReconstructionGenerator {

    /**
	 * 
	 */
    private final KeggExtracter keggExtracter = new KeggExtracter();

    /**
	 * 
	 */
    private final SbmlSpeciesProtonator sbmlSpeciesProtonator;

    /**
	 * 
	 * @param pH
	 * @throws Exception
	 */
    public Path2ModelsReconstructionGenerator(final float pH) throws Exception {
        sbmlSpeciesProtonator = new SbmlSpeciesProtonator(pH);
    }

    /**
	 * 
	 * @param directory
	 * @param keggOrganismId
	 * @throws Exception
	 */
    public void reconstructAll(final File directory) throws Exception {
        for (String organismId : KeggGenomeUtils.getInstance().getOrganismIds()) {
            reconstruct(directory, organismId);
        }
    }

    /**
	 * 
	 * @param directory
	 * @param keggOrganismId
	 * @throws Exception
	 */
    public void reconstructFrom(final File directory, final String keggOrganismId) throws Exception {
        boolean found = false;
        for (String organismId : KeggGenomeUtils.getInstance().getOrganismIds()) {
            if (!found && organismId.equals(keggOrganismId)) {
                found = true;
            }
            if (found) {
                reconstruct(directory, organismId);
            }
        }
    }

    /**
	 * 
	 * @param directory
	 * @param keggOrganismId
	 * @throws Exception
	 */
    public void reconstruct(final File directory, final String keggOrganismId) throws Exception {
        final OntologyTerm keggGenomeTerm = new KeggGenomeTerm(keggOrganismId);
        final OntologyTerm taxonomyTerm = OntologyUtils.getInstance().getXref(keggGenomeTerm, Ontology.TAXONOMY);
        if (taxonomyTerm == null || taxonomyTerm.getName() == null) {
            final String[] organismIds = KeggGenomeUtils.getInstance().getOrganismIds();
            throw new UnsupportedOperationException("KEGG organism id " + keggOrganismId + " unknown. Supported organisms are " + Arrays.toString(organismIds) + ".");
        }
        final int taxonomyId = Integer.parseInt(OntologyUtils.getInstance().getXref(keggGenomeTerm, Ontology.TAXONOMY).getId());
        reconstruct(directory, keggOrganismId, taxonomyId);
    }

    /**
	 * 
	 * @param directory
	 * @param keggOrganismId
	 * @param taxonomyId
	 * @throws Exception
	 */
    private void reconstruct(final File directory, final String keggOrganismId, final int taxonomyId) throws Exception {
        final File organismDirectory = new File(directory, keggOrganismId);
        if (!organismDirectory.exists() && !organismDirectory.mkdirs()) {
            throw new IOException();
        }
        SubliminalUtils.getInstance(true).setDefaultCompartment(false);
        final File keggDirectory = new File(organismDirectory, "kegg");
        keggExtracter.run(keggDirectory, keggOrganismId);
        final File keggMergedDirectory = new File(keggDirectory, "merged");
        if (!keggMergedDirectory.exists() && !keggMergedDirectory.mkdirs()) {
            throw new IOException();
        }
        final File keggMerged = new File(keggMergedDirectory, keggOrganismId + "_k.xml");
        if (!keggMerged.exists()) {
            new SbmlMerger(keggDirectory, keggMerged, true, false, true, true).run();
        }
        final File keggProtonated = new File(keggMergedDirectory, keggOrganismId + "_kp.xml");
        if (!keggProtonated.exists()) {
            sbmlSpeciesProtonator.run(keggMerged, keggProtonated);
        }
        final File keggBalanced = new File(keggMergedDirectory, keggOrganismId + "_kb.xml");
        if (!keggBalanced.exists()) {
            new SbmlReactionBalancerTask(keggProtonated, keggBalanced).run();
        }
        final File metacycDirectory = new File(organismDirectory, "metacyc");
        final File metacycOrganismDirectory = new File(metacycDirectory, keggOrganismId);
        if (!metacycOrganismDirectory.exists() && !metacycOrganismDirectory.mkdirs()) {
            throw new IOException();
        }
        File metacycBalanced = null;
        try {
            final File metacyc = new File(metacycOrganismDirectory, keggOrganismId + "_m.xml");
            if (!metacyc.exists()) {
                new MetaCycAnnotator(taxonomyId, metacyc).run(false);
            }
            final File metacycProtonated = new File(metacycOrganismDirectory, keggOrganismId + "_mp.xml");
            if (!metacycProtonated.exists()) {
                sbmlSpeciesProtonator.run(metacyc, metacycProtonated);
            }
            metacycBalanced = new File(metacycOrganismDirectory, keggOrganismId + "_mb.xml");
            if (!metacycBalanced.exists()) {
                new SbmlReactionBalancerTask(metacycProtonated, metacycBalanced).run();
            }
        } catch (UnsupportedOperationException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        final File mergeDirectory = new File(organismDirectory, "merge");
        final File mergeOrganismDirectory = new File(mergeDirectory, keggOrganismId);
        if (!mergeOrganismDirectory.exists() && !mergeOrganismDirectory.mkdirs()) {
            throw new IOException();
        }
        final File merged = new File(mergeOrganismDirectory, keggOrganismId + ".xml");
        if (!merged.exists()) {
            if (metacycBalanced == null) {
                new FileUtils().fileCopy(keggBalanced, merged);
            } else {
                new SbmlMerger(new File[] { keggBalanced, metacycBalanced }, merged, true, true, true, true).run();
            }
        }
    }

    /**
	 * @param args
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            new Path2ModelsReconstructionGenerator(Float.parseFloat(args[1])).reconstructAll(new File(args[0]));
        } else if (args.length == 3) {
            new Path2ModelsReconstructionGenerator(Float.parseFloat(args[1])).reconstruct(new File(args[0]), args[2]);
        } else if (args.length == 4 && args[0].equals("-f")) {
            new Path2ModelsReconstructionGenerator(Float.parseFloat(args[2])).reconstructFrom(new File(args[1]), args[3]);
        }
    }
}
