package ao.com.bna.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.hssf.record.formula.functions.Dmax;
import org.hibernate.Query;
import org.hibernate.Session;
import ao.com.bna.beans.Devedor;
import ao.com.bna.beans.DmfasParticipante;
import ao.com.bna.beans.DmfasSigma;
import ao.com.bna.beans.SigmaParticipante;
import ao.com.bna.beans.TipoRelatorio;
import ao.com.bna.configuracao.ConexaoHibernate;
import ao.com.bna.util.BeanComboBoxItem;
import ao.com.bna.util.Constante;

public class DmfasSigmaDao extends AbstractHibernateDao<DmfasSigma> {

    public DmfasSigmaDao() {
        super(DmfasSigma.class);
    }

    @Override
    protected Session getSession() {
        return ConexaoHibernate.getSessionfactory().getCurrentSession();
    }

    public List<BeanComboBoxItem> listaDevedores(int sistema) {
        Query query = (Query) this.getSession().getNamedQuery("DmfasSigma.findAll");
        List<DmfasSigma> listaDmfasSigma = query.list();
        List<String> itemsString = new ArrayList<String>();
        for (DmfasSigma dmfasSigma : listaDmfasSigma) {
            itemsString.add(new String(dmfasSigma.getDevedor().trim()));
        }
        Set listaSet = new HashSet(itemsString);
        itemsString = new ArrayList(listaSet);
        Collections.sort(itemsString);
        List<BeanComboBoxItem> listaDevedores = new ArrayList<BeanComboBoxItem>();
        listaDevedores.add(new BeanComboBoxItem("*", "«Todos»"));
        for (String item : itemsString) {
            listaDevedores.add(new BeanComboBoxItem(item, item));
        }
        return listaDevedores;
    }

    public List<BeanComboBoxItem> listaMoedas(int sistema) {
        Query query = (Query) this.getSession().getNamedQuery("DmfasSigma.findAll");
        List<DmfasSigma> listaDmfasSigma = query.list();
        List<String> itemsString = new ArrayList<String>();
        for (DmfasSigma dmfasSigma : listaDmfasSigma) {
            itemsString.add(new String(dmfasSigma.getMoeda().trim()));
        }
        Set listaSet = new HashSet(itemsString);
        itemsString = new ArrayList(listaSet);
        Collections.sort(itemsString);
        List<BeanComboBoxItem> listaMoedas = new ArrayList<BeanComboBoxItem>();
        listaMoedas.add(new BeanComboBoxItem("*", "«Todas»"));
        for (String item : itemsString) {
            listaMoedas.add(new BeanComboBoxItem(item, item));
        }
        return listaMoedas;
    }

    public List<TipoRelatorio> listarTipoRelatorio() {
        TipoRelatorioDao dao = new TipoRelatorioDao();
        List<TipoRelatorio> lista = dao.listarTipoRelatorio();
        return lista;
    }
}
