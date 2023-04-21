package object.screen;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import util.math.algebra.Matrix;

public class FileScreen extends Matrix implements Screen {

    private String fileName;

    public FileScreen(String fn, int resX, int resY) {
        super(resX, resY);
        this.fileName = fn;
    }

    public void setColor(int x, int y, int c) {
        c = (int) Math.min(0xFFFFFF, c);
        set(x, y, c);
    }

    public void display() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(fileName));
            out.println("P3");
            out.println("#--");
            Matrix m = this.transpose();
            out.println(m.getRows() + " " + m.getColumns());
            out.println("255");
            for (int i = 0; i < m.getRows(); i++) {
                for (int j = 0; j < m.getColumns(); j++) {
                    int c = (int) (m.get(i, j)), r = (0xFF0000 & c) >> 16, g = (0xFF00 & c) >> 8, b = (0xFF & c);
                    out.print(r + " " + g + " " + b);
                    if (j < m.getColumns() - 1) out.print("\t");
                }
                out.println();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
