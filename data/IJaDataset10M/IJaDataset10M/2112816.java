package sf.ru.coyote.model;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Andrey Raygorodsky
 * Date: 29.06.2007
 * Time: 22:49:36
 */
public class Operator {

    Region region;

    String name;

    byte[] pix;

    Vector sites = new Vector();

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPix() {
        return pix;
    }

    public void setPix(byte[] pix) {
        this.pix = pix;
    }

    public Vector getSites() {
        return sites;
    }

    public void setSites(Vector sites) {
        this.sites = sites;
    }

    public Vector getSitesByPhone(String phone) {
        Vector sites = new Vector();
        for (int i = 0; i < this.sites.size(); i++) {
            Site site = (Site) this.sites.elementAt(i);
            if (site.isAccepted(phone)) {
                sites.addElement(site);
            }
        }
        return sites;
    }
}
