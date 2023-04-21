package br.com.bafonline.model.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import br.com.bafonline.helper.AlbumUsuarioBean;
import br.com.bafonline.helper.QuantidadeFigurinhaBean;
import br.com.bafonline.model.dao.FigurinhaDAO;
import br.com.bafonline.model.dto.AlbumDTO;
import br.com.bafonline.model.dto.FigurinhaUsuarioDTO;
import br.com.bafonline.model.dto.UsuarioDTO;
import br.com.bafonline.model.factory.DAOFactory;
import br.com.bafonline.util.exception.business.BOException;
import br.com.bafonline.util.exception.dao.DAOException;

/**
 * Classe de Neg�cio (BO - Business Object) que fornece todos as funcionalidades
 * relacionadas a figurinha. 
 * @author bafonline
 *
 */
public class FigurinhaBO {

    /**
	 * M�todo que gera uma lista de Helper Bean (AlbumUsuarioBean) contendo todas as
	 * figurinhas desejadas pelo usu�rio.
	 * @param desejadas : String separada por v�rgula contendo todos os c�digos das figurinhas desejadas.
	 * @return List<AlbumUsuarioBean> : Lista de Helper Bean (AlbumUsuarioBean)
	 * @throws BOException : Erro
	 */
    public List<AlbumUsuarioBean> geraListaFigurinhasDesejadas(String desejadas) throws BOException {
        List<AlbumUsuarioBean> list = new ArrayList<AlbumUsuarioBean>();
        String[] des = desejadas.split(",");
        for (int i = 0; i < des.length; i++) {
            AlbumUsuarioBean albumUsuarioBean = new AlbumUsuarioBean();
            albumUsuarioBean.setCodigoFigurinha(des[i]);
            albumUsuarioBean.setQuantidade("0");
            albumUsuarioBean.setTipoFigurinha(FigurinhaDAO.TIPO_FIGURINHA_DESEJADA);
            list.add(albumUsuarioBean);
        }
        return list;
    }

