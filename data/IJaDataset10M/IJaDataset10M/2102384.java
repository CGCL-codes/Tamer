package com.webmotix.facade;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.jcr.Session;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.xa.XAResource;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.webmotix.core.MotixSessionContext;
import com.webmotix.util.ControllerUtil;

/**
 * Essa classe representa a transferencia de dados entre o cliente e o servidor do Motix.
 * Ela condensa todos os objetos que transferem dados, inclusive um documento XML com o request da aplicacao.
 * @author Ronaldo Florence
 *
 */
public class MotixParameter {

    private Session session;

    private HttpSession httpSession;

    private XAResource xAResource;

    private Map<String, Object> activeFields;

    private Map<String, Object> inactiveFields;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private MotixSessionContext sessionContext;

    private ServletContext servletContext;

    /**
	 * UUID virtual, usado para o controle de inser��o
	 */
    private String virtualUUID = null;

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public MotixParameter() {
        activeFields = new HashMap<String, Object>();
        inactiveFields = new HashMap<String, Object>();
    }

    /**
	 * Retorna um Map contendo os campos ativos.
	 * @return Map
	 */
    public Map<String, Object> getActiveFields() {
        return activeFields;
    }

    /**
	 * Atualiza o Map de campos ativos. <i>(Utilizado por controlers)</i>
	 * @param activeFields
	 */
    public void setActiveFields(final Map<String, Object> activeFields) {
        this.activeFields = activeFields;
    }

    /**
	 * Retorna um Map com os campos inativos. 
	 * @return Map Campos inativos.
	 */
    public Map<String, Object> getInactiveFields() {
        return inactiveFields;
    }

    /**
	 * Atualiza o Map dos campos inativos. 
	 * @param inactiveFields Map que ir� substituir o atual Map de campos inativos.
	 */
    public void setInactiveFields(final Map<String, Object> inactiveFields) {
        this.inactiveFields = inactiveFields;
    }

