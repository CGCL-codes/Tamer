package org.openmobster.core.mobileCloud.android.module.sync;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import org.openmobster.core.mobileCloud.android.configuration.Configuration;
import org.openmobster.core.mobileCloud.android.errors.ErrorHandler;
import org.openmobster.core.mobileCloud.android.module.mobileObject.DeviceSerializer;
import org.openmobster.core.mobileCloud.android.module.mobileObject.MobileObject;
import org.openmobster.core.mobileCloud.android.module.sync.engine.ChangeLogEntry;
import org.openmobster.core.mobileCloud.android.module.sync.engine.SyncEngine;
import org.openmobster.core.mobileCloud.android.module.sync.engine.SyncError;
import org.openmobster.core.mobileCloud.android.service.Registry;
import org.openmobster.core.mobileCloud.android.module.bus.Bus;
import org.openmobster.core.mobileCloud.android.module.bus.BusException;
import org.openmobster.core.mobileCloud.android.module.bus.MobilePushInvocation;

/**
 * 
 * @author openmobster@gmail.com
 * 
 * Memory Marker - Stateful Component (RAM Usage)
 *
 */
public class SyncAdapter {

    /**
	 * Keys for attributes carried on the incoming request objects
	 */
    public static String PAYLOAD = "payload";

    public static String SOURCE = "source";

    public static String TARGET = "target";

    public static String MAX_CLIENT_SIZE = "maxClientSize";

    public static String CLIENT_INITIATED = "clientInitiated";

    public static String DATA_SOURCE = "dataSource";

    public static String DATA_TARGET = "dataTarget";

    public static String SYNC_TYPE = "syncType";

    /**
	 * Synchronization related codes
	 */
    public static String TWO_WAY = "200";

    public static String SLOW_SYNC = "201";

    public static String ONE_WAY_CLIENT = "202";

    public static String ONE_WAY_SERVER = "204";

    public static String SUCCESS = "200";

    public static String AUTH_SUCCESS = "202";

    public static String COMMAND_FAILURE = "500";

    public static String ANCHOR_FAILURE = "508";

    public static String CHUNK_ACCEPTED = "213";

    public static String CHUNK_SUCCESS = "201";

    public static String NEXT_MESSAGE = "222";

    public static String SIZE_MISMATCH = "424";

    public static String GENERIC_SYNC_FAILURE = "500";

    public static final String AUTHENTICATION_SUCCESS = "212";

    public static final String AUTHENTICATION_FAILURE = "401";

    /**
	 * Sync Engine extension
	 */
    public static String STREAM = "250";

    public static String STREAM_RECORD_ID = "streamRecordId";

    public static final String BOOT_SYNC = "260";

    /**
	 * Status codes carried by the response object
	 */
    public static final int RESPONSE_CLOSE = 1;

    /**
	 * Synchronization Phase Codes
	 */
    public static final int PHASE_INIT = 1;

    public static final int PHASE_SYNC = 2;

    public static final int PHASE_CLOSE = 3;

    public static final int PHASE_END = 4;

    /**
	 * object helping out
	 */
    protected SyncXMLGenerator syncXMLGenerator;

    protected SyncEngine syncEngine;

    protected SyncObjectGenerator syncObjectGenerator;

    protected Session activeSession;

    protected PhaseManager phaseManager;

    /**
	 * 
	 *
	 */
    public SyncAdapter() {
        this.syncXMLGenerator = new SyncXMLGenerator();
        this.syncObjectGenerator = SyncObjectGenerator.getInstance();
        this.phaseManager = new PhaseManager();
    }

    /**
	 * 
	 * @param syncEngine
	 */
    public void setSyncEngine(SyncEngine syncEngine) {
        this.syncEngine = syncEngine;
    }

    /**
	 * 
	 * @param request
	 * @return
	 */
    public SyncAdapterResponse service(SyncAdapterRequest request) throws SyncException {
        SyncAdapterResponse response = new SyncAdapterResponse();
        String payload = (String) request.getAttribute(SyncAdapter.PAYLOAD);
        Session parsedSession = this.syncObjectGenerator.parse(payload);
        this.activeSession.setCurrentMessage(parsedSession.getCurrentMessage());
        this.phaseManager.processPhase(this, this.activeSession);
        String syncPayload = null;
        switch(this.activeSession.getPhaseCode()) {
            case SyncAdapter.PHASE_INIT:
                break;
            case SyncAdapter.PHASE_SYNC:
                syncPayload = this.sync(this.activeSession);
                response.setAttribute(SyncAdapter.PAYLOAD, syncPayload);
                break;
            case SyncAdapter.PHASE_CLOSE:
                syncPayload = this.close(this.activeSession);
                response.setAttribute(SyncAdapter.PAYLOAD, syncPayload);
                break;
            case SyncAdapter.PHASE_END:
                this.end(this.activeSession);
                response.setStatus(SyncAdapter.RESPONSE_CLOSE);
                break;
        }
        return response;
    }

