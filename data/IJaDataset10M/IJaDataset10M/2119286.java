package com.pccw.portlet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class DocumentOperationUtil {

    private static String COMMON_TEMP_DIR_ROOT = "upload";

    private static int commonTempDirRemainHours = 24;

    static {
        String commonTempDirRemainHoursStr = "";
        if (commonTempDirRemainHoursStr != null && !"".equals(commonTempDirRemainHoursStr.trim())) {
            try {
                int hours = Integer.parseInt(commonTempDirRemainHoursStr);
                if (hours >= 1) {
                    commonTempDirRemainHours = hours;
                }
            } catch (Exception e) {
            }
        }
        if (COMMON_TEMP_DIR_ROOT == null || "".equals(COMMON_TEMP_DIR_ROOT.trim())) {
            COMMON_TEMP_DIR_ROOT = "/temp_dir";
        }
        try {
            File tempDir = new File(COMMON_TEMP_DIR_ROOT);
            if (tempDir != null && !tempDir.exists()) {
                tempDir.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=== In DocumentOperationUtil, create temp dir root failed(\"" + COMMON_TEMP_DIR_ROOT + "\") ===");
        }
    }

    /**
	 * ����һ����ʱ��Ŀ¼����������ȫ·��
	 */
    public static synchronized String createTempDir() {
        boolean allExpiredTempDirDeleted = delAllExpiredTempDir();
        return createTempSubDir(COMMON_TEMP_DIR_ROOT);
    }

    /**
	 * ɾ�����й��ڵ���ʱĿ¼�����е�������Ŀ¼���ļ�
	 */
    public static synchronized boolean delAllExpiredTempDir() {
        boolean flag = false;
        flag = delTempSubDirBeforeHours(COMMON_TEMP_DIR_ROOT, commonTempDirRemainHours);
        return flag;
    }

    /**
	 * ��ָ����Ŀ¼�д�����ʱ��Ŀ¼
	 */
    public static synchronized String createTempSubDir(String parentDir) {
        if (parentDir == null || "".equals(parentDir.trim())) {
            return null;
        }
        if ((!parentDir.endsWith("/")) && (!parentDir.endsWith(File.separator))) {
            parentDir = parentDir + "/";
        }
        File dirFile = null;
        String tempDir = null;
        boolean dirCreated = false;
        for (int i = 0; i < 5; i++) {
            tempDir = parentDir + "tmp" + System.currentTimeMillis();
            dirFile = new File(tempDir);
            if (dirFile.mkdir() == true) {
                dirCreated = true;
                break;
            } else {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (dirCreated) {
            if ((!tempDir.endsWith("/")) && (!tempDir.endsWith(File.separator))) {
                tempDir = tempDir + "/";
            }
            return tempDir;
        } else {
            return null;
        }
    }

    /**
	 * ɾ��ָ��Ŀ¼�й��ڵ���ʱ��Ŀ¼��ָ����Сʱ��֮ǰ�����ģ������е�������Ŀ¼���ļ�
	 */
    public static boolean delTempSubDirBeforeHours(String parentDir, int hours) {
        boolean flag = false;
        if (parentDir == null || "".equals(parentDir.trim()) || hours < 0) {
            return false;
        }
        if ((!parentDir.endsWith("/")) && (!parentDir.endsWith(File.separator))) {
            parentDir = parentDir + "/";
        }
        long currentTime = System.currentTimeMillis();
        File exportDirFile = new File(parentDir);
        String tempDirName = null;
        File[] subTempDirs = exportDirFile.listFiles();
        for (int i = 0; subTempDirs != null && i < subTempDirs.length; i++) {
            if (subTempDirs[i].isDirectory()) {
                tempDirName = subTempDirs[i].getName();
                if (tempDirName.startsWith("tmp")) {
                    String timeStr = tempDirName.substring(3);
                    try {
                        long dirCreateTime = Long.parseLong(timeStr);
                        long timeLimit = hours * 3600000;
                        if (currentTime - dirCreateTime > timeLimit) {
                            flag = delDirAndAllFiles(parentDir + tempDirName);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        return flag;
    }

    /**
	 * ɾ��ָ������ʱĿ¼�����е�������Ŀ¼���ļ�
	 */
    public static boolean delDirAndAllFiles(String path) {
        boolean flag = false;
        File file = new File(path);
        if (file == null || file.exists() == false) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                flag = delDirAndAllFiles(files[i].getAbsolutePath());
                if (flag == false) {
                    return false;
                }
            }
            flag = file.delete();
        }
        return flag;
    }

    /**
	 * ɾ��ָ�����ļ�
	 */
    public static boolean delFile(File file) {
        boolean flag = false;
        if (file == null || file.exists() == false) {
            return true;
        }
        if (file.isFile()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
	 * ɾ��ָ�����ļ�
	 */
    public static boolean delFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        if (file == null) {
            return true;
        }
        if (file.exists() == false) {
            return true;
        }
        if (file.isFile()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
	 * ��ָ�����ļ����浽�����ϣ���ָ���ļ�ȫ·�����ļ���
	 */
    public static File writeFileToDisk(File file, String filePath) {
        filePath = addDateTimeStringToEndOfFileName(filePath);
        if (file == null || filePath == null || "".equals(filePath.trim())) {
            return null;
        }
        FileOutputStream fos = null;
        FileInputStream fis = null;
        File outFile = null;
        try {
            outFile = new File(filePath);
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            fos = new FileOutputStream(outFile);
            fis = new FileInputStream(file);
            byte[] content = new byte[1024];
            int t = 0;
            while ((t = fis.read(content)) <= 1024 && t != -1) {
                fos.write(content, 0, t);
            }
        } catch (Exception e) {
            System.out.println("============================================");
            System.out.println("In DocumentOperationUtil.writeFileToDisk(), write file to disk Failed! file name: " + filePath);
            System.out.println("============================================");
        } finally {
            try {
                if (fos != null && fis != null) {
                    fos.close();
                    fis.close();
                }
            } catch (IOException e) {
            }
        }
        return outFile;
    }

    /**
	 * ��һ���ļ����ĩβ���"_������_ʱ����_long�ͺ���ֵ"���ַ�һ�����������ļ����ظ�
	 */
    public static String addDateTimeStringToEndOfFileName(String filePath) {
        if (filePath == null || "".equals(filePath.trim())) {
            return null;
        }
        int indexOfExtension = filePath.lastIndexOf(".");
        String filePathWithoutExtension = filePath;
        String fileExtension = "";
        if (indexOfExtension > 0) {
            filePathWithoutExtension = filePath.substring(0, indexOfExtension);
            fileExtension = filePath.substring(indexOfExtension);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        String year = cal.get(Calendar.YEAR) + "";
        String month = (cal.get(Calendar.MONTH) + 1) + "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        String day = (cal.get(Calendar.DAY_OF_MONTH)) + "";
        if (day.length() == 1) {
            day = "0" + day;
        }
        String hour = (cal.get(Calendar.HOUR_OF_DAY)) + "";
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        String min = (cal.get(Calendar.MINUTE)) + "";
        if (min.length() == 1) {
            min = "0" + min;
        }
        String second = (cal.get(Calendar.SECOND)) + "";
        if (second.length() == 1) {
            second = "0" + second;
        }
        StringBuffer filePathSB = new StringBuffer();
        filePathSB.append(filePathWithoutExtension);
        filePathSB.append("_");
        filePathSB.append(year);
        filePathSB.append(month);
        filePathSB.append(day);
        filePathSB.append("_");
        filePathSB.append(hour);
        filePathSB.append(min);
        filePathSB.append(second);
        filePathSB.append("_");
        filePathSB.append(System.currentTimeMillis());
        filePathSB.append(fileExtension);
        return filePathSB.toString();
    }

    /**
	 *  ���û�������ַ�����д��һ����ʱ�ı��ļ��У������ȴ���һ����ʱĿ¼��
	 *  Added on 2007-10-11
	 */
    public static File getTempTxtFile(String fileContent) {
        if (fileContent == null) {
            fileContent = "";
        }
        String tempDir = createTempDir();
        File txtFile = null;
        if (tempDir != null) {
            if ((!tempDir.endsWith("/")) && (!tempDir.endsWith(File.separator))) {
                tempDir = tempDir + "/";
            }
            txtFile = new File(tempDir + "Message.txt");
            OutputStream outFile = null;
            try {
                outFile = new FileOutputStream(txtFile);
                outFile.write(fileContent.getBytes());
            } catch (Exception e) {
                txtFile = null;
            } finally {
                if (outFile != null) {
                    try {
                        outFile.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return txtFile;
    }

    /**
	 * �õ���ʱĿ¼����ʱ���Сʱ���������ļ������ã�
	 */
    public static int getCommonTempDirRemainHours() {
        return commonTempDirRemainHours;
    }

    /**
	 * �õ���ʱĿ¼�ĸ�Ŀ¼·��(Added on 2007-10-16)
	 */
    public static String getCommonTempDirRoot() {
        return COMMON_TEMP_DIR_ROOT;
    }

    public static void main(String[] args) {
        String filePath = "d:/testFile.txt";
        File file = new File(filePath);
        file = writeFileToDisk(file, filePath);
        System.out.println("file already saved to " + file.getAbsolutePath());
    }
}
