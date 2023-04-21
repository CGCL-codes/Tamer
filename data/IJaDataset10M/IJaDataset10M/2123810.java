package net.marloncarvalho.loteriaz.extrator.impl;

import java.util.Date;
import java.util.TreeSet;
import net.marloncarvalho.alfred.loterias.Loterias;
import net.marloncarvalho.loteriaz.entidades.Concurso;
import net.marloncarvalho.loteriaz.entidades.Loteria;
import net.marloncarvalho.loteriaz.entidades.Sorteio;
import net.marloncarvalho.loteriaz.extrator.Extrator;
import net.marloncarvalho.loteriaz.rn.NegocioException;
import net.marloncarvalho.loteriaz.rn.NegocioFactory;
import net.marloncarvalho.loteriaz.rn.impl.LoteriaNegocio;

/**
 * Extrator de Megasena.
 * 
 * @author Marlon Silva Carvalho
 * @since 15/07/2009
 */
public class MegasenaExtrator implements Extrator {

    public void extrair() {
        try {
            int numUltimoConcurso = Integer.valueOf(Loterias.obterNumeroUltimoConcursoMegaSena());
            Concurso ultimoConcursoCadastrado = NegocioFactory.getInstancia().getMegasenaRN().obterUltimoConcurso();
            int numUltimoConcursoCad = 1;
            if (ultimoConcursoCadastrado != null) {
                numUltimoConcursoCad = Integer.valueOf(ultimoConcursoCadastrado.getNumero());
            }
            Loteria loteria = (Loteria) NegocioFactory.getInstancia().getLoteriaRN().obter(LoteriaNegocio.MEGASENA);
            for (int i = numUltimoConcursoCad; i <= numUltimoConcurso; i++) {
                if (numUltimoConcursoCad == numUltimoConcurso) break;
                Concurso concurso = new Concurso();
                concurso.setData(new Date());
                concurso.setLoteria(loteria);
                concurso.setNumero(String.valueOf(i));
                NegocioFactory.getInstancia().getConcursoRN().salvar(concurso);
                Sorteio sorteio = new Sorteio();
                sorteio.setConcurso(concurso);
                String[] dezenas = Loterias.obterResultadoMegaSena(String.valueOf(i));
                TreeSet<String> ts = new TreeSet<String>();
                for (int j = 0; j < dezenas.length; j++) {
                    ts.add(dezenas[j]);
                }
                StringBuilder sb = new StringBuilder();
                for (String s : ts) {
                    sb.append(s);
                    sb.append(" ");
                }
                sorteio.setDezenas(sb.toString());
                NegocioFactory.getInstancia().getSorteioRN().salvar(sorteio);
            }
        } catch (NegocioException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MegasenaExtrator me = new MegasenaExtrator();
        me.extrair();
    }
}
