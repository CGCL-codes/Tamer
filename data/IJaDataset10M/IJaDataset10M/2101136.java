package org.xteam.sled.solver;

import java.util.ArrayList;
import java.util.List;

public class Balance {

    private List<BalanceItem> left = new ArrayList<BalanceItem>();

    private List<BalanceItem> right = new ArrayList<BalanceItem>();

    public Balance(BalanceItem left, BalanceItem right) {
        this.left.add(left);
        this.right.add(right);
    }

    public Balance() {
    }

    public List<BalanceItem> getRight() {
        return right;
    }

    public List<BalanceItem> getLeft() {
        return left;
    }

    public void addLeft(BalanceItem item) {
        left.add(item);
    }

    public void addAllLeft(List<BalanceItem> left) {
        this.left.addAll(left);
    }

    public void addRight(BalanceItem item) {
        right.add(item);
    }

    public String toString() {
        return left + " <=> " + right;
    }

    public boolean hasVariable(String var) {
        for (BalanceItem item : left) {
            if (item.variable().equals(var)) return true;
        }
        for (BalanceItem item : right) {
            if (item.variable().equals(var)) return true;
        }
        return false;
    }
}
