package es.eside.deusto.pfc.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import es.eside.deusto.pfc.kernel.impl.bo.CancionBO;
import es.eside.deusto.pfc.kernel.impl.bo.GrupoBO;
import es.eside.deusto.pfc.kernel.impl.bo.VideoBO;
import es.eside.deusto.pfc.kernel.impl.dao.CancionDAOHibernate;
import es.eside.deusto.pfc.kernel.impl.dao.GrupoDAOHibernate;
import es.eside.deusto.pfc.kernel.impl.dao.UsuarioDAOHibernate;
import es.eside.deusto.pfc.kernel.impl.dao.VideoDAOHibernate;

public class GruposController implements Controller {

    private static Logger log = Logger.getLogger(MainController.class);

    private GrupoDAOHibernate grupoDAO = null;

    private CancionDAOHibernate cancionDAO = null;

    private VideoDAOHibernate videoDAO = null;

    private UsuarioDAOHibernate usuarioDAO = null;

    public void setGrupoDAO(GrupoDAOHibernate gDAO) {
        this.grupoDAO = gDAO;
    }

    public void setVideoDAO(VideoDAOHibernate vDAO) {
        this.videoDAO = vDAO;
    }

    public void setUsuarioDAO(UsuarioDAOHibernate uDAO) {
        this.usuarioDAO = uDAO;
    }

    public GrupoDAOHibernate getGrupoDAO() {
        return this.grupoDAO;
    }

    public void setCancionDAO(CancionDAOHibernate cDAO) {
        this.cancionDAO = cDAO;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> myModel = new HashMap<String, Object>();
        if (request.getParameter("custom") != null) myModel.put("custom", request.getParameter("custom"));
        myModel.put("grupos", grupoDAO.getGrupos());
        myModel.put("finGrupo", grupoDAO.getGrupos().size());
        if (request.getParameter("Mostrar") != null) {
            String grupo = request.getParameter("Mostrar");
            List<CancionBO> songs = cancionDAO.getCanciones();
            List<CancionBO> songs2 = new ArrayList<CancionBO>();
            for (int i = 0; i < songs.size(); i++) {
                if (songs.get(i).getGrupo().getNombre().equals(grupo)) songs2.add(songs.get(i));
            }
            if (songs2.size() > 0) {
                myModel.put("finSong", songs2.size());
                myModel.put("canciones", songs2);
                request.getSession().setAttribute("finCanciones", songs2.size());
                request.getSession().setAttribute("canciones", songs2);
            }
            List<VideoBO> videos = videoDAO.getVideos();
            List<VideoBO> videos2 = new ArrayList<VideoBO>();
            for (int i = 0; i < videos.size(); i++) {
                if (videos.get(i).getGrupo().getNombre().equals(grupo)) videos2.add(videos.get(i));
            }
            if (videos2.size() > 0) {
                myModel.put("finVideo", videos2.size());
                myModel.put("videos", videos2);
                request.getSession().setAttribute("finVideos", videos2.size());
                request.getSession().setAttribute("videos", videos2);
            }
        }
        if (request.getParameter("reproducirCancion") != null) {
            CancionBO cancion = cancionDAO.getCancion(Integer.parseInt(request.getParameter("idCancion")));
            cancion.setContador(cancion.getContador() + 1);
            cancionDAO.saveCancion(cancion);
            String usuario = (usuarioDAO.getUsuario(cancion.getUsuario().getId())).getUsuario();
            myModel.put("usuario", usuario);
            myModel.put("cancionReproducir", cancion);
            myModel.put("finSong", request.getSession().getAttribute("finCanciones"));
            myModel.put("canciones", request.getSession().getAttribute("canciones"));
            myModel.put("finVideo", request.getSession().getAttribute("finVideos"));
            myModel.put("videos", request.getSession().getAttribute("videos"));
            return new ModelAndView("grupos", "modelo", myModel);
        }
        if (request.getParameter("reproducirVideo") != null) {
            VideoBO video = videoDAO.getVideo(Integer.parseInt(request.getParameter("idVideo")));
            video.setContador(video.getContador() + 1);
            videoDAO.saveVideo(video);
            String usuario = (usuarioDAO.getUsuario(video.getUsuario().getId())).getUsuario();
            myModel.put("usuario", usuario);
            myModel.put("videoReproducir", video);
            myModel.put("finSong", request.getSession().getAttribute("finCanciones"));
            myModel.put("canciones", request.getSession().getAttribute("canciones"));
            myModel.put("finVideo", request.getSession().getAttribute("finVideos"));
            myModel.put("videos", request.getSession().getAttribute("videos"));
            return new ModelAndView("grupos", "modelo", myModel);
        }
        return new ModelAndView("grupos", "modelo", myModel);
    }
}
