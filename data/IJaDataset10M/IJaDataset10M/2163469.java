package org.kineticsystem.blender.tmp;

import java.io.UnsupportedEncodingException;
import org.kineticsystem.blender.utils.ColorUtils;

public class RunMe {

    public RunMe() {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + "<!-- Created with Inkscape (http://www.inkscape.org/) -->\n" + "<svg\n" + "   xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n" + "   xmlns:cc=\"http://creativecommons.org/ns#\"\n" + "   xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" + "   xmlns:svg=\"http://www.w3.org/2000/svg\"\n" + "   xmlns=\"http://www.w3.org/2000/svg\"\n" + "   xmlns:sodipodi=\"http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd\"\n" + "   xmlns:inkscape=\"http://www.inkscape.org/namespaces/inkscape\"\n" + "   width=\"200\"\n" + "   height=\"10\"\n" + "   id=\"svg2\"\n" + "   version=\"1.1\"\n" + "   inkscape:version=\"0.47pre4 \"\n" + "   sodipodi:docname=\"square.svg\">\n" + "  <defs\n" + "     id=\"defs89\">\n" + "    <inkscape:perspective\n" + "       sodipodi:type=\"inkscape:persp3d\"\n" + "       inkscape:vp_x=\"0 : 5 : 1\"\n" + "       inkscape:vp_y=\"0 : 1000 : 0\"\n" + "       inkscape:vp_z=\"100 : 5 : 1\"\n" + "       inkscape:persp3d-origin=\"50 : 3.3333333 : 1\"\n" + "       id=\"perspective91\" />\n" + "  </defs>\n" + "  <sodipodi:namedview\n" + "     pagecolor=\"#ffffff\"\n" + "     bordercolor=\"#666666\"\n" + "     borderopacity=\"1\"\n" + "     objecttolerance=\"10\"\n" + "     gridtolerance=\"10\"\n" + "     guidetolerance=\"10\"\n" + "     inkscape:pageopacity=\"0\"\n" + "     inkscape:pageshadow=\"2\"\n" + "     inkscape:window-width=\"1600\"\n" + "     inkscape:window-height=\"1148\"\n" + "     id=\"namedview87\"\n" + "     showgrid=\"false\"\n" + "     inkscape:zoom=\"10.8\"\n" + "     inkscape:cx=\"58.680938\"\n" + "     inkscape:cy=\"19.65733\"\n" + "     inkscape:window-x=\"-8\"\n" + "     inkscape:window-y=\"-8\"\n" + "     inkscape:window-maximized=\"1\"\n" + "     inkscape:current-layer=\"layer1\" />\n" + "  <metadata\n" + "     id=\"metadata7\">\n" + "    <rdf:RDF>\n" + "      <cc:Work\n" + "         rdf:about=\"\">\n" + "        <dc:format>image/svg+xml</dc:format>\n" + "        <dc:type\n" + "           rdf:resource=\"http://purl.org/dc/dcmitype/StillImage\" />\n" + "        <dc:title />\n" + "      </cc:Work>\n" + "    </rdf:RDF>\n" + "  </metadata>\n" + "  <g\n" + "     inkscape:label=\"Layer 1\"\n" + "     inkscape:groupmode=\"layer\"\n" + "     id=\"layer1\">\n";
        String footer = "</g>\n" + "</svg>\n";
        String tmpl = "<rect style=\"fill:#%s;fill-rule:evenodd;stroke:none;\" width=\"%s\" height=\"10\" x=\"%s\" y=\"0\"/>\n";
        System.out.print(header);
        String oldHex = "000000";
        int oldI = 0;
        int n = 200;
        for (int i = 0; i < n; i++) {
            double wl = 380 + i * (780 - 380) / n;
            double[] rgb = ColorUtils.wlToRgb(wl);
            double r = rgb[0];
            double g = rgb[1];
            double b = rgb[2];
            int r1 = (int) (r * 255);
            int g1 = (int) (g * 255);
            int b1 = (int) (b * 255);
            String hex = byteToString(r1) + byteToString(g1) + byteToString(b1);
            if (!hex.equals(oldHex)) {
                System.out.printf(tmpl, oldHex, i - oldI + 1, oldI);
                oldI = i;
                oldHex = hex;
            }
        }
        System.out.print(footer);
    }

    static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f' };

    public static String byteToString(int raw) {
        byte[] hex = new byte[2];
        hex[0] = HEX_CHAR_TABLE[raw >>> 4];
        hex[1] = HEX_CHAR_TABLE[raw & 0xF];
        String str = "";
        try {
            str = new String(hex, "ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void main(String[] args) {
        new RunMe();
    }
}
