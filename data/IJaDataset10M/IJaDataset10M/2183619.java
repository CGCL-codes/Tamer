package com.webmotix.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.xml.serialize.XMLSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import com.webmotix.core.MotixNodeTypes;
import com.webmotix.core.MotixNodes;
import com.webmotix.util.QueryUtil;
import com.webmotix.util.XPathUtils;
import com.webmotix.util.XmlUtils;

public abstract class MotixDataTransporter {

    private static final Logger log = LoggerFactory.getLogger(MotixDataTransporter.class);

    /**
     * Identifica��o do formato de arquivo ZIP.
     */
    public static final String ZIP = ".zip";

    /**
     * Identifica��o do formato de arquivo GZIP.
     */
    public static final String GZ = ".gz";

    /**
     * Identifica��o do formato de arquivo XML.
     */
    public static final String XML = ".xml";

    public static final boolean RECURSIVE_YES = true;

    public static final boolean RECURSIVE_NO = false;

    public static final int DEFAULT_IMPORT_MODE = ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING;

    /**
     * Exporta todo o conjunto e subconjunto de dados de um Node para um
	 * OutputStream.
     * @param node
     * @param baseOutputStream
     * @param format
     * @param noRecursive
     * @throws IOException
     */
    public static void executeExport(final Node node, final OutputStream baseOutputStream, final String format, final boolean recursive) throws IOException {
        OutputStream outputStream = baseOutputStream;
        if (format.endsWith(ZIP)) {
            outputStream = new GZIPOutputStream(baseOutputStream);
        } else if (format.endsWith(GZ)) {
            outputStream = new GZIPOutputStream(baseOutputStream);
        }
        File tempFile = null;
        OutputStream fileStream = null;
        try {
            log.info(MessageFormat.format("Export Node [ {0} ] ({1}), to {2}", new Object[] { node.getPath(), node.getPrimaryNodeType().getName(), format }));
            final Session session = node.getSession();
            tempFile = File.createTempFile("export-" + session.getWorkspace().getName() + "-" + session.getUserID(), "xml");
            fileStream = new FileOutputStream(tempFile);
            try {
                session.exportSystemView(node.getPath(), fileStream, false, !recursive);
            } finally {
                IOUtils.closeQuietly(fileStream);
            }
            final InputStream fileInputStream = new FileInputStream(tempFile);
            formatXML(fileInputStream, outputStream);
            IOUtils.closeQuietly(fileInputStream);
        } catch (final IOException e) {
            throw new NestableRuntimeException(e);
        } catch (final SAXException e) {
            throw new NestableRuntimeException(e);
        } catch (final RepositoryException e) {
            throw new NestableRuntimeException(e);
        } finally {
            if (tempFile != null) {
                if (!tempFile.delete()) {
                    log.warn("N�o foi poss�vel excluir o arquivo tempor�rio de exporta��o: {}", tempFile);
                }
            }
        }
        if (outputStream instanceof DeflaterOutputStream) {
            ((DeflaterOutputStream) outputStream).finish();
        }
        baseOutputStream.flush();
        IOUtils.closeQuietly(baseOutputStream);
    }

    /**
     * Exporta todo o conjunto e subconjunto de dados de um Node para um
	 * OutputStream.
	 * <p><b>Assume o padr�o como Recursivo!</b></p>
     * @param node
     * @param baseOutputStream
     * @param format
     * @throws IOException
     */
    public static void executeExport(final Node node, final OutputStream baseOutputStream, final String format) throws IOException {
        executeExport(node, baseOutputStream, format, RECURSIVE_YES);
    }

