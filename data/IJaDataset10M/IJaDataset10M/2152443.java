package org.xfeep.asura.bootstrap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.xfeep.asura.core.ComponentDefinition;
import org.xfeep.asura.core.ContractType;
import org.xfeep.asura.core.CoreConsts;
import org.xfeep.asura.core.MultiplicityType;
import org.xfeep.asura.core.ReferenceDefinition;
import org.xfeep.asura.core.match.SmartMatcher;

@XmlType
public class DependDefinitionInfo {

    @XmlAttribute
    public String service;

    @XmlAttribute
    public String name;

    @XmlAttribute
    public ContractType contractType = ContractType.CAREFUL;

    @XmlAttribute
    public String matcher = "";

    @XmlAttribute
    public int order = CoreConsts.DEPEND_REF_DEFALUT_ORDER;

    public ReferenceDefinition createDependReferenceDefinition(ComponentDefinition componentDefinition) throws ClassNotFoundException {
        ReferenceDefinition rd = new ReferenceDefinition();
        rd.setComponentDefinition(componentDefinition);
        rd.setName(name);
        rd.setServiceClass(componentDefinition.getImplement().getClassLoader().loadClass(service));
        rd.setMultiplicityType(MultiplicityType.ONE_ONE);
        if (matcher.length() > 0) {
            rd.setMatcher(SmartMatcher.getMatcher(matcher, componentDefinition.getImplement()));
        }
        rd.setContractType(contractType);
        rd.setOrder(order);
        return rd;
    }

    public static DependDefinitionInfo convert(ReferenceDefinition drd) {
        if (drd.getBind() != null && drd.getInjectProperties() != null || CoreConsts.CONFIG_SERVICE_REF_NAME.equals(drd.getName())) {
            throw new IllegalArgumentException("must be depend definition");
        }
        DependDefinitionInfo rdi = new DependDefinitionInfo();
        rdi.name = drd.getName();
        rdi.service = drd.getServiceClass().getName();
        rdi.contractType = drd.getContractType();
        if (drd.getMatcher() != null) {
            rdi.matcher = drd.getMatcher().toString();
        }
        rdi.order = drd.getOrder();
        return rdi;
    }
}
