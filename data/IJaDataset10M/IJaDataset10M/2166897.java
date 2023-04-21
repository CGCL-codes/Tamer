package org.slasoi.gslam.templateregistry;

import org.slasoi.gslam.core.negotiation.SLATemplateRegistry.Metadata;
import org.slasoi.gslam.templateregistry.SLATemplateRegistryImpl.Mode;
import org.slasoi.slamodel.primitives.ID;
import org.slasoi.slamodel.primitives.UUID;
import org.slasoi.slamodel.service.Interface;
import org.slasoi.slamodel.service.Interface.Specification;
import org.slasoi.slamodel.sla.Endpoint;
import org.slasoi.slamodel.sla.InterfaceDeclr;
import org.slasoi.slamodel.sla.Party;
import org.slasoi.slamodel.sla.SLATemplate;
import org.slasoi.slamodel.sla.Party.Operative;
import org.slasoi.slamodel.vocab.sla;

class _examples {

    static final String $slam_id = "{the_slam_id}";

    static final String $another_provider_id = "{another_provider_id}";

    static final String $registrar_id = "{keven}";

    static final ID provider_id = new ID("{provider_id}");

    static final ID provider_agent_1_id = new ID("{provider_agent_1}");

    static final ID customer_id = new ID("{customer_id}");

    static final String $i_simple1 = "i_simple1";

    static final String $i_simple2 = "i_simple2";

    static final String $i_composite1 = "i_composite1";

    static final UUID template_all_id = new UUID("template_all");

    static final UUID template_simple1_id = new UUID("template_simple1");

    static final UUID template_simple2_id = new UUID("template_simple2");

    static final UUID template_composite1_id = new UUID("template_composite1");

    static final ID idec_1_id = new ID("{idec_1_id}");

    static final ID idec_1_endpoint_id = new ID("{idec_1_endpoint_id}");

    static final ID idec_2_id = new ID("{idec_2_id}");

    static final ID idec_3_id = new ID("{idec_3_id}");

    static SLATemplateRegistryImpl p_SLATREG(SLATemplate slat, boolean log) throws Exception {
        SLATemplateRegistryImpl r = new SLATemplateRegistryImpl(Mode.OFFLINE_TEST);
        if (log) r.addListener(new _system_out_logger());
        if (slat != null) {
            r.addSLATemplate(slat, p_METADATA_1());
        }
        return r;
    }

    static Metadata p_METADATA_1() {
        Metadata m = new Metadata();
        m.setPropertyValue(Metadata.provider_uuid, $slam_id);
        m.setPropertyValue(Metadata.registrar_id, $registrar_id);
        return m;
    }

    static Metadata p_METADATA_2() {
        Metadata m = new Metadata();
        m.setPropertyValue(Metadata.provider_uuid, $another_provider_id);
        m.setPropertyValue(Metadata.registrar_id, $registrar_id);
        return m;
    }

    static SLATemplate p_SLAT_all() {
        SLATemplate slat = new SLATemplate();
        slat.setUuid(template_all_id);
        Party provider = new Party(provider_id, sla.provider);
        Operative provider_agent_1 = new Operative(provider_agent_1_id);
        provider.setOperatives(new Operative[] { provider_agent_1 });
        Party customer = new Party(customer_id, sla.customer);
        slat.setParties(new Party[] { provider, customer });
        InterfaceDeclr iface_declr_1 = new InterfaceDeclr(idec_1_id, sla.provider, p_ISPEC_simple1());
        iface_declr_1.setEndpoints(new Endpoint[] { new Endpoint(idec_1_endpoint_id, sla.telephone) });
        InterfaceDeclr ispec_1 = new InterfaceDeclr(idec_2_id, sla.provider, p_ISPEC_simple2());
        InterfaceDeclr ispec_2 = new InterfaceDeclr(idec_3_id, sla.provider, p_ISPEC_composite1());
        slat.setInterfaceDeclrs(new InterfaceDeclr[] { iface_declr_1, ispec_1, ispec_2 });
        return slat;
    }

    static SLATemplate p_SLAT_simple1() {
        SLATemplate slat = new SLATemplate();
        slat.setUuid(template_simple1_id);
        Party provider = new Party(provider_id, sla.provider);
        Party customer = new Party(customer_id, sla.customer);
        slat.setParties(new Party[] { provider, customer });
        InterfaceDeclr idec_1 = new InterfaceDeclr(idec_1_id, sla.provider, p_ISPEC_simple1());
        idec_1.setEndpoints(new Endpoint[] { new Endpoint(idec_1_endpoint_id, sla.telephone) });
        slat.setInterfaceDeclrs(new InterfaceDeclr[] { idec_1 });
        return slat;
    }

    static SLATemplate p_SLAT_simple2() {
        SLATemplate slat = new SLATemplate();
        slat.setUuid(template_simple2_id);
        Party provider = new Party(provider_id, sla.provider);
        Party customer = new Party(customer_id, sla.customer);
        slat.setParties(new Party[] { provider, customer });
        InterfaceDeclr idec_1 = new InterfaceDeclr(idec_1_id, sla.provider, p_ISPEC_simple2());
        slat.setInterfaceDeclrs(new InterfaceDeclr[] { idec_1 });
        return slat;
    }

    static SLATemplate p_SLAT_composite1() {
        SLATemplate slat = new SLATemplate();
        slat.setUuid(template_composite1_id);
        Party provider = new Party(provider_id, sla.provider);
        slat.setParties(new Party[] { provider });
        InterfaceDeclr idec_1 = new InterfaceDeclr(idec_1_id, sla.provider, p_ISPEC_composite1());
        slat.setInterfaceDeclrs(new InterfaceDeclr[] { idec_1 });
        return slat;
    }

    static Specification p_ISPEC_composite1() {
        Specification spec = new Specification($i_composite1);
        spec.setExtended(new UUID[] { new UUID($i_simple1), new UUID($i_simple2) });
        return spec;
    }

    static Specification p_ISPEC_simple1() {
        Specification spec = new Specification($i_simple1);
        Interface.Operation op1 = new Interface.Operation(new ID("op1"));
        spec.setOperations(new Interface.Operation[] { op1 });
        return spec;
    }

    static Specification p_ISPEC_simple2() {
        Specification spec = new Specification($i_simple2);
        Interface.Operation op1 = new Interface.Operation(new ID("opX"));
        spec.setOperations(new Interface.Operation[] { op1 });
        return spec;
    }
}
