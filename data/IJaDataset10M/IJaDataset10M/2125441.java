package org.jcvi.common.core.align;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jcvi.common.core.align.AlnVisitor.ConservationInfo;
import org.jcvi.common.core.io.IOUtil;
import org.jcvi.common.core.io.TextLineParser;

/**
 * @author dkatzel
 *
 *
 */
public class AlnParser {

    /**
     * 
     */
    private static final String REGEX = "^([^*\\s]+)\\s+([\\-ACGTNVHDBWMRSYK]+)";

    private static final Pattern ALIGNMENT_PATTERN = Pattern.compile(REGEX);

    private static final Pattern CONSERVATION_PATTERN = Pattern.compile("\\s+([-:\\. \\*]+)$");

    public static void parse(File alnFile, AlnVisitor visitor) throws IOException {
        InputStream in = null;
        in = new FileInputStream(alnFile);
        try {
            parse(in, visitor);
        } finally {
            IOUtil.closeAndIgnoreErrors(in);
        }
    }

    public static void parse(InputStream alnStream, AlnVisitor visitor) throws IOException {
        TextLineParser parser = new TextLineParser(alnStream);
        boolean inGroup = false;
        visitor.visitFile();
        int numberOfBasesPerGroup = 0;
        while (parser.hasNextLine()) {
            String line = parser.nextLine();
            visitor.visitLine(line);
            Matcher alignmentMatcher = ALIGNMENT_PATTERN.matcher(line);
            if (alignmentMatcher.find()) {
                if (!inGroup) {
                    visitor.visitBeginGroup();
                    inGroup = true;
                }
                String basecalls = alignmentMatcher.group(2);
                numberOfBasesPerGroup = basecalls.length();
                visitor.visitAlignedSegment(alignmentMatcher.group(1), basecalls);
            } else {
                Matcher conservationMatcher = CONSERVATION_PATTERN.matcher(line);
                if (conservationMatcher.find()) {
                    String conservationString = conservationMatcher.group(1);
                    List<ConservationInfo> info = parseConservationInfo(conservationString, numberOfBasesPerGroup);
                    visitor.visitConservationInfo(info);
                    inGroup = false;
                    visitor.visitEndGroup();
                }
            }
        }
        visitor.visitEndOfFile();
    }

    /**
     * @param conservationString
     * @param numberOfBasesPerGroup
     * @return
     */
    private static List<ConservationInfo> parseConservationInfo(String conservationString, int numberOfBasesPerGroup) {
        final String paddedString = createPaddedConservationString(conservationString, numberOfBasesPerGroup);
        List<ConservationInfo> result = new ArrayList<AlnVisitor.ConservationInfo>(numberOfBasesPerGroup);
        for (int i = 0; i < paddedString.length(); i++) {
            switch(paddedString.charAt(i)) {
                case '*':
                    result.add(ConservationInfo.IDENTICAL);
                    break;
                case ':':
                    result.add(ConservationInfo.CONSERVED_SUBSITUTION);
                    break;
                case '.':
                    result.add(ConservationInfo.SEMI_CONSERVED_SUBSITUTION);
                    break;
                default:
                    result.add(ConservationInfo.NOT_CONSERVED);
                    break;
            }
        }
        return result;
    }

    /**
     * Aln format uses spaces to denote not conserved regions,
     * this is hard to parse out using regular expressions if 
     * the conservation string is supposed to START with spaces.
     * By using the expected number of basecalls in this group,
     * we can create a padded string with the correct number of leading
     * spaces.
     * @param conservationString
     * @param numberOfBasesPerGroup
     * @return
     */
    private static String createPaddedConservationString(String conservationString, int numberOfBasesPerGroup) {
        int length = conservationString.length();
        int padding = numberOfBasesPerGroup - length;
        final String paddedString;
        if (padding > 0) {
            String format = "%" + padding + "s%s";
            paddedString = String.format(format, "", conservationString);
        } else {
            paddedString = conservationString;
        }
        return paddedString;
    }
}
