package org.sam.colisiones;

import org.sam.util.Reflexion;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Clase para testear la serializacion en XML de un {@code Poligono}.
 */
public class Test06SerializePoligonos {

    /**
	 * Clase privada para evitar repetir las cadenas.
	 */
    private static class S {

        private static final String Poligono = "Poligono";

        private static final String nLados = "nLados";

        private static final String coordX = "coordX";

        private static final String coordY = "coordY";

        private static final String Punto2F = "Punto2F";

        private static final String x = "x";

        private static final String y = "y";
    }

    private static class PoligonoConverter implements Converter {

        @SuppressWarnings("rawtypes")
        public boolean canConvert(Class clazz) {
            return Poligono.class == clazz;
        }

        private static float[] getArray(Poligono p, String name) {
            java.lang.reflect.Field f = Reflexion.findField(Poligono.class, name);
            float[] array = null;
            try {
                boolean accesible = f.isAccessible();
                f.setAccessible(true);
                array = (float[]) f.get(p);
                f.setAccessible(accesible);
                return array;
            } catch (IllegalAccessException ignorada) {
                return null;
            }
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
            Poligono p = (Poligono) value;
            int nLados = p.getNLados();
            writer.addAttribute(S.nLados, ((Integer) nLados).toString());
            float coordX[] = getArray(p, S.coordX);
            float coordY[] = getArray(p, S.coordY);
            for (int i = 0; i < nLados; i++) {
                writer.startNode(S.Punto2F);
                writer.addAttribute(S.x, ((Float) coordX[i]).toString());
                writer.addAttribute(S.y, ((Float) coordY[i]).toString());
                writer.endNode();
            }
        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            int nLados = Integer.parseInt(reader.getAttribute(S.nLados));
            float coordX[] = new float[nLados];
            float coordY[] = new float[nLados];
            int i = 0;
            while (i < nLados && reader.hasMoreChildren()) {
                reader.moveDown();
                coordX[i] = Float.parseFloat(reader.getAttribute(S.x));
                coordY[i] = Float.parseFloat(reader.getAttribute(S.y));
                reader.moveUp();
                i++;
            }
            return new Poligono(coordX, coordY);
        }
    }

    /**
	 * Método principal encargado de lanzar este test.
	 * @param args ignorados.
	 */
    public static void main(String args[]) {
        XStream xStream = new XStream(new DomDriver());
        xStream.registerConverter(new PoligonoConverter());
        xStream.alias(S.Poligono, Poligono.class);
        Poligono poligono = (Poligono) xStream.fromXML("<Poligono nLados=\"30\">" + "  <Punto2F x=\"0.4346424\" y=\"-0.021266539\"/>" + "  <Punto2F x=\"0.35709032\" y=\"0.11248199\"/>" + "  <Punto2F x=\"0.27233785\" y=\"0.1317237\"/>" + "  <Punto2F x=\"0.2610543\" y=\"0.18786925\"/>" + "  <Punto2F x=\"0.44507104\" y=\"0.40735522\"/>" + "  <Punto2F x=\"0.4059035\" y=\"0.70943046\"/>" + "  <Punto2F x=\"0.08746025\" y=\"0.121447995\"/>" + "  <Punto2F x=\"0.079562776\" y=\"0.34834272\"/>" + "  <Punto2F x=\"0.03354211\" y=\"0.25248396\"/>" + "  <Punto2F x=\"0.023557078\" y=\"0.106632754\"/>" + "  <Punto2F x=\"-0.44694552\" y=\"0.8297775\"/>" + "  <Punto2F x=\"-0.25851014\" y=\"0.3285682\"/>" + "  <Punto2F x=\"-0.42461315\" y=\"0.31489292\"/>" + "  <Punto2F x=\"-0.37330607\" y=\"0.21952209\"/>" + "  <Punto2F x=\"-0.7430642\" y=\"0.21223488\"/>" + "  <Punto2F x=\"-0.15596378\" y=\"-0.003588276\"/>" + "  <Punto2F x=\"-0.4420117\" y=\"-0.09097305\"/>" + "  <Punto2F x=\"-0.38847634\" y=\"-0.15956959\"/>" + "  <Punto2F x=\"-0.25881538\" y=\"-0.20132351\"/>" + "  <Punto2F x=\"-0.48862433\" y=\"-0.65875226\"/>" + "  <Punto2F x=\"-0.113713466\" y=\"-0.32039472\"/>" + "  <Punto2F x=\"-0.2486592\" y=\"-0.74893796\"/>" + "  <Punto2F x=\"0.04300569\" y=\"-0.44438168\"/>" + "  <Punto2F x=\"0.12725313\" y=\"-0.47788882\"/>" + "  <Punto2F x=\"0.26783845\" y=\"-0.6727951\"/>" + "  <Punto2F x=\"0.08163352\" y=\"-0.030746374\"/>" + "  <Punto2F x=\"0.12952757\" y=\"-0.056150854\"/>" + "  <Punto2F x=\"0.05342065\" y=\"0.014213836\"/>" + "  <Punto2F x=\"0.7180337\" y=\"-0.3076169\"/>" + "  <Punto2F x=\"0.5217694\" y=\"-0.102591746\"/>" + "</Poligono>");
        System.out.println(xStream.toXML(poligono));
    }
}
