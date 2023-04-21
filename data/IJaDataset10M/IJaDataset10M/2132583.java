package slim3.service;

import java.util.List;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;
import slim3.meta.MailTemplateVariablesMeta;
import slim3.model.MailTemplateVariables;
import com.google.appengine.api.datastore.Key;

public class MailTemplateVariablesService {

    private MailTemplateVariablesMeta t = new MailTemplateVariablesMeta();

    /**
     * �ϒl���X�g���P���쐬���܂�
     * 
     * @param vars
     * @return
     */
    public MailTemplateVariables regist(MailTemplateVariables var) {
        if (var == null) {
            throw new IllegalArgumentException("Error! Input var is null.");
        } else if (searchFromName(var.getName()) != null) {
            throw new IllegalArgumentException("Error! Input var.name is already exist.");
        }
        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        gtx.put(var);
        gtx.commit();
        return var;
    }

    /**
     * �ϒl���X�g��S�Ď擾���܂�
     * 
     * @return
     */
    public List<MailTemplateVariables> searchAll() {
        return Datastore.query(t).asList();
    }

    /**
     * �ϒl���X�g��Key�łP���擾���܂�
     * 
     * @param key
     * @return
     */
    public MailTemplateVariables searchFromKey(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Error! Input key is null.");
        }
        return Datastore.query(t).filter(t.key.equal(key)).asSingle();
    }

    /**
     * �ϒl���X�g�𖼑O�łP���擾���܂�
     * 
     * @param name
     * @return
     */
    public MailTemplateVariables searchFromName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Error! Input name is null.");
        }
        return Datastore.query(t).filter(t.name.equal(name)).asSingle();
    }

    /**
     * �ϒl���X�g���P���X�V���܂�
     * 
     * @param var
     * @return
     */
    public MailTemplateVariables update(MailTemplateVariables var) {
        if (var == null) {
            throw new IllegalArgumentException("Error! Input var is null.");
        } else if (searchFromKey(var.getKey()) == null) {
            throw new IllegalArgumentException("Error! Target var does not exist. Key is " + var.getKey());
        } else if (searchFromName(var.getName()) != null) {
            throw new IllegalArgumentException("Error! Target name has already exists.");
        }
        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        gtx.put(var);
        gtx.commit();
        return var;
    }

    /**
     * �ϒl���X�g���P���폜���܂�
     * 
     * @param key
     * @return
     */
    public boolean delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Error! Input key is null.");
        } else if (searchFromKey(key) == null) {
            throw new IllegalArgumentException("Error! Target variables does not exist. Key is " + key);
        }
        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        gtx.delete(key);
        gtx.commit();
        return true;
    }

    /**
     * �ϒl���X�g���P���폜���܂�
     * 
     * @param var
     * @return
     */
    public boolean delete(MailTemplateVariables var) {
        if (var == null) {
            throw new IllegalArgumentException("Error! Input var is null.");
        } else if (searchFromKey(var.getKey()) == null) {
            throw new IllegalArgumentException("Error! Target variables does not exist. var is " + var.getKey());
        }
        return delete(var.getKey());
    }
}
