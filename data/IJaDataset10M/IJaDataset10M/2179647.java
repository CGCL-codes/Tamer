package com.marcogaratti.jstrips;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ComicsGrabber {

    private final String GIF_MIME_TYPE = "image/gif";

    private boolean gifSupported;

    private Controller controller;

    public ComicsGrabber(Controller controller) {
        String mediaTypes[];
        mediaTypes = Manager.getSupportedContentTypes(null);
        int count = mediaTypes.length;
        gifSupported = false;
        for (int i = 0; i < count; i++) {
            if (mediaTypes[i] == GIF_MIME_TYPE) gifSupported = true;
        }
        this.controller = controller;
    }

    public Image getComicOfTheDay(int id) throws IOException {
        Image image = null;
        switch(id) {
            case Controller.DILBERT:
                image = getDilbert();
                break;
            case Controller.GARFIELD:
                image = getGarfield();
                break;
            case Controller.ANDYCAPP:
                image = getAndyCapp();
                break;
            case Controller.BC:
                image = getBC();
                break;
            case Controller.BORNLOSER:
                image = getBornLoser();
                break;
            case Controller.DRABBLE:
                image = getDrabble();
                break;
            case Controller.FERDNAND:
                image = getFerdnand();
                break;
            case Controller.FLOANDFRIENDS:
                image = getFloAndFriends();
                break;
            case Controller.FRAZZ:
                image = getFrazz();
                break;
            case Controller.GETFUZZY:
                image = getGetFuzzy();
                break;
            case Controller.GRANDAVENUE:
                image = getGrandAvenue();
                break;
            case Controller.LOLA:
                image = getLola();
                break;
            case Controller.LUANN:
                image = getLuann();
                break;
            case Controller.MARMADUKE:
                image = getMarmaduke();
                break;
            case Controller.MOMMA:
                image = getMomma();
                break;
            case Controller.SHIRLEY:
                image = getShirley();
                break;
            case Controller.SINGLELOOKING:
                image = getSingleLooking();
                break;
            case Controller.SOUPTONUTZ:
                image = getSoupToNutz();
                break;
            case Controller.UNION:
                image = getUnion();
                break;
            case Controller.SUNSHINECLUB:
                image = getSunshineClub();
                break;
            case Controller.OFFTHEMARK:
                image = getOffTheMark();
                break;
            case Controller.ONEBIGHAPPY:
                image = getOneBigHappy();
                break;
            case Controller.PEANUTS:
                image = getPeanuts();
                break;
            case Controller.PEARLS:
                image = getPearls();
                break;
            case Controller.PICKLES:
                image = getPickles();
                break;
            case Controller.ROSEISROSE:
                image = getRoseIsRose();
                break;
            case Controller.RUDYPARK:
                image = getRudyPark();
                break;
            case Controller.XKCD:
                image = getXkcd();
                break;
        }
        return image;
    }

    private Image getDilbert() throws IOException {
        String dilbertUrl = "http://www.dilbert.com";
        String page = loadHtmlPage(dilbertUrl);
        int idx = page.indexOf("ALT=\"Today's Comic\"");
        String gifAddress = "";
        while (idx > 0) {
            char ch = page.charAt(--idx);
            gifAddress = ch + gifAddress;
            if (gifAddress.startsWith("IMG SRC")) {
                break;
            }
        }
        gifAddress = gifAddress.substring(9, gifAddress.length() - 2);
        return getGifImage("http://www.dilbert.com" + gifAddress);
    }

    private String loadHtmlPage(String url) throws IOException {
        HttpConnection connection = null;
        InputStream istream = null;
        StringBuffer page = new StringBuffer();
        try {
            controller.showStatus("Connecting");
            connection = (HttpConnection) Connector.open(url);
            istream = connection.openInputStream();
            long len = connection.getLength();
            int ch;
            if (len != -1) {
                for (int i = 0; i < len; i++) {
                    if ((ch = istream.read()) != -1) {
                        controller.showStatus("Reading page (" + (i * 100) / len + "%)");
                        page.append((char) ch);
                    }
                }
            } else {
                controller.showStatus("Reading page (unknown size)");
                while ((ch = istream.read()) != -1) {
                    page.append((char) ch);
                }
            }
        } catch (IOException ex) {
            if (istream != null) istream.close();
            if (connection != null) connection.close();
            throw ex;
        }
        if (istream != null) istream.close();
        if (connection != null) connection.close();
        return page.toString();
    }

    private Image getGarfield() throws IOException {
        Date now = new Date();
        TimeZone tz = TimeZone.getTimeZone("PST");
        Calendar calendar = Calendar.getInstance(tz);
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String address = createGarfieldName(year, month + 1, day);
        controller.showStatus("Downloading image");
        return getGifImage(address);
    }

    private Image getAndyCapp() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("creators", "andycapp");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getBC() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("creators", "bc");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getBornLoser() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "bornloser");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getDrabble() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "drabble");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getFerdnand() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "ferdnand");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getFloAndFriends() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("creators", "floandfriends");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getFrazz() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "frazz");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getGetFuzzy() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "getfuzzy");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getGrandAvenue() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "grandave");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getLola() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "lola");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getLuann() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "luann");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getMarmaduke() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "marmaduke");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getLila() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("creators", "lila");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getMomma() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("creators", "momma");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getShirley() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "shirleynson");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getSingleLooking() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("wash", "singlelooking");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getSoupToNutz() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "soup2nutz");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getUnion() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("creators", "union");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getSunshineClub() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "sunshineclub");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getOffTheMark() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "offthemark");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getOneBigHappy() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("creators", "onebighappy");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getPeanuts() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "peanuts");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getPearls() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "pearls");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getPickles() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("wash", "pickles");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getRoseIsRose() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "roseisrose");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getRudyPark() throws IOException {
        String gifAddress = getGifAddressFromComicsCom("comics", "rudypark");
        return getGifImage("http://www.comics.com" + gifAddress);
    }

    private Image getXkcd() throws IOException {
        String page = loadHtmlPage("http://www.xkcd.com");
        String imageAddress = null;
        String tag = "Image URL (for hotlinking/embedding):";
        int startIdx = page.indexOf(tag);
        if (startIdx >= 0) {
            int endIdx = page.indexOf("</h3>", startIdx);
            if (endIdx >= 0) {
                imageAddress = page.substring(startIdx + tag.length(), endIdx);
            }
        }
        if (imageAddress != null) {
            imageAddress = imageAddress.trim();
            return getPngImage(imageAddress);
        } else {
            return null;
        }
    }

    private String getGifAddressFromComicsCom(String dir, String name) throws IOException {
        String url = "http://www.comics.com/" + dir + "/" + name + "/index.html";
        String page = loadHtmlPage(url);
        String tag = "<IMG SRC=\"/" + dir + "/" + name + "/archive/images/" + name;
        int idx = page.indexOf(tag);
        if (idx >= 0) {
            String gifAddress = page.substring(idx + 10);
            idx = gifAddress.indexOf(".gif");
            gifAddress = gifAddress.substring(0, idx + 4);
            return gifAddress;
        } else {
            throw new IOException("Cannot find image tag");
        }
    }

    private String createGarfieldName(int year, int month, int day) {
        StringBuffer baseAddress = new StringBuffer("http://images.ucomics.com/comics/ga/");
        baseAddress.append(year);
        baseAddress.append("/ga");
        StringBuffer address = new StringBuffer(baseAddress.toString());
        year = year - ((year / 100) * 100);
        if (year < 10) address.append("0");
        address.append(year);
        if (month < 10) address.append("0");
        address.append(month);
        if (day < 10) address.append("0");
        address.append(day);
        address.append(".gif");
        System.out.println("Garfield GIF address: " + address.toString());
        return address.toString();
    }

    private Image getGifImage(String address) throws IOException {
        HttpConnection connection = (HttpConnection) Connector.open(address);
        InputStream istream = connection.openInputStream();
        long len = connection.getLength();
        controller.showStatus("Downloading image");
        if (gifSupported) {
            Image mapImage = Image.createImage(istream);
            return mapImage;
        } else {
            GifDecoder decoder = new GifDecoder(controller);
            decoder.read(istream, len);
            return decoder.getImage();
        }
    }

    private Image getPngImage(String address) throws IOException {
        HttpConnection connection = (HttpConnection) Connector.open(address);
        InputStream istream = connection.openInputStream();
        long len = connection.getLength();
        Image mapImage = null;
        if (len > 100) {
            controller.showStatus("Reading strip (0%)");
            byte[] data = new byte[(int) len];
            int progress = -1;
            int b = -1;
            int i = 0;
            do {
                b = istream.read();
                if (b != -1) {
                    data[i++] = (byte) b;
                }
                int newProgress = (i * 100) / (int) len;
                if (newProgress != progress) {
                    controller.showStatus("Reading strip (" + progress + "%)");
                    progress = newProgress;
                }
            } while (b != -1);
            mapImage = Image.createImage(data, 0, (int) len - 1);
        } else {
            controller.showStatus("Reading strip (...)");
            mapImage = Image.createImage(istream);
        }
        return mapImage;
    }
}
