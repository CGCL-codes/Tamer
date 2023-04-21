package servicos.impl.materialApoio;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import listagem.ListagemInfo;
import org.jboss.annotation.ejb.LocalBinding;
import constantes.EjbNames;
import dao.materialApoio.MaterialApoioDao;
import entidades.materialApoio.MaterialApoio;
import servicos.interfaces.materialApoio.IMaterialApoio;
import util.ConversorCodigoColuna;
import util.ConversorPaginacaoDao;

/**
 * Session Bean implementation class TesteImpl
 */
@Stateless
@LocalBinding(jndiBinding = EjbNames.MATERIALAPOIO)
public class MaterialApoioImpl implements IMaterialApoio {

    /**
     * Default constructor. 
     */
    public MaterialApoioImpl() {
    }

    /**
     * Servico de consulta de alunos
     */
    public ListagemInfo buscarMaterialApoios(String valor, ListagemInfo info_listagem) throws PersistenceException {
        MaterialApoioDao dao = new MaterialApoioDao();
        Long quantidade = dao.contarMaterialApoios(valor);
        info_listagem.setTamanhoTotal(quantidade);
        info_listagem.setPaginaAtual(ConversorPaginacaoDao.corrigir_pagina_atual(info_listagem.getPaginaAtual(), info_listagem.getTamanhoPagina(), info_listagem.getTamanhoTotal()));
        Integer inicio_paginacao = ConversorPaginacaoDao.getPosicaoInicial(info_listagem.getPaginaAtual(), info_listagem.getTamanhoPagina());
        List<MaterialApoio> materialApoios = new ArrayList<MaterialApoio>(0);
        if (inicio_paginacao != null && inicio_paginacao <= quantidade) {
            String atributo_ordenacao = ConversorCodigoColuna.converter_codigo_para_nome(info_listagem.getOrdenacao(), MaterialApoio.class);
            materialApoios = dao.buscarMaterialApoios(valor, inicio_paginacao, info_listagem.getTamanhoPagina(), atributo_ordenacao, info_listagem.getCrescente());
        }
        info_listagem.setLista(materialApoios);
        return info_listagem;
    }

    /**
	 * Servico para salvar os materialApoios
	 */
    public void salvarMaterialApoio(MaterialApoio materialApoio) throws PersistenceException {
        MaterialApoioDao dao = new MaterialApoioDao();
        dao.save(materialApoio);
    }

    public MaterialApoio bucarMaterialApoio(Long id) throws PersistenceException {
        MaterialApoioDao dao = new MaterialApoioDao();
        return dao.findById(MaterialApoio.class, id);
    }

    public void atualizarMaterialApoio(MaterialApoio materialApoio) throws PersistenceException {
        MaterialApoioDao dao = new MaterialApoioDao();
        if (materialApoio.getAnexo() == null) {
            MaterialApoio material_banco = dao.getMaterialApoioComAnexo(materialApoio.getId());
            material_banco.setLink(materialApoio.getLink());
            material_banco.setNome(materialApoio.getNome());
            material_banco.setAnexo(null);
            material_banco.setNome_anexo(null);
            material_banco.setData_anexo(null);
            dao.merge(material_banco);
        } else {
            dao.merge(materialApoio);
        }
    }

    public MaterialApoio bucarMaterialApoioComAnexo(Long id) throws PersistenceException {
        MaterialApoioDao dao = new MaterialApoioDao();
        MaterialApoio retorno = dao.getMaterialApoioComAnexo(id);
        return retorno;
    }
}
