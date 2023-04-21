package santjoans.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import santjoans.client.piezes.navigator.viewer.IControllerViewerContext;
import santjoans.client.util.IConfiguration;
import santjoans.client.util.PiezeClassEnum;

public class CenterModel extends Model implements IConfiguration {

    private int[] invalidStartX = new int[] { 23, 24, 26, 27, 28, 30, 31, 33, 34, 35 };

    private int[] invalidEndX = new int[] { 21, 22, 23, 25, 26, 28, 29, 30, 32, 33 };

    private int[] invalidStartY = new int[] { 9, 10, 12, 13, 15, 16, 17 };

    private int[] invalidEndY = new int[] { 7, 8, 9, 11, 12, 14, 15 };

    public CenterModel() {
        super(PiezeClassEnum.CENTER);
    }

    @Override
    public List<IModelEntry> queryByContext(Collection<IModelEntry> entries, IControllerViewerContext context) {
        List<IModelEntry> piezes = new ArrayList<IModelEntry>();
        for (IModelEntry entry : entries) {
            if (context.getStartX() < invalidStartX[entry.getX()] && context.getEndX() > invalidEndX[entry.getX()] && context.getStartY() < invalidStartY[entry.getY()] && context.getEndY() > invalidEndY[entry.getY()]) {
                piezes.add(entry);
            }
        }
        return piezes;
    }
}
