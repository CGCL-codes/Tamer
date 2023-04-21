package com.museum4j.negocio;

import java.util.*;
import java.io.*;
import com.museum4j.negocio.GoogleMapManager;
import com.museum4j.dao.MuseoDAO;
import com.museum4j.dao.IdiomaDAO;
import com.museum4j.dao.PlantillaDAO;
import com.museum4j.dao.DirectorDAO;
import com.museum4j.modelo.*;
import com.museum4j.utils.*;
import org.apache.log4j.Logger;

public interface MuseoManager {

    public void setMuseoDAO(MuseoDAO dao);

    public void setIdiomaDAO(IdiomaDAO dao);

    public void setPlantillaDAO(PlantillaDAO dao);

    public void setDirectorDAO(DirectorDAO dao);

    public void setGoogleMapManager(GoogleMapManager googleMapManager);

    public Museo getMuseo(int idMuseo);

    public Museo guardarMuseo(Museo m);

    public Museo guardarMuseo(int idMuseo, String nombre, String direccion, String localidad, String telefono, String fax, String email);

    public Museo guardarMuseo(String nombre, String usuario, String nombrePlantilla, String direccion, String localidad, String telefono, String fax, String email, String rutaMultimedia);

    public Museo guardarOpcionesGenerales(int idMuseo, boolean publicarEnFlickr, boolean publicarEnPicasa, boolean publicarEnGoogleMaps, double gisCoordenadaX, double gisCoordenadaY, double gisCoordenadaZ);

    public Museo getMuseo(String usuario);

    public void publicar(int idMuseo);

    public void despublicar(int idMuseo);

    public List<Museo> getMuseos();

    public List<Museo> getMuseosPublicados();

    public List getEstadisticasMuseos(String rutaAplicacion);

    public EstadisticaMuseo getEstadisticaMuseo(int idMuseo, String rutaAplicacion);

    public Plantilla getPlantilla(int idMuseo);

    public List<Comentario> getComentarios(int idMuseo);

    public void guardarComentario(int idMuseo, String texto);

    public void guardarComentario(Comentario comentario);

    public void guardarMensaje(int idMuseo, String texto);

    public List getMensajes();

    public List getMensajes(String loginDirector);

    public List<IdiomaMuseo> getIdiomas(int idMuseo);

    public IdiomaMuseo getIdioma(int idMuseo, String codigoIdioma);

    public List<IdiomaMuseo> getIdiomasOrdenados(int idMuseo);

    public Set getMultimedias(int idMuseo);

    public IdiomaMuseo getIdiomaPrincipal(int idMuseo);

    public void setIdiomaPrincipal(int idMuseo, String codigoIdioma);

    public Set getIdiomasComplementarios(int idMuseo);

    public void agregarIdiomaComplementario(int idMuseo, String codigoIdioma);

    public void borrarIdioma(int idMuseo, String idioma);

    public void setPlantilla(int idMuseo, String codigoPlantilla);

    public void borrarMuseo(int idMuseo);

    public Museo cambiarNombreMuseo(int idMuseo, String nuevoNombre);

    public Museo cambiarUrlPublicidad(int idMuseo, String nuevaPublicidad);

    public Museo cambiarRss(int idMuseo, boolean publicarRSS);

    public void actualizarSitemapGoogleMap(String rutaRaiz);

    public Museo cambiarDublinCore(int idMuseo, boolean publicarXmlDublinCoreObra, boolean publicarDublinCoreWS);

    public Obra getUnaObraEjemplo(int idMuseo);
}
