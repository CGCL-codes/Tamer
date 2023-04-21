package Mail;

import ComponenteBanco.MailLocal;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "EnvioCorreo")
public class EnvioCorreoBean implements EnvioCorreo, EnvioCorreoLocal {

    @PersistenceContext(unitName = "BDArquitecturaOrion")
    private EntityManager em;

    public EnvioCorreoBean() {
    }

    public boolean realizarPagoSuCertificado(String cuentaOrigen, String bancoOrigen, String claveCuentaOrigen, int monto, String correoUsuario) {
        boolean pudo = false;
        Context context;
        try {
            context = new InitialContext();
            MailLocal recuperacion = (MailLocal) context.lookup("Mail/local");
            System.out.println("Estoy en EnviarCorreoBeanRealizarPagoSuCertificado");
            pudo = recuperacion.realizarPagoSuCertificado(cuentaOrigen, bancoOrigen, claveCuentaOrigen, monto, correoUsuario);
        } catch (NamingException e) {
        }
        return pudo;
    }
}
