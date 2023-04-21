package com.mgensystems.jarindexer.model.index;

import java.text.MessageFormat;
import org.junit.Before;
import org.junit.Test;
import com.mgensystems.jarindexer.model.ModelTest;

/**
 * <b>Title:</b> <br />
 * <b>Description:</b> <br />
 * <b>Changes:</b><li></li>
 * 
 * @author raykroeker@gmail.com
 */
public final class DeleteTest extends ModelTest {

    /** An index. */
    private Index index;

    /** An index model. */
    private IndexModel model;

    /**
	 * Create DeleteTest.
	 * 
	 */
    public DeleteTest() {
        super();
    }

    /**
	 * Setup an index model.
	 * 
	 * @throws IndexExistsException
	 *             if the index exists
	 */
    @Before
    public void before() throws IndexExistsException {
        model = factory.newIndexModel();
        final String name = MessageFormat.format("create-{0}", System.currentTimeMillis());
        index = IndexBuilder.newBuilder().setName(name).newInstance();
        model.createIndex(index);
    }

    /**
	 * Test delete.
	 * 
	 */
    @Test
    public void delete() {
        model.deleteIndex(index);
    }

    /**
	 * Test delete.
	 * 
	 */
    @Test
    public void deleteNonExisting() {
        final String name = MessageFormat.format("create-{0}", System.currentTimeMillis());
        model.deleteIndex(IndexBuilder.newBuilder().setName(name).newInstance());
    }
}
