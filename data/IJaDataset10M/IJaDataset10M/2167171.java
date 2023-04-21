package ces.platform.infoplat.service.syn;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.net.ftp.FTPClient;
import ces.coral.dbo.DBOperation;
import ces.coral.lang.StringUtil;
import ces.platform.infoplat.core.Channel;
import ces.platform.infoplat.core.Site;
import ces.platform.infoplat.core.base.BaseDAO;
import ces.platform.infoplat.core.base.ConfigInfo;
import ces.platform.infoplat.core.dao.ChannelDAO;
import ces.platform.infoplat.core.tree.TreeNode;

/**
 * <p>Title: ������Ϣƽ̨:վ��ͬ����</p>
 * <p>Description: �����ڲ�վ��ͬ��������,������ݷ��ص��ڲ���վ</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: �Ϻ�������Ϣ��չ���޹�˾</p>
 * @author ����
 * @version 2.5
 */
public class SiteSyn extends BaseDAO {

    private Map localFileList = new Hashtable();

    private Map remoteFileList = new Hashtable();

    private static String propertiesFileName = "rmanifest.properties";

    private static String synFileName = "smanifest.lst";

    private String ftpServerIp;

    private int ftpPort = -1;

    private String ftpLoginUser;

    private String ftpPassword;

    private String ftpRootPath;

    private String synInfoplatExportClassName;

    private String synOtherExportClassName;

    private String localRootPath;

    public SiteSyn() {
    }

    /**
	 * ͬ��һ��վ�㵽����,��Ҫ�����漸������:<br>
	 * 1.����Ҫͬ����վ�����Ƶ������������ļ�,�ŵ�Hashmap����Hashtable��<br>
	 * 2.�ж��Ƿ�����ͬ��,�������ȫͬ��,��ô,�ж���û��rmanifest.properties�ļ�<br>
	 *     ���û��:��ôȫ���ϴ�<br>
	 *     �����:���ļ�rmanifest.properties�е����ӳ���Hashmap��<br>
	 *           �Ƚ��������,��Ҫͬ�����ļ��ҳ���<br>
	 *   ���������ͬ��,��ô,ȫ���ϴ�
	 * 3.����ЩҪ�ϴ����ļ��������Ӧ��Ŀ¼�ṹ,�ϴ��������������<br>
	 * 4.����sql���,��sql������������,������Щsql���ִ��,�ﵽ��ݿ�����ͬ��
	 * @param siteChannelPath Ҫͬ���������������վ��Ƶ����·��
	 * @param isIncSyn �Ƿ�����ͬ��
	 * @throws java.lang.Exception
	 */
    public synchronized void toOutSite(String siteChannelPath, boolean isIncSyn) throws Exception {
        localFileList.clear();
        remoteFileList.clear();
        checkParam();
        if (siteChannelPath == null || siteChannelPath.length() < 10) {
            throw new Exception("ͬ������:��Ч��ͬ��վ��Ƶ��path(" + siteChannelPath + ")");
        }
        exportSynData(siteChannelPath);
        boolean isSiteSyn = siteChannelPath.length() == 10 ? true : false;
        localRootPath = ConfigInfo.getInstance().getInfoplatDataDir() + File.separator + "pub" + File.separator;
        String siteDir = ConfigInfo.getInstance().getInfoplatDataDir() + File.separator + "pub" + File.separator;
        TreeNode tn = TreeNode.getInstance(siteChannelPath);
        if (tn == null) {
            throw new Exception("ͬ������,û���ҵ�pathΪ" + siteChannelPath + "��վ��Ƶ��,�������!");
        }
        String channelAsciiNamem = null;
        if (isSiteSyn) {
            Site site = null;
            try {
                site = (Site) tn;
            } catch (Exception ex) {
                log.error("path=" + siteChannelPath + "��ʵ����һ��վ������!", ex);
                throw new Exception("path=" + siteChannelPath + "��ʵ����һ��վ������!");
            }
            localRootPath += site.getAsciiName();
            siteDir += site.getAsciiName();
            channelAsciiNamem = site.getAsciiName();
        } else {
            Channel channel = null;
            try {
                channel = (Channel) tn;
            } catch (Exception ex) {
                log.error("path=" + siteChannelPath + "��ʵ����һ��Ƶ������!", ex);
                throw new Exception("path=" + siteChannelPath + "��ʵ����һ��Ƶ������!");
            }
            localRootPath += channel.getSiteAsciiName() + File.separator + channel.getAsciiName();
            siteDir += channel.getSiteAsciiName();
            channelAsciiNamem = channel.getSiteAsciiName();
        }
        localRootPath = new File(localRootPath).getPath();
        if (!new File(localRootPath).exists()) {
            throw new Exception("ͬ������:��վ�����Ƶ��(path=" + siteChannelPath + ")û�з���!");
        }
        log.debug("Ҫͬ����Ŀ¼:" + localRootPath);
        log.debug("��ʼ��ȡ���ص��ļ��б?");
        if (isSiteSyn) {
            readLocalFileList(localRootPath);
        } else {
            readLocalFileList(siteChannelPath, siteDir);
            localRootPath = siteDir;
        }
        log.debug("��ȡ���ص��ļ��б����");
        log.debug("��ʼ��ȡ�Ѿ�ͬ�����ļ��嵥��");
        readRemoteFileList(new File(siteDir + File.separator + propertiesFileName).getPath());
        log.debug("��ȡ�Ѿ�ͬ�����ļ��嵥����");
        log.debug("��ʼ�õ���Ҫͬ�����ļ��嵥��");
        Map synFileList = getSynFileList(siteDir);
        log.debug("�õ���Ҫͬ�����ļ��嵥����");
        log.debug("��ʼ���ļ���" + synFileList);
        this.uploadFileToWebSite(siteDir, channelAsciiNamem, synFileList);
        log.debug("�����ļ���");
        log.debug("��ʼ����rmanifest.properties�ļ���");
        if (null != synFileList && !synFileList.isEmpty()) updateSynProperties(new File(siteDir + File.separator + propertiesFileName).getPath(), synFileList);
        log.debug("����rmanifest.properties�ļ�����");
    }

