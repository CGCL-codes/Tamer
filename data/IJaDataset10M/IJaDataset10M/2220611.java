package it.pronetics.madstore.crawler.impl.grid;

import it.pronetics.madstore.crawler.Pipeline;
import it.pronetics.madstore.crawler.model.Page;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridJob;
import org.gridgain.grid.GridJobAdapter;
import org.gridgain.grid.GridJobResult;
import org.gridgain.grid.GridTaskSplitAdapter;
import org.gridgain.grid.resources.GridSpringResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Grid task which splits pipeline-based processing of several pages into several distributed jobs,
 * each one processing a single page.
 *
 * @author Christian Mongillo
 * @author Sergio Bossa
 */
public class ProcessorTask extends GridTaskSplitAdapter<Collection<Page>, Collection<ProcessorTaskResult>> {

    private static final transient Logger LOG = LoggerFactory.getLogger(ProcessorTask.class);

    @GridSpringResource(resourceName = "pipeline")
    private transient Pipeline pipeline;

    public ProcessorTask(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    protected Collection<? extends GridJob> split(int gridSize, Collection<Page> pages) throws GridException {
        Collection<GridJob> jobs = new ArrayList<GridJob>(pages.size());
        for (final Page page : pages) {
            jobs.add(new GridJobAdapter() {

                public Serializable execute() throws GridException {
                    try {
                        Page processedPage = pipeline.execute(page);
                        if (processedPage != null) {
                            ProcessorTaskResult result = new ProcessorTaskResult(processedPage);
                            return result;
                        } else {
                            return null;
                        }
                    } catch (Exception ex) {
                        LOG.error(ex.getMessage(), ex);
                        return null;
                    }
                }
            });
        }
        return jobs;
    }

    public Collection<ProcessorTaskResult> reduce(List<GridJobResult> jobResults) throws GridException {
        Collection<ProcessorTaskResult> processorResults = new ArrayList<ProcessorTaskResult>(jobResults.size());
        for (GridJobResult jobResult : jobResults) {
            processorResults.add(jobResult.<ProcessorTaskResult>getData());
        }
        return processorResults;
    }
}
