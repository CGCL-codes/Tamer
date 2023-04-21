package componentes.flor;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.media.opengl.GL;
import transformaciones.TAfin;
import componentes.Componente3D;
import componentes.ComponenteCompuesto3D;
import figuras.PuntoVector;
import figuras.malla.Cara;
import figuras.malla.Malla;
import figuras.malla.MallaHoja;
import figuras.malla.VerticeNormal;

public class HojaDentada extends ComponenteCompuesto3D {

    private int nDent;

    private int maxHeight;

    private int minHeight;

    private int alg;

    public int getAlg() {
        return alg;
    }

    public void setAlg(int alg) {
        this.alg = alg;
    }

    public HojaDentada(GL gl, Hoja hoja) {
        super(gl, new TAfin(gl), null, hoja.getColor());
        this.componentes.add(hoja);
    }

    public void dentarHojaAlgoritmo1(int nDentaciones, int maxAltura, int minAltura) {
        alg = 1;
        nDent = nDentaciones;
        maxHeight = maxAltura;
        minHeight = minAltura;
        Componente3D hoja = componentes.get(0);
        componentes = new ArrayList<Componente3D>();
        componentes.add(hoja);
        ArrayList<Malla> dentaciones = deformaMallaConDentaciones1(nDentaciones, maxAltura / 3.0, ((MallaHoja) hoja.getMalla()).getDivY(), ((MallaHoja) hoja.getMalla()).getDivX(), hoja.getMalla().getNVertices(), hoja.getMalla().getVertices());
        Componente3D dentacionesIzq = new Componente3D(gl, new TAfin(gl), dentaciones.get(0), componentes.get(0).getColor());
        Componente3D dentacionesDch = new Componente3D(gl, new TAfin(gl), dentaciones.get(1), componentes.get(0).getColor());
        boolean blendTexture = hoja.getMalla().isBlendTexture();
        dentacionesIzq.getMalla().setBlendTexture(blendTexture);
        dentacionesDch.getMalla().setBlendTexture(blendTexture);
        componentes.add(dentacionesIzq);
        componentes.add(dentacionesDch);
        setTexture(componentes.get(0).getMalla().getTexture());
    }

    public void dentarHojaAlgoritmo2(int nDentaciones, int maxAltura, int minAltura) {
        alg = 2;
        nDent = nDentaciones;
        maxHeight = maxAltura;
        minHeight = minAltura;
        Componente3D hoja = componentes.get(0);
        componentes = new ArrayList<Componente3D>();
        componentes.add(hoja);
        ArrayList<Malla> dentaciones = deformaMallaConDentaciones2(nDentaciones, maxAltura / 3.0, ((MallaHoja) hoja.getMalla()).getDivY(), ((MallaHoja) hoja.getMalla()).getDivX(), hoja.getMalla().getNVertices(), hoja.getMalla().getVertices());
        Componente3D dentacionesIzq = new Componente3D(gl, new TAfin(gl), dentaciones.get(0), componentes.get(0).getColor());
        Componente3D dentacionesDch = new Componente3D(gl, new TAfin(gl), dentaciones.get(1), componentes.get(0).getColor());
        boolean blendTexture = hoja.getMalla().isBlendTexture();
        dentacionesIzq.getMalla().setBlendTexture(blendTexture);
        dentacionesDch.getMalla().setBlendTexture(blendTexture);
        componentes.add(dentacionesIzq);
        componentes.add(dentacionesDch);
        setTexture(componentes.get(0).getMalla().getTexture());
    }

