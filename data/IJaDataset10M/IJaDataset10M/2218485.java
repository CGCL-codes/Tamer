package net.community.chest.mail.message;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.InvalidObjectException;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.io.SyncFailedException;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.util.InvalidPropertiesFormatException;
import java.util.Stack;
import net.community.chest.io.EOLStyle;
import net.community.chest.io.IOCopier;
import net.community.chest.io.output.NullOutputStream;
import net.community.chest.io.output.OutputStreamEmbedder;
import net.community.chest.mail.RFCMimeDefinitions;
import net.community.chest.mail.headers.RFCHdrLineBufParseResult;
import net.community.chest.mail.headers.RFCHeaderDefinitions;

/**
 * <P>Copyright GPLv2</P>
 *
 * @author Lyor G.
 * @since May 4, 2009 1:49:32 PM
 */
public class StructureParserOutputStream extends OutputStreamEmbedder {

    /**
	 * Offset within the stream
	 */
    protected long _streamOffset;

    /**
	 * Parts "saved" while sub-parts are parsed. The part at the top
	 * of the stack is the "parent" of all current sub-parts
	 */
    protected Stack<RFCMessageStructure> _topParts = new Stack<RFCMessageStructure>();

    /**
	 * Current parsed part
	 */
    protected RFCMessageStructure _curPart = new RFCMessageStructure();

    /**
	 * @return part ID of current part - null if error
	 */
    private String getCurrentPartId() {
        return (null == _curPart) ? null : _curPart.getPartId();
    }

    /**
	 * TRUE if currently handling part headers
	 */
    protected boolean _hdrsParse = true;

    /**
	 * Used to delimit parts if a MIME boundary is available
	 */
    protected RFCMIMEBoundaryDelimiter _mmDelimiter = new RFCMIMEBoundaryDelimiter();

    /**
	 * Extracts the message structure while at the same time writing the
	 * data to the supplied underlying output stream.
	 * @param os "real" underling stream to write data to (as well as
	 * extracting the message structure)
	 * @param realClose TRUE if calling (@link #close()) should also close
	 * the underlying output stream (if any) that was provided.
	 */
    public StructureParserOutputStream(OutputStream os, boolean realClose) {
        super(os, realClose);
        _curPart.setPartId("");
    }

    /**
	 * Extracts the message structure while at the same time writing the
	 * data to the supplied underlying output stream.
	 * @param os "real" underling stream to write data to (as well as
	 * extracting the message structure). <B>Note:</B> calling (@link #close())
	 * on this stream also closes the underlying supplied stream
	 * (unless (@link #setRealClosure(boolean)) was called beforehand)
	 * @see #StructureParserOutputStream(OutputStream, boolean)
	 */
    public StructureParserOutputStream(OutputStream os) {
        this(os, true);
    }

    /**
	 * Default constructor extracts the message structure without passing
	 * the data to any underlying output stream
	 */
    public StructureParserOutputStream() {
        this(new NullOutputStream());
    }

    /**
	 * Pushes the specified part to the parts stack
	 * @param curPart part to be pushed
	 * @return new part to be used as "current" (if current is root then root) - null if error
	 * @throws UnsupportedEncodingException if unable to set MIME boundary
	 * bytes - should NEVER happen since we use UTF-8 which is built-in
	 * into ALL JVMs as per Java specifications...
	 */
    private RFCMessageStructure pushPartsStack(final RFCMessageStructure curPart) throws UnsupportedEncodingException {
        if (null == curPart) return null;
        final String mmb = curPart.isMIMEBoundarySet() ? curPart.getMIMEBoundary() : curPart.extractMIMEBoundary(true);
        if ((mmb != null) && (mmb.length() > 0)) _mmDelimiter.setBoundary(mmb);
        if (_mmDelimiter.isBoundarySet()) _topParts.push(curPart);
        if (curPart.isRootPart() || curPart.isCompoundPart()) return curPart;
        final RFCMessageStructure newPart = new RFCMessageStructure();
        newPart.setPartId(RFCMessageStructure.getNextPartId(curPart));
        newPart.setParent(curPart);
        newPart.setHeadersStartOffset(_streamOffset);
        return newPart;
    }

