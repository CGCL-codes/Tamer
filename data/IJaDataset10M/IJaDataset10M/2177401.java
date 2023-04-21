package com.phloc.commons.microdom.reader;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.IOutputStreamProvider;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

/**
 * Simple class that reads a generic String-to-String mapping from a classpath
 * resource into a {@link Map}.<br>
 * The XML file needs to look as follows:
 * 
 * <pre>
 * &lt;mapping>
 *   &lt;map key="..." value="..."/>
 *   &lt;map key="..." value="..."/>
 *   ...
 * &lt;/mapping>
 * </pre>
 * 
 * @author Philip
 */
@Immutable
public final class XMLMapHandler {

    public static final String ELEMENT_MAPPING = "mapping";

    public static final String ELEMENT_MAP = "map";

    public static final String ATTR_KEY = "key";

    public static final String ATTR_VALUE = "value";

    private static final Logger s_aLogger = LoggerFactory.getLogger(XMLMapHandler.class);

    @PresentForCodeCoverage
    @SuppressWarnings("unused")
    private static final XMLMapHandler s_aInstance = new XMLMapHandler();

    private XMLMapHandler() {
    }

    @Nullable
    @ReturnsMutableCopy
    public static Map<String, String> readMap(@Nonnull final IInputStreamProvider aISP) {
        if (aISP == null) throw new NullPointerException("ISP");
        return readMap(aISP.getInputStream());
    }

    @Nonnull
    public static ESuccess readMap(@Nonnull final IInputStreamProvider aISP, @Nonnull final Map<String, String> aTargetMap) {
        if (aISP == null) throw new NullPointerException("ISP");
        return readMap(aISP.getInputStream(), aTargetMap);
    }

    /**
   * Read a mapping from the passed input stream.
   * 
   * @param aIS
   *        The input stream to read from. May not be <code>null</code>.
   * @return <code>null</code> if reading the map failed
   */
    @Nullable
    @ReturnsMutableCopy
    public static Map<String, String> readMap(@Nonnull @WillClose final InputStream aIS) {
        final Map<String, String> ret = new HashMap<String, String>();
        if (readMap(aIS, ret).isFailure()) return null;
        return ret;
    }

    /**
   * Read a mapping from the passed input stream.
   * 
   * @param aIS
   *        The input stream to read from. May not be <code>null</code>.
   * @param aTargetMap
   *        The target map to be filled.
   * @return {@link ESuccess#SUCCESS} if the stream could be opened, if it could
   *         be read as XML and if the root element was correct.
   *         {@link ESuccess#FAILURE} otherwise.
   */
    @Nonnull
    public static ESuccess readMap(@Nonnull @WillClose final InputStream aIS, @Nonnull final Map<String, String> aTargetMap) {
        if (aIS == null) throw new NullPointerException("inputStream");
        if (aTargetMap == null) throw new NullPointerException("targetMap");
        try {
            final IMicroDocument aDoc = MicroReader.readMicroXML(aIS);
            if (aDoc != null) {
                for (final IMicroElement eMap : aDoc.getDocumentElement().getChildElements(ELEMENT_MAP)) {
                    final String sName = eMap.getAttribute(ATTR_KEY);
                    if (sName == null) s_aLogger.warn("Ignoring mapping element because key is null"); else {
                        final String sValue = eMap.getAttribute(ATTR_VALUE);
                        if (sValue == null) s_aLogger.warn("Ignoring mapping element because value is null"); else {
                            if (aTargetMap.containsKey(sName)) s_aLogger.warn("Key '" + sName + "' is already contained - overwriting!");
                            aTargetMap.put(sName, sValue);
                        }
                    }
                }
                return ESuccess.SUCCESS;
            }
        } catch (final Throwable t) {
            s_aLogger.warn("Failed to read mapping resource '" + aIS + "'", t);
        } finally {
            StreamUtils.close(aIS);
        }
        return ESuccess.FAILURE;
    }

    @Nonnull
    public static ESuccess writeMap(@Nonnull final Map<String, String> aMap, @Nonnull final IOutputStreamProvider aOSP) {
        if (aOSP == null) throw new NullPointerException("outputStreamProvider");
        return writeMap(aMap, aOSP.getOutputStream(EAppend.DEFAULT));
    }

    /**
   * Write the passed map to the passed output stream using the predefined XML
   * layout.
   * 
   * @param aMap
   *        The map to be written. May not be <code>null</code>.
   * @param aOS
   *        The output stream to write to. The stream is closed independent of
   *        success or failure. May not be <code>null</code>.
   * @return {@link ESuccess#SUCCESS} when everything went well,
   *         {@link ESuccess#FAILURE} otherwise.
   */
    @Nonnull
    public static ESuccess writeMap(@Nonnull final Map<String, String> aMap, @Nonnull @WillClose final OutputStream aOS) {
        if (aMap == null) throw new NullPointerException("map");
        if (aOS == null) throw new NullPointerException("outputStream");
        try {
            final IMicroDocument aDoc = new MicroDocument();
            final IMicroElement eRoot = aDoc.appendElement(ELEMENT_MAPPING);
            for (final Map.Entry<String, String> aEntry : aMap.entrySet()) {
                final IMicroElement eMap = eRoot.appendElement(ELEMENT_MAP);
                eMap.setAttribute(ATTR_KEY, aEntry.getKey());
                eMap.setAttribute(ATTR_VALUE, aEntry.getValue());
            }
            MicroWriter.writeToStream(aDoc, aOS, XMLWriterSettings.DEFAULT_XML_SETTINGS);
            return ESuccess.SUCCESS;
        } finally {
            StreamUtils.close(aOS);
        }
    }
}
