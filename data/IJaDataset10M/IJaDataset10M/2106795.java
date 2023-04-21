package com.cartagena.financo.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.cartagena.financo.model.Category;
import com.cartagena.financo.model.Transaction;
import com.cartagena.financo.model.TransactionType;

public class TransactionUtil {

    public static final BigDecimal sum(List<Transaction> transactions) {
        return sum(transactions, null);
    }

    public static final BigDecimal sum(List<Transaction> transactions, TransactionType type) {
        BigDecimal sum = new BigDecimal(0);
        for (Transaction transaction : transactions) {
            if (type == null || type.equals(transaction.getCategory().getType())) {
                boolean isNegative = transaction.getType() == TransactionType.DEBIT;
                sum = sum.add(isNegative ? transaction.getValue().negate() : transaction.getValue());
            }
        }
        return sum.abs();
    }

    public static List<CategorySum> sumGrouped(List<Transaction> transactions, TransactionType type, List<Category> categories) {
        return sumGrouped(transactions, type, categories.toArray(new Category[categories.size()]));
    }

    public static List<CategorySum> sumGrouped(List<Transaction> transactions) {
        return sumGrouped(transactions, null, (Category[]) null);
    }

    public static final List<CategorySum> sumGrouped(List<Transaction> transactions, TransactionType type, Category... categories) {
        if (categories == null || categories.length == 0) {
            Set<Category> catSet = new HashSet<Category>();
            for (Transaction trans : transactions) {
                catSet.add(trans.getCategory());
            }
            categories = catSet.toArray(new Category[catSet.size()]);
        }
        List<CategorySum> result = new ArrayList<CategorySum>();
        for (Category category : categories) {
            CategorySum sum = new CategorySum(category);
            result.add(sum);
            for (Transaction transaction : transactions) {
                if ((type == null || category.getType() == type) && category.equals(transaction.getCategory())) {
                    boolean isNegative = transaction.getType() == TransactionType.DEBIT;
                    sum.addValue(isNegative ? transaction.getValue().negate() : transaction.getValue());
                }
            }
            sum.setType();
            sum.abs();
        }
        return result;
    }
}