    /**
	 * Starts an envelope part as a sub-part of the supplied part
	 * @param curPart parent part - assumed to be a message part (not checked)
	 * @return envelope part - null if error
	 */
    protected RFCMessageStructure startEnvelopePart(final RFCMessageStructure curPart) {
        if ((null == curPart) || (null == _topParts)) return null;
        _topParts.push(curPart);
        final RFCMessageStructure newPart = new RFCMessageStructure();
        newPart.setPartId(RFCMessageStructure.getEnvelopePartId(curPart));
        newPart.setParent(curPart);
        newPart.setHeadersStartOffset(_streamOffset);
        _mmDelimiter.reset();
        return newPart;
    }

    /**
	 * Finalizes an envelope part and restores the parent message
	 * @param curPart envelope part (not checked)
	 * @return parent message part - null if error
	 * @throws UnsupportedEncodingException if unable to set MIME boundary
	 * bytes - should NEVER happen since we use UTF-8 which is built-in
	 * into ALL JVMs as per Java specifications...
	 */
    protected RFCMessageStructure endEnvelopePart(RFCMessageStructure curPart) throws UnsupportedEncodingException {
        if ((null == curPart) || (null == _topParts) || _topParts.isEmpty()) return null;
        final RFCMessageStructure parent = _topParts.pop();
        if (null == parent) return null;
        parent.addSubPart(curPart, true);
        parent.setMIMEBoundary(curPart.getMIMEBoundary());
        parent.setDataStartOffset(curPart.getDataStartOffset());
        if (parent.isRootPart()) {
            parent.setMIMEType(curPart.getMIMEType());
            parent.setMIMESubType(curPart.getMIMESubType());
            parent.setHeadersStartOffset(curPart.getHeadersStartOffset());
            parent.setHeadersEndOffset(curPart.getHeadersEndOffset());
        }
        curPart.setDataEndOffset(curPart.getDataStartOffset());
        final String mmb = parent.getMIMEBoundary();
        if ((mmb != null) && (mmb.length() > 0)) _mmDelimiter.setBoundary(mmb);
        return parent;
    }

    /**
	 * @return TRUE if stop parsing - called during crucial stages only.
	 * Default=FALSE (i.e. ALWAYS keep parsing) - intended for special case
	 * overriding (use with GREAT(est) CARE)
	 */
    protected boolean clearXfer() {
        return false;
    }

    /**
	 * Processes headers data for the current part
	 * @param buf buffer to be processed
	 * @param offset offset of data in buffer
	 * @param len number of bytes
	 * @return processed length (<0 if error)
	 * @throws UnsupportedEncodingException if unable to set MIME boundary
	 * bytes - should NEVER happen since we use UTF-8 which is built-in
	 * into ALL JVMs as per Java specs...
	 */
    protected int processHeadersData(byte[] buf, int offset, int len) throws UnsupportedEncodingException {
        if (_curPart.isMsgPart() && (!_curPart.isEnvelopePartAvailable())) {
            if (null == (_curPart = startEnvelopePart(_curPart))) return Integer.MIN_VALUE;
        }
        final RFCHdrLineBufParseResult res = _curPart.parseHeadersData(buf, offset, len);
        if (res.getErrCode() != 0) return (res.getErrCode() > 0) ? (0 - res.getErrCode()) : res.getErrCode();
        if (res.isMoreDataRequired()) {
            _streamOffset += len;
            return len;
        }
        final int processedLen = (res.getOffset() - offset) + 1;
        _streamOffset += processedLen;
        if (res.isCRDetected()) _curPart.setHeadersEndOffset(_streamOffset - 2L); else _curPart.setHeadersEndOffset(_streamOffset - 1);
        _curPart.setDataStartOffset(_streamOffset);
        if (!_curPart.isContentTypeSet()) _curPart.extractContentType(true);
        if (_curPart.isEnvelopePart()) {
            if (null == (_curPart = endEnvelopePart(_curPart))) return Integer.MIN_VALUE;
        }
        _hdrsParse = false;
        if (clearXfer()) return processedLen;
        if (_curPart.isRootPart() || _curPart.isCompoundPart()) {
            if (_curPart.isEmbeddedMessagePart() && (!_curPart.isEnvelopePartAvailable())) {
                if (null == (_curPart = startEnvelopePart(_curPart))) return Integer.MIN_VALUE;
                _hdrsParse = true;
            } else {
                if (null == (_curPart = pushPartsStack(_curPart))) return Integer.MIN_VALUE;
            }
        }
        return processedLen;
    }

