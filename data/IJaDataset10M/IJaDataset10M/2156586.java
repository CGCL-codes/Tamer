package br.com.nix.map.display;

import java.awt.Stroke;
import org.apache.commons.collections15.Transformer;
import br.com.nix.beans.edge.Edge;

public class EdgeStroke implements Transformer<Edge, Stroke> {

    public Stroke transform(Edge e) {
        return e.getStroke();
    }
}
