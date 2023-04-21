package org.xith3d.loaders.models.impl.tds.internal;

import java.io.IOException;

/**
 * A processor to handle hiearchy link chunk
 * 
 * @author Kevin Glass
 * @author Marvin Froehlich (aka Qudus) [code cleaning]
 */
public class HLinkProcessor implements ChunkProcessor {

    static {
        HLinkProcessor single = new HLinkProcessor();
        TDSFile.addChunkProcessor(ChunkType.S3D_HIERARCHY_LINK, single);
    }

    public HLinkProcessor() {
    }

    public void process(TDSFile file, ModelContext context, int length) throws IOException {
        context.nodeName = file.readName();
        file.readUnsignedShort();
        file.readUnsignedShort();
        file.readUnsignedShort();
    }
}
