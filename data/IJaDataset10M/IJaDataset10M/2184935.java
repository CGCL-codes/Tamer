package de.powerstaff.business.service;

import de.powerstaff.business.entity.Freelancer;
import java.util.List;
import de.mogwai.common.business.service.Service;
import de.powerstaff.business.dto.DataPage;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.dto.ProfileSearchRequest;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.business.entity.Project;

public interface ProfileSearchService extends Service {

    ProfileSearchRequest getLastSearchRequest() throws Exception;

    void saveSearchRequest(ProfileSearchRequest searchRequest, boolean cleanup);

    DataPage<ProfileSearchEntry> findProfileDataPage(ProfileSearchRequest aRequest, int startRow, int pageSize) throws Exception;

    void removeSavedSearchEntry(String aDocumentId);

    int getPageSize();

    List<FreelancerProfile> loadProfilesFor(Freelancer aFreelancer);
}
