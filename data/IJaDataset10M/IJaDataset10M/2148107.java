package edu.ucsd.osdt.util;

import com.rbnb.sapi.ChannelMap;
import java.util.Hashtable;
import java.util.logging.Logger;

public abstract class MetaDataParser extends Hashtable {

    protected Logger logger;

    protected ChannelMap cmap = null;

    protected String[] channels = null;

    protected String[] units = null;
}
