package gcr.mmm2.model;

/**
 * @author Andrew Iskandar
 * 
 */
public class AnnotationImpl {

    private int m_photoID;

    private IPerson m_person;

    private int m_xcoord;

    private int m_ycoord;

    private int m_width;

    private int m_height;

    protected AnnotationImpl(int photoID, IPerson person, int xcoord, int ycoord, int width, int height) {
        this.m_photoID = photoID;
        this.m_person = person;
        this.m_xcoord = xcoord;
        this.m_ycoord = ycoord;
        this.m_width = width;
        this.m_height = height;
    }

    public int getPhotoID() {
        return this.m_photoID;
    }

    public IPerson getPerson() {
        return this.m_person;
    }

    public int getXCoord() {
        return this.m_xcoord;
    }

    public int getYCoord() {
        return this.m_ycoord;
    }

    public int getWidth() {
        return this.m_width;
    }

    public int getHeight() {
        return this.m_height;
    }

    public String toString() {
        return this.m_person.toString() + "," + this.m_xcoord + "," + this.m_ycoord + "," + this.m_width + "," + m_height;
    }
}
