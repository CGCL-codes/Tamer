package br.com.jnfe.core;

import org.helianto.document.Customizer;
import br.com.jnfe.base.TpSerie;

/**
 * Interface para s�rie de NF-e.
 * 
 * @author mauriciofernandesdecastro
 */
public interface NFeSerie extends Customizer {

    /**
     * Emitente.
     */
    public Emitente getEmitente();

    /**
	 * Tipo de s�rie.
	 */
    public TpSerie getTpSerie();
}
