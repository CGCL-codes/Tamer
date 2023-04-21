package appbaratoextreme.util;

import appbaratoextreme.fachada.Fachada;

/**
 *
 * @author marcospaulo
 */
public interface ITela {

    String FACHADA_NULL = "Fachada Não Pode Ser Null";

    public void fachadaNova(Fachada fachadaNova) throws Exception;
}
