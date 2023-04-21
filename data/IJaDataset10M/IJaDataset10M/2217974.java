package vademecum.ui.visualizer.vgraphics.D3.surface.jRenderer3D;

class Transform {

    private int zOrientation = -1;

    Transform(int width, int height) {
        xs = (double) (width / 2. + 0.5);
        ys = (double) (height / 2. + 0.5);
        initializeTransformation();
    }

    ;

    protected void transform(SurfacePlotData plotItem) {
        y = plotItem.y;
        x = plotItem.x;
        z = plotItem.z;
        xyzPos();
    }

    protected void transform(Text3D textItem) {
        x = textItem.x;
        y = textItem.y;
        z = textItem.z;
        xyzPos();
    }

    protected void transform(double x_, double y_, double z_) {
        x = x_;
        y = y_;
        z = z_;
        xyzPos_noShift();
    }

    protected void transform(Point3D pt) {
        x = pt.x;
        y = pt.y;
        z = pt.z;
        xyzPos();
    }

    protected void setScale(double scale) {
        this.scale = scale;
        initializeTransformation();
    }

    protected void setZOrientation(int zOrientation) {
        if (zOrientation <= 0) this.zOrientation = -1; else this.zOrientation = 1;
        initializeTransformation();
    }

    protected void setZAspectRatio(double zAspectRatio) {
        this.zAspect = zAspectRatio;
        initializeTransformation();
    }

    protected void setRotationZ(double angleZ) {
        this.angleZ = angleZ;
        initializeTransformation();
    }

    protected void setRotationY(double angleY) {
        this.angleY = angleY;
        initializeTransformation();
    }

    protected void setRotationX(double angleX) {
        this.angleX = angleX;
        initializeTransformation();
    }

    protected void setRotationXYZ(double angleX, double angleY, double angleZ) {
        this.angleX = angleX;
        this.angleY = angleY;
        this.angleZ = angleZ;
        initializeTransformation();
    }

    protected void setRotationXZ(double angleX, double angleZ) {
        this.angleX = angleX;
        this.angleZ = angleZ;
        initializeTransformation();
    }

    protected double getRotationX() {
        return angleX;
    }

    protected double getRotationY() {
        return angleY;
    }

    protected double getRotationZ() {
        return angleZ;
    }

    protected void changeRotationXZ(double dx, double dz) {
        angleX += dx;
        angleZ += dz;
        initializeTransformation();
    }

    protected void changeRotationXYZ(double dx, double dy, double dz) {
        angleX += dx;
        angleY += dy;
        angleZ += dz;
        initializeTransformation();
    }

    protected void setOffsets(double xOff, double yOff, double zOff) {
        xO = -xOff;
        yO = -yOff;
        zO = -zOff;
        initializeTransformation();
    }

