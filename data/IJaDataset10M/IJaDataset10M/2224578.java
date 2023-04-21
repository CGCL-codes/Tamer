package CADI.Proxy.Client;

import java.io.PrintStream;
import java.util.ArrayList;
import CADI.Common.Cache.CacheManagement;
import CADI.Common.Cache.DataBin;
import CADI.Common.Cache.MainHeaderDataBin;
import CADI.Common.Cache.PrecinctDataBin;
import CADI.Common.LogicalTarget.JPEG2000.RelevantPrecinct;
import CADI.Common.Network.JPIP.ClassIdentifiers;
import CADI.Common.Network.JPIP.ViewWindowField;
import CADI.Proxy.Core.SendDataInfo;
import CADI.Proxy.LogicalTarget.JPEG2000.ProxyJPEG2KCodestream;
import CADI.Proxy.Server.ProxyCacheModel;

/**
 * This class is used to save a logical target that is being cached by
 * the CADIProxy. It extends the basic class {@link CacheManagement} adding new
 * features useful for CADIProxy.
 *
 * @author Group on Interactive Coding of Images (GICI)
 * @version 1.0.3 2012/03/09
 */
public class ProxyCacheManagement extends CacheManagement {

    /**
   * This flag allows that when a precinct is partialy in the cache, the
   * available data is delivered to client without waiting for the remainder.
   * Although it reduces delivering times it also increases JPIP message headers
   * because the same packet must be signaled twice.
   */
    private boolean ALLOW_PARTIAL_PRECINCTS = true;

    /**
   * Constructor.
   */
    public ProxyCacheManagement() {
        super();
    }

    /**
   * 
   * @param viewWindow
   * @param relevantPrecincts
   * @param cacheModel
   * @param availableData
   * @param unAvailableData 
   */
    public void checkAvailableData(ViewWindowField viewWindow, ArrayList<RelevantPrecinct> relevantPrecincts, ProxyCacheModel cacheModel, ArrayList<SendDataInfo> availableData, ArrayList<SendDataInfo> unAvailableData) {
        if (viewWindow == null) {
            throw new NullPointerException();
        }
        if (relevantPrecincts == null) {
            throw new NullPointerException();
        }
        if (cacheModel == null) {
            throw new NullPointerException();
        }
        if (availableData == null) {
            throw new NullPointerException();
        }
        if (unAvailableData == null) {
            throw new NullPointerException();
        }
        assert (codestream != null);
        SendDataInfo sendDataInfo = null;
        if (!isComplete(ClassIdentifiers.MAIN_HEADER, 0)) {
            assert (true);
        }
        int lengthInClient = cacheModel.getMainHeaderLength();
        if (lengthInClient < mainHeaderDataBin.getLength()) {
            sendDataInfo = new SendDataInfo(SendDataInfo.MAIN_HEADER, 0);
            sendDataInfo.bytesOffset = lengthInClient;
            sendDataInfo.bytesLength = (int) mainHeaderDataBin.getLength() - lengthInClient;
            sendDataInfo.layersOffset = 0;
            sendDataInfo.layersLength = 0;
            availableData.add(sendDataInfo);
        }
        lengthInClient = cacheModel.getDataBinLength(ClassIdentifiers.TILE_HEADER, 0);
        if (getDatabinLength(ClassIdentifiers.TILE_HEADER, 0) <= 0) {
            sendDataInfo = new SendDataInfo(SendDataInfo.TILE_HEADER, 0);
            sendDataInfo.bytesOffset = lengthInClient;
            unAvailableData.add(sendDataInfo);
        } else {
            int thLength = (int) getDatabinLength(ClassIdentifiers.TILE_HEADER, 0);
            if (lengthInClient < thLength) {
                sendDataInfo = new SendDataInfo(SendDataInfo.TILE_HEADER, 0);
                sendDataInfo.bytesOffset = lengthInClient;
                sendDataInfo.bytesLength = thLength - lengthInClient;
                availableData.add(sendDataInfo);
            }
        }
        for (RelevantPrecinct relevantPrecinct : relevantPrecincts) {
            long inClassIdentifier = relevantPrecinct.inClassIdentifier;
            PrecinctDataBin dataBin = (PrecinctDataBin) getDataBin(DataBin.PRECINCT, inClassIdentifier);
            if (dataBin == null) {
                sendDataInfo = new SendDataInfo(SendDataInfo.PRECINCT, inClassIdentifier);
                sendDataInfo.layersOffset = relevantPrecinct.startLayer;
                sendDataInfo.layersLength = relevantPrecinct.endLayer - relevantPrecinct.startLayer;
                sendDataInfo.bytesOffset = (int) relevantPrecinct.msgOffset;
                unAvailableData.add(sendDataInfo);
                continue;
            }
            dataBin.lock();
            int numCachedPackets = dataBin.getNumCompletePackets();
            if (relevantPrecinct.endLayer <= numCachedPackets) {
                sendDataInfo = new SendDataInfo(SendDataInfo.PRECINCT, inClassIdentifier);
                sendDataInfo.layersOffset = relevantPrecinct.startLayer;
                sendDataInfo.layersLength = relevantPrecinct.endLayer - relevantPrecinct.startLayer;
                sendDataInfo.bytesOffset = (int) relevantPrecinct.msgOffset;
                sendDataInfo.bytesLength = (int) relevantPrecinct.msgLength;
                availableData.add(sendDataInfo);
            } else if (relevantPrecinct.startLayer >= numCachedPackets) {
                sendDataInfo = new SendDataInfo(SendDataInfo.PRECINCT, inClassIdentifier);
                sendDataInfo.layersOffset = relevantPrecinct.startLayer;
                sendDataInfo.layersLength = relevantPrecinct.endLayer - relevantPrecinct.startLayer;
                sendDataInfo.bytesOffset = (int) relevantPrecinct.msgOffset;
                unAvailableData.add(sendDataInfo);
            } else {
                if (ALLOW_PARTIAL_PRECINCTS) {
                    sendDataInfo = new SendDataInfo(SendDataInfo.PRECINCT, inClassIdentifier);
                    sendDataInfo.layersOffset = relevantPrecinct.startLayer;
                    sendDataInfo.layersLength = numCachedPackets - relevantPrecinct.startLayer;
                    sendDataInfo.bytesOffset = (int) relevantPrecinct.msgOffset;
                    sendDataInfo.bytesLength = (int) (dataBin.getLength() - relevantPrecinct.msgOffset);
                    availableData.add(sendDataInfo);
                    int tmpOffset = sendDataInfo.bytesOffset + sendDataInfo.bytesLength;
                    sendDataInfo = new SendDataInfo(SendDataInfo.PRECINCT, inClassIdentifier);
                    sendDataInfo.layersOffset = numCachedPackets;
                    sendDataInfo.layersLength = relevantPrecinct.endLayer - numCachedPackets;
                    sendDataInfo.bytesOffset = tmpOffset;
                    unAvailableData.add(sendDataInfo);
                } else {
                    sendDataInfo = new SendDataInfo(SendDataInfo.PRECINCT, inClassIdentifier);
                    sendDataInfo.layersOffset = relevantPrecinct.startLayer;
                    sendDataInfo.layersLength = relevantPrecinct.endLayer - relevantPrecinct.startLayer;
                    sendDataInfo.bytesOffset = (int) relevantPrecinct.msgOffset;
                    unAvailableData.add(sendDataInfo);
                }
            }
            dataBin.unlock();
        }
    }

