package org.slasoi.slamodel;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.slasoi.slamodel.core.CompoundDomainExpr;
import org.slasoi.slamodel.core.DomainExpr;
import org.slasoi.slamodel.core.EventExpr;
import org.slasoi.slamodel.core.FunctionalExpr;
import org.slasoi.slamodel.core.SimpleDomainExpr;
import org.slasoi.slamodel.core.TypeConstraintExpr;
import org.slasoi.slamodel.primitives.CONST;
import org.slasoi.slamodel.primitives.Expr;
import org.slasoi.slamodel.primitives.ID;
import org.slasoi.slamodel.primitives.LIST;
import org.slasoi.slamodel.primitives.STND;
import org.slasoi.slamodel.primitives.TIME;
import org.slasoi.slamodel.primitives.UUID;
import org.slasoi.slamodel.primitives.ValueExpr;
import org.slasoi.slamodel.service.Interface;
import org.slasoi.slamodel.service.ServiceRef;
import org.slasoi.slamodel.service.Interface.Operation;
import org.slasoi.slamodel.service.Interface.Operation.Property;
import org.slasoi.slamodel.sla.AgreementTerm;
import org.slasoi.slamodel.sla.Customisable;
import org.slasoi.slamodel.sla.Endpoint;
import org.slasoi.slamodel.sla.Guaranteed;
import org.slasoi.slamodel.sla.InterfaceDeclr;
import org.slasoi.slamodel.sla.InterfaceRef;
import org.slasoi.slamodel.sla.Invocation;
import org.slasoi.slamodel.sla.Party;
import org.slasoi.slamodel.sla.SLA;
import org.slasoi.slamodel.sla.VariableDeclr;
import org.slasoi.slamodel.vocab.common;
import org.slasoi.slamodel.vocab.core;
import org.slasoi.slamodel.vocab.sla;
import org.slasoi.slamodel.vocab.units;
import org.slasoi.slamodel.vocab.xsd;

public class EXAMPLE_SLA_1 extends SLA {

    private static final long serialVersionUID = 1L;

