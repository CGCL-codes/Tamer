package com.unboundid.asn1;

import com.unboundid.util.NotMutable;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;
import static com.unboundid.asn1.ASN1Constants.*;
import static com.unboundid.asn1.ASN1Messages.*;
import static com.unboundid.util.Debug.*;

/**
 * This class provides an ASN.1 long element.  It is exactly the same as an
 * ASN.1 integer, but it uses a 64-bit representation for the decoded value
 * rather than a 32-bit representation.
 */
@NotMutable()
@ThreadSafety(level = ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class ASN1Long extends ASN1Element {

    /**
   * The serial version UID for this serializable class.
   */
    private static final long serialVersionUID = -3445506299288414013L;

    private final long longValue;

    /**
   * Creates a new ASN.1 long element with the default BER type and the
   * provided long value.
   *
   * @param  longValue  The long value to use for this element.
   */
    public ASN1Long(final long longValue) {
        super(UNIVERSAL_INTEGER_TYPE, encodeLongValue(longValue));
        this.longValue = longValue;
    }

    /**
   * Creates a new ASN.1 long element with the specified BER type and the
   * provided long value.
   *
   * @param  type       The BER type to use for this element.
   * @param  longValue  The long value to use for this element.
   */
    public ASN1Long(final byte type, final long longValue) {
        super(type, encodeLongValue(longValue));
        this.longValue = longValue;
    }

    /**
   * Creates a new ASN.1 long element with the specified BER type and the
   * provided long and pre-encoded values.
   *
   * @param  type       The BER type to use for this element.
   * @param  longValue  The long value to use for this element.
   * @param  value      The pre-encoded value to use for this element.
   */
    private ASN1Long(final byte type, final long longValue, final byte[] value) {
        super(type, value);
        this.longValue = longValue;
    }

    /**
   * Encodes the provided long value to a byte array suitable for use as the
   * value of a long element.
   *
   * @param  longValue  The long value to be encoded.
   *
   * @return  A byte array containing the encoded value.
   */
    static byte[] encodeLongValue(final long longValue) {
        if (longValue < 0) {
            if ((longValue & 0xFFFFFFFFFFFFFF80L) == 0xFFFFFFFFFFFFFF80L) {
                return new byte[] { (byte) (longValue & 0xFFL) };
            } else if ((longValue & 0xFFFFFFFFFFFF8000L) == 0xFFFFFFFFFFFF8000L) {
                return new byte[] { (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            } else if ((longValue & 0xFFFFFFFFFF800000L) == 0xFFFFFFFFFF800000L) {
                return new byte[] { (byte) ((longValue >> 16) & 0xFFL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            } else if ((longValue & 0xFFFFFFFF80000000L) == 0xFFFFFFFF80000000L) {
                return new byte[] { (byte) ((longValue >> 24) & 0xFFL), (byte) ((longValue >> 16) & 0xFFL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            } else if ((longValue & 0xFFFFFF8000000000L) == 0xFFFFFF8000000000L) {
                return new byte[] { (byte) ((longValue >> 32) & 0xFFL), (byte) ((longValue >> 24) & 0xFFL), (byte) ((longValue >> 16) & 0xFFL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            } else if ((longValue & 0xFFFF800000000000L) == 0xFFFF800000000000L) {
                return new byte[] { (byte) ((longValue >> 40) & 0xFFL), (byte) ((longValue >> 32) & 0xFFL), (byte) ((longValue >> 24) & 0xFFL), (byte) ((longValue >> 16) & 0xFFL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            } else if ((longValue & 0xFF80000000000000L) == 0xFF80000000000000L) {
                return new byte[] { (byte) ((longValue >> 48) & 0xFFL), (byte) ((longValue >> 40) & 0xFFL), (byte) ((longValue >> 32) & 0xFFL), (byte) ((longValue >> 24) & 0xFFL), (byte) ((longValue >> 16) & 0xFFL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            } else {
                return new byte[] { (byte) ((longValue >> 56) & 0xFFL), (byte) ((longValue >> 48) & 0xFFL), (byte) ((longValue >> 40) & 0xFFL), (byte) ((longValue >> 32) & 0xFFL), (byte) ((longValue >> 24) & 0xFFL), (byte) ((longValue >> 16) & 0xFFL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            }
        } else {
            if ((longValue & 0x000000000000007FL) == longValue) {
                return new byte[] { (byte) (longValue & 0x7FL) };
            } else if ((longValue & 0x0000000000007FFFL) == longValue) {
                return new byte[] { (byte) ((longValue >> 8) & 0x7FL), (byte) (longValue & 0xFFL) };
            } else if ((longValue & 0x00000000007FFFFFL) == longValue) {
                return new byte[] { (byte) ((longValue >> 16) & 0x7FL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            } else if ((longValue & 0x000000007FFFFFFFL) == longValue) {
                return new byte[] { (byte) ((longValue >> 24) & 0x7FL), (byte) ((longValue >> 16) & 0xFFL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            } else if ((longValue & 0x0000007FFFFFFFFFL) == longValue) {
                return new byte[] { (byte) ((longValue >> 32) & 0x7FL), (byte) ((longValue >> 24) & 0xFFL), (byte) ((longValue >> 16) & 0xFFL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            } else if ((longValue & 0x00007FFFFFFFFFFFL) == longValue) {
                return new byte[] { (byte) ((longValue >> 40) & 0x7FL), (byte) ((longValue >> 32) & 0xFFL), (byte) ((longValue >> 24) & 0xFFL), (byte) ((longValue >> 16) & 0xFFL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            } else if ((longValue & 0x007FFFFFFFFFFFFFL) == longValue) {
                return new byte[] { (byte) ((longValue >> 48) & 0x7FL), (byte) ((longValue >> 40) & 0xFFL), (byte) ((longValue >> 32) & 0xFFL), (byte) ((longValue >> 24) & 0xFFL), (byte) ((longValue >> 16) & 0xFFL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            } else {
                return new byte[] { (byte) ((longValue >> 56) & 0x7FL), (byte) ((longValue >> 48) & 0xFFL), (byte) ((longValue >> 40) & 0xFFL), (byte) ((longValue >> 32) & 0xFFL), (byte) ((longValue >> 24) & 0xFFL), (byte) ((longValue >> 16) & 0xFFL), (byte) ((longValue >> 8) & 0xFFL), (byte) (longValue & 0xFFL) };
            }
        }
    }

    /**
   * Retrieves the long value for this element.
   *
   * @return  The long value for this element.
   */
    public long longValue() {
        return longValue;
    }

    /**
   * Decodes the contents of the provided byte array as a long element.
   *
   * @param  elementBytes  The byte array to decode as an ASN.1 long element.
   *
   * @return  The decoded ASN.1 long element.
   *
   * @throws  ASN1Exception  If the provided array cannot be decoded as a long
   *                         element.
   */
    public static ASN1Long decodeAsLong(final byte[] elementBytes) throws ASN1Exception {
        try {
            int valueStartPos = 2;
            int length = (elementBytes[1] & 0x7F);
            if (length != elementBytes[1]) {
                final int numLengthBytes = length;
                length = 0;
                for (int i = 0; i < numLengthBytes; i++) {
                    length <<= 8;
                    length |= (elementBytes[valueStartPos++] & 0xFF);
                }
            }
            if ((elementBytes.length - valueStartPos) != length) {
                throw new ASN1Exception(ERR_ELEMENT_LENGTH_MISMATCH.get(length, (elementBytes.length - valueStartPos)));
            }
            final byte[] value = new byte[length];
            System.arraycopy(elementBytes, valueStartPos, value, 0, length);
            long longValue;
            switch(value.length) {
                case 1:
                    longValue = (value[0] & 0xFFL);
                    if ((value[0] & 0x80L) != 0x00L) {
                        longValue |= 0xFFFFFFFFFFFFFF00L;
                    }
                    break;
                case 2:
                    longValue = ((value[0] & 0xFFL) << 8) | (value[1] & 0xFFL);
                    if ((value[0] & 0x80L) != 0x00L) {
                        longValue |= 0xFFFFFFFFFFFF0000L;
                    }
                    break;
                case 3:
                    longValue = ((value[0] & 0xFFL) << 16) | ((value[1] & 0xFFL) << 8) | (value[2] & 0xFFL);
                    if ((value[0] & 0x80L) != 0x00L) {
                        longValue |= 0xFFFFFFFFFF000000L;
                    }
                    break;
                case 4:
                    longValue = ((value[0] & 0xFFL) << 24) | ((value[1] & 0xFFL) << 16) | ((value[2] & 0xFFL) << 8) | (value[3] & 0xFFL);
                    if ((value[0] & 0x80L) != 0x00L) {
                        longValue |= 0xFFFFFFFF00000000L;
                    }
                    break;
                case 5:
                    longValue = ((value[0] & 0xFFL) << 32) | ((value[1] & 0xFFL) << 24) | ((value[2] & 0xFFL) << 16) | ((value[3] & 0xFFL) << 8) | (value[4] & 0xFFL);
                    if ((value[0] & 0x80L) != 0x00L) {
                        longValue |= 0xFFFFFF0000000000L;
                    }
                    break;
                case 6:
                    longValue = ((value[0] & 0xFFL) << 40) | ((value[1] & 0xFFL) << 32) | ((value[2] & 0xFFL) << 24) | ((value[3] & 0xFFL) << 16) | ((value[4] & 0xFFL) << 8) | (value[5] & 0xFFL);
                    if ((value[0] & 0x80L) != 0x00L) {
                        longValue |= 0xFFFF000000000000L;
                    }
                    break;
                case 7:
                    longValue = ((value[0] & 0xFFL) << 48) | ((value[1] & 0xFFL) << 40) | ((value[2] & 0xFFL) << 32) | ((value[3] & 0xFFL) << 24) | ((value[4] & 0xFFL) << 16) | ((value[5] & 0xFFL) << 8) | (value[6] & 0xFFL);
                    if ((value[0] & 0x80L) != 0x00L) {
                        longValue |= 0xFF00000000000000L;
                    }
                    break;
                case 8:
                    longValue = ((value[0] & 0xFFL) << 56) | ((value[1] & 0xFFL) << 48) | ((value[2] & 0xFFL) << 40) | ((value[3] & 0xFFL) << 32) | ((value[4] & 0xFFL) << 24) | ((value[5] & 0xFFL) << 16) | ((value[6] & 0xFFL) << 8) | (value[7] & 0xFFL);
                    break;
                default:
                    throw new ASN1Exception(ERR_LONG_INVALID_LENGTH.get(value.length));
            }
            return new ASN1Long(elementBytes[0], longValue, value);
        } catch (final ASN1Exception ae) {
            debugException(ae);
            throw ae;
        } catch (final Exception e) {
            debugException(e);
            throw new ASN1Exception(ERR_ELEMENT_DECODE_EXCEPTION.get(e), e);
        }
    }

    /**
   * Decodes the provided ASN.1 element as a long element.
   *
   * @param  element  The ASN.1 element to be decoded.
   *
   * @return  The decoded ASN.1 long element.
   *
   * @throws  ASN1Exception  If the provided element cannot be decoded as a long
   *                         element.
   */
    public static ASN1Long decodeAsLong(final ASN1Element element) throws ASN1Exception {
        long longValue;
        final byte[] value = element.getValue();
        switch(value.length) {
            case 1:
                longValue = (value[0] & 0xFFL);
                if ((value[0] & 0x80L) != 0x00L) {
                    longValue |= 0xFFFFFFFFFFFFFF00L;
                }
                break;
            case 2:
                longValue = ((value[0] & 0xFFL) << 8) | (value[1] & 0xFFL);
                if ((value[0] & 0x80L) != 0x00L) {
                    longValue |= 0xFFFFFFFFFFFF0000L;
                }
                break;
            case 3:
                longValue = ((value[0] & 0xFFL) << 16) | ((value[1] & 0xFFL) << 8) | (value[2] & 0xFFL);
                if ((value[0] & 0x80L) != 0x00L) {
                    longValue |= 0xFFFFFFFFFF000000L;
                }
                break;
            case 4:
                longValue = ((value[0] & 0xFFL) << 24) | ((value[1] & 0xFFL) << 16) | ((value[2] & 0xFFL) << 8) | (value[3] & 0xFFL);
                if ((value[0] & 0x80L) != 0x00L) {
                    longValue |= 0xFFFFFFFF00000000L;
                }
                break;
            case 5:
                longValue = ((value[0] & 0xFFL) << 32) | ((value[1] & 0xFFL) << 24) | ((value[2] & 0xFFL) << 16) | ((value[3] & 0xFFL) << 8) | (value[4] & 0xFFL);
                if ((value[0] & 0x80L) != 0x00L) {
                    longValue |= 0xFFFFFF0000000000L;
                }
                break;
            case 6:
                longValue = ((value[0] & 0xFFL) << 40) | ((value[1] & 0xFFL) << 32) | ((value[2] & 0xFFL) << 24) | ((value[3] & 0xFFL) << 16) | ((value[4] & 0xFFL) << 8) | (value[5] & 0xFFL);
                if ((value[0] & 0x80L) != 0x00L) {
                    longValue |= 0xFFFF000000000000L;
                }
                break;
            case 7:
                longValue = ((value[0] & 0xFFL) << 48) | ((value[1] & 0xFFL) << 40) | ((value[2] & 0xFFL) << 32) | ((value[3] & 0xFFL) << 24) | ((value[4] & 0xFFL) << 16) | ((value[5] & 0xFFL) << 8) | (value[6] & 0xFFL);
                if ((value[0] & 0x80L) != 0x00L) {
                    longValue |= 0xFF00000000000000L;
                }
                break;
            case 8:
                longValue = ((value[0] & 0xFFL) << 56) | ((value[1] & 0xFFL) << 48) | ((value[2] & 0xFFL) << 40) | ((value[3] & 0xFFL) << 32) | ((value[4] & 0xFFL) << 24) | ((value[5] & 0xFFL) << 16) | ((value[6] & 0xFFL) << 8) | (value[7] & 0xFFL);
                break;
            default:
                throw new ASN1Exception(ERR_LONG_INVALID_LENGTH.get(value.length));
        }
        return new ASN1Long(element.getType(), longValue, value);
    }

    /**
   * Appends a string representation of this ASN.1 element to the provided
   * buffer.
   *
   * @param  buffer  The buffer to which to append the information.
   */
    @Override()
    public void toString(final StringBuilder buffer) {
        buffer.append(longValue);
    }
}
