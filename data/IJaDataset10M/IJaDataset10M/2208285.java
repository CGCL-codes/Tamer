package us.wthr.jdem846.render;

import us.wthr.jdem846.ModelContext;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;

public class RenderPipelineProcessContainer {

    private static Log log = Logging.getLog(RenderPipelineProcessContainer.class);

    private ModelContext modelContext;

    private RenderPipeline pipeline;

    private TileProcessPipe tileProcessPipe;

    private TriangleStripFillRenderPipe triangleStripFillRenderPipe;

    private ScanlinePathRenderPipe scanlinePathRenderingPipe;

    private ShapeFillPipe shapeFillPipe;

    private ThreadGroup pipelineThreadGroup;

    private Thread tileProcessThread;

    private Thread triangleStripFillRenderThread;

    private Thread scanlinePathRenderingThread;

    private Thread shapeFillThread;

    public RenderPipelineProcessContainer(ModelContext modelContext) {
        this(new RenderPipeline(modelContext), modelContext);
    }

    public RenderPipelineProcessContainer(RenderPipeline pipeline, ModelContext modelContext) {
        this.modelContext = modelContext;
        this.pipeline = pipeline;
        tileProcessPipe = new TileProcessPipe(pipeline, modelContext);
        triangleStripFillRenderPipe = new TriangleStripFillRenderPipe(pipeline, modelContext);
        scanlinePathRenderingPipe = new ScanlinePathRenderPipe(pipeline, modelContext);
        shapeFillPipe = new ShapeFillPipe(pipeline, modelContext);
    }

    public void start() {
        pipelineThreadGroup = new ThreadGroup(Thread.currentThread().getThreadGroup(), "pipeline.group." + modelContext.getContextId());
        tileProcessThread = new Thread(pipelineThreadGroup, "pipeline.pipe.tileProcess." + modelContext.getContextId()) {

            public void run() {
                tileProcessPipe.run();
            }
        };
        triangleStripFillRenderThread = new Thread(pipelineThreadGroup, "pipeline.pipe.triangleStripFillRender." + modelContext.getContextId()) {

            public void run() {
                triangleStripFillRenderPipe.run();
            }
        };
        scanlinePathRenderingThread = new Thread(pipelineThreadGroup, "pipeline.pipe.scanlinePathRender." + modelContext.getContextId()) {

            public void run() {
                scanlinePathRenderingPipe.run();
            }
        };
        shapeFillThread = new Thread(pipelineThreadGroup, "pipeline.pipe.shapeFill." + modelContext.getContextId()) {

            public void run() {
                shapeFillPipe.run();
            }
        };
        log.info("Starting Tile Process Thread...");
        tileProcessThread.start();
        log.info("Starting Canvas Fill Render Thread...");
        triangleStripFillRenderThread.start();
        log.info("Starting Shape Fill Thread...");
        shapeFillThread.start();
        log.info("Starting Scanline Path Rendering Thread...");
        scanlinePathRenderingThread.start();
    }

    public boolean areQueuesEmpty() {
        return (!pipeline.hasMoreTriangleStripFills() && !pipeline.hasMoreScanlinePaths() && !pipeline.hasMoreTileRenderRunnables() && !pipeline.hasMoreShapeFills());
    }

    public boolean areAllCompleted() {
        return (isTileProcessPipeCompleted() && isTriangleStripRenderPipeCompleted() && isShapeFillPipeCompleted() && isScanlinePathRenderingPipeCompleted());
    }

    public void pause() {
        tileProcessPipe.pause();
        triangleStripFillRenderPipe.pause();
        scanlinePathRenderingPipe.pause();
        shapeFillPipe.pause();
    }

    public boolean isTileProcessPipeCompleted() {
        return tileProcessPipe.isCompleted();
    }

    public void cancelTileProcessPipe() {
        tileProcessPipe.cancel();
    }

    public boolean isTriangleStripRenderPipeCompleted() {
        return triangleStripFillRenderPipe.isCompleted();
    }

    public void cancelTriangleStripRenderPipe() {
        triangleStripFillRenderPipe.cancel();
    }

    public boolean isScanlinePathRenderingPipeCompleted() {
        return scanlinePathRenderingPipe.isCompleted();
    }

    public void cancelScanlinePathRenderingPipe() {
        scanlinePathRenderingPipe.cancel();
    }

    public boolean isShapeFillPipeCompleted() {
        return shapeFillPipe.isCompleted();
    }

    public void cancelShapeFillPipe() {
        shapeFillPipe.cancel();
    }

    public void stop(boolean block) {
        cancelTileProcessPipe();
        cancelTriangleStripRenderPipe();
        cancelScanlinePathRenderingPipe();
        cancelShapeFillPipe();
        if (block) {
            while (!areAllCompleted()) {
            }
        }
    }

    public RenderPipeline getRenderPipeline() {
        return pipeline;
    }
}
