package campos;

import excepciones.ExcepcionEstableciendoValorDelCampo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.AbstractTagFrame;

/**
 * Representa la imagen del album asociada a la cancion.
 * 
 * @author Rodrigo Villamil Perez
 */
public class CampoImagen extends CampoTAG {

    public CampoImagen() {
        super(EnumeracionNombresCampos.Imagen);
    }

    /**
     * Retorn la imagen asociada al campo. En principio es un array de bytes.
     * @return Array de bytes con la imagen
     */
    @Override
    public Object getValor() {
        try {
            for (TagField tagFrame : getTag().get(TagFieldKey.COVER_ART)) {
                if (tagFrame instanceof AbstractTagFrame) {
                    return ((AbstractTagFrame) (tagFrame)).getBody().getObjectValue(DataTypes.OBJ_PICTURE_DATA);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CampoTAG.class.getName()).log(Level.WARNING, java.util.ResourceBundle.getBundle("resources/textos").getString("**_No_se_ha_podido_obtener_el_campo_") + this.getClass().getName());
        }
        return null;
    }

    @Override
    public void setValor(Object valor) throws ExcepcionEstableciendoValorDelCampo {
        if (valor instanceof String) {
            File ruta = new File((String) valor);
            if (ruta.isFile()) {
                try {
                    valor = this.generaImagen(ruta.getAbsolutePath());
                } catch (IOException ex) {
                    throw new ExcepcionEstableciendoValorDelCampo(this, java.util.ResourceBundle.getBundle("resources/textos").getString("_Error_de_entrada/salida._") + ex.getMessage());
                }
            } else {
                throw new ExcepcionEstableciendoValorDelCampo(this, (String) valor + java.util.ResourceBundle.getBundle("resources/textos").getString("_no_es_un_fichero.") + System.getProperty("line.separator"));
            }
        }
        estableceImagen(valor);
    }

    /**
     * Establece la imagen en forma de array de bytes que se pasa
     * como parametro.
     * 
     * @param valor Objeto, que en principio es un array de bytes
     */
    private void estableceImagen(Object valor) throws ExcepcionEstableciendoValorDelCampo {
        boolean tieneCoverArt = false;
        for (TagField tagFrame : getTag().get(TagFieldKey.COVER_ART)) {
            if (tagFrame instanceof AbstractTagFrame) {
                ((AbstractTagFrame) (tagFrame)).getBody().setObjectValue(DataTypes.OBJ_PICTURE_DATA, valor);
                Logger.getLogger(CampoImagen.class.getName()).log(Level.WARNING, java.util.ResourceBundle.getBundle("resources/textos").getString("**_Se_ha_establecido_una_imagen."));
                tieneCoverArt = true;
            }
        }
        if (!tieneCoverArt) {
            if (getTag() instanceof AbstractID3v2Tag) {
                try {
                    byte[] imagedata = (byte[]) valor;
                    AbstractID3v2Tag v2tag = (AbstractID3v2Tag) getTag();
                    v2tag.add(v2tag.createArtworkField(imagedata, java.util.ResourceBundle.getBundle("resources/textos").getString("image/jpg")));
                    Logger.getLogger(CampoImagen.class.getName()).log(Level.WARNING, java.util.ResourceBundle.getBundle("resources/textos").getString("**_Se_ha_generado_una_nueva_imagen_") + java.util.ResourceBundle.getBundle("resources/textos").getString("y_se_ha_establecido."));
                    tieneCoverArt = true;
                } catch (Exception ex) {
                    throw new ExcepcionEstableciendoValorDelCampo(this, (String) valor + java.util.ResourceBundle.getBundle("resources/textos").getString("_No_se_ha_podido_crear_un_COVER-ART_") + java.util.ResourceBundle.getBundle("resources/textos").getString("para_el_fichero.") + System.getProperty("line.separator"));
                }
            } else {
                throw new ExcepcionEstableciendoValorDelCampo(this, (String) valor + java.util.ResourceBundle.getBundle("resources/textos").getString("_No_es_un_fichero_ID3v2TAG"));
            }
        }
    }

    /**
     * Genera una imagen a partir de un fichero con imagenes.
     * 
     * @param rutaFicheroImagen Fichero con la imagen a establecer
     * @return Un array de bytes con la imagen
     * @throws java.io.IOException
     */
    private Object generaImagen(String rutaFicheroImagen) throws IOException {
        byte[] imagedata = null;
        try {
            RandomAccessFile imageFile = new RandomAccessFile(new File(rutaFicheroImagen), "r");
            imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CampoImagen.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imagedata;
    }

    @Override
    public String toString() {
        StringBuffer salida = new StringBuffer();
        salida.append("-" + getNombre() + ":");
        if (getValor() != null) {
            salida.append(java.util.ResourceBundle.getBundle("resources/textos").getString("FOTO"));
        } else {
            salida.append(java.util.ResourceBundle.getBundle("resources/textos").getString("Sin_imagen"));
        }
        salida.append(System.getProperty("line.separator"));
        return salida.toString();
    }
}
