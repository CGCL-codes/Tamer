package org.xith3d.loaders.models.impl.tds.internal;

import java.io.IOException;
import javax.vecmath.Point3f;

/**
 * A processor to handle the pivot information
 * 
 * @author Kevin Glass
 */
public class PivotProcessor implements ChunkProcessor {

    static {
        PivotProcessor single = new PivotProcessor();
        TDSFile.addChunkProcessor(ChunkType.S3D_PIVOT, single);
    }

    public PivotProcessor() {
    }

    public void process(TDSFile file, ModelContext context, int length) throws IOException {
        float x = file.readFloat();
        float y = file.readFloat();
        float z = file.readFloat();
        context.pivot = new Point3f(x, y, z);
        System.out.println("Read pivot: " + context.pivot);
    }
}
