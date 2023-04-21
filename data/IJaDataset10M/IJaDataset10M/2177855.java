package it.southdown.avana.alignscan;

import it.southdown.avana.alignment.*;
import it.southdown.avana.metadata.*;
import java.util.*;

public class AlignmentScan {

    private Alignment alignment;

    private AlignmentScanConfig config;

    private AlignmentCursor cursor;

    private PositionData[] positionData;

    private Sequence consensus;

    private String name;

    /**
	 * Contructor: causes the creation of an alignment cursor 
	 * which includes a sequence cursor for each sequences, scanning 
	 * all the sequences in the alignment. It then computes the raw 
	 * position diversity statistics for this alignment.
	 * 
	 * @param alignment the alignment to be scanned
	 * @param config alignment scanning configuration
	 */
    public AlignmentScan(Alignment alignment, AlignmentScanConfig config) {
        this(new AlignmentCursor(alignment, config));
    }

    /**
	 * Creates a scan from a subset of sequences included in the current
	 * scan. The SubsetSelection input parameter indicates which sequences 
	 * must be selected for the subset. 
	 * 
	 * This operation automatically creates the appropriate SubsetAlignment 
	 * in the process. The alignment cursor of the resulting scan reuses 
	 * the sequence cursors for this scan, so that the sequences need not 
	 * be scanned again, which results in performance improvement.
	 * 
	 * @param SubsetSelection the alignment sequence selection criteria
	 */
    public AlignmentScan extractScan(SubsetSelection selection) {
        AlignmentCursor subCursor = cursor.extractCursor(selection);
        AlignmentScan newScan = new AlignmentScan(subCursor);
        return newScan;
    }

    public AlignmentScan extractScan(Alignment extAlignment) {
        AlignmentCursor subCursor = cursor.extractCursor(extAlignment);
        AlignmentScan newScan = new AlignmentScan(subCursor);
        return newScan;
    }

    public static AlignmentScan getMergedScan(Collection<AlignmentScan> scans) {
        ArrayList<AlignmentCursor> cursorsToMerge = new ArrayList<AlignmentCursor>();
        for (AlignmentScan sc : scans) {
            cursorsToMerge.add(sc.cursor);
        }
        AlignmentCursor mergedCursor = AlignmentCursor.getMergedCursor(cursorsToMerge);
        return new AlignmentScan(mergedCursor);
    }

    /**
	 * Main contructor. It computes the raw position diversity 
	 * statistics for this alignment.
	 * 
	 * @param alignment the alignment to be scanned
	 */
    private AlignmentScan(AlignmentCursor cursor) {
        this.cursor = cursor;
        this.alignment = cursor.getAlignment();
        this.name = alignment.getName();
        this.config = cursor.getAlignmentScanConfig();
        computePositionData();
    }

    /**
	 * Gets the id of this scan
	 * 
	 * @return the id (any string)
	 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Gets the sequence alignment used to perform the scan
	 * 
	 * @return the sequence alignment 
	 */
    public Alignment getAlignment() {
        return (alignment);
    }

    /**
	 * Get the count of sequences in the alignment
	 * 
	 * @return the count of sequences
	 */
    public int getSequenceCount() {
        return alignment.getSequenceCount();
    }

    /**
	 * Get the length of each sequence in the alignment
	 * 
	 * @return the sequence length
	 */
    public int getAlignmentLength() {
        return alignment.getAlignmentLength();
    }

    /**
	 */
    public AlignmentScanConfig getAlignmentScanConfig() {
        return config;
    }

    /**
	 */
    public void setAlignmentScanConfig(AlignmentScanConfig newConfig) {
        this.cursor.setAlignmentScanConfig(newConfig);
        this.config = newConfig;
        computePositionData();
    }

    public Metadata getPositionValuesAsMetadata(int pos) {
        this.cursor.setPosition(pos);
        return this.cursor.getPositionValuesAsMetadata();
    }

    /**
	 */
    protected AlignmentCursor getAlignmentCursor() {
        return cursor;
    }

    /**
	 * Gets all the scanned position data for each position in the alignment
	 * 
	 * @return an array of all scanned position data
	 */
    public PositionData[] getPositionData() {
        return (positionData);
    }

    /**
	 * Gets the scanned position data for a specific position in the alignment
	 * 
	 * @param pos the position within the alignment
	 * @return the scanned position data
	 */
    public PositionData getPositionData(int pos) {
        return (positionData[pos]);
    }

    /**
	 * Get the consunsus peptide sequence for this alignment
	 * 
	 * @return the consensus sequence
	 */
    public Sequence getConsensus() {
        return consensus;
    }

    /**
	 * Returns an array of booleans, one per sequence, that indicates
	 * which sequences contain the value specified, at the specified 
	 * position. Has to be an exact match. The boolean for the sequence 
	 * is true if the value is found.
	 * 
	 * @param value The value to be found
	 * @param pos The position to be searched
	 * @return the array of booleans, one per sequence
	 */
    public boolean[] findSequencesWithValue(String value, int pos) {
        cursor.setPosition(pos);
        return cursor.findSequencesWithValue(value);
    }

