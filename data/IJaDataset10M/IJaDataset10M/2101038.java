package es.cim.sistra.plugins.firma;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import es.caib.sistra.plugins.firma.FirmaIntf;
import es.caib.util.FirmaUtil;
import es.cim.ESBClient.v1.ServiceESBClientFactory;
import es.cim.ESBClient.v1.firma.FirmaESBClient;
import es.cim.ESBClient.v1.firma.RespuestaFirmaServidor;
import es.cim.ESBClient.v1.firma.RespuestaObtenerDatosCertificado;
import es.cim.ESBClient.v1.firma.RespuestaValidarFirma;

/**
 * Utilidad para manejo de firma CAIB
 */
public class UtilFirmaCIM {

    public static final String FORMATO_FIRMA = "XADES-BES";

    public static final String CHARSET = "UTF-8";

    private ServiceESBClientFactory sf = null;

    private FirmaESBClient firmaService = null;

    private static Log log = LogFactory.getLog(UtilFirmaCIM.class);

    /**
	 * Inicializa entorno firma
	 * @return
	 */
    public void iniciarDispositivo() throws Exception {
        try {
            String urlService = DadesProperties.getInstance().getProperty("bus.servicio.firma.url");
            boolean generateTimestamp = new Boolean(DadesProperties.getInstance().getProperty("bus.generateTimestamp")).booleanValue();
            boolean logCalls = new Boolean(DadesProperties.getInstance().getProperty("bus.logCall")).booleanValue();
            boolean disableCnCheck = new Boolean(DadesProperties.getInstance().getProperty("bus.disableCnCheck")).booleanValue();
            String usuario = DadesProperties.getInstance().getProperty("bus.usuario");
            String password = DadesProperties.getInstance().getProperty("bus.password");
            this.sf = new ServiceESBClientFactory(generateTimestamp, logCalls, disableCnCheck);
            this.firmaService = sf.getFirmaESBClient();
            this.firmaService.setAutenticacion(urlService, usuario, password);
        } catch (Exception e) {
            throw new Exception("Error al obtener propiedades conexion bus", e);
        }
    }

