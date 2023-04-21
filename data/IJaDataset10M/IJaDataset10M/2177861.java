package org.opennfc;

final class MethodAutogen {

    private MethodAutogen() {
    }

    static native void W14Part3ExchangeData(int hConnection, int pCallback, int pReaderToCardBuffer, int pCardToReaderBuffer, int[] phOperation);

    static native int W14Part3GetConnectionInfoBuffer(int hConnection, byte[] pInfoBuffer);

    static native int W14Part3ListenToCardDetectionTypeB(int pHandler, int nPriority, int nAFI, boolean bUseCID, int nCID, int nBaudRate, byte[] pHigherLayerDataBuffer, int[] phEventRegistry);

    static native int W14Part3SetTimeout(int hConnection, int nTimeout);

    static native void W14Part4ExchangeData(int hConnection, int pCallback, int pReaderToCardBuffer, int pCardToReaderBuffer, int[] phOperation);

    static native int W14Part4GetConnectionInfoBuffer(int hConnection, byte[] pInfoBuffer);

    static native int W14Part4ListenToCardDetectionTypeA(int pHandler, int nPriority, boolean bUseCID, int nCID, int nBaudRate, int[] phEventRegistry);

    static native int W14Part4ListenToCardDetectionTypeB(int pHandler, int nPriority, int nAFI, boolean bUseCID, int nCID, int nBaudRate, byte[] pHigherLayerDataBuffer, int[] phEventRegistry);

    static native int W14Part4SetNAD(int hConnection, int nNAD);

    static native int W15GetConnectionInfoBuffer(int hConnection, byte[] pInfoBuffer);

    static native int W15IsWritable(int hConnection, int nSectorIndex);

    static native int W15ListenToCardDetection(int pHandler, int nPriority, int nAFI, int[] phEventRegistry);

    static native void W15Read(int hConnection, int pCallback, int pBuffer, int nOffset, int[] phOperation);

    static native void W15SetAttribute(int hConnection, int pCallback, int nActions, int nAFI, int nDSFID, int[] phOperation);

    static native int W15SetTagSize(int hConnection, int nSectorNumber, int nSectorSize);

    static native void W15Write(int hConnection, int pCallback, int pBuffer, int nOffset, boolean bLockSectors, int[] phOperation);

    static native void W7816CloseLogicalChannel(int hLogicalChannel, int pCallback);

    static native void W7816ExchangeAPDU(int hConnection, int pCallback, int pSendAPDUBuffer, int pReceivedAPDUBuffer, int[] phOperation);

    static native int W7816GetATR(int hConnection, byte[] pBuffer, int[] pnActualLength);

    static native int W7816GetATRSize(int hConnection, int[] pnSize);

    static native int W7816GetResponseAPDUData(int hConnection, byte[] pReceivedAPDUBuffer, int[] pnReceivedAPDUActualLength);

    static native void W7816OpenLogicalChannel(int hConnection, int pCallback, int pAID, int[] phOperation);

    static native void WBasicCancelOperation(int hOperation);

    static native int WBasicCheckConnectionProperty(int hConnection, int nPropertyIdentifier);

    static native void WBasicCloseHandle(int hHandle);

    static native void WBasicCloseHandleSafe(int hHandle, int pCallback);

    static native int WBasicGetConnectionProperties(int hConnection, byte[] pPropertyArray);

    static native int WBasicGetConnectionPropertyNumber(int hConnection, int[] pnPropertyNumber);

    static native int WBasicInit(String pVersionString);

    static native int WBasicPumpEvent(boolean bWait);

    static native void WBasicStopEventLoop();

    static native void WBasicTerminate();

    static native void WBPrimeExchangeData(int hConnection, int pCallback, int pReaderToCardBuffer, int pCardToReaderBuffer, int[] phOperation);

    static native int WBPrimeGetConnectionInfoBuffer(int hConnection, byte[] pInfoBuffer);

    static native int WBPrimeListenToCardDetection(int pHandler, int nPriority, byte[] pAPGENBuffer, int[] phEventRegistry);

    static native int WBPrimeSetTimeout(int hConnection, int nTimeout);

    static native boolean WDFCPumpJNICallback(int[] pArgs);

    static native void WEmulClose(int hHandle, int pCallback);

