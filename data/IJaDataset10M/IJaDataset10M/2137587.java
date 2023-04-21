package failure.wm.business.elements.layer0;

import failure.wm.business.*;
import failure.wm.business.elements.*;

/**
 * @author Higurashi
 * @version WM 1.0
 * @since WM 1.0
 */
public class WeNature2Part0 extends WorldElement {

    @Override
    public void build() throws WorldException {
        this.setBlock(0, 4, BlockType.nature_2_04, true);
        this.setBlock(1, 4, BlockType.nature_2_14, true);
        this.setBlock(2, 4, BlockType.nature_2_24, true);
        this.setBlock(3, 4, BlockType.nature_2_34, true);
    }
}
