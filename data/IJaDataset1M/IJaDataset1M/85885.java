package gov.sns.xal.tools.simulationmanager;

public class TwissFastData {

    String id;

    double x, y, z;

    double xp, yp, zp;

    double ax, ay, az;

    double bx, by, bz;

    double ex, ey, ez;

    double mux, muy, muz;

    double sx, sy, sz;

    double etx, ety;

    double etpx, etpy;

    double w;

    double s;

    String shape;

    double apx, apy;

    public TwissFastData(String id_, double x_, double y_, double z_, double xp_, double yp_, double zp_, double ax_, double ay_, double az_, double bx_, double by_, double bz_, double ex_, double ey_, double ez_, double mux_, double muy_, double muz_, double sx_, double sy_, double sz_, double etx_, double ety_, double etpx_, double etpy_, double w_, double s_, String shape_, double apx_, double apy_) {
        id = id_;
        x = x_;
        y = y_;
        z = z_;
        xp = xp_;
        yp = yp_;
        zp = zp_;
        ax = ax_;
        ay = ay_;
        az = az_;
        bx = bx_;
        by = by_;
        bz = bz_;
        ex = ex_;
        ey = ey_;
        ez = ez_;
        mux = mux_;
        muy = muy_;
        muz = muz_;
        sx = sx_;
        sy = sy_;
        sz = sz_;
        etx = etx_;
        ety = ety_;
        etpx = etpx_;
        etpy = etpy_;
        w = w_;
        s = s_;
        shape = shape_;
        apx = apx_;
        apy = apy_;
    }

    public TwissFastData() {
        id = "";
        x = y = z = 0;
        xp = yp = zp = 0;
        ax = ay = az = 0;
        bx = by = bz = 0;
        ex = ey = ez = 0;
        mux = muy = muz = 0;
        sx = sy = sz = 0;
        etx = ety = 0;
        etpx = etpy = 0;
        w = 0;
        s = 0;
        shape = "";
        apx = apy = 0;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getXp() {
        return xp;
    }

    public double getYp() {
        return yp;
    }

    public double getZp() {
        return zp;
    }

    public double getAx() {
        return ax;
    }

    public double getAy() {
        return ay;
    }

    public double getAz() {
        return az;
    }

    public double getBx() {
        return bx;
    }

    public double getBy() {
        return by;
    }

    public double getBz() {
        return bz;
    }

    public double getEx() {
        return ex;
    }

    public double getEy() {
        return ey;
    }

    public double getEz() {
        return ez;
    }

    public double getMux() {
        return mux;
    }

    public double getMuy() {
        return muy;
    }

    public double getMuz() {
        return muz;
    }

    public double getSx() {
        return sx;
    }

    public double getSy() {
        return sy;
    }

    public double getSz() {
        return sz;
    }

    public double getEtx() {
        return etx;
    }

    public double getEty() {
        return ety;
    }

    public double getEtpx() {
        return etpx;
    }

    public double getEtpy() {
        return etpy;
    }

    public double getW() {
        return w;
    }

    public double getS() {
        return s;
    }

    public String getShape() {
        return shape;
    }

    public double getApx() {
        return apx;
    }

    public double getApy() {
        return apy;
    }

    public void setId(String str) {
        id = str;
    }

    public void setX(double d) {
        x = d;
    }

    public void setY(double d) {
        y = d;
    }

    public void setZ(double d) {
        z = d;
    }

    public void setXp(double d) {
        xp = d;
    }

    public void setYp(double d) {
        yp = d;
    }

    public void setZp(double d) {
        zp = d;
    }

    public void setAx(double d) {
        ax = d;
    }

    public void setAy(double d) {
        ay = d;
    }

    public void setAz(double d) {
        az = d;
    }

    public void setBx(double d) {
        bx = d;
    }

    public void setBy(double d) {
        by = d;
    }

    public void setBz(double d) {
        bz = d;
    }

    public void setEx(double d) {
        ex = d;
    }

    public void setEy(double d) {
        ey = d;
    }

    public void setEz(double d) {
        ez = d;
    }

    public void setMux(double d) {
        mux = d;
    }

    public void setMuy(double d) {
        muy = d;
    }

    public void setMuz(double d) {
        muz = d;
    }

    public void setSx(double d) {
        sx = d;
    }

    public void setSy(double d) {
        sy = d;
    }

    public void setSz(double d) {
        sz = d;
    }

    public void setEtx(double d) {
        etx = d;
    }

    public void setEty(double d) {
        ety = d;
    }

    public void setEtpx(double d) {
        etpx = d;
    }

    public void setEtpy(double d) {
        etpy = d;
    }

    public void setW(double d) {
        w = d;
    }

    public void setS(double d) {
        s = d;
    }

    public void setShape(String str) {
        shape = str;
    }

    public void setApx(double d) {
        apx = d;
    }

    public void setApy(double d) {
        apy = d;
    }
}
