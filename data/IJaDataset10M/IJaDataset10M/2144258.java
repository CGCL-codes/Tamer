package net.sf.borg.model.beans;

import net.sf.borg.common.XTree;

public class LinkXMLAdapter extends BeanXMLAdapter<Link> {

    public XTree toXml(Link o) {
        XTree xt = new XTree();
        xt.name("Link");
        xt.appendChild("KEY", Integer.toString(o.getKey()));
        if (o.getLinkType() != null) xt.appendChild("LinkType", o.getLinkType());
        if (o.getOwnerKey() != null) xt.appendChild("OwnerKey", BeanXMLAdapter.toString(o.getOwnerKey()));
        if (o.getOwnerType() != null) xt.appendChild("OwnerType", o.getOwnerType());
        if (o.getPath() != null) xt.appendChild("Path", o.getPath());
        return (xt);
    }

    public Link fromXml(XTree xt) {
        Link ret = new Link();
        String ks = xt.child("KEY").value();
        ret.setKey(BeanXMLAdapter.toInt(ks));
        String val = "";
        val = xt.child("LinkType").value();
        ret.setLinkType(val);
        val = xt.child("OwnerKey").value();
        ret.setOwnerKey(BeanXMLAdapter.toInteger(val));
        val = xt.child("OwnerType").value();
        ret.setOwnerType(val);
        val = xt.child("Path").value();
        ret.setPath(val);
        return (ret);
    }
}
