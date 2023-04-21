package com.shoesobjects.bourre.game;

import com.shoesobjects.bourre.BaseBourreGameTest;

public class DrawPositionTest extends BaseBourreGameTest {

    public void test2PlayersCanDraw5() {
        DrawPosition p = new DrawPosition(2, 2);
        assertEquals(5, p.calcMaxDraw());
    }

    public void test5PlayersCanOnlyDraw5() {
        DrawPosition p = new DrawPosition(5, 5);
        assertEquals(5, p.calcMaxDraw());
    }

    public void test6PlayersCanOnlyDraw3() {
        DrawPosition p = new DrawPosition(6, 6);
        assertEquals(3, p.calcMaxDraw());
    }

    public void test7PlayersCanOnlyDraw2() {
        DrawPosition p = new DrawPosition(7, 7);
        assertEquals(2, p.calcMaxDraw());
    }
}
