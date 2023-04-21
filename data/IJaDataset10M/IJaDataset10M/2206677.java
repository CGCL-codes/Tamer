package unbbayes.exemplo;

import unbbayes.jprs.jbn.*;
import unbbayes.io.*;
import java.io.*;

/**
 * Title: Exemplo de Uso da API atrav�s de um Modo de Texto
 * Description: Essa classe feita em JAVA abre um arquivo ".net", "asia.net". Depois esse arquivo �
 *              carregado, modificado em algumas partes e ent�o compilado. Essa classe tem a fun��o
 *              de apenas exemplificar como se pode usar a API desenvolvida para trabalhar com
 *              Redes Bayesianas.
 * Copyright:   Copyright (c) 2001
 * Company:     UnB - Universidade de Bras�lia
 * @author      Rommel Novaes Carvalho
 * @author      Michael S. Onishi
 */
public class ModoTexto {

    public static void main(String[] args) {
        ProbabilisticNetwork rede = null;
        try {
            BaseIO io = new NetIO();
            rede = io.load(new File("./exemplos/asia.net"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        ProbabilisticNode auxVP = new ProbabilisticNode();
        auxVP.setName("K");
        auxVP.setDescription("Vari�vel de Teste");
        auxVP.appendState("Estado 0");
        auxVP.appendState("Estado 1");
        PotentialTable auxTabPot = auxVP.getPotentialTable();
        auxTabPot.addVariable(auxVP);
        auxTabPot.addValueAt(0, 0.99);
        auxTabPot.addValueAt(1, 0.01);
        rede.addNode(auxVP);
        ProbabilisticNode auxVP2 = (ProbabilisticNode) rede.getNode("A");
        Edge auxArco = new Edge(auxVP, auxVP2);
        rede.addEdge(auxArco);
        try {
            rede.compile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        double likelihood[] = new double[auxVP.getStatesSize()];
        likelihood[0] = 1.0;
        likelihood[1] = 0.8;
        auxVP.addLikeliHood(likelihood);
        try {
            rede.updateEvidences();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
