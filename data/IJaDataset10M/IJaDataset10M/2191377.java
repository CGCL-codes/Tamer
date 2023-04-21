package net.disy.ogc.wps.v_1_0_0.model.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.disy.ogc.wps.v_1_0_0.model.DataType;
import net.disy.ogc.wps.v_1_0_0.model.DefaultLiteralTypeRegistry;
import net.disy.ogc.wps.v_1_0_0.model.LiteralType;
import org.junit.Before;
import org.junit.Test;

public class DefaultLiteralTypeRegistryTest {

    private DefaultLiteralTypeRegistry typeRegistry;

    private DataType<Object> dataType;

    @Before
    public void before() {
        typeRegistry = new DefaultLiteralTypeRegistry();
        dataType = mock(DataType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyGetType() throws Exception {
        typeRegistry.getLiteralType(dataType);
    }

    @Test
    public void singleType() throws Exception {
        LiteralType<Object> literalType = mock(LiteralType.class);
        when(literalType.getDataType()).thenReturn(dataType);
        typeRegistry.add(literalType);
        assertThat(typeRegistry.getLiteralType(dataType), is(literalType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongTypeGetType() throws Exception {
        LiteralType<Object> literalType = mock(LiteralType.class);
        when(literalType.getDataType()).thenReturn(dataType);
        typeRegistry.add(literalType);
        DataType<Object> dataType2 = mock(DataType.class);
        typeRegistry.getLiteralType(dataType2);
    }

    @Test
    public void wrongTypeIsLiteral() throws Exception {
        LiteralType<Object> literalType = mock(LiteralType.class);
        when(literalType.getDataType()).thenReturn(dataType);
        typeRegistry.add(literalType);
        DataType<Object> dataType2 = mock(DataType.class);
    }

    @Test
    public void twoTypes() throws Exception {
        LiteralType<Object> literalType = mock(LiteralType.class);
        when(literalType.getDataType()).thenReturn(dataType);
        typeRegistry.add(literalType);
        DataType<Object> dataType2 = mock(DataType.class);
        LiteralType<Object> literalType2 = mock(LiteralType.class);
        when(literalType2.getDataType()).thenReturn(dataType2);
        typeRegistry.add(literalType2);
        assertThat(typeRegistry.getLiteralType(dataType), is(literalType));
        assertThat(typeRegistry.getLiteralType(dataType2), is(literalType2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void sameTypes() throws Exception {
        LiteralType<Object> literalType = mock(LiteralType.class);
        when(literalType.getDataType()).thenReturn(dataType);
        typeRegistry.add(literalType);
        LiteralType<Object> literalType2 = mock(LiteralType.class);
        when(literalType2.getDataType()).thenReturn(dataType);
        typeRegistry.add(literalType2);
    }
}
