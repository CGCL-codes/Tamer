package net.kano.joscar.snaccmd.icq;

import java.io.*;
import java.util.*;
import net.kano.joscar.*;
import net.kano.joscar.tlv.*;
import java.nio.charset.CharsetDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharacterCodingException;

/**
 * Sends packet with changed data
 * of Full User Info.
 * The packet is created then setters are used to set the info
 * you want to change
 *
 * @author Damian Minkov
 */
public class MetaFullInfoSetCmd extends ToIcqCmd {

    private List changeDataTlvs = new LinkedList();

    /**
     * Constructs command send to server
     * @param uin String the uin of the sender
     * @param id request id
     */
    public MetaFullInfoSetCmd(long uin, int id) {
        super(uin, AbstractIcqCmd.CMD_META_SET_CMD, 0x0002);
    }

    /**
     * Data writen to the sever corresponding the changes
     *
     * @param out OutputStream
     * @throws IOException
     */
    public void writeIcqData(OutputStream out) throws IOException {
        Iterator iter = changeDataTlvs.iterator();
        while (iter.hasNext()) {
            Writable item = (Writable) iter.next();
            item.write(out);
        }
    }

    /**
     * See end of file for coutry codes
     * @param value int
     * @throws IOException
     */
    public void setCountry(int value) throws IOException {
        setInt(0x01A4, value);
    }

    /**
     * Sets the nickname retreived in MetaBasicInfoCmd
     * @param value String
     */
    public void setNickName(String value) {
        setString(0x0154, value);
    }

    /**
     * Sets the lastname retreived in MetaBasicInfoCmd
     * @param value String
     */
    public void setLastName(String value) {
        setString(0x014A, value);
    }

    /**
     * Sets the firstname retreived in MetaBasicInfoCmd
     * @param value String
     */
    public void setFirstName(String value) {
        setString(0x0140, value);
    }

    /**
     * Sets the email retreived in MetaBasicInfoCmd
     * @param value String
     * @param publish whether to be published
     * @throws IOException
     */
    public void setEmail(String value, boolean publish) throws IOException {
        if (value == null) changeDataTlvs.add(getClearTlv(0x015E)); else {
            DetailTlv tlv = new DetailTlv(0x015E);
            tlv.writeString(value);
            tlv.writeUByte(publish ? 1 : 0);
            changeDataTlvs.add(tlv);
        }
    }

    /**
     * Sets the homecity retreived in MetaBasicInfoCmd
     * @param value String
     */
    public void setHomeCity(String value) {
        setString(0x0190, value);
    }

    /**
     * Sets the homestate retreived in MetaBasicInfoCmd
     * @param value String
     */
    public void setHomeState(String value) {
        setString(0x019A, value);
    }

    /**
     * Sets the homephone retreived in MetaBasicInfoCmd
     * @param value String
     */
    public void setHomePhone(String value) {
        setString(0x0276, value);
    }

    /**
     * Sets the homefax retreived in MetaBasicInfoCmd
     * @param value String
     */
    public void setHomeFax(String value) {
        setString(0x0280, value);
    }

    public void setAddress(String value) {
        setString(0x0262, value);
    }

    public void setCellPhone(String value) {
        setString(0x028A, value);
    }

    public void setHomeZip(String value) {
        setString(0x026C, value);
    }

    public void setWebAware(boolean isSet) throws IOException {
        DetailTlv tlv = new DetailTlv(0x02F8);
        tlv.writeUByte(isSet ? 1 : 0);
        changeDataTlvs.add(tlv);
    }

    public void setAuthorization(boolean isSet) throws IOException {
        DetailTlv tlv = new DetailTlv(0x030C);
        tlv.writeUByte(isSet ? 1 : 0);
        changeDataTlvs.add(tlv);
    }

    public void setGender(int value) throws IOException {
        DetailTlv tlv = new DetailTlv(0x030C);
        tlv.writeUByte(value);
        changeDataTlvs.add(tlv);
    }

    public void setHomePage(String value) {
        setString(0x0213, value);
    }

