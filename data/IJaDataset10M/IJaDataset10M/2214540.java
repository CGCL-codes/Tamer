package fi.tkk.ics.hadoop.bam.custom.samtools;

import java.util.*;
import net.sf.picard.PicardException;

/**
 * Merges SAMFileHeaders that have the same sequences into a single merged header
 * object while providing read group translation for cases where read groups
 * clash across input headers.
 */
public class SamFileHeaderMerger {

    private final SAMFileHeader mergedHeader;

    private Collection<SAMFileReader> readers;

    private final Collection<SAMFileHeader> headers;

    private final Map<SAMFileHeader, Map<String, String>> samReadGroupIdTranslation = new IdentityHashMap<SAMFileHeader, Map<String, String>>();

    private boolean hasReadGroupCollisions = false;

    private boolean hasProgramGroupCollisions = false;

    private Map<SAMFileHeader, Map<String, String>> samProgramGroupIdTranslation = new IdentityHashMap<SAMFileHeader, Map<String, String>>();

    private boolean hasMergedSequenceDictionary = false;

    private final Map<SAMFileHeader, Map<Integer, Integer>> samSeqDictionaryIdTranslationViaHeader = new IdentityHashMap<SAMFileHeader, Map<Integer, Integer>>();

    private static final HeaderRecordFactory<SAMReadGroupRecord> READ_GROUP_RECORD_FACTORY = new HeaderRecordFactory<SAMReadGroupRecord>() {

        public SAMReadGroupRecord createRecord(String id, SAMReadGroupRecord srcReadGroupRecord) {
            return new SAMReadGroupRecord(id, srcReadGroupRecord);
        }
    };

    private static final HeaderRecordFactory<SAMProgramRecord> PROGRAM_RECORD_FACTORY = new HeaderRecordFactory<SAMProgramRecord>() {

        public SAMProgramRecord createRecord(String id, SAMProgramRecord srcProgramRecord) {
            return new SAMProgramRecord(id, srcProgramRecord);
        }
    };