    public EXAMPLE_SLA_1() {
        this.setDescr("an example SLA ...");
        this.setPropertyValue(new STND("hello"), "world (an example of an annotation)");
        GregorianCalendar cal = new GregorianCalendar(2010, Calendar.MARCH, 12, 12, 0, 0);
        this.setAgreedAt(new TIME(cal));
        cal.roll(Calendar.DATE, true);
        this.setEffectiveFrom(new TIME(cal));
        cal.add(Calendar.DATE, 2);
        this.setEffectiveUntil(new TIME(cal));
        this.setTemplateId(new UUID("http://UUID_OF_THE_TEMPLATE_ON_WHICH_THIS_SLA_WAS_BASED"));
        this.setUuid(new UUID("http://THE_UUID_OF_THIS_SLA"));
        Party p = new Party(new ID("PROVIDER_ID"), sla.provider);
        p.setDescr("describes the 'provider' ...");
        Party.Operative op = new Party.Operative(new ID("OPERATIVE_ID"));
        op.setDescr("describes a party operative/agent ...");
        p.setOperatives(new Party.Operative[] { op });
        Party c = new Party(new ID("CUSTOMER_ID"), sla.customer);
        c.setDescr("describes the 'customer' ...");
        this.setParties(new Party[] { p, c });
        InterfaceRef iref = new InterfaceRef(new UUID("http://LOCATION_OF_THE_INTERFACE_SPECIFICATION"));
        InterfaceDeclr idec1 = new InterfaceDeclr(new ID("PROVIDER_INTERFACE_ID"), sla.provider, iref);
        idec1.setDescr("describes an interface offered by the provider ...");
        Endpoint ep = new Endpoint(new ID("ENDPOINT_ID"), sla.SOAP);
        ep.setLocation(new UUID("AN_OPTIONAL_ENDPOINT_LOCATION"));
        idec1.setEndpoints(new Endpoint[] { ep });
        String $customer_iface_id = "CUSTOMER_INTERFACE_ID";
        String $op_1_name = "OPERATION_1_NAME";
        String $input_1_name = "INPUT_1_NAME";
        Interface.Specification ispec = new Interface.Specification("THE_NAME_OF_THE_INTERFACE");
        Operation op1 = new Operation(new ID($op_1_name));
        Property in1 = new Property(new ID($input_1_name));
        in1.setDatatype(xsd.string);
        op1.setInputs(new Property[] { in1 });
        Property out1 = new Property(new ID("OUTPUT_1_NAME"));
        out1.setDatatype(xsd.integer);
        out1.setDomain(new CompoundDomainExpr(core.and, new DomainExpr[] { new SimpleDomainExpr(new CONST("0", null), core.greater_than), new SimpleDomainExpr(new CONST("10", null), core.less_than_or_equals) }));
        op1.setOutputs(new Property[] { out1 });
        op1.setFaults(new STND[] { new STND("http://DATATYPE_OF_AN_EXCEPTION") });
        ispec.setOperations(new Operation[] { op1 });
        InterfaceDeclr idec2 = new InterfaceDeclr(new ID($customer_iface_id), sla.customer, ispec);
        idec2.setDescr("describes an interface expected from the customer ...");
        this.setInterfaceDeclrs(new InterfaceDeclr[] { idec1, idec2 });
        ServiceRef sref = new ServiceRef();
        sref.setInterfaceDeclrIds(new ID[] { idec1.getId(), idec2.getId() });
        VariableDeclr service_ref_vdec = new VariableDeclr(new ID("SERVICE_REF_1"), sref);
        service_ref_vdec.setDescr("'SERVICE-REF-1' denotes a collection of interfaces ...");
        VariableDeclr completion_time_vdec = new VariableDeclr(new ID("COMPLETION_TIME_1"), new FunctionalExpr(common.completion_time, new ValueExpr[] { service_ref_vdec.getVar() }));
        completion_time_vdec.setDescr("'COMPLETION_TIME_1' denotes the completion-time of operations/invocations of the interfaces denoted by SERVICE_REF_1");
        LIST options = new LIST();
        CONST[] cs = new CONST[] { new CONST("OPTION_1", null), new CONST("OPTION_2", null), new CONST("OPTION_3", null) };
        for (CONST q : cs) options.add(q);
        Customisable customisable_vdec = new Customisable(new ID("CUSTOMER_SELECTION"), new SimpleDomainExpr(options, core.member_of), cs[0]);
        customisable_vdec.setDescr("'CUSTOM_1' denotes a customisable value; there are 3 options, with a <<default>> of OPTION_1");
        this.setVariableDeclrs(new VariableDeclr[] { service_ref_vdec, completion_time_vdec, customisable_vdec });
        String $descr = "Guarantees a completion time of < 10ms, but only on Fridays";
        AgreementTerm at1 = new AgreementTerm(new ID("AGREEMENT_TERM_1"), new TypeConstraintExpr(new FunctionalExpr(core.day_is, new ValueExpr[] {}), new SimpleDomainExpr(new CONST("FRIDAY", null), core.equals)), null, new Guaranteed[] { new Guaranteed.State(new ID("GUARANTEED_STATE_1"), new TypeConstraintExpr(completion_time_vdec.getVar(), new SimpleDomainExpr(new CONST("10", units.ms), core.less_than_or_equals))) });
        at1.setDescr($descr);
        $descr = "If the customer selects OPTION_2 then the provider promises to say 'hello' once a month";
        Invocation action = new Invocation(new ID($customer_iface_id + ID.$path_separator + $op_1_name));
        action.setParameterValue(new ID($input_1_name), new CONST("hello :)", null));
        AgreementTerm at2 = new AgreementTerm(new ID("AGREEMENT_TERM_2"), new TypeConstraintExpr(customisable_vdec.getVar(), new SimpleDomainExpr(cs[1], core.equals)), null, new Guaranteed[] { new Guaranteed.Action(new ID("GUARANTEED_ACTION_1"), sla.provider, sla.mandatory, new EventExpr(core.periodic, new Expr[] { new CONST("1", units.month) }), action) });
        at2.setDescr($descr);
        this.setAgreementTerms(new AgreementTerm[] { at1, at2 });
    }
}
