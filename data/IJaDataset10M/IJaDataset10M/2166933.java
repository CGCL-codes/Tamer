package net.sf.easymol.comm.bluetooth.linux;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.GZIPOutputStream;
import javax.bluetooth.DataElement;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.sf.easymol.io.xml.NewXMLtoMolecule;
import de.avetana.bluetooth.connection.Connector;
import de.avetana.bluetooth.connection.JSR82URL;
import de.avetana.bluetooth.rfcomm.RFCommConnectionImpl;
import de.avetana.bluetooth.rfcomm.RFCommConnectionNotifierImpl;

/**
 * @author gmelin
 *
 * 
 */
public class SendReceive {

    public void beClient(String dest) throws Exception {
        System.out.println("Connection");
        RFCommConnectionImpl bs = (RFCommConnectionImpl) Connector.open("btspp://" + dest + ":2;authenticate=false;encrypt=false");
        System.out.println();
        communicateClient(bs);
    }

    public void beServer() throws Exception {
        UUID myUUID = new UUID("0ecef8b070d211daa97c000fb07a0b78", false);
        System.out.println(myUUID);
        JSR82URL urli = new JSR82URL("btspp://localhost:" + myUUID + ";name=EasyMol;authenticate=false;encrypt=false;");
        urli.setAttrNumber(14);
        LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
        RFCommConnectionNotifierImpl localConnectionNotifier = new RFCommConnectionNotifierImpl(urli);
        ServiceRecord rec = localConnectionNotifier.getServiceRecord();
        System.out.println(rec);
        System.out.println(localConnectionNotifier.getConnectionURL());
        RFCommConnectionImpl bluetoothStream = (RFCommConnectionImpl) localConnectionNotifier.acceptAndOpen();
        communicateServer(bluetoothStream);
    }

    public void communicateServer(RFCommConnectionImpl con) throws Exception {
        try {
            RemoteDevice dev = RemoteDevice.getRemoteDevice(con);
            PrintStream o = System.out;
            DataOutputStream os = con.openDataOutputStream();
            DataOutputStream os2 = null;
            DataInputStream is = con.openDataInputStream();
            InputStream is2 = null;
            os = con.openDataOutputStream();
            is = con.openDataInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
            AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
            paramGen.init(512, SecureRandom.getInstance("SHA1PRNG"));
            AlgorithmParameters params = paramGen.generateParameters();
            DHParameterSpec dhSpec = (DHParameterSpec) params.getParameterSpec(DHParameterSpec.class);
            keyGen.initialize(dhSpec);
            KeyPair keypair = keyGen.generateKeyPair();
            PrivateKey privateKey = keypair.getPrivate();
            PublicKey publicKey = keypair.getPublic();
            PublicKey rempub = null;
            byte[] ourpublicKeyBytes = publicKey.getEncoded();
            byte[] theirpublicKeyBytes = null;
            theirpublicKeyBytes = ((PublicKey) ois.readObject()).getEncoded();
            System.out.println("The: " + toHexString(theirpublicKeyBytes));
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(theirpublicKeyBytes);
            KeyFactory keyFact = KeyFactory.getInstance("DH");
            rempub = keyFact.generatePublic(x509KeySpec);
            DHParameterSpec paramspec = ((DHPublicKey) rempub).getParams();
            keyGen.initialize(paramspec);
            keypair = keyGen.genKeyPair();
            publicKey = keypair.getPublic();
            privateKey = keypair.getPrivate();
            System.out.println("Our: " + toHexString(publicKey.getEncoded()));
            oos.writeObject(publicKey);
            oos.close();
            os.close();
            Thread.sleep(1000);
            KeyAgreement ka = KeyAgreement.getInstance("DH");
            ka.init(privateKey);
            ka.doPhase(rempub, true);
            String algorithm = "TripleDES";
            SecretKey secretKey = ka.generateSecret(algorithm);
            System.out.println(toHexString(secretKey.getEncoded()));
            SecretKeySpec skeyspec = new SecretKeySpec(secretKey.getEncoded(), algorithm);
            Cipher c = Cipher.getInstance(algorithm);
            c.init(Cipher.DECRYPT_MODE, skeyspec);
            long ttim = System.currentTimeMillis();
            is2 = con.openInputStream();
            os2 = con.openDataOutputStream();
            ois = new ObjectInputStream(is2);
            SealedObject so = (SealedObject) ois.readObject();
            org.jdom.Document mol = (org.jdom.Document) so.getObject(secretKey);
            ois.close();
            NewXMLtoMolecule mol2 = new NewXMLtoMolecule(mol);
            System.out.println("Time taken:" + (System.currentTimeMillis() - ttim));
            System.out.println(mol2.getMolecule());
            con.close();
        } catch (ClassNotFoundException cnfe) {
        }
    }

