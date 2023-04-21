package es.cim.loginModule;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("config/sistra-plugins/plugin-login.properties"));
        LdapCim l = new LdapCim(props);
        UsuarioLdapCim usu = l.autenticar("sistraoper", "sistraoper1");
        if (usu != null) {
            System.out.println(usu.getDn());
            System.out.println(usu.getNif());
            System.out.println(usu.getNombre());
            System.out.println(usu.getApellidos());
            System.out.println(usu.getUser());
            if (usu.getRoles() != null) {
                for (Iterator it = usu.getRoles().iterator(); it.hasNext(); ) {
                    System.out.println(it.next());
                }
            }
        } else {
            System.out.println("Auth incorrecta");
        }
    }
}
