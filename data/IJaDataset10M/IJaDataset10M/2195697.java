package edu.cast.teseg.algoritmos.ingenuo;

import edu.cast.teseg.modelo.RedSocial;
import edu.cast.teseg.modelo.Individuos;
import java.util.ArrayList;

/**
 *
 * @author edgarin
 */
public class Ingenuo {

    private ArrayList<Individuos> vertices;

    private int porcentajeSi;

    private int porcentajeNo;

    private int[] nodo_cumple;

    private int[][] matrizAdj;

    private int[][] caminosCortos;

    public Ingenuo() {
    }

    public void inicializarIngenuo(RedSocial grafo) {
        vertices = new ArrayList();
        vertices.addAll(grafo.getIndividuos().values());
        matrizAdj = grafo.getMatrizAdj();
        nodo_cumple = new int[matrizAdj.length];
        caminosCortos = new int[matrizAdj.length][matrizAdj.length];
        for (int i = 0; i < matrizAdj.length; i++) {
            for (int j = 0; j < matrizAdj.length; j++) {
                if (matrizAdj[i][j] == 0 && i != j) {
                    matrizAdj[i][j] = 999;
                }
                caminosCortos[i][j] = 999;
            }
            nodo_cumple[i] = 1;
        }
    }

    public void ejecutarIngenuo() {
        for (int i = 0; i < vertices.size(); i++) {
            Individuos v = vertices.get(i);
            int idV = v.getId();
            for (int j = 0; j < vertices.size(); j++) {
                Individuos k = vertices.get(j);
                int idK = k.getId();
                if (idV != idK) {
                    ArrayList<ArrayList<Individuos>> caminos = encontrarCaminos(v, k);
                    int camino_min = minimo(caminos);
                    if (camino_min > 6) nodo_cumple[idV - 1] = 0;
                }
            }
        }
        calcularPorcentaje();
    }

    private ArrayList<ArrayList<Individuos>> encontrarCaminos(Individuos v, Individuos k) {
        ArrayList<Individuos> caminoTemporal = new ArrayList();
        int nivel = 0;
        ArrayList<ArrayList<Individuos>> caminos = new ArrayList();
        return encontrarCaminosAux(v, k, caminoTemporal, nivel, caminos);
    }

    private ArrayList<ArrayList<Individuos>> encontrarCaminosAux(Individuos v, Individuos k, ArrayList<Individuos> visitados, int nivel, ArrayList<ArrayList<Individuos>> caminos) {
        ArrayList<Individuos> vecinosV = vecinosVertice(v, vertices);
        visitados.add(v);
        for (int i = 0; i < vecinosV.size(); i++) {
            if (visitados.contains(vecinosV.get(i))) continue;
            if (vecinosV.get(i) == k) {
                visitados.add(vecinosV.get(i));
                ArrayList<Individuos> caminoTemporal = new ArrayList();
                caminoTemporal.addAll(visitados);
                caminos.add(caminoTemporal);
                visitados.remove(nivel + 1);
                continue;
            } else {
                nivel++;
                encontrarCaminosAux(vecinosV.get(i), k, visitados, nivel, caminos);
                for (int j = visitados.size() - 1; j >= nivel; j--) visitados.remove(j);
                nivel--;
            }
        }
        return caminos;
    }

    private ArrayList<Individuos> vecinosVertice(Individuos ind, ArrayList<Individuos> lsVertices) {
        ArrayList vecinos = new ArrayList();
        for (int i = 0; i < lsVertices.size(); i++) {
            if (vertices.get(0).getId() == 0) {
                if (matrizAdj[ind.getId()][lsVertices.get(i).getId()] == 1) {
                    vecinos.add(lsVertices.get(i));
                }
            } else {
                if (matrizAdj[ind.getId() - 1][lsVertices.get(i).getId() - 1] == 1) {
                    vecinos.add(lsVertices.get(i));
                }
            }
        }
        return vecinos;
    }

    private void calcularPorcentaje() {
        porcentajeSi = 0;
        for (int v = 0; v < matrizAdj.length; v++) {
            porcentajeSi += nodo_cumple[v];
        }
        porcentajeNo = matrizAdj.length - porcentajeSi;
        porcentajeNo = (porcentajeNo * 100) / matrizAdj.length;
        porcentajeSi = (porcentajeSi * 100) / matrizAdj.length;
    }

    public int minimo(ArrayList<ArrayList<Individuos>> caminos) {
        int minimo = 0;
        if (caminos.isEmpty()) {
            return minimo;
        } else minimo = caminos.get(0).size();
        for (int i = 1; i < caminos.size(); i++) {
            if (minimo > caminos.get(i).size()) {
                minimo = caminos.get(i).size();
            }
        }
        return minimo;
    }

    public int getPorcentajeSi() {
        return porcentajeSi;
    }

    public int getPorcentajeNo() {
        return porcentajeNo;
    }

    public void setPorcentajeNo(int porcentajeNo) {
        this.porcentajeNo = porcentajeNo;
    }

    public void setPorcentajeSi(int porcentajeSi) {
        this.porcentajeSi = porcentajeSi;
    }
}
