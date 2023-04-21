package com.cerner.system.rest.resource.organizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import java.net.URI;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract base class for functional test cases of the
 * {@link ResourceOrganizer} API.
 * 
 * @author Alex Horn
 */
public abstract class AbstractResourceOrganizerTest {

    /**
   * URI of the root resource.
   * <p>
   * Its base URI is <code>http://base/uri/</code>.
   * 
   * <pre>
   *       (a) ---- e
   *        |       |
   *        b       f.txt
   *       / \
   *      c   d.txt
   *     /
   *   ..
   * </pre>
   */
    URI a;

    /**
   * URI of a resource that has two children.
   * <p>
   * Its base URI is <code>http://base/uri/</code>.
   * 
   * <pre>
   *        a ----- e
   *        |       |
   *       (b)      f.txt
   *       / \
   *      c   d.txt
   *     /
   *   ..
   * </pre>
   */
    URI ab;

    /**
   * URI of a resource that may have children but is currently only a leaf.
   * <p>
   * Its base URI is <code>http://base/uri/</code>.
   * 
   * <pre>
   *        a ----- e
   *        |       |
   *        b       f.txt
   *       / \
   *     (c)  d.txt
   *     /
   *   ..
   * </pre>
   */
    URI abc;

    /**
   * URI of a resource that has no and will never have children.
   * <p>
   * Its base URI is <code>http://base/uri/</code>.
   * 
   * <pre>
   *        a ----- e
   *        |       |
   *        b       f.txt
   *       / \
   *      c  (d).txt
   *     /
   *   ..
   * </pre>
   */
    URI abd;

    /**
   * URI of a resource that has a different base than {@code a}, {@code b},
   * {@code c}, {@code d}.
   * <p>
   * Its base URI is <code>http://other.base/uri/</code>.
   * 
   * <pre>
   *        a ---- (e)
   *        |       |
   *        b       f.txt
   *       / \
   *      c   d.txt
   *     /
   *   ..
   * </pre>
   */
    URI e;

    /**
   * URI of a resource that shares the same base as {@code e}.
   * 
   * <pre>
   *        a ----- e
   *        |       |
   *        b      (f).txt
   *       / \
   *      c   d.txt
   *     /
   *   ..
   * </pre>
   */
    URI ef;

    private final URI baseURI = URI.create("http://base/uri/");

    /**
   * Class under test
   */
    ResourceOrganizer<Integer> organizer;

    /**
   * Setup a resource structure that takes the following form:
   * 
   * <pre>
   *        a ----- e
   *        |       |
   *        b       f.txt
   *       / \
   *      c   d.txt
   *     /
   *   ..
   * </pre>
   */
    @Before
    public void setup() {
        this.organizer = newResourceOrganizer(this.baseURI);
        this.a = URI.create("http://base/uri/a/");
        this.ab = URI.create("http://base/uri/a/b/");
        this.abc = URI.create("http://base/uri/a/b/c/");
        this.abd = URI.create("http://base/uri/a/b/d.txt");
        this.e = URI.create("http://other.base/uri/e/");
        this.ef = URI.create("http://other.base/uri/e/f.txt");
    }

    /**
   * Create a new {@link ResourceOrganizer} implementation that should be
   * tested.
   * 
   * @param baseURI valid base URI
   * @return new {@link ResourceOrganizer} target test object
   */
    @SuppressWarnings("hiding")
    abstract ResourceOrganizer<Integer> newResourceOrganizer(URI baseURI);

    @After
    public void teardown() {
        this.organizer.clear();
    }

