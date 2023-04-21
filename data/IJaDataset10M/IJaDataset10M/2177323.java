package com.hack23.cia.fit.scenarios.service.common;

import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import com.hack23.cia.model.api.application.configuration.AgencyData;
import com.hack23.cia.model.api.application.configuration.PortalData;
import com.hack23.cia.model.api.application.content.LanguageData;
import com.hack23.cia.service.api.dto.api.common.AgencyDtoImpl;
import com.hack23.cia.service.api.dto.api.common.LanguageDtoImpl;
import com.hack23.cia.service.api.dto.api.common.PortalDtoImpl;
import com.hack23.cia.service.impl.configuration.ConfigurationService;
import com.hack23.cia.testfoundation.AbstractFunctionalIntegrationTest;
import com.hack23.cia.testfoundation.MockWebContextLoader;

/**
 * The Class AbstractFunctionalIntegrationTransactionalTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockWebContextLoader.class, locations = { "classpath:/META-INF/sessionFactory.xml", "classpath:/META-INF/cia-application-context-model.xml", "classpath:/META-INF/cia-application-context-dao.xml", "classpath:/META-INF/cia-application-context-service.xml", "classpath:/META-INF/cia-application-context-ehcache-spring-annotations.xml", "classpath:META-INF/cia-application-context-service-agent-operations.xml", "classpath:/META-INF/cia-application-context-web-actionhandlers.xml", "classpath:/META-INF/cia-application-context-web-viewfactories.xml", "file:src/main/webapp/WEB-INF/cia-webservices-servlet.xml" })
@TransactionConfiguration(transactionManager = "JtaTransactionManager", defaultRollback = false)
@Transactional
public abstract class AbstractFunctionalIntegrationTransactionalTest extends AbstractFunctionalIntegrationTest {

    /** The configuration service. */
    @Autowired
    @Qualifier("configurationService")
    protected ConfigurationService configurationService;

    /** The datasource. */
    @Autowired
    private DataSource datasource;

    /** The transaction manager. */
    @Autowired
    @Qualifier("JtaTransactionManager")
    protected PlatformTransactionManager transactionManager;

    /**
	 * Clean out old test data.
	 */
    protected final void cleanOutOldTestData() {
        final TransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        final TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        final AgencyData agency = configurationService.loadAgency();
        final PortalData[] portals = new PortalData[agency.getPortalsData().size()];
        for (final PortalData portal : agency.getPortalsData().toArray(portals)) {
            if ("Default portal".equals(portal.getName())) {
                configurationService.deletePortal(new PortalDtoImpl(portal.getId(), portal.getActiveByDefault(), portal.getName(), portal.getUsageOrder(), portal.getMatchesUrl(), portal.getTitleDescription()));
            }
        }
        final LanguageData[] languages = new LanguageData[agency.getLanguagesData().size()];
        for (final LanguageData language : agency.getLanguagesData().toArray(languages)) {
            if ("Default".equalsIgnoreCase(language.getName().trim())) {
                configurationService.deleteLanguage(new AgencyDtoImpl(agency.getId(), agency.getName()), new LanguageDtoImpl(language.getId(), false, language.getName(), 1, language.getShortCode()));
            }
        }
        transactionManager.commit(transaction);
    }

    @Override
    protected final Connection getDatabaseConnection() throws Exception {
        return datasource.getConnection();
    }
}