    /**
	 * Called when finished with the current part and we can add it as a
	 * sub-part of whatever part is now on the stack top (the "parent")
	 * @param curPart current part that needs be linked to its parent
	 * @param dataEndOffset to be used compound parts
	 * @return new part if successful (null otherwise)
	 * @throws UnsupportedEncodingException if unable to set MIME boundary
	 * bytes - should NEVER happen since we use UTF-8 which is built-in
	 * into ALL JVMs as per Java specs...
	 */
    private RFCMessageStructure popPartsStack(RFCMessageStructure curPart, long dataEndOffset) throws UnsupportedEncodingException {
        if ((null == _topParts) || _topParts.isEmpty()) return null;
        final RFCMessageStructure parent = _topParts.pop();
        if (null == parent) return null;
        parent.addSubPart(curPart, true);
        parent.setDataEndOffset(dataEndOffset);
        if (!parent.isRootPart()) {
            final RFCMessageStructure mimeParent = _topParts.peek();
            if (null == mimeParent) return null;
            _mmDelimiter.setBoundary(mimeParent.getMIMEBoundary());
        } else _mmDelimiter.setBoundary(parent.getMIMEBoundary());
        return parent;
    }

    /**
	 * Closes the part and generates a new one/or pops the parent if last part
	 * @param curPart part to be closed
	 * @param dataEndOffset to be used for last part of compound parts
	 * @param isLastPart TRUE if last one
	 * @return new part if successful (null otherwise)
	 * @throws UnsupportedEncodingException if unable to set MIME boundary
	 * bytes - should NEVER happen since we use UTF-8 which is built-in
	 * into ALL JVMs as per Java specs...
	 */
    protected RFCMessageStructure closePart(RFCMessageStructure curPart, long dataEndOffset, boolean isLastPart) throws UnsupportedEncodingException {
        if (isLastPart) return popPartsStack(curPart, dataEndOffset);
        if ((null == _topParts) || _topParts.isEmpty()) return null;
        final RFCMessageStructure parent = _topParts.peek();
        if (null == parent) return null;
        if (!parent.equals(curPart)) parent.addSubPart(curPart, true);
        final RFCMessageStructure newPart = new RFCMessageStructure();
        newPart.setPartId(RFCMessageStructure.getNextPartId(parent));
        newPart.setHeadersStartOffset(_streamOffset);
        _hdrsParse = true;
        return newPart;
    }

    /**
	 * Set to TRUE if currently processing a direct sub-part - i.e., one that
	 * does not have headers of its own, but "inherits" them from its container
	 */
    private boolean _directSubPart;

    /**
	 * Called to end the direct sub-part hunt mode
	 * @param curPart current (direct) sub-part
	 * @param dataEndOffset end of (direct) sub-part data
	 * @return new sub-part to be used for parsing from now on (null if error)
	 * @throws IOException internal parsing state error(s)
	 */
    private RFCMessageStructure endDirectSubPart(final RFCMessageStructure curPart, final long dataEndOffset) throws IOException {
        if (null == curPart) return curPart;
        if (!_directSubPart) throw new SyncFailedException("Direct sub-part not started for part ID=" + curPart.getPartId());
        final RFCMessageStructure parent = _topParts.isEmpty() ? null : _topParts.pop();
        if (null == parent) throw new StreamCorruptedException("No direct sub-part parent for ID=" + curPart.getPartId());
        final boolean isEmbedded = parent.isEmbeddedMessagePart(), isRoot = parent.isRootPart();
        if ((!isEmbedded) && (!isRoot)) throw new SyncFailedException("Parent of sub-part=" + curPart.getPartId() + " not a direct sub-part container: " + parent.getPartHeader(RFCHeaderDefinitions.stdContentTypeHdr));
        curPart.setDataEndOffset(dataEndOffset);
        parent.setDataEndOffset(dataEndOffset);
        parent.addSubPart(curPart, true);
        _directSubPart = false;
        return parent;
    }

