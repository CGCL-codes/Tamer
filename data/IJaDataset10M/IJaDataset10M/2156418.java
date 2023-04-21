package estructuras;

import java.io.Serializable;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class SimpleCorrelationMatrixModel extends DefaultTableModel implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 253278661567641649L;

    public SimpleCorrelationMatrixModel() {
        super();
    }

    public SimpleCorrelationMatrixModel(Vector<Vector<Object>> data, Vector<String> header) {
        super(data, header);
    }

    public Class<?> getClass(int col) {
        return Double.class;
    }

    private static final double EPSILON = 1e-10;

    public void removeColumn(int c) {
        Vector<Vector> data2 = this.getDataVector();
        for (int i = 0; i < data2.size(); i++) data2.elementAt(i).remove(c);
        Vector<String> names = new Vector<String>();
        for (int i = 0; i < this.getColumnCount(); i++) names.add(this.getColumnName(i));
        names.remove(c);
        this.setDataVector(data2, names);
        this.fireTableDataChanged();
    }

    public static boolean isSymmetric(double[][] A) {
        int N = A.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                if (A[i][j] != A[j][i]) return false;
            }
        }
        return true;
    }

    public static int cholesky(double[][] A) {
        if (!isSymmetric(A)) {
            return 2;
        }
        int N = A.length;
        double[][] L = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= i; j++) {
                double sum = 0.0;
                for (int k = 0; k < j; k++) {
                    sum += L[i][k] * L[j][k];
                }
                if (i == j) L[i][i] = Math.sqrt(A[i][i] - sum); else L[i][j] = 1.0 / L[j][j] * (A[i][j] - sum);
            }
            if (L[i][i] <= 0) {
                return 0;
            }
        }
        return 1;
    }

    public boolean verificarMatriz() {
        int n = this.getDataVector().size();
        double[][] A = new double[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
            Object obj = ((Vector<Object>) this.getDataVector().elementAt(Math.max(i, j))).elementAt(Math.min(j, i));
            if (obj instanceof String) A[i][j] = new Double((String) obj); else A[i][j] = (Double) obj;
        }
        int res = this.cholesky(A);
        if (res == 1) return true; else return false;
    }
}
