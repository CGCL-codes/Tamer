package buscador;

import java.util.*;
import org.carrot2.core.ProcessingResult;

public class ResultadosClustering {

    private List<ClusterClustering> clusters;

    private ProcessingResult resultadosCarrot;

    public ResultadosClustering() {
        clusters = new LinkedList<ClusterClustering>();
    }

    public List<ClusterClustering> getClusters() {
        return clusters;
    }

    public long cantidadDocumentosRecuperados() {
        return resultadosCarrot.getDocuments().size();
    }

    public void setClusters(List<ClusterClustering> clusters) {
        this.clusters = clusters;
    }

    public ProcessingResult getResultadosCarrot() {
        return resultadosCarrot;
    }

    public void setResultadosCarrot(ProcessingResult resultadosCarrot) {
        this.resultadosCarrot = resultadosCarrot;
    }
}
