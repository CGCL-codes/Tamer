package coursetimetable;

import java.sql.ResultSet;
import javax.swing.*;

/**
 *
 * @author sony
 */
public class Weight {

    procedures P = new procedures();

    connect C = new connect();

    String Query;

    ResultSet R;

    course[] Ver_array;

    int No_of_Conflicts = 0;

    int[][] Conflict_Matrix;

    int Sort_ID = 0;

    public Weight(int[][] Matrix, course[] course_array) {
        Ver_array = course_array;
        Conflict_Matrix = Matrix;
        Calculate_Weights();
        Final_Sorting();
    }

    public void Calculate_Weights() {
        try {
            for (int i = 0; i < Ver_array.length; i++) System.out.println(Ver_array[i].id + " " + Ver_array[i].weight);
            int counter;
            for (int i = 0; i < Conflict_Matrix.length; i++) {
                counter = 0;
                for (int j = 0; j < Conflict_Matrix.length; j++) if (Conflict_Matrix[i][j] == 1 || Conflict_Matrix[i][j] == 2) counter++;
                Ver_array[i].weight = counter;
                No_of_Conflicts += counter;
            }
            No_of_Conflicts /= 2;
            Sort_ID = 0;
            quickSort(Ver_array, 0, Ver_array.length - 1);
            System.out.println("after sorting " + No_of_Conflicts);
            for (int i = 0; i < Ver_array.length; i++) System.out.println(Ver_array[i].id + " " + Ver_array[i].weight);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    public void Final_Sorting() {
        try {
            Sort_ID = 1;
            int len = Ver_array.length;
            int we, Repeated;
            for (int i = 0; i < len - 1; i++) {
                Repeated = 0;
                if (Ver_array[i].weight == Ver_array[i + 1].weight) {
                    we = Ver_array[i].weight;
                    Repeated = 2;
                    i++;
                    if (i == len - 1) quickSort(Ver_array, (i - Repeated + 1), i);
                    for (int j = i + 1; j < len; j++) {
                        if (Ver_array[j].weight == we) {
                            Repeated++;
                            i++;
                            if (i == len - 1 && j == len - 1) quickSort(Ver_array, (i - Repeated + 1), i);
                        } else {
                            quickSort(Ver_array, (i - Repeated + 1), i);
                            j = len;
                        }
                    }
                }
            }
            System.out.println("after last sorting " + No_of_Conflicts);
            for (int i = 0; i < Ver_array.length; i++) System.out.println(Ver_array[i].id + " " + Ver_array[i].weight);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    public void quickSort(course array[], int start, int end) {
        int i = start;
        int k = end;
        if (Sort_ID == 0) {
            if (end - start >= 1) {
                int pivot = array[start].weight;
                while (k > i) {
                    while (array[i].weight >= pivot && i <= end && k > i) i++;
                    while (array[k].weight < pivot && k >= start && k >= i) k--;
                    if (k > i) swap(array, i, k);
                }
                swap(array, start, k);
                quickSort(array, start, k - 1);
                quickSort(array, k + 1, end);
            } else {
                return;
            }
        } else {
            if (end - start >= 1) {
                int pivot = Students_No(array[start]);
                while (k > i) {
                    while (Students_No(array[i]) >= pivot && i <= end && k > i) i++;
                    while (Students_No(array[i]) < pivot && k >= start && k >= i) k--;
                    if (k > i) swap(array, i, k);
                }
                swap(array, start, k);
                quickSort(array, start, k - 1);
                quickSort(array, k + 1, end);
            } else {
                return;
            }
        }
    }

    public void swap(course array[], int index1, int index2) {
        course temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public int Students_No(course a) {
        try {
            Query = P.get_NoOfStudents_in_Course(a.name);
            R = C.connection(Query);
            if (R.next()) return R.getInt(1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
            return 0;
        }
        return 0;
    }
}
