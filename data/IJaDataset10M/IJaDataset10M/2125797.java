package br.com.srv.componentes.relatorio.velocidademedia.util;

import br.com.srv.model.PontoTO;

public class GeometriaAnalitica {

    public static final double MUITO_PERTO = 0.99997;

    public static final double PERTO = 0.9997;

    public static final double NAO_MUITO_PERTO = 0.9989;

    public static final double MEIO_LONGE = 0.9970;

    public static double distanciaEntrePontos(PontoTO q, PontoTO p) {
        double dx = p.getLongitude() - q.getLongitude();
        double dy = p.getLatitude() - q.getLatitude();
        return 1 - new Double(Math.sqrt(dx * dx + dy * dy));
    }

    public static void main(String[] args) {
        PontoTO pontoTO = new PontoTO();
        PontoTO pontoTO2 = new PontoTO();
        pontoTO.setLongitude(new Double(-38.53927731513977));
        pontoTO.setLatitude(new Double(-3.75090846177725));
        pontoTO2.setLongitude(new Double(-38.53910565376281));
        pontoTO2.setLatitude(new Double(-3.75090846177725));
        System.out.println("perto: " + distanciaEntrePontos(pontoTO, pontoTO2));
        pontoTO.setLongitude(new Double(-38.54012489318847));
        pontoTO.setLatitude(new Double(-3.74940964097829));
        pontoTO2.setLongitude(new Double(-38.54010343551635));
        pontoTO2.setLatitude(new Double(-3.74940964097829));
        System.out.println("muito perto: " + distanciaEntrePontos(pontoTO, pontoTO2));
        pontoTO.setLongitude(new Double(-38.54012489318847));
        pontoTO.setLatitude(new Double(-3.74940964097829));
        pontoTO2.setLongitude(new Double(-38.53912711143495));
        pontoTO2.setLatitude(new Double(-3.74975222881604));
        System.out.println("long: " + distanciaEntrePontos(pontoTO, pontoTO2));
    }
}
