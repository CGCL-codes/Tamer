package yager.meshfiles.tds;

import java.io.IOException;

/**
 * @author Ryan Hild (therealfreaker@sourceforge.net)
 */
public final class MaterialNameChunk extends Chunk {

    public final int id() {
        return 0xa000;
    }

    public final void read(TDSInputStream input, TDSModel model, int size) throws IOException {
        model.currentMaterial.setName(input.readName());
    }
}