    /**
	 * Processes data for the current part looking for the MIME boundary
	 * @param buf buffer to be processed
	 * @param offset offset of data in buffer
	 * @param len number of bytes
	 * @return processed length (<0 if error)
	 * @throws IOException if internal errors
	 */
    protected int processMIMEBoundaryData(byte[] buf, int offset, int len) throws IOException {
        final RFCHdrLineBufParseResult res = _mmDelimiter.processBuffer(buf, offset, len);
        if (res.getErrCode() != 0) return (res.getErrCode() > 0) ? (0 - res.getErrCode()) : res.getErrCode();
        if (res.isMoreDataRequired()) {
            _streamOffset += len;
            return len;
        }
        final int processedLen = (res.getOffset() - offset) + 1;
        _streamOffset += processedLen;
        long dtEnd = _streamOffset - 1L - _mmDelimiter.length() - RFCMimeDefinitions.MIMEBoundaryDelimsBytes.length;
        if (res.isCRDetected()) dtEnd--;
        if (_mmDelimiter.isLastBoundary()) dtEnd -= RFCMimeDefinitions.MIMEBoundaryDelimsBytes.length;
        if ((!_curPart.isRootPart()) && (!_curPart.isCompoundPart())) _curPart.setDataEndOffset(dtEnd);
        if (_directSubPart) {
            if (null == (_curPart = endDirectSubPart(_curPart, dtEnd))) return Integer.MIN_VALUE;
        }
        if (null == (_curPart = closePart(_curPart, dtEnd, _mmDelimiter.isLastBoundary()))) return Integer.MIN_VALUE;
        return processedLen;
    }

    /**
	 * Checks if the current parsing state requires starting a "direct"
	 * sub-part hunt mode. This check is called by the (@link #write(byte[], int, int))
	 * method if no current header/MIME/(other)direct sub-part hunt mode is on.
	 * @param curPart current part to be checked
	 * @param dataStartOffset data start offset of (direct) sub-part - if needed
	 * @return new current part to be used if direct sub-part handling is
	 * required - otherwise same as input (<B>Note:</B> it also sets the
	 * internal (@link #_directSubPart) flag to TRUE to indicate this mode).
	 * @throws IOException if internal parsing state mismatches found
	 */
    private RFCMessageStructure checkDirectSubPartStart(final RFCMessageStructure curPart, final long dataStartOffset) throws IOException {
        if (null == curPart) return curPart;
        if (_directSubPart) throw new SyncFailedException("Direct sub-part already started for part ID=" + curPart.getPartId());
        final boolean isEmbedded = curPart.isEmbeddedMessagePart(), isRoot = curPart.isRootPart();
        if ((!isEmbedded) && (!isRoot)) return curPart;
        final RFCMessageStructure envPart = curPart.getEnvelopePart();
        if (null == envPart) throw new StreamCorruptedException("No envelope for sub-part of ID=" + curPart.getPartId());
        final RFCMessageStructure newPart = new RFCMessageStructure();
        newPart.setParent(curPart);
        newPart.setPartId(RFCMessageStructure.getNextPartId(curPart));
        newPart.setHeadersStartOffset(envPart.getHeadersStartOffset());
        newPart.setHeadersEndOffset(envPart.getHeadersEndOffset());
        int nErr = newPart.copyHeaders(envPart, true);
        if (nErr != 0) throw new SyncFailedException("Failed (err=" + nErr + ") to copy content headers of part=" + curPart.getPartId());
        newPart.setMIMEType(envPart.getMIMEType());
        newPart.setMIMESubType(envPart.getMIMESubType());
        newPart.setDataStartOffset(dataStartOffset);
        if (isEmbedded) {
            RFCMessageStructure parent = curPart.getParent();
            if (null == parent) parent = _topParts.isEmpty() ? null : _topParts.peek();
            final String mmbPar = (null == parent) ? null : parent.getMIMEBoundary();
            if ((null == mmbPar) || (mmbPar.length() <= 0)) throw new SyncFailedException("No parent MIME boundary for direct sub-part of part=" + curPart.getPartId());
            _mmDelimiter.setBoundary(mmbPar);
        }
        _topParts.push(curPart);
        _directSubPart = true;
        return newPart;
    }

    /**
	 * Called to process data when hunting for direct sub-part end
	 * @param buf buffer to process
	 * @param offset offset in buffer to check the data
	 * @param len number of valid data bytes
	 * @return number of bytes successfully processed (<0 if error)
	 * @throws IOException if unable to process
	 */
    private int processDirectSubPartData(byte[] buf, int offset, int len) throws IOException {
        if (!_directSubPart) throw new StreamCorruptedException("Not direct sub-part data mode to process for ID=" + getCurrentPartId());
        if (_mmDelimiter.isBoundarySet()) return processMIMEBoundaryData(buf, offset, len);
        _streamOffset += len;
        return len;
    }