    /**
	 * 
	 * @param request
	 * @param syncType
	 * @return
	 */
    public SyncAdapterResponse start(SyncAdapterRequest request) throws SyncException {
        SyncAdapterResponse response = new SyncAdapterResponse();
        Context context = Registry.getActiveInstance().getContext();
        String source = (String) request.getAttribute(SyncAdapter.SOURCE);
        String target = (String) request.getAttribute(SyncAdapter.TARGET);
        String maxClientSize = (String) request.getAttribute(SyncAdapter.MAX_CLIENT_SIZE);
        String clientInitiated = (String) request.getAttribute(SyncAdapter.CLIENT_INITIATED);
        String dataSource = (String) request.getAttribute(SyncAdapter.DATA_SOURCE);
        String dataTarget = (String) request.getAttribute(SyncAdapter.DATA_TARGET);
        String syncType = (String) request.getAttribute(SyncAdapter.SYNC_TYPE);
        String isBackground = (String) request.getAttribute("isBackgroundSync");
        String app = (String) request.getAttribute(SyncXMLTags.App);
        this.activeSession = new Session();
        this.activeSession.setSessionId(this.syncEngine.generateSync());
        this.activeSession.setSource(source);
        this.activeSession.setTarget(target);
        this.activeSession.setApp(app);
        if (request.getAttribute(STREAM_RECORD_ID) != null) {
            this.activeSession.setAttribute(STREAM_RECORD_ID, request.getAttribute(STREAM_RECORD_ID));
        }
        SyncMessage syncMessage = new SyncMessage();
        syncMessage.setMessageId("1");
        if (maxClientSize != null) {
            syncMessage.setMaxClientSize(Integer.parseInt(maxClientSize));
        }
        if (clientInitiated != null) {
            if (clientInitiated.equals("true")) {
                syncMessage.setClientInitiated(Boolean.TRUE.booleanValue());
            } else {
                syncMessage.setClientInitiated(Boolean.FALSE.booleanValue());
            }
        }
        Anchor anchor = this.syncEngine.createNewAnchor(source + "/" + dataSource);
        String anchorXml = this.syncXMLGenerator.generateAnchor(anchor);
        String meta = "<![CDATA[\n" + anchorXml + "]]>\n";
        this.activeSession.setAnchor(anchor);
        Item item = new Item();
        item.setSource(dataSource);
        item.setTarget(dataTarget);
        item.setMeta(meta);
        syncType = this.evaluateSyncType(dataSource, dataTarget, syncType);
        Alert alert = new Alert();
        alert.setCmdId("1");
        alert.setData(syncType);
        alert.addItem(item);
        this.activeSession.setSyncType(syncType);
        syncMessage.addAlert(alert);
        Credential credential = new Credential(SyncXMLTags.sycml_auth_sha, Configuration.getInstance(context).getAuthenticationHash());
        syncMessage.setCredential(credential);
        syncMessage.setFinal(true);
        this.activeSession.getClientInitPackage().addMessage(syncMessage);
        if (isBackground != null && isBackground.equalsIgnoreCase("true")) {
            this.activeSession.setBackgroundSync(true);
        }
        String payload = this.syncXMLGenerator.generateInitMessage(this.activeSession, syncMessage);
        response.setAttribute(SyncAdapter.PAYLOAD, payload);
        return response;
    }

    /**
	 * 
	 * @param session
	 * @return
	 */
    protected String sync(Session session) throws SyncException {
        String payload = null;
        SyncMessage syncReply = this.handleSyncScenarios(session);
        if (session.getPhaseCode() == SyncAdapter.PHASE_SYNC) {
            session.getClientSyncPackage().addMessage(syncReply);
        }
        payload = this.syncXMLGenerator.generateSyncMessage(session, syncReply);
        return payload;
    }

    /**
	 * 
	 * @param session
	 */
    protected String close(Session session) throws SyncException {
        String payload = null;
        SyncMessage currentMessage = session.getCurrentMessage();
        SyncMessage clientSyncMessage = new SyncMessage();
        int messageId = Integer.parseInt(currentMessage.getMessageId());
        messageId++;
        int cmdId = 1;
        clientSyncMessage.setMessageId(String.valueOf(messageId));
        this.processSyncCommands(cmdId, session, clientSyncMessage);
        this.cleanupChangeLog(session);
        if (!session.getRecordMap().isEmpty()) {
            RecordMap recordMap = this.setUpRecordMap(cmdId++, session, session.getRecordMap());
            clientSyncMessage.setRecordMap(recordMap);
        }
        clientSyncMessage.setFinal(true);
        session.getClientClosePackage().addMessage(clientSyncMessage);
        payload = this.syncXMLGenerator.generateSyncMessage(session, clientSyncMessage);
        return payload;
    }

