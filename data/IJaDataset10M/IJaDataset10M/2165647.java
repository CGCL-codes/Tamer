package it.southdown.avana.tools.alignment;

import it.southdown.avana.alignment.*;
import it.southdown.avana.alignment.io.*;
import it.southdown.avana.util.*;
import java.io.*;
import java.util.*;

public class DuplicateRemover {

    public void execute(String inFilename, String outFilename) throws IOException, SequenceSourceException {
        System.out.println("Reading Sequences");
        SequenceReader seqReader = new FastaSequenceReader();
        SequenceSetReader reader = new SequenceSetReader(seqReader);
        SequenceSource src = new FileSequenceSource(inFilename, reader);
        Sequence[] sequences = src.getSequences();
        System.out.println("Read " + sequences.length + " sequences");
        Arrays.sort(sequences, new SequenceLengthSorter());
        System.out.println("Removing duplicate sequences");
        ArrayList<Sequence> list = new ArrayList<Sequence>(sequences.length);
        for (int i = 0; i < sequences.length; i++) {
            Sequence shorter = sequences[i];
            boolean found = false;
            for (int j = (sequences.length - 1); j > i; j--) {
                Sequence longer = sequences[j];
                if (longer.getData().contains(shorter.getData())) {
                    found = true;
                    System.out.println("Remove: " + shorter.getId() + " found in " + longer.getId());
                    break;
                }
            }
            if (!found) {
                list.add(shorter);
            }
        }
        Sequence[] cleaned = new Sequence[list.size()];
        cleaned = list.toArray(cleaned);
        Arrays.sort(cleaned, new SequenceIdSorter());
        System.out.println("Remaining sequences:" + cleaned.length);
        System.out.println("Writing FASTA file");
        String content = SequenceUtilities.makeFastaAlignment(cleaned);
        try {
            File outFile = new File(outFilename);
            FileUtilities.saveContent(outFile, content);
        } catch (Exception e) {
            System.err.println("Error extracting subalignment: " + e.getMessage());
            return;
        }
        System.out.println("Task completed");
    }

    private static class SequenceLengthSorter implements Comparator<Sequence> {

        public int compare(Sequence a, Sequence b) {
            return (a.getData().length() - b.getData().length());
        }
    }

    private static class SequenceIdSorter implements Comparator<Sequence> {

        public int compare(Sequence a, Sequence b) {
            return (a.getId().compareTo(b.getId()));
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java it.southdown.avana.tools.alignment.DuplicateRemover " + "<in_fasta_file> <out_fasta_file>");
            System.exit(1);
        }
        String inFilename = args[0];
        String outFilename = args[1];
        try {
            DuplicateRemover loader = new DuplicateRemover();
            loader.execute(inFilename, outFilename);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
