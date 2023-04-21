package misc;

import java.io.File;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import net.sf.samtools.AlignmentBlock;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;

public class SAMReader extends MappingResultIteratorAdaptor implements SamMappingResultIterator {

    public static String methodName = "SAM";

    private SAMFileReader ndataFile;

    private SAMRecordWrapper nextRec = null;

    private ArrayList<AlignmentRecord> nextMappedReadRecords = new ArrayList<AlignmentRecord>();

    private String readID = null;

    private int bestScore = 0;

    private int readLen = 0;

    private Iterator iteratorN = null;

    public SAMReader(String samFilename) throws FileNotFoundException {
        File inF = new File(samFilename);
        ndataFile = new SAMFileReader(inF);
        ndataFile.setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
        iteratorN = ndataFile.iterator();
        fetchNextSamRecordWithAlignment();
    }

    private void fetchNextMappedRead() {
        readID = nextRec.getSAMRecord().getReadName();
        readLen = nextRec.getReadLen();
        bestScore = 0;
        int matchMismatchSum = nextRec.getMatchMismatchSum();
        int score = matchMismatchSum - nextRec.getMismatchNum();
        if (score > bestScore) bestScore = score;
        nextMappedReadRecords = new ArrayList<AlignmentRecord>();
        nextMappedReadRecords.add(getAlignmentRecord(nextRec, score));
        while (fetchNextSamRecordWithAlignment() != null) {
            String nextReadID = nextRec.getSAMRecord().getReadName();
            if (nextReadID.equals(readID)) {
                matchMismatchSum = nextRec.getMatchMismatchSum();
                score = matchMismatchSum - nextRec.getMismatchNum();
                if (score > bestScore) bestScore = score;
                nextMappedReadRecords.add(getAlignmentRecord(nextRec, score));
            } else {
                break;
            }
        }
    }

    private SAMRecordWrapper fetchNextSamRecordWithAlignment() {
        boolean parseAble = false;
        while (parseAble == false && iteratorN.hasNext()) {
            parseAble = true;
            try {
                nextRec = new SAMRecordWrapper((SAMRecord) iteratorN.next());
                parseAble = !nextRec.getSAMRecord().getReadUnmappedFlag();
            } catch (Exception parseEx) {
                System.out.println(parseEx.toString());
                parseAble = false;
            }
        }
        if (parseAble) {
            return nextRec;
        } else {
            nextRec = null;
            return nextRec;
        }
    }

    private AlignmentRecord getAlignmentRecord(SAMRecordWrapper record, int score) {
        if (!(record.getSAMRecord().getCigarString().equals("*"))) {
            float identity = (float) score / readLen;
            String strand = "+";
            if (record.getSAMRecord().getReadNegativeStrandFlag()) strand = "-";
            String chr = record.getSAMRecord().getReferenceName().toLowerCase();
            int numBlocks = record.getSAMRecord().getAlignmentBlocks().size();
            List<AlignmentBlock> BlockList = record.getSAMRecord().getAlignmentBlocks();
            int blockSizes[] = new int[numBlocks];
            for (int i = 0; i < numBlocks; i++) {
                blockSizes[i] = (Integer) BlockList.get(i).getLength();
            }
            int qStarts[] = new int[numBlocks];
            qStarts = record.getqStarts();
            int tStarts[] = new int[numBlocks];
            for (int i = 0; i < numBlocks; i++) {
                tStarts[i] = (Integer) BlockList.get(i).getReferenceStart();
            }
            String MDtag = (String) record.getSAMRecord().getAttribute("MD");
            String readSEQ = record.getSAMRecord().getReadString();
            if (MDtag == null || readSEQ.equals("*")) {
                return new AlignmentRecord(identity, numBlocks, qStarts, blockSizes, chr, tStarts, blockSizes.clone(), strand);
            } else {
                String qSeqs[] = new String[numBlocks];
                for (int i = 0; i < numBlocks; i++) {
                    qSeqs[i] = readSEQ.substring(qStarts[i] - 1, qStarts[i] + blockSizes[i] - 1);
                }
                String SeqsMatchblocks = "";
                for (int i = 0; i < numBlocks; i++) {
                    SeqsMatchblocks += qSeqs[i];
                }
                Pattern pattern = Pattern.compile("(\\d+|A|T|C|G|N|\\^[A-Z]+)");
                Matcher matcher = pattern.matcher(MDtag);
                String MDelement = "";
                String refSEQ = "";
                int qIDX = 0;
                while (matcher.find()) {
                    MDelement = matcher.group();
                    if (MDelement.matches("\\d+")) {
                        refSEQ += SeqsMatchblocks.substring(qIDX, qIDX + Integer.parseInt(MDelement));
                        qIDX += Integer.parseInt(MDelement);
                    } else if (MDelement.matches("[A|T|C|G|N]")) {
                        refSEQ += MDelement;
                        qIDX++;
                    }
                }
                String tSeqs[] = new String[numBlocks];
                int tBlockStart = 1;
                for (int i = 0; i < numBlocks; i++) {
                    tSeqs[i] = refSEQ.substring(tBlockStart - 1, tBlockStart - 1 + blockSizes[i]);
                    tBlockStart += blockSizes[i];
                }
                return new SamAlignmentRecord(identity, numBlocks, qStarts, blockSizes, chr, tStarts, blockSizes.clone(), strand, qSeqs, tSeqs, record.getSAMRecord());
            }
        } else {
            return null;
        }
    }

    public String getReadID() {
        return readID;
    }

    public int getReadLength() {
        return readLen;
    }

    public float getBestIdentity() {
        return ((float) bestScore / (float) readLen);
    }

    public int getNumMatch() {
        return nextMappedReadRecords.size();
    }

    public boolean hasNext() {
        if (nextRec == null) {
            return false;
        } else {
            return true;
        }
    }

    public Object next() {
        fetchNextMappedRead();
        return nextMappedReadRecords;
    }

    public static void main(String args[]) throws IOException {
        for (SAMReader iterator = new SAMReader(args[0]); iterator.hasNext(); ) {
            ArrayList mappingRecords = (ArrayList) iterator.next();
            System.out.println(iterator.getReadID() + "\t" + iterator.getNumMatch() + "\t" + iterator.getBestIdentity());
        }
    }
}
