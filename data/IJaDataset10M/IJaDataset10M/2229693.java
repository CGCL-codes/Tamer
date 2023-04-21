package figuras.malla;

import java.util.ArrayList;
import figuras.PuntoVector;

public class Cara {

    private int numVertices;

    private ArrayList<VerticeNormal> vector;

    public Cara(int numVertices, ArrayList<VerticeNormal> vector) {
        this.numVertices = numVertices;
        this.vector = vector;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }

    public ArrayList<VerticeNormal> getVector() {
        return vector;
    }

    public void setVector(ArrayList<VerticeNormal> vector) {
        this.vector = vector;
    }

    public int getIndiceVertice(int i) {
        return this.vector.get(i).getIndiceVertice();
    }

    public int getIndiceNormal(int i) {
        return this.vector.get(i).getIndiceNormal();
    }

    public PuntoVector calculaNormal(PuntoVector vertices[], int nCara) {
        float nx = 0;
        float ny = 0;
        float nz = 0;
        int suci = 0;
        for (int i = 0; i < numVertices; i++) {
            suci = (i + 1) % numVertices;
            nx += (vertices[getIndiceVertice(i)].getY() - vertices[getIndiceVertice(suci)].getY()) * (vertices[getIndiceVertice(i)].getZ() + vertices[getIndiceVertice(suci)].getZ());
            ny += (vertices[getIndiceVertice(i)].getZ() - vertices[getIndiceVertice(suci)].getZ()) * (vertices[getIndiceVertice(i)].getX() + vertices[getIndiceVertice(suci)].getX());
            nz += (vertices[getIndiceVertice(i)].getX() - vertices[getIndiceVertice(suci)].getX()) * (vertices[getIndiceVertice(i)].getY() + vertices[getIndiceVertice(suci)].getY());
            vector.get(i).setIndiceNormal(nCara);
        }
        PuntoVector normal = new PuntoVector(nx, ny, nz, 0);
        normal.normaliza();
        return normal;
    }
}
