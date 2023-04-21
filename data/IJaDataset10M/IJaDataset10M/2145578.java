package enml.documents;

import enml.devices.Device;
import enml.subjects.Authority;
import enml.validity.Validity;
import java.io.Serializable;
import java.util.ArrayList;

public class Certification implements Serializable {

    private long number;

    private ArrayList<Authority> authority;

    private Device device;

    private Validity validity;

    public Certification() {
    }

    @Override
    public String toString() {
        return null;
    }

    public ArrayList<Authority> getAuthority() {
        return null;
    }

    public long getNumber() {
        return 0;
    }

    public void setNumber(long val) {
    }

    public void setAuthority(ArrayList<Authority> val) {
    }

    public long getId() {
        return 0;
    }

    public void setId(long id) {
    }

    public Device getDevice() {
        return null;
    }

    public void setDevice(Device val) {
    }

    public Validity getValidity() {
        return null;
    }

    public void setValidity(Validity val) {
    }
}
