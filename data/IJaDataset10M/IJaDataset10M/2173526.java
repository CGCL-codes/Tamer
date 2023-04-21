package net.todd.games.boardgame;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import com.sun.j3d.utils.geometry.Sphere;

public class SelectablePiece extends Shape3D {

    private static final float SHININESS = 128.0f;

    private static final Color3f BLACK = new Color3f(0.0f, 0.0f, 0.0f);

    private static final Color3f SPECULAR = new Color3f(0.9f, 0.9f, 0.9f);

    private final IPieceGroup piece;

    public SelectablePiece(IPieceGroup piece) {
        this.piece = piece;
        Color3f color = piece.getPieceInfo().getColor();
        Material matterial = new Material(BLACK, color, BLACK, SPECULAR, SHININESS);
        Appearance appearance = new Appearance();
        appearance.setMaterial(matterial);
        Sphere sphere = new Sphere(3f, 1, 50, appearance);
        setGeometry(sphere.getShape().getGeometry());
        setAppearance(sphere.getShape().getAppearance());
    }

    public IPieceGroup getPiece() {
        return piece;
    }
}
