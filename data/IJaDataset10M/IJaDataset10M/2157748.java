package game.visualizations.structure;

import java.awt.Color;
import java.awt.Paint;
import org.apache.commons.collections15.Transformer;
import com.rapidminer.gui.plotter.ColorProvider;
import com.rapidminer.gui.tools.SwingTools;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import game.models.Model;
import game.visualizations.tools.jfree.ColorHelper;

public class GameStructureVertexPaintTransformer implements Transformer<Object, Paint> {

    GameStructureGraphCreator creator;

    VisualizationViewer<Object, Integer> viewer;

    public GameStructureVertexPaintTransformer(GameStructureGraphCreator creator, VisualizationViewer<Object, Integer> viewer) {
        this.creator = creator;
        this.viewer = viewer;
    }

    public GameStructureVertexPaintTransformer(GameStructureGraphCreator creator) {
        this.creator = creator;
        this.viewer = null;
    }

    @Override
    public Paint transform(Object vertex) {
        if (viewer != null && viewer.getPickedVertexState().isPicked(vertex)) {
            return SwingTools.LIGHT_YELLOW;
        }
        if (creator.isClassification() && vertex instanceof Model) {
            Color[] c = new Color[2];
            c[0] = new ColorProvider().getPointColor(creator.getClassFraction(((Model) vertex).getTargetVariable()), 25);
            c[1] = Color.WHITE;
            return ColorHelper.mixColors(c);
        }
        if (vertex instanceof Output) {
            return Color.LIGHT_GRAY;
        }
        return SwingTools.LIGHT_BLUE;
    }
}
