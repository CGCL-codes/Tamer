package jmx;

public class Simple implements SimpleMBean {

    static final String INIT_STATE = "initial state";

    protected String state = INIT_STATE;

    protected String master = "";

    protected int nbChanges = 0;

    public String getState() {
        return state;
    }

    public void setState(String s) {
        if (!s.equals(state)) {
            state = s;
            attributeChanged("state");
            incrNbChanges();
        }
    }

    private void incrNbChanges() {
        nbChanges++;
    }

    public int getNbChanges() {
        return nbChanges;
    }

    public void reset() {
        nbChanges = 0;
        state = "initial state";
    }

    private void attributeChanged(String attributeName) {
        System.out.println("- attribute '" + attributeName + "' has been changed.");
    }

    @Override
    public String getMaster() {
        return master;
    }

    @Override
    public void setMaster(String s) {
        if (!s.equals(master)) {
            master = s;
            attributeChanged("master");
            incrNbChanges();
        }
    }
}
