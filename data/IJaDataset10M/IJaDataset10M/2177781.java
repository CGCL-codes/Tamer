package com.calfater.mailcarbon.services.guiconfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.calfater.mailcarbon.models.guiconfig.GuiConfig;

/**
 * The Interface IGuiConfigService.
 */
public interface IGuiConfigService {

    public abstract void save(GuiConfig config, File file) throws IOException;

    public abstract GuiConfig load(File file) throws FileNotFoundException;
}
