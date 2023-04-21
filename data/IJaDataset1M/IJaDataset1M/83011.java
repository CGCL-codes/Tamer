package com.docum.test.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDataEnumCounter<E extends Enum<E>> extends TestDataEntityCounter<E> {

    public TestDataEnumCounter(E... enums) {
        super(Arrays.asList(enums));
    }

    public TestDataEnumCounter(E[] enums, E... excluded) {
        super(new TestDataEnumCounter.Helper<E>(enums, excluded).getList());
    }

    private static class Helper<E extends Enum<E>> {

        private List<E> enums;

        public Helper(E[] enums, E[] excluded) {
            this.enums = new ArrayList<E>(Arrays.asList(enums));
            this.enums.removeAll(Arrays.asList(excluded));
        }

        public List<E> getList() {
            return enums;
        }
    }
}
