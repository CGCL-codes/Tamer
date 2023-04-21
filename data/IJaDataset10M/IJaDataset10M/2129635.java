package net.sf.fileexchange.api.snapshot;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class AddressSourcesSnapshot {

    @XmlElements({ @XmlElement(name = "constant", type = ConstantAddressSourceSnapshot.class), @XmlElement(name = "ips-of-network-interfaces", type = IPsFromNetworkInterfacesSnapshot.class), @XmlElement(name = "address-extracted-with-regex", type = AddressExtractedWithRegExSourceSnapshot.class) })
    private List<AddressSourceSnapshot> childs;

    public List<AddressSourceSnapshot> getChilds() {
        if (childs == null) childs = new ArrayList<AddressSourceSnapshot>();
        return childs;
    }
}
