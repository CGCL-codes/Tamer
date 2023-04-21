package org.jcvi.fasta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jcvi.common.command.CommandLineOptionBuilder;
import org.jcvi.common.command.CommandLineUtils;
import org.jcvi.common.core.io.IOUtil;
import org.jcvi.common.core.seq.fastx.fasta.FastaFileParser;
import org.jcvi.common.core.seq.fastx.fasta.FastaFileVisitor;
import org.jcvi.common.core.seq.fastx.fasta.nt.AbstractNucleotideFastaVisitor;
import org.jcvi.common.core.seq.fastx.fasta.nt.DefaultNucleotideSequenceFastaRecord;
import org.jcvi.common.core.seq.fastx.fasta.nt.NucleotideSequenceFastaRecord;

/**
 * {@code ConvertGappedFasta2UngappedFasta} converts a gapped
 * nucleotide fasta file into an ungapped
 * fasta file.
 * @author dkatzel
 *
 *
 */
public class ConvertGappedFasta2UngappedFasta {

    /**
     * @param args
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException {
        Options options = new Options();
        options.addOption(new CommandLineOptionBuilder("i", "input gapped fasta").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("o", "output ungapped fasta").isRequired(true).build());
        try {
            CommandLine commandLine = CommandLineUtils.parseCommandLine(options, args);
            File fastaFile = new File(commandLine.getOptionValue("i"));
            File outputFile = new File(commandLine.getOptionValue("o"));
            final PrintWriter output = new PrintWriter(outputFile);
            FastaFileVisitor visitor = new AbstractNucleotideFastaVisitor() {

                @Override
                protected boolean visitFastaRecord(NucleotideSequenceFastaRecord fastaRecord) {
                    NucleotideSequenceFastaRecord ungapped = new DefaultNucleotideSequenceFastaRecord(fastaRecord.getId(), fastaRecord.getComment(), fastaRecord.getSequence().asUngappedList());
                    output.print(ungapped);
                    return true;
                }
            };
            FastaFileParser.parse(fastaFile, visitor);
            IOUtil.closeAndIgnoreErrors(output);
        } catch (ParseException e) {
            printHelp(options);
            System.exit(1);
        }
    }

    /**
     * @param options
     */
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("gappedFasta2ungapped -i <gapped fasta file> -o <ungapped fata file>", "convert a gapped fasta file into an ungapped version" + " (ids, comments and fasta record order is maintained)", options, "Created by Danny Katzel");
    }
}
