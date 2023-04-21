package es.randres.remotesensing.geometry.interpoler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import es.randres.remotesensing.geometry.affine.Affine;

public class Interpoler {

    public enum Type {

        NEAREST_NEIGHTBOR, BILINEAR
    }

    private HashMap<String, PixelData> map;

    private Type type;

    public Interpoler(HashMap<String, PixelData> map, Type type) {
        super();
        this.map = map;
        this.type = type;
    }

    private Neighborhood getNeighborhood(int oRow, int oCol) {
        Neighborhood nh = new Neighborhood(oRow, oCol);
        int radius = 1;
        while (!nh.isFull() && radius < 10) {
            for (int dy = -1 * radius; dy <= radius; dy++) {
                for (int dx = -1 * radius; dx <= radius; dx++) {
                    int row = oRow + dy;
                    int col = oCol + dx;
                    if (dy == -1 * radius || dy == radius) {
                        if (map.containsKey(Affine.getKey(row, col))) {
                            nh.addNeighbor(row, col);
                        }
                    } else if (dx == -1 * radius || dx == radius) {
                        if (map.containsKey(Affine.getKey(row, col))) {
                            nh.addNeighbor(row, col);
                        }
                    }
                }
            }
            radius++;
        }
        return nh;
    }

    private PixelData nearestNeighbor(int row, int col) {
        PixelData value = null;
        Neighborhood nh = getNeighborhood(row, col);
        List<Neighbor> neighbors = nh.getNeighbors();
        if (neighbors.size() == 4) {
            Collections.sort(neighbors);
            Neighbor neighbor = neighbors.get(0);
            value = map.get(Affine.getKey(neighbor.getRow(), neighbor.getCol()));
        }
        return value;
    }

    private PixelData bilinear(int row, int col) {
        PixelData value = null;
        Neighborhood nh = getNeighborhood(row, col);
        if (nh.isFull()) {
            int[] size = nh.getNeighborhoodSize();
            int rowSize = size[0];
            int colSize = size[1];
            for (Neighbor n : nh.getNeighbors()) {
                double rowDelta = Math.abs((double) (n.getRow() - row) / rowSize);
                double colDelta = Math.abs((double) (n.getCol() - col) / colSize);
                double weight = (1 - rowDelta) * (1 - colDelta);
                if (value == null) {
                    value = map.get(Affine.getKey(n.getRow(), n.getCol())).getIdentity();
                }
                value.combine(weight, map.get(Affine.getKey(n.getRow(), n.getCol())));
            }
        }
        return value;
    }

    public PixelData interpolate(int row, int col) {
        PixelData value = null;
        if (type.equals(Type.NEAREST_NEIGHTBOR)) {
            value = nearestNeighbor(row, col);
        } else if (type.equals(Type.BILINEAR)) {
            value = bilinear(row, col);
        }
        return value;
    }

    public static void main(String[] args) {
        HashMap<String, PixelData> hashMap = new HashMap<String, PixelData>();
        Interpoler i = new Interpoler(hashMap, Type.BILINEAR);
    }
}
