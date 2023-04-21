package bump3.engines;

import bump3.*;
import java.util.*;
import java.io.*;

public class Myzuka {

    public static void p(String txt) {
        Methods.p(txt);
    }

    public static void pr(String txt) {
        Methods.pr(txt);
    }

    public static int search(String theArtist, String theTitle) {
        String theURL = "http://myzuka.ru/Search?page=1&type=artists&searchText=" + theArtist.replaceAll(" ", "%20") + "%20";
        p(Main.GR + "[+]" + Main.GR + " Loading " + Main.G + "myzuka" + Main.GR + " search results... ");
        Methods.status("loading myzuka");
        String page = GetUrl.getURL(theURL);
        if (page.equals("")) {
            p(Main.GR + "[+]" + Main.R + " myzuka returned invalid search results");
            return -1;
        }
        Methods.status("searching myzuka");
        String maintitle = theTitle.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String mainartist = theArtist.replaceAll("[^a-zA-Z0-9]", "").toLowerCase().replace("the", "");
        boolean checked = false;
        for (String linky : between(page, "href=\"/Artist/", "\">")) {
            String artist = linky.substring(linky.lastIndexOf("/") + 1);
            artist = artist.replaceAll("[^a-zA-Z0-9]", "").toLowerCase().replace("the", "");
            if (!mainartist.equals(artist)) continue;
            String link = "http://myzuka.ru/Artist/" + linky;
            String page2 = GetUrl.getURL(link);
            int pageNum = 2;
            while (!page2.equals("")) {
                for (String chunk : between(page2, " href=\"/Song", "/a>")) {
                    String title = between(chunk, ">", "<").get(0);
                    title = title.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                    String titleurl = "http://myzuka.ru/Song/" + between(chunk, "/", "\">").get(0);
                    checked = true;
                    Methods.pv(Main.GR + "[+] Comparing " + Main.G + title + Main.GR + " to " + Main.G + maintitle + Main.GR + "");
                    if (!title.startsWith(maintitle)) continue;
                    String the_friggin_link = "";
                    String page3 = GetUrl.getURL(titleurl);
                    for (String chunk2 : between(page3, "<a title=\"", "\">")) {
                        List<String> linx = between(chunk2, "href=\"", "\"");
                        if (linx.size() > 0) {
                            the_friggin_link = linx.get(0);
                            break;
                        }
                    }
                    if (the_friggin_link.equals("")) continue;
                    String mid = the_friggin_link.replace("&amp;", "&");
                    if (mid.length() > 25) Methods.pr(Main.GR + "[+]" + Main.GR + " Checking    " + Main.BR + mid.substring(0, 25) + Main.GR + "... " + Main.W); else Methods.pr(Main.GR + "[+]" + Main.GR + " Checking    " + Main.BR + mid + Main.GR + "... " + Main.W);
                    int size = GetUrl.getFilesize(mid);
                    if (Methods.GuiStop()) return -1;
                    if (size == -2) {
                        p(Main.R + "Not found");
                    } else if (size == -3) {
                        p(Main.R + "Unable to connect");
                    } else if (size >= Main.MIN_FILESIZE) {
                        String save = theArtist + " - " + theTitle + ".mp3";
                        p(Main.G + "Found song; Downloading..." + Main.W);
                        p(Main.GR + "[+]" + Main.GR + " Source:     " + Main.BR + mid + Main.W);
                        p(Main.GR + "[+]" + Main.GR + " Desination: " + Main.GR + Main.SAVE_DIR + (Main.NOSPACES ? Methods.trimspaces(save) : save) + Main.W);
                        int dl = Downloader.download(mid, Main.SAVE_DIR + save, size);
                        if (dl >= 0) return 1;
                    } else {
                        p(Main.R + "Filesize is too small (" + size + " < " + Main.GR + Main.MIN_FILESIZE + Main.R + ")");
                    }
                }
                if (page2.indexOf("/Page" + pageNum + "\">") >= 0) {
                    Methods.pv(Main.GR + "[+] loading next page of results...");
                    page2 = GetUrl.getURL(link + "/Page" + pageNum + "");
                    pageNum++;
                } else {
                    page2 = "";
                }
            }
        }
        if (!checked) p(Main.R + "No results");
        return 0;
    }

    public static List<String> between(String source, String start, String finish) {
        List<String> result = new ArrayList<String>();
        int i, j;
        i = source.indexOf(start);
        j = source.indexOf(finish, i + start.length());
        while (i >= 0 && j >= 0) {
            result.add(source.substring(i + start.length(), j));
            i = source.indexOf(start, j + finish.length());
            j = source.indexOf(finish, i + start.length());
        }
        return result;
    }
}
