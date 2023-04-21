package org.jfge.games.mk2.effect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jfge.api.effect.CollisionEffect;
import org.jfge.api.effect.CollisionEffectFactory;
import org.jfge.spi.graphics.GraphicsFactory;
import org.jfge.spi.graphics.Image;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * The Class LowHitBlood.
 */
public final class LowHitBlood implements Provider<CollisionEffect> {

    /** The images. */
    private List<Image> images;

    /** The collision effect factory. */
    private CollisionEffectFactory collisionEffectFactory;

    /** The graphics factory. */
    private GraphicsFactory graphicsFactory;

    /**
	 * Instantiates a new low hit blood.
	 *
	 * @param graphicsFactory the graphics factory
	 * @param collisionEffectFactory the collision effect factory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Inject
    public LowHitBlood(GraphicsFactory graphicsFactory, CollisionEffectFactory collisionEffectFactory) throws IOException {
        this.graphicsFactory = graphicsFactory;
        this.collisionEffectFactory = collisionEffectFactory;
    }

    @Override
    public CollisionEffect get() {
        if (this.images == null) {
            this.images = new ArrayList<Image>();
            try {
                this.images.add(graphicsFactory.createImage("/org/jfge/games/mk2/effect/images/low_hit_blood_1.png"));
                this.images.add(graphicsFactory.createImage("/org/jfge/games/mk2/effect/images/low_hit_blood_2.png"));
                this.images.add(graphicsFactory.createImage("/org/jfge/games/mk2/effect/images/low_hit_blood_3.png"));
                this.images.add(graphicsFactory.createImage("/org/jfge/games/mk2/effect/images/low_hit_blood_4.png"));
                this.images.add(graphicsFactory.createImage("/org/jfge/games/mk2/effect/images/low_hit_blood_5.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.collisionEffectFactory.createCollisionEffect(images, 0.3, 0.7);
    }
}
