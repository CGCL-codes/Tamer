package com.ecomponentes.formularios.mapabuyersetor.bo;

import java.util.ArrayList;
import java.util.List;
import com.ecomponentes.dao.BeanUtilsBO;
import com.ecomponentes.formularios.empresasetor.to.EmpresaSetorTO;
import com.ecomponentes.formularios.mapabuyer.to.MapaBuyerTO;
import com.ecomponentes.formularios.mapabuyersetor.dao.MapaBuyerSetorDAO;
import com.ecomponentes.formularios.mapabuyersetor.to.MapaBuyerSetorTO;
import com.ecomponentes.formularios.setor.bo.SetorBO;
import com.ecomponentes.formularios.setor.to.SetorTO;
import com.ecomponentes.formularios.subsetor.bo.SubSetorBO;
import com.ecomponentes.formularios.subsetor.to.SubSetorTO;
import com.ecomponentes.hibernate.empresasetor.TbEmpresaSetor;
import com.ecomponentes.hibernate.mapabuyersetor.TbMapaBuyerSetor;

public class MapaBuyerSetorBO {

    private MapaBuyerSetorDAO dao;

    /** MapaBuyerSetorBO padr�o. */
    public MapaBuyerSetorBO() {
        dao = new MapaBuyerSetorDAO();
    }

