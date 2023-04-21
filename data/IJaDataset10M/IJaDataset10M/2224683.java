package model.dao;

import model.dao.hibernate.HibernateDAO;
import model.valueObject.ImagemVO;
import java.util.*;

/**
 * <strong>ImagemDao</strong>
 * <p>
 * Objeto de Acesso a Fonte de Dados da entidade Imagem.
 * <br>
 * <strong>Hist�rico de Vers�es</strong>
 * <br>
 * 1.0 - Cria��o da classe
 *
 * @author <a href="mailto:raphaufrj@gmail.com">Raphael Rodrigues</a>
 * @author <a href="mailto:pvkelecom@gmail.com">Patrick Kelecom</a>
 * <br>
 * @version 1.0 - 12/12/2009 18:35 : X-MDA
 */
public class ImagemDao extends HibernateDAO {

    public ImagemDao() {
        super(ImagemVO.class);
    }

    public void criarImagem() {
    }

    public void setDocumento() {
    }
}
