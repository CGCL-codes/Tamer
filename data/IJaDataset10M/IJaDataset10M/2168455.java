package es.eside.deusto.pfc.kernel.ifc.dao;

import java.util.List;
import es.eside.deusto.pfc.kernel.impl.bo.VideoBO;

public interface VideoDAO extends DAO {

    public List getVideos();

    public VideoBO getVideo(int videoId);

    public void saveVideo(VideoBO video);

    public void removeVideo(int videoId);

    public List buscarVideos(String clave);
}
