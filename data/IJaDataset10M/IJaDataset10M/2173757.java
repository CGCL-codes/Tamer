package edu.gsbme.geometrykernel.input.SAT;

public class EntityObject {

    public int index;

    public String class_name;

    public String[] identifier;

    public String[] data;

    public String toString() {
        String data_string = "";
        for (int i = 0; i < data.length; i++) {
            data_string += data[i];
            if (i != data.length - 1) data_string += " ";
        }
        return "-" + index + " " + class_name + " " + data_string;
    }
}
