package com.icteam.fiji.command.bsns;

import com.icteam.fiji.command.DefaultCommand;
import com.icteam.fiji.command.CommandContext;
import com.icteam.fiji.command.CommandResult;
import com.icteam.fiji.command.CommandException;
import com.icteam.fiji.model.EntBsns;
import java.io.Serializable;

public abstract class SaveEntBsnsCmd<T extends EntBsns> extends DefaultCommand implements Serializable {

    private T m_entBsns = null;

    public void setEntBsns(T p_entBsns) {
        m_entBsns = p_entBsns;
    }

    public T getEntBsns() {
        return m_entBsns;
    }
}
