package org.elf.weblayer;

/**
 *
 * @author  <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class Paths {

    /**
     * Contiene el protocolo, host, puerto y conexto de la aplicaci�n
     * @return Contiene el protocolo, host, puerto y conexto de la aplicaci�n
     * Ej: http://192.168.1.1:8080/myapp
     */
    private String _absolutePath;

    /**
     * Es el valor de 'request.getContextPath()'
     * Tal y como est� definido en la especificaci�n de J2EE
     * Ej: /myapp
     */
    private String _contextPath;

    /**
     * Es el valor de 'request.getRequestURI()'
     * Tal y como est� definido en la especificaci�n de J2EE 
     * Ej: /myapp/controls/img/void.img
     */
    private String _requestURI;

    /**
     * Al 'request.getRequestURI()' le eliminamos el prefijo 'request.getContextPath()'
     * Ej: /controls/img/void.img
     */
    private String _relativeURI;

    /**
     * URL raiz de la aplicaci�n en el sistema de ficheros
     * Es el valor de 'servletContext.getRealPath("/")'
     * Tal y como est� definido en la especificaci�n de J2EE
     * Obtiene el path real (en el sistema de ficheros) de la raiz de la aplicaci�n  
     */
    private String _applicationRootRealPath;

    private String _controlsImagePath;

    /** Create una nueva instancia de la clase Paths */
    public Paths(String absolutePath, String contextPath, String requestURI, String relativeURI, String applicationRootRealPath, String controlsImagePath) {
        _absolutePath = absolutePath;
        _contextPath = contextPath;
        _requestURI = requestURI;
        _relativeURI = relativeURI;
        _applicationRootRealPath = applicationRootRealPath;
        _controlsImagePath = controlsImagePath;
    }

    /**
     * Contiene el protocolo, host, puerto y conexto de la aplicaci�n
     * @return Contiene el protocolo, host, puerto y conexto de la aplicaci�n
     * Ej: http://192.168.1.1:8080/myapp
     */
    public String getAbsolutePath() {
        return _absolutePath;
    }

    /**
     * Contiene el mismo valor que {@link javax.servlet.http.HttpServletRequest#getContextPath() }
     * @return String con el Path del contexto de la aplicaci�n. Es decir
     * donde est� instalada la aplicaci�n
     * Ej: /myapp
     */
    public String getContextPath() {
        return _contextPath;
    }

    /**
     * Contiene el mismo valor que {@link javax.servlet.http.HttpServletRequest#getRequestURI() }
     * @return Obtenet el URI de la petici�n que se ha hecho.
     * El mas o menos como la URL que se ha pedido pero sin
     * el protocolo y el Host (tampoco est� el puerto, etc) 
     * Ej: /myapp/controls/img/void.img
     */
    public String getRequestURI() {
        return _requestURI;
    }

    /**
     * Es igual al getRequestURI pero quitandole la parte de getContextPath
     * Es decir de la URL que ha lanzado el usuario se le quita
     * toda la parte primera desde el protocolo hasta la parte que
     * incluye el path donde est� instala la aplicaci�n
     * @return URI de la petici�n al que se le ha quitado el
     * path de donde est� instalala la aplicaci�n
     * Ej: /controls/img/void.img
     */
    public String getRelativeURI() {
        return _relativeURI;
    }

    /**
     * Contiene el mismo valor que {@link javax.servlet.ServletContext#getRealPath(java.lang.String) }
     * pero llamado con el valor de "/"
     * @return Obtiene el path real (en el sistema de ficheros) de la raiz de la aplicaci�n
     */
    public String getApplicationRootRealPath() {
        return _applicationRootRealPath;
    }

    /**
     * Contiene el Path de donde buscar la imagenes de los controles
     * Lo normal es que sea /app/controls/img/nombreImagen
     * Siendo /app igual a getContextPath
     */
    public String getControlsImagePath() {
        return _controlsImagePath;
    }
}
