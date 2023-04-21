package com.es.state;

import com.es.components.gamelogic.Position;
import com.es.manager.GameInfo;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class Camera {

    public static final float MAX_SCALE = 1.2f;

    public static final float MIN_SCALE = 0.25f;

    private Rectangle cameraBounds;

    private float globalScale = 0.6f;

    private float width, height;

    private Position position;

    public Camera(int viewWidth, int viewHeight) {
        this.width = viewWidth;
        this.height = viewHeight;
        cameraBounds = new Rectangle(0, 0, viewWidth, viewHeight);
    }

    public void setPosition(Position position) {
        if (position != null) {
            this.position = position;
            refresh();
        }
    }

    public void setViewSize(float width, float height) {
        this.width = width;
        this.height = height;
        refresh();
    }

    public Position getPosition() {
        return position;
    }

    public Rectangle getNormalBounds() {
        return new Rectangle(0, 0, cameraBounds.getWidth(), cameraBounds.getHeight());
    }

    public Rectangle getBounds() {
        return cameraBounds;
    }

    public void setGlobalScale(float scale) {
        globalScale = scale;
        checkGlobalScaleSaturation();
        refresh();
    }

    public float getGlobalScale() {
        return globalScale;
    }

    private void checkGlobalScaleSaturation() {
        if (!GameInfo.getInstance().isDebugMode()) {
            if (globalScale > MAX_SCALE) {
                globalScale = MAX_SCALE;
            } else if (globalScale < MIN_SCALE) {
                globalScale = MIN_SCALE;
            }
        }
    }

    public void modifyGlobalScale(float modifyScaleBy) {
        globalScale += modifyScaleBy;
        checkGlobalScaleSaturation();
        refresh();
    }

    public void multiplyGlobalScale(float multiplier) {
        globalScale *= multiplier;
        checkGlobalScaleSaturation();
        refresh();
    }

    private void refresh() {
        cameraBounds.setSize(width / globalScale, height / globalScale);
        cameraBounds.setLocation(position.x - cameraBounds.getWidth() / 2, position.y - cameraBounds.getHeight() / 2);
    }

    public Vector2f getRenderPosition(float xPos, float yPos) {
        return new Vector2f((xPos - cameraBounds.getX()) * globalScale, (yPos - cameraBounds.getY()) * globalScale);
    }

    private float getDistanceGlobalScale(float distance) {
        return 1 + (globalScale - 1) * distance;
    }

    public Vector2f getRenderPositionDistanced(float xPos, float yPos, float distance) {
        float slowGlobal = getDistanceGlobalScale(distance);
        float slowWidth = width / slowGlobal;
        float slowHeight = width / slowGlobal;
        return new Vector2f((xPos - position.x + slowWidth / 2) * slowGlobal, (yPos - position.y + slowHeight / 2) * slowGlobal);
    }

    public Vector2f getShapeRenderPosition(float xPos, float yPos) {
        return new Vector2f((xPos - cameraBounds.getX()), (yPos - cameraBounds.getY()));
    }

    public float getScaledWidth() {
        return width / globalScale;
    }

    public float getScaledHeight() {
        return height / globalScale;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void drawImage(Image image, float xPos, float yPos, float scale, Color color) {
        float drawPosX = (xPos - cameraBounds.getX()) * globalScale;
        float drawPosY = (yPos - cameraBounds.getY()) * globalScale;
        float drawScale = scale * globalScale;
        float width = image.getWidth() * drawScale;
        float height = image.getHeight() * drawScale;
        if (drawPosX > -width && drawPosX < GameInfo.getInstance().getGameWidth() + width && drawPosY > -height && drawPosY < GameInfo.getInstance().getGameHeight() + height) {
            if (width > 1 && height > 1 && color.a > 0.0001f) {
                image.setCenterOfRotation(width / 2, height / 2);
                image.draw(drawPosX - width / 2, drawPosY - height / 2, drawScale, color);
            }
        }
    }

    public void drawImageDistanced(Image image, float xPos, float yPos, float scale, float distance, Color color) {
        Vector2f drawPos = getRenderPositionDistanced(xPos, yPos, distance);
        float drawScale = scale * getDistanceGlobalScale(distance);
        float width = image.getWidth() * drawScale;
        float height = image.getHeight() * drawScale;
        if (width > 1 && height > 1) {
            image.setCenterOfRotation(width / 2, height / 2);
            image.draw(drawPos.x - width / 2, drawPos.y - height / 2, drawScale, color);
        }
    }

    public void drawShape(Shape shape, Color color, Graphics gr) {
        Vector2f drawPos = getShapeRenderPosition(shape.getCenterX(), shape.getCenterY());
        gr.setColor(color);
        Shape renderShape = shape.transform(Transform.createTranslateTransform((-shape.getCenterX() + drawPos.x), (-shape.getCenterY() + drawPos.y))).transform(Transform.createScaleTransform(globalScale, globalScale));
        gr.draw(renderShape);
    }

    public void fillShape(Shape shape, Color color, Graphics gr) {
        Vector2f drawPos = getShapeRenderPosition(shape.getCenterX(), shape.getCenterY());
        gr.setColor(color);
        Shape renderShape = shape.transform(Transform.createTranslateTransform((-shape.getCenterX() + drawPos.x), (-shape.getCenterY() + drawPos.y))).transform(Transform.createScaleTransform(globalScale, globalScale));
        gr.fill(renderShape);
    }
}