    /**
	 * M�todo que gera uma lista de Helper Bean (AlbumUsuarioBean) contendo todas as
	 * figurinhas repetidas do usu�rio.
	 * @param repetidas : String separada por v�rgula contendo todos os c�digos das figurinhas repetidas.
	 * @return List<AlbumUsuarioBean> : Lista de Helper Bean (AlbumUsuarioBean)
	 * @throws BOException : Erro
	 */
    public List<AlbumUsuarioBean> geraListaFigurinhasRepetidas(String repetidas) throws BOException {
        List<AlbumUsuarioBean> list = new ArrayList<AlbumUsuarioBean>();
        String[] rep = repetidas.split(",");
        for (int i = 0; i < rep.length; i++) {
            AlbumUsuarioBean albumUsuarioBean = new AlbumUsuarioBean();
            String[] fig = rep[i].split("_");
            albumUsuarioBean.setCodigoFigurinha(fig[0]);
            albumUsuarioBean.setQuantidade(fig[1]);
            albumUsuarioBean.setTipoFigurinha(FigurinhaDAO.TIPO_FIGURINHA_REPETIDA);
            list.add(albumUsuarioBean);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<QuantidadeFigurinhaBean> contaQtdFigurinhas(UsuarioDTO usuarioDTO) throws BOException {
        List<QuantidadeFigurinhaBean> list = new ArrayList<QuantidadeFigurinhaBean>();
        UsuarioBO usuarioBO = new UsuarioBO();
        int qtdDesejadas = 0;
        int qtdRepetidas = 0;
        int i = 0;
        if (usuarioDTO.getCodigo() == null) {
            throw new BOException("C�digo do usu�rio n�o informado!");
        }
        usuarioDTO = usuarioBO.findById(usuarioDTO.getCodigo());
        Set figurinhasUsuarios = usuarioDTO.getFigurinhaUsuarios();
        if (figurinhasUsuarios != null && figurinhasUsuarios.size() > 0) {
            Iterator it = figurinhasUsuarios.iterator();
            AlbumDTO albumAnterior = new AlbumDTO();
            while (it.hasNext()) {
                QuantidadeFigurinhaBean bean = new QuantidadeFigurinhaBean();
                FigurinhaUsuarioDTO figurinhaUsuarioDTO = (FigurinhaUsuarioDTO) it.next();
                if (i == 0) {
                    albumAnterior = figurinhaUsuarioDTO.getFigurinha().getAlbum();
                    i++;
                }
                if (figurinhaUsuarioDTO.getQuantidade().equals(new Short("0"))) {
                    bean.setQuantidadeDesejadas(new Integer(qtdDesejadas++));
                } else {
                    bean.setQuantidadeRepetidas(new Integer(qtdRepetidas++));
                }
                if (!albumAnterior.equals(figurinhaUsuarioDTO.getFigurinha().getAlbum())) {
                    qtdDesejadas = 0;
                    qtdRepetidas = 0;
                    albumAnterior = figurinhaUsuarioDTO.getFigurinha().getAlbum();
                    bean.setUsuarioDTO(usuarioDTO);
                    bean.setAlbumDTO(figurinhaUsuarioDTO.getFigurinha().getAlbum());
                    list.add(bean);
                }
            }
        }
        return list;
    }

    /**
	 * M�todo que retorna a quantidade de figurinhas desejadas do usu�rio (<code>usuarioDTO</code>) 
	 * para um determinado �lbum (<code>albumDTO</code>). 
	 * @param usuarioDTO : Usu�rio que cont�m �lbum com figurinhas desejadas
	 * @param albumDTO : �lbum o qual cont�m as figurinhas desejadas
	 * @return Integer : Quantidade de figurinhas desejadas
	 * @throws BOException : Erro
	 */
    public Integer contaQtdDesejadas(UsuarioDTO usuarioDTO, AlbumDTO albumDTO) throws BOException {
        int qtd = 0;
        String hql = "from FigurinhaUsuarioDTO fu where fu.usuario.codigo = " + usuarioDTO.getCodigo() + " and fu.quantidade = 0" + " and fu.figurinha.album.codigo = " + albumDTO.getCodigo();
        try {
            List<?> list = DAOFactory.getGenericDAO().findByHQL(hql);
            if (list != null) {
                qtd = list.size();
            }
        } catch (DAOException e) {
            throw new BOException(e);
        }
        return new Integer(qtd);
    }

    /**
	 * M�todo que retorna a quantidade de figurinhas repetidas do usu�rio (<code>usuarioDTO</code>) 
	 * para um determinado �lbum (<code>albumDTO</code>).
	 * @param usuarioDTO : Usu�rio que cont�m �lbum com figurinhas repetidas
	 * @param albumDTO : �lbum o qual cont�m as figurinhas repetidas
	 * @return Integer : Quantidade de figurinhas repetidas
	 * @throws BOException : Erro
	 */
    public Integer contaQtdRepetidas(UsuarioDTO usuarioDTO, AlbumDTO albumDTO) throws BOException {
        int qtd = 0;
        String hql = "from FigurinhaUsuarioDTO fu where fu.usuario.codigo = " + usuarioDTO.getCodigo() + " and fu.quantidade > 0" + " and fu.figurinha.album.codigo = " + albumDTO.getCodigo();
        try {
            List<?> list = DAOFactory.getGenericDAO().findByHQL(hql);
            if (list != null) {
                qtd = list.size();
            }
        } catch (DAOException e) {
            throw new BOException(e);
        }
        return new Integer(qtd);
    }

    /**
	 * M�todo que retorna todas as Figurinhas do �lbum (<code>albumDTO</code>) de um Usu�rio (<code>usuarioDTO</code>).
	 * @param usuarioDTO : Usu�rio do sistema
	 * @param albumDTO : �lbum do usu�rio que cont�m as figurinhas a serem retornadas
	 * @param desejadaRepetida : Indica se os registros retornados devem ser de FigurinhaUsuarioDTO Desejadas (<code>"d"</code>) ou Repetidas (<code>"r"</code>)
	 * @return List<FigurinhaUsuarioDTO> : Lista de <code>FigurinhaUsuarioDTO</code>
	 * @throws BOException : Erro
	 */
    @SuppressWarnings("unchecked")
    public List<FigurinhaUsuarioDTO> recuperaFigurinhaUsuario(UsuarioDTO usuarioDTO, AlbumDTO albumDTO, String desejadaRepetida) throws BOException {
        String hql = "from FigurinhaUsuarioDTO fu where fu.usuario.codigo = " + usuarioDTO.getCodigo() + " and fu.figurinha.album.codigo = " + albumDTO.getCodigo();
        if (desejadaRepetida != null && !"".equals(desejadaRepetida)) {
            if (desejadaRepetida.toLowerCase().equals("r")) {
                hql = hql + " and fu.quantidade > 0";
            } else if (desejadaRepetida.toLowerCase().equals("d")) {
                hql = hql + " and fu.quantidade = 0";
            }
        }
        List<FigurinhaUsuarioDTO> list = null;
        try {
            list = (List<FigurinhaUsuarioDTO>) DAOFactory.getGenericDAO().findByHQL(hql);
        } catch (DAOException e) {
            throw new BOException(e);
        }
        return list;
    }

    /**
	 * M�todo que exclui todas as Figurinhas do �lbum (<code>albumDTO</code>) de um Usu�rio (<code>usuarioDTO</code>).
	 * @param usuarioDTO : Usu�rio do sistema
	 * @param albumDTO : �lbum do usu�rio que cont�m as figurinhas a serem exclu�das
	 * @return boolean : Sucesso se verdadeiro, falha se falso
	 * @throws BOException : Erro
	 */
    public boolean excluirFigurinhaUsuario(UsuarioDTO usuarioDTO, AlbumDTO albumDTO) throws BOException {
        List<FigurinhaUsuarioDTO> list = recuperaFigurinhaUsuario(usuarioDTO, albumDTO, "");
        if (list != null && list.size() > 0) {
            for (FigurinhaUsuarioDTO figurinhaUsuarioDTO : list) {
                try {
                    DAOFactory.getGenericDAO().delete(figurinhaUsuarioDTO);
                } catch (DAOException e) {
                    throw new BOException(e);
                }
            }
        }
        return true;
    }
}
