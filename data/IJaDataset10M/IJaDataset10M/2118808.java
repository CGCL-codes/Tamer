package failure.wm.business.animations.layer0;

import failure.wm.business.animations.*;
import failure.wm.business.*;

/**
 * @author Higurashi
 * @version WM 1.0
 * @since WM 1.0
 */
public class WaObject1Part0 extends WorldAnimation {

    protected boolean passing;

    protected BlockType[] blocks = null;

    @Override
    public void build() throws WorldException {
        int[] delays = { 200, 200, 200, 200 };
        this.blocks = new BlockType[4];
        this.blocks[0] = BlockType.animation_object_1_f1;
        this.blocks[1] = BlockType.animation_object_1_f2;
        this.blocks[2] = BlockType.animation_object_1_f3;
        this.blocks[3] = BlockType.animation_object_1_f4;
        this.setAnimatedBlock(0, 0, this.blocks, delays, this.passing);
    }
}
