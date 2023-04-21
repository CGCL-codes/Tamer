package appbaratoextreme.negocio;

import appbaratoextreme.classesBasicas.Locatario;
import appbaratoextreme.repositorio.RepositorioContrato;
import appbaratoextreme.repositorio.RepositorioImovel;
import appbaratoextreme.repositorio.RepositorioLocatario;
import appbaratoextreme.util.MethodsUtil;
import appbaratoextreme.util.Validator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;

/**
 *
 * @author MarcosPaulo
 */
public class NegocioLocatario {

    private static NegocioLocatario negocioLocatario;

    private RepositorioLocatario repositorioLocatario;

    private NegocioLocatario() {
        MethodsUtil.systemOutInstanciandoInfo(NegocioLocatario.class);
        if (repositorioLocatario == null) {
            repositorioLocatario = RepositorioLocatario.getRepositorioLocatario();
        }
    }

    /**
     * 
     * @return 
     */
    public static NegocioLocatario getNegocioLocatario() {
        if (negocioLocatario != null) {
            return negocioLocatario;
        }
        return new NegocioLocatario();
    }

    public void cadastrarLocatario(Locatario locatario) throws HibernateException, Exception {
        Validator.validarObj(locatario);
        repositorioLocatario.cadasrarLocatario(locatario);
    }

    public void deletarLocatario(final Locatario... locatarios) throws HibernateException, Exception {
        repositorioLocatario.deletarLocatario(locatarios);
    }

    public void atualizarLocatario(final Locatario locatario) throws HibernateException, Exception {
        Validator.validarObj(locatario);
        repositorioLocatario.atualizarLocatario(locatario);
    }

    public List<Locatario> listarLocatario(final String hql) throws HibernateException, Exception {
        return popularImovelContrato(repositorioLocatario.listarLocatario(hql));
    }

    public List<Locatario> listarLocatario() throws HibernateException, Exception {
        return popularImovelContrato(this.repositorioLocatario.listarLocatario());
    }

    private List<Locatario> popularImovelContrato(List<Locatario> l) throws Exception {
        List<Locatario> list = new ArrayList<Locatario>();
        for (Locatario locatario : l) {
            Set contratos = new HashSet(RepositorioContrato.getRepositorioContrato().listarContrato("from Contrato where codLocatario = " + locatario.getCodLocatario()));
            Set imoveis = new HashSet(RepositorioImovel.getRepositorioImovel().listarImovel("from Imovel where codImovel in(from Locatario where codLocatario = " + locatario.getCodLocatario() + " )"));
            locatario.setContratos(contratos);
            locatario.setImovels(imoveis);
            list.add(locatario);
        }
        return list;
    }

    public Locatario procurarLocatario(final Locatario locatario) throws HibernateException, Exception {
        return repositorioLocatario.procurarLocatario(locatario);
    }

    public Locatario procurarLocatarioId(final Integer id) throws HibernateException, Exception {
        return repositorioLocatario.procurarLocadorId(id);
    }

    public static void main(String[] args) {
    }
}
