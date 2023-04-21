package ramon.entity.file;

import java.io.Serializable;

/**
 * <p>
 * Representa una referencia a un archivo. El archivo puede estar tanto en el
 * sistema de archivos como en la memoria, o bien en algún otro tipo de soporte
 * como podría ser un campo blog en una base de datos relacional.
 * </p>
 * <p>
 * Es una clase de propósito general que puede usarse en cualquier contexto que
 * implique intercambio con archivos,sin embargo se utiliza especialmente en la
 * subida y bajada de archivos a través de http.
 * </p>
 * <p>
 * Para el caso se supone que posterior a la carga de un archivo, el uploader
 * correspondiente (@link ramon.FileUploader) creará un objeto ReferenciaArchivo
 * correspondiente a su especialización. Los detalles de las implementaciones
 * pasarán exclusivamente por los uploaders. De esta manera el usuario utilizará
 * los uploaders para obtener posteriormente el contenido de los archivos
 * cargados a través de la invocación al método traspasar (@link
 * ramon.FileUploader#traspasar(ReferenciaArchivo, OutputStream))
 * </p>
 */
public interface ReferenciaArchivo extends Serializable {

    public String getId();

    public String getNombre();

    public long getTamanio();
}
