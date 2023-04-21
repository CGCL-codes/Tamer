package org.antlride.core.build;

import org.antlride.bridge.core.BuildMessage;
import org.antlride.core.settings.ConfigurableOptions;
import org.eclipse.core.runtime.CoreException;

/**
 * Manipulate the {@link BuildMessage message} reported by ANTLR.
 * 
 * @author Edgar Espina
 * @since 2.1.0
 */
public interface BuildMessageHandler extends ConsoleAware {

    /**
   * The initialization call.
   */
    void begin(ConfigurableOptions settings) throws CoreException;

    /**
   * Handle the build message.
   * 
   * @param settings The preferences.
   * @param message The build message to process.
   */
    void handle(ConfigurableOptions settings, BuildMessage message) throws CoreException;

    /**
   * The end call.
   */
    void end(ConfigurableOptions settings) throws CoreException;
}
