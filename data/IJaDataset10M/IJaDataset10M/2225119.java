package joshua.e2e.jhu;

import joshua.e2e.scripts.joshua.AlignedSubsampler;
import joshua.e2e.scripts.joshua.Subsampler;
import joshua.e2e.scripts.joshua.CompileJoshdir;
import joshua.e2e.scripts.joshua.ExtractGrammarFromJoshdir;
import joshua.e2e.scripts.joshua.Mert;
import joshua.e2e.scripts.joshua.JoshuaDecoder;
import joshua.e2e.scripts.joshua.ExtractTopCand;
import joshua.e2e.scripts.joshua.Sgmlize;
import joshua.e2e.scripts.Concatenate;
import joshua.e2e.scripts.ShellPipe;
import joshua.e2e.scripts.WclCheck;
import joshua.e2e.UnsatisfiableArchitectureException;
import joshua.e2e.BashScript;
import joshua.e2e.AbstractScript;
import joshua.e2e.QsubSubmitter;
import joshua.e2e.NullSubmitter;
import joshua.e2e.JobSubmitter;
import joshua.e2e.Site;
import joshua.e2e.NullJob;
import joshua.e2e.Job;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.LinkedList;
import java.util.List;
import java.io.IOException;
import java.io.File;

/**
 * This executable gives an example of how to use the {@link joshua.e2e} framework for writing an end-to-end pipeline. This particular implementation is (nearly) the one used for Arabic--English translation at Johns Hopkins University.
 *
 * @author wren ng thornton <wren@users.sourceforge.net>
 * @version $LastChangedDate: 2009-03-26 15:06:57 -0400 (Thu, 26 Mar 2009) $
 */
public class EndToEnd {

    private final EndToEndCLI cli;

    private final Site site;

    private final JobSubmitter<BashScript> qsub;

    private final ShortenFile sf;

    private final String outDir;

    protected int stepNumber;

    public EndToEnd(EndToEndCLI cli, Site site, JobSubmitter<BashScript> qsub, ShortenFile sf, String outDir) {
        this.cli = cli;
        this.site = site;
        this.qsub = qsub;
        this.sf = sf;
        this.outDir = outDir;
        this.stepNumber = 0;
    }

    public static final String canonicalPath(String file) throws IOException {
        return new File(file).getCanonicalPath();
    }

    public EndToEnd setStep(int step) {
        this.stepNumber = step;
        return this;
    }

    /**
	 * Dynamically prune out parts of the pipeline we don't need to run. The current implementation only supports pruning entire prefixes of the pipeline. Future implementations may also support pruning complete suffixes of the pipeline.
	 */
    public <S extends AbstractScript> Job<S> checkStep(Job<S> job) throws IOException {
        if (this.cli.startStep > this.stepNumber) {
            job.submit(new NullSubmitter<S>());
        }
        return job;
    }

    public String scriptName(String basename) {
        return String.format("step%02d-%s__%s.sh", this.stepNumber, basename, this.cli.tag);
    }

    protected Job<BashScript> preprocessSkipping(Pattern skip, Pattern endMatch, String endReplace, String file) throws IOException, UnsatisfiableArchitectureException {
        Matcher m = skip.matcher(file);
        if (m.find()) {
            System.out.println("Choosing not to preprocess " + file);
            this.sf.shorten(file);
            return new NullJob<BashScript>(new BashScript("nowhere", "nothing", canonicalPath(file)));
        } else {
            String ppFile;
            Matcher m2 = endMatch.matcher(file);
            if (m2.find()) {
                ppFile = m2.replaceFirst(endReplace);
            } else {
                throw new RuntimeException("Couldn't change suffix on " + file);
            }
            return this.checkStep(this.preprocess(file, ppFile));
        }
    }

    protected Job<BashScript> preprocess(String originalFileName, String preprocessedFileName) throws IOException, UnsatisfiableArchitectureException {
        return new ShellPipe(this.outDir, this.scriptName("preprocess__" + this.sf.shorten(originalFileName)), "'" + this.site.getEnv(null, "JOB_BIN") + "/preprocess.pl'", canonicalPath(originalFileName), this.outDir + File.separator + preprocessedFileName);
    }

