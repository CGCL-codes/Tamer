package net.turambar.just;

/** A link in the source processing chain which passes further the processed source.
 *  It is implemented by each processor class which is not last in the processing chain.
 *  Implementation classes are either {@link SourceProcessor SourceProcessor} or {@link SourceReader SourceReader}
 *  instances.
 *  All links have a next {@link SourceProcessor SourceProcessor} (output), to which they
 *  pass the generated code. If not stated differently in implementator's documentation, it assumes that
 *  its output is not null and will throw <code>NullPointerException</code> from any method otherwise.
 *  When exactly all methods defined by <code>SourceProcessor</code> will be called depends on the sublass.
 *
 *  @see SourceProcessor
 *  @see SourceReader
 *  @author Marcin Moscicki
 */
public interface SourceInput {

    /** Returns the <code>SourceProcessor</code> to which this link passes the processed source. */
    public SourceProcessor getOutput();

    /** Sets the <code>SourceProcessor</code> to which this instance will pass information about the processed source. */
    public void setOutput(SourceProcessor outlet);
}
