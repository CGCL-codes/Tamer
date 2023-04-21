package pedrociarlini.mesaderpg;

import pedrociarlini.mesaderpg.business.JogadoresBusiness;
import pedrociarlini.mesaderpg.business.ServidorBusiness;
import pedrociarlini.mesaderpg.model.JogadorVO;
import pedrociarlini.mesaderpg.model.ServidorVO;
import pedrociarlini.mesaderpg.ui.JanelaPrincipal;

public class ClassePrincipal {

    /**
     * @param args
     */
    public static void main(String[] args) {
        registrarClasses();
        JogadoresBusiness.setJogadorLocal(new JogadorVO("Player 1", false));
        ServidorBusiness.setConfiguracaoServidor(new ServidorVO(5544, 2));
        new JanelaPrincipal().setVisible(true);
    }

    private static void registrarClasses() {
        try {
            Class.forName("pedrociarlini.mesaderpg.net.trabalho.ConnLayer");
        } catch (ClassNotFoundException e) {
            System.err.println("N�o foi poss�vel carregar as classes necess�rias.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
