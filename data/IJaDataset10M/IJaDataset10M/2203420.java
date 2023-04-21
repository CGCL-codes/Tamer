package org.ldaptive.asn1;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a SAX-like parsing facility for DER-encoded data where
 * elements of interest in the parse tree may be registered to handlers via the
 * {@link #registerHandler} methods.
 *
 * @author  Middleware Services
 * @version  $Revision: 2352 $ $Date: 2012-04-11 10:00:33 -0400 (Wed, 11 Apr 2012) $
 * @see  DERPath
 */
public class DERParser {

    /** Logger for this class. */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /** DER application tags. */
    private final Set<DERTag> applicationTags;

    /** Handlers for DER paths. */
    private final Map<DERPath, ParseHandler> handlerMap = new HashMap<DERPath, ParseHandler>();

    /** Current path location. */
    private DERPath currentPath;

    /** Default constructor. */
    public DERParser() {
        this(Collections.<DERTag>emptySet());
    }

    /**
   * See {@link #DERParser(Set)}.
   *
   * @param  applicationSpecificTags  list of application specific tags.
   */
    public DERParser(final DERTag... applicationSpecificTags) {
        this(new LinkedHashSet<DERTag>(Arrays.asList(applicationSpecificTags)));
    }

    /**
   * Creates a new parser that uses the given application-specific tags to
   * supplement universal tags during parsing. The order of tags is preserved in
   * the underlying collection, which is helpful for cases where different
   * contextual tags share the same tag number. In that case tags should be
   * provided in the order they are expected during parsing.
   *
   * @param  applicationSpecificTags  set of application specific tags.
   */
    public DERParser(final Set<DERTag> applicationSpecificTags) {
        applicationTags = applicationSpecificTags;
    }

    /**
   * See {@link #registerHandler(DERPath, ParseHandler)}.
   *
   * @param  path  to register
   * @param  handler  to associate with the path
   */
    public void registerHandler(final String path, final ParseHandler handler) {
        registerHandler(new DERPath(path), handler);
    }

    /**
   * Registers the supplied handler to fire when the supplied path is
   * encountered.
   *
   * @param  path  to register
   * @param  handler  to associate with the path
   */
    public void registerHandler(final DERPath path, final ParseHandler handler) {
        handlerMap.put(path, handler);
    }

    /**
   * Parse a DER-encoded data structure by calling registered handlers when
   * points of interest are encountered in the parse tree.
   *
   * @param  encoded  DER-encoded bytes.
   */
    public void parse(final ByteBuffer encoded) {
        currentPath = new DERPath();
        parseTags(encoded);
    }

    /**
   * Gets the current state of the parser in terms of the path that describes
   * the position in the parse tree currently visited by the parser.
   *
   * @return  current path in parse tree. Changes to the returned object do not
   * affect parser position.
   */
    public DERPath getCurrentPath() {
        return new DERPath(currentPath);
    }

    /**
   * Reads a DER tag from a single byte at the current position of the given
   * buffer. The buffer position is naturally advanced one byte in this
   * operation.
   *
   * @param  encoded  Buffer containing DER-encoded bytes positioned at tag.
   *
   * @return  Tag or null if no universal tag or application-specific tag is
   * known that matches the byte read in.
   */
    public DERTag readTag(final ByteBuffer encoded) {
        if (encoded.position() >= encoded.limit()) {
            return null;
        }
        DERTag tag;
        final byte b = encoded.get();
        final int tagNo = b & 0x1F;
        if (b >> 6 == 0) {
            tag = UniversalDERTag.fromTagNo(tagNo);
        } else {
            tag = lookupApplicationTag(tagNo);
            if (tag == null) {
                logger.debug("Unknown application tag " + tagNo);
            }
        }
        return tag;
    }

    /**
   * Reads the length of a DER-encoded value from the given byte buffer. The
   * buffer is expected to be positioned at the byte immediately following the
   * tag byte, which is where the length byte(s) begin(s). Invocation of this
   * method has two generally beneficial side effects:
   *
   * <ol>
   *   <li>Buffer is positioned at <em>start</em> of value bytes.</li>
   *   <li>Buffer limit is set to the <em>end</em> of value bytes.</li>
   * </ol>
   *
   * @param  encoded  buffer containing DER-encoded bytes positioned at start of
   * length byte(s).
   *
   * @return  number of bytes occupied by tag value.
   */
    public int readLength(final ByteBuffer encoded) {
        final byte b = encoded.get();
        if (b < 0x7F) {
            return b;
        } else {
            final int len = b & 0x7F;
            if (len > 0 && len < 0x7F) {
                encoded.limit(encoded.position() + len);
                return IntegerType.decode(encoded).intValue();
            } else {
                throw new IllegalArgumentException("Invalid length");
            }
        }
    }

    /**
   * Reads the supplied DER encoded bytes and invokes handlers as configured
   * paths are encountered.
   *
   * @param  encoded  to parse
   */
    private void parseTags(final ByteBuffer encoded) {
        int index = 0;
        while (encoded.position() < encoded.limit()) {
            final DERTag tag = readTag(encoded);
            if (tag != null) {
                currentPath.pushChild(tag.name(), index++);
                parseTag(tag, encoded);
                currentPath.popChild();
            }
        }
    }

    /**
   * Invokes the parse handler for the current path and advances to the next
   * position in the encoded bytes.
   *
   * @param  tag  to inspect for internal tags
   * @param  encoded  to parse
   */
    private void parseTag(final DERTag tag, final ByteBuffer encoded) {
        final int nextPos = readLength(encoded) + encoded.position();
        final ParseHandler handler = handlerMap.get(currentPath);
        if (handler != null) {
            encoded.limit(nextPos);
            handler.handle(this, encoded);
        }
        if (tag.isConstructed()) {
            parseTags(encoded);
        }
        encoded.position(nextPos);
        encoded.limit(encoded.capacity());
    }

    /**
   * Returns the application tag that matches the supplied tag number.
   *
   * @param  tagNo  to search for
   *
   * @return  application tag
   */
    private DERTag lookupApplicationTag(final int tagNo) {
        for (DERTag tag : applicationTags) {
            if (tagNo == tag.getTagNo()) {
                return tag;
            }
        }
        return null;
    }
}
