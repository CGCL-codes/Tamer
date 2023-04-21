package newtonERP.viewers.viewerData;

import java.util.Iterator;
import java.util.Vector;
import newtonERP.common.ActionLink;

/**
 * Représente les données pouvant être visible par le grid viewer
 * 
 * @author Guillaume
 */
public class GridViewerData extends BaseViewerData {

    private GridCaseData[] header = new GridCaseData[0];

    private GridCaseData[] leftHeader = new GridCaseData[0];

    private GridCaseData[][] cases = new GridCaseData[0][0];

    private Vector<ActionLink> specificActionButtonList = new Vector<ActionLink>();

    private boolean isColored = false;

    private boolean spanSimilar = false;

    /**
	 * s
	 */
    public GridViewerData() {
        super();
    }

    /**
	 * @return the header
	 */
    public GridCaseData[] getHeader() {
        return header;
    }

    /**
	 * @param header the header to set
	 */
    public void setHeader(GridCaseData[] header) {
        this.header = header;
    }

    /**
	 * @return the leftHeader
	 */
    public GridCaseData[] getLeftHeader() {
        return leftHeader;
    }

    /**
	 * @param leftHeader the leftHeader to set
	 */
    public void setLeftHeader(GridCaseData[] leftHeader) {
        this.leftHeader = leftHeader;
    }

    /**
	 * @return the cases
	 */
    public GridCaseData[][] getCases() {
        return cases;
    }

    /**
	 * @param cases the cases to set
	 */
    public void setCases(GridCaseData[][] cases) {
        this.cases = cases;
    }

    /**
	 * permet d'obtenir les action specifique
	 * 
	 * @return liste de specificAction
	 */
    public Vector<ActionLink> getSpecificActionButtonList() {
        return specificActionButtonList;
    }

    /**
	 * @param specificActionButtonList the specificActionButtonList to set
	 */
    public void setSpecificActionButtonList(Vector<ActionLink> specificActionButtonList) {
        this.specificActionButtonList = specificActionButtonList;
    }

    /**
	 * @param actionLink action du bouton
	 */
    public void addSpecificActionButtonList(ActionLink actionLink) {
        specificActionButtonList.add(actionLink);
    }

    /**
	 * @param specificActionName the globalActionName to remove
	 */
    public void removeSpecificActions(String specificActionName) {
        for (Iterator<ActionLink> sActionIT = specificActionButtonList.iterator(); sActionIT.hasNext(); ) {
            ActionLink gAction = sActionIT.next();
            if (gAction.getName().equals(specificActionName)) {
                sActionIT.remove();
            }
        }
    }

    /**
	 * @param SpecificActionName the globalActionName to remove
	 */
    public void removeSpecificActions(ActionLink SpecificActionName) {
        for (Iterator<ActionLink> sActionIT = specificActionButtonList.iterator(); sActionIT.hasNext(); ) {
            ActionLink gAction = sActionIT.next();
            if (gAction.getName().equals(SpecificActionName)) {
                sActionIT.remove();
            }
        }
    }

    /**
	 * @return the color
	 */
    public boolean isColored() {
        return isColored;
    }

    /**
	 * @param isColored the color to set
	 */
    public void setColored(boolean isColored) {
        this.isColored = isColored;
    }

    /**
	 * @return the spanSimilar
	 */
    public boolean isSpanSimilar() {
        return spanSimilar;
    }

    /**
	 * @param spanSimilar the spanSimilar to set
	 */
    public void setSpanSimilar(boolean spanSimilar) {
        this.spanSimilar = spanSimilar;
    }
}