    /**
   *
   * @param viewWindow
   * @param sentData
   *
   * @return
   */
    public void getRemainderData(ProxyCacheModel cacheModel, ArrayList<SendDataInfo> unAvailableData) {
        if (unAvailableData == null) {
            throw new NullPointerException();
        }
        for (SendDataInfo dataInfo : unAvailableData) {
            if (dataInfo.classIdentifier == SendDataInfo.MAIN_HEADER) {
                if (!isComplete(ClassIdentifiers.MAIN_HEADER, 0)) {
                    assert (true);
                }
                MainHeaderDataBin dataBin = (MainHeaderDataBin) getDataBin(DataBin.MAIN_HEADER, 0);
                dataInfo.bytesLength = (int) (dataBin.getLength() - dataInfo.bytesOffset);
                continue;
            } else if (dataInfo.classIdentifier == SendDataInfo.TILE_HEADER) {
                dataInfo.bytesLength = (int) getDatabinLength(ClassIdentifiers.TILE_HEADER, 0) - dataInfo.bytesOffset;
            } else if (dataInfo.classIdentifier == SendDataInfo.PRECINCT) {
                long inClassIdentifier = dataInfo.inClassIdentifier;
                PrecinctDataBin dataBin = (PrecinctDataBin) getDataBin(DataBin.PRECINCT, inClassIdentifier);
                if (dataBin == null) {
                    dataInfo.bytesOffset = 0;
                    dataInfo.bytesLength = 0;
                    dataInfo.layersOffset = 0;
                    dataInfo.layersLength = 0;
                } else {
                    dataBin.lock();
                    int numCachedPackets = dataBin.getNumCompletePackets();
                    if (dataInfo.layersOffset + dataInfo.layersLength <= numCachedPackets) {
                        int endLayer = dataInfo.layersOffset + dataInfo.layersLength;
                        dataInfo.bytesLength = dataBin.getPacketOffset(endLayer) + dataBin.getPacketLength(endLayer) - dataInfo.bytesOffset;
                    } else if (dataInfo.layersOffset < numCachedPackets) {
                        dataInfo.layersLength = numCachedPackets - dataInfo.layersOffset;
                        dataInfo.bytesLength = dataBin.getPacketOffset(numCachedPackets) + dataBin.getPacketLength(numCachedPackets) - dataInfo.bytesOffset;
                    } else {
                        dataInfo.bytesOffset = 0;
                        dataInfo.bytesLength = 0;
                        dataInfo.layersOffset = 0;
                        dataInfo.layersLength = 0;
                    }
                    dataBin.unlock();
                }
            }
        }
    }

    /**
   * Returns the {@link #codestream} attribute.
   *
   * @return definition in {@link #codestream}.
   */
    public final ProxyJPEG2KCodestream getProxyJPEG2KCodestream() {
        return (ProxyJPEG2KCodestream) codestream;
    }

    @Override
    public String toString() {
        String str = "";
        str = getClass().getName() + " [";
        str += super.toString();
        str += "]";
        return str;
    }

    /**
   * Prints this Proxy Logical Target fields out to the specified output
   * stream. This method is useful for debugging.
   *
   * @param out an output stream.
   */
    @Override
    public void list(PrintStream out) {
        out.println("-- Proxy Cache Management --");
        super.list(out);
        out.flush();
    }
}
