package backend.parser.generalfastafileparser.grameneParser;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Level;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXGraph;
import backend.core.AttributeName;
import backend.core.CV;
import backend.core.ConceptClass;
import backend.core.EvidenceType;
import backend.core.security.Session;
import backend.event.type.AttributeNameMissing;
import backend.event.type.StatisticalOutput;
import backend.parser.ParserArguments;
import backend.parser.basicFasta.FastaBlock;
import backend.parser.basicFasta.ReadFastaFiles;
import backend.parser.basicFasta.WriteFastaFile;
import backend.parser.generalfastafileparser.args.ArgumentNames;

/**
 * NCBI FASTA FILE parser
 * 
 * @author hindlem
 * 
 */
public class GrameneParser {

    private ParserArguments pa;

    private Session s;

    private ConceptClass ccGene;

    private ConceptClass ccProt;

    private EvidenceType etIMPD;

    private AttributeName taxIdAttr;

    private AttributeName naAttr;

    private AttributeName aaAttr;

    private CV cvGramene;

    public GrameneParser(Session s, ParserArguments pa) {
        this.s = s;
        this.pa = pa;
    }

    public void setONDEXGraph(AbstractONDEXGraph graph) {
        ccGene = graph.getONDEXGraphData(s).getConceptClass(s, MetaData.gene);
        ccProt = graph.getONDEXGraphData(s).getConceptClass(s, MetaData.protein);
        cvGramene = graph.getONDEXGraphData(s).getCV(s, MetaData.gramene);
        etIMPD = graph.getONDEXGraphData(s).getEvidenceType(s, MetaData.IMPD);
        taxIdAttr = graph.getONDEXGraphData(s).getAttributeName(s, MetaData.taxID);
        naAttr = graph.getONDEXGraphData(s).getAttributeName(s, MetaData.nucleicAcid);
        aaAttr = graph.getONDEXGraphData(s).getAttributeName(s, MetaData.aminoAcid);
        StatisticalOutput so = new StatisticalOutput("Starting Gramene Fasta File parsing...");
        so.setLog4jLevel(Level.INFO);
        graph.fireEventOccurred(so);
        String inFilesDir = pa.getInputDir();
        List<Object> fileList = pa.getObjectValueList(ArgumentNames.FILES_TO_IMPORT_ARG);
        String[] files = new String[0];
        if (fileList == null || fileList.size() == 0) {
            files = new File(inFilesDir).list(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.endsWith(".fasta");
                }
            });
        } else {
            files = fileList.toArray(new String[fileList.size()]);
        }
        for (int a = 0; a < files.length; a++) {
            String fileName = files[a];
            WriteFastaFile writeFastaFileSimple = new WriteFastaFileSimple();
            try {
                ReadFastaFiles.parseFastaFile(graph, inFilesDir + File.separator + fileName, writeFastaFileSimple);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        so = new StatisticalOutput("Finished Gramene Fasta File parsing...");
        so.setLog4jLevel(Level.INFO);
        graph.fireEventOccurred(so);
    }

    private Pattern patternNotNA = Pattern.compile("[^A|T|G|C|U|a|t|g|c|u]");

    private Pattern barPatter = Pattern.compile("[|]");

    private class WriteFastaFileSimple extends WriteFastaFile {

        @Override
        public void parseFastaBlock(AbstractONDEXGraph graph, FastaBlock fasta) {
            String header = fasta.getHeader().trim();
            String[] values = barPatter.split(header);
            String accession = values[0].trim();
            ;
            String accessionType = values[1].trim();
            ;
            String description = values[2].trim();
            ;
            String tax = values[values.length - 1];
            int eqindex = tax.indexOf("=");
            if (eqindex > -1) {
                tax = tax.substring(eqindex + 1).trim();
            } else {
                System.err.println("unknown tax id " + tax);
            }
            String sequence = fasta.getSequence();
            CV acCV = null;
            if (accessionType.equalsIgnoreCase("TREMBL") || accessionType.equalsIgnoreCase("SWISSPROT")) {
                acCV = graph.getONDEXGraphData(s).getCV(s, MetaData.u_prot);
            }
            if (acCV == null) {
                System.err.println("unknow db " + accessionType);
            }
            boolean isNotNA = patternNotNA.matcher(sequence).find();
            ConceptClass ccType = ccProt;
            AttributeName seqAt;
            if (isNotNA) {
                ccType = ccProt;
                seqAt = aaAttr;
            } else {
                ccType = ccGene;
                seqAt = naAttr;
            }
            String attr_name = (String) pa.getUniqueValue(ArgumentNames.TYPE_OF_SEQ_ARG);
            if (attr_name != null) {
                if (graph.getONDEXGraphData(s).getAttributeName(s, attr_name) != null) {
                    seqAt = graph.getONDEXGraphData(s).getAttributeName(s, attr_name);
                } else {
                    AttributeNameMissing ge = new AttributeNameMissing("Missing: " + attr_name);
                    graph.fireEventOccurred(ge);
                }
            }
            AbstractConcept ac = graph.createConcept(s, accession, description, cvGramene, ccType, etIMPD);
            if (acCV != null) ac.createConceptAccession(s, accession, acCV);
            if (tax != null) ac.createConceptGDS(s, taxIdAttr, tax, true);
            ac.createConceptGDS(s, seqAt, sequence, false);
        }
    }
}