    public void communicateClient(RFCommConnectionImpl con) {
        OutputStream os, os2 = null;
        InputStream is, is2 = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        GZIPOutputStream gzout = null;
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream bis = null;
        try {
            os = con.openOutputStream();
            is = con.openInputStream();
            oos = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            bos = new ByteArrayOutputStream();
            RemoteDevice dev = con.getRemoteDevice();
            System.out.println(dev.getBluetoothAddress());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
            AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
            paramGen.init(512, SecureRandom.getInstance("SHA1PRNG"));
            AlgorithmParameters params = paramGen.generateParameters();
            DHParameterSpec dhSpec = (DHParameterSpec) params.getParameterSpec(DHParameterSpec.class);
            keyGen.initialize(dhSpec);
            KeyPair keypair = keyGen.generateKeyPair();
            System.out.println("KeyPair generated.");
            PrivateKey privateKey = keypair.getPrivate();
            PublicKey publicKey = keypair.getPublic();
            System.out.println(publicKey.getFormat());
            PublicKey rempub = null;
            byte[] ourpublicKeyBytes = publicKey.getEncoded();
            byte[] theirpublicKeyBytes = null;
            System.out.println("Our: " + toHexString(ourpublicKeyBytes));
            oos.writeObject(publicKey);
            Thread.sleep(1000);
            System.out.println("Our public was sent.");
            theirpublicKeyBytes = ((PublicKey) ois.readObject()).getEncoded();
            System.out.println("The: " + toHexString(theirpublicKeyBytes));
            System.out.println("Remote public key is read.");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(theirpublicKeyBytes);
            KeyFactory keyFact = KeyFactory.getInstance("DH", "BC");
            rempub = keyFact.generatePublic(x509KeySpec);
            DHParameterSpec paramspec = ((DHPublicKey) rempub).getParams();
            KeyAgreement ka = KeyAgreement.getInstance("DH");
            ka.init(privateKey);
            ka.doPhase(rempub, true);
            String algorithm = "TripleDES";
            SecretKey secretkey = ka.generateSecret(algorithm);
            System.out.println(toHexString(secretkey.getEncoded()));
            SecretKeySpec skeySpec = new SecretKeySpec(secretkey.getEncoded(), algorithm);
            Cipher c = Cipher.getInstance(algorithm);
            c.init(Cipher.ENCRYPT_MODE, skeySpec);
            oos.flush();
            oos.close();
            os.flush();
            os.close();
            os2 = con.openOutputStream();
            oos = new ObjectOutputStream(os2);
            long ttim = System.currentTimeMillis();
            org.jdom.Document doc = new NewXMLtoMolecule(new File("xml/VX.xml")).getDocument();
            SealedObject so = new SealedObject(doc, c);
            oos.writeObject(so);
            oos.flush();
            System.out.println("Time taken:" + (System.currentTimeMillis() - ttim));
            oos.flush();
            oos.close();
            os2.flush();
            os2.close();
            con.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        System.out.println("The image was sent");
    }

    public static void main(String[] args) throws Exception {
        if (args[0].equals("server")) new SendReceive().beServer(); else new SendReceive().beClient(args[0]);
    }

    private void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    private String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();
        int len = block.length;
        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len - 1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }

    public void setChannelId(ServiceRecord r, int chan) {
        DataElement testEl = new DataElement(DataElement.DATSEQ);
        DataElement testEl1 = new DataElement(DataElement.DATSEQ);
        testEl1.addElement(new DataElement(DataElement.UUID, new UUID(0x0100)));
        testEl.addElement(testEl1);
        DataElement testEl2 = new DataElement(DataElement.DATSEQ);
        testEl2.addElement(new DataElement(DataElement.UUID, new UUID(0x0003)));
        testEl2.addElement(new DataElement(DataElement.U_INT_1, chan));
        testEl.addElement(testEl2);
        r.setAttributeValue(0x0004, testEl);
    }
}
