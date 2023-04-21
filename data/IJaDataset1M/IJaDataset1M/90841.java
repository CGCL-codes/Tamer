package com.francetelecom.rd.maps.semeuse.t31d2_slachecking;

import org.apache.log4j.Logger;
import org.objectweb.dream.Push;
import org.objectweb.dream.PushException;
import org.objectweb.dream.control.activity.manager.TaskManager;
import org.objectweb.dream.dreamannotation.DreamComponent;
import org.objectweb.dream.dreamannotation.DreamLifeCycle;
import org.objectweb.dream.dreamannotation.util.DreamLifeCycleType;
import org.objectweb.dream.message.ChunkFactoryReference;
import org.objectweb.dream.message.Message;
import org.objectweb.dream.message.MessageManagerType;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fraclet.annotation.annotations.Attribute;
import org.objectweb.fractal.fraclet.annotation.annotations.Interface;
import org.objectweb.fractal.fraclet.annotation.annotations.Provides;
import org.objectweb.fractal.fraclet.annotation.annotations.Requires;
import org.objectweb.fractal.fraclet.annotation.annotations.Service;
import org.objectweb.fractal.util.AbstractComponent;
import com.francetelecom.rd.maps.semeuse.t31d2_slachecking.chunk.ProceededReportFromSloIndicatorsCalculationToAlertManagerAdapterChunk;
import com.francetelecom.rd.maps.semeuse.t31d2_slachecking.chunk.RawReportFromPetalsDataStreamAdapterToSloIndicatorsCalculationChunk;
import com.francetelecom.rd.maps.semeuse.t31d2_slachecking.serializable_objects.ProcessedReport;
import com.francetelecom.rd.maps.semeuse.t31d2_slachecking.serializable_objects.RawReport;

/**
 * ---------------------------------------------------------
 * 
 * @Software_Name : SLO Monitoring
 * @Version : 1.0.0
 * 
 * @Copyright � 2009 France Telecom
 * @License: This software is distributed under the GNU Lesser General Public
 *           License (Version 2.1) as published by the Free Software Foundation,
 *           the text of which is available at
 *           http://www.gnu.org/licenses/lgpl-2.1.html or see the "license.txt"
 *           file for more details.
 * 
 * @--------------------------------------------------------
 * 
 * @Created : 02/2009
 * @Author(s) : Antonin CHAZALET
 * @Contact: antonin.chazalet@gmail.com
 * 
 * @Description :
 * 
 * @--------------------------------------------------------
 */
@DreamComponent
@Provides(interfaces = { @Interface(name = "in-push", signature = Push.class) })
public class SloIndicatorsCalculation implements Push {

    /**
	 * Component Reference
	 */
    @Service
    Component ref;

    private Logger consoleLogger = null;

    @Attribute(argument = "nbMsgToReceive")
    int nbMsgToReceive;

    /**
	 * The number of message that the PetalsDataStreamAdapter must still
	 * receives. This method is synchronized because two different threads call
	 * get/set
	 * 
	 * @return a number of messages
	 */
    public synchronized int getNbMsgToReceive() {
        return nbMsgToReceive;
    }

    /**
	 * Set the number that the PetalsDataStreamAdapter must still receives. This
	 * method is synchronized because two different threads call get/set
	 * 
	 * @return a number of messages
	 */
    public synchronized void setNbMsgToReceive(int nbMsgToReceive_) {
        this.nbMsgToReceive = nbMsgToReceive_;
    }

    @Requires(name = "message-manager_2")
    MessageManagerType messageManagerItf;

    /**
	 * @see Push#push(Message)
	 */
    public void push(Message message) throws PushException {
        RawReportFromPetalsDataStreamAdapterToSloIndicatorsCalculationChunk c = (RawReportFromPetalsDataStreamAdapterToSloIndicatorsCalculationChunk) messageManagerItf.getChunk(message, RawReportFromPetalsDataStreamAdapterToSloIndicatorsCalculationChunk.DEFAULT_NAME);
        if (c == null) {
            throw new PushException("Unable to find " + RawReportFromPetalsDataStreamAdapterToSloIndicatorsCalculationChunk.DEFAULT_NAME);
        }
        consoleLogger.debug("Here, we are in " + this.getClass().getSimpleName());
        this.go(c.getRawReport());
        messageManagerItf.deleteMessage(message);
    }

    /**
	 * the Push interface.
	 */
    @Requires(name = "out-push")
    private Push outPushItf;

    /**
	 * the message manager interface.
	 */
    @Requires(name = "message-manager_3")
    private MessageManagerType messageManager_3Itf;

    /**
	 * Task Manager Interface : used to register the task.
	 */
    @SuppressWarnings("unused")
    @Requires(name = "task-manager")
    private TaskManager taskManagerItf;

    private ChunkFactoryReference<ProceededReportFromSloIndicatorsCalculationToAlertManagerAdapterChunk> chunkFactory;

    private void go(RawReport rawReport_) {
        try {
            Message msg = messageManager_3Itf.createMessage();
            ProceededReportFromSloIndicatorsCalculationToAlertManagerAdapterChunk chunk = messageManager_3Itf.createChunk(chunkFactory);
            ProcessedReport pR = null;
            pR = new ProcessedReport(rawReport_.getMessageId(), rawReport_.getRequestIn(), rawReport_.getRequestOut(), rawReport_.getResponseIn(), rawReport_.getResponseOut());
            consoleLogger.debug("Pushing Message containing data ; I travelled via " + this.getClass().getSimpleName());
            chunk.setProcessedReport(pR);
            messageManager_3Itf.addChunk(msg, ProceededReportFromSloIndicatorsCalculationToAlertManagerAdapterChunk.DEFAULT_NAME, chunk);
            outPushItf.push(msg);
            this.setNbMsgToReceive(--nbMsgToReceive);
        } catch (PushException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @see AbstractComponent#beforeFirstStart(Component)
	 */
    @DreamLifeCycle(on = DreamLifeCycleType.FIRST_START)
    protected void beforeFirstStart(Component componentItf) throws IllegalLifeCycleException {
        consoleLogger = Logger.getRootLogger();
        chunkFactory = messageManager_3Itf.getChunkFactory(ProceededReportFromSloIndicatorsCalculationToAlertManagerAdapterChunk.class);
    }
}
