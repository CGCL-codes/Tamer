package org.remus.infomngmnt.onlineresource.extension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.cyberneko.html.parsers.DOMParser;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.remus.BinaryReference;
import org.eclipse.remus.common.core.streams.StreamCloser;
import org.eclipse.remus.core.extension.AbstractInformationRepresentation;
import org.eclipse.remus.core.model.InformationStructureRead;
import org.eclipse.remus.js.rendering.FreemarkerRenderer;
import org.eclipse.remus.resources.util.ResourceUtil;
import org.eclipse.remus.util.StatusCreator;
import org.remus.infomngmnt.onlineresource.OnlineResourceActivator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class OnlineMediaInformationRepresentation extends AbstractInformationRepresentation {

    @Override
    public InputStream handleHtmlGeneration(final IProgressMonitor monitor) throws CoreException {
        InformationStructureRead read = InformationStructureRead.newSession(getValue());
        ByteArrayOutputStream returnValue = new ByteArrayOutputStream();
        InputStream templateIs = null;
        Map<String, Object> additionals = new HashMap<String, Object>();
        additionals.put("embedded", parseContent());
        List<BinaryReference> binaryReferences = read.getBinaryReferences(OnlineResourceActivator.NODE_NAME_ATTACHMENT, false);
        if (binaryReferences.size() > 0) {
            String projectRelativePath = binaryReferences.get(0).getProjectRelativePath();
            IPath location = getFile().getProject().getFolder(ResourceUtil.BINARY_FOLDER).getFile(projectRelativePath).getLocation();
            additionals.put("attachment", URI.createFileURI(location.toOSString()));
        }
        try {
            templateIs = FileLocator.openStream(Platform.getBundle(OnlineResourceActivator.PLUGIN_ID), new Path("$nl$/template/htmlserialization.flt"), true);
            FreemarkerRenderer.getInstance().process(OnlineResourceActivator.PLUGIN_ID, templateIs, returnValue, additionals, read.getContentsAsStrucuturedMap(), read.getDynamicContentAsStructuredMap());
        } catch (IOException e) {
            throw new CoreException(StatusCreator.newStatus("Error reading locations", e));
        } finally {
            StreamCloser.closeStreams(templateIs);
        }
        return new ByteArrayInputStream(returnValue.toByteArray());
    }

    private String parseContent() {
        InformationStructureRead read = InformationStructureRead.newSession(getValue());
        String content = (String) read.getValueByNodeId(OnlineResourceActivator.NODE_NAME_EMBED);
        final DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(new StringReader(content)));
            final Document document = parser.getDocument();
            NodeList elementsByTagName = document.getElementsByTagName("a");
            for (int i = 0; i < elementsByTagName.getLength(); i++) {
                final Node node = elementsByTagName.item(i);
                ((Element) node).setAttribute("target", "_blank");
            }
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            final DOMSource source = new DOMSource(document);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            return writer.toString();
        } catch (Exception e) {
        }
        return content;
    }
}
