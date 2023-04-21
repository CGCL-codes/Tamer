package channel.master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;

/**
 *
 * @author Michael Hanns
 *
 */
public class GrabMasterChannelData {

    public static String[][] getChannelData() {
        try {
            URL whatismyip = new URL("http://www.witna.co.uk/chanData/channelList.html");
            InputStream stream = whatismyip.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            String line = in.readLine();
            if (line == null) {
                stream.close();
                in.close();
                return new String[0][4];
            }
            int numChannels = Integer.parseInt(line);
            String[][] channels = new String[numChannels][4];
            for (int x = 0; x < numChannels; x++) {
                String chanData = in.readLine();
                if (chanData != null && isMasterChannelDataValid(chanData)) {
                    channels[x][0] = getServerIP(chanData);
                    for (int y = 1; y < 4; y++) {
                        channels[x][y] = readQuotes(chanData, y);
                    }
                } else {
                    System.out.println(chanData);
                }
            }
            stream.close();
            in.close();
            return channels;
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0][7];
        }
    }

    private static String getServerIP(String chanData) {
        StringTokenizer tkns = new StringTokenizer(chanData);
        return tkns.nextToken();
    }

    private static String readQuotes(String chanData, int quoteNo) {
        if (quoteNo > 4 || quoteNo < 1) {
            return "";
        }
        StringTokenizer tkns = new StringTokenizer(chanData);
        tkns.nextToken();
        int currentQuote = 1;
        boolean openQuote = false;
        String elem = "";
        while (tkns.hasMoreTokens()) {
            String token = tkns.nextToken();
            if (currentQuote != quoteNo) {
                if (!openQuote && token.startsWith("`")) {
                    openQuote = true;
                }
                if (openQuote && token.endsWith("`")) {
                    currentQuote++;
                    openQuote = false;
                }
            } else if (currentQuote == quoteNo) {
                if (!openQuote) {
                    if (token.startsWith("`")) {
                        openQuote = true;
                        elem = token.substring(1);
                        if (token.endsWith("`")) {
                            openQuote = false;
                            elem = elem.substring(0, token.length() - 2);
                            return elem;
                        }
                    } else if (token.endsWith("`")) {
                        openQuote = false;
                        elem += " " + elem.substring(0, token.length() - 2);
                        return elem;
                    }
                } else if (openQuote) {
                    if (token.endsWith("`")) {
                        openQuote = false;
                        if (elem.length() > 1) {
                            elem += " " + token.substring(0, token.length() - 1);
                        }
                        return elem;
                    } else {
                        elem += " " + token;
                    }
                }
            }
        }
        return "";
    }

    private static boolean isMasterChannelDataValid(String chanData) {
        int quoteCount = 0;
        for (int x = 0; x < chanData.length(); x++) {
            if (chanData.charAt(x) == '`') {
                quoteCount++;
            }
        }
        if (quoteCount == 6) {
            return true;
        }
        System.out.println("Quote count:" + quoteCount);
        return false;
    }

    public static String getMasterChannelIP() {
        try {
            URL whatismyip = new URL("http://www.witna.co.uk/chanDets/officialIP.html");
            InputStream stream = whatismyip.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            String ip = in.readLine();
            stream.close();
            in.close();
            if (ip != null) {
                return ip;
            }
            return "Can't get master channel ip!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Can't get master channel ip!";
        }
    }

    public static void main(String[] args) {
        getChannelData();
    }
}
