package bebopicker.net;

import bebopicker.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mark
 */
public class SessionManager {

    String userName, password;

    URL url;

    URLConnection conn;

    long memberID;

    PhotoAlbum[] photoAlbumArray;

    String savePath;

    public SessionManager() {
    }

    public SessionManager(String username) {
        try {
            url = new URL("http://" + username + ".bebo.com");
            conn = url.openConnection();
        } catch (MalformedURLException ex) {
            Logger.getLogger(SessionManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SessionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean checkUser() {
        boolean check = false;
        try {
            for (int i = 0; ; i++) {
                String headerName = conn.getHeaderFieldKey(i);
                String headerValue = conn.getHeaderField(i);
                if (headerName == null && headerValue == null) {
                    break;
                }
                if (headerName == null) {
                    if (headerValue.contains("200 OK")) {
                        check = true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return check;
    }

    public long getMemberID() {
        memberID = 0;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            int indexStart;
            int indexEnd;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("<title>Please")) {
                    memberID = -1;
                    break;
                }
                if (inputLine.contains("MemberId=")) {
                    indexStart = (inputLine.indexOf("MemberId=") + 9);
                    indexEnd = indexStart + (inputLine.substring(indexStart)).indexOf(" ");
                    memberID = Long.parseLong((inputLine.substring(indexStart, indexEnd)).trim());
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memberID;
    }

    public void getAlbumLinks() {
        URL photoAlbumsURL;
        try {
            photoAlbumsURL = new URL("http://www.bebo.com/PhotoAlbums.jsp?MemberId=" + memberID);
            conn = photoAlbumsURL.openConnection();
            ArrayList photoAlbumList = new ArrayList();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            int count = 0;
            while ((inputLine = in.readLine()) != null) {
                String lines[] = inputLine.split("<a href=PhotoAlbum.jsp?");
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].startsWith("?PhotoNbr")) {
                        String current = lines[i];
                        int indexStart = current.indexOf("AlbumId=") + 8;
                        int indexEnd = current.indexOf(" ");
                        long photoAlbumId = Long.parseLong(current.substring(indexStart, indexEnd));
                        String link = "http://www.bebo.com/PhotoAlbum.jsp" + current.substring(0, indexEnd);
                        indexStart = indexEnd + 11;
                        indexEnd = current.indexOf("</a>");
                        String name = current.substring(indexStart, indexEnd);
                        indexStart = current.indexOf("<i>(") + 4;
                        int photoCount = Integer.parseInt(current.substring(indexStart, indexStart + 2).trim());
                        PhotoAlbum pa = new PhotoAlbum(name, link, photoAlbumId, photoCount);
                        photoAlbumList.add(pa);
                        count++;
                    }
                }
            }
            in.close();
            photoAlbumList.trimToSize();
            photoAlbumArray = new PhotoAlbum[count];
            photoAlbumList.toArray(photoAlbumArray);
        } catch (IOException ex) {
            Logger.getLogger(SessionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getPhotoLinks() {
        for (int j = 0; j < photoAlbumArray.length; j++) {
            try {
                URL currentURL = new URL(photoAlbumArray[j].getLink());
                conn = currentURL.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                int count = 0;
                while ((inputLine = in.readLine()) != null) {
                    String lines[] = inputLine.split("<a href=PhotoAlbumBig.jsp");
                    for (int i = 0; i < lines.length; i++) {
                        if (lines[i].startsWith("?M")) {
                            i++;
                            String current = lines[i];
                            int indexStart = current.indexOf("PhotoId=") + 8;
                            int indexEnd = current.indexOf(">");
                            long photoId = Long.parseLong(current.substring(indexStart, indexEnd));
                            String link = "http://www.bebo.com/PhotoAlbumBig.jsp" + current.substring(0, indexEnd);
                            indexStart = indexEnd + 1;
                            indexEnd = current.indexOf("<");
                            String name = current.substring(indexStart, indexEnd);
                            photoAlbumArray[j].Photos[count] = new Photo(link, name, photoId);
                            count++;
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(SessionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getOriginalPhotoLinks() {
        for (int i = 0; i < photoAlbumArray.length; i++) {
            boolean success;
            success = (new File(savePath + File.separator + photoAlbumArray[i].getName())).mkdirs();
            if (!success) {
                System.out.println("Error creating Directory: " + savePath + File.separator + photoAlbumArray[i].getName());
            }
        }
        try {
            PicWriter out = new PicWriter();
            for (int j = 0; j < photoAlbumArray.length; j++) {
                for (int i = 0; i < photoAlbumArray[j].Photos.length; i++) {
                    URL currentURL = new URL(photoAlbumArray[j].Photos[i].getLink());
                    conn = currentURL.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    int count = 0;
                    parseOriginal: while ((inputLine = in.readLine()) != null) {
                        String[] lines = inputLine.split("<a href=");
                        for (int k = 0; k < lines.length; k++) {
                            if (lines[k].contains("/original/")) {
                                int indexStart = lines[k].indexOf("\"") + 1;
                                int indexEnd = lines[k].indexOf("\"", indexStart);
                                String link = lines[k].substring(indexStart, indexEnd);
                                photoAlbumArray[j].Photos[i].setOriginalLink(link);
                                System.out.println("Got link for: " + photoAlbumArray[j].Photos[i].getName());
                                System.out.println("Downloading: " + photoAlbumArray[j].Photos[i].getName() + "...");
                                out.get(photoAlbumArray[j].Photos[i].getOriginalLink(), (savePath + File.separator + photoAlbumArray[j].getName() + File.separator + photoAlbumArray[j].Photos[i].getName() + ".jpg"));
                                break parseOriginal;
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SessionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public PhotoAlbum[] getAlbumPrintInfo() {
        return photoAlbumArray;
    }

    public boolean login(String userName, String password) {
        boolean success = true;
        this.userName = userName;
        this.password = password;
        return success;
    }
}
