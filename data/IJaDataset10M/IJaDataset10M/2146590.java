package org.apache.ibatis.type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Test;

public class ByteTypeHandlerTest extends BaseTypeHandlerTest {

    private static final TypeHandler<Byte> TYPE_HANDLER = new ByteTypeHandler();

    @Test
    public void shouldSetParameter() throws Exception {
        TYPE_HANDLER.setParameter(ps, 1, (byte) 100, null);
        verify(ps).setByte(1, (byte) 100);
    }

    @Test
    public void shouldGetResultFromResultSet() throws Exception {
        when(rs.getByte("column")).thenReturn((byte) 100);
        when(rs.wasNull()).thenReturn(false);
        assertEquals(new Byte((byte) 100), TYPE_HANDLER.getResult(rs, "column"));
    }

    @Test
    public void shouldGetResultFromCallableStatement() throws Exception {
        when(cs.getByte(1)).thenReturn((byte) 100);
        when(cs.wasNull()).thenReturn(false);
        assertEquals(new Byte((byte) 100), TYPE_HANDLER.getResult(cs, 1));
    }
}
