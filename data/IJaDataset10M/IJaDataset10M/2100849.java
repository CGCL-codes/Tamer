package util.bio.annotation;

import java.util.ArrayList;

/**For fetching all combinations of transcripts given alternative overlapping exons*/
public class IndexedExonIntron {

    int indexNumber;

    IndexedExonIntron[] indexes;

    ArrayList<ExonIntron> alternativeExons;

    int maxCombinations;

    public IndexedExonIntron(int indexNumber, IndexedExonIntron[] indexes, ArrayList<ExonIntron> alternativeExons, int maxCombinations) {
        this.indexNumber = indexNumber;
        this.indexes = indexes;
        this.alternativeExons = alternativeExons;
        this.maxCombinations = maxCombinations;
    }

    public ArrayList<ArrayList<ExonIntron>> fetchRight() {
        if (indexNumber == indexes.length - 1) {
            ArrayList<ArrayList<ExonIntron>> toReturn = new ArrayList<ArrayList<ExonIntron>>();
            for (int i = 0; i < alternativeExons.size(); i++) {
                ArrayList<ExonIntron> al = new ArrayList<ExonIntron>();
                al.add(alternativeExons.get(i));
                toReturn.add(al);
            }
            return toReturn;
        }
        ArrayList<ArrayList<ExonIntron>> rightOverlaps = indexes[indexNumber + 1].fetchRight();
        if (rightOverlaps == null || rightOverlaps.size() > maxCombinations) return null;
        ArrayList<ArrayList<ExonIntron>> toReturn = new ArrayList<ArrayList<ExonIntron>>();
        for (int i = 0; i < alternativeExons.size(); i++) {
            for (int j = 0; j < rightOverlaps.size(); j++) {
                ArrayList<ExonIntron> al = new ArrayList<ExonIntron>();
                al.add(alternativeExons.get(i));
                al.addAll(rightOverlaps.get(j));
                toReturn.add(al);
            }
        }
        return toReturn;
    }

    public ArrayList<ExonIntron> getAlternativeExons() {
        return alternativeExons;
    }
}
