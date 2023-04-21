package mpower_hibernate;

import java.sql.*;
import java.io.ByteArrayOutputStream;
import java.io.*;
import org.hibernate.Hibernate;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import javax.imageio.ImageReader;

/**
 *
 * @author sta
 */
public class Person {

    private String id;

    private String givenNames;

    private String familyName;

    private Telecom telecom;

    private ActorAddress address;

    private boolean organDonor;

    private byte[] picture;

    private Gender gender;

    private Date birthDate;

    public Person() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGivenNames() {
        return givenNames;
    }

    public void setGivenNames(String givenNames) {
        this.givenNames = givenNames;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Telecom getTelecom() {
        return telecom;
    }

    public void setTelecom(Telecom telecom) {
        this.telecom = telecom;
    }

    public ActorAddress getAddress() {
        return address;
    }

    public void setAddress(ActorAddress address) {
        this.address = address;
    }

    public boolean isOrganDonor() {
        return organDonor;
    }

    public void setOrganDonor(boolean organDonor) {
        this.organDonor = organDonor;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @SuppressWarnings("unused")
    private void setBlob(Blob imageBlob) {
        this.picture = toByteArray(imageBlob);
    }

    @SuppressWarnings("unused")
    private Blob getBlob() {
        if (this.picture == null) {
            this.picture = new byte[0];
        }
        return Hibernate.createBlob(this.picture);
    }

    public BufferedImage getImage(String format) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(picture);
        ImageReader ir = (ImageReader) ImageIO.getImageReadersByFormatName(format).next();
        ir.setInput(bis);
        return ir.read(0);
    }

    public void setImage(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, format, bos);
        picture = bos.toByteArray();
    }

    private byte[] toByteArray(Blob fromImageBlob) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            return toByteArrayImpl(fromImageBlob, baos);
        } catch (Exception e) {
        }
        return null;
    }

    private byte[] toByteArrayImpl(Blob fromImageBlob, ByteArrayOutputStream baos) throws SQLException, IOException {
        byte buf[] = new byte[4000];
        int dataSize;
        InputStream is = fromImageBlob.getBinaryStream();
        try {
            while ((dataSize = is.read(buf)) != -1) {
                baos.write(buf, 0, dataSize);
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return baos.toByteArray();
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
