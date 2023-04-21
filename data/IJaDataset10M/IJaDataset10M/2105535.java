package example;

import br.ufg.integrate.controller.Controller;
import br.ufg.integrate.controller.ControllerI;
import br.ufg.integrate.exception.IntegrateException;

/**
 * Mediator que envia uma cole��o de consultas SQL para uma
 * cole��o de fonte de dados, definidas no arquivo
 * 'integrate-datasources.xml'.
 * 
 * No final, � impresso o tempo total com as consultas.
 */
public class TestWrapper3 {

    public static void main(String[] args) {
        ControllerI c = Controller.getInstance();
        try {
            String[] idDataSources = { "csv", "hsqldb" };
            String[] sql = { "SELECT CODINSTRUTOR, NOMEINSTRUTOR FROM DEPTO WHERE NOMECURSO = 'Engenharia de software'", "SELECT CODCLIENTE, NOME, SOBRENOME FROM CLIENTES" };
            StringBuilder s = c.executeQueryInXmlFormat(idDataSources, sql);
            c.stop();
            System.out.println(s.toString());
            System.out.println("\nTempo total (ms): " + c.getTotalTime());
        } catch (IntegrateException e) {
            e.printStackTrace();
        }
    }
}
