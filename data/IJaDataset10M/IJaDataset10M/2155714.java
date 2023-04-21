package dalsong.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import dalsong.player.DalSongPlayer;

public class Utility {

    public static int getTimeNormlType(long msec) {
        int ms, s, m;
        String timeStr;
        int time = 0;
        ms = (int) (msec / 10) % 100;
        s = (int) ((msec / 1000) % 60);
        m = (int) (msec / 1000) / 60;
        timeStr = String.format("%02d%02d%02d", m, s, ms);
        try {
            time = Integer.parseInt(timeStr);
        } catch (Exception e) {
            time = 0;
        }
        return time;
    }

    public static String toKorean(String str) {
        String rtn = null;
        try {
            if (str != null) {
                rtn = new String(str.getBytes("8859_1"), "KSC5601");
            }
        } catch (java.io.UnsupportedEncodingException e) {
        }
        return rtn;
    }

    public static Map<String, String> getTextData(int mode) {
        String xml = getLanguageXmlFile(mode);
        Map<String, String> menuTextMap = new HashMap<String, String>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            Document doc = builder.parse(is);
            String titleBarText = doc.getChildNodes().item(0).getChildNodes().item(1).getAttributes().item(0).getNodeValue();
            String playListTitleBarText = doc.getChildNodes().item(0).getChildNodes().item(3).getAttributes().item(0).getNodeValue();
            NodeList menuNode = doc.getChildNodes().item(0).getChildNodes().item(5).getChildNodes();
            String menuFile = menuNode.item(1).getAttributes().item(0).getNodeValue();
            String menuOption = menuNode.item(3).getAttributes().item(0).getNodeValue();
            String menuTheme = menuNode.item(5).getAttributes().item(0).getNodeValue();
            String menuHelp = menuNode.item(7).getAttributes().item(0).getNodeValue();
            String menuItemOpen = menuNode.item(1).getChildNodes().item(1).getAttributes().item(0).getNodeValue();
            String menuItemPlaylist = menuNode.item(1).getChildNodes().item(3).getAttributes().item(0).getNodeValue();
            String menuItemExit = menuNode.item(1).getChildNodes().item(5).getAttributes().item(0).getNodeValue();
            String menuItemMiniMode = menuNode.item(3).getChildNodes().item(1).getAttributes().item(0).getNodeValue();
            String menuItemLyricMode = menuNode.item(3).getChildNodes().item(3).getAttributes().item(0).getNodeValue();
            String menuItemSync = menuNode.item(3).getChildNodes().item(5).getAttributes().item(0).getNodeValue();
            String menuItemFont = menuNode.item(3).getChildNodes().item(7).getAttributes().item(0).getNodeValue();
            String menuItemAbout = menuNode.item(7).getChildNodes().item(1).getAttributes().item(0).getNodeValue();
            NodeList popupMenuNode = doc.getChildNodes().item(0).getChildNodes().item(7).getChildNodes();
            String popupMenuDelete = popupMenuNode.item(1).getAttributes().item(0).getNodeValue();
            String popupMenuInformation = popupMenuNode.item(3).getAttributes().item(0).getNodeValue();
            NodeList tagPanelNode = doc.getChildNodes().item(0).getChildNodes().item(9).getChildNodes();
            String tagTrack = tagPanelNode.item(1).getAttributes().item(0).getNodeValue();
            String tagTitle = tagPanelNode.item(3).getAttributes().item(0).getNodeValue();
            String tagArtist = tagPanelNode.item(5).getAttributes().item(0).getNodeValue();
            String tagAlbum = tagPanelNode.item(7).getAttributes().item(0).getNodeValue();
            String tagYear = tagPanelNode.item(9).getAttributes().item(0).getNodeValue();
            String tagGenre = tagPanelNode.item(11).getAttributes().item(0).getNodeValue();
            String tagComment = tagPanelNode.item(13).getAttributes().item(0).getNodeValue();
            String tagComposer = tagPanelNode.item(15).getAttributes().item(0).getNodeValue();
            String tagOriginalArtist = tagPanelNode.item(17).getAttributes().item(0).getNodeValue();
            String tagCopyright = tagPanelNode.item(19).getAttributes().item(0).getNodeValue();
            String tagUrl = tagPanelNode.item(21).getAttributes().item(0).getNodeValue();
            String tagEncodedBy = tagPanelNode.item(23).getAttributes().item(0).getNodeValue();
            menuTextMap.put("titleBarText", titleBarText);
            menuTextMap.put("playListTitleBarText", playListTitleBarText);
            menuTextMap.put("menuFile", menuFile);
            menuTextMap.put("menuOption", menuOption);
            menuTextMap.put("menuTheme", menuTheme);
            menuTextMap.put("menuHelp", menuHelp);
            menuTextMap.put("menuItemOpen", menuItemOpen);
            menuTextMap.put("menuItemPlaylist", menuItemPlaylist);
            menuTextMap.put("menuItemExit", menuItemExit);
            menuTextMap.put("menuItemMiniMode", menuItemMiniMode);
            menuTextMap.put("menuItemLyricMode", menuItemLyricMode);
            menuTextMap.put("menuItemSync", menuItemSync);
            menuTextMap.put("menuItemFont", menuItemFont);
            menuTextMap.put("menuItemAbout", menuItemAbout);
            menuTextMap.put("popupMenuDelete", popupMenuDelete);
            menuTextMap.put("popupMenuInformation", popupMenuInformation);
            menuTextMap.put("tagTrack", tagTrack);
            menuTextMap.put("tagTitle", tagTitle);
            menuTextMap.put("tagArtist", tagArtist);
            menuTextMap.put("tagAlbum", tagAlbum);
            menuTextMap.put("tagYear", tagYear);
            menuTextMap.put("tagGenre", tagGenre);
            menuTextMap.put("tagComment", tagComment);
            menuTextMap.put("tagComposer", tagComposer);
            menuTextMap.put("tagOriginalArtist", tagOriginalArtist);
            menuTextMap.put("tagCopyright", tagCopyright);
            menuTextMap.put("tagUrl", tagUrl);
            menuTextMap.put("tagEncodedBy", tagEncodedBy);
            is.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return menuTextMap;
    }

    private static String getLanguageXmlFile(int mode) {
        String resultStr = "";
        InputStreamReader isr;
        BufferedReader br;
        try {
            FileInputStream fis = null;
            if (mode == DalSongPlayer.ENGLISH_MODE) {
                fis = new FileInputStream("./lang/english.xml");
            } else if (mode == DalSongPlayer.KOREAN_MODE) {
                fis = new FileInputStream("./lang/korean.xml");
            }
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                resultStr += inputLine;
            }
            br.close();
        } catch (Exception e) {
        }
        return resultStr;
    }
}
