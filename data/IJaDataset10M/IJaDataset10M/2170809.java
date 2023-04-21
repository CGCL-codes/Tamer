package cz.razor.dzemuj.datamodels;

/**
 * Expects data in text file, row = one record, attributes separated by semicolon; first
 * line contains metadata
 * 
 * @author zdenek.kedaj@gmail.com
 * @version 20.5. 2008
 */
public class ScoreModelItem {

    private int rowNumber;

    private Double pointsPerWrong;

    private Double pointsPerNone;

    private Double pointsPerRight;

    private Double average;

    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public void setPointsPerWrong(Double pointsPerWrong) {
        this.pointsPerWrong = pointsPerWrong;
    }

    public void setPointsPerNone(Double pointsPerNone) {
        this.pointsPerNone = pointsPerNone;
    }

    public void setPointsPerRight(Double pointsPerRight) {
        this.pointsPerRight = pointsPerRight;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public ScoreModelItem(int row, Double pointsPerWrong, Double pointsPerNone, Double pointsPerRight, Double average) {
        super();
        this.rowNumber = row;
        this.pointsPerWrong = pointsPerWrong;
        this.pointsPerNone = pointsPerNone;
        this.pointsPerRight = pointsPerRight;
        this.average = average;
    }

    public ScoreModelItem(int row, Double pointsPerWrong, Double pointsPerNone, Double pointsPerRight, Double average, String hash) {
        this(row, pointsPerWrong, pointsPerNone, pointsPerRight, average);
        this.setHash(hash);
    }

    public Double getPointsPerWrong() {
        return pointsPerWrong;
    }

    public Double getPointsPerNone() {
        return pointsPerNone;
    }

    public Double getPointsPerRight() {
        return pointsPerRight;
    }

    public Double getAverage() {
        return average;
    }

    public int getRowNumber() {
        return rowNumber;
    }
}
