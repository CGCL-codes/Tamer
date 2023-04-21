package jogamp.graph.font;

import java.util.ArrayList;
import jogamp.graph.geom.plane.Path2D;
import com.jogamp.graph.curve.OutlineShape;
import com.jogamp.graph.font.Font;
import com.jogamp.graph.geom.Vertex;
import com.jogamp.graph.geom.Vertex.Factory;

public interface FontInt extends Font {

    public interface Glyph extends Font.Glyph {

        public static final int ID_UNKNOWN = 0;

        public static final int ID_CR = 2;

        public static final int ID_SPACE = 3;

        public Path2D getPath();

        public Path2D getPath(float pixelSize);
    }

    public ArrayList<OutlineShape> getOutlineShapes(CharSequence string, float pixelSize, Factory<? extends Vertex> vertexFactory);
}
