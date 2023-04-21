package com.android.internal.telephony.cat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemProperties;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
import android.util.Config;
import java.io.ByteArrayOutputStream;

/**
 * Enumeration for representing the tag value of COMPREHENSION-TLV objects. If
 * you want to get the actual value, call {@link #value() value} method.
 *
 * {@hide}
 */
enum ComprehensionTlvTag {

    COMMAND_DETAILS(0x01), DEVICE_IDENTITIES(0x02), RESULT(0x03), DURATION(0x04), ALPHA_ID(0x05), USSD_STRING(0x0a), TEXT_STRING(0x0d), TONE(0x0e), ITEM(0x0f), ITEM_ID(0x10), RESPONSE_LENGTH(0x11), FILE_LIST(0x12), HELP_REQUEST(0x15), DEFAULT_TEXT(0x17), EVENT_LIST(0x19), ICON_ID(0x1e), ITEM_ICON_ID_LIST(0x1f), IMMEDIATE_RESPONSE(0x2b), LANGUAGE(0x2d), URL(0x31), BROWSER_TERMINATION_CAUSE(0x34), TEXT_ATTRIBUTE(0x50);

    private int mValue;

    ComprehensionTlvTag(int value) {
        mValue = value;
    }

    /**
     * Returns the actual value of this COMPREHENSION-TLV object.
     *
     * @return Actual tag value of this object
     */
    public int value() {
        return mValue;
    }

    public static ComprehensionTlvTag fromInt(int value) {
        for (ComprehensionTlvTag e : ComprehensionTlvTag.values()) {
            if (e.mValue == value) {
                return e;
            }
        }
        return null;
    }
}

class RilMessage {

    int mId;

    Object mData;

    ResultCode mResCode;

    RilMessage(int msgId, String rawData) {
        mId = msgId;
        mData = rawData;
    }

    RilMessage(RilMessage other) {
        this.mId = other.mId;
        this.mData = other.mData;
        this.mResCode = other.mResCode;
    }
}

/**
 * Class that implements SIM Toolkit Telephony Service. Interacts with the RIL
 * and application.
 *
 * {@hide}
 */
public class CatService extends Handler implements AppInterface {

    private static IccRecords mIccRecords;

    private static CatService sInstance;

    private CommandsInterface mCmdIf;

    private Context mContext;

    private CatCmdMessage mCurrntCmd = null;

    private CatCmdMessage mMenuCmd = null;

    private RilMessageDecoder mMsgDecoder = null;

    static final int MSG_ID_SESSION_END = 1;

    static final int MSG_ID_PROACTIVE_COMMAND = 2;

    static final int MSG_ID_EVENT_NOTIFY = 3;

    static final int MSG_ID_CALL_SETUP = 4;

    static final int MSG_ID_REFRESH = 5;

    static final int MSG_ID_RESPONSE = 6;

    static final int MSG_ID_RIL_MSG_DECODED = 10;

    private static final int MSG_ID_ICC_RECORDS_LOADED = 20;

    private static final int DEV_ID_KEYPAD = 0x01;

    private static final int DEV_ID_DISPLAY = 0x02;

    private static final int DEV_ID_EARPIECE = 0x03;

    private static final int DEV_ID_UICC = 0x81;

    private static final int DEV_ID_TERMINAL = 0x82;

    private static final int DEV_ID_NETWORK = 0x83;

    private CatService(CommandsInterface ci, IccRecords ir, Context context, IccFileHandler fh, IccCard ic) {
        if (ci == null || ir == null || context == null || fh == null || ic == null) {
            throw new NullPointerException("Service: Input parameters must not be null");
        }
        mCmdIf = ci;
        mContext = context;
        mMsgDecoder = RilMessageDecoder.getInstance(this, fh);
        mCmdIf.setOnCatSessionEnd(this, MSG_ID_SESSION_END, null);
        mCmdIf.setOnCatProactiveCmd(this, MSG_ID_PROACTIVE_COMMAND, null);
        mCmdIf.setOnCatEvent(this, MSG_ID_EVENT_NOTIFY, null);
        mCmdIf.setOnCatCallSetUp(this, MSG_ID_CALL_SETUP, null);
        mIccRecords = ir;
        mIccRecords.registerForRecordsLoaded(this, MSG_ID_ICC_RECORDS_LOADED, null);
        mCmdIf.reportStkServiceIsRunning(null);
        CatLog.d(this, "Is running");
    }

