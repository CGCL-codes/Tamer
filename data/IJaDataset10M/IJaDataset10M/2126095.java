package org.larozanam.arq.pages.enferm.medico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.larozanam.admin.beans.Permissao;
import org.larozanam.enferm.beans.Medico;
import org.larozanam.admin.util.PermissaoSelectModel;
import org.larozanam.arq.acesso.GerenciadorFluxo;

public class AlterarMedico {

    @Property
    @Persist
    private Medico medico;

    @SessionState
    private GerenciadorFluxo gerenciadorFluxo;

    @Property
    private String consulta;
}
