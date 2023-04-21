package com.itzg.svn;

import java.io.File;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.kohsuke.args4j.Option;
import com.itzg.fileutil.GaussianSpec;

/**
 * Holds the configurable parameters of the slammer. This object works with args4j and
 * JAXB for command-line and XML based config, respectively.
 * @author Geoff
 *
 */
@XmlRootElement
public class SvnSlammerOptions {

    @Option(name = "--load", usage = "Load slam settings from an XML file")
    private File loadFrom;

    @Option(name = "--save", usage = "Save slam settings to an XML file")
    private File saveTo;

    @Option(name = "--local-repo", usage = "Path for SVN local repository to create")
    private File localRepoDir;

    @Option(name = "--scenario", usage = "dirtree-stats scenario file")
    private File scenarioFile;

    @Option(name = "--workarea", usage = "Path to a directory to wipe and use for check-outs")
    private File workareaDir;

    @Option(name = "--max-concurrent-branches", usage = "Maximum number of concurrent branches")
    private int maxConcurrentBranches = 5;

    @Option(name = "--min-concurrent-branches")
    private int minConcurrentBranches = 0;

    @Option(name = "--help", aliases = { "-?", "-h" }, usage = "Display command line usage")
    private boolean helpRequested;

    private long totalBranches = 5;

    /**
	 * A value closer to one (above 0.5) prefers creating branches. Towards zero
	 * prefers deleting branches.
	 */
    private float branchCreationAffinity = 0.75f;

    private GaussianSpec trunkCommitsPerBranching = new GaussianSpec(10, 5, 1);

    private GaussianSpec trunkCommitsPerSync = new GaussianSpec(5, 1, 2);

    private GaussianSpec syncsPerReintegrate = new GaussianSpec(3, 1, 1);

    private GaussianSpec filesToModify = new GaussianSpec(10, 5, 1);

    private GaussianSpec filesToModifyConcurrently = new GaussianSpec(3, 1, 0);

    public SvnSlammerOptions() {
    }

    public File getLocalRepoDir() {
        return localRepoDir;
    }

    public File getScenarioFile() {
        return scenarioFile;
    }

    public File getWorkareaDir() {
        return workareaDir;
    }

    public int getMaxConcurrentBranches() {
        return maxConcurrentBranches;
    }

    public int getMinConcurrentBranches() {
        return minConcurrentBranches;
    }

    public long getTotalBranches() {
        return totalBranches;
    }

    public float getBranchCreationAffinity() {
        return branchCreationAffinity;
    }

    public GaussianSpec getSyncsPerReintegrate() {
        return syncsPerReintegrate;
    }

    public GaussianSpec getFilesToModify() {
        return filesToModify;
    }

    public GaussianSpec getFilesToModifyConcurrently() {
        return filesToModifyConcurrently;
    }

    public GaussianSpec getTrunkCommitsPerBranching() {
        return trunkCommitsPerBranching;
    }

    public GaussianSpec getTrunkCommitsPerSync() {
        return trunkCommitsPerSync;
    }

    public void setLocalRepoDir(File localRepoDir) {
        this.localRepoDir = localRepoDir;
    }

    public void setScenarioFile(File scenarioFile) {
        this.scenarioFile = scenarioFile;
    }

    public void setWorkareaDir(File workareaDir) {
        this.workareaDir = workareaDir;
    }

    public void setMaxConcurrentBranches(int maxConcurrentBranches) {
        this.maxConcurrentBranches = maxConcurrentBranches;
    }

    public void setMinConcurrentBranches(int minConcurrentBranches) {
        this.minConcurrentBranches = minConcurrentBranches;
    }

    public void setTotalBranches(long totalBranches) {
        this.totalBranches = totalBranches;
    }

    public void setBranchCreationAffinity(float branchCreationAffinity) {
        this.branchCreationAffinity = branchCreationAffinity;
    }

    public void setTrunkCommitsPerBranching(GaussianSpec trunkCommitsPerBranching) {
        this.trunkCommitsPerBranching = trunkCommitsPerBranching;
    }

    public void setTrunkCommitsPerSync(GaussianSpec trunkCommitsPerSync) {
        this.trunkCommitsPerSync = trunkCommitsPerSync;
    }

    public void setSyncsPerReintegrate(GaussianSpec syncsPerReintegrate) {
        this.syncsPerReintegrate = syncsPerReintegrate;
    }

    public void setFilesToModify(GaussianSpec filesToModify) {
        this.filesToModify = filesToModify;
    }

    public void setFilesToModifyConcurrently(GaussianSpec filesToModifyConcurrently) {
        this.filesToModifyConcurrently = filesToModifyConcurrently;
    }

    @XmlTransient
    public File getLoadFrom() {
        return loadFrom;
    }

    public void setLoadFrom(File loadFrom) {
        this.loadFrom = loadFrom;
    }

    @XmlTransient
    public File getSaveTo() {
        return saveTo;
    }

    public void setSaveTo(File saveTo) {
        this.saveTo = saveTo;
    }

    public static SvnSlammerOptions loadFrom(File xmlSrc) {
        return JAXB.unmarshal(xmlSrc, SvnSlammerOptions.class);
    }

    public void save() {
        JAXB.marshal(this, getSaveTo());
    }

    @XmlTransient
    public boolean isHelpRequested() {
        return helpRequested;
    }
}
