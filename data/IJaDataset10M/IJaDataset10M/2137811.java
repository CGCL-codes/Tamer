package edu.cast.teseg.algortimos.ingenuoOptimizado;

import edu.cast.teseg.modelo.RedSocial;
import edu.cast.teseg.modelo.Individuos;
import java.util.ArrayList;

/**
 *
 * @author edgarin
 */
public class IngenuoOptimizado {

    private ArrayList<Individuos> vertices;

    private int porcentajeSi;

    private int porcentajeNo;

    private int cant_nodo_no_cumple;

    private int[][] matrizAdj;

    private int[][] caminosCortos;

    public IngenuoOptimizado() {
    }

    public void inicializarIngenuoOptimizado(RedSocial grafo) {
        vertices = new ArrayList();
        vertices.addAll(grafo.getIndividuos().values());
        matrizAdj = grafo.getMatrizAdj();
        caminosCortos = new int[matrizAdj.length][matrizAdj.length];
        cant_nodo_no_cumple = 0;
        for (int i = 0; i < matrizAdj.length; i++) {
            for (int j = 0; j < matrizAdj.length; j++) {
                if (matrizAdj[i][j] == 0 && i != j) {
                    matrizAdj[i][j] = 999;
                }
                caminosCortos[i][j] = 999;
            }
        }
    }

    public void ejecutarIngenuoOptimizado() {
        boolean nodoCumple = true;
        ArrayList<Individuos> copiaVertices = new ArrayList();
        copiaVertices.addAll(vertices);
        for (int i = 0; i < vertices.size(); i++) {
            Individuos v = vertices.get(i);
            int idV = v.getId();
            copiaVertices.remove(0);
            for (int j = 0; j < copiaVertices.size(); j++) {
                Individuos k = copiaVertices.get(j);
                int idK = k.getId();
                if (idV != idK) {
                    if (!hayCaminoMenorASeis(v, k)) {
                        nodoCumple = false;
                    }
                }
            }
            if (!nodoCumple) cant_nodo_no_cumple++;
            nodoCumple = true;
        }
        calcularPorcentaje();
    }

    private boolean hayCaminoMenorASeis(Individuos v, Individuos k) {
        ArrayList<Individuos> visitados = new ArrayList();
        int nivel = 0;
        ArrayList<Individuos> camino = new ArrayList();
        camino = caminoMenorASeis(v, k, visitados, nivel, camino);
        if (camino.isEmpty()) return false; else return true;
    }

    private ArrayList<Individuos> caminoMenorASeis(Individuos v, Individuos k, ArrayList<Individuos> visitados, int nivel, ArrayList<Individuos> camino) {
        int idV = v.getId() - 1;
        int idK = k.getId() - 1;
        if (vertices.get(0).getId() == 0) {
            idV++;
            idK++;
        }
        if (matrizAdj[idV][idK] == 1) {
            visitados.add(v);
            visitados.add(k);
            ArrayList<Individuos> caminoTemporal = new ArrayList();
            caminoTemporal.addAll(visitados);
            if (caminoTemporal.size() <= 7) {
                camino.addAll(caminoTemporal);
            }
            visitados.remove(nivel + 1);
        } else {
            ArrayList<Individuos> vecinosV = vecinosVertice(v, vertices);
            visitados.add(v);
            for (int i = 0; i < vecinosV.size(); i++) {
                if (!camino.isEmpty()) break;
                if (visitados.contains(vecinosV.get(i))) continue; else {
                    nivel++;
                    caminoMenorASeis(vecinosV.get(i), k, visitados, nivel, camino);
                    for (int j = visitados.size() - 1; j >= nivel; j--) visitados.remove(j);
                    nivel--;
                }
            }
        }
        return camino;
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
        porcentajeNo = cant_nodo_no_cumple * 2;
        porcentajeSi = (matrizAdj.length) - porcentajeNo;
        porcentajeNo = (porcentajeNo * 100) / (matrizAdj.length);
        porcentajeSi = (porcentajeSi * 100) / (matrizAdj.length);
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
