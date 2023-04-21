package jreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Stąd można pobrać nowo utworzony kanał lub aktualną treść kanału.
 */
public class ChannelFactory extends DefaultHandler {

    /**
	 * Kanał, który zostanie zwrócony przez metodę getChannelFromXML().
	 */
    private static Channel channel;

    /**
	 * Lista elementów kanału, który zostanie zwrócony przez getChannelFromXML().
	 */
    private static List<Item> downloadedItems;

    private String guid;

    private String title;

    private String link;

    private String description;

    private String author;

    private Date date;

    /**
	 * Definicja standardowego formatu daty stosowanego w kanałach RSS (RFC 822).
	 */
    private DateFormat RSSDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    /** Format daty używany np&#46; w userfriendly.org. */
    private DateFormat DateFormat1 = new SimpleDateFormat("E, dd MMM yyyy HH:mm z", Locale.ENGLISH);

    /** Format daty uzywany np&#46; w slashdot.org. */
    private DateFormat DateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

    private boolean insideItem;

    private boolean insideImage;

    private boolean insideTextinput;

    private String currentTag = "";

    private String currentURI = "";

    /**
	 * Licznik, który pozwoli na rozróżnienie dat elementów, dla których data
	 * nie jest podana w źródle XML i w konsekwencji posortowanie ich według
	 * kolejności w jakiej wystepują w źródle.
	 */
    private int counter;

    /**
	 * Data parsowania danego kanału.
	 */
    private long currentUnixTime;

    public ChannelFactory() {
        super();
    }

    /**
	 * Udostępnia listę elementów należących do kanału pobranego przy pomocy
	 * metody getChannelFromSite lub getChannelFromXML.
	 *
	 * @return Lista elementów ostatnio pobranego kanału.
	 */
    public static List<Item> getDownloadedItems() {
        return downloadedItems;
    }

