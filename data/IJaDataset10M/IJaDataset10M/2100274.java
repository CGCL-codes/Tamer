package org.jcvi.assembly.cas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.jcvi.assembly.AssemblyUtil;
import org.jcvi.assembly.cas.alignment.CasAlignment;
import org.jcvi.assembly.cas.alignment.CasAlignmentRegion;
import org.jcvi.assembly.cas.alignment.CasAlignmentRegionType;
import org.jcvi.assembly.cas.read.CasDataStoreFactory;
import org.jcvi.assembly.cas.read.LargeFastQCasDataStoreFactory;
import org.jcvi.assembly.cas.read.FastaCasDataStoreFactory;
import org.jcvi.assembly.cas.read.MultiCasDataStoreFactory;
import org.jcvi.assembly.cas.read.DefaultSffCasDataStoreFactory;
import org.jcvi.common.util.Range;
import org.jcvi.common.util.Range.CoordinateSystem;
import org.jcvi.datastore.DataStore;
import org.jcvi.datastore.DataStoreException;
import org.jcvi.fastX.fastq.FastQQualityCodec;
import org.jcvi.glyph.nuc.NucleotideDataStore;
import org.jcvi.glyph.nuc.NucleotideEncodedGlyphs;
import org.jcvi.glyph.nuc.NucleotideGlyph;
import org.jcvi.glyph.phredQuality.QualityDataStore;
import org.jcvi.util.MultipleWrapper;

public class CasPrintAlignedReads extends AbstractOnePassCasFileVisitor {

    private final CasIdLookup readNameLookup;

    private final CasIdLookup contigNameLookup;

    private final CasDataStoreFactory casDataStoreFactory;

    private List<NucleotideDataStore> ReferenceNucleotideDataStores = new ArrayList<NucleotideDataStore>();

    private List<NucleotideDataStore> nucleotideDataStores = new ArrayList<NucleotideDataStore>();

    private List<QualityDataStore> qualityDataStores = new ArrayList<QualityDataStore>();

    private long alignCount = 0;

    private long nonalignCount = 0;

    private final PrintWriter writer;

    /**
     * @param casNucleotideDataStoreFactory
     */
    public CasPrintAlignedReads(CasDataStoreFactory casDataStoreFactory, CasIdLookup contigNameLookup, CasIdLookup readNameLookup, PrintWriter writer) {
        this.casDataStoreFactory = casDataStoreFactory;
        this.writer = writer;
        this.contigNameLookup = contigNameLookup;
        this.readNameLookup = readNameLookup;
    }

    @Override
    public void visitContigFileInfo(CasFileInfo contigFileInfo) {
        super.visitContigFileInfo(contigFileInfo);
        for (String filePath : contigFileInfo.getFileNames()) {
            try {
                System.out.println(filePath);
                ReferenceNucleotideDataStores.add(casDataStoreFactory.getNucleotideDataStoreFor(filePath));
            } catch (Exception e) {
                throw new IllegalStateException("could not load read file: " + filePath, e);
            }
        }
    }

