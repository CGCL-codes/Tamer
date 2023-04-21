package ch.photoindex.servlet.elements;

import ch.photoindex.PhotoindexConstants;
import ch.photoindex.db.TPhotos;
import ch.photoindex.db.TRatings;
import ch.photoindex.localization.Dictionary;
import ch.photoindex.servlet.actions.RatingAction;
import ch.photoindex.servlet.pages.DetailsPage;
import com.orelias.infoaccess.InfoBean;

/**
 * Rating tools.
 * 
 * @author Lukas Blunschi
 * 
 */
public class RatingTools implements Element {

    private final InfoBean photo;

    private final InfoBean rating;

    public RatingTools(InfoBean photo, InfoBean rating) {
        this.photo = photo;
        this.rating = rating;
    }

    public void appendHtml(StringBuffer html, Dictionary dict) {
        String actionStr = PhotoindexConstants.P_ACTION + "=" + RatingAction.ACTIONNAME + "&amp;";
        String pageStr = PhotoindexConstants.P_PAGE + "=" + DetailsPage.PAGENAME + "&amp;";
        String idStr = TPhotos.F_ID + "=" + photo.getId() + "&amp;";
        String ratingPrefix = PhotoindexConstants.P_RATING + "=";
        String href = "?" + actionStr + pageStr + idStr + ratingPrefix;
        int ratingInt = 0;
        if (rating != null) {
            ratingInt = (Integer) rating.getProperty(TRatings.F_RATING);
        }
        html.append("<!-- rating tools -->\n");
        html.append("<div class='rating-tools-box'>\n");
        String srcStars = PhotoindexConstants.REL_PATH_RESOURCES + "jpg/rating-stars" + ratingInt + ".jpg";
        String srcBlank = PhotoindexConstants.REL_PATH_RESOURCES + "gif/blank.gif";
        html.append("<div class='rating-tools'>\n");
        html.append("<img src='" + srcStars + "' alt='" + ratingInt + "' />\n");
        for (int i = 1; i < 6; i++) {
            html.append("<div class='rating' style='left:" + ((i - 1) * 50) + "px;'>");
            html.append("<a href='" + href + i + "'><img src='" + srcBlank + "' alt='' width='50' height='46' /></a>");
            html.append("</div>\n");
        }
        html.append("</div>\n");
        html.append("</div>\n\n");
    }
}
