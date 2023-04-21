package Controller;

import Controller.CrumbData.CrumbType;
import Controller.CrumbData.Parameter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author calvin
 */
public class CrumbFactoryImpl implements CrumbFactory, Serializable {

    private Map<String, CrumbData> allCrumbs;

    public CrumbFactoryImpl() throws Exception {
        allCrumbs = CrumbXMLFileReader.getInstance().getCrumbsFromXML();
    }

    public CrumbData getCrumb(String name) throws IllegalArgumentException {
        CrumbData crumbToCopy = allCrumbs.get(name);
        if (crumbToCopy == null) throw new IllegalArgumentException();
        return new CrumbData(crumbToCopy);
    }

    public List<CrumbData> getListOfAllCrumbs() {
        List<CrumbData> crumbs = new ArrayList<CrumbData>();
        for (CrumbData c : allCrumbs.values()) {
            crumbs.add(new CrumbData(c));
        }
        return crumbs;
    }

    public void addCrumbs(CrumbData... crumbs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeCrumbs(CrumbData... crumbs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CrumbData createVariableCrumb(String name) {
        CrumbData c = new CrumbData(name, name, CrumbType.Expression, "Variables", -1, -1, name, null);
        return c;
    }

    public CrumbData createSetVariableCrumb(String name) {
        CrumbData c = new CrumbData("Set " + name, null, CrumbType.Statement, "Variables", -1, -1, null, null);
        c.setCode(name + " = _val_;\n");
        c.getParameters().add(c.new Parameter("_val_", CrumbType.Expression, "set " + name + " to _val_", "0"));
        return c;
    }

    public CrumbData createCallFunctionCrumb(String name) {
        CrumbData c = new CrumbData("call " + name, "call " + name, CrumbType.Statement, "Functions", -1, -1, name + "();\n", null);
        return c;
    }

    public CrumbData createFunctionCrumb(String name) {
        CrumbData c = new CrumbData(name, null, CrumbType.Control, "Functions", -1, -1, null, null);
        c.setCode("void " + name + "() {\n_sub_\n}\n");
        return c;
    }
}
