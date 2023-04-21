package br.edu.ufcg.lsd.seghidro.server.util;

/**
 * Classe que contém os Padrões para criação dos Cabeçalhos dos arquivos
 * @author Romeryto Lira
 *
 */
public class Cabecalho {

    private static final String FIM_DE_LINHA = System.getProperty("line.separator");

    /**
	 * Método que retorna um String com o padrão do Cabeçalho Principal
	 * @return Cabeçalho Principal
	 */
    public static String getPatternCabecalhoPrincipal() {
        return "!MandaChuva - Sistema de Busca de Dados Observados" + FIM_DE_LINHA + "!SegHidro - http://seghidro.lsd.ufcg.edu.br" + FIM_DE_LINHA;
    }

    /**
	 * Método que retorna um String com o padrão do Cabeçalho de Município
	 * @return Cabeçalho do Município
	 */
    public static String getPatternLinhaCabecalhoMunicipio() {
        return "!Código=%d Município=%s" + FIM_DE_LINHA + FIM_DE_LINHA;
    }

    /**
	 * Método que retorna um String com o padrão do Cabeçalho da Região
	 * @return Cabeçalho da Região
	 */
    public static String getPatternLinhaCabecalhoRegiao() {
        return "!Código=%d Região=%s" + FIM_DE_LINHA + FIM_DE_LINHA;
    }

    /**
	 * Método que retorna um String com o padrão do Cabeçalho da Sub-bacia
	 * @return Cabeçalho Da Sub-bacia
	 */
    public static String getPatternLinhaCabecalhoSubBacia() {
        return "!Código=%d Sub-Bacia=%s" + FIM_DE_LINHA + FIM_DE_LINHA;
    }

    /**
	 * Método que retorna um String com o padrão do Cabeçalho da Bacia
	 * @return Cabeçalho da Bacia
	 */
    public static String getPatternLinhaCabecalhoBacia() {
        return "!Código=%d Bacia=%s" + FIM_DE_LINHA + FIM_DE_LINHA;
    }

    /**
	 * Método que retorna um String com o padrão do Cabeçalho do Posto Pluviométrico
	 * @return Cabeçalho do Posto Pluviométrico
	 */
    public static String getPatternLinhaCabecalhoPostoPluviometrico() {
        return FIM_DE_LINHA + "!Código=%d Posto_Pluviométrico=%s" + FIM_DE_LINHA + FIM_DE_LINHA;
    }

    /**
	 * Método que retorna um String com o padrão do Cabeçalho do Posto de Reservatório
	 * @return Cabeçalho do Posto de Reservatório
	 */
    public static String getPatternLinhaCabecalhoPostoDeReservatorio() {
        return FIM_DE_LINHA + "!Código=%d Posto_Reservatório=%s" + FIM_DE_LINHA + FIM_DE_LINHA;
    }
}
