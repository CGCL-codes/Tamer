package terica.pln;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.io.*;
import java.util.*;
import terica.xml.NavegadorXML;
import terica.pln.StringTokenV2;

/**
* Esta clase se ha dise�ado para leer un fichero XML que contien el % de ontologias encontradas
* requiere un fichero tipo ".ide"
*
* @throws java.lang.exception
* @see sitio web del autor: <a href="http://www.luis.criado.org">Luis Criado Fern�ndez</a> 
* @since version 1.0 
* @version junio 2007 
*/
public class porcentajeOntologico {

    private boolean parseo = false;

    private String fichero = "";

    private int numero = 0;

    private int dimen = 5;

    private int numFicheros = 0;

    private int porcentajeMax = 0;

    /**
* Estos atributos representan el "porcentaje" de las ontologias (% de frecuencia de aparici�n)
*/
    private int porcentajeVERTEBRADOS = 0;

    private int porcentajePARENTESCOS = 0;

    private int porcentajeFOOD = 0;

    private int porcentajePIZZA = 0;

    private int porcentajeWINE = 0;

    /**
* Estos atributos representan el "porcentaje" maximo de la ontologias alcanzado en una p�gina html
*/
    private int maxVERTEBRADOS = 0;

    private int maxPARENTESCOS = 0;

    private int maxFOOD = 0;

    private int maxPIZZA = 0;

    private int maxWINE = 0;

    /**
* Estos atributos representan el "porcentaje" minimo de la ontologias alcanzado en una p�gina html
*   se inicia con un valor muy alto para ir reduciendo
*/
    private int minVERTEBRADOS = 1000000;

    private int minPARENTESCOS = 1000000;

    private int minFOOD = 1000000;

    private int minPIZZA = 1000000;

    private int minWINE = 1000000;

    /**
* Estos atributos representan el "numero de p�ginas" con presencia de la ontologias asociada
*/
    private int numVERTEBRADOS = 0;

    private int numPARENTESCOS = 0;

    private int numFOOD = 0;

    private int numPIZZA = 0;

    private int numWINE = 0;

    int Vistas3 = 0, Vistas2 = 0, Vistas1 = 0;

