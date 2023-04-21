package com.ibm.tuningfork.core.graphics;

import com.ibm.tuningfork.core.figure.CategoryDescriptor;
import com.ibm.tuningfork.infra.event.IEvent;
import com.ibm.tuningfork.infra.stream.core.Stream;
import com.ibm.tuningfork.infra.streambundle.StreamBundle;

/**
 * An ICategoryMapper determines how events are mapped into CategoryDescriptors which control how
 * they are drawn by painters and displayed in the legend.  Most commonly, these mappers are used to control how events
 * are mapped into colors by the painters.  For a simple common example see StreamToColorMapper, which
 * assigns a color to each stream, and ignores the specific contents of the events.
 *
 * The CategoryDescriptors are also typically used by figures to generate the legend, so the text of the
 * descriptor should be set appropriately.
 */
public interface ICategoryMapper {

    /**
     * Get a printable name for this mapper, that could be used in a GUI for selecting from multiple different ways
     * of displaying the data.
     *
     * @return Name of this mapper
     */
    String getName();

    /**
     * Map an event and the stream it comes from into a category descriptor.
     *
     * @param event An event
     * @param source The stream it came from
     * @return A category descriptor that determines how the event/stream will be displayed.
     */
    CategoryDescriptor mapToCategory(IEvent event, Stream source);

    /**
     * Map an event and the stream it comes from into a color.
     *
     * @param event An event
     * @param source The stream it came from
     * @return The color in which to paint the event/stream
     */
    RGBColor mapToColor(IEvent event, Stream source);

    /**
     * Change a mapping.
     *
     * @param categoryName The name of the category to change
     * @param descriptor The new descriptor to provide for that category
     */
    void changeMapping(String categoryName, CategoryDescriptor descriptor);

    /**
     * Get a count of the number of categories.
     *
     * @return Number of categories
     */
    int getCategoryCount();

    /**
     * Get an array containing the category descriptors.
     *
     * @return The category descriptors of this mapper
     */
    CategoryDescriptor[] getCategoryDescriptors();

    /**
     * Invoked when streams are added to the figure associated with this mapper, giving it
     * an opportunity to adjust its mappings.
     *
     * @param streams Streams added to the figure being diplayed
     */
    void addStreams(StreamBundle streams);

    /**
     * Invoked when a single stream is added to the figure associated with this mapper, giving it
     * an opportunity to adjust its mappings.
     *
     * @param stream The stream added to the figure
     */
    void addStream(Stream stream);

    /**
     * Clear the mapping
     */
    void clear();
}
