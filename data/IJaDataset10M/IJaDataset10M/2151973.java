package br.com.petrobras.persistence.ibatis;

import java.sql.SQLException;
import java.util.List;
import br.com.petrobras.model.TipoLogradouroVO;
import br.com.petrobras.persistence.TipoLogradouroDAO;
import br.com.petrobras.util.IbatisUtil;

public class IbatisTipoLogradouroDAOImpl implements TipoLogradouroDAO {

    IbatisTipoLogradouroDAOImpl() {
    }

    @SuppressWarnings("unchecked")
    public List<TipoLogradouroVO> findAll() throws SQLException {
        List<TipoLogradouroVO> list = (List) IbatisUtil.getSQLMap().queryForList("TipoLogradouro.findAll", null);
        return list;
    }

    public TipoLogradouroVO findByPk(TipoLogradouroVO obj) throws SQLException {
        TipoLogradouroVO ret = (TipoLogradouroVO) IbatisUtil.getSQLMap().queryForObject("TipoLogradouro.findByPk", obj);
        return ret;
    }

    public TipoLogradouroVO findBySigla(TipoLogradouroVO obj) throws SQLException {
        TipoLogradouroVO ret = (TipoLogradouroVO) IbatisUtil.getSQLMap().queryForObject("TipoLogradouro.findBySigla", obj);
        return ret;
    }
}
