package net.sf.buildbox.buildrobot.emodel;

import net.sf.buildbox.reactor.model.JobId;

/**
 * TODO: split into "FlatCheckoutJobId" and "DeepUpdateJobId" as they seem quite fundamentally different
 */
@Deprecated
public class CheckoutJobId extends JobId {

    private final boolean flatCheckout;

    private final String vcsId;

    private final String vcsPath;

    private final CheckoutJobParams checkoutJobParams;

    /**
     * Prepares checkout job.
     *
     * @param vcsId   which VCS to checkout from, see {@link net.sf.buildbox.devmodel.VcsLocation#getVcsId()}
     * @param vcsPath location inside vcs, see {@link net.sf.buildbox.devmodel.VcsLocation#getLocationPath()}
     */
    private CheckoutJobId(boolean isFlatCheckout, String vcsId, String vcsPath, CheckoutJobParams checkoutJobParams) {
        super(vcsId, null, vcsPath + ":" + (isFlatCheckout ? "co" : "up"));
        vcsId.toString();
        vcsPath.toString();
        this.vcsId = vcsId;
        this.vcsPath = vcsPath;
        this.flatCheckout = isFlatCheckout;
        this.checkoutJobParams = checkoutJobParams;
    }

    /**
     * Constructs job that brings only files from top-level directory.
     * The only purpose is to determine locations of submodules that will be checked out separately.
     * Based on the list of submodules, either full or limited update follows.
     */
    public static CheckoutJobId flatCheckout(String vcsId, String vcsPath, CheckoutJobParams checkoutJobParams) {
        return new CheckoutJobId(true, vcsId, vcsPath, checkoutJobParams);
    }

    /**
     * Constructs job that updates existent working copy.
     * While doing so, it avoids useless updates of specified subtrees.
     */
    public static CheckoutJobId treeUpdate(String vcsId, String vcsPath, CheckoutJobParams checkoutJobParams) {
        return new CheckoutJobId(false, vcsId, vcsPath, checkoutJobParams);
    }

    @Deprecated
    protected String getParamString() {
        final StringBuilder sb = new StringBuilder();
        if (checkoutJobParams == null || checkoutJobParams.revision == null) {
            sb.append("HEAD");
        } else {
            sb.append("r");
            sb.append(checkoutJobParams.revision);
        }
        return sb.toString();
    }

    public String getVcsId() {
        return vcsId;
    }

    public String getVcsType() {
        return initInfo().vcsType;
    }

    public String getVcsUrl() {
        return initInfo().vcsUrl;
    }

    public String getVcsPath() {
        return vcsPath;
    }

    public String getRevision() {
        return initInfo().revision;
    }

    private CheckoutJobParams initInfo() {
        if (checkoutJobParams == null) {
            throw new IllegalStateException("missing initinfo on " + toString());
        }
        return checkoutJobParams;
    }

    public boolean isFlatCheckout() {
        return flatCheckout;
    }
}
