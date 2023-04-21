package net.sourceforge.dita4publishers.util;

import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

/**
 * Manages creation and access to a master Xerces grammar pool.
 * The grammar pool is managed as a ThreadLocal variable so it can
 * be used across Ant task invocations.
 */
public class GrammarPoolManager {

    public static XMLGrammarPool initializeGrammarPool() {
        XMLGrammarPool pool = null;
        try {
            pool = new XMLGrammarPoolImpl();
        } catch (Exception e) {
            System.out.println("Failed to create Xerces grammar pool for caching DTDs and schemas");
        }
        grammarPool.set(pool);
        return pool;
    }

    private static ThreadLocal<XMLGrammarPool> grammarPool = new ThreadLocal<XMLGrammarPool>() {

        @SuppressWarnings("unused")
        protected synchronized XMLGrammarPool initialvalue() {
            XMLGrammarPool grammarPool = initializeGrammarPool();
            set(grammarPool);
            return grammarPool;
        }
    };

    /**
	 * @return 
	 * 
	 */
    public static XMLGrammarPool getGrammarPool() {
        XMLGrammarPool pool = grammarPool.get();
        if (pool == null) pool = initializeGrammarPool();
        return pool;
    }
}
