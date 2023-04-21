package com.businesslayerbuilder.web.front.security.session;

import com.businesslayerbuilder.web.front.security.BusinessLayerUser;

/**
 * Classe <BR>
 * Criada em 12/09/2007. <BR>
 * @author Rafael Bernardo [BuscaP�]
 */
public class SessionUser extends BusinessLayerUser {

    /** sessionId: Identificador de sess�o. */
    private String sessionId = "";

    /**
     * @param user nome de usu�rio
     * @param password senha de acesso
     * @param session_id identificador da sess�o
     */
    public SessionUser(String user, String password, String session_id) {
        super(user, password);
        setSessionId(session_id);
    }

    /**
     * O m�todo getSessionId obtem o valor de sessionId.
     * @return o valor de sessionId.
     * @see SessionUser
     */
    protected String getSessionId() {
        return this.sessionId;
    }

    /**
     * Define o valor de sessionId
     * @param sessionId O novo valor de sessionId .
     */
    protected void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    protected boolean equals(BusinessLayerUser user) {
        if (user instanceof SessionUser) {
            return this.equals(SessionUser.class.cast(user));
        }
        return false;
    }

    /**
     * Compara dois objetos do tipo {@link SessionUser}
     * @param user usu�rio a ser comparado com este.
     * @return true se os usu�rios tiverem o mesmo id de sess�o
     */
    protected boolean equals(SessionUser user) {
        return ((user != null) && (this.getUser().equals(user.getUser())) && (this.getPassword().equals(user.getPassword())));
    }
}
