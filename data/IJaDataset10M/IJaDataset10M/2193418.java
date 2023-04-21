package com.ingenico.tools.data.sampling;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.logging.Logger;
import com.ingenico.tools.nio.SampleBuffer;
import com.ingenico.tools.nio.channel.RandomReadableSampleChannel;

public abstract class AbstractDiscretePathSampler implements PathSampler {

    private static final Logger _logger = Logger.getLogger(AbstractDiscretePathSampler.class.getCanonicalName());

    private SampleBuffer samplesBuffer = null;

    protected abstract double getDoubleValue(SampleBuffer buffer);

    protected Path2D subsampleToPath(final RandomReadableSampleChannel samplesChannel, double targetScale, int targetLength, long sampleOffset) throws IOException {
        final Path2D path = new Path2D.Double();
        final double virtualSamplesWindowLength = 1 / targetScale;
        double virtualX;
        long realX;
        double y;
        int requiredSampleWindowLength = (int) virtualSamplesWindowLength;
        int availableSamples;
        if ((samplesBuffer == null) || (samplesBuffer.capacity() < requiredSampleWindowLength)) {
            samplesBuffer = samplesChannel.allocate(requiredSampleWindowLength);
            _logger.fine("SamplesBuffer capacity set to " + requiredSampleWindowLength + " samples");
        }
        virtualX = sampleOffset;
        realX = sampleOffset;
        samplesChannel.position(realX);
        path.reset();
        _logger.fine("realX = " + realX + ", virtualX = " + virtualX);
        _logger.fine("requiredSampleWindowLength = " + requiredSampleWindowLength + ", virtualSamplesWindowLength = " + virtualSamplesWindowLength);
        samplesBuffer.clear();
        samplesBuffer.limit(requiredSampleWindowLength);
        availableSamples = samplesChannel.read(samplesBuffer);
        _logger.fine("read " + availableSamples + " samples");
        samplesBuffer.position(0);
        if (availableSamples > 0) {
            samplesBuffer.limit(availableSamples);
            y = getDoubleValue(samplesBuffer);
            path.moveTo(realX, y);
        } else {
            return path;
        }
        for (int i = 1; i < targetLength; i++) {
            virtualX += virtualSamplesWindowLength;
            realX += availableSamples;
            requiredSampleWindowLength = (int) (virtualX - realX) + (int) virtualSamplesWindowLength;
            if (samplesBuffer.capacity() < requiredSampleWindowLength) {
                samplesBuffer = samplesChannel.allocate(requiredSampleWindowLength);
            }
            samplesBuffer.clear();
            samplesBuffer.limit(requiredSampleWindowLength);
            samplesChannel.position(realX);
            availableSamples = samplesChannel.read(samplesBuffer);
            samplesBuffer.position(0);
            if (availableSamples > 0) {
                samplesBuffer.limit(availableSamples);
                y = getDoubleValue(samplesBuffer);
                path.lineTo(realX, y);
            } else {
                return path;
            }
        }
        return path;
    }

    protected Path2D supersampleToPath(final RandomReadableSampleChannel samplesChannel, double targetScale, int targetLength, long sampleOffset) throws IOException {
        final Path2D path = new Path2D.Double();
        int realLength = (int) ((double) targetLength / targetScale);
        long realX;
        double y;
        final int requiredSampleWindowLength = 1;
        int availableSamples;
        if ((samplesBuffer == null) || (samplesBuffer.capacity() < requiredSampleWindowLength)) {
            samplesBuffer = samplesChannel.allocate(requiredSampleWindowLength);
            _logger.fine("SamplesBuffer capacity set to " + requiredSampleWindowLength + " samples");
        }
        realX = sampleOffset;
        samplesChannel.position(realX);
        path.reset();
        samplesBuffer.clear();
        samplesBuffer.limit(requiredSampleWindowLength);
        availableSamples = samplesChannel.read(samplesBuffer);
        _logger.fine("read " + availableSamples + " samples");
        samplesBuffer.position(0);
        if (availableSamples > 0) {
            samplesBuffer.limit(availableSamples);
            y = getDoubleValue(samplesBuffer);
            path.moveTo(realX, y);
        } else {
            return path;
        }
        for (int i = 1; i < realLength; i++) {
            realX += availableSamples;
            samplesBuffer.clear();
            samplesBuffer.limit(requiredSampleWindowLength);
            samplesChannel.position(realX);
            availableSamples = samplesChannel.read(samplesBuffer);
            samplesBuffer.position(0);
            if (availableSamples > 0) {
                samplesBuffer.limit(availableSamples);
                y = getDoubleValue(samplesBuffer);
                path.lineTo(realX, y);
            } else {
                return path;
            }
        }
        return path;
    }

    @Override
    public Path2D sampleToPath(final RandomReadableSampleChannel samplesChannel, double targetScale, int targetLength, long sampleOffset) throws IOException {
        final long samplesChannelSize = samplesChannel.size();
        _logger.fine("targetLength = " + targetLength);
        if ((targetLength == 0) || (samplesChannelSize <= sampleOffset)) {
            return null;
        }
        if (targetLength < 1) {
            return null;
        }
        if (targetScale < 1) {
            return subsampleToPath(samplesChannel, targetScale, targetLength, sampleOffset);
        } else {
            return supersampleToPath(samplesChannel, targetScale, targetLength, sampleOffset);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
