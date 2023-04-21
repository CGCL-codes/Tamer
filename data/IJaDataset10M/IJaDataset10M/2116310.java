package org.pagger.data.picture.xmp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pagger.data.LocaleArrayFactory;
import org.pagger.data.LocaleFactory;
import org.pagger.data.Metadata;
import org.pagger.data.MetadataPropertyTester;
import org.pagger.data.Property;
import org.pagger.data.picture.Jpeg;
import org.pagger.data.picture.TestHelper;
import org.pagger.data.picture.io.JpegIO;

public class XmpDublinCoreSchemaTest {

    private JpegIO _io = new JpegIO();

    private Jpeg _image;

    @Before
    public void initialize() throws IOException, URISyntaxException {
        final File image = TestHelper.getResource("/org/pagger/data/picture/test_002.jpg");
        _image = _io.read(image.toURI());
    }

    @Test
    public void getPropertiesValue() {
        final XmpRawMetadata xmpMetadata = _image.getXmpRawMetadata();
        Assert.assertNotNull(xmpMetadata);
        final XmpDublinCoreSchema schema = (XmpDublinCoreSchema) xmpMetadata.getSchema(XmpDublinCoreSchema.NS);
        Assert.assertNotNull(schema);
        for (Property<?> prop : schema.getProperties()) {
            schema.getValue(prop);
        }
    }

    @Test
    public void setGetProperty() {
        final XmpRawMetadata xmpMetadata = _image.getXmpRawMetadata();
        Assert.assertNotNull(xmpMetadata);
        final XmpDublinCoreSchema schema = (XmpDublinCoreSchema) xmpMetadata.getSchema(XmpDublinCoreSchema.NS);
        Assert.assertNotNull(schema);
        final MetadataPropertyTester tester = new MetadataPropertyTester();
        tester.addObjectFactory(LanguageAlternative.class, new LanguageAlternativeFactory());
        tester.addObjectFactory(LanguageAlternative[].class, new LanguageAlternativeArrayFactory());
        tester.addObjectFactory(Locale.class, new LocaleFactory());
        tester.addObjectFactory(Locale[].class, new LocaleArrayFactory());
        tester.exclude(XmpDublinCoreSchema.DESCRIPTION);
        tester.exclude(XmpDublinCoreSchema.RIGHTS);
        tester.exclude(XmpDublinCoreSchema.TITLE);
        tester.test(schema);
    }

    @Test
    public void read() {
        XmpRawMetadata xmpMetadata = _image.getXmpRawMetadata();
        Assert.assertNotNull(xmpMetadata);
        XmpDublinCoreSchema dublinCoreSchema = (XmpDublinCoreSchema) xmpMetadata.getSchema(XmpDublinCoreSchema.NS);
        Assert.assertNotNull(dublinCoreSchema);
        final String[] creator = dublinCoreSchema.getValue(XmpDublinCoreSchema.CREATOR);
        Assert.assertNotNull(creator);
        Assert.assertEquals("Gerd Saurer", creator[0]);
        final String[] subject = dublinCoreSchema.getValue(XmpDublinCoreSchema.SUBJECT);
        Assert.assertNotNull(subject);
        Assert.assertEquals("San Francisco", subject[0]);
        Assert.assertEquals("Vereinigte Staaten", subject[1]);
    }

    public void printI18n() {
        final Metadata metadata = _image.getXmpRawMetadata().getSchema(XmpDublinCoreSchema.NS);
        String clazzName = metadata.getClass().getName();
        clazzName = clazzName.substring(clazzName.lastIndexOf(".") + 1);
        System.out.println("# " + clazzName);
        System.out.println();
        System.out.println(metadata.getName() + "=" + clazzName);
        System.out.println(metadata.getDescription() + "=" + clazzName);
        for (Property<?> prop : metadata.getProperties()) {
            System.out.println(prop.getName() + "=" + prop.getId());
            System.out.println(prop.getDescription() + "=" + prop.getId());
        }
    }
}
