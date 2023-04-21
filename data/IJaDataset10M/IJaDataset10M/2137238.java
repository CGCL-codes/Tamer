package net.sf.sail.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import net.sf.sail.common.apps.LaunchGenericSession;
import net.sf.sail.common.apps.PreviewCurnit;
import net.sf.sail.common.apps.preview.PreviewSessionConfiguration;
import net.sf.sail.core.bundle.BundleManager;
import net.sf.sail.core.curnit.Curnit;
import net.sf.sail.core.service.ServiceContext;
import net.sf.sail.core.service.SessionManager;
import net.sf.sail.core.session.ICurnitProvider;
import net.sf.sail.core.session.SessionConfiguration;
import net.sf.sail.core.util.BinaryUtils;
import net.sf.sail.core.uuid.CurnitUuid;
import net.sf.sail.core.uuid.PodUuid;

/**
 * @author turadg
 * 
 */
public class CurnitPreviewer {

    private final Curnit curnit;

    private final PodUuid rootPodId;

    /**
	 * 
	 */
    public CurnitPreviewer(Curnit curnit) {
        this.curnit = curnit;
        rootPodId = curnit.getRootPodId();
    }

    public URL writeTempCurnit() throws IOException {
        File tempFile = BinaryUtils.tempCurnitArchive(rootPodId);
        URL curnitArchiveUrl = tempFile.toURL();
        return curnitArchiveUrl;
    }

    /**
	 * Preview the whole curnit as if the learner had begun it from a
	 * freshly-saved curnit file and using the simple launcher
	 * 
	 * @throws IOException
	 */
    public void simpleSavedPreview() throws IOException {
        PreviewCurnit.previewRootPod(rootPodId);
    }

    /**
	 * @throws IOException
	 * 
	 */
    public void simpleLivePreview() throws IOException {
        SessionConfiguration sp = new PreviewSessionConfiguration(curnit);
        LaunchGenericSession.launchSession(sp);
    }

    /**
	 * Preview the whole curnit as if the learner had begun it, using the
	 * services architecture.
	 * 
	 * @param bundleSource
	 *            TODO
	 * @throws IOException
	 */
    public void bundleLivePreview(InputStream bundleSource) throws IOException {
        previewLiveServiceBundled(curnit, bundleSource);
    }

    public void bundleSavedPreview(InputStream bundleSource, boolean newJVM) {
        throw new UnsupportedOperationException("not yet not implemented");
    }

    /**
	 * Preview the whole curnit, as if the learner had begun it, using the
	 * services architecture.
	 * 
	 * @param curnit
	 *            TODO
	 * @param bundleSource
	 *            TODO
	 * 
	 * @throws IOException
	 */
    static void previewLiveServiceBundled(final Curnit curnit, InputStream bundleSource) throws IOException {
        BundleManager bundleManager = new BundleManager();
        ServiceContext serviceContext = bundleManager.getServiceContext();
        ICurnitProvider provider = new ICurnitProvider() {

            public Curnit getCurnit(CurnitUuid curnitId) {
                return curnit;
            }

            public Collection<CurnitUuid> getRegisteredCurnitUuids() {
                ArrayList<CurnitUuid> curnitIds = new ArrayList<CurnitUuid>();
                curnitIds.add(curnit.getCurnitId());
                return curnitIds;
            }
        };
        serviceContext.addService(ICurnitProvider.class, provider);
        bundleManager.addBundles(bundleSource);
        bundleManager.initializeBundles();
        SessionManager manager = (SessionManager) serviceContext.getService(SessionManager.class);
        manager.start(serviceContext);
    }

    public static void main(String[] args) {
    }
}
