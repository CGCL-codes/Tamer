package org.jcvi.vics.compute.service.export.model;

import org.jcvi.vics.compute.access.search.ClusterResult;
import org.jcvi.vics.model.genomics.AnnotationDescription;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: smurphy
 * Date: Aug 1, 2008
 * Time: 10:24:23 AM
 */
public class ClusterResultFormatter extends ColumnFormatter {

    public static final Map<ClusterResultHeader, String> headerMap = new HashMap<ClusterResultHeader, String>();

    public static enum ClusterResultHeader {

        FINAL_CLUSTER, NUM_CORE_CLUSTERS, NUM_PROTEINS, NUM_NON_REDUNDANT, GENE_SYMBOLS, PROTEIN_FUNCTIONS, EC_NUMBER, GENE_ONTOLOGY
    }

    static {
        headerMap.put(ClusterResultHeader.FINAL_CLUSTER, "Final Cluster");
        headerMap.put(ClusterResultHeader.NUM_CORE_CLUSTERS, "# Core Clusters");
        headerMap.put(ClusterResultHeader.NUM_PROTEINS, "# Proteins");
        headerMap.put(ClusterResultHeader.NUM_NON_REDUNDANT, "# Non-Redundant");
        headerMap.put(ClusterResultHeader.GENE_SYMBOLS, "Gene Symbols");
        headerMap.put(ClusterResultHeader.PROTEIN_FUNCTIONS, "Protein Functions");
        headerMap.put(ClusterResultHeader.EC_NUMBER, "EC #");
        headerMap.put(ClusterResultHeader.GENE_ONTOLOGY, "Gene Ontology");
    }

    public static List<String> getHeaderList() {
        List<String> headerList = new ArrayList<String>();
        for (ClusterResultHeader h : ClusterResultHeader.values()) {
            headerList.add(headerMap.get(h));
        }
        return headerList;
    }

    public static List<String> formatColumns(ClusterResult clusterResult) {
        ArrayList<String> fields = new ArrayList<String>();
        fields.add(clusterResult.getFinalAccession());
        fields.add(String.valueOf(clusterResult.getNumCoreClusters()));
        fields.add(String.valueOf(clusterResult.getNumProteins()));
        fields.add(String.valueOf(clusterResult.getNumNRProteins()));
        String geneAnnotation = clusterResult.getGeneSymbols();
        if (geneAnnotation == null || geneAnnotation.length() == 0) {
            fields.add(null);
        } else {
            fields.add(geneAnnotation);
        }
        String proteinFunctionsAnnotation = clusterResult.getProteinFunctions();
        if (proteinFunctionsAnnotation == null || proteinFunctionsAnnotation.length() == 0) {
            fields.add(null);
        } else {
            fields.add(proteinFunctionsAnnotation);
        }
        List annotations = clusterResult.getEcAnnotationDescription();
        StringBuffer annotationField = new StringBuffer();
        for (Object annotationObj : annotations) {
            AnnotationDescription annotation = (AnnotationDescription) annotationObj;
            if (annotationField.length() > 0) {
                annotationField.append(';');
            }
            annotationField.append(annotation.getId());
            annotationField.append(" - ");
            annotationField.append(annotation.getDescription());
        }
        if (annotationField.length() == 0) {
            fields.add(null);
        } else {
            fields.add(annotationField.toString());
        }
        annotations = clusterResult.getGoAnnotationDescription();
        annotationField = new StringBuffer();
        for (Object annotationObj : annotations) {
            AnnotationDescription annotation = (AnnotationDescription) annotationObj;
            if (annotationField.length() > 0) {
                annotationField.append(';');
            }
            annotationField.append(annotation.getId());
            annotationField.append(" - ");
            annotationField.append(annotation.getDescription());
        }
        if (annotationField.length() == 0) {
            fields.add(null);
        } else {
            fields.add(annotationField.toString());
        }
        return fields;
    }
}
