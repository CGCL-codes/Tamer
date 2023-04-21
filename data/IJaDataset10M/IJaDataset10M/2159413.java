package com.ravi.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.StringTokenizer;

public class CopyFiles {

    private String message;

    private int copyCnt;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("��;��copy���µ�WIAS������");
            System.err.println("Usage:CpoyFiles fromPath toPath");
            System.err.println("      CpoyFiles \\10.144.66.201\\release C:\\V3R2");
            return;
        }
        CopyFiles cf = new CopyFiles();
        File src = new File(args[0]);
        if (!src.isDirectory()) {
            System.out.println("·����Ч��");
            return;
        }
        int srcFolder = 0;
        long temp = 0;
        File srcFiles[] = src.listFiles();
        if (srcFiles.length == 0) {
            return;
        }
        for (int i = 0; i < srcFiles.length; i++) {
            if (srcFiles[i].isFile()) {
                continue;
            }
            if (temp < srcFiles[i].lastModified()) {
                temp = srcFiles[i].lastModified();
                srcFolder = i;
            }
        }
        String srcWIASPath = args[0] + "\\" + srcFiles[srcFolder].getName() + "\\WIAS";
        String dstWIASPath = args[1] + "\\" + srcFiles[srcFolder].getName() + "\\WIAS";
        File srcWias = new File(srcWIASPath);
        if (!srcWias.exists()) {
            System.out.println("������WIASĿ¼");
            return;
        }
        File dstFolder = new File(args[1] + "/" + srcFiles[srcFolder].getName());
        if (!dstFolder.exists() || dstFolder.lastModified() < temp) {
            cf.copyFolder(srcWIASPath, dstWIASPath);
            dstFolder.setLastModified(temp);
            System.out.println(new Date() + " copy�ļ���Ŀ" + cf.getCopyCnt());
            return;
        }
        System.out.println("�������ļ�û�и��£�");
    }

    /**
	 * ��ȡ�ı��ļ�����
	 * 
	 * @param filePathAndName
	 *            ����������·�����ļ���
	 * @param encoding
	 *            �ı��ļ��򿪵ı��뷽ʽ
	 * @return �����ı��ļ�������
	 */
    public String readTxt(String filePathAndName, String encoding) throws IOException {
        encoding = encoding.trim();
        StringBuffer str = new StringBuffer("");
        String st = "";
        try {
            FileInputStream fs = new FileInputStream(filePathAndName);
            InputStreamReader isr;
            if (encoding.equals("")) {
                isr = new InputStreamReader(fs);
            } else {
                isr = new InputStreamReader(fs, encoding);
            }
            BufferedReader br = new BufferedReader(isr);
            try {
                String data = "";
                while ((data = br.readLine()) != null) {
                    str.append(data + " ");
                }
            } catch (Exception e) {
                str.append(e.toString());
            }
            st = str.toString();
        } catch (IOException es) {
            st = "";
        }
        return st;
    }

    /**
	 * �½�Ŀ¼
	 * 
	 * @param folderPath
	 *            Ŀ¼
	 * @return ����Ŀ¼�������·��
	 */
    public String createFolder(String folderPath) {
        String txt = folderPath;
        try {
            java.io.File myFilePath = new java.io.File(txt);
            txt = folderPath;
            if (!myFilePath.exists()) {
                myFilePath.mkdir();
            }
        } catch (Exception e) {
            message = "����Ŀ¼��������";
        }
        return txt;
    }

    /**
	 * �༶Ŀ¼����
	 * 
	 * @param folderPath
	 *            ׼��Ҫ�ڱ���Ŀ¼�´�����Ŀ¼��Ŀ¼·�� ���� c:myf
	 * @param paths
	 *            ���޼�Ŀ¼�������Ŀ¼�Ե�������� ���� a|b|c
	 * @return ���ش����ļ����·�� ���� c:myfac
	 */
    public String createFolders(String folderPath, String paths) {
        String txts = folderPath;
        try {
            String txt;
            txts = folderPath;
            StringTokenizer st = new StringTokenizer(paths, "|");
            for (int i = 0; st.hasMoreTokens(); i++) {
                txt = st.nextToken().trim();
                if (txts.lastIndexOf("/") != -1) {
                    txts = createFolder(txts + txt);
                } else {
                    txts = createFolder(txts + txt + "/");
                }
            }
        } catch (Exception e) {
            message = "����Ŀ¼�������?";
        }
        return txts;
    }

    /**
	 * �½��ļ�
	 * 
	 * @param filePathAndName
	 *            �ı��ļ�������·�����ļ���
	 * @param fileContent
	 *            �ı��ļ�����
	 * @return
	 */
    public void createFile(String filePathAndName, String fileContent) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            FileWriter resultFile = new FileWriter(myFilePath);
            PrintWriter myFile = new PrintWriter(resultFile);
            String strContent = fileContent;
            myFile.println(strContent);
            myFile.close();
            resultFile.close();
        } catch (Exception e) {
            message = "�����ļ���������";
        }
    }

    /**
	 * �б��뷽ʽ���ļ�����
	 * 
	 * @param filePathAndName
	 *            �ı��ļ�������·�����ļ���
	 * @param fileContent
	 *            �ı��ļ�����
	 * @param encoding
	 *            ���뷽ʽ ���� GBK ���� UTF-8
	 * @return
	 */
    public void createFile(String filePathAndName, String fileContent, String encoding) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            PrintWriter myFile = new PrintWriter(myFilePath, encoding);
            String strContent = fileContent;
            myFile.println(strContent);
            myFile.close();
        } catch (Exception e) {
            message = "�����ļ���������";
        }
    }

    /**
	 * ɾ���ļ�
	 * 
	 * @param filePathAndName
	 *            �ı��ļ�������·�����ļ���
	 * @return Boolean �ɹ�ɾ���true�����쳣����false
	 */
    public boolean delFile(String filePathAndName) {
        boolean bea = false;
        try {
            String filePath = filePathAndName;
            File myDelFile = new File(filePath);
            if (myDelFile.exists()) {
                myDelFile.delete();
                bea = true;
            } else {
                bea = false;
                message = (filePathAndName + "ɾ���ļ���������");
            }
        } catch (Exception e) {
            message = e.toString();
        }
        return bea;
    }

    /**
	 * ɾ���ļ���
	 * 
	 * @param folderPath
	 *            �ļ���������·��
	 * @return
	 */
    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete();
        } catch (Exception e) {
            message = ("ɾ���ļ��в�������");
        }
    }

    /**
	 * ɾ��ָ���ļ����������ļ�
	 * 
	 * @param path
	 *            �ļ���������·��
	 * @return
	 * @return
	 */
    public boolean delAllFile(String path) {
        boolean bea = false;
        File file = new File(path);
        if (!file.exists()) {
            return bea;
        }
        if (!file.isDirectory()) {
            return bea;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);
                delFolder(path + "/" + tempList[i]);
                bea = true;
            }
        }
        return bea;
    }

    /**
	 * ���Ƶ����ļ�
	 * 
	 * @param oldPathFile
	 *            ׼�����Ƶ��ļ�Դ
	 * @param newPathFile
	 *            �������¾��·�����ļ���
	 * @return
	 */
    public void copyFile(String oldPathFile, String newPathFile) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPathFile);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPathFile);
                FileOutputStream fs = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            message = ("���Ƶ����ļ���������");
        }
    }

    /**
	 * ��������ļ��е�����
	 * 
	 * @param oldPath
	 *            ׼��������Ŀ¼
	 * @param newPath
	 *            ָ�����·������Ŀ¼
	 * @return
	 */
    public void copyFolder(String oldPath, String newPath) {
        try {
            File newFolder = new File(newPath);
            File a = new File(oldPath);
            if (!newFolder.exists()) {
                newFolder.mkdirs();
            }
            newFolder.setLastModified(a.lastModified());
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    File newFile = new File(newPath + "/" + (temp.getName()).toString());
                    if (newFile.exists() && newFile.lastModified() >= temp.lastModified()) {
                    } else {
                        FileInputStream input = new FileInputStream(temp);
                        FileOutputStream output = new FileOutputStream(newFile);
                        byte[] b = new byte[1024 * 5];
                        int len;
                        while ((len = input.read(b)) != -1) {
                            output.write(b, 0, len);
                        }
                        output.flush();
                        output.close();
                        input.close();
                        copyCnt++;
                        newFile.setLastModified(temp.lastModified());
                    }
                }
                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            message = "��������ļ������ݲ�������";
        }
    }

    int getCopyCnt() {
        return copyCnt;
    }

    /**
	 * �ƶ��ļ�
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
    public void moveFile(String oldPath, String newPath) {
        copyFile(oldPath, newPath);
        delFile(oldPath);
    }

    /**
	 * �ƶ�Ŀ¼
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
    public void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }

    public String getMessage() {
        return this.message;
    }
}
