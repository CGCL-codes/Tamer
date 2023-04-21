package es.caib.pagos.client;

import java.io.ByteArrayInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import es.caib.pagos.xml.DocXml;
import es.caib.pagos.xml.UtilXml;

public class JustificantePago extends DocXml {

    private static Log log = LogFactory.getLog(JustificantePago.class);

    private String localizador = null;

    private String dui = null;

    private String fechaPago = null;

    private String firma = null;

    public String getDui() {
        return dui;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public String getFirma() {
        return firma;
    }

    public String getLocalizador() {
        return localizador;
    }

    public JustificantePago(final String justificanteXML) {
        final ByteArrayInputStream bais = new ByteArrayInputStream(justificanteXML.getBytes());
        parse(bais);
        initialize();
    }

    private void initDatosPago(final Node nodo) {
        final Node l_child = UtilXml.getFirstChild(nodo);
        if (l_child != null) {
            try {
                Node l_xchild = l_child;
                do {
                    if (l_xchild.getNodeName().equalsIgnoreCase("LOCALIZADOR")) {
                        final Node l_textChild = UtilXml.getFirstChild(l_xchild);
                        if (l_textChild != null) {
                            this.localizador = (String) l_textChild.getNodeValue();
                        }
                    } else if (l_xchild.getNodeName().equalsIgnoreCase("DUI")) {
                        final Node l_textChild = UtilXml.getFirstChild(l_xchild);
                        if (l_textChild != null) {
                            this.dui = (String) l_textChild.getNodeValue();
                        }
                    } else if (l_xchild.getNodeName().equalsIgnoreCase("FECHA_PAGO")) {
                        final Node l_textChild = UtilXml.getFirstChild(l_xchild);
                        if (l_textChild != null) {
                            this.fechaPago = (String) l_textChild.getNodeValue();
                        }
                    }
                } while ((l_xchild = UtilXml.getNextSibling(l_xchild)) != null);
            } catch (Throwable ex) {
                log.error(" Error obteniendo informaci�n del nodo DATOS_PAGO. ", ex);
            }
        }
    }

    private int initDatos(final Node nodo) {
        Node l_child = UtilXml.getFirstChild(nodo);
        if (l_child != null) {
            try {
                do {
                    if (l_child.getNodeName().equalsIgnoreCase("FIRMA")) {
                        final Node l_textChild = UtilXml.getFirstChild(l_child);
                        if (l_textChild != null) {
                            this.firma = (String) l_textChild.getNodeValue();
                        }
                    } else if (l_child.getNodeName().equalsIgnoreCase("DATOS_PAGO")) {
                        initDatosPago(l_child);
                    }
                } while ((l_child = UtilXml.getNextSibling(l_child)) != null);
                return 0;
            } catch (Throwable ex) {
                log.error(" Error obteniendo informaci�n del nodo JUSTIFICANTE_PAGO. ", ex);
                return -1;
            }
        }
        return 0;
    }

    private void initialize() {
        try {
            Node l_nodo = UtilXml.getFirstChild(this.getXml());
            while (l_nodo != null && l_nodo.getNodeType() != Document.ELEMENT_NODE && !(l_nodo.getNodeName().equalsIgnoreCase("JUSTIFICANTE_PAGO"))) {
                l_nodo = UtilXml.getNextSibling(l_nodo);
            }
            if (l_nodo == null) {
                return;
            }
            if (initDatos(l_nodo) == -1) {
                log.error(" Error obteniendo datos del tag JUSTIFICANTE_PAGO del XML. ");
            }
        } catch (Throwable ex) {
            log.error(" Error obteniendo datos del XML de entrada JUSTIFICANTE_PAGO. ", ex);
        }
    }

    public boolean comprobarFirma() {
        return true;
    }
}
