package org.fest.assertions;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static org.fest.assertions.Fail.*;
import static org.fest.assertions.Formatting.inBrackets;
import static org.fest.util.Objects.areEqual;
import static org.fest.util.Strings.*;

/**
 * Understands assertion methods for images. To create a new instance of this class use the
 * method <code>{@link Assertions#assertThat(BufferedImage)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public final class ImageAssert extends GenericAssert<BufferedImage> {

    private static ImageReader imageReader = new ImageReader();

    /**
   * Reads the image in the specified path.
   * @param imageFilePath the path of the image to read.
   * @return the read image.
   * @throws IllegalArgumentException if the given path does not belong to a file.
   * @throws IOException wrapping any I/O errors thrown when reading the image.
   */
    public static BufferedImage read(String imageFilePath) throws IOException {
        File imageFile = new File(imageFilePath);
        if (!imageFile.isFile()) throw new IllegalArgumentException(concat("The path ", quote(imageFilePath), " does not belong to a file"));
        try {
            return imageReader.read(imageFile);
        } catch (IOException e) {
            throw new IOException(concat("Unable to read image from file ", quote(imageFilePath)), e);
        }
    }

    ImageAssert(BufferedImage actual) {
        super(actual);
    }

    /**
   * Sets the description of the actual value, to be used in as message of any <code>{@link AssertionError}</code>
   * thrown when an assertion fails. This method should be called before any assertion method, otherwise any assertion
   * failure will not show the provided description.
   * <p>
   * For example:
   * <pre>
   * assertThat(picture).<strong>as</strong>(&quot;Vacation Picture&quot;).hasSize(new Dimension(800, 600));
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public ImageAssert as(String description) {
        description(description);
        return this;
    }

    /**
   * Alternative to <code>{@link #as(String)}</code>, since "as" is a keyword in
   * <a href="http://groovy.codehaus.org/" target="_blank">Groovy</a>. This method should be called before any assertion
   * method, otherwise any assertion failure will not show the provided description.
   * <p>
   * For example:
   * <pre>
   * assertThat(picture).<strong>describedAs</strong>(&quot;Vacation Picture&quot;).hasSize(new Dimension(800, 600));
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public ImageAssert describedAs(String description) {
        return as(description);
    }

    /**
   * Sets the description of the actual value, to be used in as message of any <code>{@link AssertionError}</code>
   * thrown when an assertion fails. This method should be called before any assertion method, otherwise any assertion
   * failure will not show the provided description.
   * <p>
   * For example:
   * <pre>
   * assertThat(picture).<strong>as</strong>(new BasicDescription(&quot;Vacation Picture&quot;)).hasSize(new Dimension(800, 600));
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public ImageAssert as(Description description) {
        description(description);
        return this;
    }

    /**
   * Alternative to <code>{@link #as(Description)}</code>, since "as" is a keyword in
   * <a href="http://groovy.codehaus.org/" target="_blank">Groovy</a>. This method should be called before any assertion
   * method, otherwise any assertion failure will not show the provided description.
   * <p>
   * For example:
   * <pre>
   * assertThat(picture).<strong>describedAs</strong>(new BasicDescription(&quot;Vacation Picture&quot;)).hasSize(new Dimension(800, 600));
   * </pre>
   * </p>
   * @param description the description of the actual value.
   * @return this assertion object.
   */
    public ImageAssert describedAs(Description description) {
        return as(description);
    }

    /**
   * Verifies that the actual image satisfies the given condition.
   * @param condition the given condition.
   * @return this assertion object.
   * @throws AssertionError if the actual image does not satisfy the given condition.
   * @throws IllegalArgumentException if the given condition is null.
   */
    public ImageAssert satisfies(Condition<BufferedImage> condition) {
        assertSatisfies(condition);
        return this;
    }

    /**
   * Verifies that the actual image does not satisfy the given condition.
   * @param condition the given condition.
   * @return this assertion object.
   * @throws AssertionError if the actual image satisfies the given condition.
   * @throws IllegalArgumentException if the given condition is null.
   */
    public ImageAssert doesNotSatisfy(Condition<BufferedImage> condition) {
        assertDoesNotSatisfy(condition);
        return this;
    }

    /**
   * Verifies that the actual image is equal to the given one. Two images are equal if they have the same size and the
   * pixels at the same coordinates have the same color.
   * @param expected the given image to compare the actual image to.
   * @return this assertion object.
   * @throws AssertionError if the actual image is not equal to the given one.
   */
    public ImageAssert isEqualTo(BufferedImage expected) {
        if (areEqual(actual, expected)) return this;
        failIfNull(expected);
        failIfNotEqual(sizeOf(actual), sizeOf(expected));
        if (!hasEqualColor(expected)) fail("images do not have the same color(s)");
        return this;
    }

    private void failIfNull(BufferedImage expected) {
        if (expected != null) return;
        fail(errorMessageIfNotEqual(actual, null));
    }

    private void failIfNotEqual(Dimension actual, Dimension expected) {
        if (!areEqual(actual, expected)) fail(concat("image size, expected:", inBrackets(expected), " but was:", inBrackets(actual)));
    }

    /**
   * Verifies that the actual image is not equal to the given one. Two images are equal if they have the same size and
   * the pixels at the same coordinates have the same color.
   * @param image the given image to compare the actual image to.
   * @return this assertion object.
   * @throws AssertionError if the actual image is equal to the given one.
   */
    public ImageAssert isNotEqualTo(BufferedImage image) {
        if (areEqual(actual, image)) fail(errorMessageIfEqual(actual, image));
        if (image == null) return this;
        if (areEqual(sizeOf(actual), sizeOf(image)) && hasEqualColor(image)) fail("images are equal");
        return this;
    }

    private static Dimension sizeOf(BufferedImage image) {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    private boolean hasEqualColor(BufferedImage expected) {
        int w = actual.getWidth();
        int h = actual.getHeight();
        for (int x = 0; x < w; x++) for (int y = 0; y < h; y++) if (actual.getRGB(x, y) != expected.getRGB(x, y)) return false;
        return true;
    }

    /**
   * Verifies that the actual image is not <code>null</code>.
   * @return this assertion object.
   * @throws AssertionError if the actual image is <code>null</code>.
   */
    public ImageAssert isNotNull() {
        assertNotNull();
        return this;
    }

    /**
   * Verifies that the actual image is not the same as the given one.
   * @param expected the given image to compare the actual image to.
   * @return this assertion object.
   * @throws AssertionError if the actual image is the same as the given one.
   */
    public ImageAssert isNotSameAs(BufferedImage expected) {
        assertNotSameAs(expected);
        return this;
    }

    /**
   * Verifies that the actual image is the same as the given one.
   * @param expected the given image to compare the actual image to.
   * @return this assertion object.
   * @throws AssertionError if the actual image is not the same as the given one.
   */
    public ImageAssert isSameAs(BufferedImage expected) {
        assertSameAs(expected);
        return this;
    }

    /**
   * Verifies that the size of the actual image is equal to the given one.
   * @param expected the expected size of the actual image.
   * @return this assertion object.
   * @throws AssertionError if the actual image is <code>null</code>.
   * @throws AssertionError if the size of the actual image is not equal to the given one.
   */
    public ImageAssert hasSize(Dimension expected) {
        isNotNull();
        if (expected == null) throw new IllegalArgumentException("The size to compare to should not be null");
        Dimension actual = new Dimension(this.actual.getWidth(), this.actual.getHeight());
        Fail.failIfNotEqual(description(), actual, expected);
        return this;
    }

    static void imageReader(ImageReader newImageReader) {
        imageReader = newImageReader;
    }
}
