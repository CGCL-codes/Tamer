package org.vous.facelib.sources;

import java.io.IOException;
import javax.media.CaptureDeviceInfo;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoDataSourceException;
import javax.media.NoProcessorException;
import javax.media.NotRealizedError;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.control.FormatControl;
import javax.media.control.FrameRateControl;
import javax.media.format.RGBFormat;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;

public class CameraSource extends AbstractFrameSource implements ControllerListener {

    private CaptureDeviceInfo mDevInfo;

    private RGBFormat mFormat;

    private DataSource mDataSource = null;

    private Object mRealizedLock = null;

    private Processor mProcessor = null;

    private PushBufferDataSource mPushSource = null;

    private PushBufferStream mStream = null;

    public CameraSource(CaptureDeviceInfo devInfo, RGBFormat format, int fps) {
        super(fps);
        mDevInfo = devInfo;
        mFormat = format;
        mRealizedLock = new Object();
    }

    public CaptureDeviceInfo getCaptureDeviceInfo() {
        return mDevInfo;
    }

    public RGBFormat getFormat() {
        return mFormat;
    }

    protected DataSource getDataSource() {
        return mDataSource;
    }

    protected Processor getProcessor() {
        return mProcessor;
    }

    protected PushBufferDataSource getPushSource() {
        return mPushSource;
    }

    protected PushBufferStream getPushStream() {
        return mStream;
    }

    public static DataSource getConnectedDataSource(CaptureDeviceInfo devInfo) throws Exception {
        MediaLocator locator = devInfo.getLocator();
        if (locator == null) throw new Exception("Unable to get a MediaLocator from device");
        DataSource source = Manager.createDataSource(locator);
        source.connect();
        return source;
    }

    public CameraSourceReader connect() throws Exception {
        try {
            mDataSource = CameraSource.getConnectedDataSource(mDevInfo);
        } catch (NoDataSourceException nde) {
            throw new Exception(nde);
        } catch (IOException ioe) {
            throw new Exception(ioe);
        } catch (Exception e) {
            throw new Exception(e);
        }
        FormatControl[] controls = ((CaptureDevice) mDataSource).getFormatControls();
        Format setFormat = null;
        for (FormatControl control : controls) {
            if (control == null) continue;
            if ((setFormat = control.setFormat(mFormat)) != null) break;
        }
        if (setFormat == null) throw new Exception("Unable to set selected format");
        try {
            mProcessor = Manager.createProcessor(mDataSource);
        } catch (NoProcessorException npe) {
            throw new Exception("Unable to get JMF processor");
        } catch (IOException ioe) {
            throw new Exception("I/O exception, unable to get JMF processor");
        }
        FrameRateControl control = (FrameRateControl) mProcessor.getControl("javax.media.control.FrameRateControl");
        if (control != null) {
            control.setFrameRate(getFps());
        }
        mProcessor.addControllerListener(this);
        mProcessor.realize();
        try {
            while (mProcessor.getState() != Processor.Realized) {
                synchronized (mRealizedLock) {
                    mRealizedLock.wait();
                }
            }
        } catch (InterruptedException ie) {
            throw new Exception("Failed to realize device");
        }
        mProcessor.start();
        try {
            mPushSource = (PushBufferDataSource) mProcessor.getDataOutput();
        } catch (NotRealizedError nre) {
        }
        PushBufferStream[] streams = mPushSource.getStreams();
        for (PushBufferStream stream : streams) {
            if (stream.getFormat() instanceof RGBFormat) {
                mStream = stream;
                break;
            }
        }
        if (mStream == null) throw new Exception("Unable to get an RGB stream from device");
        return new CameraSourceReader(this);
    }

    @Override
    public void controllerUpdate(ControllerEvent event) {
        if (event instanceof RealizeCompleteEvent) {
            synchronized (mRealizedLock) {
                mRealizedLock.notify();
            }
        }
    }
}
