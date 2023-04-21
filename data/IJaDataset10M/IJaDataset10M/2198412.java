package it.allerj.model;

import java.util.ArrayList;

/**
 * @author Alessandro Veracchi
 * @version 1.0
 * @created 12-apr-2008 12.10.03
 */
public class AllergicTestDoubleFrame extends AllergicTest {

    private static final long serialVersionUID = 1L;

    @Override
    public void initEmptyAllergicTest() {
        if (this.getFrameList() == null) this.setFrameList(new ArrayList<Frame>());
        this.getFrameList().add(createEmptyTest("0000000000000"));
        this.getFrameList().add(createEmptyTest("0000000000001"));
    }
}
