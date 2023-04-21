package com.vmware.spring.workshop.facade.support.views;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.Assert;
import com.vmware.spring.workshop.dto.IdentifiedDTO;
import com.vmware.spring.workshop.facade.AbstractFacadeTestSupport;
import com.vmware.spring.workshop.facade.support.marshal.JaxbClassesMarshaller;
import com.vmware.spring.workshop.facade.support.marshal.JaxbPackagesMarshaller;

/**
 * @author lgoldstein
 */
public abstract class AbstractDTOMarshallingTestSupport<DTO extends IdentifiedDTO, L extends List<DTO>> extends AbstractFacadeTestSupport {

    protected final Class<DTO> _dtoClass;

    protected final Class<L> _listClass;

    protected final Constructor<L> _listConstructor, _sizedConstructor;

    @Inject
    @Named("oxmMarshaller")
    protected Marshaller _oxmMarshaller;

    @Inject
    @Named("oxmMarshaller")
    protected Unmarshaller _oxmUnmarshaller;

    protected AbstractDTOMarshallingTestSupport(final Class<DTO> dtoClass, final Class<L> listClass) throws IntrospectionException, SecurityException, NoSuchMethodException {
        Assert.state((_dtoClass = dtoClass) != null, "No DTO class provided");
        Assert.state((_listClass = listClass) != null, "No DTO list class provided");
        _listConstructor = listClass.getConstructor(Collection.class);
        _sizedConstructor = listClass.getConstructor(Integer.TYPE);
    }

    @Test
    public void testSimpleInstanceJAXBClassesMarshalling() throws Exception {
        final JaxbClassesMarshaller m = new JaxbClassesMarshaller(_dtoClass);
        runInstanceMarshallingTest(m, m);
    }

    @Test
    public void testSimpleListJAXBClassesMarshalling() throws Exception {
        final JaxbClassesMarshaller m = new JaxbClassesMarshaller(_dtoClass, _listClass);
        runInstanceListMarshallingTest(m, m);
    }

    @Test
    public void testSimpleInstanceJAXBPackagesMarshalling() throws Exception {
        final JaxbPackagesMarshaller m = getJaxbPackagesMarshaller();
        runInstanceMarshallingTest(m, m);
    }

    @Test
    public void testSimpleListJAXBPackagesMarshalling() throws Exception {
        final JaxbPackagesMarshaller m = getJaxbPackagesMarshaller();
        runInstanceListMarshallingTest(m, m);
    }

    @Test
    public void testSimpleInstanceOXMMarshalling() throws Exception {
        runInstanceMarshallingTest(_oxmMarshaller, _oxmUnmarshaller);
    }

    @Test
    public void testSimpleListOXMMarshalling() throws Exception {
        runInstanceListMarshallingTest(_oxmMarshaller, _oxmUnmarshaller);
    }

    protected static final JaxbPackagesMarshaller getJaxbPackagesMarshaller() throws Exception {
        return new JaxbPackagesMarshaller("com.vmware.spring.workshop.dto", "com.vmware.spring.workshop.dto.user", "com.vmware.spring.workshop.dto.banking");
    }

    protected List<DTO> runInstanceListMarshallingTest(final Marshaller marshal, final Unmarshaller unmarshal) throws InstantiationException, IllegalAccessException, InvocationTargetException, XmlMappingException, IOException, IntrospectionException {
        Assert.isTrue(marshal.supports(_listClass), "Marshaller does not support DTO list class");
        Assert.isTrue(unmarshal.supports(_listClass), "Unmarshaller does not support DTO list class");
        final int MAX_ITEMS = Byte.SIZE;
        final L dtoList = _sizedConstructor.newInstance(Integer.valueOf(MAX_ITEMS));
        for (int index = 0; index < MAX_ITEMS; index++) {
            dtoList.add(createDTOInstance());
        }
        final Writer writer = new StringWriter(1024);
        try {
            marshal.marshal(dtoList, new StreamResult(writer));
        } finally {
            writer.close();
        }
        final Reader reader = new StringReader(writer.toString());
        final Object unmList;
        try {
            unmList = unmarshal.unmarshal(new StreamSource(reader));
        } finally {
            reader.close();
        }
        Assert.notNull(unmList, "No list unmarshalled");
        Assert.isInstanceOf(List.class, unmList, "Unmarshalled object not a list");
        org.junit.Assert.assertNotSame("Identity marshalling", dtoList, unmList);
        final List<?> resList = (List<?>) unmList;
        org.junit.Assert.assertEquals("Mismatched list sizes", dtoList.size(), resList.size());
        if (!CollectionUtils.isEqualCollection(dtoList, resList)) org.junit.Assert.fail("Mismatched lists contents - expected: " + dtoList + ", actual: " + resList);
        return dtoList;
    }

    protected DTO runInstanceMarshallingTest(final Marshaller marshal, final Unmarshaller unmarshal) throws InstantiationException, IllegalAccessException, XmlMappingException, IOException, IntrospectionException {
        Assert.isTrue(marshal.supports(_dtoClass), "Marshaller does not support DTO class");
        Assert.isTrue(unmarshal.supports(_dtoClass), "Unmarshaller does not support DTO class");
        final DTO orgDTO = createDTOInstance();
        final Writer writer = new StringWriter(1024);
        try {
            marshal.marshal(orgDTO, new StreamResult(writer));
        } finally {
            writer.close();
        }
        final Reader reader = new StringReader(writer.toString());
        final Object unmDTO;
        try {
            unmDTO = unmarshal.unmarshal(new StreamSource(reader));
        } finally {
            reader.close();
        }
        Assert.notNull(unmDTO, "No DTO unmarshalled");
        Assert.isInstanceOf(_dtoClass, unmDTO, "Unmarshalled DTO not compatible with DTO class");
        org.junit.Assert.assertNotSame("Identity marshalling", orgDTO, unmDTO);
        org.junit.Assert.assertEquals("Mismatched DTO contents", orgDTO, unmDTO);
        return orgDTO;
    }

    protected L wrapAsList(Collection<? extends DTO> dtoList) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return _listConstructor.newInstance(dtoList);
    }

    protected DTO createDTOInstance() throws InstantiationException, IllegalAccessException, IntrospectionException {
        final DTO dto = _dtoClass.newInstance();
        initializeDTOValues(dto);
        return dto;
    }
}