    @Test
    public void clear() {
        putAndGet();
        this.organizer.clear();
        assertNull(this.organizer.resource(this.a).get());
        assertNull(this.organizer.resource(this.ab).get());
        assertNull(this.organizer.resource(this.abc).get());
        assertNull(this.organizer.resource(this.abd).get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNull() {
        this.organizer.resource(this.ab).put((Integer) null);
    }

    @Test
    public void load() {
    }

    @Test
    public void putAndGet() {
        this.organizer.resource(this.a).put(0xA);
        assertEquals(0xA, (int) this.organizer.resource(this.a).get());
        this.organizer.resource(this.ab).put(0xAB);
        assertEquals(0xA, (int) this.organizer.resource(this.a).get());
        assertEquals(0xAB, (int) this.organizer.resource(this.ab).get());
        this.organizer.resource(this.abc).put(0xABC);
        assertEquals(0xA, (int) this.organizer.resource(this.a).get());
        assertEquals(0xAB, (int) this.organizer.resource(this.ab).get());
        assertEquals(0xABC, (int) this.organizer.resource(this.abc).get());
        this.organizer.resource(this.abd).put(0xABD);
        assertEquals(0xA, (int) this.organizer.resource(this.a).get());
        assertEquals(0xAB, (int) this.organizer.resource(this.ab).get());
        assertEquals(0xABC, (int) this.organizer.resource(this.abc).get());
        assertEquals(0xABD, (int) this.organizer.resource(this.abd).get());
        this.organizer.resource(this.e).put(0xE);
        assertEquals(0xA, (int) this.organizer.resource(this.a).get());
        assertEquals(0xAB, (int) this.organizer.resource(this.ab).get());
        assertEquals(0xABC, (int) this.organizer.resource(this.abc).get());
        assertEquals(0xABD, (int) this.organizer.resource(this.abd).get());
        assertEquals(0xE, (int) this.organizer.resource(this.e).get());
        this.organizer.resource(this.ef).put(0xEF);
        assertEquals(0xA, (int) this.organizer.resource(this.a).get());
        assertEquals(0xAB, (int) this.organizer.resource(this.ab).get());
        assertEquals(0xABC, (int) this.organizer.resource(this.abc).get());
        assertEquals(0xABD, (int) this.organizer.resource(this.abd).get());
        assertEquals(0xE, (int) this.organizer.resource(this.e).get());
        assertEquals(0xEF, (int) this.organizer.resource(this.ef).get());
    }

    @Test
    public void putAndGetOppositeOrder() {
        this.organizer.resource(this.ef).put(0xEF);
        assertEquals(0xEF, (int) this.organizer.resource(this.ef).get());
        this.organizer.resource(this.e).put(0xE);
        assertEquals(0xE, (int) this.organizer.resource(this.e).get());
        assertEquals(0xEF, (int) this.organizer.resource(this.ef).get());
        this.organizer.resource(this.abd).put(0xABD);
        assertEquals(0xABD, (int) this.organizer.resource(this.abd).get());
        assertEquals(0xE, (int) this.organizer.resource(this.e).get());
        assertEquals(0xEF, (int) this.organizer.resource(this.ef).get());
        this.organizer.resource(this.abc).put(0xABC);
        assertEquals(0xABC, (int) this.organizer.resource(this.abc).get());
        assertEquals(0xABD, (int) this.organizer.resource(this.abd).get());
        assertEquals(0xE, (int) this.organizer.resource(this.e).get());
        assertEquals(0xEF, (int) this.organizer.resource(this.ef).get());
        this.organizer.resource(this.ab).put(0xAB);
        assertEquals(0xAB, (int) this.organizer.resource(this.ab).get());
        assertEquals(0xABC, (int) this.organizer.resource(this.abc).get());
        assertEquals(0xABD, (int) this.organizer.resource(this.abd).get());
        assertEquals(0xE, (int) this.organizer.resource(this.e).get());
        assertEquals(0xEF, (int) this.organizer.resource(this.ef).get());
        this.organizer.resource(this.a).put(0xA);
        assertEquals(0xA, (int) this.organizer.resource(this.a).get());
        assertEquals(0xAB, (int) this.organizer.resource(this.ab).get());
        assertEquals(0xABC, (int) this.organizer.resource(this.abc).get());
        assertEquals(0xABD, (int) this.organizer.resource(this.abd).get());
        assertEquals(0xE, (int) this.organizer.resource(this.e).get());
        assertEquals(0xEF, (int) this.organizer.resource(this.ef).get());
    }

    @Test
    public void repeatPut() {
        this.organizer.resource(this.ab).put(0xAB1);
        assertEquals(0xAB1, (int) this.organizer.resource(this.ab).get());
        this.organizer.resource(this.ab).put(0xAB2);
        assertEquals(0xAB2, (int) this.organizer.resource(this.ab).get());
        this.organizer.resource(this.ab).put(0xAB1);
        assertEquals(0xAB1, (int) this.organizer.resource(this.ab).get());
        this.organizer.resource(this.e).put(0xE2);
        assertEquals(0xE2, (int) this.organizer.resource(this.e).get());
        this.organizer.resource(this.e).put(0xE1);
        assertEquals(0xE1, (int) this.organizer.resource(this.e).get());
        this.organizer.resource(this.e).put(0xE1);
        assertEquals(0xE1, (int) this.organizer.resource(this.e).get());
        this.organizer.resource(this.e).put(0xE2);
        assertEquals(0xE2, (int) this.organizer.resource(this.e).get());
        this.organizer.resource(this.e).put(0xE1);
        assertEquals(0xE1, (int) this.organizer.resource(this.e).get());
    }

    @Test
    public void parent() {
        this.organizer.resource(this.a).put(0xA);
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.a).parent().uri());
        this.organizer.resource(this.ab).put(0xAB);
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.a).parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.ab).parent().parent().uri());
        assertEquals(this.a, this.organizer.resource(this.ab).parent().uri());
        this.organizer.resource(this.abc).put(0xABC);
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.a).parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.ab).parent().parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.abc).parent().parent().parent().uri());
        assertEquals(this.a, this.organizer.resource(this.ab).parent().uri());
        assertEquals(this.ab, this.organizer.resource(this.abc).parent().uri());
        this.organizer.resource(this.abd).put(0xABD);
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.a).parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.ab).parent().parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.abc).parent().parent().parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.abd).parent().parent().parent().uri());
        assertEquals(this.a, this.organizer.resource(this.ab).parent().uri());
        assertEquals(this.ab, this.organizer.resource(this.abc).parent().uri());
        assertEquals(this.ab, this.organizer.resource(this.abd).parent().uri());
        final URI otherBase = URI.create("http://other.base/uri/");
        this.organizer.resource(this.e).put(0xE);
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.a).parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.ab).parent().parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.abc).parent().parent().parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.abd).parent().parent().parent().uri());
        assertEquals(this.a, this.organizer.resource(this.ab).parent().uri());
        assertEquals(this.ab, this.organizer.resource(this.abc).parent().uri());
        assertEquals(this.ab, this.organizer.resource(this.abd).parent().uri());
        assertEquals(otherBase, this.organizer.resource(this.e).parent().uri());
        this.organizer.resource(this.ef).put(0xEF);
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.a).parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.ab).parent().parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.abc).parent().parent().parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.abd).parent().parent().parent().uri());
        assertEquals(this.a, this.organizer.resource(this.ab).parent().uri());
        assertEquals(this.ab, this.organizer.resource(this.abc).parent().uri());
        assertEquals(this.ab, this.organizer.resource(this.abd).parent().uri());
        assertEquals(otherBase, this.organizer.resource(this.e).parent().uri());
        assertEquals(this.e, this.organizer.resource(this.ef).parent().uri());
        assertEquals(otherBase, this.organizer.resource(this.ef).parent().parent().uri());
    }

    /**
   * Add {@code "a/b/"} before {@code "a/"}.
   */
    @Test
    public void parentOppositeOrder() {
        this.organizer.resource(this.ab).put(0xAB);
        this.organizer.resource(this.a).put(0xA);
        assertEquals(this.a, this.organizer.resource(this.ab).parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.ab).parent().parent().uri());
    }

    /**
   * Add {@code "a/b/d.txt"} before {@code "a/b/"}.
   */
    @Test
    public void parentFileOppositeOrder() {
        this.organizer.resource(this.abd).put(0xABD);
        this.organizer.resource(this.ab).put(0xAB);
        assertEquals(this.ab, this.organizer.resource(this.abd).parent().uri());
        assertEquals(this.a, this.organizer.resource(this.abd).parent().parent().uri());
        assertEquals(this.organizer.baseURI(), this.organizer.resource(this.abd).parent().parent().parent().uri());
    }

    @Test
    public void deleteParent() {
        this.organizer.resource(this.a).put(0xA);
        this.organizer.resource(this.ab).put(0xAB);
        this.organizer.resource(this.abc).put(0xABC);
        assertEquals(0xA, (int) this.organizer.resource(this.a).get());
        assertEquals(0xAB, (int) this.organizer.resource(this.ab).get());
        assertEquals(0xABC, (int) this.organizer.resource(this.abc).get());
        this.organizer.resource(this.ab).delete();
        assertNull(this.organizer.resource(this.ab).get());
        assertNull(this.organizer.resource(this.abc).get());
        assertNull(this.organizer.resource(this.abd).get());
    }

    @Test
    public void deleteFileAncestor() {
        this.organizer.resource(this.a).put(0xA);
        this.organizer.resource(this.abd).put(0xABD);
        this.organizer.resource(this.abc).put(0xABC);
        assertEquals(0xA, (int) this.organizer.resource(this.a).get());
        assertEquals(0xABC, (int) this.organizer.resource(this.abc).get());
        assertEquals(0xABD, (int) this.organizer.resource(this.abd).get());
        this.organizer.resource(this.a).delete();
        assertNull(this.organizer.resource(this.a).get());
        assertNull(this.organizer.resource(this.ab).get());
        assertNull(this.organizer.resource(this.abc).get());
        assertNull(this.organizer.resource(this.abd).get());
    }

    /**
   * Delete {@code "a/"}. Verify that {@code "a/b/"} does not exist afterwards.
   */
    @Test
    public void afterParentDelete() {
        this.organizer.resource(this.a).put(0xA);
        this.organizer.resource(this.ab).put(0xAB);
        assertEquals(0xAB, (int) this.organizer.resource(this.ab).get());
        this.organizer.resource(this.a).delete();
        assertNull(this.organizer.resource(this.ab).get());
        assertNull(this.organizer.resource(this.ab).get());
    }

    /**
   * Delete {@code "a/"} and then create it. Verify that {@code "a/b/"} does not
   * exist after the deletion and re-creation operation.
   */
    @Test
    public void afterParentRecreateWithB() {
        this.organizer.resource(this.a).put(0xA1);
        this.organizer.resource(this.ab).put(0xAB1);
        assertEquals(0xAB1, (int) this.organizer.resource(this.ab).get());
        this.organizer.resource(this.a).delete();
        assertNull(this.organizer.resource(this.ab).get());
        this.organizer.resource(this.a).put(0xA2);
        assertEquals(0xA2, (int) this.organizer.resource(this.a).get());
        assertNull(this.organizer.resource(this.ab).get());
        this.organizer.resource(this.ab).put(0xAB2);
        assertEquals(0xAB2, (int) this.organizer.resource(this.ab).get());
    }

    /**
   * Delete {@code "a/b/"} after adding {@code "a/b/d.txt"}. Verify that the
   * removal of {@code "a/b/"} deletes also {@code "a/b/d.txt"}.
   */
    @Test
    public void afterParentRecreateWithD() {
        this.organizer.resource(this.ab).put(0xAB1);
        this.organizer.resource(this.abd).put(0xABD1);
        assertEquals(0xABD1, (int) this.organizer.resource(this.abd).get());
        this.organizer.resource(this.ab).delete();
        assertNull(this.organizer.resource(this.abd).get());
        this.organizer.resource(this.ab).put(0xAB2);
        assertEquals(0xAB2, (int) this.organizer.resource(this.ab).get());
        assertNull(this.organizer.resource(this.abd).get());
        this.organizer.resource(this.abd).put(0xABD2);
        assertEquals(0xABD2, (int) this.organizer.resource(this.abd).get());
    }

    /**
   * Create {@code "a/"} and delete it. Verify that {@code "a/b/c"} is also
   * deleted.
   */
    @Test
    public void afterAncestorDelete() {
        this.organizer.resource(this.a).put(0xA1);
        this.organizer.resource(this.abc).put(0xABC1);
        assertEquals(0xA1, (int) this.organizer.resource(this.a).get());
        assertEquals(0xABC1, (int) this.organizer.resource(this.abc).get());
        this.organizer.resource(this.a).delete();
        assertNull(this.organizer.resource(this.abc).get());
        this.organizer.resource(this.abc).put(0xABC2);
        assertEquals(0xABC2, (int) this.organizer.resource(this.abc).get());
    }

    /**
   * Create {@code "a/"} and delete it. Verify that {@code "a/b/c"} is also
   * deleted. In order to strengthen the test case, we also verify the behavior
   * when deleting the direct parent {@code "a/b/"}.
   */
    @Test
    public void afterAncestorDeleteAndParentRecreate() {
        this.organizer.resource(this.a).put(0xA1);
        this.organizer.resource(this.abc).put(0xABC1);
        assertEquals(0xA1, (int) this.organizer.resource(this.a).get());
        assertEquals(0xABC1, (int) this.organizer.resource(this.abc).get());
        this.organizer.resource(this.a).delete();
        assertNull(this.organizer.resource(this.abc).get());
        this.organizer.resource(this.ab).delete();
        assertNull(this.organizer.resource(this.abc).get());
        this.organizer.resource(this.abc).put(0xABC2);
        assertEquals(0xABC2, (int) this.organizer.resource(this.abc).get());
    }

    @Test
    public void afterAncestorDeleteAndRepeatedParentRecreate() {
        this.organizer.resource(this.a).put(0xA1);
        this.organizer.resource(this.ab).put(0xAB1);
        this.organizer.resource(this.abc).put(0xABC1);
        assertEquals(0xA1, (int) this.organizer.resource(this.a).get());
        assertEquals(0xAB1, (int) this.organizer.resource(this.ab).get());
        assertEquals(0xABC1, (int) this.organizer.resource(this.abc).get());
        this.organizer.resource(this.a).delete();
        assertNull(this.organizer.resource(this.abc).get());
        this.organizer.resource(this.ab).delete();
        this.organizer.resource(this.ab).put(0xAB2);
        assertEquals(0xAB2, (int) this.organizer.resource(this.ab).get());
        this.organizer.resource(this.ab).delete();
        this.organizer.resource(this.abc).put(0xABC2);
        assertEquals(0xABC2, (int) this.organizer.resource(this.abc).get());
    }

    @Test
    public void afterRepeatedAncestorRecreate() {
        this.organizer.resource(this.a).put(0xA1);
        this.organizer.resource(this.ab).put(0xAB1);
        this.organizer.resource(this.abc).put(0xABC1);
        assertEquals(0xA1, (int) this.organizer.resource(this.a).get());
        assertEquals(0xAB1, (int) this.organizer.resource(this.ab).get());
        assertEquals(0xABC1, (int) this.organizer.resource(this.abc).get());
        this.organizer.resource(this.a).delete();
        assertNull(this.organizer.resource(this.abc).get());
        this.organizer.resource(this.a).delete();
        this.organizer.resource(this.a).put(0xA2);
        assertEquals(0xA2, (int) this.organizer.resource(this.a).get());
        this.organizer.resource(this.a).delete();
        this.organizer.resource(this.abc).put(0xABC2);
        assertEquals(0xABC2, (int) this.organizer.resource(this.abc).get());
    }

    @Test
    public void deletedUris() {
        this.organizer.resource(this.a).put(0xA1);
        this.organizer.resource(this.ab).put(0xAB1);
        this.organizer.resource(this.abc).put(0xABC1);
        final Set<URI> deletedUris = this.organizer.resource(this.ab).delete();
        assertEquals(2, deletedUris.size());
        assertThat(deletedUris, hasItem(this.ab));
        assertThat(deletedUris, hasItem(this.abc));
    }

    @Test
    public void overrideRepresentation() {
        this.organizer.resource(this.a).put(0xA1);
        this.organizer.resource(this.ab).put(0xAB1);
        this.organizer.resource(this.abc).put(0xABC1);
        assertEquals(0xAB1, (int) this.organizer.resource(this.ab).put(0xAB2));
    }
}
