package jwikidump.entidad;

import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import jwikidump.entidad.mapeos.XMLPagina;

/**
 * Representa una Pagina de Wikipedia
 * @author Gonzalo <contacto@vagobit.com.ar>
 */
public class Pagina extends Entidad {

    private String titulo;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Revision revision;

    public Pagina() {
    }

    public Pagina(long id, String titulo, Revision revision) {
        this.setTitulo(titulo);
        this.setId(id);
        this.setRevision(revision);
    }

    /**
     * Constructor que permite instanciar una Pagina Obteniendo sus Atributos de un MAP.
     */
    public Pagina(Map<String, String> datos) {
        this(Long.parseLong(datos.get(XMLPagina.id.getNombreDelElemento())), datos.get(XMLPagina.titulo.getNombreDelElemento()), new Revision(datos));
    }

    public Revision getRevision() {
        return revision;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setRevision(Revision revision) {
        this.revision = revision;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return this.getTitulo() + " [" + this.getId() + "]\n" + this.getRevision().toString();
    }

    /**
     * Metodo que representa la Pagina Actual en Formato XML
     * @return
     */
    public String toXML() {
        return "\t<" + XMLPagina.nombreEnXML + ">\n" + "\t\t<" + XMLPagina.titulo + ">" + this.getTitulo() + "</" + XMLPagina.titulo + ">\n" + "\t\t<" + XMLPagina.id + ">" + this.getId() + "</" + XMLPagina.id + ">\n" + this.getRevision().toXML() + "\n" + "\t</" + XMLPagina.nombreEnXML + ">";
    }

    /**
     * Actualiza su Revision al publicado en el Sitio Wikipedia
     * @return True si se Actualizo, false en caso contrario
     */
    public boolean actualizar() {
        throw new UnsupportedOperationException();
    }

    /**
     * Determina si el Su Revision esta actualizada segunn el publicado en el Sitio Wikipedia
     */
    public boolean estaActualizado() {
        throw new UnsupportedOperationException();
    }

    /**
     * Indica si se trata de una Pagina de Desambiguación, de acuerdo al contenido de su Revision.
     * @return true en caso de tratarse de una Pagina de Desambiguacion
     * @see Revision#esPaginaDeDesambiguacion()
     */
    public boolean esPaginaDeDesambiguacion() {
        return this.getRevision().esPaginaDeDesambiguacion();
    }

    /**
     * Retorna la lista de Categorias a las cuales pertenece, indicada en el Contenido de la Revisión-
     * @return lista de String con el formato Categoría:xxxxxx
     * @see Revision#getCategorias() 
     */
    public List<String> getCategorias() {
        return this.getRevision().getCategorias();
    }

    /**
     * Metodo que devuelve un titulo "normalizado", el cual es una representacion se escribe en minusculas, sin acentos, ni signos de puntucion ni otros simbolos. Es util para el caso de quere comparar dos Titulos, ya sean propio de una Pagina o uno Apuntado (de Paginas Redireccionadas)
     * @param titulo
     * @return
     */
    public static String normalizarTitulo(String titulo) {
        String resultado = titulo.toLowerCase();
        resultado = resultado.replace('á', 'a');
        resultado = resultado.replace('é', 'e');
        resultado = resultado.replace('í', 'i');
        resultado = resultado.replace('ó', 'o');
        resultado = resultado.replace('ú', 'u');
        resultado = resultado.replace(".", "");
        resultado = resultado.replace(",", "");
        resultado = resultado.replace(":", "");
        resultado = resultado.replace("(", "");
        resultado = resultado.replace(")", "");
        resultado = resultado.replace("/", "");
        resultado = resultado.replace("-", "");
        return resultado;
    }
}
