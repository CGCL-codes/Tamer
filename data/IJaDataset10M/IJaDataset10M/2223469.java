package test.car;

import com.threed.jpct.*;
import java.io.*;

/**
 * A skidmark is something the car produces while driving around
 */
public class SkidMark extends AbstractEntity {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final Object3D SKIDMARK;

    static {
        Texture skidmark = new Texture("textures" + File.separatorChar + "skidmark.jpg");
        TextureManager.getInstance().addTexture("skidmark", skidmark);
        SKIDMARK = Primitives.getPlane(1, 8);
        SKIDMARK.rotateX((float) Math.PI / 2f);
        SKIDMARK.rotateMesh();
        SKIDMARK.setTranslationMatrix(new Matrix());
        SKIDMARK.setTexture("skidmark");
        SKIDMARK.getMesh().compress();
        SKIDMARK.build();
    }

    /**
    * Creates a new skidmark
    */
    public SkidMark() {
        super(SKIDMARK);
        setVisibility(false);
        setTransparency(1);
    }

    /**
    * Places a skidmark. The skidmark has the same direction as the car.
    */
    public void place(int pos, Car car, SimpleVector sPos) {
        sPos = new SimpleVector(sPos);
        setRotationMatrix(car.getRotationMatrix().cloneMatrix());
        SimpleVector yAxis = getYAxis();
        SimpleVector xAxis = getXAxis();
        xAxis.scalarMul(pos * 8);
        yAxis.scalarMul(4f * 1.95f);
        sPos.add(xAxis);
        sPos.add(yAxis);
        setOrigin(sPos);
        setVisibility(true);
    }
}
