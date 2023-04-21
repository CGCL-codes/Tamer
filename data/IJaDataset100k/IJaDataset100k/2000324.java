package galerias.servlets.imagenes;

import galerias.servlets.imagenes.to.ImagenGenericaTo;
import galerias.utilidades.Utilidades;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImagenesEventoServlet extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4548101011196883404L;

    public static HashMap<String, ImagenGenericaTo> IMAGENES_EVENTOS_HM = new HashMap<String, ImagenGenericaTo>();

    private static final String CODIGO_IMAGEN_EVENTO = "codigoImagenObra";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String codigoEvento = String.valueOf(request.getParameter(CODIGO_IMAGEN_EVENTO));
            ImagenGenericaTo imagenEvento = IMAGENES_EVENTOS_HM.get(codigoEvento);
            byte[] imagen = null;
            String tipoImagen = null;
            if (imagenEvento != null) {
                imagen = imagenEvento.getImagen();
                tipoImagen = imagenEvento.getTipoImagen();
                if (imagen == null) {
                    imagen = Utilidades.obtenerImagenNoDisponible();
                    tipoImagen = Utilidades.TIPO_IMAGEN_NO_DISPONIBLE;
                }
            } else {
                imagen = Utilidades.obtenerImagenNoDisponible();
                tipoImagen = Utilidades.TIPO_IMAGEN_NO_DISPONIBLE;
            }
            if (imagen != null) {
                ServletOutputStream out = response.getOutputStream();
                response.setContentType(tipoImagen);
                out.write(imagen);
                out.flush();
                out.close();
            } else {
                System.out.println("foto nula");
            }
        } catch (Exception e) {
            System.out.println("Error foto >> " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
