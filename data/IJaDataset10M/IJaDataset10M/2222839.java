package equilibrium.commons.report.generator.chart.property.transformer;

import org.jfree.chart.axis.CategoryLabelPositions;
import equilibrium.commons.report.config.model.CategoryLabelPositionsType;

public class DomainAxisLabelTransformer {

    protected CategoryLabelPositions transform(CategoryLabelPositionsType categoryLabelPositionsType) {
        if (categoryLabelPositionsType.equals(CategoryLabelPositionsType.STANDARD)) {
            return CategoryLabelPositions.STANDARD;
        }
        if (categoryLabelPositionsType.equals(CategoryLabelPositionsType.UP_90)) {
            return CategoryLabelPositions.UP_90;
        }
        if (categoryLabelPositionsType.equals(CategoryLabelPositionsType.DOWN_45)) {
            return CategoryLabelPositions.DOWN_45;
        }
        if (categoryLabelPositionsType.equals(CategoryLabelPositionsType.DOWN_90)) {
            return CategoryLabelPositions.DOWN_90;
        }
        if (categoryLabelPositionsType.equals(CategoryLabelPositionsType.STANDARD)) {
            return CategoryLabelPositions.STANDARD;
        }
        if (categoryLabelPositionsType.equals(CategoryLabelPositionsType.UP_45)) {
            return CategoryLabelPositions.UP_45;
        }
        throw new IllegalArgumentException("No category label position for " + categoryLabelPositionsType);
    }
}
