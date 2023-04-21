package br.usp.ime.protoc.neuroimagem;

import java.util.HashSet;
import java.util.Iterator;
import com.google.gwt.user.client.rpc.IsSerializable;
import br.usp.ime.protoc.fujaba.FEmptyIterator;

public class GrupoAnalise implements IsSerializable {

    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int value) {
        if (this.id != value) {
            this.id = value;
        }
    }

    /**
    * <pre>
    *               1            has             1..* 
    * GrupoAnalise ----------------------------------- MapaEstatistico
    *               grupoAnalise      mapaEstatistico 
    * </pre>
    */
    private HashSet mapaEstatistico;

    public boolean addToMapaEstatistico(MapaEstatistico value) {
        boolean changed = false;
        if (value != null) {
            if (this.mapaEstatistico == null) {
                this.mapaEstatistico = new HashSet();
            }
            changed = this.mapaEstatistico.add(value);
            if (changed) {
                value.setGrupoAnalise(this);
            }
        }
        return changed;
    }

    public boolean hasInMapaEstatistico(MapaEstatistico value) {
        return ((this.mapaEstatistico != null) && (value != null) && this.mapaEstatistico.contains(value));
    }

    public Iterator iteratorOfMapaEstatistico() {
        return ((this.mapaEstatistico == null) ? FEmptyIterator.get() : this.mapaEstatistico.iterator());
    }

    public void removeAllFromMapaEstatistico() {
        MapaEstatistico tmpValue;
        Iterator iter = this.iteratorOfMapaEstatistico();
        while (iter.hasNext()) {
            tmpValue = (MapaEstatistico) iter.next();
            this.removeFromMapaEstatistico(tmpValue);
        }
    }

    public boolean removeFromMapaEstatistico(MapaEstatistico value) {
        boolean changed = false;
        if ((this.mapaEstatistico != null) && (value != null)) {
            changed = this.mapaEstatistico.remove(value);
            if (changed) {
                value.setGrupoAnalise(null);
            }
        }
        return changed;
    }

    public int sizeOfMapaEstatistico() {
        return ((this.mapaEstatistico == null) ? 0 : this.mapaEstatistico.size());
    }

    public void removeYou() {
        removeAllFromMapaEstatistico();
    }
}
