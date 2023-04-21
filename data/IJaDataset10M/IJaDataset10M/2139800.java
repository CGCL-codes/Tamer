package it.southdown.avana.tools.typing;

import it.southdown.avana.alignment.*;
import it.southdown.avana.alignment.io.*;
import it.southdown.avana.alignscan.*;
import it.southdown.avana.metadata.*;
import it.southdown.avana.typing.*;
import it.southdown.avana.util.*;
import java.io.*;

public class AvanaTyping {

    private AvanaTypingConfig config = null;

    public void execute(String alignmentFilename, String outFilename) throws IOException, SequenceSourceException, MetadataFileException {
        String propFilename = System.getProperty("config");
        if (propFilename != null) {
            File propFile = new File(propFilename);
            config = new AvanaTypingConfig(propFile);
        } else {
            config = new AvanaTypingConfig();
        }
        System.out.println();
        System.out.println(config);
        System.out.println();
        File outFile = new File(outFilename);
        String outFileRoot = FileUtilities.getFilenameRoot(outFile);
        File tableFile = new File(outFile.getParentFile(), outFileRoot + ".clusterTable.csv");
        File metaFile = new File(outFile.getParentFile(), outFileRoot + ".classMeta.csv");
        File statFile = new File(outFile.getParentFile(), outFileRoot + ".classStat.csv");
        System.out.println("Reading Alignment");
        SequenceReader seqReader = new FastaSequenceReader();
        AlignmentReader reader = new AlignmentReader(seqReader);
        SequenceSource src = new FileSequenceSource(alignmentFilename, reader);
        MasterAlignment inAlignment = new MasterAlignment(src);
        AlignmentScan masterScan = new AlignmentScan(inAlignment, config.getAlignScanConfig());
        System.out.println("Performing typing");
        ClusteringController tc = new ClusteringController(config.getTypingConfig(), config.getMutualInfoConfig(), config.getPatternConfig());
        Cluster rootCluster = tc.clusterSequences(masterScan);
        System.out.println();
        System.out.println("Writing clustering output");
        ClusterSerializer cs = new ClusterSerializer();
        String outContent = cs.serializeClusterTree(rootCluster);
        FileUtilities.saveContent(outFile, outContent);
        System.out.println("Writing Cluster Variant Table");
        ClusterVariantTable table = new ClusterVariantTable(rootCluster);
        FileUtilities.saveContent(tableFile, table.toCsvString());
        System.out.println("Classifying sequences");
        Classifier cl = new Classifier(config.getTypingConfig(), rootCluster);
        AlignmentClassification ac = cl.classifySequences(masterScan.getAlignment());
        System.out.println("Writing classification Statistics");
        String classStat = ac.serializeClassificationStats();
        FileUtilities.saveContent(statFile, classStat);
        System.out.println("Writing classification metadata");
        Metadata classMeta = ac.getMetadata();
        MetadataSerializer ms = new MetadataSerializer();
        String metaContent = ms.serializeMetadata(classMeta);
        FileUtilities.saveContent(metaFile, metaContent);
        System.out.println("Completed");
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java it.southdown.avana.tools.typing.AvanaTyping " + "<alignment_file> <out_file>");
            System.exit(1);
        }
        String alignFilename = args[0];
        String outFilename = args[1];
        try {
            AvanaTyping controller = new AvanaTyping();
            controller.execute(alignFilename, outFilename);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