    public void setBirthDay(Date birthDate) throws IOException {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(birthDate);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LEBinaryTools.writeUShort(out, gc.get(Calendar.YEAR));
        LEBinaryTools.writeUShort(out, gc.get(Calendar.MONTH));
        LEBinaryTools.writeUShort(out, gc.get(Calendar.DAY_OF_MONTH));
        DetailTlv tlv = new DetailTlv(0x023A);
        tlv.writeUShort(gc.get(Calendar.YEAR));
        tlv.writeUShort(gc.get(Calendar.MONTH));
        tlv.writeUShort(gc.get(Calendar.DAY_OF_MONTH));
        changeDataTlvs.add(tlv);
    }

    public void setLanguages(int l1, int l2, int l3) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DetailTlv tlv = new DetailTlv(0x0186);
        if (l1 == -1) tlv.writeUShort(0); else tlv.writeUShort(l1);
        if (l2 == -1) tlv.writeUShort(0); else tlv.writeUShort(l2);
        if (l3 == -1) tlv.writeUShort(0); else tlv.writeUShort(l3);
        changeDataTlvs.add(tlv);
    }

    public void setOriginCity(String value) {
        setString(0x0320, value);
    }

    public void setOriginState(String value) {
        setString(0x032A, value);
    }

    public void setOriginCountry(int value) throws IOException {
        setInt(0x0334, value);
    }

    public void setWorkCity(String value) {
        setString(0x029E, value);
    }

    public void setWorkState(String value) {
        setString(0x02A8, value);
    }

    public void setWorkPhone(String value) {
        setString(0x02C6, value);
    }

    public void setWorkFax(String value) {
        setString(0x02D0, value);
    }

    public void setWorkAddress(String value) {
        setString(0x0294, value);
    }

    public void setWorkZip(String value) {
        setString(0x02BC, value);
    }

    public void setWorkCountry(int value) throws IOException {
        setInt(0x02B2, value);
    }

    public void setWorkCompany(String value) {
        setString(0x01AE, value);
    }

    public void setWorkDepartment(String value) {
        setString(0x01B8, value);
    }

    public void setWorkPosition(String value) {
        setString(0x01C2, value);
    }

    public void setWorkOccupationCode(int value) throws IOException {
        setInt(0x01CC, value);
    }

    public void setWorkWebPage(String value) {
        setString(0x02DA, value);
    }

    public void setNotes(String value) {
        setString(0x0258, value);
    }

    public void setInterests(int[] categories, String[] interests) throws IOException {
        if (categories.length != interests.length) return;
        DetailTlv tlv = new DetailTlv(0x01EA);
        for (int i = 0; i < categories.length; i++) {
            tlv.writeUShort(categories[i]);
            tlv.writeString(interests[i]);
        }
        changeDataTlvs.add(tlv);
    }

    public void setTimeZone(int zone) throws IOException {
        DetailTlv tlv = new DetailTlv(0x0316);
        tlv.writeUByte(zone);
        changeDataTlvs.add(tlv);
    }

    private void setString(int code, String value) {
        try {
            if (value == null) {
                changeDataTlvs.add(getClearTlv(code));
            } else {
                DetailTlv result = new DetailTlv(code);
                result.writeString(value);
                changeDataTlvs.add(result);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void setInt(int code, int value) throws IOException {
        if (value == -1) changeDataTlvs.add(getClearTlv(code)); else {
            DetailTlv tlv = new DetailTlv(code);
            tlv.writeUShort(value);
            changeDataTlvs.add(tlv);
        }
    }

    /**
     * Correspondig the type of ServerStoredDetails returns empty Tlv or Tlv
     * with default value
     * @param type The type of the detail
     * @return Tlv
     * @throws IOException
     */
    private Writable getClearTlv(int type) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DetailTlv tlv = new DetailTlv(type);
        switch(type) {
            case 0x01A4:
            case 0x0334:
            case 0x02B2:
                tlv.writeUShort(0);
                break;
            case 0x0186:
                tlv.writeUShort(0);
            case 0x017C:
                tlv.writeUByte(0);
            case 0x023A:
                tlv.writeUShort(0);
                tlv.writeUShort(0);
                tlv.writeUShort(0);
            case 0x01CC:
                tlv.writeUShort(0);
            case 0x01EA:
                tlv.writeUShort(0);
                tlv.writeString("");
            case 0x0316:
                tlv.writeUByte(0);
            default:
                tlv.writeString("");
        }
        return tlv;
    }
}
