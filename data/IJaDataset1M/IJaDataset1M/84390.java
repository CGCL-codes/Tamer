package org.metastatic.rsync.v2;

import java.io.InputStream;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.metastatic.rsync.Configuration;
import org.metastatic.rsync.DataBlock;
import org.metastatic.rsync.Delta;
import org.metastatic.rsync.DeltaDecoder;
import org.metastatic.rsync.Offsets;
import org.metastatic.rsync.Util;

public class PlainDeltaDecoder extends DeltaDecoder {

    private static final Logger logger = Logger.getLogger(PlainDeltaDecoder.class.getName());

    private long offset;

    private Statistics stats;

    public PlainDeltaDecoder(Configuration config, InputStream in) {
        super(config, in);
        offset = 0;
        stats = new Statistics();
    }

    public void setStatistics(Statistics stats) {
        if (stats != null) this.stats = stats;
    }

    public Statistics getStatistics() {
        return stats;
    }

    public Delta read() throws IOException {
        int token = in.read() & 0xFF;
        token |= (in.read() & 0xFF) << 8;
        token |= (in.read() & 0xFF) << 16;
        token |= (in.read() & 0xFF) << 24;
        logger.debug("read token=" + token);
        if (token < 0) {
            long readOffset = (long) (-token - 1) * (long) config.blockLength;
            Offsets o = new Offsets(readOffset, offset, config.blockLength);
            logger.debug("decoded offsets=" + o);
            offset += config.blockLength;
            return o;
        } else if (token > 0) {
            byte[] buf = new byte[token];
            in.read(buf);
            DataBlock d = new DataBlock(offset, buf);
            logger.debug("decoded data block=" + d);
            offset += token;
            return d;
        } else return null;
    }
}