    /**
	 * Retorna o request feito para o controler.
	 * @return HttpServletRequest gerado pelo request do controler.
	 */
    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(final HttpServletRequest request) {
        this.request = request;
        this.httpSession = request.getSession();
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(final HttpServletResponse response) {
        this.response = response;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(final Session session) {
        this.session = session;
    }

    /**
	 * @return the xAResource
	 */
    public XAResource getXAResource() {
        return xAResource;
    }

    public MotixSessionContext getSessionContext() {
        return sessionContext;
    }

    public void setSessionContext(final MotixSessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    /**
	 * @param resource the xAResource to set
	 */
    public void setXAResource(final XAResource resource) {
        xAResource = resource;
    }

    /**
	 * Retorna o valor do campo, cujo nome � passado por parametro, do Mapa de campos ativos.<br/>
	 * Caso o nome de um campo inativo seja passado, o m�todo retornar� o valor "".
	 * @param name Nome do campo a retornar o valor.
	 * @return Valor do campo no mapa de campos ativos.
	 */
    public String getValue(final String name) {
        return getValue(name, true);
    }

    /**
	 * Retorna o valor do MODULO passado para o request.
	 * @return
	 */
    public String getMotixModule() {
        return getValue(ControllerUtil.MOTIXMODULE, false);
    }

    /**
	 * Retorna o valor do UUID passado para o request.
	 * @return
	 */
    public String getMotixUUID() {
        return getValue(ControllerUtil.MOTIXUUID, false);
    }

    /**
	 * Retorna o NodeType passado para o request.
	 * @return
	 */
    public String getMotixType() {
        return getValue(ControllerUtil.MOTIXTYPE, false);
    }

    /**
	 * Retorna o Path passado para o request.
	 * @return
	 */
    public String getMotixPath() {
        return getValue(ControllerUtil.MOTIXPATH, false);
    }

    /**
	 * Retorna a url destino passado para o request.
	 * @return
	 */
    public String getMotixDestiny() {
        return getValue(ControllerUtil.MOTIXDESTINY, false);
    }

    /**
	 * Retorna o Name do Node passado para o request.
	 * @return
	 */
    public String getMotixName() {
        return getValue(ControllerUtil.MOTIXNAME, false);
    }

    /**
	 * Retorna o action/query passado para o request.
	 * @return
	 */
    public String getMotixAction() {
        return getValue(ControllerUtil.MOTIXACTION, false);
    }

    /**
	 * Retorna o UUID virtual
	 * @return
	 */
    public String getVirtualUUID() {
        return virtualUUID;
    }

    /**
	 * Define o UUID virtual
	 * @param virtualUUID
	 */
    public void setVirtualUUID(String virtualUUID) {
        this.virtualUUID = virtualUUID;
    }

    /**
	 * Redefine o destino da pagina.
	 * @param newDestiny
	 */
    public void setMotixDestiny(final String newDestiny) {
        setValue(ControllerUtil.MOTIXDESTINY, newDestiny, false);
    }

    /**
	 * Atualiza o valor do campo passado por parametro, com o valor da string passada.
	 * @param name Nome do campo a atualizar
	 * @param value Valor a ser atualizado
	 * @param active Deve atualizar um campo ativo ou inativo: <code>true</code> ativo | <code>false</code> inativo
	 */
    public void setValue(final String name, final String value, final boolean active) {
        if (active) {
            if (this.activeFields.containsKey(name)) {
                this.activeFields.remove(name);
            }
            this.activeFields.put(name, value);
        } else {
            if (this.inactiveFields.containsKey(name)) {
                this.inactiveFields.remove(name);
            }
            this.inactiveFields.put(name, value);
        }
    }

    /**
	 * Atualiza o valor de um ativo passado por parametro, com o valor da string passada.
	 * @param name Nome do campo a atualizar
	 * @param value Valor a ser atualizado
	 * @param active Deve atualizar um campo ativo ou inativo: <code>true</code> ativo | <code>false</code> inativo
	 */
    public void setValue(final String name, final String value) {
        this.setValue(name, value, true);
    }

    /**
	 * Define um valor padr�o para o campo caso n�o seja passado pelo formul�rio.
	 * @param name nome do campo
	 * @param value valor padr�o que ser� atribuido caso n�o exista.
	 */
    public void setDefault(final String name, final String value) {
        if (!this.contains(name)) {
            this.setValue(name, value, true);
        }
    }

    /**
	 * Retorna um array de strings contendo os valores do campo passado como parametro.
	 * @see javax.servlet.http.HttpServletRequest
	 * @param name Nome do campo a retornar o valor.
	 * @return Valor do campo no mapa de campos ativos.
	 */
    public String[] getValues(final String name) {
        return getValues(name, true);
    }

    /**
	 * Retorna o valor do campo, cujo nome � passado por parametro, do Mapa de campos ativos ou inativos.<br/>
	 * Caso o nome de um campo inativo seja passado, o m�todo retornar� o valor "".
	 * @param name Nome do campo a retornar o valor.
	 * @param activeField True busca o parametro no mapa de campos ativos, False faz com que o m�todo </br>
	 * busque o parametro no mapa de campos inativos.
	 * @return Valor do campo no mapa de campos escolhido.
	 */
    public String getValue(final String name, final boolean activeField) {
        if (activeField) {
            if (activeFields.containsKey(name)) {
                return activeFields.get(name).toString();
            } else return StringUtils.EMPTY;
        } else {
            if (inactiveFields.containsKey(name)) {
                return inactiveFields.get(name).toString();
            } else return StringUtils.EMPTY;
        }
    }

    /**
	 * Retorna um array de valores de um campo.
	 * Mesmo tratamento que #getValue(String, boolean)
	 * @see #getValue(String, boolean)
	 * @param name
	 * @param activeField
	 * @return
	 */
    public String[] getValues(final String name, final boolean activeField) {
        if (activeField) {
            if (activeFields.containsKey(name)) {
                final Object values = activeFields.get(name);
                if (values instanceof String) {
                    return new String[] { (String) values };
                } else {
                    return (String[]) values;
                }
            } else return new String[0];
        } else {
            if (inactiveFields.containsKey(name)) {
                final Object values = inactiveFields.get(name);
                if (values instanceof String) {
                    return new String[] { (String) values };
                } else {
                    return (String[]) values;
                }
            } else return new String[0];
        }
    }

    public boolean contains(final String key) {
        return activeFields.keySet().contains(key) || inactiveFields.keySet().contains(key);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("\n**********************************************************\n");
        sb.append("Facade: " + this.getValue(ControllerUtil.MOTIXFACADE, false) + "\n");
        sb.append("Workspace: " + this.getSession().getWorkspace().getName() + "\n");
        sb.append("Session: " + this.getSession() + "\n");
        sb.append("Request Map: " + this.getRequest().getParameterMap() + "\n");
        sb.append("ACTIVE FIELDS:\n");
        final Iterator actives = activeFields.keySet().iterator();
        while (actives.hasNext()) {
            final String key = (String) actives.next();
            final Object values = activeFields.get(key);
            sb.append("\t");
            sb.append(key);
            sb.append(" = ");
            if (values instanceof DiskFileItem) {
                sb.append(ToStringBuilder.reflectionToString(values, ToStringStyle.MULTI_LINE_STYLE));
            } else {
                if (values instanceof String) {
                    sb.append("[ ");
                    sb.append((String) values);
                    sb.append(" ]");
                } else {
                    sb.append("[ ");
                    final String[] aValues = (String[]) values;
                    final int l = aValues.length;
                    for (int i = 0; i < aValues.length; i++) {
                        sb.append(aValues[i]);
                        if (i < (l - 1)) {
                            sb.append(", ");
                        }
                    }
                    sb.append(" ]");
                }
            }
            sb.append("\n");
        }
        sb.append("INACTIVE FIELDS:\n");
        final Iterator inactives = inactiveFields.keySet().iterator();
        while (inactives.hasNext()) {
            final String key = (String) inactives.next();
            final Object values = inactiveFields.get(key);
            sb.append("\t");
            sb.append(key);
            sb.append(" = ");
            if (values instanceof String) {
                sb.append("[ ");
                sb.append((String) values);
                sb.append(" ]");
            } else {
                sb.append("[ ");
                final String[] aValues = (String[]) values;
                final int l = aValues.length;
                for (int i = 0; i < aValues.length; i++) {
                    sb.append(aValues[i]);
                    if (i < (l - 1)) {
                        sb.append(", ");
                    }
                }
                sb.append(" ]");
            }
            sb.append("\n");
        }
        sb.append("**********************************************************");
        return sb.toString();
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(final HttpSession httpSession) {
        this.httpSession = httpSession;
    }
}
