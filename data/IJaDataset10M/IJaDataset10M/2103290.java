package org.freedom.acao;

import java.util.EventListener;

public interface TabelaEditListener extends EventListener {

    public void valorAlterado(TabelaEditEvent evt);
}
