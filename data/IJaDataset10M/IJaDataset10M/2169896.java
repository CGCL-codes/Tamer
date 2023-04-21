package org.remus.infomngmnt.video.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.remus.BinaryReference;
import org.eclipse.remus.InformationUnit;
import org.eclipse.remus.common.core.streams.StreamCloser;
import org.eclipse.remus.core.extension.IEmitter;
import org.eclipse.remus.core.model.InformationStructureRead;
import org.eclipse.remus.js.rendering.FreemarkerRenderer;
import org.eclipse.remus.util.InformationUtil;
import org.eclipse.remus.util.StatusCreator;
import org.remus.infomngmnt.emitter.uixml.UiXml;
import org.remus.infomngmnt.video.VideoActivator;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class UIXMLEmitter implements IEmitter {

    public void preEmitt() {
    }

    public Object emitt(final InformationUnit element) throws CoreException {
        ByteArrayOutputStream returnValue = new ByteArrayOutputStream();
        InputStream templateIs = null;
        InformationStructureRead read = InformationStructureRead.newSession(element);
        List<BinaryReference> binaryReferences = read.getBinaryReferences();
        String id = null;
        if (binaryReferences.size() == 1) {
            id = binaryReferences.get(0).getId();
        }
        Long valueByNodeId = (Long) read.getValueByNodeId(VideoActivator.NODE_NAME_WIDTH);
        Map<String, String> additionals = new HashMap<String, String>();
        additionals.put("refId", id);
        additionals.put("width", String.valueOf(valueByNodeId));
        try {
            templateIs = FileLocator.openStream(Platform.getBundle(VideoActivator.PLUGIN_ID), new Path("template/uiserialization.flt"), true);
            FreemarkerRenderer.getInstance().process(VideoActivator.PLUGIN_ID, templateIs, returnValue, additionals, read.getContentsAsStrucuturedMap(), read.getDynamicContentAsStructuredMap());
        } catch (IOException e) {
            throw new CoreException(StatusCreator.newStatus("Error reading locations", e));
        } finally {
            StreamCloser.closeStreams(templateIs);
        }
        try {
            UiXml uixml = new UiXml();
            uixml.setXmlString(new String(returnValue.toByteArray(), "UTF-8"));
            uixml.setBinaryReferences(Collections.singletonMap(id, InformationUtil.binaryReferenceToFile(binaryReferences.get(0), element)));
            return uixml;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    public void postEmitt() {
    }
}