    /**
	 * �õ�����Ҫͬ�����ļ��б�
	 * ���һ��·��,�õ���·����������е��ļ����б�,����HashMap��<br>
	 * key:�ļ���ȫ·��,value:�ļ�������޸�ʱ��
	 * @param dir Ҫͬ����վ�����Ƶ���ĸ�·��
	 */
    private void readLocalFileList(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File strTemp[] = file.listFiles();
            for (int i = 0; strTemp != null && i < strTemp.length; i++) {
                file = null;
                readLocalFileList(strTemp[i].getPath());
            }
        } else {
            if (!file.getName().equalsIgnoreCase(propertiesFileName)) {
                localFileList.put(file.getPath(), String.valueOf(file.lastModified()));
            }
        }
        file = null;
    }

    /**
	 * Ƶ��ͬ��ʱ��Ƶ��������Ƶ���µ��ĵ�Ҳͬ��
	 * ������Դ
	 * @param channelPath	Ƶ��Path
	 * @param siteDir		Ƶ��վ��·��
	 * @throws Exception 
	 */
    private void readLocalFileList(String channelPath, String siteDir) throws Exception {
        DBOperation dbo = null;
        Connection connection = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            dbo = createDBOperation();
            String sqlStr = "select CHANNEL_PATH, ASCII_NAME from T_IP_CHANNEL " + "where CHANNEL_PATH like '" + channelPath + "%' " + "or CHANNEL_PATH in (select CHANNELPATHRELATING from T_IP_CHANNEL_RELATING where CHANNELPATH = '" + channelPath + "') " + "or CHANNEL_PATH in (select RELATIVE_PAGE from T_IP_CHANNEL_RELATING where CHANNELPATH = '" + channelPath + "') ";
            connection = dbo.getConnection();
            stat = connection.createStatement();
            rs = stat.executeQuery(sqlStr);
            if (rs != null) {
                String sitePath = channelPath.substring(0, 10);
                String rPath, ascName, sTemp = "";
                while (rs.next()) {
                    rPath = rs.getString("CHANNEL_PATH");
                    ascName = rs.getString("ASCII_NAME") == null ? "" : rs.getString("ASCII_NAME");
                    if (rPath.startsWith(sitePath)) {
                        sTemp = siteDir + File.separator + ascName;
                        readLocalFileList(sTemp);
                    }
                }
            }
            sqlStr = "select DOC_ID, CREATE_DATE from T_IP_BROWSE " + "where CHANNEL_PATH like '" + channelPath + "%' " + "and LASTEST_MODIFY_DATE > sysdate-1 ";
            rs = stat.executeQuery(sqlStr);
            if (rs != null) {
                SimpleDateFormat sDatef = new SimpleDateFormat("yyyyMM");
                String sdocIds = "", sdocId, createD = "";
                HashMap hmDocCreate = new HashMap();
                Date cDate;
                String sFilePath = "";
                File file = null;
                while (rs.next()) {
                    sdocId = rs.getString("DOC_ID");
                    cDate = rs.getDate("CREATE_DATE");
                    if (cDate != null) {
                        createD = sDatef.format(cDate);
                        if (createD != null) {
                            sFilePath = siteDir + File.separator + "docs" + File.separator + createD + File.separator + "d_" + sdocId + ".html";
                            file = new File(sFilePath);
                            if (file.exists()) {
                                if (!file.getName().equalsIgnoreCase(propertiesFileName)) {
                                    localFileList.put(file.getPath(), String.valueOf(file.lastModified()));
                                }
                                hmDocCreate.put(sdocId, createD);
                                sdocIds += ",'" + sdocId + "'";
                                file = null;
                            }
                        }
                    }
                }
                if (!"".equals(sdocIds)) {
                    String sUri = "";
                    sdocIds = sdocIds.substring(1);
                    sqlStr = "select URI, DOC_ID from T_IP_DOC_RES where TYPE in ('1','2','3') and DOC_ID in (" + sdocIds + ")";
                    rs = stat.executeQuery(sqlStr);
                    if (rs != null) {
                        while (rs.next()) {
                            sUri = rs.getString("URI");
                            if (sUri != null && !"".equals(sUri)) {
                                sdocId = rs.getString("DOC_ID");
                                createD = hmDocCreate.get(sdocId).toString();
                                sFilePath = siteDir + File.separator + "docs" + File.separator + createD + File.separator + "res" + File.separator + sUri;
                                file = new File(sFilePath);
                                if (file.exists()) {
                                    if (!file.getName().equalsIgnoreCase(propertiesFileName)) {
                                        localFileList.put(file.getPath(), String.valueOf(file.lastModified()));
                                    }
                                    file = null;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Ƶ��ͬ�����?��", ex);
            throw new Exception("Ƶ��ͬ�����?��" + ex.getMessage());
        } finally {
            close(rs, stat, null, connection, dbo);
        }
    }

    /**
	 * ���rmanifest.properties�ļ��õ��Ѿ�ͬ������ļ��嵥
	 * ����HashMap��<br>
	 * key:�ļ���ȫ·��,value:�ļ�������޸�ʱ��
	 * @param dir rmanifest.properties�ļ����ڵ�ȫ·��
	 */
    private void readRemoteFileList(String dir) throws IOException {
        File file = null;
        InputStream is = null;
        try {
            file = new File(dir);
            if (!file.exists()) {
                return;
            }
            is = new FileInputStream(file);
            Properties p = new Properties();
            p.load(is);
            Iterator lit = p.keySet().iterator();
            while (lit.hasNext()) {
                String key = (String) lit.next();
                String value = (String) p.getProperty(key);
                remoteFileList.put(key, value);
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (is != null) {
                is.close();
            }
            file = null;
        }
    }

    /**
	 * �Ƚϱ����ļ��б����ͬ���ļ��б�,�õ���Ҫͬ�����ļ��б�
	 * @param siteDir վ���·��
	 * @return Ҫ�������ļ����б�,Map����,key:��ȫ·�����ļ���,value:����޸�ʱ��
	 */
    private Map getSynFileList(String siteDir) {
        Map result = new Hashtable();
        if (localFileList == null || remoteFileList == null || siteDir == null) {
            return result;
        }
        siteDir = new File(siteDir).getPath() + File.separator;
        Iterator lit = localFileList.keySet().iterator();
        while (lit.hasNext()) {
            String key = (String) lit.next();
            if (key == null) {
                continue;
            }
            String localLastModifyTime = (String) localFileList.get(key);
            String remoteLsatModityTime = (String) remoteFileList.get(key.substring(siteDir.length()));
            if (localLastModifyTime != null && !localLastModifyTime.equalsIgnoreCase(remoteLsatModityTime)) {
                result.put(key.substring(siteDir.length()), localLastModifyTime);
            }
        }
        return result;
    }

    /**
	 * ��ݴ����Ĳ�������ļ�����
	 * ������Ҫ�Ǹ���ͬ�������ļ�(rmanifest.properties)
	 * @param dir �����ļ�����Ŀ¼��ȫ·��
	 * @param synFileList Ҫ���µ���ݣ����ͬ������ļ��嵥
	 */
    private void updateSynProperties(String dir, Map synFileList) {
        if (dir == null || synFileList == null || synFileList.isEmpty()) {
            return;
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            File file = new File(dir);
            if (!file.exists()) {
                new File(dir).createNewFile();
            }
            is = new FileInputStream(file);
            Properties p = new Properties();
            p.load(is);
            p.putAll(synFileList);
            os = new FileOutputStream(new File(dir));
            p.store(os, null);
        } catch (IOException ex) {
            log.error("д���ļ�:" + dir + "����!");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex1) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex2) {
                }
            }
        }
    }

    /**
	 * ����Ҫͬ�����ļ��ϴ���������
	 * @param synFileList
	 */
    private void uploadFileToWebSite(String siteDir, String channelAsciiName, Map synFileList) throws Exception {
        if (siteDir == null) {
            siteDir = "";
        }
        log.debug("uploadFileToWebSite begin! siteDir:= " + siteDir + "  currDate:= " + new Date().toString());
        siteDir = new File(siteDir).getPath() + File.separator;
        FTPClient client = new FTPClient();
        try {
            for (int i = 0; i < 3; i++) {
                try {
                    client.connect(ftpServerIp, ftpPort);
                    break;
                } catch (IOException ex2) {
                    if (i == 2) {
                        log.error("ftp����������ʧ��,�Ѿ�����3��!", ex2);
                        throw new IOException("ftp����������ʧ��,�Ѿ�����3��!" + ex2.toString());
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                try {
                    client.login(ftpLoginUser, ftpPassword);
                    break;
                } catch (IOException ex3) {
                    if (i == 2) {
                        log.error("��¼ftp������ʧ��,�Ѿ�����3��!", ex3);
                        throw new IOException("��¼ftp������ʧ��,�Ѿ�����3��!" + ex3.toString());
                    }
                }
            }
            log.debug("Ftp login is over !");
            client.syst();
            String ftpWD = client.printWorkingDirectory();
            log.debug("client.initiateListParsing() is over !");
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            Iterator iterator = synFileList.keySet().iterator();
            ArrayList alKey = new ArrayList();
            while (iterator.hasNext()) {
                alKey.add((String) iterator.next());
            }
            log.debug("FTP Files size:= " + alKey.size());
            String basePath = ftpRootPath + (channelAsciiName == null || channelAsciiName.trim().equals("") ? "" : File.separator + channelAsciiName);
            log.debug("localRootPath:= " + localRootPath + " basePath:= " + basePath);
            String path;
            boolean isSuc;
            String sFileSep = File.separator;
            String sRep = "";
            if (basePath.startsWith("/")) {
                sFileSep = "/";
                sRep = "\\";
            } else if (basePath.startsWith("\\")) {
                sFileSep = "\\";
                sRep = "/";
            }
            if (!"".equals(sRep)) {
                basePath = StringUtil.replaceAll(basePath, sRep, sFileSep);
                while (basePath.startsWith(sFileSep)) basePath = basePath.substring(1);
            }
            for (int j = 0; j < alKey.size(); j++) {
                String key = (String) alKey.get(j);
                File file = new File(siteDir + key);
                String filePath = file.getParent();
                String fileName = file.getName();
                if (fileName == null || filePath == null || !file.exists() || filePath.length() < localRootPath.length()) {
                    continue;
                }
                filePath = filePath.substring(localRootPath.length());
                FileInputStream fis = null;
                String temp1;
                ArrayList alTemp;
                int iInd;
                try {
                    path = basePath + (filePath == null || filePath.trim().equals("") || filePath.equals(File.separator) ? "" : File.separator + filePath);
                    if (!"".equals(sRep)) {
                        path = StringUtil.replaceAll(path, sRep, sFileSep);
                    }
                    if (!client.changeWorkingDirectory(path)) {
                        isSuc = client.makeDirectory(path);
                        if (isSuc) {
                            log.debug(" **** makeDirectory1(" + path + "): " + isSuc);
                        } else {
                            temp1 = path;
                            alTemp = new ArrayList();
                            iInd = temp1.lastIndexOf(sFileSep);
                            alTemp.add(temp1.substring(iInd));
                            temp1 = temp1.substring(0, iInd);
                            isSuc = client.makeDirectory(temp1);
                            if (isSuc) {
                                log.debug(" **** makeDirectory2(" + temp1 + "): " + isSuc);
                            }
                            while (!"".equals(temp1) && !isSuc) {
                                iInd = temp1.lastIndexOf(sFileSep);
                                alTemp.add(temp1.substring(iInd));
                                temp1 = temp1.substring(0, iInd);
                                isSuc = client.makeDirectory(temp1);
                                if (isSuc) {
                                    log.debug(" **** makeDirectory3(" + temp1 + "): " + isSuc);
                                }
                            }
                            for (int i = alTemp.size(); i > 0; i--) {
                                temp1 += alTemp.get(i - 1);
                                isSuc = client.makeDirectory(temp1);
                                log.debug(" **** makeDirectory4(" + temp1 + "): " + isSuc);
                            }
                        }
                        client.changeWorkingDirectory(path);
                    }
                    fis = new FileInputStream(file);
                    client.storeFile(fileName, fis);
                    client.changeWorkingDirectory(ftpWD);
                } catch (Throwable ex1) {
                    log.error("ͬ���ļ�����:������ļ�Ϊ:" + file.getPath());
                    ex1.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (RuntimeException e1) {
                        log.error("close()����!");
                        e1.printStackTrace();
                    }
                    file = null;
                }
            }
        } catch (Throwable ex) {
            log.error("ͬ��ʧ��--1202!", ex);
            ex.printStackTrace();
        } finally {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        }
    }

    /**
	 * �������Ƿ���������
	 * @return
	 */
    private void checkParam() throws Exception {
        if (this.ftpServerIp == null) {
            throw new Exception("û������ftp��������ip��ַ(�����ʶ)!");
        }
        if (this.ftpPort == -1) {
            throw new Exception("û������ftp�������Ķ˿ں�!");
        }
        if (this.ftpLoginUser == null) {
            throw new Exception("û������ftp�������ĵ�¼�û���!");
        }
        if (this.ftpPassword == null) {
            throw new Exception("û������ftp�������ĵ�¼����!");
        }
        if (this.ftpRootPath == null) {
            throw new Exception("û�����õ�ǰ��¼ftp���������û��ĵ�ǰĿ¼!");
        }
    }

    /**
	 * ����������Ҫ�����
	 * @param site
	 */
    private void exportSynData(String siteChannelPath) {
        if (siteChannelPath == null || siteChannelPath.length() < 10) {
            return;
        }
        try {
            Site site = (Site) TreeNode.getInstance(siteChannelPath.substring(0, 10));
            if (site == null) {
                return;
            }
            ExportSynData esd = null;
            try {
                if (synInfoplatExportClassName != null && !synInfoplatExportClassName.trim().equals("")) {
                    esd = (ExportSynData) Class.forName(synInfoplatExportClassName).newInstance();
                    esd.export(site);
                }
            } catch (Exception ex1) {
                log.error("ͬ��ʱ����:��Ϣƽ̨��ݵ���ʱʧ��!", ex1);
            }
            try {
                if (synOtherExportClassName != null && !synOtherExportClassName.trim().equals("")) {
                    esd = (ExportSynData) Class.forName(synOtherExportClassName).newInstance();
                    esd.export(site);
                }
            } catch (Exception ex1) {
                log.error("ͬ��ʱ����:ҵ����ݵ���ʱʧ��!", ex1);
            }
        } catch (Exception ex) {
            log.error("ͬ��ʱ��ݵ���ʧ��!", ex);
        }
    }

    public static void main(String[] a) throws Exception {
        SiteSyn s = new SiteSyn();
        String siteChannelPath = "0000004202";
        Site site = (Site) TreeNode.getInstance(siteChannelPath);
        ConfigInfo ci = ConfigInfo.getInstance();
        s.setFtpLoginUser(ci.getSynFtpLoginUser(site.getSiteID()));
        s.setFtpPassword(ci.getSynFtpPassword(site.getSiteID()));
        s.setFtpPort(ci.getSynFtpPort(site.getSiteID()));
        s.setFtpServerIp(ci.getSynFtpServer(site.getSiteID()));
        s.setFtpRootPath(ci.getSynFtpRootPath(site.getSiteID()));
        s.setSynInfoplatExportClassName(ci.getSynInfoplatExportClassName(site.getSiteID()));
        s.setSynOtherExportClassName(ci.getSynOtherExportClassName(site.getSiteID()));
        boolean isIncSyn = true;
        s.toOutSite(siteChannelPath, true);
    }

    /**
	 * һ���Get/Set����
	 */
    public void setFtpLoginUser(String ftpLoginUser) {
        this.ftpLoginUser = ftpLoginUser;
    }

    /**
	 * һ���Get/Set����
	 */
    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    /**
	 * һ���Get/Set����
	 */
    public void setFtpRootPath(String ftpRootPath) {
        this.ftpRootPath = ftpRootPath;
    }

    /**
	 * һ���Get/Set����
	 */
    public void setFtpServerIp(String ftpServerIp) {
        this.ftpServerIp = ftpServerIp;
    }

    /**
	   * һ���Get/Set����
	   */
    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    /**
	 * һ���Get/Set����
	 */
    public void setSynInfoplatExportClassName(String synInfoplatExportClassName) {
        this.synInfoplatExportClassName = synInfoplatExportClassName;
    }

    /**
	 * һ���Get/Set����
	 */
    public void setSynOtherExportClassName(String synOtherExportClassName) {
        this.synOtherExportClassName = synOtherExportClassName;
    }

    /**
	 * ���ý�Ҫͬ�����ļ�ѹ����ͬ����ͬ��������Dos���������
	 * �˴������������:<br>
	 * 1.����Ҫͬ����վ�����Ƶ������������ļ�,�ŵ�Hashmap����Hashtable��<br>
	 * 2.�ж���û��rmanifest.properties�ļ�,���û��:��ôȫ���ϴ�<br>
	 *     �����:���ļ�rmanifest.properties�е����ӳ���Hashmap��<br>
	 * 3.�Ƚ��������,��Ҫͬ�����ļ��ҳ���������ļ��嵥�б�smanifest.lst,<br>
	 * ����վ�����ͬһ���ļ���,���ļ�Ŀ¼���pub��<br>
	 * 4.�����ļ��б�rmanifest.properties<br>
	 * @param siteChannelPath Ҫͬ���������������վ���·��
	 * @param isClear Ҫͬ���������������վ���·��
	 * @throws java.lang.Exception
	 */
    public synchronized void createOutSiteFileList(String siteChannelPath, boolean isClear) throws Exception {
        localFileList.clear();
        remoteFileList.clear();
        if (siteChannelPath == null || siteChannelPath.length() != 10) {
            throw new Exception("ͬ������:��Ч��ͬ��վ��path(" + siteChannelPath + ")");
        }
        localRootPath = ConfigInfo.getInstance().getInfoplatDataDir() + File.separator + "pub" + File.separator;
        String siteDir = ConfigInfo.getInstance().getInfoplatDataDir() + File.separator + "pub" + File.separator;
        TreeNode tn = TreeNode.getInstance(siteChannelPath);
        if (tn == null) {
            throw new Exception("ͬ������,û���ҵ�pathΪ" + siteChannelPath + "��վ��,�������!");
        }
        String channelAsciiNamem = null;
        Site site = null;
        try {
            site = (Site) tn;
        } catch (Exception ex) {
            log.error("path=" + siteChannelPath + "��ʵ����һ��վ������!", ex);
            throw new Exception("path=" + siteChannelPath + "��ʵ����һ��վ������!");
        }
        localRootPath += site.getAsciiName();
        siteDir += site.getAsciiName();
        localRootPath = new File(localRootPath).getPath();
        if (!new File(localRootPath).exists()) {
            throw new Exception("ͬ������:��վ�����Ƶ��(path=" + siteChannelPath + ")û�з���!");
        }
        log.debug("Ҫͬ����Ŀ¼:" + localRootPath);
        log.debug("��ʼ��ȡ���ص��ļ��б?");
        readLocalFileList(localRootPath);
        log.debug("��ȡ���ص��ļ��б����");
        log.debug("��ʼ��ȡ�Ѿ�ͬ�����ļ��嵥��");
        readRemoteFileList(new File(siteDir + File.separator + propertiesFileName).getPath());
        log.debug("��ȡ�Ѿ�ͬ�����ļ��嵥����");
        log.debug("��ʼ�õ���Ҫͬ�����ļ��嵥��");
        Map synFileList = getSynFileList(siteDir);
        log.debug("�õ���Ҫͬ�����ļ��嵥����");
        log.debug("��ʼ����smanifest.txt�ļ���");
        createSynList(ConfigInfo.getInstance().getInfoplatDataDir() + "pub" + File.separator + synFileName, site.getAsciiName(), synFileList, isClear);
        log.debug("����smanifest.txt�ļ�����");
        log.debug("��ʼ����rmanifest.properties�ļ���");
        if (null != synFileList && !synFileList.isEmpty()) updateSynProperties(new File(siteDir + File.separator + propertiesFileName).getPath(), synFileList);
        log.debug("����rmanifest.properties�ļ�����");
    }

    /**
	 * ��ݴ����Ĳ����ļ�����
	 * ������Ҫ�Ǵ���ͬ�������ļ�(smanifest.txt)
	 * @param dir �����ļ�����Ŀ¼��ȫ·��
	 * @param synFileList Ҫ��������ݣ����ͬ������ļ��嵥
	 * @param isClear �Ƿ�����ļ��б��嵥
	 */
    private void createSynList(String dir, String asciiName, Map synFileList, boolean isClear) {
        BufferedOutputStream os = null;
        try {
            File file = new File(dir);
            if (!file.exists()) {
                new File(dir).createNewFile();
                os = new BufferedOutputStream(new FileOutputStream(file.getPath(), true));
                os.write("smanifest.lst\n".getBytes());
            } else if (isClear) {
                file.delete();
                new File(dir).createNewFile();
                os = new BufferedOutputStream(new FileOutputStream(file.getPath(), true));
                os.write("smanifest.lst\n".getBytes());
            } else {
                os = new BufferedOutputStream(new FileOutputStream(file.getPath(), true));
            }
            if (dir == null || synFileList == null || synFileList.isEmpty()) {
                return;
            }
            Iterator iterator = synFileList.keySet().iterator();
            String sTemp;
            while (iterator.hasNext()) {
                sTemp = asciiName + "\\" + iterator.next().toString() + "\n";
                os.write(sTemp.getBytes());
            }
        } catch (IOException ex) {
            log.error("д���ļ�:" + dir + "����!");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex2) {
                }
            }
        }
    }
}