    private static final Comparator<AbstractSAMHeaderRecord> RECORD_ID_COMPARATOR = new Comparator<AbstractSAMHeaderRecord>() {

        public int compare(AbstractSAMHeaderRecord o1, AbstractSAMHeaderRecord o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };

    /**
     * Create SAMFileHeader with additional information.  Required that sequence dictionaries agree.
     *
     * @param readers sam file readers to combine
     * @param sortOrder sort order new header should have
     * @deprecated replaced by SamFileHeaderMerger(Collection<SAMFileHeader>, SAMFileHeader.SortOrder, boolean)
     */
    @Deprecated
    public SamFileHeaderMerger(final Collection<SAMFileReader> readers, final SAMFileHeader.SortOrder sortOrder) {
        this(readers, sortOrder, false);
    }

    /**
     * Create SAMFileHeader with additional information.
     *
     * @param readers sam file readers to combine
     * @param sortOrder sort order new header should have
     * @param mergeDictionaries If true, merge sequence dictionaries in new header.  If false, require that
     * all input sequence dictionaries be identical.
     * @deprecated replaced by SamFileHeaderMerger(Collection<SAMFileHeader>, SAMFileHeader.SortOrder, boolean)
     */
    @Deprecated
    public SamFileHeaderMerger(final Collection<SAMFileReader> readers, final SAMFileHeader.SortOrder sortOrder, final boolean mergeDictionaries) {
        this(sortOrder, getHeadersFromReaders(readers), mergeDictionaries);
        this.readers = readers;
    }

    /**
     * Create SAMFileHeader with additional information..  This is the preferred constructor.
     *
     * @param sortOrder sort order new header should have
     * @param headers sam file headers to combine
     * @param mergeDictionaries If true, merge sequence dictionaries in new header.  If false, require that
     * all input sequence dictionaries be identical.
     */
    public SamFileHeaderMerger(final SAMFileHeader.SortOrder sortOrder, final Collection<SAMFileHeader> headers, final boolean mergeDictionaries) {
        this.headers = headers;
        this.mergedHeader = new SAMFileHeader();
        SAMSequenceDictionary sequenceDictionary;
        try {
            sequenceDictionary = getSequenceDictionary(headers);
            this.hasMergedSequenceDictionary = false;
        } catch (SequenceUtil.SequenceListsDifferException pe) {
            if (mergeDictionaries) {
                sequenceDictionary = mergeSequenceDictionaries(headers);
                this.hasMergedSequenceDictionary = true;
            } else {
                throw pe;
            }
        }
        this.mergedHeader.setSequenceDictionary(sequenceDictionary);
        for (final SAMProgramRecord program : mergeProgramGroups(headers)) {
            this.mergedHeader.addProgramRecord(program);
        }
        final List<SAMReadGroupRecord> readGroups = mergeReadGroups(headers);
        this.mergedHeader.setReadGroups(readGroups);
        this.mergedHeader.setGroupOrder(SAMFileHeader.GroupOrder.none);
        this.mergedHeader.setSortOrder(sortOrder);
        for (final SAMFileHeader header : headers) {
            for (final String comment : header.getComments()) {
                this.mergedHeader.addComment(comment);
            }
        }
    }

    private static List<SAMFileHeader> getHeadersFromReaders(Collection<SAMFileReader> readers) {
        List<SAMFileHeader> headers = new ArrayList<SAMFileHeader>(readers.size());
        for (SAMFileReader reader : readers) {
            headers.add(reader.getFileHeader());
        }
        return headers;
    }

    /**
     * Checks to see if there are clashes where different readers are using the same read
     * group IDs. If yes, then those IDs that collided are remapped.
     *
     * @param headers headers to combine
     * @return new list of read groups constructed from all the readers
     */
    private List<SAMReadGroupRecord> mergeReadGroups(final Collection<SAMFileHeader> headers) {
        final HashSet<String> idsThatAreAlreadyTaken = new HashSet<String>();
        final List<HeaderRecordAndFileHeader<SAMReadGroupRecord>> readGroupsToProcess = new LinkedList<HeaderRecordAndFileHeader<SAMReadGroupRecord>>();
        for (final SAMFileHeader header : headers) {
            for (final SAMReadGroupRecord readGroup : header.getReadGroups()) {
                if (!idsThatAreAlreadyTaken.add(readGroup.getId())) throw new PicardException("Input file: " + header + " contains more than one RG with the same id (" + readGroup.getId() + ")");
                readGroupsToProcess.add(new HeaderRecordAndFileHeader<SAMReadGroupRecord>(readGroup, header));
            }
            idsThatAreAlreadyTaken.clear();
        }
        final List<SAMReadGroupRecord> result = new LinkedList<SAMReadGroupRecord>();
        hasReadGroupCollisions = mergeHeaderRecords(readGroupsToProcess, READ_GROUP_RECORD_FACTORY, idsThatAreAlreadyTaken, samReadGroupIdTranslation, result);
        Collections.sort(result, RECORD_ID_COMPARATOR);
        return result;
    }

    /**
     * Checks to see if there are clashes where different readers are using the same program
     * group IDs. If yes, then those IDs that collided are remapped.
     *
     * @param headers headers to combine
     * @return new list of program groups constructed from all the readers
     */
    private List<SAMProgramRecord> mergeProgramGroups(final Collection<SAMFileHeader> headers) {
        final List<SAMProgramRecord> overallResult = new LinkedList<SAMProgramRecord>();
        final HashSet<String> idsThatAreAlreadyTaken = new HashSet<String>();
        List<HeaderRecordAndFileHeader<SAMProgramRecord>> programGroupsLeftToProcess = new LinkedList<HeaderRecordAndFileHeader<SAMProgramRecord>>();
        for (final SAMFileHeader header : headers) {
            for (final SAMProgramRecord programGroup : header.getProgramRecords()) {
                if (!idsThatAreAlreadyTaken.add(programGroup.getId())) throw new PicardException("Input file: " + header + " contains more than one PG with the same id (" + programGroup.getId() + ")");
                programGroupsLeftToProcess.add(new HeaderRecordAndFileHeader<SAMProgramRecord>(programGroup, header));
            }
            idsThatAreAlreadyTaken.clear();
        }
        List<HeaderRecordAndFileHeader<SAMProgramRecord>> currentProgramGroups = new LinkedList<HeaderRecordAndFileHeader<SAMProgramRecord>>();
        for (final Iterator<HeaderRecordAndFileHeader<SAMProgramRecord>> programGroupsLeftToProcessIterator = programGroupsLeftToProcess.iterator(); programGroupsLeftToProcessIterator.hasNext(); ) {
            final HeaderRecordAndFileHeader<SAMProgramRecord> pair = programGroupsLeftToProcessIterator.next();
            if (pair.getHeaderRecord().getAttribute(SAMProgramRecord.PREVIOUS_PROGRAM_GROUP_ID_TAG) == null) {
                programGroupsLeftToProcessIterator.remove();
                currentProgramGroups.add(pair);
            }
        }
        while (!currentProgramGroups.isEmpty()) {
            final List<SAMProgramRecord> currentResult = new LinkedList<SAMProgramRecord>();
            hasProgramGroupCollisions |= mergeHeaderRecords(currentProgramGroups, PROGRAM_RECORD_FACTORY, idsThatAreAlreadyTaken, samProgramGroupIdTranslation, currentResult);
            overallResult.addAll(currentResult);
            currentProgramGroups = translateIds(currentProgramGroups, samProgramGroupIdTranslation, false);
            programGroupsLeftToProcess = translateIds(programGroupsLeftToProcess, samProgramGroupIdTranslation, true);
            LinkedList<HeaderRecordAndFileHeader<SAMProgramRecord>> programGroupsToProcessNext = new LinkedList<HeaderRecordAndFileHeader<SAMProgramRecord>>();
            for (final Iterator<HeaderRecordAndFileHeader<SAMProgramRecord>> programGroupsLeftToProcessIterator = programGroupsLeftToProcess.iterator(); programGroupsLeftToProcessIterator.hasNext(); ) {
                final HeaderRecordAndFileHeader<SAMProgramRecord> pairLeftToProcess = programGroupsLeftToProcessIterator.next();
                final Object ppIdOfRecordLeftToProcess = pairLeftToProcess.getHeaderRecord().getAttribute(SAMProgramRecord.PREVIOUS_PROGRAM_GROUP_ID_TAG);
                for (final HeaderRecordAndFileHeader<SAMProgramRecord> justProcessedPair : currentProgramGroups) {
                    String idJustProcessed = justProcessedPair.getHeaderRecord().getId();
                    if (pairLeftToProcess.getFileHeader() == justProcessedPair.getFileHeader() && ppIdOfRecordLeftToProcess.equals(idJustProcessed)) {
                        programGroupsLeftToProcessIterator.remove();
                        programGroupsToProcessNext.add(pairLeftToProcess);
                        break;
                    }
                }
            }
            currentProgramGroups = programGroupsToProcessNext;
        }
        if (!programGroupsLeftToProcess.isEmpty()) {
            StringBuffer errorMsg = new StringBuffer(programGroupsLeftToProcess.size() + " program groups weren't processed. Do their PP ids point to existing PGs? \n");
            for (final HeaderRecordAndFileHeader<SAMProgramRecord> pair : programGroupsLeftToProcess) {
                SAMProgramRecord record = pair.getHeaderRecord();
                errorMsg.append("@PG ID:" + record.getProgramGroupId() + " PN:" + record.getProgramName() + " PP:" + record.getPreviousProgramGroupId() + "\n");
            }
            throw new PicardException(errorMsg.toString());
        }
        Collections.sort(overallResult, RECORD_ID_COMPARATOR);
        return overallResult;
    }

    /**
     * Utility method that takes a list of program groups and remaps all their
     * ids (including ppIds if requested) using the given idTranslationTable.
     *
     * NOTE: when remapping, this method creates new SAMProgramRecords and
     * doesn't mutate any records in the programGroups list.
     *
     * @param programGroups The program groups to translate.
     * @param idTranslationTable The translation table.
     * @param translatePpIds Whether ppIds should be translated as well.
     *
     * @return The list of translated records.
     */
    private List<HeaderRecordAndFileHeader<SAMProgramRecord>> translateIds(List<HeaderRecordAndFileHeader<SAMProgramRecord>> programGroups, Map<SAMFileHeader, Map<String, String>> idTranslationTable, boolean translatePpIds) {
        List<HeaderRecordAndFileHeader<SAMProgramRecord>> result = new LinkedList<HeaderRecordAndFileHeader<SAMProgramRecord>>();
        for (final HeaderRecordAndFileHeader<SAMProgramRecord> pair : programGroups) {
            final SAMProgramRecord record = pair.getHeaderRecord();
            final String id = record.getProgramGroupId();
            final String ppId = record.getAttribute(SAMProgramRecord.PREVIOUS_PROGRAM_GROUP_ID_TAG);
            final SAMFileHeader header = pair.getFileHeader();
            final Map<String, String> translations = idTranslationTable.get(header);
            SAMProgramRecord translatedRecord = null;
            if (translations != null) {
                String translatedId = translations.get(id);
                String translatedPpId = translatePpIds ? translations.get(ppId) : null;
                boolean needToTranslateId = translatedId != null && !translatedId.equals(id);
                boolean needToTranslatePpId = translatedPpId != null && !translatedPpId.equals(ppId);
                if (needToTranslateId && needToTranslatePpId) {
                    translatedRecord = new SAMProgramRecord(translatedId, record);
                    translatedRecord.setAttribute(SAMProgramRecord.PREVIOUS_PROGRAM_GROUP_ID_TAG, translatedPpId);
                } else if (needToTranslateId) {
                    translatedRecord = new SAMProgramRecord(translatedId, record);
                } else if (needToTranslatePpId) {
                    translatedRecord = new SAMProgramRecord(id, record);
                    translatedRecord.setAttribute(SAMProgramRecord.PREVIOUS_PROGRAM_GROUP_ID_TAG, translatedPpId);
                }
            }
            if (translatedRecord != null) {
                result.add(new HeaderRecordAndFileHeader<SAMProgramRecord>(translatedRecord, header));
            } else {
                result.add(pair);
            }
        }
        return result;
    }

    /**
     * Utility method for merging a List of AbstractSAMHeaderRecords. If it finds
     * records that have identical ids and attributes, it will collapse them
     * into one record. If it finds records that have identical ids but
     * non-identical attributes, this is treated as a collision. When collision happens,
     * the records' ids are remapped, and an old-id to new-id mapping is added to the idTranslationTable.
     *
     * NOTE: Non-collided records also get recorded in the idTranslationTable as
     * old-id to old-id. This way, an idTranslationTable lookup should never return null.
     *
     * @param headerRecords The header records to merge.
     * @param headerRecordFactory Constructs a specific subclass of AbstractSAMHeaderRecord.
     * @param idsThatAreAlreadyTaken If the id of a headerRecord matches an id in this set, it will be treated as a collision, and the headRecord's id will be remapped.
     * @param idTranslationTable When records collide, their ids are remapped, and an old-id to new-id
     *      mapping is added to the idTranslationTable. Non-collided records also get recorded in the idTranslationTable as
     *      old-id to old-id. This way, an idTranslationTable lookup should never return null.
     *
     * @param result The list of merged header records.
     *
     * @return True if there were collisions.
     */
    private <RecordType extends AbstractSAMHeaderRecord> boolean mergeHeaderRecords(final List<HeaderRecordAndFileHeader<RecordType>> headerRecords, HeaderRecordFactory<RecordType> headerRecordFactory, final HashSet<String> idsThatAreAlreadyTaken, Map<SAMFileHeader, Map<String, String>> idTranslationTable, List<RecordType> result) {
        final Map<String, Map<RecordType, List<SAMFileHeader>>> idToRecord = new HashMap<String, Map<RecordType, List<SAMFileHeader>>>();
        for (final HeaderRecordAndFileHeader<RecordType> pair : headerRecords) {
            final RecordType record = pair.getHeaderRecord();
            final SAMFileHeader header = pair.getFileHeader();
            final String recordId = record.getId();
            Map<RecordType, List<SAMFileHeader>> recordsWithSameId = idToRecord.get(recordId);
            if (recordsWithSameId == null) {
                recordsWithSameId = new LinkedHashMap<RecordType, List<SAMFileHeader>>();
                idToRecord.put(recordId, recordsWithSameId);
            }
            List<SAMFileHeader> fileHeaders = recordsWithSameId.get(record);
            if (fileHeaders == null) {
                fileHeaders = new LinkedList<SAMFileHeader>();
                recordsWithSameId.put(record, fileHeaders);
            }
            fileHeaders.add(header);
        }
        boolean hasCollisions = false;
        for (final Map.Entry<String, Map<RecordType, List<SAMFileHeader>>> entry : idToRecord.entrySet()) {
            final String recordId = entry.getKey();
            final Map<RecordType, List<SAMFileHeader>> recordsWithSameId = entry.getValue();
            for (Map.Entry<RecordType, List<SAMFileHeader>> recordWithUniqueAttr : recordsWithSameId.entrySet()) {
                final RecordType record = recordWithUniqueAttr.getKey();
                final List<SAMFileHeader> fileHeaders = recordWithUniqueAttr.getValue();
                String newId;
                if (!idsThatAreAlreadyTaken.contains(recordId)) {
                    newId = recordId;
                    idsThatAreAlreadyTaken.add(recordId);
                } else {
                    hasCollisions = true;
                    int idx = 1;
                    while (idsThatAreAlreadyTaken.contains(newId = recordId + "." + Integer.toString(idx++))) ;
                    idsThatAreAlreadyTaken.add(newId);
                }
                for (SAMFileHeader fileHeader : fileHeaders) {
                    Map<String, String> readerTranslationTable = idTranslationTable.get(fileHeader);
                    if (readerTranslationTable == null) {
                        readerTranslationTable = new HashMap<String, String>();
                        idTranslationTable.put(fileHeader, readerTranslationTable);
                    }
                    readerTranslationTable.put(recordId, newId);
                }
                result.add(headerRecordFactory.createRecord(newId, record));
            }
        }
        return hasCollisions;
    }

    /**
     * Get the sequences off the SAMFileHeader.  Throws runtime exception if the sequence
     * are different from one another.
     *
     * @param headers headers to pull sequences from
     * @return sequences from files.  Each file should have the same sequence
     */
    private SAMSequenceDictionary getSequenceDictionary(final Collection<SAMFileHeader> headers) {
        SAMSequenceDictionary sequences = null;
        for (final SAMFileHeader header : headers) {
            if (sequences == null) {
                sequences = header.getSequenceDictionary();
            } else {
                final SAMSequenceDictionary currentSequences = header.getSequenceDictionary();
                SequenceUtil.assertSequenceDictionariesEqual(sequences, currentSequences);
            }
        }
        return sequences;
    }

    /**
     * Get the sequences from the SAMFileHeader, and merge the resulting sequence dictionaries.
     *
     * @param headers headers to pull sequences from
     * @return sequences from files.  Each file should have the same sequence
     */
    private SAMSequenceDictionary mergeSequenceDictionaries(final Collection<SAMFileHeader> headers) {
        SAMSequenceDictionary sequences = new SAMSequenceDictionary();
        for (final SAMFileHeader header : headers) {
            final SAMSequenceDictionary currentSequences = header.getSequenceDictionary();
            sequences = mergeSequences(sequences, currentSequences);
        }
        createSequenceMapping(headers, sequences);
        return sequences;
    }

    /**
     * They've asked to merge the sequence headers.  What we support right now is finding the sequence name superset.
     *
     * @param mergeIntoDict the result of merging so far.  All SAMSequenceRecords in here have been cloned from the originals.
     * @param mergeFromDict A new sequence dictionary to merge into mergeIntoDict.
     * @return A new sequence dictionary that resulting from merging the two inputs.
     */
    private SAMSequenceDictionary mergeSequences(SAMSequenceDictionary mergeIntoDict, SAMSequenceDictionary mergeFromDict) {
        LinkedList<SAMSequenceRecord> holder = new LinkedList<SAMSequenceRecord>();
        LinkedList<SAMSequenceRecord> resultingDict = new LinkedList<SAMSequenceRecord>();
        for (final SAMSequenceRecord sequenceRecord : mergeIntoDict.getSequences()) {
            resultingDict.add(sequenceRecord);
        }
        int prevloc = -1;
        SAMSequenceRecord previouslyMerged = null;
        for (SAMSequenceRecord sequenceRecord : mergeFromDict.getSequences()) {
            int loc = getIndexOfSequenceName(resultingDict, sequenceRecord.getSequenceName());
            if (loc == -1) {
                holder.add(sequenceRecord.clone());
            } else if (prevloc > loc) {
                throw new PicardException("Cannot merge sequence dictionaries because sequence " + sequenceRecord.getSequenceName() + " and " + previouslyMerged.getSequenceName() + " are in different orders in two input sequence dictionaries.");
            } else {
                resultingDict.addAll(loc, holder);
                prevloc = loc + holder.size();
                previouslyMerged = sequenceRecord;
                holder.clear();
            }
        }
        if (holder.size() != 0) {
            resultingDict.addAll(holder);
        }
        return new SAMSequenceDictionary(resultingDict);
    }

    /**
     * Find sequence in list.
     * @param list List to search for the sequence name.
     * @param sequenceName Name to search for.
     * @return Index of SAMSequenceRecord with the given name in list, or -1 if not found.
     */
    private static int getIndexOfSequenceName(final List<SAMSequenceRecord> list, final String sequenceName) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).getSequenceName().equals(sequenceName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * create the sequence mapping.  This map is used to convert the unmerged header sequence ID's to the merged
     * list of sequence id's.
     * @param headers the collections of headers.
     * @param masterDictionary the superset dictionary we've created.
     */
    private void createSequenceMapping(final Collection<SAMFileHeader> headers, SAMSequenceDictionary masterDictionary) {
        LinkedList<String> resultingDictStr = new LinkedList<String>();
        for (SAMSequenceRecord r : masterDictionary.getSequences()) {
            resultingDictStr.add(r.getSequenceName());
        }
        for (final SAMFileHeader header : headers) {
            Map<Integer, Integer> seqMap = new HashMap<Integer, Integer>();
            SAMSequenceDictionary dict = header.getSequenceDictionary();
            for (SAMSequenceRecord rec : dict.getSequences()) {
                seqMap.put(rec.getSequenceIndex(), resultingDictStr.indexOf(rec.getSequenceName()));
            }
            this.samSeqDictionaryIdTranslationViaHeader.put(header, seqMap);
        }
    }

    /**
     * Returns the read group id that should be used for the input read and RG id.
     *
     * @deprecated replaced by getReadGroupId(SAMFileHeader, String)
     * */
    @Deprecated
    public String getReadGroupId(final SAMFileReader reader, final String originalReadGroupId) {
        return getReadGroupId(reader.getFileHeader(), originalReadGroupId);
    }

    /** Returns the read group id that should be used for the input read and RG id. */
    public String getReadGroupId(final SAMFileHeader header, final String originalReadGroupId) {
        return this.samReadGroupIdTranslation.get(header).get(originalReadGroupId);
    }

    /**
     * @param reader one of the input files
     * @param originalProgramGroupId a program group ID from the above input file
     * @return new ID from the merged list of program groups in the output file
     * @deprecated replaced by getProgramGroupId(SAMFileHeader, String)
     */
    @Deprecated
    public String getProgramGroupId(final SAMFileReader reader, final String originalProgramGroupId) {
        return getProgramGroupId(reader.getFileHeader(), originalProgramGroupId);
    }

    /**
     * @param header one of the input headers
     * @param originalProgramGroupId a program group ID from the above input file
     * @return new ID from the merged list of program groups in the output file
     */
    public String getProgramGroupId(final SAMFileHeader header, final String originalProgramGroupId) {
        return this.samProgramGroupIdTranslation.get(header).get(originalProgramGroupId);
    }

    /** Returns true if there are read group duplicates within the merged headers. */
    public boolean hasReadGroupCollisions() {
        return this.hasReadGroupCollisions;
    }

    /** Returns true if there are program group duplicates within the merged headers. */
    public boolean hasProgramGroupCollisions() {
        return hasProgramGroupCollisions;
    }

    /** @return if we've merged the sequence dictionaries, return true */
    public boolean hasMergedSequenceDictionary() {
        return hasMergedSequenceDictionary;
    }

    /** Returns the merged header that should be written to any output merged file. */
    public SAMFileHeader getMergedHeader() {
        return this.mergedHeader;
    }

    /** Returns the collection of readers that this header merger is working with. May return null.
     * @deprecated replaced by getHeaders()
     */
    @Deprecated
    public Collection<SAMFileReader> getReaders() {
        return this.readers;
    }

    /** Returns the collection of readers that this header merger is working with.
     */
    public Collection<SAMFileHeader> getHeaders() {
        return this.headers;
    }

    /**
     * returns the new mapping for a specified reader, given it's old sequence index
     * @param reader the reader
     * @param oldReferenceSequenceIndex the old sequence (also called reference) index
     * @return the new index value
     * @deprecated replaced by getMergedSequenceIndex(SAMFileHeader, Integer)
     */
    @Deprecated
    public Integer getMergedSequenceIndex(SAMFileReader reader, Integer oldReferenceSequenceIndex) {
        return this.getMergedSequenceIndex(reader.getFileHeader(), oldReferenceSequenceIndex);
    }

    /**
     * Another mechanism for getting the new sequence index, for situations in which the reader is not available.
     * Note that if the SAMRecord has already had its header replaced with the merged header, this won't work.
     * @param header The original header for the input record in question.
     * @param oldReferenceSequenceIndex The original sequence index.
     * @return the new index value that is compatible with the merged sequence index.
     */
    public Integer getMergedSequenceIndex(final SAMFileHeader header, Integer oldReferenceSequenceIndex) {
        final Map<Integer, Integer> mapping = this.samSeqDictionaryIdTranslationViaHeader.get(header);
        if (mapping == null) {
            throw new PicardException("No sequence dictionary mapping available for header: " + header);
        }
        final Integer newIndex = mapping.get(oldReferenceSequenceIndex);
        if (newIndex == null) {
            throw new PicardException("No mapping for reference index " + oldReferenceSequenceIndex + " from header: " + header);
        }
        return newIndex;
    }

    /**
     * Implementations of this interface are used by mergeHeaderRecords(..) to instantiate
     * specific subclasses of AbstractSAMHeaderRecord.
     */
    private static interface HeaderRecordFactory<RecordType extends AbstractSAMHeaderRecord> {

        /**
        * Constructs a new instance of RecordType.
        * @param id The id of the new record.
        * @param srcRecord Except for the id, the new record will be a copy of this source record.
        */
        public RecordType createRecord(final String id, RecordType srcRecord);
    }

    /**
     * Struct that groups together a subclass of AbstractSAMHeaderRecord with the
     * SAMFileHeader that it came from.
     */
    private static class HeaderRecordAndFileHeader<RecordType extends AbstractSAMHeaderRecord> {

        private RecordType headerRecord;

        private SAMFileHeader samFileHeader;

        public HeaderRecordAndFileHeader(RecordType headerRecord, SAMFileHeader samFileHeader) {
            this.headerRecord = headerRecord;
            this.samFileHeader = samFileHeader;
        }

        public RecordType getHeaderRecord() {
            return headerRecord;
        }

        public SAMFileHeader getFileHeader() {
            return samFileHeader;
        }
    }
}
