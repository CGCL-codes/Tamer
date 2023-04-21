package net.disy.ogc.gml.v_3_1_1.dwr;

import java.util.Map;
import net.disy.commons.dwr.convert.AbstractConverter;
import net.opengis.gml.v_3_1_1.LineStringType;
import net.opengis.gml.v_3_1_1.MultiLineStringType;
import net.opengis.gml.v_3_1_1.MultiPointType;
import net.opengis.gml.v_3_1_1.MultiPolygonType;
import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.gml.v_3_1_1.PolygonType;
import org.directwebremoting.dwrp.ParseUtil;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.util.LocalUtil;

public class GeometryConverter extends AbstractConverter {

    @Override
    protected Object convertInbound(@SuppressWarnings({ "rawtypes" }) Class paramType, InboundVariable data, Map<String, String> tokens, InboundContext inctx, String value) throws MarshallException {
        final String typeString = tokens.get("type");
        {
            String[] split = ParseUtil.splitInbound(typeString);
            String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];
            String splitType = split[LocalUtil.INBOUND_INDEX_TYPE];
            InboundVariable nested = new InboundVariable(data.getLookup(), null, splitType, splitValue);
            final String type = (String) getConverterManager().convertInbound(String.class, nested, inctx, null);
            if ("Point".equals(type)) {
                return getConverterManager().convertInbound(PointType.class, data, inctx, null);
            } else if ("LineString".equals(type)) {
                return getConverterManager().convertInbound(LineStringType.class, data, inctx, null);
            } else if ("Polygon".equals(type)) {
                return getConverterManager().convertInbound(PolygonType.class, data, inctx, null);
            } else if ("MultiPoint".equals(type)) {
                return getConverterManager().convertInbound(MultiPointType.class, data, inctx, null);
            } else if ("MultiLineString".equals(type)) {
                return getConverterManager().convertInbound(MultiLineStringType.class, data, inctx, null);
            } else if ("MultiPolygon".equals(type)) {
                return getConverterManager().convertInbound(MultiPolygonType.class, data, inctx, null);
            } else {
                throw new MarshallException(paramType);
            }
        }
    }
}
