package weightingscheme;

import java.math.BigDecimal;
import java.util.Vector;

public class DownElementsScheme implements WeightingScheme {

    public DownElementsScheme() {
        super();
    }

    /**
	 * Weight a vector of doubles using DownElements
	 * Scheme.
	 * 
	 * @param	A vector of doubles.
	 * @return  The weighting result.
	 */
    public double weight(Vector<Double> sv) {
        int n = sv.size();
        double item = 0;
        double total = 0;
        double result = 0;
        for (int i = 1; i <= n; i++) {
            int index = i - 1;
            double xi = sv.elementAt(index);
            item = (xi * xi) * (2.0 * (n - i + 1.0) / (n * (n + 1.0)));
            total = total + item;
        }
        result = Math.sqrt(total);
        BigDecimal bd = new BigDecimal(result);
        bd = bd.setScale(DECIMAL_PLACE, BigDecimal.ROUND_UP);
        return bd.doubleValue();
    }
}
