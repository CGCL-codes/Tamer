package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import services.busqueda.GetProductAction;
import modelo.Product;

public class ProductController implements Controller {

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<Product> todosLosProductos = null;
        GetProductAction busqueda = new GetProductAction();
        try {
            todosLosProductos = busqueda.getSelectedP();
            Map<String, Object> searchResult = new HashMap<String, Object>();
            logger.debug("Productos traidos de la base de datos");
            searchResult.put("titulo", "reporte_producto_titulo");
            if (todosLosProductos == null || todosLosProductos.isEmpty()) {
                logger.debug("Tabla de productos vacia");
                searchResult.put("todosLosProductos", false);
            } else {
                logger.debug("Tabla de productos lista para enviarse");
                searchResult.put("todosLosProductos", true);
                searchResult.put("results", todosLosProductos);
            }
            return new ModelAndView("products4Users", searchResult);
        } catch (Exception e) {
            return new ModelAndView("products4Users");
        }
    }
}
