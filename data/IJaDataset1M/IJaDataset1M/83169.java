package net.sourceforge.picdev.components;

import net.sourceforge.picdev.Workbench;
import net.sourceforge.picdev.wb.DevelopmentStudio;
import javax.swing.*;
import java.util.Arrays;

/**
 * @author Klaus Friedel
 *         Date: 01.01.2008
 *         Time: 19:37:53
 */
public class Stack {

    private final int[] elements;

    private int ptr = 0;

    public Stack() {
        this(8);
    }

    public Stack(int count) {
        elements = new int[count];
    }

    public Stack(Stack copyMe) {
        elements = new int[copyMe.elements.length];
        System.arraycopy(copyMe.elements, 0, elements, 0, copyMe.elements.length);
        ptr = copyMe.ptr;
    }

    public void push(int x) {
        if (ptr >= elements.length) {
            JOptionPane.showMessageDialog(DevelopmentStudio.singleInstance.mainframe, "Warning! Stack overflow!", "Stack overflow", JOptionPane.ERROR_MESSAGE);
            DevelopmentStudio.singleInstance.project.stop();
            return;
        }
        elements[ptr++] = x;
    }

    public int pop() {
        if (size() == 0) {
            JOptionPane.showMessageDialog(DevelopmentStudio.singleInstance.mainframe, "Warning! Stack underflow!", "Stack underflow", JOptionPane.ERROR_MESSAGE);
            DevelopmentStudio.singleInstance.project.stop();
            return 0;
        }
        return elements[--ptr];
    }

    public int peek() {
        return elements[ptr - 1];
    }

    public int get(int idx) {
        return elements[ptr - idx - 1];
    }

    public int size() {
        return ptr;
    }

    public void clear() {
        Arrays.fill(elements, 0);
        ptr = 0;
    }
}
