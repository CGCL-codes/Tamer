package org.elf.weblayer;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.elf.common.*;
import org.elf.weblayer.kernel.dispatchers.controllers.XMLMapping;
import org.w3c.dom.*;

/**
 * Permite acceden a los m�todos de un Controller concreto que se encuentra en otra m�quina o aplicaci�n.
 * @author <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class RemoteController {

    String _controllerName;

    URL _baseURL;

    List<HttpCookie> _httpCookies;

    RemoteController(String name, URL baseURL, List<HttpCookie> httpCookies) {
        _controllerName = name;
        _baseURL = baseURL;
        _httpCookies = httpCookies;
    }

    /**
     * Llama a un m�todo del Controller y retorna el resulta de la llamada a dicho m�todo
     * @param name Nombre del m�todo
     * @param parameters Lista de argumentos del m�todo 
     * @return El valor que retorna la llamada al m�todo
     */
    public Object executeService(String serviceName, List parameters) {
        try {
            Document documentRequest = generateDocumentRequestFromParameters(parameters);
            HttpURLConnection httpURLConnection;
            {
                URL realURL;
                if (_baseURL.toExternalForm().endsWith("/")) {
                    realURL = new URL(_baseURL.toExternalForm() + "controller/" + _controllerName + "/service/" + serviceName);
                } else {
                    realURL = new URL(_baseURL.toExternalForm() + "/controller/" + _controllerName + "/service/" + serviceName);
                }
                httpURLConnection = (HttpURLConnection) realURL.openConnection();
                httpURLConnection.setRequestProperty("Content-type", "text/xml");
                httpURLConnection.setAllowUserInteraction(false);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                setCookies(httpURLConnection, _httpCookies);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(generateByteArrayFromDocument(documentRequest));
                outputStream.flush();
                outputStream.close();
            }
            List<HttpCookie> newSetCookies = getNewSetCookies(httpURLConnection);
            mergeNewSetCookies(_httpCookies, newSetCookies);
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Document documentResponse = XMLUtil.parseFile(httpURLConnection.getInputStream());
                return generateObjectResponseFromDocument(documentResponse);
            } else {
                StringBuilder sb = new StringBuilder();
                Reader is = new InputStreamReader(httpURLConnection.getErrorStream());
                char[] buffer = new char[32 * 1024];
                int bytesRead = 0;
                while ((bytesRead = is.read(buffer)) != -1) {
                    sb.append(buffer, 0, bytesRead);
                }
                throw new RuntimeException(sb.toString());
            }
        } catch (RuntimeException rex) {
            throw rex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Document executeRawService(String serviceName, Document document) {
        try {
            Document documentRequest = document;
            HttpURLConnection httpURLConnection;
            {
                URL realURL;
                if (_baseURL.toExternalForm().endsWith("/")) {
                    realURL = new URL(_baseURL.toExternalForm() + "controller/" + _controllerName + "/rawService/" + serviceName);
                } else {
                    realURL = new URL(_baseURL.toExternalForm() + "/controller/" + _controllerName + "/rawService/" + serviceName);
                }
                httpURLConnection = (HttpURLConnection) realURL.openConnection();
                httpURLConnection.setRequestProperty("Content-type", "text/xml");
                httpURLConnection.setAllowUserInteraction(false);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                setCookies(httpURLConnection, _httpCookies);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(generateByteArrayFromDocument(documentRequest));
                outputStream.flush();
                outputStream.close();
            }
            List<HttpCookie> newSetCookies = getNewSetCookies(httpURLConnection);
            mergeNewSetCookies(_httpCookies, newSetCookies);
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Document documentResponse = XMLUtil.parseFile(httpURLConnection.getInputStream());
                return documentResponse;
            } else {
                StringBuilder sb = new StringBuilder();
                Reader is = new InputStreamReader(httpURLConnection.getErrorStream());
                char[] buffer = new char[32 * 1024];
                int bytesRead = 0;
                while ((bytesRead = is.read(buffer)) != -1) {
                    sb.append(buffer, 0, bytesRead);
                }
                throw new RuntimeException(sb.toString());
            }
        } catch (RuntimeException rex) {
            throw rex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Genera el documento XML con los datos para llamar a un m�todo de un Controoler
     * @param parameters Argumentos de la llamada al m�todo del Controller
     * @return El documento XML con los datos para hacer la llamada a un m�todo de un servicio
     */
    private Document generateDocumentRequestFromParameters(List parameters) {
        Document documentRequest = XMLUtil.newDocument();
        Element responseElement = documentRequest.createElement("REQUEST");
        documentRequest.appendChild(responseElement);
        Element parametersElement = documentRequest.createElement("PARAMETERS");
        responseElement.appendChild(parametersElement);
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                Element valueElement = XMLMapping.toXML(documentRequest, parameters.get(i));
                if (valueElement != null) {
                    Element parameter = documentRequest.createElement("PARAMETER");
                    parameter.appendChild(valueElement);
                    parametersElement.appendChild(parameter);
                }
            }
        }
        return documentRequest;
    }

    /**
     * Obtiene el objeto de retorno de la llamada a un Service de un Controller Remoto
     * @param documentResponse El XML que retorn� el Controller remoto al llamarlo
     * @return El Objeto que resulta de transformar el XML en un objeto Java
     */
    private Object generateObjectResponseFromDocument(Document documentResponse) {
        Element returnValueElement = XMLUtil.getFirstChild(documentResponse.getDocumentElement(), "RETURNVALUE");
        Element value = XMLUtil.getFirstChild(returnValueElement);
        return XMLMapping.toObject(value);
    }

    /**
     * Dado un Documento XML lo tranforma en un String y a su vez en un Array de Bytes
     * @param document Documento XML a tranformar en un Array de Bytes
     * @return El Array de bytes que resulta de tranformar el XML en un String y luego en un XML
     */
    private byte[] generateByteArrayFromDocument(Document document) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Properties properties = transformer.getOutputProperties();
            StreamResult result = new StreamResult(out);
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperties(properties);
            transformer.transform(source, result);
            return out.toByteArray();
        } catch (RuntimeException rex) {
            throw rex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Establece las Cookies que usar� la conexi�n HTTP
     * @param httpURLConnection La conexi�n a la que se le establecen las Cookies
     * @param httpCookies Una lista de Objetos HttpCookie.
     */
    private void setCookies(HttpURLConnection httpURLConnection, List<HttpCookie> httpCookies) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < httpCookies.size(); i++) {
            if (i != 0) {
                sb.append(";");
            }
            sb.append(httpCookies.get(i).toString());
        }
        httpURLConnection.setRequestProperty("Cookie", sb.toString());
    }

    /**
     * Obtiene la lista de Cookies que se han asignado desde el servidor.
     * Se asignan mediante "Set-Cookie" o "Set-Cookie2"
     * @param httpURLConnection Conexi�n al servidor de la que se obtienen las cookies
     * @return Un lista de objetos HttpCookie que se han asignado desde el servidor a la conexi�n
     */
    private List<HttpCookie> getNewSetCookies(HttpURLConnection httpURLConnection) {
        List<HttpCookie> httpCookies = new ArrayList<HttpCookie>();
        String headerFieldKey;
        int i = 1;
        do {
            headerFieldKey = httpURLConnection.getHeaderFieldKey(i);
            if (headerFieldKey != null) {
                if (headerFieldKey.equalsIgnoreCase("Set-Cookie") || headerFieldKey.equalsIgnoreCase("Set-Cookie2")) {
                    String headerField = httpURLConnection.getHeaderField(i);
                    List<HttpCookie> newHttpCookies = HttpCookie.parse(headerFieldKey + ": " + headerField);
                    for (int j = 0; j < newHttpCookies.size(); j++) {
                        newHttpCookies.get(j).setVersion(1);
                    }
                    httpCookies.addAll(newHttpCookies);
                }
            }
            i++;
        } while (headerFieldKey != null);
        return httpCookies;
    }

    /**
     * A�ade a una lista de Cookies unas nuevas Cookies.
     * Si hay Cookies repetidas se queda con las de la lista newHttpCookies
     * Y si en esa lista tambien hay repetidas se queda siempre con la �ltima
     * @param httpCookies Lista de HttpCookies a la que se le a�aden las nuevas Cookies
     * @param newHttpCookies La nueva lista de Cookies que queremos a�adir a la lista.
     */
    private void mergeNewSetCookies(List<HttpCookie> httpCookies, List<HttpCookie> newHttpCookies) {
        for (int i = 0; i < newHttpCookies.size(); i++) {
            for (int j = httpCookies.size() - 1; j >= 0; j--) {
                if (httpCookies.get(j).equals(newHttpCookies.get(i))) {
                    httpCookies.remove(j);
                }
            }
            httpCookies.add(newHttpCookies.get(i));
        }
    }

    /**
     * Imprime la informaci�n de una lista de cookies por pantalla
     * Esta funci�n est� solo por motivos de depuraci�n
     * @param msg Mesaje a mostrar
     * @param httpCookies La lista de Cookies a mostrar por pantalla
     */
    private void printCookies(String msg, List<HttpCookie> httpCookies) {
        System.out.println(msg);
        for (int i = 0; i < httpCookies.size(); i++) {
            System.out.println("\t" + httpCookies.get(i));
        }
    }
}
