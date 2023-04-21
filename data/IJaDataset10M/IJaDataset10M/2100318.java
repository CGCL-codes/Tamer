package pck_tap.alg.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    String lf = "\n";

    String sitemapHtml = "";

    int sitemapHtml_aaFiles = 0;

    int sitemapHtml_aaSkippedFiles = 0;

    int sitemapHtml_aaDirs = 0;

    String sitemapHtml_Root = "";

    public FileUtil() {
        makeHTML();
    }

    public void AppendDir(String p_dir) {
        String t;
        t = p_dir.replaceAll("___", " ");
        t = t.replaceAll("__", " ");
        t = t.replaceAll("_", " ");
        t = "<a>".concat(t).concat("</a>");
        sitemapHtml = sitemapHtml.concat(t).concat("\n");
    }

    public void AppendFile(String p_file, String p_AbsolutePath) {
        String t_AbsolutePath = p_AbsolutePath.substring(sitemapHtml_Root.length());
        t_AbsolutePath = t_AbsolutePath.replace("\\", "/");
        String t = "";
        t = t.concat("<ul TYPE=disc>");
        t = t.concat("<li>");
        t = t.concat("<a href=");
        t = t.concat(t_AbsolutePath);
        t = t.concat("\">");
        t = t.concat("<h2>");
        t = t.concat("Kyles Red Cream Ale");
        t = t.concat("</h2>");
        t = t.concat("</a>");
        t = t.concat("</li>");
        t = t.concat("<p>");
        t = t.concat("Brouwer: Pietje Bell" + t_AbsolutePath);
        t = t.concat("</p>");
        t = t.concat("<p>");
        t = t.concat("Geplanned SG: 1.056");
        t = t.concat("</p>");
        t = t.concat("</ul>");
        sitemapHtml = sitemapHtml.concat(t).concat("\n");
    }

    public void FindAllFiles(File dir) {
        if (dir.isDirectory()) {
            int indent = OnlySeps(dir.getAbsolutePath()).length();
            AppendDir(dir.getName());
            sitemapHtml_aaDirs++;
            String[] children = dir.list();
            sitemapHtml = sitemapHtml.concat("<ul TYPE=disc>");
            for (int i = 0; i < children.length; i++) {
                try {
                    FindAllFiles(new File(dir, children[i]));
                } catch (Exception e) {
                }
            }
            sitemapHtml = sitemapHtml.concat("</ul>");
        } else {
            String x;
            x = dir.getAbsolutePath();
            x = x.substring(x.lastIndexOf(".") + 1, x.length());
            if (x.toUpperCase().equals("XML")) {
                String StyleType = "beerxml.css;beerxml.xsl;beerxml_ebc2rgb.xml";
                if (!StyleType.contains(dir.getName().toLowerCase())) {
                    AppendFile(dir.getName(), dir.getAbsolutePath());
                    sitemapHtml_aaFiles++;
                } else {
                    sitemapHtml_aaSkippedFiles++;
                }
            }
        }
    }

    public void WriteAsciiFile(String p_text, String p_filename) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(p_filename));
            out.write(p_text);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeHTML() {
        sitemapHtml = sitemapHtml.concat(getHeader());
        sitemapHtml_Root = "E:\\ReceptenRepository";
        FindAllFiles(new File(sitemapHtml_Root));
        sitemapHtml = sitemapHtml.concat(getFooter());
        WriteAsciiFile(sitemapHtml, "E:\\ReceptenRepository\\html.htm");
    }

    public static void main(String[] args) {
        FileUtil fileUtil = new FileUtil();
    }

    public static int showDir(int indent, File file) throws IOException {
        int x = 0;
        for (int i = 0; i < indent; i++) System.out.print('-');
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int ii = 0; ii < files.length; ii++) x = showDir(indent + 2, files[ii]);
        } else {
            String StyleType = "beerxml.css;beerxml.xsl;beerxml_ebc2rgb.xml";
            if (!StyleType.contains(file.getName().toLowerCase())) {
                x++;
            }
        }
        return x++;
    }

    private String getHeader() {
        String head;
        head = "<html><!-- Sitemap Creator by josgroen@hotmail.com, www.bier-brouwen.nl-->".concat(lf);
        head = head.concat("<head>").concat(lf);
        head = head.concat("<link rel=\"stylesheet\" href=\"http://www.nhcbierforum.yourbb.nl/styles/subsilver2/theme/stylesheet.css\" type=\"text/css\" />").concat(lf);
        head = head.concat("<title>bier-brouwen.nl &bull; BeerXml recepten database</title>").concat(lf);
        head = head.concat("</head>").concat(lf);
        head = head.concat("<body>").concat(lf);
        return head;
    }

    private String getFooter() {
        String footer;
        footer = "";
        footer = footer.concat("</body>").concat(lf);
        footer = footer.concat("</html>").concat(lf);
        return footer;
    }

    public static String OnlySeps(String s) {
        String good = File.pathSeparator.concat("\\/");
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            if (good.indexOf(s.charAt(i)) >= 0) result += s.charAt(i);
        }
        return result.toLowerCase();
    }
}
