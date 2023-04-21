package com.icteam.fiji.command.prcs;

import com.icteam.fiji.command.CommandResult;
import com.icteam.fiji.command.DefaultCommand;
import com.icteam.fiji.command.CommandContext;
import com.icteam.fiji.command.CommandException;
import com.icteam.fiji.model.EntPrcs;
import java.io.Serializable;

public abstract class NewEntPrcsCmd<T extends EntPrcs> extends DefaultCommand implements Serializable {

    private T m_entPrcs = null;

    public void setEntPrcs(T p_entPrcs) {
        m_entPrcs = p_entPrcs;
    }

    public T getEntPrcs() {
        return m_entPrcs;
    }
}