    /**
	 * Szuka adresu pliku XML z treścią kanału w źródle strony HTML znajdującej
	 * się pod podanym adresem. Po wywołaniu tej metody należy też pobrać listę
	 * elementów pobranego kanału przy pomocy metody getDownloadedItems.
	 *
	 * @return Nowy kanał o treści ze znalezionego pliku XML.
	 * @throws LinkNotFoundException jeśli na podanej stronie nie znaleziono
	 *         żadnych odnośników do kanałów.
	 * @throws MalformedURLException jeśli podany przez użytkownika adres
	 *         nie jest prawidłowym adresem URL.
	 * @throws SAXParseException jeśli parsowanie źródła XML kanału nie powiodło
	 *         się.
	 * @throws SAXException jeśli wystąpił błąd parsera XML.
	 * @throws IOException jeśli pobieranie pliku nie powiodło się.
	 */
    public static Channel getChannelFromSite(String siteURL) throws LinkNotFoundException, MalformedURLException, SAXException, IOException {
        String channelURL = "";
        siteURL = siteURL.trim();
        if (!siteURL.startsWith("http://")) {
            siteURL = "http://" + siteURL;
        }
        URL url = new URL(siteURL);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String[] lines = new String[3];
        for (int i = 0; i < lines.length; i++) {
            if ((lines[i] = in.readLine()) == null) {
                lines[i] = "";
                break;
            }
        }
        if (lines[0].contains("xml version")) {
            if (lines[0].contains("rss") || lines[1].contains("rss")) {
                channelURL = siteURL;
            }
            if (lines[0].contains("Atom") || lines[1].contains("Atom") || lines[2].contains("Atom")) {
                channelURL = siteURL;
            }
        }
        in.close();
        in = new BufferedReader(new InputStreamReader(url.openStream()));
        String iconURL = null;
        String inputLine;
        if ("".equals(channelURL)) {
            boolean isIconURLFound = false;
            boolean isChannelURLFound = false;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("type=\"image/x-icon\"") || inputLine.toLowerCase().contains("rel=\"shortcut icon\"")) {
                    String tmp = new String(inputLine);
                    String[] smallLines = inputLine.replace(">", ">\n").split("\n");
                    for (String smallLine : smallLines) {
                        if (smallLine.contains("type=\"image/x-icon\"") || smallLine.toLowerCase().contains("rel=\"shortcut icon\"")) {
                            tmp = smallLine;
                            break;
                        }
                    }
                    isIconURLFound = true;
                    iconURL = tmp.replaceAll("^.*href=\"", "");
                    iconURL = iconURL.replaceAll("\".*", "");
                    tmp = null;
                    String originalSiteURL = new String(siteURL);
                    siteURL = getHome(siteURL);
                    if (iconURL.charAt(0) == '/') {
                        if (siteURL.charAt(siteURL.length() - 1) == '/') {
                            iconURL = siteURL + iconURL.substring(1);
                        } else {
                            iconURL = siteURL + iconURL;
                        }
                    } else if (!iconURL.startsWith("http://")) {
                        if (siteURL.charAt(siteURL.length() - 1) == '/') {
                            iconURL = siteURL + iconURL;
                        } else {
                            iconURL = siteURL + "/" + iconURL;
                        }
                    }
                    siteURL = originalSiteURL;
                    if (isChannelURLFound && isIconURLFound) {
                        break;
                    }
                }
                if ((inputLine.contains("type=\"application/rss+xml\"") || inputLine.contains("type=\"application/atom+xml\"")) && !isChannelURLFound) {
                    if (!inputLine.contains("href=")) {
                        while ((inputLine = in.readLine()) != null) {
                            if (inputLine.contains("href=")) {
                                break;
                            }
                        }
                    }
                    inputLine = inputLine.replace(">", ">\n");
                    String[] smallLines = inputLine.split("\n");
                    for (String smallLine : smallLines) {
                        if (smallLine.contains("type=\"application/rss+xml\"") || smallLine.contains("type=\"application/atom+xml\"")) {
                            inputLine = smallLine;
                            break;
                        }
                    }
                    channelURL = inputLine.replaceAll("^.*href=\"", "");
                    channelURL = channelURL.replaceAll("\".*", "");
                    if (channelURL.charAt(0) == '/') {
                        if (siteURL.charAt(siteURL.length() - 1) == '/') {
                            channelURL = siteURL + channelURL.substring(1);
                        } else {
                            channelURL = siteURL + channelURL;
                        }
                    } else if (!channelURL.startsWith("http://")) {
                        if (siteURL.charAt(siteURL.length() - 1) == '/') {
                            channelURL = siteURL + channelURL;
                        } else {
                            channelURL = siteURL + "/" + channelURL;
                        }
                    }
                    isChannelURLFound = true;
                    if (isChannelURLFound && isIconURLFound) {
                        break;
                    }
                }
                if (inputLine.contains("</head>".toLowerCase())) {
                    break;
                }
            }
            in.close();
            if ("".equals(channelURL)) {
                throw new LinkNotFoundException();
            }
        }
        channel = getChannelFromXML(channelURL.trim());
        if (iconURL == null || "".equals(iconURL.trim())) {
            iconURL = "favicon.ico";
            if (siteURL.equalsIgnoreCase(channel.getChannelURL())) {
                siteURL = channel.getLink();
            }
            siteURL = getHome(siteURL);
            if (siteURL.charAt(siteURL.length() - 1) == '/') {
                iconURL = siteURL + iconURL;
            } else {
                iconURL = siteURL + "/" + iconURL;
            }
        }
        try {
            String iconFileName = getHome(channel.getLink());
            if (iconFileName.startsWith("http://")) {
                iconFileName = iconFileName.substring(7);
            }
            iconFileName = iconFileName.replaceAll("\\W", " ").trim().replace(" ", "_").concat(".ico");
            String iconPath = JReader.getConfig().getShortcutIconsDir() + File.separator + iconFileName;
            InputStream inIcon = new URL(iconURL).openStream();
            OutputStream outIcon = new FileOutputStream(iconPath);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inIcon.read(buf)) > 0) {
                outIcon.write(buf, 0, len);
            }
            inIcon.close();
            outIcon.close();
            channel.setIconPath(iconPath);
        } catch (Exception e) {
        }
        return channel;
    }

    /**
	 * Pobiera i parsuje kanał o źródle w podanym adresie URL.
	 * Po wywołaniu tej metody należy też pobrać listę elementów pobranego kanału
	 * przy pomocy metody getDownloadedItems.
	 *
	 * @return Aktualna postać kanału o podanym adresie URL.
	 * @throws SAXParseException jeśli parsowanie źródła XML kanału nie powiodło
	 *         się.
	 * @throws SAXException jeśli wystąpił błąd parsera XML.
	 * @throws IOException jeśli pobieranie pliku nie powiodło się.
	 */
    public static Channel getChannelFromXML(String channelURL) throws SAXException, IOException {
        channel = new Channel(channelURL);
        downloadedItems = new LinkedList<Item>();
        URL url = new URL(channelURL);
        XMLReader xr = XMLReaderFactory.createXMLReader();
        ChannelFactory handler = new ChannelFactory();
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        xr.parse(new InputSource(url.openStream()));
        channel.setUnreadItemsCount(downloadedItems.size());
        return channel;
    }

    /**
	 * Szuka adresu ikony w źródle strony HTML znajdującej się pod podanym
	 * adresem. Jeśli znajdzie, zapisuje ją na dysku.
	 *
	 * @return Znaleziony adres ikony.
	 * @throws IOException jeśli pobieranie pliku nie powiodło się.
	 */
    public static String extractIconPath(String siteURL) throws IOException {
        siteURL = siteURL.trim();
        if (!siteURL.startsWith("http://")) {
            siteURL = "http://" + siteURL;
        }
        URL url = new URL(siteURL);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String iconURL = null;
        String iconPath = null;
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("type=\"image/x-icon\"") || inputLine.toLowerCase().contains("rel=\"shortcut icon\"")) {
                String tmp = new String(inputLine);
                String[] smallLines = inputLine.replace(">", ">\n").split("\n");
                for (String smallLine : smallLines) {
                    if (smallLine.contains("type=\"image/x-icon\"") || smallLine.toLowerCase().contains("rel=\"shortcut icon\"")) {
                        tmp = smallLine;
                        break;
                    }
                }
                iconURL = tmp.replaceAll("^.*href=\"", "");
                iconURL = iconURL.replaceAll("\".*", "");
                tmp = null;
                String originalSiteURL = new String(siteURL);
                siteURL = getHome(siteURL);
                if (iconURL.charAt(0) == '/') {
                    if (siteURL.charAt(siteURL.length() - 1) == '/') {
                        iconURL = siteURL + iconURL.substring(1);
                    } else {
                        iconURL = siteURL + iconURL;
                    }
                } else if (!iconURL.startsWith("http://")) {
                    if (siteURL.charAt(siteURL.length() - 1) == '/') {
                        iconURL = siteURL + iconURL;
                    } else {
                        iconURL = siteURL + "/" + iconURL;
                    }
                }
                siteURL = originalSiteURL;
                break;
            }
            if (inputLine.contains("</head>".toLowerCase())) {
                break;
            }
        }
        in.close();
        siteURL = getHome(siteURL);
        if (iconURL == null || "".equals(iconURL.trim())) {
            iconURL = "favicon.ico";
            if (siteURL.charAt(siteURL.length() - 1) == '/') {
                iconURL = siteURL + iconURL;
            } else {
                iconURL = siteURL + "/" + iconURL;
            }
        }
        try {
            String iconFileName = siteURL;
            if (iconFileName.startsWith("http://")) {
                iconFileName = iconFileName.substring(7);
            }
            iconFileName = iconFileName.replaceAll("\\W", " ").trim().replace(" ", "_").concat(".ico");
            iconPath = JReader.getConfig().getShortcutIconsDir() + File.separator + iconFileName;
            InputStream inIcon = new URL(iconURL).openStream();
            OutputStream outIcon = new FileOutputStream(iconPath);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inIcon.read(buf)) > 0) {
                outIcon.write(buf, 0, len);
            }
            inIcon.close();
            outIcon.close();
        } catch (Exception e) {
        }
        return iconPath;
    }

    /** Treść aktualnie parsowanego znacznika. */
    private String chars;

    public void startDocument() {
        insideItem = false;
        insideImage = false;
        insideTextinput = false;
        counter = 0;
        currentUnixTime = new Date().getTime();
    }

    public void endDocument() {
        insideItem = false;
        insideImage = false;
        insideTextinput = false;
        counter = 0;
    }

    /**
	 * Wywoływana kiedy parser natrafia na początek znacznika.
	 */
    public void startElement(String uri, String name, String qName, Attributes atts) {
        chars = "";
        if ("".equals(uri)) {
            currentTag = qName;
            currentURI = "";
        } else {
            currentTag = name;
            currentURI = uri;
        }
        if (currentTag.equals("item") || currentTag.equals("entry")) {
            insideItem = true;
            guid = null;
            title = null;
            link = null;
            description = null;
            author = null;
            date = null;
            counter++;
        } else if (currentTag.equals("image")) {
            insideImage = true;
        } else if (currentTag.equals("textinput")) {
            insideTextinput = true;
        }
        if (currentTag.equals("link")) {
            String hrefLink = "";
            hrefLink = atts.getValue("href");
            if (!"".equals(hrefLink) && !(hrefLink == null)) {
                if (insideItem) {
                    if ("alternate".equals(atts.getValue("rel"))) {
                        if (link == null || "".equals(link)) {
                            link = hrefLink;
                        }
                    }
                } else {
                    if ("alternate".equals(atts.getValue("rel"))) {
                        if (channel.getLink() == null || "".equals(channel.getLink())) {
                            channel.setLink(hrefLink);
                        }
                    }
                }
            }
        }
    }

    /**
	 * Wywoływana kiedy parser natrafia na koniec znacznika.
	 */
    public void endElement(String uri, String name, String qName) {
        String closingTag;
        chars = chars.trim();
        if (insideImage) {
            if (currentTag.equals("url")) {
                channel.setImageURL(chars);
            } else if (currentTag.equals("title")) {
                channel.setImageTitle(chars);
            } else if (currentTag.equals("link")) {
                if ("".equals(channel.getImageLink()) || channel.getImageLink() == null) {
                    channel.setImageLink(chars);
                }
            }
        } else if (insideTextinput) {
        } else if (!insideItem) {
            if (currentTag.equals("title")) {
                channel.setTitle(chars.replaceAll("<.*?>", "").replace("\n", " ").replaceAll(" +", " "));
            } else if (currentTag.equals("link")) {
                if ("".equals(channel.getLink()) || channel.getLink() == null) {
                    channel.setLink(chars);
                }
            } else if (currentTag.equals("description") || currentTag.equals("content") || currentTag.equals("summary")) {
                channel.setDescription(chars);
            } else if (currentTag.equals("subtitle")) {
                if (channel.getDescription() == null) {
                    channel.setDescription(chars);
                }
            }
        } else {
            if (currentTag.equals("title") && title == null) {
                title = chars.replaceAll("<.*?>", "").replace("\n", " ").replaceAll(" +", " ");
            } else if (currentTag.equals("link")) {
                if ("".equals(link) || link == null) {
                    link = chars;
                }
            } else if (currentTag.equals("description") || currentTag.equals("content") || currentTag.equals("summary") || currentURI.contains("content")) {
                description = chars;
            } else if (currentTag.equals("author") || currentTag.equals("name") || currentTag.equals("creator")) {
                author = chars;
            } else if (currentTag.equals("email")) {
                if (insideItem && author != null) {
                    author = author + " (" + chars + ")";
                }
            } else if (currentTag.equals("pubDate") || currentTag.equals("date") || currentTag.equals("updated")) {
                try {
                    Date parsedDate = RSSDateFormat.parse(chars);
                    date = parsedDate;
                } catch (ParseException pe) {
                    try {
                        Date parsedDate = DateFormat1.parse(chars);
                        date = parsedDate;
                    } catch (ParseException pe1) {
                        try {
                            Date parsedDate = DateFormat2.parse(chars);
                            date = parsedDate;
                        } catch (ParseException pe2) {
                        }
                    }
                }
            } else if (currentTag.equals("guid") || currentTag.equals("id")) {
                guid = chars;
            }
        }
        if ("".equals(uri)) {
            closingTag = qName;
        } else {
            closingTag = name;
        }
        if (currentTag.equals(closingTag)) {
            currentTag = "";
        }
        if (closingTag.equals("item") || closingTag.equals("entry")) {
            insideItem = false;
            if (date == null) {
                date = new Date(currentUnixTime - counter);
            }
            makeNewItem();
        } else if (closingTag.equals("image")) {
            insideImage = false;
        } else if (closingTag.equals("textinput")) {
            insideTextinput = false;
        }
    }

    /**
	 * Analiza treści znacznika.<br>
	 * UWAGA: Cała treść danego znacznika może być podzielona na kilka zdarzeń
	 * "characters" - w szczególności, każda linia jest innym zdarzeniem.
	 */
    public void characters(char ch[], int start, int length) {
        for (int i = start; i < start + length; i++) {
            chars += ch[i];
        }
    }

    /**
	 * Tworzy nowy element z danych pobranych podczas parsowania i dodaje
	 * go do kanału.
	 *
	 * @return Utworzony element.
	 */
    private Item makeNewItem() {
        String id;
        if (guid != null && !"".equals(guid)) {
            id = guid;
        } else {
            if (description == null) {
                id = title.concat(channel.getId());
            } else if (description.length() > 32) {
                id = title.concat(channel.getId()).concat(description.substring(0, 32));
            } else {
                id = title.concat(channel.getId()).concat(description);
            }
        }
        Item item = new Item(new String(id), new String(channel.getId()));
        if (title == null) {
            item.setTitle(null);
        } else {
            item.setTitle(new String(title));
        }
        if (link == null) {
            item.setLink(null);
        } else {
            item.setLink(new String(link));
        }
        if (description == null) {
            item.setDescription(null);
        } else {
            item.setDescription(new String(description));
        }
        if (author == null) {
            item.setAuthor(null);
        } else {
            item.setAuthor(new String(author));
        }
        item.setDate((Date) date.clone());
        item.markAsUnread();
        if (!channel.getItems().contains(item.getId())) {
            channel.addItem(item.getId());
            downloadedItems.add(item);
        }
        return item;
    }

    /**
	 * Zwraca stronę główną z podanego adresu podstrony.<br>
	 * Na przykład, jeśli podano "http://delicious.com/network/username", zwraca
	 * napis "http://delicious.com".
	 *
	 * @return Napis będący adresem do strony głównej.
	 */
    private static String getHome(String URL) {
        if (URL.startsWith("http://")) {
            return "http://" + URL.substring(7).replaceAll("/.*", "");
        } else {
            return URL.replaceAll("/.*", "");
        }
    }
}
