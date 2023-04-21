package espider.network.message;

import java.util.HashMap;
import espider.Config;
import espider.libs.file.SpiderFile;

/**
 * @author christophe
 *
 */
public class InitTransfertMessage extends SpiderMessage {

    public InitTransfertMessage() {
    }

    /**
	 * 
	 * @param idFileToDL
	 * @param paquetsSize taille des paquets � transf�rer (en bit)
	 */
    public InitTransfertMessage(SpiderFile fileToDownload) {
        queryData = new HashMap<String, String>();
        setIdFileToDL(fileToDownload.getId());
        setPacketSize(Integer.parseInt(Config.getInstance().getProperty("packet_size")));
    }

    /**
	 * 
	 * @param idFileToDownload
	 * @param packetsize
	 */
    public InitTransfertMessage(long idDownload, long idFileToDownload, int packetsize) {
        queryData = new HashMap<String, String>();
        setIdDownload(idDownload);
        setIdFileToDL(idFileToDownload);
        setPacketSize(packetsize);
    }

    public long getIdFileToDL() {
        return Long.parseLong(queryData.get("idFileToDL"));
    }

    public void setIdFileToDL(long idFileToDL) {
        queryData.put("idFileToDL", String.valueOf(idFileToDL));
    }

    public int getPacketSize() {
        return Integer.parseInt(queryData.get("packetSize"));
    }

    public void setPacketSize(int packetSize) {
        queryData.put("packetSize", String.valueOf(packetSize));
    }

    public long getIdDownload() {
        if (queryData.get("idDownload") != null) return Long.parseLong(queryData.get("idDownload")); else return -1;
    }

    public void setIdDownload(long idDownload) {
        queryData.put("idDownload", String.valueOf(idDownload));
    }

    public long getFileSize() {
        return Long.parseLong(resultData.get("fileSize").toString());
    }

    public void setFileSize(long fileSize) {
        if (resultData == null) resultData = new HashMap<String, Object>();
        resultData.put("fileSize", String.valueOf(fileSize));
    }

    public int getUploadPort() {
        return Integer.parseInt(resultData.get("uploadPort").toString());
    }

    public void setUploadPort(int uploadPort) {
        if (resultData == null) resultData = new HashMap<String, Object>();
        resultData.put("uploadPort", String.valueOf(uploadPort));
    }

    public String getFilename() {
        return resultData.get("filename").toString();
    }

    public void setFilename(String filename) {
        if (resultData == null) resultData = new HashMap<String, Object>();
        resultData.put("filename", filename);
    }

    @Override
    public String getType() {
        return InitTransfertMessage.class.getName();
    }

    public static String getSchema() {
        return "ressources/schemas/initTransfert.xsd";
    }
}
