package org.jcvi.assembly.ace;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jcvi.io.TextLineParser;
import org.jcvi.sequence.SequenceDirection;

/**
 * {@code AceFileParser} contains methods for parsing
 * ACE formatted files.
 * @author dkatzel
 *
 *
 */
public final class AceFileParser {

    private static final String BEGIN_CONSENSUS_QUALITIES_LINE = "BQ\\s*";

    private static final Pattern BASECALL_PATTERN = Pattern.compile("^([*a-zA-Z]+)\\s*$");

    private static final Pattern ACE_HEADER_PATTERN = Pattern.compile("^AS\\s+(\\d+)\\s+(\\d+)");

    private static final Pattern CONTIG_HEADER_PATTERN = Pattern.compile("^CO\\s+(\\S+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+([UC])");

    private static final Pattern ASSEMBLED_FROM_PATTERN = Pattern.compile("^AF\\s+(\\S+)\\s+([U|C])\\s+(-?\\d+)");

    private static final Pattern READ_HEADER_PATTERN = Pattern.compile("^RD\\s+(\\S+)\\s+(\\d+)");

    private static final Pattern QUALITY_PATTERN = Pattern.compile("^QA\\s+(-?\\d+)\\s+(-?\\d+)\\s+(\\d+)\\s+(\\d+)");

    private static final Pattern TRACE_DESCRIPTION_PATTERN = Pattern.compile("^DS\\s+");

    private static final Pattern CHROMAT_FILE_PATTERN = Pattern.compile("CHROMAT_FILE:\\s+(\\S+)\\s+");

    private static final Pattern PHD_FILE_PATTERN = Pattern.compile("PHD_FILE:\\s+(\\S+)\\s+");

    private static final Pattern TIME_PATTERN = Pattern.compile("TIME:\\s+(.+:\\d\\d\\s+\\d\\d\\d\\d)");

    private static final Pattern SFF_CHROMATOGRAM_NAME_PATTERN = Pattern.compile("sff:(\\S+)?\\.sff:(\\S+)");

    private static final Pattern BEGIN_READ_TAG_PATTERN = Pattern.compile("RT\\{");

    private static final Pattern READ_TAG_PATTERN = Pattern.compile("(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d{6}:\\d{6})");

    private static final Pattern BEGIN_WHOLE_ASSEMBLY_TAG_PATTERN = Pattern.compile("WA\\{");

    private static final Pattern WHOLE_ASSEMBLY_TAG_PATTERN = Pattern.compile("(\\S+)\\s+(\\S+)\\s+(\\d{6}:\\d{6})");

    private static final Pattern BEGIN_CONSENSUS_TAG_PATTERN = Pattern.compile("CT\\{");

    private static final Pattern CONSENSUS_TAG_PATTERN = Pattern.compile("(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d{6}:\\d{6})(\\s+(noTrans))?");

    public static void parseAceFile(File file, AceFileVisitor visitor) throws IOException {
        parseAceFile(new FileInputStream(file), visitor);
    }

