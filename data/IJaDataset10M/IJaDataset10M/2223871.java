package gdi1sokoban.graphic;

import gdi1sokoban.graphic.base.ModelDescriptor;
import gdi1sokoban.graphic.base.ModelManager;
import gdi1sokoban.logic.Position;
import java.util.LinkedList;
import org.lwjgl.opengl.GL11;

public class Path {

    LinkedList<Position> _positions;

    ModelManager.Resource _highlight;

    float _centerX, _centerY;

    public Path(ModelManager.Resource highlight, LinkedList<Position> positions, float centerX, float centerY) throws Exception {
        _positions = positions;
        _centerX = centerX;
        _centerY = centerY;
        _highlight = highlight;
    }

    public void setPosition(LinkedList<Position> positions) {
        _positions = positions;
    }

    public void render() {
        for (Position position : _positions) {
            GL11.glPushMatrix();
            GL11.glTranslated(position.getX() - _centerX, 0, position.getY() - _centerY);
            _highlight.getInstance().render();
            GL11.glPopMatrix();
        }
    }
}
