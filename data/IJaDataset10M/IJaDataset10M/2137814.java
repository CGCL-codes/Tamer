package net.esle.sinadura.core.firma;

import net.esle.sinadura.core.certificado.Certificado;
import net.esle.sinadura.core.firma.exceptions.SinaduraCoreException;

/**
 * @author zylk.net
 */
public interface DocumentIFace {

    /**
	 * Tipo PDF
	 */
    public static final String PDF_TYPE = "PDF";

    /**
	 * Devuelve el tipo del documento
	 * 
	 * @return
	 */
    public String getType();

    /**
	 * @return
	 */
    public String getPathOrigen();

    /**
	 * @return
	 */
    public boolean isSigned();

    /**
	 *
	 */
    public void deleteFile();

    /**
	 * @return
	 */
    public float getPageHeight();

    /**
	 * @return
	 */
    public float getPageWidth();

    /**
	 * @return
	 */
    public String getAlias();

    /**
	 * @param signed
	 */
    public void setSigned(boolean signed);

    /**
	 * @param TSURL
	 */
    public void setTSURL(String TSURL);

    /**
	 * @return
	 */
    public String getPathDestino();

    /**
	 * @param signStore
	 * @param certificado
	 * @param pathDestino
	 * @param reason
	 * @param location
	 * @param imagePath
	 * @throws SinaduraCoreException
	 */
    public void firmar(SignStoreIFace signStore, Certificado certificado, String pathDestino, String reason, String location, String imagePath) throws SinaduraCoreException;

    /**
	 * @param signStore
	 * @param certificado
	 * @param pathDestino
	 * @param visible
	 * @param reason
	 * @param location
	 * @param imagePath
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @throws SinaduraCoreException
	 */
    public void firmar(SignStoreIFace signStore, Certificado certificado, String pathDestino, boolean visible, String reason, String location, String imagePath, int startX, int startY, int endX, int endY) throws SinaduraCoreException;

    /**
	 * @param signStore
	 * @param certificado
	 * @param pathDestino
	 * @param visible
	 * @param reason
	 * @param location
	 * @param imagePath
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param hasPDF417
	 * @param marginLeftPDF417
	 * @param marginTopPDF417
	 * @throws SinaduraCoreException
	 */
    public void firmar(SignStoreIFace signStore, Certificado certificado, String pathDestino, boolean visible, String reason, String location, String imagePath, int startX, int startY, int endX, int endY, boolean hasPDF417, int marginLeftPDF417, int marginTopPDF417) throws SinaduraCoreException;

    /**
	 * @param signStore
	 * @param certificado
	 * @param pathDestino
	 * @param reason
	 * @param location
	 * @param imagePath
	 * @param hasPDF417
	 * @param marginLeftPDF417
	 * @param marginTopPDF417
	 * @throws SinaduraCoreException
	 */
    public void firmar(SignStoreIFace signStore, Certificado certificado, String pathDestino, String reason, String location, String imagePath, boolean hasPDF417, int marginLeftPDF417, int marginTopPDF417) throws SinaduraCoreException;
}
