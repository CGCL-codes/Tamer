package org.jcvi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jcvi.common.command.CommandLineOptionBuilder;
import org.jcvi.common.command.CommandLineUtils;
import org.jcvi.common.core.Direction;
import org.jcvi.common.core.Range;
import org.jcvi.common.core.assembly.ace.AbstractAceFileVisitor;
import org.jcvi.common.core.assembly.ace.AceContig;
import org.jcvi.common.core.assembly.ace.AceFileParser;
import org.jcvi.common.core.assembly.ace.AceFileWriter;
import org.jcvi.common.core.assembly.ace.DefaultAceContig;
import org.jcvi.common.core.assembly.ace.HiLowAceContigPhdDatastore;
import org.jcvi.common.core.assembly.ace.PhdInfo;
import org.jcvi.common.core.io.IOUtil;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequence;
import org.jcvi.common.io.idReader.DefaultFileIdReader;
import org.jcvi.common.io.idReader.IdReader;
import org.jcvi.common.io.idReader.IdReaderException;
import org.jcvi.common.io.idReader.StringIdParser;

public class PullOutMisAssembledAceReads {

    /**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
    public static void main(String[] args) throws IOException {
        Options options = new Options();
        options.addOption(new CommandLineOptionBuilder("a", "path to ace file to read").isRequired(true).longName("ace").build());
        options.addOption(new CommandLineOptionBuilder("o", "path to output ace file to write").isRequired(true).longName("out").build());
        options.addOption(new CommandLineOptionBuilder("i", "path to file containing reads to pull out").isRequired(true).build());
        options.addOption(CommandLineUtils.createHelpOption());
        if (CommandLineUtils.helpRequested(args)) {
            printHelp(options);
            System.exit(0);
        }
        try {
            CommandLine commandLine = CommandLineUtils.parseCommandLine(options, args);
            File readFile = new File(commandLine.getOptionValue("i"));
            Set<String> readIds = parseIdsFrom(readFile);
            File originalAceFile = new File(commandLine.getOptionValue("a"));
            ReadContigDetector detector = new ReadContigDetector(readIds);
            AceFileParser.parseAceFile(originalAceFile, detector);
            Map<String, Map<String, DirectedName>> map = detector.getMap();
            int numberOfReadsFound = 0;
            for (Entry<String, Map<String, DirectedName>> contigEntry : map.entrySet()) {
                numberOfReadsFound += contigEntry.getValue().size();
            }
            System.out.printf("will pull %d reads%n", numberOfReadsFound);
            OutputStream out = new FileOutputStream(new File(commandLine.getOptionValue("o")));
            AcePuller puller = new AcePuller(map, numberOfReadsFound, out);
            AceFileParser.parseAceFile(originalAceFile, puller);
            IOUtil.closeAndIgnoreErrors(out);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printHelp(options);
            System.exit(1);
        }
    }

    private static Set<String> parseIdsFrom(final File idFile) throws IdReaderException {
        IdReader<String> idReader = new DefaultFileIdReader<String>(idFile, new StringIdParser());
        Set<String> ids = new HashSet<String>();
        Iterator<String> iter = idReader.getIds();
        while (iter.hasNext()) {
            ids.add(iter.next());
        }
        return ids;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("pullOutMisAssembledReads -ace <ace file> -i <read ids file> -out <output ace>", "Make a new ace file from the input ace file but with the given " + "reads pulled out of their current contigs and placed in their own " + "individual degenerate contigs.", options, "Created by Danny Katzel");
    }

    private static final class ReadContigDetector extends AbstractAceFileVisitor {

        private final Set<String> readsToDetect;

        private final Map<String, Map<String, DirectedName>> map = new HashMap<String, Map<String, DirectedName>>();

        private String currentContigId = null;

        public ReadContigDetector(Set<String> readsToDetect) {
            this.readsToDetect = readsToDetect;
        }

        @Override
        protected void visitNewContig(String contigId, NucleotideSequence consensus, int numberOfBases, int numberOfReads, boolean isComplimented) {
            currentContigId = contigId;
        }

        @Override
        protected void visitAceRead(String readId, NucleotideSequence validBasecalls, int offset, Direction dir, Range validRange, PhdInfo phdInfo, int ungappedFullLength) {
            if (readsToDetect.contains(readId)) {
                if (!map.containsKey(currentContigId)) {
                    map.put(currentContigId, new HashMap<String, DirectedName>());
                }
                System.out.printf("found %s in contig %s%n", readId, currentContigId);
                map.get(currentContigId).put(readId, new DirectedName(readId, dir));
            }
        }

        public Map<String, Map<String, DirectedName>> getMap() {
            return map;
        }
    }

    private static final class DirectedName {

        private final String id;

        private final Direction dir;

        public DirectedName(String id, Direction dir) {
            super();
            this.id = id;
            this.dir = dir;
        }

        public String getId() {
            return id;
        }

        public Direction getDir() {
            return dir;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((dir == null) ? 0 : dir.hashCode());
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            DirectedName other = (DirectedName) obj;
            if (dir != other.dir) return false;
            if (id == null) {
                if (other.id != null) return false;
            } else if (!id.equals(other.id)) return false;
            return true;
        }
    }

    private static class AcePuller extends AbstractAceFileVisitor {

        private static final Pattern AF_LINE_PATTERN = Pattern.compile("^AF\\s+(\\S+)\\s+([U|C])\\s+(-?\\d+)");

        private StringBuilder currentReadRecord = null;

        private StringBuilder commentBuilder = new StringBuilder("\n");

        private boolean inComment = false;

        private StringBuilder newDegenerateContigsStringBuilder = new StringBuilder();

        private final Map<String, Map<String, DirectedName>> map;

        private final OutputStream out;

        private boolean shouldStreamThrough = false;

        private final int numberOfReadsToPull;

        private String currentContigId;

        public AcePuller(Map<String, Map<String, DirectedName>> map, int numberOfReadsToPull, OutputStream out) {
            this.map = map;
            this.numberOfReadsToPull = numberOfReadsToPull;
            this.out = out;
        }

        @Override
        public synchronized void visitEndOfFile() {
            try {
                out.write(newDegenerateContigsStringBuilder.toString().getBytes());
                out.write(commentBuilder.toString().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            super.visitEndOfFile();
        }

        @Override
        public synchronized void visitHeader(int numberOfContigs, int totalNumberOfReads) {
            try {
                AceFileWriter.writeAceFileHeader(numberOfContigs + numberOfReadsToPull, totalNumberOfReads, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public synchronized boolean shouldVisitContig(String contigId, int numberOfBases, int numberOfReads, int numberOfBaseSegments, boolean reverseComplimented) {
            currentReadRecord = null;
            currentContigId = contigId;
            shouldStreamThrough = !map.containsKey(contigId);
            if (map.containsKey(contigId)) {
                int numberOfReadsToRemove = map.get(contigId).size();
                try {
                    int numberOfReadsLeft = numberOfReads - numberOfReadsToRemove;
                    AceFileWriter.writeAceContigHeader(contigId, numberOfBases, numberOfReadsLeft, 0, reverseComplimented, out);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    AceFileWriter.writeAceContigHeader(contigId, numberOfBases, numberOfReads, 0, reverseComplimented, out);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        }

        @Override
        protected void visitNewContig(String contigId, NucleotideSequence consensus, int numberOfBases, int numberOfReads, boolean isComplimented) {
        }

        @Override
        public synchronized void visitLine(String line) {
            if (line.startsWith("BS ")) {
                super.visitLine(line);
                return;
            }
            if (line.startsWith("AF ")) {
                Matcher matcher = AF_LINE_PATTERN.matcher(line);
                if (matcher.find()) {
                    String readId = matcher.group(1);
                    if (map.containsKey(currentContigId) && map.get(currentContigId).containsKey(readId)) {
                        super.visitLine(line);
                        return;
                    }
                }
            }
            if (line.startsWith("WA{") || line.startsWith("CT{") || line.startsWith("RT{")) {
                this.inComment = true;
            }
            if (inComment) {
                commentBuilder.append(line);
                if (line.trim().equals("}")) {
                    inComment = false;
                }
            } else if (currentReadRecord == null) {
                if (line.startsWith("RD")) {
                    currentReadRecord = new StringBuilder(line);
                } else if (!line.startsWith("AS") && !line.startsWith("CO")) {
                    try {
                        out.write(line.getBytes(IOUtil.UTF_8));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                currentReadRecord.append(line);
            }
            super.visitLine(line);
        }

        @Override
        protected void visitAceRead(String readId, NucleotideSequence validBasecalls, int offset, Direction dir, Range validRange, PhdInfo phdInfo, int ungappedFullLength) {
            if (shouldStreamThrough || !map.get(currentContigId).containsKey(readId)) {
                try {
                    out.write(currentReadRecord.toString().getBytes(IOUtil.UTF_8));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
                try {
                    StringBuilder fakeAceText = new StringBuilder();
                    fakeAceText.append("AS 1 1\n\n\n");
                    fakeAceText.append("CO blah 1 1 0 U\n");
                    fakeAceText.append(String.format("AF %s %s 0\n", readId, dir == Direction.FORWARD ? "U" : "C"));
                    fakeAceText.append(currentReadRecord);
                    ByteArrayInputStream tempIn = new ByteArrayInputStream(fakeAceText.toString().getBytes());
                    HiLowAceContigPhdDatastore phds = HiLowAceContigPhdDatastore.create(tempIn);
                    AceContig tempContig = DefaultAceContig.createBuilder(readId + "_degen", validBasecalls).addRead(readId, validBasecalls, 0, dir, validRange, phdInfo, ungappedFullLength).build();
                    AceFileWriter.writeAceContig(tempContig, phds, tempOut);
                    newDegenerateContigsStringBuilder.append(new String(tempOut.toByteArray(), IOUtil.UTF_8));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            currentReadRecord = null;
        }
    }
}