    public porcentajeOntologico(String fichero) throws Exception {
        try {
            this.fichero = fichero;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(fichero);
            this.parseo = true;
            if (this.parseo) {
                doc.normalize();
                NavegadorXML nav = new NavegadorXML(doc);
                Node t;
                String nombreElemento = "", nombreAtributo = "";
                nav.goToRoot();
                numFicheros = Integer.parseInt(nav.getAtributoValueByName("numero_ficheros"));
                nav.childNode();
                nav.nextElement();
                nav.childNode();
                String paginaAnalizadaBak1 = "", paginaAnalizadaBak2 = "", paginaAnalizada = "";
                while (nav.nextElement() != null) {
                    nombreElemento = nav.getThisNodeName();
                    if (nombreElemento.equals("fichero")) {
                        paginaAnalizada = nav.getAtributoValueByName("html");
                        if (nav.getAtributoValueByName("ontologia").equals("VERTEBRADOS")) {
                            porcentajeVERTEBRADOS = porcentajeVERTEBRADOS + Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            numero++;
                            numVERTEBRADOS++;
                            if (Integer.parseInt(nav.getAtributoValueByName("frecuencia")) > maxVERTEBRADOS) {
                                maxVERTEBRADOS = Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            }
                            if (Integer.parseInt(nav.getAtributoValueByName("frecuencia")) < minVERTEBRADOS) {
                                minVERTEBRADOS = Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            }
                            if (paginaAnalizada.equals(paginaAnalizadaBak1) && paginaAnalizada.equals(paginaAnalizadaBak2)) {
                                Vistas3++;
                            } else if (paginaAnalizada.equals(paginaAnalizadaBak1) || paginaAnalizada.equals(paginaAnalizadaBak2)) {
                                Vistas2++;
                            } else {
                                Vistas1++;
                            }
                        }
                        if (nav.getAtributoValueByName("ontologia").equals("PARENTESCOS")) {
                            porcentajePARENTESCOS = porcentajePARENTESCOS + Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            numero++;
                            numPARENTESCOS++;
                            if (Integer.parseInt(nav.getAtributoValueByName("frecuencia")) > maxPARENTESCOS) {
                                maxPARENTESCOS = Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            }
                            if (Integer.parseInt(nav.getAtributoValueByName("frecuencia")) < minPARENTESCOS) {
                                minPARENTESCOS = Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            }
                            if (paginaAnalizada.equals(paginaAnalizadaBak1) && paginaAnalizada.equals(paginaAnalizadaBak2)) {
                                Vistas3++;
                            } else if (paginaAnalizada.equals(paginaAnalizadaBak1) || paginaAnalizada.equals(paginaAnalizadaBak2)) {
                                Vistas2++;
                            } else {
                                Vistas1++;
                            }
                        }
                        if (nav.getAtributoValueByName("ontologia").equals("FOOD")) {
                            porcentajeFOOD = porcentajeFOOD + Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            numero++;
                            numFOOD++;
                            if (Integer.parseInt(nav.getAtributoValueByName("frecuencia")) > maxFOOD) {
                                maxFOOD = Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            }
                            if (Integer.parseInt(nav.getAtributoValueByName("frecuencia")) < minFOOD) {
                                minFOOD = Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            }
                            if (paginaAnalizada.equals(paginaAnalizadaBak1) && paginaAnalizada.equals(paginaAnalizadaBak2)) {
                                Vistas3++;
                            } else if (paginaAnalizada.equals(paginaAnalizadaBak1) || paginaAnalizada.equals(paginaAnalizadaBak2)) {
                                Vistas2++;
                            } else {
                                Vistas1++;
                            }
                        }
                        if (nav.getAtributoValueByName("ontologia").equals("PIZZA")) {
                            porcentajePIZZA = porcentajePIZZA + Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            numero++;
                            numPIZZA++;
                            if (Integer.parseInt(nav.getAtributoValueByName("frecuencia")) > maxPIZZA) {
                                maxPIZZA = Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            }
                            if (Integer.parseInt(nav.getAtributoValueByName("frecuencia")) < minPIZZA) {
                                minPIZZA = Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            }
                            if (paginaAnalizada.equals(paginaAnalizadaBak1) && paginaAnalizada.equals(paginaAnalizadaBak2)) {
                                Vistas3++;
                            } else if (paginaAnalizada.equals(paginaAnalizadaBak1) || paginaAnalizada.equals(paginaAnalizadaBak2)) {
                                Vistas2++;
                            } else {
                                Vistas1++;
                            }
                        }
                        if (nav.getAtributoValueByName("ontologia").equals("WINE")) {
                            porcentajeWINE = porcentajeWINE + Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            numero++;
                            numWINE++;
                            if (Integer.parseInt(nav.getAtributoValueByName("frecuencia")) > maxWINE) {
                                maxWINE = Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            }
                            if (Integer.parseInt(nav.getAtributoValueByName("frecuencia")) < minWINE) {
                                minWINE = Integer.parseInt(nav.getAtributoValueByName("frecuencia"));
                            }
                            if (paginaAnalizada.equals(paginaAnalizadaBak1) && paginaAnalizada.equals(paginaAnalizadaBak2)) {
                                Vistas3++;
                            } else if (paginaAnalizada.equals(paginaAnalizadaBak1) || paginaAnalizada.equals(paginaAnalizadaBak2)) {
                                Vistas2++;
                            } else {
                                Vistas1++;
                            }
                        }
                    }
                    paginaAnalizadaBak2 = paginaAnalizadaBak1;
                    paginaAnalizadaBak1 = paginaAnalizada;
                }
                porcentajeMax = numero * 100;
            }
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public double getPorcentajeVERTEBRADOS() {
        double porcentaje = 0.0;
        double tot = (double) 100.0 * (double) this.numero;
        porcentaje = ((double) this.porcentajeVERTEBRADOS * (double) 100.0) / tot;
        return porcentaje;
    }

    public double getPorcentajePARENTESCOS() {
        double porcentaje = 0.0;
        double tot = (double) 100.0 * (double) this.numero;
        porcentaje = ((double) this.porcentajePARENTESCOS * (double) 100.0) / tot;
        return porcentaje;
    }

    public double getPorcentajeFOOD() {
        double porcentaje = 0.0;
        double tot = (double) 100.0 * (double) this.numero;
        porcentaje = ((double) this.porcentajeFOOD * (double) 100.0) / tot;
        return porcentaje;
    }

    public double getPorcentajePIZZA() {
        double porcentaje = 0.0;
        double tot = (double) 100.0 * (double) this.numero;
        porcentaje = ((double) this.porcentajePIZZA * (double) 100.0) / tot;
        return porcentaje;
    }

    public double getPorcentajeWINE() {
        double porcentaje = 0.0;
        double tot = (double) 100.0 * (double) this.numero;
        porcentaje = ((double) this.porcentajeWINE * (double) 100.0) / tot;
        return porcentaje;
    }

    public int getMaxWINE() {
        return this.maxWINE;
    }

    public int getMaxVERTEBRADOS() {
        return this.maxVERTEBRADOS;
    }

    public int getMaxPARENTESCOS() {
        return this.maxPARENTESCOS;
    }

    public int getMaxFOOD() {
        return this.maxFOOD;
    }

    public int getMaxPIZZA() {
        return this.maxPIZZA;
    }

    public int getMinWINE() {
        return this.minWINE;
    }

    public int getMinVERTEBRADOS() {
        return this.minVERTEBRADOS;
    }

    public int getMinPARENTESCOS() {
        return this.minPARENTESCOS;
    }

    public int getMinFOOD() {
        return this.minFOOD;
    }

    public int getMinPIZZA() {
        return this.minPIZZA;
    }

    public int getNumeroRegistrosProcesados() {
        return this.numero;
    }

    public int getNumeroVistasSemanticas(int n) {
        int numTuplaOrdenN = 0;
        if (n == 1) {
            numTuplaOrdenN = this.Vistas1 - this.Vistas2;
        }
        if (n == 2) {
            numTuplaOrdenN = this.Vistas2 - this.Vistas3;
        }
        if (n == 3) {
            numTuplaOrdenN = this.Vistas3;
        }
        return numTuplaOrdenN;
    }

    public int getNumeroTotalPaginasSemanticas() {
        int numTuplaOrdenN = 0;
        numTuplaOrdenN = this.Vistas1 - this.Vistas2;
        numTuplaOrdenN = numTuplaOrdenN + (this.Vistas2 - this.Vistas3);
        numTuplaOrdenN = numTuplaOrdenN + this.Vistas3;
        return numTuplaOrdenN;
    }

    public int getNumeroFicherosATransformar() {
        return this.numFicheros;
    }
}
