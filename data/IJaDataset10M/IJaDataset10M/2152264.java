package org.monet.backoffice.presentation.user.views;

import java.io.Writer;
import org.monet.backoffice.presentation.user.agents.AgentRender;
import org.monet.backoffice.presentation.user.util.Context;
import org.monet.kernel.configuration.Configuration;
import org.monet.kernel.model.Page;

public class ViewHelper extends View {

    public ViewHelper(Context oContext, AgentRender oAgentRender, String codeLanguage) {
        super(oContext, oAgentRender, codeLanguage);
    }

    @Override
    public void execute(Writer writer) {
        Page page;
        String helpDirname = Configuration.getInstance().getBusinessModelHelpDirname();
        if (this.target == null) return;
        super.execute(writer);
        page = (Page) this.target;
        if (page.getFilename().equals("")) return;
        this.agentTemplates.mergeModelTemplate(helpDirname + "/" + page.getFilename(), this.context, writer);
    }
}