    /**
	 * Find a peptide in a given alignment, at any position.
	 * 
	 * @param searchString the peptide to be found
	 * @return a list of ids of sequences that contain the peptide
	 */
    public String[] findSequencesWithPeptide(String searchString) {
        return alignment.findSequencesWithPeptide(searchString);
    }

    /**
	 * Find a peptide in a given alignment, searching at a given specific position. 
	 * 
	 * @param searchString the peptide to be found
	 * @param pos the position at which to search
	 * @return a list of ids of sequences that contain the peptide
	 */
    public String[] findSequencesWithPeptide(String searchString, int pos) {
        cursor.setPosition(pos);
        return cursor.findSequencesWithPeptide(searchString);
    }

    public RegionData getRegionData(Region region) {
        RegionData regionData = new RegionData(region, this);
        cursor.setPosition(regionData.getRegionPosition());
        AlignmentScanConfig regionConfig = new AlignmentScanConfig(regionData.getRegionSize(), config.getHighGapPercentage(), config.getLowSupportPercentage());
        PositionData posData = cursor.computePositionData(regionConfig);
        Diversity regionDiversity = posData.getSampleDiversity();
        regionData.setRegionDiversity(regionDiversity);
        return regionData;
    }

    /**
	 * Finds a list of regions in the alignemtn that contain 
	 * a given peptide. The peptide length is must be the
	 * current scan length or greater.
	 * 
	 * @param peptide the peptide
	 * @return an array of Region objects (may be empty)
	 */
    public Region[] findPeptideRegions(String peptide) {
        int scanSampleSize = config.getSampleSize();
        int peptideSize = peptide.length();
        boolean sameLength = (scanSampleSize == peptideSize);
        String searchString = peptide;
        if (!sameLength) {
            searchString = trimPeptide(peptide, scanSampleSize);
        }
        ArrayList<PositionData> candidatePosList = new ArrayList<PositionData>();
        for (int i = 0; i < positionData.length; i++) {
            PositionData pos = positionData[i];
            Diversity sampleDiversity = pos.getSampleDiversity();
            if (sampleDiversity.containsValue(searchString)) {
                candidatePosList.add(pos);
            }
        }
        ArrayList<Region> regionList = new ArrayList<Region>();
        if (sameLength) {
            Iterator i = candidatePosList.iterator();
            while (i.hasNext()) {
                PositionData pos = (PositionData) i.next();
                Region region = makeRegion(pos.getPosition(), scanSampleSize, scanSampleSize);
                regionList.add(region);
            }
        } else {
            AlignmentScanConfig searchConfig = new AlignmentScanConfig(peptideSize, config.getHighGapPercentage(), config.getLowSupportPercentage());
            cursor.setAlignmentScanConfig(searchConfig);
            Iterator<PositionData> i = candidatePosList.iterator();
            while (i.hasNext()) {
                PositionData pos = i.next();
                cursor.setPosition(pos.getPosition());
                PositionData fullSizeData = cursor.getPositionData();
                if (fullSizeData.getSampleDiversity().containsValue(peptide)) {
                    Region region = makeRegion(pos.getPosition(), peptideSize, scanSampleSize);
                    regionList.add(region);
                }
            }
            cursor.setAlignmentScanConfig(config);
        }
        Region[] regions = new Region[regionList.size()];
        regions = regionList.toArray(regions);
        return regions;
    }

    /**
	 * Get the raw position statistics for this alignment (entropy and
	 * diversity). Note that this does not rescan the sequences, but
	 * just get the scanned values from the cursors.
	 */
    private void computePositionData() {
        int alignmentLength = cursor.getAlignmentLength();
        positionData = new PositionData[alignmentLength];
        StringBuffer consensusBuffer = new StringBuffer();
        for (int i = 0; i < alignmentLength; i++) {
            cursor.setPosition(i);
            positionData[i] = cursor.getPositionData();
            String consensusSymbol = cursor.getPositionData().getSymbolDiversity().getConsensus();
            if (consensusSymbol == null) {
                consensusSymbol = "-";
            }
            consensusBuffer.append(consensusSymbol);
        }
        consensus = new Sequence(getName(), consensusBuffer.toString());
    }

    private String trimPeptide(String peptide, int sampleSize) {
        int peptideSize = peptide.length();
        if (peptideSize <= sampleSize) {
            return peptide;
        }
        int offset = calculateOffset(peptideSize, sampleSize);
        return peptide.substring(offset, offset + sampleSize);
    }

    private int calculateOffset(int regionSize, int sampleSize) {
        int diff = regionSize - sampleSize;
        return (diff / 2);
    }

    private Region makeRegion(int position, int regionSize, int sampleSize) {
        int offset = calculateOffset(regionSize, sampleSize);
        int beginPos = position - offset;
        int endPos = beginPos + (regionSize - sampleSize);
        return new Region(beginPos, endPos);
    }
}
