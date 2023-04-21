package wrapper.binding;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import wrapper.util.Caret;
import debug.AbstractDebuggable;

/**
 * A container for the binding strength and alignment column of all relevant token
 * combinations of an input code fragment. The values are predetermined and used during
 * the actual wrapping procedure. Thus, the need for determining applicable rules etc.
 * is shifted from runtime to compile time of a tutorial in the dynamic case.
 * 
 * @author Christoph Seidl
 */
public class TokenBindingContainer extends AbstractDebuggable {

    private Map<TokenBindingKey, Integer> bindingStrengthMap;

    private Map<TokenBindingKey, Integer> alignmentColMap;

    /**
	 * Creates a new <code>TokenBindingContainer</code>
	 */
    public TokenBindingContainer() {
        bindingStrengthMap = new HashMap<TokenBindingKey, Integer>();
        alignmentColMap = new HashMap<TokenBindingKey, Integer>();
    }

    /**
	 * Resets the token binding container so that it can be used in another context.
	 */
    public void clear() {
        bindingStrengthMap.clear();
        alignmentColMap.clear();
    }

    /**
	 * Retrieves the binding strength for the combination of token nodes
	 * specified by the key.
	 *  
	 * @param key Specifies the combination of token nodes.
	 * 
	 * @return The binding strength for the combination of token nodes.
	 */
    public int getBindingStrength(TokenBindingKey key) {
        if (bindingStrengthMap.containsKey(key)) {
            return bindingStrengthMap.get(key);
        }
        debug("Error: Binding strength not found!");
        return 1;
    }

    /**
	 * Sets the binding strength for the combination of token nodes
	 * specified by the key.
	 *  
	 * @param key Specifies the combination of token nodes.
	 * @param bindingStrength The binding strength for the combination of token nodes.
	 */
    public void setBindingStrength(TokenBindingKey key, int bindingStrength) {
        bindingStrengthMap.put(key, bindingStrength);
    }

    /**
	 * Retrieves the alignment column for the combination of token nodes
	 * specified by the key.
	 *  
	 * @param key Specifies the combination of token nodes.
	 * 
	 * @return The alignment column for the combination of token nodes.
	 */
    public int getAlignmentCol(TokenBindingKey key) {
        if (alignmentColMap.containsKey(key)) {
            return alignmentColMap.get(key);
        }
        debug("Error: Alignment col not found!");
        return Caret.FIRST_COL;
    }

    /**
	 * Sets the alignment column for the combination of token nodes
	 * specified by the key.
	 *  
	 * @param key Specifies the combination of token nodes.
	 * @param alignmentCol The alignment column for the combination of token nodes.
	 */
    public void setAlignmentCol(TokenBindingKey key, int alignmentCol) {
        alignmentColMap.put(key, alignmentCol);
    }

    /**
	 * {@inheritDoc}
	 */
    public String toString() {
        String s = "";
        Iterator<Entry<TokenBindingKey, Integer>> iterator1 = bindingStrengthMap.entrySet().iterator();
        Iterator<Entry<TokenBindingKey, Integer>> iterator2 = alignmentColMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            Entry<TokenBindingKey, Integer> entry1 = iterator1.next();
            Entry<TokenBindingKey, Integer> entry2 = iterator2.next();
            s += entry1.getKey() + " bindingStrength: " + entry1.getValue() + ", alignmentCol: " + entry2.getValue() + "\n";
        }
        return s;
    }

    /**
	 * For testing only!
	 * 
	 * @return The binding strength map of this token binding container.
	 */
    public Map<TokenBindingKey, Integer> getBindingStrengthMap() {
        return bindingStrengthMap;
    }

    /**
	 * For testing only!
	 * 
	 * @return The alignment column map of this token binding container.
	 */
    public Map<TokenBindingKey, Integer> getAlignmentColMap() {
        return alignmentColMap;
    }
}
