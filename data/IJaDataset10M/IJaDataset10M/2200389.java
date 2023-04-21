package teste.springsecurity;

import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import springsecurity.modelo.Usuario;
import springsecurity.servico.CustomUserDetailsService;

/**
 * @author walanem
 */
public class TesteUsuario {

    private static ApplicationContext applicationContext;

    private static CustomUserDetailsService customUserDetailsService;

    @BeforeClass
    public static void setUpClass() throws Throwable {
        applicationContext = new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/applicationContext.xml");
        customUserDetailsService = (CustomUserDetailsService) applicationContext.getBean("customUserDetailsService");
    }

    @Test
    public void getUsuarios() {
        List<Usuario> usuarios = customUserDetailsService.getListaOrdenada();
        for (Usuario usuario : usuarios) {
            System.out.println("NOME: " + usuario.getUsername());
            System.out.println("SENHA: " + usuario.getPassword());
            System.out.println("");
        }
    }
}
