package com.hack23.cia.service.api.dto.impl.factory;

import com.hack23.cia.model.api.application.administration.UserSessionData;
import com.hack23.cia.model.api.application.configuration.AgencyData;
import com.hack23.cia.model.api.application.configuration.PortalData;
import com.hack23.cia.model.api.application.content.LanguageContentData;
import com.hack23.cia.model.api.application.content.LanguageData;
import com.hack23.cia.model.api.dto.common.AgencyDto;
import com.hack23.cia.model.api.dto.common.LanguageContentDto;
import com.hack23.cia.model.api.dto.common.LanguageDto;
import com.hack23.cia.model.api.dto.common.PortalDto;
import com.hack23.cia.service.api.dto.api.application.UserSessionDto;
import com.hack23.cia.service.api.dto.api.common.AgencyDtoImpl;
import com.hack23.cia.service.api.dto.api.common.LanguageContentDtoImpl;
import com.hack23.cia.service.api.dto.api.common.LanguageDtoImpl;
import com.hack23.cia.service.api.dto.api.common.PortalDtoImpl;
import com.hack23.cia.service.api.dto.api.factory.ApplicationDtoFactory;

/**
 * The Class ApplicationModelFactoryFactoryImpl.
 */
public class ApplicationDtoFactoryImpl extends AbstractDtoFactoryImpl implements ApplicationDtoFactory {

    @Override
    public final AgencyDto createAgencyDto(final AgencyData agencyData) {
        return new AgencyDtoImpl(agencyData.getId(), agencyData.getName());
    }

    @Override
    public final LanguageContentDto createLanguageContentDto(final LanguageContentData updatedLanguageContent) {
        return new LanguageContentDtoImpl(updatedLanguageContent.getContent(), updatedLanguageContent.getContentPropertyName(), updatedLanguageContent.getDescription(), createLanguageDto(updatedLanguageContent.getLanguageData()), updatedLanguageContent.getId());
    }

    @Override
    public final LanguageDto createLanguageDto(final LanguageData language) {
        return new LanguageDtoImpl();
    }

    @Override
    public final PortalDto createPortalDto(final PortalData portal) {
        return new PortalDtoImpl();
    }

    @Override
    public final UserSessionDto createUserSessionDto(final UserSessionData userSession) {
        return new UserSessionDto(userSession);
    }
}
