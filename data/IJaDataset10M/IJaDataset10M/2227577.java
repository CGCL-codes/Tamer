package motor3d;

public class Primitivas {

    static Vector3d vectorTemp = new Vector3d();

    public static void rectangulo(ObjetoMallado obj, double ancho, double alto, Vector3d centro, MaterialShader material) {
        int indGrupoVert = obj.nuevoGrupoVertices(4);
        GrupoVertices grupoVertices = obj.getGrupoVertices(indGrupoVert);
        double ancho2 = ancho * 0.5d;
        double alto2 = alto * 0.5d;
        vectorTemp.set(ancho2, alto2, 0.0d);
        vectorTemp.increm(centro);
        grupoVertices.setPosVertice(0, vectorTemp);
        grupoVertices.setUVVertice(0, 0.0d, 0.0d);
        grupoVertices.setNormVertice(0, Vector3d.vectorMenosZ);
        vectorTemp.set(-ancho2, alto2, 0.0d);
        vectorTemp.increm(centro);
        grupoVertices.setPosVertice(1, vectorTemp);
        grupoVertices.setUVVertice(1, 1.0d, 0.0d);
        grupoVertices.setNormVertice(1, Vector3d.vectorMenosZ);
        vectorTemp.set(ancho2, -alto2, 0.0d);
        vectorTemp.increm(centro);
        grupoVertices.setPosVertice(2, vectorTemp);
        grupoVertices.setUVVertice(2, 0.0d, 1.0d);
        grupoVertices.setNormVertice(2, Vector3d.vectorMenosZ);
        vectorTemp.set(-ancho2, -alto2, 0.0d);
        vectorTemp.increm(centro);
        grupoVertices.setPosVertice(3, vectorTemp);
        grupoVertices.setUVVertice(3, 1.0d, 1.0d);
        grupoVertices.setNormVertice(3, Vector3d.vectorMenosZ);
        int indGrupoTri = obj.nuevoGrupoTriangulos(2);
        GrupoTriangulos grupoTri = obj.getGrupoTriangulos(indGrupoTri);
        grupoTri.grupoVertices = grupoVertices;
        grupoTri.material = material;
        grupoTri.setTriangulo(0, 0, 2, 1);
        grupoTri.setTriangulo(1, 1, 2, 3);
    }
}
