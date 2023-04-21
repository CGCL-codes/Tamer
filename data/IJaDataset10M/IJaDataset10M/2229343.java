package fsmsim.dataStructure;

/**
 * <p>Title: FSMSim</p>
 *
 * <p>Description: Simulatore di macchine a stati finiti.</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: Bollati, Donati, Gabrielli, Peli</p>
 *
 * @author Bollati, Donati, Gabrielli, Peli
 * @version 3.0
 */
public class State extends Element {

    /**
     * Costruttore
     * @param name
     */
    public State(String name) {
        super(name);
    }

    /**
     * Controlla se l'oggetto passato corrisponde al giusto stato
     * @param aElement
     * @return
     */
    @Override
    public boolean matchElement(Element aElement) {
        return (getName().equals(aElement.getName()));
    }
}
