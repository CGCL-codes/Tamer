package se.inera.ifv.medcert.spi.organisation.impl;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.inera.ifv.medcert.core.entity.CareUnitInterface;
import se.inera.ifv.medcert.core.entity.User;
import se.inera.ifv.medcert.core.repository.CareUnitRepository;
import se.inera.ifv.medcert.core.service.UserService;
import se.inera.ifv.medcert.core.spi.authorization.GetAuthorizationService;
import se.inera.ifv.medcert.core.spi.organisation.GetHealthCareInfoService;
import se.inera.ifv.medcert.core.spi.organisation.GetOrganisationInfoService;

@Service
public class GetHealthCareInfoServiceImpl implements GetHealthCareInfoService {

    private static final Logger log = LoggerFactory.getLogger(GetHealthCareInfoServiceImpl.class);

    @Autowired
    private CareUnitRepository careUnitRepository;

    @Autowired
    private GetAuthorizationService getAuthorizationService;

    @Autowired
    private GetOrganisationInfoService getOrganisationInfoService;

    @Autowired
    private UserService userService;

    @Value("${lookup.strategy}")
    private String lookupStrategy;

    public CareUnitInterface findForHsaId(String hsaId) {
        log.debug("Find care unit with HSA id {}, lookup strategy that will be used: {}", hsaId, lookupStrategy);
        CareUnitInterface careUnit = null;
        if ("hsa".equals(lookupStrategy)) {
            careUnit = getCareUnitFromHsa(hsaId);
        } else {
            careUnit = careUnitRepository.findByHsaId(hsaId);
        }
        return careUnit;
    }

    public List<CareUnitInterface> findCareUnitsForAdmin(String careUnitAdminUsername) {
        log.debug("Find care units for user {}, lookup strategy: {}", careUnitAdminUsername, lookupStrategy);
        if (lookupStrategy.equalsIgnoreCase("hsa")) {
            return this.getCareUnitsFromHsaForUser(careUnitAdminUsername);
        } else {
            final List<CareUnitInterface> units = new ArrayList<CareUnitInterface>();
            final User u = this.userService.findByUsername(careUnitAdminUsername);
            for (final CareUnitInterface c : u.getCareUnits()) {
                units.add(c);
            }
            return units;
        }
    }

    private CareUnitInterface getCareUnitFromHsa(String hsaId) {
        try {
            return getOrganisationInfoService.getCareUnitFullInfo(hsaId);
        } catch (Exception e) {
            throw new IllegalArgumentException("The HSA id was not valid. Reason: " + e.getMessage());
        }
    }

    private List<CareUnitInterface> getCareUnitsFromHsaForUser(final String user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        try {
            return this.getAuthorizationService.getAuthorizedCareUnitsForHosPerson(user);
        } catch (final Exception e) {
            throw new IllegalArgumentException("The HSA service could not handle the request for user " + user + ". Reason: " + e.getMessage());
        }
    }
}