    /**
	 * Exporta todo o conjunto e subconjunto de dados de um Node para um
	 * diret�rio.
	 * @param node N� que ser� exportado.
	 * @param folder Destino do conte�do do Node.
	 */
    public static void executeExportToFolder(final Node node, final File folder) {
        try {
            if (node.hasNodes()) {
                final String nodeFolerName = StringUtils.replaceChars(node.getPath(), '/', File.separatorChar);
                final File nodeFolder = new File(folder, nodeFolerName);
                if (!nodeFolder.exists()) {
                    if (!nodeFolder.mkdirs()) {
                        log.error("N�o foi poss�vel criar o diret�rio {}", nodeFolder);
                        return;
                    }
                }
                final NodeIterator nodes = node.getNodes();
                while (nodes.hasNext()) {
                    final Node nodeChild = nodes.nextNode();
                    executeExportToFolder(nodeChild, folder);
                }
                final FileOutputStream fosJcr = new FileOutputStream(createFile(node.getName(), nodeFolder.getParentFile()));
                executeExport(node, fosJcr, XML, RECURSIVE_NO);
            } else {
                final String nodeFolerName = StringUtils.replaceChars(node.getParent().getPath(), '/', File.separatorChar);
                final File nodeFolder = new File(folder, nodeFolerName);
                final FileOutputStream fosJcr = new FileOutputStream(createFile(node.getName(), nodeFolder));
                executeExport(node, fosJcr, XML, RECURSIVE_NO);
            }
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
	 * Importa para o workspace atrav�s de um arquivo contendo  o conte�do JCR.
	 * @param session
	 * @param file
	 */
    public static void executeImport(final Node toNode, final File file) {
        try {
            log.info("Importando conte�do do arquivo {} para o Node {}", file, toNode.getPath());
            executeImport(toNode, getInputStreamForFile(file));
        } catch (final Exception ex) {
            log.error("Falha ao processar importa��o com o arquivo: " + file, ex);
        }
    }

    /**
	 * Importa para o workspace atrav�s de um Stream contendo o conte�do JCR.
	 * Importa��o padr�o considerando que vai substituir os nodes com mesmo UUID.
	 * @param session
	 * @param xmlStream
	 */
    public static void executeImport(final Node toNode, final InputStream xmlStream) throws RepositoryException, IOException {
        executeImport(toNode, xmlStream, DEFAULT_IMPORT_MODE);
    }

    /**
	 * Importa para o workspace atrav�s de um Stream contendo o conte�do JCR.
	 * @param toNode
	 * @param xmlStream
	 * @param mode Tratamento do uuid:
	 * <lu>
	 * <li>ImportUUIDBehavior.IMPORT_UUID_COLLISION_REMOVE_EXISTING</li>
	 * <li>ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING</li>
	 * <li>ImportUUIDBehavior.IMPORT_UUID_COLLISION_THROW</li>
	 * <li>ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW</li>
	 * </lu>
	 */
    public static void executeImport(final Node toNode, final InputStream xmlStream, final int mode) throws RepositoryException, IOException {
        log.info("Importando conte�do para o Node {}", toNode.getPath());
        final Session session = toNode.getSession();
        session.importXML(toNode.getPath(), xmlStream, mode);
        session.save();
    }

    /**
	 * Executa a importa��o filtando por um determinado tipo de elemento.
	 * @param toNode
	 * @param xmlStream
	 * @param mode
	 * @param filterNode Tipo de node a ser filtrado
	 * @throws RepositoryException
	 * @throws IOException
	 */
    public static void executeImport(final Node toNode, final InputStream xmlStream, final int mode, final String filterNode) throws RepositoryException, IOException {
        log.info("Importando conte�do para o Node {} filtro por " + filterNode, toNode.getPath());
        final Session session = toNode.getSession();
        Node nodeTempRoot = null;
        if (session.getRootNode().hasNode(MotixNodes.TEMP)) {
            nodeTempRoot = session.getRootNode().getNode(MotixNodes.TEMP);
        } else {
            nodeTempRoot = session.getRootNode().addNode(MotixNodes.TEMP);
        }
        final Node nodeAux = nodeTempRoot.addNode(UUID.randomUUID().toString());
        try {
            session.importXML(nodeAux.getPath(), xmlStream, mode);
            session.save();
            final String query = XPathUtils.getXPath(nodeAux, filterNode);
            final NodeIterator nodeIterator = QueryUtil.query(session, query, Query.XPATH);
            while (nodeIterator.hasNext()) {
                final Node node = nodeIterator.nextNode();
                session.move(node.getPath(), toNode.getPath() + "/" + node.getName());
            }
        } finally {
            nodeAux.remove();
        }
    }

    /**
	 * Carrega o workspace atrav�s de um conjunto de arquivos localizados em WEB-INF/bootstrap
	 * @param workspace
	 */
    public static void executeImportFromFolder(final Node toNode, final File root) {
        if (root.exists()) {
            try {
                final File[] childs = root.listFiles(new XmlZipFilesFilter());
                if (root.isFile()) {
                    executeImport(toNode, root);
                } else {
                    final String workspace = toNode.getSession().getWorkspace().getName();
                    for (int j = 0; j < childs.length; j++) {
                        final File file = childs[j];
                        log.info("Importando arquivo {} para o workspace {}", root, workspace);
                        MotixDataTransporter.executeImportFromFolder(toNode, file);
                    }
                }
            } catch (RepositoryException ex) {
            }
        } else {
            log.error("N�o localizou {} ou n�o � um diret�rio para executar a importa��o.", root);
        }
    }

    /**
     * Cria um Stream a patir de um arquivo, que pode ser um XML ou a partir 
     * de XML compactado (zipped/gzipped).
     * @param xmlFile
     * @return stream of the file
     * @throws IOException
     */
    public static InputStream getInputStreamForFile(final File xmlFile) throws IOException {
        InputStream xmlStream;
        if (xmlFile.getName().endsWith(ZIP)) {
            xmlStream = new GZIPInputStream((new FileInputStream(xmlFile)));
        } else if (xmlFile.getName().endsWith(GZ)) {
            xmlStream = new GZIPInputStream((new FileInputStream(xmlFile)));
        } else {
            xmlStream = new FileInputStream(xmlFile);
        }
        return xmlStream;
    }

    /**
     * Formata a sa�da do XML para facilitar a interpreta��o do resultado.
     * Isto � necess�rio para o tratamento do XML exportado pelo JCR que possui o tratamento "Escaping of Names".
     * Ver documenta��o JCR sobre o t�pico "Escaping of Names". 
     * @param inputStream
     * @param outputStream
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SAXException
     */
    private static void formatXML(final InputStream inputStream, final OutputStream outputStream) throws FileNotFoundException, IOException, SAXException {
        final XMLReader reader = XMLReaderFactory.createXMLReader(org.apache.xerces.parsers.SAXParser.class.getName());
        reader.setContentHandler(new XMLSerializer(outputStream, XmlUtils.OUTPUT_FORMAT));
        reader.parse(new InputSource(inputStream));
        IOUtils.closeQuietly(inputStream);
    }

    private static File createFile(final String name, final File folder) throws IOException {
        String vName = StringUtils.replaceChars(name, ':', '-');
        vName = StringUtils.replaceChars(vName, '\\', '-');
        vName = StringUtils.replaceChars(vName, '/', '-');
        vName = StringUtils.replaceChars(vName, '*', '-');
        vName = StringUtils.replaceChars(vName, '?', '-');
        vName = StringUtils.replaceChars(vName, '"', '-');
        vName = StringUtils.replaceChars(vName, '>', '-');
        vName = StringUtils.replaceChars(vName, '<', '-');
        vName = StringUtils.replaceChars(vName, '|', '-');
        return File.createTempFile(vName, XML, folder);
    }
}

/**
 * Classe para filtrar apenas arquivos ZIP e XML para a importa��o a partir 
 * de um diret�rio.
 * @author wsouza
 *
 */
class XmlZipFilesFilter implements java.io.FileFilter {

    public boolean accept(File toFile) {
        if (toFile == null) {
            return (false);
        }
        String lcExt = FilenameUtils.getExtension(toFile.getName());
        if (lcExt == null) {
            return (false);
        }
        return (lcExt.compareToIgnoreCase("zip") == 0 || lcExt.compareToIgnoreCase("xml") == 0);
    }
}
