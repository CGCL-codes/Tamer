package logicalDocParts;

import java.awt.Point;
import java.io.Serializable;

public class AssociationPageStains implements Serializable {

    private static final long serialVersionUID = -6297592619140108937L;

    private String pageName;

    private Point positionStainInPage;

    public AssociationPageStains(String pageName, Point positionCharInPage) {
        this.pageName = pageName;
        this.positionStainInPage = positionCharInPage;
    }

    public AssociationPageStains() {
    }

    public String getPageName() {
        return pageName;
    }

    public Point getPositionCharInPage() {
        return positionStainInPage;
    }

    public void setPositionCharInPage(int x, int y) {
        positionStainInPage.setLocation(x, y);
    }

    public AssociationPageStains clown() {
        return new AssociationPageStains(getPageName(), getPositionCharInPage());
    }
}
