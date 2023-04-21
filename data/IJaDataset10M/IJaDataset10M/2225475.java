package appbaratoextreme.negocio;

import appbaratoextreme.classesBasicas.Locador;
import appbaratoextreme.repositorio.RepositorioContrato;
import appbaratoextreme.repositorio.RepositorioImovel;
import appbaratoextreme.repositorio.RepositorioLocador;
import appbaratoextreme.util.MethodsUtil;
import appbaratoextreme.util.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;

/**
 *
 * @author MarcosPaulo
 */
public class NegocioLocador {

    private RepositorioLocador repositorioLocador;

    private static NegocioLocador negocioLocador;

    public static NegocioLocador getNegocioLocador() {
        if (negocioLocador != null) {
            return negocioLocador;
        } else {
            return new NegocioLocador();
        }
    }

    private NegocioLocador() {
        MethodsUtil.systemOutInstanciandoInfo(NegocioLocador.class);
        if (repositorioLocador == null) {
            repositorioLocador = RepositorioLocador.getRepositorioLocador();
        }
    }

    public void cadastrarLocador(Locador locador) throws HibernateException, Exception {
        Validator.validarObj(locador);
        this.repositorioLocador.cadasrarLocador(locador);
    }

    public void deletarLocador(Locador... locadors) throws HibernateException, Exception {
        this.repositorioLocador.deletarLocador(locadors);
    }

    public void atualizarLocador(Locador locador) throws HibernateException, Exception {
        Validator.validarObj(locador);
        this.repositorioLocador.atualizarLocador(locador);
    }

    public List<Locador> listarLocador(String hql) throws HibernateException, Exception {
        List<Locador> list = new ArrayList<Locador>();
        for (Locador locador : this.repositorioLocador.listarLocador(hql)) {
            Set contratos = new HashSet(RepositorioContrato.getRepositorioContrato().listarContrato("from Contrato where codLocador = " + locador.getCodLocador()));
            Set imoveis = new HashSet(RepositorioImovel.getRepositorioImovel().listarImovel("from Imovel where codImovel in(from Locador where codLocador = " + locador.getCodLocador() + " )"));
            locador.setContratos(contratos);
            locador.setImovels(imoveis);
            list.add(locador);
        }
        return list;
    }

    public List<Locador> listarLocadorPorNome(String string) throws Exception, Exception, Exception {
        return this.repositorioLocador.listarLocadorPorNome(string);
    }

    public List<Locador> listarLocador() throws HibernateException, Exception {
        List<Locador> list = new ArrayList<Locador>();
        for (Locador locador : this.repositorioLocador.listarLocador()) {
            Set contratos = new HashSet(RepositorioContrato.getRepositorioContrato().listarContrato("from Contrato where codLocador = " + locador.getCodLocador()));
            Set imoveis = new HashSet(RepositorioImovel.getRepositorioImovel().listarImovel("from Imovel where codImovel in(from Locador where codLocador = " + locador.getCodLocador() + " )"));
            locador.setContratos(contratos);
            locador.setImovels(imoveis);
            list.add(locador);
        }
        return list;
    }

    public Locador procurarLocador(Locador locador) throws HibernateException, Exception {
        return this.repositorioLocador.procurarLocador(locador);
    }

    public Locador procurarLocadorId(Integer id) throws HibernateException, Exception {
        return this.repositorioLocador.procurarLocadorId(id);
    }

    public static void main(String[] args) {
        try {
            NegocioLocador nLocador = NegocioLocador.getNegocioLocador();
            List<Locador> listarLocador = nLocador.listarLocador();
            for (Locador locador : listarLocador) {
                System.out.println(locador.getNome());
                System.out.println(locador.getCpf());
                System.out.println(locador.getRg());
            }
        } catch (HibernateException hex) {
            for (int i = 0; i < hex.getMessages().length; i++) {
                System.err.println(hex.getMessage(i));
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
