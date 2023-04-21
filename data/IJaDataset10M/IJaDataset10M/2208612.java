package org.spantus.android.audio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.spantus.android.dto.ExtractorReaderCtx;
import org.spantus.android.dto.SpantusAudioCtx;
import org.spantus.android.service.AndroidExtractorsFactory;
import org.spantus.android.service.ReaderSimpleJsonDao;
import org.spantus.android.service.SignalSegmentAndroidJsonDao;
import org.spantus.core.FrameValues;
import org.spantus.core.FrameVectorValues;
import org.spantus.core.IValues;
import org.spantus.core.beans.FrameValuesHolder;
import org.spantus.core.beans.FrameVectorValuesHolder;
import org.spantus.core.beans.SignalSegment;
import org.spantus.core.dao.SignalSegmentDao;
import org.spantus.core.extractor.DefaultExtractorConfig;
import org.spantus.core.extractor.ExtractorParam;
import org.spantus.core.extractor.IExtractorConfig;
import org.spantus.core.extractor.IExtractorInputReader;
import org.spantus.core.extractor.dao.ReaderDao;
import org.spantus.core.io.BaseWraperExtractorReader;
import org.spantus.core.io.WraperExtractorReader;
import org.spantus.core.marker.Marker;
import org.spantus.core.marker.MarkerSet;
import org.spantus.core.marker.MarkerSetHolder;
import org.spantus.core.marker.MarkerSetHolder.MarkerSetHolderEnum;
import org.spantus.core.service.ExtractorInputReaderService;
import org.spantus.core.service.impl.ExtractorInputReaderServiceImpl;
import org.spantus.core.threshold.IClassifier;
import org.spantus.exception.ProcessingException;
import org.spantus.extractor.impl.ExtractorEnum;
import org.spantus.logger.Logger;
import org.spantus.segment.ISegmentatorService;
import org.spantus.segment.SegmentFactory;
import org.spantus.segment.SegmentFactory.SegmentatorServiceEnum;
import org.spantus.segment.SegmentatorParam;
import org.spantus.segment.online.OnlineDecisionSegmentatorParam;
import android.media.AudioRecord;

public class RecordServiceReader extends RecordServiceUrl {

    private static final Logger LOG = Logger.getLogger(RecordServiceReader.class);

    private ReaderDao readerDao;

    private SignalSegmentDao segmentDao;

    private ISegmentatorService segmentator;

    private ExtractorInputReaderService extractorInputReaderService;

    public ExtractorReaderCtx createReader(SpantusAudioCtx ctx) {
        IExtractorConfig extractorConfig = new DefaultExtractorConfig();
        Map<String, ExtractorParam> params = new HashMap<String, ExtractorParam>();
        ExtractorReaderCtx readerCtx = AndroidExtractorsFactory.createReader(extractorConfig, params, ExtractorEnum.ENERGY_EXTRACTOR, ExtractorEnum.WAVFORM_EXTRACTOR);
        return readerCtx;
    }

    /**
	 * 
	 * @param ctx
	 */
    public void recordToReader(SpantusAudioCtx ctx, WraperExtractorReader wrappedReader) {
        beforeRecord(ctx);
        RecordFormat recordFormat = newRecordFormat(ctx);
        wrappedReader.setSampleSizeInBits(ctx.getSampleSizeInBits());
        ctx.setLastFileName("tmp.json");
        try {
            AudioRecord audioRecord = newAudioRecord(recordFormat);
            byte[] buffer = new byte[recordFormat.getBufferSize()];
            audioRecord.startRecording();
            while (RecordState.RECORD.equals(ctx.getRecordState())) {
                int bufferReadResult = audioRecord.read(buffer, 0, recordFormat.getBufferSize());
                if (bufferReadResult < 0) {
                    throw new IllegalArgumentException("Nothing to read");
                }
                putValues(buffer, wrappedReader);
                if (wrappedReader.getSample() > ctx.getMaxLengthInSamples()) {
                    LOG.debug("[recordToFile]time out: {0} >{1}", wrappedReader.getSample(), ctx.getMaxLengthInSamples());
                    break;
                }
                if (isCancel()) {
                    LOG.debug("[recordToFile]canceled");
                    break;
                }
            }
            flush(wrappedReader);
            audioRecord.stop();
            audioRecord.release();
            sendFullSignal(wrappedReader.getReader(), ctx);
            ctx.setRecordState(RecordState.STOP);
        } catch (Exception ex) {
            LOG.error(ex);
        }
        afterRecord(ctx);
    }

