package net.sf.orexio.jdcp.compression;

import net.sf.orexio.jdcp.common.IGlobalSettings;
import net.sf.orexio.jdcp.common.IServiceSettings;

/**
 * Handle compression service settings.
 * @author alois.cochard@gmail.com
 */
public class CompressionServiceSettings implements IServiceSettings {

    /**
	 * Store global settings.
	 */
    private IGlobalSettings globalSettings = null;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final String getParameter(final String parameterName) {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final String[] getParametersOptional() {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final String[] getParametersRequiered() {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final void setParameter(final String parameterName, final String parameterValue) {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final void setGlobalSettings(final IGlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    /**
	 * {@inheritDoc}
	 */
    public final IGlobalSettings getGlobalSettings() {
        return globalSettings;
    }
}
