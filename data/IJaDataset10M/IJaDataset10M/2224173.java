package componentes;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.media.opengl.GL;
import transformaciones.TAfin;
import utils.math.MathFlowers;
import figuras.malla.Malla;

public class ComponenteCompuesto3D extends Componente3D {

    protected ArrayList<Componente3D> componentes;

    public ComponenteCompuesto3D() {
    }

    public ComponenteCompuesto3D(GL gl, TAfin matriz, Malla malla, Color color) {
        super(gl, matriz, malla, color);
        this.componentes = new ArrayList<Componente3D>();
    }

    public ArrayList<Componente3D> getComponentes() {
        return componentes;
    }

    public void setComponentes(ArrayList<Componente3D> componentes) {
        this.componentes = componentes;
    }

    public void addComponente(Componente3D componente) {
        if (componentes == null) this.componentes = new ArrayList<Componente3D>();
        this.componentes.add(componente);
    }

    public Componente3D getComponente(int i) {
        Componente3D retorno = null;
        try {
            retorno = componentes.get(i);
        } catch (Exception e) {
        }
        return retorno;
    }

    public void setColor(Color color, int index) {
        componentes.get(index).setColor(color);
    }

    public void setColorComponentes(Color color) {
        for (int i = 0; i < componentes.size(); i++) {
            componentes.get(i).setColor(color);
        }
        this.color = color;
    }

    public void dibuja(int modo) {
        gl.glPushMatrix();
        gl.glMultMatrixd(matriz.getMatriz());
        for (Componente3D componente : componentes) {
            if (componente != null) {
                if (componente.getGl() != null) componente.dibuja(modo);
            }
        }
        gl.glPopMatrix();
    }

    protected int getPlyNumVertices() {
        int nVertices = 0;
        for (int i = 0; i < componentes.size(); i++) {
            nVertices += componentes.get(i).getPlyNumVertices();
        }
        return nVertices;
    }

    protected int getPlyNumCaras() {
        int nCaras = 0;
        for (int i = 0; i < componentes.size(); i++) {
            nCaras += componentes.get(i).getPlyNumCaras();
        }
        return nCaras;
    }

    protected void writePlyVertex(TAfin matriz, PrintWriter out) {
        for (int i = 0; i < componentes.size(); i++) {
            TAfin m = new TAfin(gl);
            m.getMatriz().put(MathFlowers.multiplicar(matriz.getMatriz().array(), this.matriz.getMatriz().array()));
            componentes.get(i).writePlyVertex(m, out);
        }
    }

    protected void writePlyFaces(int count, PrintWriter out) {
        int index = count;
        for (int i = 0; i < componentes.size(); i++) {
            componentes.get(i).writePlyFaces(index, out);
            index += componentes.get(i).getPlyNumVertices();
        }
    }

    public void saveToAsciiPLY(String path, String nameComponent) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
            int nVertices = 0;
            int nCaras = 0;
            for (int i = 0; i < componentes.size(); i++) {
                nVertices += componentes.get(i).getPlyNumVertices();
            }
            for (int i = 0; i < componentes.size(); i++) {
                nCaras += componentes.get(i).getPlyNumCaras();
            }
            Malla.writeHeader(out, nameComponent, nVertices, nCaras);
            for (int i = 0; i < componentes.size(); i++) {
                componentes.get(i).writePlyVertex(matriz, out);
            }
            int count = 0;
            for (int i = 0; i < componentes.size(); i++) {
                componentes.get(i).writePlyFaces(count, out);
                count += componentes.get(i).getPlyNumVertices();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToBinaryPLY(String path, String nameComponent) {
        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
            int nVertices = 0;
            int nCaras = 0;
            for (int i = 0; i < componentes.size(); i++) {
                nVertices += componentes.get(i).getMalla().getNVertices();
            }
            for (int i = 0; i < componentes.size(); i++) {
                nCaras += componentes.get(i).getMalla().getNCaras();
            }
            Malla.writeHeader(out, nameComponent, nVertices, nCaras);
            for (int i = 0; i < componentes.size(); i++) {
                componentes.get(i).getMalla().writeVertex(componentes.get(i).getMatriz(), out);
            }
            int count = 0;
            for (int i = 0; i < componentes.size(); i++) {
                componentes.get(i).getMalla().writeFace(out, count);
                count += componentes.get(i).getMalla().getNCaras();
            }
            out.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String path) {
        PrintWriter out;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
            out.println(componentes.size());
            for (int i = 0; i < componentes.size(); i++) componentes.get(i).save(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String path) {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(path));
            int n = new Integer(in.readLine());
            componentes.clear();
            for (int i = 0; i < n; i++) {
                Componente3D c = new Componente3D();
                c.load(in);
                componentes.add(c);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTexture(int texture) {
        double yTop = Double.MIN_VALUE;
        double yBot = Double.MAX_VALUE;
        double xRight = Double.MIN_VALUE;
        double xLeft = Double.MAX_VALUE;
        for (int i = 0; i < componentes.size(); i++) {
            componentes.get(i).getMalla().setTexture(texture);
            componentes.get(i).getMalla().calculaPuntosDeTextura();
            yTop = Math.max(componentes.get(i).getMalla().getYTop(), yTop);
            yBot = Math.min(componentes.get(i).getMalla().getYBot(), yBot);
            xRight = Math.max(componentes.get(i).getMalla().getXRight(), xRight);
            xLeft = Math.min(componentes.get(i).getMalla().getXLeft(), xLeft);
        }
        for (int i = 0; i < componentes.size(); i++) {
            componentes.get(i).getMalla().setYTop(yTop);
            componentes.get(i).getMalla().setYBot(yBot);
            componentes.get(i).getMalla().setXRight(xRight);
            componentes.get(i).getMalla().setXLeft(xLeft);
        }
    }

    public void calculaPuntosTextura() {
        double yTop = Double.MIN_VALUE;
        double yBot = Double.MAX_VALUE;
        double xRight = Double.MIN_VALUE;
        double xLeft = Double.MAX_VALUE;
        for (int i = 0; i < componentes.size(); i++) {
            componentes.get(i).getMalla().calculaPuntosDeTextura();
            yTop = Math.max(componentes.get(i).getMalla().getYTop(), yTop);
            yBot = Math.min(componentes.get(i).getMalla().getYBot(), yBot);
            xRight = Math.max(componentes.get(i).getMalla().getXRight(), xRight);
            xLeft = Math.min(componentes.get(i).getMalla().getXLeft(), xLeft);
        }
        for (int i = 0; i < componentes.size(); i++) {
            componentes.get(i).getMalla().setYTop(yTop);
            componentes.get(i).getMalla().setYBot(yBot);
            componentes.get(i).getMalla().setXRight(xRight);
            componentes.get(i).getMalla().setXLeft(xLeft);
        }
    }

    public void setTextureBlended(boolean blended) {
        for (int i = 0; i < componentes.size(); i++) componentes.get(i).getMalla().setBlendTexture(blended);
    }

    public void removeTexture() {
        for (int i = 0; i < componentes.size(); i++) componentes.get(i).getMalla().setTexture(-1);
    }
}
