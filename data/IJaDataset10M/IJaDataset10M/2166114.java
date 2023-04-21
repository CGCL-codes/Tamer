package net.sourceforge.fluxion.portal.impl;

import net.sourceforge.fluxion.portal.DatasourceBuilderService;
import net.sourceforge.fluxion.portal.DatasourceConfiguration;
import net.sourceforge.fluxion.portal.beans.RepositoryBean;
import net.sourceforge.fluxion.portal.exception.UnspecifiedConfigurationError;
import net.sourceforge.fluxion.ajax.beans.util.ProcessListener;
import java.io.File;

/**
 * Javadocs go here!
 *
 * @author Tony Burdett
 * @date 26-Sep-2008
 */
public class RuncibleDatasourceBuilderServiceImpl implements DatasourceBuilderService {

    private RepositoryBean repository;

    public RepositoryBean getRepository() {
        return repository;
    }

    public void setRepository(RepositoryBean repository) {
        this.repository = repository;
    }

    public ProcessListener getProgressListener() {
        return null;
    }

    public void setProgressListener(ProcessListener progressListener) {
    }

    public File buildDatasource(DatasourceConfiguration config) throws UnspecifiedConfigurationError {
        if (config instanceof RuncibleDatasourceConfiguration) {
        } else {
            throw new UnspecifiedConfigurationError("Unable to build a DatapublisherDatasource from a " + config.getClass().getSimpleName() + " configuration object");
        }
        return null;
    }

    public File buildMiniPublisher(DatasourceConfiguration config) throws UnspecifiedConfigurationError {
        if (config instanceof RuncibleDatasourceConfiguration) {
        } else {
            throw new UnspecifiedConfigurationError("Unable to build a DatapublisherDatasource from a " + config.getClass().getSimpleName() + " configuration object");
        }
        return null;
    }
}
