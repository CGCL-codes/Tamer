package org.privale.node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.LinkedList;
import org.privale.clients.CryptoClient;
import org.privale.clients.EncodeFailedException;
import org.privale.clients.NetworkClient;
import org.privale.utils.ChannelWriter;

public class LocalDownload extends NodeInterfaceConnector {

    protected LocalDownload(NodeAbstract node) {
        Init(node);
        RouteNodes = new LinkedList<PiggybackRouteNodes>();
        ReturnPigRoute = new PiggybackRoute(getNode());
        PigData = new PiggybackData(getNode());
        Date d = new Date();
        CreateTime = d.getTime();
    }

    /**
	 * Build the return piggyback route.
	 * @throws RouteBuildFailedException
	 * @throws IOException
	 * @throws DataDecodeFailedException
	 */
    protected void BuildRoute() throws RouteBuildFailedException, IOException, DataDecodeFailedException {
        ReturnPigRoute.EndNodes = getNode().getCodec().getFirstNodes(ReturnRouteFile);
        ReturnPigRoute.ForwardToEndNodes = false;
        ReturnPigRoute.ProbOfForwardToOne = 0;
        RouteNodes tn = new RouteNodes(getNode());
        tn.getNodes().add(getNode().getThisNode());
        ReturnPigRoute.StartNodes = tn;
        UserRouteConfig conf = getNode().getConfig().DownloadReturnPiggybackConfig;
        conf.MaxNumberHops = getNode().getNetworkClientByID(NetworkID).getNumberDownPigsRequired();
        conf.MinNumberHops = Math.min(conf.MaxNumberHops, conf.MinNumberHops);
        if (conf.MaxNumberHops > RouteNodes.size()) {
            throw new RouteBuildFailedException("ERROR: Requestor did not supply enough piggyback pairs for the return route!");
        }
        ReturnPigRoute.UserConfig = conf;
        conf.AllowedCryptoClients = new LinkedList<Long>();
        conf.AllowedCryptoClients.add(PiggybackCrypto);
        getNode().getDataBase().BuildRoute(ReturnPigRoute);
        AddEncodes();
    }

    /**
	 * Once the return piggyback route is built we need to
	 * add the encode data from the download packet.  This
	 * is so we don't have to dispatch encode/key generators
	 * to our local crypto client.
	 */
    private void AddEncodes() {
        for (int cnt = 0; cnt < ReturnPigRoute.getNodes().size() - 1; cnt++) {
            PiggybackRouteNodes rn = (PiggybackRouteNodes) ReturnPigRoute.getNodes().get(cnt);
            rn.PrivateEncodeKeys.add(RouteNodes.get(cnt).PrivateEncodeKeys.get(0));
            rn.PublicEncodeKeys.add(RouteNodes.get(cnt).PublicEncodeKeys.get(0));
        }
        int last = ReturnPigRoute.getNodes().size() - 1;
        PiggybackRouteNodes rn = (PiggybackRouteNodes) ReturnPigRoute.getNodes().get(last);
        for (int cnt = last; cnt < RouteNodes.size(); cnt++) {
            rn.PrivateEncodeKeys.add(RouteNodes.get(cnt).PrivateEncodeKeys.get(0));
            rn.PublicEncodeKeys.add(RouteNodes.get(cnt).PublicEncodeKeys.get(0));
        }
    }

    /**
	 * Enocde the return route.  Note: we don't have to dispatch
	 * any key generators, the source did it for us!
	 * @throws EncodeFailedException 
	 * @throws IOException 
	 */
    protected File EncodeReturn() throws EncodeFailedException, IOException {
        CryptoClient c = getNode().getCryptoClientByID(PiggybackCrypto);
        File tmpf = getNode().getTempFM().createNewFile("encodereturn", "download");
        ChannelWriter cw = new ChannelWriter(tmpf);
        cw.putLongFile(DownloadData);
        cw.close();
        File encdata = c.RawEncode(tmpf, RawKey);
        File pigdata = PigData.EncodeData(encdata);
        File pigroute = ReturnPigRoute.EncodeData(ReturnRouteFile);
        FileOutputStream fos = new FileOutputStream(pigroute, true);
        FileChannel foc = fos.getChannel();
        FileInputStream fis = new FileInputStream(pigdata);
        FileChannel fic = fis.getChannel();
        fic.transferTo(0, fic.size(), foc);
        foc.close();
        fic.close();
        pigdata.delete();
        ReturnRouteFile.delete();
        encdata.delete();
        return pigroute;
    }

    protected boolean isExpired(long curtime) {
        return (CreateTime + getNode().getConfig().LocalNetworkTimeOut < curtime);
    }

    protected void Cancel() {
        NetworkClient c = getNode().getNetworkClientByID(NetworkID);
        c.CancelDownload(this);
        if (DownloadFile != null) {
            DownloadFile.delete();
        }
        if (DownloadData != null) {
            DownloadData.delete();
        }
        if (PostData != null) {
            PostData.delete();
        }
        if (ReturnRouteFile != null) {
            ReturnRouteFile.delete();
        }
    }

    private long CreateTime;

    /**
	 * The network client id.
	 */
    protected long NetworkID;

    /**
	 * The depot client to use for the piggyback route.
	 */
    protected long PiggybackDepot;

    /**
	 * The crypto client to use for the piggyback route.
	 */
    protected long PiggybackCrypto;

    /**
	 * The return route file.
	 */
    protected File ReturnRouteFile;

    /**
	 * The raw encode key for the return data.
	 */
    protected ByteBuffer RawKey;

    /**
	 * The return piggyback route. We're going to build.
	 */
    protected PiggybackRoute ReturnPigRoute;

    /**
	 * The PiggybackData to build the hiding return
	 * piggyback packet with.
	 */
    protected PiggybackData ReturnPigData;

    /**
	 * The PiggybackRouteNodes
	 */
    protected LinkedList<PiggybackRouteNodes> RouteNodes;

    /**
	 * The PublicEncodeKeys from the download command
	 */
    protected PiggybackData PigData;

    /**
	 * The download instructions for the network client.
	 */
    public File DownloadFile;

    /**
	 * Data that the requestor has appended to the download
	 * request.
	 */
    public File PostData;

    /**
	 * Network client set this when the download is complete.
	 */
    public boolean DownloadComplete;

    /**
	 * The file containing the downloaded data.
	 */
    public File DownloadData;
}
