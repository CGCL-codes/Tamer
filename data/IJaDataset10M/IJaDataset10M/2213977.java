package org.sam.jspacewars.serialization;

import org.sam.colisiones.Poligono;
import org.sam.jspacewars.servidor.elementos.Canion;
import org.sam.jspacewars.servidor.elementos.CanionInterpolado;
import org.sam.jspacewars.servidor.elementos.CanionLineal;
import org.sam.jspacewars.servidor.elementos.DisparoInterpolado;
import org.sam.jspacewars.servidor.elementos.DisparoLineal;
import org.sam.jspacewars.servidor.elementos.Elemento;
import org.sam.jspacewars.servidor.elementos.LanzaMisiles;
import org.sam.jspacewars.servidor.elementos.Misil;
import org.sam.jspacewars.servidor.elementos.NaveEnemiga;
import org.sam.jspacewars.servidor.elementos.NaveUsuario;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ElementosConverters {

    private static class PoligonoConverter implements Converter {

        private static float getFloatAttribute(HierarchicalStreamReader reader, String att, float defecto) {
            try {
                return Float.parseFloat(reader.getAttribute(att));
            } catch (Exception e) {
                return defecto;
            }
        }

        PoligonoConverter() {
        }

        @SuppressWarnings("rawtypes")
        public boolean canConvert(Class clazz) {
            return Poligono.class == clazz;
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            int nLados = Integer.parseInt(reader.getAttribute("nLados"));
            float scale = getFloatAttribute(reader, "scale", 1.0f);
            float scaleX = getFloatAttribute(reader, "scaleX", scale);
            float scaleY = getFloatAttribute(reader, "scaleY", scale);
            float offX = getFloatAttribute(reader, "offX", 0.0f);
            float offY = getFloatAttribute(reader, "offY", 0.0f);
            float coordX[] = new float[nLados];
            float coordY[] = new float[nLados];
            int i = 0;
            while (i < nLados && reader.hasMoreChildren()) {
                reader.moveDown();
                coordX[i] = Float.parseFloat(reader.getAttribute("x")) * scaleX + offX;
                coordY[i] = Float.parseFloat(reader.getAttribute("y")) * scaleY + offY;
                reader.moveUp();
                i++;
            }
            return new Poligono(coordX, coordY);
        }
    }

    private ElementosConverters() {
    }

    public static void register(XStream xStream) {
        InterpoladoresConverters.register(xStream);
        xStream.useAttributeFor(Elemento.class, "type");
        xStream.alias("Poligono", Poligono.class);
        xStream.registerConverter(new PoligonoConverter());
        xStream.alias("NaveUsuario", NaveUsuario.class);
        xStream.alias("Canion", Canion.class);
        xStream.alias("CanionLineal", CanionLineal.class);
        xStream.alias("DisparoLineal", DisparoLineal.class);
        xStream.alias("CanionInterpolado", CanionInterpolado.class);
        xStream.alias("DisparoInterpolado", DisparoInterpolado.class);
        xStream.alias("LanzaMisiles", LanzaMisiles.class);
        xStream.alias("Misil", Misil.class);
        xStream.alias("NaveEnemiga", NaveEnemiga.class);
        xStream.registerConverter(new Ignorado());
        xStream.alias("Instancia3D", Ignorado.class);
    }
}
