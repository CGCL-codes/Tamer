package net.taylor.agile.entity;

import net.taylor.fitnesse.FitnesseCreate;
import net.taylor.fitnesse.jpa.ValidationDriver;
import net.taylor.trace.Trace;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * Validates data for Card.
 * 
 * @author jgilbert
 * @generated
 */
@Name("cardValidationDriver")
@Scope(ScopeType.EVENT)
@Trace
public class CardValidationDriver extends ValidationDriver<Card> {

    /** @generated */
    @FitnesseCreate
    public void validate(String title, String status, Long estimate, String text, String number, Long rank, String partition) {
        entity = new Card();
        entity.setTitle(title);
        entity.setStatus(status);
        entity.setEstimate(estimate);
        entity.setText(text);
        entity.setNumber(number);
        entity.setRank(rank);
        entity.setPartition(partition);
    }
}
