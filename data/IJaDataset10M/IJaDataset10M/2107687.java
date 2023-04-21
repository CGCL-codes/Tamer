package com.ecomponentes.formularios.moeda.bo;

import java.util.List;
import com.ecomponentes.dao.BeanUtilsBO;
import com.ecomponentes.formularios.moeda.dao.MoedaDAO;
import com.ecomponentes.formularios.moeda.to.MoedaTO;
import com.ecomponentes.hibernate.moeda.TbMoeda;

public class MoedaBO {

    private MoedaDAO dao;

    /** MoedaBO padr�o. */
    public MoedaBO() {
        dao = new MoedaDAO();
    }

    /**
	 * Utilizado para transformar uma List de objetos hibernate para um
	 * array de moedaTO.
	 * @param lista Lista a se transformada.
	 * @return Array de moedaTO.
	 */
    private MoedaTO[] carregarMoedas(List lista) {
        MoedaTO[] to = new MoedaTO[lista.size()];
        for (int ct = 0; ct < lista.size(); ++ct) {
            to[ct] = new MoedaTO();
            try {
                BeanUtilsBO.copyProperties(to[ct], (TbMoeda) lista.get(ct));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return to;
    }

    /**
	 * Utilizado para carregar todos os regsitro do banco de dados.
	 * @return Array de SosMoedaTO populado.
	 */
    public MoedaTO[] selecionarTodos() {
        List lista = dao.selecionarTodos();
        return carregarMoedas(lista);
    }

    public MoedaTO getMoeda(Integer id) {
        MoedaTO moedaTO = new MoedaTO();
        if (id.intValue() != 0) {
            try {
                BeanUtilsBO.copyProperties(moedaTO, (TbMoeda) dao.selecionarMoeda(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moedaTO;
    }

    public void removeMoeda(Integer id) {
        TbMoeda moeda = new TbMoeda();
        try {
            BeanUtilsBO.copyProperties(moeda, getMoeda(id));
            dao.removeObject(moeda);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inserirAlterar(MoedaTO to) {
        TbMoeda moeda = new TbMoeda();
        try {
            BeanUtilsBO.copyProperties(moeda, to);
            if (moeda.getIdMoeda().longValue() == 0L) {
                moeda.setIdMoeda(null);
            }
            dao.saveUpdateObject(moeda);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MoedaTO[] getMoedas(String campo, String valor) {
        List lista = dao.selecionarMoedas(campo, valor);
        MoedaTO[] moedaTO = new MoedaTO[lista.size()];
        return carregarMoedas(lista);
    }
}
