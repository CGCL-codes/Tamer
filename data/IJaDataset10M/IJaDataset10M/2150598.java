package org.jcvi.assembly.tasm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jcvi.assembly.contig.ContigFileVisitor;
import org.jcvi.common.util.Range;
import org.jcvi.common.util.Range.CoordinateSystem;
import org.jcvi.io.IOUtil;
import org.jcvi.sequence.SequenceDirection;

/**
 * {@code TigrAssemblyFileParser} parses TIGR Assembler contig files.
 * @author dkatzel
 *
 *
 */
public class TigrAssemblyFileParser {

    private static final String CR = "\n";

    /**
     * Each contig data is separated by a pipe ('|').
     */
    private static final String END_OF_CONTIG = "|";

    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("(\\S+)\\s+(\\S+.*$)");

    /**
     * Parse the given TIGR Assembly file and call the appropriate 
     * visitXXX methods in the given {@link ContigFileVisitor}.
     * If the given visitor is a  {@link TigrAssemblyFileVisitor}
     * then additional visitXXX methods specific to {@link TigrAssemblyFileVisitor}
     * are called as well.
     * @param tasmFile the TIGR Assembly file.
     * @param visitor the {@link ContigFileVisitor} implementation to visit.
     * @throws FileNotFoundException if tasmFile does not exist.
     */
    public static void parse(File tasmFile, ContigFileVisitor visitor) throws FileNotFoundException {
        InputStream in = new FileInputStream(tasmFile);
        try {
            parse(in, visitor);
        } finally {
            IOUtil.closeAndIgnoreErrors(in);
        }
    }

    /**
     * Parse the given TIGR Assembly {@link InputStream} and call the appropriate 
     * visitXXX methods in the given {@link ContigFileVisitor}.
     * If the given visitor is a  {@link TigrAssemblyFileVisitor}
     * then additional visitXXX methods specific to {@link TigrAssemblyFileVisitor}
     * are called as well.
     * @param inputStream an InputStream containing TIGR Assembly file data.
     * @param visitor the {@link ContigFileVisitor} implementation to visit.
     * @throws FileNotFoundException if tasmFile does not exist.
     */
    public static void parse(InputStream inputStream, ContigFileVisitor visitor) {
        boolean isTigrAssemblyVisitor = visitor instanceof TigrAssemblyFileVisitor;
        Scanner scanner = new Scanner(inputStream).useDelimiter(CR);
        boolean inContigRecord = true;
        String currentSequenceName = null;
        int currentSequenceLeftEnd = -1;
        int currentSequenceRightEnd = -1;
        int currentOffset = 0;
        String currentBases = null;
        String currentContigId = null;
        String currentContigConsensus = null;
        visitor.visitFile();
        if (isTigrAssemblyVisitor) {
            ((TigrAssemblyFileVisitor) visitor).visitBeginContigBlock();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            visitor.visitLine(line + CR);
            Matcher matcher = KEY_VALUE_PATTERN.matcher(line);
            if (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2);
                if (inContigRecord) {
                    if (isTigrAssemblyVisitor) {
                        ((TigrAssemblyFileVisitor) visitor).visitContigAttribute(key, value);
                    }
                    if (key.equals("asmbl_id")) {
                        currentContigId = value;
                    } else if (key.equals("lsequence")) {
                        currentContigConsensus = value;
                    }
                } else {
                    if (isTigrAssemblyVisitor) {
                        ((TigrAssemblyFileVisitor) visitor).visitReadAttribute(key, value);
                    }
                    if (key.equals("lsequence")) {
                        currentBases = value;
                    } else if (key.equals("seq_lend")) {
                        currentSequenceLeftEnd = Integer.parseInt(value);
                    } else if (key.equals("seq_rend")) {
                        currentSequenceRightEnd = Integer.parseInt(value);
                    } else if (key.equals("offset")) {
                        currentOffset = Integer.parseInt(value);
                    } else if (key.equals("seq_name")) {
                        currentSequenceName = value;
                    }
                }
            } else {
                if (isEndOfRecord(line)) {
                    if (inContigRecord) {
                        inContigRecord = false;
                        visitor.visitNewContig(currentContigId);
                        visitor.visitConsensusBasecallsLine(currentContigConsensus);
                        currentContigId = null;
                        currentContigConsensus = null;
                        if (isTigrAssemblyVisitor) {
                            ((TigrAssemblyFileVisitor) visitor).visitEndContigBlock();
                            ((TigrAssemblyFileVisitor) visitor).visitBeginReadBlock();
                        }
                    } else {
                        visitRead(visitor, currentSequenceName, currentSequenceLeftEnd, currentSequenceRightEnd, currentOffset, currentBases);
                        if (isTigrAssemblyVisitor) {
                            ((TigrAssemblyFileVisitor) visitor).visitEndReadBlock();
                            ((TigrAssemblyFileVisitor) visitor).visitBeginReadBlock();
                        }
                        currentSequenceName = null;
                        currentSequenceLeftEnd = -1;
                        currentSequenceRightEnd = -1;
                        currentOffset = 0;
                        currentBases = null;
                    }
                } else if (isEndOfContig(line)) {
                    visitRead(visitor, currentSequenceName, currentSequenceLeftEnd, currentSequenceRightEnd, currentOffset, currentBases);
                    inContigRecord = true;
                    if (isTigrAssemblyVisitor) {
                        ((TigrAssemblyFileVisitor) visitor).visitEndReadBlock();
                        ((TigrAssemblyFileVisitor) visitor).visitBeginContigBlock();
                    }
                    currentSequenceName = null;
                    currentSequenceLeftEnd = -1;
                    currentSequenceRightEnd = -1;
                    currentOffset = 0;
                    currentBases = null;
                }
            }
        }
        if (currentSequenceName != null) {
            visitRead(visitor, currentSequenceName, currentSequenceLeftEnd, currentSequenceRightEnd, currentOffset, currentBases);
        }
        if (isTigrAssemblyVisitor) {
            ((TigrAssemblyFileVisitor) visitor).visitEndReadBlock();
        }
        visitor.visitEndOfFile();
    }

    private static void visitRead(ContigFileVisitor visitor, String currentSequenceName, int currentSequenceLeftEnd, int currentSequenceRightEnd, int currentOffset, String currentBases) {
        SequenceDirection dir = currentSequenceLeftEnd > currentSequenceRightEnd ? SequenceDirection.REVERSE : SequenceDirection.FORWARD;
        final Range validRange;
        if (dir == SequenceDirection.REVERSE) {
            validRange = Range.buildRange(CoordinateSystem.RESIDUE_BASED, currentSequenceRightEnd, currentSequenceLeftEnd);
        } else {
            validRange = Range.buildRange(CoordinateSystem.RESIDUE_BASED, currentSequenceLeftEnd, currentSequenceRightEnd);
        }
        visitor.visitNewRead(currentSequenceName, currentOffset, validRange, dir);
        visitor.visitReadBasecallsLine(currentBases);
    }

    /**
     * @param line
     * @return
     */
    private static boolean isEndOfContig(String line) {
        return line.trim().equals(END_OF_CONTIG);
    }

    private static final boolean isEndOfRecord(String line) {
        return line.trim().isEmpty();
    }
}