    /**
	 * Realiza la firma de los datos con el certificado indicado en servidor
	 * @param datos documento a firmar
	 * @param nombreCertificado alias del certificado
	 * @param parametros parametros que se necessitan para realizar la firma (nombre del documento)
	 * @return 
	 */
    public FirmaIntf firmar(InputStream datos, String nombreCertificado, Map parametros) throws Exception {
        String formatoFirma = UtilFirmaCIM.FORMATO_FIRMA;
        byte[] datosArrayBytes = datos.toString().getBytes();
        if (parametros.containsKey(FirmaUtil.AFIRMA_PARAMETER_ARCHIVO)) {
            String nombreArchivo = (String) parametros.get(FirmaUtil.AFIRMA_PARAMETER_ARCHIVO);
            String extensionArchivo = "";
            if (nombreArchivo != null && nombreArchivo.lastIndexOf(".") != -1) {
                extensionArchivo = nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1, nombreArchivo.length());
                nombreArchivo = nombreArchivo.substring(0, nombreArchivo.lastIndexOf("."));
            }
            RespuestaFirmaServidor rfs = firmaService.firmaServidor(datosArrayBytes, nombreArchivo, extensionArchivo, formatoFirma, nombreCertificado);
            if (rfs.getCodigoError() != null && !"OK".equals(rfs.getCodigoError())) {
                throw new Exception(rfs.getDescripcionError());
            }
            FirmaCIM firma = new FirmaCIM();
            firma.setFormatoFirma(FORMATO_FIRMA);
            firma.setSignature(new sun.misc.BASE64Encoder().encode(crearFirmaCIM(rfs.getFirmaElectronica(), formatoFirma).getBytes(UtilFirmaCIM.CHARSET)));
            return firma;
        } else {
            throw new Exception("No existen los parametros necesarios para realizar la firma, falta " + FirmaUtil.AFIRMA_PARAMETER_ARCHIVO + ".");
        }
    }

    /**
	 * Valida una firma electr�nica
	 * @param datos documento de la firma que vamos a validar
	 * @param firma firma electr�nica de los datos que vamos a validar
	 * @return 
	 */
    public RespuestaValidarFirma verificarFirma(InputStream datos, FirmaIntf firma) throws Exception {
        byte[] datosArrayBytes = datos.toString().getBytes();
        FirmaCIM firmaCIM = (FirmaCIM) firma;
        byte[] firmaArrayBytes = obtenerFirmaFirmaCIM(firmaCIM);
        return firmaService.validarFirma(datosArrayBytes, firmaArrayBytes, firmaCIM.getFormatoFirma());
    }

    /**
	 * Pasamos una firma con el formato indicado por la constante FORMATO_FIRMA a formato CIM
	 * <FIRMA_CIM>
     *      <NIF>nif / cif</NIF>
     *      <NOMBRE>nombre completo</NOMBRE>
     *      <FORMATO>formato firma</FORMATO>
     *      <FIRMA>firma en b64</FIRMA>
	 * </FIRMA_CIM>
	 * 
	 * @param firma firma electr�nica en formato indicado por la constante FORMATO_FIRMA
	 *  que queremos pasar a FirmaCIM
	 * @param formatoFirma 
	 * @return 
	 */
    public String crearFirmaCIM(byte[] firma, String formatoFirma) throws Exception {
        try {
            String xml = null;
            RespuestaObtenerDatosCertificado rodc = obtenerDatosCertificado(firma, formatoFirma);
            if (rodc != null && "OK".equals(rodc.getCodigoError())) {
                xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
                xml = xml + "<FIRMA_CIM>";
                xml = xml + "<NIF>" + obtenerNifCertificado(rodc) + "</NIF>";
                xml = xml + "<NOMBRE>" + obtenerNombreCertificado(rodc) + "</NOMBRE>";
                xml = xml + "<FORMATO>" + formatoFirma + "</FORMATO>";
                xml = xml + "<FIRMA>" + new sun.misc.BASE64Encoder().encode(firma) + "</FIRMA>";
                xml = xml + "</FIRMA_CIM>";
            } else {
                log.error("No se han podido obtener los datos del certificado de la firma" + (rodc != null ? ": " + rodc.getDescripcionError() : ""));
                throw new Exception("No se han podido obtener los datos del certificado de la firma." + (rodc != null ? ": " + rodc.getDescripcionError() : ""));
            }
            return xml;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
	 * Obtiene el nif del usuario que ha firmado desde el certificado de la firma
	 * @param rodc es un objeto del tipo RespuestaObtenerDatosCertificado donde podemos acceder al certificado
	 * @return 
	 * @throws Exception 
	 */
    public String obtenerNifCertificado(RespuestaObtenerDatosCertificado rodc) throws Exception {
        String nif = null;
        if ("0".equals(rodc.getCertificado().getClasificacion())) {
            nif = rodc.getCertificado().getNifResponsable();
        } else if ("1".equals(rodc.getCertificado().getClasificacion())) {
            nif = rodc.getCertificado().getCif();
        } else if ("2".equals(rodc.getCertificado().getClasificacion())) {
            throw new Exception("Certificado de componente no permitido");
        } else {
            throw new Exception("Clasificacion de certificado no reconocida : " + rodc.getCertificado().getClasificacion());
        }
        if (StringUtils.isEmpty(nif)) {
            throw new Exception("Nif asociado al certificado es vac�o");
        }
        return nif;
    }

    /**
	 * Obtiene el nif del usuario que ha firmado desde la firma en formato FirmaCIM
	 * @param firma firma electr�nica en formato FirmaCIM
	 * @return 
	 */
    public static String obtenerNifFirmaCIM(String firma) throws Exception {
        try {
            String nif = null;
            javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            javax.xml.parsers.DocumentBuilder db;
            db = dbf.newDocumentBuilder();
            Document doc;
            doc = db.parse(new ByteArrayInputStream(new sun.misc.BASE64Decoder().decodeBuffer(firma)));
            if (doc != null) {
                NodeList nl = doc.getElementsByTagName("NIF");
                if (nl != null && nl.getLength() > 0) {
                    return nl.item(0).getTextContent();
                }
            }
            return nif;
        } catch (Exception e) {
            log.error("Error al obtener nif de la firma: " + e.getMessage(), e);
            throw new Exception("Error al obtener nif de la firma", e);
        }
    }

    /**
	 * Obtiene el nombre del usuario que ha firmado desde el certificado de la firma
	 * @param rodc es un objeto del tipo RespuestaObtenerDatosCertificado donde podemos acceder al certificado
	 * @return 
	 * @throws Exception 
	 */
    public String obtenerNombreCertificado(RespuestaObtenerDatosCertificado rodc) throws Exception {
        String nombre = null;
        if ("0".equals(rodc.getCertificado().getClasificacion())) {
            nombre = (StringUtils.isNotEmpty(rodc.getCertificado().getPrimerApellido()) ? rodc.getCertificado().getPrimerApellido() : "") + (StringUtils.isNotEmpty(rodc.getCertificado().getSegundoApellido()) ? " " + rodc.getCertificado().getSegundoApellido() : "") + (StringUtils.isNotEmpty(rodc.getCertificado().getNombre()) ? " " + rodc.getCertificado().getNombre() : "");
        } else if ("1".equals(rodc.getCertificado().getClasificacion())) {
            nombre = rodc.getCertificado().getRazonSocial();
        } else if ("2".equals(rodc.getCertificado().getClasificacion())) {
            throw new Exception("Certificado de componente no permitido");
        } else {
            throw new Exception("Clasificacion de certificado no reconocida : " + rodc.getCertificado().getClasificacion());
        }
        if (StringUtils.isEmpty(nombre)) {
            throw new Exception("Nombre asociado al certificado es vac�o");
        }
        return nombre;
    }

    /**
	 * Obtiene el nombre del usuario que ha firmado desde la firma en formato FirmaCIM
	 * @param firma firma electr�nica en formato FirmaCIM
	 * @return 
	 * @throws Exception 
	 */
    public static String obtenerNombreFirmaCIM(String firma) throws Exception {
        try {
            String nombre = null;
            javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            javax.xml.parsers.DocumentBuilder db;
            db = dbf.newDocumentBuilder();
            Document doc;
            doc = db.parse(new ByteArrayInputStream(new sun.misc.BASE64Decoder().decodeBuffer(firma)));
            if (doc != null) {
                NodeList nl = doc.getElementsByTagName("NOMBRE");
                if (nl != null && nl.getLength() > 0) {
                    return nl.item(0).getTextContent();
                }
            }
            return nombre;
        } catch (Exception e) {
            throw new Exception("Error obteniendo nombre de la firma", e);
        }
    }

    /**
	 * Obtiene la firma del documento desde la firma en formato FirmaCIM
	 * @param firma firma electr�nica en formato FirmaCIM
	 * @return 
	 */
    public static byte[] obtenerFirmaFirmaCIM(FirmaCIM firmaCIM) {
        try {
            String firma = firmaCIM.getSignature();
            javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            sun.misc.BASE64Decoder b64Dec = new sun.misc.BASE64Decoder();
            javax.xml.parsers.DocumentBuilder db;
            db = dbf.newDocumentBuilder();
            Document doc;
            doc = db.parse(new ByteArrayInputStream(b64Dec.decodeBuffer(firma)));
            if (doc != null) {
                NodeList nl = doc.getElementsByTagName("FIRMA");
                if (nl != null && nl.getLength() > 0) {
                    return b64Dec.decodeBuffer(nl.item(0).getTextContent());
                }
            }
            log.error("Excepcion al obtener la firma de FirmaCIM: No se ha encontrado campo FIRMA");
            return null;
        } catch (Exception e) {
            log.error("Excepcion al obtener la firma de FirmaCIM: " + e.getMessage(), e);
            return null;
        }
    }

    /**
	 * Obtiene un objeto del tipo Certificado donde podemos acceder al certificado
	 * @param firma firma electr�nica
	 * @param formatoFirma Formato de firma
	 * @return 
	 */
    public RespuestaObtenerDatosCertificado obtenerDatosCertificado(byte[] firma, String formatoFirma) throws Exception {
        try {
            if (formatoFirma.startsWith("XADES")) {
                javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                javax.xml.parsers.DocumentBuilder db;
                db = dbf.newDocumentBuilder();
                Document doc;
                doc = db.parse(new ByteArrayInputStream(firma));
                if (doc != null) {
                    NodeList nl = doc.getElementsByTagName("ds:X509Certificate");
                    if (nl != null && nl.getLength() > 0) {
                        return firmaService.obtenerDatosCertificado(new sun.misc.BASE64Decoder().decodeBuffer(nl.item(0).getTextContent()));
                    } else {
                        throw new Exception("No se encuentra tag ds:X509Certificate");
                    }
                } else {
                    throw new Exception("No se encuentra tag ds:X509Certificate");
                }
            } else if (formatoFirma.startsWith("CADES")) {
                throw new Exception("PENDIENTE IMPLEMENTACION EN CADES");
            } else {
                throw new Exception("Formato de firma no reconocido: " + formatoFirma);
            }
        } catch (Exception e) {
            log.error("Error obteniendo datos del certificado", e);
            throw new Exception("Error obteniendo datos del certificado", e);
        }
    }

    public static String unescapeChars64UrlSafe(String cad) {
        cad = cad.replaceAll("-", "+");
        cad = cad.replaceAll("_", "/");
        return cad;
    }

    public static String escapeChars64UrlSafe(String cad) {
        cad = cad.replaceAll("\\+", "-");
        cad = cad.replaceAll("/", "_");
        cad = cad.replaceAll("[\\n\\r]", "");
        return cad;
    }
}
