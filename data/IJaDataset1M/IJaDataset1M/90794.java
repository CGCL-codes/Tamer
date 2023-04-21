package br.com.algdam.facade;

import br.com.algdam.dicionarios.ArvoreBinaria;
import br.com.algdam.dicionarios.Binaria;
import br.com.algdam.dicionarios.Hash;
import br.com.algdam.dicionarios.Sequencial;
import br.com.algdam.dominio.DadoPesquisa;
import br.com.algdam.dominio.Palavra;
import br.com.algdam.dominio.TiposDicionario;
import br.com.algdam.gui.Dialog;
import br.com.algdam.gui.Janela;
import br.com.algdam.services.TimeService;

/**
 *
 * @author Tulio
 */
public class InserirFacade {

    public void inserir(TiposDicionario tipos, Palavra palavra) {
        System.out.print("Inserção ");
        switch(tipos) {
            case SEQUENCIAL:
                {
                    System.out.println("Sequencial");
                    insercaoSequencial(palavra);
                    break;
                }
            case BINARIA:
                {
                    System.out.println("Binária");
                    insercaoBinaria(palavra);
                    break;
                }
            case HASH:
                {
                    System.out.println("Hash");
                    insercaoHash(palavra);
                    break;
                }
            case ARVORE_BINARIA:
                {
                    System.out.println("Árvore Binária");
                    insercaoArvoreBinaria(palavra);
                    break;
                }
        }
    }

    public void insercaoBinaria(Palavra palavra) {
        Binaria bin = new Binaria();
        TimeService service = new TimeService();
        service.calculaInsercao(bin, palavra);
        DadoPesquisa dado = new DadoPesquisa();
        dado.setTempo(service.getTempoExecucao());
        Dialog.info("Tempo de inserção: " + dado.getTempoInMilis() + "ms", Janela.getInstance());
    }

    public void insercaoSequencial(Palavra palavra) {
        Sequencial seq = new Sequencial();
        TimeService service = new TimeService();
        service.calculaInsercao(seq, palavra);
        DadoPesquisa dado = new DadoPesquisa();
        dado.setTempo(service.getTempoExecucao());
        Dialog.info("Tempo de inserção: " + dado.getTempoInMilis() + "ms", Janela.getInstance());
    }

    public void insercaoHash(Palavra palavra) {
        Hash hash = new Hash();
        TimeService service = new TimeService();
        service.calculaInsercao(hash, palavra);
        DadoPesquisa dado = new DadoPesquisa();
        dado.setTempo(service.getTempoExecucao());
        Dialog.info("Tempo de inserção: " + dado.getTempoInMilis() + "ms", Janela.getInstance());
    }

    public void insercaoArvoreBinaria(Palavra palavra) {
        ArvoreBinaria ab = new ArvoreBinaria();
        TimeService service = new TimeService();
        service.calculaInsercao(ab, palavra);
        DadoPesquisa dado = new DadoPesquisa();
        dado.setTempo(service.getTempoExecucao());
        Dialog.info("Tempo de inserção: " + dado.getTempoInMilis() + "ms", Janela.getInstance());
    }
}
