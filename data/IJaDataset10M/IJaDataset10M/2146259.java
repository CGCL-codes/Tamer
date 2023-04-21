package org.geonetwork.domain.ebrim.test.utilities.service;

import java.util.HashSet;
import java.util.Set;
import org.geonetwork.domain.ebrim.informationmodel.classification.Classification;
import org.geonetwork.domain.ebrim.informationmodel.core.Slot;
import org.geonetwork.domain.ebrim.informationmodel.core.VersionInfo;
import org.geonetwork.domain.ebrim.informationmodel.core.datatype.InternationalString;
import org.geonetwork.domain.ebrim.informationmodel.core.datatype.LongName;
import org.geonetwork.domain.ebrim.informationmodel.core.datatype.URI;
import org.geonetwork.domain.ebrim.informationmodel.core.datatype.URN;
import org.geonetwork.domain.ebrim.informationmodel.service.Service;
import org.geonetwork.domain.ebrim.informationmodel.service.ServiceBinding;
import org.geonetwork.domain.ebrim.test.utilities.classification.ClassificationFactory;
import org.geonetwork.domain.ebrim.test.utilities.core.SlotFactory;
import org.geonetwork.domain.ebrim.test.utilities.core.VersionInfoFactory;
import org.geonetwork.domain.ebrim.test.utilities.core.datatype.InternationalStringFactory;

public class ServiceFactory {

    public static synchronized Service create() {
        Service o = new Service();
        Set<ServiceBinding> serviceBindings = new HashSet<ServiceBinding>();
        ServiceBinding serviceBinding1 = ServiceBindingFactory.create();
        serviceBindings.add(serviceBinding1);
        o.setServiceBindings(serviceBindings);
        Set<Classification> classifications = new HashSet<Classification>();
        Classification classification1 = ClassificationFactory.create();
        Classification classification2 = ClassificationFactory.create();
        classification2.setClassificationNode(new URI("http://classification-node2"));
        classification1.setId(new URN("urn:service-classification1"));
        classification2.setId(new URN("urn:service-classification2"));
        classifications.add(classification1);
        classifications.add(classification2);
        o.setClassifications(classifications);
        InternationalString description = InternationalStringFactory.create();
        o.setDescription(description);
        o.setHome(new URI("http://service-home"));
        o.setId(new URN("urn:service-id"));
        o.setLid(new URN("urn:service-lid"));
        InternationalString name = InternationalStringFactory.create();
        o.setName(name);
        o.setObjectType(new URI("http://specificationlink-objectType"));
        Set<Slot> slots = new HashSet<Slot>();
        Slot slot1 = SlotFactory.create();
        Slot slot2 = SlotFactory.create();
        slot2.setName(new LongName("slot2-name"));
        slots.add(slot1);
        slots.add(slot2);
        o.setSlots(slots);
        o.setStatus(new URI("http://specificationlink-status"));
        VersionInfo versionInfo = VersionInfoFactory.create();
        o.setVersionInfo(versionInfo);
        return o;
    }
}
