package jp.go.ipa.jgcl;

/**
 *                _         \   C   ^ [ t F C X B
 *
 * @version $Revision: 1.4 $, $Date: 2000/04/26 09:38:42 $
 * @author Information-technology Promotion Agency, Japan
 */
public interface BooleanFunctionWithRealVariables {

    /**
	 *        ]       B
	 *
	 * @param parameter	     l   z
	 * @return	 ^             l   ]             l
	 */
    public boolean evaluate(double[] parameter);
}
