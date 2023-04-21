package org.kisst.cordys.caas;

import org.kisst.cordys.caas.support.CordysObject;
import org.kisst.cordys.caas.support.CordysObjectList;
import org.kisst.cordys.caas.util.StringUtil;
import org.kisst.cordys.caas.util.XmlNode;

public class ProcessModel extends CordysObject {

    private final String name;

    private final Organization org;

    private final CordysSystem system;

    private final String modelSpace;

    public static class List extends CordysObjectList<ProcessModel> {

        private final Organization org;

        public List(Organization org) {
            super(org.getSystem());
            this.org = org;
        }

        @Override
        protected void retrieveList() {
            XmlNode method = new XmlNode("GetAllProcessModels", xmlns_coboc);
            XmlNode response = org.call(method);
            for (XmlNode tuple : response.getChild("data").getChildren("tuple")) {
                ProcessModel p = new ProcessModel(org, tuple);
                grow(p);
            }
        }

        @Override
        public String getKey() {
            return "processModels:" + org.getKey();
        }
    }

    ;

    public ProcessModel(Organization org, XmlNode tuple) {
        this.org = org;
        this.system = org.getSystem();
        this.name = tuple.getChildText("old/bizprocess/processname");
        this.modelSpace = tuple.getChildText("old/bizprocess/processname/?@modelSpace");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getKey() {
        return "processmodel:" + name;
    }

    @Override
    public CordysSystem getSystem() {
        return system;
    }

    @Override
    public String getVarName() {
        return org.getVarName() + ".proc." + StringUtil.quotedName(name);
    }

    public void delete() {
        if ("isv".equals(modelSpace)) throw new RuntimeException("Can not delete isv processModel " + getVarName());
        XmlNode method = new XmlNode("DeleteProcessModel", xmlns_coboc);
        method.add("processname").setText(name);
        org.call(method);
    }
}
