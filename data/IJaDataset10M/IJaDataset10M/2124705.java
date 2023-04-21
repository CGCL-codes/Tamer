package com.mentalray.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class Dataset {

    public PieDataset createGradesPieDataset(ArrayList<Double> grades) throws SQLException {
        int below5 = 0, between5and7 = 0, between7and9 = 0, over9 = 0;
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Double grade : grades) {
            if (grade < 5) {
                below5++;
            }
            if (grade >= 5 && grade < 7) {
                between5and7++;
            }
            if (grade >= 7 && grade < 9) {
                between7and9++;
            }
            if (grade >= 9 && grade <= 10) {
                over9++;
            }
        }
        dataset.setValue("Smaller then 5 (" + below5 + ")", new Double(below5));
        dataset.setValue("Between 5 and 7 (" + between5and7 + ")", new Double(between5and7));
        dataset.setValue("Between 7 and 9 (" + between7and9 + ")", new Double(between7and9));
        dataset.setValue("Over 9 (" + over9 + ")", new Double(over9));
        return dataset;
    }

    public CategoryDataset createGradesCategoryDataset(ArrayList<Double> grades) {
        String series1 = "Students";
        int lowerThen4 = 0, five = 0, six = 0, seven = 0, eight = 0, overNine = 0;
        String category4 = "< 5";
        String category5 = "5 - 6";
        String category6 = "6 - 7";
        String category7 = "7 - 8";
        String category8 = "8 - 9";
        String category9 = "> 9";
        for (Double grade : grades) {
            if (grade < 5) {
                lowerThen4++;
            }
            if (grade >= 5 && grade < 6) {
                five++;
            }
            if (grade >= 6 && grade < 7) {
                six++;
            }
            if (grade >= 7 && grade < 8) {
                seven++;
            }
            if (grade >= 8 && grade < 9) {
                eight++;
            }
            if (grade >= 9 && grade <= 10) {
                overNine++;
            }
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(lowerThen4, series1, category4);
        dataset.addValue(five, series1, category5);
        dataset.addValue(six, series1, category6);
        dataset.addValue(seven, series1, category7);
        dataset.addValue(eight, series1, category8);
        dataset.addValue(overNine, series1, category9);
        return dataset;
    }
}
