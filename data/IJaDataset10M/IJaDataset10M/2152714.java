package br.org.ged.direto.model.repository;

import java.util.Collection;
import java.util.List;
import br.org.direto.util.DataUtils;
import br.org.ged.direto.controller.forms.PesquisaForm;
import br.org.ged.direto.controller.utils.DocumentoCompleto;
import br.org.ged.direto.model.entity.Anexo;
import br.org.ged.direto.model.entity.Documento;
import br.org.ged.direto.model.entity.DocumentoDetalhes;

public interface DocumentosRepository {

    public List<DataUtils> listDocumentsFromAccount(Integer idCarteira, int ordenacao, int inicio, String box, String filtro);

    public List<Documento> listByLimited(Integer idCarteira);

    public Documento selectByIdCarteira(Integer idCarteira);

    public Long counterDocumentsByBox(String box, int idCarteira, String filtro);

    public Documento selectById(Integer idDocumentoDetalhes, Integer idCarteira);

    public Documento selectById(Integer idDocumentoDetalhes);

    public List<Documento> getAllById(Integer id);

    public List<Anexo> getAllAnexos(Integer idDocumentoDetalhes);

    public Integer getLastId();

    public Documento getByIdPKey(Integer id);

    public Collection<DocumentoCompleto> returnSearch(PesquisaForm form);

    public int returnTotalSearch(PesquisaForm form);

    public void saveNewDocumento(DocumentoDetalhes documentoDetalhes);

    public void saveOrUpdateDocumento(Documento documento);

    public DocumentoDetalhes getDocumentoDetalhes(int primaryKey);

    public int getAmountDocumentoByYear(String year);

    public void tranferirDocumentos(int idUsuario, int idCarteira);

    public List<DataUtils> fastSearch(int box, String protocolo, String assunto, String dataDe, String dataAte);

    public int returnTotalNUDExterno(int year);
}
