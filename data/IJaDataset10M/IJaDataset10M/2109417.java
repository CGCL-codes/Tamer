package co.edu.unal.fdtd.util;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import co.edu.unal.fdtd.model.Cell;
import co.edu.unal.fdtd.model.CellVolume;

public class FileDumper {

    public FileDumper() {
        try {
            m_os = new PrintWriter(new BufferedOutputStream(new FileOutputStream("data/fdtd.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void dump(final CellVolume vol) {
        if (m_os != null) {
            Cell[][][] ca = vol.getCellArray();
            for (int z = 1; z < ca.length - 1; z++) {
                for (int y = 0; y < ca[0].length; y++) {
                    for (int x = 0; x < ca[0][0].length; x++) {
                        m_os.write(ca[z][y][x].getEz() + " ");
                    }
                    m_os.write("\n");
                }
            }
        }
    }

    public void finalize() {
        try {
            if (m_os != null) {
                m_os.close();
            }
            super.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private PrintWriter m_os;
}
