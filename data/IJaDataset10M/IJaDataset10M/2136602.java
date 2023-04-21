package net.sourceforge.jruntimedesigner.provider;

import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;

/**
 * Used to abstract the way how the layout data is provided.
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 10719 $ $Date: 2007-08-17 14:32:53 +0200 (Fr, 17 Aug 2007) $
 * @since 1.0
 */
public interface ILayoutDataProvider {

    /**
   * Loads the layout into the runtime designer. A concrete implementation
   * should choose the strategy how to do it.
   * 
   * @param controller
   */
    public void load(JRuntimeDesignerController controller) throws LayoutDataProviderException;

    /**
   * Saves the current layout. A concrete implementation should choose the
   * strategy how to do it.
   * 
   * @param controller
   */
    public void save(JRuntimeDesignerController controller) throws LayoutDataProviderException;
}
