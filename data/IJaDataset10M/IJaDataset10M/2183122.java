package org.neodatis.odb.test.arraycollectionmap;

public class ObjectWithNativeArrayOfInt {

    private String name;

    private int[] numbers;

    public ObjectWithNativeArrayOfInt(String name, int[] numbers) {
        super();
        this.name = name;
        this.numbers = numbers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    public void setNumber(int index, int value) {
        this.numbers[index] = value;
    }

    public int getNumber(int i) {
        return this.numbers[i];
    }
}
