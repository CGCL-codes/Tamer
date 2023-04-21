package com.vmware.vcloud;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;
import com.vmware.vcloud.data.VCloudConstants;
import com.vmware.vcloud.rest.RestClient;

/**
 * @author Steve Jin (sjin@vmware.com)
 */
public class Org extends VCloudObject {

    private List<VDC> vdcs = new ArrayList<VDC>();

    private List<TasksList> tasksList = new ArrayList<TasksList>();

    private List<Catalog> catalogs = new ArrayList<Catalog>();

    public Org(RestClient rc, String urlPath) throws MalformedURLException {
        super(rc, urlPath);
    }

    public Org(RestClient rc, String urlPath, String name) throws MalformedURLException {
        super(rc, urlPath);
        this.name = name;
    }

    protected void parse() throws MalformedURLException {
        List<?> elems = root.elements(VCloudConstants.ELEM_LINK);
        for (int i = 0; i < elems.size(); i++) {
            Element elem = (Element) elems.get(i);
            String rel = elem.attributeValue(VCloudConstants.REL);
            String type = elem.attributeValue(VCloudConstants.TYPE);
            if (!VCloudConstants.DOWN.equals(rel)) {
                continue;
            }
            String href = elem.attributeValue(VCloudConstants.HREF);
            String name = elem.attributeValue("name");
            if (VCloudConstants.TYPE_VDC.equals(type)) {
                vdcs.add(new VDC(rc, href, name));
            } else if (VCloudConstants.TYPE_CATALOG.equals(type)) {
                catalogs.add(new Catalog(rc, href));
            } else if (VCloudConstants.TYPE_TASKSLIST.equals(type)) {
                tasksList.add(new TasksList(rc, href));
            }
        }
    }

    public Catalog[] getCatalogs() throws IOException {
        loadContentIfNotYet();
        Catalog[] cats = new Catalog[catalogs.size()];
        catalogs.toArray(cats);
        return cats;
    }

    public VDC[] getVDCs() throws IOException {
        loadContentIfNotYet();
        VDC[] vs = new VDC[vdcs.size()];
        vdcs.toArray(vs);
        return vs;
    }

    public TasksList[] getTasksList() throws IOException {
        loadContentIfNotYet();
        TasksList[] ts = new TasksList[tasksList.size()];
        tasksList.toArray(ts);
        return ts;
    }
}
