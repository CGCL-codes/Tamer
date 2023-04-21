package org.jcvi.assembly.agp;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jcvi.common.util.Range;
import org.jcvi.io.IOUtil;
import org.jcvi.sequence.SequenceDirection;

/**
 * 
 * @author dkatzel
 *
 *@see <a href="http://www.ncbi.nlm.nih.gov/projects/genome/assembly/agp/AGP_Specification.shtml">NCBI AGP Spec</a>
 */
public class AgpParser {

    private static final Pattern CONTIG_PATTERN = Pattern.compile("(\\S+)\\s+(\\d+)\\s+(\\d+)\\s+\\d+\\s+([A,D,F,G,N,O,P,U,W])\\s+(\\S+)\\s+(\\d+)\\s+(\\d+)\\s+([+,-])");

    public static void parseAgpFile(File agpFile, AgpFileVisitor visitor) throws FileNotFoundException {
        parseAgpFile(agpFile, visitor, Range.CoordinateSystem.RESIDUE_BASED);
    }

    public static void parseAgpFile(File agpFile, AgpFileVisitor visitor, Range.CoordinateSystem agpFileCoordinateSystem) throws FileNotFoundException {
        InputStream agpStream = null;
        try {
            agpStream = new BufferedInputStream(new FileInputStream(agpFile));
            parseAgpFile(agpStream, visitor, agpFileCoordinateSystem);
        } finally {
            IOUtil.closeAndIgnoreErrors(agpStream);
        }
    }

    public static void parseAgpFile(InputStream in, AgpFileVisitor visitor) {
        parseAgpFile(in, visitor, Range.CoordinateSystem.RESIDUE_BASED);
    }

    public static void parseAgpFile(InputStream in, AgpFileVisitor visitor, Range.CoordinateSystem agpFileCoordinateSystem) {
        Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = CONTIG_PATTERN.matcher(line);
            if (matcher.find()) {
                String scaffoldId = matcher.group(1);
                Range contigRange = Range.buildRange(agpFileCoordinateSystem, Long.parseLong(matcher.group(2)), Long.parseLong(matcher.group(3)));
                String contigId = matcher.group(5);
                SequenceDirection dir = SequenceDirection.parseSequenceDirection(matcher.group(8));
                visitor.visitContigEntry(scaffoldId, contigRange, contigId, dir);
            }
        }
        visitor.visitEndOfFile();
    }
}
