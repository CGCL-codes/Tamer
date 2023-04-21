package com.koutra.dist.proc.pipeline;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.Logger;
import com.koutra.dist.proc.model.ContentType;
import com.koutra.dist.proc.model.IDemuxFaucet;
import com.koutra.dist.proc.model.IFaucet;
import com.koutra.dist.proc.model.IFaucetTemplate;
import com.koutra.dist.proc.model.IMuxSink;
import com.koutra.dist.proc.model.IPipelineItem;
import com.koutra.dist.proc.model.ISimplePipelineItem;
import com.koutra.dist.proc.model.ISink;
import com.koutra.dist.proc.model.ISinkTemplate;
import com.koutra.dist.proc.model.ISplitPipelineItem;
import com.koutra.dist.proc.model.XformationException;
import com.koutra.dist.proc.util.ExecutorsHelper;

public abstract class ReaderDemuxPipelineItem implements ISimplePipelineItem, IDemuxFaucet {

    private static final Logger logger = Logger.getLogger(ReaderDemuxPipelineItem.class);

    protected String id;

    protected boolean hookedUpFaucet;

    protected boolean hookedUpSink;

    protected ISinkTemplate pipelineTemplate;

    protected IFaucet faucet;

    protected AtomicReference<ISink> sink;

    protected Reader reader;

    protected PipedReader readerForFaucet;

    protected List<PipedReader> readerListForDisposal;

    protected PipedWriter writer;

    /**
	 * @deprecated Use any of the initializing constructors instead.
	 */
    public ReaderDemuxPipelineItem() {
    }

