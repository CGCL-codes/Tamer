package teste;

import beans.CarteiraAcoes;
import beans.CarteirasExportadas;
import beans.Usuarios;
import dao.ConexaoBD;
import java.util.GregorianCalendar;
import java.util.List;
import parsers.CarteirasXMLParser;

/**
 *
 * @author Tati
 */
public class testeXML3 {

    public static void main(String[] args) {
        ConexaoBD con = new ConexaoBD();
        Usuarios usu = con.buscarUsuario("09887675431");
        List<CarteiraAcoes> carteiras = usu.getCarteiraAcoesList();
        for (CarteiraAcoes cart : carteiras) {
            System.out.println(cart.getCodAcao() + ":" + cart.getQuantidade());
        }
        CarteirasExportadas exp = new CarteirasExportadas();
        exp.setDatahora(new GregorianCalendar());
        exp.addUsuario(usu);
        CarteirasXMLParser parser = new CarteirasXMLParser();
        String xml = parser.geraXML(exp);
        parser.salvaArquivoXML(xml, "C://testeCarteira.xml");
    }
}