    public ArrayList<Malla> deformaMallaConDentaciones1(int nDentaciones, double altura, int divY, int divX, int nVertices, PuntoVector[] vertices) {
        ArrayList<Malla> dentaciones = new ArrayList<Malla>();
        Malla dentacionesIzq = new Malla();
        int nCarasDI = (divY - 2) * nDentaciones;
        int nVerticesDI = 2 * nCarasDI + 1;
        dentacionesIzq.setNVertices(nVerticesDI);
        dentacionesIzq.setNCaras(nCarasDI);
        dentacionesIzq.setNNormales(nVerticesDI / 2);
        PuntoVector[] verticesDI = new PuntoVector[nVerticesDI];
        Cara[] carasDI = new Cara[nCarasDI];
        PuntoVector[] normalesDI = new PuntoVector[nVerticesDI / 2];
        int countVertDI = 0;
        int countCarasDI = 0;
        Malla dentacionesDch = new Malla();
        int nCarasDch = (divY - 2) * nDentaciones;
        int nVerticesDch = 2 * nCarasDI + 1;
        dentacionesDch.setNVertices(nVerticesDch);
        dentacionesDch.setNCaras(nCarasDch);
        dentacionesDch.setNNormales(nVerticesDch / 2);
        PuntoVector[] verticesDch = new PuntoVector[nVerticesDch];
        Cara[] carasDch = new Cara[nCarasDch];
        PuntoVector[] normalesDch = new PuntoVector[nVerticesDch / 2];
        int countVertDch = 0;
        int countCarasDch = 0;
        int index1 = 0;
        int index2 = index1 + divX + 1;
        while (index2 < nVertices - 1 - divX) {
            index1 += 1;
            index2 += 1;
            PuntoVector p1 = new PuntoVector(vertices[index1]);
            PuntoVector p2 = new PuntoVector(vertices[index2]);
            PuntoVector p4 = new PuntoVector(vertices[index1 + 1]);
            PuntoVector inc = p1.obtenVector(p2);
            inc.escalacion(1.0 / (double) nDentaciones);
            for (int i = 0; i < nDentaciones; i++) {
                p2 = new PuntoVector(p1);
                p2 = p2.sumaPuntoVector(inc);
                PuntoVector inc2 = p1.obtenVector(p2);
                inc2.escalacion(0.5);
                PuntoVector p3 = new PuntoVector(p1);
                p3 = p3.sumaPuntoVector(inc2);
                p4 = p4.obtenVector(p1);
                p4.normaliza();
                p4.escalacion(altura * Math.random());
                p3 = p3.sumaPuntoVector(p4);
                ArrayList<VerticeNormal> verticeNormal = new ArrayList<VerticeNormal>();
                verticesDI[countVertDI++] = new PuntoVector(p1);
                verticesDI[countVertDI++] = new PuntoVector(p3);
                verticeNormal.add(new VerticeNormal(countCarasDI * 2, countCarasDI));
                verticeNormal.add(new VerticeNormal(countCarasDI * 2 + 1, countCarasDI));
                verticeNormal.add(new VerticeNormal(countCarasDI * 2 + 2, countCarasDI));
                carasDI[countCarasDI++] = new Cara(3, verticeNormal);
                p1 = p2;
            }
            index1 += divX;
            index2 += divX;
            p1 = new PuntoVector(vertices[index1]);
            p2 = new PuntoVector(vertices[index2]);
            p4 = new PuntoVector(vertices[index1 - 1]);
            inc = p1.obtenVector(p2);
            inc.escalacion(1.0 / (double) nDentaciones);
            for (int i = 0; i < nDentaciones; i++) {
                p2 = new PuntoVector(p1);
                p2 = p2.sumaPuntoVector(inc);
                PuntoVector inc2 = p1.obtenVector(p2);
                inc2.escalacion(0.5);
                PuntoVector p3 = new PuntoVector(p1);
                p3 = p3.sumaPuntoVector(inc2);
                p4 = p4.obtenVector(p1);
                p4.normaliza();
                p4.escalacion(altura * Math.random());
                p3 = p3.sumaPuntoVector(p4);
                ArrayList<VerticeNormal> verticeNormal = new ArrayList<VerticeNormal>();
                verticesDch[countVertDch++] = new PuntoVector(p1);
                verticesDch[countVertDch++] = new PuntoVector(p3);
                verticeNormal.add(new VerticeNormal(countCarasDch * 2, countCarasDch));
                verticeNormal.add(new VerticeNormal(countCarasDch * 2 + 2, countCarasDch));
                verticeNormal.add(new VerticeNormal(countCarasDch * 2 + 1, countCarasDch));
                carasDch[countCarasDch++] = new Cara(3, verticeNormal);
                p1 = p2;
            }
        }
        verticesDI[countVertDI++] = new PuntoVector(vertices[nVertices - 2 - divX]);
        dentacionesIzq.setVertices(verticesDI);
        dentacionesIzq.setCaras(carasDI);
        dentacionesIzq.setNormales(normalesDI);
        dentacionesIzq.calculaNormales();
        dentaciones.add(dentacionesIzq);
        verticesDch[countVertDch++] = new PuntoVector(vertices[nVertices - 2]);
        dentacionesDch.setVertices(verticesDch);
        dentacionesDch.setCaras(carasDch);
        dentacionesDch.setNormales(normalesDch);
        dentacionesDch.calculaNormales();
        dentaciones.add(dentacionesDch);
        return dentaciones;
    }

