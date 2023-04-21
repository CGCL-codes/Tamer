package ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.AbstractAction;
import nat.ConfigNat;
import nat.Nat;
import outils.FileToolKit;

/** Help Action
 * @author gregoire
 * */
public class HelpAction extends AbstractAction {

    /** Pour la sérialisation (non utilisé) */
    private static final long serialVersionUID = 1L;

    /** id */
    private static String id;

    /** name */
    private static String file;

    /** anchor */
    private static String anchor;

    /** component name */
    private String cName = "";

    /** Constructor
	 * @param newid : ID of the help page 
	 * @param name : name of the help page (if context)
	 * @param isContextual : false if from main window, true otherwise */
    public HelpAction(String newid, String name, Boolean isContextual) {
        id = newid;
        cName = isContextual ? toHTML(name).replaceAll("\"", "&quot;") : "";
        String[] infos = getID();
        anchor = infos[1];
        file = infos[0];
        try {
            Boolean useBrowser = ConfigNat.getCurrentConfig().getOpenWithBrowser();
            Boolean cAH = createAccessibleHelp(isContextual);
            if (useBrowser) {
                if (!ConfigNat.getCurrentConfig().getUseInternet()) {
                    URI u;
                    if (!cAH) u = new URI("file", new File("aide///" + file).toURI().getPath(), anchor); else u = new URI("file", new File("aide/ContextualHelp.html").toURI().getPath(), anchor);
                    Desktop.getDesktop().browse(u);
                } else {
                    URI u = new URI("http", "natbraille.free.fr/aide/" + file, anchor);
                    Desktop.getDesktop().browse(u);
                }
            } else {
                if (cAH) new Aide("contexthelp"); else new Aide(id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @return helpfilename and anchor
	 * */
    private static String[] getID() {
        String mapfile = FileToolKit.loadFileToStr("aide/nat.jhm", "UTF8");
        if (mapfile.contains(id)) {
            String[] fileArray = mapfile.split("mapID");
            String mapid = "";
            for (String it : fileArray) {
                if (it.contains("target=\"" + id)) {
                    mapid = it;
                    break;
                }
            }
            mapid = mapid.split("\"")[3];
            if (!mapid.contains("#")) return new String[] { mapid, "" };
            return mapid.split("#");
        }
        return new String[] { "index.html", "" };
    }

    /** @param e : action event */
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    /** tidies the table of content 
	 * @param isContextualHelp : true if contextual (creates html file) */
    public void resetTOC(Boolean isContextualHelp) {
        String source = FileToolKit.loadFileToStr("aide/TableMatieresRef.xml", "UTF8");
        String output = "";
        if (isContextualHelp == false) output = source;
        if (isContextualHelp == true) output = source.replaceFirst("</toc>", "<tocitem text=\"" + cName + "\" target=\"contexthelp\" />\n</toc>");
        output = toHTML(output);
        FileToolKit.saveStrToFile(output, "aide/TableMatieres.xml", "UTF8");
    }

    /** creates a new html file
	 * @return true if the new help sheet is successfully created
	 * @param b : false if opened from main window */
    private boolean createAccessibleHelp(Boolean b) {
        if (id != null && b && ConfigNat.getCurrentConfig().getScrReader() != Nat.SR_DEFAULT) {
            String source = FileToolKit.loadFileToStr("aide/" + file, "UTF8");
            String output;
            if (source.contains("<a name=\"" + anchor + "\"")) {
                String top = "<!DOCTYPE html PUBLIC \"-\\/\\/W3C\\/\\/DTD XHTML 1.0 Strict\\/\\/EN\" \"http:\\/\\/www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + "<html xmlns=\"http:\\/\\/www.w3.org\\/1999\\/xhtml\" xml:lang=\"fr\">\n" + "<head>\n" + "<meta http-equiv=\"Content-Type\" content=\"text\\/html; charset=iso-8859-1\" \\/>\n" + "<link title=\"default\" media=\"screen\" rel=\"stylesheet\" href=\"nat-help.css\" type=\"text\\/css\" \\/>\n" + "<link title=\"impression\" media=\"screen\" href=\"../nat-print.css\" type=\"text/css\" rel=\"alternate stylesheet\" />\n" + "<link title=\"impression\" media=\"print\" href=\"../nat-print.css\" type=\"text/css\" rel=\"stylesheet\" />\n" + "<title>" + cName + "</title>\n" + "</head>\n" + "<body class=\"doc\">\n" + "<div id=\"corps\">\n" + "<h1> Aide contextuelle :" + cName + "</h1><br/>";
                String body = findExtract(source);
                if (body == null) {
                    System.err.println("syntax error in html file");
                    resetTOC(false);
                    return false;
                }
                String bottom = source.contains("<p><a href=\"index.html\"") ? source.split("<p><a href=\"index.html\"")[1] : "</html>";
                body = closeUselessTags(body, 0);
                bottom = "<p><a href=\"" + file + "\">Aller &agrave; la page d'aide compl&egrave;te\n" + "<p><a href=\"index.html\"" + bottom;
                output = top + body + bottom;
                FileToolKit.saveStrToFile(output, "aide/ContextualHelp.html", "UTF8");
                resetTOC(true);
                return true;
            }
        }
        resetTOC(false);
        return false;
    }

    /** close tags
     * @param s : string to clean
     * @param i : iterator 
     * @return s cleared*/
    private String closeUselessTags(String s, int i) {
        if (i == -1) return s.replaceAll(">", "");
        if (s.contains("<")) {
            if (!s.contains(">")) {
                return closeUselessTags(s.replaceAll("<", ""), i + 1);
            }
            String[] s2 = s.split("<", 2);
            String begin = s2[0];
            String rest = s2[1];
            if (rest.startsWith("/") && rest.contains(">")) return closeUselessTags(begin, -1) + closeUselessTags(rest.split(">", 2)[1], i + 1);
            if (rest.contains("/>")) {
                if (!rest.split("/>")[0].contains("<")) {
                    begin += "<" + rest.split(">", 2)[0] + ">";
                    rest = rest.split(">", 2)[1];
                    return closeUselessTags(begin, -1) + closeUselessTags(rest, i + 1);
                }
            }
            String[] s3;
            String ext = ">";
            if (s2[1].split(">")[0].contains(" ")) {
                s3 = s2[1].split(" ", 2);
                String[] s4 = s3[1].split(">", 2);
                ext = " " + s4[0] + ">";
                rest = s4[1];
            } else {
                s3 = s2[1].split(">", 2);
                rest = s3[1];
            }
            String tagName = s3[0];
            if (rest.contains("</" + tagName + ">")) {
                String tag = "";
                String[] s5 = rest.split("</" + tagName + ">", 2);
                rest = s5[0];
                String end = s5[1];
                String mid = "";
                while (rest.contains("<" + tagName)) {
                    tag = "<" + tagName + ext;
                    String[] s6 = rest.split("<" + tagName, 2);
                    mid += s6[0];
                    rest = s6[1];
                    if (!rest.contains("<" + tagName) && rest.contains(">")) {
                        String[] s7 = rest.split(">", 2);
                        ext = s7[0] + ">";
                        rest = s7[1];
                        break;
                    }
                    mid += "<" + tagName;
                }
                rest = "___" + i + "___" + rest;
                rest = mid + rest;
                return closeUselessTags(begin, -1) + closeUselessTags(tag + rest + "---" + i + "---" + end, i + 1).replaceFirst("---" + i + "---", "</" + tagName + ">").replaceFirst("___" + i + "___", "<" + tagName + ext);
            }
            return closeUselessTags(begin, -1) + closeUselessTags(rest, i + 1);
        }
        return s;
    }

    /** replaces special characters (except <, >, ", &)
     * @param s string with special characters
     * @return string with html characters*/
    public String toHTML(String s) {
        String a = s;
        if (a == null) return a;
        a = a.replaceAll("œ", "&oelig;");
        a = a.replaceAll("Ÿ", "&Yuml;");
        a = a.replaceAll("¡", "&iexcl;");
        a = a.replaceAll("¢", "&cent;");
        a = a.replaceAll("£", "&pound;");
        a = a.replaceAll("¤", "&curren;");
        a = a.replaceAll("¦", "&brvbar;");
        a = a.replaceAll("§", "&sect;");
        a = a.replaceAll("¨", "&uml;");
        a = a.replaceAll("©", "&copy;");
        a = a.replaceAll("ª", "&ordf;");
        a = a.replaceAll("«", "&laquo;");
        a = a.replaceAll("¬", "&not;");
        a = a.replaceAll("®", "&reg;");
        a = a.replaceAll("¯", "&masr;");
        a = a.replaceAll("°", "&deg;");
        a = a.replaceAll("±", "&plusmn;");
        a = a.replaceAll("²", "&sup2;");
        a = a.replaceAll("³", "&sup3;");
        a = a.replaceAll("´", "&acute;");
        a = a.replaceAll("µ", "&micro;");
        a = a.replaceAll("¶", "&para;");
        a = a.replaceAll("·", "&middot;");
        a = a.replaceAll("¸", "&cedil;");
        a = a.replaceAll("¹", "&sup1;");
        a = a.replaceAll("º", "&ordm;");
        a = a.replaceAll("»", "&raquo;");
        a = a.replaceAll("¼", "&frac14;");
        a = a.replaceAll("½", "&frac12;");
        a = a.replaceAll("¾", "&frac34;");
        a = a.replaceAll("¿", "&iquest;");
        a = a.replaceAll("À", "&Agrave;");
        a = a.replaceAll("Á", "&Aacute;");
        a = a.replaceAll("Â", "&Acirc;");
        a = a.replaceAll("Ã", "&Atilde;");
        a = a.replaceAll("Ä", "&Auml;");
        a = a.replaceAll("à", "&agrave;");
        a = a.replaceAll("â", "&acirc;");
        a = a.replaceAll("ã", "&atilde;");
        a = a.replaceAll("ä", "&Auml;");
        a = a.replaceAll("Å", "&Aring;");
        a = a.replaceAll("Æ", "&Aelig");
        a = a.replaceAll("Ç", "&Ccedil;");
        a = a.replaceAll("ç", "&ccedil;");
        a = a.replaceAll("È", "&Egrave;");
        a = a.replaceAll("É", "&Eacute;");
        a = a.replaceAll("Ê", "&Ecirc;");
        a = a.replaceAll("Ë", "&Euml;");
        a = a.replaceAll("è", "&egrave;");
        a = a.replaceAll("é", "&eacute;");
        a = a.replaceAll("ê", "&ecirc;");
        a = a.replaceAll("ë", "&euml;");
        a = a.replaceAll("Ì", "&Igrave;");
        a = a.replaceAll("Í", "&Iacute;");
        a = a.replaceAll("Î", "&Icirc;");
        a = a.replaceAll("Ï", "&Iuml;");
        a = a.replaceAll("ì", "&igrave;");
        a = a.replaceAll("î", "&icirc;");
        a = a.replaceAll("ï", "&iuml;");
        a = a.replaceAll("Ð", "&eth;");
        a = a.replaceAll("Ñ", "&Ntilde;");
        a = a.replaceAll("ñ", "&ntilde;");
        a = a.replaceAll("Ò", "&Ograve;");
        a = a.replaceAll("Ó", "&Oacute;");
        a = a.replaceAll("Ô", "&Ocirc;");
        a = a.replaceAll("Õ", "&Otilde;");
        a = a.replaceAll("Ö", "&Ouml;");
        a = a.replaceAll("ò", "&ograve;");
        a = a.replaceAll("ô", "&ocirc;");
        a = a.replaceAll("ö", "&ouml");
        a = a.replaceAll("õ", "&otilde;");
        a = a.replaceAll("×", "&times;");
        a = a.replaceAll("Ø", "&Oslash;");
        a = a.replaceAll("Ù", "&Ugrave;");
        a = a.replaceAll("Ú", "&Uacute;");
        a = a.replaceAll("Û", "&Ucirc;");
        a = a.replaceAll("Ü", "&Uuml;");
        a = a.replaceAll("ù", "&ugrave;");
        a = a.replaceAll("û", "&ucirc;");
        a = a.replaceAll("ü", "&uuml;");
        a = a.replaceAll("Ý", "&Yacute;");
        a = a.replaceAll("Þ", "&thorn;");
        return a;
    }

    /** finds extract surrounded by <a name="..."></a> and <a name=...
     * or <a name="..."> [...] </a>
     * @param e TODO
     * @return true if extract found */
    private String findExtract(String e) {
        String s = e;
        if (!s.contains("<a name=\"" + anchor + "\"")) return null;
        s = s.split("<a name=\"" + anchor + "\"")[1];
        if (s.replaceAll(" ", "").startsWith("></a>")) {
            s = s.replaceFirst(">", "").replaceFirst("</a>", "");
            if (s.contains("<a name=")) {
                s = s.split("<a name=")[0];
                return s;
            } else if (s.contains("<p><a href=\"index.html\"")) {
                s = s.split("<p><a href=\"index.html\"")[0];
                return s;
            }
            return null;
        } else if (s.replaceFirst(" ", "").startsWith(">")) {
            if (!s.contains("</a>")) {
                return null;
            }
            int i = 1;
            String s2 = s;
            String s3 = "";
            s = "";
            Boolean b1, b2;
            while (b2 = s2.contains("</a")) {
                b1 = s2.contains("<a");
                if (b1) b2 = (s2.split("<a", 2)[0].length() < s2.split("</a", 2)[0].length()) ? false : true;
                if (b2) {
                    i--;
                    if (i == 0) {
                        return s + s2.split("</a", 2)[0];
                    }
                    s3 = s2.split("</a", 2)[0] + "</a";
                    s2 = s2.split("</a", 2)[1];
                    s = s + s3;
                } else if (b1) {
                    i++;
                    s3 = s2.split("<a", 2)[0] + "<a";
                    s2 = s2.split("<a", 2)[1];
                    s = s + s3;
                }
            }
            return s;
        }
        return null;
    }
}