    private void initializeTransformation() {
        cosX = (double) Math.cos(angleX);
        sinX = (double) Math.sin(angleX);
        cosY = (double) Math.cos(angleY);
        sinY = (double) Math.sin(angleY);
        cosZ = (double) Math.cos(angleZ);
        sinZ = (double) Math.sin(angleZ);
        double s = 1;
        m[0][0] = s;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = s * xO;
        m[1][0] = 0;
        m[1][1] = s;
        m[1][2] = 0;
        m[1][3] = s * yO;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = s * zOrientation * zAspect;
        m[2][3] = s * zOrientation * zAspect * zO;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;
        mZ[0][0] = cosZ;
        mZ[0][1] = sinZ;
        mZ[0][2] = 0;
        mZ[0][3] = 0;
        mZ[1][0] = -sinZ;
        mZ[1][1] = cosZ;
        mZ[1][2] = 0;
        mZ[1][3] = 0;
        mZ[2][0] = 0;
        mZ[2][1] = 0;
        mZ[2][2] = 1;
        mZ[2][3] = 0;
        mZ[3][0] = 0;
        mZ[3][1] = 0;
        mZ[3][2] = 0;
        mZ[3][3] = 1;
        mY[0][0] = cosY;
        mY[0][1] = 0;
        mY[0][2] = -sinY;
        mY[0][3] = 0;
        mY[1][0] = 0;
        mY[1][1] = 1;
        mY[1][2] = 0;
        mY[1][3] = 0;
        mY[2][0] = sinY;
        mY[2][1] = 0;
        mY[2][2] = cosY;
        mY[2][3] = 0;
        mY[3][0] = 0;
        mY[3][1] = 0;
        mY[3][2] = 0;
        mY[3][3] = 1;
        mX[0][0] = 1;
        mX[0][1] = 0;
        mX[0][2] = 0;
        mX[0][3] = 0;
        mX[1][0] = 0;
        mX[1][1] = cosX;
        mX[1][2] = sinX;
        mX[1][3] = 0;
        mX[2][0] = 0;
        mX[2][1] = -sinX;
        mX[2][2] = cosX;
        mX[2][3] = 0;
        mX[3][0] = 0;
        mX[3][1] = 0;
        mX[3][2] = 0;
        mX[3][3] = 1;
        matProd(m_Z, mZ, m);
        matProd(m_YZ, mY, m_Z);
        matProd(m_XYZ, mX, m_YZ);
        a00 = m_XYZ[0][0];
        a01 = m_XYZ[0][1];
        a02 = m_XYZ[0][2];
        a03 = m_XYZ[0][3];
        a10 = m_XYZ[1][0];
        a11 = m_XYZ[1][1];
        a12 = m_XYZ[1][2];
        a13 = m_XYZ[1][3];
        a20 = m_XYZ[2][0];
        a21 = m_XYZ[2][1];
        a22 = m_XYZ[2][2];
        a23 = m_XYZ[2][3];
        matInv4(m_XYZInv, m_XYZ);
        ai00 = m_XYZInv[0][0];
        ai01 = m_XYZInv[0][1];
        ai02 = m_XYZInv[0][2];
        ai03 = m_XYZInv[0][3];
        ai10 = m_XYZInv[1][0];
        ai11 = m_XYZInv[1][1];
        ai12 = m_XYZInv[1][2];
        ai13 = m_XYZInv[1][3];
        ai20 = m_XYZInv[2][0];
        ai21 = m_XYZInv[2][1];
        ai22 = m_XYZInv[2][2];
        ai23 = m_XYZInv[2][3];
    }

    private final void xyzPos() {
        X = a00 * x + a01 * y + a02 * z + a03;
        Y = a10 * x + a11 * y + a12 * z + a13;
        Z = a20 * x + a21 * y + a22 * z + a23;
        double sz = scale * maxDistance / (maxDistance + perspective * Z);
        X = sz * X + xs;
        Y = sz * Y + ys;
    }

    private final void xyzPos_noShift() {
        X = a00 * x + a01 * y + a02 * z + a03;
        Y = a10 * x + a11 * y + a12 * z + a13;
        Z = a20 * x + a21 * y + a22 * z + a23;
    }

    private final void invxyzPos() {
        double sz = (maxDistance + perspective * Z) / (scale * maxDistance);
        X = (X - xs) * sz;
        Y = (Y - ys) * sz;
        x = ai00 * X + ai01 * Y + ai02 * Z + ai03;
        y = ai10 * X + ai11 * Y + ai12 * Z + ai13;
        z = ai20 * X + ai21 * Y + ai22 * Z + ai23;
    }

    void matInv4(double z[][], double u[][]) {
        int i, j, n, ii[] = new int[4];
        double f;
        double w[][] = new double[4][4];
        n = 4;
        matCopy4(w, u);
        matUnit4(z);
        for (i = 0; i < n; i++) {
            ii[i] = matge4(w, i);
            matXr4(w, i, ii[i]);
            for (j = 0; j < n; j++) {
                if (i == j) continue;
                f = -w[i][j] / w[i][i];
                matAc4(w, j, j, f, i);
                matAc4(z, j, j, f, i);
            }
        }
        for (i = 0; i < n; i++) matMc4(z, 1.0f / w[i][i], i);
        for (i = 0; i < n; i++) {
            j = n - i - 1;
            matXc4(z, j, ii[j]);
        }
    }