    @Override
    public void write(byte[] buf, int offset, int len) throws IOException {
        if (null == this.out) throw new IOException("No underlying output stream to write to");
        if (!clearXfer()) {
            if ((null == _topParts) || (null == _mmDelimiter) || (null == _curPart)) throw new EOFException("No parsing in progress to write");
            for (int curOffset = offset, remLen = len; (remLen > 0) && (!clearXfer()); ) {
                final int processedLen;
                if (_directSubPart) {
                    if ((processedLen = processDirectSubPartData(buf, curOffset, remLen)) < 0) throw new InvalidPropertiesFormatException("Cannot (err=" + processedLen + ") parse direct sub-part=" + getCurrentPartId() + " data");
                } else if (_hdrsParse) {
                    if ((processedLen = processHeadersData(buf, curOffset, remLen)) < 0) throw new InvalidPropertiesFormatException("Cannot (err=" + processedLen + ") parse part=" + getCurrentPartId() + " headers");
                } else if (_mmDelimiter.isBoundarySet()) {
                    if ((processedLen = processMIMEBoundaryData(buf, curOffset, remLen)) < 0) throw new UTFDataFormatException("Cannot (err=" + processedLen + ") parse part=" + getCurrentPartId() + " MIME data");
                } else {
                    final RFCMessageStructure newPart = checkDirectSubPartStart(_curPart, _streamOffset);
                    if (newPart != _curPart) {
                        if (null == (_curPart = newPart)) throw new InterruptedIOException("Failed to start direct sub-part of part=" + getCurrentPartId());
                        continue;
                    }
                    _streamOffset += remLen;
                    break;
                }
                remLen -= processedLen;
                curOffset += processedLen;
            }
        }
        this.out.write(buf, offset, len);
    }

    @Override
    public void flush() throws IOException {
        if ((null == _topParts) || (null == _mmDelimiter)) throw new EOFException("No parsing in progress to flush");
        if (null == this.out) throw new EOFException("No underlying output stream to flush");
        this.out.flush();
    }

    @Override
    public void close() throws IOException {
        if (null == _curPart) throw new IOException("No current top-level part on closure");
        if ((_topParts != null) && (!clearXfer())) {
            flush();
            if ((!_curPart.isRootPart()) && _mmDelimiter.isBoundarySet()) {
                write(EOLStyle.CRLF.getStyleBytes());
                _streamOffset -= 2L;
            }
            if (_directSubPart) {
                final String curId = getCurrentPartId();
                if (_mmDelimiter.isBoundarySet()) throw new SyncFailedException("MIME boundary still set on direct sub-part of " + curId);
                if (null == (_curPart = endDirectSubPart(_curPart, _streamOffset))) throw new SyncFailedException("cannot terminate direct sub-part of " + curId);
            }
            if (!_topParts.isEmpty()) throw new StreamCorruptedException("Mismatched parts depth");
            if (!_curPart.isRootPart()) throw new InvalidObjectException("Non-root top level part: " + _curPart.getPartId());
            final long hStart = _curPart.getHeadersStartOffset(), hEnd = _curPart.getHeadersEndOffset();
            if (hStart != 0L) throw new SyncFailedException("Bad/Illegal top-level headers start offset: " + hStart);
            if (hEnd > hStart) {
                final long dStart = _curPart.getDataStartOffset(), dEnd = _curPart.getDataEndOffset();
                if (dEnd < dStart) _curPart.setDataEndOffset(_streamOffset);
            } else _curPart.setHeadersEndOffset(_streamOffset);
            _topParts = null;
            _mmDelimiter = null;
        }
        super.close();
    }

