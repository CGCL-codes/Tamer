package org.eclipse.update.core;

import java.net.*;
import org.eclipse.core.runtime.*;
import org.eclipse.osgi.util.NLS;
import org.eclipse.update.internal.core.Messages;
import org.eclipse.update.internal.core.UpdateCore;

/**
 * Base site content provider
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves.
 * </p>
 */
public abstract class SiteContentProvider implements ISiteContentProvider {

    private URL base;

    private ISite site;

    /**
	 * Constructor for SiteContentProvider
	 */
    public SiteContentProvider(URL url) {
        super();
        this.base = url;
    }

    /**
	 * Returns the URL of this site
	 * 
	 * @see ISiteContentProvider#getURL()
	 * @since 2.0
	 */
    public URL getURL() {
        return base;
    }

    /**
	 * Returns a URL for the identified archive
	 * 
	 * @see ISiteContentProvider#getArchiveReference(String)
	 * @since 2.0
	 */
    private URL getArchiveReference1(String archiveID) throws CoreException {
        try {
            return new URL(getURL(), archiveID);
        } catch (MalformedURLException e) {
            throw Utilities.newCoreException(NLS.bind(Messages.SiteContentProvider_ErrorCreatingURLForArchiveID, (new String[] { archiveID, getURL().toExternalForm() })), e);
        }
    }

    /**
	 * Returns the site for this provider
	 * 
	 * @see ISiteContentProvider#getSite()
	 * @since 2.0
	 */
    public ISite getSite() {
        return site;
    }

    /**
	 * Sets the site for this provider
	 * 
	 * @param site site for this provider
	 * @since 2.0
	 */
    public void setSite(ISite site) {
        this.site = site;
    }

    public URL getArchiveReference(String archiveId) throws CoreException {
        URL contentURL = null;
        contentURL = getArchiveURLfor(archiveId);
        if (contentURL == null) {
            return getArchiveReference1(archiveId);
        }
        return contentURL;
    }

    /**
	 * return the URL associated with the id of teh archive for this site
	 * return null if the archiveId is null, empty or 
	 * if teh list of archives on the site is null or empty
	 * of if there is no URL associated with the archiveID for this site
	 */
    private URL getArchiveURLfor(String archiveId) {
        URL result = null;
        boolean found = false;
        IArchiveReference[] siteArchives = getSite().getArchives();
        if (siteArchives.length > 0) {
            for (int i = 0; i < siteArchives.length && !found; i++) {
                if (UpdateCore.DEBUG && UpdateCore.DEBUG_SHOW_INSTALL) UpdateCore.debug("GetArchiveURL for:" + archiveId + " compare to " + siteArchives[i].getPath());
                if (archiveId.trim().equalsIgnoreCase(siteArchives[i].getPath())) {
                    result = siteArchives[i].getURL();
                    found = true;
                    break;
                }
            }
        }
        return result;
    }
}
