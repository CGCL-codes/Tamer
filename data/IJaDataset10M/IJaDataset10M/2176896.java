package org.corrib.s3b.recommendations.types;

import java.util.HashSet;
import java.util.TreeSet;
import org.corrib.s3b.recommendations.Recommendation;
import org.corrib.s3b.recommendations.RecommendationComparator;
import org.corrib.s3b.recommendations.RecommendationItemType;
import org.corrib.s3b.recommendations.RecommendationUtils;
import org.corrib.s3b.recommendations.rdf.RecommendationQueries;
import org.jeromedl.db.rdf.Repository;
import org.jeromedl.db.rdf.SesameWrapper;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.sesame.constants.QueryLanguage;

/**
 * @deprecated
 * @author Mateusz Kaczmarek
 */
public class AnnotationRecommendation extends RecommendationType {

    public AnnotationRecommendation() {
        super();
    }

    @Override
    public void findRecommendations(URI book) {
        Value[] aValues;
        aValues = SesameWrapper.performVectorQuery(Repository.JEROMEDL_REPOSITORY.getLocalRepository(), QueryLanguage.SERQL, RecommendationQueries.GENERAL_ITEM_QUERY.toString(book, RecommendationItemType.ANNOTATION));
        if (aValues != null) for (Value val : aValues) System.out.println(val);
        if (aValues == null) {
            this.setResultsArray(null);
            return;
        }
        HashSet<Value> allBooks = new HashSet<Value>();
        for (Value keyword : aValues) {
            Value[] aBooks = SesameWrapper.performVectorQuery(Repository.JEROMEDL_REPOSITORY.getLocalRepository(), QueryLanguage.SERQL, RecommendationQueries.GENERAL_BOOK_QUERY.toString(RecommendationItemType.ANNOTATION, keyword));
            for (Value aBook : aBooks) allBooks.add(aBook);
        }
        allBooks.remove(book);
        for (Value val : allBooks) System.out.println(val);
        TreeSet<Recommendation> resultBooks = new TreeSet<Recommendation>(new RecommendationComparator<Recommendation>());
        if (aValues.length == 1) {
            for (Value aBook : allBooks) {
                int n = SesameWrapper.performVectorQuery(Repository.JEROMEDL_REPOSITORY.getLocalRepository(), QueryLanguage.SERQL, RecommendationQueries.GENERAL_ITEM_QUERY.toString(aBook, RecommendationItemType.ANNOTATION)).length;
                float accuracy = (float) 1 / (float) n;
                resultBooks.add(new Recommendation((URI) aBook, accuracy));
            }
        } else {
            int s = aValues.length;
            for (Value aBook : allBooks) {
                Value[] annotations = SesameWrapper.performVectorQuery(Repository.JEROMEDL_REPOSITORY.getLocalRepository(), QueryLanguage.SERQL, RecommendationQueries.GENERAL_ITEM_QUERY.toString(aBook, RecommendationItemType.ANNOTATION));
                int n = annotations.length;
                int m = RecommendationUtils.commonPart(aValues, annotations);
                float accuracy = 1 - (1 - (float) m / (float) s) - ((float) m / (float) s * (float) Math.pow(((float) (n - m) / (float) n), 2.0));
                resultBooks.add(new Recommendation((URI) aBook, accuracy));
            }
        }
        System.out.println();
        for (Recommendation r : resultBooks) System.out.println(r);
        this.setResultsArray(resultBooks);
    }
}
