package benchBox.utils;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import benchBox.db.Config;
import benchBox.db.XMLDB_Utils;

/**
 * @author ReNo
 * 
 * TODO
 *  %=in process *=done !=urgent @=just a thought 
 *	# [ ]	
 *
 *  
 */
public class JdomUtils {

    private static Database database1 = null;

    private static Collection col = null;

    public static org.jdom.Document getStatus() {
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory() / 1024;
        long totalMemory = rt.totalMemory() / 1024;
        long freeMemory = rt.freeMemory() / 1024;
        String strJAVAVendor = System.getProperty("java.vendor"), strJAVAVersion = System.getProperty("java.version"), strOSName = System.getProperty("os.name"), strOSver = System.getProperty("os.version");
        String[] sectTitle = { "eXist version:", "eXist build:", "Operating System:", "Java vendor:", "Java version:" }, sectPara = { "", "", strOSName + " " + strOSver, strJAVAVendor, strJAVAVersion }, chapPara = { "" }, sectTitle2 = { "Max. memory:", "Current total:", "Free:" }, sectPara2 = { maxMemory + "K", totalMemory + "K", freeMemory + "K" };
        Element book = new Element("book");
        Element bookInfo = new Element("bookinfo");
        Element bookTitle = new Element("title");
        bookTitle.setText("System status");
        book.addContent(genChapter("General", chapPara, sectTitle, sectPara));
        book.addContent(genChapter("Memory usage", chapPara, sectTitle2, sectPara2));
        bookInfo.addContent(bookTitle);
        book.addContent(bookInfo);
        book.addContent(XMLDB_Utils.getInfoAsPreface());
        Document document = new Document(book);
        return document;
    }

    public static org.jdom.Element getInfoAsPreface() {
        String strTitle = "Info";
        String[] strParaArray = { "" }, strSectTitleArray = { "version", "build", "location", "user" }, strSectParaArray = { "", "", Config.getDatabaseURL(), "admin" };
        return genPreface(strTitle, strParaArray, strSectTitleArray, strSectParaArray);
    }

    public static org.jdom.Element genSection(String strSectName, String strSectTitle, String strSectPara) {
        String strSectionName = strSectName, strSectionTitle = strSectTitle, strSectionPara = strSectPara;
        Element sectTitle = new Element("title");
        sectTitle.setText(strSectionTitle);
        Element sectContent = new Element("para");
        sectContent.addContent(strSectionPara);
        Element section = new Element(strSectName);
        section.addContent(sectTitle);
        section.addContent(sectContent);
        return section;
    }

    public static org.jdom.Element genSection(String strSectName, String strSectStatus, String strSectTitle, String strSectPara) {
        String strSectionName = strSectName, strSectionStatus = strSectStatus, strSectionTitle = strSectTitle, strSectionPara = strSectPara;
        Element sectTitle = new Element("title");
        sectTitle.setText(strSectionTitle);
        Element sectContent = new Element("para");
        sectContent.addContent(strSectionPara);
        Element section = new Element(strSectName);
        Attribute status = new Attribute("status", strSectionStatus);
        section.setAttribute(status);
        section.addContent(sectTitle);
        section.addContent(sectContent);
        return section;
    }

    public static org.jdom.Element genChapter(String strChapTitle, String[] strChapPara, String[] sectTitle, String[] sectPara) {
        Element chapter = new Element("chapter");
        Element chapTitle = new Element("title");
        chapTitle.addContent(strChapTitle);
        chapter.addContent(chapTitle);
        for (int i = 0; i < strChapPara.length; i++) {
            Element chapPara = new Element("para");
            chapPara.setText(strChapPara[i]);
            chapter.addContent(chapPara);
        }
        for (int j = 0; j < sectTitle.length; j++) {
            chapter.addContent(genSection("sect1", sectTitle[j], sectPara[j]));
        }
        return chapter;
    }

    public static org.jdom.Element genChapter(String strChapTitle, String[] strChapPara, String[] sectionStatus, String[] sectTitle, String[] sectPara) {
        Element chapter = new Element("chapter");
        Element chapTitle = new Element("title");
        chapTitle.addContent(strChapTitle);
        chapter.addContent(chapTitle);
        for (int i = 0; i < strChapPara.length; i++) {
            Element chapPara = new Element("para");
            chapPara.setText(strChapPara[i]);
            chapter.addContent(chapPara);
        }
        for (int j = 0; j < sectTitle.length; j++) {
            chapter.addContent(genSection("sect1", sectionStatus[j], sectTitle[j], sectPara[j]));
        }
        return chapter;
    }

    public static org.jdom.Element genPreface(String strPrefaceTitle, String[] strPrefPara, String[] sectTitle, String[] sectPara) {
        Element preface = new Element("preface");
        Element chapTitle = new Element("title");
        chapTitle.addContent(strPrefaceTitle);
        preface.addContent(chapTitle);
        for (int i = 0; i < strPrefPara.length; i++) {
            Element chapPara = new Element("para");
            chapPara.setText(strPrefPara[i]);
            preface.addContent(chapPara);
        }
        for (int j = 0; j < sectTitle.length; j++) {
            preface.addContent(genSection("sect1", sectTitle[j], sectPara[j]));
        }
        return preface;
    }
}
