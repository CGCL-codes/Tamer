package it.southdown.avana.predict;

public interface PredictionDataListener {

    public void missingPredition(String peptide, String allele);
}