    public void dispose() {
        mIccRecords.unregisterForRecordsLoaded(this);
        mCmdIf.unSetOnCatSessionEnd(this);
        mCmdIf.unSetOnCatProactiveCmd(this);
        mCmdIf.unSetOnCatEvent(this);
        mCmdIf.unSetOnCatCallSetUp(this);
        this.removeCallbacksAndMessages(null);
    }

    protected void finalize() {
        CatLog.d(this, "Service finalized");
    }

    private void handleRilMsg(RilMessage rilMsg) {
        if (rilMsg == null) {
            return;
        }
        CommandParams cmdParams = null;
        switch(rilMsg.mId) {
            case MSG_ID_EVENT_NOTIFY:
                if (rilMsg.mResCode == ResultCode.OK) {
                    cmdParams = (CommandParams) rilMsg.mData;
                    if (cmdParams != null) {
                        handleProactiveCommand(cmdParams);
                    }
                }
                break;
            case MSG_ID_PROACTIVE_COMMAND:
                cmdParams = (CommandParams) rilMsg.mData;
                if (cmdParams != null) {
                    if (rilMsg.mResCode == ResultCode.OK) {
                        handleProactiveCommand(cmdParams);
                    } else {
                        sendTerminalResponse(cmdParams.cmdDet, rilMsg.mResCode, false, 0, null);
                    }
                }
                break;
            case MSG_ID_REFRESH:
                cmdParams = (CommandParams) rilMsg.mData;
                if (cmdParams != null) {
                    handleProactiveCommand(cmdParams);
                }
                break;
            case MSG_ID_SESSION_END:
                handleSessionEnd();
                break;
            case MSG_ID_CALL_SETUP:
                break;
        }
    }

    /**
     * Handles RIL_UNSOL_STK_PROACTIVE_COMMAND unsolicited command from RIL.
     * Sends valid proactive command data to the application using intents.
     *
     */
    private void handleProactiveCommand(CommandParams cmdParams) {
        CatLog.d(this, cmdParams.getCommandType().name());
        CatCmdMessage cmdMsg = new CatCmdMessage(cmdParams);
        switch(cmdParams.getCommandType()) {
            case SET_UP_MENU:
                if (removeMenu(cmdMsg.getMenu())) {
                    mMenuCmd = null;
                } else {
                    mMenuCmd = cmdMsg;
                }
                sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
                break;
            case DISPLAY_TEXT:
                if (!cmdMsg.geTextMessage().responseNeeded) {
                    sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
                }
                break;
            case REFRESH:
                cmdParams.cmdDet.typeOfCommand = CommandType.SET_UP_IDLE_MODE_TEXT.value();
                break;
            case SET_UP_IDLE_MODE_TEXT:
                sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
                break;
            case PROVIDE_LOCAL_INFORMATION:
                sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
                return;
            case LAUNCH_BROWSER:
            case SELECT_ITEM:
            case GET_INPUT:
            case GET_INKEY:
            case SEND_DTMF:
            case SEND_SMS:
            case SEND_SS:
            case SEND_USSD:
            case PLAY_TONE:
            case SET_UP_CALL:
                break;
            default:
                CatLog.d(this, "Unsupported command");
                return;
        }
        mCurrntCmd = cmdMsg;
        Intent intent = new Intent(AppInterface.CAT_CMD_ACTION);
        intent.putExtra("STK CMD", cmdMsg);
        mContext.sendBroadcast(intent);
    }

