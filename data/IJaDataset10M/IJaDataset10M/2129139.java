package system;

import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Rafael
 * @version 1.0
 * @created 24-ago-2007 14:29:30
 */
public class Atributo extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1L;

    /**
	 * Contem os valores da entidade. Se for um experimento, conter� um vector de
	 * worksheets. Se for uma worksheet, conter� um vector de atributos. Se for um
	 * atributo, conter� um vector de float.
	 */
    private Vector<Float> valores;

    private Vector<Float> tempo;

    public Atributo(String str) {
        super(str);
        this.valores = new Vector<Float>();
        this.tempo = new Vector<Float>();
    }

    public Float getValorAt(int index) {
        return valores.elementAt(index);
    }

    public Float getTempoAt(int index) {
        return tempo.elementAt(index);
    }

    public void addValue(Float obj) {
        valores.add(obj);
    }

    public int getValoresVectorSize() {
        return valores.size();
    }

    public void addTempo(Float f) {
        Float aux = new Float(f);
        tempo.add(aux);
    }

    public int getTempoVectorSize() {
        return tempo.size();
    }
}
