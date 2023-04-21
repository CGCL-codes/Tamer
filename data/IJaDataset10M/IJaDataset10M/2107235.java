package normal.engine.triangulation;

public class NVertexEmbedding {

    private NTetrahedron tetrahedron;

    private int vertex;

    public NVertexEmbedding(NTetrahedron tetrahedron, int vertex) {
        this.tetrahedron = tetrahedron;
        this.vertex = vertex;
    }

    public NVertexEmbedding(NVertexEmbedding cloneMe) {
        this.tetrahedron = cloneMe.tetrahedron;
        this.vertex = cloneMe.vertex;
    }

    public NTetrahedron getTetrahedron() {
        return tetrahedron;
    }

    public int getVertex() {
        return vertex;
    }
}
