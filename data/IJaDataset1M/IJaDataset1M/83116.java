package br.farn.web3;

/**
 * A classe Computador re�ne atributos que referenciam outras classes e 
 * oferece duas formas de resolver essas depend�ncias sem ajuda de frameworks.
 * 
 * @author Daniel Siqueira
 */
public class Computador implements IComputador {

    /** Resolver a depend�ncia atrav�s de inst�ncia direta com forte acoplamento,
	 * ou atrav�s do padr�o Factory reduzindo o acoplamento */
    public enum howToResolveDependecy {

        /** Inst�ncia direta */
        NEW, /** Inst�ncia via Factory*/
        FACTORY
    }

    private Monitor monitor;

    private Teclado teclado;

    private Impressora impressora;

    /**
	 * Construtor da classe com op��o de resolver as depend�ncias diretamente,
	 * ou utilizando Factory
	 */
    public Computador(howToResolveDependecy option) {
        switch(option) {
            case NEW:
                initNew();
                break;
            case FACTORY:
                initFactory();
                break;
            default:
                break;
        }
    }

    /**
	 * Inicializando os atributos com instancia��o direta.
	 * <br />Forte acoplamento.
	 */
    public void initNew() {
        monitor = new MonitorCRT();
        teclado = new TecladoPS2();
        impressora = new ImpressoraDeskjet();
    }

    /**
	 * Inicializando os atributos com instancia��o via Factory.
	 * <br />"M�dio" acoplamento.
	 */
    public void initFactory() {
        monitor = Factory.getMonitor();
        teclado = Factory.getTeclado();
        impressora = Factory.getImpressora();
    }

    /**
	 * Ligar o computador.
	 * <br />
	 * � necess�rio que os atributos estejam inicializados 
	 * (depend�ncias resolvidas).
	 */
    @Override
    public void ligar() {
        System.out.println("\n\t -- Computador Simples -- ");
        monitor.exibir("Digite um texto para impress�o");
        String texto = teclado.ler();
        impressora.imprimir(texto);
    }
}
