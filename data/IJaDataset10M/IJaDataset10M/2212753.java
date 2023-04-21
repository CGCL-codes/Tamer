package de.lema.client.editor.configuration;

import de.lema.bo.Configuration;
import de.lema.client.editor.template.LemaBoEditorInput;
import de.lema.client.server.service.ServerConfig;

public class ConfigurationEditorInput extends LemaBoEditorInput<Configuration> {

    private final String id;

    public ConfigurationEditorInput(final Configuration bo, final ServerConfig serverConfig, final boolean isEdit, final boolean exists) {
        super(bo, serverConfig, isEdit, exists);
        id = bo.getKey();
    }

    @Override
    public Object getIdForEquals() {
        return id;
    }
}
