package framework.objectRole;

import java.io.Serializable;
import java.util.*;
import framework.FIPA.AgentPlatformDescription;
import framework.organization.MainOrganization;

/**
 * Classe abstrata respons�vel por representar as caracter�sticas estruturais e comportamentais
 * de um papel de objeto. 
 */
public abstract class ObjectRole implements Serializable {

    /**
     * Nome do papel de objeto.
     */
    protected String name = null;

    /**
     * Organiza��o onde o papel de objeto est� sendo desempenhado.
     */
    protected MainOrganization owner = null;

    /**
     * Objeto associado ao papel de objeto.
     */
    protected Object theObject = null;

    /**
     * Fornece a organiza��o em que o papel de objeto est� sendo desempenhado.
     * @return
     * Organiza��o onde o papel de objeto est� sendo desempenhado.
     */
    public MainOrganization getOwner() {
        return this.owner;
    }

    /**
     * Atribui a organiza��o onde o papel de objeto ser� desempenhado.
     * @param newOwner
     * Organiza��o onde o papel de objeto ser� desempenhado.
     */
    public void setOwner(MainOrganization newOwner) {
        this.owner = newOwner;
    }

    /**
     * Fornece o objeto que est� associado ao papel de objeto.
     * @return
     * Objeto associado ao papel de objeto.
     */
    public Object getObject() {
        return this.theObject;
    }

    /**
     * Atribui o objeto que est� associado ao papel de objeto.
     * @param newObject
     * Objeto associado ao papel de objeto.
     */
    public void setObject(Object newObject) {
        this.theObject = newObject;
    }

    /**
     * M�todo respons�vel por realizar a destrui��o do papel de objeto.
     */
    public void destroy() {
        setObject(null);
        MainOrganization organization = getOwner();
        Collection vRoles = organization.getObjectRoles();
        Iterator enumvRoles = vRoles.iterator();
        while (enumvRoles.hasNext()) {
            ObjectRole roleAux = (ObjectRole) enumvRoles.next();
            if (roleAux == this) {
                vRoles.remove(roleAux);
            }
        }
    }

    /**
     * Construtor da classe respons�vel por atribuir um valor booleano que indica se
     * o papel de objeto relacionado ao identificador, tem sua origem de cria��o
     * da plataforma local ou n�o, e al�m disso, atribui o seu nome.
     * @param name
     * Nome do papel de objeto. 
     * @param isLocal
     * Vari�vel booleana que indica se o papel de objeto 
     * relacionado ao identificador, possui sua origem da plataforma local (true), 
     * ou de outra qualquer (false).
     */
    public void setObjectRoleName(String name, boolean isLocal) {
        if (!isLocal) {
            this.name = name;
        } else {
            String hap;
            hap = AgentPlatformDescription.getInstance().getName();
            if (hap == null) {
                throw new RuntimeException("Unknown Platform Name");
            }
            hap = "@" + hap;
            this.name = name.trim();
            this.name = name.concat(hap);
        }
    }

    /**
     * Fornece o nome que identifica o papeld e objeto.
     * @return
     * Nome que identifica o papel de objeto.
     */
    public String getObjectRoleName() {
        return name;
    }
}
