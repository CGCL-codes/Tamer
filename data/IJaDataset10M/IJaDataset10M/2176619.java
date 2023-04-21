package net.sf.jelly.apt.strategies;

import net.sf.jelly.apt.TemplateBlock;
import net.sf.jelly.apt.TemplateException;
import net.sf.jelly.apt.TemplateModel;
import net.sf.jelly.apt.TemplateOutput;
import java.io.IOException;

/**
 * Writes a value to template output.
 *
 * @author Ryan Heaton
 */
public abstract class TemplateValueStrategy<B extends TemplateBlock> extends TemplateStrategyControl<B> {

    /**
   * Get the value to write to the template output.
   *
   * @return The value to write to the template output.
   */
    protected abstract String getValue() throws TemplateException;

    /**
   * Writes the {@link #getValue() value} to the output.
   *
   * @param block  The block.
   * @param output The output.
   * @param model  The model.
   * @return false, as no body should be processed.
   */
    public boolean preProcess(B block, TemplateOutput<B> output, TemplateModel model) throws IOException, TemplateException {
        String value = getValue();
        if (value != null) {
            output.write(value);
        }
        return false;
    }

    /**
   * @throws UnsupportedOperationException Because a value strategy shouldn't have a body.
   */
    public boolean processBody(B block, TemplateOutput<B> output, TemplateModel model) throws IOException, TemplateException {
        throw new UnsupportedOperationException("A value strategy shouldn't have a body.");
    }

    /**
   * Do nothing.
   */
    public void postProcess(B block, TemplateOutput<B> output, TemplateModel model) throws IOException, TemplateException {
    }
}
