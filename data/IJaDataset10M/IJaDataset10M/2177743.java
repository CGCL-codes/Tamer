package dmedv.grids;

public class RgbDataGrid extends DataGrid<Integer> {

    public RgbDataGrid(Double[] x, Double[] y) {
        super(x, y);
    }

    public RgbDataGrid(XYGrid grid) {
        super(grid);
    }

    public int GetInterpolated(double x, double y) {
        int[] ia = new int[2];
        int[] ja = new int[2];
        if (Interpolation.FindNeighbours(X, new Double(x), ia) && Interpolation.FindNeighbours(Y, new Double(y), ja)) {
            double x1 = X[ia[0]];
            double x2 = X[ia[1]];
            double y1 = Y[ja[0]];
            double y2 = Y[ja[1]];
            int f11 = GetValue(ia[0], ja[0]);
            int f21 = GetValue(ia[1], ja[0]);
            int f1 = Interpolation.LinearRgb(x1, x2, x, f11, f21);
            int f12 = GetValue(ia[0], ja[1]);
            int f22 = GetValue(ia[1], ja[1]);
            int f2 = Interpolation.LinearRgb(x1, x2, x, f12, f22);
            return Interpolation.LinearRgb(y1, y2, y, f1, f2);
        } else return 0;
    }

    public void InterpolateFrom(RgbDataGrid source) {
        for (int i = 0; i < X.length; i++) {
            int[] ia = new int[2];
            if (!Interpolation.FindNeighbours(source.X, new Double(X[i]), ia)) continue;
            double x1 = source.X[ia[0]];
            double x2 = source.X[ia[1]];
            for (int j = 0; j < Y.length; j++) {
                int[] ja = new int[2];
                if (!Interpolation.FindNeighbours(source.Y, Y[j], ja)) continue;
                double y1 = source.Y[ja[0]];
                double y2 = source.Y[ja[1]];
                int f11 = source.GetValue(ia[0], ja[0]);
                int f21 = source.GetValue(ia[1], ja[0]);
                int f1 = Interpolation.LinearRgb(x1, x2, X[i], f11, f21);
                int f12 = source.GetValue(ia[0], ja[1]);
                int f22 = source.GetValue(ia[1], ja[1]);
                int f2 = Interpolation.LinearRgb(x1, x2, X[i], f12, f22);
                SetValue(i, j, Interpolation.LinearRgb(y1, y2, Y[j], f1, f2));
            }
        }
    }
}
