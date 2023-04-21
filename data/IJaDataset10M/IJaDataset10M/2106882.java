package org.rt.util;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.util.Iterator;

/**
 * Signs clear text files.
 */
public class ClearFileSigner {

    /**
     * Creates a clear text signed file.
     * The parameters <code>fileToBeSigned</code> and <code>signedFile</code>
     * should <b>NOT</b> point to the same file.
     * @param fileToBeSigned the unsigned file which we want to sign
     * @param keyFile the private key file
     * @param signedFile the file which will contain the signed text
     */
    @SuppressWarnings("unchecked")
    public static void signFile(File fileToBeSigned, File keyFile, File signedFile, char[] pass) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, PGPException, SignatureException {
        if (fileToBeSigned.equals(signedFile)) {
            throw new IOException("Source file and signed file are the same");
        }
        Security.addProvider(new BouncyCastleProvider());
        InputStream keyIn = PGPUtil.getDecoderStream(new FileInputStream(keyFile));
        OutputStream out = new FileOutputStream(signedFile);
        int digest = PGPUtil.SHA1;
        PGPSecretKey pgpSecKey = readSecretKey(keyIn);
        PGPPrivateKey pgpPrivKey = pgpSecKey.extractPrivateKey(pass, "BC");
        PGPSignatureGenerator sGen = new PGPSignatureGenerator(pgpSecKey.getPublicKey().getAlgorithm(), digest, "BC");
        PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();
        sGen.initSign(PGPSignature.CANONICAL_TEXT_DOCUMENT, pgpPrivKey);
        Iterator it = pgpSecKey.getPublicKey().getUserIDs();
        if (it.hasNext()) {
            spGen.setSignerUserID(false, (String) it.next());
            sGen.setHashedSubpackets(spGen.generate());
        }
        FileInputStream fIn = new FileInputStream(fileToBeSigned);
        ArmoredOutputStream aOut = new ArmoredOutputStream(out);
        aOut.beginClearText(digest);
        ByteArrayOutputStream lineOut = new ByteArrayOutputStream();
        int lookAhead = readInputLine(lineOut, fIn);
        processLine(aOut, sGen, lineOut.toByteArray());
        if (lookAhead != -1) {
            do {
                lookAhead = readInputLine(lineOut, lookAhead, fIn);
                sGen.update((byte) '\r');
                sGen.update((byte) '\n');
                processLine(aOut, sGen, lineOut.toByteArray());
            } while (lookAhead != -1);
        }
        aOut.endClearText();
        BCPGOutputStream bOut = new BCPGOutputStream(aOut);
        sGen.generate().encode(bOut);
        aOut.close();
        bOut.close();
        out.close();
    }

    /**
     * A simple routine that opens a key ring file and loads the first available key suitable for
     * signature generation.
     * 
     * @param in  stream to read the secret key ring collection from.
     * @return  a secret key.
     * @throws IOException on a problem with using the input stream.
     * @throws PGPException if there is an issue parsing the input stream.
     */
    @SuppressWarnings("unchecked")
    private static PGPSecretKey readSecretKey(InputStream in) throws IOException, PGPException {
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(in);
        PGPSecretKey key = null;
        Iterator rIt = pgpSec.getKeyRings();
        while (key == null && rIt.hasNext()) {
            PGPSecretKeyRing kRing = (PGPSecretKeyRing) rIt.next();
            Iterator kIt = kRing.getSecretKeys();
            while (key == null && kIt.hasNext()) {
                PGPSecretKey k = (PGPSecretKey) kIt.next();
                if (k.isSigningKey()) {
                    key = k;
                }
            }
        }
        if (key == null) {
            throw new IllegalArgumentException("Can't find signing key in key ring.");
        }
        return key;
    }

    private static int readInputLine(ByteArrayOutputStream bOut, InputStream fIn) throws IOException {
        bOut.reset();
        int lookAhead = -1;
        int ch;
        while ((ch = fIn.read()) >= 0) {
            bOut.write(ch);
            if (ch == '\r' || ch == '\n') {
                lookAhead = readPassedEOL(bOut, ch, fIn);
                break;
            }
        }
        return lookAhead;
    }

    private static int readInputLine(ByteArrayOutputStream bOut, int lookAhead, InputStream fIn) throws IOException {
        bOut.reset();
        int ch = lookAhead;
        do {
            bOut.write(ch);
            if (ch == '\r' || ch == '\n') {
                lookAhead = readPassedEOL(bOut, ch, fIn);
                break;
            }
        } while ((ch = fIn.read()) >= 0);
        if (ch < 0) {
            lookAhead = -1;
        }
        return lookAhead;
    }

    private static int readPassedEOL(ByteArrayOutputStream bOut, int lastCh, InputStream fIn) throws IOException {
        int lookAhead = fIn.read();
        if (lastCh == '\r' && lookAhead == '\n') {
            bOut.write(lookAhead);
            lookAhead = fIn.read();
        }
        return lookAhead;
    }

    private static void processLine(OutputStream aOut, PGPSignatureGenerator sGen, byte[] line) throws SignatureException, IOException {
        int length = getLengthWithoutWhiteSpace(line);
        if (length > 0) {
            sGen.update(line, 0, length);
        }
        aOut.write(line, 0, line.length);
    }

    private static int getLengthWithoutWhiteSpace(byte[] line) {
        int end = line.length - 1;
        while (end >= 0 && isWhiteSpace(line[end])) {
            end--;
        }
        return end + 1;
    }

    private static boolean isWhiteSpace(byte b) {
        return b == '\r' || b == '\n' || b == '\t' || b == ' ';
    }
}
