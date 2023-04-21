package cdc.standard.pbe;

import codec.*;
import codec.asn1.*;
import codec.x509.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import javax.crypto.spec.IvParameterSpec;

/**
* This class represents the parameters for the
* {@link cdc.standard.pbe.PBE2BasicCipher passphrase based encryption scheme 2}.
* <p>
*
* @author
* <a href="mailto:twahren@cdc.informatik.tu-darmstadt.de">Thomas Wahrenbruch</a>
*
* @version 0.01
*/
public class PBE2Parameters extends AlgorithmParametersSpi {

    /**
	* The reference to the AlgorithmIdentifier of the key derivation function.
	* @serial
	*/
    private AlgorithmIdentifier keyDerivationFunction_;

    /**
	* The reference to the AlgorithmIdentifier of the encryption scheme.
	* @serial
	*/
    private AlgorithmIdentifier encryptionScheme_;

    /**
	* The default constructor does nothing.
	* <p>
	*/
    public PBE2Parameters() {
    }

    /**
	* Outputs an ASN.1 sequence, representing the PBE2 parameters.
	*
	* <p>
	* @return the ASN.1 encoded parameters.
	* @throws IOException on encoding errors.
	*/
    public byte[] engineGetEncoded() throws IOException {
        try {
            ASN1PBE2Params asn1pbe2Params = new ASN1PBE2Params(keyDerivationFunction_, encryptionScheme_);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DEREncoder encoder = new DEREncoder(baos);
            asn1pbe2Params.encode(encoder);
            byte[] bs = baos.toByteArray();
            baos.close();
            return bs;
        } catch (ASN1Exception ae) {
            System.out.println("shouldn't happen");
            throw new IOException("error during decoding ASN1Parameters!");
        }
    }

    /**
	* Returns the parameters in their primary encoding format.
	* If format is null, the primary encoding format for parameters is used.
	* The primary encoding format for parameters is ASN.1.
	* Currently only the default encoding format is supported.
	* <p>
	* @return the parameters encoded using the ASN.1 encoding scheme.
	* @throws IOException on encoding errors.
	*/
    public byte[] engineGetEncoded(String format) throws IOException {
        if (format == null) return engineGetEncoded(); else throw new IOException("decoding error - " + format + " not supported.");
    }

    /**
	* Returns a (transparent) specification of this parameters object.
	* paramSpec identifies the specification class in which the parameters
	* should be returned. Currently only DSAParameterSpec is supported.
	* <p>
	* @param paramSpec the the specification class in which the parameters should be returned.
	* @return the parameter specification.
	* @throws InvalidParameterSpecException if the requested
	* parameter specification is inappropriate for this parameter object.
	*/
    public AlgorithmParameterSpec engineGetParameterSpec(Class paramSpec) throws InvalidParameterSpecException {
        if (PBE2ParameterSpec.class.isAssignableFrom(paramSpec)) {
            return new PBE2ParameterSpec(keyDerivationFunction_, encryptionScheme_);
        } else {
            throw new InvalidParameterSpecException("spec not supported");
        }
    }

    /**
	* Imports the specified parameters and decodes them according to the primary
	* decoding format for parameters. The primary decoding format for parameters is ASN.1.
	* <p>
	* @param enc the encoded parameters.
	* @throws IOException on decoding errors.
	*/
    public void engineInit(byte[] enc) throws IOException {
        try {
            ASN1PBE2Params asn1pbe2Params = new ASN1PBE2Params();
            ByteArrayInputStream bais = new ByteArrayInputStream(enc);
            asn1pbe2Params.decode(new DERDecoder(bais));
            bais.close();
            keyDerivationFunction_ = asn1pbe2Params.getKeyDerivationFunction();
            encryptionScheme_ = asn1pbe2Params.getEncryptionScheme();
        } catch (ASN1Exception ae) {
            throw new IOException("unable to decode parameters.");
        }
    }

    /**
	* Imports the parameters from enc and decodes them according
	* to the specified decoding format. If format is null, the primary
	* decoding format for parameters is used. The primary decoding format
	* is ASN.1. Currently only the default decoding format is supported.
	* <p>
	*
	* @param enc the encoded parameters.
	* @param format the name of the decoding format.
	* @throws IOException on decoding errors.
	*/
    public void engineInit(byte[] enc, String format) throws IOException {
        if (format == null) {
            engineInit(enc);
        } else {
            throw new IOException("decoding error - " + format + " not supported.");
        }
    }

    /**
	* Initializes this parameters object using the parameters specified in spec.
	* <p>
	* @param spec the parameter specification.
	* @throws InvalidParameterSpecException if the given parameter specification is
	* inappropriate for the initialization of this parameter object.
	*/
    public void engineInit(AlgorithmParameterSpec spec) throws InvalidParameterSpecException {
        if (spec instanceof PBE2ParameterSpec) {
            PBE2ParameterSpec pbe2ParamSpec = (PBE2ParameterSpec) spec;
            keyDerivationFunction_ = pbe2ParamSpec.getKeyDerivationFunction();
            encryptionScheme_ = pbe2ParamSpec.getEncryptionScheme();
        } else {
            throw new InvalidParameterSpecException("parameters not supported");
        }
    }

    /**
	* Returns a human readable form of the parameters.
	* <p>
	* @return a formatted string describing the parameters.
	*/
    public String engineToString() {
        return ("key derivation function OID: " + keyDerivationFunction_.getAlgorithmOID().toString() + "\n" + "encryption scheme OID:       " + encryptionScheme_.getAlgorithmOID().toString());
    }

    /**
	* Return the AlgorithmIdentifier of the encryption scheme.
	* <p>
	* @return the encryption scheme.
	*/
    public AlgorithmIdentifier getEncryptionScheme() {
        return encryptionScheme_;
    }

    /**
	* Return the AlgorithmIdentifier of the key derivation function.
	* <p>
	* @return the key derivation function.
	*/
    public AlgorithmIdentifier getKeyDerivationFunction() {
        return keyDerivationFunction_;
    }
}