    static native int WEmulGetMessageData(int hHandle, byte[] pDataBuffer, int[] pnActualDataLength);

    static native boolean WEmulIsPropertySupported(int nPropertyIdentifier);

    static native void WEmulOpenConnectionBuffer(int pOpenCallback, int pEventCallback, int pCommandCallback, int nCardType, byte[] pUID, int nRandomIdentifierLength, int[] phHandle);

    static native int WEmulSendAnswer(int hDriverConnection, byte[] pDataBuffer);

    static native int WFeliCaGetConnectionInfoBuffer(int hConnection, byte[] pInfoBuffer);

    static native void WFeliCaReadSimple(int hConnection, int pCallback, int pBuffer, int[] pServiceCodeList, byte[] pBlockList);

    static native void WFeliCaWriteSimple(int hConnection, int pCallback, int pBuffer, int[] pServiceCodeList, byte[] pBlockList);

    static native int WJavaCreateByteBuffer(byte[] pJavaBuffer, int nOffset, int nLength);

    static native void WJavaNDEFWriteMessage(int hConnection, int pCallback, int pMessageBuffer, int nActionMask, int[] phOperation);

    static native void WJavaNDEFWriteMessageOnAnyTag(int pCallback, int nPriority, int pMessageBuffer, int nActionMask, int[] phOperation);

    static native boolean WJavaNFCControllerGetBooleanProperty(int nPropertyIdentifier);

    static native int WJavaP2PGetConfigurationBuffer(byte[] pConfigurationBuffer);

    static native int WJavaP2PGetLinkPropertiesBuffer(int hLink, byte[] pInfoBuffer);

    static native int WJavaP2PSetConfigurationBuffer(byte[] pConfigurationBuffer);

    static native void WJavaReleaseByteBuffer(int hBufferReference, byte[] pJavaBuffer);

    static native int WMifareGetConnectionInfoBuffer(int hConnection, byte[] pInfoBuffer);

    static native void WMifareRead(int hConnection, int pCallback, int pBuffer, int nOffset, int[] phOperation);

    static native void WMifareULCAuthenticate(int hConnection, int pCallback, byte[] pKey);

    static native void WMifareULCSetAccessRights(int hConnection, int pCallback, byte[] pKey, int nThreshold, int nRights, boolean bLockConfiguration);

    static native int WMifareULForceULC(int hConnection);

    static native void WMifareULFreezeDataLockConfiguration(int hConnection, int pCallback);

    static native int WMifareULGetAccessRights(int hConnection, int nOffset, int nLength, int[] pnRights);

    static native void WMifareULRetrieveAccessRights(int hConnection, int pCallback);

    static native void WMifareWrite(int hConnection, int pCallback, int pBuffer, int nOffset, boolean bLockSectors, int[] phOperation);

    static native int WMyDGetConnectionInfoBuffer(int hConnection, byte[] pInfoBuffer);

    static native void WMyDMoveAuthenticate(int hConnection, int pCallback, int nPassword);

    static native void WMyDMoveFreezeDataLockConfiguration(int hConnection, int pCallback);

    static native void WMyDMoveGetConfiguration(int hConnection, int pCallback);

    static native void WMyDMoveSetConfiguration(int hConnection, int pCallback, int nStatusByte, int nPasswordRetryCounter, int nPassword, boolean bLockConfiguration);

    static native void WMyDRead(int hConnection, int pCallback, int pBuffer, int nOffset, int[] phOperation);

    static native void WMyDWrite(int hConnection, int pCallback, int pBuffer, int nOffset, boolean bLockSectors, int[] phOperation);

    static native int WNDEFGetTagInfoBuffer(int hConnection, byte[] pInfoBuffer);

    static native void WNDEFReadMessage(int hConnection, int pCallback, int nTNF, String pTypeString, int[] phOperation);

    static native void WNDEFReadMessageOnAnyTag(int pCallback, int nPriority, int nTNF, String pTypeString, int[] phRegistry);

    static native void WNFCControllerFirmwareUpdate(int pCallback, int pUpdateBuffer, int nMode);

    static native int WNFCControllerFirmwareUpdateState();

    static native int WNFCControllerGetFirmwareProperty(byte[] pUpdateBuffer, int nPropertyIdentifier, char[] pValueBuffer, int[] pnValueLength);

    static native int WNFCControllerGetMode();

