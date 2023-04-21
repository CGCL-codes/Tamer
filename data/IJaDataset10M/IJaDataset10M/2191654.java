package org.openfast.template.loader;

import java.io.InputStream;
import org.openfast.template.MessageTemplate;
import org.openfast.template.TemplateRegistry;

public interface MessageTemplateLoader {

    MessageTemplate[] load(InputStream source);

    void setTemplateRegistry(TemplateRegistry templateRegistry);

    TemplateRegistry getTemplateRegistry();
}
