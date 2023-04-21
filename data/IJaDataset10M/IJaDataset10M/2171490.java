package com.museum4j.negocio;

import java.io.*;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.apache.struts.upload.FormFile;
import com.museum4j.negocio.MuseoManager;
import com.museum4j.dao.MultimediaDAO;
import com.museum4j.dao.MuseoDAO;
import com.museum4j.modelo.*;
import com.museum4j.utils.*;
import org.apache.log4j.Logger;
import com.aetrion.flickr.*;
import com.aetrion.flickr.photosets.*;
import com.aetrion.flickr.photos.*;
import com.aetrion.flickr.uploader.*;
import com.aetrion.flickr.people.*;
import com.aetrion.flickr.auth.*;

/** 
 * Clase de comunicacion con Flickr
 */
public interface FlickrManager {

    /** Envia una imagen a Flickr por correo electr�nico */
    public void enviar(Multimedia mf, String ruta) throws Exception;

    /** Elimina una imagen de Flickr */
    public void eliminarImagen(String idImagen) throws Exception;

    public String buscarImagen(String nombreMuseo, String nombreImagen) throws Exception;
}
