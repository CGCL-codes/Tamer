package net.sf.jumle.core.entities.shapes.usecase;

import java.util.LinkedList;
import net.sf.jumle.core.entities.BasicShape;
import net.sf.jumle.core.entities.GraphicsAdapter;
import net.sf.jumle.core.entities.Shape;
import net.sf.jumle.core.types.Color;
import net.sf.jumle.core.types.Rectangle;
import net.sf.jumle.core.types.Size;

public class ActorShape extends BasicShape {

    private LinkedList<String> textKeys;

    public ActorShape() {
        textKeys = new LinkedList<String>();
        textKeys.add("Text");
    }

    @Override
    public String getName() {
        return "Actor";
    }

    private Size getActorFigureSize() {
        return new Size(35, 55);
    }

    @Override
    public Size size(GraphicsAdapter graphicsAdapter) {
        Size actorFigSize = getActorFigureSize();
        Size textSize = graphicsAdapter.textSize(text("Text"));
        int width = textSize.width;
        int height = actorFigSize.height + textSize.height;
        if (width < actorFigSize.width) width = actorFigSize.width;
        return new Size(width, height);
    }

    @Override
    public LinkedList<Rectangle> hitRegions(GraphicsAdapter graphicsAdapter) {
        Size shapeSize = size(graphicsAdapter);
        Size actorFigSize = getActorFigureSize();
        LinkedList<Rectangle> hitRectList = new LinkedList<Rectangle>();
        int textHeight = shapeSize.height - actorFigSize.height;
        Rectangle actorFigHitRect = new Rectangle();
        actorFigHitRect.x = getCenteredPosition().x - (actorFigSize.width / 2);
        actorFigHitRect.y = getCenteredPosition().y - (actorFigSize.height / 2) - (textHeight / 2);
        actorFigHitRect.width = actorFigSize.width;
        actorFigHitRect.height = actorFigSize.height;
        hitRectList.add(actorFigHitRect);
        Rectangle textHitRect = new Rectangle();
        textHitRect.x = getCenteredPosition().x - (shapeSize.width / 2);
        textHitRect.y = getCenteredPosition().y + (shapeSize.height / 2) - textHeight;
        textHitRect.width = shapeSize.width;
        textHitRect.height = textHeight;
        hitRectList.add(textHitRect);
        return hitRectList;
    }

    @Override
    public String getIconImageFilename() {
        return "actor.png";
    }

    @Override
    protected String getDefaultText(String key) {
        if (key.equals("Text")) return getName();
        return null;
    }

    @Override
    public LinkedList<String> textKeys() {
        return textKeys;
    }

    @Override
    protected Color getDefaultForegroundColor() {
        return Color.BLACK;
    }

    @Override
    protected Color getDefaultBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public void paint(GraphicsAdapter graphicsAdapter) {
        int borderThickness = 1;
        if (isSelected()) borderThickness = 2;
        Size size = size(graphicsAdapter);
        int centerX = getCenteredPosition().x;
        int centerY = getCenteredPosition().y;
        int textheight = size.height - getActorFigureSize().height;
        graphicsAdapter.setColor(getForegroundColor());
        graphicsAdapter.setStrokeThickness(borderThickness);
        graphicsAdapter.fillActorFigureShapeCentered(centerX, centerY - (textheight / 2), getActorFigureSize().width, getActorFigureSize().height, getBackgroundColor());
        graphicsAdapter.drawTextCentered(text("Text"), centerX, (centerY - (size.height / 2)) + size.height - (textheight / 2), GraphicsAdapter.ORIENTATION_CENTER);
    }

    @Override
    public Shape clone() {
        ActorShape newShape = new ActorShape();
        newShape.setCenteredPosition(getCenteredPosition().clone());
        newShape.setText("Text", text("Text"));
        newShape.setBackgroundColor(getBackgroundColor().clone());
        newShape.setForegroundColor(getForegroundColor().clone());
        return newShape;
    }
}
