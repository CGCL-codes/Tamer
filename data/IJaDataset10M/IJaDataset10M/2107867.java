package uk.ac.roslin.ensembl.test;

import org.apache.log4j.PropertyConfigurator;
import uk.ac.roslin.ensembl.config.DBConnection.DataSource;
import uk.ac.roslin.ensembl.config.EnsemblCoordSystemType;
import uk.ac.roslin.ensembl.dao.database.DBRegistry;
import uk.ac.roslin.ensembl.dao.database.DBSpecies;
import uk.ac.roslin.ensembl.datasourceaware.core.DAExon;
import uk.ac.roslin.ensembl.datasourceaware.core.DAGene;
import uk.ac.roslin.ensembl.datasourceaware.core.DATranscript;
import uk.ac.roslin.ensembl.model.Mapping;

public class ExonTest {

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("log4j.properties");
        DBRegistry eReg = new DBRegistry(DataSource.ENSEMBLDB);
        DBSpecies sp = eReg.getSpeciesByAlias("cow");
        DAGene gene = sp.getGeneByStableID("ENSBTAG00000021527");
        Mapping mapping = gene.getAnnotationLevelMappings().first();
        System.out.println("Gene: " + gene.getStableID());
        System.out.println("\tchr start: " + mapping.getTargetCoordinates().getStart());
        System.out.println("\tchr stop: " + mapping.getTargetCoordinates().getEnd());
        System.out.println("\tassembly: " + gene.getAssembly());
        System.out.println("\tdescription: " + gene.getDescription());
        System.out.println("\tsymbol: " + gene.getDisplayName());
        System.out.println("\tstrand: " + mapping.getTargetCoordinates().getStrand());
        System.out.println("\ttaxonID: " + gene.getSpecies().getTaxonomyID());
        System.out.println("\tstatus: " + gene.getStatus());
        System.out.println("\ttype: " + gene.getBiotype());
        System.out.println("\tTranscript Count: " + gene.getTranscripts().size());
        for (DATranscript t : gene.getTranscripts()) {
            System.out.println("\t\tTranscript: " + t.getStableID());
            System.out.println("\t\t " + t.getDisplayName());
            System.out.println("\t\t " + t.getStatus());
            System.out.println("\t\t " + t.getBiotype());
            System.out.println("\t\t " + t.getDescription());
            System.out.println("\t\t " + t.getGene().getStableID());
            System.out.println("\t\tCanonical ?  " + t.isCanonical());
            System.out.println("\t\tXREF: " + t.getDisplayXRef().getDBName());
            System.out.println("\t\tXREF: " + t.getDisplayXRef().getDisplayID());
            System.out.println("\t\tXREF: " + t.getDisplayXRef().getInfoType());
            System.out.println("\t\tXREF: " + t.getDisplayXRef().getInfo());
            for (Mapping m : t.getLoadedMappings(EnsemblCoordSystemType.chromosome)) {
                System.out.println("\t\tMapping: " + m.getTargetHashID());
                System.out.println("\t\t\tCoords: " + m.getTargetCoordinates().toString());
            }
            System.out.println("EXONS");
            for (DAExon e : t.getExons()) {
                System.out.println("\t\tRank: " + e.getRank());
                System.out.println("\t\tStableID: " + e.getStableID());
                System.out.println("\t\tID: " + e.getId());
                System.out.println("\t\tstart phase: " + e.getPhase());
                System.out.println("\t\tend phase: " + e.getEndPhase());
                System.out.println("\t\tcurrent: " + e.isCurrent());
                System.out.println("\t\tconstitutive: " + e.isConstitutive());
                for (Mapping m : e.getLoadedMappings(EnsemblCoordSystemType.chromosome)) {
                    System.out.println("\t\tMapping: " + m.getTargetHashID());
                    System.out.println("\t\t\tCoords: " + m.getTargetCoordinates().toString());
                }
            }
            for (DAExon e : t.getExons()) {
                System.out.print(e.getRank());
                System.out.print("\t" + e.getStableID());
                System.out.print("\t" + e.getId());
                System.out.print("\t" + e.getPhase());
                System.out.print("\t" + e.getEndPhase());
                for (Mapping m : e.getLoadedMappings(EnsemblCoordSystemType.chromosome)) {
                    System.out.println("\t" + m.getTargetCoordinates().toString());
                }
            }
        }
    }
}
