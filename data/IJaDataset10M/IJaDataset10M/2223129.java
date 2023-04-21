package nomads.agents.vo;

import java.util.Iterator;

/**
 * //TODO: O scurta descriere a clasei
 * 
 * @author Victor
 * 
 */
public interface NodeVision {

    /**
	 * Doar peste nodurile <b>parcurse</b>.
	 * @return Un {@link Iterator} peste nodurile vecine.
	 */
    public Iterator neighboursIterator();

    /**
	 * @return toate drumurile care pleaca din acest nod.
	 */
    public Iterator roadsIterator();

    /**
	 * @return valoarea de materiale gasite in acest nod.<br>
	 *         <b> La un al doilea apel va intoarce mereu 0.</b>
	 */
    public double obtainBuildMaterials();

    /**
	 * @return <b>distdest</b> pentru acest nod. Momentan reprezinta numarul de
	 *         hopuri pana la destinatie.
	 */
    public int getDistDest();

    /**
	 * @return <code>true</code> daca acest nod este un nod final (fie ca e
	 *         ultimul) fie ca e unul intamplator.
	 */
    public boolean isEndNode();

    public Integer getId();
}