    static native int WNFCControllerGetProperty(int nPropertyIdentifier, char[] pValueBuffer, int[] pnValueLength);

    static native void WNFCControllerGetRFActivity(int[] pnReaderState, int[] pnCardState, int[] pnP2PState);

    static native void WNFCControllerGetRFLock(int nLockSet, int[] pbReaderLock, int[] pbCardLock);

    static native int WNFCControllerMonitorException(int pHandler, int[] phEventRegistry);

    static native void WNFCControllerProductionTest(int pParameterBuffer, int pResultBuffer, int pCallback);

    static native void WNFCControllerReset(int pCallback, int nMode);

    static native void WNFCControllerSelfTest(int pCallback);

    static native void WNFCControllerSetRFLock(int nLockSet, boolean bReaderLock, boolean bCardLock, int pCallback);

    static native int WNFCControllerSwitchStandbyMode(boolean bStandbyOn);

    static native void WP2PConnect(int hSocket, int hLink, int pEstablishmentCallback);

    static native int WP2PCreateSocket(int nType, String pServiceURI, int nSAP, int[] phSocket);

    static native void WP2PEstablishLink(int pEstablishmentCallback, int pReleaseCallback, int[] phOperation);

    static native int WP2PGetSocketParameter(int hSocket, int nParameter, int[] pnValue);

    static native void WP2PRead(int hSocket, int pCallback, int pReceptionBuffer, int[] phOperation);

    static native void WP2PRecvFrom(int hSocket, int pCallback, int pReceptionBuffer, int[] phOperation);

    static native void WP2PSendTo(int hSocket, int pCallback, int nSAP, int pSendBuffer, int[] phOperation);

    static native int WP2PSetSocketParameter(int hSocket, int nParameter, int nValue);

    static native void WP2PShutdown(int hSocket, int pReleaseCallback);

    static native void WP2PURILookup(int hLink, int pCallback, String pServiceURI);

    static native void WP2PWrite(int hSocket, int pCallback, int pSendBuffer, int[] phOperation);

    static native int WPicoGetConnectionInfoBuffer(int hConnection, byte[] pInfoBuffer);

    static native int WPicoIsWritable(int hConnection);

    static native void WPicoRead(int hConnection, int pCallback, int pBuffer, int nOffset, int[] phOperation);

    static native void WPicoWrite(int hConnection, int pCallback, int pBuffer, int nOffset, boolean bLockCard, int[] phOperation);

    static native int WReaderErrorEventRegister(int pHandler, int nEventType, boolean bCardDetectionRequested, int[] phRegistryHandle);

    static native int WReaderGetPulsePeriod(int[] pnTimeout);

    static native void WReaderHandlerWorkPerformed(int hConnection, boolean bGiveToNextListener, boolean bCardApplicationMatch);

    static native boolean WReaderIsPropertySupported(int nPropertyIdentifier);

    static native int WReaderListenToCardDetection(int pHandler, int nPriority, byte[] pConnectionPropertyArray, int[] phEventRegistry);

    static native boolean WReaderPreviousApplicationMatch(int hConnection);

    static native void WReaderSetPulsePeriod(int pCallback, int nPulsePeriod);

    static native int WSecurityAuthenticate(byte[] pApplicationDataBuffer);

    static native int WType1ChipGetConnectionInfoBuffer(int hConnection, byte[] pInfoBuffer);

    static native int WType1ChipIsWritable(int hConnection);

    static native void WType1ChipRead(int hConnection, int pCallback, int pBuffer, int nOffset, int[] phOperation);

    static native void WType1ChipWrite(int hConnection, int pCallback, int pBuffer, int nOffset, boolean bLockBlocks, int[] phOperation);

    static native int WVirtualTagCreate(int nTagType, byte[] pIdentifier, int nMaximumMessageLength, int[] phVirtualTag);

    static native void WVirtualTagStart(int hVirtualTag, int pCompletionCallback, int pEventCallback, boolean bReadOnly);

    static native void WVirtualTagStop(int hVirtualTag, int pCompletionCallback);
}

abstract class Callback_tWBasicGenericCallbackFunction extends AsynchronousCompletion {

    protected Callback_tWBasicGenericCallbackFunction(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWBasicGenericCallbackFunction(int nResult);

    protected void invokeFunction(int[] args) {
        tWBasicGenericCallbackFunction(args[1]);
    }
}

abstract class Callback_tWBasicGenericCompletionFunction extends AsynchronousCompletion {

