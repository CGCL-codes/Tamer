package hu.schmidtsoft.map.model.io;

public class SAXImportMapType implements StringEvent {

    SaxImportMap sim;

    public SAXImportMapType(SaxImportMap sim) {
        this.sim = sim;
    }

    public void setString(String s) {
        sim.map.setType(s);
    }
}