    /**
     * Handles RIL_UNSOL_STK_SESSION_END unsolicited command from RIL.
     *
     */
    private void handleSessionEnd() {
        CatLog.d(this, "SESSION END");
        mCurrntCmd = mMenuCmd;
        Intent intent = new Intent(AppInterface.CAT_SESSION_END_ACTION);
        mContext.sendBroadcast(intent);
    }

    private void sendTerminalResponse(CommandDetails cmdDet, ResultCode resultCode, boolean includeAdditionalInfo, int additionalInfo, ResponseData resp) {
        if (cmdDet == null) {
            return;
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        Input cmdInput = null;
        if (mCurrntCmd != null) {
            cmdInput = mCurrntCmd.geInput();
        }
        int tag = ComprehensionTlvTag.COMMAND_DETAILS.value();
        if (cmdDet.compRequired) {
            tag |= 0x80;
        }
        buf.write(tag);
        buf.write(0x03);
        buf.write(cmdDet.commandNumber);
        buf.write(cmdDet.typeOfCommand);
        buf.write(cmdDet.commandQualifier);
        tag = ComprehensionTlvTag.DEVICE_IDENTITIES.value();
        buf.write(tag);
        buf.write(0x02);
        buf.write(DEV_ID_TERMINAL);
        buf.write(DEV_ID_UICC);
        tag = 0x80 | ComprehensionTlvTag.RESULT.value();
        buf.write(tag);
        int length = includeAdditionalInfo ? 2 : 1;
        buf.write(length);
        buf.write(resultCode.value());
        if (includeAdditionalInfo) {
            buf.write(additionalInfo);
        }
        if (resp != null) {
            resp.format(buf);
        } else {
            encodeOptionalTags(cmdDet, resultCode, cmdInput, buf);
        }
        byte[] rawData = buf.toByteArray();
        String hexString = IccUtils.bytesToHexString(rawData);
        if (Config.LOGD) {
            CatLog.d(this, "TERMINAL RESPONSE: " + hexString);
        }
        mCmdIf.sendTerminalResponse(hexString, null);
    }

    private void encodeOptionalTags(CommandDetails cmdDet, ResultCode resultCode, Input cmdInput, ByteArrayOutputStream buf) {
        switch(AppInterface.CommandType.fromInt(cmdDet.typeOfCommand)) {
            case GET_INKEY:
                if ((resultCode.value() == ResultCode.NO_RESPONSE_FROM_USER.value()) && (cmdInput != null) && (cmdInput.duration != null)) {
                    getInKeyResponse(buf, cmdInput);
                }
                break;
            case PROVIDE_LOCAL_INFORMATION:
                if ((cmdDet.commandQualifier == CommandParamsFactory.LANGUAGE_SETTING) && (resultCode.value() == ResultCode.OK.value())) {
                    getPliResponse(buf);
                }
                break;
            default:
                CatLog.d(this, "encodeOptionalTags() Unsupported Cmd:" + cmdDet.typeOfCommand);
                break;
        }
    }

    private void getInKeyResponse(ByteArrayOutputStream buf, Input cmdInput) {
        int tag = ComprehensionTlvTag.DURATION.value();
        buf.write(tag);
        buf.write(0x02);
        buf.write(cmdInput.duration.timeUnit.SECOND.value());
        buf.write(cmdInput.duration.timeInterval);
    }

    private void getPliResponse(ByteArrayOutputStream buf) {
        String lang = SystemProperties.get("persist.sys.language");
        if (lang != null) {
            int tag = ComprehensionTlvTag.LANGUAGE.value();
            buf.write(tag);
            ResponseData.writeLength(buf, lang.length());
            buf.write(lang.getBytes(), 0, lang.length());
        }
    }

    private void sendMenuSelection(int menuId, boolean helpRequired) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int tag = BerTlv.BER_MENU_SELECTION_TAG;
        buf.write(tag);
        buf.write(0x00);
        tag = 0x80 | ComprehensionTlvTag.DEVICE_IDENTITIES.value();
        buf.write(tag);
        buf.write(0x02);
        buf.write(DEV_ID_KEYPAD);
        buf.write(DEV_ID_UICC);
        tag = 0x80 | ComprehensionTlvTag.ITEM_ID.value();
        buf.write(tag);
        buf.write(0x01);
        buf.write(menuId);
        if (helpRequired) {
            tag = ComprehensionTlvTag.HELP_REQUEST.value();
            buf.write(tag);
            buf.write(0x00);
        }
        byte[] rawData = buf.toByteArray();
        int len = rawData.length - 2;
        rawData[1] = (byte) len;
        String hexString = IccUtils.bytesToHexString(rawData);
        mCmdIf.sendEnvelope(hexString, null);
    }

