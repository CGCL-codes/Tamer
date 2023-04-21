package gov.sns.tools.optimizer;

public class SimulationConstraintVariable {

    public SimulationConstraintVariable(String e, String p, double l, double u) {
        elem = e;
        param = p;
        ulimit = u;
        llimit = l;
        if (ulimit > llimit) {
            double temp = llimit;
            llimit = ulimit;
            ulimit = temp;
        }
    }

    private String elem;

    private String param;

    private double ulimit;

    private double llimit;

    public String getElem() {
        return elem;
    }

    public String getParam() {
        return param;
    }

    public double getUlimit() {
        return ulimit;
    }

    public double getLlimit() {
        return llimit;
    }
}
