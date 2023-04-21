package net.sf.sasl.language.placeholder.aop.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import net.sf.sasl.language.placeholder.aop.interpreter.IEnvironment;
import net.sf.sasl.language.placeholder.aop.interpreter.IPlaceholderResolver;
import net.sf.sasl.language.placeholder.aop.interpreter.PlaceholderResolverSymbolTable;
import net.sf.sasl.language.placeholder.aop.interpreter.ResolveException;
import org.junit.Test;

/**
 * Test cases for the
 * {@link net.sf.sasl.language.placeholder.aop.interpreter.PlaceholderResolverSymbolTable
 * PlaceholderResolverSymbolTable} class.
 * 
 * @author Philipp Förmer
 * 
 */
public class PlaceholderResolverSymbolTableTest {

    /**
	 * Test cases for the
	 * {@link net.sf.sasl.language.placeholder.aop.interpreter.PlaceholderResolverSymbolTable#size()
	 * size()} method.
	 * 
	 */
    @Test
    public void testSize() {
        PlaceholderResolverSymbolTable underTest = new PlaceholderResolverSymbolTable();
        Assert.assertEquals(0, underTest.size());
        List<IPlaceholderResolver> l = new ArrayList<IPlaceholderResolver>(Arrays.asList(new DummyPlaceholderResolver("a", "b")));
        underTest = new PlaceholderResolverSymbolTable(l);
        Assert.assertEquals(2, underTest.size());
    }

    /**
	 * Test cases for the
	 * {@link net.sf.sasl.language.placeholder.aop.interpreter.PlaceholderResolverSymbolTable#isEmpty()
	 * isEmpty()} method.
	 * 
	 */
    @Test
    public void testIsEmpty() {
        PlaceholderResolverSymbolTable underTest = new PlaceholderResolverSymbolTable();
        Assert.assertTrue(underTest.isEmpty());
        List<IPlaceholderResolver> l = new ArrayList<IPlaceholderResolver>(Arrays.asList(new DummyPlaceholderResolver("a")));
        underTest = new PlaceholderResolverSymbolTable(l);
        Assert.assertFalse(underTest.isEmpty());
    }

    /**
	 * Test cases for the
	 * {@link net.sf.sasl.language.placeholder.aop.interpreter.PlaceholderResolverSymbolTable#lookUp(String)
	 * lookUp(String)} method.
	 * 
	 */
    @Test
    public void testLookUp() {
        PlaceholderResolverSymbolTable underTest = new PlaceholderResolverSymbolTable();
        Assert.assertNull(underTest.lookUp("abc"));
        DummyPlaceholderResolver dummy = new DummyPlaceholderResolver("a", "b");
        List<IPlaceholderResolver> l = new ArrayList<IPlaceholderResolver>(Arrays.asList(dummy));
        underTest = new PlaceholderResolverSymbolTable(l);
        Assert.assertEquals(dummy, underTest.lookUp("a"));
        Assert.assertEquals(dummy, underTest.lookUp("b"));
        Assert.assertNull(underTest.lookUp("c"));
    }

    private class DummyPlaceholderResolver implements IPlaceholderResolver {

        private final Set<String> set = new HashSet<String>();

        public DummyPlaceholderResolver(String... placeholderNames) {
            set.addAll(Arrays.asList(placeholderNames));
        }

        /**
		 * @see net.sf.sasl.language.placeholder.aop.interpreter.IPlaceholderResolver#getResolveablePlaceholders()
		 */
        public Set<String> getResolveablePlaceholders() {
            return set;
        }

        /**
		 * @see net.sf.sasl.language.placeholder.aop.interpreter.IPlaceholderResolver#resolve(java.lang.String,
		 *      java.lang.Object[],
		 *      net.sf.sasl.language.placeholder.aop.interpreter.IEnvironment)
		 */
        public Object resolve(String placeholderName, Object[] placeholderArguments, IEnvironment environment) throws ResolveException {
            return null;
        }
    }
}
