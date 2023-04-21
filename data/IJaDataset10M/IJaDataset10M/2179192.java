package org.esprit.ocm.client.model.oca.datasource;

import java.util.ArrayList;
import java.util.List;
import org.esprit.ocm.client.model.oca.VnetRecord;
import org.esprit.ocm.model.oca.VnetOca;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class VnetLocalDS extends DataSource {

    private static VnetLocalDS instance = null;

    public static VnetLocalDS getInstance(List<VnetOca> lists) {
        instance = new VnetLocalDS("vnetLocalDS", lists);
        return instance;
    }

    public VnetLocalDS(String id, List<VnetOca> lists) {
        DataSourceTextField idField = new DataSourceTextField("id", "id");
        DataSourceTextField uid = new DataSourceTextField("uid", "uid");
        DataSourceTextField username = new DataSourceTextField("username", "username");
        DataSourceTextField name = new DataSourceTextField("name", "name");
        DataSourceTextField type = new DataSourceTextField("type", "type");
        DataSourceTextField bridge = new DataSourceTextField("bridge", "bridge");
        DataSourceTextField publicState = new DataSourceTextField("publicState", "publicState");
        DataSourceTextField total_leases = new DataSourceTextField("total_leases", "total_leases");
        DataSourceTextField template_bridge = new DataSourceTextField("template_bridge", "template_bridge");
        DataSourceTextField template_name = new DataSourceTextField("template_name", "template_name");
        DataSourceTextField template_type = new DataSourceTextField("template_type", "template_type");
        DataSourceTextField template_description = new DataSourceTextField("template_description", "template_description");
        DataSourceTextField template_network_address = new DataSourceTextField("template_network_address", "template_network_address");
        DataSourceTextField template_network_size = new DataSourceTextField("template_network_size", "template_network_size");
        DataSourceTextField template_leases_ip = new DataSourceTextField("template_leases_ip", "template_leases_ip");
        DataSourceTextField leases_lease_ip = new DataSourceTextField("leases_lease_ip", "leases_lease_ip");
        DataSourceTextField leases_lease_mac = new DataSourceTextField("leases_lease_mac", "leases_lease_mac");
        DataSourceTextField leases_lease_used = new DataSourceTextField("leases_lease_used", "leases_lease_used");
        DataSourceTextField leases_lease_vid = new DataSourceTextField("leases_lease_vid", "leases_lease_vid");
        setFields(idField, uid, username, name, type, bridge, publicState, total_leases, template_bridge, template_name, template_type, template_description, template_network_address, template_network_size, template_leases_ip, leases_lease_ip, leases_lease_mac, leases_lease_used, leases_lease_vid);
        List<ListGridRecord> l = new ArrayList<ListGridRecord>();
        for (VnetOca from : lists) {
            VnetRecord to = new VnetRecord();
            to.setId(from.getId());
            to.setUid(from.getUid());
            to.setUsername(from.getUsername());
            to.setName(from.getName());
            to.setType(from.getType());
            to.setBridge(from.getBridge());
            to.setPublicState(from.getPublicState());
            to.setTotal_leases(from.getTotal_leases());
            to.setTemplate_bridge(from.getTemplate_bridge());
            to.setTemplate_name(from.getTemplate_name());
            to.setTemplate_type(from.getTemplate_type());
            to.setTemplate_description(from.getTemplate_description());
            to.setTemplate_network_address(from.getTemplate_network_address());
            to.setTemplate_network_size(from.getTemplate_network_size());
            to.setTemplate_leases_ip(from.getTemplate_leases_ip());
            to.setLeases_lease_ip(from.getLeases_lease_ip());
            to.setLeases_lease_mac(from.getLeases_lease_mac());
            to.setLeases_lease_used(from.getLeases_lease_used());
            to.setLeases_lease_vid(from.getLeases_lease_vid());
            l.add(to);
        }
        setTestData(l.toArray(new VnetRecord[0]));
        setClientOnly(true);
    }
}
