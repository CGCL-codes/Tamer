package ikube.cluster.cache;

import java.util.List;

/**
 * No maven support, and this project looks like it is dying.
 * 
 * @see ICache
 * @author Michael Couck
 * @since 01.10.11
 * @version 01.00
 */
public class CacheJcs implements ICache {

    public void initialise() throws Exception {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int size(final String name) {
        return 0;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public <T extends Object> T get(final String name, final Long id) {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public <T extends Object> void set(final String name, final Long id, final T object) {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void remove(final String name, final Long id) {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public <T extends Object> List<T> get(final String name, final ICriteria<T> criteria, final IAction<T> action, final int size) {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void clear(final String name) {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public <T extends Object> T get(final String name, final String sql) {
        return null;
    }

    @Override
    public boolean lock(String name) {
        return false;
    }

    @Override
    public boolean unlock(String name) {
        return false;
    }
}
