package erki.abcpeter.parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import erki.abcpeter.BotInterface;
import erki.abcpeter.Parser;
import erki.abcpeter.msgs.observers.TextMessageObserver;
import erki.abcpeter.msgs.receive.TextMessage;
import erki.abcpeter.msgs.response.ResponseMessage;
import erki.abcpeter.util.Logger;

/**
 * This {@link Parser} can look up things either using google or wikipedia.
 * 
 * @author Edgar Kalkowski
 */
public class LookUp implements Parser, TextMessageObserver {

    private BotInterface bot;

    @Override
    public void init(BotInterface bot) {
        this.bot = bot;
        bot.register(this);
    }

    @Override
    public LinkedList<ResponseMessage> inform(TextMessage message) {
        String google = "(" + bot.getName() + "|" + bot.getName().toLowerCase() + ")[:,]? ?([gG]oogle|([gG]uck|[sS]chau)( mal)? (bei|in) " + "[gG]oogle nach)[:;, ](.*)";
        if (message.getText().matches(google)) {
            String query = message.getText().replaceAll(google, "$6").trim();
            Logger.info(this, "Googling »" + query + "«.");
            query = query.replaceAll(" ", "+");
            try {
                Socket socket = new Socket("www.google.de", 80);
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ISO-8859-1"));
                BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1"));
                socketOut.write("GET /search?hl=de&q=" + query + "&btnG=Suche&meta= HTTP/1.1\r\nHost: " + "www.google.de\r\n\r\n");
                socketOut.flush();
                String response;
                while ((response = socketIn.readLine()) != null) {
                    if (response.contains("<div class=g>")) {
                        String[] split = response.split("hnliche Seiten");
                        LinkedList<ResponseMessage> result = new LinkedList<ResponseMessage>();
                        int pause = 1000;
                        for (String s : split) {
                            String urlR = "(.*?)<a href=\"([\\d\\s\\w:/\\" + "._öÖäÄüÜß\\-=&?;%]*?)\" class=l>(.*)";
                            String txtR = "(.*?)<div class=std>(.*?)" + "<b>...</b><br><span class=a(.*)";
                            String url = null, txt = null;
                            if (s.matches(urlR)) {
                                url = s.replaceAll(urlR, "$2");
                            }
                            if (s.matches(txtR)) {
                                txt = s.replaceAll(txtR, "$2");
                                txt = txt.replaceAll("</?b>", "");
                                txt = txt.replaceAll("</?em>", "");
                                txt = txt.trim() + " ...";
                            }
                            if (txt != null && url != null) {
                                result.add(new ResponseMessage(txt + " (" + url + ")", 100, 2000 + pause));
                                pause += 1000;
                            }
                            if (result.size() > 2) {
                                break;
                            }
                        }
                        return result;
                    }
                }
            } catch (UnknownHostException e) {
                LinkedList<ResponseMessage> result = new LinkedList<ResponseMessage>();
                result.add(new ResponseMessage("Ich kann leider im " + "Moment den Hostnamen www.google.de nicht auflösen.", 100, 3000));
                return result;
            } catch (IOException e) {
                LinkedList<ResponseMessage> result = new LinkedList<ResponseMessage>();
                result.add(new ResponseMessage("Ich kann leider im " + "Moment keine Verbindung zu Google herstellen.", 100, 3000));
                return result;
            }
        }
        String wikipedia = "(" + bot.getName() + "|" + bot.getName().toLowerCase() + ")[:,]? ?(([gG]uck|[sS]chau)( mal)? (bei|in|im) [wW]iki" + "(pedia)?( nach)?|[wW]iki(pedia)?)[:;, ](.*)";
        if (message.getText().matches(wikipedia)) {
            String query = message.getText().replaceAll(wikipedia, "$9").trim();
            Logger.info(this, "Looking up »" + query + "« in Wikipedia.");
            String request = query.replaceAll(" ", "_");
            try {
                Socket socket = new Socket("de.wikipedia.org", 80);
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                socketOut.write("GET /wiki/" + request + " HTTP/1.0\r\n" + "Host: de.wikipedia.org\r\n\r\n");
                socketOut.flush();
                String response, txt;
                LinkedList<ResponseMessage> result = new LinkedList<ResponseMessage>();
                LinkedList<String> paragraphs = new LinkedList<String>();
                boolean disambiguation = false;
                while ((response = socketIn.readLine()) != null) {
                    String notExist = "HTTP/1.0 403 Forbidden";
                    if (response.matches(notExist)) {
                        Logger.info(this, "Wikipedia has no article about " + query + ".");
                        socket.close();
                        result.add(new ResponseMessage("Über " + query + " existiert in der Wikipedia kein Artikel.", 100, 5000));
                        return result;
                    }
                    String content = "\\t?\\t?\\t?<p>(.*?)</p>";
                    String seeAlso = "<p><a name=\"Siehe_auch\" " + "id=\"Siehe_auch\"></a></p>";
                    if (response.matches(content) && !response.matches(seeAlso)) {
                        txt = response.replaceAll(content, "$1");
                        txt = txt.replaceAll("</?b>", "");
                        txt = txt.replaceAll("<a href=(.*?)>", "");
                        txt = txt.replaceAll("</a>", "");
                        txt = txt.replaceAll("</?sub>", "");
                        txt = txt.replaceAll("</?i>", "");
                        paragraphs.add(txt);
                    }
                    String list = "<li>(.*?)</li>";
                    if (response.matches(list)) {
                        txt = response.replaceAll(list, "$1");
                        String link = ".*?<a href=\"(.*?)\" (title=\".*?\">" + ".*?</a>.*|class=\".*?\">.*?</a>.*)";
                        link = txt.replaceAll(link, "$1");
                        Logger.debug(this, "Text: " + txt);
                        Logger.debug(this, "Link recognized: »" + link + "«");
                        link = "http://de.wikipedia.org" + link;
                        txt = txt.replaceAll("<a href=.*?>", "");
                        txt = txt.replaceAll("</a>", "");
                        Logger.debug(this, "Parsed Text: »" + txt + "«");
                        txt += " (" + link + ")";
                        paragraphs.add(txt);
                    }
                    String dis = "<div id=\"Vorlage_Begriffsklaerung\">";
                    if (response.matches(dis)) {
                        disambiguation = true;
                    }
                }
                if (disambiguation) {
                    int i = 1000;
                    for (String p : paragraphs) {
                        result.add(new ResponseMessage(p, 100, 5000 + i));
                        i += 1000;
                    }
                } else {
                    result.add(new ResponseMessage(paragraphs.getFirst(), 100, 5000));
                }
                socket.close();
                return result;
            } catch (UnknownHostException e) {
                LinkedList<ResponseMessage> result = new LinkedList<ResponseMessage>();
                result.add(new ResponseMessage("Ich kann leider im " + "Moment den Hostnamen de.wikipedia.org nicht " + "auflösen.", 100, 3000));
                return result;
            } catch (IOException e) {
                LinkedList<ResponseMessage> result = new LinkedList<ResponseMessage>();
                result.add(new ResponseMessage("Ich kann leider im " + "Moment keine Verbindung zur Wikipedia herstellen.", 100, 3000));
                return result;
            }
        }
        return null;
    }
}
