package alocador.persistencia;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import alocador.entidades.salas.Classificacao;
import alocador.entidades.salas.SubTipo;

public class SalasFileManager {

    private static String FILE_NAME = "DescricaoSalas.txt";

    private static String carregaArquivo() throws IOException {
        BufferedReader fr = new BufferedReader(new FileReader(FILE_NAME));
        String tudo = "";
        String aux = "";
        while ((aux = fr.readLine()) != null) tudo += aux;
        return tudo;
    }

    public static List<Classificacao> carregaClassificacoes() throws IOException {
        return processa(carregaArquivo());
    }

    private static List<Classificacao> processa(String stream) {
        List<Classificacao> classificacoesConcretas = new ArrayList<Classificacao>();
        List<String> classificacoes = Arrays.asList(stream.split("="));
        classificacoes = classificacoes.subList(1, classificacoes.size());
        for (String s : classificacoes) {
            String[] classSplited = s.split("<");
            String nomeClass = classSplited[0];
            List<SubTipo> subTipos = processaSubTipos(classSplited[1]);
            classificacoesConcretas.add(new Classificacao(nomeClass, subTipos));
        }
        return classificacoesConcretas;
    }

    private static List<SubTipo> processaSubTipos(String streamSubTipos) {
        List<SubTipo> subTiposConcretos = new ArrayList<SubTipo>();
        List<String> subTipos = Arrays.asList(streamSubTipos.split("@"));
        subTipos = subTipos.subList(1, subTipos.size());
        for (String s : subTipos) {
            String[] subTipoEPropriedades = s.split(";");
            String nomeSubTipo = subTipoEPropriedades[0];
            String propriedadesAtivadas = "";
            if (!(subTipoEPropriedades.length == 1)) {
                for (String p : subTipoEPropriedades[1].split(",")) {
                    propriedadesAtivadas += p.split(":")[1];
                }
            }
            subTiposConcretos.add(new SubTipo(nomeSubTipo, propriedadesAtivadas));
        }
        return subTiposConcretos;
    }
}
