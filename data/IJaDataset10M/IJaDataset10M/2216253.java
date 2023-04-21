package se.inera.ifv.medcert.spi.authorization.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.inera.ifv.auth.userdetails.MedicalPersonalUserImpl;
import se.inera.ifv.medcert.core.entity.CareUnitInterface;
import se.inera.ifv.medcert.core.spi.authentication.MedicalPersonalUser;
import se.inera.ifv.medcert.core.spi.authorization.GetAuthorizationService;
import se.inera.ifv.medcert.spi.authorization.vo.AuthCareGiverImpl;
import se.inera.ifv.medcert.spi.authorization.vo.AuthCareUnitImpl;

public class GetAuthorizationServiceImpl implements GetAuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(GetAuthorizationServiceImpl.class);

    private static final String HANNA = "TST5565594230-106C";

    private static final String MARCUS = "TST5565594230-106J";

    private Map<String, List<CareUnitInterface>> perms = null;

    public List<CareUnitInterface> getAuthorizedCareUnitsForHosPerson(String hosPersonHsaId) {
        log.debug("Authorized care units for {}", hosPersonHsaId);
        if (perms == null) {
            this.init(hosPersonHsaId);
        }
        if (perms.containsKey(hosPersonHsaId)) {
            return perms.get(hosPersonHsaId);
        } else {
            return perms.get("other");
        }
    }

    public boolean isAuthorizedForCareUnit(final MedicalPersonalUser user2, String hsaId) {
        final MedicalPersonalUserImpl user = (MedicalPersonalUserImpl) user2;
        if (perms == null) {
            this.init(user.getHsaId());
        }
        log.debug("Is {} authorized for hsa id {}", user.getHsaId(), hsaId);
        if (user.getHsaId().equals(MARCUS) && hsaId.equals("IFV1239877878-103J")) {
            log.debug("Checking if Marcus is already authorized for subunit");
            boolean found = false;
            if (perms.get(MARCUS) != null) {
                final List<CareUnitInterface> units = perms.get(MARCUS);
                for (final CareUnitInterface cu : units) {
                    if (cu.getHsaId().equals(hsaId)) {
                        found = true;
                    }
                }
            }
            log.debug("Check result {}", found);
            if (!found) {
                final AuthCareUnitImpl careUnit = new AuthCareUnitImpl();
                careUnit.setHsaId("IFV1239877878-103J");
                careUnit.setName("VårdEnhet2A1");
                careUnit.setEmail("www@hotmail.com");
                careUnit.setWorkplaceCode("0123456789014");
                careUnit.setPhone("082-237342");
                careUnit.setPostalAddress("Agatan 34");
                careUnit.setPostalCode("12345");
                careUnit.setPostalCity("Teststad");
                careUnit.setCareGiver(perms.get(MARCUS).get(0).getCareGiver());
                log.debug("Adding subunit to Marcus list of authorized care units");
                perms.get(MARCUS).add(careUnit);
                user.addCareUnit(careUnit);
            }
        }
        if (perms.containsKey(user.getHsaId())) {
            for (final CareUnitInterface cu : perms.get(user.getHsaId())) {
                String careUnitHsaID = cu.getHsaId();
                if (careUnitHsaID.equals(hsaId)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void init(final String hosPersonHsaId) {
        this.perms = new HashMap<String, List<CareUnitInterface>>();
        ArrayList<CareUnitInterface> response = new ArrayList<CareUnitInterface>();
        if (hosPersonHsaId.equalsIgnoreCase(HANNA)) {
            AuthCareGiverImpl ifv = createCareGiverIFVTestData();
            AuthCareUnitImpl careUnitA = createCareUnitA(ifv);
            AuthCareUnitImpl careUnit1A = createCareUnit1A(ifv);
            response.add(careUnitA);
            response.add(careUnit1A);
            perms.put(HANNA, response);
        } else if (hosPersonHsaId.equalsIgnoreCase(MARCUS)) {
            AuthCareGiverImpl ifv = createCareGiverIFVTestData();
            AuthCareUnitImpl careUnitA = createCareUnitA(ifv);
            AuthCareUnitImpl careUnit2A = createCareUnit2A(ifv);
            response.add(careUnitA);
            response.add(careUnit2A);
            perms.put(MARCUS, response);
        } else {
            AuthCareGiverImpl ifv = createCareGiverIFVTestData();
            AuthCareUnitImpl careUnitA = createCareUnitA(ifv);
            AuthCareUnitImpl careUnit1A = createCareUnit1A(ifv);
            AuthCareUnitImpl careUnit2A = createCareUnit2A(ifv);
            response.add(careUnitA);
            response.add(careUnit1A);
            response.add(careUnit2A);
            perms.put("other", response);
        }
    }

    private AuthCareGiverImpl createCareGiverIFVTestData() {
        AuthCareGiverImpl giver1 = new AuthCareGiverImpl();
        giver1.setHsaId("IFV1239877878-0001");
        giver1.setName("IFV Testdata");
        return giver1;
    }

    private AuthCareUnitImpl createCareUnit2A(AuthCareGiverImpl giver1) {
        AuthCareUnitImpl unit3 = new AuthCareUnitImpl();
        unit3.setHsaId("IFV1239877878-103H");
        unit3.setName("VårdEnhet2A");
        unit3.setCareGiver(giver1);
        return unit3;
    }

    private AuthCareUnitImpl createCareUnit1A(AuthCareGiverImpl giver1) {
        AuthCareUnitImpl unit2 = new AuthCareUnitImpl();
        unit2.setHsaId("IFV1239877878-103F");
        unit2.setName("VårdEnhet1A");
        unit2.setCareGiver(giver1);
        return unit2;
    }

    private AuthCareUnitImpl createCareUnitA(AuthCareGiverImpl giver1) {
        AuthCareUnitImpl unit1 = new AuthCareUnitImpl();
        unit1.setHsaId("IFV1239877878-103D");
        unit1.setName("VårdEnhetA");
        unit1.setCareGiver(giver1);
        return unit1;
    }
}