    int matge4(double p[][], int n) {
        double g, h;
        int m;
        m = n;
        g = p[n][n];
        g = (g < 0.0 ? -g : g);
        for (int i = n; i < 4; i++) {
            h = p[i][n];
            h = (h < 0.0 ? -h : h);
            if (h < g) continue;
            g = h;
            m = i;
        }
        return m;
    }

    void matCopy4(double z[][], double x[][]) {
        int i, j;
        for (i = 0; i < 4; i++) for (j = 0; j < 4; j++) z[i][j] = x[i][j];
    }

    void matUnit4(double z[][]) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) z[i][j] = 0.0f;
            z[i][i] = 1.0f;
        }
    }

    void matXc4(double z[][], int i, int j) {
        double t;
        if (i == j) return;
        for (int k = 0; k < 4; k++) {
            t = z[k][i];
            z[k][i] = z[k][j];
            z[k][j] = t;
        }
    }

    void matXr4(double z[][], int i, int j) {
        double t;
        if (i == j) return;
        for (int k = 0; k < 4; k++) {
            t = z[i][k];
            z[i][k] = z[j][k];
            z[j][k] = t;
        }
    }

    void matXtc4(double p[], double z[][], int n) {
        int i;
        for (i = 0; i < 4; i++) p[i] = z[i][n];
    }

    void matAc4(double z[][], int i, int j, double f, int k) {
        int l;
        for (l = 0; l < 4; l++) z[l][i] = z[l][j] + f * z[l][k];
    }

    void matMc4(double z[][], double f, int i) {
        int j;
        for (j = 0; j < 4; j++) z[j][i] *= f;
    }

    void matProd(double z[][], double u[][], double v[][]) {
        int i, j, k;
        for (i = 0; i < 4; i++) for (j = 0; j < 4; j++) {
            z[i][j] = 0.0f;
            for (k = 0; k < 4; k++) z[i][j] += u[i][k] * v[k][j];
        }
    }

    final void xyzPos(int[] xyz) {
        x = xyz[0];
        y = xyz[1];
        z = xyz[2];
        xyzPos();
    }

    final double getScalarProduct() {
        return cosZ * x + sinZ * y;
    }

    final void invxyzPosf(int[] XYZ) {
        X = XYZ[0];
        Y = XYZ[1];
        Z = XYZ[2];
        invxyzPos();
    }

    final void invxyzPosf(double[] XYZ) {
        X = XYZ[0];
        Y = XYZ[1];
        Z = XYZ[2];
        invxyzPos();
    }

    private double angleX = 0;

    private double angleY = 0;

    private double angleZ = 0;

    private double xs = 256;

    private double ys = 256;

    private double cosZ;

    private double sinZ;

    private double cosX;

    private double sinX;

    private double cosY;

    private double sinY;

    private double scale = 1;

    private double zAspect = 1;

    double m[][] = new double[4][4];

    double mX[][] = new double[4][4];

    double mY[][] = new double[4][4];

    double mZ[][] = new double[4][4];

    double mP[][] = new double[4][4];

    double m_Z[][] = new double[4][4];

    double m_YZ[][] = new double[4][4];

    double m_XYZ[][] = new double[4][4];

    double m_PXYZ[][] = new double[4][4];

    double m_XYZInv[][] = new double[4][4];

    double xO;

    double yO;

    double zO;

    double a00, a01, a02, a03;

    double a10, a11, a12, a13;

    double a20, a21, a22, a23;

    double ai00, ai01, ai02, ai03;

    double ai10, ai11, ai12, ai13;

    double ai20, ai21, ai22, ai23;

    double y, x, z;

    int[] xyz;

    double X, Y, Z;

    private double perspective = 0;

    private double maxDistance = 256;

    protected double getScale() {
        return scale;
    }

    protected double getZAspectRatio() {
        return zAspect;
    }

    protected int getZOrientation() {
        return zOrientation;
    }

    public void setPerspective(double perspective) {
        this.perspective = perspective;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public double getPerspective() {
        return perspective;
    }

    public double getMaxDistance() {
        return maxDistance;
    }
}