    /**
	 * @return "top"-level message structure - may be called ONLY AFTER
	 * closing the stream to ensure final parsing (do NOT ignore the
	 * exception thrown by the "close" method), otherwise result is
	 * undefined (may be null or an invalid structure)
	 */
    public RFCMessageStructure getParsedStructure() {
        return (_topParts != null) ? null : _curPart;
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in) throws IOException {
        return parseInputStream(in, (-1L));
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param copySize if >=0 then number of bytes from input stream to parse.
	 * Otherwise, input stream is read till EOF
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, long copySize) throws IOException {
        return parseInputStream(in, IOCopier.DEFAULT_COPY_SIZE, copySize);
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param bufSize number of bytes to be used from the work buffer
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, int bufSize) throws IOException {
        return parseInputStream(in, bufSize, (-1L));
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param bufSize number of bytes to be used from the work buffer
	 * @param copySize if >=0 then number of bytes from input stream to parse.
	 * Otherwise, input stream is read till EOF
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, int bufSize, long copySize) throws IOException {
        return parseInputStream(in, new byte[bufSize], copySize);
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param workBuf work buffer to be used for processing - the large the better
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, byte[] workBuf) throws IOException {
        return parseInputStream(in, workBuf, (-1L));
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param workBuf work buffer to be used for processing - the large the better
	 * @param copySize if >=0 then number of bytes from input stream to parse.
	 * Otherwise, input stream is read till EOF
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, byte[] workBuf, long copySize) throws IOException {
        return parseInputStream(in, workBuf, 0, (null == workBuf) ? 0 : workBuf.length, copySize);
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param out parsing output stream - Note: closed by this call !!!
	 * @param bufSize number of bytes to be used from the work buffer
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, StructureParserOutputStream out, int bufSize) throws IOException {
        return parseInputStream(in, out, bufSize, (-1L));
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param out parsing output stream - Note: closed by this call !!!
	 * @param bufSize number of bytes to be used from the work buffer
	 * @param copySize if >=0 then number of bytes from input stream to parse.
	 * Otherwise, input stream is read till EOF
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, StructureParserOutputStream out, int bufSize, long copySize) throws IOException {
        return parseInputStream(in, out, new byte[bufSize], 0, bufSize, copySize);
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param out parsing output stream - Note: closed by this call !!!
	 * @param workBuf work buffer to be used for processing - the large the better
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, StructureParserOutputStream out, byte[] workBuf) throws IOException {
        return parseInputStream(in, out, workBuf, 0, (null == workBuf) ? 0 : workBuf.length);
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param workBuf work buffer to be used for processing - the large the better
	 * @param offset offset in work buffer where processing data may be stored
	 * @param bufSize number of bytes to be used from the work buffer
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, byte[] workBuf, int offset, int bufSize) throws IOException {
        return parseInputStream(in, workBuf, offset, bufSize, (-1L));
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param workBuf work buffer to be used for processing - the large the better
	 * @param offset offset in work buffer where processing data may be stored
	 * @param bufSize number of bytes to be used from the work buffer
	 * @param copySize if >=0 then number of bytes from input stream to parse.
	 * Otherwise, input stream is read till EOF
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, byte[] workBuf, int offset, int bufSize, long copySize) throws IOException {
        return parseInputStream(in, new StructureParserOutputStream(), workBuf, offset, bufSize, copySize);
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param out parsing output stream - Note: closed by this call !!!
	 * @param workBuf work buffer to be used for processing - the large the better
	 * @param offset offset in work buffer where processing data may be stored
	 * @param bufSize number of bytes to be used from the work buffer
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, StructureParserOutputStream out, byte[] workBuf, int offset, int bufSize) throws IOException {
        return parseInputStream(in, out, workBuf, offset, bufSize, (-1L));
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param out parsing output stream - Note: closed by this call !!!
	 * @param workBuf work buffer to be used for processing - the large the better
	 * @param offset offset in work buffer where processing data may be stored
	 * @param bufSize number of bytes to be used from the work buffer
	 * @param copySize if >=0 then number of bytes from input stream to parse.
	 * Otherwise, input stream is read till EOF
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(final InputStream in, final StructureParserOutputStream out, byte[] workBuf, int offset, int bufSize, long copySize) throws IOException {
        final long cpyLen = IOCopier.copyStreams(in, out, workBuf, offset, bufSize, copySize);
        if (cpyLen < 0L) throw new IOException("MIME stream copying failed: " + cpyLen);
        out.close();
        final RFCMessageStructure rootPart = out.getParsedStructure();
        if (null == rootPart) throw new IOException("No data parsed");
        return rootPart;
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param out parsing stream
	 * @param copySize if >=0 then number of bytes from input stream to parse.
	 * Otherwise, input stream is read till EOF
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, StructureParserOutputStream out, long copySize) throws IOException {
        return StructureParserOutputStream.parseInputStream(in, out, IOCopier.DEFAULT_COPY_SIZE, copySize);
    }

    /**
	 * Parses the input stream in an attempt to get a message structure from it
	 * @param in input stream to read from
	 * @param out parsing stream
	 * @return message structure
	 * @throws IOException if unable to extract message structure
	 */
    public static final RFCMessageStructure parseInputStream(InputStream in, StructureParserOutputStream out) throws IOException {
        return parseInputStream(in, out, (-1L));
    }
}
