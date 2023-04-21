package moduloRemoto.bo;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import moduloRemoto.bo.interfaces.DocumentoRemote;
import moduloRemoto.dao.interfaces.ServerLocal;
import moduloRemoto.pojo.Documento;
import org.jboss.annotation.ejb.RemoteBinding;

@Stateless
@Remote(DocumentoRemote.class)
@RemoteBinding(jndiBinding = "ejb/documento")
public class DocumentoBO implements DocumentoRemote {

    @EJB
    private ServerLocal dao;

    @Override
    public Documento persistir(Documento objDoc) {
        if (objDoc == null) return null;
        try {
            if (objDoc.getIdDoc() > 0) objDoc = dao.update(objDoc); else objDoc = dao.insert(objDoc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return objDoc;
    }

    @Override
    public Boolean excluir(int idDoc) {
        if (idDoc <= 0) return false;
        try {
            Documento objDoc = this.carregarObjeto(idDoc);
            if (objDoc != null) dao.remove(objDoc);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Documento carregarObjeto(int idDoc) {
        if (idDoc <= 0) return null;
        Documento objDoc = null;
        try {
            objDoc = dao.find(Documento.class, idDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objDoc;
    }

    @Override
    public List<Documento> getDocumentosByProjeto(int idPro) {
        List<Documento> listaDocumentos = new ArrayList<Documento>();
        try {
            for (Object objeto : dao.executeQuery("SELECT d FROM Documento d WHERE d.projetoDoc = " + idPro)) listaDocumentos.add((Documento) objeto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDocumentos;
    }
}
