package edu.univalle.lingweb.persistence;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CoMatrixExercises1 entity.
 * 
 * @author LingWeb
 */
@Entity
@Table(name = "co_matrix_exercises1", schema = "public", uniqueConstraints = {  })
public class CoMatrixExercises1 extends AbstractCoMatrixExercises1 implements java.io.Serializable {

    /** default constructor */
    public CoMatrixExercises1() {
    }

    /** minimal constructor */
    public CoMatrixExercises1(Long matrixId, CoExercises1 coExercises1, Long numberRows, Long numberColumns, Long matrixWidth, Long matrixHeight, Long rowHeight, Long columnWidht) {
        super(matrixId, coExercises1, numberRows, numberColumns, matrixWidth, matrixHeight, rowHeight, columnWidht);
    }

    /** full constructor */
    public CoMatrixExercises1(Long matrixId, CoExercises1 coExercises1, String enunciate, Long numberRows, Long numberColumns, Long matrixWidth, Long matrixHeight, Long rowHeight, Long columnWidht, String matrixXml, Set<CoMatrixExr1User> coMatrixExr1Users) {
        super(matrixId, coExercises1, enunciate, numberRows, numberColumns, matrixWidth, matrixHeight, rowHeight, columnWidht, matrixXml, coMatrixExr1Users);
    }
}
