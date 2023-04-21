package com.xsky.logic.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.xsky.common.util.Sort;

public class Test {

    public List suoList(List list) {
        Random random = new Random();
        Sort sort = new Sort();
        int[] remove = new int[list.size() / 2];
        for (int i = 0; i < list.size() / 2; i++) {
            remove[i] = Math.abs(random.nextInt()) % list.size();
        }
        remove = sort.paopao(remove);
        int last = 0;
        for (int i = 0; i < remove.length; i++) {
            if (last == 0 && i != 0) {
                break;
            }
            if (last == remove[i]) {
                last = remove[i] - 1;
            } else {
                last = remove[i];
            }
            System.out.println(last);
            list.remove(last);
        }
        return list;
    }

    public static void main(String[] args) {
        Random random = new Random();
        List list = new ArrayList();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("g");
        list.add("h");
        list.add("i");
        Sort sort = new Sort();
        int[] remove = new int[list.size() / 2];
        for (int i = 0; i < list.size() / 2; i++) {
            remove[i] = Math.abs(random.nextInt()) % list.size();
        }
        remove = sort.paopao(remove);
        int last = 0;
        for (int i = 0; i < remove.length; i++) {
            if (last == 0 && i != 0) {
                break;
            }
            if (last == remove[i]) {
                last = remove[i] - 1;
            } else {
                last = remove[i];
            }
            System.out.println(last);
            list.remove(last);
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
        }
    }
}
