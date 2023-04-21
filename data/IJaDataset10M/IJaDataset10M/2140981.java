package org.jcvi.common.core.assembly.ace;

import java.util.Date;
import org.jcvi.common.core.Direction;
import org.jcvi.common.core.Range;
import org.jcvi.common.core.io.FileVisitor;
import org.jcvi.common.core.io.TextFileVisitor;

/**
 * {@code AceFileVisitor} is a {@link FileVisitor}
 * implementation for Ace Files.
 * <p/>
 * The {@link AceFileVisitor} will have the appropriate
 * visit methods called on it as an ace file is parsed/ traversed.
 * @author dkatzel
 *
 *
 */
public interface AceFileVisitor extends TextFileVisitor {

    /**
     * Visits the Ace File Header.
     * @param numberOfContigs number of contigs in this file.
     * @param totalNumberOfReads total number of reads in this file.
     */
    void visitHeader(int numberOfContigs, int totalNumberOfReads);

    /**
     * Should the given contig with the following
     * attributes get visited?
     * @param contigId the id of the contig visited.
     * @param numberOfBases number of bases in contig (Does this count bases
     * outside of valid range?)
     * @param numberOfReads number of reads in contig.
     * @param numberOfBaseSegments number of base segment lines
     * which indicate reads phrap has chosen to be the consensus
     * at a particular position.
     * @param isComplemented is this contig reverse complemented
     * @return {@code true} if this contig should be parsed; {@code false}
     * if this contig should be skipped.  If this contig is to be skipped,
     * then only {@link #visitLine(String)} and {@link #visitEndOfContig()} methods will get called
     * for this contig.
     */
    boolean shouldVisitContig(String contigId, int numberOfBases, int numberOfReads, int numberOfBaseSegments, boolean isComplemented);

    /**
     * Begin Visiting the current contig.  This method will
     * only be called if the previous call to {@link #shouldVisitContig(String, int, int, int, boolean)}
     * returns {@code true}.
     * @param contigId the id of the contig visited.
     * @param numberOfBases number of bases in contig (Does this count bases
     * outside of valid range?)
     * @param numberOfReads number of reads in contig.
     * @param numberOfBaseSegments number of base segment lines
     * which indicate reads phrap has chosen to be the consensus
     * at a particular position.
     * @param reverseComplemented is this contig reverse complemented
     */
    void visitBeginContig(String contigId, int numberOfBases, int numberOfReads, int numberOfBaseSegments, boolean reverseComplemented);

    /**
     * begin visiting consensus qualities.  This method will only
     * get called if the current contig is being parsed which is determined
     * by the return value of {@link #visitBeginContig(String, int, int, int, boolean)}.
     */
    void visitConsensusQualities();

    /**
     * AssembledFroms define the location of the 
     * read within a contig.  This method will only
     * get called if the current contig is being parsed which is determined
     * by the return value of {@link #visitBeginContig(String, int, int, int, boolean)}.
     * @param readId id of read.
     * @param dir {@link Direction} of read inside contig.
     * @param gappedStartOffset gapped start offset of read inside contig.
     */
    void visitAssembledFromLine(String readId, Direction dir, int gappedStartOffset);

    /**
     * Base Segments indicate reads phrap has chosen to be the consensus
     * at a particular position.  This method will only
     * get called if the current contig is being parsed which is determined
     * by the return value of {@link #visitBeginContig(String, int, int, int, boolean)}.
     * @param gappedConsensusRange range of consensus being defined.
     * @param readId read id that provides coverage at that range.
     */
    void visitBaseSegment(Range gappedConsensusRange, String readId);

    /**
     * Begin visiting a read.  This method will only
     * get called if the current contig is being parsed which is determined
     * by the return value of {@link #visitBeginContig(String, int, int, int, boolean)}.
     * @param readId id of read being visited.
     * @param gappedLength gapped length of read.
     */
    void visitReadHeader(String readId, int gappedLength);

    /**
     * Visit quality line of currently visited read.  This method will only
     * get called if the current contig is being parsed which is determined
     * by the return value of {@link #visitBeginContig(String, int, int, int, boolean)}.
     * @param qualLeft left position(1-based)  of clear range.
     * @param qualRight right position(1-based) of clear range.
     * @param alignLeft left alignment(1-based) position. 
     * @param alignRight right alignment(1-based) position.
     */
    void visitQualityLine(int qualLeft, int qualRight, int alignLeft, int alignRight);

