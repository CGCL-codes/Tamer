package edu.ucsd.osdt.source.numeric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.ucsd.osdt.util.MetaDataParser;
import edu.ucsd.osdt.util.ISOtoRbnbTime;
import com.rbnb.sapi.ChannelMap;
import com.rbnb.sapi.SAPIException;

public class LoggerNetParser extends MetaDataParser {

    public LoggerNetParser() {
        super();
        logger = Logger.getLogger(getClass().getName());
    }

    public ChannelMap getCmap() {
        return (ChannelMap) this.get("cmap");
    }

    public String[] getChannels() {
        return (String[]) this.get("channels");
    }

    public String[] getUnits() {
        return (String[]) this.get("units");
    }

    public boolean parse(String cmdFromInstr) throws IOException, SAPIException {
        BufferedReader cmdReader = new BufferedReader(new StringReader(cmdFromInstr));
        String channelsString = cmdReader.readLine();
        logger.finer("channelsString: " + channelsString);
        String[] channelsTmp = channelsString.split(",");
        channels = new String[channelsTmp.length];
        Pattern pattern = Pattern.compile("\"(.*)\"", Pattern.DOTALL);
        Matcher matcher = null;
        for (int i = 0; i < channelsTmp.length; i++) {
            matcher = pattern.matcher(channelsTmp[i]);
            if (matcher.find()) {
                channels[i] = matcher.group(1).trim();
                logger.finer(channels[i]);
            }
        }
        this.cmap = new ChannelMap();
        for (int i = 0; i < channelsTmp.length; i++) {
            this.cmap.Add(channels[i]);
            this.cmap.PutMime(cmap.GetIndex(channels[i]), "application/octet-stream");
        }
        this.put("channels", channels);
        this.put("cmap", cmap);
        return true;
    }

    public double getRbnbTimestamp(String loggernetDate) {
        String[] loggernetDateTokens = loggernetDate.split(" ");
        StringBuffer retval = new StringBuffer();
        retval.append(loggernetDateTokens[0]);
        retval.append("T");
        retval.append(loggernetDateTokens[1]);
        retval.append(".00000");
        String iso8601String = retval.toString();
        logger.fine("ISO8601:" + iso8601String);
        ISOtoRbnbTime rbnbTimeConvert = new ISOtoRbnbTime(iso8601String);
        return rbnbTimeConvert.getValue();
    }
}
