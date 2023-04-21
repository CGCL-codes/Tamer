package photospace.graphics;

import java.awt.image.*;

public interface Sampler {

    BufferedImage scale(BufferedImage image, Transform transform);

    BufferedImage rotate(BufferedImage image, float degrees);
}
