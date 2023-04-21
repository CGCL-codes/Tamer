package edu.univalle.lingweb.persistence;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoMomentum entity.
 * 
 * @author LingWeb
 */
@Entity
@Table(name = "co_momentum", schema = "public", uniqueConstraints = {  })
public class CoMomentum extends AbstractCoMomentum implements java.io.Serializable {

    /** default constructor */
    public CoMomentum() {
    }

    /** minimal constructor */
    public CoMomentum(Long momentumId, String momentumName, String momentumNameEn, String momentumNameFr) {
        super(momentumId, momentumName, momentumNameEn, momentumNameFr);
    }

    /** full constructor */
    public CoMomentum(Long momentumId, String momentumName, String momentumNameEn, String momentumNameFr, Set<CoStrategyMomentumDes> coStrategyMomentumDeses, Set<CoExercises2> coExercises2s, Set<CoExercises1> coExercises1s) {
        super(momentumId, momentumName, momentumNameEn, momentumNameFr, coStrategyMomentumDeses, coExercises2s, coExercises1s);
    }
}
