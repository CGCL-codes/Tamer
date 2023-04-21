package cdc.standard.rijndael;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import cdc.standard.spec.*;

/**
 * This class is used as an opaque representation of RIJNDAEL parameters.
 *
 * ASN.1/DER encoding and decoding are supported.
 * Parameters are encoded as :
 *
 *  RIJNDAEL-Parameter ::=
 *    CHOICE
 *    {
 *      iv IV,
 *      params SEQUENCE
 *      {
 *        keylength KeyLength,
 *        blocklength BlockLength,
 *        iv IV
 *      }
 *    }
 *
 *   where
 *
 *   IV ::= OCTET STRING -- 8 octets
 *   KeyLength ::= INTEGER -- 128, 192 or 256
 *   BlockLength ::= INTEGER -- 128, 192 or 256
 *
 * Note that only  16, 24, 32 bytes keylength are valid ones
 * and that the default keylength is 16.
 *
 * Note that only  16, 24, 32 bytes blocklength are valid ones
 * and that the default blocklength is 16.
 *
 * @see AlgorithmParameters
 * @see java.security.spec.AlgorithmParameterSpec
 *
 * @author  Katja Rauch
 * @version 1.0
 *
 */
public class RIJNDAELParameters extends AlgorithmParametersSpi {

    private AlgorithmParameterSpec algParamSpec;

    private byte[] parameterbyte;

    private int blocklength;

    private int keylength;

    private byte[] iv;

    /**
     * Initializes parameters object using the parameters
     * specified in <code>paramSpec</code>.
     *
     * @param paramSpec the parameter specification.
     *
     * @exception InvalidParameterSpecException if the given parameter
     * specification is inappropriate for the initialization of this parameter
     * object.
     */
    protected void engineInit(AlgorithmParameterSpec paramSpec) throws InvalidParameterSpecException {
        if (paramSpec == null || !(paramSpec instanceof RIJNDAELParameterSpec)) {
            throw new InvalidParameterSpecException("Invalid AlgorithmParameterSpec");
        } else {
            blocklength = ((RIJNDAELParameterSpec) paramSpec).getBlocklength();
            keylength = ((RIJNDAELParameterSpec) paramSpec).getKeylength();
            iv = ((RIJNDAELParameterSpec) paramSpec).getIV();
        }
    }

    /**
     * Imports the specified parameters and decodes them
     * according to the primary decoding format for parameters.
     * The primary decoding format for parameters is ASN.1.
     *
     * @param params the encoded parameters.
     *
     * @exception IOException on decoding errors
     */
    protected void engineInit(byte[] params) throws IOException {
        int i = 0;
        int n;
        if (params == null) throw new IOException("parameter is null");
        n = params.length;
        if (n != 18 && n != 23 && n != 26) throw new IOException("Bad encoding length (" + params.length + ")!");
        if (n == 26) {
            if (params[0] != 0x10 || params[1] != 24 || params[2] != 0x02 || params[3] != 1 || params[5] != 0x02 || params[6] != 1) throw new IOException("Bad encoding!");
            keylength = params[4];
            blocklength = params[7];
            i = 8;
        } else {
            if (n == 23) {
                if (params[0] != 0x10 || params[1] != 21 || params[2] != 0x02 || params[3] != 1) throw new IOException("Bad encoding!");
                keylength = params[4];
                blocklength = 16;
                i = 5;
            } else {
                keylength = 16;
                blocklength = 16;
            }
        }
        if (params[i] != 0x04 || params[i + 1] != 16) throw new IOException("Bad IV encoding!");
        iv = new byte[16];
        System.arraycopy(params, i + 2, iv, 0, 16);
    }

    /**
     * This Method is not implemented in RIJNDAELParameters
     * Please use <code>engineInit(byte[] params)</code>.
     *
     * @param params the encoded parameters.
     *
     * @param format the name of the decoding format.
     *
     * @exception IOException on decoding errors
     */
    protected void engineInit(byte[] params, String format) throws IOException {
        throw new IOException("not implemented");
    }

    /**
     * Returns a (transparent) specification of this parameters
     * object.
     * <code>paramSpec</code> identifies the specification class in which
     * the parameters should be returned. Here it has to be RIJNDAELParameterSpec.
     *
     * @param paramSpec the the specification class in which
     *                  the parameters should be returned.
     *
     * @return the parameter specification.
     *
     * @exception InvalidParameterSpecException if the requested parameter
     *            is not RIJNDAELParameterSpec.
     */
    protected AlgorithmParameterSpec engineGetParameterSpec(Class paramSpec) throws InvalidParameterSpecException {
        if (paramSpec != RIJNDAELParameterSpec.class) throw new InvalidParameterSpecException("Not a RIJNDAELParameterSpec.class");
        return new RIJNDAELParameterSpec(keylength, blocklength, iv);
    }

    /**
     * Returns the parameters in encoding format.
     *
     * @return the parameters encoded using the specified encoding scheme.
     *
     * @exception IOException on encoding errors.
     */
    protected byte[] engineGetEncoded() throws IOException {
        byte[] params;
        if (keylength == 16 && blocklength == 16) {
            params = new byte[18];
            params[0] = (byte) 0x04;
            params[1] = (byte) 16;
            System.arraycopy(iv, 0, params, 2, 16);
        } else {
            if (blocklength == 16) {
                params = new byte[23];
                params[0] = (byte) 0x10;
                params[1] = (byte) 21;
                params[2] = (byte) 0x02;
                params[3] = (byte) 1;
                params[4] = (byte) keylength;
                params[5] = (byte) 0x04;
                params[6] = (byte) 16;
                System.arraycopy(iv, 0, params, 7, 16);
            } else {
                params = new byte[26];
                params[0] = (byte) 0x10;
                params[1] = (byte) 24;
                params[2] = (byte) 0x02;
                params[3] = (byte) 1;
                params[4] = (byte) keylength;
                params[5] = (byte) 0x02;
                params[6] = (byte) 1;
                params[7] = (byte) blocklength;
                params[8] = (byte) 0x04;
                params[9] = (byte) 16;
                System.arraycopy(iv, 0, params, 10, 16);
            }
        }
        return params;
    }

    /**
     * This Method is not implemented in RIJNDAELParameters.
     * Please use <code>engineGetEncoded()</code>.
     *
     * @param format the name of the encoding format.
     *
     * @return the parameters encoded using the specified encoding scheme.
     *
     * @exception IOException on encoding errors.
     */
    protected byte[] engineGetEncoded(String format) throws IOException {
        throw new IOException("not implemented");
    }

    /**
     * Returns a formatted string describing the parameters.
     * This Method always returns null.
     *
     * @return a formatted string describing the parameters.
     */
    protected String engineToString() {
        int i;
        StringBuffer buf;
        buf = new StringBuffer(36);
        buf.append("RIJNDAELParameters (key length " + keylength + ")(block length " + blocklength + ")(IV");
        for (i = 0; i < iv.length; i++) {
            buf.append(" " + (iv[i] & 0xff));
        }
        buf.append(")");
        return buf.toString();
    }
}
