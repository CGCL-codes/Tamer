package negocio.servico;

import java.sql.ResultSet;
import java.util.Vector;
import negocio.entidade.*;
import database.BDConector;

/**
 * Classe respons�vel em disponibilizar os servi�os relacionados aos DVDs. Esta classe disponibiliza
 * os servi�os para o cliente permitindo opera��es que envolvem os DVDs armazenados na base de dados.
 * @author Luiz Carlos Viana Melo, Gizeli Ribas de Moraes e Denise Lam
 */
public class UCManterDVD {

    /**
	 * Busca um DVD pelo c�digo.
	 * @param codigo c�digo do DVD
	 * @return null caso n�o encontrado
	 */
    public static DVD buscaDVD(int codigo) throws Exception {
        if (BDConector.isConnected()) {
            ResultSet aux = BDConector.executarQuery("SELECT * FROM dvd WHERE idDVD = " + codigo);
            if (aux.next()) {
                Filme f = UCManterFilme.buscaFilme(aux.getInt("Filme_idFilme"));
                DVD d = new DVD();
                d.setCodigo(aux.getInt("idDVD"));
                d.setFilme(f);
                return d;
            }
            return null;
        } else throw new Exception("N�o conectado ao banco");
    }

    /**
	 * Busca os DVDs de um filme. A estrutura filme deve conter o c�digo do filme.
	 * @param filme a estrutura do filme
	 * @return uma lista com os DVDs de um determinado filme
	 */
    public static Vector<DVD> buscaDVDs(Filme filme) throws Exception {
        if (BDConector.isConnected()) {
            ResultSet aux = BDConector.executarQuery("SELECT * FROM dvd WHERE Filme_idFilme = " + filme.getCodigo());
            Vector<DVD> dvds = new Vector<DVD>();
            while (aux.next()) {
                DVD d = new DVD();
                d.setCodigo(aux.getInt("idDVD"));
                d.setFilme(filme);
                dvds.add(d);
            }
            return dvds;
        } else throw new Exception("N�o conectado ao banco");
    }

    /**
	 * Remove todos os DVD de um filme. A estrutura filme deve conter o c�digo do filme.
	 * @param filme a estrutura do filme
	 */
    public static void removeTodosDVDDoFilme(Filme filme) throws Exception {
        if (BDConector.isConnected()) {
            BDConector.executar("DELETE FROM dvd WHERE Filme_idFilme = " + filme.getCodigo());
        } else throw new Exception("N�o conectado ao banco");
    }

    /**
	 * Remove o DVD da base de dados. A estrutura deve conter o c�digo do filme.
	 * @param dvd o DVD a ser removido
	 */
    public static void removeDVD(DVD dvd) throws Exception {
        if (BDConector.isConnected()) {
            BDConector.executar("DELETE FROM dvd WHERE idDVD = " + dvd.getCodigo());
        } else throw new Exception("N�o conectado ao banco");
    }

    /**
	 * Cadastra um DVD. A estrutura deve conter a estrutura do filme contendo o seu respectivo c�digo.
	 * @param dvd um dvd
	 */
    public static void cadastraDVD(DVD dvd) throws Exception {
        if (BDConector.isConnected()) {
            BDConector.executar("INSERT INTO dvd (Filme_idFilme) VALUES (" + dvd.getFilme().getCodigo() + ")");
        } else throw new Exception("N�o conectado ao banco");
    }

    /**
	 * Verifica se o filme est� dispon�vel para loca��o. Verifica se a �ltima loca��o do DVD foi
	 * devolvida. A estrutura deve conter o c�digo do DVD.
	 * @param filme o DVD do filme
	 * @return true se o filme est� dispon�vel, caso contr�rio, false
	 */
    public static boolean isDVDDisponivel(DVD filme) throws Exception {
        if (BDConector.isConnected()) {
            ResultSet aux = BDConector.executarQuery("SELECT dataEntrega FROM locacao WHERE idLocacao = (SELECT MAX(idLocacao) FROM locacao WHERE DVD_idDVD = " + filme.getCodigo() + ");");
            if (aux.next()) {
                if (aux.getDate("dataEntrega") != null) return true; else return false;
            } else return true;
        } else throw new Exception("N�o conectado ao banco");
    }
}
