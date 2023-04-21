package negocio;

public class Computador extends Produto {

    private String processador;

    private String memoria;

    private String hd;

    private String monitor;

    public Computador(String nome, double preco, UnidadeMedida unidade, char disponivel, String processador, String memoria, String hd, String monitor) {
        super(nome, preco, unidade, disponivel);
        this.processador = processador;
        this.memoria = memoria;
        this.hd = hd;
        this.monitor = monitor;
    }

    public void imprime() {
        super.imprime();
        System.out.print("\n\tProcessador: " + this.processador);
        System.out.print("\n\tMem�ria: " + this.memoria);
        System.out.print("\n\tHD: " + this.hd);
        System.out.print("\n\tMonitor: " + this.monitor);
    }
}
