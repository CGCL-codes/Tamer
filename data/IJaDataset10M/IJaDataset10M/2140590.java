package bitWave.utilities;

import java.io.FileWriter;
import java.io.IOException;
import bitWave.geometry.GeometryFactory;
import bitWave.geometry.Line;
import bitWave.geometry.Mesh;
import bitWave.linAlg.LinAlgFactory;
import bitWave.linAlg.Vec4;

/**
 * Mesh adapter for reading and writing in the wavefront format.
 * @see <a href="http://www.fileformat.info/format/wavefrontobj/">Wavefront OBJ File Format Summary</a> 
 * @author fw
 *
 */
public class MeshAdapter_Wavefront {

    private GeometryFactory m_geof;

    MeshAdapter_Wavefront(LinAlgFactory laf, GeometryFactory geof) {
        this.m_geof = geof;
    }

    public void saveMesh(Mesh m, String file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(String.format("o Mesh\n"));
        for (int i = 0; i < m.getVertexCount(); i++) {
            Vec4 pos = m.getVertexPositionByIndex(i);
            writer.write(String.format("v %g %g %g\n", pos.x(), pos.y(), pos.z()));
        }
        for (int i = 0; i < m.getLineCount(); i++) {
            writer.write(String.format("cstype bspline\n"));
            writer.write(String.format("deg 3\n"));
            Line l = m.getLineByIndex(i);
            StringBuilder buf = new StringBuilder();
            for (int p = 0; p < l.getVertexCount(); p++) {
                buf.append(" ");
                buf.append(l.getVertexIndexByIndex(p) + 1);
            }
            writer.write(String.format("curv 0 1%s\n", buf.toString()));
            writer.write(String.format("parm u 0 1\n"));
            writer.write(String.format("end\n"));
        }
        writer.flush();
        writer.close();
    }

    public Mesh loadMesh(String file) {
        Mesh m = m_geof.createMesh();
        return m;
    }
}
