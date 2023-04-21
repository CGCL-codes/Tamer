package benchmark.cute.original.tests.sequential;

import benchmark.cute.original.cute.Cute;
import benchmark.cute.original.sequential.Struct;

public class TestStruct {

    public static void main(String[] args) {
        Struct s = (Struct) Cute.input.Object("tests.Struct");
        int y = Cute.input.Integer();
        Struct.testme(s, y);
    }
}