    public static void parseAceFile(InputStream inputStream, AceFileVisitor visitor) throws IOException {
        if (inputStream == null) {
            throw new NullPointerException("input stream can not be null");
        }
        TextLineParser parser;
        try {
            parser = new TextLineParser(new BufferedInputStream(inputStream));
        } catch (IOException e1) {
            e1.printStackTrace();
            throw new IllegalStateException("error reading file");
        }
        boolean firstContigBeingVisited = true;
        while (parser.hasNextLine()) {
            String lineWithCR = parser.nextLine();
            visitor.visitLine(lineWithCR);
            String line = lineWithCR.endsWith("\n") ? line = lineWithCR.substring(0, lineWithCR.length() - 1) : lineWithCR;
            Matcher headerMatcher = ACE_HEADER_PATTERN.matcher(line);
            if (headerMatcher.find()) {
                if (!firstContigBeingVisited) {
                    firstContigBeingVisited = false;
                    visitor.visitEndOfContig();
                }
                int numberOfContigs = Integer.parseInt(headerMatcher.group(1));
                int totalNumberOfReads = Integer.parseInt(headerMatcher.group(2));
                visitor.visitHeader(numberOfContigs, totalNumberOfReads);
            } else {
                if (line.matches(BEGIN_CONSENSUS_QUALITIES_LINE)) {
                    visitor.visitConsensusQualities();
                } else {
                    Matcher basecallMatcher = BASECALL_PATTERN.matcher(line);
                    if (basecallMatcher.find()) {
                        visitor.visitBasesLine(basecallMatcher.group(1));
                    } else {
                        Matcher contigMatcher = CONTIG_HEADER_PATTERN.matcher(line);
                        if (contigMatcher.find()) {
                            String contigId = contigMatcher.group(1);
                            int numberOfBases = Integer.parseInt(contigMatcher.group(2));
                            int numberOfReads = Integer.parseInt(contigMatcher.group(3));
                            int numberOfBaseSegments = Integer.parseInt(contigMatcher.group(4));
                            boolean reverseComplimented = parseIsComplimented(contigMatcher.group(5));
                            visitor.visitContigHeader(contigId, numberOfBases, numberOfReads, numberOfBaseSegments, reverseComplimented);
                        } else {
                            Matcher assembledFromMatcher = ASSEMBLED_FROM_PATTERN.matcher(line);
                            if (assembledFromMatcher.find()) {
                                String name = assembledFromMatcher.group(1);
                                final String group = assembledFromMatcher.group(2);
                                SequenceDirection dir = parseIsComplimented(group) ? SequenceDirection.REVERSE : SequenceDirection.FORWARD;
                                int fullRangeOffset = Integer.parseInt(assembledFromMatcher.group(3));
                                visitor.visitAssembledFromLine(name, dir, fullRangeOffset);
                            } else {
                                Matcher readMatcher = READ_HEADER_PATTERN.matcher(line);
                                if (readMatcher.find()) {
                                    String readId = readMatcher.group(1);
                                    int fullLength = Integer.parseInt(readMatcher.group(2));
                                    visitor.visitReadHeader(readId, fullLength);
                                } else {
                                    Matcher qualityMatcher = QUALITY_PATTERN.matcher(line);
                                    if (qualityMatcher.find()) {
                                        int clearLeft = Integer.parseInt(qualityMatcher.group(1));
                                        int clearRight = Integer.parseInt(qualityMatcher.group(2));
                                        int alignLeft = Integer.parseInt(qualityMatcher.group(3));
                                        int alignRight = Integer.parseInt(qualityMatcher.group(4));
                                        visitor.visitQualityLine(clearLeft, clearRight, alignLeft, alignRight);
                                    } else {
                                        Matcher traceInfoMatcher = TRACE_DESCRIPTION_PATTERN.matcher(line);
                                        if (traceInfoMatcher.find()) {
                                            Matcher chromatogramMatcher = CHROMAT_FILE_PATTERN.matcher(line);
                                            if (!chromatogramMatcher.find()) {
                                                throw new IOException("could not parse chromatogram name from " + line);
                                            }
                                            String traceName = chromatogramMatcher.group(1);
                                            Matcher phdMatcher = PHD_FILE_PATTERN.matcher(line);
                                            String phdName;
                                            if (!phdMatcher.find()) {
                                                Matcher sffNameMatcher = SFF_CHROMATOGRAM_NAME_PATTERN.matcher(traceName);
                                                if (sffNameMatcher.find()) {
                                                    String sffRootName = sffNameMatcher.group(2);
                                                    final String group = sffNameMatcher.group(1);
                                                    boolean isForward = group.startsWith("-f:");
                                                    phdName = String.format("%s_%s", sffRootName, isForward ? "left" : "right");
                                                } else {
                                                    phdName = traceName;
                                                }
                                            } else {
                                                phdName = phdMatcher.group(1);
                                            }
                                            Matcher timeMatcher = TIME_PATTERN.matcher(line);
                                            if (!timeMatcher.find()) {
                                                throw new IOException("could not parse phd time stamp from " + line);
                                            }
                                            Date date = AceFileUtil.CHROMAT_DATE_TIME_FORMATTER.parseDateTime(timeMatcher.group(1)).toDate();
                                            visitor.visitTraceDescriptionLine(traceName, phdName, date);
                                        } else {
                                            Matcher readTag = BEGIN_READ_TAG_PATTERN.matcher(line);
                                            if (readTag.find()) {
                                                lineWithCR = parser.nextLine();
                                                visitor.visitLine(lineWithCR);
                                                Matcher readTagMatcher = READ_TAG_PATTERN.matcher(lineWithCR);
                                                if (!readTagMatcher.find()) {
                                                    throw new IllegalStateException("expected read tag infomration: " + lineWithCR);
                                                }
                                                String id = readTagMatcher.group(1);
                                                String type = readTagMatcher.group(2);
                                                String creator = readTagMatcher.group(3);
                                                long gappedStart = Long.parseLong(readTagMatcher.group(4));
                                                long gappedEnd = Long.parseLong(readTagMatcher.group(5));
                                                Date creationDate = AceFileUtil.TAG_DATE_TIME_FORMATTER.parseDateTime(readTagMatcher.group(6)).toDate();
                                                visitor.visitReadTag(id, type, creator, gappedStart, gappedEnd, creationDate, true);
                                                lineWithCR = parser.nextLine();
                                                visitor.visitLine(lineWithCR);
                                                if (!lineWithCR.startsWith("}")) {
                                                    throw new IllegalStateException("expected close read tag: " + lineWithCR);
                                                }
                                            } else {
                                                Matcher wholeAssemblyTag = BEGIN_WHOLE_ASSEMBLY_TAG_PATTERN.matcher(lineWithCR);
                                                if (wholeAssemblyTag.find()) {
                                                    lineWithCR = parser.nextLine();
                                                    visitor.visitLine(lineWithCR);
                                                    Matcher tagMatcher = WHOLE_ASSEMBLY_TAG_PATTERN.matcher(lineWithCR);
                                                    if (!tagMatcher.find()) {
                                                        throw new IllegalStateException("expected whole assembly tag information: " + lineWithCR);
                                                    }
                                                    String type = tagMatcher.group(1);
                                                    String creator = tagMatcher.group(2);
                                                    Date creationDate = AceFileUtil.TAG_DATE_TIME_FORMATTER.parseDateTime(tagMatcher.group(3)).toDate();
                                                    boolean doneTag = false;
                                                    StringBuilder data = new StringBuilder();
                                                    while (!doneTag && parser.hasNextLine()) {
                                                        lineWithCR = parser.nextLine();
                                                        visitor.visitLine(lineWithCR);
                                                        if (!lineWithCR.startsWith("}")) {
                                                            data.append(lineWithCR);
                                                        } else {
                                                            doneTag = true;
                                                        }
                                                    }
                                                    if (!doneTag) {
                                                        throw new IllegalStateException("unexpected EOF, Whole Assembly Tag not closed!");
                                                    }
                                                    visitor.visitWholeAssemblyTag(type, creator, creationDate, data.toString());
                                                } else {
                                                    Matcher consensusTag = BEGIN_CONSENSUS_TAG_PATTERN.matcher(lineWithCR);
                                                    if (consensusTag.find()) {
                                                        lineWithCR = parser.nextLine();
                                                        visitor.visitLine(lineWithCR);
                                                        Matcher tagMatcher = CONSENSUS_TAG_PATTERN.matcher(lineWithCR);
                                                        if (!tagMatcher.find()) {
                                                            throw new IllegalStateException("expected read tag infomration: " + lineWithCR);
                                                        }
                                                        String id = tagMatcher.group(1);
                                                        String type = tagMatcher.group(2);
                                                        String creator = tagMatcher.group(3);
                                                        long gappedStart = Long.parseLong(tagMatcher.group(4));
                                                        long gappedEnd = Long.parseLong(tagMatcher.group(5));
                                                        Date creationDate = AceFileUtil.TAG_DATE_TIME_FORMATTER.parseDateTime(tagMatcher.group(6)).toDate();
                                                        boolean isTransient = tagMatcher.group(7) != null;
                                                        visitor.visitBeginConsensusTag(id, type, creator, gappedStart, gappedEnd, creationDate, isTransient);
                                                        boolean doneTag = false;
                                                        boolean inComment = false;
                                                        StringBuilder consensusComment = null;
                                                        while (!doneTag && parser.hasNextLine()) {
                                                            lineWithCR = parser.nextLine();
                                                            visitor.visitLine(lineWithCR);
                                                            if (lineWithCR.startsWith("COMMENT{")) {
                                                                inComment = true;
                                                                consensusComment = new StringBuilder();
                                                            } else {
                                                                if (inComment) {
                                                                    if (lineWithCR.startsWith("C}")) {
                                                                        visitor.visitConsensusTagComment(consensusComment.toString());
                                                                        inComment = false;
                                                                    } else {
                                                                        consensusComment.append(lineWithCR);
                                                                    }
                                                                } else if (!lineWithCR.startsWith("}")) {
                                                                    visitor.visitConsensusTagData(lineWithCR);
                                                                } else {
                                                                    doneTag = true;
                                                                }
                                                            }
                                                        }
                                                        if (!doneTag) {
                                                            throw new IllegalStateException("unexpected EOF, Consensus Tag not closed!");
                                                        }
                                                        visitor.visitEndConsensusTag();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        visitor.visitEndOfContig();
        visitor.visitEndOfFile();
    }

    private static boolean parseIsComplimented(final String group) {
        boolean complimented = group.equals("C");
        return complimented;
    }
}
