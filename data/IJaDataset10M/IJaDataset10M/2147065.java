package org.expasy.jpl.perf;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expasy.jpl.bio.exceptions.JPLAAByteUndefinedException;
import org.expasy.jpl.bio.exceptions.JPLEmptySequenceException;
import org.expasy.jpl.core.mol.polymer.pept.JPLPeptide;
import org.expasy.jpl.core.mol.polymer.pept.cutter.JPLDigestedPeptide;
import org.expasy.jpl.core.mol.polymer.pept.cutter.JPLDigester;
import org.expasy.jpl.core.mol.polymer.pept.cutter.JPLPeptidase;
import org.expasy.jpl.core.util.model.parser.JPLParseException;
import org.expasy.jpl.io.mol.fasta.JPLFastaEntry;
import org.expasy.jpl.io.mol.fasta.JPLFastaReader;

public class ProteinsDigester {

    private static Log logger = LogFactory.getLog(ProteinsDigester.class);

    static long startTime;

    static String fastaFilename;

    static JPLDigester digester;

    static boolean isDebug = true;

    public static void main(String[] args) throws Exception {
        double startTime = 0;
        setUp();
        fastaFilename = "uniprot-organism_subtilis.fasta";
        if (!isDebug) {
            startTime = System.nanoTime();
        }
        mainTest();
        if (!isDebug) {
            System.out.println((System.nanoTime() - startTime) + " ns.");
        }
    }

    public static void setUp() throws Exception {
        fastaFilename = "proteins.fasta";
        JPLPeptidase trypsin = JPLPeptidase.getInstance("Trypsin");
        digester = JPLDigester.newInstance(trypsin);
    }

    public static void mainTest() {
        startTime = System.nanoTime();
        logger.info("Entering proteins digestion benchmark of file " + fastaFilename);
        try {
            scanNProcessFastaFile(ClassLoader.getSystemResource(fastaFilename).getFile());
        } catch (final IOException e) {
            logger.fatal(e.getMessage() + ": " + fastaFilename + " I/O error.");
        } catch (JPLParseException e) {
            logger.fatal(e.getMessage() + ": " + fastaFilename + " parse error.");
        }
        logger.info("Benchmarking over.");
        logger.info("Test exec : " + (System.nanoTime() - startTime) + " ns.");
    }

    /**
	 * Read fasta file
	 * 
	 * @param filename (optionally gzipped) fasta filename to get sequence from.
	 * @throws IOException
	 * @throws JPLParseException
	 * @throws JPLEmptySequenceException
	 * @throws JPLAAByteUndefinedException
	 * @throws Exception if ...
	 */
    public static void scanNProcessFastaFile(final String filename) throws IOException, JPLParseException {
        if (logger.isInfoEnabled()) {
            logger.info("open filename " + filename);
        }
        final JPLFastaReader reader = JPLFastaReader.newInstance();
        reader.parse(new File(filename));
        Iterator<JPLFastaEntry> it = reader.iterator();
        int i = 0;
        int j = 0;
        while (it.hasNext()) {
            final String nextSequence = it.next().getSequence();
            final JPLPeptide sequence = new JPLPeptide.Builder(nextSequence).ambiguityEnabled().build();
            if (logger.isDebugEnabled()) {
                logger.debug("Trypsin digestion on " + sequence + "(" + sequence.length() + " aas)");
            }
            if (!sequence.isAmbiguous()) {
                digester.digest(sequence);
                final Set<JPLDigestedPeptide> peptides = digester.getDigests();
                if (logger.isInfoEnabled()) {
                    j += peptides.size();
                }
            }
            if (logger.isInfoEnabled()) {
                if (((i + 1) % 1000) == 0) {
                    logger.info((i + 1) + " fasta sequences red (" + (System.nanoTime() - startTime) + " ns.)");
                }
                i++;
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("Total of " + i + " fasta protein sequences red.");
            logger.info("Total of " + j + " digested peptides.");
        }
    }
}
