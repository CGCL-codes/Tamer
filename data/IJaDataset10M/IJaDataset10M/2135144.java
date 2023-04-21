package org.ldaptive.control;

import java.nio.ByteBuffer;
import org.ldaptive.LdapUtils;
import org.ldaptive.ResultCode;
import org.ldaptive.asn1.DERParser;
import org.ldaptive.asn1.DERPath;
import org.ldaptive.asn1.IntegerType;
import org.ldaptive.asn1.OctetStringType;
import org.ldaptive.asn1.ParseHandler;

/**
 * Response control for server side sorting. See RFC 2891. Control is defined
 * as:
 *
 * <pre>
       SortResult ::= SEQUENCE {
          sortResult  ENUMERATED {
              success                   (0), -- results are sorted
              operationsError           (1), -- server internal failure
              timeLimitExceeded         (3), -- timelimit reached before
                                             -- sorting was completed
              strongAuthRequired        (8), -- refused to return sorted
                                             -- results via insecure
                                             -- protocol
              adminLimitExceeded       (11), -- too many matching entries
                                             -- for the server to sort
              noSuchAttribute          (16), -- unrecognized attribute
                                             -- type in sort key
              inappropriateMatching    (18), -- unrecognized or
                                             -- inappropriate matching
                                             -- rule in sort key
              insufficientAccessRights (50), -- refused to return sorted
                                             -- results to this client
              busy                     (51), -- too busy to process
              unwillingToPerform       (53), -- unable to sort
              other                    (80)
              },
        attributeType [0] AttributeDescription OPTIONAL }
 * </pre>
 *
 * @author  Middleware Services
 * @version  $Revision: 2329 $ $Date: 2012-03-26 11:39:30 -0400 (Mon, 26 Mar 2012) $
 */
public class SortResponseControl extends AbstractControl implements ResponseControl {

    /** OID of this control. */
    public static final String OID = "1.2.840.113556.1.4.474";

    /** hash code seed. */
    private static final int HASH_CODE_SEED = 733;

    /** Result of the server side sorting. */
    private ResultCode sortResult;

    /** Failed attribute name. */
    private String attributeName;

    /** Default constructor. */
    public SortResponseControl() {
        super(OID);
    }

    /**
   * Creates a new sort response control.
   *
   * @param  critical  whether this control is critical
   */
    public SortResponseControl(final boolean critical) {
        super(OID, critical);
    }

    /**
   * Creates a new sort response control.
   *
   * @param  code  result of the sort
   * @param  critical  whether this control is critical
   */
    public SortResponseControl(final ResultCode code, final boolean critical) {
        super(OID, critical);
        setSortResult(code);
    }

    /**
   * Creates a new sort response control.
   *
   * @param  code  result of the sort
   * @param  attrName  name of the failed attribute
   * @param  critical  whether this control is critical
   */
    public SortResponseControl(final ResultCode code, final String attrName, final boolean critical) {
        super(OID, critical);
        setSortResult(code);
        setAttributeName(attrName);
    }

    /**
   * Returns the result code of the server side sort.
   *
   * @return  result code
   */
    public ResultCode getSortResult() {
        return sortResult;
    }

    /**
   * Sets the result code of the server side sort.
   *
   * @param  code  result code
   */
    public void setSortResult(final ResultCode code) {
        sortResult = code;
    }

    /**
   * Returns the attribute name that caused the sort to fail.
   *
   * @return  attribute name
   */
    public String getAttributeName() {
        return attributeName;
    }

    /**
   * Sets the attribute name that caused the sort to fail.
   *
   * @param  name  of an attribute
   */
    public void setAttributeName(final String name) {
        attributeName = name;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return LdapUtils.computeHashCode(HASH_CODE_SEED, getOID(), getCriticality(), sortResult, attributeName);
    }

    /**
   * Provides a descriptive string representation of this instance.
   *
   * @return  string representation
   */
    @Override
    public String toString() {
        return String.format("[%s@%d::criticality=%s, sortResult=%s, attributeName=%s]", getClass().getName(), hashCode(), getCriticality(), sortResult, attributeName);
    }

    /** {@inheritDoc} */
    @Override
    public void decode(final byte[] berValue) {
        final SortResponseHandler handler = new SortResponseHandler(this);
        final DERParser parser = new DERParser();
        parser.registerHandler(SortResponseHandler.RESULT_PATH, handler);
        parser.registerHandler(SortResponseHandler.ATTR_PATH, handler);
        parser.parse(ByteBuffer.wrap(berValue));
    }

    /** Parse handler implementation for the sort response control. */
    private static class SortResponseHandler implements ParseHandler {

        /** DER path to result code. */
        public static final DERPath RESULT_PATH = new DERPath("/SEQ/ENUM");

        /** DER path to attr value. */
        public static final DERPath ATTR_PATH = new DERPath("/SEQ/CTX[1]");

        /** Sort response control to configure with this handler. */
        private final SortResponseControl sortResponse;

        /**
     * Creates a new sort response handler.
     *
     * @param  control  to configure
     */
        public SortResponseHandler(final SortResponseControl control) {
            sortResponse = control;
        }

        /** {@inheritDoc} */
        @Override
        public void handle(final DERParser parser, final ByteBuffer encoded) {
            if (RESULT_PATH.equals(parser.getCurrentPath())) {
                final int resultValue = IntegerType.decode(encoded).intValue();
                final ResultCode rc = ResultCode.valueOf(resultValue);
                if (rc == null) {
                    throw new IllegalArgumentException("Unknown result code " + resultValue);
                }
                sortResponse.setSortResult(rc);
            } else if (ATTR_PATH.equals(parser.getCurrentPath())) {
                sortResponse.setAttributeName(OctetStringType.decode(encoded));
            }
        }
    }
}
