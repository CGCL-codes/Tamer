package org.slasoi.businessManager.businessEngine.mockups;

import java.io.FileInputStream;
import java.io.IOException;
import org.slasoi.gslam.core.negotiation.SLARegistry;
import org.slasoi.gslam.syntaxconverter.SLASOIParser;
import org.slasoi.slamodel.primitives.UUID;
import org.slasoi.slamodel.sla.SLA;
import org.slasoi.slamodel.vocab.bnf;

public class SLARegistryMockup implements SLARegistry, SLARegistry.IRegister, SLARegistry.IQuery {

    private static final String BUSINESS_SLA_FILE = "ORC_Business_SLA.xml";

    private SLA sla;

    public IQuery getIQuery() {
        return this;
    }

    public IRegister getIRegister() {
        return this;
    }

    public UUID register(SLA sla, UUID[] uuids, SLAState arg2) throws RegistrationFailureException {
        this.sla = sla;
        return null;
    }

    public UUID update(UUID uuid, SLA sla, UUID[] arg2, SLAState arg3) throws UpdateFailureException {
        this.sla = sla;
        return uuid;
    }

    public SLA[] getSLA(UUID[] arg0) throws InvalidUUIDException {
        SLA[] slas = new SLA[1];
        try {
            slas[0] = createSLA();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return slas;
    }

    public UUID[] getDependencies(UUID arg0) throws InvalidUUIDException {
        return null;
    }

    public SLAMinimumInfo[] getMinimumSLAInfo(UUID[] arg0) throws InvalidUUIDException {
        return null;
    }

    public UUID[] getSLAsByState(SLAState[] arg0, boolean arg1) throws InvalidStateException {
        return null;
    }

    public SLAStateInfo[] getStateHistory(UUID arg0, boolean arg1) throws InvalidUUIDException, InvalidStateException {
        return null;
    }

    public UUID[] getUpwardDependencies(UUID arg0) throws InvalidUUIDException {
        return null;
    }

    public SLA[] getSLAsByParty(UUID arg0) throws InvalidUUIDException {
        return null;
    }

    public SLA[] getSLAsByTemplateId(UUID arg0) throws InvalidUUIDException {
        return null;
    }

    private SLA createSLA() throws IOException {
        String slaXml = readFile(SLARegistryMockup.class.getClassLoader().getResource(BUSINESS_SLA_FILE).getPath());
        SLA sla = null;
        try {
            SLASOIParser slasoiParser = new SLASOIParser();
            sla = slasoiParser.parseSLA(slaXml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(bnf.render(sla, true));
        return sla;
    }

    private String readFile(String filename) throws IOException {
        String content = null;
        FileInputStream file = new FileInputStream(filename);
        byte[] b = new byte[file.available()];
        file.read(b);
        file.close();
        content = new String(b);
        return content;
    }

    public UUID sign(UUID arg0) throws UpdateFailureException {
        return null;
    }

    public IAdminUtils getIAdmin() {
        return null;
    }
}
