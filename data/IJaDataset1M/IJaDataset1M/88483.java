package com.museum4j.negocio;

import java.util.*;
import java.io.*;
import com.museum4j.negocio.MuseoManager;
import com.museum4j.dao.ContenidoDAO;
import com.museum4j.dao.MuseoDAO;
import com.museum4j.dao.MenuDAO;
import com.museum4j.modelo.*;
import com.museum4j.utils.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ContenidoManagerImp implements ContenidoManager {

    private Logger logger = Logger.getLogger(this.getClass());

    private MuseoManager museoManager;

    public void setMuseoManager(MuseoManager museoManager) {
        this.museoManager = museoManager;
    }

    private MultimediaManager multimediaManager;

    public void setMultimediaManager(MultimediaManager multimediaManager) {
        this.multimediaManager = multimediaManager;
    }

    private ContenidoDAO contenidoDAO;

    public void setContenidoDAO(ContenidoDAO dao) {
        this.contenidoDAO = dao;
    }

    private MuseoDAO museoDAO;

    public void setMuseoDAO(MuseoDAO dao) {
        this.museoDAO = dao;
    }

    private MovilManager movilManager;

    public void setMovilManager(MovilManager m) {
        this.movilManager = m;
    }

    /**
     * Obtiene los contenidos del museo
     * @param idMuseo Museo
     * @since Java1.5
     */
    public List<Contenido> getContenidos(int idMuseo) {
        return contenidoDAO.getContenidos(new Integer(idMuseo));
    }

    /**
     * Obtiene los contenidos del museo por tipo
     * @param idMuseo Museo
     * @param tipoContenido Tipo de contenidos solicitado
     * @since Java1.5
     */
    public List<Contenido> getContenidos(int idMuseo, String tipoContenido) {
        return contenidoDAO.getContenidos(new Integer(idMuseo), tipoContenido);
    }

    /**
     * Obtiene los recorridos usados para la vista para el movil
     * @since Java1.5
     */
    public List<Recorrido> getRecorridosParaMovil(int idMuseo) {
        logger.debug("GetRecorridosParaMovil del museo " + idMuseo);
        List<Contenido> contenidos = getContenidos(idMuseo, "'" + TipoContenido.RECORRIDO + "'");
        List<Recorrido> recorridos = new ArrayList<Recorrido>();
        Iterator iterador = contenidos.iterator();
        Recorrido r;
        while (iterador.hasNext()) {
            r = (Recorrido) iterador.next();
            logger.debug("A�adir a recorridos para movil? " + r.getIdContenido() + r.getEnVistaMovil());
            if (r.getEnVistaMovil() == true) recorridos.add(r);
        }
        return recorridos;
    }

    public List<Obra> getObrasRecientes() {
        List<Contenido> obras = contenidoDAO.getContenidos("'" + TipoContenido.OBRA + "'");
        Collections.sort(obras, new ContenidoComparator());
        List<Obra> obrasRecientes = new ArrayList<Obra>();
        for (int i = 0; i < 10 && i < obras.size(); i++) obrasRecientes.add((Obra) obras.get(i));
        return obrasRecientes;
    }

    /**
     * Obtiene la lista de contenidos para el museo dado
     * que se acaban de crear o modificar
     */
    public List<Contenido> getContenidosRecienModificados(int idMuseo) {
        List<Contenido> contenidos = getContenidos(idMuseo);
        Collections.sort(contenidos, new ContenidoComparator());
        int nContenidos = 10;
        if (contenidos.size() < nContenidos) nContenidos = contenidos.size();
        return contenidos.subList(0, nContenidos);
    }

    /**
     * Obtiene la lista de contenidos para el museo dado
     * que se han visitado mas recientemente
     */
    public List<Contenido> getContenidosRecientes(int idMuseo) {
        List<Contenido> contenidos = getContenidos(idMuseo);
        Collections.sort(contenidos, new ContenidoFechaHitComparator());
        int nContenidos = 0;
        List<Contenido> contenidosRecientes = new ArrayList<Contenido>();
        for (Contenido c : contenidos) {
            if (c.getFechaUltimoHit() != null) {
                contenidosRecientes.add(c);
                nContenidos++;
            }
            if (nContenidos == 10) break;
        }
        return contenidosRecientes;
    }

    /**
     * Obtiene la lista de contenidos para el museo dado
     * que mas se han visitado
     */
    public List<Contenido> getContenidosPopulares(int idMuseo) {
        List<Contenido> contenidos = getContenidos(idMuseo);
        Collections.sort(contenidos, new ContenidoNHitsComparator());
        int nContenidos = 0;
        List<Contenido> contenidosPopulares = new ArrayList<Contenido>();
        for (Contenido c : contenidos) {
            if (c.getHits() > 0) {
                contenidosPopulares.add(c);
                nContenidos++;
            }
            if (nContenidos == 10) break;
        }
        return contenidosPopulares;
    }

    /**
     * Compone un enlace interno de un contenido
     */
    public EnlaceInterno componerEnlaceDeContenido(int idMuseo, Contenido c) {
        EnlaceInterno enlace = new EnlaceInterno();
        enlace.setUrl(Directorios.DIR_BASE + "/VerContenido.html?museo=" + idMuseo + "&item=" + c.getIdContenido());
        enlace.setTitulo(c.getTipo() + ":: " + c.getTituloPrincipal());
        return enlace;
    }

    /**
     * Compone un enlace interno de un objeto multimedia
     */
    public EnlaceInterno componerEnlaceDeMultimedia(int idMuseo, Multimedia m) {
        EnlaceInterno enlace = new EnlaceInterno();
        enlace.setUrl("../" + Directorios.DIR_MULTIMEDIA + "/" + idMuseo + "/" + m.getNombreFichero());
        enlace.setTitulo(m.getNombreFichero());
        return enlace;
    }

    /**
     * Analiza un texto buscando posibles enlaces a objetos multimedia del
     * propio museo
     */
    public void asociarMultimediaEnTexto(Texto texto) {
        List<MultimediaTexto> multimedias = buscarMultimediaEnEtiquetasImg(texto);
        texto.setMultimedias(multimedias);
    }

    private List<MultimediaTexto> buscarMultimediaEnEtiquetasImg(Texto texto) {
        logger.info("Buscando multimedia en tags img...");
        List<String> etiquetasImg = null;
        try {
            etiquetasImg = HtmlManager.buscarImgTags(texto.getDescripcion());
        } catch (Exception e) {
            logger.error("No se pueden recuperar las etiquetas html img");
            return null;
        }
        List<MultimediaTexto> multimedias = new ArrayList<MultimediaTexto>();
        for (String etiquetaImg : etiquetasImg) {
            Multimedia m = multimediaManager.buscarMultimediaPorRuta(etiquetaImg, texto.getContenido().getMuseo().getId());
            if (m != null) {
                MultimediaTexto mt = new MultimediaTexto();
                mt.setTexto(texto);
                mt.setMultimedia(m);
                multimedias.add(mt);
            }
        }
        return multimedias;
    }

    public List getObrasMaestras(int idMuseo) {
        List contenidos = getContenidos(idMuseo, "'" + TipoContenido.OBRA + "'");
        List obrasMaestras = new ArrayList();
        Iterator iterador = contenidos.iterator();
        Obra obra;
        while (iterador.hasNext()) {
            obra = (Obra) iterador.next();
            logger.info("A�adir a maestras? " + obra.getIdContenido() + obra.getEsObraMaestra());
            if (obra.getEsObraMaestra() == true) obrasMaestras.add(obra);
        }
        return obrasMaestras;
    }

    public Set getCategorias() {
        return convertirArray2Set(TipoContenido.Categorias);
    }

    public void borrarContenido(int idContenido) {
        contenidoDAO.borrarContenido(new Integer(idContenido));
    }

    /**
     * Obtener los contenidos del museo que se pueden referenciar desde 
     * otros contenidos (eventos, de momento)
     */
    public List getContenidosReferenciables(int idMuseo) {
        Integer museo = new Integer(idMuseo);
        List contenidos = contenidoDAO.getContenidos(museo);
        return contenidos;
    }

    /**
     * Obtener los titulos (idContenido=>titulo en el idioma principal que se pueden 
     * referenciar desde otros contenidos:
     *  - EVENTOS 
     */
    public Map getTitulosReferenciables(int idMuseo) {
        Integer museo = new Integer(idMuseo);
        List contenidos = contenidoDAO.getContenidos(museo);
        Map titulos = new HashMap();
        Iterator iterador = contenidos.iterator();
        Contenido c;
        while (iterador.hasNext()) {
            c = (Contenido) iterador.next();
            logger.info("id: " + c.getIdContenido() + " titulo: " + c.getTituloPrincipal());
            if (c.getTipo().compareTo(TipoContenido.EVENTO) != 0) titulos.put(new Integer(c.getIdContenido()), c.getTituloPrincipal());
        }
        logger.info(titulos);
        return titulos;
    }

    public Contenido getContenidoDePortada(int idMuseo) {
        Contenido contenido = null;
        try {
            contenido = contenidoDAO.getContenidoDePortada(new Integer(idMuseo));
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return contenido;
    }

    public void setContenidoDePortada(int idMuseo, int idContenido) {
        Contenido portada;
        try {
            portada = getContenidoDePortada(idMuseo);
        } catch (Exception e) {
            logger.error(e.toString());
            portada = null;
        }
        if (portada != null) {
            portada.setEsPortada(false);
            contenidoDAO.actualizarContenido(portada);
        }
        Contenido contenido = getContenido(idContenido);
        contenido.setEsPortada(true);
        contenidoDAO.actualizarContenido(contenido);
    }

    public Contenido getContenido(int idContenido) {
        Contenido contenido = null;
        try {
            contenido = contenidoDAO.getContenido(new Integer(idContenido));
        } catch (Exception e) {
            return null;
        }
        Set textos = contenido.getTextos();
        int idMuseo = contenido.getMuseo().getId();
        List<IdiomaMuseo> idiomas = museoManager.getIdiomas(idMuseo);
        for (IdiomaMuseo i : idiomas) {
            String codigo = i.getIdiomaMuseoId().getIdioma().getCodigo();
            Texto texto = contenido.getTextoPorIdioma(codigo);
            if (texto != null) logger.debug(texto.getTitulo() + "|" + texto.getDescripcion()); else {
                logger.info("El idioma " + codigo + " no tiene -> Se a�aden");
                texto = new Texto();
                texto.setTitulo("");
                texto.setDescripcion("");
                texto.setContenido(contenido);
                texto.setIdioma(i.getIdiomaMuseoId().getIdioma());
                textos.add(texto);
            }
        }
        return contenido;
    }

    /**
    * Crear contenido de tipo generico
    */
    public Contenido crearContenido(Contenido contenido, int idMuseo, String[] codigoIdioma, String[] titulo, String[] descripcion, String estiloCSS) {
        contenido.setFechaCreacion(new Date());
        contenido.setFechaUltimoCambio(new Date());
        contenido.setEstiloCSS(estiloCSS);
        contenido.setMuseo(museoDAO.getMuseo(new Integer(idMuseo)));
        Set textos = new HashSet();
        for (int i = 0; i < codigoIdioma.length; i++) {
            Texto texto = new Texto();
            Idioma idioma = new Idioma();
            idioma.setCodigo(codigoIdioma[i]);
            texto.setContenido(contenido);
            texto.setIdioma(idioma);
            if (titulo[i].length() > 0) {
                texto.setTitulo(titulo[i]);
                texto.setDescripcion(descripcion[i]);
            } else {
                texto.setTitulo(" -- Sin titulo (" + codigoIdioma[i] + ")");
            }
            asociarMultimediaEnTexto(texto);
            textos.add(texto);
        }
        contenido.setTextos(textos);
        return contenido;
    }

    /**
     * Actualiza el contador de visitas para un contenido
     */
    public void visitarContenido(Contenido contenido) {
        contenido.setHits(contenido.getHits() + 1);
        contenido.setFechaUltimoHit(new Date(System.currentTimeMillis()));
        logger.info("actualizando hits para el contenido " + contenido.getIdContenido() + ": " + contenido.getHits());
        contenidoDAO.actualizarContenido(contenido);
    }

    public Contenido guardarContenido(int idMuseo, String[] codigoIdioma, String[] titulo, String[] descripcion, String estiloCSS) {
        Contenido contenido = new Contenido();
        contenido = crearContenido(contenido, idMuseo, codigoIdioma, titulo, descripcion, estiloCSS);
        contenido.setTipo(TipoContenido.GENERICO);
        contenidoDAO.guardarContenido(contenido);
        return contenido;
    }

    public Autor guardarAutor(int idMuseo, String[] codigoIdioma, String[] titulo, String[] descripcion, String estiloCSS, String nombre, String apellidos, Date fechaNacimiento, Date fechaFallecimiento, String corrienteArtistica, String pais) {
        logger.warn("hay " + codigoIdioma.length);
        Autor autor = new Autor();
        for (int i = 0; i < titulo.length; i++) {
            if ((titulo[i] == null) || (titulo[i].length() == 0)) titulo[i] = nombre + " " + apellidos;
        }
        autor = (Autor) crearContenido(autor, idMuseo, codigoIdioma, titulo, descripcion, estiloCSS);
        autor.setTipo(TipoContenido.AUTOR);
        autor.setNombre(nombre);
        autor.setApellidos(apellidos);
        autor.setFechaNacimiento(fechaNacimiento);
        autor.setFechaFallecimiento(fechaFallecimiento);
        autor.setCorrienteArtistica(corrienteArtistica);
        autor.setPais(pais);
        mostrarTextos(autor.getTextos());
        contenidoDAO.guardarContenido(autor);
        return autor;
    }

    public Obra guardarObra(int idMuseo, String[] codigoIdioma, String[] titulo, String[] descripcion, boolean esObraMaestra, Autor autor, String[] dcTitles, String[] dcCreators, String[] dcSubjects, String[] dcPublishers, String[] dcContributors, Date dcDate, String dcType, String dcFormat, String[] dcIdentifiers, String[] dcSources, String[] dcLanguages, String[] dcCoverages, String[] dcRights) {
        logger.info("guardando obra");
        Obra obra = new Obra();
        if ((dcTitles.length > 0) && (dcTitles[0].length() > 0)) for (int i = 0; i < titulo.length; i++) titulo[i] = dcTitles[0];
        obra = (Obra) crearContenido(obra, idMuseo, codigoIdioma, titulo, descripcion, "");
        obra.setEsObraMaestra(esObraMaestra);
        obra.setAutor(autor);
        obra.setTipo(TipoContenido.OBRA);
        Set titles = convertirArray2Set(dcTitles);
        logger.info("Guardado hasta titles");
        Set creators = convertirArray2Set(dcCreators);
        logger.info("Guardado hasta creators");
        Set subjects = convertirArray2Set(dcSubjects);
        logger.info("Guardado hasta subjects");
        Set publishers = convertirArray2Set(dcPublishers);
        Set contributors = convertirArray2Set(dcContributors);
        Set identifiers = convertirArray2Set(dcIdentifiers);
        Set sources = convertirArray2Set(dcSources);
        Set languages = convertirArray2Set(dcLanguages);
        Set coverages = convertirArray2Set(dcCoverages);
        Set rights = convertirArray2Set(dcRights);
        obra.setTitle(titles);
        obra.setCreator(creators);
        obra.setSubject(subjects);
        obra.setPublisher(publishers);
        obra.setContributor(contributors);
        logger.info("la fecha q se guarda es " + dcDate);
        obra.setDate(dcDate);
        obra.setType(dcType);
        obra.setFormat(dcFormat);
        obra.setIdentifier(identifiers);
        obra.setSource(sources);
        obra.setLanguage(languages);
        obra.setCoverage(coverages);
        obra.setRights(rights);
        logger.info("y a guardar con el DAO contenido");
        contenidoDAO.guardarContenido(obra);
        return obra;
    }

    public Evento guardarEvento(int idMuseo, String[] codigoIdioma, String[] titulo, String[] descripcion, Date fechaInicio, Date fechaFin, Boolean esActivo, Contenido contenidoReferente) {
        logger.info("guardando evento");
        Evento evento = new Evento();
        evento = (Evento) crearContenido(evento, idMuseo, codigoIdioma, titulo, descripcion, "");
        evento.setTipo(TipoContenido.EVENTO);
        evento.setFechaInicio(fechaInicio);
        evento.setFechaFin(fechaFin);
        evento.setEsActivo(esActivo);
        evento.setContenidoReferente(contenidoReferente);
        contenidoDAO.guardarContenido(evento);
        logger.info(evento);
        return evento;
    }

    public InfoLocalizacion guardarInfoLocalizacion(int idMuseo, String[] codigoIdioma, String[] titulo, String[] descripcion) {
        logger.warn("==============================");
        logger.warn("hay " + codigoIdioma.length);
        InfoLocalizacion info = new InfoLocalizacion();
        info = (InfoLocalizacion) crearContenido(info, idMuseo, codigoIdioma, titulo, descripcion, "");
        info.setTipo(TipoContenido.UBICACION);
        contenidoDAO.guardarContenido(info);
        return info;
    }

    public InfoMuseo guardarInfoMuseo(int idMuseo, String[] codigoIdioma, String[] titulo, String[] descripcion) {
        InfoMuseo info = new InfoMuseo();
        info = (InfoMuseo) crearContenido(info, idMuseo, codigoIdioma, titulo, descripcion, "");
        info.setTipo(TipoContenido.MUSEO);
        contenidoDAO.guardarContenido(info);
        return info;
    }

    public Novedad guardarNovedad(int idMuseo, String[] codigoIdioma, String[] titulo, String[] descripcion) {
        logger.warn("==============================");
        logger.warn("hay " + codigoIdioma.length);
        Novedad novedad = new Novedad();
        novedad = (Novedad) crearContenido(novedad, idMuseo, codigoIdioma, titulo, descripcion, "");
        novedad.setTipo(TipoContenido.NOVEDAD);
        contenidoDAO.guardarContenido(novedad);
        return novedad;
    }

    public Recorrido guardarRecorrido(int idMuseo, String[] codigoIdioma, String[] titulo, String[] descripcion, boolean esParaMovil) {
        Recorrido recorrido = new Recorrido();
        recorrido = (Recorrido) crearContenido(recorrido, idMuseo, codigoIdioma, titulo, descripcion, "");
        recorrido.setEnVistaMovil(esParaMovil);
        recorrido.setTipo(TipoContenido.RECORRIDO);
        contenidoDAO.guardarContenido(recorrido);
        return recorrido;
    }

    public Recorrido guardarRecorrido(int idMuseo, String[] codigoIdioma, String[] titulo, String[] descripcion) {
        return guardarRecorrido(idMuseo, codigoIdioma, titulo, descripcion, false);
    }

    public Parada guardarParada(int idRecorrido, int idObra) {
        Recorrido recorrido = (Recorrido) getContenido(idRecorrido);
        Obra obra = (Obra) getContenido(idObra);
        Parada parada = new Parada();
        parada.setRecorrido(recorrido);
        parada.setObra(obra);
        recorrido.getParadas().add(parada);
        contenidoDAO.guardarContenido(recorrido);
        return parada;
    }

    /**			METODOS ACTUALIZAR CONTENIDO (y derivados)	     */
    public Novedad actualizarNovedad(int idMuseo, int idContenido, String[] codigoIdioma, String[] titulo, String[] descripcion, String estiloCSS) {
        return (Novedad) actualizarContenido(idMuseo, idContenido, codigoIdioma, titulo, descripcion, estiloCSS);
    }

    public InfoLocalizacion actualizarInfoLocalizacion(int idMuseo, int idContenido, String[] codigoIdioma, String[] titulo, String[] descripcion, String estiloCSS) {
        return (InfoLocalizacion) actualizarContenido(idMuseo, idContenido, codigoIdioma, titulo, descripcion, estiloCSS);
    }

    public InfoMuseo actualizarInfoMuseo(int idMuseo, int idContenido, String[] codigoIdioma, String[] titulo, String[] descripcion, String estiloCSS) {
        return (InfoMuseo) actualizarContenido(idMuseo, idContenido, codigoIdioma, titulo, descripcion, estiloCSS);
    }

    public Recorrido actualizarRecorrido(int idMuseo, int idContenido, String[] codigoIdioma, String[] titulo, String[] descripcion, String estiloCSS, Boolean esRecorridoMovil) {
        logger.info("ACtualizando recorrido, movil?" + esRecorridoMovil);
        Recorrido recorrido = (Recorrido) actualizarContenido(idMuseo, idContenido, codigoIdioma, titulo, descripcion, estiloCSS, false);
        recorrido.setEnVistaMovil(esRecorridoMovil);
        contenidoDAO.actualizarContenido(recorrido);
        return recorrido;
    }

    public Contenido actualizarContenido(int idMuseo, int idContenido, String[] codigoIdioma, String[] titulo, String[] descripcion, String estiloCSS) {
        return actualizarContenido(idMuseo, idContenido, codigoIdioma, titulo, descripcion, estiloCSS, true);
    }

    public Contenido actualizarContenido(int idMuseo, int idContenido, String[] codigoIdioma, String[] titulo, String[] descripcion, String estiloCSS, boolean almacenar) {
        Contenido contenido = (Contenido) getContenido(idContenido);
        contenido.setFechaUltimoCambio(new Date(System.currentTimeMillis()));
        contenido.setEstiloCSS(estiloCSS);
        Set textos = contenido.getTextos();
        for (int i = 0; i < codigoIdioma.length; i++) {
            if (titulo[i].length() > 0) {
                Texto texto = contenido.getTextoPorIdioma(codigoIdioma[i]);
                texto.setTitulo(titulo[i]);
                texto.setDescripcion(descripcion[i]);
                asociarMultimediaEnTexto(texto);
            }
        }
        if (almacenar) contenidoDAO.actualizarContenido(contenido);
        return contenido;
    }

    public Autor actualizarAutor(int idMuseo, int idContenido, String[] codigoIdioma, String[] titulo, String[] descripcion, String estiloCSS, String nombre, String apellidos, Date fechaNacimiento, Date fechaFallecimiento, String corrienteArtistica, String pais) {
        Autor autor = (Autor) getContenido(idContenido);
        autor.setFechaUltimoCambio(new Date(System.currentTimeMillis()));
        autor.setEstiloCSS(estiloCSS);
        autor.setNombre(nombre);
        autor.setApellidos(apellidos);
        autor.setFechaNacimiento(fechaNacimiento);
        autor.setFechaFallecimiento(fechaFallecimiento);
        autor.setCorrienteArtistica(corrienteArtistica);
        autor.setPais(pais);
        Set textos = autor.getTextos();
        for (int i = 0; i < codigoIdioma.length; i++) {
            Texto texto = autor.getTextoPorIdioma(codigoIdioma[i]);
            texto.setTitulo(nombre + " " + apellidos);
            texto.setDescripcion(descripcion[i]);
            asociarMultimediaEnTexto(texto);
            logger.info("texto cazado en " + texto.getIdioma().getCodigo() + ":" + texto.getTitulo() + "," + texto.getDescripcion());
        }
        contenidoDAO.actualizarContenido(autor);
        return autor;
    }

    public Evento actualizarEvento(int idMuseo, int idContenido, String[] codigoIdioma, String[] titulo, String[] descripcion, Date fechaInicio, Date fechaFin, Boolean esActivo, Contenido contenidoReferente, String estiloCSS) {
        logger.info("Actualizando evento...");
        Evento evento = (Evento) actualizarContenido(idMuseo, idContenido, codigoIdioma, titulo, descripcion, estiloCSS, false);
        evento.setFechaInicio(fechaInicio);
        evento.setFechaFin(fechaFin);
        evento.setEsActivo(esActivo);
        evento.setContenidoReferente(contenidoReferente);
        contenidoDAO.actualizarContenido(evento);
        return evento;
    }

    public Obra actualizarObra(int idContenido, String[] codigoIdioma, String[] titulo, String[] descripcion, String estiloCSS, boolean esObraMaestra, Autor autor, String[] dcTitles, String[] dcCreators, String[] dcSubjects, String[] dcPublishers, String[] dcContributors, Date dcDate, String dcType, String dcFormat, String[] dcIdentifiers, String[] dcSources, String[] dcLanguages, String[] dcCoverages, String[] dcRights) {
        logger.info("Actualizando obra");
        Obra obra = (Obra) getContenido(idContenido);
        obra.setFechaUltimoCambio(new Date(System.currentTimeMillis()));
        obra.setEstiloCSS(estiloCSS);
        obra.setEsObraMaestra(esObraMaestra);
        obra.setAutor(autor);
        if ((dcTitles.length > 0) && (dcTitles[0].length() > 0)) for (int i = 0; i < titulo.length; i++) titulo[i] = dcTitles[0];
        Set textos = obra.getTextos();
        for (int i = 0; i < codigoIdioma.length; i++) {
            if (titulo[i].length() > 0) {
                Texto texto = obra.getTextoPorIdioma(codigoIdioma[i]);
                texto.setTitulo(titulo[i]);
                texto.setDescripcion(descripcion[i]);
                logger.info("texto cazado en " + texto.getIdioma().getCodigo() + ":" + texto.getTitulo() + "," + texto.getDescripcion());
                asociarMultimediaEnTexto(texto);
            }
        }
        Set titles = convertirArray2Set(dcTitles);
        Set creators = convertirArray2Set(dcCreators);
        Set subjects = convertirArray2Set(dcSubjects);
        Set publishers = convertirArray2Set(dcPublishers);
        Set contributors = convertirArray2Set(dcContributors);
        Set identifiers = convertirArray2Set(dcIdentifiers);
        Set sources = convertirArray2Set(dcSources);
        Set languages = convertirArray2Set(dcLanguages);
        Set coverages = convertirArray2Set(dcCoverages);
        Set rights = convertirArray2Set(dcRights);
        logger.info("dcRights");
        mostrarArray(dcRights);
        obra.setTitle(titles);
        obra.setCreator(creators);
        obra.setSubject(subjects);
        obra.setPublisher(publishers);
        obra.setContributor(contributors);
        obra.setDate(dcDate);
        obra.setType(dcType);
        obra.setFormat(dcFormat);
        obra.setIdentifier(identifiers);
        obra.setSource(sources);
        obra.setLanguage(languages);
        obra.setCoverage(coverages);
        obra.setRights(rights);
        contenidoDAO.actualizarContenido(obra);
        return obra;
    }

    /**
     * Reordena las paradas de un contenido de tipo RECORRIDO 
     */
    public Recorrido reordenarParadas(int idRecorrido, int[] ordenParadas) {
        Recorrido recorrido = (Recorrido) getContenido(idRecorrido);
        List paradas = recorrido.getParadas();
        List listaOrdenada = new LinkedList();
        logger.info("Reordenando lista...");
        if (paradas.toArray().length != ordenParadas.length) logger.warn("\tlongitudes distintas! Saliendo..."); else if (paradas.isEmpty()) logger.info("\tlista vacia. Saliendo..."); else {
            for (int i = 0; i < ordenParadas.length; i++) {
                listaOrdenada.add((Parada) paradas.get(ordenParadas[i] - 1));
            }
            mostrarListaParadas(paradas);
            paradas.clear();
        }
        mostrarListaParadas(listaOrdenada);
        recorrido.setParadas(listaOrdenada);
        contenidoDAO.actualizarContenido(recorrido);
        return recorrido;
    }

    public Recorrido quitarParadaDeRecorrido(int idRecorrido, int idObra, int posicion) {
        logger.info("Quitar la parada " + posicion + "(Id.Obra: " + idObra + ")" + " del recorrido " + idRecorrido);
        Recorrido recorrido = (Recorrido) getContenido(idRecorrido);
        List paradas = recorrido.getParadas();
        if (paradas.isEmpty()) logger.info("No hay paradas"); else {
            Parada parada = (Parada) paradas.get(posicion);
            if (parada.getObra().getIdContenido() == idObra) {
                logger.info("Borrando parada en posicion " + posicion);
                paradas.remove(posicion);
                recorrido.setParadas(paradas);
                contenidoDAO.actualizarContenido(recorrido);
            } else logger.info("No coincide la posicion y el idObra a borrar");
        }
        return recorrido;
    }

    /**
     * Genera una vista movil para un recorrido dado
     */
    public HashMap generarMuseoMovil(int idMuseo, String rutaRaiz, int idRecorrido, String codigoIdioma) throws IOException {
        logger.info("Creando estructura base");
        Recorrido recorrido = (Recorrido) getContenido(idRecorrido);
        IdiomaMuseo idioma = museoManager.getIdioma(idMuseo, codigoIdioma);
        if ((recorrido == null) || (idioma == null)) {
            logger.warn("Intentando generar vista movil vacia para el recorrido #" + idRecorrido + " / Idioma: " + codigoIdioma);
            return null;
        } else if (recorrido.getMuseo().getId() != idMuseo) {
            logger.warn("El museo del recorrido y de la sesion no coinciden");
            return null;
        }
        String rutaDestino = movilManager.crearBaseMuseoMovil(rutaRaiz, recorrido.toString());
        try {
            logger.info("Creando Forms Inicio");
            movilManager.crearFormsInicio(rutaRaiz, recorrido, rutaDestino, codigoIdioma);
            int nParada = 1;
            for (Parada parada : recorrido.getParadas()) {
                logger.info("Creando Form Parada");
                movilManager.crearParadaForm(rutaRaiz, parada, nParada++, recorrido.getParadas().size(), rutaDestino, codigoIdioma);
            }
            logger.info("Creando Fichero jar");
            String tituloJar = "vm_" + recorrido.getMuseo().getId() + "_Recorrido_" + recorrido.getIdContenido() + ".jar";
            String rutaFicheroJar = movilManager.crearJarMovil(rutaRaiz, rutaDestino, tituloJar);
            if (rutaFicheroJar.compareTo("") != 0) {
                recorrido.setRutaFicheroJar(tituloJar);
                recorrido.setFechaGeneracionJar(new Date());
                contenidoDAO.actualizarContenido(recorrido);
                HashMap resultado = new HashMap();
                resultado.put("ruta", Directorios.DIR_VISTA_MOVIL + "/" + tituloJar);
                resultado.put("fecha", recorrido.getFechaGeneracionJar());
                return resultado;
            } else return null;
        } catch (Exception e) {
            logger.fatal("Error generando vista movil: " + e.getMessage());
            return null;
        } finally {
            movilManager.borrarTemporales(rutaDestino);
        }
    }

    /**			METODOS PRIVADOS	   			     */
    private Set convertirArray2Set(String[] vector) {
        Set conjunto = new HashSet();
        if (vector != null) for (int i = 0; i < vector.length; i++) conjunto.add(vector[i]);
        return conjunto;
    }

    private void mostrarArray(Object[] vector) {
        if (vector != null) for (int i = 0; i < vector.length; i++) logger.info("#" + i + "- " + vector[i].toString());
    }

    private void mostrarTextos(Set textos) {
        logger.info("MOSTRANDO TEXTOS");
        Iterator iterador = textos.iterator();
        Texto texto;
        while (iterador.hasNext()) {
            texto = (Texto) iterador.next();
            logger.info(texto.getContenido().getIdContenido() + "|" + texto.getIdioma().getCodigo() + "\n" + texto.getTitulo() + "," + texto.getDescripcion());
        }
    }

    private void mostrarListaParadas(List lista) {
        logger.info("CONTENIDO LISTA");
        if (lista.isEmpty()) logger.info("Lista vacia"); else {
            ListIterator iterador = lista.listIterator();
            Parada actual;
            while (iterador.hasNext()) {
                actual = (Parada) iterador.next();
                logger.info(iterador.nextIndex() + ": " + actual.getObra().getIdContenido());
            }
        }
    }
}
