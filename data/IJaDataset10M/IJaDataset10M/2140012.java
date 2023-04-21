package servlet.util;

/**
 *
 * @author  Administrator
 */
public class FileUploadDownloadHandler {

    /** Creates a new instance of FileUploadDownloadHandler */
    public FileUploadDownloadHandler() {
    }

    public java.util.Vector download(Object[] obj) {
        java.util.Vector retvec = new java.util.Vector(1, 1);
        String purpose = obj[1].toString();
        System.out.println("purpose=" + purpose);
        if (purpose.equals("PATRONPHOTO")) {
            try {
                java.util.ArrayList alParameters = (java.util.ArrayList) obj[2];
                System.out.println("here 1");
                String patronId = alParameters.get(0).toString();
                String libraryId = alParameters.get(1).toString();
                System.out.println("here2");
                String fileSeperator = System.getProperties().get("file.separator").toString();
                java.io.File patpho = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getRoot() + "/PatronPhotos/" + "LIB_" + libraryId + "/" + "PAT_" + patronId + ".jpg");
                System.out.println("patronId : " + patronId);
                retvec.addElement("PAT_" + patronId + ".jpg");
                java.nio.channels.FileChannel fc = (new java.io.FileInputStream(patpho)).getChannel();
                int fileLength = (int) fc.size();
                System.out.println("fileLength : " + fileLength);
                java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                byte[] byx = new byte[bb.capacity()];
                System.out.println(byx.length);
                System.out.println(bb.hasArray());
                fc.close();
                bb.get(byx);
                System.out.println(byx.length);
                retvec.addElement(byx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (purpose.equals("CATALOGUEATTACHMENTS")) {
            try {
                java.util.ArrayList alParameters = (java.util.ArrayList) obj[2];
                String catrecid = alParameters.get(0).toString();
                String libid = alParameters.get(1).toString();
                String filename = alParameters.get(2).toString();
                java.io.File ff = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getAttachmentsPath() + "/CatalogueRecords/CAT_" + catrecid + "_" + libid + "/" + filename);
                java.nio.channels.FileChannel fc = (new java.io.FileInputStream(ff)).getChannel();
                int fileLength = (int) fc.size();
                java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                byte[] byx = new byte[bb.capacity()];
                fc.close();
                bb.get(byx);
                retvec.addElement(filename);
                retvec.addElement(byx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (purpose.equals("MAPPHOTO")) {
            byte[] byx = new byte[100];
            try {
                String libraryId = ((String[]) obj[2])[0];
                System.out.println("library id in location hancler=" + libraryId);
                String filename = ((String[]) obj[2])[1];
                String filepath = ejb.bprocess.util.NewGenLibRoot.getRoot() + java.util.ResourceBundle.getBundle("server").getString("Maps");
                filepath += "/LIB_" + libraryId + "/" + filename;
                System.out.println("in location handler file path=" + filepath);
                java.io.File actualfile = new java.io.File(filepath);
                java.nio.channels.FileChannel fc = (new java.io.FileInputStream(actualfile)).getChannel();
                int fileLength = (int) fc.size();
                java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                byx = new byte[bb.capacity()];
                bb.get(byx);
                retvec.addElement(byx);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        return retvec;
    }

    public java.util.Vector removeAttachment(Object[] obj) {
        System.out.println("starting in remove attachments...");
        String ret = "OK";
        java.util.Vector vret = new java.util.Vector(1, 1);
        try {
            String filename = obj[1].toString().trim();
            String foldername = obj[2].toString().trim();
            System.out.println("finames..." + filename + "..foldername.." + foldername + "  path " + ejb.bprocess.util.NewGenLibRoot.getAttachmentsPath() + "/CatalogueRecords");
            java.io.File rootfolder = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getAttachmentsPath() + "/CatalogueRecords");
            String[] arrFolders = rootfolder.list();
            boolean value = true;
            for (int j = 0; j < arrFolders.length; j++) {
                System.out.println("in loop");
                String thisFolder = arrFolders[j];
                System.out.println(thisFolder);
                if (thisFolder.equals(foldername.trim())) {
                    java.io.File flnm = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getAttachmentsPath() + "/CatalogueRecords" + java.io.File.separator + foldername.trim() + java.io.File.separator + filename.trim());
                    System.out.println("equls folder....is  " + flnm.getAbsolutePath() + "....." + flnm.getName());
                    boolean valid = flnm.delete();
                    System.out.println("after deleting..." + valid);
                    if (valid) {
                        vret.addElement("Successfully deleted");
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vret;
    }

    public java.util.Vector upload(Object[] obj) {
        String ret = "OK";
        java.util.Vector vret = new java.util.Vector(1, 1);
        try {
            String purpose = obj[1].toString();
            System.out.println("1");
            if (purpose.equals("CATALOGUEATTACHMENTS")) {
                if (obj[3] != null) {
                    java.util.Vector vecRem = (java.util.Vector) obj[3];
                    for (int kp = 0; kp < vecRem.size(); kp++) {
                        Object[] objrem = (Object[]) vecRem.elementAt(kp);
                        this.removeAttachment(objrem);
                    }
                }
                System.out.println("2");
                java.util.Vector vec = (java.util.Vector) obj[2];
                String cataloguerecordid = vec.elementAt(0).toString();
                String libraryid = vec.elementAt(1).toString();
                if (vec != null && vec.size() > 2) {
                    for (int i = 2; i < vec.size(); i += 2) {
                        System.out.println("3");
                        String filename = vec.elementAt(i).toString();
                        System.out.println("File name: " + filename);
                        byte[] bx = (byte[]) vec.elementAt(i + 1);
                        java.io.File rootfolder = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getAttachmentsPath() + "/CatalogueRecords");
                        String[] arrFolders = rootfolder.list();
                        boolean value = true;
                        for (int j = 0; j < arrFolders.length; j++) {
                            String thisFolder = arrFolders[j];
                            System.out.println(thisFolder);
                            if (thisFolder.equals("CAT_" + cataloguerecordid + "_" + libraryid)) {
                                value = true;
                                break;
                            } else {
                                value = false;
                                continue;
                            }
                        }
                        if (arrFolders.length == 0) value = false;
                        java.io.File folder = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getAttachmentsPath() + "/CatalogueRecords/" + "CAT_" + cataloguerecordid + "_" + libraryid);
                        if (!value) {
                            boolean val = folder.mkdir();
                        }
                        System.out.println("Enterd here");
                        java.io.File ff = new java.io.File(folder.getAbsolutePath() + "/" + filename);
                        ff.createNewFile();
                        java.io.FileOutputStream fos = new java.io.FileOutputStream(ff);
                        fos.write(bx);
                        fos.close();
                    }
                }
                java.io.File folder23 = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getAttachmentsPath() + "/CatalogueRecords/" + "CAT_" + cataloguerecordid + "_" + libraryid);
                if (folder23.exists()) {
                    if (folder23.list().length == 0) {
                        try {
                            folder23.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            } else if (purpose.equals("PATRONPHOTO")) {
                System.out.println("patron photo");
                java.util.Vector vec = (java.util.Vector) obj[2];
                String patronID = vec.elementAt(1).toString();
                System.out.println("patronID : " + patronID);
                String libraryid = vec.elementAt(0).toString();
                System.out.println("libraryid : " + libraryid);
                byte[] byteArray = (byte[]) vec.elementAt(2);
                java.io.File rootfolder = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getRoot() + "/PatronPhotos");
                String[] arrFolders = rootfolder.list();
                boolean value = true;
                for (int j = 0; j < arrFolders.length; j++) {
                    String thisFolder = arrFolders[j];
                    System.out.println(thisFolder);
                    if (thisFolder.equals("LIB_" + libraryid)) {
                        value = true;
                        break;
                    } else {
                        value = false;
                        continue;
                    }
                }
                if (arrFolders.length == 0) value = false;
                java.io.File folder = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getRoot() + "/PatronPhotos/" + "LIB_" + libraryid);
                if (!value) {
                    boolean val = folder.mkdir();
                }
                System.out.println("Enterd here");
                java.io.File ff = new java.io.File(folder.getAbsolutePath() + "/PAT_" + patronID + ".jpg");
                boolean flag = ff.createNewFile();
                System.out.println("file created : " + flag);
                java.io.FileOutputStream fos = new java.io.FileOutputStream(ff);
                fos.write(byteArray);
                fos.close();
            } else if (purpose.equals("FORMLETTER")) {
                java.util.Vector vec = (java.util.Vector) obj[2];
                String libraryId = vec.elementAt(0).toString();
                String formatId = vec.elementAt(1).toString();
                int formatIdInt = Integer.parseInt(formatId);
                byte[] byteArrayOO = (byte[]) vec.elementAt(2);
                byte[] byteArrayHTML = (byte[]) vec.elementAt(3);
                String ooPath = (new servlet.administration.FormLetterHandler(flh)).getOODocumentPath(libraryId, formatIdInt);
                String htmlPath = (new servlet.administration.FormLetterHandler(flh)).getHTMLDocumentPath(libraryId, formatIdInt);
                java.io.FileOutputStream fosOO = new java.io.FileOutputStream(ooPath);
                java.io.FileOutputStream fosHTML = new java.io.FileOutputStream(htmlPath);
                fosOO.write(byteArrayOO);
                fosHTML.write(byteArrayHTML);
                fosOO.close();
                fosHTML.close();
            } else if (purpose.equals("CatalogueRecords")) {
                java.util.Vector vec = (java.util.Vector) obj[2];
                byte[] byteArray = (byte[]) vec.elementAt(1);
                java.io.File f = new java.io.File("D:/Jar/");
                if (!f.isDirectory()) {
                    f.mkdir();
                }
                java.io.File ff = new java.io.File(f.getAbsolutePath() + "/Cat_jar" + ".jar");
                boolean flag = ff.createNewFile();
                System.out.println("file created : " + flag);
                java.io.FileOutputStream fos = new java.io.FileOutputStream(ff);
                fos.write(byteArray);
                fos.close();
                java.util.jar.JarInputStream jis = new java.util.jar.JarInputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(ff)));
                java.util.jar.JarFile jf = new java.util.jar.JarFile(ff);
                java.util.jar.JarEntry je = null;
                java.util.jar.JarEntry je1 = null;
                je1 = jis.getNextJarEntry();
                long init = 0;
                long fin = 0;
                System.out.println("first jar entry" + je1.getName());
                System.out.println("fir  " + jf.getJarEntry(je1.getName()).getName());
                int no = jf.size() - 1;
                System.out.println("no of entries" + jf.size());
                int i = 0;
                je = jf.getJarEntry("library.xml");
                java.io.InputStream entryStream1 = jf.getInputStream(je);
                String libraryName = "";
                org.jdom.input.SAXBuilder sab1 = new org.jdom.input.SAXBuilder();
                org.jdom.Document doc1 = sab1.build(entryStream1);
                libraryName = doc1.getRootElement().getText();
                java.sql.Connection con = ejb.bprocess.util.DBConnector.getInstance().getDBConnection();
                String query1 = "select library_id from library where library_name='" + libraryName + "'";
                java.sql.Statement sta = con.createStatement();
                java.sql.ResultSet rs = sta.executeQuery(query1);
                int libId = 0;
                while (rs.next()) {
                    libId = rs.getInt(1);
                }
                System.out.println("libid" + libId);
                rs.close();
                sta.close();
                String query2 = "select harvest_url from harvestlibrary where library_id=" + libId;
                java.sql.Statement sta1 = con.createStatement();
                java.sql.ResultSet rs1 = sta1.executeQuery(query2);
                String serPath = null;
                while (rs1.next()) {
                    serPath = rs1.getString(1);
                }
                System.out.println("libid" + serPath);
                rs1.close();
                sta1.close();
                con.close();
                java.io.File fSer = new java.io.File(serPath);
                if (!fSer.isDirectory()) {
                    fSer.mkdir();
                }
                while (i < no) {
                    je = jf.getJarEntry(je1.getName());
                    java.io.InputStream entryStream = jf.getInputStream(je);
                    String save = serPath;
                    save = save + je1.getName();
                    java.io.FileOutputStream out = new java.io.FileOutputStream(save);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = entryStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    je1 = jis.getNextJarEntry();
                    i++;
                }
                java.io.File parF = new java.io.File(ff.getParent());
                System.out.println("deileted is" + ff.delete());
                parF.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = "FAIL";
        }
        vret.add(ret);
        return vret;
    }

    private ejb.bprocess.administration.FormLetterHome flh;
}
