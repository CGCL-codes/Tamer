package realcix20.guis.views.settings.tools;

import java.util.Vector;

public class GVGroup {

    private String grp;

    private Vector gvs;

    public GVGroup(String grp, Vector gvs) {
        setGrp(grp);
        setGvs(gvs);
    }

    public String getGrp() {
        return grp;
    }

    public void setGrp(String grp) {
        this.grp = grp;
    }

    public Vector getGvs() {
        return gvs;
    }

    public void setGvs(Vector gvs) {
        this.gvs = gvs;
    }
}
