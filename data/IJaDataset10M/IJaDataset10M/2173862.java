package enml.devices;

import enml.documents.Certification;
import enml.metainfo.Life;
import java.io.Serializable;
import java.util.ArrayList;

public class Device implements Serializable {

    private DeviceType type;

    private String brand;

    private String model;

    private String serial;

    private ArrayList<Certification> certifications;

    private Life life;

    public Device() {
    }

    public String getBrand() {
        return null;
    }

    public void setBrand(String val) {
    }

    public String getModel() {
        return null;
    }

    public void setModel(String val) {
    }

    public String getSerial() {
        return null;
    }

    public void setSerial(String val) {
    }

    @Override
    public String toString() {
        return null;
    }

    public DeviceType getType() {
        return null;
    }

    public void setType(DeviceType t) {
    }

    public ArrayList<Certification> getCertifications() {
        return null;
    }

    public void setCertifications(ArrayList<Certification> val) {
    }

    public Life getLife() {
        return null;
    }

    public void setLife(Life val) {
    }
}
