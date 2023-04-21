package Scene;

public class Cube {

    public Integer x;

    public Integer y;

    public Integer z;

    public Integer idObjet;

    public int R;

    public int G;

    public int B;

    public int opacite;

    public Scene myScene;

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public Integer getIdObjet() {
        return idObjet;
    }

    public void setIdObjet(Integer idObjet) {
        this.idObjet = idObjet;
    }

    public Scene getMyScene() {
        return myScene;
    }

    public void setMyScene(Scene myScene) {
        this.myScene = myScene;
    }

    public int getR() {
        return R;
    }

    public void setR(int r) {
        R = r;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public int getOpacite() {
        return opacite;
    }

    public void setOpacite(int opacite) {
        this.opacite = opacite;
    }
}
