package repast.simphony.demo.surface;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.StrictBorders;
import repast.simphony.valueLayer.GridValueLayer;

/**
 * @author Eric Tatara
 */
public class SurfaceBuilder implements ContextBuilder {

    public Context build(Context context) {
        Parameters p = RunEnvironment.getInstance().getParameters();
        Integer width = (Integer) p.getValue("width");
        Integer height = (Integer) p.getValue("height");
        Grid grid = GridFactoryFinder.createGridFactory(null).createGrid("Grid", context, GridBuilderParameters.singleOccupancy2D(new RandomGridAdder(), new StrictBorders(), width, height));
        GridValueLayer vl = new GridValueLayer("Surface", true, width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double z = Math.sin((double) x / 10) * Math.sin((double) y / 10);
                vl.set(z, x, y);
            }
        }
        context.addValueLayer(vl);
        context.add(new WaveGenerator());
        return context;
    }
}
