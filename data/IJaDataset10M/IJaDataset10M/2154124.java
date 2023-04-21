package br.usp.ime.protoc.pessoa;

import java.util.HashSet;
import java.util.Iterator;
import com.google.gwt.user.client.rpc.IsSerializable;
import br.usp.ime.protoc.fujaba.FEmptyIterator;

public class Telefone implements IsSerializable {

    private String ddd;

    public String getDdd() {
        return this.ddd;
    }

    public void setDdd(String value) {
        if (this.ddd != value) {
            this.ddd = value;
        }
    }

    private String numero;

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String value) {
        if (this.numero != value) {
            this.numero = value;
        }
    }

    /**
    * <pre>
    *           1..*    has     1..* 
    * Telefone ---------------------- Pessoa
    *           telefone      pessoa 
    * </pre>
    */
    private HashSet pessoa;

    public boolean addToPessoa(Pessoa value) {
        boolean changed = false;
        if (value != null) {
            if (this.pessoa == null) {
                this.pessoa = new HashSet();
            }
            changed = this.pessoa.add(value);
            if (changed) {
                value.addToTelefone(this);
            }
        }
        return changed;
    }

    public boolean hasInPessoa(Pessoa value) {
        return ((this.pessoa != null) && (value != null) && this.pessoa.contains(value));
    }

    public Iterator iteratorOfPessoa() {
        return ((this.pessoa == null) ? FEmptyIterator.get() : this.pessoa.iterator());
    }

    public void removeAllFromPessoa() {
        Pessoa tmpValue;
        Iterator iter = this.iteratorOfPessoa();
        while (iter.hasNext()) {
            tmpValue = (Pessoa) iter.next();
            this.removeFromPessoa(tmpValue);
        }
    }

    public boolean removeFromPessoa(Pessoa value) {
        boolean changed = false;
        if ((this.pessoa != null) && (value != null)) {
            changed = this.pessoa.remove(value);
            if (changed) {
                value.removeFromTelefone(this);
            }
        }
        return changed;
    }

    public int sizeOfPessoa() {
        return ((this.pessoa == null) ? 0 : this.pessoa.size());
    }

    /**
    * <pre>
    *           0..*         has         1 
    * Telefone ---------------------------- TipoTelefone
    *           telefone      tipoTelefone 
    * </pre>
    */
    private TipoTelefone tipoTelefone;

    public TipoTelefone getTipoTelefone() {
        return this.tipoTelefone;
    }

    public boolean setTipoTelefone(TipoTelefone value) {
        boolean changed = false;
        if (this.tipoTelefone != value) {
            TipoTelefone oldValue = this.tipoTelefone;
            if (this.tipoTelefone != null) {
                this.tipoTelefone = null;
                oldValue.removeFromTelefone(this);
            }
            this.tipoTelefone = value;
            if (value != null) {
                value.addToTelefone(this);
            }
            changed = true;
        }
        return changed;
    }

    public void removeYou() {
        TipoTelefone tmpTipoTelefone = getTipoTelefone();
        if (tmpTipoTelefone != null) {
            setTipoTelefone(null);
        }
        removeAllFromPessoa();
    }
}
