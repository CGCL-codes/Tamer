package net.sf.chellow.billing;

import java.util.List;
import net.sf.chellow.monad.DeployerException;
import net.sf.chellow.monad.DesignerException;
import net.sf.chellow.monad.Hiber;
import net.sf.chellow.monad.Invocation;
import net.sf.chellow.monad.MonadUtils;
import net.sf.chellow.monad.ProgrammerException;
import net.sf.chellow.monad.Urlable;
import net.sf.chellow.monad.UserException;
import net.sf.chellow.monad.XmlTree;
import net.sf.chellow.monad.types.MonadUri;
import net.sf.chellow.monad.types.UriPathElement;
import net.sf.chellow.physical.HhEndDate;
import net.sf.chellow.physical.Mpan;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings("serial")
public class DcsService extends Service {

    public static DcsService getContractDcs(Long id) throws UserException, ProgrammerException {
        DcsService contract = (DcsService) Hiber.session().get(DcsService.class, id);
        if (contract == null) {
            throw UserException.newOk("There isn't a DCS contract with that id.");
        }
        return contract;
    }

    private Dcs provider;

    public DcsService() {
        setTypeName("contract-dcs");
    }

    public DcsService(int type, String name, HhEndDate startDate, HhEndDate finishDate, String chargeScript, Dcs dcs) throws UserException, ProgrammerException, DesignerException {
        intrinsicUpdate(type, name, chargeScript, dcs);
    }

    public Dcs getProvider() {
        return provider;
    }

    void setProvider(Dcs provider) {
        this.provider = provider;
    }

    protected void intrinsicUpdate(int type, String name, String chargeScript, Dcs provider) throws UserException, ProgrammerException, DesignerException {
        super.update(type, name, chargeScript);
        setProvider(provider);
    }

    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj instanceof DcsService) {
            DcsService contract = (DcsService) obj;
            isEqual = contract.getId().equals(getId());
        }
        return isEqual;
    }

    public MonadUri getUri() throws ProgrammerException, UserException {
        return provider.contractsInstance().getUri().resolve(getUriId()).append("/");
    }

    public void httpPost(Invocation inv) throws ProgrammerException, UserException, DesignerException, DeployerException {
        int type = inv.getInteger("type");
        String name = inv.getString("name");
        String chargeScript = inv.getString("charge-script");
        if (!inv.isValid()) {
            throw UserException.newInvalidParameter(document());
        }
        update(type, name, chargeScript);
        Hiber.commit();
        inv.sendOk(document());
    }

    private Document document() throws ProgrammerException, UserException, DesignerException {
        Document doc = MonadUtils.newSourceDocument();
        Element source = doc.getDocumentElement();
        source.appendChild(getXML(new XmlTree("dcs").put("organization"), doc));
        return doc;
    }

    public void httpGet(Invocation inv) throws DesignerException, ProgrammerException, UserException, DeployerException {
        inv.sendOk(document());
    }

    public void httpDelete(Invocation inv) throws ProgrammerException, UserException {
    }

    public String toString() {
        return "Contract id " + getId() + " " + getProvider() + " name " + getName();
    }

    public Urlable getChild(UriPathElement uriId) throws ProgrammerException, UserException {
        return null;
    }

    @Override
    public List<Mpan> getMpans(Account account, HhEndDate from, HhEndDate to) {
        return null;
    }
}