    /**
	 * Utilizado para transformar uma List de objetos hibernate para um array de
	 * empresasetorTO.
	 * 
	 * @param lista
	 *            Lista a se transformada.
	 * @return Array de empresasetorTO.
	 */
    private MapaBuyerSetorTO[] carregarMapaBuyerSetors(List lista) {
        MapaBuyerSetorTO[] to = new MapaBuyerSetorTO[lista.size()];
        for (int ct = 0; ct < lista.size(); ++ct) {
            to[ct] = new MapaBuyerSetorTO();
            try {
                BeanUtilsBO.copyPropertiesMapaBuyerSetor(to[ct], (TbMapaBuyerSetor) lista.get(ct));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return to;
    }

    /**
	 * Utilizado para carregar todos os regsitro do banco de dados.
	 * 
	 * @return Array de SosEmpresaSetorTO populado.
	 */
    public MapaBuyerSetorTO[] selecionarTodos() {
        List lista = dao.selecionarTodos();
        return carregarMapaBuyerSetors(lista);
    }

    public MapaBuyerSetorTO getMapaBuyerSetor(Integer id) {
        MapaBuyerSetorTO mapaBuyerSetorTO = new MapaBuyerSetorTO();
        try {
            BeanUtilsBO.copyPropertiesMapaBuyerSetor(mapaBuyerSetorTO, (TbMapaBuyerSetor) dao.selecionarMapaBuyerSetor(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapaBuyerSetorTO;
    }

    public void removeMapaBuyerSetor(Integer id) {
        TbMapaBuyerSetor mapaBuyerSetor = new TbMapaBuyerSetor();
        try {
            BeanUtilsBO.copyPropertiesMapaBuyerSetor(mapaBuyerSetor, getMapaBuyerSetor(id));
            dao.removeObject(mapaBuyerSetor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeMapaBuyersSetor(Integer id) {
        if (id != null && id.intValue() != 0) {
            List lista = dao.selecionarMapaBuyerSetorByMapaBuyer(id.toString());
            if (lista != null) {
                for (int i = 0; i < lista.size(); i++) {
                    dao.removeObject((TbMapaBuyerSetor) lista.get(i));
                }
            }
        }
    }

    public void inserirAlterar(MapaBuyerSetorTO to) {
        TbMapaBuyerSetor mapaBuyerSetor = new TbMapaBuyerSetor();
        try {
            BeanUtilsBO.copyPropertiesMapaBuyerSetor(mapaBuyerSetor, to);
            mapaBuyerSetor.setIdTbMapaBuyerSetor(null);
            dao.saveUpdateObject(mapaBuyerSetor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popInserirAlterar(EmpresaSetorTO to) {
        TbEmpresaSetor mapaBuyerSetor = new TbEmpresaSetor();
        try {
            BeanUtilsBO.copyPropertiesEmpresaSetor(mapaBuyerSetor, to);
            if (mapaBuyerSetor.getIdTbEmpresaSetor().longValue() == 0L) {
                mapaBuyerSetor.setIdTbEmpresaSetor(null);
            }
            dao.saveUpdateObject(mapaBuyerSetor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MapaBuyerSetorTO getMapaBuyerSetorByEmpresa(String id) {
        MapaBuyerSetorTO mapaBuyerSetorTO = new MapaBuyerSetorTO();
        List lista = dao.selecionarMapaBuyerMainSetorByMapaBuyer(id);
        if (lista != null && lista.size() > 0) {
            for (int i = 0; i < lista.size(); i++) {
                try {
                    BeanUtilsBO.copyPropertiesMapaBuyerSetor(mapaBuyerSetorTO, (TbMapaBuyerSetor) lista.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return mapaBuyerSetorTO;
    }

    public MapaBuyerSetorTO[] getMapaBuyerSetoresByMapaBuyer(String id) {
        List lista = dao.selecionarMapaBuyerSetorByMapaBuyer(id);
        MapaBuyerSetorTO[] mapaBuyerSetorTO = new MapaBuyerSetorTO[lista.size()];
        if (lista != null && lista.size() > 0) {
            for (int i = 0; i < lista.size(); i++) {
                try {
                    mapaBuyerSetorTO[i] = new MapaBuyerSetorTO();
                    BeanUtilsBO.copyPropertiesMapaBuyerSetor(mapaBuyerSetorTO[i], (TbMapaBuyerSetor) lista.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return mapaBuyerSetorTO;
    }

    public static void main(String[] args) {
        MapaBuyerSetorBO bo = new MapaBuyerSetorBO();
        TbMapaBuyerSetor tbmapaBuyerSetor = (TbMapaBuyerSetor) bo.dao.selecionarMapaBuyerSetor(new Integer(1));
        System.out.println(tbmapaBuyerSetor);
    }

    public void gravaMapaBuyerSetor(Integer mapaBuyer, MapaBuyerSetorTO[] mapaBuyerSetor) {
        removeMapaBuyersSetor(mapaBuyer);
        for (int i = 0; i < mapaBuyerSetor.length; i++) {
            mapaBuyerSetor[i].getTbMapaBuyer().setIdMapaBuyer(mapaBuyer.toString());
            inserirAlterar(mapaBuyerSetor[i]);
        }
    }

    public MapaBuyerSetorTO[] selectAllSetors() {
        SetorBO setorBO = new SetorBO();
        SubSetorBO subSetorBO = new SubSetorBO();
        SetorTO[] listaSetor = setorBO.selecionarTodos();
        SubSetorTO[] listaSubSetor = subSetorBO.selecionarTodos();
        int i = 0;
        MapaBuyerSetorTO[] mapaBuyerSetorTO = new MapaBuyerSetorTO[(listaSetor.length + listaSubSetor.length)];
        for (i = 0; i < listaSetor.length; i++) {
            mapaBuyerSetorTO[i] = new MapaBuyerSetorTO();
            mapaBuyerSetorTO[i].setIdTbMapaBuyerSetor(Integer.toString(1 + i));
            mapaBuyerSetorTO[i].setTbMapaBuyer(new MapaBuyerTO());
            mapaBuyerSetorTO[i].setTbSetor(listaSetor[i]);
            mapaBuyerSetorTO[i].setTbSubSetor(new SubSetorTO());
            mapaBuyerSetorTO[i].getTbSubSetor().setNomeSetor("");
            mapaBuyerSetorTO[i].getTbSubSetor().setTbSetor(listaSetor[i]);
        }
        for (int j = 0; j < listaSubSetor.length; j++) {
            mapaBuyerSetorTO[i + j] = new MapaBuyerSetorTO();
            mapaBuyerSetorTO[i + j].setIdTbMapaBuyerSetor(Integer.toString(1 + i + j));
            mapaBuyerSetorTO[i + j].setTbMapaBuyer(new MapaBuyerTO());
            mapaBuyerSetorTO[i + j].setTbSetor(listaSubSetor[j].getTbSetor());
            mapaBuyerSetorTO[i + j].setTbSubSetor(listaSubSetor[j]);
        }
        return mapaBuyerSetorTO;
    }

    public List selectAllSetorsList() {
        List lista = new ArrayList();
        SetorBO setorBO = new SetorBO();
        SubSetorBO subSetorBO = new SubSetorBO();
        SetorTO[] listaSetor = setorBO.selecionarTodos();
        SubSetorTO[] listaSubSetor = subSetorBO.selecionarTodos();
        int i = 0;
        MapaBuyerSetorTO[] mapaBuyerSetorTO = new MapaBuyerSetorTO[(listaSetor.length + listaSubSetor.length)];
        for (i = 0; i < listaSetor.length; i++) {
            mapaBuyerSetorTO[i] = new MapaBuyerSetorTO();
            mapaBuyerSetorTO[i].setIdTbMapaBuyerSetor(Integer.toString(1 + i));
            mapaBuyerSetorTO[i].setTbMapaBuyer(new MapaBuyerTO());
            mapaBuyerSetorTO[i].setTbSetor(listaSetor[i]);
            mapaBuyerSetorTO[i].setTbSubSetor(new SubSetorTO());
            mapaBuyerSetorTO[i].getTbSubSetor().setNomeSetor("");
            mapaBuyerSetorTO[i].getTbSubSetor().setTbSetor(listaSetor[i]);
            lista.add(mapaBuyerSetorTO[i]);
        }
        for (int j = 0; j < listaSubSetor.length; j++) {
            mapaBuyerSetorTO[i + j] = new MapaBuyerSetorTO();
            mapaBuyerSetorTO[i + j].setIdTbMapaBuyerSetor(Integer.toString(1 + i + j));
            mapaBuyerSetorTO[i + j].setTbMapaBuyer(new MapaBuyerTO());
            mapaBuyerSetorTO[i + j].setTbSetor(listaSubSetor[j].getTbSetor());
            mapaBuyerSetorTO[i + j].setTbSubSetor(listaSubSetor[j]);
            lista.add(mapaBuyerSetorTO[i]);
        }
        return lista;
    }
}
