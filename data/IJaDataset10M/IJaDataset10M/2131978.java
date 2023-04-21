package figuras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.media.opengl.GL;

public class PuntoVector {

    private double x;

    private double y;

    private double z;

    private double pv;

    public PuntoVector() {
        x = 0;
        y = 0;
        z = 0;
        pv = 1;
    }

    public PuntoVector(double x, double y, double z, double pv) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pv = pv;
    }

    public PuntoVector(PuntoVector p) {
        x = p.x;
        y = p.y;
        z = p.z;
        pv = p.pv;
    }

    public double productoEscalar(PuntoVector p) {
        return this.x * p.x + this.y * p.y + this.z * p.z;
    }

    public void escalacion(double k) {
        x = k * x;
        y = k * y;
        z = k * z;
    }

    public double modulo() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void normaliza() {
        double n = this.modulo();
        x = x / n;
        y = y / n;
        z = z / n;
    }

    public PuntoVector productoVectorial(PuntoVector p) {
        double det1 = (this.y * p.z) - (this.z * p.y);
        double det2 = (this.x * p.z) - (this.z * p.x);
        double det3 = (this.x * p.y) - (this.y * p.x);
        return new PuntoVector(det1, -det2, det3, 0);
    }

    public double distancia(PuntoVector p) {
        double x2 = x - p.x;
        double y2 = y - p.y;
        double z2 = z - p.z;
        return Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
    }

    public boolean esMayor(PuntoVector g) {
        return (y > g.y || (y == g.y && x > g.x));
    }

    public PuntoVector perpendicularIzquierda() {
        return new PuntoVector(-this.y, this.x, this.z, this.pv);
    }

    public PuntoVector obtenVector(PuntoVector p) {
        return new PuntoVector(p.x - this.x, p.y - this.y, p.z - this.z, 0);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getPv() {
        return pv;
    }

    public void setPv(double pv) {
        this.pv = pv;
    }

    public void dibuja(GL gl) {
        gl.glVertex3d(getX(), getY(), getZ());
    }

    public void save(PrintWriter out) {
        out.println(getX());
        out.println(getY());
        out.println(getZ());
        out.println(getPv());
    }

    public void load(BufferedReader in) {
        try {
            x = new Double(in.readLine());
            y = new Double(in.readLine());
            z = new Double(in.readLine());
            pv = new Double(in.readLine());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PuntoVector sumaPuntoVector(PuntoVector vector) {
        return new PuntoVector(x + vector.x, y + vector.y, z + vector.z, 1);
    }

    public double distancia2D(PuntoVector p) {
        double x2 = x - p.x;
        double y2 = y - p.y;
        return Math.sqrt(x2 * x2 + y2 * y2);
    }
}
