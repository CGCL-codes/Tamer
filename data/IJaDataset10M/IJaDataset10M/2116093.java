package visugraph.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import visugraph.data.Attribute;
import visugraph.graph.Graph;

/**
 * Classe abstraire qui facilite l'utilisation de l'interface GraphExporter.
 * Elle permet notamment de s'abstraire de la destination de l'enregistrement (flux ou fichier).
 * Pour étendre cette classe, il suffit juste d'implémenter la méthode save() relative aux flux.
 */
public abstract class AbstractExporter<N, E> implements GraphExporter<N, E> {

    /**
	 * Charge le fichier et passe le writer à save(writer, ...).
	 */
    public void save(String filename, Graph<N, E> graph) throws IOException {
        OutputStream stream = new FileOutputStream(filename);
        try {
            this.save(stream, graph);
        } finally {
            stream.close();
        }
    }

    public void setNodeAttributes(Collection<? extends Attribute<N, ?>> attrs) {
        throw new UnsupportedOperationException("Cet exporteur n'accepte aucun attribut sur les noeuds");
    }

    public void setEdgeAttributes(Collection<? extends Attribute<E, ?>> attrs) {
        throw new UnsupportedOperationException("Cet exporteur n'accepte aucun attribut sur les arêtes");
    }
}
