package com.wikiup.romulan.servlet.context;

import com.wikiup.core.imp.mc.ObjectModelContainer;
import com.wikiup.core.inf.GetterInf;
import com.wikiup.core.inf.ModelContainerInf;
import com.wikiup.romulan.gom.Player;
import com.wikiup.romulan.util.GameUtil;
import com.wikiup.servlet.ServletProcessorContext;
import com.wikiup.servlet.inf.ProcessorContextInf;
import com.wikiup.servlet.inf.ServletProcessorContextAware;
import com.wikiup.util.StringUtil;

public class PlayerProcessorContext implements ProcessorContextInf, ServletProcessorContextAware {

    private Player player;

    public Object get(String name) {
        return (StringUtil.isEmpty(name) || name.equals("this")) ? player : player.get(name);
    }

    public ModelContainerInf getModelContainer(String name, GetterInf<?> params) {
        return new ObjectModelContainer(get(name));
    }

    public void setServletProcessorContext(ServletProcessorContext context) {
        player = GameUtil.getCurrentPlayer(context);
    }
}
