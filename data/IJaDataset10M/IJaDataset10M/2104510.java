package com.neurogrid.commands;

import java.io.Serializable;
import org.prevayler.Command;
import org.prevayler.PrevalentSystem;
import com.neurogrid.om.Peer;
import com.neurogrid.om.StringTriple;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * CreateUriTripleCommand - command for creating UriTriples<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   12/June/2003   sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */
public class CreateUriTripleCommand implements Command {

    private final StringTriple o_string_triple;

    public CreateUriTripleCommand(StringTriple p_string_triple) {
        this.o_string_triple = p_string_triple;
    }

    public Serializable execute(PrevalentSystem p_system) throws Exception {
        ((Peer) p_system).o_uri_triple_peer.storeUriTriple(o_string_triple);
        return null;
    }
}
