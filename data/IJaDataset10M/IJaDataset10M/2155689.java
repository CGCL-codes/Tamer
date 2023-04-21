package org.proclos.etlcore.transformer;

import org.proclos.etlcore.component.IComponent;
import org.proclos.etlcore.config.transformer.TransformerConfigurator;
import org.proclos.etlcore.node.IColumn;

/**
 * 
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public interface ITransformer extends IColumn, IComponent {

    public void setInput(IColumn input);

    public void clearInput();

    public TransformerConfigurator getConfigurator();
}
