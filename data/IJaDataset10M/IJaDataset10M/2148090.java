package org.gromurph.javascore.exception;

import java.util.ResourceBundle;
import org.gromurph.javascore.JavaScoreProperties;
import org.gromurph.javascore.model.Division;
import org.gromurph.javascore.model.ratings.Rating;

/**
 * Exception thrown when trying to add a rating to RatingList when RatingList
 * already contains a rating of the same system
**/
public class RatingOutOfBoundsException extends java.lang.Exception {

    protected static ResourceBundle res = JavaScoreProperties.getResources();

    Division fDivision;

    Rating fBadRating;

    public RatingOutOfBoundsException() {
        this(null, null);
    }

    public RatingOutOfBoundsException(Division div, Rating rtg) {
        fDivision = div;
        fBadRating = rtg;
    }

    public String toString() {
        if (fDivision != null && fBadRating != null) {
            StringBuffer sb = new StringBuffer(50);
            sb.append("Rating, ");
            sb.append(fBadRating);
            sb.append(", is out of bounds for division, ");
            sb.append(fDivision);
            return sb.toString();
        } else {
            return "Rating is out of bounds for Division";
        }
    }
}
