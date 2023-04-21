package net.sf.isnake.codec;

import net.sf.isnake.codec.clientEncoder.*;
import net.sf.isnake.codec.serverDecoder.*;
import net.sf.isnake.codec.clientDecoder.*;
import net.sf.isnake.codec.serverEncoder.*;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**<p>The isnake Codec Factory.</p>
 * <p> All the encoders and decoders are registered in it and provides a proper 
 * encoder/decoder for server/client whenever required.</p>
 *
 *@author Suraj Sapkota [ ssapkota<at>gmail<dot>com ]
 *@version $Id: IsnakeProtocolCodecFactory.java 130 2008-04-28 16:28:12Z sursata $
 */
public class IsnakeProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    public IsnakeProtocolCodecFactory(boolean server) {
        if (server) {
            super.register(ChatServerEncoder.class);
            super.register(ControlSignalServerEncoder.class);
            super.register(InformationServerEncoder.class);
            super.register(LevelServerEncoder.class);
            super.register(MoveServerEncoder.class);
            super.register(ChatServerDecoder.class);
            super.register(ControlSignalServerDecoder.class);
            super.register(InformationServerDecoder.class);
            super.register(MoveServerDecoder.class);
        } else {
            super.register(ChatClientDecoder.class);
            super.register(ControlSignalClientDecoder.class);
            super.register(InformationClientDecoder.class);
            super.register(LevelClientDecoder.class);
            super.register(MoveClientDecoder.class);
            super.register(ChatClientEncoder.class);
            super.register(ControlSignalClientEncoder.class);
            super.register(InformationClientEncoder.class);
            super.register(MoveClientEncoder.class);
        }
    }
}
