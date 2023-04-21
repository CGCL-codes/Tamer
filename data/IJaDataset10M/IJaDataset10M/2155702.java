package org.gruposp2p.dnie.server.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.gruposp2p.dnie.model.DNIeAuthority;
import org.gruposp2p.dnie.model.DNIeDocumentToSign;
import org.gruposp2p.dnie.model.DNIeDocumentToSignOption;
import org.gruposp2p.dnie.model.DNIeDocumentType;
import org.gruposp2p.dnie.model.DNIeOptionSelectionType;
import org.gruposp2p.dnie.model.DNIeUser;
import org.gruposp2p.dnie.utils.DateUtils;

/**
 *
 * @author jj
 */
public class TestData {

    public static DNIeUser getDNIeUser(DNIeAuthority userType) {
        DNIeUser dnieUser = new DNIeUser();
        dnieUser.setName("Murluengo");
        dnieUser.setAuthority(DNIeAuthority.ROLE_USER);
        return dnieUser;
    }

    public static DNIeDocumentToSign getDNIeDocumentToSign() {
        DNIeDocumentToSign dnieDocumentToSign = new DNIeDocumentToSign();
        dnieDocumentToSign.setContent("¿Crees que el acceso a Internet debería ser Universal?");
        dnieDocumentToSign.setDateCreated(DateUtils.getTodayDate());
        dnieDocumentToSign.setDateBegin(DateUtils.getTodayDate());
        dnieDocumentToSign.setDateEnd(DateUtils.getTodayDate());
        dnieDocumentToSign.setOptionSelectionType(DNIeOptionSelectionType.EXCLUSIVE);
        dnieDocumentToSign.setSignatureDataType(DNIeDocumentType.MANIFEST);
        dnieDocumentToSign.setTitle("Niskawaskas");
        dnieDocumentToSign.setUrl("gruposp2p.org");
        dnieDocumentToSign.setDNIeDocumentToSignoptionCollection(null);
        DNIeDocumentToSignOption option1 = new DNIeDocumentToSignOption();
        option1.setContent("1111111111");
        Collection<DNIeDocumentToSignOption> collection = new ArrayList<DNIeDocumentToSignOption>();
        collection.add(option1);
        DNIeDocumentToSignOption option2 = new DNIeDocumentToSignOption();
        option2.setContent("222222222");
        collection.add(option2);
        dnieDocumentToSign.setDNIeDocumentToSignoptionCollection(collection);
        return dnieDocumentToSign;
    }
}