    public ReaderDemuxPipelineItem(String id, ISinkTemplate pipelineTemplate) {
        if (!pipelineTemplate.supportsInput(ContentType.CharStream)) throw new IllegalArgumentException("SinkTemplate '" + pipelineTemplate.getId() + "' must support the CharStream content type.");
        this.id = id;
        this.hookedUpFaucet = false;
        this.hookedUpSink = false;
        this.pipelineTemplate = pipelineTemplate;
        this.faucet = null;
        this.sink = new AtomicReference<ISink>(null);
        this.reader = null;
        this.readerForFaucet = null;
        this.readerListForDisposal = new ArrayList<PipedReader>();
        this.writer = null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ISink getSink() {
        return sink.get();
    }

    @Override
    public IFaucet getFaucet() {
        return faucet;
    }

    @Override
    public boolean supportsInput(ContentType contentType) {
        switch(contentType) {
            case CharStream:
                return true;
            case ByteStream:
            case XML:
            case ResultSet:
            default:
                return false;
        }
    }

    @Override
    public boolean supportsOutput(ContentType contentType) {
        switch(contentType) {
            case CharStream:
                return true;
            case ByteStream:
            case XML:
            case ResultSet:
            default:
                return false;
        }
    }

    protected void checkFaucetValidity(IFaucet faucet) {
        if (!faucet.supportsOutput(ContentType.CharStream)) throw new IllegalArgumentException("Faucet '" + faucet.getId() + "' must support the CharStream content type.");
        if (faucet instanceof IDemuxFaucet) throw new IllegalArgumentException("Faucet '" + faucet.getId() + "' is an IDemuxFaucet instance. This is not allowed.");
        if (faucet instanceof IFaucetTemplate) throw new IllegalArgumentException("Faucet '" + faucet.getId() + "' is an IFaucetTemplate instance. This is not allowed.");
    }

    @Override
    public void hookupFaucet(IFaucet faucet) {
        checkFaucetValidity(faucet);
        if (hookedUpFaucet && this.faucet == faucet) {
            return;
        }
        if (hookedUpFaucet) throw new XformationException("Trying to hook up an already hooked up sink.");
        this.faucet = faucet;
        this.hookedUpFaucet = true;
        this.faucet.hookupSink(this);
    }

    protected void checkSinkValidity(ISink sink) {
        if (!sink.supportsInput(ContentType.CharStream)) throw new IllegalArgumentException("Sink '" + sink.getId() + "' must support the CharStream content type.");
        if (!(sink instanceof IMuxSink) && !(sink instanceof ISinkTemplate)) throw new IllegalArgumentException("Sink '" + sink.getId() + "' is neither an IMuxSink or an ISinkTemplate instance. " + "This is not allowed.");
    }

    @Override
    public void hookupSink(ISink sink) {
        checkSinkValidity(sink);
        if (hookedUpSink && this.sink.get() == sink) {
            return;
        }
        this.sink.set(sink);
        this.hookedUpSink = true;
        this.sink.get().hookupFaucet(this);
    }

    @Override
    public Object getSource(ContentType contentType) {
        switch(contentType) {
            case ByteStream:
            case XML:
            case ResultSet:
                throw new XformationException("Content type: " + contentType + " is not supported.");
        }
        if (readerForFaucet != null) return readerForFaucet;
        this.writer = new PipedWriter();
        try {
            this.readerForFaucet = new PipedReader(this.writer);
        } catch (IOException e) {
            throw new XformationException("Unable to create hooked piped reader/writer", e);
        }
        this.readerListForDisposal.add(this.readerForFaucet);
        if (logger.isTraceEnabled()) logger.trace("Created reader " + readerForFaucet + " and writer " + writer);
        return readerForFaucet;
    }

    @Override
    public void registerSource(Object source) {
    }

    @Override
    public void handleMuxSwitch() {
    }

    @Override
    public void endMux() {
    }

    @Override
    public void dispose() {
        ISink currentSink = sink.get();
        if (currentSink == null) return;
        if (currentSink instanceof ISinkTemplate) if (((ISinkTemplate) currentSink).isClone()) return;
        try {
            logger.debug("Disposing reader list now!!!");
            for (PipedReader reader : readerListForDisposal) {
                reader.close();
            }
        } catch (IOException e) {
            throw new XformationException("Unable to close reader", e);
        }
        faucet.dispose();
    }

    protected void createPipeline() {
        pipelineTemplate.createClone(this);
    }

    @Override
    public String dumpPipeline() {
        return sink.get().dumpPipeline() + "\n" + getClass().getName() + ": " + reader + "->" + writer + "->" + readerForFaucet;
    }

    protected void switchPipeline() {
        sink.set(null);
        readerForFaucet = null;
        writer = null;
        hookedUpSink = false;
        createPipeline();
        getSource(ContentType.CharStream);
        if (sink.get() instanceof IPipelineItem) ((IPipelineItem) sink.get()).handleMuxSwitch();
        if (logger.isDebugEnabled()) logger.debug("Dumping pipeline:\n" + dumpPipeline());
    }

    /**
	 * This method is a callback that performs the transformation. The returned char
	 * array will get written on the writer that hooks this pipeline item to its
	 * sink down the pipeline chain.
	 * 
	 * @param buffer The buffer to read chars from. Only the chars in the specified
	 * range should be used.
	 * @param off The offset of the first char in the buffer that is part of the range
	 * to use.
	 * @param len The number of chars of the valid range, or null if the end of the
	 * input has been reached.
	 * @return The char array to pass down the pipeline chain.
	 */
    public abstract char[] transformBuffer(char[] buffer, int off, int len);

    /**
	 * This method should return true, if the rest of the pipeline should "break"
	 * at this point in the transformation.
	 * 
	 * @return True iff the transformation pipeline should "break" at this point.
	 */
    public abstract boolean shouldChangePipeline();

    @Override
    public void consume() {
        throw new XformationException("Calling ISink.consume() on a IPipelineItem " + "implementation. Call IPipelineItem.consume(ISink) instead.");
    }

    @Override
    public Future<?> consumeAsynchronously() {
        throw new XformationException("Calling ISink.consumeAsynchronously() on a IPipelineItem " + "implementation. Call IPipelineItem.consume(ISink) instead.");
    }

    @Override
    public Object consume(ISink previousSink) {
        if (!hookedUpFaucet) throw new XformationException("Pipeline item has not been set up correctly:" + "faucet has not been set");
        if (previousSink instanceof ISinkTemplate) {
            if (((ISinkTemplate) previousSink).isClone()) return null;
        }
        if (faucet instanceof IPipelineItem) ((IPipelineItem) faucet).consume(this);
        createPipeline();
        reader = (Reader) faucet.getSource(ContentType.CharStream);
        getSource(ContentType.CharStream);
        if (sink.get() instanceof IPipelineItem) ((IPipelineItem) sink.get()).handleMuxSwitch();
        final ISink fPreviousSink = previousSink;
        ExecutorsHelper.getInstance().executeInProc(new Runnable() {

            @Override
            public void run() {
                try {
                    int count;
                    char[] buffer = new char[8 * 1024];
                    while ((count = reader.read(buffer)) != -1) {
                        char[] transformation = transformBuffer(buffer, 0, count);
                        if (logger.isTraceEnabled()) logger.trace("Read '" + new String(buffer, 0, count) + "' from " + reader + " writing '" + new String(transformation) + "' to writer " + writer);
                        writer.write(transformation, 0, transformation.length);
                        while (shouldChangePipeline()) {
                            writer.close();
                            if (logger.isTraceEnabled()) logger.trace("About to switch pipeline");
                            switchPipeline();
                            transformation = transformBuffer(buffer, 0, 0);
                            if (logger.isTraceEnabled()) logger.trace("Writing  additional '" + new String(transformation) + "' to writer " + writer);
                            writer.write(transformation, 0, transformation.length);
                        }
                    }
                    char[] transformation = transformBuffer(buffer, 0, count);
                    if (logger.isTraceEnabled()) logger.trace("Writing  final '" + new String(transformation) + "' to writer " + writer);
                    writer.write(transformation, 0, transformation.length);
                    writer.close();
                    if (fPreviousSink instanceof IPipelineItem) {
                        IPipelineItem pipelineSink = (IPipelineItem) fPreviousSink;
                        pipelineSink.endMux();
                    }
                    logger.debug("Executable for the reader pipeline will now exit.");
                } catch (Throwable t) {
                    logger.error("Error during pipeline thread execution.", t);
                }
            }
        });
        return null;
    }

    @Override
    public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {
        this.id = in.readUTF();
        this.hookedUpFaucet = false;
        this.hookedUpSink = false;
        this.sink = new AtomicReference<ISink>(null);
        this.faucet = null;
        this.reader = null;
        this.readerForFaucet = null;
        this.readerListForDisposal = new ArrayList<PipedReader>();
        this.writer = null;
        String sinkClassName = in.readUTF();
        Class<?> sinkClass;
        try {
            sinkClass = Class.forName(sinkClassName);
        } catch (ClassNotFoundException e) {
            throw new InstantiationException("Unable to load class: " + sinkClassName);
        }
        this.pipelineTemplate = (ISinkTemplate) sinkClass.newInstance();
        this.pipelineTemplate.readFrom(in);
        this.pipelineTemplate.hookupFaucet(this);
    }

    @Override
    public void writeTo(DataOutputStream out) throws IOException {
        out.writeUTF(id);
        out.writeUTF(pipelineTemplate.getClass().getCanonicalName());
        pipelineTemplate.writeTo(out);
    }

    @Override
    public List<ISink> getTerminalSinks() {
        if (pipelineTemplate instanceof IFaucet) return ((IFaucet) pipelineTemplate).getTerminalSinks(); else if (pipelineTemplate instanceof ISplitPipelineItem) return ((ISplitPipelineItem) pipelineTemplate).getTerminalSinks(); else {
            List<ISink> retVal = new ArrayList<ISink>();
            retVal.add(pipelineTemplate);
            return retVal;
        }
    }
}