    private void eventDownload(int event, int sourceId, int destinationId, byte[] additionalInfo, boolean oneShot) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int tag = BerTlv.BER_EVENT_DOWNLOAD_TAG;
        buf.write(tag);
        buf.write(0x00);
        tag = 0x80 | ComprehensionTlvTag.EVENT_LIST.value();
        buf.write(tag);
        buf.write(0x01);
        buf.write(event);
        tag = 0x80 | ComprehensionTlvTag.DEVICE_IDENTITIES.value();
        buf.write(tag);
        buf.write(0x02);
        buf.write(sourceId);
        buf.write(destinationId);
        if (additionalInfo != null) {
            for (byte b : additionalInfo) {
                buf.write(b);
            }
        }
        byte[] rawData = buf.toByteArray();
        int len = rawData.length - 2;
        rawData[1] = (byte) len;
        String hexString = IccUtils.bytesToHexString(rawData);
        mCmdIf.sendEnvelope(hexString, null);
    }

    /**
     * Used for instantiating/updating the Service from the GsmPhone or CdmaPhone constructor.
     *
     * @param ci CommandsInterface object
     * @param ir IccRecords object
     * @param context phone app context
     * @param fh Icc file handler
     * @param ic Icc card
     * @return The only Service object in the system
     */
    public static CatService getInstance(CommandsInterface ci, IccRecords ir, Context context, IccFileHandler fh, IccCard ic) {
        if (sInstance == null) {
            if (ci == null || ir == null || context == null || fh == null || ic == null) {
                return null;
            }
            HandlerThread thread = new HandlerThread("Cat Telephony service");
            thread.start();
            sInstance = new CatService(ci, ir, context, fh, ic);
            CatLog.d(sInstance, "NEW sInstance");
        } else if ((ir != null) && (mIccRecords != ir)) {
            CatLog.d(sInstance, "Reinitialize the Service with SIMRecords");
            mIccRecords = ir;
            mIccRecords.registerForRecordsLoaded(sInstance, MSG_ID_ICC_RECORDS_LOADED, null);
            CatLog.d(sInstance, "sr changed reinitialize and return current sInstance");
        } else {
            CatLog.d(sInstance, "Return current sInstance");
        }
        return sInstance;
    }

    /**
     * Used by application to get an AppInterface object.
     *
     * @return The only Service object in the system
     */
    public static AppInterface getInstance() {
        return getInstance(null, null, null, null, null);
    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_ID_SESSION_END:
            case MSG_ID_PROACTIVE_COMMAND:
            case MSG_ID_EVENT_NOTIFY:
            case MSG_ID_REFRESH:
                CatLog.d(this, "ril message arrived");
                String data = null;
                if (msg.obj != null) {
                    AsyncResult ar = (AsyncResult) msg.obj;
                    if (ar != null && ar.result != null) {
                        try {
                            data = (String) ar.result;
                        } catch (ClassCastException e) {
                            break;
                        }
                    }
                }
                mMsgDecoder.sendStartDecodingMessageParams(new RilMessage(msg.what, data));
                break;
            case MSG_ID_CALL_SETUP:
                mMsgDecoder.sendStartDecodingMessageParams(new RilMessage(msg.what, null));
                break;
            case MSG_ID_ICC_RECORDS_LOADED:
                break;
            case MSG_ID_RIL_MSG_DECODED:
                handleRilMsg((RilMessage) msg.obj);
                break;
            case MSG_ID_RESPONSE:
                handleCmdResponse((CatResponseMessage) msg.obj);
                break;
            default:
                throw new AssertionError("Unrecognized CAT command: " + msg.what);
        }
    }

    public synchronized void onCmdResponse(CatResponseMessage resMsg) {
        if (resMsg == null) {
            return;
        }
        Message msg = this.obtainMessage(MSG_ID_RESPONSE, resMsg);
        msg.sendToTarget();
    }

    private boolean validateResponse(CatResponseMessage resMsg) {
        if (mCurrntCmd != null) {
            return (resMsg.cmdDet.compareTo(mCurrntCmd.mCmdDet));
        }
        return false;
    }

    private boolean removeMenu(Menu menu) {
        try {
            if (menu.items.size() == 1 && menu.items.get(0) == null) {
                return true;
            }
        } catch (NullPointerException e) {
            CatLog.d(this, "Unable to get Menu's items size");
            return true;
        }
        return false;
    }

    private void handleCmdResponse(CatResponseMessage resMsg) {
        if (!validateResponse(resMsg)) {
            return;
        }
        ResponseData resp = null;
        boolean helpRequired = false;
        CommandDetails cmdDet = resMsg.getCmdDetails();
        switch(resMsg.resCode) {
            case HELP_INFO_REQUIRED:
                helpRequired = true;
            case OK:
            case PRFRMD_WITH_PARTIAL_COMPREHENSION:
            case PRFRMD_WITH_MISSING_INFO:
            case PRFRMD_WITH_ADDITIONAL_EFS_READ:
            case PRFRMD_ICON_NOT_DISPLAYED:
            case PRFRMD_MODIFIED_BY_NAA:
            case PRFRMD_LIMITED_SERVICE:
            case PRFRMD_WITH_MODIFICATION:
            case PRFRMD_NAA_NOT_ACTIVE:
            case PRFRMD_TONE_NOT_PLAYED:
                switch(AppInterface.CommandType.fromInt(cmdDet.typeOfCommand)) {
                    case SET_UP_MENU:
                        helpRequired = resMsg.resCode == ResultCode.HELP_INFO_REQUIRED;
                        sendMenuSelection(resMsg.usersMenuSelection, helpRequired);
                        return;
                    case SELECT_ITEM:
                        resp = new SelectItemResponseData(resMsg.usersMenuSelection);
                        break;
                    case GET_INPUT:
                    case GET_INKEY:
                        Input input = mCurrntCmd.geInput();
                        if (!input.yesNo) {
                            if (!helpRequired) {
                                resp = new GetInkeyInputResponseData(resMsg.usersInput, input.ucs2, input.packed);
                            }
                        } else {
                            resp = new GetInkeyInputResponseData(resMsg.usersYesNoSelection);
                        }
                        break;
                    case DISPLAY_TEXT:
                    case LAUNCH_BROWSER:
                        break;
                    case SET_UP_CALL:
                        mCmdIf.handleCallSetupRequestFromSim(resMsg.usersConfirm, null);
                        mCurrntCmd = null;
                        return;
                }
                break;
            case NO_RESPONSE_FROM_USER:
            case UICC_SESSION_TERM_BY_USER:
            case BACKWARD_MOVE_BY_USER:
                resp = null;
                break;
            default:
                return;
        }
        sendTerminalResponse(cmdDet, resMsg.resCode, false, 0, resp);
        mCurrntCmd = null;
    }
}
