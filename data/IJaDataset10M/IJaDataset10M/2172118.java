package de.mpg.mpiz.koeln.anna.step.getresults;

import java.io.File;
import java.io.IOException;
import de.bioutils.gff3.element.GFF3ElementGroup;
import de.bioutils.gff3.element.GFF3ElementGroupImpl;
import de.bioutils.gff3.file.GFF3FileImpl;
import de.kerner.commons.file.FileUtils;
import de.mpg.mpiz.koeln.anna.abstractstep.gff3.AbstractGFF3AnnaStep;
import de.mpg.mpiz.koeln.anna.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.anna.data.gff3.GFF3DataBean;
import de.mpg.mpiz.koeln.anna.server.data.DataProxy;
import de.mpg.mpiz.koeln.anna.step.StepExecutionException;

public class GetResults extends AbstractGFF3AnnaStep {

    private static final String OUT_DIR_KEY = "anna.step.getResults.outDir";

    private static final String OUT_FILE_NAME_KEY = "anna.step.getResults.fileName";

    public boolean run(DataProxy<GFF3DataBean> proxy) throws Throwable {
        boolean success = false;
        final File outDir = new File(super.getStepProperties().getProperty(OUT_DIR_KEY));
        logger.debug("got outdir=" + outDir);
        success = FileUtils.dirCheck(outDir, true);
        if (success) writeFile(outDir, proxy);
        return success;
    }

    private void writeFile(File outDir, DataProxy<GFF3DataBean> proxy) throws DataBeanAccessException, IOException {
        logger.debug("reading GFF for predicted genes");
        final GFF3ElementGroup predicted = proxy.viewData().getPredictedGenesGFF();
        logger.debug("reading GFF for predicted genes done (elements=" + predicted.getSize() + ")");
        logger.debug("reading GFF for repetetive elements");
        final GFF3ElementGroup repeat = proxy.viewData().getRepeatMaskerGFF();
        logger.debug("reading GFF for repetetive elements done (elements=" + repeat.getSize() + ")");
        logger.debug("reading GFF for mapped ests");
        final GFF3ElementGroup ests = proxy.viewData().getMappedESTs();
        logger.debug("reading GFF for mapped ests done (elements=" + ests.getSize() + ")");
        logger.debug("merging");
        final GFF3ElementGroup merged = new GFF3ElementGroupImpl(true);
        merged.addAll(predicted);
        merged.addAll(repeat);
        merged.addAll(ests);
        logger.debug("merging done (elements=" + merged.getSize() + ")");
        if (merged.getSize() == 0) {
            logger.info("nothing to write");
            return;
        }
        final File outFile = new File(outDir, super.getStepProperties().getProperty(OUT_FILE_NAME_KEY));
        logger.info("writing results to " + outFile);
        new GFF3FileImpl(merged).write(outFile);
        logger.debug("done writing results to " + outFile);
    }

    @Override
    public boolean canBeSkipped(DataProxy<GFF3DataBean> proxy) throws StepExecutionException {
        return false;
    }

    @Override
    public boolean requirementsSatisfied(DataProxy<GFF3DataBean> proxy) throws StepExecutionException {
        return true;
    }

    public boolean isCyclic() {
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