    private void syncPush(MobilePushInvocation invocation) {
        Map<String, Object> shared = invocation.getShared();
        Bundle bundle = new Bundle();
        Set<String> keys = shared.keySet();
        for (String key : keys) {
            String value = shared.get(key).toString();
            bundle.putString(key, value);
        }
        Intent intent = new Intent("org.openmobster.sync.push");
        intent.putExtra("bundle", bundle);
        Context context = Registry.getActiveInstance().getContext();
        context.sendBroadcast(intent);
    }

    /**
	 * 
	 */
    protected void end(Session session) {
        SyncMessage currentMessage = session.getCurrentMessage();
        MobilePushInvocation invocation = session.getPushInvocation();
        if (session.isBackgroundSync() && invocation != null && !invocation.getMobilePushMetaData().isEmpty()) {
            try {
                this.syncPush(invocation);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                ErrorHandler.getInstance().handle(e);
            }
        }
        List<Status> statuses = currentMessage.getStatus();
        for (Status status : statuses) {
            if (status.getCmd().equals(SyncXMLTags.Map) && status.getData().equals(SyncAdapter.SUCCESS)) {
                session.setRecordMap(null);
            }
        }
        if (!session.getRecordMap().isEmpty()) {
            try {
                this.syncEngine.saveRecordMap(session.getDataSource(false), session.getDataTarget(false), session.getRecordMap());
            } catch (Exception e) {
                try {
                    this.resetSyncState(session);
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
	 * 
	 * @param session
	 */
    protected boolean processRecordMapStatus(Session session) throws SyncException {
        boolean success = true;
        SyncMessage currentMessage = session.getCurrentMessage();
        List<Status> statuses = currentMessage.getStatus();
        for (Status status : statuses) {
            if (status.getCmd().equals(SyncXMLTags.Map)) {
                if (status.getData().equals(SyncAdapter.SUCCESS)) {
                    this.syncEngine.removeRecordMap(session.getDataSource(false), session.getDataTarget(false));
                    session.setRecordMap(null);
                    session.setMapExchangeInProgress(false);
                } else {
                    session.setMapExchangeInProgress(false);
                    success = false;
                }
            }
        }
        return success;
    }

    /**
	 * 
	 * @param session
	 * @return
	 */
    protected SyncMessage handleSyncScenarios(Session session) throws SyncException {
        SyncMessage reply = null;
        boolean success = this.processRecordMapStatus(session);
        Map<String, String> previousSyncRecordMap = this.syncEngine.readRecordMap(session.getDataSource(false), session.getDataTarget(false));
        if (!previousSyncRecordMap.isEmpty()) {
            if (success) {
                reply = this.setUpReply(session);
                RecordMap recordMap = this.setUpRecordMap(1, session, previousSyncRecordMap);
                reply.setRecordMap(recordMap);
                reply.setFinal(true);
                session.setMapExchangeInProgress(true);
                return reply;
            } else {
                session.setPhaseCode(SyncAdapter.PHASE_CLOSE);
                reply = this.handleCloseScenarios(session);
                return reply;
            }
        }
        this.cleanupChangeLog(session);
        SyncMessage currentMessage = session.getCurrentMessage();
        List<Status> statusCodes = new ArrayList<Status>();
        List<Status> statuses = currentMessage.getStatus();
        for (Status status : statuses) {
            if (!status.getData().equals(SyncAdapter.SUCCESS)) {
                statusCodes.add(status);
            }
        }
        boolean doesMessageContainChunks = this.doesMessageContainChunks(currentMessage);
        if (!session.isChunkOpen() && doesMessageContainChunks) {
            reply = this.startChunkReception(session);
            return reply;
        }
        if (session.isChunkOpen()) {
            reply = this.handleNextChunkReception(session);
            return reply;
        }
        if (session.getSyncType().equals(SyncAdapter.STREAM)) {
            reply = this.processStreamSync(session);
            return reply;
        }
        if (session.getSyncType().equals(SyncAdapter.BOOT_SYNC)) {
            reply = this.processBootSync(session);
            return reply;
        }
        reply = this.processNormalSync(session);
        return reply;
    }

    /**
	 * This performs the normal sync scenario. The Happy Path
	 * 
	 * @param session
	 * @return
	 */
    protected SyncMessage processNormalSync(Session session) throws SyncException {
        int cmdId = 1;
        SyncMessage reply = this.setUpReply(session);
        this.processSyncCommands(cmdId, session, reply);
        SyncCommand syncCommand = null;
        if (session.getSyncType().equals(SyncAdapter.TWO_WAY) || session.getSyncType().equals(SyncAdapter.SLOW_SYNC) || session.getSyncType().equals(SyncAdapter.ONE_WAY_CLIENT)) {
            syncCommand = this.generateSyncCommand(cmdId, false, session, reply);
        }
        this.setUpClientSyncFinal(session, reply, syncCommand);
        return reply;
    }

    /**
	 * This performs the stream sync scenario. The Happy Path
	 * @param session
	 * @return
	 */
    protected SyncMessage processStreamSync(Session session) {
        int cmdId = 1;
        SyncMessage reply = this.setUpReply(session);
        SyncCommand syncCommand = new SyncCommand();
        syncCommand.setCmdId(String.valueOf(cmdId++));
        syncCommand.setSource(session.getDataSource(false));
        syncCommand.setTarget(session.getDataTarget(false));
        Add add = new Add();
        add.setCmdId(String.valueOf(cmdId++));
        add.setMeta((String) session.getAttribute(STREAM_RECORD_ID));
        syncCommand.getAddCommands().add(add);
        reply.addSyncCommand(syncCommand);
        this.setUpClientSyncFinal(session, reply, syncCommand);
        return reply;
    }

    protected SyncMessage processBootSync(Session session) throws SyncException {
        int cmdId = 1;
        SyncMessage reply = this.setUpReply(session);
        SyncCommand syncCommand = new SyncCommand();
        syncCommand.setCmdId(String.valueOf(cmdId++));
        syncCommand.setSource(session.getDataSource(false));
        syncCommand.setTarget(session.getDataTarget(false));
        this.syncEngine.startBootSync(session, syncCommand.getTarget());
        this.setUpClientSyncFinal(session, reply, syncCommand);
        return reply;
    }

    /**
	 * 
	 * @param currentMessage
	 * @return
	 */
    protected void processSyncCommands(int cmdId, Session session, SyncMessage replyMessage) throws SyncException {
        List<Status> status = new ArrayList<Status>();
        SyncMessage currentMessage = session.getCurrentMessage();
        if (currentMessage.getSyncCommands().isEmpty()) {
            return;
        }
        if (!session.getSyncType().equals(SyncAdapter.SLOW_SYNC)) {
            List<SyncCommand> syncCommands = currentMessage.getSyncCommands();
            for (SyncCommand syncCommand : syncCommands) {
                Status syncStatus = new Status();
                syncStatus.setCmdId(String.valueOf(cmdId++));
                syncStatus.setCmd(SyncXMLTags.Sync);
                syncStatus.setData(SyncAdapter.SUCCESS);
                syncStatus.setMsgRef(currentMessage.getMessageId());
                syncStatus.setCmdRef(syncCommand.getCmdId());
                syncStatus.getSourceRefs().add(syncCommand.getSource());
                syncStatus.getTargetRefs().add(syncCommand.getTarget());
                status.add(syncStatus);
                List<Status> cour = this.syncEngine.processSyncCommand(session, syncCommand.getTarget(), syncCommand);
                for (Status courStatus : cour) {
                    courStatus.setCmdId(String.valueOf(cmdId++));
                    courStatus.setMsgRef(currentMessage.getMessageId());
                    status.add(courStatus);
                }
                replyMessage.getStatus().addAll(status);
            }
        } else {
            List<SyncCommand> syncCommands = currentMessage.getSyncCommands();
            for (SyncCommand syncCommand : syncCommands) {
                Status syncStatus = new Status();
                syncStatus.setCmdId(String.valueOf(cmdId++));
                syncStatus.setCmd(SyncXMLTags.Sync);
                syncStatus.setData(SyncAdapter.SUCCESS);
                syncStatus.setMsgRef(currentMessage.getMessageId());
                syncStatus.setCmdRef(syncCommand.getCmdId());
                syncStatus.getSourceRefs().add(syncCommand.getSource());
                syncStatus.getTargetRefs().add(syncCommand.getTarget());
                status.add(syncStatus);
                List<Status> cour = this.syncEngine.processSlowSyncCommand(session, syncCommand.getTarget(), syncCommand);
                for (Status courStatus : cour) {
                    courStatus.setCmdId(String.valueOf(cmdId++));
                    courStatus.setMsgRef(currentMessage.getMessageId());
                    status.add(courStatus);
                }
                replyMessage.getStatus().addAll(status);
            }
        }
    }

    /**
	 * 
	 *
	 */
    protected SyncCommand generateSyncCommand(int cmdId, boolean isServer, Session session, SyncMessage replyMessage) throws SyncException {
        SyncCommand syncCommand = new SyncCommand();
        syncCommand.setCmdId(String.valueOf(cmdId++));
        syncCommand.setSource(session.getDataSource(isServer));
        syncCommand.setTarget(session.getDataTarget(isServer));
        if (!session.isOperationCommandStateInitiated()) {
            try {
                if (!session.getSyncType().equals(SyncAdapter.SLOW_SYNC)) {
                    List<? extends AbstractOperation> addCommands = this.syncEngine.getAddCommands(session.getMaxClientSize(), syncCommand.getSource(), session.getSyncType());
                    for (AbstractOperation cour : addCommands) {
                        cour.setCmdId(String.valueOf(cmdId++));
                    }
                    List<? extends AbstractOperation> replaceCommands = this.syncEngine.getReplaceCommands(session.getMaxClientSize(), syncCommand.getSource(), session.getSyncType());
                    for (AbstractOperation cour : replaceCommands) {
                        cour.setCmdId(String.valueOf(cmdId++));
                    }
                    List<? extends AbstractOperation> deleteCommands = this.syncEngine.getDeleteCommands(syncCommand.getSource(), session.getSyncType());
                    for (AbstractOperation cour : deleteCommands) {
                        cour.setCmdId(String.valueOf(cmdId++));
                    }
                    session.clearOperationCommandState();
                    session.initiateOperationCommandState();
                    session.getAllOperationCommands().addAll(addCommands);
                    session.getAllOperationCommands().addAll(replaceCommands);
                    session.getAllOperationCommands().addAll(deleteCommands);
                } else {
                    List<? extends AbstractOperation> slowSyncCommands = this.syncEngine.getSlowSyncCommands(session.getMaxClientSize(), syncCommand.getSource());
                    for (AbstractOperation cour : slowSyncCommands) {
                        cour.setCmdId(String.valueOf(cmdId++));
                    }
                    session.clearOperationCommandState();
                    session.initiateOperationCommandState();
                    session.getAllOperationCommands().addAll(slowSyncCommands);
                }
            } catch (Exception e) {
                SyncException syne = new SyncException(this.getClass().getName(), "generateSyncCommand", new Object[] { "Session=" + session.getSessionId(), "Exception=" + e.toString(), "Message=" + e.getMessage() });
                ErrorHandler.getInstance().handle(syne);
            }
        }
        int numberOfCommands = this.calculateNumberOfCommands(session.getMaxClientSize(), session.getAllOperationCommands());
        int allSize = session.getAllOperationCommands().size();
        List<AbstractOperation> allCommands = session.getAllOperationCommands();
        for (int i = 0; i < numberOfCommands; i++) {
            int commandIndex = session.getOperationCommandIndex();
            if (commandIndex < allSize) {
                AbstractOperation op = (AbstractOperation) allCommands.get(commandIndex);
                commandIndex++;
                session.setOperationCommandIndex(commandIndex);
                if (op instanceof Add) {
                    syncCommand.getAddCommands().add((Add) op);
                } else if (op instanceof Replace) {
                    syncCommand.getReplaceCommands().add((Replace) op);
                } else if (op instanceof Delete) {
                    syncCommand.getDeleteCommands().add((Delete) op);
                }
                if (commandIndex == allSize) {
                    session.clearOperationCommandState();
                }
            } else {
                session.clearOperationCommandState();
                break;
            }
        }
        List<? extends AbstractOperation> chunkedCommands = syncCommand.filterChunkedCommands();
        session.setChunkedCommands(chunkedCommands);
        replyMessage.addSyncCommand(syncCommand);
        return syncCommand;
    }

    /**
	 * 
	 * @param maxClientSize
	 * @param commands
	 * @return
	 */
    protected int calculateNumberOfCommands(int maxClientSize, List<AbstractOperation> commands) {
        int numberOfCommands = 0;
        int numberOfCommandsAfterFiltering = 0;
        for (AbstractOperation command : commands) {
            if (!(command instanceof Delete) && !command.isChunked()) {
                numberOfCommandsAfterFiltering++;
            }
        }
        long sizeCounter = 0;
        for (AbstractOperation command : commands) {
            if (command instanceof Delete || command.isChunked()) {
                continue;
            }
            Item item = command.getItems().iterator().next();
            if ((sizeCounter + item.getData().length()) <= maxClientSize) {
                sizeCounter += item.getData().length();
                numberOfCommands++;
            } else {
                break;
            }
        }
        if (numberOfCommands == 0 || numberOfCommands == numberOfCommandsAfterFiltering) {
            numberOfCommands = commands.size();
        }
        return numberOfCommands;
    }

    /**
	 * 
	 * @param session
	 * @return
	 */
    protected SyncMessage setUpReply(Session session) {
        SyncMessage syncMessage = new SyncMessage();
        SyncMessage currentMessage = session.getCurrentMessage();
        int messageId = Integer.parseInt(currentMessage.getMessageId());
        messageId++;
        syncMessage.setMessageId(String.valueOf(messageId));
        return syncMessage;
    }

    /**
	 * 
	 * @param session
	 * @param reply
	 * @param syncCommand
	 */
    protected void setUpClientSyncFinal(Session session, SyncMessage reply, SyncCommand syncCommand) {
        reply.setFinal(true);
    }

    /**
	 * 
	 * @param source
	 * @param target
	 * @param requestedSyncType
	 * @return
	 */
    protected String evaluateSyncType(String source, String target, String requestedSyncType) throws SyncException {
        String syncType = requestedSyncType;
        SyncError error = this.syncEngine.readError(source, target, SyncError.RESET_SYNC_STATE);
        if (error != null && error.getCode().equals(SyncError.RESET_SYNC_STATE)) {
            syncType = SyncAdapter.SLOW_SYNC;
            this.syncEngine.removeError(error.getSource(), error.getTarget(), SyncError.RESET_SYNC_STATE);
        }
        return syncType;
    }

    /**
	 * 
	 * @param currentMessage
	 * @return
	 */
    protected boolean doesMessageContainChunks(SyncMessage currentMessage) {
        boolean doesMessageContainChunks = false;
        List<SyncCommand> syncCommands = currentMessage.getSyncCommands();
        for (SyncCommand syncCommand : syncCommands) {
            List<? extends AbstractOperation> chunkedCommands = syncCommand.getChunkedCommands();
            if (chunkedCommands != null && !chunkedCommands.isEmpty()) {
                return true;
            }
        }
        return doesMessageContainChunks;
    }

    /**
	 * 
	 */
    protected SyncMessage startChunkReception(Session session) throws SyncException {
        SyncMessage currentMessage = session.getCurrentMessage();
        SyncMessage clientSyncMessage = new SyncMessage();
        int messageId = Integer.parseInt(currentMessage.getMessageId());
        messageId++;
        int cmdId = 1;
        clientSyncMessage.setMessageId(String.valueOf(messageId));
        this.processSyncCommands(cmdId, session, clientSyncMessage);
        Status chunkAcceptedStatus = new Status();
        AbstractOperation chunkedCommand = this.getChunkedCommand(currentMessage);
        chunkAcceptedStatus.setCmdId(String.valueOf(cmdId++));
        if (chunkedCommand instanceof Add) {
            chunkAcceptedStatus.setCmd(SyncXMLTags.Add);
        } else {
            chunkAcceptedStatus.setCmd(SyncXMLTags.Replace);
        }
        chunkAcceptedStatus.setData(SyncAdapter.CHUNK_ACCEPTED);
        chunkAcceptedStatus.setMsgRef(currentMessage.getMessageId());
        chunkAcceptedStatus.setCmdRef(chunkedCommand.getCmdId());
        clientSyncMessage.getStatus().add(chunkAcceptedStatus);
        session.addChunk(chunkedCommand);
        clientSyncMessage.setFinal(true);
        return clientSyncMessage;
    }

    /**
	 * 
	 * @param session
	 * @return
	 */
    protected SyncMessage handleNextChunkReception(Session session) throws SyncException {
        SyncMessage reply = this.setUpReply(session);
        int cmdId = 1;
        SyncMessage currentMessage = session.getCurrentMessage();
        AbstractOperation chunkedCommand = this.getFirstOperationCommand(currentMessage);
        session.addChunk(chunkedCommand);
        if (chunkedCommand.isChunked()) {
            Alert nextMessage = new Alert();
            nextMessage.setCmdId(String.valueOf(cmdId++));
            nextMessage.setData(SyncAdapter.NEXT_MESSAGE);
            reply.addAlert(nextMessage);
        } else {
            boolean sizeMismatch = false;
            if (!this.doesTotalSizeOfChunksMatch(session)) {
                sizeMismatch = true;
            }
            Status status = null;
            if (!sizeMismatch) {
                status = this.processChunkedCommand(session, chunkedCommand);
                status.setCmdId(String.valueOf(cmdId++));
                status.setMsgRef(currentMessage.getMessageId());
                status.setCmdRef(chunkedCommand.getCmdId());
                reply.addStatus(status);
            } else {
                status = new Status();
                status.setCmdId(String.valueOf(cmdId++));
                if (chunkedCommand instanceof Add) {
                    status.setCmd(SyncXMLTags.Add);
                } else {
                    status.setCmd(SyncXMLTags.Replace);
                }
                status.setMsgRef(currentMessage.getMessageId());
                status.setCmdRef(chunkedCommand.getCmdId());
                status.setData(SyncAdapter.SIZE_MISMATCH);
                reply.addStatus(status);
            }
            session.clearChunkState();
            boolean containsAnotherChunkedCommand = this.doesMessageContainChunks(currentMessage);
            if (containsAnotherChunkedCommand) {
                Status chunkAcceptedStatus = new Status();
                AbstractOperation newChunkedCommand = this.getChunkedCommand(currentMessage);
                chunkAcceptedStatus.setCmdId(String.valueOf(cmdId++));
                if (newChunkedCommand instanceof Add) {
                    chunkAcceptedStatus.setCmd(SyncXMLTags.Add);
                } else {
                    chunkAcceptedStatus.setCmd(SyncXMLTags.Replace);
                }
                chunkAcceptedStatus.setData(SyncAdapter.CHUNK_ACCEPTED);
                chunkAcceptedStatus.setMsgRef(currentMessage.getMessageId());
                chunkAcceptedStatus.setCmdRef(newChunkedCommand.getCmdId());
                reply.getStatus().add(chunkAcceptedStatus);
                session.addChunk(newChunkedCommand);
            }
        }
        reply.setFinal(true);
        return reply;
    }

    /**
	 * 
	 * @param session
	 * @return
	 */
    protected boolean doesTotalSizeOfChunksMatch(Session session) {
        return true;
    }

    /**
	 * 
	 * @param session
	 * @return
	 */
    protected SyncMessage getSizeMismatchReply(Session session) {
        SyncMessage reply = this.setUpReply(session);
        int cmdId = 1;
        SyncMessage currentMessage = session.getCurrentMessage();
        AbstractOperation chunkedCommand = this.getFirstOperationCommand(currentMessage);
        Status status = new Status();
        status.setCmdId(String.valueOf(cmdId++));
        if (chunkedCommand instanceof Add) {
            status.setCmd(SyncXMLTags.Add);
        } else {
            status.setCmd(SyncXMLTags.Replace);
        }
        status.setMsgRef(currentMessage.getMessageId());
        status.setCmdRef(chunkedCommand.getCmdId());
        status.setData(SyncAdapter.SIZE_MISMATCH);
        reply.addStatus(status);
        session.clearChunkState();
        session.activateRetransmission();
        reply.setFinal(true);
        return reply;
    }

    /**
	 * 
	 * @param session
	 * @param chunkedCommand
	 */
    protected Status processChunkedCommand(Session session, AbstractOperation chunkedCommand) throws SyncException {
        try {
            Status status = null;
            AbstractOperation reconstructedCommand = (AbstractOperation) chunkedCommand.clone();
            String reassembledChunks = session.reassembleChunks();
            reconstructedCommand.getItems().iterator().next().setData(reassembledChunks);
            SyncCommand command = new SyncCommand();
            command.addOperationCommand(reconstructedCommand);
            String source = session.getDataSource(false);
            List<Status> cour = this.syncEngine.processSyncCommand(session, source, command);
            status = (Status) cour.iterator().next();
            if (status.getData().equals(SyncAdapter.SUCCESS)) {
                status.setData(SyncAdapter.CHUNK_SUCCESS);
            }
            return status;
        } catch (Exception e) {
            throw new SyncException(this.getClass().getName(), "processChunkedCommand", new Object[] { session.toString(), session.reassembleChunks() });
        }
    }

    /**
	 * 
	 * @param currentMessage
	 * @return
	 */
    protected AbstractOperation getChunkedCommand(SyncMessage currentMessage) {
        AbstractOperation chunkedCommand = null;
        List<SyncCommand> syncCommands = currentMessage.getSyncCommands();
        for (SyncCommand syncCommand : syncCommands) {
            List<? extends AbstractOperation> chunkedCommands = syncCommand.getChunkedCommands();
            if (chunkedCommands != null && !chunkedCommands.isEmpty()) {
                chunkedCommand = chunkedCommands.iterator().next();
                break;
            }
        }
        return chunkedCommand;
    }

    /**
	 * 
	 * @param currentMessage
	 * @return
	 */
    protected AbstractOperation getFirstOperationCommand(SyncMessage currentMessage) {
        AbstractOperation operationCommand = null;
        List<SyncCommand> syncCommands = currentMessage.getSyncCommands();
        for (SyncCommand syncCommand : syncCommands) {
            if (!syncCommand.getAddCommands().isEmpty()) {
                operationCommand = syncCommand.getAddCommands().iterator().next();
            } else if (!syncCommand.getReplaceCommands().isEmpty()) {
                operationCommand = syncCommand.getReplaceCommands().iterator().next();
            } else if (!syncCommand.getDeleteCommands().isEmpty()) {
                operationCommand = syncCommand.getDeleteCommands().iterator().next();
            }
        }
        return operationCommand;
    }

    /**
	 * 
	 * @param status
	 */
    protected void cleanupChangeLog(Session session) throws SyncException {
        SyncMessage currentMessage = session.getCurrentMessage();
        if (session.getSyncType().equals(SyncAdapter.SLOW_SYNC) && session.getPhaseCode() == SyncAdapter.PHASE_CLOSE) {
            try {
                this.syncEngine.clearChangeLog();
            } catch (Exception e) {
                SyncException syne = new SyncException(this.getClass().getName(), "cleanupChangeLog", new Object[] { "Session=" + session.getSessionId(), "Exception=" + e.toString(), "Message=" + e.getMessage() });
                ErrorHandler.getInstance().handle(syne);
            }
            return;
        }
        List<Status> statuses = currentMessage.getStatus();
        for (Status cour : statuses) {
            try {
                if (cour.getCmd().equals(SyncXMLTags.Add) || cour.getCmd().equals(SyncXMLTags.Delete) || cour.getCmd().equals(SyncXMLTags.Replace)) {
                    if (cour.getData().equals(SyncAdapter.SUCCESS) || cour.getData().equals(SyncAdapter.CHUNK_SUCCESS)) {
                        ChangeLogEntry changeLogEntry = session.findClientLogEntry(cour);
                        if (changeLogEntry != null) {
                            if (cour.getData().equals(SyncAdapter.CHUNK_SUCCESS)) {
                                changeLogEntry.getItem().setData(session.reassembleChunks());
                                session.clearChunkBackup();
                            }
                            MobileObject mobileObject = DeviceSerializer.getInstance().deserialize(changeLogEntry.getItem().getData());
                            String recordId = mobileObject.getRecordId();
                            String operation = cour.getCmd();
                            changeLogEntry.setRecordId(recordId);
                            changeLogEntry.setOperation(operation);
                            this.syncEngine.clearChangeLogEntry(changeLogEntry);
                        }
                    }
                }
            } catch (Exception e) {
                SyncException syne = new SyncException(this.getClass().getName(), "cleanupChangeLog", new Object[] { "Session=" + session.getSessionId(), "Exception=" + e.toString(), "Message=" + e.getMessage() });
                ErrorHandler.getInstance().handle(syne);
            }
        }
    }

    /**
	 * 
	 */
    protected SyncMessage handleCloseScenarios(Session session) throws SyncException {
        SyncMessage currentMessage = session.getCurrentMessage();
        SyncMessage clientSyncMessage = new SyncMessage();
        int messageId = Integer.parseInt(currentMessage.getMessageId());
        messageId++;
        int cmdId = 1;
        clientSyncMessage.setMessageId(String.valueOf(messageId));
        this.processSyncCommands(cmdId, session, clientSyncMessage);
        this.cleanupChangeLog(session);
        if (!session.getRecordMap().isEmpty()) {
            RecordMap recordMap = this.setUpRecordMap(cmdId++, session, session.getRecordMap());
            clientSyncMessage.setRecordMap(recordMap);
        }
        clientSyncMessage.setFinal(true);
        session.getClientClosePackage().addMessage(clientSyncMessage);
        return clientSyncMessage;
    }

    /**
	 * 
	 * @param session
	 * @return
	 */
    protected RecordMap setUpRecordMap(int cmdId, Session session, Map<String, String> recordMaps) {
        RecordMap recordMap = null;
        Set<String> keys = recordMaps.keySet();
        recordMap = new RecordMap();
        recordMap.setCmdId(String.valueOf(cmdId));
        recordMap.setSource(session.getDataSource(false));
        recordMap.setTarget(session.getDataTarget(false));
        for (String guid : keys) {
            Object luid = recordMaps.get(guid);
            MapItem mapItem = new MapItem();
            mapItem.setSource(luid.toString());
            mapItem.setTarget(guid.toString());
            recordMap.getMapItems().add(mapItem);
        }
        return recordMap;
    }

    /**
	 * 
	 *
	 */
    protected void resetSyncState(Session session) throws SyncException {
        String source = session.getDataSource(false);
        String target = session.getDataTarget(false);
        this.syncEngine.clearAll(session, source);
        SyncError error = new SyncError();
        error.setCode(SyncError.RESET_SYNC_STATE);
        error.setSource(source);
        error.setTarget(target);
        this.syncEngine.saveError(error);
    }
}