    /**
	 * 
	 * @param iExtractorInputReader
	 * @param ctx
	 * @return
	 */
    public URL sendFullSignal(IExtractorInputReader iExtractorInputReader, SpantusAudioCtx ctx) {
        LOG.debug("[sendFullSignal] config {0}", iExtractorInputReader.getConfig());
        File file = getAudioCtxService().recreateFile(ctx, "spnt.json");
        getReaderDao().write(iExtractorInputReader, file);
        URL url = null;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            LOG.error(e);
        }
        return url;
    }

    public URL sendSegment(SignalSegment segment, SpantusAudioCtx ctx) throws IOException {
        HttpURLConnection conn = getTrainUrl(ctx);
        OutputStream outputStream = conn.getOutputStream();
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        getSegmentDao().write(segment, byteOutputStream);
        conn.setFixedLengthStreamingMode(byteOutputStream.size());
        outputStream.write(byteOutputStream.toByteArray());
        return conn.getURL();
    }

    /**
	 * 
	 * @param readerCtx
	 * @return
	 */
    public List<SignalSegment> extractSegments(ExtractorReaderCtx readerCtx) {
        List<SignalSegment> segments = new LinkedList<SignalSegment>();
        Set<IClassifier> classifiers = getExtractorInputReaderService().extractClassifiers(readerCtx.getReader());
        MarkerSetHolder markerSetHolder = getSegmentator().extractSegments(classifiers, createSegmentatorParam());
        MarkerSet markerSet = markerSetHolder.getMarkerSets().get(MarkerSetHolderEnum.word.name());
        if (markerSet == null) {
            markerSet = markerSetHolder.getMarkerSets().get(MarkerSetHolderEnum.phone.name());
        }
        if (markerSet == null) {
            throw new ProcessingException("no segment layer was found");
        }
        List<Marker> markers = markerSet.getMarkers();
        for (Marker marker : markers) {
            LOG.debug("[extractSegments]marker {0}", marker);
            Map<String, IValues> featureData = getExtractorInputReaderService().findAllVectorValuesForMarker(readerCtx.getReader(), marker);
            SignalSegment segment = createSignalSegment(marker, featureData);
            segments.add(segment);
        }
        return segments;
    }

    public SignalSegment createSignalSegment(Marker marker, Map<String, IValues> featureData) {
        SignalSegment segment = new SignalSegment();
        segment.setMarker(marker);
        for (Entry<String, IValues> entry : featureData.entrySet()) {
            if (entry.getValue() instanceof FrameValues) {
                segment.getFeatureFrameValuesMap().put(entry.getKey(), new FrameValuesHolder((FrameValues) entry.getValue()));
            } else if (entry.getValue() instanceof FrameVectorValues) {
                segment.getFeatureFrameVectorValuesMap().put(entry.getKey(), new FrameVectorValuesHolder((FrameVectorValues) entry.getValue()));
            } else {
                throw new IllegalArgumentException("Not implemented");
            }
        }
        return segment;
    }

    private HttpURLConnection getTrainUrl(SpantusAudioCtx ctx) {
        try {
            URL url = ctx.getTrainUrl();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            return conn;
        } catch (MalformedURLException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        }
        return null;
    }

    public void flush(BaseWraperExtractorReader wrappedReader) {
        wrappedReader.pushValues();
    }

    /**
	 * 
	 * @param config
	 * @return
	 */
    public SegmentatorParam createSegmentatorParam() {
        OnlineDecisionSegmentatorParam param = new OnlineDecisionSegmentatorParam();
        param.setMinLength(91L);
        param.setMinSpace(61L);
        param.setExpandEnd(61L);
        param.setExpandStart(61L);
        return param;
    }

    /**
	 * 
	 * @param buffer
	 * @param wrappedReader
	 */
    public void putValues(byte[] buffer, BaseWraperExtractorReader wrappedReader) {
        for (Byte value : buffer) {
            wrappedReader.put(value);
        }
    }

    public ReaderDao getReaderDao() {
        if (readerDao == null) {
            readerDao = new ReaderSimpleJsonDao();
        }
        return readerDao;
    }

    public void setReaderDao(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }

    public ExtractorInputReaderService getExtractorInputReaderService() {
        if (extractorInputReaderService == null) {
            extractorInputReaderService = new ExtractorInputReaderServiceImpl();
        }
        return extractorInputReaderService;
    }

    public void setExtractorInputReaderService(ExtractorInputReaderService extractorInputReaderService) {
        this.extractorInputReaderService = extractorInputReaderService;
    }

    public SignalSegmentDao getSegmentDao() {
        if (segmentDao == null) {
            segmentDao = new SignalSegmentAndroidJsonDao();
        }
        return segmentDao;
    }

    public void setSegmentDao(SignalSegmentDao segmentDao) {
        this.segmentDao = segmentDao;
    }

    public ISegmentatorService getSegmentator() {
        if (segmentator == null) {
            segmentator = SegmentFactory.createSegmentator(SegmentatorServiceEnum.online.name());
        }
        return segmentator;
    }

    public void setSegmentator(ISegmentatorService segmentator) {
        this.segmentator = segmentator;
    }

    public void readFile(URL inputFile, RecordServiceReader aRecordService, BaseWraperExtractorReader wrappedReader) throws IOException {
        int mFrameBytes;
        int mSampleRate;
        int mChannels;
        InputStream stream = inputFile.openStream();
        byte[] header = new byte[12];
        stream.read(header, 0, 12);
        if (header[0] != 'R' || header[1] != 'I' || header[2] != 'F' || header[3] != 'F' || header[8] != 'W' || header[9] != 'A' || header[10] != 'V' || header[11] != 'E') {
            throw new java.io.IOException("Not a WAV file");
        }
        mChannels = 0;
        mSampleRate = 0;
        while (true) {
            byte[] chunkHeader = new byte[8];
            int read = stream.read(chunkHeader, 0, 8);
            if (read < 0) {
                break;
            }
            int chunkLen = ((0xff & chunkHeader[7]) << 24) | ((0xff & chunkHeader[6]) << 16) | ((0xff & chunkHeader[5]) << 8) | ((0xff & chunkHeader[4]));
            if (chunkHeader[0] == 'f' && chunkHeader[1] == 'm' && chunkHeader[2] == 't' && chunkHeader[3] == ' ') {
                if (chunkLen < 16 || chunkLen > 1024) {
                    throw new java.io.IOException("WAV file has bad fmt chunk");
                }
                byte[] fmt = new byte[chunkLen];
                stream.read(fmt, 0, chunkLen);
                int format = ((0xff & fmt[1]) << 8) | ((0xff & fmt[0]));
                mChannels = ((0xff & fmt[3]) << 8) | ((0xff & fmt[2]));
                mSampleRate = ((0xff & fmt[7]) << 24) | ((0xff & fmt[6]) << 16) | ((0xff & fmt[5]) << 8) | ((0xff & fmt[4]));
                if (format != 1) {
                    throw new java.io.IOException("Unsupported WAV file encoding");
                }
            } else if (chunkHeader[0] == 'd' && chunkHeader[1] == 'a' && chunkHeader[2] == 't' && chunkHeader[3] == 'a') {
                if (mChannels == 0 || mSampleRate == 0) {
                    throw new java.io.IOException("Bad WAV file: data chunk before fmt chunk");
                }
                int frameSamples = (mSampleRate * mChannels) / 50;
                mFrameBytes = frameSamples * 2;
                byte[] buffer = new byte[mFrameBytes];
                int i = 0;
                while (i < chunkLen) {
                    int oneFrameBytes = mFrameBytes;
                    if (i + oneFrameBytes > chunkLen) {
                        i = chunkLen - oneFrameBytes;
                    }
                    stream.read(buffer, 0, oneFrameBytes);
                    aRecordService.putValues(buffer, wrappedReader);
                    i += oneFrameBytes;
                }
            } else {
                stream.skip(chunkLen);
            }
        }
        aRecordService.flush(wrappedReader);
    }
}
