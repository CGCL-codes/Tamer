package cea;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Application Lifecycle Listener implementation class AplicacionListener
 *
 */
public class AplicacionListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("Aplicacion TIENDA iniciada");
        List<Producto> catalogo = new ArrayList<Producto>();
        Producto p1 = new Producto(1, "PRODUCTO 1", 150);
        Producto p2 = new Producto(2, "PRODUCTO 2", 360.90);
        Producto p3 = new Producto(3, "PRODUCTO 3", 290.95);
        catalogo.add(p1);
        catalogo.add(p2);
        catalogo.add(p3);
        arg0.getServletContext().setAttribute(AtributosConstantes.catalogo.toString(), catalogo);
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("Aplicacion TIENDA destruida");
    }
}