    protected Callback_tWBasicGenericCompletionFunction(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWBasicGenericCompletionFunction();

    protected void invokeFunction(int[] args) {
        tWBasicGenericCompletionFunction();
    }
}

abstract class Callback_tWBasicGenericDataCallbackFunction extends AsynchronousCompletion {

    protected Callback_tWBasicGenericDataCallbackFunction(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWBasicGenericDataCallbackFunction(int nDataLength, int nResult);

    protected void invokeFunction(int[] args) {
        tWBasicGenericDataCallbackFunction(args[1], args[2]);
    }
}

abstract class Callback_tWBasicGenericEventHandler extends AsynchronousCompletion {

    protected Callback_tWBasicGenericEventHandler(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWBasicGenericEventHandler(int nEventCode);

    protected void invokeFunction(int[] args) {
        tWBasicGenericEventHandler(args[1]);
    }
}

abstract class Callback_tWBasicGenericEventHandler2 extends AsynchronousCompletion {

    protected Callback_tWBasicGenericEventHandler2(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWBasicGenericEventHandler2(int nEventCode1, int nEventCode2);

    protected void invokeFunction(int[] args) {
        tWBasicGenericEventHandler2(args[1], args[2]);
    }
}

abstract class Callback_tWBasicGenericHandleCallbackFunction extends AsynchronousCompletion {

    protected Callback_tWBasicGenericHandleCallbackFunction(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWBasicGenericHandleCallbackFunction(int hHandle, int nResult);

    protected void invokeFunction(int[] args) {
        tWBasicGenericHandleCallbackFunction(args[1], args[2]);
    }
}

abstract class Callback_tWEmulCommandReceived extends AsynchronousCompletion {

    protected Callback_tWEmulCommandReceived(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWEmulCommandReceived(int nDataLength);

    protected void invokeFunction(int[] args) {
        tWEmulCommandReceived(args[1]);
    }
}

abstract class Callback_tWMyDMoveGetConfigurationCompleted extends AsynchronousCompletion {

    protected Callback_tWMyDMoveGetConfigurationCompleted(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWMyDMoveGetConfigurationCompleted(int nError, int nStatusByte, int nPasswordRetryCounter);

    protected void invokeFunction(int[] args) {
        tWMyDMoveGetConfigurationCompleted(args[1], args[2], args[3]);
    }
}

abstract class Callback_tWNDEFReadMessageCompleted extends AsynchronousCompletion {

    protected Callback_tWNDEFReadMessageCompleted(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWNDEFReadMessageCompleted(int hMessage, int nError);

    protected void invokeFunction(int[] args) {
        tWNDEFReadMessageCompleted(args[1], args[2]);
    }
}

abstract class Callback_tWNFCControllerSelfTestCompleted extends AsynchronousCompletion {

    protected Callback_tWNFCControllerSelfTestCompleted(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWNFCControllerSelfTestCompleted(int nError, int nResult);

    protected void invokeFunction(int[] args) {
        tWNFCControllerSelfTestCompleted(args[1], args[2]);
    }
}

abstract class Callback_tWP2PRecvFromCompleted extends AsynchronousCompletion {

    protected Callback_tWP2PRecvFromCompleted(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWP2PRecvFromCompleted(int nDataLength, int nError, int nSAP);

    protected void invokeFunction(int[] args) {
        tWP2PRecvFromCompleted(args[1], args[2], args[3]);
    }
}

abstract class Callback_tWP2PURILookupCompleted extends AsynchronousCompletion {

    protected Callback_tWP2PURILookupCompleted(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWP2PURILookupCompleted(int nDSAP, int nError);

    protected void invokeFunction(int[] args) {
        tWP2PURILookupCompleted(args[1], args[2]);
    }
}

abstract class Callback_tWReaderCardDetectionHandler extends AsynchronousCompletion {

    protected Callback_tWReaderCardDetectionHandler(boolean isEvent) {
        super(isEvent);
    }

    protected abstract void tWReaderCardDetectionHandler(int hConnection, int nError);

    protected void invokeFunction(int[] args) {
        tWReaderCardDetectionHandler(args[1], args[2]);
    }
}
