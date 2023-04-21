package co.edu.unal.ungrid.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import co.edu.unal.ungrid.client.util.AbstractDimension;

public class DeformationField extends VoxelImage<Double> {

    public static final long serialVersionUID = 1L;

    private static class Factory extends ImageFactory<VoxelPlane<Double>> {

        protected DeformationField create() {
            return new DeformationField();
        }
    }

    static {
        ImageFactory.register("co.edu.unal.ungrid.image.DeformationField", new Factory());
    }

    public DeformationField() {
        super();
    }

    public DeformationField(final AbstractDimension<Integer> size, final Collection<Voxel<Double>> col) {
        super(size, col);
    }

    public DeformationField(final AbstractDimension<Integer> size, double[] fa) {
        super(size, fa);
    }

    public DeformationField(final AbstractDimension<Integer> size, int[] data) {
        super(size, data);
    }

    public DeformationField(final AbstractDimension<Integer> size, final Voxel<Double> clear) {
        super(size, clear);
    }

    public DeformationField(final AbstractDimension<Integer> size, final Voxel<Double>[] va) {
        super(size, va);
    }

    public DeformationField(final AbstractDimension<Integer> size) {
        super(size);
    }

    public DeformationField(final ArrayList<VoxelPlane<Double>> planes) {
        super(planes);
    }

    public DeformationField(final BufferedImage bi) {
        super(bi);
    }

    public DeformationField(int numPlanes) {
        super(numPlanes);
    }

    public DeformationField(final VoxelImage<Double> img) {
        super(img);
    }

    public DeformationField(final VoxelPlane<Double> plane) {
        super(plane);
    }

    public DeformationField(final VoxelPlane<Double>[] pa) {
        super(pa);
    }
}
