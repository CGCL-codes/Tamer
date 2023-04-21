package rctgl.park.sv4;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import panoramic.PanoScene;
import rctgl.park.sv4.*;
import rctgl.ICompilable;

public class SV4ParkTile implements ICompilable {

    private SV4Surface m_surface = new SV4Surface();

    private ArrayList<SV4Path> m_paths = new ArrayList<SV4Path>();

    private int m_x, m_y;

    public SV4ParkTile() {
    }

    public SV4Surface getSurface() {
        return m_surface;
    }

    public ArrayList<SV4Path> getPaths() {
        return m_paths;
    }

    public void setCoords(int x, int y) {
        m_x = x;
        m_y = y;
    }

    private static boolean isLastElement(int testByte) {
        if ((testByte & 0x80) != 0) return true;
        return false;
    }

    public void loadFromStream(ByteArrayInputStream stream) throws IOException {
        int[] bytes = new int[8];
        boolean lastElement = false;
        while (!lastElement) {
            for (int i = 0; i < 8; i++) bytes[i] = stream.read();
            int type = UnitTypes.getCode(bytes[0]);
            SV4Element curElement = null;
            if (type == UnitTypes.SURFACE_TILE) {
                m_surface.loadFromBytes(bytes);
                curElement = m_surface;
            } else if (type == UnitTypes.PATH) {
                SV4Path path = new SV4Path();
                path.loadFromBytes(bytes);
                curElement = path;
                m_paths.add(path);
            }
            if (curElement != null) {
                curElement.setCoordinates(m_x, m_y);
            }
            lastElement = isLastElement(bytes[1]);
        }
    }

    public void compile(PanoScene scene) {
    }
}