    public ArrayList<Malla> deformaMallaConDentaciones2(int nDentaciones, double altura, int divY, int divX, int nVertices, PuntoVector[] vertices) {
        ArrayList<Malla> dentaciones = new ArrayList<Malla>();
        Malla dentacionesIzq = new Malla();
        int nCarasDI = (divY - 2) * nDentaciones;
        int nVerticesDI = 2 * nCarasDI + 1;
        dentacionesIzq.setNVertices(nVerticesDI);
        dentacionesIzq.setNCaras(nCarasDI);
        dentacionesIzq.setNNormales(nVerticesDI / 2);
        PuntoVector[] verticesDI = new PuntoVector[nVerticesDI];
        Cara[] carasDI = new Cara[nCarasDI];
        PuntoVector[] normalesDI = new PuntoVector[nVerticesDI / 2];
        int countVertDI = 0;
        int countCarasDI = 0;
        Malla dentacionesDch = new Malla();
        int nCarasDch = (divY - 2) * nDentaciones;
        int nVerticesDch = 2 * nCarasDI + 1;
        dentacionesDch.setNVertices(nVerticesDch);
        dentacionesDch.setNCaras(nCarasDch);
        dentacionesDch.setNNormales(nVerticesDch / 2);
        PuntoVector[] verticesDch = new PuntoVector[nVerticesDch];
        Cara[] carasDch = new Cara[nCarasDch];
        PuntoVector[] normalesDch = new PuntoVector[nVerticesDch / 2];
        int countVertDch = 0;
        int countCarasDch = 0;
        int index1 = 0;
        int index2 = index1 + divX + 1;
        while (index2 < nVertices - 1 - divX) {
            index1 += 1;
            index2 += 1;
            PuntoVector p1 = new PuntoVector(vertices[index1]);
            PuntoVector p2 = new PuntoVector(vertices[index2]);
            PuntoVector p4 = new PuntoVector(vertices[index1 + 1]);
            PuntoVector inc = p1.obtenVector(p2);
            inc.escalacion(1.0 / (double) nDentaciones);
            for (int i = 0; i < nDentaciones; i++) {
                p2 = new PuntoVector(p1);
                p2 = p2.sumaPuntoVector(inc);
                PuntoVector inc2 = p1.obtenVector(p2);
                inc2.escalacion(0.5);
                PuntoVector p3 = new PuntoVector(p1);
                p3 = p3.sumaPuntoVector(inc2);
                p4 = p4.obtenVector(p1);
                p4.normaliza();
                p4.escalacion(altura);
                p3 = p3.sumaPuntoVector(p4);
                ArrayList<VerticeNormal> verticeNormal = new ArrayList<VerticeNormal>();
                verticesDI[countVertDI++] = new PuntoVector(p1);
                verticesDI[countVertDI++] = new PuntoVector(p3);
                verticeNormal.add(new VerticeNormal(countCarasDI * 2, countCarasDI));
                verticeNormal.add(new VerticeNormal(countCarasDI * 2 + 1, countCarasDI));
                verticeNormal.add(new VerticeNormal(countCarasDI * 2 + 2, countCarasDI));
                carasDI[countCarasDI++] = new Cara(3, verticeNormal);
                p1 = p2;
            }
            index1 += divX;
            index2 += divX;
            p1 = new PuntoVector(vertices[index1]);
            p2 = new PuntoVector(vertices[index2]);
            p4 = new PuntoVector(vertices[index1 - 1]);
            inc = p1.obtenVector(p2);
            inc.escalacion(1.0 / (double) nDentaciones);
            for (int i = 0; i < nDentaciones; i++) {
                p2 = new PuntoVector(p1);
                p2 = p2.sumaPuntoVector(inc);
                PuntoVector inc2 = p1.obtenVector(p2);
                inc2.escalacion(0.5);
                PuntoVector p3 = new PuntoVector(p1);
                p3 = p3.sumaPuntoVector(inc2);
                p4 = p4.obtenVector(p1);
                p4.normaliza();
                p4.escalacion(altura);
                p3 = p3.sumaPuntoVector(p4);
                ArrayList<VerticeNormal> verticeNormal = new ArrayList<VerticeNormal>();
                verticesDch[countVertDch++] = new PuntoVector(p1);
                verticesDch[countVertDch++] = new PuntoVector(p3);
                verticeNormal.add(new VerticeNormal(countCarasDch * 2, countCarasDch));
                verticeNormal.add(new VerticeNormal(countCarasDch * 2 + 2, countCarasDch));
                verticeNormal.add(new VerticeNormal(countCarasDch * 2 + 1, countCarasDch));
                carasDch[countCarasDch++] = new Cara(3, verticeNormal);
                p1 = p2;
            }
        }
        verticesDI[countVertDI++] = new PuntoVector(vertices[nVertices - 2 - divX]);
        dentacionesIzq.setVertices(verticesDI);
        dentacionesIzq.setCaras(carasDI);
        dentacionesIzq.setNormales(normalesDI);
        dentacionesIzq.calculaNormales();
        dentaciones.add(dentacionesIzq);
        verticesDch[countVertDch++] = new PuntoVector(vertices[nVertices - 2]);
        dentacionesDch.setVertices(verticesDch);
        dentacionesDch.setCaras(carasDch);
        dentacionesDch.setNormales(normalesDch);
        dentacionesDch.calculaNormales();
        dentaciones.add(dentacionesDch);
        return dentaciones;
    }

    public void deleteDentaciones() {
        if (componentes.size() == 3) {
            componentes.remove(1);
            componentes.remove(1);
        }
    }

    public void load(String path) {
        BufferedReader in;
        try {
            componentes.clear();
            in = new BufferedReader(new FileReader(path));
            int n = new Integer(in.readLine());
            Hoja petalo = new Hoja(gl);
            petalo.load(in);
            componentes.add(petalo);
            if (n == 3) {
                Componente3D dentacionIzq = new Componente3D(gl, new TAfin(gl), new Malla(), Color.GREEN);
                dentacionIzq.load(in);
                Componente3D dentacionDch = new Componente3D(gl, new TAfin(gl), new Malla(), Color.GREEN);
                dentacionDch.load(in);
                componentes.add(dentacionIzq);
                componentes.add(dentacionDch);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void trasladar(float size) {
        if (componentes != null && componentes.size() > 0 && componentes.get(0) != null) for (int i = 0; i < componentes.size(); i++) componentes.get(i).getMalla().trasladar(size);
    }

    public int getNDent() {
        return nDent;
    }

    public void setNDent(int dent) {
        nDent = dent;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }
}
