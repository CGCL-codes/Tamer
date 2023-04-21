package hephaestus.model.transformer;

import java.util.Map;
import hephaestus.model.metamodel.Model;

public abstract class Transformer {

    /**
	 * @label transforma
	 */
    protected Model model;

    public Transformer(Map initParams) {
        if (initParams == null) {
            throw new IllegalArgumentException("N�o foi poss�vel acessar os par�metros iniciais para gera��o de c�digo");
        }
        initialize(initParams);
    }

    /**
	 * Executa uma transforma��o
	 * 
	 * @throws Exception
	 */
    public abstract void transform() throws Exception;

    /**
	 * Prepara o transformador para a transforma��o
	 * 
	 */
    public abstract void initialize(Map initParams);

    /**
	 * @return Retorna o model.
	 */
    public Model getModel() {
        return model;
    }

    /**
	 * @param model
	 *            O model a ser definido.
	 */
    public void setModel(Model model) {
        this.model = model;
    }
}