    /**
     * Visit Trace Description line of currently visited read.
     * @param traceName name of trace file corresponding
     * to currently visited read.  This method will only
     * get called if the current contig is being parsed which is determined
     * by the return value of {@link #visitBeginContig(String, int, int, int, boolean)}.
     * @param phdName name of phd file.
     * @param date date phd file created.
     */
    void visitTraceDescriptionLine(String traceName, String phdName, Date date);

    /**
     * Visit a line of basecalls of currently visited read. A read 
     * probably has several lines of basecalls.  The characters in the bases
     * could be mixed case.  Consed differentiates high quality basecalls
     * vs low quality basecalls by using upper and lowercase letters respectively.
     * This method will only
     * get called if the current contig is being parsed which is determined
     * by the return value of {@link #visitBeginContig(String, int, int, int, boolean)}.
     * @param mixedCaseBasecalls (some of) the basecalls of the currently visited read
     * or consensus which might have both upper and lower case letters to denote
     * high vs low quality.
     * 
     */
    void visitBasesLine(String mixedCaseBasecalls);

    /**
     * Visit a tag that refers to data concerning a single read in the assembly.
     * @param id the read id.
     * @param type the type of this tag; will not have any whitespace.
     * @param creator the program or tool that generated this tag.
     * @param gappedStart the gapped start offset where this tag refers.  Depending
     * on what the tag is used for this could be consensus coordinates or read coordinates.
     * @param gappedEnd the gapped end offset where this tag refers.  Depending
     * on what the tag is used for this could be consensus coordinates or read coordinates.
     * @param creationDate the date that this tag was created.
     * @param isTransient a transient tag should not be preserved if the assembly
     * is re-assembled; returns {@code true} if transient; {@code false} otherwise.
     */
    void visitReadTag(String id, String type, String creator, long gappedStart, long gappedEnd, Date creationDate, boolean isTransient);

    /**
     * The current contig being visited contains no more data.
     * @return {@code true} if the ace file should keep parsing;
     * {@code false} otherwise.
     */
    boolean visitEndOfContig();

    /**
     * Begin to start visiting a consensus tag.  Consensus tags are multiple lines
     * and can contain nested comments inside them.  Any lines visited from now
     * until {@link #visitEndConsensusTag()} will be for lines inside this tag.
     * @param id the contig id.
     * @param type the type of this tag; will not have any whitespace.
     * @param creator the program or tool that generated this tag.
     * @param gappedStart the gapped start offset where this tag refers.  Depending
     * on what the tag is used for this could be consensus coordinates or read coordinates.
     * @param gappedEnd the gapped end offset where this tag refers.  Depending
     * on what the tag is used for this could be consensus coordinates or read coordinates.
     * @param creationDate the date that this tag was created.
     * @param isTransient a transient tag should not be preserved if the assembly
     * is re-assembled; returns {@code true} if transient; {@code false} otherwise.
     */
    void visitBeginConsensusTag(String id, String type, String creator, long gappedStart, long gappedEnd, Date creationDate, boolean isTransient);

    /**
     * The current consensus tag contains a comment (which might span multiple lines).
     * @param comment the full comment as a string.
     */
    void visitConsensusTagComment(String comment);

    /**
     * The current consensus tag contains a data.
     * @param data the data as a string.
     */
    void visitConsensusTagData(String data);

    /**
     * The current consensus tag has been completely parsed.
     */
    void visitEndConsensusTag();

    /**
     * Visit a tag that refers to data that concerns the entire ace file.  Example
     * whole assembly tags are locations to phd.balls that store the quality data
     * for all the reads in the assembly.
     * @param type the type of this tag; will not have any whitespace.
     * @param creator the program or tool that generated this tag.
     * @param creationDate the date that this tag was created.
     * @param data the actual content of this tag which could be a single line or span
     * multiple lines depending on what the tag is for.
     */
    void visitWholeAssemblyTag(String type, String creator, Date creationDate, String data);
}
