package unbbayes.prs.mebn.compiler;

import unbbayes.prs.Node;
import unbbayes.prs.bn.PotentialTable;
import unbbayes.prs.bn.ProbabilisticTable;
import unbbayes.prs.mebn.exception.MEBNException;
import unbbayes.prs.mebn.ssbn.SSBNNode;

public interface ICompiler {

    /**
	 * Initializes compiler. Sets the text to parse by
	 * parse() method
	 * @param text: text to parse
	 * @see unbbayes.gui.UnBBayesFrame.prs.mebn.compiler.ICompiler#parse()
	 */
    public abstract void init(String text);

    /**
	 * Parse the string passed by init method
	 * @see unbbayes.gui.UnBBayesFrame.prs.mebn.compiler.ICompiler#init(String)
	 */
    public abstract void parse() throws MEBNException;

    /**
	 * Use this method to determine where the error has occurred
	 * @return Returns the last read index.
	 */
    public abstract int getIndex();

    /**
	 * generates CPT using a pseudocode.
	 * @param ssbnnode: a SSBN-generation time node containing informations about a
	 * resident node having a pseudocode to parse, every parent-child structure previously
	 * built and a reference to a ProbabilisticNode which the generated CPT should be 
	 * whitten.
	 * @return a reference to the generated PotentialTable (which also can be accessed from
	 * the ProbabilisticNode contained inside the ssbnnode).
	 */
    public PotentialTable generateCPT(SSBNNode ssbnnode) throws MEBNException;
}