    @Override
    public void visitMatch(CasMatch match, long readCounter) {
        String readId = readNameLookup.getLookupIdFor(readCounter);
        long numberOfAlignments = match.getNumberOfReportedAlignments();
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("read %s\t", readId));
        if (numberOfAlignments == 0) {
            builder.append("didn't match");
        } else if (numberOfAlignments > 1) {
            builder.append("was multi match");
        } else {
            CasAlignment alignment = match.getChosenAlignment();
            long contigId = alignment.contigSequenceId();
            long offset = alignment.getStartOfMatch();
            long numberOfGaps = 0;
            long ungappedValidRangeLength = 0;
            boolean firstRegion = true;
            long readStartOffset = 0;
            for (CasAlignmentRegion region : alignment.getAlignmentRegions()) {
                CasAlignmentRegionType type = region.getType();
                if (firstRegion) {
                    firstRegion = false;
                    if (type == CasAlignmentRegionType.INSERT) {
                        readStartOffset += region.getLength();
                        offset += readStartOffset;
                        continue;
                    }
                }
                if (type == CasAlignmentRegionType.DELETION) {
                    numberOfGaps += region.getLength();
                } else {
                    ungappedValidRangeLength += region.getLength();
                }
            }
            final String contigName = contigNameLookup.getLookupIdFor(contigId);
            if (contigName == null) {
                System.out.println("contigName null for " + contigId);
            }
            Range validRange = Range.buildRangeOfLength(0, ungappedValidRangeLength).shiftRight(readStartOffset).convertRange(CoordinateSystem.RESIDUE_BASED);
            if (alignment.readIsReversed()) {
                try {
                    final NucleotideEncodedGlyphs fullRangeSequenceFor = getFullRangeSequenceFor(readId);
                    Range complimentedValidRange = AssemblyUtil.reverseComplimentValidRange(validRange, (int) fullRangeSequenceFor.getLength());
                    List<NucleotideGlyph> readBases = NucleotideGlyph.reverseCompliment(fullRangeSequenceFor.decode());
                    List<NucleotideGlyph> referenceBases = getContigRangeSequenceFor(contigName).decode(Range.buildRangeOfLength(offset, validRange.size()));
                    System.out.println(NucleotideGlyph.convertToString(referenceBases));
                    System.out.println(NucleotideGlyph.convertToString(readBases).substring((int) complimentedValidRange.getStart(), (int) complimentedValidRange.getEnd()));
                } catch (DataStoreException e) {
                    e.printStackTrace();
                }
            }
            long validLeft = validRange.getLocalStart();
            long validRight = validRange.getLocalEnd();
            if (alignment.readIsReversed()) {
                try {
                    int fullLength = (int) getFullRangeSequenceFor(readId).getLength();
                    validRange = AssemblyUtil.reverseComplimentValidRange(validRange, fullLength);
                    validLeft = validRange.getLocalEnd();
                    validRight = validRange.getLocalStart();
                } catch (DataStoreException e) {
                    throw new RuntimeException(e);
                }
            }
            builder.append(String.format("matched contig %s offset = %d read valid range = [%d , %d] gaps =%d", contigName, offset, validLeft, validRight, numberOfGaps));
        }
        writer.printf("%s%n", builder.toString());
        if (match.matchReported()) {
            alignCount++;
        } else {
            nonalignCount++;
        }
    }

    private NucleotideEncodedGlyphs getContigRangeSequenceFor(String readId) throws DataStoreException {
        for (DataStore<NucleotideEncodedGlyphs> fastaFiles : ReferenceNucleotideDataStores) {
            if (fastaFiles.contains(readId)) {
                return fastaFiles.get(readId);
            }
        }
        throw new IllegalArgumentException("read id " + readId + " not found");
    }

    private NucleotideEncodedGlyphs getFullRangeSequenceFor(String readId) throws DataStoreException {
        for (DataStore<NucleotideEncodedGlyphs> fastaFiles : nucleotideDataStores) {
            if (fastaFiles.contains(readId)) {
                return fastaFiles.get(readId);
            }
        }
        throw new IllegalArgumentException("read id " + readId + " not found");
    }

    @Override
    public void visitReadFileInfo(CasFileInfo readFileInfo) {
        super.visitReadFileInfo(readFileInfo);
        for (String filePath : readFileInfo.getFileNames()) {
            try {
                System.out.println(filePath);
                nucleotideDataStores.add(casDataStoreFactory.getNucleotideDataStoreFor(filePath));
                qualityDataStores.add(casDataStoreFactory.getQualityDataStoreFor(filePath));
            } catch (Exception e) {
                throw new IllegalStateException("could not load read file: " + filePath, e);
            }
        }
        super.visitReadFileInfo(readFileInfo);
    }

    @Override
    public void visitEndOfFile() {
        System.out.printf("# reads that aligned = %d%n#reads that didn't align = %d%n", alignCount, nonalignCount);
    }

    public static void main(String[] args) throws IOException {
        int cacheSize = 100;
        File casFile = new File(args[0]);
        FileOutputStream out = new FileOutputStream(new File(args[1]));
        MultiCasDataStoreFactory casDataStoreFactory = new MultiCasDataStoreFactory(new DefaultSffCasDataStoreFactory(), new FastaCasDataStoreFactory(EmptyCasTrimMap.getInstance(), cacheSize), new LargeFastQCasDataStoreFactory(FastQQualityCodec.ILLUMINA, cacheSize));
        AbstractDefaultCasFileLookup readIdLookup = new DefaultReadCasFileLookup();
        AbstractDefaultCasFileLookup referenceIdLookup = new DefaultReferenceCasFileLookup();
        CasParser.parseCas(casFile, MultipleWrapper.createMultipleWrapper(CasFileVisitor.class, readIdLookup, referenceIdLookup));
        CasFileVisitor visitor = new CasPrintAlignedReads(casDataStoreFactory, referenceIdLookup, readIdLookup, new PrintWriter(out, true));
        CasParser.parseCas(casFile, visitor);
    }
}
