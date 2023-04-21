package layer.disseminator.disseminationManagement;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import apps.disseminator.ProtocolConstants;

/**
 * Ein Objekt dieser Klasse bildet den Rahmen f�r Objekte, die verteilt werden
 * sollen. Es enth�lt alle Parameter, die von der Verteilungskomponente ben�tigt
 * werden, um festzustellen, mit welchem Verteilungsprotokoll das zu verteilende
 * Objekt weiterverteilt werden soll. Folgender Aufbau liegt dem Objekt zu
 * Grunde:
 * 
 * <blockquote> <table border="0" bgcolor="#D4D7CD">
 * <tr>
 * <th colspan="3" align=left><code>SF_PDU</code></th>
 * </tr>
 * <tr>
 * <td><code>iDisseminationProtocol</code> |</td>
 * <td> <table border="0" bgcolor="#B5A9BB">
 * <tr>
 * <td><code>abyPayload</code></td>
 * </tr>
 * </table> </td>
 * </tr>
 * </table> </blockquote>
 * 
 * <blockquote> <table border="0">
 * <tr>
 * <td>&lt; <code>iDisseminationProtocol</code> &gt;</td>
 * <td>:= SFP = 0 | PROB_FLOODING = 1 | ADAP_PROB_FLOODING = 2 </tr>
 * <tr>
 * <td>&lt; <code>abyPayload</code> &gt;</td>
 * <td>:= byte[]</td>
 * </tr>
 * </table> </blockquote>
 * 
 * @author Yark Schroeder, Manuel Scholz
 * @version $Id: PF_PDU.java,v 1.8 2006/04/07 07:06:01 yark Exp $
 * @since 1.3
 */
public class PF_PDU extends AD_PDU {

    /**
	 * Wenn das Probalistische Fluten als Protokoll ausgew�hlt wurde, ist dieser
	 * Wert die Wahrscheinlichkeit der weiteren Verteilung.
	 */
    private double mdProbability;

    /**
	 * Setzt die Wahrscheinlichkeit, mit der ein Datenobjekt weiterverteilt
	 * wird.
	 * 
	 * @param d
	 *            Die Wahrscheinlichkeit, mit der ein Datenobjekt weiterverteilt
	 *            wird.
	 * @since 1.3
	 */
    public void setProbabilityHeaderField(double dProbability) {
        mdProbability = dProbability;
    }

    /**
	 * Liefert die Wahrscheinlichkeit, mit der ein Datenobjekt weiterverteilt
	 * wird, zur�ck.
	 * 
	 * @return Die Wahrscheinlichkeit, mit der ein Datenobjekt weiterverteilt
	 *         wird.
	 * @since 1.3
	 */
    public double getProbabilityHeaderField() {
        return mdProbability;
    }

    public byte[] serialize() {
        ByteArrayOutputStream kByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream kDataOutputStream;
        kDataOutputStream = new DataOutputStream(kByteArrayOutputStream);
        try {
            kDataOutputStream.writeByte(getDstProtocol());
            kDataOutputStream.writeByte(getPrtTypeHeaderField());
            kDataOutputStream.writeShort(getHopsHeaderField());
            kDataOutputStream.writeLong(getIdentificationHeaderField());
            kDataOutputStream.writeFloat((float) mdProbability);
            kDataOutputStream.write(getDstAdrHeaderField().getAddress());
            kDataOutputStream.write(getSrcAdrHeaderField().getAddress());
            kDataOutputStream.write(getPayload());
            kDataOutputStream.close();
            return kByteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean unserialize(byte[] abyData) {
        ByteArrayInputStream kByteArrayInputStream = new ByteArrayInputStream(abyData);
        DataInputStream kDataInputStream;
        try {
            kDataInputStream = new DataInputStream(kByteArrayInputStream);
            kDataInputStream.skip(1);
            setHopsHeaderField(kDataInputStream.readShort());
            setIdentificationHeaderField(kDataInputStream.readLong());
            setProbabilityHeaderField(kDataInputStream.readFloat());
            byte[] abyAddress = new byte[4];
            kDataInputStream.read(abyAddress);
            setDstAdrHeaderField(createInetAddress(convertInetAddressToString(abyAddress)));
            kDataInputStream.read(abyAddress);
            setSrcAdrHeaderField(createInetAddress(convertInetAddressToString(abyAddress)));
            int iPayloadLength = kDataInputStream.available();
            byte[] abyDat = new byte[iPayloadLength];
            kDataInputStream.read(abyDat);
            setPayload(abyData);
            kDataInputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String toString() {
        String strResultString = "PF_PDU[Id: ";
        strResultString = strResultString + getIdentificationHeaderField() + "; ";
        strResultString = strResultString + "Hops: " + getHopsHeaderField() + "; ";
        strResultString = strResultString + "Probability: " + getProbabilityHeaderField() + "; ";
        strResultString = strResultString + "Src: " + getSrcAdrHeaderField().getHostAddress() + "; ";
        strResultString = strResultString + "Dest: " + getDstAdrHeaderField().getHostAddress() + "; ";
        strResultString = strResultString + "PayloadLength: " + getPayload().length + "]";
        return strResultString;
    }

    public int getPrtTypeHeaderField() {
        return ProtocolConstants.PFP;
    }
}