    public Job<BashScript> mbrReranking(int testingS, Job<BashScript> configQ, Job<BashScript> nbestQ) {
        return nbestQ;
    }

    public static void main(String[] args) throws IOException, UnsatisfiableArchitectureException {
        final EndToEnd e2e;
        {
            EndToEndCLI cli = new EndToEndCLI(args);
            CLSP site = new CLSP();
            QsubSubmitter qsub = site.getJobSubmitter(cli.isDryrun);
            String outDir = System.getProperty("user.dir");
            ShortenFile sf = new ShortenFile(outDir + File.separator + "shorten_ids", cli.fext);
            e2e = new EndToEnd(cli, site, qsub, sf, outDir);
        }
        e2e.setStep(1);
        List<Job<BashScript>> referenceQs = new LinkedList<Job<BashScript>>();
        {
            Pattern refsSkip = Pattern.compile("\\.(?:" + e2e.cli.eext + "|ref)\\.\\d+$");
            Pattern refsSuffix = Pattern.compile("\\.(\\d+)\\.sgm$");
            for (String reference : e2e.cli.mertReferences) {
                referenceQs.add(e2e.preprocessSkipping(refsSkip, refsSuffix, ".pp.ref.$1", reference));
            }
        }
        Job<BashScript> developmentQ;
        List<Job<BashScript>> testingQs = new LinkedList<Job<BashScript>>();
        {
            Pattern sgm = Pattern.compile("\\.sgm$");
            String ppFext = ".pp." + e2e.cli.fext;
            {
                List<Job<BashScript>> devs = new LinkedList<Job<BashScript>>();
                devs.add(e2e.preprocessSkipping(Pattern.compile("\\." + e2e.cli.fext + "$"), sgm, ppFext, e2e.cli.mertTrainingFile));
                devs.addAll(referenceQs);
                developmentQ = e2e.checkStep(new WclCheck(e2e.outDir, e2e.scriptName("wcl-check"), devs));
            }
            for (String testFile : e2e.cli.testingFiles) {
                testingQs.add(e2e.checkStep(e2e.preprocess(testFile, sgm.matcher(testFile).replaceFirst(ppFext))));
            }
        }
        e2e.setStep(2);
        Job<BashScript> bicorpusQ;
        {
            Job<BashScript> subsampleSeedQ;
            {
                List<Job<BashScript>> allForeign = new LinkedList<Job<BashScript>>();
                allForeign.add(developmentQ);
                allForeign.addAll(testingQs);
                subsampleSeedQ = e2e.checkStep(new Concatenate(e2e.outDir, e2e.scriptName("concatenate"), e2e.outDir + File.separator + "all_files." + e2e.cli.fext, allForeign));
            }
            bicorpusQ = e2e.checkStep(new Subsampler(e2e.outDir, e2e.scriptName("subsample"), e2e.site, e2e.cli.subsampleMB, e2e.cli.subsampleRatio, e2e.cli.subsampleFpath, e2e.cli.subsampleEpath, e2e.cli.fext, e2e.cli.eext, subsampleSeedQ, e2e.outDir));
        }
        e2e.setStep(3);
        Job<BashScript> alignmentsQ, alignedBicorpusQ;
        {
            Giza giza = new Giza(e2e.outDir, e2e.scriptName("giza"), e2e.site, e2e.cli.fext, e2e.cli.eext, e2e.cli.lm, bicorpusQ, e2e.outDir);
            alignmentsQ = e2e.checkStep(giza);
            alignedBicorpusQ = e2e.checkStep(giza.filteredBicorpus);
        }
        e2e.setStep(4);
        Job<BashScript> joshdirQ = e2e.checkStep(new CompileJoshdir(e2e.outDir, e2e.scriptName("compile_sa"), e2e.site, 6000, e2e.cli.fext, e2e.cli.eext, alignedBicorpusQ, alignmentsQ, e2e.outDir + File.separator + "binary_bicorpus"));
        e2e.setStep(5);
        /**
		 * A local class for adjusting the scriptName and outputFile based out outputs of other scripts. Closes over e2e to gain access to standard methods for scriptName munging and corpus name shortening.
		 */
        class ExtractGrammar extends Job<BashScript> {

            ExtractGrammar(int mbMemory, Job<BashScript> joshdirJob, Job<BashScript> seedFileJob) throws IllegalArgumentException, IOException, UnsatisfiableArchitectureException {
                String joshdir = unsafeGetValue(joshdirJob).getOutputFile();
                String seedFile = unsafeGetValue(seedFileJob).getOutputFile();
                String scriptName = "extract_grammar__" + shorten(seedFile);
                String outputFile = e2e.outDir + File.separator + e2e.cli.tag + "__" + shorten(seedFile) + ".grammar";
                this.fulfillPromise(new ExtractGrammarFromJoshdir.Script(e2e.outDir, e2e.scriptName(scriptName), e2e.site, mbMemory, joshdir, seedFile, outputFile));
            }

            private int shorten(String fullSeedPath) throws IOException {
                return e2e.sf.shorten(fullSeedPath.equals(canonicalPath(e2e.cli.mertTrainingFile)) ? e2e.cli.mertTrainingFile : fullSeedPath.replaceFirst(e2e.outDir + "/", ""));
            }
        }
        ;
        Job<BashScript> developmentGrammarQ = e2e.checkStep(new ExtractGrammar(6000, joshdirQ, developmentQ));
        List<Job<BashScript>> testingGrammarQs = new LinkedList<Job<BashScript>>();
        for (Job<BashScript> testingCorpusQ : testingQs) {
            testingGrammarQs.add(e2e.checkStep(new ExtractGrammar(6000, joshdirQ, testingCorpusQ)));
        }
        e2e.setStep(6);
        Job<BashScript> mertConfigQ;
        {
            String mertConfigTemplate = canonicalPath(e2e.cli.mertConfigTemplate);
            mertConfigQ = e2e.checkStep(new Mert(e2e.outDir, e2e.scriptName("mert_train"), e2e.site, 7000, e2e.cli.fext, e2e.cli.eext, e2e.cli.lm, mertConfigTemplate, e2e.cli.mertInitialWeights, e2e.cli.mertNbestSize, e2e.outDir, developmentGrammarQ, developmentQ, referenceQs));
        }
        List<Job<BashScript>> testingSgmlQs = new LinkedList<Job<BashScript>>();
        {
            Pattern FEXT = Pattern.compile("\\." + e2e.cli.fext + "$");
            String EEXT = ".nbest." + e2e.cli.eext;
            for (int i = 0; i < testingQs.size(); ++i) {
                int testingS = e2e.sf.shorten(e2e.cli.testingFiles[i]);
                e2e.setStep(7);
                Job<BashScript> configQ = e2e.checkStep(new ChangeGrammarInConfig(e2e.outDir, e2e.scriptName("configCorrection__" + testingS), testingS, testingGrammarQs.get(i), mertConfigQ));
                Job<BashScript> nbestQ = e2e.checkStep(new JoshuaDecoder(e2e.outDir, e2e.scriptName("decoding__" + testingS), e2e.site, 7600, testingQs.get(i), configQ, FEXT, EEXT));
                e2e.setStep(8);
                Job<BashScript> mbrbestQ = e2e.checkStep(e2e.mbrReranking(testingS, mertConfigQ, nbestQ));
                e2e.setStep(9);
                Job<BashScript> onebestQ = e2e.checkStep(new ExtractTopCand(e2e.outDir, e2e.scriptName("get1best__" + testingS), e2e.site, 500, mbrbestQ));
                testingSgmlQs.add(e2e.checkStep(new Sgmlize(e2e.outDir, e2e.scriptName("sgmlize__" + testingS), e2e.site, 500, "JHU", false, canonicalPath(e2e.cli.testingFiles[i]), onebestQ, Pattern.compile("$"), ".sgm")));
            }
        }
        for (Job<BashScript> job : testingSgmlQs) {
            job.submit(e2e.qsub);
        }
        e2e.qsub.runMasterScript();
        System.exit(0);
    }
}
