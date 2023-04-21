package gerenciadorescola.model.pattern.strategy;

public class Matematica extends Disciplina {

    public Matematica() {
        comportamento_avaliacao = new ComportamentoTresProvas();
    }
}
