package br.ufal.tci.nexos.arcolive.beans;

import java.io.IOException;
import java.io.Serializable;
import javax.media.CannotRealizeException;
import javax.media.DataSink;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoDataSinkException;
import javax.media.NoProcessorException;
import javax.media.Processor;
import javax.media.ProcessorModel;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import br.ufal.ic.nexos.arcolive.util.StateHelper;

/**
 * CLASSNAME.java
 * 
 * CLASS DESCRIPTION
 * 
 * @see CLASSNAME
 * 
 * @author <a href="mailto:felipe@labpesquisas.tci.ufal.br">Felipe Barros Pontes</a>.
 * @author <a href="mailto:leandro@labpesquisas.tci.ufal.br">Leandro Melo de
 *         Sales</a>.
 * @since 0.1
 * @version 0.1
 * 
 * <p>
 * <b>Revisions:</b>
 * 
 * <p>
 * <b>yyyymmdd USERNAME:</b>
 * <ul>
 * <li> VERSION
 * </ul>
 */
public class MediaRecorder implements Serializable {

    private Format[] formats;

    private FileTypeDescriptor outputType;

    private Processor processor;

    private DataSource dataSource;

    private DataSink fileWriter;

    private MediaLocator sourceMediaLocator;

    private MediaLocator targetMediaLocator;

    private StateHelper stateHelper;

    /**
	 * 
	 */
    public MediaRecorder() {
    }

    /**
	 * @return
	 */
    public MediaLocator getMediaSource() {
        return this.sourceMediaLocator;
    }

    /**
	 * @param sourceMediaLocator
	 */
    public void setMediaSource(MediaLocator sourceMediaLocator) {
        this.sourceMediaLocator = sourceMediaLocator;
    }

    /**
	 * @return
	 */
    public MediaLocator getTarget() {
        return this.targetMediaLocator;
    }

    /**
	 * @param targetMediaLocator
	 */
    public void setTarget(MediaLocator targetMediaLocator) {
        this.targetMediaLocator = targetMediaLocator;
    }

    /**
	 * @param formats
	 */
    public void setFormat(Format[] formats) {
        this.formats = formats;
    }

    /**
	 * @param outputType
	 */
    public void setOutputType(FileTypeDescriptor outputType) {
        this.outputType = outputType;
    }

    /**
	 * @param dataSource
	 */
    public void setDatasource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
	 * @return
	 */
    public DataSource getDatasource() {
        return this.dataSource;
    }

    /**
	 * @throws NoProcessorException
	 * @throws CannotRealizeException
	 * @throws IOException
	 * @throws NoDataSinkException
	 */
    private void createMediaRecorder() throws NoProcessorException, CannotRealizeException, IOException, NoDataSinkException {
        this.processor = Manager.createRealizedProcessor(new ProcessorModel(this.sourceMediaLocator, this.formats, outputType));
        this.dataSource = this.processor.getDataOutput();
        this.fileWriter = Manager.createDataSink(this.dataSource, this.targetMediaLocator);
    }

    /**
	 * @param dataSource
	 * @throws NoProcessorException
	 * @throws CannotRealizeException
	 * @throws IOException
	 * @throws NoDataSinkException
	 */
    private void createMediaRecorder(DataSource dataSource) throws NoProcessorException, CannotRealizeException, IOException, NoDataSinkException {
        this.processor = Manager.createRealizedProcessor(new ProcessorModel(dataSource, this.formats, outputType));
        this.dataSource = this.processor.getDataOutput();
        this.fileWriter = Manager.createDataSink(this.dataSource, this.targetMediaLocator);
    }

    /**
	 * @param miliSeconds
	 * @throws CannotMediaRecordException
	 */
    public void record(int miliSeconds) throws CannotMediaRecordException {
        String message = "Cannot record media: ";
        try {
            if (this.dataSource != null) {
                this.createMediaRecorder(this.dataSource);
            } else {
                this.createMediaRecorder();
            }
            this.fileWriter.open();
            this.fileWriter.start();
            this.processor.start();
            this.stateHelper = new StateHelper(this.processor);
            this.stateHelper.playToEndOfMedia(miliSeconds);
            this.stateHelper.close();
        } catch (NoProcessorException e) {
            throw new CannotMediaRecordException(message + e.getMessage());
        } catch (CannotRealizeException e) {
            throw new CannotMediaRecordException(message + e.getMessage());
        } catch (NoDataSinkException e) {
            throw new CannotMediaRecordException(message + e.getMessage());
        } catch (IOException e) {
            throw new CannotMediaRecordException(message + e.getMessage());
        }
    }

    /**
	 * @throws CannotStopRecorderException
	 */
    public void closeRecorder() throws CannotStopRecorderException {
        String message = "Cannot close recorder: ";
        this.processor.stop();
        try {
            this.fileWriter.stop();
        } catch (IOException e) {
            throw new CannotStopRecorderException(message + e.getMessage());
        }
        this.fileWriter.close();
    }
}
