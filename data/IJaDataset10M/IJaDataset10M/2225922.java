package glaureano.concursos;

import glaureano.uteis.Ordenacao;
import glaureano.uteis.leitorArquivos.Comparador;
import glaureano.uteis.leitorArquivos.LeitorArquivos;
import glaureano.uteis.leitorArquivos.ouvidor.ListMaker;
import glaureano.uteis.leitorArquivos.ouvidor.ObjectPrinter;
import java.util.List;

public class LerResultado {

    public static void main(String[] args) {
        ListMaker<Candidato> listMaker = new ListMaker<Candidato>(new Comparador(Ordenacao.DESCENDENTE, CandidatoGetter.PONTUACAO, CandidatoGetter.NOME));
        LeitorArquivos<Candidato> fileReader = new LeitorArquivos<Candidato>(new TradutorCandidato(), listMaker);
        fileReader.ler("resultados/2007.trt-12.txt");
        List<Candidato> lista = listMaker.getLista();
        ObjectPrinter<Candidato> printer = new ObjectPrinter<Candidato>("\t", CandidatoGetter.NUMERO_INSCRICAO, CandidatoGetter.NOME, CandidatoGetter.PONTUACAO_OBJETIVA, CandidatoGetter.PONTUACAO_DISCURSIVA, CandidatoGetter.PONTUACAO);
        System.out.println("inicio");
        for (Candidato candidato : lista) {
            printer.receba(candidato);
        }
        System.out.println("fim");
    }
}
