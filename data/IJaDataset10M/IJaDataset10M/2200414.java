package com.google.gdata.model;

import com.google.gdata.util.common.base.Charsets;
import com.google.gdata.util.ParseException;
import com.google.gdata.wireformats.ContentCreationException;
import com.google.gdata.wireformats.ContentValidationException;
import com.google.gdata.wireformats.WireFormat;
import com.google.gdata.wireformats.WireFormatGenerator;
import com.google.gdata.wireformats.WireFormatParser;
import com.google.gdata.wireformats.input.InputProperties;
import com.google.gdata.wireformats.input.InputPropertiesBuilder;
import com.google.gdata.wireformats.output.OutputProperties;
import com.google.gdata.wireformats.output.OutputPropertiesBuilder;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * Arbitrary self-contained block of XML.
 * <p>
 * Need to support multi-rooted content e.g. {@code <a/><b/>}.
 */
public class XmlBlob extends Element {

    /**
   * The id-less key for XmlBlob, a real name must be set before being used.
   */
    public static final ElementKey<Void, XmlBlob> KEY = ElementKey.of(null, XmlBlob.class);

    /**
   * Constructs a new instance using the specified element key.
   *
   * @param key the element key for this element.
   */
    public XmlBlob(ElementKey<?, ? extends XmlBlob> key) {
        super(key);
    }

    /**
   * Initial {@code xml:lang} value. This value is typically inherited through
   * the XML tree. The blob itself may contain overrides.
   * <p>
   *
   * See http://www.w3.org/TR/REC-xml/#sec-lang-tag for more information.
   */
    public String getLang() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    public void setLang(String v) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    /**
   * Initial {@code xml:base} value. This value is typically inherited through
   * the XML tree. The blob itself may contain overrides.
   * <p>
   *
   * See http://www.cafeconleche.org/books/xmljava/chapters/ch03s03.html for
   * more information.
   */
    public String getBase() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    public void setBase(String v) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    /**
   * Get blob content. Depending on how the blob was parsed, it may contain
   * top-level text() nodes mixed together with child elements.
   *
   * @return blob content
   */
    public String getBlob() {
        StringWriter sw = new StringWriter();
        MetadataRegistry registry = DefaultRegistry.get();
        OutputProperties outProps = new OutputPropertiesBuilder().build();
        WireFormatGenerator generator = WireFormat.XML.createGenerator(outProps, sw, Charsets.UTF_8, false);
        try {
            generator.generate(this);
        } catch (ContentValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sw.toString();
    }

    /**
   * Set blob content.
   *
   * @param v blob content
   */
    public void setBlob(String v) {
        clear();
        MetadataRegistry registry = DefaultRegistry.get();
        InputProperties inProps = new InputPropertiesBuilder().build();
        WireFormatParser parser = WireFormat.XML.createParser(inProps, new StringReader(v), Charset.forName("utf-8"));
        try {
            parser.parse(this);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (ContentCreationException e) {
            throw new RuntimeException(e);
        } catch (ContentValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
   * Get blob content full-text indexing.
   */
    public String getFullText() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    /**
   * Set blob content full-text indexing.
   */
    public void setFullText(String v) {
        throw new UnsupportedOperationException("Not supported yet");
    }
}
